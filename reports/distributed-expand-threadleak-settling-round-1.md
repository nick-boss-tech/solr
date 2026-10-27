# DistributedExpandComponentTest thread leak: settling run, round 1 (vm4)

Answers `assignments/distributed-expand-threadleak-settling-round-1.md`. Claim: `claims/distributed-expand-threadleak-settling-round-1-vm4.md`. Sighting: PR 5198 CI (Crave job 121178733450, seed `85F9F77FD74C1D41`), a `ThreadLeakError` at class scope, 1 thread leaked (`httpShardExecutor-420-thread-2`).

## Result

Classification (b): seed-clean on both trees. The leak did not reproduce at this seed on this host, JDK 21, in either tree. Recorded as a one-off sighting, not a ticket-shaped defect, unless the CI environment can be reproduced.

| Tree | Head | Result | Log | XML totals |
| --- | --- | --- | --- | --- |
| branch_9x | `dae5ac66f9c821d0edb26ca62493a1896c001d3b` | BUILD SUCCESSFUL, rc 0, 20m 23s | `logs/vm4/distributed-expand-threadleak/de-9x-dae5ac6-20261027T073541Z.log` | 1 test, 0 failures, 0 errors, 0 skipped (`de-9x-dae5ac6-20261027T073541Z.xml/`) |
| apache/main | `ee2f1bf2e0109956145d7fd995da63c4091a589f` | BUILD SUCCESSFUL, rc 0, 8m 5s | `logs/vm4/distributed-expand-threadleak/de-main-ee2f1bf-20261027T081253Z.log` | 1 test, 0 failures, 0 errors, 0 skipped (`de-main-ee2f1bf-20261027T081253Z.xml/`) |

Both runs: `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.DistributedExpandComponentTest -Ptests.seed=85F9F77FD74C1D41 -Ptests.file.encoding=ISO-8859-1`, through `pool/bin/sweep-run.sh` under the vm4 build lock, one at a time. The log shows `tests.seed=85F9F77FD74C1D41` in both runs. Neither log contains `ThreadLeak` or `leaked`.

## Limits

- The CI sighting is not reproduced here. Crave's JDK, image, and security-manager settings may differ from vm4's (JDK 21, `-Ptests.file.encoding=ISO-8859-1`). Matching them is outside this assignment.
- One test method per run and one seed. This is not a seed sweep. The assignment did not ask for one.
- The branch_9x tip (`dae5ac66f9`) is later than the head PR 5198 merged at. The CI run's head is not recorded in the sighting, so the run tests the current tip, not the merged commit.
- No fix, no branch, no JIRA or GitHub text. A ticket is the owner's call.
