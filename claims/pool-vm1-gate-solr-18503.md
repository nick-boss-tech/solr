# Claim: pool-vm1-gate-solr-18503.md

- Claimant host: vm1, matching a file in `hosts/`.
- Status: CLAIMED 2026-10-19T03:40Z. Job: `gates/SOLR-18503.md`.
- Slice: the whole gate job at branch head cc5b84dfcdd783ec2c2626aba4ba9931bb027893 (tip confirmed by ls-remote at claim time).
- Capability tags used: gate.
- Started: 2026-10-19T03:40Z (UTC). Queued on vm1 behind the SOLR-18458 round 2 gate; runners serialize on the vm1 build lock.
- Note: the branch tip moved after the job was written (dba48d69b4d8 to cc5b84dfcdd7) because a tidy commit ("SOLR-18503: Apply tidy formatting to TestJsonRangeFacets") landed, which is the fix for the vm4 GATE INVALID run. The job is retargeted to cc5b84dfcdd7 with the owner queueing gates on vm1.
Heartbeat: 2026-10-19T03:40Z (claim taken; queued)
