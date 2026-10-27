# SOLR-17849 header with its loading and selection state (round 2)

Claim: `claims/christos-reply-17849-header-criteria-round-2-vm6.md`. Round 1: `reports/christos-reply-17849-header-scope-round-1.md` (head `5d90d83d5b`). Gate job: `header-17849-14ff707865-211244`.

## Result

- Branch `solr-17849-header-submit` on `nick-boss-tech/solr`, head `14ff7078657e6907212dbaa2bdabeee59e3678c8`. Fast-forward from `5d90d83d5b`. Remote SHA verified.
- Gate at the exact head: spotless rc=0. `IndexQueryHeaderTest` 5 of 5 and `IndexQueryStateHolderTest` 3 of 3 pass, 0 failures, 0 errors. JUnit timestamps 2026-10-27T21:14. Logs: `/workspace/gates/logs/vm6/pool-ui-header-17849/header-17849-14ff707865-211244/`.
- PR draft: `drafts/pr-solr-17849-header.md`, with the scope options stated as behavior.
- No PR opened, no Jira or GitHub post.

## Decision (decision ladder)

- Rung 1 (code): the header's loading and selection state are `IndexQueryStateHolder.loadCollections`, `selectCollection` and `selectHandler`. The repository methods it calls are `listCollections` and `listRequestHandlers`. `execute` and the response, executing paths are request execution.
- Rung 3 (stated intent) settled it. The ticket's criteria say "The available collections are listed", "The available request handlers are loaded based on the collection that is currently selected", and "The currently selected collection and request handler are part of the state". The "Additional Information" excludes only "the actual form of a request handler".
- Options: (1) header component only, criteria 1, 3 and 5 open; (2) header with its loading and selection state, chosen; (3) the whole screen with request execution, the content, which Christos asked to keep in a separate PR.
- The widening of the round 1 scope was approved by the user's direction in this turn.

## What changed from round 1

- Added: `IndexQueryRepository` (two list methods), `HttpIndexQueryRepository` (two list calls and their response types), `IndexQueryStateHolder` (header state only), `IndexQueryStateHolderTest` (three tests: load collections, select collection, failed load).
- Removed from the branch's data layer: `execute`, its response handling, and the request tests.
- `IndexQueryHeader.kt`: the license's closing `*/` is now on its own line, not joined to `package`.

## Acceptance criteria after this change

| # | Criterion | Status |
|---|---|---|
| 1 | Collections are listed | Met (state loads them; header renders them) |
| 2 | User can select a collection | Met |
| 3 | Handlers load for the selected collection | Met (state) |
| 4 | Handler can be picked once a collection is selected | Met |
| 5 | Selected collection and handler are part of the state | Met (state) |
| 6 | Placeholder text when no handler is selected | Met |
| 7 | No collections: both dropdowns disabled, message shown | Met |
| 8 | Collection without handlers: message shown | Met |
| 9 | Disabled during loading (both while collections load; only handlers while handlers load) | Met |
| 10 | Handler dropdown shows endpoint name and class; long text handled | Partly: name and class with ellipsis in the open menu. The closed field shows only the name. Not checked against the Figma design. |

## Not in this change

The request form (SOLR-17850), running a request, the response view, `execute` on the repository, and the Configsets wiring. Nothing shows the header until the content PR wires it in.
