# PR 5301 / SOLR-18623: reconnect liveness and deterministic coverage

Status: CLAIMED (Windows implementation; Linux proof required)

- Claimant: windows, Codex desktop; started 2026-11-05 20:33 UTC.
- Authorization: Nick explicitly requested a patch on the contributor PR, a Linux gate handoff, and a prepared PR comment in this chat.
- Tags: implementation, review, records, draft. Windows runs no Gradle or tests.
- Board snapshot: `3a860c33f4f5ac750c7c80cac7cffaded38a2297`; no matching assignment, claim, gate or receipt for PR 5301 / SOLR-18623 was found (numeric matches inside unrelated XML are not assignments).
- Contributor head: `01e820f0539cddc4697d26176d216614a776a99a`, apache/solr PR 5301, base branch_9x. Preserve ancestry with ordinary commits.
- Worktree: `C:\Users\shaninna\dev\Solr-issues\wt\PR-5301-reconnect-ordering`.
- Fork branch: `solr-18623-reconnect-liveness` on nick-boss-tech/solr only.
- Scope: make a reconnect handshake stop waiting when its replacement ZooKeeper session becomes terminal; retain event ordering protection; supply deterministic ordinary-test coverage for stale event application and expiration during reconnect, using actual callback completion barriers. Reassess the stress fixture's synchronization and replace its misleading oracle if appropriate.
- Deliverables: fork patch and verified remote SHA; `reports/pr-5301-reconnect-implementation-round-1.md`; `gates/pr-5301-reconnect-liveness-round-1.md`; local comment draft and an add-only board draft under reports/ naming pending proof accurately.
- Proof: capable Linux gate host; exact pushed head; focused and neighboring tests, explicit nightly stress if retained, tidy, root check, runtime fail-before legs. Tests failing to compile or initialize do not count as negative proof.
- Public communication: prepare a draft only in this slice. No public posting while proof is incomplete. Follow the root clean-gate authorization and any later draft-review hold before publication.
- Decision ladder: direct owner assignment and established source change, so the Windows implementation capability applies; runtime proof belongs to Linux. No owner decision is needed to start.
