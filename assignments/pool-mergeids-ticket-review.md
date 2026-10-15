# Pool assignment: mergeIds Jira ticket draft review

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-mergeids-ticket-review.md.

## Material under review

`material/mergeids-ticket-draft.md` on this branch: a draft Bug ticket (title plus Jira wiki markup description) for the finding that `QueryComponent.mergeIds` counts a partial shard's hits in the merged totals even when that shard's documents are skipped, and still reports `numFoundExact=true`. The draft is staged verbatim from the main side's verification report; that report stays on the main side and is not on this branch. The evidence summary below is what the draft is reviewed against.

## Verification evidence (from the main side's report, CONFIRMED BY RUN)

Verified 2026-10-06 on a clean Apache Solr main snapshot, commit cabedd1d968059215188f4e7563fb303241899ed. All line numbers are at that snapshot, in `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`:

- Accumulation happens before the skip, per shard response: `approximateTotalHits` at lines 1160-1165, `maxScore` at lines 1168-1170, `numFound += docs.getNumFound()` at line 1171, and the `hitCountIsExact` fold at lines 1173-1175.
- The skip is at lines 1189-1195: `if (thisResponseIsPartial && sortFieldValues.size() == 0 && needsUnmarshalling) { continue; }`, the `if` at line 1193 and the `continue` at line 1194. A skipped shard's documents never enter the per-document queue loop.
- The only downward adjustment, `numFound--` at line 1225, runs inside that queue loop when a document is rejected, so it never compensates for a wholesale skip. `setResultIdsAndResponseDocs` writes the accumulated values into the response, including `setNumFoundExact(hitCountIsExact)` at line 1295. The merged header carries `partialResults=true` when any response was partial.
- The partial shard does not rescue the exactness flag: its `numFoundExact` comes from the DocList hit-count relation, which the partial-results state does not downgrade. In the SOLR-14758 scenario the timeout lands in `doFieldSortValues` after the shard's search completed, so the shard's own count is exact and it sends `numFoundExact=true`.
- Reproduction, merge level, using the project's own SOLR-15595 mock harness (`QueryComponentPartialResultsTest`) extended with realistic totals (the existing test never sets `numFound`, which is why this stayed invisible): shard1 partial, no `sort_values`, 2 documents, `numFound=2`, `numFoundExact=true`, `maxScore=99.0`; shard2 normal, 3 documents, `numFound=3`, `numFoundExact=true`, `maxScore=3.0`; sort on an INT field requiring unmarshalling. Result: merged list holds 3 documents (correct), merged `numFound` 5, merged `numFoundExact` true, merged `maxScore` 99.0 from the skipped shard. Seed F2C2B3B7EFFDB021; 4 tests, 3 failures (the three symptom assertions) and the document-absence control passed, from fresh JUnit XML. A full MiniSolrCloud end-to-end reproduction was not attempted; the run exercises the production merge code and the writer-side trace covers what a real partial shard sends.
- Intent: SOLR-14758 (description and all 30 comments) and SOLR-15595 were read in full. Neither mentions `numFound`, `maxScore`, or `hitCountIsExact` accounting for a skipped shard; the counting is not documented as deliberate anywhere in the two tickets that shaped this code.
- Duplicate check (2026-10-06): no existing ticket reports this issue. SOLR-15144 is the nearest cousin and is a different mechanism (empty document list under `timeAllowed`, fields-retrieval stage, no skipped-shard merge). SOLR-14004 mentions mergeIds only in an NPE log dump. SOLR-14758 is the NPE ticket that added the `sort_values` null guard; SOLR-15595 narrowed the skip. History note: the skip itself predates SOLR-14758; that ticket added the null guard (commit a72c99312380ef88fedd57d5f9806dc0f48406a1) and SOLR-15595 added the `needsUnmarshalling` condition.

## Review criteria

Check the draft in `material/mergeids-ticket-draft.md` against the evidence above and the standing text rules:

1. Title accuracy: the title states only what the evidence supports (counting of `numFound` and `maxScore`, skipped documents, `numFoundExact=true` still reported).
2. Every number and line reference in the ticket matches the evidence: the accumulation lines, the skip line, the `numFound--` line, and the reproduction values (3 documents merged, `numFound` 5, `numFoundExact` true, `maxScore` 99.0).
3. The mechanism description is complete and not overstated. It is an accuracy issue: the response header already carries `partialResults=true`, the draft says so, and there is no crash, data-loss, or security framing anywhere in the ticket.
4. Both fix options are presented fairly: testing the skip condition before accumulating the shard's totals, and setting `hitCountIsExact = false` when a shard is skipped. The first option is recommended, with the reasoning that correcting the totals when the skip fires cannot correctly undo `maxScore` (a maximum cannot be decremented; it would need per-shard tracking and a recomputation) and that `hitCountIsExact` would need recomputation as well. Verify that reasoning against the code evidence above rather than accepting it as stated: the accumulation is a running fold with no per-shard record kept, and nothing downstream recomputes the merged maximum. Nick directed the recommendation on 2026-10-11: presenting both options with neither picked was not viable, so the ticket recommends one.
5. Jira wiki markup is correct throughout: code spans in `{{...}}`, links in `[label|url]` form if any are added, bullets starting with " * ", headings `h2.` or `h3.`. No GitHub markdown anywhere in the ticket text.
6. Disclosure form: the AI assistance footer ("AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.") is present as the last element of the description, and the AI header is in the Jira underscore form, `🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_`, at the top of the description. The staged draft carries the footer; add or correct the header in the draft if it is absent or in the wrong form.
7. No internal process vocabulary in the ticket text (no gate, receipt, ledger, claim, seed as a process label, run identifiers, or review-round language). The reproduction seed stays in the evidence record, not the ticket, unless the ticket text already carries run detail at that level; judge it against the evidence, and flag rather than rewrite if unsure.
8. Authored text never uses "we" for Nick plus agent; the ticket speaks in neutral phrasing.

## Fixes, deliverable, and limits

Fix wording and markup problems directly in `material/mergeids-ticket-draft.md`. Anything where the draft and the evidence above disagree on a fact (a number, a line, a mechanism step) is flagged in the report, not silently fixed.

Deliverable: `reports/mergeids-ticket-review.md`, with the verdict first: SATISFIED or STILL OPEN. List each fix made (before and after text), each evidence disagreement flagged, and the criteria checked clean.

When the review lands SATISFIED, the main agent emails the final text of `material/mergeids-ticket-draft.md` to Nick, and Nick files the ticket. The reviewer does not file anything in Jira, does not create the ticket, and does not email anyone.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
