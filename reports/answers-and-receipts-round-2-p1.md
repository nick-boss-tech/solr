# Round 2 p1: replication draft pass, items 2 to 4 (SOLR-9865, SOLR-17287)

Result: items 2 to 4 are applied to both drafts (bold openers on Choice and Limits, linked citations at the head SHAs); item 1 stays open; one factual FIX in SOLR-17287 line 17 is reported and not applied.

## Changes

Line numbers are the draft's new numbering, with the old number in brackets where it moved.

### pr-drafts/replication-backup/SOLR-9865.md

1. Choice section (item 2). Old line 29 opened with body text. Inserted at new line 29 (old body line 29 is now line 31):
   - New line 29: `**Should the rollback write back the previous directory name? This change writes it back.**`
2. Limits section (item 3). Old line 33 opened with a bullet. Inserted at new line 35 (old "## Limits" line 31 is now 33, old bullet line 33 is now 37):
   - New line 35: `**The SolrCloud restore gets the same rollback without a test, and a restore that fails during the copy is not changed.**`
3. Line 15 (same number, item 4). Citations linked at head `4937608bb181efae104c0d6f0257f445af50bf52`:
   - Old: `(`RestoreCore.java` line 210)` New: `([RestoreCore.java line 210](https://github.com/nick-boss-tech/solr/blob/4937608bb181efae104c0d6f0257f445af50bf52/solr/core/src/java/org/apache/solr/handler/RestoreCore.java#L210))`
   - Old: `(lines 232-233)` New: `([lines 232-233](.../handler/RestoreCore.java#L232-L233))`
   - Old: `(line 235)` New: `([line 235](.../handler/RestoreCore.java#L235))`
4. Line 21 (same number, item 4). Old: `The test runs a restore that succeeds`. New: `The [test](https://github.com/nick-boss-tech/solr/blob/4937608bb181efae104c0d6f0257f445af50bf52/solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java#L182-L243) runs a restore that succeeds`. The test is also cited in the Proof, so the link goes there.
5. Changelog line (old 37, new 41). Old: `` Changelog: `changelog/unreleased/SOLR-9865-restore-rollback-previous-index.yml` `` New: `Changelog: [changelog/unreleased/SOLR-9865-restore-rollback-previous-index.yml](https://github.com/nick-boss-tech/solr/blob/4937608bb181efae104c0d6f0257f445af50bf52/changelog/unreleased/SOLR-9865-restore-rollback-previous-index.yml)`

### pr-drafts/replication-backup/SOLR-17287.md

1. Choice section (item 2). Old line 31 opened with body text. Inserted at new line 31 (old body line 31 is now 33):
   - New line 31: `**Should SolrCloud restores clear the update log too? This change clears it for standalone cores only.**`
2. Limits section (item 3). Old "## Limits" line 33 is now 35. Inserted at new line 37 (old bullet line 35 is now 39):
   - New line 37: `**SolrCloud is not covered, a failed cleanup after the switch skips the clear, and concurrent updates are untested.**`
3. Line 15 (same number, item 4). Head `6957daf82610d6f09c533297cceec173a46e82a7`:
   - Old: `(`RestoreCore.java` lines 250-255)` New: `([RestoreCore.java lines 250-255](.../solr/core/src/java/org/apache/solr/handler/RestoreCore.java#L250-L255))`
   - Old: `(`UpdateLog.java` lines 2037-2072)` New: `([UpdateLog.java lines 2037-2072](.../solr/core/src/java/org/apache/solr/update/UpdateLog.java#L2037-L2072))`
4. Line 17 (same number). Old: `This also touches `UpdateLog.java`, so`. New: `This also touches [UpdateLog.java](.../solr/core/src/java/org/apache/solr/update/UpdateLog.java), so` (file link, no line range).
5. Line 23 (same number). Old: `The test adds a document` New: `The [test](.../solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java#L173-L253) adds a document`.
6. Line 25 (same number). Old: `` `UpdateLogTest.testClearAndActivateKeepsReferencedLogsAlive` tests the new method `` New: `` [`UpdateLogTest.testClearAndActivateKeepsReferencedLogsAlive`](.../solr/core/src/test/org/apache/solr/update/UpdateLogTest.java#L244-L277) tests the new method ``
7. Changelog line (old 39, new 43). Linked to `.../changelog/unreleased/SOLR-17287-restorecore-clears-updatelog.yml` at the same head.

Link text keeps the citation as written (for example "RestoreCore.java line 210"), while SOLR-12246 links only the bare file name. The URL shape is the same as in SOLR-12246.

## Not done

- Item 1 (`[CONFIRM: count]`): not filled. It stays at SOLR-9865.md line 25 and SOLR-17287.md line 27.
- Item 5 (OWNER NOTE headers): not applicable here. Neither draft has an OWNER NOTE header. The item belongs to the SOLR-8430 and SOLR-9598 drafts.
- Item 6 (SOLR-11650 re-pointing): not applicable here. Not edited.
- Item 7 (length): not applicable to the 9598, 11650 and 12246 drafts. Not edited. My two drafts are over the guide; see NOTE 2.
- FIX 1 (not applied, outside items 2 to 4). SOLR-17287.md line 17 says "It adds one method and changes no existing method. The new method has one caller, this restore path." Evidence: the diff from the merge-base `e2cdb2d7e8ae` (with upstream/main) to head `6957daf82610` adds 56 lines and deletes 0 in UpdateLog.java. It adds two methods: public `clearAndActivate` (UpdateLog.java line 2037) and private helper `discardLog` (line 2074). The one-caller claim holds (RestoreCore.java line 253 is the only caller). Replacement: "It adds two methods, the public clearAndActivate and the private helper discardLog, and changes no existing method. The public method has one caller, this restore path."
- NOTE 1 (body repeats the opener; not applied). Replacements if the lead wants the claim stated once:
  - SOLR-9865.md line 31: replace "The first writes the previous directory name back into index.properties (this change)." with "The first route is this change."
  - SOLR-9865.md line 37: replace "The SolrCloud restore calls the same method, so it gets the same rollback. This change adds no SolrCloud test." with "The SolrCloud restore calls the same method."
  - SOLR-17287.md line 33: replace "The first clears the update log only for standalone cores, after a successful restore (this change)." with "The first route is this change."
  - SOLR-17287.md line 39: "SolrCloud restores are not changed or tested here." repeats the opener at line 37. Delete the bullet, or keep it for the evidence. Lead's call.
- NOTE 2 (length, not applied). Characters with links (wc -m): SOLR-9865.md 4,265 (was 3,272 before these edits). SOLR-17287.md 4,140 (was 2,989). Reference SOLR-12246.md 3,611 with links, which the round 2 notes accept as marginal. Both of my drafts are about 600 to 800 characters over the 3,500 guide. Not trimmed. The lead decides whether they stand as complex tickets.
- Not checked: the Proof claims (fails on base, run counts, the 70 and 0 document figures). No builds, tests or runs were allowed, so these rest on the receipts only.

## Checks

- Dashes: 0 em dashes and 0 en dashes in each of the two drafts after the edits.
- Internal words: no hits for owner, receipt, gate, round, audit, handoff, or TESTING.md (case-insensitive). The only "process" hit is "Update processing and atomic updates" in SOLR-17287.md line 17, which is the JIRA component name.
- Heads named: SOLR-9865.md line 25 names head `4937608bb181`, the full SHA `4937608bb181efae104c0d6f0257f445af50bf52` matches receipts/SOLR-9865.md line 4. SOLR-17287.md line 27 names head `6957daf8261`, the full SHA `6957daf82610d6f09c533297cceec173a46e82a7` matches receipts/SOLR-17287.md line 4.
- Links: every blob link resolves at its head (`git cat-file -e` on each path, all OK). Line ranges match the head blobs: RestoreCore.java 210, 232-233, 235 and 250-255; UpdateLog.java 2037-2072; TestRestoreCore.java test methods 182-243 (SOLR-9865) and 173-253 (SOLR-17287); UpdateLogTest.java 244-277.
- Scope: `git status` shows only the two draft files modified. Nothing was committed, staged, pushed or posted. No gh write calls, builds or tests.
