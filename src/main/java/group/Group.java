package group;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public record Group(BigInteger prime, BigInteger q, BigInteger generator, BigInteger n,
                    BigInteger nSquared) {

  static Random random = new Random();

  public static BigInteger gaussian(int security, BigInteger n) {
    BigInteger desiredStandardDeviation = BigInteger.valueOf(security).sqrt()
        .multiply(n.pow(5).sqrt()).add(BigInteger.TWO);
    BigDecimal deviation = new BigDecimal(desiredStandardDeviation);
    return BigDecimal.valueOf(random.nextGaussian())
        .multiply(deviation).toBigInteger();
  }

  public static Group generatePaillierGroup(int security) {
    Group group = generateGroup(security);
    BigInteger n = group.prime().multiply(group.q());
    BigInteger nSquared = n.pow(2);
    while (true) {
      BigInteger gPrim = randomElement(BigInteger.TWO,
          nSquared.subtract(BigInteger.ONE).bitLength());
      BigInteger generator = gPrim.modPow(BigInteger.TWO.multiply(n), nSquared);
      return new Group(group.prime, group.q, generator, n, nSquared);
    }
  }

  private static Group generateSafePrimes(int security) {
    BigInteger prime;
    BigInteger q;
    while (true) {
      prime = BigInteger.probablePrime(security, random);
      q = prime.subtract(BigInteger.ONE).shiftRight(1);
      boolean probablePrime = q.isProbablePrime(1000);
      if (probablePrime) {
        return new Group(prime, q, null, null, null);
      }
    }
  }

  /**
   * @param security security parameter
   * @return group.
   */
  public static Group generateGroup(int security) {
    BigInteger prime;
    BigInteger q;
    BigInteger generator;
    while (true) {
      prime = BigInteger.probablePrime(security, random);
      q = prime.subtract(BigInteger.ONE).shiftRight(1);
      boolean probablePrime = q.isProbablePrime(1000);
      if (probablePrime) {
        while (true) {
          BigInteger randomBig = randomElement(BigInteger.TWO,
              prime.subtract(BigInteger.ONE).bitLength());
          generator = randomBig.modPow(BigInteger.TWO, prime);
          if (generator.compareTo(BigInteger.ONE) != 0) {
            break;
          }
        }
        break;
      }
    }
    return new Group(prime, q, generator, null, null);
  }

  public static BigInteger randomElement(BigInteger low, int bits) {
    BigInteger result = new BigInteger(bits, random);
    while (result.compareTo(low) <= 0) {
      result = new BigInteger(bits, random);
    }
    return result;
  }

  public BigInteger randomElementInQ() {
    return randomElement(BigInteger.ONE, q.subtract(BigInteger.ONE).bitLength());
  }

  public BigInteger[] maskInput(BigInteger[] input) {
    for (int i = 0; i < input.length; i++) {
      BigInteger random = randomElementInQ();
      input[i] = input[i].multiply(random);
    }
    return input;
  }
}
