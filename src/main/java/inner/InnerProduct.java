package inner;

import java.math.BigInteger;

/**
 * Notes:
 * Attack in exponent for encrypter, e.g. can the encrypter encrypt something so a decrypter for
 * blood type AB+ will decrypt to false (they will always have true).
 *
 * Masking bruges ikke kun for at gemme leakes, men også for at håndtere korrupt attacker. Han ved
 * ikke hvad han skal gange på sit input for at ændre resultet.
 *
 * Der skal maskes på begge sider af grundende ovenfor.
 */
public class InnerProduct {

  static boolean decrypt(BigInteger ct0, BigInteger[] ct, BigInteger sky, BigInteger[] y,
      Group group) {
    BigInteger ct0Inverse = ct0.modPow(sky, group.prime()).modInverse(group.prime());
    BigInteger product = BigInteger.ONE;
    for (int i = 0; i < ct.length; i++) {
      product = product.multiply(ct[i].modPow(y[i], group.prime()));
    }
    BigInteger h = product.multiply(ct0Inverse).mod(group.prime());
    return h.compareTo(BigInteger.ONE) == 0;
  }

  static BigInteger keyDer(BigInteger[] msk, BigInteger[] y) {
    BigInteger sum = BigInteger.ZERO;
    for (int i = 0; i < msk.length; i++) {
      sum = sum.add(msk[i].multiply(y[i]));
    }
    return sum;
  }

  static EncryptObj encrypt(BigInteger[] mpk, BigInteger[] x, Group group) {
    BigInteger random = group.randomElementInQ();
    BigInteger ct0 = group.generator().modPow(random, group.prime());
    BigInteger[] ci = new BigInteger[x.length];
    for (int i = 0; i < x.length; i++) {
      ci[i] = mpk[i].modPow(random, group.prime())
          .multiply(group.generator().modPow(x[i], group.prime()));
    }
    return new EncryptObj(ct0, ci);
  }

  static SetupObj setup(Group group) {
    int cipherSize = 3;
    BigInteger[] msk = new BigInteger[cipherSize];
    for (int i = 0; i < cipherSize; i++) {
      msk[i] = group.randomElementInQ();
    }

    BigInteger[] mpk = new BigInteger[cipherSize];
    for (int i = 0; i < cipherSize; i++) {
      mpk[i] = group.generator().modPow(msk[i], group.prime());
    }
    return new SetupObj(msk, mpk);
  }
}

record SetupObj(BigInteger[] msk, BigInteger[] mpk) {

}
