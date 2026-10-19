We checked Solr main at commit 498a09aea9 (October 7, 2026). The failure comes from how the Calcite SQL library treats the character set of a text literal in a LIKE condition. Calcite's own issue for this, CALCITE-2616, is still open and has no fix version, and the Calcite release Solr uses does not include a fix.

What we checked:

- The Solr SQL and JDBC code. It does not set a character set for literals, so the behavior comes from Calcite.
- The Calcite issue CALCITE-2616, and its status on the Apache JIRA.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- Equality tests with the same text work, and LIKE does not, which fits the literal charset problem. A possible workaround is to override the charset in the JDBC connection settings, as the earlier comment suggests. We have not tested that.

If you still see this:

- Please send the Solr version, the JDBC connection string, and the exact SQL that fails, with the full error. With those, we can test whether the charset override works for your case.
