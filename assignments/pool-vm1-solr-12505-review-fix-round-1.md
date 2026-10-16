# Pool assignment: SOLR-12505 review fix round 1 (narrow the fetch join field name check, reject a leading '$')

Status: CLAIMED (vm1, 2026-10-16)

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
