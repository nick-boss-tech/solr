🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**A reader can see an empty or partial `security.json`, and a crash mid-write leaves one.** [`persistConf`](https://github.com/nick-boss-tech/solr/blob/63f2459f3b58861de8575ecbfbefc38281482f3a/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L93-L109) at the merge base opens the file with truncation before it writes the new content. [#5065](https://github.com/apache/solr/pull/5065) serializes edits inside one process. It does not protect the file on disk.

## What this change does

**An edit writes a temp file, syncs it, and renames it over `security.json`.** Readers see the old file or the new one. The new file keeps the owner, group and mode ([code](https://github.com/nick-boss-tech/solr/blob/c34abaec94/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L121-L171)).

- A failed write, sync or permission copy fails the edit and keeps the old file.
- The file is written in place, with a warning, for a symlink, a hard-linked file, a file system without POSIX attributes, a read-only SOLR_HOME, a refused move (for example a bind-mounted file), a file owned by another user, and a group that cannot be set.
- ACLs and SELinux labels are not kept. See the choice below.
- The version marker stays.

Other projects do the same. ZooKeeper's [`AtomicFileOutputStream`](https://github.com/apache/zookeeper/blob/master/zookeeper-server/src/main/java/org/apache/zookeeper/common/AtomicFileOutputStream.java) syncs, then renames. Kafka's [`atomicMoveWithFallback`](https://github.com/apache/kafka/blob/461c5cfe056db0951d9b74f5adc45973670404d7/clients/src/main/java/org/apache/kafka/common/utils/Utils.java) renames, then syncs the parent directory.

## A choice to check

**Renaming over `security.json` drops its ACLs and SELinux labels. Is that acceptable?**

A rename is what stops a crash from leaving a partial file. The new file is a new inode, so anything stored on the old one is lost. Java on Linux cannot read or copy ACLs or SELinux labels, so the code cannot keep them.

- **Rename (implemented).** No partial file after a crash. An ACL or custom label must be set again after each edit. The new file gets the directory's defaults.
- **Write in place (main today).** Keeps everything. A crash during the write can truncate the file, which is the bug this PR fixes.

Decision ladder:

1. Does your `security.json` have an ACL or a custom SELinux label? If no, rename costs nothing. Use rename.
2. If yes, can an admin set it again after each edit, for example in a deploy script? If yes, use rename and document the step. This is the recommended path.
3. If no, the file has to be written in place, and a crash can truncate it again. Choose in place only when the label matters more than crash safety.

**Recommendation:** rename. Most installs have no ACL or custom label on this file. The rest can set it again after an edit. A lost label usually shows up as a permission error on the next start, so it is found quickly. The reference guide says the labels are not kept.

Was losing ACLs and SELinux labels after an edit the right call?

## Proof

**The concurrency tests fail on main and pass on this head** (`c34abaec94`, verified 2026-10-22).

- `SecurityConfHandlerTest`: 6 of 7 pass on this head. The hard-link test skips on the NFS build host, passes on local disk, and fails with its check removed. On main, 2 of 6 fail: `testConcurrentPersistConfLeavesOneWholeDocument` ("not a single JSON document") and the reader test, which was in this class before the move ("reader saw a partial security.json of 0 bytes"). The other four pass on main because they pin behavior main already has.
- `SecurityConfHandlerReaderTest`: 1 of 1 on this head. Same test body as on main.
- `BasicAuthStandaloneTest`: 2 of 2 on this head.
- Failure paths: branch [`solr-18010-seams-2`](https://github.com/nick-boss-tech/solr/compare/solr-18010-submit..solr-18010-seams-2) adds package-private hooks for the sync, owner, permission-copy, move and directory-sync steps, with 9 seam tests. All pass there.
- Outside the suite, on this write path: kill -9 during 8 MB writes, 100 trials, 0 torn (an in-place write under the same window: 13 of 100 torn or empty). Disk full with every inode in use: the edit fails and the file is unchanged. A second process made 2,000 reads during writes: 0 torn.

## Limits

**Not covered here: the group path, failure paths outside the seams branch, and Windows.**

- No test covers the group path. The test host has one group.
- `SecurityConfHandlerReaderTest` runs without Lucene's Windows-like test filesystem, which cannot track a file replaced while open. The class has no other tests.
- On NFS, a read can fail with a stale handle during a replace. The reader test skips those reads. A production reader gets an error, not partial content.
- Two processes sharing one SOLR_HOME are not coordinated, as with #5065. Power loss is not covered, since sync is untested.
- A kill mid-write leaves `security.json.<uuid>.tmp`, about one kill in three. It is not removed, since a pattern delete could remove an unrelated file.
- The symlink case runs outside the suite. The test policy grants `LinkPermission "hard"` only. We recommend leaving the policy as it is.
- Windows writes in place, as on main. Not run on this head.

Changelog: [changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml](https://github.com/nick-boss-tech/solr/blob/c34abaec94/changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
