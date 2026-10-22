# Decisions from vm5's status assignment (2026-10-22), settled on the decision ladder

Recorded by vm2, 2026-10-22T05:50:04Z (UTC). Source: `assignments/vm5-status-for-nick-2026-10-22.md`. Ladder: `material/decision-ladder.md`.

## 1. Delete the banked branch solr-12743-submit

- Rung used: 1 (history) and 4 (choice).
- Evidence: the branch has 2 commits past c8ab71430f. Its last commit is "add open-questions note for the owner (remove before the PR)". The ticket is not on main and vm5 found the patched class absent on the fork pin and upstream main.
- Choice: keep the branch. Deleting it removes the only copy of the work, and nothing requires the deletion. Agents do not delete branches.
- Owner: not needed unless Nick wants the branch deleted; then that is his call.

## 2. Add the matching solr.cmd default on the 9.x line (SOLR-18552)

- Rung used: 1 (history).
- Evidence: the main-line commit a5406c9fe5 changed both `solr/bin/solr` and `solr/bin/solr.cmd` (plus the changelog). The 9.x backport cd7cc695fb changed only `solr/bin/solr`. So the 9.x line diverges from main on Windows.
- Choice: yes, add the same two-line default to `solr.cmd` on the 9.x line, matching main (vm5's recommendation).
- Follow-up: a 9.x implementation item on `solr-18552-sharedarena-9x`, gated like any change. Not done here. No PR.

## 3. Retire or keep solr-14200-submit, solr-15326-submit, solr-17433-17143-submit

- Rung used: 1 (history and records).
- Evidence: each branch has unique commits past c8ab71430f and none is merged to main. SOLR-14200 is HANDED BACK for implementation by vm5's review, so its branch is the work still owed. SOLR-15326 has a DONE review (receipts/SOLR-15326.md). SOLR-17433/17143 has one reconciled commit.
- Choice: keep all three. "Retire" means a status change, and no ticket here is closed or superseded. Retire a branch only when its ticket is closed or superseded in a record.

## 4. SOLR-16155: open a PR?

- Rung used: 1 (upstream) and 4 (choice).
- Evidence: upstream apache/solr PR #1151 (SOLR-16155 DocumentBuilder should not include field values in error msgs) is open at this check. The fork's receipt (receipts/SOLR-16155.md) shows the change passes its gate. A second PR for the same ticket would duplicate #1151.
- Choice: do not open a PR. The existing upstream PR covers the ticket, and PR opening is an owner act regardless.

## Lost-alias re-gate (SOLR-13943 / SOLR-18562), checked at the same time

- Receipt: none. receipts/SOLR-13943.md has only the vm3 re-gate of 2026-10-19 at head aa14590303, which predates the regression test.
- Claim: claims/regate-solr-13943-lost-alias-vm5.md is ACTIVE by vm5 (started 2026-10-22T04:38Z). No heartbeat or result is recorded. solr-13943-submit is still at aa14590303, so the test has not been pushed.
- Upstream fix: PR #5191 (SOLR-18562, head 07d000c2b3) is open, author SpookyJumpyBeans, created 2026-10-22T02:22Z.
- Conflict probe (read only, real trial merge, aborted): origin/solr-17680-submit conflicts with both the PR 5088 head (960355fce3) and the PR 5089 head (aa14590303). Conflicting files: `solr/core/src/java/org/apache/solr/handler/admin/api/CreateAlias.java` and `solr/core/src/test/org/apache/solr/handler/admin/api/CreateAliasAPITest.java`.
- Regression test: vm2 does not add it to #5089 while vm5's claim is live. The claim goes stale 2 hours after its last heartbeat (04:38Z), so it becomes takeable at 06:38Z.
