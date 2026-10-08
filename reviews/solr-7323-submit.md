# solr-7323-submit

- Branch: origin/solr-7323-submit
- Head: fb034dc5f877 (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base cabedd1d968, 16 commits behind)
- Scope: 4 files, +46/-1. `solr/core/.../core/FileSystemConfigSetService.java` (the `locateInstanceDir` error message now names the configSet, the base directory, and the `configSetBaseDir` hint), `solr/core/src/test/.../core/TestFileSystemConfigSetService.java` (one new test, Mockito `CoreDescriptor`), changelog fragment (type `changed`), `SOLR-7323-TESTING.md` (author's unrun note, left in place).
- Verdict: Ready for review (code read only; nothing compiled or run)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

No defects found. The message and test were checked against the code:

- verified: `configSet` (local, `locateInstanceDir` line 330) and `configSetBase` (field, line 57) are both in scope for the message. The exception type and status are unchanged (SERVER_ERROR), as the author intended.
- verified: `configSetBaseDir` is the real `solr.xml` key (`SolrXmlConfig.java:361`). The default is `solrHome.resolve("configsets")` (`NodeConfig.java:662`), so "defaults to SOLR_HOME/configsets" is accurate.
- verified: `locateInstanceDir` is `protected` in the same package as the test, so the test can call it. `CoreDescriptor` is a non-final class (`CoreDescriptor.java:47`) and `getConfigSet()` is public, so Mockito can stub it (author's guess 1). The test's static `configSetBase` and the `FileSystemConfigSetService(Path)` constructor match.
- verified: no other `FileSystemConfigSetService` message text is asserted in `solr/core/src/test` by this change. The author searched that tree. Other modules were not searched (TESTING guess 2). Not re-searched here.
- LOW: the final string literal in the new message runs past 100 columns. google-java-format does not split string literals, so spotless should not fail on it, but it is out of the file's usual width.
- LOW (author's own question, not patched): `SERVER_ERROR` vs `BAD_REQUEST`. A missing configSet is arguably a client error. The author left the status alone to avoid a behavior change. Left as is.

## Not checked
- Nothing compiled, formatted, or run. No Gradle, no tests.
- Other modules' tests asserting the old message text (TESTING guess 2). Only `solr/core/src/test` was looked at.
