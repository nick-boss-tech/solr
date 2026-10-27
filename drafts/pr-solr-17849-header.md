🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17849

## What happens today

The new UI has no header for the Index & Query section. There is no collection dropdown and no request handler dropdown to start from. This change adds the header alone. The content is handled outside this ticket, in a separate change.

## What this change does

Adds `IndexQueryHeader`, a composable with two dropdowns: one for the collection and one for the request handler. Each handler shows its endpoint name and its class. Both dropdowns are disabled while loading and when there is nothing to pick. Two messages cover the empty cases: no collections, and a collection with no request handlers.

The header only renders what it is given. The caller loads the data and handles the selection. `RequestHandlerOption` moves into its own file in `domain`, because the header and its test both use it. Four strings are added for the labels and messages.

Christos Malliaridis asked for the branch to carry only the header, with the content in a separate pull request ([SOLR-17849 comment 18133044](https://issues.apache.org/jira/browse/SOLR-17849)). Jane Sandberg is proposed as the first reviewer.

## Proof

`IndexQueryHeaderTest` passes 5 of 5 (0 failures, 0 errors, 0 skipped) at head [5d90d83d5b](https://github.com/nick-boss-tech/solr/commit/5d90d83d5b12a69d02f0f302c45ac6d8077180d2), verified 2026-10-27 with `:shared:spotlessCheck` and `:shared:jvmTest` on the software renderer.

Failing on the base is not possible by construction. The component and its test are new, so the test class does not exist on the base commit.

## Limits

- No screen uses the header yet. The Index / Query tab and its wiring come with the content change.
- Not in this change: the content (`IndexQueryContent`), the data layer and state holder (`HttpIndexQueryRepository`, `IndexQueryStateHolder`), the request form for `solr.SearchHandler`, and the Configsets wiring. These follow in a separate pull request.
- Tests are JVM unit tests on a software renderer. Nothing was run in a browser.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
