We checked this against Solr main at commit 498a09aea97 and could not confirm the cause yet.

What we checked:

- The reference guide's synonym-filter examples put the synonym filter before the stemmer. The guide does not give an explicit rule on this for Portuguese.
- The report's schema is not in the text we have, so we cannot check the analyzer chain for this field.

How we tried to reproduce it:

- Not attempted. A run would need the schema and the documents that fail to match.

What this means:

- A stemmer before the synonym filter is a likely cause, as a comment on this ticket suggests. We cannot confirm that for this schema.

If you still see this:

- Please attach the schema (or the field type's analyzer chain) and an example query with the expected and actual results, with the Solr version. Reopen with that information and we will check the chain.
