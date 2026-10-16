# Claim: pool-vm1-gate-8051-live-head

- Claimant host: vm1, matching `hosts/vm1.md`.
- Status: DONE.
- Job or assignment: `gates/SOLR-8051-live-head-gate.md`.
- Slice: the whole job.
- Capability tags used: gate.
- Started: 2026-10-16T03:24:04Z (UTC).

Competition check (2026-10-16T03:23Z to 03:25Z, at pr-prepare tips b270410ac7 and 6fd7676539): claims/, assignments/ and gates/ were searched by filename and by content for this head (d4fe2e6f66) and for ExactStatsCacheNoAnswerTest. No file names either, no vm2 claim covers gating this head, and no assignment names another host for it. This claim is the first for this gate.

Heartbeat: 2026-10-16T03:24:04Z

Result: 2026-10-16T03:38Z: GATE GREEN at d4fe2e6f6620e918d29eeb63fe481cf28ec9645f (log ~/workspace/tools/g8051-gate.log on vm1). All steps rc 0; focused ExactStatsCacheMergeTest 1 of 1 and ExactStatsCacheNoAnswerTest 1 of 1 from fresh JUnit XML at seed 8051C0FFEE8051; proof leg failed on base production with the premise NullPointerException for the MergeTest and passed for the NoAnswerTest, as expected. Receipt: receipts/SOLR-8051.md.

DONE: 2026-10-16T03:38Z. Deliverables: receipts/SOLR-8051.md (new top section), gates/SOLR-8051-live-head-gate.md (DONE with outcome section).
