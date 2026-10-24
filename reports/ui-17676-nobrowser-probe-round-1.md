# SOLR-17676 no-browser probe, round 1

Claim: `claims/ui-17676-nobrowser-probe-round-1.md` (vm6). Assignment: `assignments/ui-17676-nobrowser-probe-round-1.md`. Read-only run: no code change, no branch push, no Jira or GitHub comment, no PR.

## Run

- Host: vm6 (Linux 6.1 aarch64). JDK `/workspace/jdk/jdk-21.0.12.1+1`, `GRADLE_USER_HOME=/workspace/gradle-home`.
- Tree: `main` of the fork (nick-boss-tech/solr) at `498a09aea97aac86c2cc7ac114113b946d249942` ("Fork test runner v2: comma-separated test list runs one matrix job per class"). This is the fork's main, not a direct checkout of apache/solr main.
- Command, run through `pool/bin/sweep-run.sh` detached: `CHROME_BIN=/nonexistent/no-browser-here ./gradlew -p ui :shared:wasmJsBrowserTest`
- Run name: `probe-17676-498a09aea9-055827`. Log: `/workspace/gates/logs/vm6/ui-17676/probe-17676-498a09aea9-055827.log`. Headroom record alongside it.
- Start 05:58:27Z, end 06:09:48Z (11 m 17 s). Gradle exit code 1 (`rc=1`).
- Earlier launch attempts (05:40Z and 05:57Z) never started Gradle; they are recorded in the claim and are not runs.

## Outcome

The npm setup passed, so the PKIX failure seen on vm1 did not occur on this host. The build reached the test task and failed there:

- Line 19: `> Task :kotlinWasmNpmInstall` completed, with no certificate error in the log.
- Line 81: `> Task :shared:wasmJsBrowserTest FAILED`.
- Decisive lines:
  - `Cannot start ChromeHeadless`
  - `Can not find the binary /nonexistent/no-browser-here`
  - `Please set env variable CHROME_BIN`
  - `Errors occurred during launch of browser for testing.`
- No test executed. No `build/test-results` XML was written, so there are no test counts to report.

Outcome class: fails with a Karma launcher error (ChromeHeadless). It does not skip the browser tests, and it does not pass.

## Why it fails this way (rung 1, code)

- `ui/shared/build.gradle.kts` lines 32 to 33: `wasmJs { browser() }`. No browser is named and no headless or skip option is set, so the test task uses the Gradle default, which is Karma.
- `ui/build/wasm/packages/solr-ui-shared-test/karma.conf.js` lines 5 and 20 to 21: `singleRun: true` and `browsers: ["ChromeHeadless"]`. That is the only browser configured. Nothing falls back to another browser or to a skip.
- The launcher reads the binary from `CHROME_BIN`. A path that does not exist is the same condition the assignment asked for.

The failure is deterministic for this configuration and happens before any test code runs. The run is conclusive for that question; no second run was made, per the failed-run rule.

## Verdict on the no-browser criterion

Under this simulation, `wasmJsBrowserTest` with no usable browser fails the build. It does not skip the wasm tests. On this branch no no-browser path exists for the wasm tests. The JVM tests have a separate route (`material/ui-test-setup/run-ui-jvm-tests.sh`), which does not cover wasm.

Verdict: the probe question is answered as **failed**: the wasm browser tests hard-fail without a browser, rather than skipping or passing.

Limits:
- The simulation sets `CHROME_BIN` to a missing path. A machine with no Chrome installed at all, and no `CHROME_BIN`, may take a different launcher path. That was not run.
- Whether the ticket intended a skip is a question about SOLR-17676's wording, which this probe did not check. The probe reports only what the build does.

## Failed-run record

1. Question: what the wasm browser test task does with no browser. Expected: a Karma launcher error, a skip, or a pass.
2. Information gathering: the log lines above, `ui/shared/build.gradle.kts` lines 32 to 33, and the generated Karma config lines 5 and 20 to 21.
3. Rerun: none. The same head, command and environment would give the same result.
4. Status: the run's result is the answer to the question, so no patch or retry follows.

## Effect on other work

`assignments/ui-volunteer-drafts-round-1.md` item 3 waits on this report to finish `jira/jira-comment-SOLR-17676-draft.md`. The no-browser leg is now a checked fact: failed under the default config.

Headroom during the run: `/` went from 4.6G to 4.3G free; free inodes from 479579 to 449021 of 524288 (about 86% free), above the 15% floor.
