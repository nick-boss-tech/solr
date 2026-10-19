# Pool assignment: SOLR-18555 fixes on solr-18555-submit, then gate (round 1)

Status: OPEN

- Capability tags: implementation, gate.
- Intended host: any Linux host with the Gradle toolchain. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm3, 2026-10-19, at the direction of Nick Shanin.

The gate on `solr-18555-submit` at head `884de51579` is NOT GREEN. Its receipt is `receipts/SOLR-18555.md`. This assignment fixes what the receipt lists, applies the starting patch `gates/SOLR-18555-local-fixes.patch`, and files a new gate job at the resulting head. The patch was produced by vm3 as a local experiment. It is a starting point and not an authority; check every hunk.

## Items

1. **Apply the starting patch.** Check out `solr-18555-submit` at `884de51579`. Apply `gates/SOLR-18555-local-fixes.patch` with `git apply`. It has four changes: the changelog fragment (`type: added`, a `links` entry for SOLR-18555); the missing `org.apache.solr.search.SyntaxError` import in `HighlightRequestHandler.java`; the rename of the private `params(String...)` helper in `HighlightRequestHandlerTest.java` to `paramsSet`, so it no longer hides the inherited helper; and the reflection rewrite in `newHandler()` (`asSubclass` and `LinkageError`). Start from: `gates/SOLR-18555-local-fixes.patch`. Output: the changed files on the branch.
2. **Remove the unused import.** `HighlightFlag` in `HighlightRequestHandler.java` (line 31) is never used, and the ECJ lint in the module check fails on it. Remove it. Start from: the file after item 1. Output: the same file.
3. **Apply the main-agent decisions below.** Preserve the phrase-highlight expectation and fix the handler path. Update the HTML and parser-rejection test expectations as decided, while preserving production HTML encoding and parser isolation. If the phrase behavior cannot be implemented confidently, stop and report the evidence instead of weakening that test.
4. **Run tidy on the branch.** Run `:solr:core:tidy` after all code and test edits. Commit any formatting changes so the tree is clean before the gate. Output: a tidy-clean tree.
5. **Commit and push the fixes.** Commit the code and test changes from items 1-4 as Nick Shanin, with no trailers or em dashes and messages `SOLR-18555: <summary>`. Push as a fast-forward from `884de51579`. Output: the new head on `solr-18555-submit`.
6. **File a gate job at the new head.** Use `pool/bin/request-gate.py` with `pool/templates/gate-request.md`, after item 5. The ticket-level `gates/SOLR-18555.md` already exists, so use `Gate ID: SOLR-18555-phase-fix-round-1` and post `gates/SOLR-18555-phase-fix-round-1.md`. Set `Focused class` to `org.apache.solr.handler.HighlightRequestHandlerTest`, which contains all three affected assertions. The HTTP class passed with 2 tests and no failures in gate 4, and this round does not change request routing. In the Why field, summarize the prior NOT GREEN receipt and the three main-agent decisions. Output: the phase gate job on coordination.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Starting patch | decided: apply it and check each hunk | none | Nick Shanin |
| Unused import | decided: remove it | none | Nick Shanin |
| Tidy changes | decided: run tidy and commit its changes before the gate | none | Nick Shanin |
| Push | decided: fast-forward from 884de51579 only | if the branch has moved, stop and record the new head here | Nick Shanin |
| testPhraseAndMultiTermQueries: expected one `<em>` span, handler gives one per word | decided: handler behavior is wrong | Keep the expected `<em>Solr highlights</em>` phrase span. Fix query and highlighter handling so default strict phrase highlighting works without a searcher; do not merge adjacent term spans as a formatting workaround. | Nick Shanin |
| testHtmlEncoderEscapesSuppliedText: expected raw `/`, encoder writes `&#x2F;` | decided: test expectation is wrong | Expect `&#x2F;` for the slash and keep HTML encoding. Existing `TestUnifiedSolrHighlighter` coverage expects this encoded slash. | Nick Shanin |
| testRejectsParserOverridesAndParserSubqueries: expected SyntaxError, handler throws SolrException (undefined field _query_) | decided: exception-class expectation is wrong | Keep `setAllowSubQueryParsing(false)` and the built-in Lucene parser. Assert `SolrException` with BAD_REQUEST for the `_query_` input; do not wrap it as SyntaxError. | Nick Shanin |

## Deliverable

`assignments/pool-solr-18555-fix-round-1.md` Status updated to DONE with the new head, and `gates/SOLR-18555-phase-fix-round-1.md` from item 6. The deliverable also lists each item's status and confirms the three decisions above.

## Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
