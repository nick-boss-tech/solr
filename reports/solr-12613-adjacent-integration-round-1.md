# Composed adjacent follow-up candidate, integration round 1

Status: Composed and pushed, Proof Pending.

- Fork `pr-5110-followup-integration`, exact pushed/read-back head `2dd6003d4b97e2b7eeeb762af361cf510851b087`.
- Baseline `b45bae5da3a151c7b439ff4e42009c383b538fae` (main plus picker plus existing browser prerequisites).
- Inputs independently verified before composition: C2 `bd05d1ed39a005317f0a6655e4cd8fed078531b8`, C5 `96747864862bf716daf451e02a7c24194d41c267`, C3 `a331e7faf96c8c215f2d623f4865d4cbabca6492`, C4 `67cc03dfbed1866216c8cda8c31a65beb2c5672e`.
- Claim `claims/solr-12613-adjacent-integration-windows-round-1.md`; gate `gates/solr-12613-adjacent-integration-round-1.md`; receipt target `receipts/PR-5110-adjacent-integration-round-1.md`.

All four ordinary merges completed without conflicts or manual resolutions. The shared NodeTargetProxyFilterTest file auto-merged and retains both added test groups. No production source change was introduced during composition. The final tree has 17 changed paths against the UI baseline, equal to the union of the slice scopes; the four independent branches are preserved. The merge chain is 3815d5faedd5 (C2), 933d9a42dd41 (C5), 694c204eb5af (C3), then the final head (C4). Nick Shanin authored the merge commits without attribution trailers or PR references.

Static Git checks establish clean merge composition and whitespace only, not runtime compatibility. Source method inventories on this tree are 21 filter tests, 6 auth, 8 picker, 11 node-screen, 6 logging-screen (the integration gate selects five), and 2 standalone logging. There are still 20 newly authored methods relative to the relevant original baselines, not 20 passing tests.

The composed job requires actual green receipts for the four pinned slice heads before integration execution. If a slice is formatted or repaired and moves, the composed source must be refreshed and assigned a superseding gate; a receipt for a different head cannot be borrowed. The job then runs combined core/transport/API tests, focused browser contracts and neighbors, tidy/root checks, and captures visual evidence for the added log control/column/failure labels. It explicitly exercises initialization plus later selected-local pinning together. Neither the independent branch jobs nor this merge record can substitute for that result.

The logging default/All nodes decision is unchanged and remains in the unposted draft for maintainer review per Nick's direction. No public PR/comment/Jira action, upstream push or contributor-branch change occurred. Windows ran no Gradle, compiler, test, browser or runtime probe. The active goal remains incomplete pending matching Linux proof and any required repair.
