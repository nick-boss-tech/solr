🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**A reader can see an empty or partial `security.json`, and a crash mid-write leaves one behind.** [`persistConf`](https://github.com/nick-boss-tech/solr/blob/63f2459f3b58861de8575ecbfbefc38281482f3a/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L93-L109) at the merge base opens the file with truncation before it writes the new content. [#5065](https://github.com/apache/solr/pull/5065) serializes edits inside one process. It does not protect the file on disk.

## What this change does

**An edit writes a temp file, syncs it, and renames it over `security.json`.** Readers see the old file or the new one. The new file keeps the owner, group and mode ([code](https://github.com/nick-boss-tech/solr/blob/d8388c6519/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L127-L178)).

- The temp file is owner-only until the old mode is restored, so it is never wider than the file it replaces.
- A failed write, sync or permission copy fails the edit and keeps the old file.
- The file is written in place, with a warning, for a symlink, a hard-linked file, a file system without POSIX attributes, a read-only SOLR_HOME, a refused move (for example a bind-mounted file), a file owned by another user, and a group that cannot be set.
- ACLs and SELinux labels are not kept. See the choice below.
- The version marker stays.

Other projects use a similar pattern. ZooKeeper's [`AtomicFileOutputStream`](https://github.com/apache/zookeeper/blob/master/zookeeper-server/src/main/java/org/apache/zookeeper/common/AtomicFileOutputStream.java) syncs, then renames. Kafka's [`atomicMoveWithFallback`](https://github.com/apache/kafka/blob/461c5cfe056db0951d9b74f5adc45973670404d7/clients/src/main/java/org/apache/kafka/common/utils/Utils.java) renames, then syncs the parent directory.

## A choice to check

**Renaming over `security.json` drops its ACLs and SELinux labels. Is that acceptable?**

Context: main writes this file in place, so a crash can truncate it. A rename cannot leave a partial file, but the new file is a new inode, and Java on Linux cannot read or copy ACLs or SELinux labels. The [reference guide](https://github.com/nick-boss-tech/solr/blob/d8388c6519/solr/solr-ref-guide/modules/deployment-guide/pages/authentication-and-authorization-plugins.adoc) says so.

- **Rename (implemented).** No partial file after a crash. An ACL or custom label must be set again after each edit.
- **Write in place (main).** Keeps everything. A crash during the write can truncate the file, which is the bug this PR fixes.

Decision ladder:

1. Does `security.json` have an ACL or a custom SELinux label? Check with `getfacl` and `ls -Z`. If no, rename costs nothing.
2. If yes, can an admin set it again after each edit, for example in a deploy script? If yes, rename and document the step.
3. If no, the file has to be written in place, and a crash can truncate it again.

**Recommendation:** rename. A partial file after a crash is worse than an admin setting a label again, and rung 2 covers the rest.

Is losing ACLs and SELinux labels after an edit acceptable?

## Proof

**The temp-file test fails on the previous head and passes on this one** (`d8388c6519`, 2026-10-23).

- `SecurityConfHandlerTest`: 7 of 8 pass on this head. The hard-link test skips on the NFS build host and passes on local disk. The temp-file test fails on `c34abaec94`, where the temp file is `0644` while it is written. On main, 2 of 6 fail, the two concurrency tests.
- `SecurityConfHandlerReaderTest`: 1 of 1. `BasicAuthStandaloneTest`: 2 of 2.
- Failure paths: branch [`solr-18010-seams-3`](https://github.com/nick-boss-tech/solr/compare/solr-18010-submit..solr-18010-seams-3) adds package-private hooks for the sync, owner, permission-copy, move and directory-sync steps, with 9 seam tests. On that branch, built on this head, all 9 pass and `check -x test` passes. They include a failed file sync (the edit fails, the move is not attempted, the old file stays), the sync-before-move order, and a failed directory sync after the move (the edit still succeeds).
- Outside the suite, on this head: kill -9 during 8 MB writes, 100 trials: 52 old, 48 new, 0 torn. Main's in-place write under the same window: 13 of 100 torn or empty. Disk full, with every inode in use or every byte in use: the edit fails, and the 1 MB file is unchanged and parses. Symlink: the edit writes through the link. Bind mount: the rename is refused, the in-place fallback writes, and the mounted file gets the new content.

## Limits

**Not covered by the suite: the group copy and failure paths.**

- The group copy runs in the suite and the group stays the same, but the test host has one group, so a dropped copy would not show.
- Sync, directory-sync, owner, group, permission-copy and refused-move failures are tested only on the seams branch. Ordinary file operations cannot make most of those steps fail, so tests would need package-private hooks in production code. Adding hooks only for tests was judged not viable, so this PR has none. The branch can be opened as a follow-up if maintainers want those tests.
- `SecurityConfHandlerReaderTest` runs without Lucene's Windows-like test filesystem, which fails when a file is replaced while another handle is open. On NFS, a read can also fail with a stale handle during a replace. The test skips those reads, and a production reader gets an error, not partial content.
- Two processes sharing one SOLR_HOME are not coordinated, as with #5065.
- Power loss is not tested. The kill -9 runs cannot show it, because the kernel keeps unsynced data after a process dies.
- A kill during a write leaves `security.json.<uuid>.tmp` behind (75 of 100 kills in the run above). It is not removed, since a pattern delete could remove an unrelated file. It is owner-only when it replaces an existing file.
- The symlink case runs outside the suite. The test policy grants `LinkPermission "hard"` only. Leaving the policy as it is is the recommendation.
- Windows writes in place, as on main. Not run on this head.

Changelog: [changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml](https://github.com/nick-boss-tech/solr/blob/d8388c6519/changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
