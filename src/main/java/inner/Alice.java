package inner;

import group.SafePrimeGroup;
import java.math.BigInteger;

public final class Alice {

  private final BigInteger[] input;
  private final SafePrimeGroup group;
  private final BigInteger secretKey;

  public Alice(int input, Authority authority) {
    this.group = authority.getGroup();
    this.input = getBits(input);
    this.secretKey = authority.keyDer(this.input);
  }

  public boolean decrypt(EncryptObj encryption) {
    return InnerProduct.decrypt(encryption.ct0(), encryption.ci(), this.secretKey, this.input,
        this.group);
  }

  static BigInteger[] getBits(int input) {
    int y3 = input & 1;
    int y2 = (input >> 1) & 1;
    int y1 = (input >> 2) & 1;
    return new BigInteger[]{BigInteger.valueOf(y1), BigInteger.valueOf(y2), BigInteger.valueOf(y3)};
  }
}
