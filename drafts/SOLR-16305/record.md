# Skip record: SOLR-16305

- Ticket: SOLR-16305, "MODIFYCOLLECTION with 'property.*' changes can't change values used in config file variables (even though they can be set during collection CREATE)". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2022-07-22, updated 2025-03-13, no fix version. Links: SOLR-17006 (Closed). Seventeen comments: a design debate between hossman, dsmiley, ab, vprimault and gerlowskija on one property API (collection properties, state.json, core.properties) and on whether the propagation is a bug or an enhancement. The most recent comment (2025-03-13) asks that the reference guide be updated if the API is unified.
- Verdict: keep-open. Unchanged from the earlier verdict; this record replaces the audit-only record with a pinned-head check.
- Draft branch: solr-16305-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 15:11-15:11 UTC.

## Ticket reading

`MODIFYCOLLECTION` with a `property.*` parameter is accepted and stored, but the value does not reach the cores, so a `${custom.prop}` in `solrconfig.xml` does not change. `CREATE` with the same parameter does reach the cores, because it is written into `core.properties`. The reporter and commenters disagree on whether this is a bug or an enhancement, and they propose different homes for the property (state.json, collection properties, or a single read/write API).

## Code reading

- `solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java:306-317`: the collection-creation path adds each `property.*` key to the per-core properties. `CollectionAdminParams.PROPERTY_PREFIX` is the prefix checked.
- `solr/core/src/java/org/apache/solr/handler/admin/CollectionsHandler.java:809`: `propName.startsWith(PROPERTY_PREFIX)` is used on the modify path, for the collection-level property handling. The propagation of a modified value into core properties is not present on that path at the pin (the modify path does not rewrite `core.properties`).
- SOLR-17006, `0f5a8f929e` (2023-10-20, "Collections/AddReplica: Persist user-defined properties to state.json (#1973)"): ancestor of the pin (`git merge-base --is-ancestor`: yes, checked). The change stores user-defined properties in state.json at create and add-replica time. The queue note calls it a partial fix; the audit reads it the same way.

What this shows at the pin: SOLR-17006 covers part of the ticket, the storage of user-defined properties at creation. The gap the ticket reports, a modify call that does not reach the cores' config variables, is not closed by that change. Whether to close it depends on the unified API the commenters have not agreed on.

## History

- No commit on the pinned head names SOLR-16305 (evidence tool: no hits).
- SOLR-17006 (`0f5a8f929e`) is an ancestor of the pin, as above. Its scope is creation-time persistence, not the modify-to-core path.

## Reproduction attempts

Not attempted. A check needs a configset with `${custom.prop}`, a collection created with one value and modified with another, and a read of the core's effective value. That is a cluster test run, which is outside this review-only pass.

## Gap analysis

The remaining gap is the design: which store should hold the property, and whether modify should push the new value into `core.properties` or into the cores in some other way. The comments show no agreement on that, and the reference guide currently describes `core.properties` as the place for it. Without the decision, neither a PR nor a close has a defined target.

Missing piece for a verdict change: the maintainers' decision on the property API (where a property is stored and whether MODIFYCOLLECTION updates the cores). With that decision, the record can name the change (a PR) or close the ticket as answered or as a design won't-fix.

## Verdict justification

keep-open meets the rubric: the code path is read at the pin (`CollectionHandlingUtils.java:306-317`, `CollectionsHandler.java:809`), the partial fix is verified as an ancestor (`0f5a8f929e`), and the missing piece is named (the design decision). close-fixed is not met: the gap the ticket reports is not closed by `0f5a8f929e`. close-answered is not met: the docs do not settle the modify-to-core question. pr-needed is not met: there is no agreed target behavior.

## Limits

This record does not reproduce the modify-to-core behavior and does not run a cluster test. It does not decide the property API. It does not post anything to JIRA or GitHub.
