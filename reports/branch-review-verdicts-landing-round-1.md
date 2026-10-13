# Branch review verdicts landing, round 1

- Assignment: `assignments/pool-branch-review-verdicts-landing-round-1.md`, steps 1 and 2 (the landing and the accounting). Steps 3 and 4 are worked in part (section d).
- Claim: `claims/pool-branch-review-verdicts-landing-round-1.md`.
- Host and tree: Windows review host, worktree `wt/pr-prepare-suggester`, landed on pr-prepare at tip `e27b39abef3`. Date: 2026-10-13.

## Summary

- The ten verdict files are landed under `reports/`, byte-identical to their branch blobs (10 of 10 on both tests: the blob hash and a fresh `cmp`).
- Search components: 48 drafts have verdicts (47 SATISFIED, 1 OPEN (gate)), plus 2 excluded files. All nine receipt-only tickets the assignment names are accounted for. The summary carries two more draft-owed receipts than the assignment names.
- Query parsing: 18 drafts have verdicts (15 SATISFIED, 3 OPEN (gate)), all inside the 25 round 1 tickets. All 25 are accounted for.
- Flag: SOLR-16570 is claimed by both categories, with different draft status. Not decided here.
- Wording edits made on the review tips are not on pr-prepare for 23 drafts (10 search components, 13 query parsing). For those, the verdict describes the review tip, not the pr-prepare file.

## (a) Provenance

| Set | Source branch | Tip | Review commit | Branch base | Merge base with pr-prepare |
|---|---|---|---|---|---|
| Search components | `assignment-search-components-review` | `289d5d3ad5fe34b21852a4187657b03796bbc5e7` | `289d5d3ad5f` (2026-10-13 11:52 -0400) | `790624c4e56` | `699d446731b` |
| Query parsing | `assignment-query-parsing-review` | `03fbf23793f0ab23d46dce01709a3afec01a2fe6` | `03fbf23793f` (2026-10-13 11:04 -0400) | `98691e0eb2d` | `699d446731b` |

Tips were checked with `git ls-remote origin` before the copy, immediately before it, and at the final re-check.

Landed files (blob hash from `git hash-object --no-filters`, equal to the tip blob):

| Set | Path | Blob | Bytes |
|---|---|---|---|
| Search components | `reports/search-components-review-1.md` | `8c7052e683858fa2e6582556f11178cee21fb987` | 12402 |
| Search components | `reports/search-components-review-2.md` | `3ef478cd3dcb12a3997fe19413c6936c4a0783e3` | 15920 |
| Search components | `reports/search-components-review-3.md` | `66696d3b60bec149a99ff9b0b7c43287004747a0` | 16640 |
| Search components | `reports/search-components-review-4.md` | `573e079e5739b677c0e16b269273f0d4b97cee20` | 14548 |
| Search components | `reports/search-components-review-summary.md` | `483dc036cee23d5e16bc8c19747f27edc8b8735c` | 18081 |
| Query parsing | `reports/query-parsing-review-q1.md` | `95d1ae40eccc5695e1f0b11b28ea410ec221d611` | 8495 |
| Query parsing | `reports/query-parsing-review-q2.md` | `6ad9e0a8d776459d2f64044334701af95ced371b` | 8603 |
| Query parsing | `reports/query-parsing-review-q3.md` | `88303003b3c05af40baedc3ce64af09de69147a1` | 9188 |
| Query parsing | `reports/query-parsing-review-q4.md` | `5b97ff119e99081cde5f3c82e9222d7fdfca8226` | 5386 |
| Query parsing | `reports/query-parsing-review-summary.md` | `8534033e651434b8b789bf8f0d0adc790d353aef` | 12144 |

The worktree HEAD is `e27b39abef3`, equal to `origin/pr-prepare`. The local branch ref `refs/heads/pr-prepare` is behind and was not used.

## (b) Accounting carried forward

Search components (`pr-drafts/search-components/`, 50 files):
- 48 drafts have verdicts. The summary, the four sub-batch tables and the draft files agree.
- 2 files are excluded by the assignment: `SOLR-18541.md` (its own strong-review loop) and `mergeids-skipped-shard-totals.md` (SOLR-18539 staging).
- Receipt-only tickets named by the assignment, each with a green receipt: SOLR-16570, SOLR-8088, SOLR-6759, SOLR-8003, SOLR-10305, SOLR-8051 (landing dependency for SOLR-15319), SOLR-9124, SOLR-9595, SOLR-17055.
- Not named by the assignment, draft owed: SOLR-8767 and SOLR-8954 (both receipts read GATE GREEN).
- Held or qualified: SOLR-13851 (a maintainer decision) and SOLR-15144 (a later round 35 finding).
- Live PR text, not drafts: SOLR-13568, SOLR-17539, SOLR-18482, SOLR-18506.
- Not owed: SOLR-18196, SOLR-18356, SOLR-3044, SOLR-17841, SOLR-16155, SOLR-16290, SOLR-17372.

Query parsing (`pr-drafts/query-parsing/`, 18 files):
- The 25 round 1 tickets are listed in `assignments/query-parsing-round-1.md` at `9bfcb2ff1c1`. The summary's 25 entries match that list.
- Seven have no draft: SOLR-13202 (merged, skip), SOLR-17882 (premise dead; retire call is the owner's), SOLR-13838 and SOLR-13903 (record gap: no gate log or date), SOLR-15906 (queued for drafting), SOLR-16570 (audit only), SOLR-16130 (live PR 5004; text review only).

## Flags (recorded, not filled)

1. SOLR-16570 has two owners. The search-components summary counts it as a draft owed. The query-parsing summary records "NO DRAFT YET (audit only)". The owner or coordinator must say which category holds it.
2. The assignment names nine receipt-only tickets, and the summary has eleven draft-owed entries (SOLR-8767 and SOLR-8954 are the two extra). The assignment's list is incomplete.
3. The search-components summary says SOLR-18541 and the mergeIds staging file were "not drafted or reviewed here". Both exist and are excluded. The wording should say so.
4. SOLR-13876's sub-batch table gives an edit count of 2. Its section and the summary describe one edit.
5. SOLR-11391 has a draft and a SATISFIED verdict, but the round 1 assignment said "audit only". Its receipt now records GATE GREEN at `b0f15a22856`. The query-parsing summary treats the receipt at the live tip as the gate record (decision D4) and asks the main side to confirm.
6. SOLR-6014's live tip disagrees with its round 1 disagreement list. Carried by reference.

## (c) One line per draft

Verdict is the exact string from the sub-batch table. "Yes" in the last column means the pr-prepare draft has the blob the verdict was issued against. "No" means the review tip carries wording edits that pr-prepare does not have yet. Edits counts are from the sub-batch tables. SC is search components; QP is query parsing.

| No. | Set | Ticket | Verdict | Edits | Last commit on pr-prepare | Verdict current on pr-prepare |
|---|---|---|---|---|---|---|
| 1 | SC | SOLR-4374 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 2 | SC | SOLR-5394 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 3 | SC | SOLR-6193 | OPEN (gate) | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 4 | SC | SOLR-6207 | SATISFIED | none | `d4d9d417577` 2026-10-12 | Yes |
| 5 | SC | SOLR-6831 | SATISFIED | none | `523df520d8a` 2026-10-12 | Yes |
| 6 | SC | SOLR-6975 | SATISFIED | 3 | `d4d9d417577` 2026-10-12 | No |
| 7 | SC | SOLR-7390 | SATISFIED | none | `29ab095519d` 2026-10-11 | Yes |
| 8 | SC | SOLR-7498 | SATISFIED | none | `7f583f139d5` 2026-10-11 | Yes |
| 9 | SC | SOLR-7520 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 10 | SC | SOLR-7550 | SATISFIED | 1 | `d4d9d417577` 2026-10-12 | No |
| 11 | SC | SOLR-8009 | SATISFIED | 1 | `1ad4d52f55b` 2026-10-11 | No |
| 12 | SC | SOLR-8020 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 13 | SC | SOLR-8240 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 14 | SC | SOLR-8939 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 15 | SC | SOLR-9148 | SATISFIED | none | `7f583f139d5` 2026-10-11 | Yes |
| 16 | SC | SOLR-9396 | SATISFIED | none | `181818be5a9` 2026-10-11 | Yes |
| 17 | SC | SOLR-9864 | SATISFIED | none | `7f583f139d5` 2026-10-11 | Yes |
| 18 | SC | SOLR-10424 | SATISFIED | none | `504c4c269b7` 2026-10-12 | Yes |
| 19 | SC | SOLR-10492 | SATISFIED | 1 | `1ad4d52f55b` 2026-10-11 | No |
| 20 | SC | SOLR-10694 | SATISFIED | none | `d4d9d417577` 2026-10-12 | Yes |
| 21 | SC | SOLR-10844 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 22 | SC | SOLR-11129 | SATISFIED | none | `089a8fab262` 2026-10-12 | Yes |
| 23 | SC | SOLR-11153 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 24 | SC | SOLR-11310 | SATISFIED | none | `d4d9d417577` 2026-10-12 | Yes |
| 25 | SC | SOLR-11364 | SATISFIED | 1 | `1ad4d52f55b` 2026-10-11 | No |
| 26 | SC | SOLR-11470 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 27 | SC | SOLR-12044 | SATISFIED | none | `d4d9d417577` 2026-10-12 | Yes |
| 28 | SC | SOLR-12543 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 29 | SC | SOLR-12556 | SATISFIED | none | `62c28eafcee` 2026-10-11 | Yes |
| 30 | SC | SOLR-13245 | SATISFIED | 1 | `1ad4d52f55b` 2026-10-11 | No |
| 31 | SC | SOLR-13876 | SATISFIED | 2 (see flag 4) | `6c970902f6d` 2026-10-12 | No |
| 32 | SC | SOLR-14381 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 33 | SC | SOLR-14451 | SATISFIED | 1 | `1ad4d52f55b` 2026-10-11 | No |
| 34 | SC | SOLR-14678 | SATISFIED | none | `325ca02c2f6` 2026-10-11 | Yes |
| 35 | SC | SOLR-14931 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 36 | SC | SOLR-15018 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 37 | SC | SOLR-15041 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 38 | SC | SOLR-15319 | SATISFIED | none | `86aa0daaf15` 2026-10-13 | Yes |
| 39 | SC | SOLR-15331 | SATISFIED | none | `523df520d8a` 2026-10-12 | Yes |
| 40 | SC | SOLR-15479 | SATISFIED | 1 | `c4a524ac787` 2026-10-10 | No |
| 41 | SC | SOLR-15895 | SATISFIED | none | `325ca02c2f6` 2026-10-11 | Yes |
| 42 | SC | SOLR-16444 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 43 | SC | SOLR-17051 | SATISFIED | 1 | `523df520d8a` 2026-10-12 | No |
| 44 | SC | SOLR-17155 | SATISFIED | none | `523df520d8a` 2026-10-12 | Yes |
| 45 | SC | SOLR-17748 | SATISFIED | none | `29ab095519d` 2026-10-11 | Yes |
| 46 | SC | SOLR-17791 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 47 | SC | SOLR-17976 | SATISFIED | none | `1ad4d52f55b` 2026-10-11 | Yes |
| 48 | SC | SOLR-18109 | SATISFIED | none | `c4a524ac787` 2026-10-10 | Yes |
| 49 | SC | SOLR-18541 | none (excluded) | - | `4c30bcd581c` 2026-10-13 | Not examined (excluded) |
| 50 | SC | mergeids-skipped-shard-totals | none (excluded) | - | `4d7c0de40de` 2026-10-13 | Not examined (excluded) |
| 51 | QP | SOLR-874 | SATISFIED | none | `d4d9d417577` 2026-10-12 | Yes |
| 52 | QP | SOLR-4824 | SATISFIED | 2 | `4bbd2f42061` 2026-10-12 | No |
| 53 | QP | SOLR-6014 | OPEN (gate) | 2 | `4bbd2f42061` 2026-10-12 | No (see patch-loop record) |
| 54 | QP | SOLR-8977 | SATISFIED | none | `4bbd2f42061` 2026-10-12 | Yes |
| 55 | QP | SOLR-9048 | SATISFIED | 1 | `4bbd2f42061` 2026-10-12 | No |
| 56 | QP | SOLR-9149 | SATISFIED | 1 | `4bbd2f42061` 2026-10-12 | No |
| 57 | QP | SOLR-10897 | SATISFIED | none | `4bbd2f42061` 2026-10-12 | Yes |
| 58 | QP | SOLR-11391 | SATISFIED | 2 | `523df520d8a` 2026-10-12 | No |
| 59 | QP | SOLR-11761 | SATISFIED | none | `4bbd2f42061` 2026-10-12 | Yes |
| 60 | QP | SOLR-12212 | SATISFIED | 1 | `54cf8d71ad0` 2026-10-12 | No |
| 61 | QP | SOLR-12532 | SATISFIED | 2 | `1ad4d52f55b` 2026-10-11 | No |
| 62 | QP | SOLR-12608 | OPEN (gate) | 2 | `523df520d8a` 2026-10-12 | No (see patch-loop record) |
| 63 | QP | SOLR-12871 | SATISFIED | 2 | `4bbd2f42061` 2026-10-12 | No |
| 64 | QP | SOLR-15615 | SATISFIED | 2 | `4bbd2f42061` 2026-10-12 | No |
| 65 | QP | SOLR-16267 | SATISFIED | 3 | `f28412d71d3` 2026-10-09 | No |
| 66 | QP | SOLR-17280 | SATISFIED | 3 | `4bbd2f42061` 2026-10-12 | No |
| 67 | QP | SOLR-17311 | SATISFIED | none | `d4d9d417577` 2026-10-12 | Yes |
| 68 | QP | SOLR-17796 | OPEN (gate) | 1 | `325ca02c2f6` 2026-10-11 | No (see patch-loop record) |

Counts: 43 Yes, 23 No, 2 excluded (68 rows).

Working-tree note: SOLR-6014, SOLR-12608 and SOLR-17796 have uncommitted edits made by this round's patch-loop pass (section d). This table reports committed state.

## (d) Patch-loop record and ready lists

Patch loop, query parsing, the three OPEN (gate) drafts, wording only (this round):
- SOLR-6014: the Change line is qualified; Limits states the four 2026-10-13 verdicts; the Proof date is added. Holds: the base-merge probe needs a Linux gate run; the in-branch fix for the cache, cost and fq cases needs a branch commit and a gate.
- SOLR-12608: a fail-before premise sentence is added; an internal Limits bullet is replaced; the Proof date is added. Holds: the changelog title (G2) needs a branch commit and a gate, and the draft and the title disagree until it lands, so PR opening waits; the quoted base failure line rests on `g12608-premise.log`, which is not on disk.
- SOLR-17796: the Limits negated-collapse bullet states the EXCEPTION OCCURS verdict. Holds: the negated-collapse fix, the changelog title and the Limits offer line need code, grammar regeneration and a gate; SOLR-17796 and SOLR-12212 both change the QueryParser.jj Query production, so their landing order matters.

Disagreement D-1: `reports/query-parsing-round-1-q6.md` (NOTE 6) bars a SOLR-12608 fail-before claim without an on-disk base run, but the q2 sentence rests on the receipt alone. Kept and flagged for the main side.

Not yet worked:
- The 23 "No" drafts still carry pre-edit text on pr-prepare. Their review-tip edits need a landing commit (a later round).
- The OPEN (gate) items: SOLR-6193 (search components, gates SC-G1 to SC-G3), and the three query-parsing holds above.
- Ready lists (step 4): not yet built. The candidates are the 47 search-components SATISFIED drafts and the 15 query-parsing SATISFIED drafts. The live-tip and receipt check has not run.
- Flags 1, 2, 3, 4 and 5 need an owner or coordinator call or a correction.

## Shared worktree

Changes by other reviewers in the same worktree: `pr-drafts/security/SOLR-11678.md` and `SOLR-12161.md`, and the security gate and response files. They are committed under the security category record, not here.
