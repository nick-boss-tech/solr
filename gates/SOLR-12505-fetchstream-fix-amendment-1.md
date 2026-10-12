# Gate job amendment 1: SOLR-12505 FetchStream fix, direction after the failed gate at d81a77311773

- Job amended: gates/SOLR-12505-fetchstream-fix.md. Read it first; this amendment does not restate the defect, the fix spec, the proof shape or the gate steps, which stand as written there.
- Branch: solr-12505-submit. Fork tip: 5f20e1171bc890509d76e4f5f7defcc4fc8c102c (unchanged). The failed fix head d81a77311773 exists only on vm2 and was never pushed.
- Intended host: vm2, which holds the failed fix head and the claim (claims/pool-vm2-gate-backlog-12505.md). No other host claims this job; the amendment is worked under that claim.
- Status: OPEN (direction recorded 2026-10-12 by the main side, from its investigation of the failed gate).
- Capability tags: implementation, gate.

## Investigation record (main side)

- Compile leg: `:solr:solrj-streaming` compileTestJava with Error Prone and `-Werror` failed at StreamDecoratorTest.java:1033, which trips AssertThrowsMultipleStatements ("lambda passed to assertThrows should contain exactly one statement"). Gate log: logs/vm2/queue-g12505-gate-20261012T213052Z.out, GATE END 2026-10-12T21:45:47Z. Classification: a test-source Error Prone violation at compile. Not a product failure, and not a premise result.
- Focused head leg: 57 tests, 1 failure. The new test, testFetchStreamRejectsJoinFieldNameThatBreaksLocalParams, failed with "Unable to construct instance of FetchStream". The proof leg failed with the same construction error. The FetchStream constructor runs before the batch query is built, so a construction error cannot show the corrupted-batch premise the fix has to prove (reports/gate-status-12505-18134-round-1.md). The proof is not established.
- Route: the original job lets the claimant pick escape or validate, and requires the choice and its reason recorded in the job file and in the receipt. Neither records it (reports/gate-status-12505-18134-round-1.md), so no route is on record for the patch at d81a77311773.
- Relaunch rule: the failed gate is not relaunched by anyone else, not by the queue cron and not by another host. It is re-run only by vm2 under this amendment, after the directions below are carried out.

## Directions to vm2

1. Restructure the new test so each `assertThrows` lambda holds exactly one statement, as the Error Prone rule requires: extract a helper method or use a single-expression lambda. Keep the assertion's intent: a join field name that breaks out of the `df=` local-params value must be rejected, by failing with the clear validation error (validate route) or by fetching without corrupting the batch (escape route), matching the route the patch implements.
2. Fix the construction error in the new test's setup so the stream constructs the way the existing fetch tests in StreamDecoratorTest construct it, and record the cause in gates/SOLR-12505-fetchstream-fix.md. If the cause turns out to be in production code rather than the test setup, stop and record that in the original job file instead of working around it; no production change beyond the FetchStream fix is part of that job.
3. Record the route: in gates/SOLR-12505-fetchstream-fix.md and in receipts/SOLR-12505.md, state which production route the patch took (escape the `on=` field name, or validate it and fail with a clear error) and the reason for the choice.
4. Re-run the full gate from gates/SOLR-12505-fetchstream-fix.md at the new head: changelog YAML parse; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; re-run tidy on the final tree after the last edit, before committing); Error Prone compile for `:solr:solrj-streaming` with `-Pvalidation.errorprone=true`; the proof leg (FetchStream.java reverted to 5f20e1171bc, the branch tests kept; the new test must fail there by content, the corrupted batch, not by a construction error); focused StreamDecoratorTest counted from fresh JUnit XML; module check `:solr:solrj-streaming:check -x test`. Record the seed used in the original job file.
5. On green: push the fix commits plainly (fast-forward) to solr-12505-submit, refresh receipts/SOLR-12505.md at the new head, do the draft update the original job's on-green section names, and mark the original job, the claim, and this amendment DONE.

## Step checklist

- [ ] new test restructured to one statement per `assertThrows` lambda; assertion intent unchanged
- [ ] construction error fixed in the test setup; its cause recorded in gates/SOLR-12505-fetchstream-fix.md
- [ ] production route (escape or validate) and its reason recorded in gates/SOLR-12505-fetchstream-fix.md and receipts/SOLR-12505.md
- [ ] full gate re-run at the new head per gates/SOLR-12505-fetchstream-fix.md, proof leg failing by content on reverted FetchStream
- [ ] pushed plainly to solr-12505-submit; receipt refreshed; draft updated; original job, claim and this amendment DONE
