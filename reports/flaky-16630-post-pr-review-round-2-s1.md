# Post-PR review round 2, part s1: SOLR-16630 round 1 findings (apache/solr PR 5106)

Slice verdict: F1 FIXED, F2 FIXED, F3 FIXED. No blocking item in this slice. One optional wording note on the Limits link (see below).

## Live PR and head

- PR 5106 on apache/solr: OPEN, draft, head `nick-boss-tech:solr-16630-submit` at `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7`. This is the head round 1 covered. No difference.
- Title: "SOLR-16630: TestCoordinatorRole.testNRTRestart waits for the first add to complete before stopping the PULL node". It matches the changelog title apart from the prefix.
- Worktree draft `pr-drafts/flaky-fixes/SOLR-16630.md` is identical to commit `d15de4f3024` (`git diff` against that commit is empty).
- Live body against the draft: compared sentence by sentence across all sections and link targets. No differences found. No byte-level hash was run.
- Gated head `9bea59741ac` to live head `9c77a4db`: one file, `changelog/unreleased/SOLR-16630.yml`, one line changed (the title). Merge-base `3f5d4c5bf8ac` to live head: changelog (10 lines) and `TestCoordinatorRole.java` (6 added lines), as in round 1.

## F1: symptom citations link the base commit with base lines, and the text says so. FIXED

Base commit `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c`, read with `git grep -n` and `git grep -A`:
- `TestCoordinatorRole.java` L221 `SolrClient client = pullJetty.getSolrClient();`
- L241 `final long pullServiceTimeMs = 1000 + (long) r.nextInt(9000);`
- L259 `Thread.sleep(pullServiceTimeMs);`, L261 `pullJettyF.stop();`
- L306 `client.add(COLL, d);`
- L309 `} catch (SolrException ex) {`
- `SolrServerException.java` L24 `public class SolrServerException extends Exception {`
- `JettySolrRunner.java` L616-L617 `IOUtils.closeQuietly(jettySolrClient); jettySolrClient = null;`
- `JettySolrRunner.java` L847-L852 `getSolrClient()` returns the cached client.

The live body has nine symptom links on the base SHA with base lines (4 in "What happens today" para 1, 2 in para 2, 2 in para 3, 1 in Limits). Each resolves to the cited code above. The sentence "The links in this section point to the base commit, which is the code before this change." is present in "What happens today".

Fix citations link the head SHA `9c77a4db`: L243 (the `CountDownLatch` line added), L262-L264 (the await block), L311-L313 (add, commit, `countDown`), and the changelog L1-L10. Checked with `git diff -U0 3f5d4c5bf8ac 9c77a4db -- TestCoordinatorRole.java`: added lines at new 243, 262-264, and 313. Lines 311-312 are the existing add and commit lines.

Residual, optional: the Limits link (base L309) sits in the Limits section. The sentence that says the links point to the base commit is in "What happens today", not in Limits. The SHA in the URL shows it is not head, so this is not a blocker. Optional replacement for the Limits bullet, if the main side wants the statement next to the link:

- The add loop still catches only `SolrException` at the base commit ([test](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L309-L309)). Widening that catch was not done. On request, a follow-up ticket and pull request can cover it.

## F2: Proof wording matches the receipt. FIXED

- Live headline: "**On the base code the test failed in its one run, with the recorded failure. With this change it passed six runs.**" This matches the round 1 replacement exactly.
- Live: "This change targets that failure: the stop now waits for the first successful add." This matches the round 1 replacement. The phrase "the failure this change prevents" is gone.
- Numbers against `receipts/SOLR-16630.md`:
  - Base: one run, stopped after run 1, which failed (receipt line 6). The body says "one run on 2026-10-11 (UTC)", and the receipt's base runs are dated 2026-10-11 UTC.
  - Failure: `SolrServerException` caused by `ClosedChannelException` at `TestCoordinatorRole.java:306`, with the PULL jetty stopped and no successful add in the loop (receipt line 6). The body matches.
  - Six focused runs: CI seed plus five random seeds, each 1 test, 0 failures, 0 errors, rc=0, at `9bea59741ac30ffd11ed11be5269ea025cf17f68` (receipt line 9). The body matches.
  - "2026-10-10 (UTC)" for the six runs: the receipt gives only the gate finish date, not per-run dates (round 1 N8). The day-level claim follows from that date. Not a finding.
  - "The only commit since corrects the changelog title, and no code changed" matches receipt line 5 and the one-line diff above.

## F3: Limits follow-up offer is present and plain. FIXED

- Live bullet: "The add loop still catches only `SolrException` ([test](...#L309-L309)). Widening that catch was not done. On request, a follow-up ticket and pull request can cover it." This matches the round 1 replacement exactly.
- `pr-formula.md` (Limits rule, around lines 44-51 and 178-183) asks for "an offer to open a follow-up ticket and PR for it on request". The sentence names both, says "On request", and is plain. No change needed.

## Round 1 optional notes, status in the live body (not findings)

- N1 (line 23, timing wording): not applied. Live text: "if no add ever succeeds, the test reaches its failure up to two minutes later than before."
- N2 ("In a failing CI run"): not applied. The receipt records no CI run. The only failing record is the vm1 base run at the CI seed. The source still needs confirming (ticket or CI link), or the wording should be "In a failing run". This slice did not read Jira.
- N3 ("can go stale"): not applied. Optional.
- N4 (name the base commit once in Proof): not applied. Optional.
- N5 (changelog link text in backticks): not applied. Trivial.

Checks on the draft: no em dashes (U+2014). No internal process vocabulary (word search for gate, receipt, ledger, seed, claim, pool, assignment, subagent, submission, round, worktree, Muse, vm: no matches). "Pooled connection" is a technical term.

## STILL OPEN items

None in this slice.

## Not done

- No edits to the draft, the receipt, the claim, the assignment, the hosts file or any branch. No commit, push, PR comment, review, edit, close, or Jira write. No builds, Gradle, tests, gate runs or queue commands.
- The claim file `claims/pool-flaky-16630-post-pr-review-round-2.md` was not read or marked DONE. That is the main side's step with the deliverable push.
- The byte-level comparison of the live body with the draft was not run. The comparison was manual, sentence by sentence.
- The source of "In a failing CI run" (N2) was not checked. No Jira read.
- CI check status on PR 5106 was not read (not in this slice).
- `research\gh.ps1` was called by its absolute path, because the relative path fails from the worktree. Read-only `pr list` and `pr view` only.
- No fetch was run. Both `9c77a4db` and `3f5d4c5b` were already in the local object store.
