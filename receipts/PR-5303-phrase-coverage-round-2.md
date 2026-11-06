# Receipt: PR #5303 phrase coverage, round 2 (H2/F2)

**Verdict: GREEN at the exact pinned heads.** Gate run by `phone-agent-2` on Linux/JDK 21 with the phone-agent-2 Gradle lock and inode watchdog. Claim: `claims/pool-phone-agent-2-gate-pr-5303-phrase-coverage-round-2.md`; gate plan: `gates/pr-5303-phrase-coverage-round-2.md`. This receipt supersedes no source refs; it records the new H2-based run and does not reuse round-1 proof.

## Exact inputs and reviewed change

At final readback (2026-11-05T23:59:43Z), Apache PR #5303 was OPEN at head H2 `fd106e02428bf1d619128029c73ea053fb4aedcb`, base B2 `e1799d8a71726bd8ec2ad69f1e3a4b5c4f3e019f`. H2's parent is B1 `1fc38d1e40cb12512aba2b1990b22b04111dad2d`; B2 is a sibling-line base update above B1. Candidate fork branch `nick-boss-tech/solr:pr-5303-phrase-coverage-round-2` remained F2 `07328d6973c4bb3385ca6d50bfe871f8808c1550`, one ordinary commit directly above H2. Prior F1 `256612b7f85dc831f9332d51182258d9594fcc1f` was unchanged. PR state, H2, B2, F1, and F2 were checked before and after each proof leg; none moved.

The exact H2..F2 diff was self-reviewed again before closeout and compared byte-for-byte with the saved patch. It contains one file only: `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, 62 additions and 21 removals; `git diff --check` is clean. H2's production parser blob remains `57cd3d3689c6efaefa772ff27c65255f0e81b514`. The H2 changelog `changelog/unreleased/SOLR-18625-empty-phrase-boost-matchalldocs.yml` remains at blob `6969ec33db428f755e91fa7ca42c5c5d87be9718`. H2's `testMatchAllDocs` expansion for `pf`, `pf2`, `pf3`, and all-stopword `bq` in both `sow` modes is unchanged. The single-token `wireless` assertion is preserved; the `wi-fi` assertion is moved into a separately named preservation test, not removed. F2 adds the fielded-only empty-phrase case, the two analyzer-preservation cases, and both-mode coverage, with `qf=id` isolating the configured `grackle` synonym phrase to `pf`.

## Acceptance results

The detached pre-claim F2 `tidy` preflight passed at `2026-11-05T23:28:48Z`–`23:30:00Z` (62 tasks). All subsequent Gradle runs used the repository's Gradle 9.7.0 wrapper, host JDK OpenJDK 21.0.12.1, the serialized phone-agent-2 runner, and `env -u NODE_ENV`.

The exact acceptance commands were `./gradlew tidy`; `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`; and, separately at H2 and F2, `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser --tests org.apache.solr.search.QueryEqualityTest --tests org.apache.solr.DisMaxRequestHandlerTest --tests org.apache.solr.search.TestMultiWordSynonyms.testEdismaxQueryParsing_multiTermWithPf_shouldParseCorrectPhraseQueries -Ptests.seed=5303C0DE --rerun-tasks`. The standalone clone ran `./gradlew check -x test`.

M1 and M2 each used: `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser.testNoEmptyPhraseBoostClause --tests org.apache.solr.search.TestExtendedDismaxParser.testNoEmptyPhraseBoostClauseForFieldedQuery --tests org.apache.solr.search.TestExtendedDismaxParser.testMatchAllDocs --tests org.apache.solr.search.TestExtendedDismaxParser.testAnalyzedSingleTermKeepsPhraseBoost --tests org.apache.solr.search.TestExtendedDismaxParser.testSingleTermSynonymKeepsPhraseBoost -Ptests.seed=5303C0DE --rerun-tasks`.

M3 and M4 each used: `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser.testAnalyzedSingleTermKeepsPhraseBoost --tests org.apache.solr.search.TestExtendedDismaxParser.testSingleTermSynonymKeepsPhraseBoost -Ptests.seed=5303C0DE --rerun-tasks`.

M5 used: `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser.testMatchAllDocs -Ptests.seed=5303C0DE --rerun-tasks`. The M1–M5 diffs and manifests preserve the exact changed parser blob per mutation.

| Proof leg | UTC interval | Result |
|---|---|---|
| F2 root `./gradlew tidy` | 23:36:12–23:37:21 | Exit 0; `BUILD SUCCESSFUL`, 62 tasks; tracked tree stayed clean. |
| F2 `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | 23:37:34–23:41:16 | Exit 0; 18 tasks (14 executed, 4 up-to-date). |
| Selected acceptance suite at H2, seed `5303C0DE`, `--rerun-tasks` | 23:41:32–23:43:02 | Exit 0; 157 tests, 0 failures/errors/skips. Class counts: ExtendedDismaxParser 40, QueryEqualityTest 113, DisMaxRequestHandlerTest 3, selected MultiWordSynonyms method 1. |
| Same selected suite at F2, seed `5303C0DE`, `--rerun-tasks` | 23:43:16–23:44:28 | Exit 0; 160 tests, 0 failures/errors/skips. ExtendedDismaxParser ran 43 tests and all five required methods are present in fresh XML. |
| Standalone-clone root `./gradlew check -x test` at exact F2 | 23:52:25–23:57:50 | Exit 0; `BUILD SUCCESSFUL`, 556 tasks; real `.git` directory, exact HEAD F2, clean tracked tree. |

The selected-suite logs emitted repeated non-fatal `URLPermission`/CLDR locale-provider messages. Both Gradle runs completed successfully and fresh JUnit XML shows zero errors and skips.

## Mutation proof

Each mutation began from clean F2, changed only `ExtendedDismaxQParser.java`, kept the F2 test blob byte-identical (`a96884318e3a6da411b81f807c1d8f3685a7750d`), ran with seed `5303C0DE` and `--rerun-tasks`, and was restored and verified clean afterward. The expected Gradle exit is 1 for these negative controls because their intended JUnit assertions fail; no mutation had compilation errors, test errors, or skips.

| Mutation and UTC interval | Fresh tests | Expected assertion failures (all other selected tests pass) |
|---|---:|---|
| M1, restore only B2's pre-PR parser file (`809c0305be39716442c6052ea3cd02dc8cdeefe7`), 23:44:53–23:46:02 | 5 | 3: plain-token empty phrase, fielded-only empty phrase, MatchAll. Both phrase-preservation controls pass. |
| M2, re-enable the empty phrase path only for `sow=false`, 23:46:22–23:47:34 | 5 | 3: the same empty-phrase/MatchAll methods; the regression appears only in false mode. Both preservation controls pass. |
| M3, add an unconditional `clauses.size() == 1` shortcut, 23:47:51–23:49:03 | 2 | 2: `wi-fi` and `grackle` phrase-preservation assertions fail in true mode. |
| M4, restrict that shortcut to `sow=false`, 23:49:19–23:50:34 | 2 | 2: both true-mode checks pass, then the `wi-fi` and `grackle` assertions fail in false mode. |
| M5, re-enable empty `bq` only for `sow=false`, 23:50:49–23:51:58 | 1 | 1: `testMatchAllDocs` fails on the false-mode all-stopword `bq` case; true-mode checks pass first. |

## Resources, final state, and limits

The inode watchdog sampled every three seconds and did not trigger. Lowest free inode sample was 506,636/1,868,096 (27.12%) during the standalone root check, above the 15% stop floor; free disk remained above 40 GB (well above the 2 GiB guard). The phone-agent-2 host lock is released. F2, the H2 baseline worktree, and the standalone root-check clone were clean at completion. No PR body, review, comment, or Jira action was made; no PR-author or Apache upstream ref was changed. The only source ref published was the authorized Nick fork candidate branch at F2.

Evidence root: `/workspace/gates/logs/phone-agent-2/pr-5303-phrase-coverage-round-2/`. It contains the H2-to-F2 patch and rechecked copy, run logs and fresh XML for both acceptance suites, individual M1–M5 patches/manifests/logs/XML, the full standalone root-check log, watchdog samples, and runner hashes. The published candidate remains a test-only branch at F2; no PR was opened and F2 was not rebased onto B2.

Coverage limits: this is a targeted selected-suite and root-check proof, not a full Solr test-suite run, benchmark, or performance/allocation claim. H2 was tested as the live PR head and F2 as its direct child; the entire suite was not run against B2 because H2 and B2 are siblings. M1 restored only the verified identical B2/B1 pre-PR parser file, not the whole base tree. The all-stopword `bq` case is part of H2's combined MatchAll test; M5 isolates that guard by mutation.

## PR description draft (not posted)

Expanded `TestExtendedDismaxParser` coverage around the existing eDisMax empty-phrase/boost-query fix. The tests cover plain-token and fielded-only queries in both `sow` modes, preserve `pf` phrase boosts for analyzer-expanded `wi-fi` and the configured `grackle` synonym, and retain H2's MatchAll coverage for `pf`, `pf2`, `pf3`, and all-stopword `bq`. The selected suites passed at H2 (157 tests) and F2 (160 tests) with seed `5303C0DE`; Error Prone compilation and root `check -x test` passed. Five compiling mutation controls reproduced the intended regressions. This is targeted correctness coverage, not a performance or full-suite claim.
