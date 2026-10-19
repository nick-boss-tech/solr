# Claim: SOLR-18555 fix round 1 (vm3)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `assignments/pool-solr-18555-fix-round-1.md`.
- Slice: items 1 to 4 and 6 (apply the starting patch, the import, tidy, push fast-forward from 884de51579, file a gate job at the new head). Item 5 (three escalated tests) is not touched.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T06:20:48Z (UTC).

Heartbeat: 2026-10-19T06:20:48Z

Note on item 2: corrected wording on claim; the import is shadowed, not dead code. Same fix.

Run: 2026-10-19T06:24:55Z: job r1-prep-062443 (log dir /workspace/gates/logs/vm3/18555/r1-prep-062443). Steps: tidy, then ErrorProne compile of :solr:core and :solr:solrj. Head: local branch on top of 884de51579 (commits c7778b917f starting patch, 71a34b3bc0 import). Not pushed yet; tidy changes, if any, are committed as item 3 before the push.
