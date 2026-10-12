# PR drift round 1 settlement

Verdict. All seven items in `assignments/pool-pr-drift-round-1-settlement.md` are settled. Item 1 (module-check wording) is REWORDED on four bodies. Item 2 (titles) is TITLE APPLIED on three PRs, and the SOLR-14262 body has its cloud follow-up sentence removed. Every edit was read back and matched what was sent. The owner decisions in `reports/pr-drift-round-1.md` that the assignment did not settle remain open.

Inputs: the assignment, claim `claims/pool-pr-drift-settlement-round-1.md`. Two read-only reviewers did the checks. The review agent applied the edits through the REST endpoint for PR titles and bodies, one PR at a time.

## Item 1: module-check wording (REWORDED on four bodies)

Basis. Each receipt says "module check passes" at line 5 and prints no command. None of the named gate logs is on disk, so none is evidence. The body sentence was reworded to the receipt's own phrasing. Nothing else in the bodies changed.

- **PR 4968 (SOLR-18178), REWORDED.** Receipt `receipts/SOLR-18178.md` line 5.
  - Before: "Verified at head b75e7d4d3c4 on 2026-10-06 (tidy clean, Error Prone compile clean, `:solr:core:check -x test` green)."
  - After: "Verified at head b75e7d4d3c4 on 2026-10-06 (tidy clean, Error Prone compile clean, module check passes)."
  - Read-back: matches.
- **PR 5002 (SOLR-11431), REWORDED.** Receipt `receipts/SOLR-11431.md` line 5.
  - Before: "Tidy, the Error Prone compile, and `:solr:core:check -x test` are clean at this head, and the changelog entry parses as YAML."
  - After: "Tidy and the Error Prone compile are clean at this head, the module check passes, and the changelog entry parses as YAML."
  - Read-back: matches.
- **PR 5003 (SOLR-7394), REWORDED.** Receipt `receipts/SOLR-7394.md` line 5.
  - Before: "Verified at head 9857d9ee802 on 2026-10-06 (changelog YAML parse clean, tidy clean, Error Prone compile clean, `:solr:core:check -x test` green)."
  - After: "Verified at head 9857d9ee802 on 2026-10-06 (changelog YAML parse clean, tidy clean, Error Prone compile clean, module check passes)."
  - Read-back: matches.
- **PR 5004 (SOLR-16130), REWORDED.** Receipt `receipts/SOLR-16130.md` line 5. The GitHub Actions sentence on the same line is not a module-check sentence and is unchanged.
  - Before: "Verified at head 3c48dec4b79 on 2026-10-06 (tidy clean, Error Prone compile clean, `:solr:core:check -x test` green)."
  - After: "Verified at head 3c48dec4b79 on 2026-10-06 (tidy clean, Error Prone compile clean, module check passes)."
  - Read-back: matches.

## Item 2: titles (TITLE APPLIED on three PRs)

Each proposed title was checked against the branch's change at its head, using the receipt and the diff. Each read-back matched the title exactly.

- **PR 4996 (SOLR-18280), TITLE APPLIED.** The branch is test-only, changing `testUrlAllowList` in `TestReplicationHandler.java` (lines 257 to 293) and the helper that feeds it. The title names that test and the failures the ticket records.
  - Title now: "SOLR-18280: Fix `TestReplicationHandler.testUrlAllowList` failures"
  - Read-back: matches.
  - Outside item 2, not edited: body line 7 still says "is flaky and failed reproducibly", which now contradicts the title. A body correction needs its own decision.
- **PR 5003 (SOLR-7394), TITLE APPLIED.** `ShardTerms.java` lines 243 to 245 save the current term when recovery starts. Lines 275 to 283 clear that entry and write the saved term back, not 0. The title says both.
  - Title now: "SOLR-7394: Clear the recovering shard-terms entry and restore the pre-recovery term when recovery is abandoned"
  - Read-back: matches.
  - Outside item 2, not edited: the changelog at the head still omits the restore.
- **PR 5090 (SOLR-14262), TITLE APPLIED, and body sentence removed.** The title is the branch's changelog title text with the ticket key in front. `DistributedUpdateProcessor.java` lines 1174 to 1188 skip the commit and add the header at the point the title describes. The receipt records a pre-fix proof that passes, with the module check at rc 0.
  - Title now: "SOLR-14262: A commit that is skipped because the update log is not ACTIVE now reports commitIgnored in the response header instead of looking successful."
  - Read-back: matches.
  - The removed follow-up sentence (the assignment directs its removal, since the owner ruling in `reports/review-28-round-2.md` item 2 says SOLR-14262 carries no cloud follow-up):
    - Removed: " A follow-up submission for the cloud case is planned."
    - The bullet it was in now reads: "SolrCloud: the path from a leader to a replica is not traced, and no cloud test is added. This PR makes no claim about what a SolrCloud client sees when a replica skips a commit."
    - Read-back: the sentence is absent and the rest of the body is unchanged.

## Closing line

Items 1 and 2 are settled, covering seven edits across four bodies and three titles. The owner decisions in `reports/pr-drift-round-1.md` not named in the assignment remain open, and so do the two outside-scope notes above (PR 4996 body line 7, and PR 5003 changelog).
