# PR 5118 mention of the seam-free branch (draft, not applied)

Status: drafted 2026-10-22 by vm3. Not applied to the live PR body. Placement and wording need Nick's approval before anything is posted.

Proposed paragraph for the PR body:

> Alternative: branch `solr-18010-atomic-noseam` (commit e9e71fad06) carries the same POSIX temporary-file write without test seams. It keeps the security.json version marker, which the head of this PR removes, so it is not a drop-in replacement. Its tests cover the atomic success path, permissions kept on edit, a new file's default mode, the read-only SOLR_HOME fallback and the POSIX-only predicate. It does not test the fsync, refused-move, permission-copy, owner-mismatch and directory-sync fallbacks. Maintainers may prefer to ship that branch. The fallback tests are on `solr-18010-atomic-posix` (commit 67be8b077f), which needs package-private hooks in `SecurityConfHandlerLocal`.

Open for Nick: which head goes to maintainers, and whether this paragraph goes in the live body or waits for a decision.
