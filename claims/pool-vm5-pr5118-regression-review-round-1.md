# Claim: pr5118-regression-review-round-1

- Claimant host: vm5, matching a file in `hosts/`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `assignments/pool-pr5118-regression-review-round-1.md`.
- Slice: the whole job, scoped to the request-level question in the assignment.
- Capability tags used: review, gate.
- Started: 2026-10-23T04:50:01Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 0. Idempotency key: pr5118-regression@d742eb99dd28be761b5f60aea6fbfc832e1b7a50.
- Links: Ticket or parent: SOLR-18010, PR #5118 (`solr-18010-submit`). Category: 10, Core admin and cores (code area `org.apache.solr.handler.admin`); secondary 7, Permissions and security declarations. Siblings: none open on this surface; #5065 merged 2026-10-21. Supersedes: none.
- Host environment: JAVA_HOME `/workspace/jdk/jdk-21.0.12.1+1`, GRADLE_USER_HOME `/workspace/gradle-home`, Gradle lock `/workspace/tools/.gradle-vm5.lock`, worktree `/workspace/worktrees/pool-vm5-5118`.

Heartbeat: 2026-10-23T04:50:01Z

Append one heartbeat line at least once an hour while the claim is active, and push it.

Result: pending.

DONE: pending. Deliverables: `reports/pool-pr5118-regression-review-round-1.md`.

Heartbeat: 2026-10-23T05:44:00Z. Interim result, 2026-10-23T05:44:00Z: request-level test on branch `solr-18010-request-edits-test` (`31461dc35a`). Head `d742eb99dd`: passes, 1 of 1 from fresh XML. Same test with the handler lock removed: fails with a lost update (`expected [alice, bob] but was [bob]`). Two handler instances on one SOLR_HOME, no lock shared: a lost update (`expected [alice, bob] but was [bob]`). Logs are on vm5 under `/workspace/tmp/pool-vm5-5118/logs/`, not on this branch. Base-main leg not yet valid (the test needs main's handler test files); deferred until host memory recovers. Deliverable `reports/pool-pr5118-regression-review-round-1.md` still pending.
