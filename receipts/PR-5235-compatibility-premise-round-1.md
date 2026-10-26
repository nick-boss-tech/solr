# PR 5235 compatibility premise gate: receipt, round 1 (superseding a7215a5)

Written by vm2, the gating host, as its finishing step. Gate: `gates/pr-5235-compatibility-premise-superseding-a7215a5.md` (board `79295439b79`). Claim: `claims/pr-5235-compatibility-premise-round-1-vm2.md`. Outcome report: `reports/pr-5235-compatibility-premise-round-1-outcome.md`.

## Inputs

- Fork test head: `nick-boss-tech/solr:solr-18351-submit` at `a7215a5bf1c79b3b7dbcbf9289aac2a43e31fbcd` (fixture repair on `de1ffbbb55add66391eed68e4153322d6a8cc6c0`, which sits on reviewed PR head `73fa87f1f41b45862c165a9571176c89e2ef4def`).
- Unchanged production base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`. Base leg is this base plus the three pinned test files from `a7215a5`. Tracked files under the base tree are unchanged.
- Test blob hashes, identical on both legs: `TestEmbeddedContentStreamCompatibility.java` `7136d6eb016a9a9ef7af24d432ed3db799b83d27`; `TestCommandOperationCompatibility.java` `e3a8821c3c147d37d3b9311a0d6289beb5c3400d`; `TestLegacyContentStreamCompatibility.java` `d3d4cc2982091d3b3796b8ba91c81e03b47ba96d`.
- PR leg: the PR tree at `a7215a5`. It carries all PR changes (20 production files under `src/java`, plus the PR's other test files), so the legs differ by the PR's whole change, not only the findings.
- Toolchain: vm2 (Linux, aarch64), JDK `/workspace/jdk/jdk-21.0.12.1+1`, Gradle from the branch wrapper, `GRADLE_USER_HOME=/workspace/gradle-home`, `--no-daemon --no-build-cache`, host lock `/workspace/tools/.gradle-vm2.lock`.
- Seed `5235C0FFEE18351` on every test leg.

## Steps and exit codes (superseding run, head a7215a5)

| Step | Command | Exit |
| --- | --- | --- |
| Repair preflight | `./gradlew tidy` on the repaired tree, tree clean | 0 |
| base-solrj | `:solr:solrj:cleanTest :solr:solrj:test --tests ...TestCommandOperationCompatibility -Ptests.seed=5235C0FFEE18351` | 0 |
| base-core | `:solr:core:cleanTest :solr:core:test --tests ...TestEmbeddedContentStreamCompatibility ...` | 0 |
| base-jetty | `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests ...TestLegacyContentStreamCompatibility ...` | 1 (4 of 10 failed, see table) |
| pr-solrj | same as base-solrj on PR tree | 1 (2 of 2 failed) |
| pr-core | same as base-core on PR tree | 1 (1 of 2 failed) |
| pr-jetty | same as base-jetty on PR tree | 1 (8 of 10 failed) |
| pr-check-core | runner passed the check as one argument, so rc is an argument-parsing error. INVALID, not a check result. | 1 (invalid) |
| core check (rerun by hand) | `./gradlew --no-daemon :solr:core:check -x test` on PR tree, after the legs | 0 |

All six legs executed tests: 14 methods on each tree, zero skips, fresh XML from `cleanTest`.

## Method outcomes (14 methods, fresh JUnit XML)

| # | Method | Base | PR |
| --- | --- | --- | --- |
| 1 | solrj `testReadCommandsTwoArgumentSignature` | PASS | FAIL: NoSuchMethodException `readCommands(Iterable, NamedList)` |
| 2 | solrj `testReadCommandsThreeArgumentSignature` | PASS | FAIL: NoSuchMethodException, three-argument form |
| 3 | core `testUnnamedContentStreamsRetainAllDocuments` | PASS | FAIL: `Both content streams must be indexed` expected 2, was 1 |
| 4 | core `testNamedContentStreamsRetainAllDocuments` (control) | PASS | PASS |
| 5 | jetty `testJettyLegacyRequestWithJavaBinWriter` | PASS | FAIL: captured body 0 bytes, expected 21 |
| 6 | jetty `testJettyLegacyRequestWithXmlWriter` | PASS | FAIL: captured body 0 bytes, expected 21 |
| 7 | jetty `testJdkLegacyRequestWithJavaBinWriter` | FAIL: captured body 0 bytes, expected 21 | FAIL: captured body 0 bytes, expected 21 |
| 8 | jetty `testJdkLegacyRequestWithXmlWriter` | FAIL: captured body 0 bytes, expected 21 | FAIL: captured body 0 bytes, expected 21 |
| 9 | jetty `testJettyJavaBinWriterStreamOverride` | PASS | FAIL: captured body 21 bytes (original), expected 18 (custom writer) |
| 10 | jetty `testJettyXmlWriterStreamOverride` | PASS | FAIL: captured body 21 bytes (original), expected 18 |
| 11 | jetty `testJdkJavaBinWriterStreamOverride` | FAIL: captured body 0 bytes, expected 18 | FAIL: captured body 21 bytes, expected 18 |
| 12 | jetty `testJdkXmlWriterStreamOverride` | FAIL: captured body 0 bytes, expected 18 | FAIL: captured body 21 bytes, expected 18 |
| 13 | jetty `testJettyContentWriterControl` (control) | PASS | PASS |
| 14 | jetty `testJdkContentWriterControl` (control) | PASS | PASS |

Totals: base 10 pass, 4 fail (all JDK-client). PR 3 pass (controls 4, 13, 14), 11 fail. Predicted totals for base (14 pass) and PR (11 fail, 3 controls pass) match on the PR side; base does not match on the four JDK methods.

## Premises

- **F1 (embedded unnamed streams drop documents): CONFIRMED.** Method 3 passes on base and fails on PR for the predicted reason (one of two streams indexed). Named control (4) passes on both legs.
- **F2 (command two- and three-argument `readCommands`): CONFIRMED by reflection.** Methods 1 and 2 pass on base and fail on PR with `NoSuchMethodException`. The test uses reflection, so this shows the public signatures are absent at runtime lookup. It is not a precompiled binary linkage probe.
- **F3 (POST body for legacy requests): Jetty CONFIRMED, JDK BLOCKED BY HARNESS.** Jetty methods 5 and 6 pass on base and fail on PR with an empty body. JDK methods 7 and 8 fail on base with the same empty body, so they cannot discriminate the fix.
- **F4 (legacy stream override bodies): Jetty CONFIRMED, JDK BLOCKED BY HARNESS.** Jetty methods 9 and 10 pass on base (custom writer bytes sent) and fail on PR, which sends the original 21-byte body. JDK methods 11 and 12 fail on base with an empty body, so base does not show the expected behavior.

Controls 4, 13 and 14 pass on both legs. The embedded control's setup defect was repaired in the superseding head.

## Caveats

- The JDK-client base result contradicts the premise's expectation that base transmits the custom writer body. This is recorded as an observation, not repaired. Whether base JDK sends no body for these requests is a question for a follow-up, not something this gate settles.
- The only test edit between the first run (`de1ffbbb55a`) and this head is the two id lines in the embedded test (`first`/`second` to `1`/`2`), recorded as a fixture repair in `a7215a5`.
- The first-run logs are archived at `/workspace/gates/logs/vm2/pr5235-first-run-de1ffbb/` and are not the result of record.

## Evidence (vm2, not on the board)

- Runner status: `/workspace/gates/logs/vm2/pr5235-superseding-a7215/status.txt`.
- Logs: `<leg>.log` for base-solrj, base-core, base-jetty, pr-solrj, pr-core, pr-jetty, pr-check-core, in the same directory.
- Fresh XML per leg: `<leg>-xml/`.
- Manual core check: `manual-core-check.log` and `manual-core-check.rc` in the same directory.
- Invalid check note: `check-invalid-note.txt`.
