# Pool assignment: IOContext audit focused tests, round 1

Status: OPEN

- Capability tags: settling-run.
- Intended host: vm2 (Linux, Gradle toolchain). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm4, 2026-10-17, at the direction of Nick Shanin.

Context: branch `solr-ioctx-audit` on the fork (https://github.com/nick-boss-tech/solr) at `2a35fb87ff5e58f08678fa719dd4ea1ca05de514`, based on upstream main `70aa8d75bd`. It changes eight read-once Directory opens from `IOContext.DEFAULT` to `IOContext.READONCE` in solr-core. The output should not change, so the check is the existing focused tests, not new tests.

## Items

1. **Focused tests on the branch head.** Check out `2a35fb87ff5e58f08678fa719dd4ea1ca05de514` detached. Run the classes below on JDK 21 with the branch's Gradle wrapper and seed 18551C0FFEE18551:

   `./gradlew --no-daemon --console=plain -Ptests.seed=18551C0FFEE18551 -Dtests.seed=18551C0FFEE18551 :solr:core:test --tests org.apache.solr.core.SolrCoreTest --tests org.apache.solr.handler.TestReplicationHandler --tests org.apache.solr.handler.TestReplicationHandlerBackup --tests org.apache.solr.core.TestBackupRepositoryFactory --tests org.apache.solr.handler.TestIncrementalCoreBackup --tests org.apache.solr.handler.TestSnapshotCoreBackup --tests org.apache.solr.cloud.api.collections.TestLocalFSCloudBackupRestore --tests org.apache.solr.core.snapshots.TestSolrCoreSnapshots --tests org.apache.solr.handler.admin.api.CoreSnapshotAPITest`

   Record the JUnit counts per class from `solr/core/build/test-results/test/` and the build result. Output: one section per class in the deliverable.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Head result | decided: every listed class passes, with 0 failures, 0 errors, and 0 skipped | if any class fails or is skipped, record the output and stop; do not rerun the whole set | Nick Shanin |
| Failure triage | default: run only the failing class on base `70aa8d75bd` | if the same class also fails on base, record it as pre-existing; if it passes on base, record the failure and stop | vm4 at Nick's direction |
| Source changes | decided: none | if a run exposes a problem, report it without editing or pushing the branch | Nick Shanin |

## Deliverable

`reports/ioctx-audit-test-round-1.md`: the branch and exact commit, the command and seed, JUnit counts per class, the build result, and log paths on the host. Add the report commit and the coordination head to the claim's DONE line.

## Limits

- No PR, comment, JIRA or email. No force push. No change to the fork branch.
- Do not edit or push source. Do not add tests.
- Commit the report as Nick Shanin with no trailers. No em dashes or en dashes.
