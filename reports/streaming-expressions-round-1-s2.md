# Streaming expressions round 1, part S2: SOLR-13524 consistency and SOLR-14231 draft

No builds, tests, `gh` write calls, commits, pushes, or posts. The only file written is `pr-drafts/streaming/SOLR-14231.md`.

## Part A: SOLR-13524 (live PR, consistency only)

**Verdict:** consistent on head, title, changelog and counts. Five drift items, all about proof text and receipt wording. None is about the code.

**Consistent:**
- The live branch `solr-13524-submit` (ls-remote), the receipt, and the PR's `headRefOid` all equal `f08e7c12a8ed51f5b0829637f8fe8e95d6ae47a8`. The PR is open and not a draft. It is the only PR whose head ref is `solr-13524-submit`.
- The PR title equals the changelog title. `changelog/unreleased/SOLR-13524.yml` has type `fixed`, author Nick Shanin, and the SOLR-13524 link. The PR's changelog line matches.
- Counts at the head match the receipt: OrEvaluatorTest 6 `@Test`, AndEvaluatorTest 2, ExclusiveOrEvaluatorTest 2, RecursiveEvaluatorTest 1.
- The head commit `f08e7c12a8ed` changes only `OrEvaluator.java` (3 insertions, 1 deletion). It turns the unreachable checker body into `return false` with a "Never invoked" comment, which matches the receipt.
- Live checks on the PR: changelog check pass, Solr Tests via Crave pass (at `f08e7c12a8ed`), gradle check pass, labeler pass, generate skipped. `mergeStateStatus` is UNKNOWN in gh output.

**Drift items:**
1. **PR Proof names a stale head.** "Verified on the fork's GitHub Actions test runner at head `2b6f17e2622` on 2026-10-04". The live head is `f08e7c12a8ed`. The count (6 of 6) still matches, but the proof does not name the head it covers.
2. **Receipt line 5 is wrong.** It cites "A GitHub runner run at the same head (37567533617, OrEvaluatorTest)". Run 37567533617 is on the fork (`Fork test runner`, workflow_dispatch) at headSha `1869191c4cff741fd4fa64d0ca02a97e8a206564`. That commit ("ci: 13524-orevaluator") sits on branch `ci/13524-orevaluator`, one commit above the live head, and adds only `.github/workflows/fork-test-runner.yml`. So the run was not at the live head. The PR text cites `2b6f17e` instead. Three different heads are in play.
3. **Receipt line 8 is wrong on date and head.** "An apache/solr CI failure on this PR (Crave run 37178631002, 2026-10-05)". The run was created 2026-10-04T05:00:31Z and ran at headSha `2b6f17e26227dae499ca19aebf3d84d3f7ab779e`, not the live head. The live head's Crave run 37567044712 passed.
4. **PR Proof: "(12 of 15 test executions failed under randomization)" on base code.** The receipt records no base run for this ticket, so the count is unsupported by the receipt. A read of `orThreeBooleans` supports the direction: the base pairwise check returns false for (false, false, true).
5. **PR Proof names only OrEvaluatorTest.** The receipt's AndEvaluatorTest 2 of 2, ExclusiveOrEvaluatorTest 2 of 2 and RecursiveEvaluatorTest 1 of 1 are not in the PR text. AndEvaluator has no `doWork` override, so it runs the base `doWork` that this head refactors (`validateValues` extracted). The Limits line "Only the or() evaluation path changes" holds for behavior, but the shared path that the And tests cover is not named in the Proof. No replacement drafted.

**Not verified in Part A:** the receipt's "Changelog YAML parses; tidy clean; Error Prone compile passes" (no builds); "the only failures were in TestThinCache" (CI logs not read); gate log `g5013r29-gate.log` (not found under `research/` to depth 3); the takeover log and receipts ledger (not read); the SOLR-13524 ticket text (`research/jira-context/SOLR-13524.json` does not exist).

## Part B: SOLR-14231 draft

**Draft:** `pr-drafts/streaming/SOLR-14231.md`.

**Length:** 3,953 characters with links (`LC_ALL=C.UTF-8 wc -m`). That is over the roughly 3,500 guide. About 1,300 of those are link URLs, which carry the full SHAs.

**Self-check:** no em or en dashes. No process words (gate, receipt, ledger, reconcile, round, takeover, owed, rc=0, JUnit, pre-fix, batch, gated). Every head reference uses the full SHA `2f73d00a3996da1623156ccef649b59be0b59d45` (8 times). Base citations use the full merge-base `b5c71bc5573c4e31b4cee5a7965d73587fc0ae58` and say "base" (2 times). No short SHAs.

**Premise on current main (`upstream/main` 8e62c2686882): holds.** `ScoreNodesStream.java` has the same blob (`1374513b5d4f`) at the merge-base and on main. The base sets field and collection only from the second node on, has no empty-node guard, and sends a default GET. The two GraphExpressionTest tests the draft names (`testScoreNodesNoNodes`, `testScoreNodesStreamSingleNode`) and ScoreNodesStreamTest are absent at the merge-base. GraphExpressionTest has 8 `@Test` at the head and 6 at the base.

**Open points for the lead:**
1. The ticket text is missing. `research/jira-context/SOLR-14231.json` does not exist, so "What happens today" is derived from code only. The changelog title ("no longer fails when it receives a single node or no nodes") asserts a failure that could not be tied to the ticket symptom or to a base run. Check the wording against the ticket before posting.
2. No fail-without-fix run is recorded for the new tests. The Proof says "not run" and gives the by-reading result: the base cannot pass `testSingleNodeTermsRequest`. The formula asks for a fail-without-fix statement. Decide whether a base run is owed before posting.
3. The receipt gives no verification date for the 8 of 8 and 1 of 1 counts ("reconcile gate, 2026-10-04 era; head verified 2026-10-10"). The Proof has no date. Supply one.
4. No Choice section. The POST change and the empty-node early return have no live alternative, as far as the part agent judged. Overrule if there is one.
5. Length is over the guide by about 450 characters. Trim if wanted.
6. The Limits entry states the dropped test as a limit of the change: a nested gather over one indexed document returns no tuples on its own, before `scoreNodes` runs. That rests on the receipt and commit `2f73d00a3996`. It was not run.

**Not checked:**
- Ticket text for SOLR-14231 and SOLR-13524 (both JSON files absent).
- Any base-code run, build, or test (rule). So fail-without-fix, the "12 of 15" count and the receipt's gate statements are not verified.
- Crave and CI logs, gate log `g5013r29-gate.log`, the takeover log, the receipts ledger.
- Changelog YAML parse and tidy.
- The changelog-check run's headSha (only the Crave and gradle runs were checked).
- Tooling note: the first `gh.ps1` call with a comma-separated `--json` list failed at the wrapper. The quoted form worked. All gh calls were read-only.
