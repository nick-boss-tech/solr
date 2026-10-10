# Pool assignment: Admin UI premise runs (SOLR-9831, SOLR-9818, SOLR-9759)

Capability tags: `selenium`. Staffing: 1. Claim path: claims/pool-admin-ui-premise-runs.md.

Background: the Admin UI round 1 audit (reports/admin-ui-round-1.md and its part reports) covered three hypothetical-reproduction branches whose Selenium tests have never run anywhere. The audits are done; what is owed is premise runs, in the audit's recommended order: 9831 first, then 9818, then 9759.

Per ticket:
- SOLR-9831 (tip f269a70e84f0): run AdminUiLoggingScreenTest on base and at the branch head. The audit found the premise holds and the fix is cosmetic; the run confirms the recorded fail-before shape ("WARN false" in the logging screen rendering) or corrects it.
- SOLR-9818 (tip 63f2d7ce9267): run AdminUiRetryPolicyTest on base and at the head. The audit found the premise holds in part (the blind replay fails for reload and ADDREPLICA calls made through the v2 client). The run records exactly which calls fail on base and pass at the head.
- SOLR-9759 (tip 31e702622dae): the audit found the branch superseded on main by a change that already merged, and recommends retiring it. Run nothing for 9759 unless the first two runs finish early and the claimant wants the confirmation; the retirement call is Nick's either way.

Deliverable: a results file at reports/admin-ui-premise-runs.md with, per run: the exact command shape, seed, outcome on base and at head, and the corrected premise statement where a run disagreed with the audit. Update the three receipts (receipts/SOLR-9831.md, receipts/SOLR-9818.md, receipts/SOLR-9759.md) with the run outcomes. No branch edits under this assignment.
