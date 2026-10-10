# Claim: receipt refresh round 1 (gate-green receipts for previously held tickets)

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Scope: upstream commits `3c462a74d13` through `2c3633c8a37` and `14002ec1655` (receipt refreshes for SOLR-8088, 16570, 11391, 10403, 7120, 3923, 2681 and 4540), and the cap note in `51082bc9a37`. No new assignment. Each refresh records a gate-green run at the live head, so the earlier audit verdicts for these tickets are out of date. Each part re-checks the refreshed receipt against the branch, updates the verdict, and writes a draft only where the ticket is now draftable.

## Heads checked live on 2026-10-10

`git ls-remote`, then a read-only fetch. Each live head matches the gated head its refreshed receipt names. Earlier audit heads are listed where they moved.

| Ticket | Branch | Live head (= receipt gated head) | Earlier audit head | Earlier verdict | Refreshed receipt |
|---|---|---|---|---|---|
| 10403 | `solr-10403-submit` | `d8e03755cb5d3d8bc990cdb49a6bc1d11ac8583c` | `d8e03755cb5` | audit only (configsets s2) | gate green after a runner correction; 3 parametrized variants fail on base |
| 11391 | `solr-11391-submit` | `b0f15a22856a887d4e369cd1542c0ce09a687059` | `b0f15a22856` | held, audit only (query q3) | gate green; `testUnknownJoinMethodIsBadRequest` fails on base (500 vs 400) |
| 16570 | `solr-16570-submit` | `9d3466101a29b70ac0b45aa0770b4cf4f34e6cb3` | `974c44f9608` (**moved**) | not submittable, audit only (query q8) | gate green; `testTopFcHintOnDocValuesWithoutUninversion` fails on base (NPE) |
| 3923 | `solr-3923-submit` | `723f61ee4ab7438220193db0c39dad32601dfe07` | `723f61ee4ab` | not ready for a draft (edismax e5) | gate green; `testPfPs` fails on base; the premise is settled by the run |
| 7120 | `solr-7120-submit` | `4664014a919cc30448b784a5d8998c6536f69d55` | `6a434a7fc3d` (**moved**) | sound in shape; remove handoff note (edismax e5) | gate green; `testNeitherQfNorDfIsBadRequest` fails on base |
| 8088 | `solr-8088-submit` | `567efa9b78c32df3454ed431a40a04c06ed09d46` | `567efa9b78c` | audit only, no gate (search components s6) | gate green; `testMultiValuedFieldIsRejected` fails on base |
| 2681 | `solr-2681-submit` | `a3b1ea7994d932cdf78667f799542d7d823f7d49` | `4cb25b1691b` (**moved**) | highlighting round 1 draft | title re-gate green at this head |
| 4540 | `solr-4540-submit` | `180b6e8a7d3c23ac308a28d547f61816b5815f04` | `62c06439fb0` (**moved**) | highlighting round 1 draft | title re-gate green at this head |

## Shared rules for every part

- Read only, except the report file and any draft named for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new beyond what is named. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine.
- No builds, no Gradle, no tests, no test runs of any kind. No `gh` calls.
- Post nothing anywhere. No PR, no comment, no JIRA.
- Proof numbers come only from the refreshed receipt. The gate logs are not on disk; say so. Do not take counts from earlier reports.
- A draft follows `pr-formula.md`: the AI header as line 1, the Jira link, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Each draft names its head in its Proof.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit XML, pre-fix proof as a label, "premise run", internal log names). Plain words. No em dash and no en dash.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so. Do not guess.
- If the live head differs from the head named in your table, hold the ticket and say so.

## Parts

Four subagents. Reports go to `reports/receipt-refresh-round-1-<part>.md`. Drafts go to the folder named in each part.

**Part r1: SOLR-10403 and SOLR-11391.** 10403 (configsets): the refreshed receipt records a gate-green run after a runner correction, and the history of that correction. Verify the claim that the three variants of `testConvertAmountRoundsInsteadOfTruncating` fail on base and pass on the branch. Check the rounding change in `CurrencyValue.java` (`convertAmount`) against the receipt's account (2554 on base, 2555 on the branch). The earlier audit said the change affects query-time results only; confirm that still holds. Draft it under `pr-drafts/configsets/SOLR-10403.md` if draftable. 11391 (query parsing): the receipt's Proof is a 500 to 400 change on an unknown join method. Our earlier verdict held it because the Jira ticket describes a different problem (GraphTermsCollector). Keep that hold, and say whether the new receipt changes it. Draft under `pr-drafts/query-parsing/SOLR-11391.md` only if the ticket question is settled by the owner (it is not, so hold with a short draft marked HOLD).

**Part r2: SOLR-16570 and SOLR-8088.** 16570 (query parsing, collapse): the head moved from `974c44f9608` to `9d3466101a2`. The refreshed receipt says packaging removed the handoff note and folded in one tidy rewrap inside the new test. Verify the branch change to `CollapsingQParserPlugin.getTopFieldCacheReader` and the claim that collapse and the two ExpandComponent call sites stop failing with a 500 on a docValues string field. Verify the test's 20-of-20 count against the file. The earlier audit said the pairing with SOLR-17796 is test-file only; re-check that at the new head. Draft under `pr-drafts/query-parsing/SOLR-16570.md` if draftable. 8088 (search components, SearchGroupsFieldCommandTest): verify the 2-of-2 count and the single failure on base (`testMultiValuedFieldIsRejected`) against the file. Check the branch change and the changelog. Draft under `pr-drafts/search-components/SOLR-8088.md` if draftable.

**Part r3: SOLR-3923 and SOLR-7120 (edismax).** 3923: the earlier audit (edismax e5, finding 1) said the new guard in `ExtendedDismaxQParser.java` (lines 310-317 at the earlier head) misses signed lone parens such as `+(` or `-)`, because `isBareWord()` is false for signed clauses. The refreshed receipt says the branch skips a bare clause whose raw text is only parentheses, in `addPhraseFieldQueries`. Check whether the guard at the live head still misses signed lone parens, and whether the premise run (`testPfPs`, 39 of 39, one failure on base) settles the premise. Draft under `pr-drafts/edismax/SOLR-3923.md` only if the guard finding is resolved at the live head; otherwise hold and state the remaining FIX. 7120: the earlier verdict said the committed handoff note must be removed first. The receipt says packaging removed it. Verify the note is gone at `4664014a919`, and check the BAD_REQUEST change in `ExtendedDismaxConfiguration` (500 to 400 with "Neither qf nor df are present."), its changelog fragment, and the test count (40 of 40, one failure on base). Draft under `pr-drafts/edismax/SOLR-7120.md` if draftable.

**Part r4: highlighting drafts SOLR-2681 and SOLR-4540.** Main-side commits updated these drafts and receipts to new title-re-gate heads: 2681 at `a3b1ea7994d` (earlier `4cb25b1691b`) and 4540 at `180b6e8a7d3` (earlier `62c06439fb0`). Verify each draft's citation links and Proof head against the live head, the receipt's counts, and the new changelog titles in the branch. Check the drafts for dashes and internal vocabulary. Do not write new drafts. Edit nothing; list the fixes in your report. These drafts live in `pr-drafts/highlighting/`.

## Deliverables

1. `reports/receipt-refresh-round-1-r1.md` through `-r4.md`. The lead writes `reports/receipt-refresh-round-1.md` with the verdicts, moved heads, and owner decisions.
2. Drafts only where a ticket is now draftable, in the folders named above.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.
