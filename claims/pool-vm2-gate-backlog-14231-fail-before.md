# Claim: SOLR-14231 fail-without-fix run for the added tests

- Claimed: 2026-10-14T05:50Z (UTC) by the vm2 gate backlog coordinator.
- Job: gates/SOLR-14231-fail-before.md
- Head: 593463f398cb96a50ea9ab4a70b26c0b9a5b1599; proof base b5c71bc5573c4e31b4cee5a7965d73587fc0ae58.
- Runner: /workspace/gates/g14231-fail-before-gate.sh (throwaway worktree /workspace/gates/solr-14231-failbefore, no push, no branch change).
- Status: DONE, 2026-10-14. The main agent confirmed the recorded rc=1 as the expected fail-before result: all three added tests fail on the proof base with the designed messages, quoted in gates/SOLR-14231-fail-before.md, whose "Main-side confirmation, 2026-10-14" section carries the evidence. Two record defects stay open with vm2: the GraphExpressionTest count restatement (XML attributes 10 tests / 4 failures against 3 failure elements; the raw XML and log are on vm2 and were not reachable from the main host) and an explanation of the base leg's class-level ObjectTracker failure. The throwaway worktree /workspace/gates/solr-14231-failbefore remains on vm2 to discard.
