🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**A reader can see an empty or partial `security.json`, and a crash mid-write leaves one.** [`persistConf`](https://github.com/nick-boss-tech/solr/blob/63f2459f3b58861de8575ecbfbefc38281482f3a/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L93-L109) opens the file with truncation before it serializes the new content. [#5065](https://github.com/apache/solr/pull/5065) serializes edits in one process. This PR is about the file on disk.

## What this change does

**An edit writes a temp file, syncs it, and renames it over `security.json`.** The folder is synced too. Readers see the old file or the new one. The new file keeps the owner, group and mode ([code](https://github.com/nick-boss-tech/solr/blob/e7d1c336f470d3f3dbd97cd9ddb75b9a045a4f99/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L121-L171)).

Behavior changes:

- A failed write, sync or permission copy fails the edit and keeps the old file. Main leaves a truncated file.
- Main's in-place write, with a warning, is kept for a symlink, a file system without POSIX attributes, a read-only SOLR_HOME, a refused move, a file owned by another user, and a group that cannot be set.
- The version marker stays.

Other projects use the same pattern. ZooKeeper's [`AtomicFileOutputStream`](https://github.com/apache/zookeeper/blob/master/zookeeper-server/src/main/java/org/apache/zookeeper/common/AtomicFileOutputStream.java) syncs, then renames. Kafka's [`atomicMoveWithFallback`](https://github.com/apache/kafka/blob/461c5cfe056db0951d9b74f5adc45973670404d7/clients/src/main/java/org/apache/kafka/common/utils/Utils.java) renames, then syncs the parent directory. Solr's [`DistribFileStore`](https://github.com/nick-boss-tech/solr/blob/63f2459f3b58861de8575ecbfbefc38281482f3a/solr/core/src/java/org/apache/solr/filestore/DistribFileStore.java#L615-L621) syncs the files it writes.

## A choice to check

**Keep the in-place fallback for owner, group and refused-move cases, or fail those edits?** We recommend keeping it.

1. Those setups save on main today, such as bind-mounted files and files owned by another user. Failing the edit would stop them.
2. The fallback is main's write. Its extra risk is a full disk during the rewrite, which can truncate the file. Main has that risk for every edit.
3. If you would rather break those setups than take that risk, fail the edit instead.

Was keeping the fallback the right call?

## Proof

- **Fails on main, passes on this head** (`e7d1c336f4`, 2026-10-22). On main `63f2459f3b`, `SecurityConfHandlerTest` 6 of 6 (the predicate test needs the new helper): `testReaderNeverSeesPartialSecurityJsonDuringEdits` fails ("reader saw a partial security.json of 0 bytes") and `testConcurrentPersistConfLeavesOneWholeDocument` fails ("not a single JSON document"). On this head, `SecurityConfHandlerTest` 7 of 7 and `BasicAuthStandaloneTest` 2 of 2.
- **Failure paths, with seams.** Branch [`solr-18010-seams`](https://github.com/nick-boss-tech/solr/compare/solr-18010-submit..solr-18010-seams) adds package-private hooks for the sync, owner, permission-copy, move and directory-sync steps, plus `SecurityConfHandlerLocalSeamTest` with 9 tests. On that branch all 9 seam tests pass, along with the 7 suite tests and `check -x test`. An earlier run that wrote in place every time failed the reader test.
- **Outside the suite, on this head.** Kill -9 during 8 MB writes, 100 trials: 50 old, 50 new, 0 torn. The in-place write under the same window: 13 of 100 torn or empty. Disk full with every inode in use: the edit fails and the 1 MB file is unchanged. The earlier fallback left 2 MB of unparseable JSON. A second process made 2,000 reads during writes: 0 torn. The in-place write, from an earlier run, had 1,843 torn or empty. Symlink: the edit writes through the link. Bind-mounted file: the rename is refused, the in-place fallback writes, and the mounted file gets the new content.

## Limits

- No test covers the group path. The test host has one group.
- Sync, owner, permission-copy and move failures are tested only on the seams branch. This PR has no seams.
- Two Solr processes sharing one SOLR_HOME are not coordinated, as with #5065.
- Power loss is not covered, since sync is untested.
- A kill mid-write leaves `security.json.<uuid>.tmp`, about one kill in three. It is not removed, since a pattern delete could remove an unrelated file.
- The file is replaced, not rewritten. Hard links stop sharing content, and ACLs and SELinux labels are not kept.
- On NFS, a read can fail with a stale handle while the file is replaced. The reader test skips those reads. A production reader would see an error, not partial content.
- `SecurityConfHandlerTest` runs without Lucene's Windows-like test filesystem. That filesystem fails its bookkeeping when a file is renamed over while open, which the reader test does on purpose.
- The symlink case is outside the suite. The test policy grants `LinkPermission "hard"` only. We recommend leaving the policy as it is.
- Windows writes in place, as on main. Not run on this head.

Changelog: [changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml](https://github.com/nick-boss-tech/solr/blob/e7d1c336f470d3f3dbd97cd9ddb75b9a045a4f99/changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
