# Gate job: SOLR-16013 interrupt-flag fix (hardening gate at the new head)

- Branch: solr-16013-submit. Head under gate: `4215c8cee5625fc20be7fce1e688bcce03d11fdb` (local on vm1, on top of `ecda7b862ba`; the fork tip stays `ecda7b862ba` until this gate greens and the push lands). Merge-base with upstream main: `14c7aac0d151`.
- Change at the head under gate: on top of the change recorded for `ecda7b862ba` below, `ZkController.close()` now also folds and clears an interrupt that lands during `zkStateReader.close()` (`interrupted |= Thread.interrupted()` at the head of the `finally`, before `zkClient.close()`), so the interrupt flag stays cleared through both session-ending closes and the existing innermost `finally` restores it once at the very end, after the client close and the close thread pool shutdown. Decision records: `material/held-items-decisions-2026-10-13.md` and the owner ruling of 2026-10-13 recorded in `receipts/SOLR-16013.md`.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.OverseerCloseOrderingTest` (3 tests at the head).
- Status: GATE PENDING at `4215c8cee5` (vm1 runner `gate-16013-flagclear.sh`, started 2026-10-13 ~18:10 MDT; gate log `g16013-flagclear-gate.log` and summary `g16013-flagclear-summary.log` on vm1; GATE PENDING entry in the main side's takeover log). The earlier gate at `ecda7b862ba` FAILED (vm2 run record below) and is not relaunched.
- Owner ruling (2026-10-13): the 2026-10-12 investigation's BRANCH-CAUSED classification (see `receipts/SOLR-16013.md`) governs the `ecda7b862ba` failure. The test-fixture route below is superseded: the prepared fixture patch (`gates/SOLR-16013-fixture-patch-round-1.patch`) is NOT pushed, its claim is closed SUPERSEDED BY OWNER DECISION 2026-10-13, and the production fix at the head under gate is the route taken.

## Steps (full hardening gate, as run by the vm1 runner)

1. Changelog YAML parse for `changelog/unreleased/SOLR-16013.yml` (unchanged file; parse at the head).
2. Tidy: `:solr:core:tidy` leaves 0 changed files afterward (`gradle/libs.versions.toml` restored). Any tree change after tidy marks the run GATE INVALID.
3. Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
4. Focused run at the head: `:solr:core:test --tests org.apache.solr.cloud.OverseerCloseOrderingTest` at seeds `2E201C17D50DCEAA` and `16013C0FFEE16013` (the two seeds this branch's gates have used; the failure is method-order dependent). Count the 3 tests from fresh JUnit XML at each seed.
5. Pre-fix proof (fail-before): revert only `solr/core/src/java/org/apache/solr/cloud/ZkController.java` to the merge-base version, keep the branch's test file, and run the new test `testZkClientClosesUninterruptedWhenInterruptArrivesAfterOverseerCloseFinishes` alone at seed `2E201C17D50DCEAA`. Record PASS (it fails without the fix), NOT_PROVEN (it passes without the fix), or INCONCLUSIVE (compile or setup failure), with the failure text. Restore the file afterward and re-verify the tree matches the head.
6. Module check: `:solr:core:check -x test`.

## On green

- Push `4215c8cee5` plainly to fork `solr-16013-submit` (fast-forward from `ecda7b862ba`).
- Refresh `receipts/SOLR-16013.md` at `4215c8cee5`, with the counts, the seeds, the tidy result, and the pre-fix verdict. The receipt must say the fix's justification is the JDK `FutureTask.get()` behavior (`lib/src.zip`, lines 187 to 192 of JDK 21.0.11), so a reader can check it.
- Mark this job DONE in the same push sequence.

## On failure

- Record the failing step, test and seed in the main side's takeover log. Do not relaunch; the failure goes back to the main agent for investigation.

## Limits for this gate

- The new test's fail-before depends on scheduling. The overseer close interrupts the shutdown thread and returns at once, and the edge is hit only when the interrupt arrives after the future is done. A NOT_PROVEN verdict is a real result. Record it and do not relaunch more than once for it.

## Run record at ecda7b862ba (vm2, 2026-10-13; FAILED, history)

- Changelog YAML OK, rc=0. Tidy rc=0, status clean. Error Prone compile rc=0.
- Pre-fix proof: NOT_PROVEN. With `ZkController.java` reverted to `14c7aac0d151`, `testZkClientClosesUninterruptedWhenInterruptArrivesAfterOverseerCloseFinishes` passed (BUILD SUCCESSFUL, `logs/vm2/16013-proof.log`).
- Focused run at head: rc=1. JUnit XML: 3 tests, 1 failure. `testZkClientClosesOnlyAfterOverseerCloseCompletes` failed with `java.lang.AssertionError: no node is running the overseer`, seed `C3E3D7F50937D13C` (`logs/vm2/16013-head.log`). This is the same shared-cluster ordering failure recorded for `ba26b70289`.
- Module check at that head passed (`logs/vm2/16013-check.log`).
- The job was not GREEN, so no receipt was written and no fork push was made.

## Failure investigation (vm2, 2026-10-13T17:00Z; classification superseded by the owner ruling above)

- Failing test: OverseerCloseOrderingTest.testZkClientClosesOnlyAfterOverseerCloseCompletes, line 62, "no node is running the overseer", seed C3E3D7F50937D13C (logs/vm2/16013-head.log lines 91 to 97). Module check at the same head passed (logs/vm2/16013-check.log).
- Classification (vm2): test-fixture defect, probable. The class shares one two-node cluster from @BeforeClass. Earlier tests stop an overseer node and never restart it, and the failing test's precondition assumes an overseer left by the tests before it. Not proven: why no overseer started within the 60 second wait.
- Fix route recorded at the time: assignments/pool-vm2-16013-head-failure-fixture-patch.md. SUPERSEDED by the owner ruling of 2026-10-13: the branch-caused classification from the 2026-10-12 investigation governs, the fixture patch is not pushed, and the production fix at the head under gate is the route taken.
- Note: the same ordering failure was recorded at ba26b70289, where the main side's investigation reproduced it deterministically and classified it branch-caused (see `receipts/SOLR-16013.md`).
