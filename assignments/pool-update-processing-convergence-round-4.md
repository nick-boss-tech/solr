# Pool assignment: update processing convergence, round 4

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-update-processing-convergence-round-4.md.

## Scope: the 10 drafts not converged in round 3

Round 3 (reports/update-processing-convergence-round-3.md) returned NOT CONVERGED: 6 of the 16 re-checked drafts converged, 10 did not, with one blocking item and 23 should-fix replacements. The main side has applied them; the record is reports/update-processing-convergence-round-3-response.md. Eighteen of the replacements were applied by the open review fixes lane (reports/review-open-fixes-lane-round-1.md) and are verified in the response; the rest, and the blocking item, are applied in the response itself. This round re-checks only the 10 drafts that were open:

SOLR-12705, SOLR-7022, SOLR-14718, SOLR-16673, SOLR-6973, SOLR-16655, SOLR-3657, SOLR-5065, SOLR-5941, SOLR-7504.

For each draft: verify every disposition in the response file against the live draft text in pr-drafts/update-processing/, and give a verdict of CONVERGED or NOT CONVERGED for that draft. Start with the drafts whose text changed most (SOLR-6973, 12705, 16655, 5941). Do not re-open the 6 converged drafts, and do not re-open settled decisions in material/update-processing-*.md or in the round 1, round 2 and round 3 responses. This round is the convergence verdict pass for these drafts; the separate re-check of the open fixes lane's other slices runs under its own assignment.

## One item to judge on the code

SOLR-6973, the behavior statement at line 17 of the draft. Round 3 held the old statement misstated and called for the owner to confirm the replacement. The main side instead verified the reading against the code and applied the replacement; the response records the reading (the merge-base signature write at SignatureUpdateProcessorFactory line 200 and the updateTerm at lines 202 to 204, the merge in DistributedUpdateProcessor.getUpdatedDocument at lines 778 and 782, the "normal fields are treated as a set" rule at AtomicUpdateDocumentMerger line 195, and the early return at the branch head, lines 165 to 179). Judge the statement as it now stands against the branch and the merge-base: confirm it states the behavior change accurately for both new and existing documents, or name the exact passage and the code it disagrees with.

## Deliverable

reports/update-processing-convergence-round-4.md: verdict first (CONVERGED or NOT CONVERGED overall), then one line per draft (converged, or the exact passage and the evidence it disagrees with). New findings, if any, are graded blocking, should-fix or note, with replacement text for anything the main side should apply.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). Read-only on drafts, receipts and branches; the only writes are the claim, the report, and heartbeat updates. No em dashes in authored text.
