# Premise job: what does the temp-file move do to a symlinked or bind-mounted security.json?

- Code under test: draft PR #5118 head `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (`SecurityConfHandlerLocal.persistConf`). No production edit.
- Status: OPEN, unclaimed.
- Capability tags: premise-run.
- Intended host: a Linux host. The symlink case needs no privileges. The bind-mount case needs mount rights; skip it and record that if the host has none.
- Source: `reports/four-new-prs-strong-review-round-1.md`, section SOLR-18010, finding on replace semantics. The old write opened the existing path and wrote in place, which follows a symlink and works on a single-file bind mount. The new write creates a temp file and moves it over the path, which replaces the directory entry.
- Job type: premise run only. Throwaway probe test in a worktree at the head above; nothing is pushed.

## Questions

**Q1.** `security.json` in SOLR_HOME is a symlink to a file in another directory. After one edit through `SecurityConfHandlerLocal` (a real `set-user-role` edit, as in `testEditKeepsExistingSecurityJsonPermissions`), is `security.json` still a symlink? Does the target hold the edit? Is the target unchanged? Run the same probe at the merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` for contrast.

**Q2.** `security.json` is a single-file bind mount (`mount --bind file file`, or a container volume mapping one file). After one edit, does the edit succeed or throw? If it throws, the exception class and message, and whether the bind-mounted file is unchanged. Same probe at the merge-base for contrast. Reading says `rename(2)` onto a mount point fails with EBUSY, which is not an `AtomicMoveNotSupportedException`, so the plain-move fallback does not run.

**Q3.** Ownership: when `security.json` is owned by a different user than the Solr process and the folder is writable by Solr, who owns the file after the edit? (Only if the host can create a second user; otherwise skip and record it.)

## Deliverable

Verdicts per question in this file, with the observed values, the seed, and the log path. Append the same verdicts to `reports/four-new-prs-strong-review-round-1.md` as a premise addendum for SOLR-18010, in the same push that marks this job DONE.

## Step checklist

- [ ] Q1 symlink at head and at the merge-base
- [ ] Q2 bind mount at head and at the merge-base, or skipped with the reason
- [ ] Q3 ownership, or skipped with the reason
- [ ] verdicts recorded here and in the report; job DONE
