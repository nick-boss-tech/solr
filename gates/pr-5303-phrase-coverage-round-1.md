# Gate: PR 5303 phrase-boost coverage, round 1

Status: OPEN

- Category: owner-authorized test-only follow-up to another contributor's PR. Tags: gate, review, implementation, records.
- Intended host: capable Linux CPU gate host, JDK 21, standard runner/resource guard, one build at a time under the host lock. No Gradle on Windows.
- Contributor PR: apache/solr #5303 (SOLR-18625), Ian Springer, OPEN at H `e7f66ef3220f412bc3a54b566ca78ce4a906715e`.
- Contributor base/merge base B: `1fc38d1e40cb12512aba2b1990b22b04111dad2d`.
- Fork branch: `nick-boss-tech/solr:pr-5303-phrase-coverage`.
- Exact offered head F: `256612b7f85dc831f9332d51182258d9594fcc1f`, one ordinary child of H.
- Only outbound path H..F: `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, +66/-22. Production, changelog, schema and synonym files must remain byte-identical to H.
- Claim: `claims/pr-5303-phrase-coverage-round-1-windows.md`.
- Report: `reports/pr-5303-phrase-coverage-round-1-windows.md`.
- New receipt: `receipts/PR-5303-phrase-coverage-round-1.md` (executor-owned).
- Fixed seed: `5303C0DE` throughout. Record actual commands, fresh log/XML paths, UTC intervals, runner/JDK/host and exact source/test/mutation blobs.

## Purpose and proof boundary

This adds optional coverage to Ian's existing fix. H and F with their own tests are both expected to pass; this patch establishes no new production defect. Preserve the original test's plain-token assertion, separate fielded-only and match-all regressions, separate wi-fi and configured grackle synonym preservation, and exercise both sow modes. qf=id isolates the grackle subject phrase to pf. No new analyzer fixture or helper is needed.

The five methods in TestExtendedDismaxParser are:

- testNoEmptyPhraseBoostClause
- testNoEmptyPhraseBoostClauseForFieldedQuery
- testMatchAllDocsWithPhraseBoost
- testAnalyzedSingleTermKeepsPhraseBoost
- testSingleTermSynonymKeepsPhraseBoost

Every method loops sow=true first, then sow=false. Mutations below deliberately target the false mode separately so an early true-mode failure cannot be mistaken for proof of both modes. Restoring pre-PR production demonstrates coverage of Ian's already-fixed bug, not that our test-only change fixes a new bug. Parser allocation/performance and the early zero-clause guard in isolation remain unproven; later empty-result filtering also protects query correctness.

## Preflight

Refresh live job, fork ref, PR head/state/discussion, claims and receipts. Require F and its immediate parent H. Stop and reconcile before running if Ian's head changes or the PR closes/merges; do not silently transport evidence to a new head. First claim wins. Use clean full Linux checkouts, not the sparse Windows worktree. Run the standard detached root tidy preflight at F before claiming. Observe START-HERE/WORKFLOW and the host disk/inode guard; preserve fresh artifacts before another cleanTest overwrites them.

## Required acceptance legs

1. At F confirm the exact test-only diff and unchanged production/changelog/configuration blobs against H. Run root `./gradlew tidy` and `git diff --check`; require a clean tree. Run `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`. No new changelog is required for the test-only delta; record the inherited fragment's unchanged identity. A module tidy result is not labelled root tidy.
2. On clean H and then clean F, run the same selected suite command separately:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser --tests org.apache.solr.search.QueryEqualityTest --tests org.apache.solr.DisMaxRequestHandlerTest --tests org.apache.solr.search.TestMultiWordSynonyms.testEdismaxQueryParsing_multiTermWithPf_shouldParseCorrectPhraseQueries -Ptests.seed=5303C0DE --rerun-tasks`

   Require zero failures/errors/skips at both heads and record actual fresh XML counts by class/method. Expected ExtendedDismax counts: H 40, F 44 (four additional independently executed methods). Require all five named coverage methods at F and confirm each loop reaches both sow modes. QueryEqualityTest and DisMaxRequestHandlerTest are Ian's named neighbors. The single MultiWordSynonyms query-shape method is verified to exist at H. Do not substitute the full MultiWordSynonyms class: its unrelated testPf is the unmerged known flake addressed by PR 5294; this job must not incorporate our PR 5294 file or silently absorb that issue.
3. Run the compiling mutation legs below in disposable F checkouts, resetting to clean F between them. Keep the entire F acceptance test file byte-identical in each leg. Preserve exact mutation diff, source/test hashes, commands/exits, fresh XML/logs and intended assertion output. Missing APIs, compilation errors, startup failures or skipped tests are not runtime proof.
4. Restore clean F and run root `./gradlew check -x test`. Record material execution conditions, including NODE_ENV and any checksum-verified cache/environment repair; do not call an earlier failed download a pass. Require success at F, clean source, and a separate final read-back of the fork and contributor PR heads. No mutation may remain in an offered or accepted checkout.

## Mutation legs and expected results

Use `:solr:core:cleanTest :solr:core:test`, seed `5303C0DE`, `--rerun-tasks` and explicit full class.method selectors. Do not use a wildcard that obscures actual selected counts.

### M1: pre-PR production, unchanged F tests

Restore only ExtendedDismaxQParser.java from B at F. Run all five named methods. Expect five executed tests: three intended failures (plain-token empty clause, fielded-only empty clause, MatchAllDocsQuery type) and two passing preservation controls. Each failing method must fail at its true-mode assertion; both preservation methods must complete both modes. This is the existing bug's negative control with executable original APIs, not a production repair in our patch.

### M2: empty-clause regression restricted to sow=false

Start from clean F. In addShingledPhraseQueries' guard, change only `clauses.isEmpty()` to `(clauses.isEmpty() && config.splitOnWhitespace)`. Change its final phrase-add condition from `!isEmpty(phrase)` to `!isEmpty(phrase) || (!config.splitOnWhitespace && phrase != null)`. Here config is the existing outer parser configuration. These paired changes retain H's correct true-mode behavior while recreating the old empty-clause behavior only in false mode.

Run all five methods. Expect five executed tests: the same three failures, now after their true-mode checks pass and at the false-mode assertion; both preservation controls still pass. Capture the requests/sow setting in assertion output or disposable diagnostic logging if needed, without changing the acceptance tests. This discriminates the added false-mode coverage of both the ordinary and fielded inputs and the match-all result.

### M3: over-aggressive one-raw-clause shortcut

Start from clean F. At the top of addPhraseFieldQueries(BooleanQuery.Builder, List<Clause>, ExtendedDismaxConfiguration), insert only `if (clauses.size() == 1) return;`. Retain the rest of the parser and all APIs. Run the two preservation methods explicitly. Expect two intended assertion failures in true mode: wi-fi loses its pf boost; grackle loses the subject phrase/boost. No errors/skips.

### M4: the same shortcut restricted to sow=false

Start from clean F. Insert only `if (!config.splitOnWhitespace && clauses.size() == 1) return;` at the same location. Run the two preservation methods. Require their true-mode assertions to pass, followed by two intended false-mode assertion failures. No errors/skips. This discriminates the newly explicit false-mode analyzer coverage rather than counting M3's early failures twice.

## Failure/repair rules and completion

No silent retry of an executed failure or weakening assertions to force the gate green. Diagnose and record a baseline, head, mutation or formatting mismatch before the next action. If a mutation passes, report that assertion as a coverage pin until a predeclared discriminating variant establishes it; do not broaden the mutation into a parser crash. Preserve acceptance test bytes during mutations.

A scoped source/formatting repair requires an ordinary fork commit, separate remote verification, an add-only superseding exact-head gate and fresh required proof. The gate host may apply the standing tidy repair route, but must record old/new heads and never certify changed source under F. Follow WORKFLOW's documented pre-execution environment repair rules.

Receipt must state H/B/F and current public state, exact source/test blobs, host/JDK/runner/UTC intervals, full commands/exits/seeds, fresh suite/mutation XML counts, root tidy/Error Prone/root-check results, all four mutation diffs and intended failures with modes, remaining pins and final ref read-back. Earlier E3 receipts are background only and prove neither H nor F. Until the new required legs succeed, status is **Implemented, Proof Pending**.

No PR opening, contributor/upstream push, force push, public comment/review, live PR-body edit or JIRA action is authorized by this job. A posted handoff is not a claim or execution result. Prepare factual results for later review; do not publish an offer from this gate.
