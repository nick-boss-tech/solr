# Skip record: SOLR-16062

- Ticket: SOLR-16062, "Solr 8.1 - Score for same catalog entry differs for different persons / environment". JIRA status Open, resolution none, fix versions none, created 2022-03-01, updated 2022-05-12. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16062-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16062-vm2.md).

## Ticket reading

The reporter enabled learning to rank (LTR) on Solr 8.1, uploaded the same catalog and model to a local system and a QA environment, and found that the scores and sort order for the same query differ between them. The report attaches a screenshot of the score difference (not available here). It gives no query, no model or feature store contents, and no index or environment details. The report has no comments and names no test class.

## Code reading

- The LTR module is solr/modules/ltr/src/java/org/apache/solr/ltr (origin/main 498a09aea97).
- LTRRescorer.java:119-135: rescore(searcher, ...) and rerank(...) read the searcher's leaves (searcher.getIndexReader().leaves() at line 135) and compute the model score per leaf. The score therefore depends on the feature values and the model, and on the index the features are computed from.
- LTRScoringQuery.java: the features and the model are read from the feature store and the model store uploaded to the environment. Two environments that uploaded the same JSON can still differ if the feature definitions, the model, or the index contents differ.

Commands at the pinned head:
- git grep -n -i 'docFreq\|termStatistics\|collectionStatistics' origin/main -- solr/modules/ltr/src/java: no output. The LTR module does not read collection statistics directly. Whether a feature reads term statistics through a Lucene query is not checked in this pass.
- git grep -n 'SOLR-16062' origin/main: no output.

## History

- Commits on origin/main naming SOLR-16062: none.

## Reproduction attempts

Not attempted. The report gives no query, feature store, model, or index, so a run here would be guessing at the reporter's setup. The attached screenshot is not in the report text.

## Gap analysis

The score difference has several candidate causes in the code path above: a different model or feature store on one environment, feature definitions that read index-dependent values (such as term statistics), or different index contents. The report does not separate them. No candidate is confirmed, and the reporter's setup is needed to separate them.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing pieces are (1) the reporter's query, feature store, model, and the feature values for one document on each environment, and (2) the index contents or statistics for the two environments. A close verdict needs a documented answer or a reproduction showing identical scores; a pr-needed verdict needs the cause.

## Limits

This record does not reproduce the difference. It does not check the attached screenshot, which is not in the report text. It does not establish which feature type reads index statistics.
