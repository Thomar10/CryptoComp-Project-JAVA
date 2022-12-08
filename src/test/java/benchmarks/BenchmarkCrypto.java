package benchmarks;

import group.PaillierGroup;
import group.SafePrimeGroup;
import inner.InnerProduct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import paillier.Paillier;

public class BenchmarkCrypto {

  public static void main(String[] args) throws IOException {
    org.openjdk.jmh.Main.main(args);
  }

  @Fork(value = 1, warmups = 1)
  @Benchmark
  public void ddhEncryption(ExecutionPlan plan, Blackhole blackhole) {
    for (int i = plan.iterations; i > 0; i--) {
      BigInteger[] y = selectRandomY(i % 34, plan);
      blackhole.consume(InnerProduct.encrypt(plan.ddhSetup.mpk(), y, plan.ddhSetup.group()));
    }
  }

  @Fork(value = 1, warmups = 1)
  @Benchmark
  public void paillierEncryption(ExecutionPlan plan, Blackhole blackhole) {
    for (int i = plan.iterations; i > 0; i--) {
      BigInteger[] y = selectRandomY(i % 34, plan);
      blackhole.consume(Paillier.encrypt(plan.paillierSetup.mpk(), y, plan.paillierSetup.group()));
    }
  }

  @Fork(value = 1, warmups = 1)
  @Benchmark
  public void paillierDecryption(ExecutionPlan plan, Blackhole blackhole) {
    for (int i = plan.iterations; i > 0; i--) {
      BigInteger[] x = selectRandomX(i % 34, plan);
      blackhole.consume(Paillier.decrypt(plan.sky, plan.ctPaillier, x, plan.paillierSetup.group()));
    }
  }

  @Fork(value = 1, warmups = 1)
  @Benchmark
  public void ddhDecryption(ExecutionPlan plan, Blackhole blackhole) {
    for (int i = plan.iterations; i > 0; i--) {
      BigInteger[] x = selectRandomX(i % 34, plan);
      blackhole.consume(
          InnerProduct.decrypt(plan.ct0Ddh, plan.ctDdh, plan.sky, x, plan.ddhSetup.group()));
    }
  }

  @Fork(value = 1, warmups = 1)
  @Benchmark
  public void paillierKeyDer(ExecutionPlan plan, Blackhole blackhole) {
    for (int i = plan.iterations; i > 0; i--) {
      BigInteger[] y = selectRandomY(i % 34, plan);
      blackhole.consume(Paillier.keyGen(plan.paillierSetup.msk(), y, plan.paillierSetup.group()));
    }
  }

  @Fork(value = 1, warmups = 1)
  @Benchmark
  public void ddhKeyDer(ExecutionPlan plan, Blackhole blackhole) {
    for (int i = plan.iterations; i > 0; i--) {
      BigInteger[] y = selectRandomY(i % 34, plan);
      blackhole.consume(InnerProduct.keyDer(plan.ddhSetup.msk(), y));
    }
  }

  private BigInteger[] selectRandomX(int index, ExecutionPlan plan) {
    return plan.x[index % ExecutionPlan.COUNT];
  }

  private BigInteger[] selectRandomY(int index, ExecutionPlan plan) {
    return plan.y[index % ExecutionPlan.COUNT];
  }

  /** ExecutionPlan class. */
  @State(Scope.Benchmark)
  public static class ExecutionPlan {

    public static final int COUNT = 13;

    @Param({"1000"})
    private int iterations;

    private final BigInteger[][] y = new BigInteger[COUNT][];
    private final BigInteger[][] x = new BigInteger[COUNT][];

    private final BigInteger[] ctPaillier = new BigInteger[4];
    private final BigInteger[] ctDdh = new BigInteger[3];
    private BigInteger ct0Ddh;

    private BigInteger sky;

    private final int security = 1024;
    private final Paillier.Setup paillierSetup = Paillier.setup(security,
        PaillierGroup.generateGroup(security / 2));
    private final InnerProduct.SetupObj ddhSetup = InnerProduct.setup(
        SafePrimeGroup.generateGroup(security));

    /** Setup method for benchmarks. */
    @Setup(Level.Invocation)
    public void setUp() {
      Random random = new Random(1234);
      sky = new BigInteger(50, random);
      for (int i = 0; i < COUNT; i++) {
        BigInteger[] stateY = new BigInteger[3];
        BigInteger[] stateX = new BigInteger[3];
        for (int j = 0; j < stateY.length; j++) {
          stateY[j] = new BigInteger(1, random);
          stateX[j] = new BigInteger(1, random);
        }
        y[i] = stateY;
        x[i] = stateX;
      }
      BigInteger randomCipher0 = new BigInteger(50, random);
      ct0Ddh = randomCipher0;
      ctPaillier[0] = randomCipher0;
      for (int j = 0; j < ctDdh.length; j++) {
        randomCipher0 = new BigInteger(50, random);
        ctDdh[j] = randomCipher0;
        ctPaillier[j + 1] = randomCipher0;
      }
    }
  }
}
