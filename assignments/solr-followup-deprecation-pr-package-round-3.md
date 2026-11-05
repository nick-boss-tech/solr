# Assignment addendum: legacy node/nodes deprecation follow-up

Status: OPEN. Intended host: any review/draft/records host. This assignment prepares scope and submission drafts; it does not authorize implementation or test execution. Required later proof belongs to an exact-head Linux job.

Owner direction, 2026-11-05: add the server-side deprecation follow-up to the assignment and briefly reconcile the three existing PR 5110 review findings. Read `reports/solr-12613-three-findings-reconciliation-round-2.md`. This addendum extends `assignments/solr-followup-pr-jira-packages-round-2.md`; all six existing paired packages and their unresolved prerequisites remain valid. Posted records are add-only.

## Scope and current status

Plan one additional candidate: deprecate the older admin `node`/`nodes` proxy parameters after their consumers move to the generic `X-Solr-targetNode` transport. Actual removal is a later release-policy step, not immediate deletion. Six source-backed packages are already drafted; this seventh candidate is a newly planned task, not an implemented/proven branch or completed PR/Jira package. Final PR count can shrink through author absorption or change if the consumer audit needs a separately scoped migration.

PR 5110 remains OPEN at `6b5d830132b941faa85856347099ade8b190be0e`. Its newly added backend parameters/helper have already been removed. Do not create another PR to redo that removal. Nick's three earlier findings are addressed in source: both helper findings by removal, and failed live-node lookup by continuing through the local fallback to core refresh. Separate startup/obsolete-response concerns remain; do not confuse them with the closed findings or infer overall merge readiness.

David's reply to Eric: https://github.com/apache/solr/pull/5110#discussion_r4483637626 . He confirms this is deprecated material that should not be newly enabled in v2. Broader direction is recorded on SOLR-18537:

- Consumer migration before server-side deprecation: https://issues.apache.org/jira/browse/SOLR-18537?focusedCommentId=18126528#comment-18126528
- Deprecate for removal in Solr 11: https://issues.apache.org/jira/browse/SOLR-18537?focusedCommentId=18136779#comment-18136779
- Start with the Admin UI migration: https://issues.apache.org/jira/browse/SOLR-18537?focusedCommentId=18137756#comment-18137756
- Multi-node operations use client-side parallel requests: https://issues.apache.org/jira/browse/SOLR-18537?focusedCommentId=18138040#comment-18138040

## Work

1. Claim this preparation slice and refresh the board, Jira, PR discussion, actual source heads and any competing work. Search for an existing deprecation ticket/PR before drafting a new linked ticket. Do not reopen resolved SOLR-18537 or invent a Jira key. Avoid duplicating work a maintainer has already taken.
2. Audit the actual consumers and public API surfaces of the legacy admin proxy parameters. Start with MetricsHandler/GetMetrics (`node` as well as `nodes` where supported), LoggingHandler, SystemInfoHandler/GetNodeSystemInfo, NodeLogging, their generated SolrJ/JS declarations, documentation, tests and the `handler/admin/proxy` infrastructure. Establish exact scope from source/history, not the old inventory alone. Do not target unrelated parameters merely because they are named node/nodes.
3. Map every UI caller to a migration or an explicit compatibility dependency. SOLR-18619's prepared candidate migrates reads and preserves existing log-level writes. At reviewed PR 5110 head, LoggingLevelController still sets `opts.nodes = "all"` for cloud writes (`logging.js:153-156`). Audit/migrate that behavior before backend retirement; make multi-node operations issue one targeted request per node and expose per-node failures/partial outcomes while preserving standalone local behavior. Decide whether the missing write migration is a bounded addition approved under SOLR-18619 or needs its own linked slice. Record that decision and the resulting PR count; do not silently broaden the current ticket/package.
4. Separate three stages: client migration, backend deprecation, and removal at the agreed release boundary. Specify how the deprecation is exposed in the existing public APIs, generated clients, docs and upgrade notes while current supported behavior remains available. The header's GET query-parameter equivalent is part of the new generic transport and must not be deprecated merely because it is a query parameter. Do not add new endpoint-specific `nodes` APIs as part of this work.
5. Prepare a complete paired package: PR title/body and Jira title/body for the deprecation candidate, plus a separate paired migration package only if the audit requires it. Suggested scope title: `Deprecate legacy admin node/nodes proxy parameters`; an unfiled identity stays `<JIRA-KEY>`. Preserve the current formulas, repository template, truthful AI review disclosure, compatibility/release limits, dependency order, exact existing branch/base references and planned acceptance criteria. Do not claim tests passed for an unimplemented candidate.
6. Define future implementation/proof requirements: existing consumers keep working during deprecation, migrated reads and multi-node writes use the intended target, standalone remains local, partial failures are visible, relevant generated clients/docs remain consistent, and no removal happens prematurely. Name the existing focused suites/fixtures after inspecting source; prefer reuse and identify any missing coverage honestly. A later implementation job needs its own branch/head and exact Linux test/formatting/root checks. No Gradle or tests on Windows under this assignment.

Keep the selected-node versus All nodes log-message-viewing choice in the SOLR-18619 PR draft as Nick requested. It is distinct from the cloud log-level write migration. Preserve the already prepared lifecycle/transport slices and their evidence; the generic transport fixes do not depend on removing the old proxy APIs.

## Deliverables and finish

- `reports/solr-followup-deprecation-scope-round-3.md`: current deduplication, consumer/API audit with source links, chosen ticket strategy, release sequence, unresolved maintainer decisions, and updated dependency/count matrix.
- `reports/solr-followup-package-deprecation-round-3.md`: complete PR and Jira title/body drafts, clearly planned/unimplemented, with acceptance/proof requirements and named migration prerequisites.
- If a separate write-migration slice is needed, a bounded linked assignment and complete paired draft package. Otherwise record where and why that migration belongs without changing existing scope implicitly.

Done means concrete reviewable scope and full paired drafts exist, with each consumer accounted for and the client-first dependency explicit. No source implementation, PR opening, Jira filing, public comment, live-body edit, contributor write or removal is authorized by this preparation assignment. Nick reviews the concrete packet before publication.
