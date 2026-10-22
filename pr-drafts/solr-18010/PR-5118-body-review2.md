🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

Standalone `security.json` is written in place. Main truncates the file before it serializes the new content, so a reader can see an empty or partly written file, and a crash mid-write can leave one. #5065 (merged) serializes edits within one process, so this change is about the file on disk, not the lock.

## What this change does

An edit writes a temporary file in the same folder, syncs it, and moves it over `security.json`. A reader sees the old file or the new one.

- The replacement keeps the existing owner, group and permission bits.
- The in-place write is kept, with a logged warning, for a symbolic link, a file system without POSIX attributes, a read-only SOLR_HOME, a refused move (for example a bind-mounted file), a file owned by another user, or a group that cannot be set on the temporary file.
- The version marker is kept, as on main. `CoreContainer.readVersion` and the ZooKeeper handler are unchanged.

## Behavior choices

- **A failed write, sync, permission copy, or temporary-file create fails the edit and keeps the existing file.** The only failure that falls back to an in-place write is a SOLR_HOME that denies writes, because main writes there in place today and that setup keeps working. An in-place write after the file system runs out of space or inodes would truncate `security.json` first. In a probe on a 2 MB tmpfs with every inode in use, the in-place fallback left a 1 MB file as 2 MB of unparseable JSON. Now the edit fails and the file is unchanged.
- **The owner, move and group fallbacks still write in place.** A file owned by another user, a refused move and a group that cannot be set are written in place because main already writes those files that way. On a full disk such an edit can still truncate the file. Failing these edits instead would break setups that work today.

## Proof

Head `d1bf9b1407` (tree `5f2625bd3c`), Linux, JDK 21.

- Gate: tidy (one line wrapped), `:solr:core:check -x test` (includes the Error Prone compile), and the focused tests `SecurityConfHandlerTest` 7 of 7 and `BasicAuthStandaloneTest` 2 of 2, with 0 failures and 0 skips.
- Inode probe on a 2 MB tmpfs, run with `enospc` against the head before this commit (`691eba654c`) and against this head. With every inode in use, the old head left the file as 2,097,152 bytes of unparseable JSON, and this head leaves the original 1,000,084 bytes unchanged. With inodes free, both heads fail on the write and leave the file unchanged.
- Earlier out-of-suite runs, on `691eba654c`; the write path they test is unchanged by this commit. Upstream main compared with the same tests: `testReaderNeverSeesPartialSecurityJsonDuringEdits` fails ("reader saw a partial security.json of 0 bytes") and `testConcurrentPersistConfLeavesOneWholeDocument` fails ("not a single JSON document"). The other new tests pass on main, because they pin behavior main already has. 150 `kill -9` runs during repeated 8 MB writes gave 0 torn files with this change, against 23 of 150 for the in-place write. A cross-process reader made 2,000 reads during repeated writes: 0 torn with this change, 1,843 torn in place. A single-file bind mount refuses the move, falls back in place, and the mounted file gets the new content.

## Limits

- The group path is not exercised here. The Linux test host has one group, so a group change cannot be produced, and the suite has no test for it. The path runs only where the process belongs to more than one group.
- Two Solr processes sharing one SOLR_HOME are not coordinated, the same limit as #5065.
- The sync calls are not tested, so power loss is not covered.
- A kill between creating and moving the temporary file leaves `security.json.<uuid>.tmp` in SOLR_HOME. Solr does not read it, and the write does not remove it, since a pattern-based delete could remove an unrelated file.
- Not covered by the suite: sync, directory-sync, owner-mismatch, group-set and permission-copy failures, and a refused move. The out-of-suite runs above and review cover them.
- The replacement is a new file. Hard links stop sharing content after an edit, and ACLs and SELinux labels are not carried over.
- Windows writes in place, as on main. Not run here.

## Question for maintainers

The suite policy grants `LinkPermission "hard"` only. A symbolic-link test would need `"symbolic"`, so the link case is checked outside the suite only. Should `"symbolic"` be added to `solr-tests.policy`, or should the link case stay untested?

Changelog: `changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml`

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
