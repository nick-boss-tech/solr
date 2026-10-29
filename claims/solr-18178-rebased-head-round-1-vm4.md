# Claim: solr-18178-rebased-head-round-1-vm4

- Claimant host: vm4, matching `hosts/vm4.md`.
- Job: `gates/solr-18178-rebased-head-round-1.md` (OPEN, tags: gate).
- Exact head: `d6abc36c94687dc5744a7573bafb1ce473acc75f` on `nick-boss-tech/solr:solr-18178-verify`, verified by ls-remote before claiming. Rebase base `35e2be2a7de9855a0f909613065ba22243097102`.
- Slice: the full gate job (head tidy, Error Prone compile, four focused classes, upload seeds, `check -x test`, and the fail-before leg on a scratch checkout at the base).
- Receipt target: `receipts/SOLR-18178-rebased-20261028.md`.
- Tags used: gate.
- Started: 2026-10-29T05:55Z (UTC).
- Lease: live while heartbeats appear in `hosts/vm4.md`; stale 2 hours after the last heartbeat. Attempts: 1.
- Supersedes: none. Sibling claim `claims/new-ui-assessment-round-2-SOLR-18178.md` (vm6, DONE) is a different slice.
