package palietta;

import group.Group;
import java.math.BigInteger;

public final class Paillier {

  static Setup setup(int security, int cipherLength, BigInteger[] x, BigInteger[] y) {
    Group group = Group.generatePaillierGroup(security);

    BigInteger[] s = new BigInteger[cipherLength];
    BigInteger[] h = new BigInteger[cipherLength];
    for (int i = 0; i < cipherLength; i++) {
      s[i] = Group.gaussian(security, group.n());
      h[i] = group.generator().modPow(s[i], group.nSquared());
    }
    MasterPublicKey mpk = new MasterPublicKey(group.n(), group.generator(), h);
    MasterSecretKey msk = new MasterSecretKey(s);
    return new Setup(mpk, msk, group);
  }

  static BigInteger[] encrypt(MasterPublicKey mpk, BigInteger[] y, Group group) {
    BigInteger random = Group.randomElement(BigInteger.ZERO,
        mpk.n.divide(BigInteger.valueOf(4)).subtract(BigInteger.ONE).bitLength());
    BigInteger c0 = group.generator().modPow(random, group.nSquared());
    BigInteger[] ci = new BigInteger[y.length + 1];
    ci[0] = c0;

    for (int i = 1; i < ci.length; i++) {
      ci[i] = BigInteger.ONE.add(y[i - 1].multiply(group.n()))
          .multiply(mpk.h[i - 1].modPow(random, group.nSquared())).mod(group.nSquared());

    }
    return ci;
  }

  static BigInteger decrypt(BigInteger sk, BigInteger[] c, BigInteger[] x, Group group) {
    BigInteger cx = BigInteger.ONE;
    BigInteger skInverse = sk.modInverse(group.nSquared());
    for (int i = 1; i < c.length; i++) {
      cx = cx.multiply(c[i].modPow(x[i - 1], group.nSquared()));
    }
    BigInteger intermediate = cx.multiply(c[0].modPow(skInverse, group.nSquared()))
        .mod(group.nSquared());
    return intermediate.subtract(BigInteger.ONE).mod(group.nSquared()).divide(group.n());
  }

  static BigInteger keyGen(MasterSecretKey msk, BigInteger[] x) {
    BigInteger sk = BigInteger.ZERO;
    for (int i = 0; i < x.length; i++) {
      sk = sk.add(msk.s[i].multiply(x[i]));
    }
    return sk;
  }

  public static void main(String[] args) {
    BigInteger[] x = new BigInteger[]{BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO};
    BigInteger[] y = new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO, BigInteger.ONE};
    Setup setup = setup(100, 3, x, y);
    BigInteger skx = keyGen(setup.msk, x);
    BigInteger[] cipher = encrypt(setup.mpk, y, setup.group);
    BigInteger result = decrypt(skx, cipher, x, setup.group);
    System.out.println(result);
  }

  public record MasterPublicKey(BigInteger n, BigInteger generator, BigInteger[] h) {

  }

  public record MasterSecretKey(BigInteger[] s) {

  }

  public record Setup(MasterPublicKey mpk, MasterSecretKey msk, Group group) {

  }
}
