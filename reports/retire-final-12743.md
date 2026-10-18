# Retire final pack: SOLR-12743

Verdict: RETIRE CLEAN. The branch patches a class that no longer exists on main.

Checked 2026-10-15.
- Fork tip: `origin/solr-12743-submit` = 1bb4b227dfe5e212d47a8d63ba57b74091b9e395 (ls-remote origin refs/heads/solr-12743-submit). Matches the receipt and RR1.
- Main: upstream main = 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 (live).

## Premise

From `receipts/SOLR-12743.md`:
- "NO GATE, and none is possible. The branch is RETIRED as obsolete on the main side's record."
- "the branch patches org/apache/solr/util/ConcurrentLRUCache.java, and that class was deleted upstream in the Caffeine migration (upstream PR #4516); the file is absent on current main, checked directly, so the patch has nothing to apply to."
- The reporter tested the equivalent 2019 patch and the leak continued.

Checked now: `git grep ConcurrentLRUCache upstream/main -- solr` returns no match on 714d6ceff.

Two questions stay with the owner, per the receipt:
1. Label the branch `solr-12743-submit` RETIRED and leave it in place (owner rule, 2026-10-14). The retirement stands either way.
2. Whether the ticket needs a fresh investigation against the Caffeine-based caches, using the ticket's heap-dump approach.

## What the branch holds

Merge-base 86bc6f292245. Two branch-only commits:
- `1c09b8d00fb`: changes `solr/core/src/java/org/apache/solr/util/ConcurrentLRUCache.java` to suppress cleanup while cache scans wait. Modify/delete conflict against main.
- `1bb4b227dfe`: `OPEN-QUESTIONS-SOLR-12743.md`, a handoff note marked for removal.

No build change.

## Gainable, and what is taken

- Gainable: nothing. The change reorders locks inside the deleted class. Main's replacement is `solr/core/src/java/org/apache/solr/search/CaffeineCache.java`. Nothing on main has the markAndSweep path. The change was not measured against Caffeine, which is receipt question 2.
- Taken: the obsolete finding and both questions are already in the receipt, the takeover log, and the inventory.

## Owner step

1. Label the fork branch `solr-12743-submit` RETIRED and leave it in place. This is receipt question 1, and the owner rule of 2026-10-14 says a retired branch is never deleted.
2. PR: none. No PR authored by nick-boss-tech on apache/solr has this head (`gh pr list --author nick-boss-tech --state all`, 2026-10-15).
3. Local worktree `wt\SOLR-12743` is clean and tracks origin. Its local branch is `wt-solr-12743-submit`. Leave both as they are.
4. Receipt question 2 is a ticket decision, not a branch action. Deleting the branch does not decide it.

Deletion is not the current rule. It would apply only if the owner reverses the 2026-10-14 ruling: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-12743-submit`. Here origin is the fork (nick-boss-tech/solr).

## Jira note (draft, not posted, optional)

"No fix from this branch. The patch targeted ConcurrentLRUCache, which was removed in the Caffeine cache migration (apache/solr PR 4516), so there is nothing to patch on current main. The reporter also found that the equivalent earlier patch did not stop the leak. Whether the leak still occurs with the Caffeine-based caches has not been checked."

Jira status: the local packet `research/jira-context/SOLR-12743.json` records status Open. That is a hydrated copy, not a live check. Check the live status before posting.

## Condition

The retire call holds whatever receipt question 2 finds, because the branch patches a class that is gone. If question 2 finds a leak in the Caffeine caches, the fix is a new change on the current classes, not this branch.
