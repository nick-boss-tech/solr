# SolrCloud answers pass into drafts: round roll-up

Claim: `claims/solrcloud-answers-round-1.md` (commit `f52f0504fc8`). Answers: `material/solrcloud-round-1-answers.md` (commit `007374eefe6`). Parts: `reports/solrcloud-answers-round-1-a1.md` and `reports/solrcloud-answers-round-1-a2.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Two subagents did the draft-fix pass. No build, Gradle run, or test was run. No submit branch, live PR, or comment was touched. Nothing was posted. No DISCUSS item was decided.

## Correction to the claim

The claim said the receipt-refresh branches were "already fetched". Six of them were not: SOLR-6759, 8003, 8051, 10305 and 9124 were absent from this checkout, and their first-pass holds were made against stale refs. They were fetched and re-checked this round (see `reports/receipt-refresh-round-2.md`). The claim's head table is correct.

## Draft corrections done

Seven drafts were edited by part a1 (items 1 to 5 of the answers file), and nine by part a2 (item 6 and item 7). Sixteen drafts in `pr-drafts/solrcloud/` changed, in place, with no commit.

- **Owner-notes headers removed**, with the internal "gate record" line, in `SOLR-9155`, `SOLR-13186`, `SOLR-15106` and `SOLR-15386`.
- **Recorded dates set** in the Proof lines: 9155 and 13186 to 2026-10-06; 15106 and 15386 to 2026-10-07.
- **Proof sections moved** to the formula order in `SOLR-9155` and `SOLR-15386`.
- **Bold Choice openers** added in `SOLR-15863` and `SOLR-12651`.
- **`SOLR-12651`**: the bracketed Proof line is replaced with the owed run at the live tip `f3131d1ee84`, and the earlier head `90032e274b7`.
- **`SOLR-15035`**: the Proof placeholder is reworded to the receipt's recorded outcome. No base failure line is quoted, because none can be sourced.
- **Limits openers** added in `SOLR-5813`, `11288`, `12991`, `13369`, `14919`, `17680` and `17733`.
- **`SOLR-17292`**: rewritten for the adopted node-down remedy. The Choice is removed, and the Proof says a run at the current head is owed.
- **`SOLR-15674`**: length 4,540 characters, over the 3,500 guide. Not trimmed.

## Still open after this pass

- **SOLR-17292.** The remedy is not in the branch. The head is still `e43200b0fb6`. The branch needs the commit and a re-gate, and the draft's links need re-pinning after it.
- **SOLR-12651.** A run at the live tip `f3131d1ee84` is owed before opening.
- **SOLR-15106.** The base failure text is not on record. The draft's wording names the assertion, and this stays open until confirmed.
- **SOLR-15386.** The owner's question from the receipt is still open. The answers adopt the position, so confirm before posting.
- **SOLR-15035.** The ticket summary is not confirmed, since no Jira packet is on disk.
- **SOLR-15674 length.** Over the guide. Trim only if you direct it.

## Checks

- No em or en dashes in the edited drafts.
- No process words in the edited drafts after the edits.
- Proof counts match the receipts.

## Not done

No DISCUSS decision was taken. The openings slate, which lists those decisions for you, is `reports/solrcloud-openings-slate.md`.
