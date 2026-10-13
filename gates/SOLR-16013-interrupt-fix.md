# Gate job: SOLR-16013 interrupt-flag fix (hardening gate at the new head)

- Branch: solr-16013-submit. Head: `ecda7b862ba` (pushed 2026-10-13, fast-forward from `408317d7e24`). Merge-base with upstream main: `14c7aac0d151` (confirm at claim time with `git merge-base upstream/main ecda7b862ba`).
- Change: `ZkController.close()` clears the interrupt flag after the overseer wait loop and before `zkStateReader.close()`; the existing `finally` restores it. New test `OverseerCloseOrderingTest.testZkClientClosesUninterruptedWhenInterruptArrivesAfterOverseerCloseFinishes`. Decision record: `material/held-items-decisions-2026-10-13.md`.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.OverseerCloseOrderingTest` (3 tests at the head).
- Intended host: vm2 or vm1, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN. Not yet run. Pushed ungated, as the coordinator handoff allows for a patch the gate then verifies.

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
