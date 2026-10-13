# Gate job batch: dated base-code runs owed by the batch A review

- Status: DONE 2026-10-13. Runs 15:26Z to 15:45Z; all four base results match the expected outcomes and are recorded in the base-run sections of receipts/SOLR-5813.md, SOLR-12991.md, SOLR-13186.md and SOLR-15863.md. Claim closed DONE.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: dated focused runs on the base code, with the branch's test file overlaid. No code change.
- Why: the batch A review found that each of these Proofs states a base-code result whose date is only a ledger or takeover-log date. A dated run on the base code settles each one. The alternative, dropping the base sentence, is the owner's call, and the reviewer has not taken it.

## Items

Each item: the ticket, the live head, the base to check out, and the class to run. Compute each base with `git merge-base upstream/main <head>` and record it. Grep the branch for the exact package before composing the run spec.

1. **SOLR-5813**, head `90b8baa08aef`. Base: the merge-base (the draft cites `cabedd1d9680`; confirm). Overlay the branch's test file. Run `CloudDescriptorTest`. Expect exactly 1 failure, `testEmptyCollectionNameDefaultsToCoreName`. Record counts, seed, date.
2. **SOLR-12991**, head `1a86966179e`. Base: the merge-base (the draft cites `97d973814336`; confirm). Overlay the branch's test file. Run `RecoveryStrategyLeaderUnreachableLogTest`. Expect exactly 1 failure on the WARN assertion. Record counts, seed, date.
3. **SOLR-13186**, head `b436d90d2a88`. Base: the merge-base. Overlay the branch's test file. Run `OverseerElectionContextTest`. Expect the failure the receipt records, "leader registration must not outlive a skipped Overseer start", once per class run as the receipt describes. Record counts, seed, date.
4. **SOLR-15863**, head `78dff1d52f6c`. Base: the merge-base. The receipt says the branch's head test calls a helper that does not exist on base, so the base compile fails. Run the class that the receipt names for that check. If the receipt does not name the class, record the gap and stop that item. Record the compiler message, counts, date.

For each item:

- Confirm the head with `ls-remote origin refs/heads/<branch>`. If it differs, stop that item and record the new tip.
- Record the failure lines, counts, seed and the run's own date in the ticket's receipt, in a base-run section.

## Checklist

- [x] item 1: head confirmed; base run with JUnit XML; receipt base-run section
- [x] item 2: head confirmed; base run with JUnit XML; receipt base-run section
- [x] item 3: head confirmed; base run with JUnit XML; receipt base-run section
- [x] item 4: head confirmed; base compile or run recorded; receipt base-run section
- [x] batch job DONE
