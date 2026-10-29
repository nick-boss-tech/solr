# Listener UBI contract proof: Windows implementation, round 1

Status: Implemented, Proof Pending

- Source: Nick's direct request to build a patch or tests on the PR author's branch.
- Parent: `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`, PR 5238.
- Candidate: `de4d222bb12302eb0e10aeb5697bae307eb2cd22` on `nick-boss-tech/solr:solr-11994-pr5238-ubi-submit`.
- Claim: `claims/pr-5238-ubi-contract-windows-round-1.md`, posted at `28a98d4269e1703bd974c15a04858064a3e1751b`.
- Gate: `gates/pr-5238-ubi-contract-round-1.md`.
- Receipt target: `receipts/PR-5238-ubi-contract.md`.

Pushed normally to the fork and independently verified through the remote refs
API at 2026-10-29 07:39 UTC: `de4d222bb12302eb0e10aeb5697bae307eb2cd22`.
The worktree is clean. The single commit adds 379 lines in the one intended test
file, uses Nick Shanin's ICLA identity and has no attribution trailers. The
scoped staged whitespace check passed before committing. The author PR remains
at the parent head. [Pinned candidate diff](https://github.com/nick-boss-tech/solr/compare/aa03c9544968f1d9de9b6b36e2823bcbe76268b9...de4d222bb12302eb0e10aeb5697bae307eb2cd22).

## Scope and approach

One new test file: `solr/core/src/test/org/apache/solr/handler/component/SearchRequestListenerUbiTest.java`. There is no production hook change, new dependency, schema, configset, or changelog. The candidate is a test-only prototype on the author's current listener implementation. It does not import or claim to migrate the unfinished UBI implementation from PR 2452.

The real Config API registers a small preparation SearchComponent on `/ubi` and an ordinary SearchHandler on `/plain`, using existing `cloud-minimal` fixtures and the test-framework ConfigRequest helper. The preparation component checks `ubi=true`, creates or preserves `query_id`, requests a document list, and adds the same identity to the response. The node-wide listener reads the prepared context and copies query/result data into an immutable recording payload before invoking a test destination. The destination never receives a request, response, response builder or searcher.

Local searches use a one-shard collection replicated on both nodes, so `distrib=false` has all three documents on node 0. Distributed searches use a separate two-shard collection. Each has its own uploaded config name to avoid cross-collection configuration propagation races. Requests deliberately target node 0, and both the recording and the next listener are counted to detect duplicate callbacks and listener-failure isolation.

## Nine contract tests

1. Local recording matches the actual response's IDs and generated query ID.
2. Distributed recording matches final merged IDs and occurs once on the coordinator.
3. A supplied `query_id` remains identical in the response and recording.
4. `ubi=false` leaves search results unchanged and produces no UBI payload/record.
5. `ubi=true` on an unconfigured handler does not opt that handler into recording.
6. A destination exception leaves returned results intact and still invokes the next listener.
7. A malformed query is not recorded as a complete result.
8. A deterministically injected partial search is not recorded as a complete result.
9. A payload copied by an embedded search remains usable and immutable after request/client closure.

The recorder checks `SolrQueryResponse.haveCompleteResults`, not only the listener's partial-results boolean, because an omitted result is also incomplete. There is no separate executed omitted-results case claimed here.

## Evidence and limits

Windows source review checked the current SearchComponent lifecycle, SearchHandler component ordering, schema ID extraction and the public completeness helper. Code was formatted by hand. No Gradle, formatter, compiler, test, or retired Windows queue ran. Git whitespace and scope checks are recorded with the pushed candidate; Linux results must come from the named gate at that exact head.

This is a positive integration contract proof, not a regression claim against the current hook. If the tests pass, they demonstrate that the unchanged author implementation can support this preparation/recording split. A baseline without the listener API failing to compile would not be fail-before evidence. Any runtime failure must be investigated before attributing it to a hook limitation.

The prototype does not establish a production UBI adapter, persistent logging/streaming behavior, configuration reload/unload lifecycle, asynchronous scheduling, performance, or query-data redaction. It exercises ordinary local/distributed results with the ID field requested; grouped responses and omitted ID fields need their own consumer policy. Existing UBI response construction and its duplicate-record guard still require an explicit refactor to use this pattern.

No public PR/JIRA comment, live body edit, contributor-branch push, or review was made. The fork candidate and Linux gate are preparation for a factual offer after proof, not a maintainer approval.
