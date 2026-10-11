# Review: CombinedQueryComponent Jira ticket draft

Verdict: **STILL OPEN.** The text criteria (5 to 9) are satisfied. The facts criteria (1 to 4 and 10) have one fact disagreement that the main side must settle before the ticket can be filed: the Symptom and End-to-end passages describe the real timeAllowed exit as inflating the query's totals, but the code shows that exit leaves no per-query entry for the query, so the inflation shown in the reproduction is from a constructed shape, not from the real exit. The bracketed maximum-fold item stays bracketed and unresolved, as the assignment requires. The reviewer did not rule on it.

Scope: `material/combinedquerycomponent-ticket-draft.md`, checked against `reports/combinedquerycomponent-premise.md` (including its main-side run addendum) and the component at main `d76fc14b9857e77b830d7a82514b28c523a239c2`. Nothing was run, built, tested, gated, filed, posted or emailed. The reviewer does not file in Jira or create the ticket.

## Wording edits made to the draft (text criteria, plus two from the facts check)

The draft was edited by two review passes running in parallel. The final text was read in full and is coherent. Each edit is wording or markup only.

- **E1, Symptom bullet 1.** Before: "The merged {{numFound}} for that query includes hits ... and the merged {{approximateTotalHits}} includes ..." After: "That query's {{numFound}} includes hits ... and its {{approximateTotalHits}} includes ...". Reason: the merged response's `numFound` is the query's total only where that query wins the maximum at 441 to 442. (Criterion 6.)
- **E2, Symptom bullet 2.** Before: "The merged {{numFound}} itself is correct in that case (5)". After: "The {{numFound}} for that query is correct in that case (5)". Same reason. (Criterion 6.)
- **E3, Mechanism.** Before: "so two shards of 2 and 3 give 2 instead of 5; here they give 7 instead of 5." After: "so two shards of 2 and 3 in arrival order A then B give 2 instead of 5; here the same order gives 7 instead of 5." Reason: both values depend on arrival order. (Criterion 5.)
- **E4, Fix options.** Before: "Testing the skip before accumulating leaves every total describing the same set of shards." After: "Testing the skip before accumulating leaves each query's totals describing the same set of shards that query merges." Reason: the skip is decided per (query, shard), so shard sets can differ across queries. (Criterion 5.)
- **E5, Symptom bullet 1 and line 21.** The fallback symptom now says "no {{approximateTotalHits}} header" rather than "no headers", because a shard that sends a header carrying `maxHitsTerminatedEarly` still makes the approximate total reach the response. The facts check made this edit. (Criterion 3.)
- **E6, Symptom bullet 3.** "An all-exact merge, the ordinary case, reports ..." became "An all-exact merge reports ...". The frequency of all-exact merges was not traced, so "the ordinary case" is unsupported. (Criterion 4.)

## Criteria 5 to 9: SATISFIED

- **5, fix-option reasoning: CONFIRMED** (after E3 and E4). The skip-before-accumulating route is picked and marked as the recommended first change. The fallback operand and fold-direction changes are named as required parts of the same fix, with the evidence reasons. The compensation route is presented and declined on reasoning that matches the code. The all-skipped detail is stated as a detail.
- **6, the bracketed open item: CONFIRMED.** The item is present at the end of the Fix options, bracketed, and unresolved. Its wording keeps the union-against-maximum question open and says the reference guide defines no `numFound`. It does not assert either answer. The only other maximum statement is the code fact at 441 to 442, which is allowed.
- **7, Jira wiki markup: CONFIRMED.** Four `h2.` headings; every bullet starts with a space, asterisk and space; every code span is in `{{...}}`; no GitHub markdown in the ticket text.
- **8, disclosure form: CONFIRMED.** The AI header in the underscore form is the first line of the description. The footer is the last element, with the prescribed wording.
- **9, internal vocabulary: CONFIRMED for the ticket text.** No gate, receipt, ledger, claim or review-round language, and no "we". Two flags, not edited: the bracketed item carries placeholder wording that must be removed when the item is resolved; and the preamble above the rule (review returns SATISFIED, consolidated email, and similar) is not ticket text and must not be pasted into Jira. Only the text from the Type line down goes to Nick.

## Criteria 1 to 4 and 10: STILL OPEN

- **1, title: CONFIRMED.** The title names `mergeIds` and all three defects. The ordering is correct: the folds at 342 and 343 sit before the skip at 399 to 400; the fallback is at 384; the inverted fold is at 343 against the default at `SolrDocumentList` line 50.
- **2, line references: CONFIRMED** at d76fc14b9857 for every cited line: 315, 328, 330 to 331, 334, 342, 343, 348 to 350, 359 to 364, 381 to 387 (the fallback at 384, the header value at 386), 390 to 396, 398, 399 to 400, 441 to 442, 449, `QueryComponent` 1173 to 1174, and `SolrDocumentList` line 50. Note: the premise report's "383 to 387" omits 381 and 382, which declare the `ath` variable the fix must also move. The draft's 381 is right.
- **3, reproduction values: CONFIRMED against the run addendum, with one flag.** The probe class and seed match. Case P (7 against 3, and 15 against 5), case F (numFound 5 correct, approximate 7 against 5), cases X1 and X2, and the arrival-order statement (A then B 7, the other order 8) all match. The draft makes no response-size claim.
  - **Flag B, line 48:** "All seven probe cases failed" is correct as a count, but the mapping is not stated. The addendum's seven are P, P2, F, X1, X2, M1 and M2. The reproduction lists P, F and X1 and X2; M1 and M2 appear only inside the bracketed item. The addendum gives no assertion expectations for M1 and M2. **Decision for Nick or the main side:** name the seven, or state the mapping. This touches the bracketed area.
- **4, verification level: STILL OPEN** (Finding A and Flag C).
  - Confirmed: "Confirmed by run" matches the addendum; the scratch probe is named and described as not a proposed test; the draft claims no cluster reproduction.
  - **FINDING A (fact disagreement; main side decides).** Lines 20 and 56 say the real exit (timeAllowed or "another query limit") leaves the shard's documents dropped but its totals counted. The code shows otherwise. `CombinedQueryComponent` line 210 calls `doFieldSortValues` inside the shard loop with no catch. `QueryComponent` lines 571 to 575 set an empty `sort_values` and rethrow. So `sort_values_<q>` and the per-query entry (213 and 215 to 219) are skipped for the exiting query. `SearchHandler` lines 507 to 509 catch the exception and call `shortCircuitedResults`, which adds no per-query entry. Consequence, by code reading and not run: on a real exit the shard has no per-query entry, so lines 338 to 341 leave `docs` null, and 342 and 343 never fold its count or its exactness. The real exit therefore does not inflate `numFound` the way the reproduction does. The approximate total can still be inflated at 386 if the shard's header carries it. The reproduction's P shape is a constructed case, and the report says so ("a different shape"; "not a traced real shard path"). The draft drops that caveat. "Query limit" is also not supported: the report names only the timeAllowed path, while the code catches the exit exception for any query limit. **Options for the main side:** trace or run a real exit shape before the sentences "still counts the shard" and the End-to-end paragraph stand; or label the P shape as constructed and reword the Symptom to match what the code does. If the exit lands on a query index above zero, the per-query list is shorter than the query count, and lines 339 to 340 read `docList.get(queryIndex)` with no size check. That shape may fail differently; it is the report's unrun adjacent item.
  - **Flag C, line 48:** "Confirmed by run on this checkout". The run was on vm1, in a worktree, against unmodified main. "This checkout" names nothing a Jira reader can see. **Suggested wording:** "Confirmed by run on unmodified main d76fc14b9857."
- **10, the Related line: CONFIRMED.** It names SOLR-14758 and SOLR-15595; both exist in the main history (`812de21e75d` and `a72c9931238`, `47c1fdcfb70`). The sibling defect is described as covered by its own ticket, filed separately, and this ticket is that follow-up, which matches the mergeIds draft's Related line. No ticket number is invented. The mergeIds draft is marked not filed, so "filed separately" holds once it is filed, which the consolidated email orders first.

## Checked clean

- The `partialResults=true` response header at line 24 matches the component source (`CombinedQueryComponent` lines 456 to 463, set when `thereArePartialResults` is true).
- The probe class name and seed in the draft match the run addendum.

## What clears the verdict

1. **Finding A:** the main side traces or runs a real exit shape and rewords the Symptom and End-to-end passages to match, or labels the P shape as constructed.
2. **Flag B:** name the seven probe cases, or state the mapping. The bracketed item stays unresolved until Nick rules on it.
3. **Flag C:** replace "this checkout" with "unmodified main d76fc14b9857".
4. The bracketed maximum-fold item needs Nick's ruling before the text can be filed. The reviewer does not rule on it.
5. Remove the placeholder wording in the bracketed item when it is resolved, and keep the preamble out of the email.
