# SOLR-16398 / PR 5284 coverage follow-up, Windows round 1

Status: Implemented, Proof Pending

- Owner request: brief second review, then patch the coverage gap and any further gaps found.
- Claim: claims/pr-5284-coverage-round-1-windows.md (board commit e4802b5228b578f69380062a62ab5b6785ae3c60).
- Original PR head: 793e1ffa0f1d67271fbf090773651cd8a26171e4.
- Ordinary follow-up commit: 08d13e33495689b67561f451b62a3e2d017df720.
- Fork branch: nick-boss-tech/solr:solr-16398-rebalance-migrate. Independent GitHub ref read-back and live PR head both match the new commit at 2026-11-03T18:02Z. The open PR head moved.
- Linux handoff: gates/pr-5284-coverage-round-2.md; receipt reserved for the executing host at receipts/pr-5284-coverage-round-1.md. Round 2 supersedes round 1 to correct the TestApiFramework package in the filter; no source/proof change or executed gate rerun.

## Second review

Revisited the full original PR source diff at the verified unchanged live head, legacy operation paths, AdminAPIBase submission, MigrateCmd alias resolution, RebalanceLeaders execution, endpoint/model shapes, registration, current discussion, and existing test infrastructure. Correctness: no new confirmed production defect. Architecture: the two resource migrations remain aligned with merged split-shard; existing v1 operation behavior is preserved. Security/compatibility: collection-edit permissions and SolrCloud guards remain; no new trust boundary. Maintenance/reuse: the new tests use MockV2APITest and the existing two-node cloud-minimal harness, with no new schema/configset/dependency. The mapping mock checks request boundaries; real cloud tests cover effects.

Additional gaps beyond the original low finding:

1. The rebalance smoke test never assigned preferredLeader, so Summary could reflect a no-op. It now sets each shard's nonleader as preferred, waits for the properties, requires both successes and actual leadership changes.
2. The reference guide still sent the removed migrate-docs/rebalance-leaders wrapper commands to the collection root. Both examples now use the dedicated endpoint and flat JSON body.
3. V2ApiIntegrationTest.testException still used the removed rebalance command path. Its typed invalid-body case now goes through the JAX-RS route for XML, javabin and JSON responses.

## Exact outbound files

- solr/core/src/test/org/apache/solr/handler/admin/api/MigrateDocsAPITest.java: 10 methods cover message fields, async submission/response ID, defaults/null omission/explicit false, subresponses, required parameters and standalone rejection.
- solr/core/src/test/org/apache/solr/handler/admin/api/RebalanceLeadersAPITest.java: 5 methods cover path/body parameter precedence, unrelated parameter preservation, Summary shape, optional body/defaults and validation.
- solr/core/src/test/org/apache/solr/handler/admin/api/CollectionCommandsV2IntegrationTest.java: 3 methods cover actual preferred leadership, synchronous migration and alias-based async migration. Migration checks the concrete routing destination and exact target IDs, including preservation of an existing document and exclusion of the other split key.
- solr/core/src/test/org/apache/solr/handler/V2ApiIntegrationTest.java: update the invalid integer-body error path for all three parsers.
- solr/solr-ref-guide/modules/deployment-guide/pages/collection-management.adoc: fix the two v2 examples.

Commit scope: 5 files, +440/-19. Production Java, endpoint interfaces, models and changelog are unchanged. Nick Shanin is the commit author; no attribution trailer.

## Verification and limits

Source review and git diff/show --check were clean. The worktree is clean after the scoped commit. No Gradle, compilation, test execution or retired local queue use on Windows. This report claims no runtime result. No exact-new-head Linux proof exists at posting time; the original receipt and PR body's old-head proof do not cover this follow-up.

The gate requires passing original/expanded suites, tidy, Error Prone compile, root check and eight isolated field/async omission mutations. Cloud/default/validation/response assertions remain coverage pins unless separately discriminated. Missing APIs or compile/setup failures are not negative proof.

No public comment, review, body edit or JIRA write was made. The live PR still had no comments/reviews after push. Its existing validation paragraph refers to the original head and will need an authorized update after Linux proof. No Linux host has been claimed by posting this handoff.
