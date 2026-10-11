# Post-PR review round 3, SOLR-16630: confirmation

Assignment: `assignments/pool-flaky-16630-post-pr-review-round-3.md`. Claim: `claims/pool-flaky-16630-post-pr-review-round-3.md` (ACTIVE, not marked DONE; see Not done). Date: 2026-10-11. Read-only throughout.

## Verdict

**SATISFIED.** Open items: 0. Two optional wording notes (O1, O2) are not blocking. The live head is unchanged, all three round 2 findings are fixed, and no new blocking item was found.

## Live PR and head

- PR 5106 on apache/solr: OPEN, draft (`isDraft: true`), head `nick-boss-tech:solr-16630-submit` at `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7`. This is the expected head, so it has not moved.
- Base ref: `main` at `d76fc14b9857e77b830d7a82514b28c523a239c2`. That tip is not in the local object store (see Not done).
- Live title: "SOLR-16630: TestCoordinatorRole.testNRTRestart waits for the first add to complete before stopping the PULL node".
- Changelog at the head (`changelog/unreleased/SOLR-16630.yml`, 10 lines): title "TestCoordinatorRole.testNRTRestart waits for the first add to complete before stopping the PULL node", type `fixed`, author Nick Shanin, link to SOLR-16630. The PR title is the changelog title with the "SOLR-16630: " prefix. Match.
- Live body against `pr-drafts/flaky-fixes/SOLR-16630.md` (worktree commit `9f939646c9b`): compared line by line with the body read through `gh pr view`, decoded as UTF-8. Identical. The only difference is one trailing empty line added by the output pipe.
- Commits: `3f5d4c5bf8` to head is two commits, `9bea59741ac` (keep PULL node up until the NRT-restart add succeeds) and `9c77a4db09d` (correct the changelog title). `9bea59741ac` to head changes only the changelog title line.
- Diff `3f5d4c5bf8` to head: two files, 16 insertions. Changelog 10 lines. `TestCoordinatorRole.java` 6 lines (CountDownLatch import, latch declaration, 3-line await block, `countDown()`). No product code.

## Round 2 findings

| Finding | Status | Evidence in the live body |
|---|---|---|
| (a) Limits says "could go stale", not "can go stale" | FIXED | Limits bullet 1: "A pooled connection from the test client to the PULL replica could go stale with no stop at all." |
| (b) Opening says "In a failing run," with no CI attribution | FIXED | Opening: "In a failing run, `client.add` threw a `SolrServerException`...". The live body has no "CI", "Actions", "workflow" or run number. |
| (c) Limits bullet: "at the base commit", links base L309, says this change does not widen the catch | FIXED | "The add loop catches only `SolrException` at the base commit ([test](...3f5d4c5bf8...TestCoordinatorRole.java#L309-L309)). This change does not widen that catch. On request, a follow-up ticket and pull request can cover it." |

## Receipt comparison (`receipts/SOLR-16630.md`, gate file `gates/SOLR-16630.md`)

| Item | Receipt or gate file | Live body | Result |
|---|---|---|---|
| Live head | `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7` (line 5) | Head links and changelog link | Match |
| Gated head | `9bea59741ac30ffd11ed11be5269ea025cf17f68` (line 4) | "Those runs were at `9bea59741ac30ffd11ed11be5269ea025cf17f68`" | Match |
| Merge-base | `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (line 6) | Base links and "the commit this branch starts from" | Match |
| Base run | One run, FAILED, on 2026-10-11 UTC (line 6) | "one run on 2026-10-11 (UTC) failed" | Match |
| Base failure | `SolrServerException` caused by `ClosedChannelException` at `TestCoordinatorRole.java:306`, PULL jetty stopped, no successful add (line 6) | Same shape, same line | Match |
| Focused runs | Six runs (CI seed plus five random seeds), each 1 test, 0 failures, 0 errors, rc=0 (line 9) | "passed six runs", "Each run: 1 test, 0 failures, 0 errors" | Match |
| Focused run date | Gate finished 2026-10-10 UTC (lines 3, 8). Gate file: first attempt 17:53 to 18:24 UTC, not counted, then reruns. No per-run dates. | "on 2026-10-10 (UTC)" | Consistent at day level. Not a finding. |
| Commits past gated head | Changelog title only, no code changed (line 5) | "the only commit since corrects the changelog title, and no code changed" | Match |
| Change shape | Test-only plus changelog, no production change (line 7) | "This is a test-only change." | Match |
| Changelog path | `changelog/unreleased/SOLR-16630.yml` | Same path | Match |
| Non-counted ENV attempt | Recorded as ENV, not counted (line 10) | Not mentioned | Correct: not a proof number |
| Seeds and internal run detail | Gate file and receipt | Not in the body | Correct for public text |

## Citations

Base SHA `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (pre-change symptoms, base lines; the text says so in the first paragraph of "What happens today"). Read with `git show <base>:<path>`.

| Link | Base lines | Content at those lines | Result |
|---|---|---|---|
| `TestCoordinatorRole.java#L221-L221` | 221 | `SolrClient client = pullJetty.getSolrClient();` | OK |
| `TestCoordinatorRole.java#L306-L306` | 306 | `client.add(COLL, d);` | OK |
| `TestCoordinatorRole.java#L241-L241` | 241 | `final long pullServiceTimeMs = 1000 + (long) r.nextInt(9000);` | OK |
| `TestCoordinatorRole.java#L259-L261` | 259 to 261 | `Thread.sleep(pullServiceTimeMs);`, log "stopping PULL jetty ...", `pullJettyF.stop();` | OK |
| `TestCoordinatorRole.java#L309-L309` (paragraph 2 of "What happens today") | 309 | `} catch (SolrException ex) {` | OK |
| `TestCoordinatorRole.java#L309-L309` (Limits bullet 2) | 309 | Same | OK |
| `SolrServerException.java#L24-L24` | 24 | `public class SolrServerException extends Exception {` | OK |
| `JettySolrRunner.java#L616-L617` | 616 to 617 | `IOUtils.closeQuietly(jettySolrClient);` and `jettySolrClient = null;` at the start of `stop()` | OK |
| `JettySolrRunner.java#L847-L852` | 847 to 852 | `getSolrClient()` returns the cached `jettySolrClient`, creating it only when null | OK |

Head SHA `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7` (code the change produces):

| Link | Head lines | Content at those lines | Result |
|---|---|---|---|
| `TestCoordinatorRole.java#L243-L243` | 243 | `CountDownLatch addDone = new CountDownLatch(1);` | OK |
| `TestCoordinatorRole.java#L262-L264` | 262 to 264 | `if (!addDone.await(2, TimeUnit.MINUTES)) {`, `log.warn(...)`, `}` | OK |
| `TestCoordinatorRole.java#L311-L313` | 311 to 313 | `client.add(COLL, d);`, `client.commit(COLL);`, `addDone.countDown();` | OK |
| `changelog/unreleased/SOLR-16630.yml#L1-L10` | 1 to 10 | Whole file | OK |

Total 13 links: 9 base, 4 head. Base lines and head lines agree with the six-line shift in the diff (base 221 maps to head 222, base 306 to head 311).

## Limits against the code

- Bullet 1 (pooled connection): a hedged hypothesis ("could"). Nothing in the code contradicts it, and it is not claimed as a finding.
- Bullet 2 (SolrException catch): base L309 and head L315 both catch only `SolrException`. The diff adds no catch. "This change does not widen that catch" is accurate.
- Bullet 3 (intermittent): consistent with one failing base run and six passing focused runs.
- Bullet 4 (test-only): the diff is the test file plus the changelog.

## Other checks

- Em dashes (U+2014) in the live body: none. Lucene: not mentioned, so the version rule does not apply.
- Internal process vocabulary (gate, receipt, ledger, seed, claim, pool, assignment, subagent, submission, worktree, Muse, vm, round): none in the live body. A control match ("AI assistance") confirmed the search ran.
- AI header: the first line matches the `pr-formula.md` template exactly. AI footer: "### AI assistance" section matches the template text. Jira link line present.
- Changelog line: a link to the fragment at the head SHA, as the formula requires.
- Presentation: each of the four sections opens with a bold one-line summary. Citations are links.
- Length: raw body 5,466 characters, about 2,100 of them link URLs. Prose is about 3,300 characters, within the guide's roughly 3,500.

## New findings

No blocking finding. Two optional wording items, not required for the verdict:

**O1. The bold summary of "What this change does" leaves out the two-minute cap that the next paragraph states.**
- Draft words: "**The test now waits for its first successful add before it stops the PULL replica. This is a test-only change.**"
- Replacement: "**The test now waits up to two minutes for its first successful add before it stops the PULL replica. This is a test-only change.**"

**O2. "recorded" is vague in the Proof headline (round 2 optional note N5, still not applied).**
- Draft words: "with the recorded failure."
- Replacement: "with the failure described below."

Round 2 optional notes still not applied, none changes a claim, count, date or link: plainer behavior-change sentence ("if no add succeeds within two minutes, the PULL replica stops up to two minutes later than before"), "PULL node" versus "PULL replica", the repeated "The failure is intermittent" and "test-only" statements, and naming the base SHA once in Proof.

## Not done

- No edits to the draft, the receipt, the claim, the assignment, the hosts file or any branch. No commit, push, PR comment, review, edit, close, or Jira read or write. No builds, Gradle, tests, or gate runs. Only `gh pr list` and `gh pr view` were run, read-only.
- The claim `claims/pool-flaky-16630-post-pr-review-round-3.md` is still ACTIVE with its round 3 heartbeat. It is not marked DONE, because claims are outside this assignment's write set. The lead marks it DONE in the same push as this report (WORKFLOW.md, rule 5).
- The PR base tip `d76fc14b` is not in the local object store, and no fetch was run. Whether `3f5d4c5b` is still the merge-base at that tip was not checked. `3f5d4c5b` is an ancestor of the head: the history above it is two commits, so the fork point is confirmed from the head side.
- The CI run 38009158573, the CI check status on PR 5106, and Jira SOLR-16630 were not read. The "reopened ticket" statement in the body rests on the assignment background.
- The gate logs on vm2 were not readable. The focused-run day rests on the receipt and gate file timing.
- `research\gh.ps1` was called by its absolute path, because the relative path does not resolve from the worktree.
