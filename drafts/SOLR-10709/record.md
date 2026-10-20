# Skip record: SOLR-10709

- Ticket: SOLR-10709, get "unexpected docvalues type NUMERIC for field" for query q=field:* if field is defined as docValues=true, indexed=false. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; the description (first 1200 characters) names a tlong field with docValues=true and indexed=false, and the trace through DocValuesRewriteMethod and DocValues.getSortedSet; updated 2019-06-08.
- Verdict: close-fixed (unchanged status; this record replaces the earlier record on this branch, which cited the same commit without checking the parser route at the pin).
- Draft branch: solr-10709-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: with a tlong field that has docValues=true and indexed=false, the query facet_d:* fails with IllegalStateException "unexpected docvalues type NUMERIC for field 'facet_d' (expected one of [SORTED, SORTED_SET])", raised from DocValuesRewriteMethod when the wildcard query is rewritten over sorted-set doc values.

## Code reading

- solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:1278-1285: a term "*" in a field other than "*" (for example facet_d:*) returns getExistenceQuery(field) instead of a wildcard query. The wildcard rewrite that produced the trace is not reached for this query form.
- solr/core/src/java/org/apache/solr/schema/FieldType.java:1062-1078 (getExistenceQuery): a field with docValues returns validateFieldExistsQuery(parser, new FieldExistsQuery(field.getName())). FieldExistsQuery does not call DocValues.getSortedSet, so the NUMERIC check in the ticket's trace is not reached for this query.
- solr/core/src/java/org/apache/solr/schema/FieldType.java:1170-1180 (getRewriteMethod): DocValuesRewriteMethod is still used for multi-term queries on not-indexed docValues fields. Other wildcard forms (for example prefix or pattern queries on the same field) can still reach it; the ticket's query form does not.

So the query in the ticket (field:*) no longer goes through the rewrite that threw the error.

## History

- c64a35adbc (SOLR-17631, "Upgrade to Lucene 10.2.1") changed FieldType.java from DocValuesFieldExistsQuery and NormsFieldExistsQuery to FieldExistsQuery (the diff shows the import and the javadoc changes). It is an ancestor of the pin (checked with merge-base).
- No commit at the pin names SOLR-10709 (git log --grep at the pin: none); the linkage is by code path.

## Reproduction attempts

Not run in this pass. A reproduction would index a document with facet_d as a tlong with docValues=true and indexed=false and run q=facet_d:* at the pin, recording whether the query returns the document without the exception.

## Gap analysis

The route the ticket's query takes at the pin is the existence query, not the rewrite that failed. Whether the other wildcard forms on not-indexed docValues fields still fail is outside this ticket's query, and would need a new ticket if seen.

## Verdict justification

close-fixed: the fixing change c64a35adbc is on main at the pin (ancestor checked), and the changed code (the existence query for docValues fields, FieldType.java:1062-1078, reached from SolrQueryParserBase.java:1278-1285) is present at the pin. The rubric's close-fixed check is met, with linkage by code path because the commit is a Lucene upgrade that does not cite the ticket.

Caveat for the owner: the close should cite c64a35adbc and state that the query was not run in this pass.

## Limits

This record does not run the query, does not check other wildcard forms, and does not post anything to JIRA or GitHub.
