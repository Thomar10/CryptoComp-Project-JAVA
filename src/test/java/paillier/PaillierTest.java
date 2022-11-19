package paillier;

import blood.BloodTypeTable;
import java.math.BigInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


public final class PaillierTest {

  @Test
  void checkCorrectness() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Authority authority = Authority.setup(100);
        Alice alice = new Alice(j, authority);
        Bob bob = new Bob(i, authority);
        BigInteger[] encryption = bob.encrypt();

        boolean result = alice.decrypt(encryption);
        Assertions.assertThat(result).isEqualTo(BloodTypeTable.bloodTypeTable[i][j]);
      }
    }
  }
}
