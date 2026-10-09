# SOLR-2681 draft check against the highlighting round 1 answers

Checked: `pr-drafts/highlighting/SOLR-2681.md` at origin/pr-prepare, against `material/highlighting-round-1-answers.md`, `reports/highlighting-round-1.md`, `reports/highlighting-round-1-g1.md`, `receipts/SOLR-2681.md`, and `pr-formula.md`. Code read at head `4cb25b1691b9ca66552a93687f79cd62993efcf9`. No builds, no tests, no branch, PR, or Jira changes.

## 1. Count 36 of 36: must stay out of public text

The draft states the count as fact in two places (draft lines 23 and 24):

- "On the base code, with only the new test added, HighlighterTest runs 36 tests and one fails: this test."
- "At head `4cb25b1691b`, HighlighterTest passes 36 of 36."

Both must stay out until the recount is recorded. Source at head has 31 `@Test` lines (30 at base), so 36 cannot be re-derived from source.

Proposed replacement, for ratification (placeholders stay until the recount record supplies the numbers and date; formula section 3 requires a verification date):

- Line 23: "On the base code, with only the new test added, HighlighterTest runs <N> tests and one fails: this test."
- Line 24: "At head `4cb25b1691b`, HighlighterTest passes <N> of <N>, verified <recount date>."

## 2. Internal note: removed

The draft opens with the AI header and the Jira link. No delete-before-posting marker, no "note", "delete", or "internal" text remains.

## 3. "on request": gone, but the Limits sentence is an unsupported plan

Line 33 now reads: "A follow-up submission is planned for the nested forms." No record read here shows a plan. The receipt has no planned follow-up, and the G1 report says so. The formula's Limits rule says "an offer to open a follow-up ticket and PR for it on request."

Proposed replacement (formula wording, no plan claimed):

- "Other value sources that wrap a query are not handled. A follow-up ticket and PR for the nested forms can be opened on request."

Use "A follow-up submission for the nested forms is planned." only if Nick confirms the plan.

## 4. Claims against code at head

- Cited lines match: `DefaultSolrHighlighter.java` L300-L303 is the new branch; `HighlighterTest.java` L91-L109 is the test. The base cite L295-L298 shows only the method header and first branch. L295-L304 shows the full chain with no FunctionQuery case (optional tightening).
- Branch diff touches only the changelog, `DefaultSolrHighlighter.java`, and `HighlighterTest.java`. FastVector is untouched and untested. Supported.
- `{!func}product($v1,$v2)` appears in `research/jira-context/SOLR-2681.json`. The test covers only `{!func}query($v1)`. Supported.
- Changelog title is overstated. Head title: "The original highlighter now extracts terms from a query nested inside a function query, e.g. {!func}query($q)." The branch matches only a function query whose value source is `query(...)`, so `product(query(...))` is not handled. The title sits in the branch file, so it needs a branch edit and re-gate before posting (the answers agree). Exact replacement: "The original highlighter now extracts terms from a query(...) function query, e.g. {!func}query($q)." Avoid "top-level": the check keys on the function's value source, not the request's position.
- Draft line 15: "All other queries take the same path as before." Not supported as written. The new branch fires on any FunctionQuery with a QueryValueSource, and the extractor recurses into clauses. Lucene's WeightedSpanTermExtractor is not in this checkout, so the recursion is not verified. Replacement: "Requests with no `query(...)` function query take the same path as before."
- Draft behavior-change sentence: "Behavior change: a request whose top-level query is `query(...)` now highlights..." Replacement: "Behavior change: a `query(...)` function query now highlights the wrapped query's terms in the requested fields. Before this change, those terms were not highlighted."
- Limits line 31 ("Only a `query(...)` call at the top of a function query is handled"): supported.

## 5. Internal vocabulary

None of the listed terms appear in the draft. The only hits are "36" (item 1) and "planned" (item 3). Em dashes: 0. First-person plural: none.

## 6. Posting state

Blocked by the recount (item 1) and by the changelog title edit with re-gate (item 4). Replacement texts above are for the owner's ratification and are not applied.

SOLR-2681 posting state: HOLD until recount
