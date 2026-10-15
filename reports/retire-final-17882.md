# Retire final pack: SOLR-17882

Verdict: RETIRE CLEAN as a branch. Nothing gainable. The ticket's coordinator question is not settled by this branch and stays the owner's re-aim call.

Checked 2026-10-15.
- Fork tip: `origin/solr-17882-submit` = d328eb392d9c9acf3d925bb20729798e0d17720f (ls-remote origin refs/heads/solr-17882-submit). Matches the receipt and RR1.
- Main: upstream main = 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 (live).

## Premise

From `receipts/SOLR-17882.md`:
- "NO GATE as a fix, by finding: the premise is dead. The round 12 and round 15 reviews found the ticket's LTR coordinator failure does not reproduce as a defect this branch fixes, and the branch is a retire candidate."
- "Disposition (retire the branch, or re-aim it) is an owner call still pending on the main side's record."

The two goal files the receipt cites (reviews-2026-10-04-round12 intake and reviews-2026-10-04-round15-eleven-postgate) are not in this worktree. RR1 did not read them, so the premise-dead call rests on the receipt's citation.

## What the branch holds

Merge-base 56ec140e3636. Branch-only commits 7f51fdd6c0f, 74d2726e29b, d328eb392d9. Two files:
- `solr/modules/ltr/src/java/org/apache/solr/ltr/search/LTRQParserPlugin.java` (+22/-1): a synchronized `modelStore(SolrCore)` fallback that reads the store from the core's RestManager, and its call site.
- `SOLR-17882-TESTING.md` (+99). No test class.

The branch's own note says the fallback does not fix the symptom. Checked on main 714d6ceff:
- `solr/core/src/java/org/apache/solr/core/SyntheticSolrCore.java` lines 80-85: `initRestManager` returns a bare `new RestManager()`, uninitialized, with the comment "We do not expect RestManager ops on Coordinator Nodes".
- `LTRQParserPlugin.java` line 177: `mr.getModel(modelNames[i])` runs on that store, so a coordinator hits a null store.

## Gainable, and what is taken

- Gainable: nothing. The fallback turns the failure into a not-found error, as the branch's Javadoc says. The branch's new lock on each model lookup was not measured and is not a gain.
- Taken: the finding is already recorded in `reports/query-parsing-round-1-q2.md` (findings 14-17) and `reports/query-parsing-round-1.md` line 44.

## Owner step

Decide retire or re-aim first. Retiring is clean either way, because the branch does not change the coordinator path. Re-aiming is a separate `solr/core` change to SyntheticSolrCore and LTRQParserPlugin. It is not this branch.

If retire:
1. Label the fork branch `solr-17882-submit` RETIRED and leave it in place (owner rule, 2026-10-14: a retired branch is never deleted).
2. PR: none. No PR authored by nick-boss-tech on apache/solr has this head (`gh pr list --author nick-boss-tech --state all`, 2026-10-15).
3. Local worktree `wt\SOLR-17882` is clean and tracks origin. Its local branch is `wt-solr-17882-submit`. Leave both as they are.

Deletion is not the current rule. It would apply only if the owner reverses the 2026-10-14 ruling: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-17882-submit`. Here origin is the fork (nick-boss-tech/solr).

## Jira note (draft, not posted, optional)

"Closing the branch without a fix. The fallback in the branch's LTRQParserPlugin change does not address the reported failure. On current main, SyntheticSolrCore.initRestManager returns an uninitialized RestManager, so an LTR model lookup on a coordinator node has no model store. Whether coordinator nodes reach the LTR parser in a supported layout is not settled here, and it needs its own investigation before any fix is proposed."

Jira status: not recorded in the local packet and not checked live.

## Condition

The owner must accept the premise-dead call before labelling the branch RETIRED. The owner check is to read the two goal files the receipt cites. Retire holds either way: a supported-layout answer changes the ticket, not the branch, because the branch does not change the coordinator path.
