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

## Addendum 2026-10-27 (vm4): pair status from the receipts on coordination-ui

Checked against the receipts named below as of board tip 0271f42464. This section is records only; the table above is unchanged.

| Item | Pre leg (fails without the fix) | Post leg (passes with the fix) | Status |
| --- | --- | --- | --- |
| PR 5098 (RecoveryAfterSoftCommitTest) | `receipts/SOLR-18530.md`: the unmodified base ran 5 times at CI seed A57A22E346C237C6 with 0 failures, so no factory pre leg. The receipt does not name PR 5098, so the link is unconfirmed. | `receipts/SOLR-18530.md`: gate green at the branch head (2026-10-10). | Missing pre leg. The PR author's evidence stays cited as his, per this assignment. |
| PR 5106 (TestCoordinatorRole.testNRTRestart) | `receipts/SOLR-16630.md`: base at merge base `3f5d4c5bf8`, CI seed 681E2A715B2CE1D3, run 1 FAILED (vm1, 2026-10-11). | `receipts/SOLR-16630.md`, final head `a02cc7ba6b9`: 10 of 10 whole-class runs, 9 tests each, 0 failures, `testNRTRestart` passes every run (vm6). | Complete. Host differs (pre vm1, post vm6); the receipt records both. |
| PR 5192 (SOLR-18564) | Not located on the board. The assignment cites the 2026-10-22 gate at `d7caa8cb78`; `claims/solr-18564-exact-head-gate-round-1.md` names that commit as the fix but has no base run. | `receipts/SOLR-18564.md`: exact head `dd65b21b41dc`, changelog-only delta over `d7caa8cb78`; 2 seeds x 2 classes, 0 failures. | Pending. Post leg present; the base leg needs its record linked before the pair counts as complete. |
| SOLR-18295 (ReplicationFactorTest routing) | Synthetic baseline on the test branch, `receipts/SOLR-18295.md`: 16 of 20 pass, 4 `ClosedChannelException` (seeds 0001, 0008, 000B, 0010). | Same receipt, head `b273b24511`: 20 of 20 fresh whole-class runs pass. | Complete, with the receipt's caveat: the baseline is synthetic and the premise match is partial. |
| SOLR-18586 (TestTlogReplica) | Reproduces at seeds 3635CC89D23D0929 and 9D6BB87C4F145405 (per the assignment; `reports/rc3-tlogreplica-rootcause-round-1.md` not reopened here). Also NFS failures at seed 5EC47295A32A8342 in `receipts/rc3-tlog-matched-pairs-round-1-vm5.md`, but those are individual tests, not a class-level pre run. | `receipts/rc3-tlog-matched-pairs-round-1-vm5.md`: copy-instead-of-hardlink fallback (commit `fdb785484c6`), full `TestTlogReplica` at seed 5EC47295A32A8342: pass, 14 run, 2 skipped, 0 EMLINK. | Pending. The "no fix exists yet" line is stale: a fallback fix has a post leg on vm5. The matched class-level pre leg at 5EC47295A32A8342 is missing, and the fix branch is not named in the receipt. |
| SOLR-18587 (CoreAdminHandlerTest) | Present: `reports/rc3-coreadmin-deleteinstance-rootcause-round-1.md` (commit `c7d3b38ac231`) reproduces `testDeleteInstanceDirAfterCreateFailure` at the RC3 tip and at apache/main, seed 787C6229591C6F1E. | Missing. Root cause is not established, so no fix exists. | Pre leg only. The standalone reproduction unit is WON'T DO (vm5 claim), since this report already covers it. |

Limits: each row is taken from the receipt or report named in it, not re-run. No new runs were made for this addendum.
