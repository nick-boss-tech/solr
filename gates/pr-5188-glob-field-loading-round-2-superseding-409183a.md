# Gate: PR 5188 full snippet and field-loading proof, superseding round 2 (pinned `409183a`)

Status: DONE

Completed 2026-10-28 (cleanup slice 2, phone agent): executed by vm6 at the pinned head `409183a`; the receipt `receipts/PR-5188-glob-field-loading-round-2.md` records the full run with the proof pair. PR 5188 is merged.
Tags: gate, implementation, review
Intended host: vm6 (Linux, JDK 21.0.12.1+1), claim `claims/pr-5188-glob-field-loading-round-2-vm6.md`.
Supersedes: `gates/pr-5188-glob-field-loading-round-2.md` at head H `cb8d3dcf0172515e11b92a9d1c7d9aa186f1364c`, under its repair rule (tidy changed one test file). Repair: `reports/pr-5188-glob-field-loading-repair-round-2.md`.

## Pinned inputs

- Final head F5: `409183aa9be8c8500942eadf22d036abaf1ead5c`.
- Pre-fix tree P: original production `e07455a0f953e702da27f677c51ea894b7bafc3a` (verified, the three production files match) plus F5's `BasicFunctionalityTest.java` (blob `1a320e232f3d96510997476223d69949aadcce78`, sha256 `b2be8289da46dd7d3d16273a7512872162a8dcdddec52976a86143a38e63592e`) and `TestPseudoReturnFields.java` (blob `b81b328189af7f2a3c273009890c642ed946d44e`, identical at H and F5).
- Seed `5188C0DE`. Module `:solr:core`.

## Legs

Same as the round-2 gate, at F5 and P: (1) verify P and F5 identities; (2) `testGlobHighlightSnippets` on P, all 24 rows positive; (3) `testGlobRTGStoredFieldLoading` on P, expected `unrelated_s should remain lazy`; (4) `testGlobHighlightPrefetch` on P, expected `original: source_* did not prefetch source_s`; (5) focused on F5, four methods; (6) suites on F5 incl. `TestUnifiedSolrHighlighter`; (7) tidy, compileJava, compileTestJava with Error Prone, check on F5.

No unified-snippet exception. Logs: `/workspace/gates/logs/vm6/pr5188-round-2-superseding-409183a/`.
