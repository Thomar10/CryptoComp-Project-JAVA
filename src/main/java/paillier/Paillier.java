package paillier;

import static group.PaillierGroup.randomElement;

import group.PaillierGroup;
import java.math.BigInteger;

public final class Paillier {

  public static Setup setup(int security, PaillierGroup group) {
    int cipherLength = 3;
    BigInteger[] s = new BigInteger[cipherLength];
    BigInteger[] h = new BigInteger[cipherLength];
    for (int i = 0; i < cipherLength; i++) {
      s[i] = group.gaussian(security);
      h[i] = group.generator().modPow(s[i], group.nSquared());
    }
    return new Setup(h, s, group);
  }
  public static BigInteger[] encrypt(BigInteger[] mpk, BigInteger[] y, PaillierGroup group) {
    BigInteger norm = BigInteger.ZERO;
    for (BigInteger value: y) {
      if (value.compareTo(norm) > 0) {
        norm = value;
      }
    }
    if (norm.compareTo(group.yBound()) > 0) {
      throw new IllegalArgumentException("The norm of y is too big!");
    }
    BigInteger random = randomElement(BigInteger.ZERO,
        group.n().divide(BigInteger.valueOf(4)).subtract(BigInteger.ONE).bitLength());
    BigInteger c0 = group.generator().modPow(random, group.nSquared());
    BigInteger[] ci = new BigInteger[y.length + 1];
    ci[0] = c0;

    for (int i = 1; i < ci.length; i++) {
      ci[i] = BigInteger.ONE.add(y[i - 1].multiply(group.n()))
          .multiply(mpk[i - 1].modPow(random, group.nSquared())).mod(group.nSquared());

    }
    return ci;
  }

  public static boolean decrypt(BigInteger sk, BigInteger[] c, BigInteger[] x,
      PaillierGroup group) {
    BigInteger cx = BigInteger.ONE;
    for (int i = 1; i < c.length; i++) {
      cx = cx.multiply(c[i].modPow(x[i - 1], group.nSquared()));
    }
    BigInteger intermediate = cx.multiply(c[0].modPow(sk.negate(), group.nSquared()))
        .mod(group.nSquared());
    return intermediate.subtract(BigInteger.ONE).mod(group.nSquared()).divide(group.n())
        .compareTo(BigInteger.ZERO) == 0;
  }

  public static BigInteger keyGen(BigInteger[] msk, BigInteger[] x, PaillierGroup group) {
    BigInteger norm = BigInteger.ZERO;
    for (BigInteger value: x) {
      if (value.compareTo(norm) > 0) {
        norm = value;
      }
    }
    if (norm.compareTo(group.xBound()) > 0) {
      throw new IllegalArgumentException("The norm of x is too big!");
    }
    BigInteger sk = BigInteger.ZERO;
    for (int i = 0; i < x.length; i++) {
      sk = sk.add(msk[i].multiply(x[i]));
    }
    return sk;
  }

  public record Setup(BigInteger[] mpk, BigInteger[] msk, PaillierGroup group) {

  }
}
