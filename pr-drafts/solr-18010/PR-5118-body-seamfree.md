🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

Standalone `security.json` is written in place. Main truncates the file before it serializes the new content, so a reader can see an empty or partly written file, and a crash mid-write can leave one. #5065 (merged) serializes edits within one process, so this change is about the file on disk, not the lock.

## What this change does

An edit writes a temporary file in the same folder, syncs it, and moves it over `security.json`. A reader sees the old file or the new one.

- The in-place write is kept, with a logged warning, for a symbolic link, a file system without POSIX attributes, a read-only SOLR_HOME, a refused move (for example a bind-mounted file), or a file owned by another user.
- An edit keeps the existing owner and permission bits.
- The version marker is kept, as on main. `CoreContainer.readVersion` and the ZooKeeper handler are unchanged.

## Choice for maintainers

**A failed write, sync or permission copy fails the edit and keeps the existing file.** The earlier design fell back to an in-place write on those failures. On a full disk that truncates `security.json`: in our test a 1 MB file became 2 MB of unparseable JSON. Failing the edit is the safer default, at the cost of reporting failure. Please confirm.

## Proof

Head `691eba654c` (tree `dd22fc09f9`), Linux, JDK 21.

- Gate: tidy and `:solr:core:check -x test` pass, and the Error Prone compile passes. Focused: `SecurityConfHandlerTest` 7 of 7 and `BasicAuthStandaloneTest` 2 of 2, 0 failures.
- Upstream main (`63f2459f3b`) with the same tests and main's `SecurityConfHandlerLocal.java`: `testReaderNeverSeesPartialSecurityJsonDuringEdits` fails ("reader saw a partial security.json of 0 bytes") and `testConcurrentPersistConfLeavesOneWholeDocument` fails ("not a single JSON document"). The other new tests pass on main, because they pin behavior main already has.
- Mutation on this branch that always writes in place: the reader test fails the same way.
- Outside the suite, with real conditions: 150 `kill -9` runs during repeated 8 MB writes gave 0 torn files with this change, against 23 of 150 for the in-place write. A cross-process reader made 2,000 reads during repeated writes: 0 torn with this change, 1,843 torn in place. A disk-full edit on a 2 MB tmpfs fails and leaves the 1 MB file intact. A single-file bind mount (Docker style) refuses the move, falls back in place, and the mounted file gets the new content.

## Limits

- Two Solr processes sharing one SOLR_HOME are not coordinated, the same limit as #5065.
- The sync calls are not tested, so power loss is not covered.
- A kill between creating and moving the temporary file leaves `security.json.<uuid>.tmp` in SOLR_HOME. Solr does not read it, and the write does not remove it, since a pattern-based delete could remove an unrelated file.
- Not covered by the suite: sync, directory-sync, owner-mismatch and permission-copy failures, and a refused move. The out-of-suite runs above or review cover them.
- Windows writes in place, as on main. Not run here.

## Question for maintainers

The suite policy grants `LinkPermission "hard"` only. A symbolic-link test would need `"symbolic"`, so the link case is checked outside the suite only. Should `"symbolic"` be added to `solr-tests.policy`, or should the link case stay untested?

Changelog: `changelog/unreleased/SOLR-18010-security-json-atomic-replace.yml`

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
