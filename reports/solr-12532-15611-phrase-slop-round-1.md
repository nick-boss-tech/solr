# SOLR-12532 / SOLR-15611 phrase slop implementation, round 1

Status: **Implemented, Proof Pending**. Owner-directed adjacent work for draft PR 5294; no public PR opened or edited by this implementation.

| Revision | Full SHA |
| --- | --- |
| Unchanged PR 5294 / parent P | `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` |
| Pushed follow-up S | `44849d2c4ca0cae283c26bc45048f824ea0d53c4` |
| Prior base-only fork candidate, unchanged | `69c06da446797cec5c3c8dd885e19a28381789d3` |

Branch `nick-boss-tech/solr:solr-12532-15611-phrase-slop` was plain-pushed and independently read back through the GitHub refs API at S. Nick Shanin authored the ordinary commit. Exact outbound files: SolrQueryParserBase.java, ExtendedDismaxQParser.java, TestMultiWordSynonyms.java and changelog/unreleased/SOLR-12532-15611-explicit-phrase-slop.yml. Commit: 4 files, 141 additions and 9 deletions.

## Decision ladder and code assessment

Rung 1 settles the explicit-slop contract: at P, handleQuotedTerm parses ~N, but graph analysis in newFieldQuery uses phraseSlop before the integer overload attempts a top-level-only rewrite. Boolean graph paths therefore retain the default. eDisMax's distinct consumer at ExtendedSolrQueryParser.getQuery has the same construction-time defect. The stale subQParser field also causes the base overload's top-level rewrite to be skipped after an earlier _query_ clause. Rung 3 corroborates scope through SOLR-12532, overlapping SOLR-11022 and SOLR-15611. Rung 2 proof remains pending; source inspection and new assertions are not executed proof.

The selected implementation supplies the explicit slop while constructing graph paths and restores the previous default in finally. It resets the base guard per quoted clause and retains existing top-level rewrite behavior for custom field implementations. It avoids recursively changing every phrase under a query returned by another parser. Existing fork SOLR-12532's private recursive helper only covers the base parser; historical lucene-solr PR 614 only handled SpanNearQuery. Neither was copied as complete current proof. Closed/unmerged PR 280 for SOLR-15611 supplied history, not an assertion that the bug was fixed upstream.

Both real consumers were assessed, along with parse ownership and the phrase-boost parser's preconfigured slop. No new API/helper in production or analyzer dependency was added. Tests reuse the existing schema, synonyms and four-document fixture. The only new fixture helper extracts the original four inserts; no extra document IDs or changed testPf assertions are introduced. Added methods check both graph alternatives and matching, explicit ~0 over qs=2, following default phrases, a non-graph phrase after _query_, and preservation of nested parser slop.

## Verification and limits

Windows source review and diff --check passed. No Gradle, compiler, formatter or tests ran here. Required Linux proof is specified by `gates/solr-12532-15611-phrase-slop-round-1.md`, including full parser neighbors, the original test's compatibility run, original-production fail-before, two restoration mutations, YAML/tidy/Error Prone/root check. Receipt reserved: `receipts/SOLR-12532-15611-phrase-slop-round-1.md`.

No new runtime correctness claim is made before that receipt. The guard-reset line is not claimed independently mutation-proven because construction-time slop also repairs ordinary phrase construction. Parser extension behavior and analyzer-specific graph shapes remain material review boundaries covered by the requested neighbor suites. This branch is stacked on PR 5294 for the shared regression fixture; before a later independent PR, reconcile its ancestry and obtain proof for the actual submission head.

Claim: `claims/solr-12532-15611-phrase-slop-round-1-windows.md`. Current testPf and PR 5294 head remain unchanged. No public communication was posted.
