import java.math.BigInteger;
import java.util.Random;

public record Group(BigInteger prime, BigInteger q, BigInteger generator) {

  static Random random = new Random();

  /**
   * https://crypto.stackexchange.com/questions/9006/how-to-find-generator-g-in-a-cyclic-group/9011#9011
   *
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
          BigInteger pMinusOneDivQ = prime.subtract(BigInteger.ONE).divide(q);
          generator = randomBig.modPow(pMinusOneDivQ, prime);
          if (generator.compareTo(BigInteger.ONE) != 0) {
            break;
          }
        }
        break;
      }
    }
    return new Group(prime, q, generator);
  }


  public static BigInteger randomElement(BigInteger low, int bits) {
    BigInteger result = new BigInteger(bits, random);
    while (result.compareTo(low) <= 0) {
      result = new BigInteger(bits, random);
    }
    return result;
  }

  public BigInteger randomElementInQ(BigInteger low) {
    return randomElement(BigInteger.ONE, q.subtract(BigInteger.ONE).bitLength());
  }
}
