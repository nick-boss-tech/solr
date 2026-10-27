# Assignment: settling run for the DistributedExpandComponentTest thread leak on PR 5198's CI (round 1)

Status: DONE
Tags: premise-run, records

DONE: 2026-10-27T08:31:31Z. Deliverables: reports/distributed-expand-threadleak-settling-round-1.md (classification b: seed-clean on branch_9x dae5ac66f9 and apache/main ee2f1bf2e0, seed 85F9F77FD74C1D41).

Sighting: apache/solr PR #5198 (SOLR-17953 branch_9x backport, merged 2026-10-25 with this check red) failed its Crave test job 121178733450 at seed `85F9F77FD74C1D41`. No test method failed: `DistributedExpandComponentTest` failed at classMethod with `com.carrotsearch.randomizedtesting.ThreadLeakError`, 1 thread leaked from SUITE scope (`httpShardExecutor-420-thread-2`, RUNNABLE at teardown). The PR's change is backup/restore hidden-file filtering and does not touch the expand path; no Jira ticket records this test failing (checked 2026-10-25). "Environment noise" is not a verdict; classify it.

## Work

1. Run the whole class `org.apache.solr.handler.component.DistributedExpandComponentTest` at seed `85F9F77FD74C1D41` on the branch_9x tip, CI-shaped as far as the local gate allows (`-Ptests.file.encoding=ISO-8859-1`, security manager on). Record whether the ThreadLeakError reproduces, and if it does, the leaked thread's stack from the test output.
2. Same run on apache/main at the same seed.
3. Report `reports/distributed-expand-threadleak-settling-round-1.md`: per tree, pass/fail with the XML cited; if the leak reproduces on either tree, the stack and the code path it points at; a classification of (a) reproduces at the seed (a real timing/teardown defect, ticket-shaped), (b) seed-clean on both (a one-off sighting, recorded), or (c) 9x-only (branch-specific).

No fix, no branch, no Jira or GitHub text in this assignment. A ticket is the owner's call after the classification lands.
