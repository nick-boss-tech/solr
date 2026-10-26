# Gate: PR 5235 JDK raw-body capture follow-up, round 2

Status: OPEN

- Tags: gate, premise-run. Intended host: any capable Linux gate host with JDK 21 and this branch's build dependencies; serialize under the host build lock.
- Source: follow-up to Nick's test request and completed premise gate. This is a diagnosed test-harness repair, not a blind repeat of the failed gate.
- Fork branch: `nick-boss-tech/solr:solr-18351-submit`.
- Exact test head: `63e517018db57c59a9c4eab5140152f898bfee1d`.
- Parent: `a7215a5bf1c79b3b7dbcbf9289aac2a43e31fbcd`.
- Production PR head: `73fa87f1f41b45862c165a9571176c89e2ef4def`.
- Unchanged production base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.
- Seed: `5235C0FFEE18351` on both legs.
- Changed path relative to parent: `solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/TestLegacyContentStreamCompatibility.java` only.
- Pinned test blob: `f3827885d6039e99259221796d2ee0d3d0ff3aaf`.
- Preparation claim: `claims/pr-5235-jdk-raw-capture-windows-round-2.md`.
- Preparation report: `reports/pr-5235-jdk-raw-capture-windows-round-2.md`.
- Receipt target: `receipts/PR-5235-jdk-raw-capture-round-2.md`, written only by the gate host.
- Prior receipt: `receipts/PR-5235-compatibility-premise-round-1.md`. Leave that completed receipt, gate, claim, and outcome report intact.

## Why this follow-up exists

The previous gate confirmed F1, F2, and the Jetty halves of F3/F4, but its four JDK methods failed on base as well. That proves an empty post-parsing capture, not necessarily an empty wire body. Base HttpJdkSolrClient labels legacy streams application/x-www-form-urlencoded, while DebugServlet calls getParameterMap before reading its input stream. Parameter parsing may consume the form bytes. The old uniform text/plain assertion is also unrelated to the body premise on the base JDK branch.

The new suite uses an owned RawBodyServlet with no parameter API calls, returns a valid XML response, and captures raw POST bytes/content type. Its HEAD response does not overwrite POST observations. All eight original exact body expectations remain unchanged; header equality remains on explicit ContentWriter controls. Two additional form-encoded controls test the capture itself. The source-based explanation must be confirmed or falsified by this experiment.

## Claim, preflight, and overlay

Read live WORKFLOW.md and START-HERE.md and check competing claims, receipts, and the fork head before claiming. Record host, JDK, Gradle version, host lock, full detached checkouts, runner, timestamps, and absolute evidence paths. Windows did not compile or run this repair. Use full Linux source trees, not the implementation host's sparse checkout.

Prepare the PR leg at the exact fork test head. Prepare the base leg at the exact unchanged production base, overlaying only this one test file from the test head. Verify its Git blob is identical on both legs and equals the pinned hash. Base tracked production must be unchanged. There is no need to overlay the command or embedded test suites for this HTTP-only experiment.

Inspect the one-file change against `a7215a5` and confirm all body marker assertions and custom hook assertions are retained. Run `./gradlew tidy` at the test head and require a clean tree. If tidy or compilation requires a justified test-only repair, commit and push it normally with Nick Shanin's identity/no trailers and publish a superseding gate for its new head before running the comparison. Carry identical repaired bytes to base. Do not modify production code or accept fixture/compile failures as regression evidence.

## Focused commands

Run the base leg first, then the PR leg, using the same command independently in each full source checkout:

```sh
./gradlew --no-daemon :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
```

Run the affected-module check at the pinned PR test head as a separate correctly tokenized command:

```sh
./gradlew --no-daemon :solr:solrj-jetty:check -x test
```

This names a module check; do not report it as a root/repository-wide check. The prior core check result stays as recorded in its own receipt.

Archive full command lines, separate exit codes, logs, fresh XML, and class output for both legs:

```text
solr/solrj-jetty/build/test-results/test/TEST-org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility.xml
solr/solrj-jetty/build/test-results/test/outputs/OUTPUT-org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility.txt
```

The output now records each observed POST's client, writer, content type, and fixed marker body before the assertion. Preserve those diagnostics for passing base methods as well as failing PR methods. Cached/up-to-date-only results do not count.

## Methods and acceptance

All 12 methods must execute without skips on each leg:

- F3, four legacy request cases: `testJettyLegacyRequestWithJavaBinWriter`, `testJettyLegacyRequestWithXmlWriter`, `testJdkLegacyRequestWithJavaBinWriter`, `testJdkLegacyRequestWithXmlWriter`. Expected body: `original-request-body` (21 bytes).
- F4, four writer override cases: `testJettyJavaBinWriterStreamOverride`, `testJettyXmlWriterStreamOverride`, `testJdkJavaBinWriterStreamOverride`, `testJdkXmlWriterStreamOverride`. Expected body: `custom-writer-body` (18 bytes), and the custom hook must be called.
- Four controls, passing on both legs: `testJettyContentWriterControl`, `testJdkContentWriterControl`, `testJettyFormContentWriterControl`, `testJdkFormContentWriterControl`. Each must preserve the explicit body and content type, including form-encoded content.

Predicted base: all 12 pass. Predicted PR: eight regression methods fail and all four controls pass. F3 should observe an empty raw body on PR; F4 should observe the original marker instead of the custom one. These are predictions, not permission to alter assertions to fit them. Record actual method outcomes and content types. A base legacy JDK form content type is diagnostic, not a failure of this body-specific comparison.

Confirm a JDK regression only if its repaired test passes on base and fails on PR for the intended body reason. If base continues to fail, record NOT REPRODUCED or BLOCKED BY HARNESS with the raw observations and do not claim the JDK issue proved. Keep Jetty outcomes separate. Passing form controls support the capture repair but do not establish any production fix.

This gate does not rerun F1/F2, which remain covered by the earlier pinned experiment. It cannot mark the PR green or fixed. An expected nonzero PR test exit is compatible with a successfully completed premise experiment, but must be recorded honestly.

## Completion

Publish the receipt with exact production/base/test SHAs, identical test blob hash, seed, toolchain, runner, all commands/exit codes, 12 method outcomes per leg, actual per-method header/body observations, tidy/module-check results, and raw evidence paths. Include a short outcome table in a new report `reports/pr-5235-jdk-raw-capture-round-2-outcome.md`; its existence and the receipt close the gate claim. Never overwrite the prior host's receipt/report.

Unexpected test failures must be classified before a controlled harness repair/retry. An environment attempt that never executes tests may be repaired and relaunched at most once, with the reason recorded. Do not weaken the body assertions or fix production code in this job.

No GitHub/JIRA comment, review, PR/body edit, PR opening, or write to apache/solr or dsmiley/solr is authorized.
