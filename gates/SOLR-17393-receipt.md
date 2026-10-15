# Gate job: SOLR-17393 receipt at the reviewed head

- Branch: solr-17393-submit. Head: `626241e647e5fcc5ac6cc485f6a96d4ffe12e00a` (live tip, `git ls-remote origin refs/heads/solr-17393-submit`, checked 2026-10-14).
Status: DONE
- Capability tags: gate.
- Intended host: a Linux host with the gate toolchain (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Why: the baseline result at this head is recorded on the main side (`material/suggester-draft-round-answers.md`, item B, 2026-10-09; log `g17393-gate.log`), but `receipts/SOLR-17393.md` is not on this branch. Under the date rule in `reports/draft-date-flags-round-1.md`, the draft's Proof carries counts and head but no verification date until a receipt file exists.
- Job type: record write first, then a re-run only if the record is not on the host.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-17393-submit` must equal `626241e647e5fcc5ac6cc485f6a96d4ffe12e00a`. If it does not, stop and record the new tip.
2. If `g17393-gate.log` is on this host and its head line is `626241e647e`, write the receipt from it (no re-run) and go to step 9. Otherwise run steps 3 to 8.
3. Changelog YAML parse for `changelog/unreleased/SOLR-17393-suggester-merge-order.yml`.
4. Tidy with a clean tree, then Error Prone compile.
5. Pre-fix proof: against base `c3cdf7b46e8`, change only the visibility of `merge` in `SuggestComponent.java` so the test can call it (the visibility-only shim), keep `SuggestComponentMergeTest`, and run at seed `17393C0FFEE17393`. Record the failing assertion verbatim. If the old code passes at this seed, run extra seeds, as the round 4 answers direct, and record them.
6. Focused run at head with fresh JUnit XML: `SuggestComponentMergeTest`, `SuggestComponentTest`, `DistributedSuggestComponentTest`. Record counts, seed and the date. Note any cluster-startup noise separately from assertion failures.
7. Module check: `:solr:core:check -x test`.
8. Write `receipts/SOLR-17393.md` at this head: the counts from the JUnit XML, the date, the pre-fix result from step 5, and the note that the record came from the existing log when step 2 applied.
9. Mark this job DONE.

## Checklist

- [x] head confirmed by ls-remote
- [x] record written from `g17393-gate.log`, or steps 3 to 8 run
- [x] pre-fix observed failure recorded (step 5)
- [x] focused counts and date recorded (step 6)
- [x] receipt written at the head; job DONE

On DONE, the review coordinator restores the verification date in `pr-drafts/suggester/SOLR-17393.md` from the receipt.

## Coordinator run record (2026-10-14)

- Gate run at 626241e647e5 ended 14:09:14Z. Pre-fix leg rc=1 as expected (merge-order assertion fails on base); head leg rc=0 with 15 tests, 0 failures; all other rc lines 0. Not GREEN under the all-rc-zero rule; left for the main agent, not relaunched. Status line unchanged.

## Main agent ruling (2026-10-14)

The hold vm2 left on this job is discharged. The pre-fix leg's rc=1 is the expected proof shape, not a gate failure: against base c3cdf7b46e8 with only the visibility of `merge` shimmed, SuggestComponentMergeTest.testEqualWeightsDoNotDependOnShardResponseOrder fails (1 failure) on the merge-order assertion, because base code keeps the first equal-weight candidate that arrives and so the kept set follows shard response order. The failure matches the premise by content; every head leg returned rc=0 (changelog, tidy, compile, focused runs at 15 tests with 0 failures, core check). An all-rc-zero rule misreads the gate, since the pre-fix leg runs on reverted production where failures are the pass condition. Gate stands GREEN at 626241e647e5fcc5ac6cc485f6a96d4ffe12e00a; the receipt is receipts/SOLR-17393.md.
