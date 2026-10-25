# Gate: SOLR-17786 / PR 5108 reverse cursor candidate, round 2 (supersedes round 1 two-shard)

Status: OPEN. Host: vm2. Tags: gate, settling-run.

## Immutable input

- Fork: `https://github.com/nick-boss-tech/solr.git`, branch `solr-17786-reverse-cursor-submit`.
- Head: `fde070281383f113a331a9ec5ccde173b66fac3c`, commit "SOLR-17786: Suppress forbidden-API check on intentional singletonList(null)" by Nick Shanin, no trailers. It changes only `solr/core/src/java/org/apache/solr/search/SortSpec.java`: an import and `@SuppressForbidden(reason = "singletonList(null) is intentional")` on `reverse()`.
- Parent: `2c8af559c8456ded97811455622fb06786ae74ba` (round 1 input, with the fixture correction `sliceCount = 2; fixShardCount(2);`).
- Public PR #5108 head: `32f80171030ca1108cd20292f50232d2729ed930`, unchanged. Upstream merge base `f3a798e424bb933ba988c8d316d0e8f35c8b6e0f`.
- Supersedes: `gates/pr-5108-reverse-cursor-round-1-two-shard.md`. Round 1 failed its final check; see `claims/solr-17786-gate-round-1-two-shard-vm2-verdict-check-failed.md`.
- Repair decision: `claims/solr-17786-forbidden-api-repair-vm2.md` (ladder rung 4; mirrors `RuleBasedAuthorizationPluginBase.java:362`). The repair changes no behavior and no tests.
- Scope: Nick's round-1 authorization covered the fixture correction and tidy repair. This head adds only the suppression above. No other source or test changes. No PR, comment, or Jira action is authorized.

## Legs (every leg on this head, fresh XML per invocation)

1. Detached clean tidy preflight at the head (must be clean).
2. Changelog YAML parse, `changelog/unreleased/SOLR-17786-reverse-cursor.yml`.
3. `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
4. `./gradlew :solr:core:compileTestJava :solr:solrj:compileTestJava`.
5. Positive, seed `5108C0DE`: core `SortSpecReverseTest`, `CursorMarkTest`, `CursorPagingTest`, `DistribCursorPagingTest`; SolrJ `QueryResponseTest`.
6. Positive, seed `17786C0DE`: core `SortSpecReverseTest`, `CursorMarkTest`, `CursorPagingTest.testReverseCursorPaging`, `DistribCursorPagingTest.testReverseCursorPaging`.
7. Discriminating mutations on independent disposable checkouts, seed `5108C0DE`:
   - M1: `QueryResult.setCursorMarkNoResults` zero-row branch restored to one-sided markers; `CursorMarkTest.testZeroRowCursorMarks`.
   - M2: remove the local normalization block in `QueryComponent.doProcessUngroupedSearch`; `CursorPagingTest.testReverseCursorPaging`.
   - M3: remove the response-position reversal in `QueryComponent.setResultIdsAndResponseDocs`; `DistribCursorPagingTest.testReverseCursorPaging`.
   A mutation that passes is reported as an unproven pin, not fail-before proof.
8. Final check on a pristine checkout: `./gradlew :solr:core:check :solr:solrj:check :solr:solr-ref-guide:check -x test`.

## Receipt rule

A green run writes `receipts/SOLR-17786.md` with the exact head, every command, JUnit totals, mutation outcomes, log paths, and a PR-description draft per `pr-formula.md`. A non-green run writes a failure verdict file instead, because a receipt marks completion.
