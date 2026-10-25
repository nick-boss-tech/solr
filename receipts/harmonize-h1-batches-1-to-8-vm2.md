# Harmonize H1 batches h1-001 to h1-008: receipt and correction (vm2)

Assignment: `assignments/harmonize-audits-comment-drafts-round-1.md`, phase H1. Written 2026-10-25T00:44:19Z on coordination-ui `5ef41f877f1`.

## Correction

The first batch files (`material/harmonized-backlog/batches/h1-001.json` to `h1-008.json`, published 2026-10-25 00:27 to 00:37 UTC) are superseded. The builder treated skip-audit draft branches (names containing `-draft`, for example `solr-5941-draft-open`) as implementation branches. That made `branch_state` draft-branch and, through the crosswalk, `disposition` implemented. For batch h1-007 this marked 76 unimplemented skips as implemented.

Corrected files, same records and same crosswalk except the draft-branch rule:

- `material/harmonized-backlog/batches/h1-001-rev2.json` to `h1-008-rev2.json`.
- Audit draft branches are now recorded in `citations.audit_draft_branch` and do not set `branch_state`.
- Implementation branches (no `-draft`) still set `draft-branch` or `gated-branch`. PR state is checked for every branch name.

The v1 files are kept unedited as the record of what was published.

## Counts (rev2)

- h1-001: unjudged-stale 89, keep-open 3, verify-first 3, implemented 2, closure-not-a-bug 2, closure-fixed 1 ; conflicts 2
- h1-002: unjudged-stale 84, implemented 5, verify-first 5, keep-open 2, closure-fixed 2, closure-not-a-bug 2 ; conflicts 4
- h1-003: unjudged-stale 76, verify-first 12, closure-fixed 3, implemented 3, closure-not-a-bug 2, closure-obsolete 2, keep-open 2 ; conflicts 3
- h1-004: unjudged-stale 64, closure-fixed 10, verify-first 10, implemented 7, closure-not-a-bug 6, keep-open 2, closure-duplicate 1 ; conflicts 5
- h1-005: unjudged-stale 58, verify-first 15, implemented 11, closure-not-a-bug 6, keep-open 5, closure-obsolete 3, closure-fixed 2 ; conflicts 10
- h1-006: verify-first 41, implemented 22, closure-not-a-bug 13, unjudged-stale 10, closure-fixed 9, keep-open 2, closure-obsolete 2, closure-duplicate 1 ; conflicts 3
- h1-007: verify-first 31, implemented 24, closure-not-a-bug 20, closure-obsolete 11, closure-fixed 8, keep-open 5, ping-stale 1 ; conflicts 0
- h1-008: verify-first 38, implemented 23, closure-not-a-bug 20, closure-fixed 7, closure-obsolete 7, keep-open 3, closure-duplicate 1, ping-stale 1 ; conflicts 0

## Limits (also written into each batch file)

- PR state checked only for PRs headed from the fork. PRs by other authors were not searched (phase H2 QA).
- owner_excluded not evaluated.
- Fix-version citations for close-fixed not checked (phase H2 QA).
- `verify-first` means the skip evidence text did not match a crosswalk category. It bars comment families until judged.

## Not done here

Batches h1-009 to h1-016 are not built. The first-version files for those were never published.
