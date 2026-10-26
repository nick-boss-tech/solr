# Claim: js-client-npm-failure-cause-vm4-round-1

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: DONE.
- Job or assignment: none posted; vm4 follow-up on its own PR 5204 and PR 5221 records, at Nick's request ("gather more information").
- Slice: determine the cause of the js-client npm failure at the PR 5204 head. Records only, plus one controlled run pair.
- Capability tags used: records, gate.
- Started: 2026-10-26T03:18:10Z (UTC).

Result: 2026-10-26T03:18:10Z: cause is `NODE_ENV=production` in the vm4 shell; control leg passes with it unset. Report `reports/js-client-npm-failure-cause-round-1.md`.

DONE: 2026-10-26T03:18:10Z. Deliverables: `reports/js-client-npm-failure-cause-round-1.md`, `reports/js-client-npm-failure-cause-round-1-evidence/`.
