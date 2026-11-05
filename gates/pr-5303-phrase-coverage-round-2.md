# Gate: PR 5303 phrase-boost coverage, round 2 (H2/F2)

Status: OPEN

- Category: owner-authorized test-only follow-up to another contributor's PR. Capability tags: `gate`, `review`, `implementation`, `records`.
- Contributor PR: [apache/solr #5303](https://github.com/apache/solr/pull/5303), SOLR-18625, OPEN at exact PR head H2 `fd106e02428bf1d619128029c73ea053fb4aedcb`; current target base head B2 `e1799d8a71726bd8ec2ad69f1e3a4b5c4f3e019f` (`main`). H2's direct parent and merge base is B1 `1fc38d1e40cb12512aba2b1990b22b04111dad2d`; B2 is one unrelated CLI commit above B1 and H2 is a sibling, not a rebased child of B2. Do not silently merge or transplant the candidate onto B2.
- Candidate fork branch: `nick-boss-tech/solr:pr-5303-phrase-coverage-round-2`; exact head F2 `07328d6973c4bb3385ca6d50bfe871f8808c1550`, one ordinary commit directly above H2. The prior branch `pr-5303-phrase-coverage` remains unchanged at F1 `256612b7f85dc831f9332d51182258d9594fcc1f`; no F1 test logs or proof are reused for this gate.
- Only H2..F2 path: `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` (+62/-21). H2 test blob: `c803f8618f113c0117927460501fb336859920b6`; F2 test blob: `a96884318e3a6da411b81f807c1d8f3685a7750d`. H2 production parser blob remains `57cd3d3689c6efaefa772ff27c65255f0e81b514`; inherited changelog blob remains `6969ec33db428f755e91fa7ca42c5c5d87be9718` (record actual full blob SHA from Git if differing in presentation). The exact H2..F2 diff is saved under `/workspace/gates/logs/phone-agent-2/pr-5303-phrase-coverage-round-2/source/H2-to-F2.diff`.
- Source refresh claim: `claims/pr-5303-phrase-coverage-round-2-refresh-phone-agent-2.md`. Round-1 F1-only claim supersession record: `claims/pr-5303-phrase-coverage-round-1-superseded-phone-agent-2.md`. Executor-owned receipt: `receipts/PR-5303-phrase-coverage-round-2.md`.
- Fixed seed: `5303C0DE` for all selected test and mutation legs. Record exact commands, exit codes, UTC intervals, fresh logs/XML, host/JDK/Gradle, and source/test/mutation blobs.

## Purpose and proof boundary

H2's production changes are preserved without edits: `getBoostQueries()` filters empty boost queries; phrase-query construction returns for empty clause lists and refuses to add empty phrase queries. H2's changelog remains unchanged. H2's expanded `testMatchAllDocs` is retained and continues to check `pf`, `pf2`, `pf3`, and an all-stopword `bq` for `q=*:*` and `q=*` in both `sow` modes. The H2 single-token `wireless` assertion is retained. Its `wi-fi` assertion is moved into its own named test so the preservation mutation can distinguish it; no H2 assertion is removed.

F2 adds only the remaining gate-required tests: the plain-token empty-phrase assertion in both modes, fielded-only `subject:wireless` in both modes, analyzed `wi-fi` phrase preservation in both modes, and configured `grackle` synonym phrase preservation in both modes. `qf=id` isolates the grackle subject phrase to `pf`. The five named coverage methods are `testNoEmptyPhraseBoostClause`, `testNoEmptyPhraseBoostClauseForFieldedQuery`, `testMatchAllDocs`, `testAnalyzedSingleTermKeepsPhraseBoost`, and `testSingleTermSynonymKeepsPhraseBoost`. The four F2 methods loop `sow=true` before `sow=false`; H2's `testMatchAllDocs` does too. There is no duplicate standalone MatchAll test.

This is test-only coverage for the PR author's existing fix, not a new production fix. It establishes no parser allocation/performance or benchmark claim. The all-stopword `bq` branch is exercised within H2's combined MatchAll parameterization; M5 below isolates that production guard by mutation. The early zero-clause check is not separately proven as a standalone optimization.

## Preflight and execution controls

Detached exact-F2 root `tidy` preflight completed before this gate claim: `2026-11-05T23:28:48Z`–`23:30:00Z`, exit 0, `tidy clean at F2`, Gradle `BUILD SUCCESSFUL`, 62 actionable tasks. Log root: `/workspace/gates/logs/phone-agent-2/pr-5303-phrase-coverage-round-2/preflight/`. The inode watchdog's lowest sample was 572,625/1,868,096 free (30.65%).

Use the `phone-agent-2` serialized Gradle runner and `/workspace/tools/.gradle-phone-agent-2.lock` for every Gradle step. Unset `NODE_ENV`. Use `/workspace/tools/inode-watch-run.py` to sample root inodes every 3 seconds; hard stop at or before 15% free inodes or below 2 GiB disk. Initial post-preflight headroom was 583,362/1,868,096 free (31.23%). Check live PR state/head/base and F2 before and after each proof leg; if any pin moves, stop immediately, discard results from that leg as stale, and request a superseding assignment. Do not clean files to recover inode headroom.

## Required acceptance legs

1. At F2, verify exact one-file H2..F2 diff, clean tracked tree, unchanged production and changelog blobs, and `git diff --check`. Run root `./gradlew tidy` and require a clean tree. Then run `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
2. At clean H2 and then clean F2, run separately with seed `5303C0DE` and `--rerun-tasks`:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser --tests org.apache.solr.search.QueryEqualityTest --tests org.apache.solr.DisMaxRequestHandlerTest --tests org.apache.solr.search.TestMultiWordSynonyms.testEdismaxQueryParsing_multiTermWithPf_shouldParseCorrectPhraseQueries -Ptests.seed=5303C0DE --rerun-tasks`

   Require zero failures, errors, and skips. Expected fresh `TestExtendedDismaxParser` counts are 40 at H2 and 43 at F2 (three added independently executable methods); verify and report the actual XML counts. Confirm the named MultiWordSynonyms method exists at H2. Do not substitute the full MultiWordSynonyms class: its unrelated `testPf` is the known flake addressed by PR 5294, which this gate must not incorporate.
3. Run M1–M5 below from clean F2 in disposable mutations. For each, keep the entire F2 acceptance test file byte-identical; only the listed production mutation may differ. Preserve mutation diff, F2 test and parser blob hashes, exact command/exit, fresh XML/logs, and intended assertion output. Restore and verify clean F2 after each leg.
4. At exact F2, run root `./gradlew check -x test` in a standalone clone with a real `.git` directory (not a worktree), via the phone-agent-2 lock/watchdog and `env -u NODE_ENV`. Require success, exact HEAD, and clean tracked tree. Record complete output.
5. Final readback: verify PR #5303 remains OPEN at H2/base B2; fork F2 remains exact; F1 remains unchanged; no mutation remains; and there was no PR/comment/review/Jira change. Do not open a PR from the candidate branch.

## Mutation legs and expected outcomes

Use `:solr:core:cleanTest :solr:core:test`, seed `5303C0DE`, `--rerun-tasks`, and explicit full class/method selectors. Missing APIs, compilation errors, startup failures, errors, or skips are not runtime proof.

### M1 — restore pre-PR parser from current base B2

Start from F2 and replace only `ExtendedDismaxQParser.java` with the B2 version. B2 and B1 have the same parser blob `809c0305be39716442c6052ea3cd02dc8cdeefe7`; do not replace the whole tree. Run all five named coverage methods. Expect intended assertion failures in plain-token, fielded-only, and MatchAll coverage; the two phrase-preservation controls pass both modes. The combined MatchAll failure may expose the empty-`bq` or empty-phrase path; M5 isolates `bq` specifically.

### M2 — reintroduce the empty-phrase path only for `sow=false`

Start from clean F2. In `addShingledPhraseQueries`, change `clauses.isEmpty()` to `(clauses.isEmpty() && config.splitOnWhitespace)` and change the final `if (!isEmpty(phrase))` to `if (!isEmpty(phrase) || (!config.splitOnWhitespace && phrase != null))`. Run all five methods. Expect the three empty-phrase/MatchAll methods to fail at the false-mode assertion after their true-mode checks pass; both phrase-preservation methods pass both modes. No unrelated parser behavior may change.

### M3 — over-aggressive one-raw-clause shortcut

Start from clean F2. At the start of `addPhraseFieldQueries(BooleanQuery.Builder, List<Clause>, ExtendedDismaxConfiguration)`, insert only `if (clauses.size() == 1) return;`. Run the two preservation methods. Expect two assertion failures in true mode: `wi-fi` loses its `pf` boost and `grackle` loses the subject phrase/boost. No errors or skips.

### M4 — same shortcut restricted to `sow=false`

Start from clean F2. Insert only `if (!config.splitOnWhitespace && clauses.size() == 1) return;` at the same location. Run the two preservation methods. Require their true-mode assertions to pass, followed by two intended false-mode failures. No errors or skips.

### M5 — empty-`bq` guard only for `sow=false`

Start from clean F2. In `getBoostQueries()`, change only `if (!isEmpty(q))` to `if (!isEmpty(q) || !config.splitOnWhitespace)`. Run only `testMatchAllDocs`. This reintroduces adding the all-stopword empty `bq` in false mode while preserving the true-mode guard. Expect the assertion to fail at the false-mode MatchAll query after the true-mode `q=*:*` and `q=*` checks pass. An exception, compile failure, skip, or unrelated failure does not count.

## Failure/repair and completion

Do not silently retry an executed failure or weaken assertions. Diagnose and record baseline, head, mutation, or formatting mismatches before taking another action. A scoped tidy repair requires an ordinary fork commit, separate remote verification, an add-only superseding exact-head gate and fresh proof; no prior results certify a changed head. Follow WORKFLOW's environment-repair rules. The receipt must state exact H2/B2/F2, current PR state, source/test blobs, host/JDK/runner, UTC intervals, full commands/exits/seeds, fresh suite/mutation XML, root tidy/Error Prone/root-check results, all five mutations with the actual intended failures and modes, remaining pins and final ref readback. Until all required legs succeed, report **Implemented, Proof Pending**. No force push, contributor/upstream push, PR edit/opening, public comment/review, or Jira action is authorized.
