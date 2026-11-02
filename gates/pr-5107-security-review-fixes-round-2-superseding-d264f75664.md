# Gate: PR 5107 security setup review fixes, Linux round 2

Superseding note: this gate pins the tidy-repaired head H2 above. The original gate run at H stopped GATE INVALID at the tidy guard: tidy reflowed two files (SSLTool.java production, SSLToolTest.java test), proven formatting-only by identical whitespace-stripped token streams. The repair was committed as Nick Shanin (no trailers) as d264f75664 and plain-pushed fast-forward to the fork branch; no test at H counts as proof for H2. All other requirements are unchanged from the original gate.

Status: DONE, verdict NOT GREEN (phone agent, 2026-11-01). All Java legs green at `d264f756644ac`; packaging BATS failed, including the new invalid-administrator case on the then-missing `--credentials` forwarding. Failure report `reports/pr-5107-security-review-round-2-failure.md` (board 9948dede283). The branch line continued through the round 3 integration to F4; the live gate for it is `gates/pr-5107-f4-unrun-legs-round-1.md`.

## Assignment and exact inputs

- Owner authorization: Nick requested implementation above Eric's branch and handoff to the gate agent, November 1, 2026.
- Tags: gate, bats, review, records. Intended host: any capable Linux pool gate host with JDK 21.0.12.1, the branch's pinned Gradle 9.7.0 distribution/dependencies, OpenSSL, keytool, curl and Bash/BATS dependencies. One gate per host under its build lock and resource guards.
- Fork: `nick-boss-tech/solr:solr-8442-security-review-r2-submit`.
- Required fixed head H: `d264f756644ac2fb8b56c09c42a0c737198c148a`.
- Contributor/original production C: `25c88fb1c432120b98f7af668cf58be0052d13d0`, Apache PR https://github.com/apache/solr/pull/5107, SOLR-8442. H is one ordinary commit above C and has been independently verified through the fork ref API.
- Implementation: `reports/pr-5107-security-review-fixes-round-2.md`.
- Implementation claim: `claims/pr-5107-security-review-fixes-windows-round-2.md`.
- Execution claim: add `claims/pr-5107-security-review-fixes-round-2-<host>.md` after the mandatory detached preflight and host checks. No Linux executor has been assigned by this posting.
- Receipt: add `receipts/PR-5107-security-review-fixes-round-2.md`. Preserve the existing `receipts/SOLR-8442.md`, which belongs to the different, incomplete service-access gate.
- Evidence: `/workspace/gates/logs/<host>/pr-5107-security-review-fixes-round-2/`, with separate positive/default, positive/no-sm, packaging/commons, packaging/picocli, negative/unit, negative/packaging and check logs/XML. Record actual checkout, JAVA_HOME, JDK/Gradle/OpenSSL/curl versions and invocation environment in the claim/receipt.

This gate is for the current contributor implementation and new fixes. Do not run the old root-only ownership job or present its partial receipt as proof here. No writer ownership code was changed in this patch. Do not modify the public PR, post comments/JIRA, rebase the contributor, or force-push.

## Preflight and permitted repairs

Refresh fork and live PR heads and current claims before work. Verify H and ancestry to C. Record PR movement; movement does not silently change this gate's input. Read host and gate-runner instructions on coordination-ui. Use the detached preflight under the host lock; use the documented standalone-clone method for Antora/root checks if needed.

Require clean tidy output at the tested head. If tidy changes files, commit only that formatting repair above H on the same fork branch, plain-push and separately verify the ref, and add a superseding gate pinned to that new full SHA before positive proof. Do not count tests at the old H as proof of a later head. Keep C as original production for negative proof, with test files taken unchanged from the final fixed head.

Environment failures are not patch failures. Record a diagnosed environment correction and the exact rerun. Do not silently retry an executed test failure. A necessary test-harness repair must be isolated, explained, committed/pushed and re-gated at the new head. Product-behavior failures require a failure report and implementation handback rather than ad hoc changes inside this gate. No automatic filesystem cleanup or weakening security checks to obtain a pass.

## Positive unit proof

Seed: `8442C0DE`. Execute these as separate serialized steps using the pool's normal logging runner. Capture fresh XML and method-level skips before later runs overwrite output. Require the relevant test tasks to actually execute, not reuse earlier outputs as fresh proof.

```bash
./gradlew :solr:core:compileTestJava
```

Default security-manager run:

```bash
./gradlew :solr:core:test --tests org.apache.solr.cli.SecureExampleCredentialsTest --tests org.apache.solr.cli.SSLToolTest --tests org.apache.solr.cli.AuthToolTest --tests org.apache.solr.cli.AuthToolPicocliTest --tests org.apache.solr.cli.IncludeFileEditorTest -Ptests.seed=8442C0DE
```

Expected: passing executed tests; record assumption skips honestly. All five `SecureExampleCredentialsTest` methods must execute, including rejection before mutation/persistence and the explicit compatibility override. `SSLToolTest` external-program methods normally skip under the security manager and must execute in the next prescribed variant. Do not count those skips as proof.

Explicit external-program variant, already required by the submitted fixtures:

```bash
./gradlew :solr:core:test --tests org.apache.solr.cli.SSLToolTest --tests org.apache.solr.cli.IncludeFileEditorTest --tests org.apache.solr.cli.TestSolrCLIRunExample.testSecureSetupExample --tests org.apache.solr.cli.TestSolrCLIRunExample.testSecureSetupCustomPasswords --tests org.apache.solr.cli.SolrCLIRunExamplePicocliTest.testSecureSetupExample --tests org.apache.solr.cli.SolrCLIRunExamplePicocliTest.testSecureSetupCustomPasswords -Ptests.useSecurityManager=false -Ptests.seed=8442C0DE
```

Require all nine `SSLToolTest` methods to execute with zero failures/errors/skips, especially:

- `testClientAuthenticationSettingsArePreserved` (both required and optional settings).
- `testShortExistingPemPassword` (real encrypted PEM with existing password `tiny`, converted key readable with the longer output password).
- `testWrongPemPasswordLeavesConfigurationUntouched`.
- `testShortKeystorePasswordLeavesConfigurationUntouched`.

Require both default/custom-password secure-example tests in each parser class, and all applicable include-file tests, to execute. Missing OpenSSL or setup exceptions are blockers, not acceptable skips.

## Positive packaging and actual TLS proof

Use the same two files through the normal `integrationTests` task twice, with its inherited parser environment set explicitly. `integrationTestsPicocli` has a curated default file list, so do not accidentally widen to that list. Archive each run's reports before the next run.

```bash
env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests --tests test_ssl.bats --tests test_secure_example.bats
```

```bash
env SOLR_PICOCLI=true ./gradlew :solr:packaging:integrationTests --tests test_ssl.bats --tests test_secure_example.bats
```

Expected: all 8 SSL cases and 2 secure-example cases pass in each parser mode, no skips. Verify the selected files and parser mode from the fresh logs rather than relying on task success alone.

The new `ssl certificate renewal preserves required client authentication` case must complete the initial mTLS startup, generate a replacement certificate/key, run `solr ssl` again, restart, accept the certificate-bearing curl request and reject the curl request without a certificate. The negative curl omits `--fail`; an HTTP 401/403 is therefore not sufficient. Record its actual TLS failure and successful preceding authenticated request so an unrelated dead server is not mistaken for proof.

The new invalid-administrator case must return the policy error before creating `http-credentials.properties`, `zk-credentials.properties`, `tls`, `zoo_data`, or the second-node home. Existing secure-example coverage must still prove both-node distributed queries/updates, permissions, anonymous monitoring, ACL isolation and restart persistence.

## Original-production fail-before proof

This is a bug-fix matched pair, not coverage-only mutation proof. Preserve new tests unchanged. In a separate disposable checkout at the final fixed head, restore exactly these two production files from C:

- `solr/core/src/java/org/apache/solr/cli/SSLTool.java`
- `solr/core/src/java/org/apache/solr/cli/SecureExampleCredentials.java`

Record the resulting diff and hashes of the retained test files. No missing API scaffold or test edits are needed. Compile before interpreting results. Run the following focused tests with the same seed and prescribed external-program variant:

```bash
./gradlew :solr:core:test --tests org.apache.solr.cli.SSLToolTest.testClientAuthenticationSettingsArePreserved --tests org.apache.solr.cli.SSLToolTest.testShortExistingPemPassword --tests org.apache.solr.cli.SecureExampleCredentialsTest.testRejectsUnusableAdministratorBeforeSaving -Ptests.useSecurityManager=false -Ptests.seed=8442C0DE
```

All three targets must execute and exhibit their named original-production defect: replaced client-authentication settings, rejection of the otherwise valid four-character PEM password, and acceptance of the unusable administrator. The PEM case may be classified as a runtime error in JUnit XML because original code throws where conversion should succeed; record that exact password-validation exception, not a compilation/startup/environment error. The other targets should fail their intended assertions. If any target passes or fails elsewhere, investigate and report; do not call the whole patch fail-before-proven.

Also rebuild the baseline distribution from those restored production files and run both packaging files once with `SOLR_PICOCLI=false`. Expected: exactly the two newly added BATS cases fail for the corresponding TLS-policy/early-validation reasons; all original cases pass. Archive full TAP/JUnit plus relevant Solr/curl output. A short-password behavior is proven by the unit pair; do not invent a packaging short-password assertion. The fixed-head packaging passes in both parser modes remain required above.

## Formatting, root checks and completion

At the final fixed head, run tidy clean and the repository root check:

```bash
./gradlew tidy
```

```bash
./gradlew check -x test
```

Record exit codes and clean `git status`/`git diff --check`. Use the documented standalone full clone if Antora rejects a worktree `.git` file. If a root check requires an environment repair such as unsetting an inherited `NODE_ENV`, diagnose it from logs, record both the interruption and the exact successful condition, and rerun the required check; never relabel the first failure as a pass or add blanket task exclusions.

The receipt must list every required leg, actual tests/skips/failures, parser modes, baseline failure signatures, fixed and original heads, the verified fork tip, and fresh raw log/XML/TAP paths. Include a concise PR follow-up draft as the final section, for review only; do not post it. State the existing truststore regeneration limit and avoid FedRAMP/FIPS claims. Green requires every named positive/check leg plus the original-production proof at the final fixed head. Until then: Implemented, Proof Pending.
