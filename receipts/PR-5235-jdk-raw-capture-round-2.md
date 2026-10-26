# PR 5235 JDK raw-body capture: receipt, round 2 (superseding 1517b48)

Written by vm2, the gating host, as its finishing step. Gate: `gates/pr-5235-jdk-raw-capture-round-2.md`, superseded for the tidy repair by `gates/pr-5235-jdk-raw-capture-round-2-superseding-1517b48.md` (board `3b327924ee0`). Claim: `claims/pr-5235-jdk-raw-capture-round-2-vm2.md`. Outcome report: `reports/pr-5235-jdk-raw-capture-round-2-outcome.md`. The earlier receipt `receipts/PR-5235-compatibility-premise-round-1.md` is left intact; this receipt corrects one observation in it.

## Inputs

- Fork test head: `nick-boss-tech/solr:solr-18351-submit` at `1517b48953dd1a11037dadc28290bfb5ca05937a`. Parent `63e517018db57c59a9c4eab5140152f898bfee1d` (raw-capture repair, Nick Shanin). Tidy repair `1517b48` is formatting only, one wrapped constructor call, Nick Shanin, no trailers.
- Production PR head `73fa87f1f41b45862c165a9571176c89e2ef4def`. Unchanged production base `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.
- Only changed test file: `solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/TestLegacyContentStreamCompatibility.java`, blob `6ae19ed015bcb601ab970595d679943a92c48db5` on both legs.
- Seed `5235C0FFEE18351`.
- Toolchain: vm2 (Linux, aarch64), JDK `/workspace/jdk/jdk-21.0.12.1+1`, Gradle from the branch wrapper, `GRADLE_USER_HOME=/workspace/gradle-home`, `--no-daemon --no-build-cache`, host lock `/workspace/tools/.gradle-vm2.lock`. Jetty 12.1.14.
- Trees: `/workspace/worktrees/pr5235-pr` (detached at `1517b48`, clean) and `/workspace/worktrees/pr5235-base` (detached at `f45b38d`, with the round 2 test file overlaid by `git restore --source`). The base worktree still has overlaid files from the first gate. They are in other modules and do not affect this module's compile. Tracked production under the base tree is unchanged.

## Steps and exit codes

| Step | Command | Exit |
| --- | --- | --- |
| Preflight at `63e517018db` | `./gradlew tidy` | 0, with one formatting change (repaired in `1517b48`) |
| Preflight at `1517b48` | `./gradlew tidy`, tree clean | 0 |
| base-jetty | `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility -Ptests.seed=5235C0FFEE18351` | 0 |
| pr-jetty | same command on PR tree | 1 (8 of 12 failed, see table) |
| pr-module-check | `:solr:solrj-jetty:check -x test` on PR tree (module check, not repository-wide) | 0 |

All 12 methods executed on both legs, zero skips, fresh XML from `cleanTest`.

## Method outcomes (12 methods, fresh JUnit XML)

| Method | Base | PR |
| --- | --- | --- |
| testJettyLegacyRequestWithJavaBinWriter | PASS | FAIL: body 6 bytes `wt=xml`, expected 21 `original-request-body` |
| testJettyLegacyRequestWithXmlWriter | PASS | FAIL: body 6 bytes `wt=xml`, expected 21 |
| testJdkLegacyRequestWithJavaBinWriter | PASS | FAIL: body 6 bytes `wt=xml`, expected 21 |
| testJdkLegacyRequestWithXmlWriter | PASS | FAIL: body 6 bytes `wt=xml`, expected 21 |
| testJettyJavaBinWriterStreamOverride | PASS | FAIL: body 21 bytes `original-request-body`, expected 18 `custom-writer-body` |
| testJettyXmlWriterStreamOverride | PASS | FAIL: body 21 bytes, expected 18 |
| testJdkJavaBinWriterStreamOverride | PASS | FAIL: body 21 bytes, expected 18 |
| testJdkXmlWriterStreamOverride | PASS | FAIL: body 21 bytes, expected 18 |
| testJettyContentWriterControl (control) | PASS | PASS |
| testJdkContentWriterControl (control) | PASS | PASS |
| testJettyFormContentWriterControl (control) | PASS | PASS |
| testJdkFormContentWriterControl (control) | PASS | PASS |

Totals: base 12 pass, 0 fail. PR 4 pass (controls), 8 fail. This matches the predicted totals.

## Captured POST observations (from the OUTPUT diagnostics, 12 captures per leg)

Base:
- JDK client, JavaBin and XML legacy writers: `application/x-www-form-urlencoded`, body `original-request-body` (21 bytes). Also `text/plain` with the same body in one case.
- JDK client, custom writers: `application/x-www-form-urlencoded`, body `custom-writer-body` (18 bytes).
- Jetty client, legacy writers: `application/x-www-form-urlencoded` or `text/plain`, body `original-request-body`.
- Jetty client, custom writers: `text/plain`, body `custom-writer-body`.

PR:
- Legacy JavaBin and XML writers, Jetty and JDK clients: `application/x-www-form-urlencoded`, body `wt=xml` (6 bytes). Not the original request body.
- Stream override writers, both clients: body `original-request-body` (21 bytes) instead of `custom-writer-body`.
- Control writers: body and content type as expected on both legs.

## Correction to the earlier receipt

The earlier receipt (`receipts/PR-5235-compatibility-premise-round-1.md`) recorded the Jetty legacy PR body as empty (0 bytes). The raw capture shows that the PR sends the form-encoded body `wt=xml`, 6 bytes. The earlier empty capture came from `DebugServlet` parsing form parameters before reading the body, which is the cause identified by the Windows agent. The base JDK client's empty capture had the same cause: base sends the original 21-byte body as a form.

## Premises

- **F3 (legacy request POST body): CONFIRMED for JDK and Jetty.** Base passes all four legacy methods with the original body. PR sends `wt=xml` (6 bytes) instead.
- **F4 (writer override bodies): CONFIRMED for JDK and Jetty.** Base passes all four override methods with the custom writer body and calls the hook. PR sends `original-request-body` instead.
- **The JDK-client cases are resolved by the raw capture.** The earlier BLOCKED BY HARNESS classification is superseded.

## Evidence (vm2, not on the board)

- Runner status: `/workspace/gates/logs/vm2/pr5235-jdk-raw-round-2/status.txt`.
- Runner script: `/workspace/gates/logs/vm2/pr5235-jdk-raw-round-2/run-r2.sh`.
- Logs: `base-jetty.log`, `pr-jetty.log`, `pr-module-check.log`.
- Fresh XML and OUTPUT diagnostics: `base-jetty-xml/`, `pr-jetty-xml/`, `pr-module-check-xml/`.
- Tidy: `pr-tidy.rc` and `pr-tidy.log` (at `63e517018db`), and the repair tidy at `1517b48`.
