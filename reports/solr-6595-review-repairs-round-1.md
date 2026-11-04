# SOLR-6595 review repairs, round 1

Status: Implemented, Proof Pending. Nick controls the next Linux gate handoff.

## Revisions

- Fork branch: `nick-boss-tech/solr:solr-6595-submit`.
- Reviewed parent: `2b5a1d174f0471edb8280207ba877a4d7eb6e619`.
- Pushed repair: `fb93b62a252fb09d90084abd80b904db512e2e1b`.
- Upstream base: `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- Author: Nick Shanin, `nick.boss.us@gmail.com`.
- Source review: `research/branch-reviews/2026-11-04-solr-6595-review.md` in the Windows workspace.
- Fork ref separately read back at the repair SHA; ticket worktree clean.

## Findings addressed in source

| Finding | Repair | Required Linux evidence |
| --- | --- | --- |
| F1 / P2: older dispatch bypasses detection | Moved the failure predicate to CollectionsHandler and called it from both response assembly paths. Added tests through V1 MOVEREPLICA and its real legacy V2 wrapper for non-empty/empty failures, preserved mixed results and exception precedence. | Focused suite and unchanged candidate legacy test failing at the reviewed parent |
| F2 / P2: body assertions do not prove HTTP status | DeleteNode's deterministic regression now asserts actual HTTP 500, JSON header 500 and retained failure details for V1 and native V2. Added HTTP 200/header 0 control; added async header 0 assertion. | Focused wire tests plus independent V1 writer and native V2 filter mutations |
| F3 / P3: new suites use old base | SolrCoreResponseStatusTest and PostRequestDecorationFilterTest now extend SolrTestCase. Requests are closed; the filter test uses the existing Mockito availability guard. | Compilation, focused tests and formatting |

The production change adds one decision to older CollectionsHandler dispatch and reuses the same predicate in AdminAPIBase. It preserves the existing exception path. No operation implementation, permission, endpoint, SolrJ parser, response field, configset or dependency was added. Existing TestCollectionAPIs/ApiBag mocks and the DeleteNode cluster/configset are reused. The partial-result assertions retain both success and failure objects. The native V2 wire helper overrides the generated request's getResponseParser directly, since its generated implementation ignores setResponseParser.

## Outbound repair files

- `solr/core/src/java/org/apache/solr/handler/admin/CollectionsHandler.java`
- `solr/core/src/java/org/apache/solr/handler/admin/api/AdminAPIBase.java`
- `solr/core/src/test/org/apache/solr/core/SolrCoreResponseStatusTest.java`
- `solr/core/src/test/org/apache/solr/handler/admin/TestCollectionAPIs.java`
- `solr/core/src/test/org/apache/solr/handler/admin/api/DeleteNodeAPITest.java`
- `solr/core/src/test/org/apache/solr/handler/admin/api/ReloadCollectionAPITest.java`
- `solr/core/src/test/org/apache/solr/jersey/PostRequestDecorationFilterTest.java`

The earlier ticket commits and changelog remain in branch ancestry. This repair adds no new files to the source branch.

## Review and verification limits

Revisited all three findings, older and migrated assembly, V1 decoration/writer, native Jersey mapping, generated parser behavior, exception precedence, fixture reuse and source scope. No remaining source issue was identified in that pass. Windows ran only source inspection and a clean worktree diff whitespace check, which passed; it ran no Gradle, compilation, tidy or tests. Compilation and runtime correctness are unproven until the Linux gate.

The round-1 and round-2 gates cover older heads and cannot prove the repair. Prepared `gates/SOLR-6595-response-status-round-3.md`, pinned to the pushed SHA, with five focused classes, formatting/Error Prone/root checks, a parent fail-before leg and four isolated mutations. Its Status is HANDED: no autonomous execution, because Nick said he will take the branch to the gate after review. A selected host may execute only when Nick directs it.

No PR, Jira comment, public review follow-up, or upstream write was made.
