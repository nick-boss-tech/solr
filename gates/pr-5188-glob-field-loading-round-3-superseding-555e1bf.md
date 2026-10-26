# Gate: PR 5188 current API and regression boundaries, superseding round 3 (pinned `555e1bf`)

Status: OPEN
Tags: gate, implementation, review
Intended host: vm6 (Linux, JDK 21.0.12.1+1), claim `claims/pr-5188-round-3-gate-vm6.md`.
Supersedes: `gates/pr-5188-glob-field-loading-round-3.md` at head H `f00caeb5e83ac375672ae27fb43ee1546795e98b`, under its repair rule (tidy changed one test file). Repair: `reports/pr-5188-glob-field-loading-repair-round-3.md`.

## Pinned inputs

- Final head F6: `555e1bf2398c08b1018ed44c056274c727bd6454`.
- Current PR production B: `db867d80365510790bb6a04ce33105cd196cf34c`. Pre-fix tree P: that production, verified, plus F6's `BasicFunctionalityTest.java` (blob `1303fbab6b64abd4289cd0ca4a14f59831a43830`, sha256 `bbbd61eead2610a458a1b4cb1c1b5bed51d89e6f21813398d90bdea4085d8552`) and `TestPseudoReturnFields.java` (blob `b81b328189af7f2a3c273009890c642ed946d44e`).
- Seed `5188C0DE`. Module `:solr:core`.

## Legs

1. tidy at F6, tree clean after.
2. Positive snippets on P: `testGlobHighlightSnippets`, all 24 rows.
3. Pre-fix sensitivity on P, one invocation each: `testGlobRTGStoredFieldLoading` (expected `unrelated_s should remain lazy`), `testGlobHighlightPrefetch` (expected `original: source_* did not prefetch source_s`), `testGlobRTGEmptyAndAliasFieldLoading` (expected a `title` lazy-state failure on the empty glob), `testGlobPrefetchBoundaries` (expected `Missing prefetched document` on the 50-row non-shard glob case).
4. Post-fix focused on F6: the six methods named in the round-3 gate.
5. Post-fix suites on F6: `BasicFunctionalityTest`, `TestPseudoReturnFields`, `TestRetrieveFieldsOptimizer`, `ReturnFieldsTest`, `TestUnifiedSolrHighlighter`, `LargeFieldTest`.
6. On F6: tidy, compileJava and compileTestJava with `-Pvalidation.errorprone=true`, `:solr:core:check -x test`.

No empty-snippet waiver. Logs: `/workspace/gates/logs/vm6/pr5188-round-3-superseding-555e1bf/`.
