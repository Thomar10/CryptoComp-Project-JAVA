package blood;

public final class BloodTypeTable {
  public static final boolean[][] bloodTypeTable = new boolean[][]{
      new boolean[]{true, false, false, false, false, false, false, false},
      new boolean[]{true, true, false, false, false, false, false, false},
      new boolean[]{true, false, true, false, false, false, false, false},
      new boolean[]{true, true, true, true, false, false, false, false},
      new boolean[]{true, false, false, false, true, false, false, false},
      new boolean[]{true, true, false, false, true, true, false, false},
      new boolean[]{true, false, true, false, true, false, true, false},
      new boolean[]{true, true, true, true, true, true, true, true}};
}
