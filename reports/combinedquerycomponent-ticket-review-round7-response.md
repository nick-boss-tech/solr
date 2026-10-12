# Settlement: CombinedQueryComponent ticket review round 7 (main side, 2026-10-12)

Round 7 returned **STILL OPEN**. The five round 6 edits (d1 to d5) are confirmed. The full sweep applied three more edits (two firm labels and one accuracy correction), which need a confirmation pass, and it found five fix-design items that round 6 had left outside the sweep's scope. The report parks the scope question for the coordinator. This settlement rules on that question, accepts the three applied edits as the items round 8 confirms, and posts round 8 as `assignments/pool-combinedquerycomponent-ticket-review-round8.md`.

## The scope ruling

The labelling rule (the carry-through rule of rounds 3 to 7) governs factual claims about current behavior: what the code does today, what a real exit does, what a run showed. Its point is that no unrun claim about today's behavior reads as established fact.

Sentences that propose or predict a fix are proposals, not factual claims. They sit in Fix options, where the ticket asks the maintainer to consider a change that does not exist yet, and their predictions are about that change. They are outside the labelling rule's scope, provided they read as proposals: conditional or future phrasing ("if the change lands", "would contribute", and the like) that keeps the sentence plainly about the proposed change rather than about behavior anyone could observe today.

Two edges of the ruling:

- A fix-design sentence that also asserts something about today's code carries a factual claim, and the rule applies to that claim as usual. Round 7's fifth item has that shape: that the fallback fires for complete shards too is a claim about current behavior, separate from the prediction that follows it.
- A prediction phrased in the present tense, so it could be read as a statement of fact, does not meet the proviso. Such a sentence is either rephrased into proposal form or takes the label. Checking the five items for exactly this, and fixing what fails, is part of round 8.

Reasoning, under the owner's decision ladder: no information is missing (the five sentences and their contexts are on record), so nothing further is owed before ruling. The broader reading, labelling every prediction, was considered and not adopted: a "(from code reading, not run)" label on a proposal misstates what the sentence is, since it claims nothing about today's code, and it would teach a reader to discount the Fix options section as unexamined rather than as proposed. The narrower obligation is the one adopted: proposals stay out of the rule, and their phrasing has to keep them proposals.

From round 8 on, sweeps use one classification, answering round 7's closing note: a sentence is a factual claim about current behavior, a run result stated as a run, a point stated as not settled, or a proposal about a fix. The first three are the sweep's scope. Proposals are checked only for proposal phrasing, under item (b) of the round 8 assignment.

## Round 7's applied edits: accepted, round 8 confirms

The three edits applied in round 7's push are accepted into the draft as they stand, and round 8 confirms each in substance and against the code:

- **Line 44 (firm label).** "Nothing in the skip depends on a total (from code reading, not run): its inputs are..."
- **Line 56 (firm label).** "...the folds at lines 342 and 343 fire on every merge in which a shard sends a per-query entry (from code reading, not run)."
- **Lines 20 and 56 (accuracy).** A shard with no per-query entry still reaches the approximate block, and with no response header it adds the running total at line 384, not a header value. The two sentences now carry the wording "the approximateTotalHits value carried in its response header, or with no header the running total, can still be added at the merge for it (from code reading, not run)", and the earlier "contributes its approximate count" phrasing is not used.

## Round 7's optional note: not applied

The sweep's suggestion for the Reproduction lead-in ("(confirmed by run, constructed case below)") was marked optional in the report. It is not applied and is not carried as an open item, the same treatment as the optional notes in round 4. The lead-in reports the probe run that did happen; the constructed shape is stated in the Partial skip bullet itself and in the labels around it.

## State of the ticket set

The mergeIds ticket text is final (fix review round 6 returned SATISFIED) and the gradlew ticket text is final (round 4 returned SATISFIED). This CombinedQueryComponent text is the last of the three for the consolidated new-ticket email. When a round returns SATISFIED on this text, the set is complete and the consolidated email goes out with all three texts. Round 8 is scoped to be that round if it comes back clean.

## Next

Round 8 is posted as `assignments/pool-combinedquerycomponent-ticket-review-round8.md`: it confirms the three applied edits, checks the five fix-design items for proposal phrasing under the ruling above (rephrasing any present-tense factual reading, under the patch-loop directive), and repeats the full sweep for factual claims only.
