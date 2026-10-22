# Review: SOLR-16865, branch solr-16865-low-disk-warning (unit 34)

Head reviewed: 606eca38e9594ada4ed16e2a376ba30b0c21273a. Base: c8ab71430f. Ticket: SOLR-16865, "Solr should warn about low disk space upon startup".

## Pass 1

- Ticket fit. The ticket asks Solr to warn at startup when the data directory is low on disk space. The branch calls `warnIfDataDirLowOnSpace()` after the data directory is set in the `SolrCore` constructor, and logs a warning when usable space is under 2 percent of total (`SolrCore.java`, `isBelowFreeSpaceThreshold`). That fits the ticket.
- Finding, major: the test covers only the pure threshold helper (`isBelowFreeSpaceThreshold`, three cases). The startup path (`warnIfDataDirLowOnSpace`: the `FileStore` lookup, the warning text, and its error handling) is not exercised by any test. The ticket's behaviour is not shown by the branch. Recorded, not repaired (a new test is outside the repair scope).
- Proof leg: inconclusive by construction, because the test calls the new helper and base does not compile. This is recorded as a deviation from the expected-failure rule, not a pass.
- Finding, minor: the threshold is a fixed 2 percent constant. Fine for this ticket; recorded for the owner.
- Changelog: present, YAML parses (step 0).
- Repairs: none.

Regate: `gates/SOLR-16865-review-pass-1.md`, GATE GREEN at 606eca38e9 on vm4.

## Pass 2

Review (2026-10-22, vm4), same head 606eca38e9. - Pass 1 major finding, unchanged and confirmed in the file: the startup path `warnIfDataDirLowOnSpace` is not exercised by any test (`LowDataDirSpaceThresholdTest.java` calls only `isBelowFreeSpaceThreshold`). Recorded, not repaired.
- Pass 1 minor finding, unchanged: the fixed 2 percent constant.
- Repairs: none.

Regate: `gates/SOLR-16865-review-pass-2.md`, GATE GREEN at 606eca38e9 on vm4.

## Pass 3

Review (2026-10-22, vm4), same head 606eca38e9. - No new findings against the head. The pass 1 and pass 2 findings stand: the startup path `warnIfDataDirLowOnSpace` is not exercised by any test (major); the proof leg is inconclusive (major, a consequence); the fixed 2 percent constant (minor).
- Repairs: none.

Regate: `gates/SOLR-16865-review-pass-3.md`, GATE GREEN at 606eca38e9 on vm4.

Final head: 606eca38e9594ada4ed16e2a376ba30b0c21273a. Unit 34 DONE with the three pass sections.
