# SOLR-18010, PR #5118: claim-by-claim test audit (vm3, 2026-10-22)

Question from Nick: did we test every case the PR body explains? Answer: no, not all. This audit lists each claim, the run behind it, and what is still untested. Head `1b14ce9556` (no code change). Body corrected live, `pr-drafts/solr-18010/PR-5118-body-round-6.md`.

Method: every claim in the live body was matched to a run. Runs used a user namespace (`unshare -rm`) for bind mounts and ownership; the test policy was unchanged except where a row says so. Logs are under `gates/logs/vm3/`.

## Tested on Linux

| Claim in the body | Run | Result |
| --- | --- | --- |
| Read-only SOLR_HOME: edit is written in place | `testEditInPlaceWhenSolrHomeIsReadOnly`; `solr-18010-r3-before/` at `dfcf50aec0` | Fails there with `AccessDeniedException`; passes at head |
| Symbolic link: written in place, the link stays | `testEditThroughSymbolicLinkUpdatesTarget` with `LinkPermission "symbolic"` added locally (not committed); `solr-18010-r3-symlink-head-policy/` and `…-before-policy/` | Fails at `dfcf50aec0`, passes at head |
| Single-file bind mount: the move is refused, the fallback writes in place and logs a warning, no leftover file | `testEditOverSingleFileBindMount` in a user namespace (scratch, not committed); `probe-18010-bind/` | Passes. The WARN "Could not replace … through a temporary file, writing it in place" is logged, the change reaches the mounted file, the home directory holds no `.tmp` file |
| Permission bits are copied (and the new file is not 0600) | `testEditKeepsExistingSecurityJsonPermissions` with the copy removed (mutation); `probe-18010-mut-perms/` | Fails at the mutation (expected OWNER_READ, OWNER_WRITE, GROUP_READ; observed also OTHERS_READ) |
| Marker `""` not written | `BasicAuthStandaloneTest.testBasicAuth` and `testEdit`; `r4-18010-main-with-head-tests/` | `testBasicAuth` fails on main, passes at head |
| Focused tests, changelog, tidy, Error Prone compile, module check | Gate `solr-18010-r3-r3-head/` at `1b14ce9556` | All green |

## Tests that show less than the body implied

| Claim | What the run shows |
| --- | --- |
| "Two overlapping writes can leave a partly written file", and the concurrent test covers it | `testConcurrentPersistConfLeavesOneWholeDocument` fails on main. It holds the first write during serialization. The test proves the order of serialization and file opening. It does not test the atomic move. **Mutation `probe-18010-mut-inplace/`** (temp-file path forced off, serialization still first): the test still passes. **Mutation `probe-18010-mut-nocopy/`** (deep copy removed): still passes. |

## Not tested

| Claim | Why not |
| --- | --- |
| The atomic move leaves no partly written file for a reader or after a crash | No test. A crash cannot be simulated in the suite without a subprocess kill. Reasoning only. |
| A failed permission copy falls back in place | Cannot make `setPosixFilePermissions` fail as this user. Reasoning only. |
| An owner mismatch falls back in place | Cannot be produced here. In a user namespace, the host user maps to root, so the managed file and the temp file look owned by the same user. A probe passed with no fallback, which confirms the branch did not run. Needs a second host user. |
| ACL and extended attribute loss on the move | No run. Reasoning from the move semantics. |
| A crash leaves `security.json*.tmp` at 0600 | `createTempFile` mode checked by a probe (`rw-------`). No crash run. |
| `GET` no longer shows the `""` entry after an edit | Checked through the file content, not through the GET path. |
| ZooKeeper path unchanged | The diff touches only `SecurityConfHandlerLocal.java` and the tests. ZooKeeper tests not run. |
| Windows behavior (rename over an open file, POSIX permission skip) | Not run. Gradle version for a Windows run: the repo's `gradlew.bat`, pinned to Gradle 9.7.0 by `gradle/wrapper/gradle-wrapper.properties`, on JDK 21. |
| Two Solr processes on one SOLR_HOME, fsync, upgrade and downgrade | Stated as limits. Not tested. |

## What changed in the body

- The concurrent-write claim now says what the test proves (serialization before open), and that it does not test the atomic move.
- Proof lists the bind mount run and the permission mutation.
- Limits lists the untested cases above, with the reasons.

## Next steps (for Nick's decision, not started)

- A test for the atomic move needs a hook between the temp write and the move, and a reader that checks the old file is still whole. That adds a test seam in production code. Maintainer choice.
- A second host user is needed to test the owner branch. That is a Linux host with two users, or a Windows run.
- Windows run is optional for the rename-over-open-file case and the permission skip. The gate of record is Linux.

## Probe cleanup

Scratch worktree `solr-18010-probe` (uncommitted test insertion and mutations, all reverted) removed after the logs were saved.
