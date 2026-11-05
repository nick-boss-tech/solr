# Receipt: PR 5291 / SOLR-18621 parameter factory fixes, rounds 1-2

Verdict: GATE GREEN at H2 `199911b298fac80f4cea5e3de553675b64f6a6a9`. Every leg of `gates/pr-5291-params-factory-fixes-round-1.md` passed at the repaired head named by the superseding `gates/pr-5291-params-factory-fixes-round-2.md`, with one disclosed substitution on the changelog leg (below). Nothing was posted to the upstream PR or Jira.

## Identity and the tidy repair

- Executor: phone agent (Linux VM), claimed at the owner's direction in chat; claim `claims/pr-5291-params-factory-fixes-round-1-phone-agent.md`. Implementation claim and report: `claims/pr-5291-params-factory-fixes-round-1-windows.md`, `reports/pr-5291-params-factory-fixes-round-1.md`.
- Contributor H0 `600e0ec4afd82004a94852c419892897be14e6f0` (live PR 5291 head, never moved); implementation H1 `f2952c41c7c023aa6e79868df9efe8c65b02c993`; base `82abb86bb5b7326fe48c117e4de69981a43e4251`. Fork branch `nick-boss-tech/solr:solr-18621-params-factory-fixes`.
- The detached tidy preflight at H1 was not clean: tidy wrapped one `expectThrows` line in `SolrParamTest.java` (a single call exceeding the column limit; formatting only, shown in full in the superseding gate). Under the job's repair route, the repair was committed as H2 `199911b298fac80f4cea5e3de553675b64f6a6a9` ("SOLR-18621: Tidy SolrParamTest formatting", Nick Shanin identity, no trailers), plain-pushed, and read back through the fork refs API. Post-gate tidy at H2 is clean and `git diff --check` is clean. All proof below is at H2. The first preflight tidy attempt also hit this host's stale-daemon Gradle journal lock once (daemons left by the preceding gate); killed, cleared, rerun. No test had executed.
- Host: this Linux VM, vendored JDK 21, runner `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0), serialized under the host build lock. Logs and fresh JUnit XML on this host under `~/workspace/tools/gate-5291/`. Seed `5291C0DE`. Main run window 2026-11-05T06:35:09Z to 06:56:42Z.

## Positive proof at H2

- Error Prone compilation of the changed source/test sets and helper consumers (`:solr:solrj:compileJava :solr:solrj:compileTestJava :solr:test-framework:compileJava :solr:core:compileJava :solr:core:compileTestJava :solr:benchmark:compileJava -Pvalidation.errorprone=true`): rc 0. `ModifiableSolrParamsTest` compiles with the exact uncast `ModifiableSolrParams.of(null)` call present in H1, which is the M2 source-compatibility proof; it is reported as a compile property, not a runtime fail-before.
- Focused SolrJ suites (`--rerun-tasks`): `SolrParamTest` 17, `ModifiableSolrParamsTest` 8, `ShardParamsTest` 13, `CommonParamsTest` 5, `CommonAdminParamsTest` 1; every test executed, 0 failures, 0 errors, 0 skipped. rc 0. `testOf` returns an empty result for the uncast null call, and the six isolation methods named in the negative leg are among the 17 and pass at clean H2.
- Core neighbor consumers (`--rerun-tasks`): `CoreAdminHandlerTest` 8, `StreamToolTest` 12; 0 failures, 0 errors, 0 skipped (no pre-existing assumption skips occurred). rc 0.
- Root check: `check -x test` in a normal git clone (layout disclosed per host rule: Antora cannot run in a worktree). The clone fetched H2 explicitly and recorded `git rev-parse HEAD` as `199911b298fac80f4cea5e3de553675b64f6a6a9` before building. BUILD SUCCESSFUL in 11m 3s, rc 0; clone tree clean afterwards. The check leg ran with the host CA truststore for the egress relay, the same disclosed condition as prior gates on this host.

## Negative proof: restored shallow-wrapper algorithm (disposable H2 checkout)

Only `SolrParams.ofPairs` was changed, exactly as specified; both `ofPairs` signatures and the empty-input fast path retained; `SolrParamTest.java` byte-identical before and after (md5 `8f1719eaa7ca7b4c37e4c12c8c3bb99e`):

```diff
-    return new ImmutableSolrParams(namesAndValues);
+    return new MultiMapSolrParams(
+        Collections.unmodifiableMap(ModifiableSolrParams.ofPairs(namesAndValues).getMap()));
```

This is H0's algorithm adapted mechanically to the H1 API scaffold, labeled as such; it is not a claim that the unmodified H0 test suite was run. Result: 6 tests, 6 failures, 0 errors, 0 skipped, each at its intended array-isolation assertion:

- `testOfPairsGetParamsIsIsolated`, `testOfPairsIteratorIsIsolated`, `testOfPairsMapViewIsIsolated`, `testOfPairsMapCopyIsIsolated`, `testOfPairsWriteMapIsIsolated`, `testOfPairsModifiableCopyIsIsolated`: all `ArrayComparisonFailure: arrays first differed at element [0]; expected:<[1]> but was:<[changed]>`.

The mutation was reverted; it exists in no branch or retained checkout. Secondary assertions after each first failing assertion, the input-copy tests, and the other contract checks remain positive coverage pins, not fail-before proven.

## Changelog validation, with a disclosed substitution

- The job asked for validation of `changelog/unreleased/SOLR-18621.yml` with the repository's changelog parser/check. The cross-branch tool `dev-tools/scripts/validateChangelogs.py` crashes in this clone before reading any entry: `IndexError: list index out of range` at line 306 while enumerating `git branch -r` output in `discover_branches`. It fails identically in a worktree and in the main checkout, so this is a tool-versus-clone state incompatibility on this host, not an entry defect.
- The entry was therefore validated with the repository's logchange parser itself: `logchangeGenerate` in a disposable H2 checkout, rc 0, BUILD SUCCESSFUL, and the generated `changelog/unreleased/version-summary.md` contains the SOLR-18621 entry with both authors (David Smiley, Nick Shanin). Generation side effects were discarded with the disposable checkout. A plain YAML parse of the entry also succeeds with the expected keys (title, type, authors, links).

## Final state and limits

- Positive checkout and clone trees clean; mutation checkout reverted to clean H2; final fork read-back `199911b298fac80f4cea5e3de553675b64f6a6a9`.
- Limits: M2 (the `ofPairs` rename) is proven as a source-compatibility property only, per the job. The negative leg is an adapted synthetic baseline, not the unmodified H0 suite. No statement is made about upstream CI, which does not cover H1/H2. The API choice in H1 (renaming the new varargs factories to `ofPairs` to preserve the existing null-accepting call) remains subject to the contributor's and maintainers' review; this gate proves behavior, not acceptance.

## Description draft for the follow-up (not posted)

For PR 5291 (SOLR-18621), a fix branch `solr-18621-params-factory-fixes` at `199911b298` (compare: https://github.com/nick-boss-tech/solr/compare/600e0ec4afd82004a94852c419892897be14e6f0...199911b298fac80f4cea5e3de553675b64f6a6a9) addresses two review findings on top of the PR head, with contributor ancestry preserved:

- The immutable factory result leaked its stored value arrays through the getter, iterator, map view, copied map, writeMap callback and modifiable-copy paths. The branch builds the result on a private implementation that owns copied arrays and returns clones, so none of those paths can mutate the advertised immutable params. Six new isolation tests cover each path; with the original shallow-wrapper algorithm restored under the new API, all six fail on the first array element, and they pass on the branch.
- Adding a varargs factory named `of` next to the existing null-accepting `of(SolrParams)` made the previously valid uncast call `ModifiableSolrParams.of(null)` ambiguous. The new pair factories are named `ofPairs` on both classes instead; the uncast call compiles and behaves as before, and the renamed methods keep the proposed semantics (null elements become empty strings, odd argument counts are rejected, null arrays are rejected).

Verified at the branch head: tidy clean, Error Prone compilation across SolrJ, the test framework, core and benchmark, the five SolrJ parameter suites and two core consumer suites pass with no skips, and the full `check -x test` build succeeds. One formatting-only tidy commit sits on top of the fix commit. The changelog entry parses under the project's logchange tooling; the cross-branch validation script itself crashes in this clone's branch enumeration before reading entries, which is recorded in the gate receipt.
