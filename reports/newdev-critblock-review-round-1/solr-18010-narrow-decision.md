# SOLR-18010: narrowing PR 5118 and the test seams (vm3, 2026-10-22)

## Decision

Nick (2026-10-22): the atomic temp-file write is not worth its size. The PR adds about 430 lines for a crash window that is very small: a crash in the middle of writing a few hundred bytes. The fallbacks (symbolic links, bind-mounted single files, read-only SOLR_HOME, owner mismatch, refused move) exist only because the atomic move replaces the file. Main handles those cases without them.

Narrow version: keep the marker removal and serialize the content before the file is opened, both in place, as main writes it. Drop the temp file, the move, the permission copy and the owner check.

## What the narrow version changes

- `SecurityConfHandlerLocal.persistConf`: the stored content is built first (deep copy, marker removed, serialized), then the file is opened and written in place. This is the same write path as main, with the marker removal and the reordering.
- Tests: `SecurityConfHandlerTest.testConcurrentPersistConfLeavesOneWholeDocument` (fails on main, pins the order of serialization and opening), `testEdit`, and `BasicAuthStandaloneTest.testBasicAuth` (fails on main; checks the saved file has no marker) and `testConcurrentEdits`.
- Dropped from the PR: `testEditKeepsExistingSecurityJsonPermissions`, `testEditInPlaceWhenSolrHomeIsReadOnly`, `testConcurrentEditsToLocalSecurityJson`, and their helpers. They tested the atomic path or behavior that main already has (#5065's lock, in-place permissions).
- Changelog: `changelog/unreleased/SOLR-18010-security-json-version-metadata.yml`, replacing the atomic-write entry.

## Branches

- `solr-18010-submit` (PR 5118 head): unchanged at `1b14ce9556`. It still has the atomic design. Replacing it needs a decision (see below).
- `solr-18010-seam-tests` at `6f0236a9f7`: record only, not for merge. Package-private hooks around the temp-file steps, and four tests (reader sees the old document until the move; refused move; failed permission copy; owner mismatch), each forcing one fallback. Focused run on Linux: 11 of 11 pass. Failure-mode runs (removing each fallback) were not run, because the design is being dropped.
- `solr-18010-narrow` at `e81e5e28e9` (from upstream main `7892348272`): the narrow version. Gate green (receipt `receipts/SOLR-18010.md`). Fail-before on main: two tests fail as expected.
- PR body draft for the narrow design: `pr-drafts/solr-18010/PR-5118-body-narrow.md`. Not applied.

## Evidence that informed the decision

- The atomic design's tests cover cases the base code never failed: symbolic links, bind mounts and read-only directories. Each fallback has its own test or run, recorded in `solr-18010-pr5118-test-audit.md`.
- The concurrency test pins the order of serialization and opening. It does not test the atomic move. A forced in-place write still passes it (mutation `probe-18010-mut-inplace`).
- Windows check of the PR head (`solr-18010-windows-check-1.md`): 7 tests, 0 failures, 2 skipped on POSIX checks. It does not exercise the atomic path.

## Open decisions for Nick

1. Replace the PR 5118 head with the narrow version. This needs either a fast-forward commit on `solr-18010-submit` that reverts the atomic write (no force push), or a rebuilt branch (force push with a lease, which needs your approval).
2. The PR body and title describe the atomic design. They need rewriting to match the narrow version, and that is a live edit (no approval needed).
3. Whether to keep the marker change in this PR at all. It is the part that fails on main and it is small.

## Limit for the narrow version

A crash in the middle of the write can leave a truncated `security.json`. We have not measured what Solr does at startup with a truncated file. That is the limit to state in the PR body.
