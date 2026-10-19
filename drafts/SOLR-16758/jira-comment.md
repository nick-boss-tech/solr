We checked Solr main at commit 498a09aea9 (October 7, 2026). The graph query code there is different from the code the patch in this ticket changed, so the patch result does not carry over directly. We have not measured the query latency, so we cannot confirm or rule out the slowdown.

What we checked:

- The graph query scorer and how the matching document set is built at that commit.
- The comments on this ticket, including the in-test and separate-instance measurements.

How we tried to reproduce it:

- We did not run a benchmark for this check. The ticket's own in-test run found no difference, and the separate-instance numbers varied too much to read a clear change.

What this means:

- We cannot say whether this is a regression on current main. It needs a repeatable benchmark on the current code, with the query from this ticket, caching turned off, and the patch compared on the same data.

If you still see this:

- Please send the query, the document count, the Solr version, and the median latency over repeated runs with cache=false, with and without the patch. That would let us decide whether there is something to fix.
