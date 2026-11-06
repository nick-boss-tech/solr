# Receipt: PR 5301 reconnect liveness gate, round 3

Verdict: GATE GREEN at H3, with one explicit condition on the Error Prone leg, stated in its section below. This receipt covers the test repair only; production code and both changelog fragments are byte-identical to H2, whose production proof (ordering positives and both negative legs) is re-established at H3 below.

- Job: `gates/pr-5301-reconnect-liveness-round-3.md`
- Repair: `claims/pr-5301-reconnect-repair-windows-round-3.md`, `reports/pr-5301-reconnect-repair-round-3.md`
- Execution claim: `claims/pr-5301-reconnect-liveness-round-3-phone-agent.md`
- Executor: phone-agent (Linux, this VM), 2026-11-05T23:38:41Z to 2026-11-06T00:12:27Z
- Candidate H3: `dfde9bde0a348ad44e6f6ceec197d478ef11bfd0`, child of H2 `27a9cf105b25f1bf3d604534cf8bdd8674c470fc`; contributor H0 `01e820f0539cddc4697d26176d216614a776a99a` (live PR #5301 head, verified unchanged at close); base branch_9x
- H2..H3 diff verified locally: exactly two test files. `ConnectionManagerOrderingTest.java` gains the `assumeWorkingMockito()` guard; `TestConnectionManagerReconnectStress.java` changes only `flap()` to `proxy.halfClose()` in place of `proxy.close(); proxy.reopen();`
- Toolchain: Gradle 8.10 via `solr-gradle8.sh`, vendored JDK 21, fixed seed `5301C0DE`, all builds serialized under the host build lock. Separate clean clones for positive, comparison, and both negative legs
- Evidence: `evidence/pr-5301-reconnect-round-3/pr-5301-round-3-evidence.tar.gz`, SHA-256 `72ffbe08ea9023eff6425a17f17161e86444e85ee2bbb691bdeb431faf105bc2` (logs, fresh JUnit XML, mutation diffs, hashes, runner scripts)

## Preflight and repository checks

- Fork ref read back as H3 before and after the gate; all clones pristine at H3 at close; PR #5301 read back open at H0, untouched.
- `./gradlew tidy` at H3: exit 0, no tracked change (BUILD SUCCESSFUL; tree clean after).
- `git diff --check`: exit 0.
- `./gradlew logchangeGenerate`: exit 0; generated CHANGELOG.md contains both SOLR-18623 entries (the contributor line crediting Houston Putman and the follow-up line), and no fragment drew an error. Generated output was not committed and was restored. The standalone validator's cross-branch bookkeeping errors from round 2 are unchanged in kind and name no current fragment; `logchangeGenerate` is the positive evidence, as in round 2.
- `./gradlew :solr:core:validateSourcePatterns`: exit 0. The round-2 Mockito finding is closed.

## Error Prone leg, with its condition stated

Command: `:solr:solrj-zookeeper:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true -Pjavac.failOnWarnings=false`. Exit 0. This is a warning-tolerant compile, not a strict warning-free one: the single warning is the pre-existing baseline at `FloatRangeFieldTest.java:328` (`FloatingPointLiteralPrecision`), a file outside the diff, identical to the H0 base proof in round 2. No other diagnostic appeared. Under a strict `-Werror` invocation that unrelated baseline warning still fails compilation, exactly as it does at the contributor head; nothing in this branch adds or hides a warning.

## Positive suites at H3 (fresh JUnit XML)

- `ConnectionManagerOrderingTest`: 5 tests, 0 failures, 0 errors, 0 skipped. All five methods executed; the new Mockito guard produced no assumption skips.
- `ConnectionManagerTest`: 3 tests, 0 failures, 0 errors, 1 skipped. `testLikelyExpired` and `testReconnectWhenZkDisappeared` executed and passed; the skip is the pre-existing ignored `testConnectionManager`, recorded separately.
- `SolrZkClientTest`: 7 tests, 0 failures, 0 errors, 0 skipped.
- `TestConnectionManagerReconnectStress` (`-Ptests.nightly=true`, seed `5301C0DE`), run twice in independent invocations with XML preserved separately: both passed, 1 test, 0 failures, 0 errors, 0 skipped each. A pass is only reachable with all 25 flap iterations and five expirations processed: the test itself asserts delayed and processed disconnect counts are equal and greater than zero, requires actual Disconnected observations, and injects the five expirations on schedule; those counts and observations are pass conditions inside the test, and the Gradle log carries no per-test stdout to quote them from. No wait, seed, or assertion was lengthened or weakened.

## Root check

- `./gradlew check -x test` at pristine H3: exit 0, in the positive clone (a normal clone, not a worktree).

## Controlled comparison (fixture causation)

Separate disposable H3 checkout; tests otherwise byte-identical; the only change reverts `flap()` to H2's `proxy.close(); proxy.reopen();` (exact diff and mutated-file SHA-256 in the evidence). Same stress command and seed, one run: exit 1, 1 failure, message `ConnectionManager did not re-establish the session after it expired`, with repeated connection-refused lines in the log (40 matches). This reproduces the round-2 H2 failure at the same seed and sub-seed shape and isolates the fixture change as the difference: with `halfClose()` the same command passes twice; with close/reopen it fails. The comparison change was never pushed to the offered branch.

## Negative legs at H3

- Negative A: `ConnectionManager.java` replaced with the exact H0 blob in a separate H3 checkout (production file SHA-256 `99faae67945695df385cb1c4bb2b629b44d9e3364fd5cead11af5f7f829f81e2`; ordering test file byte-identical to the positive run). `testExpiredReplacementDoesNotBlockRecovery` failed as the only executed test at `An expired replacement must not block the next reconnect`.
- Negative B (synthetic): `acceptStateEvent(long)` mutated to accept every event unconditionally (mutated file SHA-256 `2b7be4d8b9bf86d33a4f693df4e155fe7ff0e1c5050eb16e3273b756c67ea5a8`). Exactly the three stale-state ordering tests failed, each at its connected-state assertion; no other test ran.
- Proof wording, per the round-2 evidence audit: four of the five ordering methods are negative-proven across legs A and B; `testAppliesEventsInDeliveryOrder` is a coverage pin, positive-only.

## Close

The round-2 blockers are both resolved at H3: the source-pattern task passes, and the stress suite passes twice at the exact seed that failed twice at H2, with the controlled comparison reproducing the H2 failure when only the fixture change is reverted. The Error Prone condition above is the one qualification, and it is a pre-existing baseline warning on an unrelated file, proven identical at the contributor head. The failed round-2 receipt (`receipts/PR-5301-reconnect-liveness-round-1.md`) is preserved unchanged. No public comment, PR edit, review submission, Jira action, or contributor-branch change was made; the drafted comment in `reports/pr-5301-comment-draft-round-3.md` remains unposted and is not part of this receipt.
