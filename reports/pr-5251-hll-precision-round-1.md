# SOLR-18598 / PR 5251 HLL merge safety implementation, round 1

Status: IMPLEMENTED, PROOF PENDING. Linux gate handoff; no execution proof claimed.

- Authorization: Nick requested implementation of the PR 5251 review finding and handoff to the gate in this chat.
- Implementation claim: `claims/pr-5251-hll-precision-windows-round-1.md`, posted at `268612066c0aaaf46ba48ef0e915a5d7fc66bc6a`.
- Original public PR head P: `ca539cad196f26c78e14a1dc698b84e0f5c1f22e`.
- Pushed fork head F: `e7834ca954505c27778e59a61140bf02490226a6` on `nick-boss-tech/solr:solr-18598-submit`.
- Commit: `SOLR-18598: reject incompatible HLL precision before merging`, author Nick Shanin, no attribution trailer.
- One plain descendant commit above P, ancestry checked. GitHub branch read-back matches F. The contributor's public PR head remains P and still has no comments or reviews.
- Gate: `gates/pr-5251-hll-precision-round-1.md`, OPEN to a capable Linux host with `gate`, `premise-run`, `records` tags. Required tests, formatting, root/guide checks and fail-before proof are specified there.
- Gate posted and content/reachability verified at coordination-ui commit `da795dcd237fa9d67363afdf3a7f8e6d75d3513a`.
- Pinned offered diff: https://github.com/nick-boss-tech/solr/compare/ca539cad196f26c78e14a1dc698b84e0f5c1f22e..e7834ca954505c27778e59a61140bf02490226a6

## Result

`HLL.union` now rejects mismatched register counts or widths before dispatching into any representation-specific union. Its error names both actual layouts. `HLLAgg.Merger` translates the rejection into a 500 SolrException that names `solr.facet.hll.log2m` and preserves the cause. Compatible explicit thresholds and representations are accepted, and an empty-shard sentinel does not impose the coordinator's local precision on incoming sketches.

The implementation refines the claim's initial metadata approach. `IHLLMetadata` is package-private; the existing HLL union method already has the relevant fields. Completing its compatibility TODO avoids exposing internal metadata or adding public getters, rejects before mutation, and protects existing callers too. No StatsComponent source is changed. Existing HLL and StatsComponent neighboring tests are included in the gate because the guard is shared.

## Outbound scope relative to P

Six files, 214 additions and six deletions:

- `changelog/unreleased/SOLR-18598-hll-precision.yml`: describes the guarded precision feature and includes both Samuel Verstraete and Nick Shanin by name.
- `solr/core/src/java/org/apache/solr/search/facet/HLLAgg.java`: configuration guidance on mismatch and updated factory Javadoc.
- `solr/core/src/java/org/apache/solr/util/hll/HLL.java`: compatibility guard and documented exception.
- `solr/core/src/test/org/apache/solr/search/facet/HLLAggTest.java`: seven new tests on the real facet merger, including independent FULL merge orders, width mismatch, mismatch before promotion, compatible thresholds, compatible FULL/EXPLICIT union, and no-value shards with a different coordinator setting.
- `solr/core/src/test/org/apache/solr/util/hll/HLLUnionTest.java`: direct rejection and nonmutation across all representation pairs, both mismatch types and both directions.
- `solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc`: full-cluster-restart guidance for log2m changes, explicit mismatch failure, and compatibility of different explicit thresholds.

## Review and verification

The source check precedes every type dispatch and has no mutation before throwing. Tests serialize shard responses and call the real HLLAgg merger without introducing production hooks. Compatible tests use overlapping populations instead of self-union alone. The mixed FULL/EXPLICIT test compares with a sketch built directly from the combined population. The direct library test compares both input serializations after rejection. No serialized format, endpoint or permission changes are introduced.

Static review, outbound-file review, clean worktree after commit, fork read-back, ancestry and `git diff --check` passed. `review-branch.ps1 SOLR-18598 -StatOnly` confirms only the contributor's P and our F commits above upstream; no relevant production/guide drift was found against the fetched upstream tip. The all-worktree sync report is an ancillary workspace inventory and is not proof for this patch.

No Gradle, compilation, tidy, tests or guide build ran on Windows. The new tests are unexecuted assertions until the Linux positive and fail-before legs run. There is no matching Linux receipt yet. Do not offer F as tested, post a clean-gate public follow-up, or mark the review finding verified before the exact-head receipt is green.

No contributor branch or upstream PR/JIRA was modified, and no public comment was posted. Gate completion is a separate Linux claim; this report does not assign a named host or claim that execution has begun.
