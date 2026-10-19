# Skip record: SOLR-15586

- Ticket: SOLR-15586, "After upgrading to Solr 8.7 from 5.2.1 seems that asking for 'score' in 'fl' causes the query to take almost twice (or so) the time". JIRA status Open, resolution none, fix versions none, created 2021-08-16, updated 2021-09-28. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15586-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15586-vm2.md).

## Ticket reading

On Solr 8.7 (upgraded from 5.2.1), a query with score in fl takes roughly twice as long as without it. The reporter uses a custom query parser that builds a large query object. A jstack sample during the slow queries shows threads in file reads (FileDispatcherImpl.pread) during the query. The report has no comments and names no test class. Its performance figures are not in a form that can be run here.

## Code reading

- solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:173-175 (origin/main 498a09aea97): when the return fields want the score (fl includes score), the request sets SolrIndexSearcher.GET_SCORES in the field flags. Requesting scores turns on the scoring path for the search, so every matching document is scored. This is the code path the report's extra time would go through.
- solr/core/src/java/org/apache/solr/response/ResultContext.java:58-62: wantsScores() is true only when the return fields want the score and the document list has scores.
- The file reads in the stack are not explained by these lines alone: the stack comes from the reporter's build and parser, and the read is in the stored-fields or index path, which was not traced here.

Commands at the pinned head:
- git grep -n 'wantsScore\|GET_SCORES' origin/main -- solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java: lines 173-175 and the score-dependent field handling near 874 and 1003.
- git grep -n 'wantsScores' origin/main -- solr/core/src/java/org/apache/solr/response: JavaBinResponseWriter.java:146, ResultContext.java:58, TabularResponseWriter.java:80.

## History

- Commits on origin/main naming SOLR-15586: none.
- The history of the GET_SCORES path and its change since 8.7 was not traced in this pass.

## Reproduction attempts

Not attempted. The reporter's comparison needs their index, their custom query parser (which builds the large query), and the timing of a query with and without score in fl. A generic benchmark would not match their query. No run was made.

## Gap analysis

The code path that adds scoring work is clear (QueryComponent.java:173-175). Whether that scoring work accounts for the reported doubling, and whether the stored-field reads in the reporter's stack come from it or from their parser, is not established. A reproduction with the reporter's query shape is needed to separate them.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing piece is a timing comparison (with and without score in fl) using a query shape like the reporter's, on the pinned head and on 8.7, with the stored-field reads counted. A close verdict needs the cause identified and fixed or shown not to apply; a pr-needed verdict needs the regression located.

## Limits

This record does not measure the slowdown. It does not establish the cause of the file reads in the reporter's stack. It does not trace the change between 8.7 and the pinned head.
