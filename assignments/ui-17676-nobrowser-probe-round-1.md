# Assignment: ui-17676-nobrowser-probe-round-1 (verify the no-browser leg of SOLR-17676)

- Status: CLAIMED (claims/ui-17676-nobrowser-probe-round-1.md, vm6). Intended host: any pool Linux host with the `premise-run` tag. Requested by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: one bounded test run plus a short report. No code changes, no branch pushes, no Jira or GitHub comments, no PRs.

## Question

SOLR-17676 (Enable wasmJs tests) names, among its criteria, running the new UI tests locally on a machine without a browser. The shared module's wasmJs tests are enabled and pass in GitHub Actions. What nobody has verified: what the build does when no browser is available.

## Run

In a checkout of current apache/solr main, from the repository root:

    CHROME_BIN=/nonexistent/no-browser-here <gradle> -p ui :shared:wasmJsBrowserTest

Use the host's normal Gradle route for the `ui` build. `CHROME_BIN` pointed at a path that does not exist is the whole simulation: it is how Karma's Chrome launcher locates the browser, so the build sees the no-browser condition on a machine that has one.

## Watch for

- The phone agent attempted this exact run on vm1 on 2026-10-23 and it failed before any test executed: `:kotlinWasmNpmInstall` died with PKIX certificate errors while downloading the Kotlin/JS toolchain (the sandbox egress proxy's certificate is not trusted by that download path). If the same failure occurs on your host, record it as the result; do not engineer around the proxy inside this assignment.
- If the run reaches the test task, record which of these it does: fails with a Karma launcher error (name it), skips the browser tests and passes, or runs them some other way. Quote the decisive log lines and give the task outcome and exit code.

## Deliverable

`reports/ui-17676-nobrowser-probe-round-1.md`: the host, the exact command, the head SHA, the outcome class above, the decisive log lines, and whether the ticket's no-browser criterion can now be called verified, failed, or still open. Mark this assignment DONE in the same push sequence.
