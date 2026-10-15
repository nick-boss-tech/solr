# Retire candidates, final evidence packs: index

Assignment: `assignments/pool-retire-round-final-reports-round-1.md`. Claim: `claims/pool-retire-round-final-reports-round-1.md`. Packs checked 2026-10-15 against live main 714d6ceff852 (the round's base d76fc14b9857 is superseded for the cited paths; the candidate-path files are unchanged between the two).

Owner rule in force (2026-10-14): a retired branch is labelled RETIRED in the records and left in place. Nothing is deleted. Deletion commands in the packs are options only, not due.

| Candidate | Verdict | Pack | Owner step it waits on |
|---|---|---|---|
| SOLR-17722 | RETIRE CLEAN (premise false at base; hunk overwritten on main) | `reports/retire-final-17722.md` | Label `solr-17722-submit` RETIRED and leave it. Main-side record: mark RETIRED. |
| SOLR-17882 | RETIRE CLEAN as a branch (premise-dead call needs owner acceptance) | `reports/retire-final-17882.md` | Owner accepts the premise-dead call after reading the two goal files the receipt cites, then labels `solr-17882-submit` RETIRED. Re-aiming the ticket is a separate change. |
| SOLR-12743 | RETIRE CLEAN (ConcurrentLRUCache absent on main) | `reports/retire-final-12743.md` | Label `solr-12743-submit` RETIRED. Receipt question 2 (fresh investigation against Caffeine caches) is a ticket decision, separate from the branch. |
| SOLR-17825 | RETIRE CLEAN (transitive beanutils absence settled on the cross-dc-manager lockfile) | `reports/retire-final-17825.md` | Label `solr-17825-submit` RETIRED and leave it. Optional: the SOLR-18300 Jira link, owner's call. |
| SOLR-10198 | RETIRE CLEAN (merged as e97c2a0884cb, PR #4965) | `reports/retire-final-10198.md` | Record the closeout (already in the receipt). Inventory row 388 still reads "live PR #4965"; owner may update it. Branch `solr-10198-submit` left in place. |
| SOLR-18119 (`solr-18119-submit`) | RETIRE CLEAN, CONDITIONAL: holds only while PR #5061 (`solr-18119-jvm`) lands or is accepted | `reports/retire-final-18119-submit.md` | Do not close #4999 or label the branch yet. Revisit once #5061 lands or is closed. Main still carries the python3 route (lines 77, 110). |
| SOLR-9759 | RETIRE CLEAN, superseded by PR #5048 (c6f910d3ccf, merged 2026-10-08) | `reports/retire-final-9759.md` | Label `solr-9759-submit` RETIRED. Main-side: set the takeover entry to RETIRED. The branch test's positive assertion is the only gap, not worth a job. |
| SOLR-17372 | RETIRED by owner ruling 2026-10-14; premise NOT SUPPORTED (testPercentiles passes on main without the hunk) | `reports/retire-final-17372.md` | None. Branch `solr-17372-submit` stays in place. Optional Jira note drafted, not posted. |
| SOLR-18277 | RETIRED by owner ruling 2026-10-13; no fail-before pin exists | `reports/retire-final-18277.md` | None. Branches `solr-18277-submit`, `ci/solr-18277`, `ci/solr-18277-proof` stay in place. Receipt rewritten as the completed record: `receipts/SOLR-18277.md`. Jira Fixed status unchecked (no Jira access in this lane). |

## Open items for the owner

- SOLR-18119: #4999 must not close until the #5061 check in its pack is done.
- SOLR-17882: read the two goal files the receipt cites before any deletion; they are not in the tree.
- SOLR-12743: the local packet says Jira status Open; it is a hydrated copy, not live. Check before any comment.
- SOLR-17722: only the vm3 base probe is recorded; the assignment's head and VM1 runs were never run.
- No PR exists for any of the five group A branches (`gh pr list --author nick-boss-tech --state all`, 2026-10-15).
- Jira notes in the packs are drafts only. Nothing was posted.

## Superseded

`reports/retire-packs-round-1.md` (2026-10-13) held the earlier verdicts in one file. The packs above supersede it for these nine candidates.
