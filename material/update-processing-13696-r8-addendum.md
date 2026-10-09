# SOLR-13696 r8 addendum: the draft re-points to the changelog head

Main side, 2026-10-09. This addendum answers items 2 and 5 of the round 3 close-out report's addendum section, which held the SOLR-13696 draft at `1d0b8a0a73cd` until the new head was gated and named. It is now gated and named.

## New head

- Head: `da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9` on solr-13696-submit.
- The delta from `1d0b8a0a73cd` is exactly one commit and one file: the changelog fragment `changelog/unreleased/SOLR-13696-dimensional-routed-alias.yml` (type `fixed`, one entry covering both production fixes: the V2 CreateAlias router parameters and the time routed alias String-to-Date cast). Production and test code are byte-identical to the r7 head; gate r8's runner verified that before running.
- Gate r8 GREEN at that head (log g13696-r8-gate.log on the main side): step 0 parses the new fragment; tidy, Error Prone compile, and `:solr:core:check -x test` all exit 0; focused counts from fresh JUnit XML: CategoryRoutedAliasUpdateProcessorTest 6 of 6, DimensionalRoutedAliasUpdateProcessorTest 2 of 2, CreateAliasAPITest 13 of 13, no failures, errors, or skips.

## What changes in the draft

1. Re-point every citation to `da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9`.
2. Replace the Limits line that says no fail-before run is recorded for the create-alias fix. That line is wrong. The fail-before is the r3 investigation run on the untouched base `c3cdf7b46e8` with the awaitsfix group enabled, seed 54689CC480DC14B0: both Dimensional tests fail at CREATEALIAS with "requires these params: [router.name, router.field]". Cite it in Proof or Limits in place of the wrong line.
3. The other pre-fix heads, as supplied in the close-out answers: the String-to-Date cast proof ran on pre-fix production head `98ad9d3fcc33` (seed FB1F0CEBAAF65F30, both Dimensional tests fail with ClassCastException); the future-date test-file proof ran at `a4e0da422327` (seed 1676C9C3B647F0E6, timeout shape); the [shard] style test-file proof ran at `08f9384e47c0` (seed 9DBC31B7C732B317, final-loop assertion shape).
4. Changelog line: the branch adds the fragment named above. Remove the sentence that says the branch adds no changelog fragment.
5. Nothing else in the draft changes. The length acceptance and both fold-in Choices stand as written.
