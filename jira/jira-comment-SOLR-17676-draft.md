# Draft Jira comment for SOLR-17676 (not posted; revised 2026-10-23 to the staged ask)

[~malliaridis], I checked the current state of the wasmJs tests against this ticket.

The tests were disabled in [PR #3195|https://github.com/apache/solr/pull/3195] (February 2025), with the {{enabled = false}} override in the old {{solr/ui}} build file. In [PR #4650|https://github.com/apache/solr/pull/4650] that build file went away, and {{ui/shared/build.gradle.kts}} now declares the {{wasmJs}} browser target with the default test setup and no disable. I verified the result in the UI check workflow: in [this run|https://github.com/apache/solr/actions/runs/39835983878] (October 22) {{:shared:wasmJsBrowserTest}} executed alongside {{:shared:jvmTest}} and the build succeeded.

Is that run sufficient evidence for the ticket's CI criterion, for the shared module?

As I see the ladder from there:

 * Close on the shared-module enablement, treating the rest as follow-ups.
 * Stay open for {{ui/webApp}}, whose build file still sets {{enabled = false}} for its browser tests.
 * Stay open for the two criteria I have not verified either way: a local run on a machine without a browser, and the Jenkins agents.

My recommendation is the first: the shared tests are the bulk of the suite and they are running in CI again. If you want webApp tracked separately, I can file it as a follow-up.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
