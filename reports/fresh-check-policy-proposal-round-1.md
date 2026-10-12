# Fresh check before a draft is marked converged: proposal, round 1

Verdict: **a proposal for the main side. It is not adopted.** The decision belongs to the main side. Nothing in `WORKFLOW.md` or `pr-formula.md` was changed.

Scope: `assignments/pool-fresh-check-policy-round-1.md`. Claim: `claims/pool-fresh-check-policy-round-1.md`. One read-only subagent counted the record: 115 in-scope report files on this branch, 63 of which contain a CONVERGED verdict. The subagent did not write this file, so the counts and proposal are written here from its report.

## P1. Drafts marked CONVERGED in one round and fixed in a later round

Eleven drafts were marked CONVERGED and needed 27 corrections later.

| Ticket | Marked CONVERGED | Later fix | Corrections |
|---|---|---|---|
| SOLR-11939 | core-admin round 1 (notes only) | repoint round 1; moved-heads round 1 | 14 |
| SOLR-17731 | core-admin round 1 response | core-admin round 2 (CA-R2-41); moved-heads round 1 | 2 |
| SOLR-5011 | core-admin round 1 response | core-admin round 2 (CA-R2-02) | 1 |
| SOLR-9750 | core-admin round 1 response | core-admin round 2 (CA-R2-01) | 1 |
| SOLR-12007 | core-admin round 1 response | core-admin round 2 (CA-R2-04) | 1 |
| SOLR-17708 | core-admin round 1 response | core-admin round 2 (CA-R2-42) | 1 |
| SOLR-11650 | replication-backup round 1 response | replication-backup round 2 (RB-R2-21) | 1 |
| SOLR-12085 | replication-backup round 1 response | replication-backup round 2 (RB-R2-22) | 1 |
| SOLR-10882 | streaming round 1 response | streaming round 2 (ST-R1-09, still open) | 1 |
| SOLR-16437 | solrcloud round 3 | moved-heads round 1 | 2 |
| SOLR-13943 | update-processing round 3 | landing re-check round 1 | 2 |

Not counted, for the reasons the subagent gave:
- Four drafts whose later label was reversed, with branch-side items only and no draft text change.
- Four drafts with branch-side owed items, where the draft text did not change.
- Three drafts where a same-round note was applied after the label and verified later.
- One cross-family note dated before the mark.
- The drafts that were never marked CONVERGED, including SOLR-6711, SOLR-16013, SOLR-17612, SOLR-11129, SOLR-14199, SOLR-15358 and SOLR-8576.
- The draft-fidelity family. Its fixes were applied before any convergence mark, so it adds no late fix.

Denominator: 127 distinct drafts were marked CONVERGED.

## P2. Who caught the 27 corrections

- **Named fresh check: 17 corrections on 4 drafts.** SOLR-11939 (12), SOLR-17731 (1), SOLR-16437 (2) and SOLR-13943 (2). These came from a re-check, repoint or landing report written after the mark.
- **Next convergence round: 7 corrections on 7 drafts.** These came from the next round's own re-read.
- **First pass: 3 corrections on 2 drafts.** SOLR-11939 (2) and SOLR-10882 (1). These were already on the record before the mark.
- **Second read caught 24 of 27.**

## P3. Proposal for the main side

**Standing rule.** A draft is not marked CONVERGED until a read-only fresh check, run by a subagent that did not write the draft or the round's verdict, has read it against its receipt facts (heads, counts, dates, run claims) and the head code it cites, and every mismatch is recorded as resolved. The check runs after the last edit that moves a head or a receipt, and any later move triggers it again. Owner, hold, format and landing-order items stay outside the check.

**Expected review cost.** One read-only subagent per draft marked converged. The record has 123 such marks over 18 convergence rounds, a median of 6 per round and a range of 1 to 20. That is about six extra reads per round, which fits within the cap of six at once for most rounds.

**Expected saving.** Eleven of 127 drafts were reopened, with 27 corrections. Of those, 25 are receipt-fact or head-code corrections inside the rule's scope, spread over nine drafts. A pre-mark check would have caught them before the label instead of in later rounds and re-checks. The trade is roughly 127 extra reads against nine reopened drafts. The main side should weigh that ratio.

## Convergence line

The proposal is written and not adopted. The main side decides whether to take the rule. Nothing else in the record changed.
