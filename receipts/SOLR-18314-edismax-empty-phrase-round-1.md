# Receipt: SOLR-18314 eDisMax empty phrase clauses, rounds 1-2

Verdict: GATE FAILED at E2 `9b4bf9276515187b3d29a39a0923f644e1b457d2`. The production change does what it claims, and two of the three new tests discriminate exactly as designed; the third, the single-term preservation control, carries a usa expectation that holds at neither the candidate nor the parent head, in either sow mode, as shown by direct diagnostic below. The gate host applied no semantic repair; the options are listed for the implementation owner. Status stays Implemented, Proof Pending.

## Identity and the tidy repair

- Executor: phone agent (Linux VM), claimed at the owner's direction in chat; claim `claims/solr-18314-edismax-empty-phrase-round-1-phone-agent.md`. Implementation claim and report: `claims/solr-18314-edismax-empty-phrase-round-1-windows.md`, `reports/solr-18314-edismax-empty-phrase-round-1.md`. Superseding job: `gates/solr-18314-edismax-empty-phrase-round-2.md`.
- Fork branch `nick-boss-tech/solr:solr-18314-edismax-empty-phrase`. Offered head E `6a8eb075ad7e906a547225845f8ac13420179e29`; parent P `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` (draft PR 5294 head, unchanged). Diff E against P: `ExtendedDismaxQParser.java`, `TestExtendedDismaxParser.java`, `changelog/unreleased/SOLR-18314-edismax-empty-phrase.yml`, +65/-2.
- The detached tidy preflight at E was not clean: two formatting-only rewraps (the new guard's `return;` joined to its condition line; one `assertThat` wrapped). Repair commit E2 `9b4bf9276515187b3d29a39a0923f644e1b457d2` ("SOLR-18314: Tidy eDisMax empty phrase changes", Nick Shanin identity, no trailers), plain-pushed and read back through the fork refs API. All proof below is at E2; post-repair tidy is clean and `git diff --check` is clean.
- Host: this Linux VM, vendored JDK 21, runner `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0), serialized under the host build lock. Seed `18314E`. Logs and fresh JUnit XML on this host under `~/workspace/tools/gate-18314/`. Main run 2026-11-05T07:31:24Z to 07:47:13Z; diagnostic and check recovery 07:55:05Z to 08:04:53Z.

## Legs that passed

- Changelog: `logchangeGenerate` at E2, rc 0; the generated summary contains the SOLR-18314 entry crediting Ian Springer and Nick Shanin. (The cross-branch python validator crashes in this clone's branch enumeration before reading entries, as recorded in the PR 5291 receipt; the logchange parser is the repository check used here.)
- Error Prone compile `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`: rc 0.
- Suites at E2 (`--rerun-tasks`): `DisMaxRequestHandlerTest` 3 of 3 clean; `TestMultiWordSynonyms` 6 of 6 clean; `TestExtendedDismaxParser` 42 tests with exactly one failure, detailed below. No skips anywhere.
- Mutation leg: with phrase generation skipped whenever `clauses.size()==1` (one inserted guard line in `addPhraseFieldQueries`, diff retained on this host), `testAnalyzedSingleTermKeepsPhraseBoost` failed on its wi-fi boost-preservation assertion: expected a string containing `(subject:"wi fi")^10.0`, actual `+DisjunctionMaxQuery((subject:"wi fi"))`. The mutation requirement is satisfied; it was reverted.
- Root check: `check -x test` at E2 in a normal clone (layout disclosed per host rule), SHA fetched explicitly, `rev-parse HEAD` recorded as E2. First attempt failed only at `:solr:webapp:verifyUiWasm` because the egress relay corrupted the `solr-ui-wasm.zip` download (checksum mismatch, a known host artifact). The zip was fetched directly from `https://nightlies.apache.org/solr/ui/wasm/473a55ef64a9a1605e00eb6c1c386887d4cb7dac/solr-ui-wasm.zip`; its sha256 is `654494303e4af3b8bfb26a83d250603f19033820295591d88db0597991ce8ad2`, exactly the value pinned in `gradle.properties`, and with it seeded the rerun finished BUILD SUCCESSFUL in 5m 22s, rc 0, clone tree clean.

## The failure: the usa half of the preservation control

`testAnalyzedSingleTermKeepsPhraseBoost` asserts, for `q=usa`, `qf=subject`, `pf=subject^10`, in both sow modes, that the parsed query contains `(subject:"unit state ? america")^10.0`. Executed results:

- At E2 (full suite): AssertionError, actual parsed query `+DisjunctionMaxQuery((subject:usa))`. No pf clause at all.
- At P production (negative checkout, tests byte-identical, md5 `d98bfef0dada83dfde6e5e8068c6dc10` before and after the production swap): the same assertion fails with actual `+DisjunctionMaxQuery((subject:usa)) ()`. The pf clause is an empty BooleanQuery.
- A diagnostic method run in disposable checkouts at both heads printed the parsed query for both sow modes. At P: sow=true and sow=false both give `+DisjunctionMaxQuery((subject:usa)) ()`. At E2: both give `+DisjunctionMaxQuery((subject:usa))`. The expected phrase string appears in no mode at no head.

Reading: for this one-token query, the pf sub-parse produces an empty phrase result (a one-token shingle whose multiword synonym expansion yields no phrase query under this schema), at both heads. The branch's change works exactly as intended on it: the empty clause that P adds as `()` is omitted at E2. What does not exist, and what this branch does not create, is the asserted synonym phrase boost. The wi-fi half of the same test is a valid control: it passes at both heads (and discriminates under the mutation above), because wi-fi analyzes into a real two-token phrase.

## Negative leg tally

- `testEmptyPhraseBoostClauses` on P production: failed as required (parsed query contains the empty clause `()`).
- `testMatchAllDocsWithPhraseBoost` on P production: failed as required (top-level query remains a BooleanQuery, `<+*:* ()>`).
- `testAnalyzedSingleTermKeepsPhraseBoost` on P production: required to pass as the preservation control; it fails on the usa half (above), so the leg is not satisfied as specified. Its wi-fi half passes on both heads.

## Repair options for the implementation owner (none applied here)

1. Scope the control to the wi-fi case (drop or rework the usa half). The branch's claims are then fully proven by the remaining legs: two discriminating new tests, the wi-fi preservation control with its mutation, and the clean suites and root check at a superseding head.
2. Keep a usa case but assert the behavior this scope actually delivers: no empty pf clause for the one-token synonym query (which is the `testEmptyPhraseBoostClauses` territory), rather than a phrase boost that the pf parse never generates.
3. Treat one-token multiword-synonym phrase generation as its own production change and slice; it is outside the extracted PR 4652 cleanup this branch scoped itself to, and Smiley's support on PR 4652 was for the cleanup, not for new phrase generation.

Any of these changes the test file or the scope, so under the job's repair route it needs an ordinary branch commit, a superseding exact-head gate, and fresh proof at the new head. The two environment incidents during this gate (stale-daemon journal lock at the first diagnostic attempt; the relay-corrupted wasm zip) were repaired and rerun as recorded; neither is a branch result.

## Final state

Candidate and clone trees clean; negative and mutation changes reverted; final fork read-back `9b4bf9276515187b3d29a39a0923f644e1b457d2`. No PR, comment, Jira write, or upstream action was taken.
