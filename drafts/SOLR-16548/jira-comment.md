We looked at this against Solr main at commit 498a09aea9 (October 7, 2026). The comments in this ticket found that a repeated query is answered from the query result cache, so the filter cache is not consulted for it. The existing test for filter caching checks how many entries are inserted into the filter cache, not the hits counter shown in the admin page, so it does not settle the question.

What we checked:

- The filter caching test in the repository, and what it asserts.
- The explanation in the comments about the query result cache.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- It is not yet clear whether the hits counter is wrong or whether the counter is correct and the repeated query is answered earlier. We are leaving the ticket open to settle that with a direct check of the hits counter.

If you still see this:

- Please send the Solr version and the exact query sequence, with the filter cache hits before and after each query, and whether the query result cache was enabled. With those, we can check whether the counter is wrong.
