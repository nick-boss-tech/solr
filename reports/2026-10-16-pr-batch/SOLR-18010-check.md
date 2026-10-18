# SOLR-18010 (PR 5118) check: undisclosed write access to SOLR_HOME, and the file-mode proof

Host: vm2, Linux, running as a non-root user (uid 1000), umask 0022. Fork branch `solr-18010-submit`, head `571307400b6917ce458db989f29febb5cff8a600` (ls-remote at the start of this tick, about 05:26Z, unchanged). Merge-base with upstream/main `14c7aac0d151`. JDK 21.0.12.1, Gradle 9.7.0 from the branch wrapper. Seed `18010C0FFEE18010`.

## Status

- Write access to SOLR_HOME: confirmed. At head, `persistConf` needs write permission on the SOLR_HOME directory. With a read-only SOLR_HOME it fails. The PR does not disclose this.
- File mode: created file is 0644 (`rw-r--r--`) under umask 0022, which matches the 0644 result. The draft's 0600 premise does not hold for a new file. An existing file keeps its mode.
- Escalated to Nick, per the Decisions table: the read-only failure must be disclosed in the PR or the write path changed. No change made.

## Write path at head

`SecurityConfHandlerLocal.java` (`persistConf`, lines 121 to 140 at head): `Files.createTempFile(securityJsonPath.getParent(), ...)` creates `security.json<random>.tmp` in SOLR_HOME, then `Files.write`, optional permission copy, then `Files.move(tmp, securityJsonPath, ATOMIC_MOVE, REPLACE_EXISTING)`.

At base `14c7aac0d151`, the same method wrote in place with `Files.newOutputStream(securityJsonPath)`. Read from code, not run: base does not need write permission on the directory when security.json itself is writable.

## Run at head

Scratch tests (uncommitted in `/workspace/worktrees/vm2-18010-head`; patch saved as `SOLR-18010-scratch-test.patch`). Run: `--tests org.apache.solr.handler.admin.SecurityConfHandlerTest.testScratch*`, 3 of 3 passed, rc=0. Log `/workspace/gates/logs/vm2/18010/head-scratch-modes.log`, XML in `head-scratch-modes.xml/`.

1. `testScratchReadOnlySolrHome`: SOLR_HOME set to `r-xr-xr-x`, security.json present and writable. Observed: `persistConf` throws `java.nio.file.AccessDeniedException` on `.../scratchRo-001/security.json4076088857169663117.tmp`, cause null. security.json unchanged (`true`). So the write fails.
2. `testScratchModeNewFile`: no security.json, SOLR_HOME writable. Observed mode `rw-r--r--` (0644).
3. `testScratchModeExistingFile`: security.json present at `rw-r--r--` before the edit. Observed `rw-r--r--` after. The copy keeps the existing mode.

The 0640 case (existing file set to `rw-r-----`) is covered by the PR's own `testEditKeepsExistingSecurityJsonPermissions`. It was not rerun here. Earlier receipts record it green at 2b5ad801c9.

## Reading

- The 0644 result: `Files.createTempFile` with an explicit directory does not force 0600 on POSIX. The temp file takes the umask default, which is 0644 under umask 0022. So the new-file mode depends on the host umask. The draft's 0600 premise is wrong on this host. The draft's fail-before line should name the observed 0644 and the 0640 expectation, as the receipt already notes.
- Read-only SOLR_HOME and single-file Docker mounts: the temp-file step fails whenever the directory is not writable, even when security.json itself is writable. Base does not have this failure. The branch's changelog entry does not state the requirement. The live PR body was not read in this check.

## For Nick

1. Disclose in the PR body that persistConf needs write access to SOLR_HOME, or change the write path to keep the in-place write for a writable security.json.
2. Correct the 0600 premise in the draft's fail-before line to the observed 0644 (umask 0022) and the 0640 expectation.

No PR, comment or branch change was made.
