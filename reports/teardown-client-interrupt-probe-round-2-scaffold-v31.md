# Scaffold v3.1: teardown client-interrupt probe, round 2 (vm2)

Supersedes the v3 scaffold (`reports/teardown-client-interrupt-probe-round-2-scaffold.md`, diff SHA-256 `368ad9c67846d4f3383f7812208c95a0f5a49948724ab4e5f8b405362f8d5c90`) for the rerun only. v3 is kept unchanged.

## Pins

- Base source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`, clean worktree `/workspace/worktrees/teardown-probe-r3`.
- v3.1 diff is cumulative against the original test file. SHA-256: `dee219511e648f566c73893b15542e4f76198ff4a8b090c8bad19d1e4f325c05`. File: `/workspace/gates/gates-vm2/teardown-probe-scaffold-v31.diff`.
- Seed `FFF5A3C3AAC69084`. JDK 21.0.12.1+1, Linux aarch64.

## The only change from v3

The evidence file moved from `build/probe-evidence` to `java.io.tmpdir/probe-evidence/<arm>.log`. The test security policy (`gradle/testing/randomization/policies/solr-tests.policy`, lines 31-32) permits writes under `java.io.tmpdir`, and the v3 path was denied (`logs/teardown-probe-r3/probe-r3-control.log`). No probe logic, close call, snapshot, cap, watchdog or cleanup changed.

## Invocation count

The assignment allows two filtered invocations. The first v3 control was denied before any close and is recorded as a scaffold failure, not as a premise run. The owner authorized the corrected re-gate on 2026-10-26, so v3.1 gets one control invocation and then one interrupted invocation.

## Compile

`:solr:solrj-jetty:compileTestJava` through the guarded runner, rc 0, no errors. This is a compile check, not a probe invocation.

## Commands

Control, then (only if it completed normally) the interrupted arm, each in its own JVM via `pool/bin/sweep-run.sh`, under `/workspace/tools/.gradle-vm2.lock`, with `timeout 600`:

```bash
--no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithoutInterrupt \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
--no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithInterrupt \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

## Diff

```diff
diff --git a/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java b/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
index 365f9a2b098..179575534bf 100644
--- a/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
+++ b/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
@@ -22,13 +22,21 @@ import static org.hamcrest.core.StringContains.containsStringIgnoringCase;
 
 import java.io.IOException;
 import java.io.InputStream;
+import java.io.UncheckedIOException;
 import java.nio.charset.StandardCharsets;
+import java.nio.file.Files;
+import java.nio.file.Path;
+import java.nio.file.StandardOpenOption;
 import java.util.Base64;
+import java.util.Map;
 import java.util.UUID;
 import java.util.concurrent.CompletableFuture;
+import java.util.concurrent.CountDownLatch;
+import java.util.concurrent.Executor;
 import java.util.concurrent.ExecutionException;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Future;
+import java.util.concurrent.ThreadPoolExecutor;
 import java.util.concurrent.TimeUnit;
 import java.util.stream.Collectors;
 import java.util.stream.IntStream;
@@ -57,6 +65,7 @@ import org.apache.solr.common.params.ModifiableSolrParams;
 import org.apache.solr.common.params.SolrParams;
 import org.apache.solr.common.util.ExecutorUtil;
 import org.apache.solr.common.util.NamedList;
+import org.apache.solr.common.util.ObjectReleaseTracker;
 import org.apache.solr.common.util.SolrNamedThreadFactory;
 import org.apache.solr.common.util.TimeSource;
 import org.apache.solr.util.ServletFixtures;
@@ -937,4 +946,209 @@ public class HttpJettySolrClientTest extends HttpSolrClientTestBase {
   }
 
   /* Missed tests : - set cookies via interceptor - invariant params - compression */
+
+  private static final long[] PROBE_MARKS_SECONDS = {5, 30, 65, 125};
+  private static final long PROBE_CAP_SECONDS = 150;
+
+  @Test
+  public void testCloseProbeWithoutInterrupt() throws Exception {
+    runCloseProbe("control", false);
+  }
+
+  @Test
+  public void testCloseProbeWithInterrupt() throws Exception {
+    runCloseProbe("interrupted", true);
+  }
+
+  private void runCloseProbe(String arm, boolean interruptBeforeClose) throws Exception {
+    ProbeState st =
+        new ProbeState(
+            arm, Path.of(System.getProperty("java.io.tmpdir"), "probe-evidence", arm + ".log"));
+    st.snapshot("baseline-before-construction");
+    HttpJettySolrClient client =
+        (HttpJettySolrClient)
+            builder(solrTestRule.getBaseUrl() + "/", DEFAULT_CONNECTION_TIMEOUT, 2000).build();
+    st.client = client;
+    st.snapshot("after-construction");
+
+    Thread closer =
+        new Thread(
+            () -> {
+              if (interruptBeforeClose) {
+                Thread.currentThread().interrupt();
+              }
+              st.closerInterruptBeforeClose = Thread.currentThread().isInterrupted();
+              try {
+                client.close();
+                st.returned = true;
+              } catch (Throwable t) {
+                st.thrown = t;
+              } finally {
+                st.closerInterruptAtEnd = Thread.currentThread().isInterrupted();
+                st.done.countDown();
+              }
+            },
+            "probe-closer-" + arm);
+    st.closerThread = closer;
+    st.closeStart = System.nanoTime();
+    closer.start();
+
+    try {
+      for (long mark : PROBE_MARKS_SECONDS) {
+        long wait = st.closeStart + TimeUnit.SECONDS.toNanos(mark) - System.nanoTime();
+        if (wait > 0 && st.done.await(wait, TimeUnit.NANOSECONDS)) {
+          break;
+        }
+        if (st.done.getCount() > 0) {
+          st.snapshot("unfinished-at-" + mark + "s");
+        }
+      }
+      long capWait = st.closeStart + TimeUnit.SECONDS.toNanos(PROBE_CAP_SECONDS) - System.nanoTime();
+      if (st.done.getCount() > 0 && capWait > 0) {
+        st.done.await(capWait, TimeUnit.NANOSECONDS);
+      }
+      if (st.done.getCount() > 0) {
+        st.snapshot("cap-" + PROBE_CAP_SECONDS + "s-close-still-active");
+        st.log("CONTAINMENT: original closer still active at the cap. Halting this isolated test JVM. No second close is run.");
+        Runtime.getRuntime().halt(3);
+      }
+      st.snapshot("terminal-latch-fired");
+    } finally {
+      if (st.done.getCount() == 0) {
+        Thread cleanup =
+            new Thread(
+                () -> {
+                  try {
+                    client.close();
+                    st.log("cleanup close returned normally");
+                  } catch (Throwable t) {
+                    st.log("cleanup close threw:");
+                    st.appendThrowable(t);
+                  }
+                },
+                "probe-cleanup-" + arm);
+        cleanup.start();
+        cleanup.join(TimeUnit.SECONDS.toMillis(30));
+        if (cleanup.isAlive()) {
+          st.snapshot("cleanup-still-alive-after-30s");
+          st.log("CONTAINMENT: cleanup still alive after 30 s. Halting this isolated test JVM.");
+          Runtime.getRuntime().halt(4);
+        }
+        st.snapshot("after-cleanup");
+      }
+    }
+  }
+
+  private static final class ProbeState {
+    final String arm;
+    final Path evidence;
+    final long t0 = System.nanoTime();
+    final CountDownLatch done = new CountDownLatch(1);
+    volatile HttpJettySolrClient client;
+    volatile Thread closerThread;
+    volatile long closeStart;
+    volatile boolean closerInterruptBeforeClose;
+    volatile boolean closerInterruptAtEnd;
+    volatile boolean returned;
+    volatile Throwable thrown;
+
+    ProbeState(String arm, Path evidence) throws IOException {
+      this.arm = arm;
+      this.evidence = evidence;
+      Files.createDirectories(evidence.getParent());
+      Files.writeString(evidence, "", StandardCharsets.UTF_8);
+    }
+
+    void log(String line) {
+      append("LOG t=" + (System.nanoTime() - t0) / 1_000_000 + "ms " + line + "\n");
+    }
+
+    void appendThrowable(Throwable t) {
+      StringBuilder sb = new StringBuilder();
+      appendChain(sb, t, "  ");
+      append(sb.toString());
+    }
+
+    private void append(String text) {
+      try {
+        Files.writeString(evidence, text, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
+      } catch (IOException e) {
+        throw new UncheckedIOException(e);
+      }
+    }
+
+    void snapshot(String label) throws IOException {
+      boolean terminal = done.getCount() == 0;
+      StringBuilder sb = new StringBuilder();
+      sb.append("=== SNAPSHOT ").append(label)
+          .append(" t=").append((System.nanoTime() - t0) / 1_000_000).append("ms")
+          .append(" arm=").append(arm).append('\n');
+      sb.append("closeOutcome: ").append(terminal ? (returned ? "RETURNED" : "THREW") : "UNAVAILABLE (close still active)").append('\n');
+      sb.append("interruptBeforeClose: ").append(closerInterruptBeforeClose).append('\n');
+      sb.append("interruptAtEnd: ").append(terminal ? String.valueOf(closerInterruptAtEnd) : "UNAVAILABLE").append('\n');
+      sb.append("closeThrowable: ").append(!terminal ? "UNAVAILABLE" : thrown == null ? "none" : "see chain below").append('\n');
+      if (terminal && thrown != null) {
+        appendChain(sb, thrown, "  ");
+      }
+      Thread ct = closerThread;
+      sb.append("closer: ").append(ct == null ? "not-started" : ct.getName() + " state=" + ct.getState() + " alive=" + ct.isAlive()).append('\n');
+      HttpJettySolrClient c = client;
+      if (c != null) {
+        sb.append("client: class=").append(c.getClass().getName())
+            .append(" identity=").append(System.identityHashCode(c))
+            .append(" trackedInObjectReleaseTracker=").append(ObjectReleaseTracker.OBJECTS.containsKey(c)).append('\n');
+        org.eclipse.jetty.client.HttpClient hc = c.getHttpClient();
+        sb.append("httpClient: class=").append(hc.getClass().getName())
+            .append(" identity=").append(System.identityHashCode(hc))
+            .append(" started=").append(hc.isStarted())
+            .append(" starting=").append(hc.isStarting())
+            .append(" stopping=").append(hc.isStopping())
+            .append(" stopped=").append(hc.isStopped())
+            .append(" failed=").append(hc.isFailed()).append('\n');
+        Executor ex = hc.getExecutor();
+        sb.append("executor: class=").append(ex == null ? "null" : ex.getClass().getName())
+            .append(" identity=").append(ex == null ? "null" : String.valueOf(System.identityHashCode(ex))).append('\n');
+        if (ex instanceof ThreadPoolExecutor tpe) {
+          sb.append("executorState: active=").append(tpe.getActiveCount())
+              .append(" poolSize=").append(tpe.getPoolSize())
+              .append(" largest=").append(tpe.getLargestPoolSize())
+              .append(" queued=").append(tpe.getQueue().size())
+              .append(" completed=").append(tpe.getCompletedTaskCount())
+              .append(" taskCount=").append(tpe.getTaskCount())
+              .append(" shutdown=").append(tpe.isShutdown())
+              .append(" terminated=").append(tpe.isTerminated()).append('\n');
+        }
+      }
+      sb.append("threads (all, no filter, no truncation):\n");
+      for (Map.Entry<Thread, StackTraceElement[]> e : Thread.getAllStackTraces().entrySet()) {
+        Thread t = e.getKey();
+        sb.append(" thread name=").append(t.getName())
+            .append(" id=").append(t.getId())
+            .append(" identity=").append(System.identityHashCode(t))
+            .append(" state=").append(t.getState())
+            .append(" daemon=").append(t.isDaemon())
+            .append(" isProbeCloser=").append(t == ct).append('\n');
+        for (StackTraceElement f : e.getValue()) {
+          sb.append("     at ").append(f).append('\n');
+        }
+      }
+      Files.writeString(evidence, sb.toString(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
+    }
+
+    private static void appendChain(StringBuilder sb, Throwable t, String indent) {
+      for (Throwable c = t; c != null; c = c.getCause()) {
+        sb.append(indent).append(c).append('\n');
+        for (StackTraceElement f : c.getStackTrace()) {
+          sb.append(indent).append("   at ").append(f).append('\n');
+        }
+        for (Throwable s : c.getSuppressed()) {
+          sb.append(indent).append("suppressed:\n");
+          appendChain(sb, s, indent + "  ");
+        }
+        if (c.getCause() == c) {
+          break;
+        }
+      }
+    }
+  }
 }
```
