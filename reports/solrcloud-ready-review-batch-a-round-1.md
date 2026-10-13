# SolrCloud READY drafts, batch A, round 1: verdicts

Verdicts: one SATISFIED (SOLR-11479, READY TO OPEN after SOLR-15035), and four held by owner items, each needing a dated base-code run or a decision to drop the base sentence. No PR was opened, closed, flipped or commented on. Gate jobs only as files under `gates/`.

Four review agents read the drafts against live heads, receipts, and the code at the cited SHAs. Two agents covered the five drafts, and every citation in them was checked at its SHA.

## SOLR-5813 (PR draft `pr-drafts/solrcloud/SOLR-5813.md`)

- Verdict: **OWNER ITEM.** The base-failure count has no run date.
- Live head `90b8baa08aef` matches the receipt. The dated head run landed: CloudDescriptorTest 3 of 3, JUnit XML 2026-10-13 06:01 UTC. The Proof now says "verified 2026-10-13".
- Held by: the base-code count "3 tests with exactly 1 failure" is dated only by the 2026-10-07 takeover-log entry, which the gate note says is not a date source. Owed: a dated run on the merge-base with the branch's test file overlaid, gate job `gates/solrcloud-base-dated-runs-batch-a.md`, item 1.
- Edits: the Proof date; "Its only main-source caller is `reload`" became "Its only caller in the main code is `reload`".

## SOLR-11479 (PR draft `pr-drafts/solrcloud/SOLR-11479.md`)

- Verdict: **SATISFIED. READY TO OPEN after SOLR-15035.**
- Live head `6f739cfbbd3` matches the receipt, refreshed by the carry-over job. The trees are identical (`eb90a93c38c7`).
- Edits: the count is dated to the verified run, and the tip is noted as having the same file contents. The landing order now says this PR opens only after SOLR-15035 and lands after it.
- Caveat: SOLR-15035 is still HOLD in the round 5 decisions. Its receipt at `12d7491e82c` shows a green gate and dated base runs. READY TO OPEN holds only once SOLR-15035 is READY.

## SOLR-12991 (PR draft `pr-drafts/solrcloud/SOLR-12991.md`)

- Verdict: **OWNER ITEM.** The base-failure count has no run date.
- Live head `1a86966179e` matches the receipt. The dated head run landed: 1 of 1, JUnit XML 2026-10-13 06:07 UTC. The Proof now says "verified 2026-10-13".
- Held by: "On the base code, the class runs 1 test with exactly 1 failure, on the WARN assertion" is undated. Owed: a dated base run, gate job item 2.
- Edits: the Proof date, and "INFO follows that comment" became "That comment points to INFO."

## SOLR-13186 (PR draft `pr-drafts/solrcloud/SOLR-13186.md`)

- Verdict: **OWNER ITEM.** The base-code run is dated only by the receipts ledger.
- Live head `b436d90d2a88` matches the receipt. The dated head run is in `gates/SOLR-13186-dated-run.md` (2026-10-13 02:15 to 02:22 UTC), with 1 of 1, 0 failures, 0 errors. The Proof's head date matches.
- Held by: the Proof says the base-code run was "recorded 2026-10-06 at this head". That date is the ledger date, and the receipt gives no run date for the base run. It also does not tie the base run to this head. Owner choice: record a dated base-code run (gate job item 3), or drop the clause.
- No edits.

## SOLR-15863 (PR draft `pr-drafts/solrcloud/SOLR-15863.md`)

- Verdict: **OWNER ITEM.** The base-code run at `8f5e94cf314` is dated only by the takeover log.
- Live head `78dff1d52f6c` matches the receipt. The gate job is DONE at 2026-10-13 02:41 UTC, with BackupCoreAPITest 6 of 6, BackupCmdTest 4 of 4 and LocalFSCloudIncrementalBackupTest 7 of 7.
- Held by: "(recorded 2026-10-08)" on the base run is the takeover-log date. Owed: a dated base run, gate job item 4, or drop the date.
- Edits: the three counts are one line per class, each verified at `78dff1d52f6c`. The compile message is quoted as the receipt has it. "The ticket thread asks whether indexVersion was meant" became "A ticket comment wonders whether indexVersion was meant."
- Visible length after edits: about 3,450 characters, under the guide.
- Note: `gates/SOLR-15863-title-regate.md` says DONE in its status line, but its step checklist is unchecked. That is a gate-lane tidy item.

## Records owed

- `gates/solrcloud-base-dated-runs-batch-a.md`, a new gate batch for four dated base runs (items 1 to 4 above).
- SOLR-15035 is needed before SOLR-11479 can open. That is batch B's concern, and it is in the HOLD assignment.
- Not covered here: SOLR-15106 and SOLR-15386, which were READY in the round 5 decisions and are in neither batch.
