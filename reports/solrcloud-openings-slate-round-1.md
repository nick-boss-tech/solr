# SolrCloud openings slate and settlement checks: round 1

Assignment: `assignments/pool-openings-slate-and-missed-work-round-1.md`. Read at pr-prepare tip `59f3bbffe6b`, with the live tips confirmed by ls-remote on each branch. Claims: `claims/pool-openings-slate-and-missed-work-round-1.md` and `claims/pool-pr-prepare-tip-59f3bbf-round-1.md`.

Scope: read-only checks of the drafts, receipts and branch heads, plus draft corrections under `pr-drafts/solrcloud/` where the records settle a mismatch. No PR opened, closed, flipped or commented on. No branch moved or pushed. No gate run, no build, no test. Openings stay with the owner.

Summary of the slate:

| Ticket | Opening head | Verdict |
|---|---|---|
| SOLR-17680 | `52b641ca1a263589afa4f1ef0ec7b68f53c84565` | READY FOR OWNER APPROVAL |
| SOLR-17733 | `636196b7954af6b34efad54bd0cda2330ef8c7a4` | NOT READY (branch-side changelog title) |
| SOLR-9155 | `29bf373828994c622ac478a968d6cff4f4c45fa5` | READY FOR OWNER APPROVAL |
| SOLR-14919 | `8f1f4ef91e798f736eb91c9625223a654b59794a` | NOT READY (owner items) |
| SOLR-15035 | `12d7491e82cd660ad55063cb85633b7e05e1918c` | NOT READY (owed steps) |
| SOLR-15106 | `40b7e5d0efa70d61b74b9fa9ac04beb1ed397637` | NOT READY (one wording fix) |
| SOLR-15386 | `ca8cb61ee957ed728034a2e6c90532e6082ef3ff` | NOT READY (one Choice clause) |

## SOLR-17680

Head: the live tip `52b641ca1a26` matches the assignment. The earlier message-only re-gate at `ce59bbe112d` covers the same production tree but predates the v2 two-router test commit. The draft's Proof and Limits describe the live tip, and the receipt's counts (CreateRoutedAliasTest 17 of 17, CreateAliasAPITest 13 of 13) come from it. So the opening should use `52b641ca1a26`.

Draft edit applied (`pr-drafts/solrcloud/SOLR-17680.md`, Proof):
- Before: "This head changes only test code, so the counts above cover the production code."
- After: "Since the prior commit, only test code changed, so the counts above cover the same production code."

Optional, not applied: the base-run sentence has no date. The receipt gives 13:42 to 13:48 UTC on 2026-10-13.

Owed before opening: none on the branch that the records name. The changelog title and the YAML parse were checked by reading (no YAML parser on this host).

READY FOR OWNER APPROVAL, at `52b641ca1a263589afa4f1ef0ec7b68f53c84565`.

## SOLR-17733

Head: the live tip `636196b7954` matches. No head, count, date or seed mismatch in the draft or the receipt.

Owed, branch side (not done here): the changelog title on `solr-17733-submit` says the DELETE no longer fails with "The path exist ZK, delete and retry". The `localDelete=true` path still does (`ClusterFileStoreApis.java` lines 98 to 99; `DistribFileStore.java` line 536, and its test lines 210 to 213, at `636196`). The title must be narrowed to the DELETE without `localDelete=true`. The head SHA in the draft links and in `receipts/SOLR-17733.md` then needs refreshing.

NOT READY. Owed: the changelog title on the branch, then the head refresh in the draft and the receipt.

## SOLR-9155

Head: the live tip `29bf373828` is covered by the receipt. `receipts/SOLR-9155.md`, "Live tip" section, records GATE GREEN at `29bf` (changelog rc 0, tidy rc 0, compile rc 0, ZkControllerGetLeaderTest 1 of 1 with 0 failures and 0 errors, XML 05:51:35Z, core check rc 0). The reviewer diffed `dbd88c84ff..29bf373828`: only the changelog title and one restored log line changed, so the gate covers the delta.

The draft's Choice and Limits that the batch B note owed are already present (flow-control bullet, pre-interrupt bullet). No TESTING.md at head. The changelog title is accurate.

No draft edits.

READY FOR OWNER APPROVAL, at `29bf373828994c622ac478a968d6cff4f4c45fa5`.

## SOLR-14919

Head: the live tip `8f1f4ef91e` matches, and it is covered by the GATE GREEN record. The batch B hold is cleared for the gate, the 2026-10-03 Proof date, the 84e7 head and the link confirmation. The last round's recheck edits are in the draft, and the draft matches its receipts.

Held items that remain, for the owner:
- a. The changelog title "a leader forwards" is imprecise: any receiving node sets `leaders`. Changing it moves the head to a new SHA and needs a focused re-run and a receipt refresh.
- b. The test comment, lines 63 and 64, "a leader's fan-out", is the same imprecision, with the same head consequence.
- c. The draft's headline, "fail when ... is in the update chain", overstates the case. With `ignoreOptimizeOnly=true`, a plain commit returns before the parse (`8f1f4ef91e`, lines 135 to 146). That is the owner's scope call.

No draft edits.

NOT READY. Owner rules on items a, b and c.

## SOLR-15035

Head: the live tip `12d7491e82c` matches the premise head.

What the opening still waits on:
1. A settlement line that moves 15035 from HOLD to READY. `reports/solrcloud-hold-first-rung-round-1.md` is absent on every ref, and decisions round 5 still says HOLD.
2. The Proof must quote the base failure line now on record: `java.lang.AssertionError: expected:<2> but was:<null>`, run 2026-10-13, merge-base `b5c71bc5573c`.
3. Rename the receipt's 2026-10-13 "banked-patch-alone" run, which changes `AddReplicaCmd` and `CoreAdminHandler`, so that "alone" keeps one meaning.
4. Tidy `gates/SOLR-15035-base-run.md`: a stale "receipt not updated" line, and an unticked checklist.
5. Parse the changelog YAML at opening (no parser on this host).

Dependency: SOLR-11479 is READY TO OPEN after SOLR-15035 (batch A). Settling 15035 unblocks SOLR-11479, whose live tip is `6f739cfbbd38`.

No draft edits, since each owed item is a wording or record change outside the mismatch-only rule.

NOT READY. Owed: items 1 to 5 above.

## SOLR-15106

Head: the live tip `40b7e5d0efa7` matches the receipt on the head, the merge-base, the 1 of 1 result dated 2026-10-13T04:41:12Z, and the compile text.

Owed: Limits bullet 3 says "which the report names", an internal reference that means nothing to a reader. Replace it with "The returns and interrupts after the runner starts are not changed in OverseerTaskProcessor.java." The proposed text is in the round scratchpad and is not applied here, since it is a wording change.

Owner checks: the ticket claims 1200 messages, and the branch has no packet for that. The draft is 4,695 characters with URLs, above the 3,500 guide.

NOT READY. Owed: the Limits wording, and the owner's rulings on the 1200-message claim and the length.

## SOLR-15386

Head: the live tip `ca8cb61ee957` matches the assignment. The dated run names the head it ran at, `ca8cb61ee957`, and settles NodeMutatorTest 3 of 3 with zero skipped, failures and errors, on 2026-10-13 (XML 05:01:08Z).

What the dated run does not settle: a base failure (inconclusive by construction, because the constant is absent at base); a seed (the runner pins none, and the receipt's seed `15386C0FFEE15386` comes from an earlier undated run that conflicts); and the vm2 log and XML (not readable on this host).

Owed: the draft's Choice says "live node entries carry no data". The code at the head contradicts that: live node znodes are created with JSON data (Solr version, node name, roles) at `ZkController.java` lines 1318 to 1341. The clause has to be reworded to match the code, and the owner then confirms the single-check position. Packaging: no TESTING.md, changelog title accurate, PR formula conforms, length about 3,412 bytes without link targets.

No draft edit was made here. The corrected clause is a Choice position, and that is the owner's call.

NOT READY. Owed: the Choice clause reworded to match the head, the seed conflict resolved, and the owner's confirmation.

## Settlement: what this round unblocks

- SOLR-15035's settlement (items 1 to 5) unblocks SOLR-11479.
- SOLR-17733's branch changelog title is the only thing between that ticket and READY.

## Round status

Draft edits applied: one sentence in `pr-drafts/solrcloud/SOLR-17680.md`.

Owner items, collected:
1. SOLR-14919 items a, b and c, including the update-chain scope call.
2. SOLR-15106: the 1200-message claim and the length.
3. SOLR-15386: the single-check Choice and the seed conflict.
4. SOLR-15035: the HOLD-to-READY settlement line.

Owed on branches (not done here, since branch moves are excluded by the assignment): the SOLR-17733 changelog title.
