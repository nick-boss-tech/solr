🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**A reader can see an empty or partly written `security.json`, and a crash mid-write leaves one behind.** Main opens the file with truncation before it serializes the new content. The code is in [`persistConf`](https://github.com/nick-boss-tech/solr/blob/63f2459f3b58861de8575ecbfbefc38281482f3a/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L93-L109) at the merge base. [#5065](https://github.com/apache/solr/pull/5065) serializes edits inside one process. This change is about the file on disk.

## What this change does

**An edit writes a temporary file, syncs it, and moves it over `security.json`.** A reader sees the old file or the new one. The new file keeps the old owner, group and permission bits ([code](https://github.com/nick-boss-tech/solr/blob/d1bf9b140729c4b196781d65c0f7c8ca0f678c27/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L121-L171)).

Behavior changes:

- A failed write, sync or permission copy fails the edit and keeps the old file. Main would leave a truncated file.
- The in-place write, with a logged warning, is kept only for: a symbolic link, a file system without POSIX attributes, a SOLR_HOME that denies writes, a refused move (for example a bind-mounted file), a file owned by another user, and a group that cannot be set.
- The version marker stays, as on main.

## Proof

**Both concurrency tests fail on main and pass on this head.** Verified 2026-10-22.

- Base `63f2459f3b`, main's `SecurityConfHandlerLocal.java`, `SecurityConfHandlerTest` 6 of 6 (the predicate test needs the new helper): `testReaderNeverSeesPartialSecurityJsonDuringEdits` fails ("reader saw a partial security.json of 0 bytes") and `testConcurrentPersistConfLeavesOneWholeDocument` fails ("not a single JSON document"). The other four pass on main because they pin behavior main already has.
- Head `d1bf9b1407`: `SecurityConfHandlerTest` 7 of 7, `BasicAuthStandaloneTest` 2 of 2, no failures or skips.

**Real conditions, run on this head (2026-10-22, outside the suite).**

- Kill -9 during 8 MB writes, 100 trials with a 2.5 s window: 50 old, 50 new, 0 torn. The in-place write (main's behavior) under the same window: 40 old, 47 new, 13 torn or empty.
- Disk full with every inode in use (2 MB tmpfs): the earlier fallback left a 1 MB file as 2 MB of unparseable JSON. Now the edit fails and the file is unchanged.
- A second process reads 2,000 times during repeated writes: 2,000 complete, 0 torn. The in-place control, from an earlier run: 1,843 torn or empty.
- Symlink: the edit writes through the link, which stays a link, and the target gets the new content.
- Bind-mounted file: the rename is refused (Device or resource busy), the in-place fallback writes, and the mounted file gets the new content.

**Test seams on Option B.** Branch `solr-18010-atomic-posix` (head `9be90e623b`) has the same write path with test seams added to production code. Commit [`e66ec9d3ea`](https://github.com/nick-boss-tech/solr/commit/e66ec9d3ea04ba288a11cb7165efdf6178f51ee9) adds six package-private hook methods to [`SecurityConfHandlerLocal`](https://github.com/nick-boss-tech/solr/blob/9be90e623b60d024c575308d2ad1ee20735731ec/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L190-L218). Commit [`b12895b4e2`](https://github.com/nick-boss-tech/solr/commit/b12895b4e2) puts nine seam tests in their own class. This head removes the hooks.

- Option B's full gate passed: `SecurityConfHandlerTest` 6 of 6, `BasicAuthStandaloneTest` 2 of 2, and 17 of 17 with the seam tests. The seams let the suite make the sync, owner, permission-copy and move steps fail. On this head those steps are reviewed only.
- Option B does not have the group copy or the narrowed create fallback. Those two changes were checked on this head only: the disk-full run above, and no test for the group, since the test host has one group.
- The two branches differ in more than the hooks, so [the two-dot compare](https://github.com/nick-boss-tech/solr/compare/solr-18010-atomic-posix..solr-18010-submit) also lists main changes that landed after Option B's base. The `security.json` files are `SecurityConfHandlerLocal.java`, `SecurityConfHandlerTest.java`, and `SecurityConfHandlerLocalSeamTest.java`, which exists only on Option B.

## A choice to check

**Keep the in-place fallback when a file owner, group or refused move blocks the atomic replace, or fail those edits?** We recommend keeping it.

Context: main already writes these files in place, so bind-mounted files and files owned by another user save today. Failing those edits would stop them.

Decision ladder:

1. Does the setup save on main today? If yes, failing the edit breaks it. All three cases save on main.
2. What does the in-place write add? It is the write main makes. Its extra risk is a full disk during the rewrite, which can truncate the file.
3. If that risk is acceptable to keep working setups, keep the fallback. If you would rather break those setups than risk a truncation on a full disk, fail the edit.

We implemented the fallback, with a logged warning. Was keeping it the right call?

## Limits

- The group path is not run here, because the test host has one group. No suite test covers it.
- Not covered by the suite: sync, directory-sync, owner, group and permission-copy failures, and a refused move. The runs above and review cover them.
- Two Solr processes sharing one SOLR_HOME are not coordinated, the same limit as #5065.
- Power loss is not covered, since the sync calls are not tested.
- A kill between creating and moving the temporary file leaves `security.json.<uuid>.tmp` in SOLR_HOME (about one kill in three in our runs). Solr does not read it, and does not remove it, since a pattern-based delete could remove an unrelated file.
- The file is replaced, not rewritten: hard links stop sharing content, and ACLs and SELinux labels are not carried over.
- The symbolic-link case runs outside the suite. The test policy grants `LinkPermission "hard"` only; adding `"symbolic"` would let a test cover it. We recommend leaving the policy as it is.
- Windows writes in place, as on main. A Windows run passed on an earlier tree; this head has not been run there.

Changelog: [changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml](https://github.com/nick-boss-tech/solr/blob/d1bf9b140729c4b196781d65c0f7c8ca0f678c27/changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
