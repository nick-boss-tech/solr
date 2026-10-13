# Premise job: what does the temp-file move do to a symlinked or bind-mounted security.json?

- Code under test: draft PR #5118 head `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (`SecurityConfHandlerLocal.persistConf`). No production edit.
- Status: DONE 2026-10-13T17:08Z by vm2 (coordinator). Q1 answered by run at both heads; Q2 and Q3 skipped (reasons below). Claim: claims/pool-vm2-gate-backlog-18010-replace-semantics.md.
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

## Result (vm2, 2026-10-13)

Seed 18010C0FFEE18010. Probe: standalone class solr/core/src/test/org/apache/solr/handler/admin/SecurityConfSymlinkProbeTest.java (throwaway, not committed), one set-user-role edit through SecurityConfHandlerLocal on a security.json that is a symlink to a file in another directory. Run with -Ptests.useSecurityManager=false: the first attempt with the default test security manager failed before the edit with AccessControlException LinkPermission "symbolic", so the symlink could not be created under the default test policy. Both legs ran with the same setting.

- Q1, symlink, PR head 2b5ad801c9: the edit returned with no response exception. security.json is no longer a symlink (a regular file). The symlink target is unchanged (158 bytes, no "alice"). The probe did not read back the new security.json content, so whether that file holds the edit is not recorded here. Verdict: CONFIRMED BY RUN, the symlink is replaced by a regular file and the target no longer receives the edit. Log: logs/vm2/18010-premise-head.log; XML: logs/vm2/18010-premise-xml-head/.
- Q1, symlink, merge-base 14c7aac0d1: the edit returned with no response exception. security.json remains a symlink, and the target holds the edit (215 bytes, "alice" present). Log: logs/vm2/18010-premise-base.log; XML: logs/vm2/18010-premise-xml-base/.
- Q2, single-file bind mount: SKIPPED. The vm2 account is uid 1000, has no sudo, and cannot run mount --bind, so no bind-mount probe was possible. The EBUSY reading is not tested here.
- Q3, ownership under a second user: SKIPPED. The vm2 account cannot create or change ownership to a second user.

Premise addendum for the review report is in reports/four-new-prs-strong-review-round-1.md (SOLR-18010, premise addendum, vm2).
