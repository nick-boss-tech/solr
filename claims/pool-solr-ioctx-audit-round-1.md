# Claim: IOContext audit focused tests, round 1 (vm2)

- Claimant host: vm2 (Linux). Assignment: assignments/pool-solr-ioctx-audit-test-round-1.md (posted 1c2c3ff2d6).
- Slice taken: item 1, the nine focused classes on the branch head, plus failure triage on base if any class fails.
- Capability tags used: settling-run.
- Head: solr-ioctx-audit at 2a35fb87ff5e58f08678fa719dd4ea1ca05de514 (ls-remote at claim time). Base 70aa8d75bd (ancestor of the head). JDK 21.0.12.1, Gradle 9.7.0 from the branch wrapper. Seed 18551C0FFEE18551.
- Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-17T20:20Z (claim).
- 2026-10-17T20:22Z: head run at 2a35fb87ff, JDK 21, seed 18551C0FFEE18551. Eight classes OK (0 failures, 0 errors, 0 skipped). TestReplicationHandler: 25 tests, all 25 skipped; no source annotation; the rule is to stop. Report: reports/ioctx-audit-test-round-1.md. Decision needed on the skip.
- Status: STOPPED (2026-10-17T20:22Z) on the skip decision. The branch is not verified.
- 2026-10-17T20:47Z: follow-up with tests.nightly=true on the head. TestReplicationHandler 25 of 25 pass, 0 failures, 0 errors, 0 skipped (fresh XML 20:42Z). Report updated: reports/ioctx-audit-test-round-1.md.
- Status: DONE (2026-10-17T20:47Z). All nine classes green on the head with JDK 21; the one skip is explained by @LuceneTestCase.Nightly and passes when nightly tests run.
