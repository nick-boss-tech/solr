# Round 3 close-out report: update processing

Round: `assignments/update-processing-round-3-closeout.md` (commit `49bc048b3e9`). Material: `material/update-processing-round-3-closeout.md`. Claim: `claims/update-processing-round-3-closeout.md` (commit `7dd4e9ccf6d`). Draft format: `pr-formula.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents drafted the items. The lead agent checked their drafts against the branches, corrected what did not hold, and made the commits.

## Summary

- Six drafts written or changed, three verified without change, one waiting.
  - Released or updated (5): SOLR-7022 (released), SOLR-16655, SOLR-12705, SOLR-12245, SOLR-11475.
  - New draft (1): SOLR-13696.
  - Verified, no change (3): SOLR-6045, SOLR-16673, SOLR-5941.
  - Waiting (1): SOLR-13943. Material item 10 needs a further addendum that names the stacked head and its gate receipt. The close-out does not wait for it.
- Live fork tips matched the named heads for all six submit branches when the claim was made (`git ls-remote`). The SOLR-13696 head was fetched read-only and matches.
- No Gradle, builds, or tests were run. No submit branch was edited. Nothing was posted to GitHub or Jira. No PR was opened or changed.

## Per-item table

| Ticket | Head | Result | Notes |
|---|---|---|---|
| SOLR-7022 | `db357868610b` | Released | Proof cites the gate at `6a233ab2fdb` (2026-10-06, material item 1): changelog YAML ok, tidy 0, Error Prone 0, `:solr:core:check -x test` 0, and the three test classes with counts. Both wording deltas named (`5b822b7e8e9`, `db357868610b`). GitHub run `37558668356` is named as corroboration on `7940e98b0ee0`, which is `6a233ab2fdb` plus one CI workflow file. |
| SOLR-16655 | `5e2317443f` | Updated | Order stated: lands first, SOLR-12705 rebases onto it (material item 2). Reason stated as scope only: this change is confined to the child-document descent in `mutateDocument`. The date was removed (see Corrections). Owner decision 1. |
| SOLR-12705 | `8624b7c3238b` | Updated | Order stated (material item 2). Null-removal bullet narrowed: the field is dropped only when no other operation remains, so `inc` keeps it. Gate counts come from the settling note in `audits/update-processing/SOLR-12705.md`, which is the named source. |
| SOLR-12245 | `f325d5d057` | Updated | Fail-before test named: `testDistribErrorMessageNamesTheHostOnce`, added by `f325d5d0576e` and failing against the pre-fix code at `4a93167458b` (material item 3). `testDistribErrorMessageNamesTheTargetReplica` is the other new check and is not the fail-before test. |
| SOLR-11475 | `0de48e492fd` | Updated | Five PeerSync classes, 1 of 1 each, five tests, 0 failures (material item 7). Date is the receipt date, 2026-10-08. The new check runs in `PeerSyncTest` and `PeerSyncWithLeaderTest` only. Pre-fix result stays inconclusive. |
| SOLR-13696 | `1d0b8a0a73cd` | New | Gate r7 GREEN per material item 9. Two Choices, one per folded-in production fix, each posing the scope question. Limits: no Time coverage from this change; `testDateMathInStart` also skipped under SOLR-13943; no fail-before run recorded for the create-alias fix; commitWithin coverage dropped. Changelog line says the branch adds no fragment. Owner decisions 2 to 4. |
| SOLR-6045 | `e4b77fa7ae53` | Verified, no change | Matches material item 4: JSON array behavior in What, the JSON arrays Limits line present, length as accepted. |
| SOLR-16673 | `d7170b12f312` | Verified, no change | Matches material item 5: the Schema Designer Limits line is present. The draft has no title line; its bold summary paraphrases the changelog title. The material does not require a verbatim title. Owner decision 5. |
| SOLR-5941 | `a4df7bfd214b` | Verified, no change | Matches material item 6: the five un-rerun classes are DirectUpdateHandlerTest, MaxSizeAutoCommitTest, TestUpdate, SolrCmdDistributorTest, DistributedUpdateProcessorTest. |
| SOLR-13943 | stacked head not yet named | Waiting | Material item 10. Not drafted. |

## Corrections to the round 3 report

- The round 3 report's row for SOLR-16655 said "Gate GREEN 2026-10-09 (material)". The round 3 material gives no date for 16655. The 2026-10-09 date belongs to SOLR-12245. The 16655 draft no longer carries a date.
- The SOLR-12245 subagent reported that the date and the "6 of 6" count had no receipt in the material. The round 3 material's SOLR-12245 entry does record both (gate GREEN recorded 2026-10-09, DistributedUpdateProcessorTest 6 of 6), so the draft keeps them.
- The round 3 report's SOLR-7022 hold is superseded by the release in this round (material item 1).
- The round 3 report's SOLR-11475 date (2026-10-09) is superseded by the receipt date 2026-10-08 (material item 7).

## Corrections made by the lead to the subagent drafts

- SOLR-11475: the subagent's inheritance sentence was partly wrong. `PeerSyncWithLeaderAndIndexFingerprintCachingTest` extends `PeerSyncWithIndexFingerprintCachingTest`, which extends `BaseDistributedSearchTestCase`, not `PeerSyncTest`. The draft now says that. The new check runs only in `PeerSyncTest` and `PeerSyncWithLeaderTest`.
- SOLR-16655: the Proof no longer carries "verified 2026-10-09". The "narrow" judgment is removed; the draft states only the confined scope.
- SOLR-12245: the fail-before sentence now says the head test class runs against the pre-fix production code at `4a93167458b`.
- SOLR-13696: two sentences saying the fix would be split out on request were removed. The material does not state them. The Limits gained the method-level `@AwaitsFix(SOLR-13943)` on `testDateMathInStart` (`TimeRoutedAliasUpdateProcessorTest.java` line 966) and a line saying no fail-before run is recorded for the create-alias fix.

## Owner decisions

1. **SOLR-16655 receipt.** The Proof says the focused tests pass at `5e2317443f`, citing the receipts-ledger entry that the material names as the source. The ledger is not in this workspace, and the material gives no head or date for that entry. The audit `audits/update-processing/SOLR-16655.md` records a green gate only at `aa7898d972a` (2026-10-07). Confirm that the ledger receipt is at `5e2317443f`, or give its date and counts. Until then the claim rests on the material's record.
2. **SOLR-13696 changelog.** The branch adds no changelog fragment. The draft says so, following the SOLR-12864 precedent. Confirm that is acceptable, or the branch needs a fragment before the PR.
3. **SOLR-13696 length.** About 4.9 KB of prose and 7.7 KB with blob URLs, over the 3.5 KB guide. It is a multi-part change. Accept it, or trim.
4. **SOLR-13696 pre-fix heads.** The material gives no head for the pre-fix results of the time-route fix and the future-date repair. It records no pre-fix run for the create-alias fix. Name the heads, or accept the draft as written.
5. **SOLR-16673 title.** The draft has no title line. Keep it as written, or add the changelog title.
6. **SOLR-13943.** Waiting for the further addendum named in material item 10.

## Checks

- Claim `7dd4e9ccf6d` pushed before any draft work. Live tips matched the named heads for the six submit branches.
- Blob links in each draft use that draft's named head. Line ranges were checked at that head by the subagents. The lead spot-checked the SOLR-13696 `L966` skip, the SOLR-13696 changelog absence (no path in the diff against merge base `c3cdf7b46e8`), the SOLR-11475 class declarations, and the SOLR-12705 null-removal lines (`L118-L120`, `L176-L177`) at `8624b7c3238b`.
- Em dash count is 0 in all six drafts (`LC_ALL=C grep`).
- SOLR-6045, SOLR-16673, and SOLR-5941 were checked against the material and not edited.
- Not done: no builds, tests, Gradle runs, PR actions, comments, or submit-branch edits.
