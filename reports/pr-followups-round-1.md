
## Item 2: PR #5117 reply (draft, not posted), vm6, 2026-10-24

Question: on 2026-10-23 at 11:51Z, ercsonusharma asked "Now that PR#5117 is merged, this PR can be rebased for review?" Nick's 14:23Z comment already says "Rebased", but the premise was not corrected.

Facts checked (GitHub API, read only, 2026-10-24):
- #5117 (SOLR-18541) is open, not a draft, and not merged. Head `49bcb450a2`.
- The PR that merged is #5172 (SOLR-18550), merged 2026-10-23T04:49:45Z, merge commit `79b23df58d`. ercsonusharma's 2026-10-19 comment named #5172 as the dependency, so the "PR#5117" in the question is most likely a wrong number for #5172.
- #5117's head is 4 commits ahead of `79b23df58d` and 0 behind (compare API), so the branch is rebased onto #5172 as the 14:23Z comment says.

Draft (GitHub comment, header form per comment-formula.md):

```
🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Thanks for the bump, @ercsonusharma. A small correction: #5117 is still open and has not merged. The PR that merged on 2026-10-23 is #5172 (SOLR-18550). This branch is already rebased on it, four commits on top of #5172's merge, and my 14:23 comment covers what changed in the rebase. It is ready for review now.
```

Not posted. Posting needs Nick's go.
