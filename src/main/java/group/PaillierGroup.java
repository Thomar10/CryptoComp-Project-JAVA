package group;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public record PaillierGroup(BigInteger n, BigInteger nSquared, BigInteger generator)  {

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
    BigInteger n = group.prime().multiply(group.q());
    BigInteger nSquared = n.pow(2);
    while (true) {
      BigInteger gPrim = randomElement(BigInteger.TWO,
          nSquared.subtract(BigInteger.ONE).bitLength());
      BigInteger generator = gPrim.modPow(BigInteger.TWO.multiply(n), nSquared);
      return new PaillierGroup(n, nSquared, generator);
    }
  }

  public static BigInteger randomElement(BigInteger low, int bits) {
    BigInteger result = new BigInteger(bits, random);
    while (result.compareTo(low) <= 0) {
      result = new BigInteger(bits, random);
    }
    return result;
  }


  public BigInteger[] maskInput(BigInteger[] input) {
    for (int i = 0; i < input.length; i++) {
      BigInteger random = randomElementInNSquare();
      input[i] = input[i].multiply(random);
    }
    return input;
  }

  public BigInteger randomElementInNSquare() {
    return randomElement(BigInteger.ONE, nSquared.subtract(BigInteger.ONE).bitLength());
  }
}
