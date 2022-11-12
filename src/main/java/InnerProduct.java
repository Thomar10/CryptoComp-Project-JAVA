import java.math.BigInteger;

public class InnerProduct {

  public static void main(String[] args) {
    SetupObj setup = setup(500, 3);

    EncryptObj encrypt = encrypt(setup.mpk(),
        new BigInteger[]{BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO}, setup.group());
    BigInteger[] y = {BigInteger.ZERO, BigInteger.ZERO, BigInteger.ONE};
    BigInteger sky = keyDer(setup.msk(), y);
    BigInteger result = decrypt(encrypt.ct0(), encrypt.ci(), sky, y, setup.group());
    System.out.println(result);
  }

  static BigInteger decrypt(BigInteger ct0, BigInteger[] ct, BigInteger sky, BigInteger[] y,
      Group group) {
    BigInteger ct0Inverse = ct0.modPow(sky, group.prime()).modInverse(group.prime());
    BigInteger product = BigInteger.ONE;
    for (int i = 0; i < ct.length; i++) {
      product = product.multiply(ct[i].modPow(y[i], group.prime()));
    }
    return product.multiply(ct0Inverse).mod(group.prime());
  }

  static BigInteger keyDer(BigInteger[] msk, BigInteger[] y) {
    BigInteger sum = BigInteger.ZERO;
    for (int i = 0; i < msk.length; i++) {
      sum = sum.add(msk[i].multiply(y[i]));
    }
    return sum;
  }

  static EncryptObj encrypt(BigInteger[] mpk, BigInteger[] x, Group group) {
    BigInteger random = group.randomElementInQ(BigInteger.ONE);
    BigInteger ct0 = group.generator().modPow(random, group.prime());
    BigInteger[] ci = new BigInteger[3];
    for (int i = 0; i < 3; i++) {
      ci[i] = mpk[i].modPow(random, group.prime())
          .multiply(group.generator().modPow(x[i], group.prime()));
    }
    return new EncryptObj(ct0, ci);
  }

  static SetupObj setup(int security, int cipherSize) {
    BigInteger[] msk = new BigInteger[cipherSize];
    Group group = Group.generateGroup(security);
    for (int i = 0; i < cipherSize; i++) {
      msk[i] = group.randomElementInQ(BigInteger.ONE);
    }

    BigInteger[] mpk = new BigInteger[cipherSize];
    for (int i = 0; i < cipherSize; i++) {
      mpk[i] = group.generator().modPow(msk[i], group.prime());
    }
    return new SetupObj(msk, mpk, group);
  }
}


record EncryptObj(BigInteger ct0, BigInteger[] ci) {

};

record SetupObj(BigInteger[] msk, BigInteger[] mpk, Group group) {

};
