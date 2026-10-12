# SolrCloud convergence, round 3

Verdict: **CONVERGED, 3 of 3.** SOLR-15035, 16437 and 17680 each have their round 2 disposition applied in the live text, and none holds an open blocking or should-fix finding against the draft.

Scope: `assignments/pool-solrcloud-convergence-round-3.md`, checked against `reports/solrcloud-convergence-round-2-response.md`. One read-only subagent did the review. Nothing was edited, posted, built or fetched.

Heads: all three match their live tips. SOLR-15035 is `12d7491e82c`, SOLR-16437 is `aa2a6b8afb6f`, and SOLR-17680 is `f4b8ce83365`. The record heads for 11479, 12651, 15478 and 15674 also match their receipts. Every named commit object is present locally, so nothing is marked as not checked on object absence.

## Per draft

- **SOLR-15035: CONVERGED.** SC-R1-31 is applied. The bold line (line 19), bullet 1 (line 21) and the base-code sentence match the register replacement word for word. The receipt records the ADDREPLICA-alone failure and the full-change pass, with no base-code run, and the draft no longer claims one. At the merge-base `b5c71bc5573c`, `AddReplicaCmd.java` and `CoreAdminHandler.java` contain no `NUM_SHARDS`. At the head, `AddReplicaCmd.java` line 305 sets it, and `paramToProp` (lines 239 to 251) includes it at line 250. The test is at `AddReplicaTest` line 238.
- **SOLR-16437: CONVERGED.** SC-R2-21 is applied. The Limits bold line (line 33) reads "Only ADDREPLICAPROP is covered. The ticket's closing sentence suggests the same check for other APIs. DELETEREPLICAPROP and those APIs are not changed." The ticket's closing sentence, "Maybe we should update this (and other APIs) to do a best-effort sanity-check of input parameters before shipping them off to the asynchronous work queue", names no DELETEREPLICAPROP, so the wording matches the ticket. Line 23 "recorded 2026-10-10" is in place.
- **SOLR-17680: CONVERGED.** SC-R2-22 is applied in full. Line 19 reads "followed by the two parameter names". Line 20 is the category-alias bullet, verbatim. The line that was 31 now reads "The up-front check for `router.field` moved out of the command." (it moved to line 32 when the bullet was inserted). The code at the head matches: `CreateAliasCmd` checks `router.name` only (lines 119 to 125), and the generic check is at lines 140 to 146. SC-R3-02 below is a precision note on line 20, not a blocking item.

## Record checks

1. **SC-R2-02, gate lists: PASS on the lists, FAIL on the per-ticket entries.** `receipts/SOLR-11479.md` records GREEN at `360dca60fcfa`, verified 2026-10-11. `receipts/SOLR-12651.md` records GATE GREEN at the live tip `f3131d1ee846`. The answers' summary lists both as gated green, and neither appears in the older-head or no-gate lists. The per-ticket entries in `material/solrcloud-round-1-answers.md` still carry the old state. See SC-R3-01.
2. **SC-R2-21, the answers bullet: PASS.** Line 167 reads "DELETEREPLICAPROP and the other APIs the ticket's closing sentence suggests", with a dated correction.
3. **SC-R2-26, landing order: PASS.** `pr-drafts/configsets/SOLR-15478.md` line 35 reads "This branch is meant to land after SOLR-15674.", which matches the adopted order (SOLR-15674 first). "A new znode starts that version over at 0." appears once in SOLR-15478 (line 9) and once in SOLR-15674 (line 11), identical. The SOLR-15478 verdict stays out of scope.
4. **SC-R2-01 and SC-R2-25, the round 1 corrections: PASS on content.** Commit `0e287a38159` added "Suggested PR title" lines to the drafts for SOLR-9155, 13186, 15106 and 15386, and `cafceac3230` removed them. The register SC-R1-37 names three drafts (13186, 15106, 15386), so the round 2 count is right. The corrections exist only in the round 2 response. See SC-R3-03.

## New findings

- **SC-R3-01 (`material/solrcloud-round-1-answers.md`, should-fix; a record, not a draft verdict).** The per-ticket gate state contradicts the receipts.
  - Line 135 (SOLR-11479) reads "Gate state: NO GATE per the receipt; the live tip `7e538e844c4` is the code commit. A first gate is main-side work owed." The receipt records GREEN at `360dca60fcfa`. Replace with: "Gate state: GATE GREEN at the live tip per the receipt (`360dca60fcfa`; focused CollectionsAPISolrJTest 26 tests, 0 failures, 1 conditional skip; both SOLR-11479 tests pass; the base proof gives the expected 2 failures). Correction, dated: this bullet said NO GATE, the state when the answers were written; the receipt records the first gate (SC-R2-02)."
  - Line 55 (SOLR-12651) reads "Gate state, receipt wins: gated at `90032e274b7` ... A gate at the live tip is main-side work owed before opening." Replace the bullet with: "Gate state: GATE GREEN at the live tip per the receipt (`f3131d1ee846`; TestLocalFSCloudBackupRestore 2 of 2 at the head; the proof discriminates). Correction, dated: the live-tip gate is recorded and supersedes the older gate at `90032e274b7` (SC-R2-02)."
  - Line 240 reads "1. SOLR-12651: gate at the live tip `f3131d1ee84` before opening." Replace with: "1. SOLR-12651: DONE, live-tip gate recorded 2026-10-10 (see the ticket entry above)."
  - Line 243 reads "4. SOLR-11479: first gate, ..." Replace with: "4. SOLR-11479: DONE, first gate recorded (receipt verified 2026-10-11; see the ticket entry above)." Confirm the line 136 correction list against the receipt, which says the gate ran on the packaged head.
- **SC-R3-02 (`pr-drafts/solrcloud/SOLR-17680.md` line 20, note).** The quoted category message is exact only when `router.field` is the only required parameter missing. `CategoryRoutedAlias`'s required list also includes `router.maxCardinality`, and the generic check lists every missing name. Optional replacement for "now fails with "Not all required params were supplied. Missing params: router.field"": "now fails with "Not all required params were supplied. Missing params: router.field" when that is the only required parameter it lacks; it also names router.maxCardinality when that is missing too."
- **SC-R3-03 (`reports/solrcloud-convergence-round-1-response.md` lines 93 and 124, note).** Neither line carries an in-place correction. Line 124 still says the titles "are not recoverable" and "The four drafts the note names". Line 93 (SC-R1-51) still says the ticket names other APIs. Optional dated correction for line 124: "Correction (SC-R2-01, SC-R2-25): the titles are recoverable with `git show 0e287a38159:pr-drafts/solrcloud/<ID>.md` for SOLR-9155, 13186, 15106 and 15386; SC-R1-37 names three of them (13186, 15106, 15386)." Optional dated note for line 93: "Superseded (SC-R2-21): the ticket names no other API; its closing sentence says '(and other APIs)'."

## Not checked

- Gate logs, which are not on disk.
- No Jira packet for SOLR-15035 was opened (none is on disk).
- The master receipts ledger, the takeover log and security material.

## Convergence line

CONVERGED, 3 of 3. SC-R3-01 is open on the answers record and does not hold any draft. The SOLR-12651 draft's Proof is due for the live-tip gate, which is draft-fix work for the main side, not a verdict item.
