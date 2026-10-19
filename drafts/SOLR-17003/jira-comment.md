We checked this against Solr main at commit 498a09aea9. We have not reproduced the failure yet, so the ticket stays open.

What we checked:

- The unified highlighter in Solr passes every field to Lucene's highlighter without checking the field type first.
- The exception in the report comes from Lucene's decoder for the date range field encoding, not from Solr's own code.
- Solr has no special case for date range fields in the highlighter.

What this means: the fix may belong in Solr (skipping highlighting for range fields) or in Lucene's decoder. We have not decided which.

If you still see this: please send a minimal index and query that fails on a current release (a DateRangeField value, the hl.fl setting, and the Solr version). That would let us check the failure on main and decide where the fix belongs.
