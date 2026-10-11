# Response: review confidence round 6 settlement (main side, 2026-10-11)

Round 6 (`reports/review-confidence-round-6.md`) returned 4 SATISFIED and 15 STILL OPEN over the 19 bodies fixed from rounds 4 and 5, with two record-wide issues (C1, C2) gated on a main-side decision. Both decisions are recorded here first, then the per-body settlement. Every body edit below was applied through the GitHub API on 2026-10-11 and verified by comparing the returned body to the intended text (line endings and trailing whitespace normalized). No comment was posted, no draft state changed (all 15 bodies were open and not drafts; PR #4997 was SATISFIED in round 6 and is untouched), and no branch was edited.

## C1. The date convention: DECIDED, MDT is the convention, bodies stand as written

The verification dates in the bodies are the author's local dates (America/Denver, MDT). The commit author offset on the heads in question is -06:00, so a head timestamped 2026-10-07 UTC is 2026-10-06 in the author's zone, which is the date the bodies record and the date the work was done locally. This matches the established practice from the earlier fix rounds, where these bodies were deliberately left as written with the local date, and it is adopted here as the recorded convention: body dates are MDT; the receipts are the record that must carry unambiguous dates and timestamps. The C1 items (18178 item 1b, 18129 item 4e, 16130 item 5b, 12849 item 7d, 13568 item 8a, 13706 item 9c, and the carried 18530 note) close with no body change. Round 7 judges these bodies under this convention.

## C2. Fork runs cited at the head: DECIDED, restate per run

A fork run is cited "at this head" only when the run actually executed that head. Where the run executed a CI commit that differs from the head only by the workflow file, the body now says the run passed "on this code" and names the run and the CI commit. Where the run covered an earlier state of the branch (SOLR-18129 runs 37225864392 and 37225860272 on CI commit `61527cf7f76`), the body says so and dates it. This is the per-item restatement the report specified, applied at items 18129 4b and 4c, 16130 5a, and 13706 9b.

## Per-body settlement

### SOLR-18178, PR #4968: APPLIED
- 1a applied: the drive-letter process-narration bullet is deleted.
- 1b: closed by the C1 decision, no change.

### SOLR-18391, PR #5027: APPLIED
- 2a applied with the date recovered: "Verified at the head adcda10b501 on 2026-10-10:". The vm2 gate receipt at that head (`receipts/SOLR-18391.md`, logs `18391-check.log` and companions on vm2) was recorded on 2026-10-10, and the takeover log's 2026-10-10 14:08 MDT pass records the vm2 gate at the live tip `adcda10b501` DONE GREEN; the main-side proof decision is also dated 2026-10-10. That is the focused gate run the formula's date refers to, so the gap is closed from the record rather than by dropping the date.

### SOLR-18129, PR #5000: APPLIED
- 4a, 4b, 4c applied as specified (4b and 4c carry the C2 restatements).
- 4d applied: both plain-language replacements.
- 4e: closed by the C1 decision, no change.

### SOLR-16130, PR #5004: APPLIED
- 5a applied (C2 restatement).
- 5b: closed by the C1 decision, no change.

### SOLR-18482, PR #5009: APPLIED, record conflict resolved
- 6a applied in the report's first form: "Verified at head 49ca9099d8e on 2026-10-06." The conflict is resolved from the takeover log's 2026-10-06 round 29 entry, which records the confirmation run (log `g5009r29-confirm.log`) as run "at unchanged head 49ca9099d8e", with TestJsonRangeFacets 10/10 and TestJsonFacetErrors 5/5 from fresh JUnit XML. Receipt line 4's "no Gradle gate was run for it" speaks of a full gate, not the confirmation run; a clarification saying so is appended to `receipts/SOLR-18482.md`.
- 6b applied: the repeated first sentence of "What happens today" is deleted.

### SOLR-12849, PR #5011: APPLIED
- 7a, 7b, 7c applied as specified.
- 7d: closed by the C1 decision, no change.

### SOLR-13568, PR #5014: SETTLED, no body change
- 8a: closed by the C1 decision. The body stands as written.
- The round's observation on "the cached entries are rarely hit" was recorded as an observation, not an item, and no replacement was specified, so nothing was applied for it. Round 7 is asked to rule on that sentence explicitly (see the round 7 assignment).

### SOLR-13706, PR #5015: APPLIED
- 9a applied: the summary and the two bullets now carry only the counts the receipt records; the unrecorded failure causes and execution counts are removed.
- 9b applied (C2 restatement).
- 9c: closed by the C1 decision, no change.

### SOLR-13097, PR #5016: APPLIED
- 10a applied: the Limits sentence now reads "Whether such sub-requests carry the caller's credentials depends on the authentication plugin in use."

### SOLR-18505, PR #5028: APPLIED
- Item 1 applied: the unrelated-PR sentence is deleted.
- Item 2 applied: the clause is replaced with the report's text (first-sample assertion sentence, rejoining at "and the surviving index").

### SOLR-18506, PR #5029: APPLIED (the report's replacement path)
- The body text stands (receipt line 9 supports it) and the append is applied at the end of the "What happens today" paragraph: "The commit title says race; the model in Proof shows the eviction is delivered synchronously, so this is not a timing race." The commit title itself is not changed; retitling it would need a history rewrite and force push on a live branch, which the settlement does not do.

### SOLR-18119, PR #5061: APPLIED
- Item a applied: the #4999 clause now ends "which is still open;" and the #5034 link follows with "and".
- Item b applied: both "checked on Linux only" claims now read "checked on one VM only", matching the receipt.
- Item c applied: the three anchored citations (`checkJavadocLinks.py` L252-L257, `dev-docs/how-to-contribute.adoc` L36, `dev-docs/solr-source-code.adoc` L37) now link the base commit `9becf6c6a154` with "(base commit)" in the link text.

### SOLR-18523, PR #5062: APPLIED
- Item 1 applied: all 12 listed citations (javac.gradle L58-L60 and L69, changes-to-html.gradle L71-L77 and L108, ref-guide build.gradle L58, L76, L464-L478 and L475, globals.gradle L175, gradle-precommit.yml L26, index.template.md L27 and L28) now link the base commit `9becf6c6a154` with "(base commit)" in the link text. The `documentation.gradle` links and the changelog link keep the head SHA, as specified.

### SOLR-18531, PR #5101: SETTLED by the receipt path, no body change
- The report offered two paths: delete the CI sentence, or first add the run to the receipt and keep it. The second is taken. `receipts/SOLR-18531.md` now records the originating occurrence: GitHub Actions run 37991927523, LeaderElectionIntegrationTest.testSimpleSliceLeaderElection failing with a BindException that escaped JettySolrRunner.start, no assertion run, sourced from `reports/flaky-tests-root-cause-round-1-t4.md` (the CI log itself is not on disk). The body sentence stands, now receipt-backed.
- Receipt side, also applied in the same push: the Change bullet's shutdown and close() phrases are corrected to the wording the body and the tree support ("closes the reservation sockets for the runners that were in its list when shutdown began, after the stops finish"; "releases the reservation on its current port, whichever runner holds it"), closing the stale-wording note round 6 recorded against the receipt.

### SOLR-3704, PR #5103: APPLIED IN PART, one item skipped
- Item b applied: the five citations to files outside the diff (SolrDocumentFetcher L735-L736 and L793-L796, LukeRequestHandler L788, IndexSchema L360 and L369) now link the merge-base `cabedd1d968` with "(base commit)" in the link text. The DefaultSolrHighlighter, DatePointField and HighlighterTest links keep the head SHA, as specified.
- Item a SKIPPED, precisely: it is not a body item. It asks for a branch edit (changelog fragment line 1 at the head loses "(or a Date.toString())" so it matches the PR title) followed by a re-gate. That belongs to the branch's opening-time packaging, alongside the other recorded changelog title corrections, and cannot be applied through the body API. The replacement text is the report's: "Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds and failed with 'Invalid Date String'."

## Counts

- Bodies edited through the API: 13 (#4968, #5027, #5000, #5004, #5009, #5011, #5015, #5016, #5028, #5029, #5061, #5062, #5103), every PATCH verified against the intended text.
- Report items applied: 23, counting each numbered item once (13706 item 9a carries three text changes; 18119 item c carries three links; 18523 item 1 carries twelve links; 5103 item b carries five links).
- Bodies settled with no body change: 2 (#5014 by the C1 decision; #5101 by the receipt path).
- Items closed by the C1 decision with no text change: 6 (plus the carried 18530 note).
- Items skipped: 1 (3704 item a, a branch edit plus re-gate, reason above).
- Comments posted: none. Draft states changed: none.

## Confirmation

Round 7 is posted as `assignments/pool-review-confidence-round-7.md`: a confirmation pass over these 15 bodies only, verdict SATISFIED or STILL OPEN per body, judging the C1 bodies under the convention recorded above and ruling explicitly on the SOLR-13568 observation.
