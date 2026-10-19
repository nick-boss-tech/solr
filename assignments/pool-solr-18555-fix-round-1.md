# Pool assignment: SOLR-18555 fixes on solr-18555-submit, then gate (round 1)

Status: OPEN

- Capability tags: implementation, gate.
- Intended host: any Linux host with the Gradle toolchain. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm3, 2026-10-19, at the direction of Nick Shanin.

The gate on `solr-18555-submit` at head `884de51579` is NOT GREEN. Its receipt is `receipts/SOLR-18555.md`. This assignment fixes what the receipt lists, applies the starting patch `gates/SOLR-18555-local-fixes.patch`, and files a new gate job at the resulting head. The patch was produced by vm3 as a local experiment. It is a starting point and not an authority; check every hunk.

## Items

1. **Apply the starting patch.** Check out `solr-18555-submit` at `884de51579`. Apply `gates/SOLR-18555-local-fixes.patch` with `git apply`. It has four changes: the changelog fragment (`type: added`, a `links` entry for SOLR-18555); the missing `org.apache.solr.search.SyntaxError` import in `HighlightRequestHandler.java`; the rename of the private `params(String...)` helper in `HighlightRequestHandlerTest.java` to `paramsSet`, so it no longer hides the inherited helper; and the reflection rewrite in `newHandler()` (`asSubclass` and `LinkageError`). Start from: `gates/SOLR-18555-local-fixes.patch`. Output: the changed files on the branch.
2. **Remove the unused import.** `HighlightFlag` in `HighlightRequestHandler.java` (line 31) is never used, and the ECJ lint in the module check fails on it. Remove it. Start from: the file after item 1. Output: the same file.
3. **Run tidy on the branch.** Tidy changed whitespace in `HighlightRequestHandlerTest.java` and `HighlightRequestHandlerHttpTest.java` at the old head. Run `:solr:core:tidy` and commit whatever it changes, so the tree is clean before the gate. Output: a tidy-clean tree.
4. **Commit and push the fixes.** One commit per item, author Nick Shanin, no trailers, no em dashes, message `SOLR-18555: <summary>`. Push as a fast-forward from `884de51579`. Output: the new head on `solr-18555-submit`.
5. **Do not decide the three failing expectations.** They are listed in the Decisions table. Stop at them; do not edit the expectations or the handler to make them pass.
6. **File a gate job at the new head.** Use `pool/bin/request-gate.py` with the request template (`pool/templates/gate-request.md`), after item 4. Name the three failing tests as a known open item in the Why field. Output: `gates/SOLR-18555.md` on coordination.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Starting patch | decided: apply it and check each hunk | none | Nick Shanin |
| Unused import | decided: remove it | none | Nick Shanin |
| Tidy changes | decided: run tidy and commit its changes before the gate | none | Nick Shanin |
| Push | decided: fast-forward from 884de51579 only | if the branch has moved, stop and record the new head here | Nick Shanin |
| testPhraseAndMultiTermQueries: expected one `<em>` span, handler gives one per word | escalate to the main agent | do not change the test or the handler; record the failure text here | Nick Shanin |
| testHtmlEncoderEscapesSuppliedText: expected raw `/`, encoder writes `&#x2F;` | escalate to the main agent | do not change the test or the handler; record the failure text here | Nick Shanin |
| testRejectsParserOverridesAndParserSubqueries: expected SyntaxError, handler throws SolrException (undefined field _query_) | escalate to the main agent | do not change the test or the handler; record the failure text here | Nick Shanin |

## Deliverable

`assignments/pool-solr-18555-fix-round-1.md` Status updated to DONE with the new head, and `gates/SOLR-18555.md` from item 6. The deliverable also lists each item's status and the three escalated tests.

## Limits

- No PR, comment, JIRA or email. No force push.
- Do not change the three escalated tests or their expected values.
- Commit as Nick Shanin with no trailers. No em dashes.
