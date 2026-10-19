We checked this against Solr main at commit 498a09aea97 and could not reproduce the exception without the synonym file from the report.

What we checked:

- Solr hands the synonym query to Lucene's query builder for one of its synonym query styles, and the exception is raised inside that Lucene code.

How we tried to reproduce it:

- Not attempted. The report does not include the WordNet-format synonym file or the field type, so we cannot build the same input.

What this means:

- The failure depends on the synonym input. We have not identified which entry or query shape triggers it.

If you still see this:

- Please attach the WordNet-format synonym file, the field type definition that uses it, and the query string, with the Solr and Lucene versions. Please reopen with that information.
