import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/** Runs only the CPU methods; never probes GPU availability or creates new fixtures. */
public class BackCompatRunner {
  public static void main(String[] args) throws Exception {
    Class<?> test = Class.forName("com.nvidia.cuvs.lucene.TestBackCompatIndices");
    boolean failed = false;
    for (String method : args) {
      Result result = new JUnitCore().run(Request.method(test, method));
      for (Failure failure : result.getFailures()) {
        System.out.println(failure.getTrace());
      }
      System.out.println("CPU_RESULT " + method + " run=" + result.getRunCount()
          + " failures=" + result.getFailureCount() + " ignored=" + result.getIgnoreCount()
          + " assumptions=" + result.getAssumptionFailureCount());
      failed |= !result.wasSuccessful() || result.getRunCount() != 1
          || result.getIgnoreCount() != 0 || result.getAssumptionFailureCount() != 0;
    }
    if (failed) System.exit(1);
  }
}
