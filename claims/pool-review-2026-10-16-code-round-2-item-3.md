# Claim: code round 2, item 3 (SOLR-18541 PR 5117, short timeAllowed list)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job assignments/pool-review-2026-10-16-code-round-2.md: Status: OPEN)

- Claimant host: vm2 (Linux). Assignment: assignments/pool-review-2026-10-16-code-round-2.md, item 3.
- Requested directly by Nick Shanin, 2026-10-17: claim, patch and run item 3; the max-versus-sum choice for numFound and approximateTotalHits is left to the claimant.
- Capability tags used: implementation, gate.
- Branch: combinedquerycomponent-merge-totals at 81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d (ls-remote at claim time).
- Scope: a short per-query list from a shard (fewer entries than sub-queries) must not reach docList.get(queryIndex) in CombinedQueryComponent.mergeIds; a regression test for that shape; the focused test run at head and at the patch. The max-versus-sum choice is recorded in the output file.
- Output: reports/2026-10-16-pr-batch/SOLR-18541-check.md.
- Heartbeat: 2026-10-17T04:20Z (claim).
