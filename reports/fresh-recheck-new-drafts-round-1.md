# Fresh re-check of the three new PR drafts, round 1

Verdict: **all three drafts needed edits, and the edits are applied.** Each draft got an independent fresh read against its receipt and head code. The receipts' counts, dates and gate steps held in all three. The problems were in links, wording and two behaviour claims. Nothing was built, tested, posted or gated.

Scope: `assignments/pool-fresh-recheck-new-drafts-round-1.md`. Claim: `claims/pool-fresh-recheck-new-drafts-round-1.md`. Three read-only subagents, one per draft.

## SOLR-6711, `pr-drafts/replication-backup/SOLR-6711.md`: NEEDS EDIT, applied

- **Heads and links.** Every head and pre-change link is correct. The merge-base is `cabedd1d968`, confirmed.
- **Counts, dates and gate steps.** All match the receipt.
- **Edit 1, the bold summary.** The summary said `enablepoll` clears the setting, but it did not say that `enablepoll` takes no `persist` parameter. The summary now says so.
- **Edit 2, `enablepoll` write path.** The draft described only `disablepoll`. It now also covers `enablepoll`, which clears the saved setting first and changes the in-memory flag only after the write succeeds (`ReplicationHandler.java` lines 828 to 831).
- **Edit 3, the `IndexFetcher.java` range.** It covered only the lock. It now runs from line 932 to 987, which includes the store and failure-record writes.
- **Edit 4, the follow-up offer.** The Limits named the follow-up but did not offer it. It now offers one, in the house wording.
- **Ladder.** The code confirmed the lock, the flag order and the restart reset. No owner decision was needed.

## SOLR-16013, `pr-drafts/solrcloud/SOLR-16013.md`: NEEDS EDIT, applied

- **Heads, links and anchors.** All correct at `408317d7e241`. The base range covers the queue and the await, and the head range covers the submit through the restore.
- **Counts, dates and steps.** All match the receipt's newest section.
- **Edit, the interrupt sentence.** "The session-ending closes never run on an interrupted thread" overstated the code. A later interrupt during the state-reader close can set the flag again before the client closes. The sentence now says the session-ending closes do not run with the remembered interrupt set.
- **Ladder.** The two behaviour claims in this draft (slower stop, election nodes released after the wait) follow the code, so no owner confirmation is needed. The earlier open decision on them is closed on that basis.

## SOLR-17612, `pr-drafts/spellcheck/SOLR-17612.md`: NEEDS EDIT, applied

This draft needed the most work, and one choice in it departs from the assignment's own text, so it is stated plainly here.

- **Pre-change links (edit).** The assignment allowed links to the branch's pre-fix commit `dd2708fbb7e`, because the receipt's proof leg uses it. The formula asks for the merge-base. The base commit `e2cdb2d7e8ae` has the threshold logic working per core, which is the behaviour the changelog title describes. So the "today" section now links the merge-base, and the pre-fix commit appears only in the Proof, labelled as the branch's pre-fix commit, not the base.
- **Suppression sentence (edit).** The statement that the merged response suppresses the suggestion was seen only at the pre-fix commit. It is removed.
- **"The test is deterministic" (edit).** The fixture counts are fixed, but the test still picks its request handler at random. The sentence now says that.
- **Limits (edit).** The fallback sentence was not in the receipt. It now states what the code does when no shard sends a list. The rolling-upgrade sentence was too strong: old shards send no list, so the sum covers only the upgraded shards.
- **Ladder.** Decision 10 is still a DISCUSS item in the material. Whether this draft needs a choice section is for the owner to confirm. The draft carries no choice section.

## Checks that held across all three

- The receipts' counts, dates, gate steps and changelog parse results match the drafts.
- No em dash or en dash in any of the three drafts.
- No internal process words in the outbound text.
- The AI header and footer are present.

## Notes for the main side

- SOLR-17612 decision 10 (the rolling-upgrade fallback) needs an owner call on whether a choice section is required. The Limits now stand without it.
- The SOLR-17612 Proof rests on the branch's pre-fix commit, because no base run is on record. The draft says so.

## Convergence line

The three drafts are edited and need one closing check of the edited lines before they are treated as converged. That check is the next round.
