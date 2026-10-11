# Response: mergeIds fix review, round 4 settlement

Round 4 (reports/mergeids-fix-review-round4.md) returned STILL OPEN on record items only. The branch needs no change and has not moved: `mergeids-skipped-shard-totals` stays at head `9e85ce5c162086ae806f617419c4405512792dde`, gates GREEN. Every correction below was verified against the code before it was applied: at the branch base, `ShardDocQueue.push` (QueryComponent.java 928 to 945) returns false only when `uniqueDoc.put` finds a shard already recorded for that document id; the non-duplicate path calls `insertWithOverflow` and returns true. The decrement `if (!shardDocQueue.push(shardDoc)) { numFound--; }` therefore fires only on a duplicate id, never on overflow. At the branch head, the skip reads `sortFieldValues.size() == 0` at line 1180, so sort values also gate the skip, a point the corrected scope sentences now carry.

## Item 1: the wrong mechanism, corrected everywhere it stood

The scope sentence in all three response records now reads, in substance: the decline covers the totals block only; sort values gate the skip at QueryComponent.java 1180; the existing tests pin the skip when sort values are absent; no test sends sort values; the declined case, sort_values on a merged partial shard, is not skipped, and its totals do not depend on sort values; numFound is decremented when the queue push returns false, which happens only for a document id already pushed (930 to 936, 1233); overflow does not change numFound; no applied test sends a duplicate id. Locations:

- reports/mergeids-fix-review-response.md, the scope note in the maximum-aggregation paragraph (was line 25).
- reports/mergeids-fix-review-round2-response.md, item 2, the closing scope sentence (was line 11).
- reports/mergeids-fix-review-round3-response.md, the scope note paragraph (was line 9).
- material/mergeids-ticket-draft.md, Mechanism (was line 39): the {{numFound--}} sentence now uses the review's suggested wording, "runs per document when the queue push returns false, which happens only for a document id already pushed (from any shard)".

Two historical asks are annotated rather than rewritten:

- assignments/pool-mergeids-fix-review-round4.md, item 1: a dated correction note follows the premise, stating the push and skip facts and that the original wording is left as written.
- reports/mergeids-fix-review-round3.md, the settlement 1 scope note request (line 16): the same dated correction is appended there, since that request is where the overflow phrasing entered the records. The request's closing list (line 52) refers to that note by name and needs no separate change.

## Item 2: the PR draft changelog line

pr-drafts/search-components/mergeids-skipped-shard-totals.md: the changelog line no longer links a file. It now reads, in plain text, that no fragment exists on the branch yet and that changelog/unreleased/SOLR-XXXXX.yml is created at packaging, once the ticket is filed and has its number. That matches the draft's own staging notes and receipt step 0 (no fragment on this branch; owed at packaging).

## Item 3: stale statements corrected to the recorded state

- The PR draft's staging note (was line 4) no longer says "full checks green". It states the recorded scope at 9e85ce5c162: `:solr:core:check -x test` passed with tests excluded, and the focused test class ran 17 of 17 in the gate's test step (re-gate 2 GREEN, GATE RUNNER DONE 2026-10-11T21:46:01Z, per receipts/mergeids-fix.md).
- reports/mergeids-fix-review-round3.md, the gate-state bullets (were lines 44 and 45): both now record the GREEN entry at receipts lines 42 to 52 and the round 2 response's GREEN lines, each marked as corrected 2026-10-11 with the original statement noted as overtaken.
- reports/mergeids-fix-review-round2.md (was line 10): a dated annotation records that the re-gate at 73d20a0f939 finished GREEN (GATE RUNNER DONE 2026-10-11T20:36:48Z), as the round 4 corrections list asked.

## Item 4: the premise label

reports/combinedquerycomponent-premise.md (was line 12): `cabedd1d968` is relabelled as an older main snapshot and an ancestor of the branch base, not the merge-base. No value in that report changes; the two component files are identical across both commits, as the report already states.

## What round 5 confirms

assignments/pool-mergeids-fix-review-round5.md is posted, scoped to these corrections only. On SATISFIED, the ticket text in material/mergeids-ticket-draft.md is final for Nick's consolidated ticket email, and the PR draft is final pending his filing of the ticket and his approval of the opening.
