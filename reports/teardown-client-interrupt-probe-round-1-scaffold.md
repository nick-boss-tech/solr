# Scaffold: teardown client-interrupt probe, round 1 (vm2)

Claim: `claims/teardown-client-interrupt-probe-round-1-vm2.md`. Assignment: `assignments/teardown-client-interrupt-probe-round-1.md`. Result: `reports/teardown-client-interrupt-probe-round-1.md`.

## Pins

- Source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`, in the clean scratch worktree `/workspace/worktrees/teardown-probe-vm2`. The tree was clean before the scaffold was added.
- Test class: `solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java`.
- Scaffold diff SHA-256: `2dc73a4038f03013761e5bef7ab58cdf500edeb143c58d088cc45f557d5b12c7`. The file is `/workspace/gates/gates-vm2/teardown-probe-scaffold.diff` on vm2. The hash was recorded before the run started, at 2026-10-25T23:58Z.
- Seed: `FFF5A3C3AAC69084`. JDK: `/workspace/jdk/jdk-21.0.12.1+1` (21.0.12.1).

## Diff (test file only; no production, dependency or hook change)

```diff
diff --git a/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java b/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
index 365f9a2b098..58e6eddf822 100644
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
@@ -937,4 +941,107 @@ public class HttpJettySolrClientTest extends HttpSolrClientTestBase {
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
+    boolean closerFinished = closerDone.await(30, TimeUnit.SECONDS);
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
+      if (t.isAlive() && (name.contains("HttpClient@") || name.contains("elector"))) {
+        sb.append(name).append('|').append(t.getState()).append(';');
+      }
+    }
+    return sb.length() == 0 ? "none" : sb.toString();
+  }
 }
```

## Environment discrepancies

- Runner: `pool/bin/sweep-run.sh` and `pool/bin/inode-guard.py` were copied from the board at the time of the run and run unchanged.
- Thread-name filter: the probe counts live threads whose names contain `HttpClient@` or `elector`. Jetty's own thread names were not checked on this host, so a `none` result is weak evidence (see the probe report).
- Formatting: the scratch diff was not run through tidy. It is not a source contribution, and the assignment waives the tidy ladder for this unit.
