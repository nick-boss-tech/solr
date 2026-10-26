# Scaffold v3: teardown client-interrupt probe, round 2 (vm2)

Claim: `claims/teardown-client-interrupt-probe-round-2-vm2.md`. Assignment: `assignments/teardown-client-interrupt-probe-round-2.md`. Result: `reports/teardown-client-interrupt-probe-round-2.md`.

## Pins

- Base source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`, clean scratch worktree `/workspace/worktrees/teardown-probe-r3`. The v3 diff is cumulative against the original test file (index `365f9a2b098` to the new blob), not a delta to the round 1 or round 2 scaffolds.
- v3 diff SHA-256: `368ad9c67846d4f3383f7812208c95a0f5a49948724ab4e5f8b405362f8d5c90`. File on vm2: `/workspace/gates/gates-vm2/teardown-probe-scaffold-v3.diff` (242 lines). Recorded before any probe invocation.
- Seed `FFF5A3C3AAC69084`. JDK `/workspace/jdk/jdk-21.0.12.1+1` (21.0.12.1), Linux aarch64.
- Preserved round 1 and round 2 vm2 logs, XML and v1/v2 diffs: `/workspace/gates/gates-vm2/preserved-round1/`, sums in `/workspace/gates/gates-vm2/preserved-round1-SHA256SUMS.txt`.

## What v3 does (test file only)

- No manual `ObjectReleaseTracker` release, clear or untracking anywhere. The tracker state is read with `containsKey` only.
- One fresh client per arm, built on the test thread with the same builder settings as before. Default owned executor (`shutdownExecutor` stays true; no supplied executor).
- Identity, `HttpClient` and executor state recorded before close (class, identity hash, lifecycle flags, and for a `ThreadPoolExecutor`: active, pool size, largest, queued, completed, task count, shutdown, terminated).
- The closer is a single original `client.close()` on its own thread. Its return or throwable chain (with causes and suppressed) and its interrupt state at the end are recorded only after the completion latch fires. Before that they print `UNAVAILABLE`.
- Snapshots (all threads, full stacks, no name filter, no truncation) at: baseline before construction, after construction, unfinished at 5 s, 30 s, 65 s and 125 s, the terminal latch, and after cleanup. Times are monotonic offsets.
- Observation cap 150 s from close start. The arms are separate invocations: `control` (no interrupt) and `interrupted`.
- Cleanup: only after the original closer's latch has fired. A second `close()` runs on a noninterrupted thread with at most 30 s. Any cleanup throwable is logged with its full chain. Cleanup thread termination is checked by a snapshot after the join.

## Watchdog and containment (arranged before running)

- If the original closer is still active at 150 s: snapshot `cap-150s-close-still-active`, log the containment line, then `Runtime.getRuntime().halt(3)` in the isolated test JVM. No second close runs. The evidence file was written by then, because every snapshot is appended and the file is closed after each write.
- If cleanup is still alive after 30 s: snapshot, log, then `halt(4)`.
- The shell runner also has an outer `timeout 600` per invocation, as a backstop for the Gradle process tree.
- The evidence files are `solr/solrj-jetty/build/probe-evidence/control.log` and `interrupted.log` in the worktree. The runner copies XML only, so each file is copied to `/workspace/gates/gates-vm2/logs/teardown-probe-r3/` immediately after its invocation, before any next `cleanTest`.

## Commands (final)

Compile check (not a probe invocation; it ran through the guarded runner to confirm the v3 test module compiles; rc 0 after one correction, see below):

```bash
./gradlew --no-daemon --max-workers=2 :solr:solrj-jetty:compileTestJava --console=plain
```

Control invocation (first; must complete normally or the interrupted arm does not run):

```bash
timeout 600 pool/bin/sweep-run.sh /workspace/worktrees/teardown-probe-r3 <logdir> probe-r3-control \
  --no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithoutInterrupt \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

Interrupted invocation (second, only after the control completed normally):

```bash
timeout 600 pool/bin/sweep-run.sh /workspace/worktrees/teardown-probe-r3 <logdir> probe-r3-interrupted \
  --no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testCloseProbeWithInterrupt \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

No automatic third invocation. The cap is not raised.

## Compile note

The first v3 compile failed at `HttpJettySolrClientTest.java:1022-1023` (a checked `IOException` inside a lambda). The helpers now wrap it in `UncheckedIOException`. The file in the diff is the corrected version; the failed attempt is not in the diff.

## Diff

```diff
diff --git a/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java b/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
index 365f9a2b098..923b78d23f2 100644
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
@@ -937,4 +946,207 @@ public class HttpJettySolrClientTest extends HttpSolrClientTestBase {
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
+    ProbeState st = new ProbeState(arm, Path.of("build", "probe-evidence", arm + ".log"));
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
