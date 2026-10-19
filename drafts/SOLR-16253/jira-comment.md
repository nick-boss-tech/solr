We checked this against Solr main at commit 498a09aea97 and reproduced it.

What we checked:

- The override code in SpellCheckCollator copies each spellcheck.collateParam value onto the collation query. For fq the value is added to the filters the original request already applied, so the original fq still applies.
- The filter list for the collation check is taken from the original request (SpellCheckCollator.java, around line 156). QueryComponent then appends the override fq to that list (QueryComponent.java, around lines 223 to 226).
- The existing collateParam test covers only the mm parameter, not fq.

How we tried to reproduce it:

- We added a test to SpellCheckCollatorTest at origin/main 498a09aea97 and ran it twice with the same command. The query was lowerfilt:(+fauth +home +loane) with fq=id:99999. With spellcheck.collateParam.fq=*:* no collation was returned in either run. Without the override no collation was returned either. With no fq at all, collations were returned, so the test harness itself produces collations.

What this means:

- With collateParam.fq set, the original fq is kept and the override is added to it. A query whose original fq matches nothing still matches nothing with the override, so the reported collation does not appear.

If you still see this:

- A reproduction on a current release with the steps and the Solr version would help confirm the behaviour on other setups. Whether the override should replace the original filter, or the documentation should say it adds to it, is a decision for the Solr developers.
