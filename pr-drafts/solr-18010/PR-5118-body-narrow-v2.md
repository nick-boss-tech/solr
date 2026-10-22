🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**Each standalone save writes Solr's internal version marker into security.json, and it opens the file before it builds the content.**

The ticket reports a corrupted security.json after a role change in the Admin UI. The Admin UI sends one request per granted permission without waiting for each response ([security.js L1248-L1280](https://github.com/apache/solr/blob/7892348272/solr/webapp/web/js/angular/controllers/security.js#L1248-L1280)). #5065, now on main, runs every request to the standalone security handler one at a time, GET included ([SecurityConfHandlerLocal.java L52-L56](https://github.com/apache/solr/blob/7892348272/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L52-L56)). That covers the reported path.

Each save still stores an empty-string key `{"v":0}` in every section ([SecurityConfHandler.java L147-L148](https://github.com/apache/solr/blob/7892348272/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandler.java#L147-L148)). The ticket's example file shows that key in both sections. The write also opens the file before it serializes the content ([SecurityConfHandlerLocal.java L99-L100](https://github.com/apache/solr/blob/7892348272/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L99-L100)), so the file is empty while the content is being built.

## What this change does

**The marker is no longer written to standalone files, and the content is built before the file is opened. The file is still written in place.**

- The stored content is copied, the marker is removed from each section, and the content is serialized. Only then is the file opened and written ([this change, L94-L110](https://github.com/nick-boss-tech/solr/blob/e81e5e28e90a46f5b84d0e4d746ee750b39bc846/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L94-L110)).
- `CoreContainer.readVersion` ([L611-L617](https://github.com/apache/solr/blob/7892348272/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L611-L617)) reads the marker only to skip a reload for a positive version. Standalone edits wrote 0, so reloads do not change.
- ZooKeeper is unchanged. Its writes use the znode version, not the stored marker.
- The file is not replaced, so symbolic links, bind mounts, read-only directories and file owners behave as on main.

## Choice for maintainers

**Should standalone security.json keep the empty-string version entry?**

- Keep it: the saved file and GET output stay as they are, including the entry the ticket's example shows.
- Remove it (this PR): the saved file and GET output no longer include it.

Keep it if the saved file's shape is part of the API. Remove it if the entry is internal. Nothing in the repository reads it for standalone files, and ZooKeeper keeps its own version, so removing it changes no reload or write.

## Proof

- On main, with this PR's tests, `SecurityConfHandlerTest.testConcurrentPersistConfLeavesOneWholeDocument` and `BasicAuthStandaloneTest.testBasicAuth` fail. Both pass with this change.
- At `e81e5e28e9`, the changelog check, tidy, the Error Prone compile, the four focused tests and the module check pass.

## Limits

- A crash during the write can leave a truncated file. We have not checked what Solr does at startup with one.
- Two writes that overlap inside the file write can still leave a mixed file. #5065 prevents this for HTTP requests. Direct calls to `persistConf` are not covered.
- The v2 Users, Roles and Permissions endpoints from #4916 call `editSecurityConfig` and skip the handler lock. #4916 is open. As Amrit Sarkar noted on the ticket on 2026-10-13, whichever of #4916 and #5065 merges second must move the lock into `editSecurityConfig`. This change does not do that.
- Two Solr processes sharing one SOLR_HOME are not coordinated.
- Nothing is fsynced.

Changelog: [changelog/unreleased/SOLR-18010-security-json-version-metadata.yml](https://github.com/nick-boss-tech/solr/blob/e81e5e28e90a46f5b84d0e4d746ee750b39bc846/changelog/unreleased/SOLR-18010-security-json-version-metadata.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
