# Skip record: SOLR-4610

- Ticket: SOLR-4610, "Calling query.toString will print binary values for encoded integer values in terms". JIRA status read 2026-10-20T15:43:44Z (UTC): Open, resolution None, updated 2013-03-20.
- Verdict: keep-open (kept; re-read at the pinned head). The schema-aware helper exists; the deeper fix (typed terms) is a design change.
- Draft branch: solr-4610-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T15:43:44Z (UTC).

## Ticket reading

`Query.toString` prints the encoded bytes of numeric terms, because a term does not record its field type. The reporter proposes carrying the type into the term. A 2013 comment points to `QueryParsing.toString(...)`, which takes the schema.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/search/QueryParsing.java:346`: `toString(Query query, IndexSchema schema)` is the schema-aware printer the comment names.
- The numeric field types are still in the tree (`solr/core/src/java/org/apache/solr/schema/TrieField.java`, `TrieIntField.java`), so encoded terms still need the schema to print.
- A plain `Query.toString` call still prints the bytes; the ticket's deeper change (types in terms, or typed setters) was not made.

## History

- evidence.py at the pinned head: no commits naming SOLR-4610.

## Reproduction attempts

Not run. A run would build a numeric term query and print it with `Query.toString` and with `QueryParsing.toString`.

## Gap analysis

A schema-aware helper answers the practical need. The reporter's deeper fix changes how terms carry types, which is a design change in Lucene-facing code.

## Verdict justification

keep-open meets the rubric for an open design question: attempts made are the helper reading and the field-type check; the missing piece is the decision on typed terms. close-answered needs a docs page that answers the question, and none was found here; close-fixed needs a fix commit, which is not named.

## Limits

This record does not run the printers. It does not check the reference guide for the helper.
