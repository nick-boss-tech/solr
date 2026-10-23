# Assignment: finish the handed-back Blocker candidate

Status: CLAIMED (claims/phone-agent-2-blocker-handbacks-round-1.md)

Addressee: phone-agent-2. Claim-first as claims/phone-agent-2-blocker-handbacks-round-1.md. Deliverable: reports/phone-agent-2-blocker-handbacks-round-1.md. The evidence for both items is in your own round 1 report, reports/phone-agent-2-blockers-round-1.md; start from those two sections, not from scratch. Base: current origin/main. Both branches are yours from round 1; continue on them.

## SOLR-17840: finish the timeout fix

Branch solr-17840-preserve-partial-results-phone-agent-2 at 3999ff0be34bd192c2325b9138b8aa8a7b5bc24d. State at handback: the timeout defect is reproduced and two runtime loss/failure boundaries are proved; the candidate passed compile and the pre-fix proof but failed its full focused class. Task: complete the implementation so it addresses the demonstrated doc-values fetch and response-streaming timeout path, preserve the unchanged regression exactly as recorded, and validate the completed implementation: the focused class green, then the full module gate. If the completed fix changes the failure rather than removing it, hand it back with the new evidence instead of narrowing the claim.

## SOLR-18446: moved, then returned to pending

Removed from this assignment at the owner's direction (2026-10-22) because the missing work is verification only (its corrected head has never been gated) and vm1 was to run its proof, focused tests and gate in session. That in-session attempt was refused by a policy check in the helper session on 2026-10-22 and did not run; no gate evidence exists for head 8bb60e6f61. The item is pending the owner's direction: a later retry in the vm1 session, or restoration to this assignment. Do not pick it up from this note alone.

## Standing rules

Premise bar: very narrow fixes only; no hardening of working paths against hypothetical failures. If a premise collapses under the new evidence, clear the item as WON'T DO in the same push with the evidence attached. No Jira or GitHub posts, no PR openings, no ready flips. Commits as Nick Shanin, no trailers, no "Claude" in author, committer or trailer, no em dashes in any written text. New or changed tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4. Gate evidence stands on the run's log file and fresh JUnit XML on disk only.
