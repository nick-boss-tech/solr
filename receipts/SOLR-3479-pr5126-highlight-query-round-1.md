# Receipt: SOLR-3479 (PR 5126 highlight-query fix, round 1)

## Gate SOLR-3479-pr5126-highlight-query-8a685825b6 (vm4, 2026-10-25): GATE GREEN at 8a685825b6 (superseded fix head 23c159d06b: compile failed)

- Branch: `solr-3479-submit`. Head: `8a685825b6202f45eb981d3b8e7f05b01a5c1829` (confirmed by ls-remote after the push). Base: `74b09fb443f060caac3f8ec9701909cec276cfb4` (original PR 5126 head, merge base confirmed by the GitHub compare API).
- Links: Ticket or parent: SOLR-3479, apache/solr PR 5126. Category: gate. Siblings: the Q reproduction on diagnostic branch `solr-3479-pr5126-query-examples-r1` at `022eb6d781` (not this patch). Supersedes: the first fix head `23c159d06bd86180e0bbd4b474517b60b693f926` (compile failure), recorded in `reports/pr-5126-highlight-query-fix-repair-round-1.md`.
- Runner: `pool/bin/sweep-run.sh` on vm4, JAVA_HOME `/workspace/tools/jdk21` (21.0.12.1). Log: `/workspace/gates/logs/vm4/pr-5126-fix/` (raw, VM-only). Durable copies: `reports/pr-5126-highlight-query-fix-gate-round-1-evidence/`.
- Step 0, changelog YAML parse: not applicable; the patch adds no changelog fragment.
- Step 1, tidy: rc=0 on the final head; post-tidy status clean (preflight at 23c159d06b also clean). The repair commit's only change is the annotation.
- Step 2, Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`): rc=0 on `8a685825b6`. Superseded: rc=1 on `23c159d06b` (`ReferenceEquality` at `HighlightAugmenterFactory.java:195-196`, promoted by `-Werror`).
- Step 3, proof leg: fail before on original production `74b09fb443f0` with the fix test file overlaid: `testImplicitQueryUsesHighlightQuery` FAILED at `TestHighlightAugmenterFactory.java:200` with `Boost-only text must not be highlighted: SolrDocument{id=2, body_hl=A story about <em>cats</em>. <em>Cats</em> sleep a lot.}`. Expected sensitivity failure, by content.
- Step 4, focused head run: `org.apache.solr.response.transform.TestHighlightAugmenterFactory`, seed `34795126`: 11 tests, 0 failures, 0 errors, 0 skipped (fresh JUnit XML, copied to `reports/pr-5126-highlight-query-fix-gate-round-1-evidence/pass-after-TestHighlightAugmenterFactory-TEST.xml`).
- Neighbor: `org.apache.solr.highlight.TestUnifiedSolrHighlighter`, seed `34795126`: 33 tests, 0 failures, 0 errors, 0 skipped (copied to `.../neighbor-TestUnifiedSolrHighlighter-TEST.xml`).
- Step 5, module check `:solr:core:check -x test`: rc=0 on `8a685825b6`. Module check only; not a full repository check.

### Proposed incremental patch (PR-description summary)

SOLR-3479: use the search request's highlight query for implicit inline snippets.

The highlight transformer used the full search query for implicit highlighting, which includes eDisMax boost clauses (`bq`). With `hl.weightMatches=false` that let boost-only terms such as `cats` appear as snippets in documents that matched only on the user query. The transformer now takes the search request's parser and uses `getHighlightQuery()`, the same query the highlight component uses. Explicit `hl.q` and transformer-local `q` still override it, and the old context-query fallback remains for results that do not belong to the current request.

Files:
- `solr/core/src/java/org/apache/solr/response/transform/HighlightAugmenterFactory.java`: `getMainHighlightQuery` reads the request's parser when the result context belongs to that request and query; one `@SuppressWarnings("ReferenceEquality")` on that method for the intentional identity check.
- `solr/core/src/test/org/apache/solr/response/transform/TestHighlightAugmenterFactory.java`: three tests (`testImplicitQueryUsesHighlightQuery`, `testImplicitQueryUsesMainParser`, `testExplicitHighlightQueryOverridesMainQuery`) covering both `hl.weightMatches` settings, the component on and off, explicit and local overrides, `q.alt`, and a local-parameter main query.

Verification: the new sensitivity test fails on the original production code at this test's first implicit assertion and passes on this head; the focused class (11) and `TestUnifiedSolrHighlighter` (33) pass; tidy, Error Prone compile, and `:solr:core:check -x test` pass.

Not included: real-time get, the `/export` alias issue, and reference-guide or changelog changes.
