# Review confidence round 9: SOLR-13568 (PR 5014)

Verdict: **SATISFIED.** The last open body in this set now reads as the round 8 ruling requires. The 19-body set covered by rounds 4 to 9 is fully settled.

Head check: PR 5014 is open, not a draft, head ref `solr-13568-submit`, head `ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721`. The fork tip matches.

Read-only. The live body was read through the GitHub wrapper; no body, comment, branch or record was edited, and nothing was built, tested or gated. Readiness is not judged.

## Checks

1. **The replacement sentence, in "A choice to check": CONFIRMED.** The live body reads "Only a later request whose page holds the same groups could hit, and this change does not measure how often that happens." Exact match.

2. **The old sentence is gone: CONFIRMED.** "Only a repeat of the same page could hit" appears nowhere. "Same page" survives only in the Limits example that round 8 accepted.

3. **The surrounding text is unchanged: CONFIRMED.** "Moving to another page never reuses these entries, because each page's group query is built from that page's own groups." is verbatim. The switch sentence ("If maintainers would rather keep cache hits available for repeated identical page requests, the switch is the route to take.") is present.

4. **No unmeasured frequency word: CONFIRMED.** "Rarely" is absent from the body. It survives only in the code comment at the head, line 437, which is outside the body and is a recorded main-side decision. The wording that remains is either a disclaimer ("how often that happens"), a flagged unmeasured cost that round 8 accepted, a conditional ("for a popular first page"), or consistent with the uncached path ("every time").

5. **The Limits text, as round 8 accepted: CONFIRMED.** "...paid in exchange for the filter cache no longer filling with per-page group queries while paging" and the "same page of the same query, requested again by any user" example are both verbatim.

6. **The code, `ExpandComponent.java` at the head, lines 840 to 897: CONFIRMED for the paths the replacement covers.**
   - The ordinal path builds a `TermInSetQuery` from `ordBytes` (lines 887 to 896, called at 334).
   - The non-point numeric path builds a `TermInSetQuery` over the group set (lines 840 to 854, called at 370).
   - Point fields (line 368) build a point set query via `getPointGroupQuery` (lines 857 to 867), not a `TermInSetQuery`. The body never says `TermInSetQuery`, so the body is not wrong.
   - Null-group pages (lines 384 to 392) wrap the group query in a boolean query with an existence clause, so equality also depends on `expandNullGroup`. "Could hit" stays accurate as a necessary condition.
   - That `TermInSetQuery` equality is over the term set is Lucene behaviour, which was not verified against the Lucene source here.

7. **The receipt, `receipts/SOLR-13568.md`: CONFIRMED.** Line 7 records the premise run on base production (9 tests, exactly 1 failure, "expected:<3> but was:<5>"). It has no hit, hit-rate or eviction record, and nothing on scope. The head `ac5d60c214c` matches. Non-blocking gaps: the receipt says "module check passes" without naming `:solr:core:check -x test`, as the body does; and the gate log `g13568-rereview-gate.log` is not in the worktree and was not read.

8. **Round 8 item 3 (the optional "no other page of those results" wording): not required.** The subject of "What happens today" is paging through expanded results, and within one paging sequence the claim holds, because each page's group set is built from that page's own documents (head lines 332 to 363). Read alone, "no other page can reuse" could be taken as any page, which "A choice to check" now contradicts. The optional wording removes that reading. The body reads accurately without it.

## Observation (outside the round 8 items; not a round 9 finding)

"What happens today" says the per-page group query "is stored in the filter cache anyway ... on every page." The code builds a group query only when the count is above zero and below 200 (head lines 333 and 366; the same cap is in the gated parent at lines 332 and 365). Pages with no groups, or with 200 or more, store none. Round 8 accepted the sentence verbatim, so this does not reopen the verdict. If the main side ever edits the body, a code-true wording would be "on each page with 1 to 199 groups". Any such edit needs a fresh confirmation round.

## Settled set

Rounds 4 to 9 covered 19 live bodies. Each is now settled: SATISFIED in the round in which its last open item landed, or closed by a recorded decision. Fork-run citations in the settled set follow decision C2, and dates follow decision C1.
