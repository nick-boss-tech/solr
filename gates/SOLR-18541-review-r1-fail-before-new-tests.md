# Gate job: SOLR-18541 fail-before for the two new review tests (the proof leg the patch gate still owes)

- Branch: `combinedquerycomponent-merge-totals` on the fork. Head: `a93f9f6a997e748514cf2575a6e94c5f1c01f85e` (the PR branch at its moved head; the same head as `review-r1-solr-18541`).
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Intended host: Linux only (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Source: `gates/SOLR-18541-review-r1-patch.md` step 7 and `reports/review-batch-round-2-slices-round-1.md` section 3. The patch gate's focused run at this head is recorded GREEN in `receipts/SOLR-18541.md` (CombinedQueryComponentMergeTotalsTest, 10 tests, 0 failures, seed `C0FFEE42C0FFEE42`, module check rc=0), but the fail-before for the two new tests is not in the receipt and step 7 of the patch gate job is unchecked. This job supplies that proof leg only; it does not re-run the whole gate.
- Job type: fail-before proof run.

## Background

Commits `354edb85a2` and `a93f9f6a99` on top of `fcd92f23ba65f304ee542c6fe0fec73d5d9e8ce2` added two tests to `CombinedQueryComponentMergeTotalsTest` (10 tests in the class at this head):

- `skippedPartialShardWithoutCountClearsExactness`: the skipped shard sends no per-query entry; the merged shard is exact with 3 documents. Asserts numFound 3 and numFoundExact false.
- `skippedPartialShardWithoutHitsClearsExactness`: the skipped shard reports numFound 0 with an exact flag; the merged shard is exact. Asserts numFound 3 and numFoundExact false.

The same two commits renamed `skippedPartialShardApproximateTotalCounted` to `skippedPartialShardApproximateTotalExcluded` (no assertion change).

## Steps

1. Confirm the head: `ls-remote` on the fork shows `combinedquerycomponent-merge-totals` at `a93f9f6a997e748514cf2575a6e94c5f1c01f85e`.
2. In a scratch worktree at that head, revert production `CombinedQueryComponent.java` only, to the merge-base `d76fc14b9857e77b830d7a82514b28c523a239c2`. Keep the branch's tests, the changelog fragment and every other file at the head.
3. Run `org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest` (`:solr:core`) with seed `C0FFEE42C0FFEE42`, from fresh JUnit XML. Record the per-test result for every test in the class, and the full failure text for the two new tests by name.
4. Expected: both new tests fail on the reverted production, by content, with the premise values: each asserts numFoundExact false for a merge where the skipped shard is the only reason the count is not exact, and on merge-base production the totals folds run before the skip and the exactness fold is inverted, so the merged flag comes out true (`numFoundExact expected:<false> but was:<true>`). The pre-existing tests behave as the earlier premise records state (`receipts/SOLR-18541.md`: on the merge-base the original seven tests fail with the premise contents recorded in the gate at `d5ed34125db9`, and `skippedPartialShardWithHitsClearsExactness` fails without the fix, as recorded in the gate at `fcd92f23ba65`). A pass by either new test, or any failure outside that recorded pattern, is a finding: record it verbatim in this job and stop.
5. Restore `CombinedQueryComponent.java` to the head version and verify by hash; the final git status is clean.
6. On completion the gating host appends the fail-before counts to `receipts/SOLR-18541.md` (tests run, failures, the two named tests' failure texts, the seed, and the log and XML paths), and records this leg against step 7 of `gates/SOLR-18541-review-r1-patch.md` (checking that step with a pointer to this job, and noting that this leg ran against the merge-base production `d76fc14b9857` rather than the side-branch production that step names). Mark this job DONE. No push to any branch, no PR edit.

## Checklist

- [ ] head confirmed
- [ ] production reverted to the merge-base, branch tests kept
- [ ] focused run at seed C0FFEE42C0FFEE42, fresh JUnit XML
- [ ] per-test results recorded, the two new tests by name
- [ ] head tree restored, hash verified, status clean
- [ ] receipt section added; step 7 recorded in the patch gate job; job DONE
