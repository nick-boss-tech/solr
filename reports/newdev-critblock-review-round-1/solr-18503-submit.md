# Review: SOLR-18503, branch solr-18503-submit (unit 42)

Head reviewed: cc5b84dfcdd783ec2c2626aba4ba9931bb027893. Base: c8ab71430f. Ticket: SOLR-18503, "JSON Facet API: seven parser-accepted keys are missing from the reference guide".

## Pass 1

- Ticket fit. The ticket asks that seven parser-accepted JSON Facet keys be documented in the reference guide. The branch adds the reference-guide text (`json-facet-api.adoc`, not built by this gate) and a changelog entry.
- Finding, major (scope, owner, rung 5): the branch also changes code. `FacetRangeParser` reads `field` with `getField(m)` instead of `getString(m, "field", null)`. That accepts the short form `f` and throws `Missing 'field'` when neither is given; before, a missing field was null. The ticket text is about documentation and does not ask for this. The two range-facet tests fail on base, so the change is pinned, but whether it belongs in this ticket is the owner's call. Not repaired.
- Finding, minor: the reference-guide change is not built or checked by any gate on this branch.
- Repairs: none.

Regate: `gates/SOLR-18503-review-pass-1.md`, GATE GREEN at cc5b84dfcd on vm4.

## Pass 2

Review (2026-10-23, vm4), same head cc5b84dfcd. - Pass 1 major finding, unchanged and confirmed in the code: `FacetRangeParser` now uses `getField`, which accepts the `f` short form and throws `Missing 'field'`. Owner call on scope.
- New, minor: no test covers either new behaviour. `TestJsonRangeFacets.java` has no use of the `f` key and no missing-field assertion (checked by search at head cc5b84dfcd).
- Pass 1 minor finding, unchanged: the reference-guide text is not built by any gate.
- Repairs: none.

Regate: `gates/SOLR-18503-review-pass-2.md`, GATE GREEN at cc5b84dfcd on vm4.

## Pass 3

Review (2026-10-23, vm4), same head cc5b84dfcd. - Regate on the same head cc5b84dfcd. No new findings in pass 3.
- Pass 1 and pass 2 findings stand: `getField` scope call is open with the owner, and no test pins the `f` alias or the missing-field error.
- Repairs: none.

Regate: `gates/SOLR-18503-review-pass-3.md`, GATE GREEN at cc5b84dfcd on vm4.

Final head: cc5b84dfcdd783ec2c2626aba4ba9931bb027893. Unit 42 DONE with the three pass sections.
