# Claim: pool-newdev-critblock-review-round-1, unit 5 (SOLR-18552, 9.x line)

- Claimant host: vm5, matching a file in `hosts/`.
- Assignment: `assignments/pool-newdev-critblock-review-round-1.md`, unit 5, Blocker.
- Branch: `solr-18552-sharedarena-9x`, head `cd7cc695fb09fc8adba211992a85b5f76380b110` (ls-remote 2026-10-20T14:15Z).
- Capability tags: review, gate.
- Status: CLAIMED. Three review passes, each with a regate. The launcher and bats leg needs vm1 (bats tag), as for unit 4.
- Started: 2026-10-20T14:15:00Z (UTC).

Heartbeat: 2026-10-20T14:15:00Z

Heartbeat: 2026-10-20T14:18:00Z. Pass 1 PARTIAL at cd7cc695fb. Findings: no solr.cmd default on the 9.x line (owner question, scope widening); no changelog file on this line (owner). Launcher and bats leg for vm1 (not yet posted as its own job).

NEEDS-OWNER: 2026-10-20T14:19:00Z (vm5, rung 5, scope). Question: add the matching `solr.cmd` default (`sharedArenaMaxPermits=64`) to the 9.x line, so Windows launchers match main? Recommendation: yes; the change is one `set` line in `solr/bin/solr.cmd`, same as units 4's main-line change. Options: (a) yes, add it, (b) no, keep bin/solr only and record that the 9.x Windows launcher is out of scope. Until the owner answers, the 9.x unit records the finding and does not change the branch.

Heartbeat: 2026-10-20T14:19:00Z. Pass 2 PARTIAL at cd7cc695fb. Draft gate evidence is an 11.0 run (finding). Pass 3 next.
