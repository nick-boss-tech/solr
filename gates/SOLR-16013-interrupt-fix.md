# Gate job: SOLR-16013 interrupt-flag fix (hardening gate at the new head)

- Branch: solr-16013-submit. Head: `ecda7b862ba` (pushed 2026-10-13, fast-forward from `408317d7e24`). Merge-base with upstream main: `14c7aac0d151` (confirm at claim time with `git merge-base upstream/main ecda7b862ba`).
- Change: `ZkController.close()` clears the interrupt flag after the overseer wait loop and before `zkStateReader.close()`; the existing `finally` restores it. New test `OverseerCloseOrderingTest.testZkClientClosesUninterruptedWhenInterruptArrivesAfterOverseerCloseFinishes`. Decision record: `material/held-items-decisions-2026-10-13.md`.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.OverseerCloseOrderingTest` (3 tests at the head).
- Intended host: vm2 or vm1, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: FAILED at `ecda7b862ba` (vm2 gate run 2026-10-13T16:24Z-16:49Z; not relaunched). Classified by vm2 2026-10-13T17:00Z as a probable test-fixture defect (shared two-node cluster; see the failure investigation below). Next step owned by vm2: assignments/pool-vm2-16013-head-failure-fixture-patch.md (patch by the review coordinator, then the full hardening gate at the new head). Pushed ungated, as the coordinator handoff allows for a patch the gate then verifies.

## Run record (vm2, 2026-10-13)

- Changelog YAML OK, rc=0. Tidy rc=0, status clean. Error Prone compile rc=0.
- Pre-fix proof (step 5): NOT_PROVEN. With `ZkController.java` reverted to `14c7aac0d151`, `testZkClientClosesUninterruptedWhenInterruptArrivesAfterOverseerCloseFinishes` passed (BUILD SUCCESSFUL, `logs/vm2/16013-proof.log`).
- Focused run at head (step 4): rc=1. JUnit XML: 3 tests, 1 failure. `testZkClientClosesOnlyAfterOverseerCloseCompletes` failed with `java.lang.AssertionError: no node is running the overseer`, seed `C3E3D7F50937D13C` (`logs/vm2/16013-head.log`). This is the same shared-cluster ordering failure recorded for `ba26b70289`.
- Module check (step 6): still running when this record was written. Its rc is in `logs/vm2/16013-check.log` and the gate's GATE END line.
- The job is not GREEN, so no receipt is written and no fork push is made.

## Steps (full hardening gate)

1. Changelog YAML parse for `changelog/unreleased/SOLR-16013.yml` (unchanged file; parse at the head).
2. Tidy: `gradlew tidy` leaves 0 changed files afterward. Record any file it rewrote.
3. Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
4. Focused run at the head: `:solr:core:test --tests org.apache.solr.cloud.OverseerCloseOrderingTest`. Count the 3 tests from fresh JUnit XML and record the seed.
5. Pre-fix proof (fail-before): revert only `solr/core/src/java/org/apache/solr/cloud/ZkController.java` to the merge-base version, keep the branch's test file, and run the new test alone. Record PASS (it fails without the fix), NOT_PROVEN (it passes without the fix), or INCONCLUSIVE (compile or setup failure), with the failure text.
6. Module check: `:solr:core:check -x test`.

## On green

- Refresh `receipts/SOLR-16013.md` at `ecda7b862ba`, with the counts, the seed, the tidy result, and the pre-fix verdict.
- Mark this job and its claim DONE in the same push sequence.

## Limits for this gate

- The new test's fail-before depends on scheduling. The overseer close interrupts the shutdown thread and returns at once, and the edge is hit only when the interrupt arrives after the future is done. A NOT_PROVEN verdict is a real result. Record it and do not relaunch more than once for it.
- The receipt must say the fix's justification is the JDK `FutureTask.get()` behavior (`lib/src.zip`, lines 187 to 192 of JDK 21.0.11), so a reader can check it.

## Failure investigation (vm2, 2026-10-13T17:00Z)

- Failing test: OverseerCloseOrderingTest.testZkClientClosesOnlyAfterOverseerCloseCompletes, line 62, "no node is running the overseer", seed C3E3D7F50937D13C (logs/vm2/16013-head.log lines 91 to 97). Module check at the same head passed (logs/vm2/16013-check.log).
- Classification: test-fixture defect, probable. The class shares one two-node cluster from @BeforeClass. Earlier tests stop an overseer node and never restart it, and the failing test's precondition assumes an overseer left by the tests before it. Not proven: why no overseer started within the 60 second wait. A production question is open only if the patch does not clear the failure.
- Fix route: assignments/pool-vm2-16013-head-failure-fixture-patch.md. Recorded here as the next step, not relaunched.
- Note: the same ordering failure was recorded at ba26b70289. The head ecda7b862b is pushed and red until the patch lands.
