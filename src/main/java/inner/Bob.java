package inner;

import java.math.BigInteger;

public final class Bob {

  private final BigInteger[] maskedInput;
  private final BigInteger[] mpk;

  private final Group group;

  public Bob(int input, Authority authority) {
    this.group = authority.getGroup();
    this.maskedInput = group.maskInput(getBits(input));
    this.mpk = authority.getMpk();
  }

  public EncryptObj encrypt() {
    return InnerProduct.encrypt(mpk, maskedInput, group);
  }

  private static BigInteger[] getBits(int input) {
    int x3 = 1 - (input & 1);
    int x2 = 1 - ((input >> 1) & 1);
    int x1 = 1 - ((input >> 2) & 1);
    return new BigInteger[]{BigInteger.valueOf(x1), BigInteger.valueOf(x2), BigInteger.valueOf(x3)};
  }
}
