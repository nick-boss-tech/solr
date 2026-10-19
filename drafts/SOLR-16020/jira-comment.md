We checked this against Solr main at commit 498a09aea97 and could not reproduce it.

What we checked:

- The exception comes from Lucene's fragment builder (BaseFragmentsBuilder), not from Solr's own code. Solr's part is the call to the FastVectorHighlighter in DefaultSolrHighlighter.
- The line numbers in the stack trace come from the 7.7.3 release, so they do not match the current code.

How we tried to reproduce it:

- Not attempted. The report does not include the query, the field definition, or the document that triggers the exception.

What this means:

- The exception means the term-vector offsets and the stored field text disagree for one document. The cause could be in the indexed data or in the Lucene fragment code. We cannot tell which from the report.

If you still see this:

- Please send the query, the highlight parameters, the field definition, and the stored value and term vectors of the document that fails, with the Solr version. That would let us reproduce it.
