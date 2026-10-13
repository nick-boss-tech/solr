# Pool assignment: decision preparation for SOLR-15357 and SOLR-15144, round 1

Capability tags: `review`. No build work. Read-only, plus the report. No code changes, no draft edits, no gate jobs. Claim under `claims/` before starting, per WORKFLOW.md.

## Purpose

Two owner items have no recorded recommendation anywhere in the records, so they cannot settle under the decision default. This assignment prepares each one to decision-ready state: the evidence read, the options priced, and one recommended option with the reasoning stated. The decision itself stays with the owner; the report is the surface he decides from.

## Item A: SOLR-15357, owner decision 2 (widen or stay narrow)

Starting record: `receipts/SOLR-15357.md`, which records the question and the state of the branch (its test-fix gate is green at the head the receipt names). The branch fixes a decoration path so copy targets are skipped in one place. The open question: widen the fix to the two sibling paths, or stay narrow.

- The real-time get decoration path, `RealTimeGetComponent.java` lines 356-357 at the branch head.
- The `fl=*` path, `SolrDocumentFetcher.java` line 866 at the branch head.

Read both paths at the branch head, and the fixed path for comparison. State, with file and line evidence:

1. What the narrow scope leaves reachable: for each sibling path, the input shape that reaches it, whether a copy target can appear there, and what a user observes when it does (wrong value shown, exception, or nothing visible).
2. What widening costs: the code change per path (same shape as the existing fix or a different one), the tests that would prove it, and any behavior beyond the defect that widening would change.
3. What staying narrow costs: the Limits wording the draft would carry and the follow-up that would be offered, in draft form inside the report.

End with one recommended option and why, stated so the owner can overrule it in one line.

## Item B: SOLR-15144, the three recorded options

Starting records: `reports/search-components-4.md` (the SOLR-15144 line and its owner-decisions list) and `reports/search-components-4-s5.md`, plus `receipts/SOLR-15144.md` if one exists. The category record holds three options, (a), (b) and (c), with no recommendation. From the records: option (a) narrows the claims to unit-level wording (the changelog title in `changelog/unreleased/SOLR-15144.yml` and the comment at `TimeAllowedLimit.java` lines 99-100 claim end-to-end behavior the receipt says does not hold; the fetch request now runs with no time limit), and option (b) widens the change to `SearchHandler`, crossing into the handler components. Extract option (c) and the full text of all three from the records rather than from this summary; if a record and this summary disagree, the record governs and the report says so.

For each option, state:

1. What it changes (code, wording, scope), at the branch head the records name.
2. What it risks: behavior changed beyond the defect, review surface created, landing interactions (option (b) crosses into sub-batch 2's handler components, per the category record's landing notes).
3. What it leaves undone, and under what name (a Limits line, a follow-up, an owner Choice in the PR).

End with one recommended option and why, stated so the owner can overrule it in one line.

## Deliverable

`reports/decision-prep-15357-15144-round-1.md`: one section per item, the evidence first, the options priced, and exactly one recommendation per item at the end of its section. Every claim about code cites the branch and file it was read from. If a starting record named here cannot be found, the report names the gap instead of reconstructing the item from memory.
