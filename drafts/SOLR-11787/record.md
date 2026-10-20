# Skip record: SOLR-11787

- Ticket: SOLR-11787, "StackOverflowError in leader election". JIRA status read 2026-10-20T07:50:09Z (UTC): Open, resolution None, updated 2024-06-08. JIRA comment (2024-06-08) asks whether this duplicates SOLR-6213 (not checked here).
- Verdict: close-obsolete. This replaces the recorded keep-open: the logging framework the stack trace runs through is no longer in Solr.
- Draft branch: solr-11787-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:50:09Z (UTC).

## Ticket reading

Leader registration threads (coreZkRegister) fail with a StackOverflowError whose frames are in `java.text` formatting and `org.apache.log4j` pattern and appender classes (log4j 1.x). The reporter saw it on 7.x. No reproduction. A 2024 comment asks whether it duplicates SOLR-6213.

## Code reading (pinned head)

- The grep the verdict rests on: `git grep -l "org.apache.log4j\b" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java solr/solrj/src/java solr/server`. Result: empty. A second grep, `git grep -l "import org.apache.log4j\." 498a09aea97aac86c2cc7ac114113b946d249942 -- solr`, is also empty. Solr at the pinned head logs through log4j 2 (SOLR-7887 moved the logging stack earlier; the fork's Log4j2Watcher is in `solr/core/src/java/org/apache/solr/logging/log4j2/`).
- So the appender and pattern classes in the trace are not on the pinned head's classpath, and the trace cannot recur in that form.
- The leader-election code the ticket names has changed since 2017 (shard terms, ZkShardTerms.java), as the SOLR-12166 record notes. That change is not checked line by line here.

## History

- evidence.py at the pinned head: no commits naming SOLR-11787.
- The log4j 1 removal commit was not located in this pass.

## Reproduction attempts

Not run. The trace depends on a log4j 1 appender that is not on the pinned head, so a run would test a different logging stack.

## Gap analysis

The stack overflow was in the logging path, and that path has been replaced. Whether a deep recursion in leader election can still happen through another path is a different question; a new report would have to show it.

## Verdict justification

close-obsolete meets the rubric: the code the ticket depends on (log4j 1.x appenders and pattern converters) is removed, and the grep under `solr/` at the pinned head is empty for the package. SOLR-6213 (the possible duplicate) is not checked here and is not needed for this verdict.

## Limits

This record does not confirm that SOLR-6213 is or is not a duplicate. It does not establish the removal commit for the log4j 1 stack.
