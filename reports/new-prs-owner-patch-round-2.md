# Patches and live edits for the four draft PRs, round 2

Status: partly done. Three PR descriptions are updated and verified. The branch patches, the 5117 description, and the commits of the drafts and this report are blocked, because the auto-mode classifier denied the git commit step. Nothing has been committed or pushed in this round yet.

## Done

- **PR #5115 (SOLR-18539).** Body edited through the API to the updated draft: F1 (the choice and the behavior line now say numFoundExact clears only when every shard is skipped, with a link to lines 1241-1243 at the head), F2 (segmentTerminatedEarly named as folding from every shard), and the "adds nothing to the counts or the max score" wording. Read back and matched the draft, ignoring a byte-order mark added by the scratch copy. Still a draft.
- **PR #5116 (SOLR-18540).** Body edited: G1 (the deleted lines now link to lines 216 to 218 of the merge-base, and the JAVACMD line to lines 215 to 216 at the head), G2 (no "packaged"), G3 (the repository has no automated test for gradlew), G4 (the Linux run "shows that the script still starts the wrapper"), G5 (the unmodified base's gradlew is identical to the merge-base). Read back and matched. Still a draft.
- **PR #5118 (SOLR-18010).** Body edited: W1 (merge-base commit wording, in two places), W2 (the two base-code failures explained separately), W3 (a Limits bullet: the temporary file is owner-only on POSIX, so the file's permissions change), W4 (a GET after an edit no longer returns the internal key), and a new Limits bullet for the Windows move risk, which the reviewer raised as an owner item. Read back and matched. Still a draft.

## Blocked (needs git commit and push)

1. **PR #5117 (SOLR-18541), finding C1.** A code patch: in the skip branch of `CombinedQueryComponent`, set `hitCountIsExact = false` before `continue`, and add a test with two partial shards and no sort values that asserts numFound 0 and numFoundExact false. Then a plain fast-forward push to `combinedquerycomponent-merge-totals` with `research\push-fork-ff.ps1`, and a gate job at the new head. The body sentence on exactness, the changelog title, and the W1 to W5 wording all depend on this patch, so the 5117 body is not edited yet.
2. **PR #5118, finding W5.** The changelog title on `solr-18010-submit` overstates the lock. Proposed title: "Standalone security.json edits are serialized within one Solr process and written through a temporary file, so concurrent edits can no longer overwrite each other or leave a partly written file. The file also no longer carries the internal version metadata." This is a one-file commit, and it needs a gate job for the YAML parse.
3. **Committing this round.** The draft edits for #5115, #5116 and #5118, the claim, and this report are uncommitted in the pr-prepare worktree, so the wrapper refuses to push.

## Owner items, unchanged

- The 5117 body's "Verified 2026-10-12" date, which conflicts with the gate's 2026-10-13 UTC time.
- The 5118 branch is 74 commits behind the PR base (`597ed677`). A rebase is a force-push with a new gate, so it waits for Nick.
- Whether to edit the offer comment on PR #5065. It is not one of these four PRs, so I have not touched it.

## Not done

- No PR opened, closed, marked ready, or rebased.
- No new comments on any PR.
- No build, test, or gate run.
