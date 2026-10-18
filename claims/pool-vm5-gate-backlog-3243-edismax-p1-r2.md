# Claim: pool-vm5-gate-backlog-3243-edismax-p1-r2

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-18T20:24:06Z. Deliverables: `receipts/SOLR-3243.md`, `gates/SOLR-3243-edismax-p1.md` (Status line).
- Job or assignment: `gates/SOLR-3243-edismax-p1.md` (HANDED; this claim takes it under the rule that HANDED is not a host restriction).
- Slice: steps 4 and 5 of the job, plus the decision on the step 3 proof leg.
- Capability tags used: gate.
- Started: 2026-10-18T20:14:57Z (UTC).

Decision (claimant, 2026-10-18T20:14:57ZZ): the step 3 proof leg is accepted from the 08:47Z run (`/workspace/gates/logs/vm5/g3243-proofleg.xml/`, 43 tests, 4 failures). The job's own failure-shape rule says stop on any failure other than the new negated test with `numFound` above 0. Here the new test does fail that way (`numFound` 0 != 39). The three other failures are `testUnfieldedOpenRangeMatchesDocWithoutQfValue`, `testUnfieldedOpenRangeIsMatchAllDocs` and `testUnfieldedOpenRangeWithPhraseFields`. They assert the widening added by `24f09dc007`, which is the only parser change on the branch, so any base without it fails them by construction. No rerun: the same base gives the same result, and nothing about the input changed.

Steps 0 to 2 were run at `a07674262e` (changelog, tidy, Error Prone compile). Steps 4 and 5 run now at the same head: clean, focused class `TestExtendedDismaxParser` with seed `3243C0FFEE3243` (expect 43 of 43 from the fresh XML), then `:solr:core:check -x test`.

Heartbeat: 2026-10-18T20:14:57Z
