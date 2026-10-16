# Assignment: SOLR-13059 test fix, round 1

Status: OPEN

Capability tags: [implementation, gate]. Intended host: either Linux host (vm1 or vm2); Windows never claims build work. Posted by the main side, 2026-10-16, from the round 1 diagnosis (reports/solr-13059-diagnosis-round-1.md).

Claim first: before starting, file a claim under `claims/` naming this assignment, per WORKFLOW.md. Heartbeat and staleness follow the pool rules.

## Grounding

- Diagnosis (round 1, 2026-10-15, on solr-13696-submit at da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9): testSliceRouting does not reproduce (0 failures in 12 dedicated runs across two matrices; the string "no core retrieved" appears nowhere in the evidence). testPreemptiveCreation fails as a test polling and timing race: 1 of 6 this round, 3 of 12 combined with the 2026-10-09 matrix at the same head and seeds. The failing assertion snapshots the exact collection count while an asynchronous preemptive creation is still landing; the ninth collection in the failing run is the next time slice, created and visible. The worker lambdas still catch only Exception, so an in-worker AssertionError escapes the threadExceptions capture. Full record: reports/solr-13059-diagnosis-round-1.md; the earlier brief is solr-13059-diagnosis-brief-2026-10-09.md in the main host's goal records.
- Branch: solr-13696-submit (the SOLR-13696 repair branch; PR #5088 is its live PR). Verify the tip by ls-remote at claim time and record it in the claim; if it has moved past da4fa6df1178, re-check that the test file still matches the diagnosis before editing.
- Test: org.apache.solr.update.processor.TimeRoutedAliasUpdateProcessorTest, module :solr:core. Grep the branch for the file and copy its exact package from the grep output before composing any run command; compose the run in a separate step.

## Scope

Test code only; no production change is authorized under this assignment.

1. Make testPreemptiveCreation's exact-count assertions settled-state assertions: assert on the state after preemptive creation has settled (poll for the expected settled state with a bounded wait), not on a snapshot taken while creation is in flight. Keep what the test proves about preemptive creation; do not weaken it into a no-op.
2. Record Throwable (not only Exception) from the worker lambdas, so an in-worker AssertionError lands in the threadExceptions capture instead of escaping it.
3. With 1 and 2 in place, evaluate removing the Time class's class-level @AwaitsFix(SOLR-13059): run the class with the awaitsfix group enabled across the diagnosis seed set (the six seeds in reports/solr-13059-diagnosis-round-1.md) plus repeated normal-mode runs. Remove the annotation only if the awaitsfix runs are clean across the set; if any run fails, keep the annotation, record the failure, and stop at that step.

## Interactions to respect

- SOLR-13943 stacks on this branch and its decided shape keeps the original class's @AwaitsFix(SOLR-13059) untouched on the stacked tree. A removal here lands on the 13696 branch first; the 13943 rebase absorbs it later. Do not edit the 13943 branch under this assignment.
- The branch backs a live PR; pushes to it carry standing authorization, but only at a gated head (below).

## Gate

Full hardening gate at the resulting head before any push: changelog YAML parse (the branch's fragment, if touched), tidy with the GATE INVALID guard, Error Prone compile (-Pvalidation.errorprone=true), the awaitsfix seed-set runs and normal-mode runs from step 3 counted from fresh JUnit XML, the focused routed-alias classes the 13696 receipt names, and module check :solr:core:check -x test. Refresh receipts/SOLR-13696.md at the new head on green. An executed-test failure is recorded and investigated before any relaunch; it is not relaunched unchanged.

## On completion

Mark this assignment DONE and the claim DONE, naming the new head, whether the annotation was removed, and the run counts behind that call. Append one line to the takeover log (files/remaining-branches-takeover-log.md in the main host's goal records).
