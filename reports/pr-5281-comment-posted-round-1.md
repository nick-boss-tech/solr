# PR 5281 follow-up offer posted, round 1

Status: POSTED AND READ BACK

- Public comment: https://github.com/apache/solr/pull/5281#issuecomment-6613016989
- Created: 2026-11-03T19:08:32Z; author `nick-boss-tech`.
- Authority: Nick explicitly approved the exact draft with "It looks good, you can post this." The draft-review hold was released for this comment.
- Upstream PR head immediately before and after posting: `35970681650b4e602632bdfc344c5922b9f1581f`; PR open.
- Offered fork branch: `nick-boss-tech/solr:pr-5281-triage-followup`.
- Remote offered/tested head separately verified before posting: `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40`; its sole parent is the live upstream head, so the pinned fast-forward merge applies.
- Proof: `receipts/PR-5281-triage-followup-round-1.md`, GREEN on vm2 at the offered head, read at `2d798474b3d944973d8677625f2d49e642b3b1ec` and unchanged at `1b6809ef0f7b66856ab2ca4e52e348b4f56a5645`.
- Verified results offered: 23 offline tests pass, three H0 assertion failures become H1 passes, GFM output checks and actionlint/ShellCheck pass, tidy clean, root check passes in a real clone after the documented Antora worktree setup failure.
- Public text retains the Antora condition and live gateway/secret/event/notification/publication activation limits.
- Pre-post duplicate check: no conversation comments; eight original author inline notes only. No equivalent offer existed.
- Posted once using `gh.ps1 pr comment --body-file` with the approved absolute draft path.
- Separate direct comment GET compared stored body to the approved draft and matched after line-ending/trailing-newline normalization. Read-back author and URL also matched. A separate PR read confirmed the stored comment and unchanged upstream head/discussion.
- Local draft: `research/branch-reviews/2026-11-03-pr-batch/PR-5281-comment-draft.txt`; public read-back snapshot: `PR-5281-comment-readback.json` beside it.

No contributor-branch change, force-push, PR body edit, JIRA post, review approval, or merge was performed. Await David's response; the offered implementation remains on Nick's fork.
