# Verdict: claims/solr-17786-gate-round-1-two-shard-vm2.md ended in check failure (not green)

- Head: `2c8af559c8456ded97811455622fb06786ae74ba`. No receipt is written for round 1, because the gate is not green.
- Legs passed (rc 0): tidy preflight, changelog YAML, Error Prone compile, compile, positive core seed 5108C0DE (16/0/0/0), positive SolrJ seed 5108C0DE (7/0/0/0), positive core seed 17786C0DE (9/0/0/0).
- M1 `CursorMarkTest.testZeroRowCursorMarks`: 1 test, 1 failure, the expected `assertNotNull` at `CursorMarkTest.java:150`.
- M2 `CursorPagingTest.testReverseCursorPaging`: 1 test, 1 failure, `expected:<[3, 4, 5]> but was:<[5, 4, 3]>`. The first attempt did not apply its patch and was stopped, so it is not counted. Its logs remain under `logs/solr-17786/mutation-2/` and are superseded by the second run.
- M3 `DistribCursorPagingTest.testReverseCursorPaging`: 1 test, 1 failure, `expected:<[3, 4, 5]> but was:<[5, 4, 3]>`.
- Final check `:solr:core:check :solr:solrj:check :solr:solr-ref-guide:check -x test`: rc 1. `:solr:core:forbiddenApisMain` failed on `SortSpec.java:68`, `Collections.singletonList(null)`. Gradle stopped there, so the remaining check tasks did not run.
- Resolution: ladder record `claims/solr-17786-forbidden-api-repair-vm2.md` (rung 4). Repair commit `fde070281383f113a331a9ec5ccde173b66fac3c` on the fork branch, superseding gate `gates/job-17786-r2` (publishing as `gates/solr-17786-reverse-cursor-round-2-vm2.md`).
- Logs: `/workspace/gates/gates-vm2/logs/solr-17786/` on vm2.
