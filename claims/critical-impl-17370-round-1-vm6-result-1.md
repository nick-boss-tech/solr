# Result 1: claim critical-impl-17370-round-1-vm6, SOLR-17370 premise run: NOT RUN

- Assignment: `assignments/critical-impl-17370-round-1.md`, step 1 (premise run). Head: apache main `ee2f1bf2e0109956145d7fd995da63c4091a589f`, worktree `/workspace/worktrees/dx-main-vm6`.
- Four setup attempts, each failed before any test body ran (no XML results, no PREMISE lines):
  - a: compileTestJava, wrong `SolrQuery` import. Fixed.
  - b: `NoSuchFileException: src/test-files/solr/solr.xml`. The Gradle test working directory is `build/tmp/tests-cwd`. Fixed by loading config from the classpath.
  - c: `solrconfig.xml` include `solrconfig.snippet.randomindexconfig.xml` not found. Fixed by copying the config directory.
  - d: started with the pre-fix file by mistake, stopped by PID, no tests run.
  - e: `NoSuchFileException` for `conf/foo/bar.txt`, because the copy did not create subdirectories. Fixed.
  - f: `SolrCoreInitializationException`: `solrconfig.xml` needs `solr.tests.maxBufferedDocs` and the other `solr.tests.*` system properties. `SolrTestCaseJ4` sets these; `SolrTestCase` does not.
- Stopped per the setup-retry rule: a fifth try is not made. The premise is not run, so no outcome is recorded for mixed, alpha, numeric, or mixed+alpha.
- Not done: no branch, no push of code, no Jira or GitHub text. The uncommitted test file was deleted from the dx-main worktree, which is clean.
- Verdict: premise not established either way. The assignment's step 1 needs a harness that sets the test system properties itself (the same properties `SolrTestCaseJ4` sets in its static setup) or a different approach. That is a decision for the next tick or the owner, not a reason to call the premise false.
- Investigation so far (progress file, 877f4198919): the ticket's stack shows a Lucene `LongPoint$1` (a `PointRangeQuery` subclass) reaching `Utils.getReflectWriter` through `JavaBinCodec`. No `IllegalAccessException` string in `search/facet` at this head. The origin of the `Query` in the response is not located.

DONE: 2026-10-27T10:00:00Z (premise not run; claim ends with this verdict).
