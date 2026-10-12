# SolrCloud convergence, round 2: main-side response

Round 2 returned NOT CONVERGED: nine of the twelve re-checked drafts converge, and three do not (SOLR-15035, 16437 and 17680, one should-fix item each). This response verifies every round 2 finding against the receipts and the branch code before applying, applies each holding finding, corrects two statements in the round 1 response that round 2 showed to be wrong (SC-R2-01, SC-R2-25), and accepts the round's judgment on the one item the main side had decided the other way (SC-R1-51, settled by applying SC-R2-21). No finding is rebutted this round: all three should-fix items and all seven notes held on the evidence. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Applied | Not applied |
|---|---|---|---|
| Blocking | 0 (SC-R1-21 confirmed closed) | 0 | 0 |
| Should-fix | 3 (SC-R1-31, SC-R2-21, SC-R2-22) | 3 | 0 |
| Note | 7 (SC-R2-01, 02, 03, 23, 24, 25, 26) | 3 as edits (SC-R2-02, 23, 26), 2 as corrections to the round 1 response record (SC-R2-01, 25) | 2 accepted without an edit (SC-R2-03, SC-R2-24) |

## Fact checks before applying

- **SC-R1-31 (SOLR-15035): holds.** receipts/SOLR-15035.md records one proof leg: the shipped test failed with the banked patch alone (numShards set in AddReplicaCmd only). No base-code run is recorded, so the draft's bold line and first bullet claimed a base failure the record does not carry; the round 1 response's statement that the Proof "already states the receipt's outcome for both legs" was wrong, and this response corrects it. The replacement's first sentence is a code-reading statement and it is accurate: at the merge-base `b5c71bc5573c`, AddReplicaCmd.java contains no numShards reference at all, and `paramToProp` in CoreAdminHandler.java (L238-L249) has no NUM_SHARDS entry, so on the base code ADDREPLICA sends no shard count.
- **SC-R2-21 (SOLR-16437): holds.** The ticket's closing sentence, quoted live in the round 1 response, reads "Maybe we should update this (and other APIs) to do a best-effort sanity-check of input parameters before shipping them off to the asynchronous work queue." It names no API, and DELETEREPLICAPROP (the draft's own example) is not in the ticket. The old Limits phrase "the other APIs the ticket mentions" overstated that, and the round 1 response's SC-R1-51 disposition (declined) does not stand against round 2's judgment. The register's replacement is applied, and the answers bullet is corrected (SC-R2-21's record note, below).
- **SC-R2-22 (SOLR-17680): holds.** At head `f4b8ce833654`, CreateAliasCmd.callCreateRoutedAlias checks only `RoutedAlias.ROUTER_TYPE_NAME` up front (L119-L125); the required-params check that follows (L140-L146) throws "Not all required params were supplied. Missing params: " plus the missing names. CategoryRoutedAlias.REQUIRED_ROUTER_PARAMS (L55-L56) includes `router.field`, and its constructor (L74-L82) reads only `router.maxCardinality` and `router.mustMatch`, so a category alias without `router.field` reaches the generic check, not the routed-alias message. The order point also holds: the old message renders `RoutedAlias.MINIMAL_REQUIRED_PARAMS`, a `Set.of`, whose iteration order can vary per JVM start. All three replacement operations are applied verbatim: the bracketed list in the time-alias bullet becomes "followed by the two parameter names", the category-alias bullet is added after it, and the Choice summary line now reads "The up-front check for `router.field` moved out of the command."
- **SC-R2-02 (answers record): holds.** receipts/SOLR-11479.md records GREEN at the live tip `360dca60fcfade`, and receipts/SOLR-12651.md records GATE GREEN at the live tip `f3131d1ee846` (superseding the older-head gate), while the answers still listed SOLR-11479 under "No gate recorded" and SOLR-12651 under "gated at an older head; the live tip is not gated". The drafts match the receipts; the answers are corrected.
- **SC-R2-01 and SC-R2-25 (round 1 response record): both hold.** Commit `0e287a38159` on this branch does carry "- Suggested PR title:" lines for SOLR-9155, 15106, 13186 and 15386, and `cafceac3230` removed them, so the titles are recoverable with `git show`; the round 1 response's statement that they "are not recoverable from the round 1 parts on this branch" is corrected here. The same response's record item said "the four drafts the note names"; the round 1 note names three (13186, 15106, 15386), and the titles were in history for four drafts. Both corrections change the record only: the decision stands (drafts in this set carry no title line; the PR title is set at opening and checked for accuracy then).
- **SC-R2-23 (SOLR-16437 date form): applied.** The receipt's own framing is "Recorded in the main side's receipts ledger and takeover log (first gate finished 2026-10-10)", so under the set's date-form pick (dates taken from a ledger or log record read "recorded") the Proof now reads "recorded 2026-10-10". The answers prescribe no form for this draft.

## Per-finding dispositions

### SOLR-15035
- SC-R1-31 (should-fix, carried): applied. The Proof bold line and the first bullet are the register's replacements. The draft no longer claims a base-code run; it states the recorded ADDREPLICA-alone failure and the base behavior as code reading. Converged on text, subject to round 3.

### SOLR-16437
- SC-R2-21 (should-fix): applied (register replacement, verbatim). Its record note is applied in `material/solrcloud-round-1-answers.md`: the SOLR-16437 ADOPTED bullet now says "the other APIs the ticket's closing sentence suggests", with a dated correction note (SC-R1-51 is thereby settled against the main side's round 1 disposition).
- SC-R2-23 (note): applied ("recorded 2026-10-10"; basis in the fact checks).
- Converged on text, subject to round 3.

### SOLR-17680
- SC-R2-22 (should-fix): applied (all three operations, verbatim). Converged on text, subject to round 3.

### Notes on records and other drafts
- SC-R2-01, SC-R2-25 (notes): corrected in this response, as recorded in the fact checks. No draft edit.
- SC-R2-02 (note): applied as corrections to `material/solrcloud-round-1-answers.md`: SOLR-11479 and SOLR-12651 move to the gated-green-at-live-tip list (with the correction noted there), and leave the older-head and no-gate lists.
- SC-R2-03 (SOLR-12651, note): accepted, recorded here. The draft is 3,543 characters against the 3,500 guide (about 1 percent over); its Choice carries three options and the ticket's own question from Tomas Lobbe, and trimming the implemented option's wording would cost clarity for 43 characters. No edit.
- SC-R2-24 (length, note): accepted with a reason per draft, recorded here. SOLR-15386 (about 4,490): its acceptance already stands in the round 1 response (the Choice prices both alternatives from the ticket comments; the draft carries three Limits). SOLR-15863 (about 4,060): three suites' counts, a by-construction proof explanation plus an earlier-head leg, a Choice, and five Limits bullets covering mixed-version behavior. SOLR-16437 (about 5,050): the wrong-shard behavior change needs the cross-shard lookup explanation with its base citations, plus a Choice pricing the other route and the two-leg proof counts. SOLR-17292 (about 4,910): the change section enumerates five call sites with distinct outcomes, each cited; the Limits carry the double-logging disclosure. SOLR-17680 (about 4,510): two create paths, error-text changes for time and (after SC-R2-22) category aliases, and a Choice on moving the check. SOLR-17733 (about 4,270): a Choice plus three Limits bullets, two of them cited base-behavior disclosures, and the ticket's unreproduced 500 report stated plainly. No trims.
- SC-R2-26 (SOLR-15478, outside the verdict, note): applied. `pr-drafts/configsets/SOLR-15478.md` gains the Limits bullet "This branch is meant to land after SOLR-15674.", mirroring the SOLR-15674 draft's sentence, per the landing order the answers adopt. The paired version-reset wording is untouched and stays identical in both drafts.

## Judged items

- **SC-R1-28 (SOLR-15035):** round 2 confirms the sentence stands against the quoted live Jira summary. Settled; round 3 does not re-raise it.
- **SC-R1-51 (SOLR-16437):** round 2 judged the phrase overstated; the main side accepts the judgment and applied SC-R2-21. Settled.
- **SC-R1-21 (SOLR-15106, blocking):** round 2 confirms it closed. Settled.

## What round 3 needs to settle

Round 3 re-checks only the three drafts round 2 marked NOT CONVERGED (SOLR-15035, 16437, 17680), verifies each disposition above against the live draft text, and gives a verdict of CONVERGED or NOT CONVERGED per draft. It also checks the record corrections: the answers changes (SC-R2-02 and the SC-R2-21 record note) against the receipts, and the SOLR-15478 landing-order bullet (SC-R2-26) against the SOLR-15674 draft's sentence. The assignment is `assignments/pool-solrcloud-convergence-round-3.md`.
