# Moved fork heads and placeholder text, round 1

Verdict. One draft moved to the live fork tip: SOLR-15805, whose receipt body gives a run at the new head on 2026-10-12. One placeholder line was removed: SOLR-12212, whose only count is attributed to a takeover-log entry. Four drafts are on HOLD, because their named heads are vm2 packaging commits that were never pushed to the fork: SOLR-10364, SOLR-11356, SOLR-11678 and SOLR-12161. Their links cannot resolve on GitHub until the main side settles them.

Inputs: assignment `assignments/pool-moved-heads-round-1.md`, claim `claims/pool-moved-heads-round-1.md`. Three read-only subagents did the receipt, object-store and link checks. Every edit below was made here and checked against the receipt line it cites.

## Draft changes

| Ticket | File | Change | Basis |
|---|---|---|---|
| SOLR-15805 | `pr-drafts/core-admin/SOLR-15805.md` | Three links name `6d78603d82575dfaef78584d5f59f59c107ff0ed` in place of `3432f950f0aed8e2b461e9d7255d590ed7df9b20`. Line 21: "1 of 1 passes at head `3432f950f0ae`." becomes "1 of 1 passes at head `6d78603d82575dfaef78584d5f59f59c107ff0ed` on 2026-10-12." | Trees equal (`23254470611802b3c2bac0bbde19733e1531ac69`). Blob SHAs equal for the three cited files. Receipt lines 5 and 10 give the run at `6d78603d82`, GATE START 2026-10-12T17:49Z, tests=1 failures=0 errors=0 skipped=0. All three links resolve at the new head with the same line numbers. |
| SOLR-12212 | `pr-drafts/query-parsing/SOLR-12212.md`, line 27 | Deleted: "- Verified at `876953fdc92`. TestSolrQueryParser: [per-class count to fill from the run log]. One run of 80 focused tests, 0 failures." | The receipt's only count, "80 focused tests, 0 failures ... at the head", is attributed in the same line to the main side's takeover log. Per-class figures are attributed the same way. The assignment bars counts carried from a takeover-log or ledger citation. The two test bullets above it keep their links to the head. |

Not changed by design: SOLR-15805's merge-base links (line 9, `b5c71bc5573c`) are correct and stay. SOLR-15805's "By code reading, it fails on the base code" stays, since the receipt gives no dated pre-fix run.

## HOLD and NEEDS MAIN SIDE

The named heads below are not in the local object store, and a fetch by SHA from the fork failed. Each receipt says the gated head is a packaging commit that is "local to vm2 and is not pushed to the fork." No draft for these was edited.

| Ticket | Draft | Named head | Fork tip | Status | Question for the main side |
|---|---|---|---|---|---|
| SOLR-10364 | `pr-drafts/solrj/SOLR-10364.md` | `efb1e6717f9` | `502bdbf033fa` | HOLD | Will the vm2 packaging commit be pushed, so the draft's links resolve? Or will the draft move to the fork tip with a receipt run that names that SHA? The receipt's "Head" run has no SHA, so it cannot be counted at the fork tip. |
| SOLR-11356 | `pr-drafts/solrj/SOLR-11356.md` | `6120dae28d04` | `8474e5a3a26d` | NEEDS MAIN SIDE | The receipt labels the 12-of-12 run at `8474e5a3a2`, the fork tip. The draft attributes it to `6120dae28d04`. On vm2, does the tree at `6120dae28d04` equal the tree at `8474e5a3a26d`, so that the packaging commit removes only `SOLR-11356-TESTING.md`? |
| SOLR-12161 | `pr-drafts/security/SOLR-12161.md` | `cdd265168a2a` | `1725cbd84898` | NEEDS MAIN SIDE | The pin run in the receipt is at the fork tip. The draft's Proof names only the named head, and one sentence says "this head" without a SHA. On vm2, does `git diff --stat 1725cbd84898 cdd265168a2a` show only the removal of `SOLR-12161-TESTING.md`? If so, the Proof can move to the tip on the existing run. |
| SOLR-11678 | `pr-drafts/security/SOLR-11678.md` | `611eae246453` (nine occurrences) | `55d8cd189d15` | HOLD | Same packaging-commit problem. The receipt also says the proof leg is "INCONCLUSIVE by compile". That status has to appear in the Proof before any publication. This draft was not reviewed in depth. |

Receipt sweep for other unreachable heads (`git cat-file -e` over every receipt line naming a packaged or local-only head):
- Not in the store, with a draft: SOLR-10364, SOLR-11356, SOLR-11678, SOLR-12161 (above).
- Not in the store, no draft: SOLR-3498 (`2ca0b3743427`, vm2 packaging commit), SOLR-10234, SOLR-5589 and SOLR-8554. The last three say "fork tip, verified by ls-remote". Their fork claims were not tested here.
- In the store (no action): SOLR-10390, SOLR-11700, SOLR-11939, SOLR-13705, SOLR-14187, SOLR-16322, SOLR-17356, SOLR-5262, SOLR-6430, SOLR-7119, SOLR-4399, SOLR-6711.
- Not commits (excluded): JUnit seeds, and a SHA-256 digest of `bin/solr` in SOLR-10390.

## Other findings for the main side

1. **Fork force-update on `solr-15805-submit`.** The fork branch moved from `3432f950f0a` to `6d78603d825` by a force update, which this session did not make. The new tip has the same tree, and the commit subjects read "SOLR-15805: unit test that startup failure fails context initialization ...", "add assumeWorkingMockito". The main side should confirm the rewrite was intended.
2. **SOLR-15805 receipt has a hardened block** (lines 15 to 19) with gated head `3432f950f0a`, no date, and a heading "at the live tip" that conflicts with its own "Gated head" line. Its line 19 says "Proof: PASS (pre-fix proof step in the hardening run)" with no date or log. Only lines 5 and 10 support the new head.
3. **Counts attributed to ledgers or takeover logs.** SOLR-12212 (80 focused tests) and SOLR-4367 (SpellCheckComponentTest 15 of 15 and the base failure) both carry counts whose receipts attribute them to a takeover log or the ledger. SOLR-4367's placeholder stays for the owner. The main side should say whether these attributed counts are usable. If they are, SOLR-12212 can have its count restored and SOLR-4367 can be filled from its receipt.
4. **Merge-base label check.** The earlier concern that `cabedd1d968` is not the merge-base was a reviewer error. The reviewer compared against `origin/main`, which is the fork's main at `498a09aea97`. Against `upstream/main` (`d76fc14b985`), `cabedd1d968` is an ancestor, so the "merge-base" labels in the drafts that use it are correct. No change.

## Not done

- No build, test, Gradle run, PR or comment, and no force push.
- No fetch beyond the four fork branches and the one head that was reachable (`3432f950f0a`).
- PR state on GitHub was not checked.
- The four HOLD drafts were checked against their receipts and the object store only. Their gate logs are not in this worktree.
