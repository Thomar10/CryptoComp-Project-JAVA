package group;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public record PaillierGroup(BigInteger n, BigInteger nSquared, BigInteger generator, BigInteger xBound, BigInteger yBound)  {

  static Random random = new Random();

  public BigInteger gaussian(int security) {
    BigInteger desiredStandardDeviation = BigInteger.valueOf(security).sqrt()
        .multiply(n.pow(5).sqrt()).add(BigInteger.TWO);
    BigDecimal deviation = new BigDecimal(desiredStandardDeviation);
    return BigDecimal.valueOf(random.nextGaussian())
        .multiply(deviation).toBigInteger();
  }

  public static PaillierGroup generateGroup(int security) {
    SafePrimeGroup group = SafePrimeGroup.generateGroup(security);
    SafePrimeGroup groupDot = SafePrimeGroup.generateGroup(security);
    BigInteger xBound = BigInteger.ONE.shiftLeft(group.p().bitLength() - 1);
    BigInteger yBound = BigInteger.ONE.shiftLeft(groupDot.p().bitLength() - 1);
    BigInteger n = group.p().multiply(groupDot.p());
    BigInteger nSquared = n.pow(2);
    while (true) {
      BigInteger gPrim = randomElement(BigInteger.TWO,
          nSquared.subtract(BigInteger.ONE).bitLength());
      BigInteger generator = gPrim.modPow(BigInteger.TWO.multiply(n), nSquared);
      return new PaillierGroup(n, nSquared, generator, xBound, yBound);
    }
  }

  public static BigInteger randomElement(BigInteger low, int bits) {
    BigInteger result = new BigInteger(bits, random);
    while (result.compareTo(low) <= 0) {
      result = new BigInteger(bits, random);
    }
    return result;
  }


  public static BigInteger[] maskInput(BigInteger[] input, BigInteger bound) {
    for (int i = 0; i < input.length; i++) {
      BigInteger random = randomElement(BigInteger.ONE, bound.subtract(BigInteger.ONE).bitLength());
      input[i] = input[i].multiply(random);
    }
    return input;
  }
}
