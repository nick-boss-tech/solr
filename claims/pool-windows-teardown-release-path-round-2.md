# Claim: teardown release paths round 2, Windows preparation

- Claimant host: windows.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-teardown-release-path-round-2.md`.
- Slice: source trace, original CI evidence review, deterministic tests and smallest factory candidate; hand off exact heads for Linux execution. No Windows builds.
- Capability tags used: implementation, review.
- Started: 2026-10-23T18:51:30Z.
- Lease: stale after 2 hours without heartbeat. Attempts: 1. Idempotency key: pool-teardown-release-path-round-2@c621d084806.
- Links: Ticket or parent: SOLR-18564 / teardown-objecttracker-leak. Category: Core admin and cores (10), SolrJ and clients (4), Test quality (13). Siblings: PR 5192, SOLR-18506, `fix-interrupted-http-client-close`, `phone-agent-2-objectrelease-autocloseable-candidate`. Supersedes: none.
- Decision: user requested progress on this assignment and reports that vm4 is publishing f0adb05094. Scratch candidate will be pushed to fork under the workspace's every-patch rule. Public PR actions and JIRA filing remain held. No Gradle or full suites here.
- Baseline correction: assignment says origin/main at 79b23df58d, but the fork origin/main is 498a09aea9. Use the explicitly requested Apache commit 79b23df58daac14b92cc35f23b8b81f72098b4ba, not the ambiguous ref.

Heartbeat: 2026-10-23T18:51:30Z.
