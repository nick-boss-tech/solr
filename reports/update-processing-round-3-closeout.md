# Round 3 close-out report: update processing

Round: `assignments/update-processing-round-3-closeout.md` (commit `49bc048b3e9`). Material: `material/update-processing-round-3-closeout.md`. Claim: `claims/update-processing-round-3-closeout.md` (commit `7dd4e9ccf6d`). Draft format: `pr-formula.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents drafted the items. The lead agent checked their drafts against the branches, corrected what did not hold, and made the commits.

## Summary

- Six drafts written or changed, three verified without change, one waiting.
  - Released or updated (5): SOLR-7022 (released), SOLR-16655, SOLR-12705, SOLR-12245, SOLR-11475.
  - New draft (1): SOLR-13696.
  - Verified, no change (3): SOLR-6045, SOLR-16673, SOLR-5941.
  - Waiting (1), now drafted: SOLR-13943. Material item 10 needed a further addendum that names the stacked head and its gate receipt. That addendum has landed, and the draft is in the Addendum section below.
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

## Addendum: answers and SOLR-13943

Material: `material/update-processing-round-3-closeout-answers.md` (commit `e441a3a3b93`). Claim: `claims/update-processing-round-3-closeout-addendum.md` (commit `aea8e1a8afb`). The assignment says SOLR-13943 joins this close-out when that addendum lands. It has.

### Answers to the owner decisions

1. **SOLR-16655 receipt: confirmed.** Gate GREEN at `5e2317443f`, recorded 2026-10-09 08:03 MDT. Pre-fix head `b201a57fb3e5` is the parent of the fix commit, and the new test fails there. The draft cites the head, date, and counts (commit `582b2c70333`). The "no fail-before run" line is removed.
2. **SOLR-13696 pre-fix heads and the create-alias fail-before: not applied, held.** The material supplies both. The SOLR-13696 live tip has moved to `da4fa6df117`, a changelog-fragment commit whose gate (r8) is pending. The assignment's rule is to hold a branch whose live tip differs from the named head. The draft stays at `1d0b8a0a73cd`, and its Limits still says no fail-before run is recorded for the create-alias fix. That line changes when the r8 addendum names the new head.
3. **SOLR-13696 length: accepted.** No trim.
4. **SOLR-16673 title: added.** The narrowed changelog title, verbatim from `changelog/unreleased/SOLR-16673.yml` at `d7170b12f312`, as a labelled "Title:" line under the JIRA link. I checked it byte for byte. `pr-formula.md` defines no title convention, so the label is the lead's choice.
5. **SOLR-13696 changelog: held with the branch.** The fragment is in `da4fa6df117`, which is not yet gated. The draft says "this branch adds no changelog fragment," which is true at `1d0b8a0a73cd`.
6. **SOLR-13943: drafted.** See below.

### SOLR-13696 status

The draft was written at `1d0b8a0a73cd` and stands in substance. It is held for the r8 addendum: its pre-fix heads, its create-alias citation, and its changelog line change once the new head is named.

Superseded by Addendum 2 for the head and the create-alias citation.

### SOLR-13943

- Draft: `pr-drafts/update-processing/SOLR-13943.md`. Head `cc155cf68e1d8e79f1bcecd2e4c25ada864d8f53`, which matched the live tip. Stacked on `1d0b8a0a73cd`.
- The test moves from `TimeRoutedAliasUpdateProcessorTest` to a new class, `TimeRoutedAliasDateMathInStartTest`, with no `@AwaitsFix`. Its second wait now polls the provider, and its first wait is unchanged. The diff is two test files, +176 and -92. No production code changes.
- Proof: 1 of 1 in normal mode; the stack sanity counts (Category 6 of 6, Dimensional 2 of 2, CreateAliasAPITest 13 of 13); tidy, Error Prone, and `:solr:core:check -x test` exit 0. The awaitsfix result is stated plainly: `testPreemptiveCreation` fails, 5 of 6 pass, the failure is pre-existing, and this change does not fix it.
- Limits: the original class keeps its SOLR-13059 skip, no fail-before run is on record for this change, and the stacking note is in Limits.
- Lead checks at the head: the class-level `@AwaitsFix` at lines 69-70 of the original class, none on the new class, the poll loop at lines 135-159, the `testPreemptiveCreation` assertion at line 705, and the comment at lines 678-683. The lead removed one untied sentence ("earlier runs, 3 of 3") from the Proof, because the material gives no head for those runs.

### Open points

- **The 3-of-5 figure.** The material says "the round 38 spot-check on the unstacked branch failed the same test in 3 of 5 runs." The draft reads "unstacked" as the 13943 branch before it was stacked on SOLR-13696. No run at the base is on record. Confirm that reading.
- **Stack gate date.** The material gives no date for the SOLR-13943 stack gate, so the draft gives none.
- **Stacking note.** It names `1d0b8a0a73cd`. When the r8 addendum names a new SOLR-13696 head, re-point the note as the SOLR-13696 draft is re-pointed.
- **Commit subject.** The SOLR-13943 branch commit `dd83fdcccb6` has a doubled prefix, "SOLR-13943: SOLR-13943: ...". It sits on a submit branch, which this round does not edit. Raise it with the owner before the PR.

### Remaining decisions for the owner

- The SOLR-13696 r8 addendum (new head, changelog line, pre-fix heads).
- The 3-of-5 reading for SOLR-13943.
- The commit subject on `dd83fdcccb6`.

## Addendum 2: SOLR-13696 r8

- Source: `material/update-processing-13696-r8-addendum.md` at commit `81339a26ef6`.
- Live checks on 2026-10-09: `solr-13696-submit` is at `da4fa6df117`, the named head, so it matches. `solr-13943-submit` is at `cc155cf68e1d`, unchanged.
- SOLR-13696 draft: now at `da4fa6df117`.
  - Item 1: every citation is re-pointed to `da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9`. Each cited line was checked at the new head, and all still hold the same code.
  - Item 2: the Limits line that said no fail-before run is recorded now cites the investigation run on base `c3cdf7b46e8`, with the awaitsfix group enabled (seed `54689CC480DC14B0`).
  - Item 3: pre-fix heads are named where each fix is proved. Time-route cast: `98ad9d3fcc33`. Future-date repair: `a4e0da422327`. `[shard]` repair: `08f9384e47c0`.
  - Item 4: the changelog line names `changelog/unreleased/SOLR-13696-dimensional-routed-alias.yml` (type `fixed`), one entry covering both production fixes.
  - Item 5: nothing else changed. Both Choices, the length, and the other Limits items stand. The Proof heading now names the r8 head.
- SOLR-13943 stacking note: base kept at `1d0b8a0a73cd`. The stacked branch does not contain `da4fa6df117`, so the base must stay. The note now says the SOLR-13696 branch gained that commit, which touches no file this branch changes.
- Open after this addendum: the 3-of-5 reading on SOLR-13943, and the commit subject on `dd83fdcccb6`. The SOLR-13696 r8 item is closed.
