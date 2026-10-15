# SolrCloud readiness round 1: application of the verdicts

Status: DONE

Source: `reports/solrcloud-readiness-round-1.md`. That round read all 19 SolrCloud drafts and returned 5 READY TO OPEN, 6 DECISION and 8 HOLD. This assignment applies what the round established: it confirms the ready drafts at their live heads, applies the recorded decision-ladder adoptions to the drafts that carry one, and reconciles the three items the round listed as open with no recorded recommendation. The 8 HOLD drafts are out of scope here; their items stand as the readiness report records them. Opening stays with the owner. Nothing in this assignment opens, posts, or pushes to a submit branch.

Claimable by the Windows review host under its standing coordinator claim (`claims/coordinator-review-rounds.md`). Staffing: up to 4 subagents in parallel, split by part.

## Part 1: confirm the five READY TO OPEN drafts

Drafts: SOLR-5813 (tip `90b8baa08aef`), SOLR-12991 (`1a86966179e1`), SOLR-16437 (`f72a9edd0a8b`), SOLR-17292 (`f614a42fbc80`), SOLR-11479 (`360dca60fcfa`, opens after SOLR-15035).

For each draft, verify against the live fork tip (re-read with `git ls-remote`; do not trust the tips quoted here) and its receipt in `receipts/`:

- every head citation in the draft names the live tip or the receipt's gated head, and the two agree;
- the Proof counts and outcome match the receipt exactly;
- the changelog line links the fragment at the head SHA;
- the Limits and Choices read as the readiness report read them (SOLR-5813: the reject route posed as a Choice; SOLR-12991: the WARN adoption recorded and the Choice posed; SOLR-11479: the landing order after SOLR-15035 stated).

Patch mechanical drift directly in the draft under the patch-loop directive and commit the patch on pr-prepare, then re-check the patched draft in the same round. One fix is already named by the readiness report: SOLR-11479's draft line 22 carries a run-on ("passes.- Base commit"); correct the punctuation. Anything that is not mechanical (a claim the receipt does not support, a Choice whose posture changed) is not patched; it is reported as a finding with exact replacement text.

Report each draft as READY CONFIRMED (naming any patch applied) or NOT READY with the reason.

## Part 2: apply the recorded ladder adoptions to the DECISION drafts that carry one

Four of the six DECISION drafts carry a recorded adoption in the readiness report's Decisions section. Apply each adoption to the draft text exactly as recorded, then re-check the patched draft in the same round:

- SOLR-14919: the processor alone ships; the RecoveryStrategy marker does not. The draft text must state that position as settled, not pose it as an open question.
- SOLR-11288: blank-only stays equal to omitted. The draft text must state that position as settled.
- SOLR-15386: the cached live-node check is the position the answers adopted. The draft text must state it as settled.
- SOLR-16013: the history rewrite stands approved without squashing, and it is already executed (head `408317d7e241`, re-gated green). There is nothing to decide in the draft; verify the draft reflects the current head and the settled position, and patch only citation drift if any is found.

Two limits on this part. First, some adoptions also need branch work (SOLR-11288's changelog title commit, after which the head moves and a re-gate is owed; SOLR-14919's branch commit removing the marker, plus its gate). Branch work is not review-side work: do not attempt it. Where an adoption needs it, name the branch follow-up in the deliverable as a gate-side item, with the exact branch change the adoption requires, and mark the draft's application as DRAFT APPLIED, BRANCH ITEM NAMED. Second, the remaining two DECISION drafts, SOLR-13369 and SOLR-9155, carry no recorded adoption; they are Part 3 items and get no edits here.

Report each of the four as APPLIED AND RE-CHECKED, DRAFT APPLIED, BRANCH ITEM NAMED, or NOT APPLIED with the reason.

## Part 3: the three items the readiness report listed as open

### SOLR-12651: reconcile the readiness reading against the category record

The readiness report lists SOLR-12651 (HOLD in its table) among the open items "with no recorded recommendation": the Choice on optional restore cleanup, and the order after SOLR-9598. The category record says otherwise. `material/solrcloud-round-1-answers.md`, entry for SOLR-12651, records as ADOPTED: delete-on-failure stays the implemented position, and the ticket thread's retry-and-resume question (whether cleanup should be optional) stays in the draft's Choice, as drafted. The draft's Choice section poses exactly that question to the maintainer, and its Limits state the landing after SOLR-9598.

Reconcile the two readings and report which governs, with the citations for both. The expected outcome is that the record governs: the Choice posture is an adopted position, not an undecided item, and the readiness report's open listing is a misreading of the record. If the reconciliation finds the record does not say that, report what it does say instead. Do not edit the SOLR-12651 draft beyond a citation fix if the draft misstates the record; if the readiness report itself needs a correction note, draft the note's exact text in the deliverable rather than editing the report.

### SOLR-13369 and SOLR-9155: state what would settle each

No recommendation is recorded for either, and this assignment does not decide them. For each, state what information would settle it (the decision ladder's first rung: where information is missing, get more information):

- SOLR-13369 (tip `dfa0db5bdf96`): the slate asks to confirm the Choice; the proof is by construction. Name the specific fact, run, or maintainer signal whose absence keeps the Choice unconfirmed, and where that fact could be obtained.
- SOLR-9155 (tip `9f08d0330233`): the declared-`InterruptedException` route, with no recommendation recorded; the readiness report also asks to confirm the recorded date (2026-10-06). Name what would settle the route question, separately from the date confirmation.

Report each as INFORMATION NAMED, with the settling information stated concretely enough that a follow-up assignment could go and get it.

## Deliverable

`reports/solrcloud-readiness-round-1-application.md`: per-draft outcomes for all five Part 1 drafts, all four Part 2 drafts, and the three Part 3 items, each with its verdict line from the vocabularies above, the evidence read (live tip, receipt lines, draft lines), any patch committed (with its pr-prepare commit), and any branch follow-up named for the gate side. Note A of the readiness report (the two history rewrites awaiting the owner's confirmation) is the owner's item and stays out of this deliverable except as a one-line pointer.
