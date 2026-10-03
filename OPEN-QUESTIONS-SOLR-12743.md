# SOLR-12743: open questions (branch note, remove before the PR)

Full review: `research/branch-reviews/round-5/SOLR-12743-review.md` in the local workspace (not on this remote).

Status: obsolete. `ConcurrentLRUCache` was deleted upstream by `ec218b20cc1` (#4516, Caffeine), so this patch has no
file to apply to. The reporter also tested the equivalent 2019 patch and saw the leak continue.

Questions for the owner:

1. Retire this branch (it cannot be rebased onto anything)? Agents may not delete branches.
2. Does SOLR-12743 need a fresh investigation against the Caffeine-based caches (heap-dump approach from the ticket)?
