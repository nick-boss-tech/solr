# Scaffold: external-executor close ownership probe, round 1 (vm2)

Claim: `claims/teardown-client-close-external-executor-round-1-vm2.md`. Assignment: `assignments/teardown-client-close-external-executor-round-1.md`. Result: `reports/teardown-client-close-external-executor-round-1.md`.

## Pins

- Base source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`, clean worktree `/workspace/worktrees/teardown-ext-r1`. The diff below is cumulative against that base. It contains the v3.1 scaffold (SHA-256 `dee219511e648f566c73893b15542e4f76198ff4a8b090c8bad19d1e4f325c05`, verified before applying) plus the two new methods.
- Cumulative diff SHA-256: `1965b60a9a14b197dd18a360763ba993f73f4f655425945653bad915029f70c7`. File on vm2: `/workspace/gates/gates-vm2/teardown-ext-scaffold-r1.diff` (515 lines).
- Seed `FFF5A3C3AAC69084`. JDK `/workspace/jdk/jdk-21.0.12.1+1` (21.0.12.1), Linux aarch64, security manager on, ISO-8859-1.
- Declared Jetty `12.1.14`. The test classpath resolved `jetty-client-12.1.12.jar` (from the Gradle cache). That mismatch is reported as in the assignment. The probe does not substitute anything.

## What the new methods do

- Each method: fresh `ExecutorUtil.newMDCAwareCachedThreadPool` with `SolrNamedThreadFactory("probe-ext-<arm>")`, and a fresh `HttpJettySolrClient` built with `.withExecutor(pool)`. No `withHttpClient(parent)`. The default HTTP/2 transport. No requests.
- Saved references before close: client, HttpClient, its executor (asserted to be the caller pool), the ClientConnector (from the transport), each SelectorManager, each ManagedSelector (copied from `getBeans(ManagedSelector.class)` before close), and each raw `java.nio.channels.Selector` from `ManagedSelector.getSelector()`. Setup fails unless exactly two saved selectors are open.
- Control: `interruptBeforeClose` is false. Interrupted: the closer sets the interrupt immediately before `client.close()`. The closer's outcome, interrupt-at-end and throwable chain are UNAVAILABLE until the latch fires.
- Snapshots (full stacks for every thread, no filter): baseline-before-construction, after-construction, pre-close, active-at-5s (if still active), cap-30s (if still active), terminal-immediate, terminal-plus-1s, pre-cleanup-labelled, after-cleanup-labelled. Each has the tracker membership, HttpClient/ClientConnector/SelectorManager/ManagedSelector lifecycle flags, each saved raw selector's `isOpen()`, and the caller pool's state.
- Caller-executor sentinel: after both terminal captures, `pool.submit(...).get(5 s)`. Result logged (alive / rejected / timeout / failed).
- Cleanup (only after the original closer has finished, and it is labelled separately): explicitly close any saved raw selector still open; one serial noninterrupted `client.close()` with a 30 s join; then `pool.shutdownNow()` and `awaitTermination(5 s)`. Every step is logged. The 60+60 s helper is not used.
- No manual tracker release, clear or untracking. No concurrent second close. No daemon conversion. No linger increase.

## Watchdog and containment (arranged before running)

- If the original close is still active at 30 s: snapshot `cap-30s-close-still-active`, log the containment line, then `Runtime.getRuntime().halt(3)` in the isolated test JVM. No rescue close runs.
- If the cleanup close is still alive after 30 s: log the containment line and `halt(4)`.
- The runner also has `timeout 600` per invocation (the guarded `pool/bin/sweep-run.sh` runs under `/workspace/tools/.gradle-vm2.lock`), and the whole pair runs under `timeout 1200`.

## Duplicate-run protection

- Driver: `gates/gates-vm2/run-ext-pair.sh` (copy in the board scaffold record). It takes a nonblocking lock `/workspace/tools/.teardown-ext-pair.lock` (distinct from the build lock), and refuses if another probe or Gradle test process is running.
- Attempt ledger: `gates/gates-vm2/ext-attempt-ledger.txt`, written before each invocation.
- Evidence and output: each arm writes to a new directory created with `mkdir` (exclusive; the driver refuses existing paths). Evidence files are created with `Files.createTempDirectory` and `Files.createFile` (exclusive), and never truncated.
- Control gate: the interrupted arm runs only if the control returns, its tracker is released, all saved raw selectors are closed, and the sentinel reports alive. The check is `gates/gates-vm2/ext-check-control.py`.

## Commands (final)

Control first, then the interrupted arm only if the control is clean:

```bash
./gradlew --no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testExternalExecutorCloseProbeControl \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1

./gradlew --no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.testExternalExecutorCloseProbeInterrupted \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

Compile-only check before publication (not a test invocation): `:solr:solrj-jetty:compileTestJava` through the guarded runner, rc 0, 0 warnings. One `@SuppressWarnings("removal")` is on the probe method, for the deprecated `HttpClient.getTransport()` that reaches the saved ClientConnector.

## Budget

At most two filtered invocations total, counting any setup failure. Round 2's overrun and the overwritten first control remain on record, and this claim does not approve them.

## Diff

```diff
diff --git a/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java b/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
index 365f9a2b098..5780e3d4266 100644
--- a/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
+++ b/solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java
@@ -22,13 +22,24 @@ import static org.hamcrest.core.StringContains.containsStringIgnoringCase;
 
 import java.io.IOException;
 import java.io.InputStream;
+import java.io.UncheckedIOException;
 import java.nio.charset.StandardCharsets;
+import java.nio.channels.Selector;
+import java.nio.file.Files;
+import java.nio.file.Path;
+import java.nio.file.StandardOpenOption;
 import java.util.Base64;
+import java.util.Map;
 import java.util.UUID;
 import java.util.concurrent.CompletableFuture;
+import java.util.ArrayList;
+import java.util.List;
+import java.util.concurrent.CountDownLatch;
+import java.util.concurrent.Executor;
 import java.util.concurrent.ExecutionException;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Future;
+import java.util.concurrent.ThreadPoolExecutor;
 import java.util.concurrent.TimeUnit;
 import java.util.stream.Collectors;
 import java.util.stream.IntStream;
@@ -57,14 +68,18 @@ import org.apache.solr.common.params.ModifiableSolrParams;
 import org.apache.solr.common.params.SolrParams;
 import org.apache.solr.common.util.ExecutorUtil;
 import org.apache.solr.common.util.NamedList;
+import org.apache.solr.common.util.ObjectReleaseTracker;
 import org.apache.solr.common.util.SolrNamedThreadFactory;
 import org.apache.solr.common.util.TimeSource;
 import org.apache.solr.util.ServletFixtures;
 import org.apache.solr.util.ServletFixtures.DebugServlet;
 import org.apache.solr.util.ServletFixtures.SlowServlet;
 import org.apache.solr.util.TimeOut;
+import org.eclipse.jetty.client.HttpClientTransport;
 import org.eclipse.jetty.client.WWWAuthenticationProtocolHandler;
+import org.eclipse.jetty.client.transport.HttpClientTransportOverHTTP;
 import org.eclipse.jetty.http.HttpStatus;
+import org.eclipse.jetty.http2.client.transport.HttpClientTransportOverHTTP2;
 import org.eclipse.jetty.util.ssl.SslContextFactory;
 import org.junit.Test;
 
@@ -937,4 +952,466 @@ public class HttpJettySolrClientTest extends HttpSolrClientTestBase {
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
+
+  @Test
+  public void testExternalExecutorCloseProbeControl() throws Exception {
+    runExternalExecutorProbe("control", false);
+  }
+
+  @Test
+  public void testExternalExecutorCloseProbeInterrupted() throws Exception {
+    runExternalExecutorProbe("interrupted", true);
+  }
+
+  @SuppressWarnings("removal")
+  private void runExternalExecutorProbe(String arm, boolean interruptBeforeClose) throws Exception {
+    Path dir = Files.createTempDirectory("ext-probe-" + arm + "-");
+    Path evidence = dir.resolve(arm + ".log");
+    Files.createFile(evidence);
+    System.out.println("PROBE-EVIDENCE arm=" + arm + " " + evidence.toAbsolutePath());
+    ExtState st = new ExtState(arm, evidence);
+    st.snapshot("baseline-before-construction");
+
+    ExecutorService pool =
+        ExecutorUtil.newMDCAwareCachedThreadPool(new SolrNamedThreadFactory("probe-ext-" + arm));
+    st.pool = pool;
+    HttpJettySolrClient client =
+        new HttpJettySolrClient.Builder(solrTestRule.getBaseUrl() + "/")
+            .withConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
+            .withIdleTimeout(2000, TimeUnit.MILLISECONDS)
+            .withExecutor(pool)
+            .build();
+    st.client = client;
+    org.eclipse.jetty.client.HttpClient hc = client.getHttpClient();
+    st.httpClient = hc;
+    st.connector = connectorOf(hc.getTransport());
+    for (org.eclipse.jetty.io.SelectorManager sm : st.connector.getBeans(org.eclipse.jetty.io.SelectorManager.class)) {
+      st.selectorManagers.add(sm);
+      for (org.eclipse.jetty.io.ManagedSelector ms : sm.getBeans(org.eclipse.jetty.io.ManagedSelector.class)) {
+        st.managedSelectors.add(ms);
+        st.rawSelectors.add(ms.getSelector());
+      }
+    }
+    st.snapshot("after-construction");
+    if (st.managedSelectors.size() != 2 || st.rawSelectors.stream().anyMatch(s -> s == null || !s.isOpen())) {
+      st.log("SETUP FAILURE: expected two open saved selectors, found " + st.managedSelectors.size());
+      throw new IllegalStateException("external-executor probe setup: expected two open selectors");
+    }
+    if (hc.getExecutor() != pool) {
+      throw new IllegalStateException("external-executor probe setup: HttpClient executor is not the caller pool");
+    }
+    st.snapshot("pre-close");
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
+            "probe-ext-closer-" + arm);
+    st.closerThread = closer;
+    st.closeStart = System.nanoTime();
+    closer.start();
+
+    try {
+      if (!st.done.await(5, TimeUnit.SECONDS)) {
+        st.snapshot("active-at-5s");
+      }
+      if (!st.done.await(25, TimeUnit.SECONDS)) {
+        st.snapshot("cap-30s-close-still-active");
+        st.log("CONTAINMENT: original close still active at the 30 s cap. Halting this isolated test JVM. No rescue close runs.");
+        Runtime.getRuntime().halt(3);
+      }
+      st.snapshot("terminal-immediate");
+      Thread.sleep(1000);
+      st.snapshot("terminal-plus-1s");
+      try {
+        st.log("caller-executor sentinel result: " + pool.submit(() -> "alive").get(5, TimeUnit.SECONDS));
+      } catch (java.util.concurrent.RejectedExecutionException e) {
+        st.log("caller-executor sentinel REJECTED: " + e);
+      } catch (java.util.concurrent.TimeoutException e) {
+        st.log("caller-executor sentinel TIMED OUT after 5 s");
+      } catch (java.util.concurrent.ExecutionException e) {
+        st.log("caller-executor sentinel failed: " + e);
+      }
+    } finally {
+      if (st.done.getCount() == 0) {
+        st.snapshot("pre-cleanup-labelled");
+        for (Selector sel : st.rawSelectors) {
+          if (sel.isOpen()) {
+            st.log("CLEANUP: explicitly closing a saved raw selector that is still open");
+            sel.close();
+          }
+        }
+        Thread cleanup =
+            new Thread(
+                () -> {
+                  try {
+                    client.close();
+                    st.log("CLEANUP: serial client close returned normally");
+                  } catch (Throwable t) {
+                    st.log("CLEANUP: serial client close threw:");
+                    st.appendThrowable(t);
+                  }
+                },
+                "probe-ext-cleanup-" + arm);
+        cleanup.start();
+        cleanup.join(TimeUnit.SECONDS.toMillis(30));
+        if (cleanup.isAlive()) {
+          st.log("CONTAINMENT: cleanup still alive after 30 s. Halting this isolated test JVM.");
+          Runtime.getRuntime().halt(4);
+        }
+        pool.shutdownNow();
+        st.log("CLEANUP: caller pool shutdownNow, awaitTermination(5s)=" + pool.awaitTermination(5, TimeUnit.SECONDS));
+        st.snapshot("after-cleanup-labelled");
+      }
+    }
+  }
+
+  private static org.eclipse.jetty.io.ClientConnector connectorOf(HttpClientTransport t) {
+    if (t instanceof HttpClientTransportOverHTTP http1) {
+      return http1.getClientConnector();
+    }
+    return ((HttpClientTransportOverHTTP2) t).getHTTP2Client().getClientConnector();
+  }
+
+  private static final class ExtState {
+    final String arm;
+    final Path evidence;
+    final long t0 = System.nanoTime();
+    final CountDownLatch done = new CountDownLatch(1);
+    volatile HttpJettySolrClient client;
+    volatile org.eclipse.jetty.client.HttpClient httpClient;
+    volatile org.eclipse.jetty.io.ClientConnector connector;
+    volatile ExecutorService pool;
+    volatile Thread closerThread;
+    volatile long closeStart;
+    volatile boolean closerInterruptBeforeClose;
+    volatile boolean closerInterruptAtEnd;
+    volatile boolean returned;
+    volatile Throwable thrown;
+    final List<org.eclipse.jetty.io.SelectorManager> selectorManagers = new ArrayList<>();
+    final List<org.eclipse.jetty.io.ManagedSelector> managedSelectors = new ArrayList<>();
+    final List<Selector> rawSelectors = new ArrayList<>();
+
+    ExtState(String arm, Path evidence) {
+      this.arm = arm;
+      this.evidence = evidence;
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
+    void snapshot(String label) {
+      boolean terminal = done.getCount() == 0;
+      StringBuilder sb = new StringBuilder();
+      sb.append("=== SNAPSHOT ").append(label)
+          .append(" t=").append((System.nanoTime() - t0) / 1_000_000).append("ms arm=").append(arm).append('\n');
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
+        sb.append("client: identity=").append(System.identityHashCode(c))
+            .append(" trackedInObjectReleaseTracker=").append(ObjectReleaseTracker.OBJECTS.containsKey(c)).append('\n');
+      }
+      org.eclipse.jetty.client.HttpClient hc = httpClient;
+      if (hc != null) {
+        sb.append("httpClient: identity=").append(System.identityHashCode(hc))
+            .append(" started=").append(hc.isStarted()).append(" stopping=").append(hc.isStopping())
+            .append(" stopped=").append(hc.isStopped()).append(" failed=").append(hc.isFailed())
+            .append(" executorIsCallerPool=").append(hc.getExecutor() == pool).append('\n');
+      }
+      org.eclipse.jetty.io.ClientConnector cc = connector;
+      if (cc != null) {
+        sb.append("clientConnector: identity=").append(System.identityHashCode(cc))
+            .append(" started=").append(cc.isStarted()).append(" stopped=").append(cc.isStopped())
+            .append(" failed=").append(cc.isFailed()).append('\n');
+      }
+      for (org.eclipse.jetty.io.SelectorManager sm : selectorManagers) {
+        sb.append("selectorManager: identity=").append(System.identityHashCode(sm))
+            .append(" started=").append(sm.isStarted()).append(" stopped=").append(sm.isStopped()).append('\n');
+      }
+      for (int i = 0; i < managedSelectors.size(); i++) {
+        org.eclipse.jetty.io.ManagedSelector ms = managedSelectors.get(i);
+        Selector raw = rawSelectors.get(i);
+        sb.append("managedSelector[").append(i).append("]: identity=").append(System.identityHashCode(ms))
+            .append(" started=").append(ms.isStarted()).append(" stopped=").append(ms.isStopped())
+            .append(" failed=").append(ms.isFailed())
+            .append(" rawSelector identity=").append(System.identityHashCode(raw))
+            .append(" rawSelectorIsOpen=").append(raw.isOpen()).append('\n');
+      }
+      ExecutorService p = pool;
+      if (p instanceof ThreadPoolExecutor tpe) {
+        sb.append("callerPool: identity=").append(System.identityHashCode(p))
+            .append(" active=").append(tpe.getActiveCount()).append(" poolSize=").append(tpe.getPoolSize())
+            .append(" queued=").append(tpe.getQueue().size()).append(" completed=").append(tpe.getCompletedTaskCount())
+            .append(" shutdown=").append(tpe.isShutdown()).append(" terminated=").append(tpe.isTerminated()).append('\n');
+      }
+      sb.append("threads (all, no filter, full stacks):\n");
+      for (Map.Entry<Thread, StackTraceElement[]> e : Thread.getAllStackTraces().entrySet()) {
+        Thread t = e.getKey();
+        sb.append(" thread name=").append(t.getName()).append(" id=").append(t.getId())
+            .append(" identity=").append(System.identityHashCode(t)).append(" state=").append(t.getState())
+            .append(" daemon=").append(t.isDaemon()).append(" isProbeCloser=").append(t == ct).append('\n');
+        for (StackTraceElement f : e.getValue()) {
+          sb.append("     at ").append(f).append('\n');
+        }
+      }
+      append(sb.toString());
+    }
+
+    private static void appendChain(StringBuilder sb, Throwable t, String indent) {
+      for (Throwable c = t; c != null; c = c.getCause()) {
+        sb.append(indent).append(c).append('\n');
+        for (StackTraceElement f : c.getStackTrace()) {
+          sb.append(indent).append("   at ").append(f).append('\n');
+        }
+        for (Throwable x : c.getSuppressed()) {
+          sb.append(indent).append("suppressed:\n");
+          appendChain(sb, x, indent + "  ");
+        }
+        if (c.getCause() == c) {
+          break;
+        }
+      }
+    }
+  }
 }
```
