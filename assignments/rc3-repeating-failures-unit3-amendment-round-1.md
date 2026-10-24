# Amendment: rc3-repeating-failures-rootcause-round-1, unit 3 (phone agent, 2026-10-24)

Unit 3 (the TestTermsQParserPlugin metrics leak) tests an existing hypothesis FIRST, before any generic repetition runs:

SOLR-18564 (filed 2026-10-22) already root-caused this shape: a close-pool timeout throw in `CoreContainer.shutdown()` (line 1314) aborts the trailing closes, including the root metrics-context close (line 1366), leaking 16 SolrMetricsContext objects that the JVM-global tracker charges to a later, innocent test. A gated fix exists on branch `corecontainer-shutdown-closes` at `d7caa8cb78`, with a regression test (`CoreContainerShutdownTest`) that reproduces the signature on base code (timeout throw plus 15 unreleased contexts).

Run 003 of the RC3 loop contained both halves of that signature in one run: `TestTaskManagement.classMethod` with a suppressed closeThreadPool shutdown timeout, and the 16-context charge against TestTermsQParserPlugin.

Unit 3's work is therefore: (1) establish whether the leak charge only ever follows a closeThreadPool timeout in the same fork, using the loop's run-003 logs and focused runs at the RC3 tip; (2) verify whether the SOLR-18564 fix, applied at the RC3 tip, removes the charge in the reproduction the unit builds. Report both answers in the unit's deliverable. If both hold, unit 3 closes with "mechanism identified as SOLR-18564" and no new fix branch is created under the RC3 assignment; the existing branch is the fix.
