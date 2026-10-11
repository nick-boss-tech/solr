# Search components draft fidelity, slice s4 (claim C4)

Assignment: `assignments/pool-draft-fidelity-searchcomponents-edismax.md`. Claim: `claims/pool-draft-fidelity-searchcomponents-edismax.md`, slice C4.
Slice drafts: `pr-drafts/search-components/SOLR-14451.md`, `SOLR-14678.md`, `SOLR-14931.md`, `SOLR-15018.md`, `SOLR-15041.md`.
Worktree: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`, HEAD d627304e96b. Read only. No builds, tests, gate runs, test-queue commands, commits, pushes, or GitHub or Jira writes.

Round reports checked per ticket (the part whose sections cover the ticket; summary in brackets):
- SOLR-14451: `reports/search-components-2-h5.md` [`search-components-2.md`]
- SOLR-14678: `reports/search-components-3-w2.md` [`search-components-3.md`]
- SOLR-14931: `reports/search-components-4-s2.md` [`search-components-4.md`]
- SOLR-15018: `reports/search-components-2-h1.md` [`search-components-2.md`]
- SOLR-15041: `reports/search-components-3-w6.md` [`search-components-3.md`]
- `search-components-1.md` and parts f1 to f6 mention none of the five tickets.

Receipts: `receipts/SOLR-<n>.md` present for all five. Answers material: `material/` grepped for each ticket number; no file mentions any of the five.

Heads (`git ls-remote origin refs/heads/<branch>`, compared with the head each draft names):
- solr-14451-submit: e60891d8716d89538109407aadc629987456017a, matches
- solr-14678-submit: 5d94e6cf3981bd8a141a2063992148893e6d1bbb, matches
- solr-14931-submit: 1a7d678d9a158455b0a6afca47c215ddf88b5a0a, matches
- solr-15018-submit: d0f29b4630c03f14db715f5f23864694ec9e8327, matches
- solr-15041-submit: 55fca0a7c2431f0fe335aaf6fb7387c7343a66bd, matches

Base for pre-change citations (`git merge-base <head> upstream/main`, worktree upstream/main 3f5d4c5bf8a, 2026-10-10): SOLR-14451 and SOLR-14931 share 9b3a84b1c460981eab09d8ffaef776acc4a184f8; SOLR-14678, SOLR-15018 and SOLR-15041 share b5c71bc5573c4e31b4cee5a7965d73587fc0ae58. The round reports used upstream 8e62c2686882 (2026-10-09); that commit is not a branch base.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-14451 | e60891d8716 (matches) | DRIFT (2 items) |
| SOLR-14678 | 5d94e6cf398 (matches) | DRIFT (4 items) |
| SOLR-14931 | 1a7d678d9a15 (matches) | CONSISTENT (held: round fixes move the head; see notes) |
| SOLR-15018 | d0f29b4630c (matches) | CONSISTENT |
| SOLR-15041 | 55fca0a7c243 (matches) | DRIFT (2 items) |

## SOLR-14451

Verdict: DRIFT (2 items).

1. Draft says: "Shards collect facet debug information only when they are in debug mode themselves ([FacetModule.java line 150](https://github.com/apache/solr/blob/8e62c2686882aa704480ab13b6a60ee8f7b5c8af/solr/core/src/java/org/apache/solr/search/facet/FacetModule.java#L150))."
   - Evidence: this is pre-change symptom code. The branch does not change FacetModule.java (its diff against 9b3a84b1c46 lists four files: the changelog, DebugComponent.java, DebugComponentTest.java, TestCloudJSONFacetSKGEquiv.java). The link uses 8e62c2686882, which is not the branch base. Line 150 (`if (rb.isDebug()) {`) is the same at both commits. `pr-formula.md`, presentation rule, requires the merge-base for pre-change code, and the text must say so.
   - Replacement: "Shards collect facet debug information only when they are in debug mode themselves ([FacetModule.java line 150 at the base commit](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/search/facet/FacetModule.java#L150))."

2. Draft says: "For a request that is not a field fetch, [DebugComponent.java lines 176-178](https://github.com/nick-boss-tech/solr/blob/e60891d8716d89538109407aadc629987456017a/solr/core/src/java/org/apache/solr/handler/component/DebugComponent.java#L176-L178) sets `debug` and `debug.query` to `false` on the shard request."
   - Evidence: pre-change code, in a range the branch does not change. At the base commit the else branch and the two set calls sit at lines 175-177 (175 `} else {`, 176 set DEBUG_QUERY, 177 set DEBUG). At head, 176-178 hold the same statements, shifted by the added import at line 54. The new code starts at head line 179. Same citation rule as item 1.
   - Replacement: "For a request that is not a field fetch, [DebugComponent.java lines 175-177 at the base commit](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/handler/component/DebugComponent.java#L175-L177) sets `debug` and `debug.query` to `false` on the shard request."

Checked and consistent:
- Proof counts: DebugComponentTest 7 of 7 and TestCloudJSONFacetSKGEquiv 7 of 7 at head (`receipts/SOLR-14451.md` line 6). Premise: DebugComponentTest 7 with exactly 1 failure, testModifyRequestFacetPurposeDebugModes (line 7). Verified date 2026-10-07 is the gate and round 35 date (lines 4 and 9).
- Test links DebugComponentTest.java L197-L243 (testModifyRequestFacetPurposeDebugModes, 197-243) and TestCloudJSONFacetSKGEquiv.java L269 (head) are correct.
- Change links DebugComponent.java L179-L189 (head) are correct: the facetRequest check is at 187 and the add at 188.
- Choice: live alternative (forward every debug mode, with the stated cost of extra shard diagnostics). Matches `search-components-2-h5.md` item 3 and owner decision 3.
- Limits match the receipt and h5 item 8 (query, results and timing are asserted; debug=true alone is not; the cloud test has no base run).
- Changelog `changelog/unreleased/SOLR-14451-facet-debug-query-cloud.yml` exists at head. Its title is consistent with the draft's summary. The draft has no separate title line.
- Public text: no process vocabulary, no em dashes, verification date present.

Notes (not blocking):
- Branch history (h5 item 2, owner decision 1): commit fa3ec91f355 "SOLR-14451: add hypothetical-reproduction handoff doc" is in the fork history and shows in a PR commit list. It is not in the draft text. Rewriting it needs owner authorization.
- The Jira text (`research/jira-context/SOLR-14451.json`) says debug=true "appears to" return facet debug info and that this is not rigorously proved. The draft says "The ticket reports that debug=true does return it." Optional: "The ticket reports that debug=true appears to return it."
- Plain language: "facet-trace" and "debug.query" are compressed; optional.

## SOLR-14678

Verdict: DRIFT (4 items).

1. Draft says (Limits): "We plan a follow-up for this path that keeps the names with each document, so a writer can check them for that document."
   - Evidence: `search-components-3-w2.md` finding 1 and owner decision 1. The follow-up is an assumption that "needs your confirmation". The owner has not decided whether /get is fixed here or named as a follow-up. Formula section 4: a gap is named in Limits with an offer, not a commitment.
   - Replacement: "A follow-up can keep the names with each document on this path, so a writer can check them for that document. I can open a follow-up ticket and PR for it on request."

2. Draft says (changelog title, `changelog/unreleased/SOLR-14678.yml` line 1, which the draft's summary must match): "The [child] doc transformer now keeps nested child documents in the response when the field list omits the nest-path fields, at any nesting depth"
   - Evidence: `search-components-3-w2.md` finding 3 (FIX). The tests cover two levels only (TestChildDocTransformerHierarchy.java head L155-L218: toppings/ingredients and lonely/lonelyGrandChild). "Any nesting depth" is untested, and /get is not covered (finding 1), so "in the response" overclaims.
   - Replacement (changelog title): "The [child] doc transformer now keeps nested child documents in search results when the field list omits the nest-path fields, including children two levels below the root."

3. Draft says (What happens today): "[SolrReturnFields.java L554-L566](https://github.com/nick-boss-tech/solr/blob/5d94e6cf3981bd8a141a2063992148893e6d1bbb/solr/core/src/java/org/apache/solr/search/SolrReturnFields.java#L554-L566)"
   - Evidence: this is the pre-change symptom (wantsField skips a name that fl does not list). At head, lines 558-560 are the new `isTransformerAddedField` check, so the link shows the change rather than the symptom. At the base commit, wantsField is L554-L565, with `return false;` at 564. The branch changes SolrReturnFields.java (+23 lines), so the citation rule applies.
   - Replacement: "[SolrReturnFields.java L554-L565 at the base commit](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/search/SolrReturnFields.java#L554-L565)"

4. Draft says (What happens today): "[JSONWriter.java L97](https://github.com/nick-boss-tech/solr/blob/5d94e6cf3981bd8a141a2063992148893e6d1bbb/solr/core/src/java/org/apache/solr/response/JSONWriter.java#L97)" and (Limits): "[RealTimeGetComponent.java L366-L375](https://github.com/nick-boss-tech/solr/blob/5d94e6cf3981bd8a141a2063992148893e6d1bbb/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L366-L375)"
   - Evidence: neither file is in the branch diff against b5c71bc5573 (the six files are the changelog, ChildDocTransformer.java, DocTransformer.java, DocTransformers.java, SolrReturnFields.java and TestChildDocTransformerHierarchy.java). Both are pre-change code linked at head, and the text does not say so. The content at these lines is the same at base and head (JSONWriter L97 is the wantsField check; RealTimeGetComponent L366-L375 is the transform loop and the write). Citation rule: pre-change code links the merge-base and says so.
   - Replacement: "[JSONWriter.java L97 at the base commit](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/response/JSONWriter.java#L97)" and "[RealTimeGetComponent.java L366-L375 at the base commit](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L366-L375)"

Checked and consistent:
- Proof: TestChildDocTransformerHierarchy 18 of 18 at head (`receipts/SOLR-14678.md` line 6); the file has 18 @Test methods. Test links L155-L218 and L260-L326 are correct. Verified date 2026-10-07 is the round 35 and head date (receipt lines 4 and 10).
- The draft makes no fail-before claim. That is correct: `3-w2` FIX 2 says the premise tree is unconfirmed, so "fails without the fix" must stay out.
- Change links at head are correct: ChildDocTransformer.java L342-L350 (addChildrenToParent records names) and L153 (clear per transform); SolrReturnFields.java L558 (isTransformerAddedField); DocTransformer.java L119-L140 (getExtraResponseFields javadoc and default).
- Changelog link `changelog/unreleased/SOLR-14678.yml` exists at head.
- Choice: live alternative (one request-wide set, which fixes /get but reopens the round-28 leak). Matches `3-w2` finding 10 and owner decision 2.
- Limits: the aggregation (DocTransformers) gap and "no SolrCloud or distributed request" match `3-w2` findings 8 and 9 and the test file (no /get, ids, or cloud calls).
- Public text: no process vocabulary, no em dashes, verification date present.

Notes (not blocking):
- DocTransformers.java is named in the "wrapper" sentence without a link. Optional: link it (`3-w2` finding 8 gives L59 for getExtraResponseFields; not re-checked here).
- `3-w2` finding 10 suggests a javadoc sentence for the per-document scope. The Limits bullet already states the same gap, so no extra text is needed.
- Plain language: "nest-path names", "request-wide set" and "projection" are compressed; optional.

## SOLR-14931

Verdict: CONSISTENT. Held: the two round fixes below change the head, so the draft must be re-pointed and a new receipt is needed before posting.

Checked and consistent:
- Head matches. Proof counts match `receipts/SOLR-14931.md` line 6 (TestMacros 2 and TestShardMacroExpansion 1, 0 failures) and the premise in line 7 (TestMacros 2 with 1 failure; TestShardMacroExpansion 1 with 1 failure). Verified date 2026-10-07 matches lines 4 and 9.
- Test method testHandlerAppendsAndInvariantsMacrosAreExpandedOnShardRequests is at TestMacros.java L106 at head. TestShardMacroExpansion uses `configureCluster(2)` and the cloud-macro-appends configset.
- Change links at head are correct: RequestUtil.java L147 (appends expandConfigMacros), L166 (invariants), L282-L292 (expandConfigMacros helper). The branch hunks in RequestUtil.java are at base lines 146, 164 and 273 onward. The defaults path has no hunk, so "Defaults handling is unchanged" holds.
- The draft's behavior bullets match the code: invariants expanded on shards (L166); expandMacros=false skips the expansion (L288-L289); client values not expanded on shards (L170).
- Choice: live alternative (skip the second application, the reporter's suggestion; costs a behavior change for every appends and invariants value). Matches `4-s2` finding 12 and owner decision 4.
- Limits match `4-s2` finding 13 (one value is checked; a duplicated fl entry is not).
- "What happens today" matches the Jira packet `research/jira-context/SOLR-14931.json` (term_appends stays "${my_term}" while defaults expand).
- Changelog link `changelog/unreleased/SOLR-14931-shard-appends-invariants-macros.yml` exists at head; its title matches the draft.
- Public text: no process vocabulary, no em dashes, verification date present.

Notes (not blocking):
- Still at head: stray backtick in `solr/core/src/test-files/solr/configsets/cloud-macro-appends/conf/schema.xml` line 27, and stray colon in `solrconfig.xml` line 55 (confirmed). `4-s2` FIX 2 and FIX 3 remove them and move the head. Re-point the draft and get a new receipt after that.
- The receipt does not name the base SHA of the premise run. The draft says "base production code", which matches the receipt. Owner decision 5 in `4-s2`.
- Branch commits 74c5b69004c ("add hypothetical-reproduction handoff doc") and 890db21959f ("remove the speculative testing handoff doc") show in the PR commit list. `4-s2` FIX 5. Not in the draft text.
- The draft cites no GitHub run (`4-s2` finding 15).

## SOLR-15018

Verdict: CONSISTENT.

Checked and consistent:
- Head matches. The head is a test-only strengthening over 5d3d36a9ab4 (`receipts/SOLR-15018.md` line 3; `2-h1` finding 8). The schema and production code are the same at both.
- Proof counts match `receipts/SOLR-15018.md` line 6 (NestedAtomicUpdateIgnoredFieldTest 1 of 1, NestedAtomicUpdateTest 15 of 15, TestRealTimeGet 4 of 4). The premise in line 7 (base b5c71bc5573, 1 failure) matches the draft. Verified date 2026-10-07 matches lines 4 and 9.
- Pre-change link: RealTimeGetComponent.java L878-L880 at b5c71bc5573 holds the stored/doc-values check and the copy-field skip, and the draft labels it "base code".
- Change link: RealTimeGetComponent.java L873-L913 at head covers the new toSolrInputDocument code (873-889) and the rest of the method.
- Test links: NestedAtomicUpdateIgnoredFieldTest.java L58-L115 at head is the test method. The filler loop is at 86-94 (12 batches of 10 records).
- Schema: the test uses `schema-nest-ignored.xml`, which has `<dynamicField name="*" type="ignored" indexed="false" stored="false" .../>` (checked at 5d3d36a9ab4; the head delta does not touch it).
- Limits: the update log bullet matches `solrconfig-tlog.xml` lines 51-52 (maxNumLogsToKeep 10, numRecordsToKeep 100). The copy-field bullet matches `2-h1` findings 8 and 9.
- No Choice section is needed. The round report draws none.
- Changelog `changelog/unreleased/SOLR-15018.yml` exists at head.
- Public text: no process vocabulary, no em dashes, verification date present.

Notes (not blocking):
- No Jira record for SOLR-15018 exists in the local export or packets (`2-h1`, "Not checked"). The "What happens today" text rests on the code and the inventory summary. Ticket text was not checked.
- The base check also skips copy-field targets (base L879). "What happens today" says only "neither stored nor doc-valued". The change section covers the copy-field case, so this is an optional addition.

## SOLR-15041

Verdict: DRIFT (2 items). Held: `3-w6` FIX 1 says the draft must not be posted until the blank-line fix and a focused test are in.

1. Draft says (Limits): "**Not covered: a blank line inside a split value, a lone CR, and escaped encapsulators.** The test covers LF and CRLF with a `!` separator."
   - Evidence: `3-w6` finding 1 (FIX). A blank line inside a split value loses a line break, and later breaks get the wrong terminator. The break branch of `recordTerminators` (CSVLoaderBase.java head L227-L235) adds an entry for the blank line, but `split` (L177-L181) takes terminators by record index, and the parser skips the blank line (`ignoreEmptyLines`). The round's example: value `x!a\n\nb` gives `a\nb`, but should be `a\n\nb`. This is a wrong result in the change, not an untested limit, so it cannot stay in Limits as a known gap.
   - Replacement (use only after the fix and its focused test are in; until then the draft stays held): "**Not covered: a lone CR and escaped encapsulators.** The test covers LF and CRLF with a `!` separator, and a blank line inside a split value."

2. Draft says (What this change does): "**Line breaks inside a split value are kept, and the split continues after them.**"
   - Evidence: same as item 1. Without the fix, a blank line inside a split value loses its break, so the sentence overstates the change.
   - Replacement (use only after the fix and its focused test are in): "**Line breaks inside a split value are kept, and the split continues after them, including a blank line inside a split value.**"

Checked and consistent:
- Proof: TestCSVLoader 8 of 8 at head including testSplitFieldWithEmbeddedLineBreaks (`receipts/SOLR-15041.md` line 6). The test is at TestCSVLoader.java L143-L160 at head (the method is at 144). The date "Recorded 2026-10-03" matches the final receipt (lines 4 and 9).
- "The new test fails on the base code" rests on "pre-fix proof PASS" (receipt line 7). Under the AGENTS.md meaning of PASS (failed without the fix) this is supported. No base count is given, as the record requires.
- Change links at head: CSVLoaderBase.java L168-L181 (split and join at 181) and L193-L242 (recordTerminators). CRLF handling at L227-L230 matches "CRLF stays CRLF."
- Pre-change link: CSVLoaderBase.java L147-L151 at b5c71bc5573 (parser.getLine at 149; loop at 151) is correct and labeled "base".
- Changelog `changelog/unreleased/SOLR-15041.yml` exists at head.
- Choice: none. Correct, because narrow scope is not a choice (`3-w6` note 13; formula section 4).
- Public text: no process vocabulary, no em dashes.

Notes (not blocking):
- The base link points to apache/solr, not nick-boss-tech/solr. The commit is upstream history and resolves. Optional: switch to the fork for consistency with the other links.
- The changelog title ("...now preserves line breaks embedded in a quoted field value, instead of dropping everything after the first break or splitting at it") is general. It holds after the blank-line fix. Until then it overstates for that case. `3-w6` records no changelog fix.
- "Recorded" is used instead of "verified". Optional: keep "Recorded" unless the owner confirms the verification wording.
- Compressed jargon: "lone CR" and "escaped encapsulators". Optional: "a carriage return on its own" and "quote characters written with an escape character".
- No Jira packet exists for SOLR-15041 (`3-w6` note 14). The draft cites no ticket text.

## Not done

- Live Jira text was not queried (no Jira tool in this run). Packets read: `research/jira-context/SOLR-14451.json` and `SOLR-14931.json` (Solr-issues root). No packet exists for SOLR-14678, SOLR-15018 or SOLR-15041.
- No builds, tests, gate runs or test-queue commands. Proof counts were checked against the receipts and the round reports. Gate logs are not on disk.
- GitHub run IDs in the receipts were not checked. No gh calls were made.
- Fork branch tips were read with `ls-remote` only; no fetch was run. Cited SHAs were confirmed with `cat-file -t`, and line ranges were read with `git show` at each SHA.
- Runtime behavior of the changes was not run. Branch history problems (handoff commits, trailers) are noted only, and no rewrite is proposed.
