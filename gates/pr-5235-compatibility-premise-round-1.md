# Gate: PR 5235 ContentStream compatibility premises, round 1

Status: OPEN

- Tags: gate, premise-run. Intended host: any capable Linux gate host with JDK 21, Git, Python 3, and the branch's Gradle toolchain/dependencies. Serialize under the host's build lock.
- Source: Nick's October 26 request for executable tests for the four PR 5235 review findings.
- Fork branch: `nick-boss-tech/solr:solr-18351-submit`.
- Exact test head: `de1ffbbb55add66391eed68e4153322d6a8cc6c0`.
- Reviewed PR head / parent of test head: `73fa87f1f41b45862c165a9571176c89e2ef4def` (apache/solr PR 5235, David Smiley's `SOLR-18351-backport-10x`).
- Unchanged production base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7` (`branch_10x` base and merge base of the reviewed PR).
- Seed for both legs: `5235C0FFEE18351`.
- Test delta: the three added files listed below, and no production change.
- State: test-only implementation, proof pending. Intentionally red regression tests are expected on the PR production code; this is a premise comparison, not a fix gate.
- Preparation claim: `claims/pr-5235-compatibility-tests-windows-round-1.md`.
- Preparation report: `reports/pr-5235-compatibility-tests-windows-round-1.md`.
- Receipt target: `receipts/PR-5235-compatibility-premise-round-1.md`; the gate host is its only writer.

## Preflight and comparison setup

Read live WORKFLOW.md and START-HERE.md and check the live claim records before claiming. Confirm the pinned fork head and record the Linux host, JDK, Gradle version, build lock, clean detached checkouts, runner, and absolute evidence/log/XML paths in the gate claim. Use full source checkouts; do not copy the Windows sparse checkout settings.

Inspect the test commit against its parent. Only these files may differ:

```text
solr/core/src/test/org/apache/solr/client/solrj/embedded/TestEmbeddedContentStreamCompatibility.java
solr/solrj/src/test/org/apache/solr/common/util/TestCommandOperationCompatibility.java
solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/TestLegacyContentStreamCompatibility.java
```

Prepare two detached source trees using the host's normal gate paths:

1. PR leg: exact fork test head `de1ffbbb55add66391eed68e4153322d6a8cc6c0`.
2. Base leg: exact unchanged production base `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`, with only the three test files overlaid from that test head. Use `git restore --source=de1ffbbb55add66391eed68e4153322d6a8cc6c0 --worktree -- <the three paths above>` or an equivalent exact-file copy. Do not cherry-pick either production commit from PR 5235 onto the base.

Verify each overlaid file's Git blob hash matches the same path in the fork test commit. Verify all tracked production files in the base tree are unchanged and that its only untracked additions are the three tests. Record the production SHA and test blob hashes separately. Use the same JDK, seed, and test bytes on both legs.

Pinned Git blob hashes (in the file order above): `d48a15f9a43def5f5848d5440f9eb89c8a63dac8`, `e3a8821c3c147d37d3b9311a0d6289beb5c3400d`, `d3d4cc2982091d3b3796b8ba91c81e03b47ba96d`.

## Formatting and compile preflight

At the PR test head, run `./gradlew tidy`, require a clean tree, then run `./gradlew check -x test` as required by the branch's AGENTS.md. Save command exit codes and logs, including independent baseline failures; do not silently exclude checks. A compile failure or formatting discrepancy is not evidence for any review finding.

Windows did not compile these new tests. A gate host may repair formatting, compile errors, or fixture setup, restricted to these three test files. Commit such a repair with Nick Shanin's ICLA identity and no trailers, push normally to the fork test branch, and post a superseding gate naming the new SHA before proving that head. Carry the identical repaired test bytes to the base leg. Preserve the scenarios and assertions; do not weaken them to manufacture a result. No production fixes are authorized by this premise job.

## Focused commands on each leg

Run the base leg first to establish that the test harness and scenarios work without the PR changes, then run the PR leg. Execute each command independently so an expected regression in one module does not prevent the other suites from running. Save every exit code. Fresh XML and method-level results are required; cached or up-to-date results do not count.

```sh
./gradlew :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.util.TestCommandOperationCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
```

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.client.solrj.embedded.TestEmbeddedContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
```

```sh
./gradlew :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
```

Retain each leg's complete logs, class outputs, and these fresh files separately:

```text
solr/solrj/build/test-results/test/TEST-org.apache.solr.common.util.TestCommandOperationCompatibility.xml
solr/core/build/test-results/test/TEST-org.apache.solr.client.solrj.embedded.TestEmbeddedContentStreamCompatibility.xml
solr/solrj-jetty/build/test-results/test/TEST-org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility.xml
```

## Expected observations, not measured results

The identical test sources contain 14 methods:

- F1: embedded `testUnnamedContentStreamsRetainAllDocuments`. Base should index both `first` and `second`; PR should fail because only the first stream is indexed.
- F2: command `testReadCommandsTwoArgumentSignature` and `testReadCommandsThreeArgumentSignature`. Base should find, invoke, and parse through both public methods; PR should fail their reflective lookup with NoSuchMethodException. Reflection intentionally allows compilation on both revisions. This is not a precompiled binary linkage probe.
- F3: HTTP `testJettyLegacyRequestWithJavaBinWriter`, `testJettyLegacyRequestWithXmlWriter`, `testJdkLegacyRequestWithJavaBinWriter`, `testJdkLegacyRequestWithXmlWriter`. These are GenericSolrRequest subclasses that inherit its null-returning getContentWriter and override getContentStreams. Base should transmit `original-request-body`; PR should fail the exact POST-body assertion.
- F4: HTTP `testJettyJavaBinWriterStreamOverride`, `testJettyXmlWriterStreamOverride`, `testJdkJavaBinWriterStreamOverride`, `testJdkXmlWriterStreamOverride`. The request supplies `original-request-body` through the SolrRequest legacy hook, while a custom JavaBin/XML writer supplies `custom-writer-body`. Base should send the custom writer's bytes and call its hook; PR should instead send the original bytes and fail.
- Controls: embedded `testNamedContentStreamsRetainAllDocuments`, HTTP `testJettyContentWriterControl`, and `testJdkContentWriterControl`. All three should pass on both legs.

Predicted totals: base 14 pass; PR 11 non-passing regression methods and 3 passing controls, with no skips. Report actual failures versus errors rather than assuming the JUnit classification. These counts are hypotheses to test, not an acceptance instruction to force matching output.

## Verdict and completion

For each finding, confirm a regression only if the relevant test passes at the unchanged base and fails at the PR production head for the predicted behavioral reason. A test that fails on both legs, fails setup/compile, is skipped, or never executes is not a reproduction. Investigate a fixture failure before a controlled retry. If a test contradicts the review, record the correction instead of changing its expected result to fit the finding.

The job can complete successfully as a comparative experiment even though the PR leg's Gradle test commands return nonzero. Never label the PR implementation green or fixed from this experiment. State each premise as CONFIRMED, NOT REPRODUCED, or BLOCKED BY HARNESS with the exact evidence. If tests execute and fail unexpectedly, classify the failure and repair only the test harness when justified; do not blindly rerun. If an environment attempt never executes tests, repair and relaunch at most once with the reason recorded.

Publish the receipt with the exact fork head, production base/head SHAs, identical test blob hashes, toolchain, seed, commands/exit codes, all 14 method outcomes on both legs, tidy/check results, and raw evidence paths. Add the outcome report as a new board file and let its existence close the gate claim. The receipt's final section may contain a concise proposed maintainer update for Nick to review; nothing is to be posted publicly.

No write to apache/solr, dsmiley/solr, the live PR, or JIRA is authorized. Do not create a public PR or comment from this job.
