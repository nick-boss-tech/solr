# SUPERSEDED: do not claim this round

The owner directed on 2026-10-09 that everything goes out as
ONE final batch, handed off only after the main side finishes:
gate r5 for SOLR-13696, then the SOLR-13943 stacked test move
with its normal-mode proof. This assignment and its material
stand as the draft of that batch; a single final assignment
will replace this file, with final heads for every item,
including SOLR-13943. Nothing in this round is cancelled.

# Assignment: update processing, final round 3

Finalize what final round 2 left lacking, on both sides. The
round 2 report is reports/update-processing-final-round-2.md.
The dispositions and evidence for every item are in
material/update-processing-final-round-3.md; where the material
records a decision, it is taken, do not re-pose it as open.

Rules, as rounds 1 and 2: verify the live tip equals the named
head or hold the branch; Proof only from the named source; no
PRs opened, no comments, no submit-branch edits; draft format
per pr-formula.md (a bold one-line summary opens each section,
real blob citations at the head SHA, simple language, roughly
under 3,500 characters unless the change is multi-part). Claim
first with a commit under claims/, then drafts under
pr-drafts/update-processing/, then a report at
reports/update-processing-final-round-3.md.

## Part A: release the three held drafts

1. SOLR-16673 at d7170b12f312b364cb280b943a042707e553ea64:
   release the held scratchpad draft against this head. The
   changelog title was narrowed on the branch (the untested
   Schema Designer clause is gone). Proof notes the gate
   receipt at the parent head d5c19e64ba1b; the delta is the
   title line only.
2. SOLR-12705 at 8624b7c3238b: the settling note is appended to
   audits/update-processing/SOLR-12705.md. Write the draft
   from it, with Limits lines for audit findings 1 and 4 and a
   Limits line that processAdd removes the field when the
   mutator returns null (FieldMutatingUpdateProcessor.java
   lines 118-120 and 176-177), which the changelog does not
   mention.
3. SOLR-7022 at db357868610b92709335129ebada4646ecf11e26:
   re-verify and update the round 1 draft to this head. The
   Javadoc at DirectUpdateHandler2 now states only the proven
   interrupt cause. Evidence chain: the gate stands at the
   earlier head; the deltas since are the changelog reword and
   this comment-only Javadoc reword.

## Part B: corrections and completions to round 2 drafts

Per the material: SOLR-5754 Proof wording (the gated pair, not
"byte for byte"); SOLR-5939 class list for the 26-test combined
run; SOLR-5941 gate date and the accepted disclosed gap;
SOLR-4841 Choice kept; SOLR-6065 Choice rewritten as the
implemented 500 position with 400 posed; SOLR-5065 pin-test
fail-before claim allowed; SOLR-7504 behavior statements stay
statements; SOLR-6045 pre-fix commit confirmed, single-child
scope confirmed, mixed input stays in Limits; SOLR-14718
green-without-counts accepted; SOLR-13265 final as written.
Trim SOLR-7504 and SOLR-6045 toward the length guide without
dropping evidence; SOLR-5939 and SOLR-5941 may stay over.

## Part C: drafts for the four decided calls

Decisions are recorded in TESTING.md ("Decisions on the four
remaining calls (2026-10-09)"). Join each item when its gate
records GREEN in the main side's receipts ledger at the named
head; if the gate has not landed when the rest of the round is
done, report the item as waiting on its gate rather than
holding the round.

- SOLR-11475 at 0de48e492fd49a15f406b2a5500c337f92ef3d4d:
  gate already GREEN. New draft. The Choice poses requesting
  the other side's version; the implemented position steps
  over the sign-mismatched version. Proof states the pre-fix
  proof was inconclusive because the old code does not
  terminate.
- SOLR-16655 at 5e2317443f4116a9e97330b0d6ce175cf44b54df:
  update the round 1 draft to this head once its gate records
  GREEN. The change now gates the child-document descent on
  the parent field being selected; the Choice poses accepting
  the wider descent as the semantics. State the sequencing
  with SOLR-12705 (same production file).
- SOLR-12245 at f325d5d0576e582d4488570eabdeabc7410a8a0f:
  new draft once its gate records GREEN. The describe()
  duplicated host is fixed and the response detail stays; the
  Choice poses the logs-only alternative.
- SOLR-13696: new draft once its gate records GREEN at the
  head the ledger names (gate r4 failed and an investigation
  is running on the main side; the head may move). The Choice
  poses the scope question: the CreateAlias remote-message
  fix is folded into this branch because the repaired
  Dimensional suite cannot pass without it (a pre-existing
  defect on base; no new ticket, per the owner's rule).
  Finding 2 (routed-alias commitWithin coverage) stays
  dropped, in Limits.

## Out of scope

SOLR-13943: after the 13696 gate lands, the main side applies
the preserved test move stacked on 13696; its draft joins a
later batch. Everything from rounds 1 and 2 not named above is
final as written.
