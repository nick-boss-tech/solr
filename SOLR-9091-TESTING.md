# SOLR-9091 - hypothetical reproduction (nothing was compiled or run)

JIRA (2016): core restore decides per file whether to copy from the backup or keep the local file, comparing only the
CRC32 footer; a failure to read the backup file's checksum is swallowed, and a file that exists only in the backup is
copied with no verification, so a corrupt backup file is restored silently. The audit note said "obsolete:
`RestoreCore` handles null checksum and uses `compareFile`". That covers the NPE half only (`cs == null` forces a copy),
not the silent copy of unverified content.

## What main does (read on `upstream/main`)
- `RestoreCore.doRestore`: file present locally -> compare backup footer checksum with the local file; unequal -> `repoCopy`;
  file absent locally -> `repoCopy`. Nothing re-reads the copied file.
- `BasicRestoreRepository.checksum` logs "Could not read checksum from index file" and returns null on any exception.
- `restoreIndexDir` is switched in by `core.modifyIndexProps` + `newIndexWriter`; Lucene opens it without a full checksum pass.

## Change
After every `repoCopy` (both branches) `verifyRestoredFile` opens the copied file and runs
`CodecUtil.checksumEntireFile`. A mismatch is a `CorruptIndexException` (IOException), which the download task wraps in a
RuntimeException; `doRestore` already rolls back to the live index on failure. Files taken from the local index
(`localCopy`) are not re-verified. New test `TestRestoreCore.testRestoreRejectsCorruptBackupFile`.

## Guesses to verify first
- Every file listed by `repository.listAllFiles()` has a Lucene footer (no `write.lock` or foreign files in a backup dir).
  If one does, `checksumEntireFile` throws; the fix would be to skip names that are not Lucene files.
- The test flips a byte in the middle of the largest non-segments file. If that file is tiny (a `.si`), the corrupt
  content may already fail the restore on open, so the test would not discriminate; it also relies on the optimize
  replacing every original segment name so the corrupt file is copied from the backup.
- `optimize` through SolrJ on `leaderClient` with a core name; `docsSeed + 1` only changes the generated docs.
- Performance: one extra sequential read of each restored file; `RestoreCore` also runs in the cloud RESTORE path via
  `createWithMetaFile` (same `repoCopy`).

## Fail-before
Expected: `testRestoreRejectsCorruptBackupFile` fails on `upstream/main` (restore status becomes success, so the
`expectThrows(AssertionError)` loop never gets an assertion).
