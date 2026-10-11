# SolrCloud drafts round 1, part SC2

Slice: SOLR-15863, SOLR-12651, SOLR-13186, SOLR-15106, SOLR-9155. All five have an adopted answer, a receipt and an existing draft under `pr-drafts/solrcloud/`. No new drafts were needed. Worktree was clean at `bd71ed56f21` at start. Nothing committed or pushed; no builds or tests run. Fork tips checked with `ls-remote` (read only) match each receipt's gated head, except SOLR-12651 where the tip is the live tip the receipt now records as gated.

Merge-bases used for pre-change citations (against local `upstream/main`): 15863, 13186 and 9155 at `97d973814336101e12475558d7419321c743de79`; 15106 at `9b3a84b1c460981eab09d8ffaef776acc4a184f8`; 12651 at `14c7aac0d151`.

## Per ticket

**SOLR-15863: corrected.**
- Proof: "the BackupCmdTest helper" changed to "the minIndexVersion method that BackupCmdTest calls" (the new test calls `BackupCmd.minIndexVersion`).
- Checked, no change needed: head `f381fd8d4dd` matches the receipt and the fork tip; counts 6 of 6, 4 of 4, 7 of 7 match; date 2026-10-08 matches the answers; Choice already has the bold opener (answers draft correction 3 already satisfied); Limits follow answers part p2 findings 15 and 16; citations at head verified (IncrementalShardBackup L182-L187, BackupCmd L345-L351, BackupCoreAPITest L138-L158); changelog link exists at head.
- Branch correction owed (not a draft edit): the changelog title starts in lower case (answers branch correction 2, part p2 finding 17).

**SOLR-12651: corrected, draft kept held.**
- The packaging note at the top was replaced with a hold comment (HTML comment, not PR text) that says the draft is not to be opened until the hold is lifted. Remove it before posting.
- Checked: head `f3131d1ee84` matches the remote tip and the receipt's gated head; Proof 2 of 2 at `f3131d1ee846`, verified 2026-10-10, and the revert result (exactly one failure) match the receipt; Choice and Limits openers present; RestoreCmd L265-L316 and the test L131-L198 verified at head; changelog link exists at head.
- Gaps for the lead:
  1. The answers (2026-10-09) hold this draft for a run at the live tip. The receipt now records that run as green at `f3131d1ee84` (live-tip gate finished 2026-10-10). The stated reason for the hold appears met, but I did not lift the hold; the lead decides.
  2. The answers' cross-cutting date for 12651 (verified 2026-10-04) refers to the superseded older head. The draft uses 2026-10-10 from the receipt.
  3. Branch correction owed: the changelog title overstates the cleanup (answers branch correction 1, part p2 finding 9). Its replacement commit moves the head, so the head, links and Proof must be re-read after it lands.

**SOLR-13186: corrected.**
- "What happens today" cited the gated head. It now links the write-and-start check at the merge-base (`97d973814336`, L64-L70), and the text says "On the base code" and "at the merge-base".
- Test link L62-L77 changed to L62-L79 so it covers the whole method.
- Checked, no change needed: the owner-notes header and the "[date to confirm]" placeholder are already absent; Proof 1 of 1 at `b436d90d2a8`, verified 2026-10-06, matches the receipt and the answers; Limits only (no Choice), as the receipt requires; Limits opener present; head citations L70-L71 (early return), L77-L93 (removal) verified.

**SOLR-15106: corrected.**
- Test link L34-L58 changed to L34-L59 so it covers the whole method.
- Checked, no change needed: head `40b7e5d0efa` matches the remote tip and the receipt; Proof 1 of 1, verified 2026-10-07, matches the receipt and the answers; owner-notes header absent; Limits opener present; Overseer.java L768-L780, L831-L846, L451-L492, L654-L668 and OverseerTaskProcessor.java L185-L186 verified at head; the changelog title is accurate.
- Gap for the lead: the test calls `overseer.getCollectionProcessorThread()`, which this change adds. A base run therefore needs a premise tree that also carries that accessor, as SOLR-9155 needed its visibility change. The receipt records only "the premise step fails as designed" and no base failure text, so I could not confirm the premise tree. The Proof wording stands per the answers (part p3 finding 6); I did not change it.

**SOLR-9155: corrected.**
- "What happens today" cited the gated head (L1731-L1739). It now links the base generic catch at the merge-base (`97d973814336`, L1725-L1730), says "at the merge-base", and states that base rethrows IllegalStateException before the generic catch.
- Proof: the test link changed to L39-L64 (whole method). The base run is qualified as the receipt states it: "with only the getLeader visibility change the test needs".
- Checked, no change needed: head `9f08d033023` matches the remote tip and the receipt; Proof 1 of 1, verified 2026-10-06, matches; owner-notes header and "[date to confirm]" absent; Proof already in formula order (answers draft correction 5 satisfied); Choice and Limits openers present; ZkController L1675, L1725-L1739 at head, the register caller (L1485, method declares `throws Exception`) and the rejoin caller (L2608, inside a try with `catch (Exception)` at L2618), and the conflict check (L1695-L1702) verified; the changelog title is accurate.

## Gaps and holds

- Hold: SOLR-12651 stays held, as instructed. No DISCUSS item holds any draft in this slice.
- Scope: all five are draftable. No live PR, retire or close item in this slice.
- Branch corrections owed (not draft edits): 12651 changelog title (answers branch correction 1); 15863 changelog title capitalization (answers branch correction 2).
- Premise gap: SOLR-15106 base-run premise (see above).
- Length: including link URLs, drafts run 3,092 to 4,905 characters (15863 4,011; 12651 3,864; 13186 3,092; 15106 3,942; 9155 4,905). Without URLs they are 2,289 to 3,504 (9155 is the longest at 3,504). Not trimmed, because the citation links are the formula's requirement and the answers did not call for cuts.
- Not verified in this pass: ticket-text claims (for example the 12651 Solr 7.x report, the 15863 version numbers, the 9155 "ticket text names as a problem" point) were not re-read against Jira; no base or head test results were re-run, per the brief.
