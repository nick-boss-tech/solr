# PR drafts for the newly gated backlog branches

Assignment: `assignments/pool-drafts-newly-gated-backlog.md`. Claim: `claims/pool-drafts-newly-gated-backlog.md`. Lead: the windows draft agent. Part reports, each with the receipt numbers used, the head check, the title source, the citation checks and the choice decision:

- `reports/drafts-newly-gated-backlog-d1.md`: SOLR-4502, SOLR-12916, SOLR-16499 (core-admin).
- `reports/drafts-newly-gated-backlog-d2.md`: SOLR-9091, SOLR-9382 (replication-backup); SOLR-10364 (solrj).
- `reports/drafts-newly-gated-backlog-d3.md`: SOLR-10882, SOLR-9852 (streaming).

Eight draft files, written to the paths the assignment names. Text only: nothing was committed, pushed, posted or built, and no GitHub or Jira write was made. Proof counts come from each ticket's receipt. Drafts follow `pr-formula.md`.

## Per draft

| Draft | Head drafted | Head check | Choice section | Status |
|---|---|---|---|---|
| SOLR-4502 | `0ee4c644ee63` | matches the receipt and the fork tip | yes: `create()` guard versus `load()` in the constructor | Hold: the answers' owed premise is not met (see below) |
| SOLR-12916 | `50b1f4bcab03` | matches | yes: keep the XML flat-list path versus Config API only | Hold: the changelog title must name both paths, which moves the head |
| SOLR-16499 | `51b7addddd45` | matches | yes: plumb `parallel` and `timeout` versus remove them | Hold before posting (see below) |
| SOLR-9091 | `e6bfa5c626812` | matches | no | Ready for owner review; the changelog title needs a branch fix |
| SOLR-9382 | `2952bae9d392` | matches | no | Ready for owner review |
| SOLR-10364 | `efb1e6717f9f` (packaged, per the receipt) | fork tip is `502bdbf033`, which does not match | no | HOLD for posting (see below) |
| SOLR-10882 | `5d7d07a5b17b` | matches | yes: error versus fixed-order sort for mixed kinds | Ready for owner review after the items below |
| SOLR-9852 | `5fe174425514` | matches | yes: camelCase labels versus JDBC names | Ready for owner review |

## Holds and gaps

1. **SOLR-4502.** The answers asked for a search-time NPE to be reproduced. The receipt shows a create-time NPE only. The draft says so in Limits. The changelog title overstates the search-time claim and should be trimmed, which moves the head.
2. **SOLR-12916.** The changelog title names only the Config API. The answers want both paths. That is a head-moving fix.
3. **SOLR-16499.** Hold before posting. The reference guide default (300 in the guide, 600 in code), the v2 timeout description ("per replica move") and the OpenAPI and SolrJ check are owed. Each fix moves the head, so the draft must be re-cut and the matching Limits lines dropped. New finding: the timeout does not reach `AddReplicaCmd`, so with `waitForFinalState` each add still waits 600 seconds. That is a code fix, not a doc fix. The lead decides.
4. **SOLR-9091.** The changelog title says "(and rolls back)", which conflicts with the round 1 finding 3 and with the code: the download throws before the switch. The draft does not repeat the claim. The branch changelog title needs a fix before posting. The `TestRestoreCore` count of 4 matches the head (three `@Test` methods, plus an unannotated test-named method); this may clear the round 1 finding 2 for SOLR-9865 and SOLR-17287 as a counting artifact, not verified from the JUnit XML. The draft runs about 3,640 characters with links, slightly over the 3,500 guide.
5. **SOLR-9382.** About 3,560 characters with links, slightly over the guide.
6. **SOLR-10364.** HOLD for posting. The packaged head `efb1e6717f` is neither on the fork nor in the local object store, so its GitHub links would return 404 until the packaging commit is pushed. Either push the packaging commit, or re-point the links to the fork tip `502bdbf033`. Packaging delta, for the report only: it removes `SOLR-10364-TESTING.md`; the Java line ranges were read at `502bdbf033`. The material does not answer the category's "worth a PR?" question, and the draft assumes yes. The lead must confirm.
7. **SOLR-10882.** The Proof has no verification date, because the receipt gives none. The job record has `2026-10-11T02:32:06Z`; add a date line to the receipt if the date should appear. The receipt's merge-base label `e044bf20` is not the fork point: git gives `cabedd1d968`, and `e044bf20` is an ancestor 72 commits older. `ArrayEvaluator.java` and the eval test paths are identical at both, so the proof holds, and the draft links `cabedd1d968`. The receipt label needs correcting on the main side. The changelog title claims the NPE fix, which no test covers; the draft says so in Limits. The title could be narrowed.
8. **SOLR-9852.** The receipt names `testJDBCMethods` and `testDriverMetadata`. `testJDBCMethods` is a helper called from `testDriverMetadata`, so the Proof names one test method.

## Receipt corrections for the main side

- SOLR-10882: the merge-base label in the receipt (`e044bf20`) should read `cabedd1d968`.
- SOLR-2018 (not in this set): the receipt correction that replaces "UpdateRequest" is already on `pr-prepare`, in commit `d99378bdc85`.

## Not done

- No build, test, gate or test-queue run. No PR, comment, Jira or submit-branch write.
- Gate logs, premise logs and JUnit XML are not on disk. Counts are taken from the receipts as recorded.
- Live Jira text was not read for these tickets. The Jira packets on disk were used for spot checks only.
- The fork submit branches were fetched read-only into the worktree's remote-tracking refs, so that the heads could be checked. No refs were moved.
