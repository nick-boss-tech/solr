🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17849

## What happens today

The new UI has no Index & Query header. A user cannot pick a collection there, and cannot see the request handlers of the chosen collection. The ticket asks for both dropdowns, with their loading and selection state.

## What this change does

Adds the header and the state it needs. The request form, the response view and the request itself are not in this change.

- [`IndexQueryHeader`](https://github.com/nick-boss-tech/solr/blob/14ff7078657e6907212dbaa2bdabeee59e3678c8/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/indexquery/IndexQueryHeader.kt): a collection dropdown and a request handler dropdown. Each handler shows its endpoint name and class, and a long class name is cut with an ellipsis. Both dropdowns are disabled while loading and when there is nothing to pick. Messages cover no collections and a collection with no handlers.
- [`IndexQueryStateHolder`](https://github.com/nick-boss-tech/solr/blob/14ff7078657e6907212dbaa2bdabeee59e3678c8/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/components/indexquery/viewmodel/IndexQueryStateHolder.kt): loads the collections and the handlers of the selected collection. It holds the selected collection and handler, and clears the handler when the collection changes.
- [`HttpIndexQueryRepository`](https://github.com/nick-boss-tech/solr/blob/14ff7078657e6907212dbaa2bdabeee59e3678c8/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/components/indexquery/data/HttpIndexQueryRepository.kt): `GET api/collections` and `GET api/collections/{collection}/config/requestHandler`.
- `RequestHandlerOption` moves to the domain package, and four strings are added.

## Scope of this change

**Summary: the header comes with the state it needs to meet the ticket's criteria.**

The ticket's criteria say that the collections are listed, that the handlers load for the selected collection, and that the selection is part of the state. Its "Additional Information" excludes only the form of a request handler. Three options fit the ticket:

1. The header component only. The criteria for loading and state would stay open, because the header takes its lists and selection as parameters and nothing supplies them.
2. The header with its loading and selection state (this change). It meets the criteria for collections, handlers and state, and it leaves out request execution.
3. The whole screen, including running a request and showing its response. That is the content, which the ticket excludes and which Christos Malliaridis asked to keep in a separate pull request.

This change takes option 2.

## Proof

- `IndexQueryHeaderTest` passes 5 of 5, and `IndexQueryStateHolderTest` passes 3 of 3, at head `14ff707865`, verified 2026-10-27 with `:shared:spotlessCheck` and `:shared:jvmTest` on the software renderer.
- These tests were carried over from the screen branch, where they passed at `b91f75ed93`. The request tests were removed with the request code. They are not new assertions, and no mutation run was made.
- Failing on the base is not possible: the components and the tests are new.

## Limits

- No screen shows the header yet. The Index / Query tab wiring comes with the content pull request.
- Not in this change: running a request, the response view, the request form (SOLR-17850's `SearchHandlerForm`), `execute` on the repository, and the Configsets wiring.
- The layout was checked against the acceptance criteria, not against the Figma design.
- The tests run on the JVM with a software renderer. Nothing was run in a browser.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
