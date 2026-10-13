# SOLR-18010 review patch: fail-before premise fix, route choice, round 1

Trigger: pr-prepare tip `1173a1e40a0`, from the vm2 assignment `assignments/pool-vm2-18010-review-r1-fail-before-patch.md`. The review-patch gate at `e99be67c9a6f` failed on a premise defect. The parent-baseline run is recorded as passing. Round claims: `claims/pool-pr-prepare-tip-1173a1e-round-1.md` and `claims/pool-review-18010-r1-fail-before-patch.md`.

Scope: the test change is prepared as diffs under `gates/`. Nothing is pushed to the fork, and `solr-18010-submit` and PR #5118 are untouched. No build, test or Gradle run. No gate relaunched.

Heads, checked by ls-remote: `review-r1-solr-18010` is `e99be67c9a6fe15f86306104843a30b35196f159` (parent `2b5ad801c9e`), and `solr-18010-submit` is `2b5ad801c9e`, which is untouched.

## Premise check against the assignment

| Premise | Result | Basis |
|---|---|---|
| (a) The merge-base failure of the new test is the truncation of `security.json` before the write | Confirmed from the record | Base lines 86 and 87: `Files.newOutputStream(securityJsonPath)` truncates before `Utils.toJSON` writes. The recorded message fits |
| (b) The parent-baseline run passes all six tests | Partly | The record says "6 of 6 passed" with a log name and an XML timestamp. It gives no seed and no head SHA, so it cannot be tied to the parent by itself |
| (c) The parent's `finally` can replace the write error | Confirmed | Parent lines 153 to 155: `} finally { Files.deleteIfExists(tmpPath); }`. An `IOException` from the delete replaces the `SolrException` |
| (d) No branch test forces the permission-copy warning or the delete failure | Confirmed | `testEditKeepsExistingSecurityJsonPermissions` covers only the copy's success path. The new test forces only a write failure, so its delete succeeds |
| (e) The parent already has the atomic move | Confirmed | Parent lines 135 to 140 use `ATOMIC_MOVE`. The PR head was read from the cached ref, not live |

The test count is five methods, with no JUnit annotations (JUnit 3 style): `testEdit`, `testConcurrentEditsToLocalSecurityJson`, `testConcurrentPersistConfLeavesOneWholeDocument`, `testEditKeepsExistingSecurityJsonPermissions`, and `testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile`.

The merge-base also lacks the edit lock and the `remove("")` sanitising. So the record's attribution of the three extra base failures to the temp file and the atomic move is loose. It is inferred, not run.

## The two routes

Route 1 (recommended). In the test's `CharSequence` callback, which runs after the temporary file is created, the temporary path is replaced by a non-empty directory of the same name. The parent's `deleteIfExists` then throws `DirectoryNotEmptyException` in its `finally`. No production change and no permission trick. Windows was verified in the JDK 21 source archive. Linux (`ENOTEMPTY`) is from JDK knowledge and not checked. Rejected: a production seam (a production change), a test subclass (`Files` is static), `chmod` (excluded), and an open handle (Windows only).

- Renamed to `testFailedWriteReportsWriteErrorWhenTemporaryFileCannotBeDeleted`. The leftover check now expects only the blocked entry.
- The patch is `gates/SOLR-18010-fail-before-route1.patch`.

Route 2 (fallback). Renamed to `testFailedSerializationKeepsPreviousFileAndRemovesTemporaryFile`. The failure happens before the move, so a direct write would pass too. The name does not claim an atomic move. It passes at both heads and does not cover the `finally` change. The patch is `gates/SOLR-18010-fail-before-route2.patch`.

## Expected results, derived by reading, nothing run

- Parent production with route 1: FAIL, with `unexpected exception type thrown; expected:<SolrException> but was:<DirectoryNotEmptyException>`. The format string was checked in the cached JUnit 4.13.2 jar.
- Patch with route 1: PASS.
- Route 2 at either head: PASS. This is why route 2 does not pin the patch's change.

## Risks and gaps

- Route 1 on Linux is unchecked. The gate on vm2 decides it.
- A Windows re-create race after the delete is a residual risk.
- No seed exists yet. The gate must record one.
- The parent-baseline record gives no seed and no head. The gate should record both.

## Owner items

1. Go-ahead to push route 1 to `review-r1-solr-18010` as a plain fast-forward from `e99be67c9a6f`. Then vm2 runs the hardening gate and the parent-baseline fail-before at the new head, as the assignment says.
2. Confirm route 1. Route 2 is the fallback if the gate shows route 1 cannot be made to fail at the parent.
3. The earlier items stand: the SOLR-18539 width against Option B, and one green rule for fail-before legs across SOLR-18539 and SOLR-18010.
