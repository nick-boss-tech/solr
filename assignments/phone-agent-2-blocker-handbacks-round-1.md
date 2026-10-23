# Assignment: finish the two handed-back Blocker candidates

Addressee: phone-agent-2. Claim-first as claims/phone-agent-2-blocker-handbacks-round-1.md. Deliverable: reports/phone-agent-2-blocker-handbacks-round-1.md. The evidence for both items is in your own round 1 report, reports/phone-agent-2-blockers-round-1.md; start from those two sections, not from scratch. Base: current origin/main. Both branches are yours from round 1; continue on them.

## SOLR-17840: finish the timeout fix

Branch solr-17840-preserve-partial-results-phone-agent-2 at 3999ff0be34bd192c2325b9138b8aa8a7b5bc24d. State at handback: the timeout defect is reproduced and two runtime loss/failure boundaries are proved; the candidate passed compile and the pre-fix proof but failed its full focused class. Task: complete the implementation so it addresses the demonstrated doc-values fetch and response-streaming timeout path, preserve the unchanged regression exactly as recorded, and validate the completed implementation: the focused class green, then the full module gate. If the completed fix changes the failure rather than removing it, hand it back with the new evidence instead of narrowing the claim.

## SOLR-18446: verify the corrected head

Branch solr-18446-path-routing-phone-agent-2 at 8bb60e6f61abc9827fb58dcae1863cabb33f9796. State at handback: the reported path and parameter precedence defect matches the report; the permitted gate retry failed compilation; the compiler-named missing import was corrected afterward and that head has never been verified. Task: verify the corrected head end to end: pre-fix proof, focused tests, full module gate, in that order, counted from the gate log and fresh JUnit XML. If it does not hold, hand it back with the failing evidence and the smallest next step you can name.

## Standing rules for both

Premise bar: very narrow fixes only; no hardening of working paths against hypothetical failures. If a premise collapses under the new evidence, clear the item as WON'T DO in the same push with the evidence attached. No Jira or GitHub posts, no PR openings, no ready flips. Commits as Nick Shanin, no trailers, no "Claude" in author, committer or trailer, no em dashes in any written text. New or changed tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4. Gate evidence stands on the run's log file and fresh JUnit XML on disk only.
