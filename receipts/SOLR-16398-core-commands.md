# Receipt: SOLR-16398 core commands (JAX-RS core split, recovery, leader election), gate

- Gate job: `gates/SOLR-16398-core-commands.md`. Claim: `claims/SOLR-16398-core-commands-gate-vm6.md`. Host: vm6.
- Branch: `nick-boss-tech/solr:solr-16398-core-commands`. Gate head: `cf2bbb0558` = tidy commit `099962c50f` on the gated head `d4a7840649` plus the route test commit. Fork fast-forward pushed 2026-10-29.
- Result: PASS on the listed steps and on the added route check, at `cf2bbb0558`. Logs: `/workspace/gates/logs/vm6/solr16398-core-r1/` (listed steps at `d4a7840`), `solr16398-core-r4/` (final head, all steps), `solr16398-core-routes-r1/` and `solr16398-core-mut1`/`mut2` (route check and mutation). Completed 2026-10-29T15:56:15Z.

## Steps (final head `cf2bbb0558`, run `solr16398-core-r4`)

| Step | Result |
|---|---|
| Changelog YAML parse (`changelog/unreleased/SOLR-16398-core-commands.yml`) | pass (run at `d4a7840`, parsed with `uv run`; file unchanged since) |
| 1. Tidy | rc 0. Changed 14 files, formatting only (line wraps, import order, javadoc reflow). Committed as `099962c50f` "SOLR-16398: tidy". |
| 2. Error Prone compile (api, core main, core test) | rc 0 |
| 3. SolrJ generation | rc 0 |
| 4. Focused: `CoreCommandsV2RoutesIntegrationTest` (added, see below) and `V2CollectionAPIMappingTest` | 8 of 8 routes pass |
| 5. `:solr:core:check -x test` | rc 0 |

On the listed steps alone, `d4a7840` also passed (run `solr16398-core-r1`: tidy, Error Prone, SolrJ, module check). The listed steps do not run tests, so they did not show that the routes are served.

## Route check (added this round)

The gate note said Jersey registration failures would surface in the check leg. They do not: `check -x test` skips the tests. This branch also deletes `V2CoreAPIMappingTest` and `V2NodeAPIMappingTest`. A new end-to-end test, `CoreCommandsV2RoutesIntegrationTest` (SolrCloud, one node), POSTs to each converted path and fails only if the answer is "Cannot find API for the path". Success and other errors pass, because the handler, not routing, decides those.

- Routes checked: `/cores/{core}/split`, `prep-recovery`, `request-recovery`, `request-sync-shard`, `request-buffer-updates`, `request-apply-updates`, and node-level `/node/overseer-op` and `/node/rejoin-leader-election`.
- Mutation (rung 2): removing the `SplitCore` and `RejoinLeaderElection` registrations from `CoreAdminHandler.getJerseyResources()` fails exactly `testSplitCoreIsRouted` and `testRejoinLeaderElectionIsRouted`, each with "Cannot find API for the path". The other six pass (`solr16398-core-mut2`). The handler was restored and re-gated.
- A first version of the helper required an exception. `testRequestSyncShardIsRouted` returned success for a missing core, so that version failed (`solr16398-core-r3`). The helper now accepts success and checks only for the routing error. Error Prone's `MissingFail` check also rejected a `try` with no `fail()`; the helper was rewritten to avoid it.

## Findings

- No registration defect on this branch: the eight resources are in `CoreAdminHandler.getJerseyResources()` and `getApis()` is empty, as the gate file says. Unlike the collection branch, no route is missing.
- Behavior is not covered beyond routing. The route check does not exercise a successful split or recovery. The V2 mapping tests this branch deletes are the only coverage of the translations, and no replacement exists. This is a limit of this gate, not a passing claim about those behaviors.

## PR description draft

Gated on vm6 at `cf2bbb0558`. Changelog parse, tidy, Error Prone compile, SolrJ generation and `:solr:core:check -x test` pass. The new route test confirms that the eight converted core command paths (split core, prep-recovery, request-recovery, request-sync-shard, request-buffer-updates, request-apply-updates, overseer-op and rejoin-leader-election) are served; behavior of the converted operations is not covered by new tests.

AI-generated draft. Nick Shanin directed the work and takes responsibility for this contribution.
