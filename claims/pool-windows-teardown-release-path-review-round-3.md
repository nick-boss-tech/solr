# Claim: release-path review round 3

- Claimant host: windows.
- Status: DONE (source review and scratch test repair; separate Linux proof pending).
- Job or assignment: `assignments/pool-teardown-release-path-review-round-3.md`.
- Slice: six concerns, source/provenance and available evidence review, scratch test-side repair if justified; Linux proof routed separately.
- Capability tags used: implementation, review.
- Started: 2026-10-23T19:51:27Z.
- Lease: stale after 2 hours without heartbeat. Attempts: 1. Idempotency key: teardown-release-path-review-round-3@9e0db91d8ec.
- Links: Ticket or parent: SOLR-18564 / teardown release paths. Category: Core admin and cores (10), SolrJ and clients (4), Test quality (13). Siblings: PR 5192, PR 5213 / SOLR-18578, the round-2 proof and f0adb05094. Supersedes: none.
- Disclosure: this Windows chat authored the round-2 tests and report; this is an investigation/correction of its own work, not independent approval.
- Scope: no Windows Gradle, public PR/JIRA changes, CI reruns, force pushes, or PR-branch movement. Keep the failed proof stopped and tracker fallback change excluded.

Heartbeat: 2026-10-23T19:51:27Z.

Heartbeat: 2026-10-23T20:04:26Z.

Result: Six concerns addressed in `reports/teardown-release-path-review-round-3.md`. Definite missing test barrier identified; exact old four-object attribution remains unverified without raw vm4 evidence. Test-only branch `solr-18564-release-path-r3-test` at 68fd05e53002f8c0a1fbff65a5623b5fea96028d is pushed and verified. Stable client patch IDs match. PR 5213 kept conceptually separate; no public PR/JIRA actions. CI comparison is bounded and inconclusive after an unavailable log. Both proof trees retain the original tracker/framework source. No builds/tests ran on Windows.

Decision ladder: source evidence resolves the missing test synchronization; a new isolated test is the bounded next experiment. Host capability is routed via `assignments/pool-teardown-release-path-round-3-test-proof.md`, not escalated. Original failed head is not relaunched. Worktree removal after the report push is authorized by parent step 7; no branch deletion or foreign-host cleanup.

DONE: 2026-10-23T20:04:26Z. Deliverables: report, CI scan provenance JSON, corrected round-2 report, pushed scratch test repair, and one-method Linux proof assignment. This is not a runtime-green or independent-review verdict.
