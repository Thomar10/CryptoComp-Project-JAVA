package paillier;

import group.PaillierGroup;
import java.math.BigInteger;
import paillier.Paillier.Setup;

public final class Authority {
  private final BigInteger[] msk;
  private final BigInteger[] mpk;
  private final PaillierGroup group;

  private Authority(BigInteger[] mpk, BigInteger[] msk, PaillierGroup group) {
    this.mpk = mpk;
    this.msk = msk;
    this.group = group;
  }
  public BigInteger[] getMpk() {
    return mpk;
  }

  public PaillierGroup getGroup() {
    return group;
  }

  public BigInteger keyGen(BigInteger[] x) {
    return Paillier.keyGen(this.msk, x);
  }

  static Authority setup(int security) {
    PaillierGroup group = PaillierGroup.generateGroup(security);
    Setup setup = Paillier.setup(security, group);
    return new Authority(setup.mpk(), setup.msk(), group);
  }
}
