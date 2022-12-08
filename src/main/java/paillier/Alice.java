package paillier;

import group.PaillierGroup;
import java.math.BigInteger;

public final class Alice {

  private final BigInteger[] input;
  private final PaillierGroup group;
  private final BigInteger secretKey;

  public Alice(int input, Authority authority) {
    this.group = authority.getGroup();
    this.input = getBits(input);
    this.secretKey = authority.keyGen(this.input);
  }

  public boolean decrypt(BigInteger[] encryption) {
    return Paillier.decrypt(this.secretKey, encryption, this.input, this.group);
  }

  static BigInteger[] getBits(int input) {
    int y3 = input & 1;
    int y2 = (input >> 1) & 1;
    int y1 = (input >> 2) & 1;
    return new BigInteger[]{BigInteger.valueOf(y1), BigInteger.valueOf(y2), BigInteger.valueOf(y3)};
  }
}
