# Review: SOLR-12743, branch solr-12743-submit (unit 8)

Head checked: 1bb4b227dfe5e212d47a8d63ba57b74091b9e395 (ls-remote 2026-10-20T15:00Z). Receipt: receipts/SOLR-12743.md (status: RETIRED as obsolete, 2026-10-03; no gate possible).

## Outcome: ENDED, the job cannot finish on this base

- The branch's patch targets `solr/core/src/java/org/apache/solr/util/ConcurrentLRUCache.java`. That file is present on the branch head (1bb4b227df) and absent at the fork pin `498a09aea9` and on upstream main (`apache/main`, `origin/main`). The branch's history is on a different base: 2797 files differ from the pin, mostly the old `ui/` tree.
- So there is no line of code on the current base for the review to read against, and the three review passes and their regates have no target. Pass 1 was not run.
- The receipt's retirement stands: the class was removed upstream in the Caffeine migration (upstream PR #4516). The branch's own note says the equivalent 2019 patch did not stop the leak.

## Owner items (not decided here)

- Delete the banked branch `solr-12743-submit`: a branch deletion needs the owner's explicit OK under the standing rules. Not done.
- Whether SOLR-12743 needs a fresh investigation against the Caffeine-based caches, using the ticket's heap-dump approach: a new investigation is scope the owner sets. Not started.

## Gate

None. No gate can run on a base that lacks the patched class.
