# Interrupted client-close source review, round 1

Status: DONE; source review complete, new ownership probe pending Linux
Host: windows
Claim: claims/teardown-client-close-source-review-round-1.md
Decision ladder: rung 1, source/history and preserved evidence; one new bounded premise unit
Board snapshot reviewed: 6555d95ba30b69df38d6d59c0559f7a8ce853b41
Owner direction: "You can do that review and hand over any other runs that need testing".

## Decision

The corrected pair establishes interrupted shutdown failure with a retained
tracker entry in the client-owned executor case. It does not establish a live
resource leak or reproduction of the original CI schedule. Preserve its budget
violation and lost first control as limitations; do not repeat that pair.

One materially different input needs Linux measurement: an owned HttpClient with
a caller-owned executor. Solr omits its executor shutdown fallback in that case.
Measure saved NIO selector objects directly, alongside threads and lifecycle
states. The separate handoff is
`assignments/teardown-client-close-external-executor-round-1.md`.

No production patch is proposed as validated. Moving tracker release into finally
would hide incomplete resource cleanup. A general retry of Jetty stop after its
exception is also not justified by this code.

## Revisions and evidence

Solr source M: **d298cd0b42602247bee7bf9e2bcf302e725f6690**. Fetched upstream/main
still named M during review. Read Git objects, not the dirty Windows checkout.
Declared Jetty: **12.1.14**, `gradle/libs.versions.toml:84`. The official annotated
`jetty-12.1.14` tag resolves to J:
**15cecbd757a2f6b0fdc6a8944f4c6d0020b17da0**. Dependency source was read at that
tag; the new probe must also report the actual runtime JAR/code source.

Inputs: round-2 result at commit
`3bcf49da9c8136c3cd359de75d40b91e7edb9f1b`, v3.1 scaffold and its cumulative diff
SHA-256 `dee219511e648f566c73893b15542e4f76198ff4a8b090c8bad19d1e4f325c05`,
and `reports/teardown-client-interrupt-probe-round-2-result-correction.md`.
Only the interrupted arm set the interrupt. The correction confirms all driver
chains stopped. Three filtered invocations followed the failed v3 setup, exceeding
the two authorized; the first control's evidence was overwritten.

Actual raw paths are **board-evidence/r2/**, not the `board-evidence-r2/` spelling
in the result/correction. Hashes verified from Git object bytes:

| Raw record | Bytes | SHA-256 |
| --- | ---: | --- |
| board-evidence/r2/control.evidence.log | 185626 | 0b99f5eec4e16d48e062bb693d20fc3f18b28d5830eadb01737e3237b64fa2eb |
| board-evidence/r2/interrupted.evidence.log | 284739 | 23ce17c3a8d529995f7aac1af9fd9ee40e551ba69d8eecd55c5204bbbd601a25 |

## What the preserved pair shows

| Boundary | Control | Interrupted |
| --- | --- | --- |
| Original close outcome | Returned, terminal snapshot t=53 ms | Threw, terminal snapshot t=60048 ms |
| Tracker after original close | Released | Retained |
| Owned executor | Terminated | Terminated, active count zero |
| HttpClient | STOPPED | FAILED, not stopped |
| Attributed selector worker | Gone | Present at 5/30 s; gone at terminal capture |
| Closer interrupt | Not set | Set before close; false at completion |

Times are relative to the scaffold's evidence origin, not isolated close-duration
benchmarks. The interrupted cause is InterruptedException in
ManagedSelector.doStop:139. A later serial, noninterrupted cleanup close returned
and removed the tracker entry. Its success does not alter the original outcome.
The snapshots do not record raw NIO Selector.isOpen(), so thread disappearance
alone cannot prove all selectors closed. Both test XML passes are scaffold
execution outcomes, not a production regression gate.

## Solr ownership and interruption contracts

At M, [HttpJettySolrClient.java](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L347)
has these paths:

- Constructor lines 136-179 distinguishes borrowed HttpClient
  (`closeClient=false`) from a newly created owned HttpClient.
- Lines 220-225 sets `shutdownExecutor=true` only when the builder supplied no
  executor. A caller executor makes it false. Lines 292/314 install it and start
  the HttpClient.
- Close lines 347-370 first drains asynchronous requests. Inside its try it
  stops/destroys an owned HttpClient, then stops the separate keystore scanner.
  An exception skips the remaining operations and tracker release, and is wrapped
  in RuntimeException. The finally shuts down only a client-owned executor.
- AsyncTracker lines 949-960 uses an interruptible phaser wait, force-terminates
  on interruption and restores the flag. This is intentional shutdown behavior;
  clearing interruption across the entire close/drain would change that contract.
- Builder lines 1159-1172 borrows a parent's HttpClient/executor. Such a wrapper
  must not stop the parent's resources. The request wrapper's no-op close at
  875 is another ownership boundary.

[ExecutorUtil.java:112-157](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj/src/java/org/apache/solr/common/util/ExecutorUtil.java#L112)
permits a 60-second wait, shutdownNow, and another wait. The interrupted arm's
timing and disappearing worker are consistent with this fallback, but the raw
probe did not intercept the shutdownNow call. Keep that mechanism an inference.

The alternative ownership occurs in production:
[HttpShardHandlerFactory.java:279-316](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java#L279)
passes `commExecutor` to the builder. Its own close, lines 345-357, subsequently
shuts that executor down in finally. Therefore this review predicts an untested
client boundary, **not a persistent factory leak**. The follow-up keeps the
caller executor alive until evidence capture to isolate that boundary.

History: close's stop/scanner/catch/finally/release shape is present in
`9f21a71bbd89fa61821850b64963062cc21256f8` (SOLR-16743); the preceding stop/catch
shape is present in `96ddf2c4c7cd52181ae51e1c966eb3e3bfef5c02` (SOLR-15955).
The interruptible async drain was introduced by `3792f2dc670` (SOLR-18174) to
unblock pending calls during shutdownNow. This is not evidence of a new regression
at M or of attribution to either original teardown CI failure.

## Jetty stop path at J

[ManagedSelector.java:130-148](https://github.com/jetty/jetty.project/blob/15cecbd757a2f6b0fdc6a8944f4c6d0020b17da0/jetty-core/jetty-io/src/main/java/org/eclipse/jetty/io/ManagedSelector.java#L130)
sets `_started` false, submits CloseConnections, then waits at line 139. It only
submits StopSelector after that wait. The observed exception therefore bypasses
that submission. StopSelector's operation at lines 1043-1070 closes the raw
selector. The selector loop also has an interrupt/failed-lifecycle exit path at
196 and a selector-close exception path around 651-669. This explains why forced
executor interruption is a plausible rescue for the owned case. Since `_started`
has already changed, calling doStop again is not a general retry of the missed
operation.

[AbstractLifeCycle.java:114-143](https://github.com/jetty/jetty.project/blob/15cecbd757a2f6b0fdc6a8944f4c6d0020b17da0/jetty-core/jetty-util/src/main/java/org/eclipse/jetty/util/component/AbstractLifeCycle.java#L114)
marks a thrown stop FAILED and rethrows. Although a FAILED lifecycle can receive
another stop, that does not ensure it retains the same children.

[ContainerLifeCycle.java:188-212](https://github.com/jetty/jetty.project/blob/15cecbd757a2f6b0fdc6a8944f4c6d0020b17da0/jetty-core/jetty-util/src/main/java/org/eclipse/jetty/util/component/ContainerLifeCycle.java#L188)
attempts other managed children after a child failure, then throws accumulated
failure. [SelectorManager.java:302-319](https://github.com/jetty/jetty.project/blob/15cecbd757a2f6b0fdc6a8944f4c6d0020b17da0/jetty-core/jetty-io/src/main/java/org/eclipse/jetty/io/SelectorManager.java#L302)
removes its ManagedSelector beans and clears its selector array in finally even
when stop fails. An empty post-stop bean list is consequently not evidence that
the underlying selectors closed. Capture both managed and raw selector references
before shutdown. Public getBeans/getSelector access suffices; no production hook
is needed.

## Implementation direction after the deciding probe

A possible narrowly scoped design is to preserve a pre-existing interrupt after
the existing async drain, shield owned-resource cleanup from that pre-existing
flag, and restore the flag in finally. This is a hypothesis, not a tested fix,
and does not handle an interrupt arriving during stop. Do not mask interrupts
across the asynchronous drain or claim that catching/retrying stop fixes all
interruption windows.

Any eventual patch needs explicit ownership and failure handling: borrowed
HttpClient stays usable; caller executor stays usable; owned resources and a
configured keystore scanner get appropriate cleanup; primary/suppressed failures
and interrupt state remain meaningful. Scanner survival is an unmeasured source
possibility here, not a finding from the non-TLS probe. Test those contracts when
there is a concrete patch. No dependency bump, public issue or broad suite is
justified by this review alone.

Windows validation: static source/history review and raw evidence hash checking
only. No build, test, local queue, source patch, public PR/Jira write or additional
Linux run was performed by this review. The one new Linux pair is handed off
separately; earlier DistributedExpand stand-down remains in force.
