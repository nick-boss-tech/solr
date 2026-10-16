# Pool assignment: SOLR-12505 review fix round 1 (narrow the fetch join field name check, reject a leading '$')

Status: DONE (GATE GREEN at bd6c16d9164fb923991c3aed0732bbb31186d0ff, 2026-10-16)

- Capability tags: implementation, gate.
- Intended host: vm1. Claimed by the vm1 implementation lane at the direction of the main agent, 2026-10-16.
- Branch: solr-12505-submit at 248a9715b1c3b00793fc4efca58738894c9acd65.

## Direction

A review of the opened PR found a gap in the branch's join-field validation in FetchStream: the local-params value reader dereferences a leading $ as a request parameter, and the wide rejection set overstates what breaks the parse. Fix shape, verified against the source by the main side:

1. Narrow FetchStream.containsBatchBreakingChar to the true breaking set: whitespace or '}' anywhere in the name; a leading '\'', '"' or '$' only at position 0. The exception message is updated to name exactly that set, and the code comment states the parser reason (an unquoted literal is ended by whitespace or '}', a leading quote starts a quoted string, a leading $ dereferences a request parameter).
2. One new test in StreamDecoratorTest beside the existing rejection tests: on="id=$a_i" is rejected, with the message containing "invalid join field name [$a_i]".
3. Nothing else changes: no other production files, no other tests, no changelog edit.

## Gate

Blocking-flock runner on vm1 (solr-gradle.sh only): changelog YAML parse; :solr:solrj-streaming:tidy with the GATE INVALID guard; Error Prone compile of main and test sources; delta proof (production FetchStream.java reverted to 248a9715b1c3 with the new test kept; the new test must fail by content, the rejection absent); focused StreamDecoratorTest at the new head counted from fresh JUnit XML; :solr:solrj-streaming:check -x test. Push the fix commit plainly to the fork only on a fully green gate. Outcome recorded in receipts/SOLR-12505.md.

## Outcome (2026-10-16, vm1)

DONE, GATE GREEN. Fix commit bd6c16d9164fb923991c3aed0732bbb31186d0ff pushed plainly to solr-12505-submit (fast-forward from 248a9715b1c3, ls-remote verified). Runner ~/workspace/tools/g12505-reviewfix-gate.sh, log g12505-reviewfix-gate.log, seed 12505C0FFEE12505: changelog parse rc 0; tidy clean; Error Prone compile rc 0; delta proof by content (the new $ test on the pre-fix production fails with the server's dereference error, "Missing param a_i while parsing function '$a_i,_version_'"); head run StreamDecoratorTest 59 tests, 5 skipped, 0 failures from fresh JUnit XML; module check rc 0. Receipt: receipts/SOLR-12505.md top section. Flagged for the main side: overstated sentences in pr-drafts/streaming/SOLR-12505.md, quoted in the lane's report.
