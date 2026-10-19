We looked at this against Solr main at commit 498a09aea9 (October 7, 2026). The error is raised inside the Lucene library, in the code that reads a date range cell from the spatial index. The highlighter in Solr passes DateRangeField values to that code without a guard, so the fix belongs in Lucene, or in Solr as a guard that changes how this field type is highlighted.

What we checked:

- The Lucene version Solr uses at that commit, which is 10.4.0.
- The unified highlighter and how it handles DateRangeField values.

How we tried to reproduce it:

- We did not run a reproduction for this check. The workaround in the comments (hl.method=fastVector) is reported to avoid the error.

What this means:

- The cause is in Lucene. Whether Solr should also guard this case is still open.

If you still see this:

- Please tell us whether the default highlighter gives correct results for the same query with the workaround removed, and the Solr and Lucene versions you run. That will show whether a Solr-side guard is needed.
