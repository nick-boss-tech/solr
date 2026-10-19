# Claim: pool-vm1-gate-solr-16673-rootfield.md

- Claimant host: vm1, matching a file in `hosts/`.
- Status: CLAIMED 2026-10-19T03:40Z. Job: `gates/SOLR-16673-rootfield.md`.
- Slice: the whole gate job at branch head 5fb792482fc3d8fef851513bb91eb3231409a033 (tip confirmed by ls-remote at claim time).
- Capability tags used: gate.
- Started: 2026-10-19T03:40Z (UTC). Queued on vm1 behind the SOLR-18458 round 2 gate; runners serialize on the vm1 build lock.
- Note: the step 1 base-drift question left by the vm5 run is decided by the main side: the gradle/libs.versions.toml oshai-logging prune is known base drift on this project (tidy does it on the merge-base too); the vm1 runner restores that file after tidy before judging the tree, so step 1 judges the branch files only.
Heartbeat: 2026-10-19T03:40Z (claim taken; queued)
