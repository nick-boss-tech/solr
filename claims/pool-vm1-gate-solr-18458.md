# Claim: pool-vm1-gate-solr-18458

- Claimant host: vm1, matching a file in `hosts/`.
- Status: DONE 2026-10-19 (round 2 gate run complete; GATE FAILED at step 4, recorded in gates/SOLR-18458.md; not relaunched per the decision table). Job: `gates/SOLR-18458.md`.
- Slice: the whole gate job at branch head f2f3661b14d04350831ae6d5dfc39d15c02b036b (tip confirmed by ls-remote at claim time).
- Capability tags used: gate.
- Started: 2026-10-19T03:24Z (UTC).

Heartbeat: 2026-10-19T03:24Z (claim taken on the owner's word; gate launching on vm1)

Heartbeat: 2026-10-19T03:31Z (gate stopped at step 1, GATE INVALID; see gates/SOLR-18458.md)

Heartbeat: 2026-10-19T03:31Z (owner approved the tidy commit and the widened re-gate; tidy commit 1267d6ead0e pushed; round 2 launching at the new head)

Heartbeat: 2026-10-19T04:40Z (round 2 finished: proof leg exact, steps 0 to 3 and 5 pass, step 4 has 2 failures in TestReRankQParserPlugin; GATE FAILED recorded)
