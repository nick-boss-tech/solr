# SOLR-8536 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-8536 - "MDC handling in MDCAwareThreadPoolExecutor uses even non-solr MDC parameters" (Konstantin Hollerith). A web app that uses SLF4J MDC with values containing line breaks gets those values copied into SolrJ executor thread names; a later comment (SolrJ 7.7) says it breaks update logging.
- Branch: `solr-8536-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`ExecutorUtil.MDCAwareThreadPoolExecutor.execute` concatenates **all** values of the submitter's MDC map into the worker thread's name (`oldName-processing-<values>`), capped at `MAX_THREAD_NAME_LEN`. It only escapes `/`. Application values with `\n` or other control characters end up in thread names and then in every log line that prints the thread name. Still true on main.

## What the branch changes
- Minimal fix: replace control characters (`\p{Cntrl}`) in the context string with spaces before it is used in the thread name. It does not limit which MDC keys are copied (the ticket title suggests that as an alternative).
- New `ExecutorUtilTest.testMdcControlCharactersNotInThreadName`.

## What was guessed (verify these first)
1. `Thread.currentThread().getName()` inside a task contains the sanitized context (`line1 line2 x` for `"line1\nline2\tx"`); the name is set inside the wrapper before the task runs.
2. The maintainers may prefer restricting to Solr's own MDC keys (collection, shard, replica, core, node name, ...) instead; that would change thread names relied on by tests/logging, so it was not done blindly.
3. Other `MDC.getCopyOfContextMap()` users that copy context into logs/names were not audited.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:solrj:spotlessApply
.\gradlew :solr:solrj:test --tests "org.apache.solr.common.util.ExecutorUtilTest"
```
Fail-before: revert only `ExecutorUtil.java`; the name contains the newline.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
