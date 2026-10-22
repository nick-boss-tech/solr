🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**A reader can see an empty or partial `security.json`, and a crash mid-write leaves one.** [`persistConf`](https://github.com/nick-boss-tech/solr/blob/63f2459f3b58861de8575ecbfbefc38281482f3a/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L93-L109) opens the file with truncation before it serializes the new content. [#5065](https://github.com/apache/solr/pull/5065) serializes edits in one process. This PR is about the file on disk.

## What this change does

**An edit writes a temp file, syncs it, and renames it over `security.json`.** The folder is synced too. Readers see the old file or the new one. The new file keeps the owner, group and mode ([code](https://github.com/nick-boss-tech/solr/blob/c34abaec94/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L121-L171)).

Behavior changes:

- A failed write, sync or permission copy fails the edit and keeps the old file. Main leaves a truncated file.
- Main's in-place write, with a warning, is kept for a symlink, a hard-linked file, a file system without POSIX attributes, a read-only SOLR_HOME, a refused move, a file owned by another user, and a group that cannot be set.
- The new file does not keep POSIX ACLs or SELinux labels. Java cannot read or copy them on Linux. The [reference guide](https://github.com/nick-boss-tech/solr/blob/c34abaec94/solr/solr-ref-guide/modules/deployment-guide/pages/authentication-and-authorization-plugins.adoc) says so.
- The version marker stays.

Other projects use the same pattern. ZooKeeper's [`AtomicFileOutputStream`](https://github.com/apache/zookeeper/blob/master/zookeeper-server/src/main/java/org/apache/zookeeper/common/AtomicFileOutputStream.java) syncs, then renames. Kafka's [`atomicMoveWithFallback`](https://github.com/apache/kafka/blob/461c5cfe056db0951d9b74f5adc45973670404d7/clients/src/main/java/org/apache/kafka/common/utils/Utils.java) renames, then syncs the parent directory. Solr's [`DistribFileStore`](https://github.com/nick-boss-tech/solr/blob/63f2459f3b58861de8575ecbfbefc38281482f3a/solr/core/src/java/org/apache/solr/filestore/DistribFileStore.java#L615-L621) syncs the files it writes.

## A choice to check

**Replacing `security.json` by rename drops its ACLs and SELinux labels. Is that acceptable?**

The rename is what makes a crash safe: a reader sees the old file or the new one, never a partial one. But the new file is a new inode, so anything stored on the old one is lost. That includes POSIX ACLs and SELinux labels. Java on Linux cannot read or copy either, so the code cannot keep them.

- **Rename (this PR).** A crash cannot leave a partial file. The cost: an ACL or custom SELinux label has to be set again after an edit. The new file gets the directory's defaults.
- **Write in place (main today).** Keeps everything. A crash during the write can truncate the file, which is the bug this PR fixes.

We recommend rename. Setups that depend on an ACL or a custom label on `security.json` are rare in our view. A lost label usually shows up as a permission error on the next start, so it is found quickly. The reference guide says the labels are not kept.

A smaller version of the same trade is hard links. A hard-linked file is written in place, so its links keep sharing content.

Is losing ACLs and SELinux labels on `security.json` after an edit acceptable?

## Proof

- **Fails on main, passes on this head** (`c34abaec94`). On main `63f2459f3b`, with the same test bodies, `testReaderNeverSeesPartialSecurityJsonDuringEdits` fails ("reader saw a partial security.json of 0 bytes") and `testConcurrentPersistConfLeavesOneWholeDocument` fails ("not a single JSON document"). The other four pass on main because they pin behavior main already has. On this head: `SecurityConfHandlerTest` 6 of 7 (the hard-link test skips on this NFS host), `SecurityConfHandlerReaderTest` 1 of 1, `BasicAuthStandaloneTest` 2 of 2.
- **Hard links, on local disk.** The hard-link test passes. With the check removed it fails. Outside the suite, the link keeps one inode and both names show the new content; without the check, the second name keeps the old content.
- **Failure paths.** Branch [`solr-18010-seams-2`](https://github.com/nick-boss-tech/solr/compare/solr-18010-submit..solr-18010-seams-2) adds package-private hooks for the sync, owner, permission-copy, move and directory-sync steps, and 9 seam tests. All pass there, with the suite tests and `check -x test`.
- **Outside the suite.** Kill -9 during 8 MB writes, 100 trials: 0 torn. An in-place write under the same window: 13 of 100 torn or empty. Disk full with every inode in use: the edit fails and the file is unchanged; the earlier fallback left 2 MB of unparseable JSON. A second process made 2,000 reads during writes: 0 torn. A symlink takes the write through the link. A bind-mounted file gets the new content.

## Limits

- No test covers the group path. The test host has one group.
- Sync, owner, permission-copy and move failures are covered only on the seams branch. This PR has no seams.
- `SecurityConfHandlerReaderTest` runs without Lucene's Windows-like test filesystem, which cannot track a file replaced while open. The class has no other tests.
- On NFS, a read can fail with a stale handle during a replace. The reader test skips those reads. A production reader gets an error, not partial content.
- Two processes sharing one SOLR_HOME are not coordinated, as with #5065. Power loss is not covered, since sync is untested.
- A kill mid-write leaves `security.json.<uuid>.tmp`, about one kill in three. It is not removed, since a pattern delete could remove an unrelated file.
- ACLs and SELinux labels are not kept. The symlink case is outside the suite; the test policy grants `LinkPermission "hard"` only, and we recommend leaving it that way.
- Windows writes in place, as on main. Not run on this head.

Changelog: [changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml](https://github.com/nick-boss-tech/solr/blob/c34abaec94/changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
