package inner;

import group.SafePrimeGroup;
import java.math.BigInteger;

public final class Authority {

  private final BigInteger[] msk;
  private final BigInteger[] mpk;
  private final SafePrimeGroup group;

  private Authority(BigInteger[] msk, BigInteger[] mpk, SafePrimeGroup group) {
    this.msk = msk;
    this.mpk = mpk;
    this.group = group;
  }

  static Authority setup(int security) {
    SafePrimeGroup group = SafePrimeGroup.generateGroup(security);
    SetupObj setup = InnerProduct.setup(group);
    return new Authority(setup.msk(), setup.mpk(), group);
  }

  public BigInteger[] getMpk() {
    return mpk;
  }

  public SafePrimeGroup getGroup() {
    return group;
  }

  public BigInteger keyDer(BigInteger[] x) {
    return InnerProduct.keyDer(this.msk, x);
  }
}
