package inner;

import group.Group;
import java.math.BigInteger;

public final class Authority {

  private final BigInteger[] msk;
  private final BigInteger[] mpk;
  private final Group group;

  private Authority(BigInteger[] msk, BigInteger[] mpk, Group group) {
    this.msk = msk;
    this.mpk = mpk;
    this.group = group;
  }

  static Authority setup(int security) {
    Group group = Group.generateGroup(security);
    SetupObj setup = InnerProduct.setup(group);
    return new Authority(setup.msk(), setup.mpk(), group);
  }

  public BigInteger[] getMpk() {
    return mpk;
  }

  public Group getGroup() {
    return group;
  }

  public BigInteger keyDer(BigInteger[] x) {
    return InnerProduct.keyDer(this.msk, x);
  }
}
