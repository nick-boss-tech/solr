# SOLR-18564 / PR 5192: repaired head verified and review reply posted

Status: GREEN, PR UNDER REVIEW

- Host: windows, records/review follow-up to `claims/solr-18564-timeout-type-round-1-windows.md`.
- The implementation report `reports/solr-18564-timeout-type-round-1-windows.md` is historical: it records the then-pushed head F `bd42a1d58b9fba4656a200536a007d9d79dc45b2` and pending proof. This add-only report supersedes its current-status statements; it does not alter another host's receipt.
- Reviewed board snapshot: `4c558c24b6b50785c7d9e0bfb70589bbd4dba01e`. Read the full original and superseding gate, vm5's receipt `receipts/SOLR-18564-timeout-type-round-1.md`, the comment formula and scoped comment list. Board workflow/start/UI/Windows instructions have no changes from the implementation snapshot.
- Original PR head O: `dd65b21b41dc1d008c51031b71f61964ee2ca169`. Implementation head F: `bd42a1d58b9fba4656a200536a007d9d79dc45b2`. Final repaired and tested head R: `6ca1cba8061b3c6298040634972afc4361f55add`.
- Verified R independently through the live fork ref API and live PR metadata. F..R is only a wrapped assertion in ExecutorUtilTest.java. The repair has Nick Shanin's ICLA identity and no trailers. O..R still touches only the three intended source/test files. `git diff --check O R` is clean. No new source commit was made in this follow-up.

## Proof assessment

The superseding job `gates/SOLR-18564-pr5192-timeout-type-round-1-superseding-6ca1cba8061b.md` pins R. Vm5's receipt satisfies every named leg at R: changelog parse, initial and final clean tidy, Error Prone production/test compile in solrj and core, both seeds `18564ABC0001` and `18564ABC0002`, the matched synthetic mutation and passing control, and the root `check -x test` with NODE_ENV unset. Each seed has ExecutorUtilTest 6 passes, CoreContainerShutdownTest 1 pass, and TestCoreContainer 22 passes plus 3 expected Windows-only skips. The one-line mutation retains the typed exception declaration and identical test file; its failure is the expected typed-vs-RuntimeException assertion mismatch, not a compilation or unrelated failure. The mutation pins the new timeout-type contract and is not claimed as a replay of the original natural incident.

The receipt's task logs/XML paths and environment were reviewed as recorded; this Windows follow-up did not independently read the Linux filesystem or execute any tests. No missing required leg or unexplained failure remains in the record. Live upstream metadata at the pre-reply read contains only a successful labeler check for R; old upstream check successes in the original Windows review apply to O and are not restated as R proof.

## Public action and read-back

- Authority: Nick's standing 2026-10-26 clean-gate authorization for factual PR review follow-ups, after the required exact-head Linux proof and fresh discussion checks. No separate approval request was needed. The gate's own no-public-write scope remains respected: vm5 posted nothing; this is the Windows review follow-up.
- Fresh general and inline discussions were read before posting; there was no equivalent reply, no inline comment, and no new objection beyond sigram's addressed catch-scope question.
- Local draft: `C:\Users\shaninna\dev\Solr-issues\research\scratch\pr-5192-timeout-type\sigram-reply.md`. It follows comment-formula.md, contains the tested full SHA and pinned O..R diff, and contains no em/en dash or internal process vocabulary.
- Posted once with gh.ps1 pr comment --body-file, at `2026-10-27T19:00:50Z` (3:00:50 PM Eastern).
- URL: https://github.com/apache/solr/pull/5192#issuecomment-6450986593.
- Separate comment API read-back matches the local draft after newline normalization. Separate live PR metadata read confirms the same R head, the intended new comment, and no new review or other discussion change.
- No adoption commands are needed: this is Nick's own PR and its live branch already contains R. The reply pins the tested head and the full follow-up diff instead of asking another contributor to merge an external branch.
- The local PR review record now states R and GREEN. No PR body edit, JIRA write, approval, merge, or conversation resolution occurred. Await the next human review event; no further code change or proof run is needed absent new feedback.
