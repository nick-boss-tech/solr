# Receipt: SOLR-18601 / PR 5253 cleanup repair, round 5 owner acceptance (vm2, Linux): GREEN with accepted pre-existing failure

Head: K `21804641d0ebbb5199a1c205f031af47c9424290`. Results are those in `receipt-round5` (board `4341a139cdc`, NOT GREEN under the strict rule). This receipt records the owner's decision on that result. It does not change any test result.

## Decision

The neighbour-leg failure `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` fails identically at the base commit `32b91612edde057c19bf1a7963e8276b816551df` with seed 5253C0DE (control: 8 run, 1 failed). The owner accepted it as a pre-existing failure. Owner decision, not a waiver of any new failure. Nothing new failed in the neighbour set.

Gate status with that acceptance: tidy, focused, 5253BEEF, 5253CAFE, neighbours (apart from the accepted baseline failure), root check (`:check -x test`), `checkSiteLinks`, and negative legs A (3/3), B (4/4) and C (1/1) all pass or behave as required.

## Still open for review (not part of this acceptance)

Commit `5cbffe2f24` removes the `REQUESTDROPBUFFEREDUPDATES` core admin action, its two tests in `CoreAdminOperationTest`, and the enum entry in SolrJ `CoreAdminParams`. Not covered by this decision. The reviewer should decide whether the public action is removed.

## Evidence

Under `repair-I/`: `receipt-round5.md`, `neighbors-xml/`, `ctl-base-xml/`, `root-check.log`, `site-links.log`, `negA-hashes.txt`, `negB-hashes.txt`, `negC-hashes.txt`, `steps.txt`.

No GitHub, PR, or JIRA action taken.
