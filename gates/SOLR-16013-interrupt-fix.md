# Gate job: SOLR-16013 interrupt-flag fix plus fixture fix (hardening gate at the new head)

- Branch: solr-16013-submit. Head under gate: `37b01337eb4c6d5af6d2a6fdf14af3a2ff2d75a7` (local on vm1, on top of the production head `4215c8cee5`, which is on top of `ecda7b862ba`; the fork tip stays `ecda7b862ba` until this gate greens and the push lands). Merge-base with upstream main: `14c7aac0d151`.
- Change at the head under gate: test-only, on top of the production change at `4215c8cee5` recorded below. The flag-clear gate at `4215c8cee5` FAILED on a test-fixture design defect (see `receipts/SOLR-16013.md`, "Flag-clear gate failure investigation"): the class shares one 2-node cluster, every method stops the overseer node, and `stopJettySolrRunner` removes it from the runner list, so the method that ran third opened with no live node and failed at its opening `awaitOverseerNode()` assert on any production code, including the merge-base. The fixture fix is the parked patch `gates/SOLR-16013-fixture-patch-round-1.patch` applied unchanged: each method's `finally` restarts the node it stopped (`cluster.startJettySolrRunner(...)`, guarded by `!stopper.isAlive()`) once the stop completes, so the next method finds two running nodes. One test file changed, 16 insertions and 2 deletions; no production file changes at this commit.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.OverseerCloseOrderingTest` (3 tests at the head).
Status: DONE
- Owner ruling (2026-10-13, reversing the earlier ruling recorded further below): the fixture route is approved on the strength of the isolation investigation (receipt commit `014da90573`). The production fix stands for the failure the 2026-10-12 investigation classified BRANCH-CAUSED; the flag-clear failure is the fixture defect, and the fixture patch is the route taken after all. Its claim stays closed; this job supersedes the flag-clear gate job at `4215c8cee5`.

## Steps (full hardening gate, as run by the vm1 runner)

1. Changelog YAML parse for `changelog/unreleased/SOLR-16013.yml` (unchanged file; parse at the head).
2. Tidy: `:solr:core:tidy` leaves 0 changed files afterward (`gradle/libs.versions.toml` restored). Any tree change after tidy marks the run GATE INVALID.
3. Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
4. Focused run at the head: `:solr:core:test --tests org.apache.solr.cloud.OverseerCloseOrderingTest` at seeds `2E201C17D50DCEAA` and `16013C0FFEE16013` (the two seeds this branch's gates have used; the failure is method-order dependent). Count the 3 tests from fresh JUnit XML at each seed; all 3 must pass at both seeds.
5. Pre-fix proof (fail-before): revert only `solr/core/src/java/org/apache/solr/cloud/ZkController.java` to the merge-base version, keep the branch's full test file, and run the full class at seed `2E201C17D50DCEAA`. Expected shape, from isolation run (c): `testZkClientStaysOpenWhenShutdownThreadIsInterrupted` and `testZkClientClosesOnlyAfterOverseerCloseCompletes` fail fast with only their premise messages ("ZooKeeper client closed after the shutdown thread was interrupted, before the overseer finished closing"; "ZooKeeper client closed before the overseer finished closing"). Record the third method's outcome as observed: on base production its behavior is the fixture defect itself, and the proof verdict (PASS, NOT_PROVEN or INCONCLUSIVE) rests on the two premise methods failing by message content. Restore the file afterward and re-verify the tree matches the head.
6. Module check: `:solr:core:check -x test`.

## On green

- Push `37b01337eb` plainly to fork `solr-16013-submit` (fast-forward from `ecda7b862ba`, carrying `4215c8cee5` and `37b01337eb`).
- Refresh `receipts/SOLR-16013.md` at `37b01337eb`, with the counts, the seeds, the tidy result, and the pre-fix verdict. The receipt must say the production fix's justification is the JDK `FutureTask.get()` behavior (`lib/src.zip`, lines 187 to 192 of JDK 21.0.11), so a reader can check it.
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
