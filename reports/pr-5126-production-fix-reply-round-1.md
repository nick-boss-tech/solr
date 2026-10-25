# PR 5126 production fix reply, round 1 (draft, not posted)

Status: AWAITING NICK SHANIN'S REVIEW. Not posted. Posting needs Nick's explicit approval. The verification paragraph is written from the final head `8a685825b6202f45eb981d3b8e7f05b01a5c1829` results in `reports/pr-5126-highlight-query-fix-gate-round-1.md`.
Source: `reports/pr-5126-highlight-query-fix-gate-round-1.md`, `reports/pr-5126-highlight-query-fix-repair-round-1.md`, `reports/pr-5126-query-examples-round-1-revision-1.md`.

## Draft text

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@dsmiley, you are right that the search parser's highlight query leaves out eDisMax boost clauses. The patch relies on exactly that. The gap was in the transformer. For an implicit highlight (no `hl.q` and no local `q`), it never asked the parser. It used the request's full query, which includes `bq`. The helper that reads `hl.q` returns nothing when `hl.q` is absent, so the code fell back to that full query.

Example, in plain terms. Document 1 has the title "Fox story" and the body "The fox ran away." Document 2 has the title "Fox cameo" and the body "A story about cats. Cats sleep a lot." The query `q=fox` matches both documents, and document 2 matches only through its title. The boost clause `bq=body_t:cats` is meant to affect ranking. It should not put "cats" into a snippet.

The original reproduction needed `hl.weightMatches=false`. With the default `hl.weightMatches=true`, the same request did not produce the cats snippet on this fixture. Document 2 had no `body_hl` at all. So the problem appears only with `false`.

The proposed patch changes two files, both under `solr/core`: `HighlightAugmenterFactory.java` and `TestHighlightAugmenterFactory.java`. When the result context belongs to the current request, the transformer now uses that request's own parser and its `getHighlightQuery()`, which is the query the highlight component already uses. Explicit `hl.q` and a local `q` still override it. The old fallback stays in place for results that come from another request.

Verification on the final commit `8a685825b6`. A new test that checks boost-only text fails on the original code at the cats snippet and passes with the patch. The focused test class passes 11 of 11, and `TestUnifiedSolrHighlighter` passes 33 of 33. Tidy, the compile with Error Prone, and the module check pass. The first commit failed Error Prone's reference-equality check on two comparisons that are meant to be identity checks. The second commit suppresses that one check on one method. It changes no logic.

The diff against David's branch is here: https://github.com/dsmiley/solr/compare/SOLR-3479-highlightDocTransformer...nick-boss-tech:solr-3479-submit. The immutable comparison of the two tested commits is here: https://github.com/nick-boss-tech/solr/compare/74b09fb443f060caac3f8ec9701909cec276cfb4...8a685825b6202f45eb981d3b8e7f05b01a5c1829.

On real-time get: the PR description's Solution bullet names real-time get, and the JIRA comment on SOLR-3479 says the current highlighter cannot support it, because it has to read from the index. This patch does not change that and adds no real-time get support or test. That is separate work.

The `/export` alias question is already tracked in #5188 and is not addressed here.

Would this evidence and this narrow change settle the query-selection concern for you, or would you want a different test or scope?
