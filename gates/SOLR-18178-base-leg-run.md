# Gate job: SOLR-18178 base-leg run for the receipt's focused set

- Branch: solr-18178-verify. Head: `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2` (live tip, verified by ls-remote at posting, 2026-10-13; GATE GREEN per receipts/SOLR-18178.md; a live PR at this head is already ready).
- Status: DONE by vm2 (gate backlog coordinator), GATE END 2026-10-13T14:11:36Z; claimed 2026-10-13T13:48Z; claim claims/pool-vm2-gate-backlog-18178-base-leg-run marked DONE. Runner /workspace/gates/g18178-gate.sh.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: base-leg record run only. The base-leg tree is a scratch worktree: never committed, never pushed.
- Why: the pr-drift round 1 item (c) base leg, the branch's whole test tree against merge-base production, failed at `:solr:core:compileTestJava` with 24 errors (log logs/vm2/prdrift-18178-base.log), because the branch-added UploadConfigSetAPITest.java and DownloadConfigSetAPITest.java reference production the merge-base lacks. No test ran, so no base result exists for the focused set in one consolidated record. This job produces that record with the two non-compiling files reverted in the base-leg tree only.
- The focused set for this base leg, from receipts/SOLR-18178.md (packages verified by grepping the branch at posting):
  - `org.apache.solr.cloud.api.collections.DeleteBackupCmdTest`
  - `org.apache.solr.core.TestFileSystemConfigSetService`
  - `org.apache.solr.handler.designer.TestSchemaDesignerConfigSetHelper`
- Record at posting, so the claimant does not rediscover it: a re-scoped run on 2026-10-13 already ran DeleteBackupCmdTest alone in this shape (the two configset files reverted to merge-base), 3 tests, 0 failures, recorded in receipts/SOLR-18178.md under "Base run, 2026-10-13". A pass at base shows no fail-before for that class, and its body sentence is already named for cutting in the drift record. This job runs the full set in one base-leg tree: the DeleteBackupCmdTest leg repeats that shape and either confirms the 2026-10-13 record with fresh counts or supersedes it, and the other two classes get their first consolidated base record here. The two configset API test classes are not in the base-leg set: their head versions are the reverted files, and their fail-before evidence stands separately in the receipt (the proof run against the previous head's production, and the 2026-10-04 Windows overlay runs against base `0d2a4649c79`).

## Steps

1. Claim this job under WORKFLOW.md before starting.
2. Compute the merge-base of the branch with upstream main at claim time and record it. The receipt's recorded base is `0d2a4649c79a`; record what you get if it differs.
3. Make a scratch worktree at the merge-base. Overlay the branch's test tree. Then revert exactly these two files to their merge-base versions, and nothing else:
   - solr/core/src/test/org/apache/solr/handler/configsets/UploadConfigSetAPITest.java
   - solr/core/src/test/org/apache/solr/handler/configsets/DownloadConfigSetAPITest.java
4. Run the focused set through the claiming host's normal gate route (on vm1, ~/workspace/tools/solr-gradle.sh; never ./gradlew): module `:solr:core`, the three classes above, counted from fresh JUnit XML, with the seed recorded.
5. If one of the focused classes itself fails to compile against base even with the two reversions, record the compiler message and classify that class's leg INCONCLUSIVE. Do not relabel it.
6. Record the result in receipts/SOLR-18178.md as a base-leg section: the merge-base, the counts per class, the failures by name and content, the seed and the date, and the deviation stated plainly: the base leg ran with the two branch-added files reverted in the base tree only. Fill the completion note below and mark this job DONE.

## Deliverable

The recorded fail-before result settles the fail-before sentences for the live PR body: a class that fails at base on content has its fail-before backed; a class that passes at base, or whose leg is INCONCLUSIVE by compile, has its sentence named for cutting or rewording in the completion note, by the claim wording reports/pr-drift-round-1.md quotes. The main side applies any cut; this job does not edit the body and does not touch the branch.

## Completion note (the claimant fills this in)

- Merge-base: 0d2a4649c79a / seed: 18178C0FFEE18178 / date: 2026-10-13 (GATE END 14:11:36Z)
- DeleteBackupCmdTest at base: 3 tests, 0 failures (confirms the 2026-10-13 record)
- TestFileSystemConfigSetService at base: 3 tests, 0 failures
- TestSchemaDesignerConfigSetHelper at base: 8 tests, 0 failures
- Sentences named for cutting or rewording: the DeleteBackupCmdTest fail-before sentence (cut, or reword to the code reading). The two other classes show no Linux fail-before; any sentence that presents them as failing at base should cite the 2026-10-04 Windows overlay runs in the receipt.

## Checklist

- [x] claimed; merge-base computed and recorded
- [x] scratch worktree at the merge-base, branch tests overlaid, exactly two files reverted
- [x] focused set run, counts from fresh JUnit XML
- [x] receipt base-leg section written, deviation stated plainly
- [x] completion note filled; job and claim DONE
