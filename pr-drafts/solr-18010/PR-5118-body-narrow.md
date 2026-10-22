🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**Standalone security.json keeps Solr's internal version marker, and the file is opened before its content is built, so it is empty while the content is built.**

The Admin UI sends one request per granted permission without waiting for responses ([security.js L1248-L1285](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/webapp/web/js/angular/controllers/security.js#L1248-L1285)). #5065 on main already serializes standalone edits within one process ([SecurityConfHandlerLocal.java L52-L56](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L52-L56)). Each save still stores an empty-string key `{"v":0}` in every section ([SecurityConfHandler.java L147-L148](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandler.java#L147-L148)). The write opens the file, then serializes the content ([SecurityConfHandlerLocal.java L99-L100](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L99-L100)). Two overlapping writes can therefore leave one document followed by the tail of another. Links point to merge base `4422d26ac7`.

## What this change does

**The marker is removed from the saved file, and the content is built before the file is opened. The write stays in place, as on main.**

- The stored content is copied, the marker is removed from each section, and the content is serialized. Only then is the file opened and written ([head L94-L110](https://github.com/nick-boss-tech/solr/blob/e81e5e28e90a46f5b84d0e4d746ee750b39bc846/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L94-L110)).
- `CoreContainer.readVersion` ([L611-L617](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L611-L617)) reads the marker only to skip a reload for a positive version. Standalone edits wrote 0, so reloads do not change.
- ZooKeeper is unchanged. Its writes use the znode version, not the stored marker.
- Symbolic links, bind-mounted files, read-only directories and file owners behave as on main, because the file is not replaced.

## Choice for maintainers

**Remove the marker from standalone files, or keep the saved shape the same as before.** After an edit, a GET on a standalone file no longer shows the empty-string entry. Nothing in the repository reads it for standalone files. This PR removes it. Should standalone files keep the same shape as the ZooKeeper copy?

## Proof

- Main with this PR's tests: `SecurityConfHandlerTest.testConcurrentPersistConfLeavesOneWholeDocument` fails ("not a single JSON document"), and `BasicAuthStandaloneTest.testBasicAuth` fails (internal metadata in the file).
- This head: `SecurityConfHandlerTest` 2 tests and `BasicAuthStandaloneTest` 2 tests, 0 failures. Changelog validation, tidy, the Error Prone compile and `:solr:core:check -x test` pass at `e81e5e28e9`.
- `testConcurrentPersistConfLeavesOneWholeDocument` holds the first write during serialization and checks that the file is one document. It pins the order of serialization and opening.

## Limits

**A crash in the middle of the write can still leave a truncated security.json.**

- Writing in place truncates the file first. A process crash during the write leaves a partial file. We have not measured what Solr does at startup with a truncated file.
- Two Solr processes sharing one SOLR_HOME are not coordinated, the same limit as #5065.
- Nothing is fsynced.

Changelog: [changelog/unreleased/SOLR-18010-security-json-version-metadata.yml](https://github.com/nick-boss-tech/solr/blob/e81e5e28e90a46f5b84d0e4d746ee750b39bc846/changelog/unreleased/SOLR-18010-security-json-version-metadata.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
