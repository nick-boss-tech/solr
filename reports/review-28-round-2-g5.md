# Round 2 group 5 review (SOLR-11483, SOLR-12245, SOLR-12864, SOLR-13265)

Assignment: `assignments/review-28-round-2.md` (commit `db860669c5b`). Final drafts read from `origin/pr-prepare` at `0d23d001594`. Live PR metadata read with `research/gh.ps1 pr view --json` on 2026-10-09. Body comparison: SHA-256 of the live body (UTF-8 bytes) against the SHA-256 of the final draft file. Read only. Nothing edited, committed, posted, or run.

Common checks, all four PRs: no em dashes (0 bytes), no "we", no "gate", "receipt", "ledger", "JUnit XML", "rc=", "takeover", "live tip", or "pre-fix" label in the draft. Bodies are byte-identical to the drafts. Heads match the receipts. All four PRs are still drafts (`isDraft: true`).

Part 2 note: dispositions 1 to 6 name SOLR-7022, 6973, 13696, 13943, 4841, 5754, 5939, 7022 (live tip), and SOLR-5941 and 13265 (method phrases). None of the four group 5 tickets is named for a live-tip or title change except 13265's method phrases (disposition 6), which is checked below.

---

## SOLR-11483 (PR 5083)

**Verdict: NOT READY TO FLIP. Remaining item: the PR title reverses the condition.**

### Part 1 (round 1 group 5 findings)

1. Title. **Missing.** Live title: `SOLR-11483: Keep more transaction log files when maxNumLogsToKeep is specified`. The new default applies when `maxNumLogsToKeep` is not specified (`UpdateLog.java` L388-393 at head `4431a250f665`). The owner's rule is that every title is accurate. The owner's ruling kept the other 24 titles as opened, but this one is wrong on its face, so it needs an accuracy call. Proposed replacement (public write, owner ratification needed):
   `SOLR-11483: Keep more transaction log files when numRecordsToKeep is set and maxNumLogsToKeep is not specified`
2. Limits bold summary. **Applied as specified** (`**Disk cost, the key check and one test detail are not covered.**`).
3. Repeated sentence in What happens today. **Applied as specified** (the sentence is absent).

Other checks: Proof matches the receipt (TestRecovery 21/21, verified 2026-10-06; base 21 tests with one failure, `expected:<1000> but was:<10>`). Code at L764-765 and L386-393 confirmed. The "keep 10 and document it" alternative matches the JIRA packet (the ticket says that if others think the change is a bad idea, the issue can be closed and the setting documented). Planned follow-up sentence present ("A follow-up submission is planned to fix that.").

### Part 2

No disposition names SOLR-11483.

### Body

Matches the final draft byte for byte (3,256 bytes, SHA-256 `6a7444f1...`). Head `4431a250f6650494f8d87bd689a7f45d5b4835ab` equals the receipt head.

---

## SOLR-12245 (PR 5084)

**Verdict: READY TO FLIP, once the live title change is recorded.**

### Part 1

1. Title. **Applied differently.** Round 1 proposed a changelog-based title. The live title is now `SOLR-12245: Name the replica in failed distributed update errors`. This does not appear in the answers file or the corrections report, which list only four title fixes. The new title is accurate for the change. Accept it and record it, or tell me which title should stand.
2. Null-guard sentence. **Applied differently (fuller wording), accurate.** Draft: "The `error.req == null` guard in `describe()` is covered by `testStatusCodeOnDistribError_NotSolrException`. The other null checks in `describe()` ... are not covered by a test." Checked at head `f325d5d0576e`: that test (L135-152) builds a `SolrError` with no `req`, so `describe()` returns at L1246-1247. `DistributedUpdatesAsyncException` is used only in `DistributedUpdateProcessorTest`, and the other two tests pass a non-null node, base URL, core name, collection, shard and message. So the other null branches are uncovered, as the draft says.
3. Limits bold summary. **Applied as specified.**
4. Cosmetic: core name for a remote error. **Applied as specified.**
5. Cosmetic: host check sentence. **Applied as specified.**
6. Repeated sentence in What happens today. **Applied as specified.**

Other checks: Proof matches the receipt (DistributedUpdateProcessorTest 6/6, verified 2026-10-09, pre-fix head `4a93167458b5`). Optional, not blocking: "It is not the test that fails without the fix" is not stated in the receipt, but the code supports it. Draft is 3,985 bytes, above the guide; it is not among the seven long drafts the owner accepted. Planned follow-up sentence present.

### Part 2

No disposition names SOLR-12245.

### Body

Matches the final draft byte for byte (3,985 bytes, SHA-256 `02007cda...`). Head `f325d5d0576e582d4488570eabdeabc7410a8a0f` equals the receipt head.

---

## SOLR-12864 (PR 5086)

**Verdict: READY TO FLIP, once the live title change is recorded.**

### Part 1

1. Title. **Applied differently.** Round 1 proposed `SOLR-12864: Add a test for echo with mapUniqueKeyOnly on the JSON update path`. The live title is now `SOLR-12864: Add test coverage for echo with mapUniqueKeyOnly in JSON updates`. It is not recorded in the answers or corrections. It is accurate for the change. Accept it and record it.
2. "A choice to check" section. **Applied as specified.** Removed; Proof is followed directly by Limits.
3. "this draft" to "this PR". **Applied as specified.**
4. Limits bold summary. **Applied as specified** (`**The test covers one combination, and each run takes one random branch.**`). The random branch is confirmed at `JsonLoaderTest.java` L404.
5. Repeated sentence. **Applied as specified.**

Other checks: Proof matches the receipt (JsonLoaderTest 32/32, verified 2026-10-08, head `b9c6c1e71ffa`). The receipt records the pin as a coverage pin, and the draft says so. No planned follow-up sentence. Round 1 did not require one, and this PR is not in the corrections list of 22 (see cross-PR notes).

### Part 2

"Live tip": none in the draft. TLOG credit: not applicable.

### Body

Matches the final draft byte for byte (2,422 bytes, SHA-256 `497011c2...`). Head `b9c6c1e71ffa9a7f68d7ac126206cd2f14af4aaa` equals the receipt head.

---

## SOLR-13265 (PR 5087)

**Verdict: NOT READY TO FLIP. Remaining item: the PR title does not say what changes.**

### Part 1

1. Title. **Missing.** Live title: `SOLR-13265: TLOG replica, updateHandler errors in metrics, no logs`. "No logs" does not describe the change, which stops counting bypassed adds as update errors. Proposed replacement (owner ratification needed):
   `SOLR-13265: Updates applied only to the transaction log (TLOG replica followers) are no longer counted in the update handler error metric`
2. TLOG-credit bullet. **Applied as specified.** The text now says the follower's add is already flagged in base and that this change does not touch that code. Verified: `DistributedUpdateProcessor.java` is not in the diff against the merge base.
3. Proof, disposition 6. **Applied as specified.** Line 22 is the recorded bold replacement; the "fails at its assertion" clause and the two base-failure clauses are removed. Verified at head: `SolrIndexMetricsTest` L209-235 sends five flagged adds and asserts `errors == null || errors.getValue() == 0`. The receipt records 3/3 at head and 1 of 3 failing on base (this test), verified 2026-10-07.
4. Limits bold summary. **Applied as specified.**
5. Repeated sentence. **Applied as specified.** Draft is about 3,450 characters, within the guide.

Other checks: the claim that the flag path does not read the replica type holds. The only replica-type read (L170) is in the constructor. Planned follow-up: none, and round 1 did not require one.

### Part 2

Disposition 6 applied as recorded. TLOG credit applied. "Live tip": not applicable.

### Body

Matches the final draft byte for byte (3,454 bytes, SHA-256 `ed3b850d...`). Head `c134b34aa27fc09b754a24d9fed65898a7dd1748` equals the receipt head.

---

## Cross-PR notes (for the lead)

- Recorded titles: the live titles for SOLR-12245 and SOLR-12864 were changed after round 1. The answers file lists only four title fixes (5754, 13943, 16673, 5505). Record the two changes.
- Follow-up sentences: a grep for "follow-up" or "planned" finds 20 drafts, not the 22 the ruling cites. Four of the five drafts the ruling says carry none (5065, 12705, 14718, 14262) contain a planned follow-up sentence. The corrections list of 22 includes those five and omits 11483. Reconcile the list before it is cited to the owner.
