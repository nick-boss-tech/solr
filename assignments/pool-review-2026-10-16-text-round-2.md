# Pool assignment: 2026-10-16 review follow-ups, PR text drafts (round 2)

Status: DONE 2026-10-18T04:37:45Z by vm4 (claims/pool-review-2026-10-16-text-round-2.md; deliverables in pr-drafts/2026-10-16-review/, index round-2-index.md)

- Capability tags: draft.
- Intended host: any host with the draft tag. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows review lane (review-round coordinator), 2026-10-17, at the direction of Nick Shanin.

These items fix PR text found in the second review pass of the 2026-10-16 cycle. Write each corrected text as a local draft under `pr-drafts/2026-10-16-review/`. Do not edit any live PR title or body. Proof numbers come only from the receipt or the gate file for that ticket, never from memory. Write in plain language, as `pr-formula.md` requires. Each item names the head its draft is written against. Re-read the live body before drafting, and record the head you used.

## Items

1. **SOLR-4374 (PR 5130): body length.** Head `433e6c6ba9905d90e9367bd9f6b566c4832b1406`. The visible body is 3,776 characters, over the 3,500 guideline. Cut it under 3,500 visible characters without dropping the compatibility disclosure. The receipt on pr-prepare cites a run at this head; the body does not cite it, so do not add it. Start from: the PR body as stored (read only) and `receipts/SOLR-4374.md`. Output: `pr-drafts/2026-10-16-review/PR-5130-text.md`.

2. **SOLR-5394 (PR 5131): body length and the fcs scope sentence.** Head `74532d0f76a6d4bb59b3e156b28f1823c5661fba`. The visible body is 3,754 characters; cut it under 3,500. The scope sentence omits that an explicit `facet.method=fcs` also applies to non-string single-valued fields such as BoolField; add that. The decision to gate this head is Nick's, so the draft must not say the head is gated. Start from: the PR body as stored (read only), and `receipts/SOLR-5394.md` if it exists. Output: `pr-drafts/2026-10-16-review/PR-5131-text.md`.

3. **SOLR-6207 (PR 5132): body length and the test's status.** Head `05cc2776d0c126e77854915f21f35b7854cf9213`. The visible body is 3,690 characters; cut it under 3,500. The test is a documentation pin, and it cannot fail on base. The receipt records that by design. If the body does not already say so, add one sentence. The receipt cites a corroboration run on another head; the body does not cite it, so do not add it. Start from: the PR body as stored (read only), and `receipts/SOLR-6207.md`. Output: `pr-drafts/2026-10-16-review/PR-5132-text.md`.

4. **SOLR-18540 (PR 5116): run date and head.** Head `58e07b4571fbbfc83c14bdd28283cd573acb0d7a`. The body and the receipt date the Linux run to 2026-10-12 and name head `58e07b`. That commit is from 2026-10-13 at 04:40 UTC. The Gradle wrapper script is byte-identical at `192aacaaf89`, so the code is unaffected. Correct the body so that the run date and head match the evidence. If the receipt is the wrong document, escalate to the main agent. Start from: the PR body as stored (read only), and `receipts/SOLR-18540.md`. Output: `pr-drafts/2026-10-16-review/PR-5116-text.md`.

5. **SOLR-18541 (PR 5117): proof paragraph, date, and the max() note.** Head `81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d`. The proof paragraph and the "verified 2026-10-15" date do not match the evidence in the head's receipt. Correct both to the receipt. The body also does not mention that `simpleCombine` takes `max()`; add that note. The `numFound` and `approximateTotalHits` wording depends on code round 2 item 3. Draft it as the code stands now, and mark it for re-draft if that item changes the code. Check the visible length, and trim under 3,500 if over. Start from: the PR body as stored (read only), and `receipts/SOLR-18541.md`. Output: `pr-drafts/2026-10-16-review/PR-5117-text.md`.

6. **SOLR-6831 (PR 5134): Limits bullet and body length.** Head `4afed04987b2343af58261b83c4b49fda0af45c0`. One Limits bullet contradicts the head. Correct it so that it matches what the head does. The visible body is 4,858 characters; trim it under 3,500. Do not claim distributed `partialResults=false` coverage until code round 2 item 1 shows a test for it. State the gap instead. Start from: the PR body as stored (read only), and `receipts/SOLR-6831.md`. Output: `pr-drafts/2026-10-16-review/PR-5134-text.md`.

7. **SOLR-6975 (PR 5135): order sentence.** Head `c666155ed058a0535f60e8c51a49c25f17821f98`. The body says the other keys decide the order when `_docid_` is a leading key. In that case the merge comparator is not transitive, so the sentence is wrong. Draft a correction that states the leading-key case plainly. If code round 1 item 3 changes the ordering behavior, re-draft after that item lands. Start from: the PR body as stored (read only), and `receipts/SOLR-6975.md`. Output: `pr-drafts/2026-10-16-review/PR-5135-text.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Live PR edits | decided: drafts only, under `pr-drafts/2026-10-16-review/`. No PATCH of any live title or body, no comment, no state change | none | Nick |
| Proof numbers | decided: only from receipts or gate files | none | Nick |
| Item 2 gated-head wording | decided: the draft does not say the head is gated | none | Nick |
| Items 5 and 7 wording that depends on code | default: draft as the code stands now and mark the draft for re-draft | if a code item changes the behavior, re-draft after that item lands | main agent |
| Item 6 distributed coverage claim | default: state the gap, not the coverage | if a test lands that covers it, escalate to the main agent before claiming it | main agent |
| Item 4 receipt is wrong | default: correct the body to the evidence | if the receipt itself is wrong, escalate to the main agent; do not edit receipts | main agent |

No row may leave the choice to the claimant.

## Deliverable

`pr-drafts/2026-10-16-review/`: one draft file per item, named as in its Output line. Each draft starts with its PR number, the head SHA it was written against, the visible character count, and the source of every proof number (receipt or gate file). Then `pr-drafts/2026-10-16-review/round-2-index.md` lists each item as drafted or skipped, with the reason.

## Limits

- No PR, comment, JIRA or email. No edit to a live title or body. No receipt edit. No force push.
- Commit as Nick Shanin with no trailers. No em dashes or en dashes.
- Proof numbers only from receipts or gate files.
