# Open PR review queue: process

Owner direction, 2026-10-20: many open PRs on apache/solr are old, were written by other people, and may have thin tests. The pool reviews them as drafts. Nothing is posted to GitHub from here. The owner or the main agent decides what goes out.

## Scope

- Queue: `material/pr-review-queue.md`, a snapshot of `apache/solr` open PRs whose author is not `nick-boss-tech`, 174 at the 2026-10-20 snapshot. Refresh it before a claim; mergeable state of `unknown` means GitHub has not computed it yet.
- Order: non-draft PRs first, then drafts, each group least recently updated first.
- Claim unit: one PR. Claim file `claims/pool-pr-review-<number>.md`, pushed before any review work.

## Steps for one PR

1. Refresh. Read the PR state, head SHA, mergeable state, reviews and comments through the read-only API. If it is merged or closed, mark the claim WON'T DO with the evidence and move on.
2. Compatibility. Fetch `pull/<N>/head` from `https://github.com/apache/solr` into a scratch worktree at the current `apache/main`. Record whether it applies cleanly, and whether it compiles at the PR head against current main. Use `/workspace/tools/git-2.49.1/bin/git merge-tree --write-tree` for a conflict check without a checkout.
3. Test thoroughness. List each production change in the diff and check that a test would fail without it. Flag missing edge cases, sleeps, unseeded randomness, and tests that only pin the new output. Where a test is cheap to break, run a mutation and record the result.
3a. Surrounding code. Read the code around each change, not only the diff: sibling pages, classes and tests that do the same job, and the pattern the repo already uses for it. Check whether the change leaves a gap that another part of the tree closes, for example a snippet that should be an include from a compiled example, or a test the branch removed. Record the pattern you compared against and the file it came from.
4. Gate, only when warranted. Run focused tests only, on the PR head rebased onto current main, under `flock /workspace/tools/.gradle-vm4.lock`. Logs go to `/workspace/gates/logs/vm4/pr-<N>/`. No full suites unless the owner asks.
5. Draft. Write the review as `drafts/pr-review/<N>.md`: a proposed comment list marked PROPOSED, plus a bump note if the PR is stale or conflicts. Nothing is posted.
6. Outcome, one of READY (tests and compatibility fine), NEEDS-CHANGES (list the items), STALE-BUMP (conflicts or rot, ask the author to rebase), SUPERSEDED (another PR or main already did it), NEEDS-OWNER (a design or scope choice the ladder cannot settle).
7. Closeout. Write `receipts/pr-review-<N>.md` with the evidence and the outcome. Mark the claim DONE with the outcome and push. Remove the scratch worktree.

## Rules

- No GitHub comments, reviews, labels, or PR opening or closing. No pushes to anyone's branch or to `apache/solr`.
- Do not use `nick-boss-tech` PRs. They are in the owner's own queue.
- Keep the review factual. Say what was run and what it showed. Do not claim a PR is correct beyond the evidence.
- A review that only reads the diff is incomplete. If the surrounding code shows a better pattern, say so in the draft, with its path.
- Use the ladder (`material/decision-ladder.md`) for any choice the review has to make on the author's behalf.
- No em dashes or en dashes in drafts.
