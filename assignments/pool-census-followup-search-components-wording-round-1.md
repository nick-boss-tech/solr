# Assignment: search components W1 wording fixes to SATISFIED, census follow-up round 1

Status: DONE

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- Apply the wording changes each finding calls for, in the same loop, and keep reviewing until a SATISFIED verdict comes back on each item. A findings-only round does not discharge this assignment.
- Scope questions: decide under the decision default and the three-rung ladder (get information; do the hard thing; defer optional items as named follow-ups in the draft's Limits). Record each decision in the deliverable report. Only an item that survives all three rungs comes back as an owner item.
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.
- Authored text: plain language, no internal process vocabulary in public text, no "we" for Nick plus agent, no em dashes. Proof numbers only from a receipt.

## Scope

The 28 search-components drafts the W1 ready lists left on HOLD, all GATE GREEN at their live tips with SATISFIED verdicts on the code:

SOLR-5394, 7390, 7520, 8009, 8240, 8939, 10424, 10844, 11310, 11364, 11470, 12044, 13245, 14381, 14451, 14678, 14931, 15018, 15319, 15331, 15479, 15895, 16444, 17051, 17155, 17748, 17791, 17976.

Starting record, do not redo it: `reports/category-ready-lists-round-1.md`, HOLD sections, which give the exact step per draft (step key S1 to S5 at the end of that report), and the owner rulings at its top, which are applied there and stand here:

- The Proof verification date (S2) is advisory, not a hold. Where a draft's only remaining step besides S1/S3/S4 is S2, the date does not block SATISFIED; add it where it reads naturally.
- SOLR-15319 (S3): the Limits must state the SOLR-8051 landing dependency in plain words (SOLR-8051 lands first; this branch follows), with no internal landing vocabulary.
- SOLR-14381 (S4): the changelog title is cut off mid-sentence and runs about 390 characters. The title lives on the branch; if the fix is branch-side, prepare the replacement title, file a gate job for the landed head, and record the draft as held on that job rather than marking it SATISFIED.
- SOLR-17051: the draft steps are this assignment's; the stale sentence in `receipts/SOLR-17051.md` is a main-side record correction, not review-side work. Do not edit the receipt.
- SOLR-9148 is not in scope (READY; its fragment type confirmation is an opening-pass item).

Census context: `reports/openings-census-round-1.md`, search components section.

## Deliverable

`reports/census-followup-search-components-wording-round-1.md`: one line per ticket, SATISFIED (READY TO OPEN) or the named hold that survives the ladder, plus a list of every wording fix applied and every gate job posted.
