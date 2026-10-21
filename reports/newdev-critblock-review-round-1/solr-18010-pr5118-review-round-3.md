# SOLR-18010, PR #5118: review round 3, maintainer blast-radius pass (vm3, 2026-10-21)

Head reviewed: `dfcf50aec0` (round 2). Head after this round: `1b14ce9556`, pushed as a fast-forward to `solr-18010-submit`. Commits: `09c8e5c8f6` (test: pin an edit in a read-only SOLR_HOME), `1b14ce9556` (fix: write security.json in place when the temporary file cannot replace it). Base: apache/main `4422d26ac7`.

PR state from the GitHub API at review time: open, draft, base `c8769bb1c7`. The live body still describes head `571307400b`. No PR opening, flip or body edit was made. The body draft is `pr-drafts/solr-18010/PR-5118-body-round-3.md` for Nick to apply.

## Scope of the review

The diff against apache/main is four files: `SecurityConfHandlerLocal.java`, `SecurityConfHandlerTest.java`, `BasicAuthStandaloneTest.java`, and the changelog. The lock in `SecurityConfHandlerLocal` is #5065's (`1afeee27c4`, already on main), not this PR's. The review covered the write path, what the edit used to do on base, and who reads the file.

## Findings

Ladder rung in brackets. "Probe" means a run outside the suite on JDK 21.0.12.1 here.

1. **Symbolic link replaced (regression).** The head's move turns a symlinked security.json into a regular file. Base wrote through the link, because `Files.newOutputStream` follows it. Probe: after the move, the link is gone and the managed target keeps its old content. Base write: the target is updated and the link stays. [1, 2] Resolved: a symlink is written in place. Not covered by a suite test, see 4.

2. **Read-only SOLR_HOME (regression).** The head creates the temporary file in SOLR_HOME, so an edit fails with `AccessDeniedException` when the directory is read-only and security.json is writable. Base succeeded. The 2026-10-16 vm2 check reached the same result and escalated it; this round settles it inside the pool. [4] Resolved: fall back to the in-place write, which is base behavior. Test `testEditInPlaceWhenSolrHomeIsReadOnly` fails at `dfcf50aec0` with the `AccessDeniedException` (`gates/logs/vm3/solr-18010-r3-before/`) and passes at `1b14ce9556`.

3. **Permission copy failure (silent mode change).** On head a failed copy of the POSIX bits is logged at debug and the edit goes on. The temporary file then keeps the default mode of a created file, while base kept the existing mode. [1] Resolved: the bits are copied whenever the file system has a POSIX view, and a copy failure now goes to the in-place write, which keeps the mode. This answers the PR body's question "should a failed copy stop the edit": it does not stop; it writes in place with the mode kept.

4. **Owner change (silent ownership change).** The move makes the Solr process user the owner of the file; base kept the owner. [1] Resolved: an owner mismatch goes to the in-place write. Not covered by a suite test, because changing a file's owner needs root and the suite runs as a normal user. Verified by reading the code only.

5. **Test policy blocks the symlink test.** The first version of the symlink test failed on `AccessControlException: LinkPermission "symbolic"`, not on the behavior. The suite policy grants only hard links (`gradle/testing/randomization/policies/solr-tests.policy`, line 38; that line came from the general script cleanup `3e47bfb7b4`, with no sign of a deliberate exclusion). [1] Decision: do not widen the suite-wide test policy in this PR. The symlink test was removed (commit `09c8e5c8f6` is amended before it was pushed). The symlink case is covered by the probe in finding 1 and by the code path, not by the suite. The policy question is open for the maintainers and is in the PR body draft.

6. **Windows.** On head, a move over a file another program holds open fails and the edit fails. Now the edit writes in place. [1] Not run here (Linux only).

7. **Single-file bind mount.** On Linux, `rename(2)` onto a mount point is refused with EBUSY (rename(2) man page). Head: the edit fails. Now: the in-place write. [1] Not run here: uid 1000 and no mount rights.

8. **Durability.** The head comment and changelog claimed that a crash leaves the previous document. The write has no fsync before the replace, so a power failure can leave an empty or stale file. [1] Decision: the changelog now claims only a process crash. No fsync is added in this PR. The in-place path does not fsync either, and a durability policy for security.json is a separate change for its own ticket (`TransactionLog.java:627` uses `force(true)`; that is a sibling pattern, not a requirement here).

9. **File mode of a created file.** Probe here: a temporary file created with an explicit directory gets `rw-------`, and the moved file keeps it. The 2026-10-16 vm2 check recorded `rw-r--r--` at head for a new file. The two readings do not agree and are not reconciled here. Reachability decides it: `SecurityConfHandler.doEdit` reads the file, returns "No configuration present" when the key is missing, and only then calls `persistConf`. A created file therefore appears only if security.json is removed between the read and the write. [1] Decision: no code change. The PR body states the behavior. Round 2 wrote "0600 from createTempFile"; that matches this probe, and the 2026-10-16 reading is recorded here as disputed.

10. **Internal version marker.** `CoreContainer.readVersion` (lines 611 to 618) reads the `""` key only to gate a reload on a positive version. A standalone edit only ever writes `v` 0 (version -1, plus one), and standalone does not read a version back. Removing the key therefore changes no reload. The visible change is that `GET /admin/authentication` on a local file no longer shows the `""` entry after an edit. [1] Kept; the PR body states it.

11. **Scope and alternative.** The ticket's reproduction, overlapping edits, is handled by the lock on main. The atomic write adds protection against a process crash mid-write. The alternative is to drop the atomic write and keep the lock and the strip. Kept the atomic write: the ticket is about corruption, and after findings 1 to 4 the atomic path changes no behavior that base kept. [4] The alternative is recorded here with its evidence and needs no decision now.

## Changes

- `1b14ce9556` `SecurityConfHandlerLocal.java`: `persistConf` calls `replaceSecurityJson`. A symlink, or an atomic attempt that fails with an IOException (including the owner check and the permission copy), is written in place with a warn log. The temporary file is always removed in a finally block.
- `1b14ce9556` changelog title: the process-crash claim only, plus "symbolic links and directories that cannot take a new file are still written in place".
- `09c8e5c8f6` `SecurityConfHandlerTest.java`: `testEditInPlaceWhenSolrHomeIsReadOnly` (POSIX assumption, skipped when the directory is writable for the user, directory permissions restored in finally).

## Gate on `1b14ce9556` (GREEN)

Logs `gates/logs/vm3/solr-18010-r3-r3-head/`.

- Changelog YAML rc 0.
- Tidy rc 0, status empty (nothing changed).
- Compile: `cleanCompileJava` and `cleanCompileTestJava` with `-Pvalidation.errorprone=true --rerun`, rc 0. Error Prone ran (not up-to-date wrappers).
- Focused tests, fresh XML: `SecurityConfHandlerTest` 5 tests, 0 failures, 0 errors; `BasicAuthStandaloneTest` 2 tests, 0 failures, 0 errors.
- `:solr:core:check -x test` rc 0.
- Disk before and after: `/` 39% used, 4.6G free; inodes 9%.

## Fail-before (rung 2)

Gate `gates/logs/vm3/solr-18010-r3-before/` on `09c8e5c8f6`'s parent `dfcf50aec0` main code with the new test. Result: compile rc 0; test rc 1. `testEditInPlaceWhenSolrHomeIsReadOnly` fails with `AccessDeniedException` on the temporary file. The symlink test is not in the run, because it was removed after this run (finding 5).

## Not run in this round

- Symbolic link and single-file bind mount on a real system (no suite policy for links; no root here).
- Owner change (needs root).
- Windows.

## Open after this round

- Maintainers: whether the suite policy should allow `LinkPermission "symbolic"` so the link case can be tested (PR body draft).
- Nick: apply the PR body draft; the PR title "written atomically" should become "written atomically where that is safe" (title change needs approval).
- Round 4 is for a maintainer read or Nick's direction, not more pool passes on the same question.
