# PR 5118 mention of the seam-free branch (draft, not applied)

Status: drafted 2026-10-22 by vm3, updated after the disk-full fix. Not applied to the live PR body. Placement and wording need Nick's approval before anything is posted.

Proposed paragraph for the PR body:

> Alternative: branch `solr-18010-atomic-noseam` (commit ce52cbca1f) carries the same POSIX temporary-file write without test seams. It keeps the security.json version marker, which the head of this PR removes, so it is not a drop-in replacement. The write falls back to an in-place write only when the temporary file cannot be created (for example a read-only SOLR_HOME), when the move is refused (for example a single-file bind mount), or when the owner differs. A failed write, fsync or permission copy fails the edit and keeps the existing file. Its tests cover the atomic success path, permissions kept on edit, a new file's default mode, the read-only SOLR_HOME fallback and the POSIX-only predicate. The fsync, permission-copy, owner-mismatch and directory-sync failure paths are covered on `solr-18010-atomic-posix` (commit 9d013bd696) by seam tests, not on this branch. Maintainers may prefer to ship the seam-free branch.

Open for Nick: which head goes to maintainers, and whether this paragraph goes in the live body or waits for a decision.
