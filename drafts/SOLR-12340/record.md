# Skip record: SOLR-12340

- Ticket: SOLR-12340, Solr 7 does not do a phrase search by default for certain queries. JIRA read in this pass: Open, resolution none, fix versions none, 1 comment (2018-05-14, a committer confirms the behavior change and the sow=true workaround).
- Verdict: keep-open.
- Draft branch: solr-12340-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2026-10-04, audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: the query "test3" against a text field gives "contents:test contents:3" in Solr 7.2.1 (two term clauses), while Solr 5.2.1 gave PhraseQuery(contents:"test 3"). Passing sow=true restores the old behavior. The Jira description was read in full through the REST record (the evidence output is truncated at 1200 characters); the comment confirms the change of behavior and the workaround.

## Code reading

- solr/core/src/java/org/apache/solr/search/QueryParsing.java:51: the sow parameter name is defined.
- solr/core/src/java/org/apache/solr/parser/QueryParser.java:29 (DEFAULT_SPLIT_ON_WHITESPACE = false) and :51 (splitOnWhitespace = DEFAULT_SPLIT_ON_WHITESPACE): with the default, a whitespace-separated term sequence goes to analysis in one shot; with sow=true it is analyzed per term. The default is false at the pin.
- solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:144 (autoGeneratePhraseQueries = false by default) and :534 (phrase generation only when the term is quoted, the field asks for it, or autoGeneratePhraseQueries is set): a single term that the analyzer splits into several tokens (here "test3" into "test" and "3") is therefore produced as a boolean of term clauses, not a phrase, under the default settings.
- solr/solr-ref-guide/modules/query-guide/pages/standard-query-parser.adoc:39-44 documents sow with the default false and the reason (multi-word synonyms and shingles need sequences analyzed together). solr/solr-ref-guide/modules/indexing-guide/pages/field-type-definitions-and-properties.adoc:154 also names sow=false as the default.
- The option and its default were introduced together by commit d1b2fb33ef (SOLR-9185, 2017-03-16), which is an ancestor of the pin; the change from Solr 5 behavior the ticket reports is consistent with that default, but the Solr 5 to 7 path was not traced in detail.

## History

- No commit at the pin names SOLR-12340 (git log --grep at the pin: none).
- The defect the ticket describes is a documented default, not a code error. A ref-guide link answers how to get the old behavior (sow=true) and what the default is.

## Reproduction attempts

Not run. A reproduction needs a core with a text field analyzed by a standard tokenizer with a word-delimiter-like split on letter-digit boundaries, a query "test3", and debugQuery at the pin, with sow unset and sow=true. Not built or run in this pass.

## Gap analysis

The behavior in the ticket follows from the defaults at the pin, and the ref guide documents them. What the ticket asks for is whether the change is acceptable or should be reverted. The comment calls the change "unfortunate", so this is an open maintainer question about the default, not a settled answer.

## Verdict justification

keep-open, not close-answered: the ref-guide entry answers how the option works and the default, but the ticket asks for the behavior change to be reconsidered, and a committer has called the change unfortunate. Closing it as answered would decide that question for the maintainers. close-fixed and close-obsolete are not supported: the default is in force at the pin.

Missing for a closing verdict: a maintainer decision either to close as answered (citing the sow entry in standard-query-parser.adoc) or to change the default or the phrase generation; and, if the default is kept, a run at the pin confirming the debug output for "test3" with sow unset and sow=true.

## Limits

This record does not run the query, does not trace the Solr 5 to 7 change in detail, and does not post anything to JIRA or GitHub.
