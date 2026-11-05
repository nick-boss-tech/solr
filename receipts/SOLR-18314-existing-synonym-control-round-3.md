# Receipt: SOLR-18314 existing synonym preservation control, round 3

Verdict: GATE GREEN at E3.

- Job: `gates/solr-18314-existing-synonym-control-round-3.md`
- Claim: `claims/solr-18314-existing-synonym-control-round-3-phone-agent.md`
- Host: phone agent Linux VM, JDK 21 (vendored), Gradle 9.7.0 through solr-gradle.sh, one build at a time under the test-queue lock.
- Branch: `nick-boss-tech/solr:solr-18314-edismax-empty-phrase`
- E3: `1ea8ceb2d5253fef6c2e05a3ee800687dea700ba`
- E2: `9b4bf9276515187b3d29a39a0923f644e1b457d2`
- P: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` (PR 5294 head, unchanged)
- Seed: `18314E` throughout.
- Final remote read-back after all legs (2026-11-05 ~15:47 UTC): the fork branch is still at E3.

## Pins and blobs

- P..E3 touches exactly three files: `changelog/unreleased/SOLR-18314-edismax-empty-phrase.yml`, `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java`, `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`.
- Production blob: P `809c0305be39716442c6052ea3cd02dc8cdeefe7`; E2 and E3 `065177fb900c35928fef20be98935807651717b6` (the repair does not touch production).
- Changelog blob: `582446ee8d195030c13e1d0b7c317c5130f7c2cc` at E2 and E3 (unchanged).
- Test blob: E2 `cd1d04bb8e622111ec77df4035482052d0039f80`; E3 `ae27a0318ef029dc8d270eefe471c47db41be3e6`. The E2..E3 diff is the test file only, +11/-3.
- E3 test file md5 `68b4bafae7b8e733ee003b0f901aac52`; the negative leg used a byte-identical copy (same md5).

## Leg 1: changelog, tidy, Error Prone compile (2026-11-05 15:10 to 15:12 UTC)

- Changelog validation: `logchangeGenerate` rc 0; the generated `version-summary.md` carries the SOLR-18314 entry (the entry title with the SOLR-18314 link, authors Ian Springer and Nick Shanin). Substitution disclosed: `dev-tools/scripts/validateChangelogs.py` crashes in `discover_branches` on this host before reading any entry, so the repository logchange parser is the validator, as in rounds 1 and 2.
- Tidy: `:solr:core:tidy` rc 0, tree clean afterwards. `git diff --check` over P..E3 rc 0.
- Error Prone: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` rc 0.

## Leg 2: focused suites at E3 (15:12 to 15:15 UTC)

Command: `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser --tests org.apache.solr.DisMaxRequestHandlerTest --tests org.apache.solr.search.TestMultiWordSynonyms -Ptests.seed=18314E --rerun-tasks`, rc 0.

Fresh JUnit XML counts: TestExtendedDismaxParser 43, DisMaxRequestHandlerTest 3, TestMultiWordSynonyms 6; 52 total, 0 failures, 0 errors, 0 skipped. All four added methods executed and passed: testEmptyPhraseBoostClauses, testMatchAllDocsWithPhraseBoost, testAnalyzedSingleTermKeepsPhraseBoost, testSingleTermSynonymKeepsPhraseBoost.

## Leg 3: negative leg on original production (15:15 to 15:19 UTC)

Disposable worktree at E3 with only `ExtendedDismaxQParser.java` restored from P (production blob `809c0305be39`), tests byte-identical to E3. Four separate method runs at seed `18314E`:

- testEmptyPhraseBoostClauses: rc 1, exactly one failure, no errors or skips. The assertion rejects an empty clause in the parsed query; at P the query is `+DisjunctionMaxQuery((subject:wireless)) ()`.
- testMatchAllDocsWithPhraseBoost: rc 1, exactly one failure, no errors or skips. Expected a MatchAllDocsQuery; at P the query is the BooleanQuery `+*:* ()`.
- testAnalyzedSingleTermKeepsPhraseBoost: rc 0. The wi-fi preservation control passes on original production.
- testSingleTermSynonymKeepsPhraseBoost: rc 0. The grackle preservation control passes on original production.

Each method covers both sow modes internally, so both controls pass on original production in both modes, as the job requires.

## Leg 4: mutation leg (15:19 to 15:21 UTC)

Clean E3 plus the round-1 synthetic mutation: one inserted line at the top of the `addShingledPhraseQueries` overload that takes (BooleanQuery.Builder, List<Clause>, ExtendedDismaxConfiguration), `if (clauses.size() == 1) return;`, skipping all phrase generation for a single eligible raw clause. The mutation applied cleanly; tests unchanged. Two separate method runs at seed `18314E`:

- testAnalyzedSingleTermKeepsPhraseBoost: rc 1, exactly one failure, no errors or skips. Expected a string containing `(subject:"wi fi")^10.0`; the parsed query was `+DisjunctionMaxQuery((subject:"wi fi"))` with the boost absent.
- testSingleTermSynonymKeepsPhraseBoost: rc 1, exactly one failure, no errors or skips. Expected `subject:"crow blackbird"` with `^10.0`; the parsed query was `+DisjunctionMaxQuery((id:grackle))` with no subject phrase at all.

Both preservation controls discriminate independently under the mutation.

## Leg 5: root check in a normal clone

The clone is a normal checkout (`.git` is a directory). E3 was fetched explicitly inside the clone and `git rev-parse HEAD` recorded `1ea8ceb2d5253fef6c2e05a3ee800687dea700ba` before building.

- Attempt 1 (15:22 to 15:27 UTC) failed at `:solr:webapp:verifyUiWasm`: the relay delivered a corrupted solr-ui-wasm.zip (sha256 `ed8bbeb34c` against the pinned `654494303e`). The zip was seeded by direct download from nightlies.apache.org; the seeded file's sha256 equals the `gradle.properties` pin exactly.
- Attempt 2 (15:27 to 15:32 UTC) failed only at `:solr:solr-ref-guide:buildLocalAntoraSite`: Antora reported it could not download the UI bundle from nightlies.apache.org at that moment. A direct download of the same URL immediately afterwards returned HTTP 200 with a valid zip, so this is recorded as a transient download failure, not a branch result.
- Attempt 3 (15:41 to 15:45 UTC): `check -x test` BUILD SUCCESSFUL in 3m 1s, no failed tasks. The clone tree is clean afterwards.

## Execution notes

The run was executed by the phone agent directly at Nick's direction after the dispatched helper was refused. Two interruptions are part of the record: the first attempt's runner stopped at its own tidy check because the changelog leg's generated `version-summary.md` was still in the tree (the tidy diff itself was empty; the runner was fixed to remove the generated file before the check), and a runtime restart killed the second attempt after leg 1c. The leg 1 to 4 evidence above is from the third attempt's fresh logs and JUnit XML; leg 5 is the clone sequence described in its section.

## Conclusion

The round-1 failure is repaired. The usa expectation lacked its premise in this branch, because PR 4652's `usa => united states of america` synonym addition is not part of this extraction; the grackle control covers the same pf phrase-boost path against a mapping the shared synonyms file already contains, and it passes at E3 and on original production while failing under the one-clause mutation. Status: Implemented, Proof Complete at E3. No PR, public comment, or Jira write was made under this job.
