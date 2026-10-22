# SOLR-18010: the empty-string version entry, evidence for the maintainer choice (vm3, 2026-10-22)

Pins: main `7892348272`. Code cited by line at that SHA. "Read" means code reading; nothing here was run unless a row says so.

## Where the entry comes from

- `SecurityConfHandler.java` L147-L148: each save writes `{"v": N}` under the empty-string key of the edited section, where N is the expected ZooKeeper version. Introduced by `db2ccb1d41` (2015-08-06, SOLR-7757, "Solr now watches /security.json").
- The entry is a ZooKeeper versioning device. A standalone file has no ZooKeeper version, so its stored value is always 0. That number carries no meaning on standalone.

## Who reads it

- `CoreContainer.readVersion` (L611-L617) reads it at load. The reload skip needs a positive version (L467, L510, L567). A standalone value of 0 never triggers the skip, so removing the entry changes no reload. Read.
- Admin UI (`solr/webapp/web/js/angular/controllers/security.js`): no read of the empty key or of `v`. Checked with `git grep`.
- SolrJ (`solr/solrj/src/java`): no security-section reader.
- Tests (`solr/core/src/test`, `solr/solrj/src/test`): no test reads the empty key. The only `get("")` hits are unrelated highlighter tests.
- #4916 (v2 Users, Roles and Permissions, open): its standalone test has no marker assertions, and its changes to `SecurityConfHandler.java` do not touch the `getMapValue(out, "")` line. Read from the PR patch.

## Who can see it

- GET on a section returns the stored section: `SecurityConfHandlerLocal.getConf` (L81-L88) for standalone, `SecurityConfHandlerZk.getConf` (L61-L69) for ZooKeeper. After an edit, the entry is in the response on both.
- Reference guide: the basic-auth page documents GET endpoints (`/admin/authentication`, `/api/cluster/security/authentication`) only through request examples. No published response contains the entry.
- Reference guide, authentication page L110-L111: tells people editing a ZooKeeper copy to remove version data, and describes the `"v"` entry. Standalone is not mentioned.
- The ticket's example file (SOLR-18010) shows the entry in both sections. The reporter's complaint is the trailing text, not the entry.

## Options

- **Keep the entry (no change).** Saved files and GET output stay the same on both storage forms. A client that reads the entry keeps working. Cost: the saved file keeps an internal value that means nothing on standalone.
- **Remove it from standalone files (this PR's current behavior).** After an edit, the saved file and GET output no longer include it. ZooKeeper keeps it, so the GET output of the two forms differs after an edit. Cost: a visible response change, unless no client reads it.

## What only maintainers can judge

Whether an external client reads the entry from GET output. The repository shows no reader. External code is not visible to us.

## Recommendation

Keep the entry in this PR. It has no effect on reloads, and nothing in the repository reads it on standalone. Removing it is a response change that maintainers should judge on its own, and it does not fix the ticket. That drops the marker test in `BasicAuthStandaloneTest` and its 25 lines.

## What the write-order change buys, stated plainly

With #5065's lock, HTTP requests to the standalone handler already run one at a time, so the order of serialization and opening changes nothing for them (read). The order matters only for direct callers of `persistConf` and for #4916's v2 path, which skips the lock (read, #4916 patch); that path still races, and the order does not fix it. The ordering test (`testConcurrentPersistConfLeavesOneWholeDocument`) calls `persistConf` directly, so it pins the order without covering any HTTP caller.
