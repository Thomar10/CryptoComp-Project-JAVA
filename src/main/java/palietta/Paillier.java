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
    MasterPublicKey mpk = new MasterPublicKey(group.n(), group.generator(), h, y);
    MasterSecretKey msk = new MasterSecretKey(s, x);
    return new Setup(mpk, msk);
  }

  static BigInteger[] encrypt(MasterPublicKey mpk, BigInteger[] y, Group group) {
    BigInteger random = Group.randomElement(BigInteger.ZERO,
        mpk.n.divide(BigInteger.valueOf(4)).subtract(BigInteger.ONE).bitLength());
    BigInteger c0 = group.generator().modPow(random, group.nSquared());
    BigInteger[] ci = new BigInteger[y.length + 1];
    ci[0] = c0;
    for (int i = 1; i < ci.length; i++) {
      ci[i] = BigInteger.ONE.add(y[i].multiply(group.n())).multiply(mpk.h[i].modPow(random, group.nSquared())).mod(group.nSquared());
    }
    return ci;
  }

  static void decrypt(MasterPublicKey mpk, BigInteger sk, BigInteger[] c) {

  }

  static BigInteger keyGen(MasterSecretKey msk, BigInteger[] x) {
    BigInteger sk = BigInteger.ZERO;
    for (int i = 0; i < x.length; i++) {
      sk = sk.add(msk.s[i].multiply(x[i]));
    }
    return sk;
  }

  public record MasterPublicKey(BigInteger n, BigInteger generator, BigInteger[] h,
                                BigInteger[] y) {

  }

  public record MasterSecretKey(BigInteger[] s, BigInteger[] x) {

  }

  public record Setup(MasterPublicKey mpk, MasterSecretKey msk) {

  }
}
