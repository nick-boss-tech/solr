# SOLR-18010: Option B, atomic write on POSIX only (vm3, 2026-10-22)

Branch `solr-18010-atomic-posix` at `e66ec9d3ea`, from upstream main `7892348272`. Not the PR head. The PR branch `solr-18010-submit` is unchanged.

## What the branch does

- Keeps the internal `""` version marker, as recommended.
- On POSIX file systems, and only when `security.json` is not a symbolic link: writes a temporary file in the same directory, fsyncs it, copies the existing permissions, checks the owner, moves it over the file with `ATOMIC_MOVE` (falling back to a plain move), then fsyncs the directory.
- Any failure in that path (IOException) falls back to the in-place write, which is main's behavior, and logs a warning. A failed directory fsync after a successful move is logged only.
- Windows and other non-POSIX file systems go straight to the in-place write.

## Gate (full, green)

Log `gates/logs/vm3/solr-18010-r3-atomic-full2/`. Changelog rc 0; tidy rc 0 (tidy rewrapped long lines in two files; the tidy diff is in the commit); Error Prone compile rc 0; focused `SecurityConfHandlerTest` 15 tests, 0 failures, 0 errors, and `BasicAuthStandaloneTest` (from main, unchanged); `:solr:core:check -x test` rc 0.

## Tests that catch their fault (mutation runs, Linux)

Each row removes one safeguard from the good code and runs `SecurityConfHandlerTest`:

| Mutation | Result |
| --- | --- |
| m1 always in place | 4 to 6 fail, including the reader test and the refused-move test |
| m2 no fsync of the file | 2 fail (sync order, failed sync) |
| m3 fallback not caught | 5 to 7 fail |
| m4 POSIX guard removed | 1 fails (`testAtomicReplaceNeedsPosixViewAndNoSymbolicLink`) |
| m5 permission copy removed | 2 fail |
| m6 directory-sync failure thrown out | 1 fails |
| m7 owner check removed | 1 fails (after it was made to assert no move) |
| m8 temp file not deleted | 2 to 6 fail |

Logs: `gates/logs/vm3/atomic2-mut-*` and `atomic3-mut-*`.

## Probes (scratch, not committed)

- Startup with a truncated `security.json`: `CoreContainer.createAndLoad` fails with "Failed opening existing security.json file", cause `Unexpected EOF`. The node does not start. This is the measured consequence of the crash risk. Log `gates/logs/vm3/probe-atomic/`.
- Symbolic link: the link stays and the target gets the edit (with `LinkPermission "symbolic"` added locally).
- Single-file bind mount in a user namespace: the move is refused, the in-place fallback writes the mounted file.

## fsync cost (measured on this host)

- Local ext4 (`/tmp`): about 0.04 ms per atomic write without fsync, 0.33 ms with fsync of file and directory.
- NFS (`/workspace`): about 16 ms without, 19 ms with. Security edits are rare admin actions, so both are acceptable.

## Not done

- Fail-before on main for this branch's tests: not run. `compileTestJava` fails against main's handler because `SecurityConfHandlerTest` calls the new seam methods, and the test source set compiles as one unit. Splitting the seam tests out would make it possible. The ordering test body is identical to the narrow branch, where its fail-before was recorded (`narrow-failbefore-main`).
- The marker removal is not in this branch. Its test (`BasicAuthStandaloneTest` marker assertions) is only in `solr-18010-narrow`.
- Windows: not run. The non-POSIX branch test runs on Windows (it forces the flag), but the held-file and ACL cases are not covered.
- Owner mismatch on a real second user: not testable here (one mapped user in the namespace).
- Crash injection mid-write (kill during the move): not run.

## Update 2026-10-22: split, POSIX assumptions, fail-before on main

Branch head `b12895b4e2` (on `e66ec9d3ea`).

- Seam tests moved to `SecurityConfHandlerLocalSeamTest` (9 tests and the hooked handler). `SecurityConfHandlerTest` keeps 4 tests that compile against main's handler (`testEdit`, `testConcurrentPersistConfLeavesOneWholeDocument`, `testEditKeepsExistingSecurityJsonPermissions`, `testEditInPlaceWhenSolrHomeIsReadOnly`).
- Six atomic-path tests now assume POSIX file attributes (`assumeAtomicReplaceSupported`). Without this, on Windows the atomic path is off and these tests would time out or fail for the wrong reason. `testNonPosixFileSystemWritesInPlace` runs everywhere.
- Focused run on Linux: 15 tests across the three classes, 0 failures (`gates/logs/vm3/split-r2/`).
- Full gate green on the split tree: `gates/logs/vm3/solr-18010-r3-atomic-split-full/` (changelog, tidy with no changes, Error Prone compile, 6 focused tests, module check).
- Fail-before on main's handler (seam class excluded, since it cannot compile against main): 6 tests run, 1 fails, `testConcurrentPersistConfLeavesOneWholeDocument` (`gates/logs/vm3/split-failbefore-main/`). The other five pass on main, which is expected: this branch keeps the marker, so the marker assertion is not in it.
- Not run: stray `security.json*.tmp` at startup. Read from code only: `SecurityConfHandlerLocal.getSecurityConfig` opens only `securityJsonPath` (L67-68 at main), so a stray temp file is not read. Not measured.

## Prompt for the Windows agent (run on `b12895b4e2`)

Run the three security test classes on Windows, with no branch change, no commit, no push, no tidy:

    git clone https://github.com/nick-boss-tech/solr.git solr-18010-atomic
    cd solr-18010-atomic
    git checkout b12895b4e2
    .\gradlew.bat --no-daemon :solr:core:test --tests org.apache.solr.handler.admin.SecurityConfHandlerTest --tests org.apache.solr.handler.admin.SecurityConfHandlerLocalSeamTest --tests org.apache.solr.security.BasicAuthStandaloneTest

Report from the JUnit XML in `solr\core\build\test-results\test\`: each test with PASS, FAIL or SKIPPED and its message; counts per class (tests, failures, errors, skipped). Expected on Windows: the POSIX-only tests are SKIPPED (permission copy, read-only SOLR_HOME, and the six atomic-path tests); `testNonPosixFileSystemWritesInPlace`, `testEdit`, `testConcurrentPersistConfLeavesOneWholeDocument`, `testAtomicReplaceNeedsPosixViewAndNoSymbolicLink`, and the BasicAuth tests PASS. Any FAIL is a finding, with its message. Gradle version from `gradlew.bat --version` and JDK version. Plain text, no em or en dashes.
