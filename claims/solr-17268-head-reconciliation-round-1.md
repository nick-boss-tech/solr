# Claim: solr-17268-head-reconciliation-round-1 (records)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Assignment: `assignments/solr-17268-head-reconciliation-round-1.md`. Tags: records.
- Slice: the whole assignment. Deliverable: `reports/solr-17268-head-reconciliation-round-1.md`.
- Heads named by the assignment: `9993394741` (re-gate NOT GREEN, vm2), `77a7e748e9dd` (live fork tip), `f888c1cea8` (re-gate GREEN, vm3, per the archive gate file). Neither `9993394741` nor `f888c1cea8` is reachable from the fork or apache/solr now.
- Constraints: records only. No pushes, no PR, no branch edits, no JIRA or GitHub post.
- Capability tags used: records.
- Started: 2026-10-25T04:25Z (UTC). Attempts: 1.
- Decision ladder: rung 1 (code, history and the archive gate record) settled the reachable head. The route choice (gate at the tip as-is, or rebase then gate) is the owner's, because it leads to a public PR step; it is recorded in the report as the one question.

Heartbeat: 2026-10-25T04:52Z

Result: 2026-10-25T04:52Z: report at `reports/solr-17268-head-reconciliation-round-1.md`. The only reachable full-scope head is `77a7e748e9dd`; it is ungated. The GREEN head `f888c1cea8` is unreachable, and its archive receipt count (645 insertions) does not match the tip (398), which stays unresolved in the report. The owner's question: gate the tip as-is and open from it (A), or rebase onto main, gate, then open (B).

DONE: 2026-10-25T04:52Z (records). Deliverable: the report above and this claim's Result line.
