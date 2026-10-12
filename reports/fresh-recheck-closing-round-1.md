# Closing check of the fresh re-check edits, round 1

Verdict: **CONFIRMED for all three drafts.** Every edit made in the fresh re-check of the three new drafts holds against its receipt and head code. The drafts are now treated as converged on the checked text.

Scope: the edits in `be1fe6edb2e` (`reports/fresh-recheck-new-drafts-round-1.md`) to `pr-drafts/replication-backup/SOLR-6711.md`, `pr-drafts/solrcloud/SOLR-16013.md` and `pr-drafts/spellcheck/SOLR-17612.md`. Claim: `claims/pool-fresh-recheck-closing-round-1.md`. Two read-only subagents, one for SOLR-6711 and SOLR-16013, one for SOLR-17612. The draft text at HEAD matches `be1fe6edb2e` for all three.

## SOLR-6711 — CONFIRMED

- **Bold summary.** `disablepoll` takes `persist` and `enablepoll` does not. At `73c5e6e0d243`, `ReplicationHandler.java` line 312 dispatches `disablePoll` with the `persist` value, and lines 313 and 314 dispatch `enablePoll(rsp)`, which has no `persist` parameter (line 826).
- **Enable path.** At lines 828 to 831, `enablePoll` writes first and changes the flag only after the write. A failed write returns at line 829 before the flag changes. The helper returns false on an I/O error (lines 859 to 861).
- **Lock range.** `IndexFetcher.java` lines 932 to 987 cover the shared lock (line 932), the time and failure records (lines 952 to 974) and the store (line 981).
- **Follow-up offer.** "I can open a follow-up ticket and PR for it on request." is present, and the draft has no first-person plural.

## SOLR-16013 — CONFIRMED

- **Interrupt sentence.** At `408317d7e241`, `ZkController.java` sets the remembered `interrupted` flag at line 919. It is restored at lines 942 and 943, after the state-reader close (line 926), the client close (line 932), the overseer close (line 938) and the pool shutdown (line 940). The sentence "The session-ending closes do not run with the remembered interrupt set" holds.

## SOLR-17612 — CONFIRMED

- **Merge-base links.** At `e2cdb2d7e8ae`, the shard-side call to `maxResultsForSuggest` is at line 165, and the method (lines 242 to 292) reads only this core's own searcher and filters. The coordinator's call at line 404 sits inside `finishStage`. Both links cover what the sentence says.
- **Determinism sentence.** At the head, `test()` picks its request handler with `random()` (lines 120 to 123). The fixture counts are fixed, so the sentence "The fixture counts are fixed, but the test still picks its request handler at random." is accurate.
- **Pre-fix commit.** The Proof names the branch's pre-fix commit `dd2708fbb7e`, not the base. The receipt's newest section names that tree as the proof leg.
- **Limits.** The null path falls back to the coordinator's own slice (`SpellCheckComponent.java` lines 285 to 288, with the null return at lines 356 and 357). A value that is not a list is skipped (line 346), which is what the rolling-upgrade sentence says.

## Checks that held across all three

- No em dash or en dash in any draft.
- No internal process words in the outbound text.

## Convergence line

The three drafts are CONFIRMED on the edited text. Nothing further is owed on the fresh re-check.
