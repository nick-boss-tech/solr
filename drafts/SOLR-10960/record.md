# Skip record: SOLR-10960

- Ticket: SOLR-10960, streaming solrj test innerJoin failures when randomizing PointFields. JIRA read in this pass: Open, resolution none, fix versions none, 2 comments (2017-06-26, the example failures and the SOLR-10946 randomization commit); updated 2021-10-28; links: SOLR-10946 (Resolved), SOLR-11048 (Closed).
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J, C and L).
- Draft branch: solr-10960-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: when SOLR-10946 randomized PointFields in the solrj test schemas, JDBCStreamTest innerJoin expressions on personId_i returned no tuples. The reporter did not know whether the root cause was solr-side or solrj-side. The 2017 comment gives the reproduce command and the commit 14a0b4fa53 (SOLR-10946) that randomizes points.

## Code reading

- solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/stream/JDBCStreamTest.java:51: the class carries @SuppressPointFields(bugUrl = "https://issues.apache.org/jira/browse/SOLR-10960"). The test framework therefore does not randomize point fields for this test, which means the bug this ticket describes is still known and still avoided at the pin.
- JDBCStreamTest.java:377-382 index personId_i as a plain field value (the schema used by this test is not randomized to points).
- 14a0b4fa53 (SOLR-10946, 2017-06-26), an ancestor of the pin, is the randomization commit that exposed the failure; the bug was not fixed by it.
- SOLR-11048 (8d7e3cc929) randomized PointsFields in the core test schema; it is an ancestor of the pin and names no fix for the innerJoin failure (its subject is randomization of the schema-add-schema-fields processor).

So the innerJoin-on-point-fields failure is still an open issue at the pin, as the suppression annotation records. The annotation is the evidence that the defect was not closed.

## History

- No commit at the pin names SOLR-10960 as a fix; the annotation references it as the bug URL.
- The innerJoin code path with point fields was not traced in this pass.

## Reproduction attempts

Not run in this pass. A reproduction would remove the @SuppressPointFields annotation at the pin and run JDBCStreamTest.testJDBCSolrInnerJoinExpression with the seed from the ticket. Not built or run here.

## Gap analysis

The defect is still open at the pin, because the test still suppresses point fields for it. Whether the innerJoin still returns no tuples with point fields is not established; the annotation implies it does or that the test was not verified.

## Verdict justification

keep-open: the suppression annotation at JDBCStreamTest.java:51 names this ticket as the bug, so the issue is still open in the code. close-fixed and close-obsolete are not supported (no fix commit; the test still avoids the points path). close-cannot-reproduce would need a run attempt, and none was made.

Missing for a closing verdict: a run with point fields enabled for JDBCStreamTest at the pin (removing the suppression); zero failures would support close-fixed on a named commit or close-cannot-reproduce, and a failure confirms the open defect.

## Limits

This record does not run JDBCStreamTest, does not trace the innerJoin code path, and does not post anything to JIRA or GitHub.
