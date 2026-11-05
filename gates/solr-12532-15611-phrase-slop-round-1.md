# Gate: SOLR-12532 / SOLR-15611 phrase slop, round 1

Status: OPEN

- Tags: gate, implementation, review, records. Intended host: capable Linux CPU gate host, JDK 21, host runner and resource guard, one build under the host lock.
- Fork branch: `nick-boss-tech/solr:solr-12532-15611-phrase-slop`.
- Exact offered head S: `44849d2c4ca0cae283c26bc45048f824ea0d53c4`.
- Parent P: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`, the unchanged draft PR 5294 head. This is a separate follow-up branch, not a PR 5294 update.
- Claim: `claims/solr-12532-15611-phrase-slop-round-1-windows.md`.
- Report: `reports/solr-12532-15611-phrase-slop-round-1.md`.
- Receipt: `receipts/SOLR-12532-15611-phrase-slop-round-1.md`, owned by executor.
- Seed: `1253215611` for all acceptance/negative/mutation legs; `B3B4534A1A4D09F4` for the existing testPf compatibility leg.
- Windows performed source review and diff --check only. No compilation, formatter or tests ran. New assertions are coverage pins pending discrimination.

## Scope and preflight

Use a clean full Linux checkout, not the sparse Windows worktree. Refresh live job, fork ref, claims and receipts. Require the fork ref to equal S and P to be its parent. Run the standard detached tidy preflight before claiming. Record host/JDK/runner/UTC times and preserve fresh log/XML artifacts per leg. Follow START-HERE/WORKFLOW and the host disk/inode guard; no concurrent build.

Exact outbound files are SolrQueryParserBase.java, ExtendedDismaxQParser.java, TestMultiWordSynonyms.java and changelog/unreleased/SOLR-12532-15611-explicit-phrase-slop.yml. The production fix supplies explicit slop during graph construction and restores the parser default in finally. The base parser resets its sub-parser guard for each quoted clause. Existing top-level PhraseQuery/MultiPhraseQuery handling stays available for custom field implementations. No recursive rewrite of nested parser queries is intended.

## Required proof

1. Validate changelog YAML using the standard board procedure. Run `./gradlew tidy`, `git diff --check`, and require a clean tree. Run `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
2. At S run `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestMultiWordSynonyms --tests org.apache.solr.search.TestExtendedDismaxParser --tests org.apache.solr.search.TestSolrQueryParser --tests org.apache.solr.parser.SolrQueryParserBaseTest -Ptests.seed=1253215611 --rerun-tasks`. Require all selected tests and all five added methods to execute with zero failures/errors/skips, and record actual fresh XML counts. This checks graph paths, explicit slop 0 overriding qs=2, default restoration, a normal non-graph phrase after _query_, and nested-parser ownership.
3. Compatibility leg at S: run `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestMultiWordSynonyms.testPf -Ptests.seed=B3B4534A1A4D09F4 --rerun-tasks`. Require 1 executed test and zero failures/errors/skips. This is compatibility coverage, not a reproduction of the historical flake.
4. Fail-before leg in a separate disposable S checkout: retain the new test/changelog blobs byte-identical, restore only both production files from P. Run the five added methods with seed `1253215611`: testExplicitSlopWithMultiWordSynonymsLucene, testExplicitSlopWithMultiWordSynonymsEdismax, testExplicitSlopOverridesQsWithMultiWordSynonyms, testPhraseSlopAfterSubQuery, testNestedParserKeepsItsOwnPhraseSlop. Expect the first four methods to fail at the intended runtime assertions; the nested-parser ownership control must pass. In testPhraseSlopAfterSubQuery, the ordinary phrase without the preceding sub-parser must pass before the second assertion fails. Preserve the exact source diff, commands, fresh logs/XML and failing assertions. Compilation, startup errors or skipped tests are not negative proof. A mismatch needs investigation, not repeated runs or weaker expectations.
5. Two isolated compiling mutations from clean S, with tests byte-identical: omit only the phraseSlop restoration in the base parser, then reset and omit only the restoration in ExtendedSolrQueryParser. Run the corresponding Lucene/eDisMax explicit-slop method. Require the compound-query assertion about the following default phrase to fail. Preserve mutation diffs and fresh logs/XML. These discriminate default-restoration coverage; they are synthetic mutations, not two additional reported bugs. Reset S after each mutation.
6. Restore clean S and run `./gradlew check -x test`. Report material execution conditions, including NODE_ENV if relevant. Require successful tidy, Error Prone and root check at the exact accepted head. Separately read back the fork ref and confirm clean source.

Do not claim the guard-reset line is independently mutation-proven: construction-time slop also makes the normal phrase correct. Report each boundary assertion's actual discrimination, and label any remaining assertion as a pin.

## Repairs and completion

No silent retry of an executed failure. Record and diagnose assertion, environment or formatting failures separately. A scoped repair needs an ordinary fork commit, separate remote read-back, an add-only superseding exact-head gate and fresh required proof. Never issue a green receipt for modified source under S. Preserve unchanged acceptance tests during negative/mutation proof. Follow WORKFLOW for a documented pre-execution environment repair.

Write the receipt with ancestry, outbound paths, actual commands/exits/seeds, UTC times, fresh logs/XML counts, YAML/tidy/Error Prone/root-check results, exact negative and mutation results and final remote read-back. Status stays **Implemented, Proof Pending** until all required proof passes. No PR opening, live PR body edit, public comment, Jira post, upstream push or force push is authorized by this job.
