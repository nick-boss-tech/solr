# solr-6193-submit

- Branch: origin/solr-6193-submit
- Head: ec94bf50c80 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base c3cdf7b46e8c, 26 commits behind, 4 commits ahead)
- Scope: 4 commits, 5 files. `PivotFacet.java` (+3/-1: passes the `facet.pivot` local params into `createFromListOfNamedLists`), `PivotFacetField.java` (local params layered over request params, `wrapDefaults(localParams, rb.req.getParams())`, plus a new overload), `PivotFacetValue.java` (+4, `getParentPivot()` accessor), `DistributedFacetPivotLargeTest.java` (the "Broken: SOLR-6193" cases are re-enabled and new `{!key=...}` cases added), changelog `SOLR-6193-pivot-facet-local-params.yml` (`type: fixed`). No `SOLR-6193-TESTING.md` on the tip.
- Verdict: Needs work (the patch fixes the distributed pivot path; the ticket's own `facet.field` path is not touched)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-6193-review.md` (verdict Needs work) was written at the same head (ec94bf50c80). No delta.

- Bulk F1 (HIGH, the ticket's facet.field case is not covered): **confirmed**, and stronger than the bulk wording. The JIRA summary itself names `facet.field` (`research/jira-context/SOLR-6193.json`, `Summary`). See finding 1.
- Bulk F2 (MEDIUM, local vs per-field precedence unproven): **confirmed.** See finding 2 and owner call 1.

## Findings (ranked)

1. **HIGH, verified. The ticket's path is not changed.** The JIRA summary is "using facet.* parameters as local params inside of facet.field causes problems in distributed search". The diff's file list has no `facet.field` code: no `FacetComponent`, no `FacetProcessor`, no field-facet change. Every changed production file is in the pivot path (`PivotFacet.java`, `PivotFacetField.java`, `PivotFacetValue.java`). The regression cases re-enabled and added in `DistributedFacetPivotLargeTest` (diff hunks at `:274-279`, `:554-577`, `:757`, `:787`, `:814`) all use `facet.pivot`. So the patch closes a related pivot case, not the reported `facet.field` case. Whether upstream `facet.field` already handles local params was not checked here, so this is a scope gap, not a proven failure. Confirm the intended scope with the owner. If the ticket stays open, add a distributed `facet.field={!key=... facet.offset=...}` regression and fix that path.

2. **MEDIUM, verified by reading the tests; precedence not decided. Local params versus per-field params has no conflict case.** The parameter view is `SolrParams.wrapDefaults(localParams, rb.req.getParams())` (`PivotFacetField` constructor). The per-field lookups use `getFieldInt(field, FACET_LIMIT, ...)`, which checks `f.<field>.facet.limit` before `facet.limit`, per `SolrParams` field-parameter semantics (not re-read here). So a request-level `f.<field>.facet.limit` beats a local `{!facet.limit=4}`, and the code does not say that is intended. The added cases compare a local `facet.limit`/`facet.sort` with a global `facet.limit` (`DistributedFacetPivotLargeTest.java:277-279`). No case sets a local `facet.limit` and a conflicting `f.<field>.facet.limit`. The JIRA comment that raises this question is in `research/jira-context/SOLR-6193.json` (around the comment at `:38`, per the bulk review). See owner call 1.

## Owner calls (not decided here)

1. **Precedence when a local `facet.limit` conflicts with `f.<field>.facet.limit`.** Options: local wins (the usual local-param rule), or the per-field request param wins (what the current `wrapDefaults` plus `getFieldInt` does). Record the rule and add a conflict test before calling this resolved.

2. **Scope of SOLR-6193.** Does the ticket close on the pivot path alone, or does `facet.field` need its own fix in this ticket? Finding 1 depends on this.

## Proposed fixes (not applied; the owner decides)

- Finding 1: add the `facet.field` regression from the JIRA example and fix `FacetComponent`/`FacetProcessor` (or narrow the ticket if the owner splits it).
- Finding 2: add a conflict case, for example `facet.pivot={!facet.limit=4}place_s,company_t` with `f.place_s.facet.limit=-1` in the request, and assert the chosen rule once owner call 1 is answered.

## Interactions with other branches

- None found in the bulk round notes for this ticket.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs. The re-enabled distributed pivot assertions have not been run; the expected counts were carried from the commented-out cases.
- Whether upstream `facet.field` already applies local params correctly (finding 1 says the branch does not change it, not that it is broken).
- `SolrParams.getFieldInt` semantics were taken from the Solr convention, not re-read in this review.
- The full JIRA comment thread was not re-read; the precedence comment is cited from the bulk review.
- No GitHub or JIRA writes were made.
