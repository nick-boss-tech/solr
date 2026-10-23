# Review: SOLR-18503, branch solr-18503-submit (unit 42)

Head reviewed: cc5b84dfcdd783ec2c2626aba4ba9931bb027893. Base: c8ab71430f. Ticket: SOLR-18503, "JSON Facet API: seven parser-accepted keys are missing from the reference guide".

## Pass 1

- Ticket fit. The ticket asks that seven parser-accepted JSON Facet keys be documented in the reference guide. The branch adds the reference-guide text (`json-facet-api.adoc`, not built by this gate) and a changelog entry.
- Finding, major (scope, owner, rung 5): the branch also changes code. `FacetRangeParser` reads `field` with `getField(m)` instead of `getString(m, "field", null)`. That accepts the short form `f` and throws `Missing 'field'` when neither is given; before, a missing field was null. The ticket text is about documentation and does not ask for this. The two range-facet tests fail on base, so the change is pinned, but whether it belongs in this ticket is the owner's call. Not repaired.
- Finding, minor: the reference-guide change is not built or checked by any gate on this branch.
- Repairs: none.

Regate: `gates/SOLR-18503-review-pass-1.md`, GATE GREEN at cc5b84dfcd on vm4.
