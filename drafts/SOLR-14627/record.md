# Skip record: SOLR-14627

- Ticket: SOLR-14627, "SynonymGraphFilterFactory with Wordnet format synonym file produces array_index_out_of_bounds_exception at query time". JIRA status Open, resolution none, fix versions none, created 2020-07-04, updated 2024-08-16. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14627-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14627-vm2.md).

## Ticket reading

A query with SynonymGraphFilterFactory using a WordNet-format synonym file throws ArrayIndexOutOfBoundsException (index 0) while the query is built, from Lucene's QueryBuilder.newSynonymQuery, reached through Solr's query parser. The report includes a stack trace and an example query; it does not include the WordNet file or the field type. The ticket has no comments.

## Code reading

- solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:631-649 (origin/main 498a09aea97): newSynonymQuery switches on synonymQueryStyle. For PICK_BEST and AS_DISTINCT_TERMS it builds the query itself; for AS_SAME_TERM it calls super.newSynonymQuery(field, terms) at line 648, which is Lucene's QueryBuilder.newSynonymQuery. The exception in the report is raised inside that Lucene method (its line 653 in the reporter's version).
- Lucene's QueryBuilder is not in this repository, so its line-level logic (the array access that fails when the terms array is empty or has a missing entry) is not read here. The graph-token input that gives an empty term array for a position is not identified, since the WordNet file is not in the report.

Commands at the pinned head:
- git grep -n 'newSynonymQuery' origin/main -- solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java: lines 617 (the call site named in the trace), 631, 648.
- git log --oneline --grep='SOLR-14627' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14627: none.

## Reproduction attempts

Not attempted. A reproduction needs the reporter's WordNet-format synonym file and the field type that uses SynonymGraphFilterFactory, and the query string. The report gives the query but not the synonym file. A test with a guessed synonym file would not show whether the reporter's input is covered.

## Gap analysis

The failing call is located: Solr delegates the AS_SAME_TERM style to Lucene's QueryBuilder, where the array access fails. The input that produces the empty or short term array (the WordNet-format synonym entries with the query's graph tokens) is not in the report, so the cause in the input is not identified, and whether the fault is in Lucene or in Solr's handling of the graph is not established.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket (bounded --grep, no output). close-cannot-reproduce does not hold: no reproduction was made, and the code path inside Lucene is not read. The missing piece is the reporter's WordNet synonym file and field type, so the failing input can be reproduced.

## Limits

This record does not reproduce the exception. It does not read Lucene's QueryBuilder at the version the pin uses. It does not identify the input that triggers it.
