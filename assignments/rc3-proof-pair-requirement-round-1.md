# Assignment: RC3 fix set proof pairs (standing requirement, round 1)

Status: OPEN
Tags: records, gate, premise-run

Owner's rule (2026-10-24): a fix counts as proven only with both legs at matched shape: the failure reproduced without the fix, and the same test passing with the fix, same class and seed set. A green head run alone is half a proof. This note applies the rule to the RC3 fix set; any host writing or extending a receipt for one of these items records both legs or marks the missing one.

## Pair status at posting (2026-10-24)

- PR #5098 (RecoveryAfterSoftCommitTest): no factory-run pair. The pre leg is external (the RC3 smoke failure; RC3 does not contain the PR) and the post leg is the PR author's own evidence. Cite it as his, not as factory proof.
- PR #5106 (TestCoordinatorRole.testNRTRestart): post leg running (vm6 final-head gate, 10 head runs at `a02cc7ba6b90`); the pre leg here is behavioral (the test passed only after its timeout, about 30s on main against about 7s at the head) and the same gate carries the base timing run. Pair completes when that receipt lands.
- PR #5192 (SOLR-18564): pair complete at the fix commit `d7caa8cb784` (2026-10-22 gate: the new test reproduces the signature on base code, the timeout throw plus 15 unreleased metrics contexts, and passes with the fix; TestCoreContainer 25 of 25). The exact-head gate at `dd65b21b41dc` re-runs the post leg; the changelog-only delta does not change test behavior.
- SOLR-18295 (ReplicationFactorTest routing): pair run (head 20 of 20; synthetic baseline 4 of 20) with the recorded caveat that the baseline failures are a different mechanism than the ticket's premise. The pair stands, the premise match is partial, and the receipt says so.
- SOLR-18586 (TestTlogReplica): pre leg done (reproduces at the RC3 tip and at main; seeds `3635CC89D23D0929` and `9D6BB87C4F145405` in `reports/rc3-tlogreplica-rootcause-round-1.md`). No fix exists yet; the peersync probe (`assignments/rc3-tlogreplica-peersync-probe-round-1.md`) is the bridge. Any future fix branch's gate job must run the matched pair at those seeds.
- SOLR-18587 (CoreAdminHandlerTest): neither leg yet; root-cause unit 2 is unclaimed.

## Work for the claiming host

Keep this file current: when a pair completes or a status changes, post a dated addendum rather than editing this file, and mirror the one-line status into the item's receipt. Owner-facing texts (the RC3 vote follow-up, PR descriptions) may call an item proven only when its pair is complete under this rule.
