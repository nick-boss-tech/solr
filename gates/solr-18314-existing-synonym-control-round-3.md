# Gate: SOLR-18314 existing synonym preservation control, round 3

Status: OPEN

- Tags: gate, implementation, review, records. Intended host: capable Linux CPU gate host with JDK 21, host runner/resource guards and one build under the lock.
- Fork branch: `nick-boss-tech/solr:solr-18314-edismax-empty-phrase`.
- Exact offered repair head E3: `1ea8ceb2d5253fef6c2e05a3ee800687dea700ba`.
- Parent E2: `9b4bf9276515187b3d29a39a0923f644e1b457d2`; original shared base P: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` (PR 5294, unchanged).
- Supersedes `gates/solr-18314-edismax-empty-phrase-round-2.md` for execution after its failed control assertion. Preserve `receipts/SOLR-18314-edismax-empty-phrase-round-1.md` as the historical failed result; do not overwrite it.
- Claim: `claims/solr-18314-existing-synonym-control-round-3-windows.md`.
- Report: `reports/solr-18624-adjacent-receipts-review-round-1.md`.
- New receipt: `receipts/SOLR-18314-existing-synonym-control-round-3.md`.
- Seed: `18314E` throughout. Windows ran source/diff checks only, no Gradle, compilation, formatter or tests. New/revised assertions remain pins until executed and discriminated.

## Diagnosed failure and bounded repair

The earlier copied usa expectation lacked its premise. PR 4652 explicitly adds `usa => united states of america` to the shared synonyms file, but the extracted candidate did not include that configuration change. At P and E2, usa therefore remains one term; the diagnostic does not establish a production failure to expand a configured multiword synonym. Rung 1 of the decision ladder settles this repair by comparing the fixture and original PR diff.

E3 changes only TestExtendedDismaxParser.java (+11/-3 against E2). It keeps the valid wi-fi case and replaces the nonexistent usa rule with the existing `crow blackbird, grackle` mapping. A separate test method exercises grackle with qf=id and pf=subject^10: the unanalyzed main-query field cannot generate the subject phrase, so the subject phrase plus boost assertions cover the pf path. Both sow modes remain covered. No production, schema, synonym configuration, changelog or relevance-policy change is included; E2's tidy repair is preserved.

## Preflight and required legs

Refresh live job/ref/claims and require E3 with E2 ancestry. Use a clean full Linux checkout and standard detached tidy preflight before claiming. Preserve fresh logs/XML and source/mutation diffs separately for each leg. Observe WORKFLOW repair rules and host disk/inode guard. A previous gate's green sub-leg is historical evidence, not acceptance for E3.

1. Confirm unchanged production/changelog blobs against E2 and exact test-only E2-to-E3 diff. Validate changelog using the repository procedure, run `./gradlew tidy`, `git diff --check`, require a clean tree, then run `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
2. At E3 run `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser --tests org.apache.solr.DisMaxRequestHandlerTest --tests org.apache.solr.search.TestMultiWordSynonyms -Ptests.seed=18314E --rerun-tasks`. Expected method counts are 43, 3 and 6 respectively, 52 total; report actual fresh XML counts with zero failures/errors/skips and confirm all four added methods execute.
3. Original-production negative leg: disposable E3 checkout with only ExtendedDismaxQParser.java restored from P and all acceptance tests byte-identical to E3. Run the four methods with full TestExtendedDismaxParser selectors: testEmptyPhraseBoostClauses, testMatchAllDocsWithPhraseBoost, testAnalyzedSingleTermKeepsPhraseBoost, testSingleTermSynonymKeepsPhraseBoost. Expect exactly the first two to fail at their intended empty-clause/MatchAllDocsQuery assertions. Both preservation controls must pass, in both sow modes. Retain diff, test blob hashes, commands/exits, fresh logs/XML and exact assertions. Compilation/setup errors or skipped tests are not negative proof.
4. Independent discrimination of both preservation controls: disposable clean E3, unchanged tests, compiling synthetic mutation that skips all phrase generation when the eligible raw clauses.size()==1 (the same over-aggressive mutation used in the earlier receipt). Run both testAnalyzedSingleTermKeepsPhraseBoost and testSingleTermSynonymKeepsPhraseBoost as separate selected methods. Require two intended runtime assertion failures, no errors/skips; record which assertion fails in each. This is a synthetic coverage mutation, not an additional real production bug. Preserve diff/log/XML and restore clean E3.
5. On restored clean E3 run root `./gradlew check -x test`, then verify clean source and independently read back the fork ref. Explain material execution conditions. The previous host's wasm ZIP download failed checksum through its relay; if relevant, verify any cache repair against the exact repository pin and record the condition instead of counting the failed download as a passing check.

No silent retry of an executed failure or weakening a control to make it pass. Diagnose any mismatch before another run; a source/formatting repair requires an ordinary fork commit, separate ref verification and an add-only superseding exact-head job before fresh acceptance proof. Preserve tests unchanged for original-production and mutation legs. An independently unchanged assertion may be labelled a coverage pin; never label the full test proven if the discriminating run did not establish it.

Receipt must state E3/E2/P, exact paths/blobs, host/JDK/runner/UTC intervals, all commands/exits/seeds, fresh XML counts, tidy/YAML/Error Prone/root-check results, both control passes on original production, both intended mutation failures and final remote read-back. Status remains **Implemented, Proof Pending** until all required proof succeeds. No PR/body/comment/Jira write, contributor/upstream push or force push is authorized here.
