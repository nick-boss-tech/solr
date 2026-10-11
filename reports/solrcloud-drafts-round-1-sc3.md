# SolrCloud drafts round 1, part SC3 (2026-10-11)

Slice: SOLR-15386, SOLR-11288, SOLR-13369, SOLR-14919, SOLR-17680. All five have an existing draft under `pr-drafts/solrcloud/`, an adopted answer and a receipt. No live PR, retire or close item sits in this slice. No new draft was needed.

Inputs read: the brief, the assignment, the claim, `material/solrcloud-round-1-answers.md` (the ticket entries, the draft corrections list, the branch and main-side lists, the DISCUSS list), `pr-formula.md`, and the five receipts. No build, test, gate, PR, Jira or branch write was made. Nothing was committed or pushed.

Worktree state: HEAD is detached at `bd71ed56f21`. The brief names origin tip `6ae4a14ff46`; that does not match, and the local `origin/pr-prepare` ref also reads `bd71ed56f21`. The edits were made on this HEAD as the brief asks.

## Head check (receipt gated head against live fork tip, `ls-remote origin refs/heads/solr-<n>-submit`)

| Ticket | Gated head (receipt) | Live tip | Result |
|---|---|---|---|
| SOLR-15386 | ca8cb61ee957 | ca8cb61ee957 | match |
| SOLR-11288 | cd094c3c623 | cd094c3c623 | match |
| SOLR-13369 | dfa0db5bdf96 | dfa0db5bdf96 | match |
| SOLR-14919 | 84e7bcaeb57 | 84e7bcaeb57 | match |
| SOLR-17680 | f4b8ce83365 | f4b8ce83365 | match |

Symptom citations use the merge-base with the local `upstream/main` ref: 15386 `e2cdb2d7e8ae`, 11288 `97d973814336`, 13369 `c3cdf7b46e8c`, 14919 `b5c71bc5573c`, 17680 `56ec140e3636`.

## Per ticket

**SOLR-15386: checked, no edit needed.** The draft already conforms to the answer: no owner-notes header, Proof date 2026-10-07 (receipt, takeover log round 35), Proof before Choice and Limits, bold openers in every section. Counts (NodeMutatorTest 3 of 3) and the inconclusive-by-construction wording match the receipt. All six code links were read at `ca8cb61ee957` and the line ranges match. The Limits claim that the distributed path runs when the Overseer is disabled is confirmed (`ZkController` passes `!overseerEnabled` to `DistributedClusterStateUpdater`). Changelog fragment title is accurate.

**SOLR-11288: corrected (citations only).**
- The two "What happens today" symptom links pointed at head lines that already contain the fix. Relinked to the merge-base: `ClusterStatus.java` L171 (the `Set.of` line) and L257-L260 (the "not found" throw), each with "code before this change".
- The "Cannot balance across a single node" link pointed at head L70-L72. Relinked to the merge-base `BalanceReplicasCmd.java` L62-L65, with "code before this change".
- Limits opener and Proof already matched the answer. Proof counts (4 of 4, recorded 2026-10-06) match the receipt.
- DISCUSS (blank-only input): the Choice poses it to the maintainer and does not settle it. Not held as a hold; the draft describes the implemented behavior and asks the question.
- Escaping gap stays in Limits with the follow-up offer. `StrUtils.splitSmart` exists at head.

**SOLR-13369: corrected (wording only).**
- Removed process vocabulary: "seed" (four uses) and "claim". Now "failing run", "1 of 1", "Six more inputs" (receipt: six further seeds, none found).
- Proof still says the proof is not obtained by run and claims no failing base run (receipt and answer).
- The SOLR-13210 suspicion in the Choice is confirmed in the ticket packet (`research/jira-context/SOLR-13369.json`).
- Test range L143-L157 matches head; `@AwaitsFix` is removed (base L66); the diff touches only the test and its changelog.

**SOLR-14919: corrected (citations only). HELD.**
- Citations: the symptom links (processor default status at base L63; forwarded `leaders`/`replicas` at base L218-L235) now point at the merge-base. The "before" half of the recovery paragraph links the base lines L215-L224. Head links are kept for code the change produces (processor L117-L130, RecoveryStrategy L302-L304, test L63-L76).
- Verified: commit `75b183196798` comments out the `commit_end_point` marker in `RecoveryStrategy` and lists SOLR-12933.
- Verified: the two-argument `StrUtils.parseBool` returns the default, so unrecognized markers take the client path (matches the Limits line).
- **HOLD.** The answers record a DISCUSS item on the RecoveryStrategy marker (ship the marker in this PR, or processor alone). The call is not taken. The draft keeps its full text, including the Choice that poses the question, and the opening stays held. No hold line was added to the draft because the brief forbids internal lines in drafts; the hold is recorded here.
- Proof counts (1 of 1, recorded 2026-10-03, base failure "invalid boolean value: leaders") match the receipt. The receipt does not cover the recovery hunk; the draft says so.

**SOLR-17680: corrected.**
- The "requires top-level router.name and router.field" symptom link now points at the merge-base `CreateAliasCmd.java` L120-L125, with "code before this change".
- The field-check paragraph was wrong about where the check lives. Rewritten: the command checks `router.name` (head L119-L126); `RoutedAlias.fromProps` fills in `router.field` for a dimensional alias (head L148-L150); a time alias's type checks it (`TimeRoutedAlias.java` head L142-L147).
- Error-text bullets corrected:
  - "Missing required parameter: router.field." is not produced by any path I traced. Replaced with the verified time-alias message ("A time routed alias requires these params."), contrasted with the base command message.
  - The "router.field but no router.name" bullet is removed. Base and head run the same v1-to-v2 path and return the same `validateRequestBody` message (base `CreateAlias.java` L209, head L217), so it is not a change.
- Proof: removed "(10 before, 11 now)", which is not in the receipt; now "pinned message count" as the receipt has it. Test range tightened to `CreateRoutedAliasTest.java` L400-L498.
- Counts (15 of 15, 13 of 13, recorded 2026-10-04) and the v2 two-router fact match the receipt and the answer. The changelog title is left as the answer says.

## Gaps and flags

1. **HOLD, SOLR-14919.** Opening held on the DISCUSS item (RecoveryStrategy marker). Not decided. The title and the recovery hunk move together if the owner rules for processor-only.
2. **DISCUSS, SOLR-11288.** Blank-only input (all nodes, or keep failing). Recommendation on record is keep as implemented; the draft poses it and does not settle it.
3. **Branch correction owed, SOLR-11288** (answers branch item 3): the changelog title does not name the blank-only change. Not a draft edit; one branch commit before opening.
4. **Branch correction owed, SOLR-14919** (answers branch item 6): the test comment at `IgnoreCommitOptimizeUpdateProcessorFactoryTest.java` L63-L66 misstates which hop carries which marker. The changelog title covers the processor change only; if the marker stays in the PR, the title must name the recovery change.
5. **SOLR-17680 error text, owner to confirm.** The category routed alias without `router.field` was not traced; the draft makes no message claim for it. The quoted base message order `[router.field, router.name]` comes from the ticket; `Set.of` order is not fixed.
6. **Answers' draft corrections already in place for this slice:** 15386 (owner-notes header, date, order), 11288/13369/14919/17680 (Limits openers). No change needed beyond the items above.

## Not verified

- SOLR-15386: the ticket's account of the "ACTIVE again only after a leader election" symptom was taken from the draft, not re-read from the ticket packet.
- SOLR-15386: the mixed-version Limits line (older Overseer ignores the flag) was not traced in code.
- SOLR-15386: the bodies of the two other `NodeMutatorTest` methods were not read (names only).
- SOLR-11288: "no direct test" for BALANCE_REPLICAS and MIGRATE_REPLICAS string inputs was checked by grep only; the tests pass collection arguments.
- SOLR-11288: the per-run failure attribution is not used in the draft (per the answer).

## Length

Text excluding link targets: 15386 about 3,280 characters; 11288 about 3,050; 13369 about 2,420; 14919 about 3,270; 17680 about 2,960. All under the 3,500 guide when links are excluded.

## Files written or edited

- `pr-drafts/solrcloud/SOLR-15386.md`: checked, unchanged.
- `pr-drafts/solrcloud/SOLR-11288.md`: corrected (citations).
- `pr-drafts/solrcloud/SOLR-13369.md`: corrected (wording).
- `pr-drafts/solrcloud/SOLR-14919.md`: corrected (citations); held.
- `pr-drafts/solrcloud/SOLR-17680.md`: corrected (citations, field-check paragraph, error text, Proof).
- `reports/solrcloud-drafts-round-1-sc3.md`: this report.
