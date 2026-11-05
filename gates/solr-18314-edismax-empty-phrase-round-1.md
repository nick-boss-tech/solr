# Gate: SOLR-18314 eDisMax empty phrase clauses, round 1

Status: OPEN

- Tags: gate, implementation, review, records. Intended host: capable Linux CPU gate host with JDK 21 and standard host runner/resource guard; one build under the lock.
- Fork branch: `nick-boss-tech/solr:solr-18314-edismax-empty-phrase`.
- Exact offered head E: `6a8eb075ad7e906a547225845f8ac13420179e29`.
- Parent P: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`, unchanged draft PR 5294 head.
- Claim: `claims/solr-18314-edismax-empty-phrase-round-1-windows.md`.
- Report: `reports/solr-18314-edismax-empty-phrase-round-1.md`.
- Receipt: `receipts/SOLR-18314-edismax-empty-phrase-round-1.md`.
- Seed: `18314E`.
- Windows reviewed source and diff --check only; no Gradle, compilation, formatting or tests. Assertions are pins until executed and discriminated.

## Reuse and scope

This extracts Ian Springer's eDisMax production cleanup from PR 4652 at `c122384ba230eecd0a6f2ba13a15c02908c9f0fc`: return early for zero eligible raw clauses, and omit an empty parsed phrase query. Changelog credits Ian Springer and Nick Shanin. Tests adapt the existing PR's analyzer examples and add fielded-query and sow=true/false checks. Classic DisMax production code and its single-term scoring stay unchanged. The disputed ranking change from PR 4652 is excluded. This fork branch is an evidence/implementation candidate for the existing work, not authorization for a competing public PR.

Use a clean full Linux checkout, not the sparse Windows worktree. Refresh job/ref/claims, require E with parent P, and run standard detached tidy preflight before claiming. Preserve fresh artifacts separately per leg; observe host resource guards.

## Required proof

1. Validate YAML using the standard board procedure. Run `./gradlew tidy`, `git diff --check`, require a clean tree, then `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
2. At E run `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser --tests org.apache.solr.DisMaxRequestHandlerTest --tests org.apache.solr.search.TestMultiWordSynonyms -Ptests.seed=18314E --rerun-tasks`. Require zero failures/errors/skips and actual fresh XML counts, including all three new methods: testEmptyPhraseBoostClauses, testMatchAllDocsWithPhraseBoost, testAnalyzedSingleTermKeepsPhraseBoost.
3. In a disposable E checkout, keep the added tests byte-identical and restore only ExtendedDismaxQParser.java from P. Run the three new methods at seed `18314E`. Expect testEmptyPhraseBoostClauses to fail on the empty clause and testMatchAllDocsWithPhraseBoost to fail because the top-level query remains BooleanQuery. testAnalyzedSingleTermKeepsPhraseBoost is a preservation control and must pass on both P production and E. Retain exact diff, commands, fresh logs/XML and assertions. Missing APIs, compilation errors, setup failures or skips are not negative proof.
4. Isolated compiling synthetic mutation at E: skip all phrase generation whenever clauses.size()==1, leaving the parser executable and tests unchanged. Run testAnalyzedSingleTermKeepsPhraseBoost; require an intended boost-preservation assertion to fail for wi-fi or usa. This checks that removing empty clauses cannot be replaced by rejecting every one-token input. Label separately which assertions are discriminated versus pins. Preserve diff/log/XML, then restore E.
5. At restored clean E run `./gradlew check -x test`, confirm clean tree and separately read back the fork ref. Report material execution conditions and all exact-head results.

The early zero-clause guard is a bounded avoidance of unnecessary parsing; do not require its deletion alone to fail when the later empty-query filter still protects correctness. No benchmark or throughput gain is claimed. Existing DisMax tests cover compatibility; do not alter them to accept lost single-term boosts.

## Repairs and completion

Record and diagnose executed failures before another run. Scoped repairs require ordinary fork commits, separate remote verification, a superseding exact-head gate and fresh proof. No green receipt for a dirty or changed head. Follow WORKFLOW for documented environment repairs and preserve test bytes in negative controls.

Receipt must name E/P, host/JDK/runner/UTC times, all commands/exits/seeds, fresh logs/XML counts, YAML/tidy/Error Prone/root-check results, negative/mutation diffs and outcomes and final remote read-back. Status remains **Implemented, Proof Pending**. No PR opening/edit/comment, Jira write, force push, contributor-branch or upstream write is authorized here.
