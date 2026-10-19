The optimize behavior here follows from the default for the optimize segment count, and the Solr reference guide describes it.

When optimize is requested without maxSegments, Solr asks the merge policy for no more than the largest possible number of segments. That is already true for an index that has no uncommitted changes, so nothing is merged and the request returns straight away. This is the documented default, not a separate skip when there are no changes.

The reference guide says maxSegments is optional, with no default, and advises leaving it out unless there is a clear reason to control the segment count. The Solr 8 upgrade notes say that if you need a specific number of segments after an optimize, you should set maxSegments to a positive integer, which is what optimize=true&maxSegments=1 does in your request.

Pages:

- Indexing with update handlers, the maxSegments parameter: https://solr.apache.org/guide/solr/latest/indexing-guide/indexing-with-update-handlers.html
- Major changes in Solr 8, the note on optimize and maxSegments: https://solr.apache.org/guide/solr/latest/upgrade-notes/major-changes-in-solr-8.html

What we checked:

- The commit path that runs the optimize, and its default segment count, at Solr main commit 498a09aea9.
- The reference guide text for maxSegments and the Solr 8 upgrade note.

If the wording in the guide does not make this clear for you, please tell us which sentence you read as saying an optimize should always reduce the index to one segment. We can then look at the wording.
