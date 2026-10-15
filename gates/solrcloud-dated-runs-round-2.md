# Gate job batch: dated focused runs for SolrCloud drafts, round 2

Status: DONE
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: dated focused runs at the live heads. No code change.
- Why: the formula requires a verification date at the head on every count. The drafts below carry only ledger or takeover-log dates ("recorded <date>"), and the gate files say those are not date sources. A dated run at the live head settles each one.

## Items

Each item: the ticket, the live head, and the class to run. The class is the one the draft's Proof names; grep the branch for its exact package before composing the run spec.

1. SOLR-5813, head `90b8baa08aef`: CloudDescriptorTest, focused run with JUnit XML. The draft cites CloudDescriptorTest 3 of 3 and the base failure.
2. SOLR-12991, head `1a86966179e`: the focused class named in `pr-drafts/solrcloud/SOLR-12991.md`'s Proof. Run it with JUnit XML.
3. SOLR-15106, head `40b7e5d0efa`: covered by vm2's claim `claims/pool-vm2-gate-backlog-15106.md` (2026-10-13 04:19 UTC), which runs the premise and the dated head run in `gates/SOLR-15106-premise-base.md`. Do not run it twice. Skip this item unless that claim goes stale.

For each item:

- Confirm the head with `ls-remote origin refs/heads/<branch>`. If the tip differs, stop that item and record the new tip.
- Record the counts from the JUnit XML, the seed and the date, and add them to `receipts/SOLR-<n>.md` with the head.

## Checklist

- [ ] item 1: head confirmed; run; receipt updated
- [ ] item 2: head confirmed; run; receipt updated
- [ ] item 3: head confirmed; run; receipt updated
- [ ] batch job DONE
