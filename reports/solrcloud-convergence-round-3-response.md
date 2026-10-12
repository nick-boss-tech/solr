# SolrCloud convergence, round 3: main-side response

Round 3 returned CONVERGED, 3 of 3 (SOLR-15035, 16437 and 17680), with three record items that hold no draft: SC-R3-01 (should-fix; the per-ticket gate entries in the answers still carried the pre-gate state), SC-R3-02 (note; a precision point on the SOLR-17680 draft) and SC-R3-03 (note; in-place corrections in the round 1 response). This response verifies each item against the receipts and the drafts before applying, applies all three, and records the two checks the round named alongside them: the SOLR-11479 correction list at answers line 136 against the receipt, and the SOLR-12651 draft's Proof against the live-tip gate. Application method: exact-string replacements, each asserted present before applying; the diff was reviewed after.

## Counts

| Grade | Items | Applied | Not applied |
|---|---|---|---|
| Should-fix | 1 (SC-R3-01) | 1 (four replacements in the answers) | 0 |
| Note | 2 (SC-R3-02, SC-R3-03) | 2 (one draft edit, two in-place corrections) | 0 |

## Fact checks before applying

- **SC-R3-01, SOLR-11479: holds.** receipts/SOLR-11479.md records GREEN at the packaged head `360dca60fcfade` (fast-forward packaged head and live tip, verified 2026-10-11): focused CollectionsAPISolrJTest 26 tests, 0 failures, 0 errors, 1 conditional skip; both SOLR-11479 tests pass; the fail-before proof gives exactly the 2 expected failures. The answers bullet still said NO GATE with a first gate owed.
- **SC-R3-01, SOLR-12651: holds.** receipts/SOLR-12651.md records GATE GREEN at the live tip `f3131d1ee846`, superseding the older-head gate at `90032e274b7`: TestLocalFSCloudBackupRestore 2 of 2 at the head, and the proof discriminates (exactly 1 failure with RestoreCmd.java reverted to the merge-base). The answers bullet and the main-side work list still carried the older gate and the owed live-tip gate.
- **SC-R3-01, the line 136 confirmation: confirmed done at packaging.** The receipt's scope line reads "the four gate-specified corrections only", and its gated head is the packaged head. The three branch corrections in the answers line 136 list (delete `SOLR-11479-TESTING.md`, correct the changelog title, the 400 message wording) plus the adopted several-replica test in the next bullet are those four corrections, and the proof leg covers both targeted tests (the multi-replica test got HTTP 500 where 400 was expected, and the one-replica alias test failed to create the replica). The list is not owed; the gate ran on the head that carries the corrections.
- **SC-R3-01, the SOLR-12651 draft Proof: already current, no edit owed.** The draft at this tip states "TestLocalFSCloudBackupRestore 2 of 2 at f3131d1ee846, verified 2026-10-10", its links are at `f3131d1ee846` throughout, and no bracketed placeholder remains. The convergence line's remark that the Proof is due for the live-tip gate is already satisfied in the live text.
- **SC-R3-02: holds.** At the draft's head `f4b8ce833654`, `CategoryRoutedAlias.REQUIRED_ROUTER_PARAMS` is `Set.of(NAME, ROUTER_TYPE_NAME, ROUTER_FIELD, ROUTER_MAX_CARDINALITY)` (lines 55 to 56), and the generic check in CreateAliasCmd (lines 140 to 146) joins every missing required name into the message. The quoted "Missing params: router.field" is therefore exact only when `router.field` is the only required parameter missing, as the finding states.
- **SC-R3-03: holds.** Lines 93 and 124 of the round 1 response carried no in-place correction: line 93 still presents SC-R1-51 as declined on the ground that the ticket mentions other APIs (settled the other way by SC-R2-21 in round 2), and line 124 still says the suggested titles "are not recoverable" and speaks of "the four drafts the note names" (SC-R1-37 names three; the titles sit in commit `0e287a38159` for four drafts).

## Per-finding dispositions

### SC-R3-01 (answers record, should-fix): applied
Four replacements in `material/solrcloud-round-1-answers.md`, the register's texts:
- SOLR-11479 entry (line 135): the gate-state bullet now reads GATE GREEN at the live tip per the receipt, with the receipt's counts, followed by a dated correction noting the bullet said NO GATE, the state when the answers were written.
- SOLR-12651 entry (line 55): the gate-state bullet now reads GATE GREEN at the live tip per the receipt, with a dated correction noting the recorded live-tip gate supersedes the older gate at `90032e274b7`.
- Main-side work list (line 240): the SOLR-12651 item now reads DONE, live-tip gate recorded 2026-10-10.
- Main-side work list (line 243): the SOLR-11479 item now reads DONE, first gate recorded (receipt verified 2026-10-11).

The register writes its corrections as "Correction, dated:"; they are rendered "Correction, 2026-10-12:" in the answers' established form (the SOLR-16437 entry's corrections of 2026-10-11 are the pattern). Dates in this response are 2026-10-12, the round 3 timeframe.

### SC-R3-02 (SOLR-17680 draft, note): applied
Line 20 now reads: A category routed alias without `router.field` now fails with "Not all required params were supplied. Missing params: router.field" when that is the only required parameter it lacks; it also names router.maxCardinality when that is missing too. The register's replacement, verbatim; the rest of the bullet is unchanged.

### SC-R3-03 (round 1 response record, note): applied
Two in-place corrections in `reports/solrcloud-convergence-round-1-response.md`, dated 2026-10-12, appended to the lines they correct; the round 1 dispositions stand as the historical record and the notes mark what superseded them:
- Line 93 (SC-R1-51): "Superseded, 2026-10-12 (SC-R2-21): the ticket names no other API; its closing sentence says "(and other APIs)"."
- Line 124 (SC-R1-37): "Correction, 2026-10-12 (SC-R2-01, SC-R2-25): the titles are recoverable with `git show 0e287a38159:pr-drafts/solrcloud/<ID>.md` for SOLR-9155, 13186, 15106 and 15386; SC-R1-37 names three of them (13186, 15106, 15386)."

## Convergence line

Round 3's verdict stands: CONVERGED, 3 of 3, and the three record items are now closed in the records themselves. No SolrCloud draft is held by any of them, and no further round is needed for this set.
