# PR 5188 restore unified snippet coverage, implementation round 2

Status: Implemented, Proof Pending
Host: windows (Codex desktop)
Authority: Nick requested another gate and more thorough coverage after the round-1 receipt's unified-snippet exception was explained. No owner acceptance of that exception is recorded.
Claim: claims/pr-5188-unified-snippet-windows-round-2.md.
Intake board: 806244f102166f68a2b465411557cacb54bfe026. Ownership/publication snapshot: 3e3d75b385763134b86343c024d5713d236827bb.

## Pushed repair

- Branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup.
- Prior receipt head: 51e92b63fddb97db83a3962f758bde3c79f1c4dd.
- New head, pushed normally and verified through the fork Git Ref API: cb8d3dcf0172515e11b92a9d1c7d9aa186f1364c.
- Author/committer: Nick Shanin <nick.boss.us@gmail.com>; no attribution trailers.
- Only changed file: solr/core/src/test/org/apache/solr/BasicFunctionalityTest.java (47 insertions, 6 deletions).
- Compare: https://github.com/nick-boss-tech/solr/compare/51e92b63fddb97db83a3962f758bde3c79f1c4dd...cb8d3dcf0172515e11b92a9d1c7d9aa186f1364c

Production files are byte-identical to the round-1 receipt head. The gate host's justified XPath and uncommitted-fixture repairs remain. The old receipt establishes the earlier loading proof at its own head and explicitly lacks unified positive snippets; it does not establish the new test head's proof.

## Fixture and assertions

The old loading test combined the ID and text clauses in the main query (id:7783 AND title:keyword). Round-1 probes found no unified snippet for that fixture on either pre-fix or fixed production. Removing the snippet assertion left part of the requested coverage unproven.

The repaired loading test uses q=title:keyword with fq=id:7783. The filter selects exactly the fixture document while the main query provides a simple text term for implicit highlighting. It restores the unconditional exact <em>keyword</em> assertion for both original and unified. Every eager/lazy cache assertion and the exact response-field count remain. This changes only the test setup; it does not claim to identify or fix the cause of the old Boolean-query snippet behavior.

A separate testGlobHighlightSnippets method checks positive snippets independently from the loading assertion. Its 24 requests cover:
- original and unified highlighters;
- implicit highlight query (q=title:keyword) and explicit hl.q=title:keyword (main q selects by ID);
- hl.weightMatches=true and false;
- fl=source_s, fl=source_*, and fl=*.

Each row requires one matched document, the stored response value, and the exact marked-up title snippet. It does not use hl.defaultSummary, an alternate field, optional assertions, or a no-snippet success case. The fixture document is deleted and committed in finally. Existing loading tests retain cold-cache checks; the new matrix is an output compatibility control.

Rung 1: HighlightComponent.java:100-109 parses explicit hl.q, and lines 144-163 use the main parser's highlight query if no explicit query was provided. Rung 2 remains for Linux: the new positive matrix must pass on original production with these final tests and on the candidate. If it does not, diagnose and repair the fixture; do not restore the exception.

## Fresh proof request

New job: gates/pr-5188-glob-field-loading-round-2.md. Required: positive snippet matrix on pre-fix and post-fix; two discriminating pre-fix loading failures with the same final tests/seed; all four focused methods; full modified/neighbor suites plus TestUnifiedSolrHighlighter; tidy, Error Prone compilation and core check. Archive commands, logs, seed, hashes and fresh XML at a durable location linked from the board report. VM-local paths alone do not finish the handoff.

Pre-fix production remains e07455a0f953e702da27f677c51ea894b7bafc3a with the final two test files overlaid. New receipt: receipts/PR-5188-glob-field-loading-round-2.md. Do not rewrite the round-1 job/receipt or treat its narrowed green legs as final acceptance of this head.

## Static validation and limits

Full repair diff reviewed; git diff --check passed; worktree clean after commit. No Gradle, compilation, test, formatting tool, benchmark or profiling ran on Windows. Linux must confirm the candidate fixture and produce a full receipt. No public PR, comment, JIRA write or original-contributor branch update was performed.

Final test-overlay SHA-256 (LF bytes):
- BasicFunctionalityTest.java: b315592ed18dbc8091cb7eb2d1dc30b28dd61d8568790de52e6cc8feaa6cfc16
- TestPseudoReturnFields.java: 577a9b132f5b44820a9c3c8d8f8c1d4a176cd678bc8be8ff5d1bc2ea84b4d393
