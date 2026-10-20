# Skip record: SOLR-13283

- Ticket: SOLR-13283, when facet with certain terms via {!terms}, facet.limit, facet.offset does not work.
- Verdict: keep-open.
- Draft branch: solr-13283-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-1, 2026-10-04, confirmed-skip) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-03-01, updated 2019-11-25, no fix versions, no links. The report: with Solr 7.7, a field facet that lists terms with `{!terms=...}` honors `facet.sort=count` but ignores `facet.limit` and `facet.offset`. The one comment (mkhl, 2019-03-01) says patches are welcome.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/request/SimpleFacets.java:904-913`: when the local param `terms` is present, the field facet calls `getListedTermCounts(facetValue, parsed, terms)`; otherwise it calls `getTermCounts`.
- `SimpleFacets.java:967-996` (`getListedTermCounts`): reads `facet.sort` (`:969`) and the field type, maps each listed term to its document count (`numDocs`, :998-), sorts by count or by index order, and returns the whole list. It does not read `facet.limit` or `facet.offset`. So a listed-terms facet returns every listed term regardless of those parameters.
- By contrast, `getTermCounts` reads them at `SimpleFacets.java:442-443` (`FACET_OFFSET`, `FACET_LIMIT`), which is the path a field facet without `terms` takes.

So the reported behavior is present at the pin: the listed-terms path ignores limit and offset.

Grep at the pin: `git grep -n 'getListedTermCounts' ... -- solr` shows only the call at `:913` and the definition at `:967`; no other place applies limit or offset to the listed terms.

## History

- No commit at the pin names SOLR-13283 (evidence tool: none).
- The history of `getListedTermCounts` was not traced in this pass.

## Reproduction attempts

Not run. The reading is of the code path; no build or test run in this pass.

## Gap analysis

- The ignored parameters are a fact of the code at the pin. Whether that is a bug or the intended behavior for an explicit list of terms is not settled: the list is the user's own selection, and applying a limit or offset to it would be a behavior change for every `{!terms}` request.
- The ticket has no maintainer decision and no fix spec. The one comment only asks for patches.
- Missing piece: a maintainer decision on whether limit and offset should apply to `{!terms}`, and, if so, a test showing the expected page of listed terms.

## Verdict justification

keep-open: the behavior the ticket reports is confirmed at the pin, but whether it should change is a design question with no decision in the ticket. close-fixed and close-obsolete do not fit (no fix commit, and the code path exists). pr-needed would need the design decision first, since the change alters the output for every listed-terms request.

Missing for a closing or PR verdict: the maintainer decision on applying limit and offset to listed terms, and a test for the expected output.

## Limits

This record does not run a `{!terms}` request. It does not trace the history of `getListedTermCounts`. It does not post anything to JIRA or GitHub.
