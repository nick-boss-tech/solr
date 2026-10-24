# Draft Jira comment for SOLR-17676 (not posted; revised 2026-10-23 to the staged ask)

[~malliaridis], I checked the current state of the wasmJs tests against this ticket.

The tests were disabled in [PR #3195|https://github.com/apache/solr/pull/3195] (February 2025), with the {{enabled = false}} override in the old {{solr/ui}} build file. In [PR #4650|https://github.com/apache/solr/pull/4650] that build file went away, and {{ui/shared/build.gradle.kts}} now declares the {{wasmJs}} browser target with the default test setup and no disable. I verified the result in the UI check workflow: in [this run|https://github.com/apache/solr/actions/runs/39835983878] (October 22) {{:shared:wasmJsBrowserTest}} executed alongside {{:shared:jvmTest}} and the build succeeded.

The local criterion is now checked too. A probe run on the fork main (498a09aea9) set {{CHROME_BIN}} to a path that does not exist, simulating a machine without a browser. The build reached {{:shared:wasmJsBrowserTest}} and failed there with a Karma launcher error ({{Cannot start ChromeHeadless}}, {{Can not find the binary}}); no test executed and nothing skipped. So on a machine without a browser the wasm browser tests hard-fail today rather than skipping.

Is that run sufficient evidence for the ticket's CI criterion, for the shared module?

As I see the ladder from there:

 * Close on the shared-module enablement, treating the rest as follow-ups.
 * Stay open for {{ui/webApp}}, whose build file still sets {{enabled = false}} for its browser tests.
 * Stay open for the local no-browser behavior, which is now a checked fact rather than an open question: the build fails on a machine without a browser instead of skipping, so closing on shared means accepting that failure as the defined behavior unless it is filed as its own follow-up.
 * Stay open for the Jenkins agents, which I still have not verified either way.

My recommendation is still the first: the shared tests are the bulk of the suite and they are running in CI again. If you want webApp or the no-browser behavior tracked separately, I can file either as a follow-up.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
