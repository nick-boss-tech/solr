# Claim: teardown release paths round 2, Windows preparation

- Claimant host: windows.
- Status: DONE (Windows preparation only; runtime proof is a separate Linux continuation).
- Job or assignment: `assignments/pool-teardown-release-path-round-2.md`.
- Slice: source trace, original CI evidence review, deterministic tests and smallest factory candidate; hand off exact heads for Linux execution. No Windows builds.
- Capability tags used: implementation, review.
- Started: 2026-10-23T18:51:30Z.
- Lease: stale after 2 hours without heartbeat. Attempts: 1. Idempotency key: pool-teardown-release-path-round-2@c621d084806.
- Links: Ticket or parent: SOLR-18564 / teardown-objecttracker-leak. Category: Core admin and cores (10), SolrJ and clients (4), Test quality (13). Siblings: PR 5192, SOLR-18506, `fix-interrupted-http-client-close`, `phone-agent-2-objectrelease-autocloseable-candidate`. Supersedes: none.
- Decision: user requested progress on this assignment and reports that vm4 is publishing f0adb05094. Scratch candidate will be pushed to fork under the workspace's every-patch rule. Public PR actions and JIRA filing remain held. No Gradle or full suites here.
- Baseline correction: assignment says origin/main at 79b23df58d, but the fork origin/main is 498a09aea9. Use the explicitly requested Apache commit 79b23df58daac14b92cc35f23b8b81f72098b4ba, not the ambiguous ref.

Heartbeat: 2026-10-23T18:51:30Z.

Heartbeat: 2026-10-23T19:13:06Z.

Result: 2026-10-23T19:13:06Z. Source trace and original-log evidence support a nested-pool interrupt/early-return hypothesis for CustomSort's 60-second gap, distinct from the thrown timeout. Prepared four focused experiments and three isolated cleanup candidates. Candidate `solr-18564-release-path-r2` at ee1eb2474f6650f9c8f8a51c6729383496a4256e and test-only baseline `solr-18564-release-path-r2-before` at f92d77151295b5b237230843e3897e4c441da26d are pushed and verified. Test files match; baseline production Java is unchanged from Apache 79b23df58d. No builds/tests on Windows and no runtime result is claimed.

Decision ladder: acquire missing evidence through focused Linux execution; host capability mismatch is routed to the pool, not escalated to the owner. Parent status HANDED BACK with the exact continuation named. New JIRA/PR and full-suite decisions remain held. No public PR branch moved.

DONE: 2026-10-23T19:13:06Z. Deliverables: `reports/teardown-release-path-round-2.md`, scratch branches and SHAs above, `assignments/pool-teardown-release-path-round-2-linux-proof.md`. This closes source/test preparation, not the parent's before/after proof requirements.
