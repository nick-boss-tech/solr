# Pool assignment: mergeIds fix review, round 5 confirmation

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-mergeids-fix-review-round5.md.

## Scope: confirmation of the round 4 corrections only

Round 4 (reports/mergeids-fix-review-round4.md) returned STILL OPEN on branch `mergeids-skipped-shard-totals` at head `9e85ce5c162086ae806f617419c4405512792dde` (gates GREEN; the branch needs no change and has not moved), on record items only: a wrong mechanism in the sort-values scope note, a changelog link in the PR draft pointing at a file the head does not contain, and stale record statements. The main side applied the corrections; the record is reports/mergeids-fix-review-round4-response.md.

Confirm each correction below against the corrected files and the code. Nothing else is in scope; do not re-open items rounds 1 to 4 confirmed, and do not edit the branch, the drafts, or the records; flag instead.

## Corrections to confirm

1. **The mechanism, in the three response records.** reports/mergeids-fix-review-response.md (the scope note in the maximum-aggregation paragraph), reports/mergeids-fix-review-round2-response.md (item 2, closing scope sentence) and reports/mergeids-fix-review-round3-response.md (the scope note paragraph) now state that sort values gate the skip at QueryComponent.java 1180, that the declined case is not skipped and its totals do not depend on sort values, that numFound is decremented when the queue push returns false, which happens only for a document id already pushed (930 to 936, 1233), that overflow does not change numFound, and that no applied test sends a duplicate id. Confirm each passage against ShardDocQueue.push and the skip at the branch head.
2. **The ticket draft.** material/mergeids-ticket-draft.md, Mechanism: the {{numFound--}} sentence now reads "runs per document when the queue push returns false, which happens only for a document id already pushed (from any shard)". Confirm the wording and that the conclusion (it never compensates for a wholesale skip) still follows.
3. **The annotated asks.** assignments/pool-mergeids-fix-review-round4.md item 1 and reports/mergeids-fix-review-round3.md line 16 carry dated correction notes; the original ask wording is left as written in both. Confirm the notes state the push and skip facts correctly and that nothing else in those files changed in substance.
4. **The PR draft.** pr-drafts/search-components/mergeids-skipped-shard-totals.md: the changelog line is plain text stating no fragment exists on the branch yet and that it is created at packaging once the ticket is filed; the staging note's gate phrasing states the recorded scope (`:solr:core:check -x test` with tests excluded, focused class 17 of 17) instead of "full checks green". Confirm both against receipts/mergeids-fix.md and the head tree.
5. **The stale record lines.** reports/mergeids-fix-review-round3.md (the two gate-state bullets for 9e85ce5c162) now record the GREEN receipt entry; reports/mergeids-fix-review-round2.md carries the dated annotation for the 73d20a0f939 re-gate (GATE RUNNER DONE 2026-10-11T20:36:48Z); reports/combinedquerycomponent-premise.md labels cabedd1d968 as an older main snapshot, not the merge-base. Confirm each against the receipt and the commit graph.

## Deliverable

reports/mergeids-fix-review-round5.md: one line per correction (CONFIRMED, or a finding with the code or record evidence), then the verdict, **SATISFIED or STILL OPEN**. On SATISFIED, the ticket text in material/mergeids-ticket-draft.md is final for Nick's consolidated ticket email, and the PR draft at pr-drafts/search-components/mergeids-skipped-shard-totals.md is final pending his filing of the ticket and his approval of the opening. Nothing is filed, opened, or posted by the reviewer.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
