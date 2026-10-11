# Post-PR review round 2, part s2: final whole-body read of SOLR-16630 (apache/solr PR 5106)

Slice verdict: 3 findings (F1 to F3). Every number, date, SHA and citation in the live body matches the receipt, the head, the base commit or the changelog fragment. The three findings are wording and citation items: one overstated possibility, one claim whose source the receipt does not carry, and one citation whose text does not name the base commit.

## Live PR and head

- PR 5106 on apache/solr: OPEN, draft (`isDraft: true`), head `nick-boss-tech:solr-16630-submit`, base `main` at `d76fc14b9857e77b830d7a82514b28c523a239c2`.
- Live head: `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7`. Matches the expected head. No difference.
- Live title: "SOLR-16630: TestCoordinatorRole.testNRTRestart waits for the first add to complete before stopping the PULL node".
- Changelog at the head (`changelog/unreleased/SOLR-16630.yml`, 10 lines): title "TestCoordinatorRole.testNRTRestart waits for the first add to complete before stopping the PULL node". This matches the PR title apart from the "SOLR-16630: " prefix. Type `fixed`, author Nick Shanin, link to SOLR-16630. The title has no unquoted ": ".
- Both SHAs (`9c77a4db`, `3f5d4c5b`) are present in the local object store. No fetch was needed.
- Commits from base to head: `9bea59741ac` (keep PULL node up until the NRT-restart add succeeds) and `9c77a4db09d` (correct the changelog title). `git diff --stat` base to head: changelog (10 lines added) and `TestCoordinatorRole.java` (6 lines added). Test-only plus changelog, as the body says.
- Worktree HEAD is detached at `86684300e5d8567abca8be4055e36699d2773575`, the SHA the assignment names. No local ref named `pr-prepare` exists in this worktree.

## Receipt comparison (`receipts/SOLR-16630.md`), line by line

| Item | Receipt (line) | Live body | Result |
|---|---|---|---|
| Live head | `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7` (5) | Head SHA in every head link | Match |
| Gated head | `9bea59741ac30ffd11ed11be5269ea025cf17f68` (4) | "Those runs were at `9bea59741ac30ffd11ed11be5269ea025cf17f68`" | Match (full SHA) |
| Merge-base | `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (6) | Base links use the same full SHA | Match |
| Gate finish date | 2026-10-10 UTC (3, 8) | "passed six runs on 2026-10-10 (UTC)" | Match at day level. The receipt has no per-run dates. |
| Base run date | 2026-10-11 UTC (6) | "one run on 2026-10-11 (UTC) failed" | Match |
| Base run count | Job stopped after run 1, which FAILED (6) | "failed in its one run" | Match |
| Base failure | `SolrServerException` caused by `ClosedChannelException` at `TestCoordinatorRole.java:306`; PULL jetty stopped; no successful add in the loop (6) | "the add threw a `SolrServerException` caused by a `ClosedChannelException`, after the test had stopped the PULL replica and before the add loop's first add had succeeded" | Match |
| Focused runs | CI seed plus five random seeds; each 1 test, 0 failures, 0 errors, rc=0 (9) | "passed six runs ... Each run: 1 test, 0 failures, 0 errors" | Match (rc not stated in the body, which is fine) |
| Commits past gated head | Changelog title only; no code changed (5) | "the only commit since corrects the changelog title, and no code changed" | Match (git log shows only `9c77a4db`) |
| Change shape | Test-only; changelog added; no production change (7) | "This is a test-only change." / "touches only the test file and a changelog entry" | Match (diff stat: 2 files) |
| Changelog path | `changelog/unreleased/SOLR-16630.yml` (7) | Same path in the changelog link | Match |
| Environment attempt | First attempt on NFS not counted (10) | Not in the body; only the six counted runs are claimed | Correct per the receipt |
| Gate steps (tidy, Error Prone, module check) | (8) | Not claimed in the body | Not required |

The 2-minute bound in the body is not in the receipt. It is in the code at head L262 (`addDone.await(2, TimeUnit.MINUTES)`), so it is supported by the code.

## Citations (13 links): SHA, lines, what the lines hold, result

Head SHA `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7` (code the change produces):

| Link | Lines at head | Content at those lines | Result |
|---|---|---|---|
| `TestCoordinatorRole.java#L243-L243` | 243 | `CountDownLatch addDone = new CountDownLatch(1);` | OK |
| `TestCoordinatorRole.java#L262-L264` | 262 to 264 | `if (!addDone.await(2, TimeUnit.MINUTES)) {`, `log.warn("NRT add did not succeed in time; stopping PULL anyway");`, `}` | OK |
| `TestCoordinatorRole.java#L311-L313` | 311 to 313 | `client.add(COLL, d);`, `client.commit(COLL);`, `addDone.countDown();` | OK |
| `changelog/unreleased/SOLR-16630.yml#L1-L10` | 1 to 10 | Whole file (10 lines) | OK |

Base SHA `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (pre-change symptoms, base lines; the first paragraph of "What happens today" says so):

| Link | Lines at base | Content at those lines | Result |
|---|---|---|---|
| `TestCoordinatorRole.java#L221-L221` | 221 | `SolrClient client = pullJetty.getSolrClient();` | OK |
| `TestCoordinatorRole.java#L306-L306` | 306 | `client.add(COLL, d);` | OK |
| `TestCoordinatorRole.java#L241-L241` | 241 | `final long pullServiceTimeMs = 1000 + (long) r.nextInt(9000);` | OK |
| `TestCoordinatorRole.java#L259-L261` | 259 to 261 | `Thread.sleep(pullServiceTimeMs);`, log line "stopping PULL jetty ...", `pullJettyF.stop();` | OK |
| `TestCoordinatorRole.java#L309-L309` (paragraph 2) | 309 | `} catch (SolrException ex) {` | OK |
| `SolrServerException.java#L24-L24` | 24 | `public class SolrServerException extends Exception {` | OK |
| `JettySolrRunner.java#L616-L617` | 616 to 617 | `IOUtils.closeQuietly(jettySolrClient);`, `jettySolrClient = null;` in `stop()`, before the proxy and thread-pool steps | OK |
| `JettySolrRunner.java#L847-L852` | 847 to 852 | `getSolrClient()` returns the cached `jettySolrClient`, creating it only when null | OK |
| `TestCoordinatorRole.java#L309-L309` (Limits bullet) | 309 | Same catch line | Content OK; the text does not say base (see F3) |

Line-number cross-check: the diff adds 6 lines (CountDownLatch import, latch declaration, 3-line await block, `countDown()`). Base 306 maps to head 311, and base 221 maps to head 222. The base lines and head lines in the links are consistent with that shift.

Base SHA links: 9 (4 in paragraph 1, 2 in paragraph 2, 2 in paragraph 3, 1 in Limits). Head SHA links: 4. Total 13.

## Other checks

- Title and changelog: match (see Live PR and head).
- Em dashes (U+2014) in the live body: 0 (counted with `gh pr view --jq`).
- Lucene: not mentioned in the body. No Lucene version is claimed, so the rule does not apply.
- AI header (first line) and "AI assistance" footer: match the `pr-formula.md` template exactly.
- JIRA link line: present (`https://issues.apache.org/jira/browse/SOLR-16630`).
- Bold one-line summary: present at the start of all four sections.
- Internal process vocabulary in the live body: none of gate, receipt, ledger, seed, run identifier, claim, pool, assignment, subagent, submission, round, worktree, Muse, vm. "runs" is used in its ordinary sense.
- Length: raw body 5,493 characters, most of it link URLs. Round 1 counted about 3,200 characters of prose before the wording fixes; those fixes added roughly 300 characters. Prose is therefore near the guide's roughly 3,500. Not a finding.
- "A choice to check" section: absent. The stop-timing cost (round 1 N6) is stated under "Behavior change". Not a finding.

## Findings

### F1. "can go stale" states an unlikely cause as a fact (Limits, first bullet)

- Draft words: "A pooled connection from the test client to the PULL replica can go stale with no stop at all. This change does not address that."
- Evidence: the root-cause record (`reports/flaky-tests-root-cause-round-1-t3.md`, H2) rates this at about 10 percent. Evidence against it: PULL is not restarted before line 306, its server idle timeout is 260 s, and the gap since the client's last use is seconds. The receipt does not mention it. "can" presents a possibility as a fact.
- Replacement text (first sentence of the bullet):

  A pooled connection from the test client to the PULL replica could go stale with no stop at all. This change does not address that.

### F2. "In a failing CI run" is not in the receipt (What happens today, paragraph 1, second sentence)

- Draft words: "In a failing CI run, `client.add` threw a `SolrServerException` whose cause was a `ClosedChannelException`."
- Evidence: the receipt records only the base run on vm1 (2026-10-11 UTC). The CI failure is in the root-cause record (`reports/flaky-tests-root-cause-round-1-t3.md`, test 3): workflow run 38009158573, triggered by a SOLR-11475 branch, with the same `SolrServerException` / `ClosedChannelException` shape at `TestCoordinatorRole.java:306`. The assignment (`assignments/pool-solr-16630-testcoordinatorrole-fix.md`) dates it 2026-10-10 and calls it an apache/solr CI run. The claim is supported by the record. A reader of the body cannot check it, because the body names no run. The run page was not opened in this slice (read-only scope).
- Replacement text (use only after the main side opens the run page and confirms the exception):

  In a failing apache/solr CI run on 2026-10-10, `client.add` threw a `SolrServerException` whose cause was a `ClosedChannelException`.

  If the run page does not show that exception, replace the first clause with "In a failing run,". The receipt supports that wording for the base run.

### F3. Limits citation links the base commit, but the sentence does not say so (Limits, second bullet)

- Draft words: "The add loop still catches only `SolrException` ([test](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L309-L309)). Widening that catch was not done. On request, a follow-up ticket and pull request can cover it."
- Rule: `pr-formula.md`, Presentation rule: a citation to the pre-change code links the merge-base commit, and the text says so. The link is correct (base L309 is the catch). But "still" reads as the current state, and the sentence does not name the base. The same catch is at head L315, so "still" is true at head as well. Round 1 F1 asked for the base link here; the base link stays, and the text has to say so.
- Replacement text (whole bullet):

  - The add loop catches only `SolrException` at the base commit ([test](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L309-L309)). This change does not widen that catch. On request, a follow-up ticket and pull request can cover it.

  Alternative if the main side prefers head links for current-state citations: link `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L315-L315` at `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7` and keep "still". Either way, the link and the sentence must agree.

## Optional notes (not findings)

- N1 (carried from round 1 N1): "Behavior change: if no add ever succeeds, the test reaches its failure up to two minutes later than before." The code confirms the stop waits at most two minutes (head L262), and the failure path matches the base run. Plainer, exact option: "Behavior change: if no add succeeds within two minutes, the PULL replica stops up to two minutes later than before."
- N2: The title and changelog say "PULL node"; the body says "PULL replica". Same thing. Optional to use one term in the body.
- N3: "The failure is intermittent." appears in "What happens today" and again in Limits. The presentation rule asks that a claim be stated once. Optional to drop the repeat.
- N4: "This is a test-only change." (What this change does, summary) and "The diff touches only the test file and a changelog entry" repeat the same claim in one section. Optional.
- N5: "with the recorded failure" (Proof headline). "recorded" is vague to a reader; the detail follows in the body. Optional: "with the failure described below".

## Not done

- No edits to the draft, the receipt, the claim, the assignment, the hosts file or any branch. No commit, push, PR comment, review, edit, close or Jira write. No builds, Gradle, tests, gate runs or queue commands.
- `claims/pool-flaky-16630-post-pr-review-round-2.md` not marked DONE. This is read-only work, and the claim is marked with the deliverable push by the main side.
- CI run 38009158573 was not opened (no `run` or `api` call; read-only list is `pr list` and `pr view`).
- Jira was not read. CI check status on PR 5106 was not read.
- The live body was compared with `pr-drafts/flaky-fixes/SOLR-16630.md` by reading, not by byte hash. No differences were seen.
- The Lucene check was done by reading the body; a jq filter failed on quoting, and the body text contains no "Lucene".
- `research\gh.ps1` was called by its absolute path, because the relative path does not resolve from the worktree.
- `reports/flaky-16630-post-pr-review-round-2-s1.md` was read for context and for the N-note overlap only. Its results were not re-verified here.
