package group;

import java.math.BigInteger;
import java.util.Random;

public record SafePrimeGroup(BigInteger p, BigInteger q, BigInteger generator) {

  static Random random = new Random();

  public static SafePrimeGroup generateGroup(int security) {
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
    return new SafePrimeGroup(prime, q, generator);
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
