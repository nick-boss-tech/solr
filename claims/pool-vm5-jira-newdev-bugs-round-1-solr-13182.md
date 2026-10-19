# Claim: pool-vm5-jira-newdev-bugs-round-1, slice SOLR-13182 (item 1 only)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Slice: item 1 only, SOLR-13182. Items 2 to 15 are not claimed; they are open for any pool Linux host to claim by slice.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 1.
- Capability tags used: implementation, gate.
- Branch: `solr-13182-child-null-parent` from apache/main `c8ab71430f` (fix and regression test in progress; gate running on vm5; not pushed yet).
- Started: 2026-10-19T12:45:00Z (UTC). Narrowed to one slice: 2026-10-19T12:50:52Z (UTC).

Heartbeat: 2026-10-19T12:50:52Z

Result: 2026-10-19T13:25:17Z: item 1 green. Branch `solr-13182-child-null-parent` (new branch, pushed) at `2cf82a0b9464`, from apache/main `c8ab71430f`: commit 785b430af4 (null check in BlockJoinChildQParser.noClausesQueryLegacy throws a 400 SyntaxError) and 2cf82a0b94 (regression test testChildQueryWithNoClausesAndNoParentFilterIsBadRequest uses `{!child of=""}`), plus changelog/unreleased/SOLR-13182-child-null-parent.yml. Proof leg (fix reverted to base): BJQParserTest fails with NullPointerException: Query must not be null. Gate steps: changelog YAML rc 0, tidy rc 0, Error Prone compile rc 0, proof rc 1 (expected), focused BJQParserTest rc 0 with 19 tests, 0 failures, 0 errors, 0 skipped (fresh XML, seed 13182C0FFEE13182), module check rc 0. Logs: /workspace/gates/logs/vm5/13182-child-null-3/. Earlier gate runs 13182-child-null (test without of: passed on base, so not a proof) and 13182-child-null-2 (host lock busy, invalid) are superseded.

DONE: 2026-10-19T13:25:17Z. Deliverable: branch `solr-13182-child-null-parent` at `2cf82a0b9464`. Items 2 to 15 are not claimed by vm5.
