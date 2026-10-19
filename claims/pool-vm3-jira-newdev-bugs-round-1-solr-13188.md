# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-13188 (item 5 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE. No branch (not reproduced). Slice: item 5 only, SOLR-13188. Other items are not claimed by vm3.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 5.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T17:36:00Z (UTC).

## Result (vm3, 2026-10-19)

- Base: `c8ab71430f`. No branch pushed.
- Reproduction attempt: the ticket's query `q={!parent fq={!collapse field=id}&rentDocument}` on the base returns HTTP 400, not an NPE. `BlockJoinParentQParser.parse` throws a `SyntaxError` when neither `which` nor `parentPath` is given. The NPE in the ticket names `BitDocIdSetFilterWrapper`, which is not in the base.
- Test run: `BJQParserTest` at base plus a test asserting 400 for this query: 19 of 19 pass, including `testParentWithFqAndNoWhichIsBadRequest`. Seed `3A1E14DB0B2A6B32`. Log: `/workspace/gates/logs/vm3/SOLR-13188-prefix-*.log` (the run wrote the file named in `/tmp/vm3-13188-log`).
- Verdict: does not reproduce on c8ab71430f. The assignment default is to record and stop without a branch. The local test-only commit was discarded.
- Receipt: `receipts/SOLR-13188.md` on coordination.

Heartbeat: 2026-10-19T18:10:00Z

DONE: 2026-10-19. Deliverables: this claim file (result section), `receipts/SOLR-13188.md`. No branch.
