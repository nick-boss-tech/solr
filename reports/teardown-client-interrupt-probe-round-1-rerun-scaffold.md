# Scaffold v2: teardown client-interrupt probe rerun (vm2)

Claim: `claims/teardown-client-interrupt-probe-round-1-rerun-vm2.md`, superseding `claims/teardown-client-interrupt-probe-round-1-vm2.md`. Result: `reports/teardown-client-interrupt-probe-round-1-rerun.md`.

## Pins

- Source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`, clean worktree `/workspace/worktrees/teardown-probe-vm2`. The tree was clean before v2 was applied.
- Base scaffold: the round-1 diff (`reports/teardown-client-interrupt-probe-round-1-scaffold.md`, SHA-256 `2dc73a4038f03013761e5bef7ab58cdf500edeb143c58d088cc45f557d5b12c7`), with v2 applied on top.
- v2 diff SHA-256: `f6d7f6f252051b6c628bb97614fb3a3cefde4e5fcb0d023ada97a23dc2c22709` (file `/workspace/gates/gates-vm2/teardown-probe-scaffold-v2.diff`, 175 lines). Hash recorded before the run, which started 2026-10-26T00:31:06Z.
- Seed `FFF5A3C3AAC69084`. JDK `/workspace/jdk/jdk-21.0.12.1+1` (21.0.12.1).

## What v2 adds (test file only)

- At 5 s and 30 s after the closer starts, if it has not returned: dump the closer thread and every live thread whose name matches the probe's filter, with up to 25 frames each (`PROBE-STACK`).
- One listing of every live thread name and state, after the closer finishes or times out (`PROBE-NAMES`).
- A broader name filter: `HttpClient`, `elector`, `qtp`, `Jetty`, `Scheduler`, `Selector`, `Scanner`, `probe`.
- The 30 s per-arm budget is unchanged (5 s plus 25 s).

Gap: the filter does not include `h2sc` (the client executor's thread names) or `workers` (the client selector's names). Those threads' stacks were not dumped; see the report.

## Diff

```diff
diff --git a/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java b/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
index 365f9a2b098..88358c506c2 100644
--- a/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
+++ b/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
@@ -26,10 +26,13 @@ import java.nio.charset.StandardCharsets;
 import java.util.Base64;
 import java.util.UUID;
 import java.util.concurrent.CompletableFuture;
+import java.util.concurrent.CountDownLatch;
 import java.util.concurrent.ExecutionException;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Future;
 import java.util.concurrent.TimeUnit;
+import java.util.concurrent.atomic.AtomicBoolean;
+import java.util.concurrent.atomic.AtomicReference;
 import java.util.stream.Collectors;
 import java.util.stream.IntStream;
 import org.apache.lucene.tests.util.LuceneTestCase;
@@ -57,6 +60,7 @@ import org.apache.solr.common.params.ModifiableSolrParams;
 import org.apache.solr.common.params.SolrParams;
 import org.apache.solr.common.util.ExecutorUtil;
 import org.apache.solr.common.util.NamedList;
+import org.apache.solr.common.util.ObjectReleaseTracker;
 import org.apache.solr.common.util.SolrNamedThreadFactory;
 import org.apache.solr.common.util.TimeSource;
 import org.apache.solr.util.ServletFixtures;
@@ -937,4 +941,148 @@ public class HttpJettySolrClientTest extends HttpSolrClientTestBase {
   }
 
   /* Missed tests : - set cookies via interceptor - invariant params - compression */
+
+  @Test
+  public void testCloseProbeWithoutInterrupt() throws Exception {
+    runCloseProbe("without-interrupt", false);
+  }
+
+  @Test
+  public void testCloseProbeWithInterrupt() throws Exception {
+    runCloseProbe("with-interrupt", true);
+  }
+
+  private void runCloseProbe(String arm, boolean interruptBeforeClose) throws Exception {
+    HttpJettySolrClient client =
+        (HttpJettySolrClient)
+            builder(solrTestRule.getBaseUrl() + "/", DEFAULT_CONNECTION_TIMEOUT, 2000).build();
+    System.out.println(
+        "PROBE arm="
+            + arm
+            + " trackedAfterBuild="
+            + ObjectReleaseTracker.OBJECTS.containsKey(client)
+            + " httpClientThreadsBefore="
+            + httpClientThreads());
+
+    AtomicReference<Throwable> closeError = new AtomicReference<>();
+    AtomicBoolean interruptFlagAfterClose = new AtomicBoolean();
+    CountDownLatch closerDone = new CountDownLatch(1);
+    Thread closer =
+        new Thread(
+            () -> {
+              if (interruptBeforeClose) {
+                Thread.currentThread().interrupt();
+              }
+              try {
+                client.close();
+              } catch (Throwable t) {
+                closeError.set(t);
+              } finally {
+                interruptFlagAfterClose.set(Thread.currentThread().isInterrupted());
+                closerDone.countDown();
+              }
+            },
+            "probe-closer-" + arm);
+    closer.start();
+    boolean closerFinished = closerDone.await(5, TimeUnit.SECONDS);
+    if (!closerFinished) {
+      dumpThreads(arm, "t5s", closer);
+      closerFinished = closerDone.await(25, TimeUnit.SECONDS);
+      if (!closerFinished) {
+        dumpThreads(arm, "t30s", closer);
+      }
+    }
+    System.out.println("PROBE-NAMES arm=" + arm + " " + allThreadNames());
+    boolean trackedAfterClose = ObjectReleaseTracker.OBJECTS.containsKey(client);
+    String threadsAfterClose = httpClientThreads();
+
+    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(1);
+    while (System.nanoTime() < deadline && !"none".equals(httpClientThreads())) {
+      Thread.sleep(50);
+    }
+    System.out.println(
+        "PROBE arm="
+            + arm
+            + " closerFinished="
+            + closerFinished
+            + " closeError="
+            + causeChain(closeError.get())
+            + " interruptFlagAfterClose="
+            + interruptFlagAfterClose.get()
+            + " trackedAfterClose="
+            + trackedAfterClose
+            + " httpClientThreadsAfterClose="
+            + threadsAfterClose
+            + " httpClientThreadsAfter1s="
+            + httpClientThreads());
+
+    if (trackedAfterClose) {
+      ObjectReleaseTracker.release(client);
+    }
+    Thread cleanup = new Thread(() -> {
+          try {
+            client.close();
+          } catch (Throwable ignored) {
+            // cleanup only; the captured evidence is already printed above
+          }
+        }, "probe-cleanup-" + arm);
+    cleanup.start();
+    cleanup.join(30_000);
+    System.out.println("PROBE arm=" + arm + " cleanupFinished=" + !cleanup.isAlive() + " closerAlive=" + closer.isAlive());
+  }
+
+  private static String causeChain(Throwable t) {
+    if (t == null) {
+      return "none";
+    }
+    StringBuilder sb = new StringBuilder();
+    for (Throwable c = t; c != null; c = c.getCause()) {
+      sb.append(c.getClass().getName()).append(": ").append(c.getMessage()).append(" <- ");
+    }
+    return sb.toString();
+  }
+
+  private static String httpClientThreads() {
+    StringBuilder sb = new StringBuilder();
+    for (Thread t : Thread.getAllStackTraces().keySet()) {
+      String name = t.getName();
+      if (t.isAlive() && isClientThread(name)) {
+        sb.append(name).append('|').append(t.getState()).append(';');
+      }
+    }
+    return sb.length() == 0 ? "none" : sb.toString();
+  }
+
+  private static boolean isClientThread(String name) {
+    return name.contains("HttpClient")
+        || name.contains("elector")
+        || name.contains("qtp")
+        || name.contains("Jetty")
+        || name.contains("Scheduler")
+        || name.contains("Selector")
+        || name.contains("Scanner")
+        || name.contains("probe");
+  }
+
+  private static String allThreadNames() {
+    StringBuilder sb = new StringBuilder();
+    for (Thread t : Thread.getAllStackTraces().keySet()) {
+      sb.append(t.getName()).append('|').append(t.getState()).append(';');
+    }
+    return sb.toString();
+  }
+
+  private static void dumpThreads(String arm, String when, Thread closer) {
+    System.out.println("PROBE-STACK arm=" + arm + " when=" + when + " closerState=" + closer.getState());
+    for (java.util.Map.Entry<Thread, StackTraceElement[]> e : Thread.getAllStackTraces().entrySet()) {
+      Thread t = e.getKey();
+      if (!t.isAlive()) continue;
+      if (!(t == closer || isClientThread(t.getName()))) continue;
+      System.out.println("PROBE-STACK arm=" + arm + " when=" + when + " thread=" + t.getName() + " state=" + t.getState());
+      StackTraceElement[] frames = e.getValue();
+      for (int i = 0; i < Math.min(frames.length, 25); i++) {
+        System.out.println("PROBE-STACK arm=" + arm + " when=" + when + "     at " + frames[i]);
+      }
+    }
+  }
 }
```

## Environment notes

- Runner: `pool/bin/sweep-run.sh` and `pool/bin/inode-guard.py`, copied from the board as in round 1. Unchanged.
- Not run through tidy; not a source contribution.
