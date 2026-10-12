# Draft date flags, round 1

Verdict: the verification-date pass is applied across the converged drafts. A date now stays in a draft only when the ticket's own receipt body gives that date as the gate, run, or proof completion date for the head the sentence names. Dates that rest only on a takeover-log entry, a receipts-ledger citation, a push or DONE line, an ls-remote check, a claim heartbeat, or a main-side note were removed. The sentence keeps its counts and its head. One date was corrected (SOLR-12657, UTC gate completion), most dates were removed because no receipt run supports them, and the rest of the set matched. Open items for the main side are listed at the end.

Scope: `assignments/pool-draft-date-flags-round-1.md`. Claim: `claims/pool-draft-date-flags-round-1.md`. Read-only subagents did the receipt checks. Every edit below was made here and checked against the receipt line it cites.

## Rule applied

- Source: the receipt file for the ticket (`receipts/<KEY>.md`). The receipt header date and gate-run lines count when they name the gated head or its tree.
- Not a source: takeover-log citations, "Recorded in" lines, receipts-ledger citations, push or "DONE and pushed" dates, ls-remote dates, claim or heartbeat dates, and main-side notes.
- Same-tree clause: a date counts for a head when the receipt says the run was on an identical tree (for example SOLR-11475 and SOLR-4841).
- Undated sentences: when the only date was removed and nothing else supported the sentence, the bullet was deleted (SOLR-17280, SOLR-10937, SOLR-11844).
- Two earlier decisions are not changed by this rule: SOLR-10882 keeps 2026-10-11 from the gate record (`gates/SOLR-10882.md`, DONE 2026-10-11T02:32:06Z UTC, GREEN at `5d7d07a5b17`), and SOLR-12007's 2026-10-08 date was removed because its receipt cites the takeover log.

## Coverage

- Case-insensitive `verified 20` / `checked 20` across `pr-drafts/`, all directories. Eight directories (core-admin, cli, configsets, query-parsing, replication-backup, schema-analysis, security, build-docs) were swept case-insensitively. The other directories were first swept case-sensitively, then re-swept for capitalised "Verified" and "Checked", which found 43 more drafts.
- Not searched: other phrasings such as "verified on 20" (none found in the sweeps), and dates that do not follow "verified" or "checked".

## Edits, by commit

Commit `4bbd2f42061` (pushed). Edits in SOLR-14913, SOLR-5813, SOLR-3999, SOLR-4335, SOLR-4422, SOLR-6046 (sweep B) and the other drafts from sweep A.

| Ticket | File:line | Draft date | Receipt basis | Verdict | Change |
|---|---|---|---|---|---|
| SOLR-14913 | edismax:30 | 2026-10-07 | takeover log round 33 only; `b80221f46d3` has no run date | REMOVE | ", verified 2026-10-07" removed; the 10-03 run on `51fe1087fe9` kept |
| SOLR-5813 | solrcloud:19 | 2026-10-07 | takeover log round 28 only | REMOVE | ", verified 2026-10-07" removed |
| SOLR-3999 | solrj:21 | 2026-10-06 | takeover log only | REMOVE | ", verified 2026-10-06" removed |
| SOLR-4335 | solrj:24 | 2026-10-06 | takeover log only | REMOVE | ", verified 2026-10-06" removed |
| SOLR-4422 | solrj:23 | 2026-10-06 | takeover log only | REMOVE | ", verified 2026-10-06" removed |
| SOLR-6046 | solrj:25 | 2026-10-06 | takeover log only | REMOVE | ", verified 2026-10-06" removed |
| SOLR-16322 | build-docs:33 | 2026-10-11 | no run at `68dd5e2b154e`; no 10-11 in receipt | REMOVE | "Verified 2026-10-11 at" to "Verified at" |
| SOLR-17842 | build-docs:25 | 2026-10-11 (tree listing) | receipt has no 10-11 entry; the 10-08 light gate kept | REMOVE | "re-checked on 2026-10-11" to "re-checked" |
| SOLR-9342 | cli:26 | 2026-10-07 | takeover log and disposition only | REMOVE | " (verified 2026-10-07)" removed |
| SOLR-17363 | configsets:31 | 2026-10-05 | takeover log DONE; "pushed 2026-10-05" is a push date | REMOVE | ", verified 2026-10-05" removed |
| SOLR-16725 | core-admin:21 | 2026-10-07 | takeover log only | REMOVE | " (verified 2026-10-07)" removed |
| SOLR-6438 | core-admin:21 | 2026-10-05 | reconciliation takeover log only | REMOVE | " (verified 2026-10-05)" removed |
| SOLR-10897 | query-parsing:25 | 2026-10-05 | takeover log only; push date | REMOVE | "Verified 2026-10-05 at this head." to "Verified at this head." |
| SOLR-11761 | query-parsing:21 | 2026-10-05 | takeover log gate DONE only | REMOVE | "Verified 2026-10-05 at head" to "Verified at head" |
| SOLR-12212 | query-parsing:27 | 2026-10-05 | takeover log gate entry only | REMOVE | "Verified 2026-10-05 at" to "Verified at"; placeholder left (see open items) |
| SOLR-12608 | query-parsing:23 | 2026-10-05 | takeover log only | REMOVE | " (verified 2026-10-05)" removed |
| SOLR-12871 | query-parsing:24 | 2026-10-07 | takeover log round 35 only | REMOVE | ", verified 2026-10-07" removed |
| SOLR-15615 | query-parsing:24 | 2026-10-07 | takeover log round 35 only | REMOVE | ", verified 2026-10-07" removed |
| SOLR-17280 | query-parsing:34 | 2026-10-07 | takeover log round 35 only | REMOVE | bullet "- Verified 2026-10-07." deleted; the Proof keeps its head |
| SOLR-4824 | query-parsing:23 | 2026-10-07 | takeover log only | REMOVE | "Verified 2026-10-07 at head" to "Verified at head" |
| SOLR-6014 | query-parsing:21 | 2026-10-07 | takeover log only | REMOVE | ", verified 2026-10-07," removed |
| SOLR-8977 | query-parsing:25 | 2026-10-05 | reconciliation takeover log only | REMOVE | "Verified 2026-10-05 at head" to "Verified at head" |
| SOLR-9048 | query-parsing:24 | 2026-10-05 | takeover log only | REMOVE | "Verified 2026-10-05 at head" to "Verified at head" |
| SOLR-9149 | query-parsing:21 | 2026-10-05 | reconciliation takeover log only | REMOVE | "Verified 2026-10-05 at head" to "Verified at head" |
| SOLR-11650 | replication-backup:27 | 2026-10-07 | takeover log round 35 only | REMOVE | ", verified 2026-10-07" removed |
| SOLR-12085 | replication-backup:21 | 2026-10-05 | takeover log only | REMOVE | ", verified 2026-10-05" removed |
| SOLR-12246 | replication-backup:21 | 2026-10-05 | takeover log only | REMOVE | ", verified 2026-10-05" removed |
| SOLR-15357 | schema-analysis:29 | 2026-10-07 | takeover log only; the 10-03 ledger entry is for an earlier head | REMOVE | "Verified 2026-10-07 at head" to "Verified at head" |
| SOLR-16977 | schema-analysis:23 | 2026-10-07 | takeover log only | REMOVE | ", verified 2026-10-07" removed |
| SOLR-17047 | schema-analysis:26 | 2026-10-07 | takeover log and correction entries only | REMOVE | ", verified 2026-10-07" removed |
| SOLR-18134 | schema-analysis:25 | 2026-10-07 | takeover log only | REMOVE | ", verified 2026-10-07" removed |

Commit `fc13f4f8103` (local; pushed with this round). 42 edits in 40 drafts.

| Ticket | File:line | Draft date | Receipt basis | Verdict | Change |
|---|---|---|---|---|---|
| SOLR-11288 | solrcloud:25 | 2026-10-06 | ledger citation only | REMOVE | ", recorded 2026-10-06" removed |
| SOLR-12991 | solrcloud:19 | 2026-10-06 | ledger only | REMOVE | ", verified 2026-10-06" removed |
| SOLR-13186 | solrcloud:21 | 2026-10-06 | ledger only | REMOVE | ", recorded 2026-10-06" removed |
| SOLR-14919 | solrcloud:23 | 2026-10-03 | ledger only | REMOVE | ", recorded 2026-10-03" removed |
| SOLR-15024 | core-admin:23 | 2026-10-03 | ledger only | REMOVE | ", recorded 2026-10-03" removed |
| SOLR-15035 | solrcloud:21 | 2026-10-03 | ledger only | REMOVE | ", recorded 2026-10-03" removed |
| SOLR-15106 | solrcloud:23 | 2026-10-07 | takeover entry only | REMOVE | ", recorded 2026-10-07" removed |
| SOLR-15386 | solrcloud:25 | 2026-10-07 | takeover only | REMOVE | ", recorded 2026-10-07" removed |
| SOLR-15478 | configsets:26 | 2026-10-03 | push and ledger only | REMOVE | ", verified 2026-10-03" removed |
| SOLR-15674 | solrcloud:23 | 2026-10-03 | ledger only | REMOVE | ", verified 2026-10-03" removed |
| SOLR-15863 | solrcloud:23 | 2026-10-08 | takeover round 36 and ledger only | REMOVE | ", recorded 2026-10-08" removed |
| SOLR-15945 | schema-analysis:31 | 2026-10-03 | push and ledger only | REMOVE | " (verified 2026-10-03)" removed |
| SOLR-16849 | core-admin:23 | 2026-10-04 | ledger only | REMOVE | ", recorded 2026-10-04" removed |
| SOLR-17155 | search-components:27 | 2026-10-05 | "test conversion DONE" line only | REMOVE | ", verified 2026-10-05" removed |
| SOLR-17311 | query-parsing:23 | 2026-10-04 | push and ledger only | REMOVE | ", verified 2026-10-04" removed |
| SOLR-17680 | solrcloud:28 | 2026-10-04 | ledger only | REMOVE | ", recorded 2026-10-04" removed |
| SOLR-17733 | solrcloud:21 | 2026-10-04 | ledger only | REMOVE | ", recorded 2026-10-04" removed |
| SOLR-3657 | update-processing:25 | 2026-10-07 | no receipt run at the merge-base `97d973814336` | REMOVE | " (verified 2026-10-07)" removed on the merge-base line; the head line keeps 10-07 (receipt Date field) |
| SOLR-4989 | core-admin:23 | 2026-10-06 | ledger only | REMOVE | ", checked 2026-10-06" removed |
| SOLR-6207 | search-components:23 | 2026-10-07 | DONE line only | REMOVE | ", verified 2026-10-07:" becomes ":" |
| SOLR-6975 | search-components:23 | 2026-10-05 | DONE and ledger only | REMOVE | ", verified 2026-10-05" removed |
| SOLR-7550 | search-components:25 | 2026-10-03 | DONE and pushed only | REMOVE | ", verified 2026-10-03" removed |
| SOLR-7550 | search-components:26 | 2026-10-03 | DONE and pushed only | REMOVE | ", verified 2026-10-03" removed |
| SOLR-8275 | core-admin:21 | 2026-10-06 | ledger only | REMOVE | " (verified 2026-10-06)" removed |
| SOLR-874 | query-parsing:30 | 2026-10-06 | ledger and push only | REMOVE | "recorded 2026-10-06" removed |
| SOLR-9155 | solrcloud:25 | 2026-10-06 | ledger only | REMOVE | ", recorded 2026-10-06" removed |
| SOLR-10694 | search-components:24 | 2026-10-09 | citation "gate finished" only | REMOVE | ", verified 2026-10-09" removed |
| SOLR-11391 | query-parsing:24 | 2026-10-09 | citation only | REMOVE | "verified 2026-10-09 at head" to "at head" |
| SOLR-12044 | search-components:30 | 2026-10-09 | citation only | REMOVE | ", verified 2026-10-09" removed |
| SOLR-12651 | solrcloud:23 | 2026-10-10 | citation only | REMOVE | ", verified 2026-10-10" removed |
| SOLR-13369 | solrcloud:21 | 2026-10-07 | round 35 citation only | REMOVE | ", recorded 2026-10-07" removed |
| SOLR-17292 | solrcloud:24 | 2026-10-10 | citation only | REMOVE | ", verified 2026-10-10" removed |
| SOLR-5887 | update-processing:27 | 2026-10-10 | citation only | REMOVE | ", verified 2026-10-10" removed |
| SOLR-5887 | update-processing:35 | 2026-10-10 | citation only | REMOVE | ", verified 2026-10-10" removed |
| SOLR-7120 | edismax:21 | 2026-10-09 | citation only | REMOVE | " (verified 2026-10-09)" removed |
| SOLR-9750 | core-admin:21 | 2026-10-05 | reconciliation citation only | REMOVE | ", recorded 2026-10-05" removed |
| SOLR-14171 | suggester:25 | 2026-10-09 | no receipt file | REMOVE | ", recorded 2026-10-09" removed |
| SOLR-17215 | suggester:27 | 2026-10-09 | no receipt file | REMOVE | ", recorded 2026-10-09" removed |
| SOLR-17393 | suggester:25 | 2026-10-09 | no receipt file | REMOVE | ", recorded 2026-10-09" removed |
| SOLR-9968 | suggester:25 | 2026-10-09 | no receipt file | REMOVE | ", recorded 2026-10-09" removed |
| SOLR-11310 | search-components:24 | 2026-10-03 (head sentence) | "DONE and pushed 2026-10-03" is a push date; the 10-07 A/B run covers the new test only | REMOVE | ", checked 2026-10-03" removed; the base sentence keeps 2026-10-07 |
| SOLR-15805 | core-admin:21 | 2026-10-03 | ledger only; the only run is at `6d78603d82`, identical tree, not the named head | REMOVE | ", recorded 2026-10-03" removed |

Commit `62c03172033` (local). 8 edits in 7 drafts.

| Ticket | File:line | Draft date | Receipt basis | Verdict | Change |
|---|---|---|---|---|---|
| SOLR-3722 | solrj:25 | 2026-10-06 | takeover citation only | REMOVE | "Verified 2026-10-06 at this head." to "Verified at this head." |
| SOLR-4336 | solrj:27 | 2026-10-06 | takeover citation only | REMOVE | as above |
| SOLR-4424 | solrj:24 | 2026-10-06 | takeover citation only | REMOVE | as above |
| SOLR-12657 | streaming:28 | 2026-10-10 | receipt body: "GATE RUNNER DONE 2026-10-11T04:04:02Z" (UTC); "recorded 2026-10-10" not used | CHANGE to 2026-10-11 | "Verified 2026-10-10." to "Verified 2026-10-11." |
| SOLR-12657 | streaming:29 | 2026-10-10 | same gate | CHANGE to 2026-10-11 | "Verified 2026-10-10." to "Verified 2026-10-11." |
| SOLR-10937 | suggester:24 | 2026-10-09 | no receipt file | REMOVE | bullet "- Checked 2026-10-09." deleted |
| SOLR-11844 | suggester:25 | 2026-10-09 | no receipt file | REMOVE | bullet "- Checked 2026-10-09." deleted |
| SOLR-9637 | suggester:27 | 2026-10-05 | no receipt file | REMOVE | "Verified 2026-10-05 at head" to "Verified at head" |

Uncommitted at the time of writing, committed with this report.

| Ticket | File:line | Draft date | Receipt basis | Verdict | Change |
|---|---|---|---|---|---|
| SOLR-10424 | search-components:46 | 2026-10-08 | only a main-side status date (line 3) | REMOVE | "Verified 2026-10-08 at this head." to "Verified at this head." |
| SOLR-17051 | search-components:24 | 2026-10-04 | push-combined date; ledger citation | REMOVE | "Verified 2026-10-04 at" to "Verified at" |
| SOLR-6831 | search-components:23 | 2026-10-06 | "gated and pushed" date; ledger citation | REMOVE | "Verified 2026-10-06 at this head." to "Verified at this head." |
| SOLR-11479 | solrcloud:22 | 2026-10-11 | focused run undated; line 4 is a head and tip stamp (excluded) | REMOVE sentence | " Verified 2026-10-11." removed |
| SOLR-14187 | solrj:29 | 2026-10-11 | push date only (line 5); gate steps undated | REMOVE | "Verified 2026-10-11 at head" to "Verified at head" |
| SOLR-14298 | solrj:21 | 2026-10-06 | takeover citation only; gate undated | REMOVE | "Verified 2026-10-06 at this head." to "Verified at this head." |
| SOLR-13246 | core-admin:21 | 2026-10-06 | receipt body gives "gate run 2026-10-06" but attributes it to the receipts ledger | REMOVE | ", checked 2026-10-06" removed. Earlier keep reversed for consistency with SOLR-12007 |

Earlier in this round (before the sweeps), `548844e9dac` removed SOLR-12007's "Verified 2026-10-08" (takeover-log source). `741d739b2d5` and `0813e086cb0` set SOLR-10882 to 2026-10-11 from the gate record.

## Kept (MATCHES)

Each kept date matches a receipt body date for the sentence's head, or for the same tree. The receipt line is in the subagent tables.

- Gate date in the receipt header, or a "Gated head" line with a date: SOLR-2309, SOLR-2988, SOLR-4362, SOLR-12556, SOLR-15018, SOLR-4374, SOLR-6193, SOLR-7390, SOLR-8009, SOLR-8240, SOLR-9148, SOLR-9864, SOLR-11356, SOLR-7709, SOLR-4367, SOLR-11475, SOLR-12245, SOLR-12703, SOLR-12705, SOLR-13265, SOLR-4841, SOLR-5065, SOLR-5505, SOLR-6045, SOLR-6065, SOLR-7504.
- Gate or run lines: SOLR-18530 (gate carries over to the tip; the changelog-only delta is stated in the receipt), SOLR-18531, SOLR-2632 (premise run at the received head), SOLR-13876, SOLR-5939 (combined gate), SOLR-10322, SOLR-11129, and the others listed in the sweep tables (for example SOLR-11939, SOLR-12161, SOLR-13705).
- Same tree: SOLR-11475 and SOLR-4841 (the receipt gives the gate tree, which the draft's head shares).

## Open items for the main side

1. SOLR-15358 line 25 is not changed. The line says "Run at `cbb345f2e7d`, recorded 2026-10-03: CurrencyFieldTypeDocValuesTest 2 of 2 ... pass". The only 2026-10-12 record is a proof run with CurrencyFieldType reverted, which shows 2 failures. Moving the date would attach a pass to a failing run. The record conflict needs a decision (the 2026-10-03 pre-fix proof record should be marked superseded).
2. SOLR-11939 keeps its 2026-10-12 date. The receipt body has a main-side note dated 2026-10-12 that verifies the gate record. The buildLocalSite run itself is undated. If the main side does not count the note, the date comes out.
3. SOLR-12657 uses UTC. The gate finished `2026-10-11T04:04:02Z`, which is 2026-10-10 in US time zones. The draft now says 2026-10-11. Confirm the convention.
4. SOLR-18530 keeps 2026-10-10 on the same-tree and carried-over-gate reading. The gate ran at `98e5368d99`, and `14868bc7a7` differs only by a changelog fragment.
5. SOLR-11479 removes its sentence "Verified 2026-10-11." The only 2026-10-11 stamp is a head and tip verification, which is treated like ls-remote. The focused run has no date.
6. SOLR-12212 line 27 still has the bracketed placeholder "[per-class count to fill from the run log]". It is outside the date scope and was not changed.
7. SOLR-4367 line 25 is a bracketed owner placeholder asking for run counts. Receipt lines 6-7 give SpellCheckComponentTest 15 of 15 and exactly one failure on base.
8. SOLR-2632 line 25 says "on 2026-10-06". That phrasing is outside the verified/checked pattern, so it was not searched or changed.
9. Several drafts now read "Verified at this head." or "Verified at head `...`." without a date. The author may want to rephrase those sentences.
10. `mergeids-skipped-shard-totals`: there is no receipt file with that name. The draft date would match `receipts/mergeids-fix.md`, but the draft was not changed.
11. SOLR-5939 says "passed 26 of 26". The 26 is the sum of the 5 focused tests in its receipt and the 21 in `receipts/SOLR-5754.md`, which records the combined gate as "GREEN, 26 of 26 tests". The count is supported; no change.
12. SOLR-9852 status line says 2026-10-10 while the draft says 2026-10-11. The receipt's re-gate runner line gives 2026-10-11T03:18:56Z, so the draft is correct; the status line is a receipt wording issue.

## Not verified here

No builds, tests, Gradle runs, PR or JIRA posts, or edits outside the draft date sentences. Gate logs were not opened. Receipts were read as recorded.
