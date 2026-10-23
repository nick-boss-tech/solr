🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**A reader can see an empty or partial `security.json`, and a crash mid-write leaves one behind.** [`persistConf`](https://github.com/nick-boss-tech/solr/blob/63f2459f3b58861de8575ecbfbefc38281482f3a/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L93-L109) at the merge base opens the file with truncation before it writes the new content. [#5065](https://github.com/apache/solr/pull/5065) serializes edits inside one process. It does not protect the file on disk.

## What this change does

**An edit writes a temp file, syncs it, and renames it over `security.json`.** Readers see the old file or the new one. The new file keeps the owner, group and mode ([code](https://github.com/nick-boss-tech/solr/blob/46869c4d8b/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L127-L178)).

- The temp file is owner-only until the old mode is restored, so it is never wider than the file it replaces.
- A failed write, sync or permission copy fails the edit and keeps the old file.
- The file is written in place, with a warning, for a symlink, a hard-linked file, a file system without POSIX attributes, a read-only SOLR_HOME, a refused move (for example a bind-mounted file), a file owned by another user, and a group that cannot be set.
- ACLs and SELinux labels are not kept. See the first choice below.
- The version marker stays.

Other projects use a similar pattern. ZooKeeper's [`AtomicFileOutputStream`](https://github.com/apache/zookeeper/blob/master/zookeeper-server/src/main/java/org/apache/zookeeper/common/AtomicFileOutputStream.java) syncs, then renames. Kafka's [`atomicMoveWithFallback`](https://github.com/apache/kafka/blob/461c5cfe056db0951d9b74f5adc45973670404d7/clients/src/main/java/org/apache/kafka/common/utils/Utils.java) renames, then syncs the parent directory.

## Choices to check

**Renaming over `security.json` drops its ACLs and SELinux labels. Is that acceptable?**

Main writes this file in place, so a crash can truncate it. A rename cannot leave a partial file. The cost is that the new file is a new inode, and Java on Linux cannot read or copy ACLs or SELinux labels. The [reference guide](https://github.com/nick-boss-tech/solr/blob/46869c4d8b/solr/solr-ref-guide/modules/deployment-guide/pages/authentication-and-authorization-plugins.adoc) says so.

- **Rename (implemented).** No partial file after a crash. An ACL or custom label is set again after each edit.
- **Write in place (main).** Labels are kept. A crash during the write can truncate the file.

Decision ladder:

1. Does `security.json` have an ACL or a custom SELinux label? Check with `getfacl` and `ls -Z`. If no, rename costs nothing.
2. If yes, can a deploy step set it again after each edit? If yes, rename and document that step.
3. If no, write in place, and accept that a crash can truncate the file.

**Recommendation:** rename. A partial file after a crash is worse than a label that has to be set again, and rung 2 covers setups that need one.

Is losing ACLs and SELinux labels after an edit acceptable, with the step documented?

**When an owner, a group or a move blocks the rename, should the edit write in place or fail?**

Bind-mounted files, files owned by another user, and groups that cannot be set all save on main today, because main writes in place. The fallback brings back main's truncation risk, but only for those files.

Decision ladder:

1. Does the setup save on main today? If yes, failing the edit breaks a working deployment. Bind-mounted and other-owner files do.
2. Does the in-place write add risk beyond main? No. It is the write main makes, and its risk is a crash or a full disk during that write.
3. If you would rather break those setups than keep that risk, fail the edit instead.

**Recommendation:** keep the fallback, with the logged warning. Those setups keep working, and their risk is no worse than main.

Should these edits keep the in-place fallback, or fail?

## Proof

**The temp-file test fails on the previous head and passes on this one** (head `46869c4d8b`, 2026-10-23; production code unchanged since `d8388c6519`).

- `SecurityConfHandlerTest`: 7 of 8 pass. The hard-link test skips on the NFS build host and passes on local disk. The temp-file test fails on `c34abaec94`, where the temp file is `0644` while it is written. On main, 2 of 6 fail, the two concurrency tests.
- `SecurityConfHandlerFailurePathsTest`: 8 of 8 pass. It makes each failure step fail through a test file system that wraps the default one: the temp-file sync, the directory sync, the move, the permission copy, the owner check, and the group copy with its failure. Removing the temp-file sync, the owner check and the group copy from production fails 6 of the 8 tests. The other two, the permission copy and the refused move, were not touched by that change.
- `SecurityConfHandlerReaderTest`: 1 of 1. `BasicAuthStandaloneTest`: 2 of 2.
- Outside the suite: kill -9 during 8 MB writes, 100 trials: 52 old, 48 new, 0 torn. Main's in-place write under the same window: 13 of 100 torn or empty. Disk full, with every inode in use or every byte in use: the edit fails, and the 1 MB file is unchanged and parses. Symlink: the edit writes through the link. Bind mount: the rename is refused, the in-place fallback writes, and the mounted file gets the new content.

## Limits

**Not covered: power loss, a real group change, the symlink case, and Windows.**

- The group copy is tested with a fake group name in the test file system. The test host has one group, so a real group change on a multi-group host is not run here.
- Power loss is not tested. The kill -9 runs cannot show it, because the kernel keeps unsynced data after a process dies.
- A kill during a write leaves `security.json.<uuid>.tmp` behind (75 of 100 kills in the run above). It is not removed, since a pattern delete could remove an unrelated file. It is owner-only when it replaces an existing file.
- The symlink case runs outside the suite. The test policy grants `LinkPermission "hard"` only. Leaving the policy as it is is the recommendation.
- `SecurityConfHandlerReaderTest` runs without Lucene's Windows-like test filesystem, which fails when a file is replaced while another handle is open. On NFS a read can fail with a stale handle during a replace. The test skips those reads, and a production reader gets an error, not partial content.
- Two processes sharing one SOLR_HOME are not coordinated, as with #5065.
- Windows writes in place, as on main. Not run on this head.

Changelog: [changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml](https://github.com/nick-boss-tech/solr/blob/46869c4d8b/changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
