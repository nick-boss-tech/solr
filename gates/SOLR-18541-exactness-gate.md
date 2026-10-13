# Gate job: SOLR-18541 exactness fix, first gate at the new branch

- Branch: solr-18541-combined-query-merge-totals (new fork branch, created 2026-10-13). Head: `fcd92f23ba65f304ee542c6fe0fec73d5d9e8ce2`. Parent: `d5ed34125db9582b584bbfdd149a4de4b40650d0`, the head the earlier gate covered (`combinedquerycomponent-merge-totals`).
- Status: CLAIMED by vm2 (gate backlog coordinator), 2026-10-13T14:45Z; claim claims/pool-vm2-gate-backlog-18541.md. Queued as /workspace/gates/g18541-gate.sh.
- Capability tags: gate.
- Intended host: Linux only (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: full focused gate at the new head, with a fail-before run for the new test.
- Why: the fix clears numFoundExact when a skipped partial shard reported hits. The earlier green gate does not cover it.

## Changes to check

1. `CombinedQueryComponent.java`: in the skip branch, `hitCountIsExact = false` when `docs.getNumFound() > 0`. Three added lines.
2. `CombinedQueryComponentMergeTotalsTest.java`: a new test `skippedPartialShardWithHitsClearsExactness` (two partial shards, both skipped, one reporting hits; expects numFound 0 and numFoundExact false), and one added exactness assertion in `skippedPartialShardContributesNothing`.
3. Changelog title updated to match.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-18541-combined-query-merge-totals` must equal `fcd92f23ba65f304ee542c6fe0fec73d5d9e8ce2`. If not, stop and record the new tip.
2. Diff scope against `d5ed34125db9582b584bbfdd149a4de4b40650d0`: three files (the changelog, the production file, the test file).
3. Changelog YAML parse.
4. Tidy, with the GATE INVALID post-tidy guard.
5. Spotless check on `:solr:core`.
6. Error Prone compile.
7. Focused run: `org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest` with fresh JUnit XML. Record counts, seed and date. The new test must pass.
8. Fail-before: revert the production change only, keep the new test, run it. The new test should fail with numFoundExact true. Record the failure text.
9. Module check for `:solr:core`.
10. On green, record the new head, counts, fail-before text and date in `receipts/SOLR-18541.md`. Mark this job DONE.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed (three files)
- [ ] changelog parse
- [ ] tidy clean with the guard; spotless check
- [ ] Error Prone compile
- [ ] focused run with JUnit XML; new test passes
- [ ] fail-before recorded
- [ ] module check
- [ ] receipt refreshed at the new head; job DONE
