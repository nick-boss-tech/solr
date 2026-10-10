# SOLR-16630 slice 4 audit (s4a)

Reviewer: windows review agent, pool subagent for slice 4a.
Date: 2026-10-10 (UTC).
Head audited: 9bea59741ac30ffd11ed11be5269ea025cf17f68 (branch solr-16630-submit).
Base: upstream main 3f5d4c5bf8ac.

Checks run: `git ls-remote origin refs/heads/solr-16630-submit` returned 9bea59741ac30ffd11ed11be5269ea025cf17f68, which matches the named head. The head object was already present locally, so no fetch was run. Git was read-only throughout. No builds, tests, Gradle, Selenium, gate, or test-queue runs. No PR, comment, Jira, branch, or claim writes.

Line numbers below are for solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java at the head, unless a path is given. "Base" means the same file at 3f5d4c5bf8ac.

## 1. Verdict

**HOLD the proof claim until owner decision O1 is made.** The code change is sound and implements the primary fix from the t3 report. The gap is evidence, not code. No run of this test on base code is on record, so the draft cannot say the test fails without the change. pr-formula.md section 3 and its rule "if no proof was ever run, run the proof step before rewriting the description" apply. Once O1 is decided, the draft can go ahead. The code needs no change for that, though the changelog title (Q6) and two comments (Q3) could be tidied.

## 2. Diff scope (Q5)

Diff 3f5d4c5bf8ac...9bea59741ac30ffd11ed11be5269ea025cf17f68: 2 files, 16 insertions, 0 deletions.

- changelog/unreleased/SOLR-16630.yml (new, 10 lines).
- solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java (+6 lines).

Findings:
- No production change.
- No debug code, System.out, printStackTrace, TODO, or FIXME in the added lines.
- No other test changed.
- The new import `java.util.concurrent.CountDownLatch` (line 30) is used at line 243. `TimeUnit` (line 33) was already imported and is used at lines 262 and 373 to 375.
- No em dash, en dash, or other non-ASCII bytes in either file. No trailing whitespace. No added line over 100 characters.
- The secondary catch-widening from t3 Part 3 is not applied. The gate file (gates/SOLR-16630.md line 4) says so, and that is the safer choice (O6).

## 3. Findings by question

### Q1. Match to t3 mechanism H1

The change implements t3 Part 3, steps (a), (b), and (c), as written:

- (a) Latch declared before the executor submit: line 243 (`CountDownLatch addDone = new CountDownLatch(1);`), submit at lines 244 to 245.
- (b) Await after the fixed sleep and before the stop: sleep at line 261, `addDone.await(2, TimeUnit.MINUTES)` at lines 262 to 264 (the warn text matches t3 exactly), "stopping PULL jetty" log at line 265, `pullJettyF.stop()` at line 266. The stop itself is unchanged.
- (c) Count down after a successful add and commit, before break: `client.add` at 311, `client.commit` at 312, `addDone.countDown()` at 313, `break` at 314.

After the countdown, the PULL client is not used again. The add loop breaks at 314, the query loops (338 to 350) use qaJetty, and the manipulation future is awaited at 360. So the stop cannot cut a test add.

Pass and fail comparison, read from code:
- Base fails when the stop (base line 261) comes before the first successful add. The client is then closed and the next add (base line 306) throws.
- Head fails only when no add succeeds within the two-minute wait. Base also fails in that case.
- I found no path where base passes and head fails. Head changes timing only: the stop is later, and a failure takes up to two minutes longer.

### Q2. Liveness

- The latch wait has a timeout of two minutes (line 262). It starts after the fixed sleep, so the worst case is `pullServiceTimeMs` plus two minutes after the NRT restart.
- On timeout, the code logs a WARN at line 263 ("NRT add did not succeed in time; stopping PULL anyway") and then stops PULL at line 266. The stop is unconditional, so the wait cannot hang the test.
- The add loop at lines 309 to 321 has no bound. Base has the same loop shape (base lines 304 to 313), so this is pre-existing. It catches only `SolrException` (line 315).
- After the stop, the add failure is a transport error. The Jetty client maps IOException and IllegalStateException to `SolrServerException` (base `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java`, lines 479 to 525). It rethrows other RuntimeExceptions unchanged (line 526). `SolrServerException` extends `Exception` (base solrj `SolrServerException.java` line 24), and `SolrException` extends `RuntimeException` (base solrj `SolrException.java` line 28). So neither is caught at line 315. The loop escapes at line 311 and the test errors. I traced no path where the loop spins after the stop. This is from reading the code, not from a run.
- The failure in that case is an exception from the transport layer, not an assertion. The only test-side text is the WARN in the manipulation thread's log (line 263). See O5.
- Early exit: if the main thread fails before any add succeeds (the routing assertion at lines 297 to 300, or any exception in phase 1), the `finally` at lines 446 to 448 calls `ExecutorUtil.shutdownAndAwaitTermination(executor)`. That waits 60 seconds (base `solrj` ExecutorUtil.java lines 134 to 150) before forcing `shutdownNow`, because the manipulation thread is parked on the two-minute await. The failure report is delayed by about 60 seconds. In that path PULL is not stopped, and `cluster.shutdown()` still runs. Base stopped PULL on time there. This is not a hang.

### Q3. Test purpose and order of events

Unchanged, and still asserted:
- Routing goes to PULL while NRT is down: lines 297 to 300.
- Adds through PULL resume after the NRT restart: add loop, lines 309 to 321.
- Routing returns to NRT: lines 338 to 358.
- The coordinator path through qaJetty: lines 189 to 190, 226 to 232, 276 to 285, 338 to 350.

The latch adds no assertion. It only orders the stop after the first add. It enforces the order that the comment at lines 329 to 331 describes: NRT back, forwarded write, index and commit complete, then PULL stopped.

Comments that now lag the code (wording only; O4):
- Lines 239 to 241, the NOTE on `pullServiceTimeMs`: that value is now a minimum delay before the stop, not the stop time.
- Lines 256 to 260: "stopping the PULL replica after a brief delay" does not mention the wait for the first add.

### Q4. H1 and H2 coverage

See section 4 for the full statement.

### Q5. Scope

See section 2.

### Q6. Changelog (`changelog/unreleased/SOLR-16630.yml`)

- Parses by eye: a folded `title: >` scalar, plain keys, a list for `authors` and `links`, and no colon-space inside the title. The URL in `links` is a plain scalar, and its colons are not followed by a space.
- `type: fixed` is plain. The changelog guide (base `dev-docs/changelog.adoc`, lines 46 and 71 to 72) defines `fixed` as "improvements that are deemed to have fixed buggy behavior". It defines `other` (lines 87 to 89) as anything else, including "test infrastructure". Base already uses `fixed` for a test refactor (PR#4419). So `fixed` is defensible. O3 is a minor owner call.
- `authors: Nick Shanin` matches the ICLA name in AGENTS.md. No placeholder ("ICLA pending", "Solr Issues Workspace") is present.
- Title accuracy: "TestCoordinatorRole.testNRTRestart no longer stops the PULL node while an add through it is still in flight" overstates in two ways. First, the stop is held until the first add succeeds, which is stronger than "while in flight". Second, after the two-minute fallback the stop can happen with no add having succeeded, so "no longer stops" is not absolute. A more accurate title, if the owner wants a change (this needs a branch edit by the implementation pool): "TestCoordinatorRole.testNRTRestart keeps the PULL node up until its first add succeeds, with a two-minute cap".

### Q7. Fail-before

- **No fail-before result exists, and I have not invented one.** The receipt (receipts/SOLR-16630.md, line 7) lists six runs at the head, each 1 test, 0 failures, 0 errors, rc=0. The gate checklist (gates/SOLR-16630.md, lines 13 to 22) has no base run and no fail-before step. A search of the pool folders found no base-code run for this ticket.
- Read from the code: base line 261 stops PULL at restart plus `pullServiceTimeMs` (1 to 10 seconds, seeded), whatever the adds are doing. If the first successful add lands after that stop, base fails at line 306. If it lands earlier, base passes. The ordering depends on wall-clock timing, not on the seed. So the code does not guarantee a base failure. The change would fail on base at that point only in the late-add case.
- A single base pass would not show the test cannot fail. Showing a base failure might take several runs, and might never happen.
- The six head passes do not show that the change fixes the flake, because the seed fixes only the sleep values (line 242), not the add timing.

## 4. H1 and H2 coverage statement

- **H1 (PULL stopped on a fixed timer while the add loop still needs it): covered by the change.** The stop is now gated on the first successful add, or two minutes, whichever comes first. The coverage is only as strong as H1 itself. t3 gives H1 about 70 percent confidence. I found no recorded run that confirms it.
- **H2 (pooled HTTP/2 connection to PULL goes stale, with no stop): not covered.** The change does not touch the client (lines 221 to 222), the HTTP client pool, the idle timeouts, the retry behavior, or the catch at line 315. The base Jetty client already maps a write on a closed pooled connection to `SolrServerException` (base lines 479 to 488), which escapes the catch at 315. So under H2 the test would still fail at line 311. The six head runs do not rule out H2, because those runs did not force a stale connection.
- **H3 (stale session to NRT): not affected.** t3 puts it under 5 percent.

## 5. Owner decisions (flagged, not taken)

- **O1 (decides the draft's proof claim):** a base-code run of `TestCoordinatorRole.testNRTRestart` at 3f5d4c5bf8ac with CI seed 681E2A715B2CE1D3, run on the gate host (vm2), because builds are barred on this Windows host. Repeat until the owner is satisfied. Or decide to publish with the statement the draft already makes: "not shown to fail without the change". pr-formula.md says to run the proof first.
- **O2 (read-only log check on vm2):** in each of the six gate logs, confirm that no "NRT add did not succeed in time" WARN appears, and that "successfully added another doc" comes before "stopping PULL jetty". t3 Tree B said a pass counts only if that order holds. The receipt does not record it.
- **O3 (changelog):** the title wording (Q6), and `fixed` versus `other`.
- **O4 (comments, optional):** update the comments at lines 239 to 241 and 256 to 260 to match the new wait.
- **O5 (failure shape):** keep the two-minute fallback as a WARN plus stop (current), or make it an explicit `assertTrue` with a clear message. The explicit assertion would fail at two minutes and would not stop PULL. The current design fails with a transport exception.
- **O6 (catch widening):** keep the catch at line 315 narrow (current). If it were widened to `SolrServerException` alone, then after the fallback stop the loop would retry against a closed client until the test timeout. t3 says the same.
- **O7 (H2):** H2 is not addressed. The draft already says so (draft line 37). The owner decides whether to accept that risk or investigate H2 separately.
- **O8 (assignment wording):** the assignment's "Added slice 4" text (assignments/pool-flaky-fix-review-round-1.md, line 19) describes the t3 mechanism as "the coordinator-endpoint race: the test read cluster state before the coordinator role finished registering." The t3 report does not say that. Its H1 is the PULL stop, and t3 (lines 78 to 79) calls the assignment's coordinator-endpoint wording likely wrong. I audited against t3, as asked. The assignment text should be corrected, or the owner should confirm t3 as the basis. The existing draft follows t3.

## 6. Not checked

- Nothing was run: no builds, tests, Gradle, Selenium, gate, or queue.
- The vm2 gate logs (not visible from this host). Jetty source (not in the repo).
- Jira status of SOLR-16630 (no Jira access used). The draft's "reopened ticket" framing is taken from the assignment.
- The t3 Tree A settling run (queued on the main side). I found no recorded result in the pool.
- reports/flaky-fix-review-round-1-s4b.md (a sibling slice file) was not read, to keep this audit independent.
- The existing draft at pr-drafts/flaky-fixes/SOLR-16630.md was read only, to check its link lines against the head. Its links to lines 222, 242, 261 to 266, 311 to 313, 315, SolrServerException line 24, and JettySolrRunner lines 616 to 617 and 847 to 852 are correct. Its statements agree with this audit. Its Limits list does not mention the 60-second teardown delay or the transport-exception failure shape (optional).
- I did not create or edit the slice 4 claim file, and I did not write the draft.
