package inner;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public final class InnerProductTest {

  private static final boolean[][] bloodTypeTable = new boolean[][]{
      new boolean[]{true, false, false, false, false, false, false, false},
      new boolean[]{true, true, false, false, false, false, false, false},
      new boolean[]{true, false, true, false, false, false, false, false},
      new boolean[]{true, true, true, true, false, false, false, false},
      new boolean[]{true, false, false, false, true, false, false, false},
      new boolean[]{true, true, false, false, true, true, false, false},
      new boolean[]{true, false, true, false, true, false, true, false},
      new boolean[]{true, true, true, true, true, true, true, true}};

  @Test
  void checkCorrectness() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Authority authority = Authority.setup(100);
        Bob bob = new Bob(i, authority);
        Alice alice = new Alice(j, authority);

        EncryptObj encryption = bob.encrypt();

        boolean result = alice.decrypt(encryption);
        Assertions.assertThat(result).isEqualTo(bloodTypeTable[i][j]);
      }
    }
  }
}
