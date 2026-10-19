We checked this against Solr main at commit 498a09aea97 and could not confirm the slowdown yet.

What we checked:

- Asking for the score in the field list switches the search onto its scoring path, so each matching document is scored. That is the place where extra time can come from.
- The stack the report includes shows file reads during the query, which the code path above does not explain by itself.

How we tried to reproduce it:

- Not attempted. The comparison needs the same index and the same query parser as the report.

What this means:

- We have not measured the difference, so we cannot say whether it comes from scoring or from the custom parser.

If you still see this:

- Please send the query, the custom query parser's name or the query object it builds, the index size, and the timings with and without score in the field list, on the Solr version you use now. Please reopen with that information.
