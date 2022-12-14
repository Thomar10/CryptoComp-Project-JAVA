package inner;

import blood.BloodTypeTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public final class InnerProductTest {

  @Test
  void checkCorrectness() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Authority authority = Authority.setup(100);
        Bob bob = new Bob(i, authority);
        Alice alice = new Alice(j, authority);

        EncryptObj encryption = bob.encrypt();

        boolean result = alice.decrypt(encryption);
        Assertions.assertThat(result).isEqualTo(BloodTypeTable.bloodTypeTable[i][j]);
      }
    }
  }
}
