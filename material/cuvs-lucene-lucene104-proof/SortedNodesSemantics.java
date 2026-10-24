/*
 * Compatibility proof for the actual patched private helper. No GPU operations.
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;
import org.apache.lucene.util.hnsw.HnswGraph;
import org.apache.lucene.util.hnsw.HnswGraph.NodesIterator;

public class SortedNodesSemantics {
  private static Method replacement;
  private static Method original;
  private static int comparisons;

  private static class Synthetic extends NodesIterator {
    private final int[] values;
    private int position;
    private int nextCalls;

    Synthetic(int size, int[] values, int start) {
      super(size);
      this.values = values.clone();
      this.position = start;
    }

    @Override
    public boolean hasNext() {
      return position < values.length;
    }

    @Override
    public int nextInt() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      nextCalls++;
      return values[position++];
    }

    @Override
    public int consume(int[] dest) {
      throw new AssertionError("Replacement must preserve hasNext/nextInt behavior");
    }
  }

  private record Outcome(int[] values, Class<?> exception) {}

  private static Outcome invoke(Method method, NodesIterator iterator) throws Exception {
    try {
      return new Outcome((int[]) method.invoke(null, iterator), null);
    } catch (InvocationTargetException e) {
      return new Outcome(null, e.getCause().getClass());
    }
  }

  private static void compare(String name, int size, int[] values, int start) throws Exception {
    Synthetic left = new Synthetic(size, values, start);
    Synthetic right = new Synthetic(size, values, start);
    Outcome expected = invoke(original, left);
    Outcome actual = invoke(replacement, right);
    if (!Arrays.equals(expected.values(), actual.values())
        || expected.exception() != actual.exception()
        || left.hasNext() != right.hasNext()
        || left.position != right.position
        || left.nextCalls != right.nextCalls) {
      throw new AssertionError(name + ": original=" + expected + ", replacement=" + actual);
    }
    if (actual.exception() == null && right.hasNext()) {
      throw new AssertionError(name + ": iterator not exhausted");
    }
    comparisons++;
    if (!name.startsWith("random-")) {
      System.out.println(name + ": PASS result=" + Arrays.toString(actual.values())
          + " exception=" + actual.exception() + " exhausted=" + !right.hasNext()
          + " nextIntCalls=" + right.nextCalls);
    }
  }

  private static void arrayIterator(String name, HnswGraph.ArrayNodesIterator a,
                                    HnswGraph.ArrayNodesIterator b) throws Exception {
    Outcome expected = invoke(original, a);
    Outcome actual = invoke(replacement, b);
    if (!Arrays.equals(expected.values(), actual.values()) || a.hasNext() || b.hasNext()) {
      throw new AssertionError(name);
    }
    comparisons++;
    System.out.println(name + ": PASS result=" + Arrays.toString(actual.values())
        + " exhausted=true");
  }

  public static void main(String[] args) throws Exception {
    replacement = Class.forName("com.nvidia.cuvs.lucene.AcceleratedHNSWUtils")
        .getDeclaredMethod("getSortedNodes", NodesIterator.class);
    replacement.setAccessible(true);
    if (args.length > 0 && args[0].equals("replacement-only")) {
      Synthetic iterator = new Synthetic(6, new int[] {9, 0, 9, 3, 1, 3}, 0);
      Outcome result = invoke(replacement, iterator);
      if (!Arrays.equals(result.values(), new int[] {0, 1, 3, 3, 9, 9})
          || iterator.hasNext() || iterator.nextCalls != 6 || result.exception() != null) {
        throw new AssertionError("10.4 production-helper smoke");
      }
      System.out.println("Lucene 10.4 actual production helper: PASS, sorted duplicates retained,"
          + " iterator exhausted, no GPU operation");
      return;
    }
    original = NodesIterator.class.getMethod("getSortedNodes", NodesIterator.class);
    compare("empty", 0, new int[] {}, 0);
    compare("single", 1, new int[] {42}, 0);
    compare("ascending", 5, new int[] {0, 1, 4, 12, 500}, 0);
    compare("descending", 5, new int[] {500, 12, 4, 1, 0}, 0);
    compare("duplicates", 7, new int[] {4, 0, 4, 2, 0, 4, 2}, 0);
    compare("integer-extremes", 5,
        new int[] {Integer.MAX_VALUE, -1, Integer.MIN_VALUE, 0, 1}, 0);
    compare("already-exhausted-size-zero", 0, new int[] {4}, 1);
    compare("partially-consumed-preserves-size", 5, new int[] {9, 8, 7, 6, 5}, 2);
    compare("size-overstates-remaining", 4, new int[] {7, 1}, 0);
    compare("size-understates-remaining", 2, new int[] {7, 1, 4, 8}, 0);
    compare("zero-size-with-values", 0, new int[] {3, 4}, 0);
    arrayIterator("lucene-array", new HnswGraph.ArrayNodesIterator(new int[] {9, 1, 9}, 3),
        new HnswGraph.ArrayNodesIterator(new int[] {9, 1, 9}, 3));
    arrayIterator("lucene-implicit", new HnswGraph.ArrayNodesIterator(6),
        new HnswGraph.ArrayNodesIterator(6));
    arrayIterator("lucene-array-empty", new HnswGraph.ArrayNodesIterator(0),
        new HnswGraph.ArrayNodesIterator(0));
    Random random = new Random(0x18556L);
    for (int run = 0; run < 512; run++) {
      int[] values = new int[random.nextInt(129)];
      for (int i = 0; i < values.length; i++) {
        values[i] = random.nextInt(41) - 20;
      }
      compare("random-" + run, values.length, values, 0);
    }
    System.out.println("PASS " + comparisons + " original-versus-patched comparisons;"
        + " seed=0x18556; arrays, exceptions, positions, nextInt calls and exhaustion matched.");
  }
}
