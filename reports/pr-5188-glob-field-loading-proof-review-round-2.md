# PR 5188 round-2 proof review (Windows)

Status: VERIFIED for the requested gate scope at 409183aa9be8c8500942eadf22d036abaf1ead5c.
Reviewer host: windows (Codex desktop), 2026-10-26T19:08:27.658Z.
Claim: claims/pr-5188-glob-field-loading-proof-review-windows-round-2.md.
Evidence snapshot: 82eabc8bf96b31aeadc981af8d396bb471843b21.
Receipt: receipts/PR-5188-glob-field-loading-round-2.md.
Job: gates/pr-5188-glob-field-loading-round-2-superseding-409183a.md.

## Verdict

The published evidence satisfies the mandatory round-2 gate. The unified-snippet exception is closed. The original job asked for focused/neighbor core suites and a core module check, not every core test or a repository-wide build. Those scope limits do not require another waiver or retroactively add missing mandatory legs.

This is technical acceptance of the recorded gate at its pinned revision. It does not authorize a public comment, PR submission, merge or an unsupported claim about a different head.

## Independent checks

- Read back the fork branch head as 409183aa9be8c8500942eadf22d036abaf1ead5c.
- Reviewed the complete cb8d3dcf0172515e11b92a9d1c7d9aa186f1364c-to-409183a diff: one test file, formatting only. Production paths are unchanged from 51e92b63fddb97db83a3962f758bde3c79f1c4dd.
- Read every manifest entry directly from Git blobs at the evidence snapshot and recomputed SHA-256 over the original bytes, including binary files. All 52 entries match. The directory has 53 files including MANIFEST.sha256 itself. Manifest SHA-256: ae6050f98d0a196e71db5a8f99c16aed8a1c25f2246e2b4c394be2ea8ce9239d.
- Parsed all 10 XML suite documents from the five actual test legs: pre-snippets, pre-rtg, pre-prefetch, post-focused and post-suites. Counts, testcase names, failure messages, errors and skips match the receipt. The XML copies taken after tidy/compile/check were not counted as new test runs.
- Checked driver.sh, every leg's raw log and status.txt. Each test leg runs cleanTest before its test filter. The printed seed is 5188C0DE. Status return codes and BUILD SUCCESSFUL/FAILED markers agree. The tidy record says 0 changed. Error Prone compileJava/compileTestJava and the core check executed and succeeded.
- Verified the final BasicFunctionalityTest blob/hash against the receipt and inspected the source loops: two highlighters x two query modes x two weightMatches settings x three field lists = 24 unconditional positive snippet rows. Final BasicFunctionalityTest SHA-256: b2be8289da46dd7d3d16273a7512872162a8dcdddec52976a86143a38e63592e.

## Results checked against raw XML

| Leg | Tests / failures / errors / skipped |
| --- | --- |
| Pre-fix positive snippet matrix | 1 / 0 / 0 / 0 |
| Pre-fix RTG | 1 / 1 / 0 / 0; unrelated_s should remain lazy |
| Pre-fix prefetch | 1 / 1 / 0 / 0; original: source_* did not prefetch source_s |
| Post-fix focused, BasicFunctionalityTest | 3 / 0 / 0 / 0 |
| Post-fix focused, TestPseudoReturnFields | 1 / 0 / 0 / 0 |
| Post-fix BasicFunctionalityTest | 26 / 0 / 0 / 0 |
| Post-fix TestPseudoReturnFields | 35 / 0 / 0 / 0 |
| Post-fix TestRetrieveFieldsOptimizer | 1 / 0 / 0 / 0 |
| Post-fix ReturnFieldsTest | 16 / 0 / 0 / 0 |
| Post-fix TestUnifiedSolrHighlighter | 33 / 0 / 0 / 0 |

The full listed-suite leg totals 111 passing tests with zero failures/errors/skips. The independent positive matrix passes on original production; the same matrix passes among the post-fix focused and suite runs. The negative prefetch failure occurs in the original/glob row after the finite original control in the source's execution order. The two loading methods carry discrimination; the snippet matrix supplies positive compatibility coverage.

No new Gradle, compilation, test execution or formatting tool ran on Windows. This review checked the recorded Linux runs. Disk-I/O and latency measurements, all core tests and the whole repository remain outside the claims.

## Current upstream applicability, separate from the accepted proof

The live Apache PR was read during this review. It is OPEN and has advanced to db867d80365510790bb6a04ce33105cd196cf34c, one commit named renames after the original e07455a base. The new commit renames the loading API from wantsLuceneField to loadsField and isExplicitLuceneField to loadsFieldExplicitly; returnsAllFields replaces wantsAllFields, with a deprecated compatibility wrapper for the latter.

The tested follow-up still uses the original API. Its proof remains valid at 409183a on the recorded base, but it must not be described as a tested drop-in patch for the current live PR. A current-head port needs the API names adapted in RealTimeGetComponent/SolrDocumentFetcher and fresh proof for that resulting revision. The verified branch is preserved; no source merge, rebase, push, or new gate was performed during this receipt review.

No public PR/comment/JIRA action was performed. The receipt's proposed description remains a draft.
