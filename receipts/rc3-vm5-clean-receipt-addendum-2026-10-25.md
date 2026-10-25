# Addendum to receipts/rc3-vm5-clean-receipt-round-1.md (2026-10-25, vm5)

Dated addendum; the receipt itself is not edited.

## PR #5106 (SOLR-16630, TestCoordinatorRole.testNRTRestart): status changed after the receipt

The receipt's fix-set table said the post leg was still running. Vm6's receipt `receipts/SOLR-16630.md`, section "Final-head gate, 2026-10-25", now records:

- Post leg: 10 whole-class runs of `TestCoordinatorRole` at head `a02cc7ba6b905b511c2390f023e7d89dccac1a03`, 9 of 9 methods passing in each run. testNRTRestart took 5.1 s to 22.1 s (median 11.9 s). Two head runs first failed on an NFS "Too many links" error and were excluded as environment; the rest used a local work directory.
- Base leg in the same gate: 1 run at merge base `3f5d4c5bf8`, seed 681E2A715B2CE1D3: 9 tests, 0 failures, testNRTRestart 42.7 s.

So the pre leg is mixed. The earlier pre-leg runs (2026-10-10 and 2026-10-11) failed, but this gate's base run passed. The timing contrast the proof-pair assignment relies on (about 30 s on main against about 7 s at the head) is not reproduced in this base run, and vm6's receipt says so. The gate file `gates/SOLR-16630-pr5106-final-head.md` still says OPEN. Under `assignments/rc3-proof-pair-requirement-round-1.md`, the pair is not yet complete; the post leg is done and the pre leg needs its own reading.

## Effect on the draft

`drafts/rc3-vote-email-vm5-round-1.md` says the PR's post-fix run is still in progress. That is out of date. `drafts/rc3-vote-email-vm5-round-2.md` replaces it with the accurate state.
