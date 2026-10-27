# SOLR-17849 header-only branch after Christos's reply

Assignment: `assignments/christos-reply-17849-header-scope-round-1.md`. Claim: `claims/christos-reply-17849-header-scope-round-1-vm6.md`. Gate: vm6, run `header-17849-5d90d83d5b-205246`.

## Result

- Reduced branch: `solr-17849-header-submit` on `nick-boss-tech/solr`, head `5d90d83d5b12a69d02f0f302c45ac6d8077180d2`. Base `c8ab71430f`, the same base as the source branch. Pushed new (no force). Remote SHA checked.
- Gate at the exact head: spotless rc=0, `IndexQueryHeaderTest` 5 of 5 passed, 0 failures, 0 errors. Logs: `/workspace/gates/logs/vm6/pool-ui-header-17849/header-17849-5d90d83d5b-205246/`.
- PR draft: `drafts/pr-solr-17849-header.md`.
- No PR opened. No Jira or GitHub post. Opening the PR needs Nick's go.

## Source tip check

The live `solr-17849-index-query-screen` tip is now `375143456a`, not the gated head `b91f75ed93`. The only change is that it removes `pr-draft/SOLR-17849.md` and `pr-draft/SOLR-17850.md`. The UI code is the same at both heads, so the gate on `b91f75ed93` still covers the code. The source branch was not changed.

## What is in the reduced branch

Four files, 369 insertions, all from the source tip except the strings:

| File | Source |
| --- | --- |
| `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/indexquery/IndexQueryHeader.kt` | tip `375143456a` (its version differs from the first header commit `fb51fbcb23` by the `RequestHandlerOption` import) |
| `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/indexquery/IndexQueryHeaderTest.kt` | tip `375143456a` |
| `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/domain/RequestHandlerOption.kt` | tip `375143456a` (moved out of the header file) |
| `ui/shared/src/commonMain/composeResources/values/strings.xml` | `fb51fbcb23`, four header strings only |

The header imports nothing else from the project. It needs no data layer, state holder or wiring to compile and run. Nothing in the reduced branch calls `IndexQueryHeader`, so the header is not on any screen yet.

## What was left out and where it still lives

On `solr-17849-index-query-screen` (unchanged):

- `IndexQueryContent.kt` and its test: the content and the response view.
- `HttpIndexQueryRepository.kt`, `IndexQueryRepository.kt`, `IndexQueryStateHolder.kt`, `IndexQueryViewModel.kt` and their state-holder test: the data layer and state holder.
- `SearchHandlerForm.kt` and `SearchHandlerFormTest.kt`: SOLR-17850's form, not in this ticket.
- `ConfigsetsContent.kt`, `ConfigsetsRouteViewModel.kt`, `ConfigsetsComponent.kt`, `DefaultConfigsetsComponent.kt`, and the preview: the Index / Query wiring.
- The `search_form_*` and `index_query_error` strings.

## Note for the content PR

The header is not visible in the app until the content PR wires it into the Index / Query tab. This is expected: Christos's comment asks for the header and content to go in separate PRs.

## Attribution

The draft's footer follows `pr-formula.md`, which Nick set for PR descriptions. I did not add the Claude Code attribution line, because the formula's footer is his rule for PR descriptions. The commit has no trailers, as AGENTS.md requires.
