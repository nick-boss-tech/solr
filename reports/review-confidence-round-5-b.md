# Review confidence round 5, part B (RC5B)

Assignment: `assignments/pool-review-confidence-round-5.md` (Method steps 1 to 6). Claim: `claims/pool-review-confidence-round-5.md`, slice RC5B. Read-only: nothing posted, edited, pushed, built or tested.

## Verdicts

| Body | PR | Live head (API = fork tip = receipt) | Verdict |
|---|---|---|---|
| SOLR-3704 | #5103 | `de63d4e5d5d1ddde0da6a100a631e254c078bd55` | DRIFT (4 items) |
| SOLR-2681 | #5104 | `a3b1ea7994d932cdf78667f799542d7d823f7d49` | DRIFT (2 items; item 1 is a record conflict with no replacement text) |
| SOLR-4540 | #5105 | `180b6e8a7d3c23ac308a28d547f61816b5815f04` | CONSISTENT |
| SOLR-16630 | #5106 | `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7` | CONSISTENT |

## Head and record check (Method steps 1 and 2)

- Live bodies read with `gh pr view --json body` on 2026-10-11. PR numbers found with `gh pr list --author nick-boss-tech` on apache/solr.
- For all four, the API head equals the `git ls-remote origin` fork tip and the head named in `receipts/<ticket>.md`. No mismatch.
- SOLR-16630: the head is the gated head `9bea5974` plus one commit that corrects only the changelog title. The receipt records that delta, and the branch diff agrees. Judged against that record, no drift.
- The three Highlighting heads equal their receipt gated heads. No delta commits.
- Each live body equals its current `pr-drafts/` copy, ignoring the BOM and a trailing newline.
- Change points: the Highlighting drafts last changed in commit `091cc80` (2026-10-11 01:43 UTC), and the live bodies were updated at about 02:04 UTC. The SOLR-16630 draft last changed in `9f93964` (06:43 UTC), and the live body was updated at 07:02 UTC.
- Merge-bases: the Highlighting bodies cite `cabedd1d968` (merge-base with upstream main). The SOLR-16630 body cites `3f5d4c5bf8` (merge-base). Both confirmed with `git merge-base`.
- Format (`pr-formula.md`): AI header and footer on all four. A bold one-line opener on each section. No em dash. No internal process vocabulary by word search. No Lucene version claim.

## SOLR-3704 (PR #5103): DRIFT (4 items)

**Item 1. Title (and the branch changelog title).**
- Live: "...the highlighter was handed epoch milliseconds (or a Date.toString()) and failed with 'Invalid Date String'."
- Evidence: the ticket packet (`research/jira-context/SOLR-3704.json`) reports only epoch-millisecond values. The receipt proof is the stored epoch-millisecond path only (`1343779201999`). The answers record (`material/highlighting-round-1-answers.md`, SOLR-3704, correction of 2026-10-10) says the docValues Date branch "was not separately discriminated". The body's "What happens today" names only epoch milliseconds. Base code does pass `Date.toString()` text for Date values (base `DefaultSolrHighlighter` else branch), so the wording is plausible, but no record shows that failure.
- Replacement (PR title): "SOLR-3704: Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds and failed with 'Invalid Date String'."
- Note: `changelog/unreleased/SOLR-3704-highlight-date-point.yml` (line 1 at head) carries the same parenthetical. Matching it needs a branch edit and re-gate. That call belongs to the main side.

**Item 2. Limits, bullet 1, last sentence.**
- Live: "It covers docValues-only date fields and stored date fields that Solr reads from docValues."
- Evidence: the sentence before it says no test covers a docValues date value. Read as test coverage, this sentence contradicts that. The sentence describes where the new branch applies (head `SolrDocumentFetcher.java` L735-L736 and L793-L796), as the answers correction states.
- Replacement: "The new Date branch also applies to docValues-only date fields and to stored date fields that Solr reads from docValues."

**Item 3. Limits, bullet 3.**
- Live: "The new test runs hl.method=unified with a match-count check only."
- Evidence: head `solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java` L91-L125. The date range query loop (L96-L112) runs with `hl.method=original` and `hl.method=unified`. The second query (L114-L124) runs with `hl.method=original` and checks the highlighted text `<em>alpha</em>`. The live sentence misstates both the methods and the checks. Round 4 roll-up (`reports/highlighting-post-pr-review-round-4.md`) flagged the same bullet as optional. The sentence is now inaccurate.
- Replacement: "The UnifiedHighlighter code is not changed. The date range query runs with hl.method=original and hl.method=unified, and checks only that one highlighting block is returned and that one document matches. A second query checks the highlighted text with hl.method=original."

**Item 4. What this change does, bullet 3 (precision).**
- Live: "...the printed value of a date unique key (IndexSchema.java L360, L369)... now use the ISO form."
- Evidence: head `IndexSchema.java` L369 is the IndexableField branch only. The else branch (L370-L371, `val.toString()`) still prints a `java.util.Date` value as `Date.toString()`. The statement holds for the stored path only.
- Replacement: "...and the printed value of a date unique key read from a stored field (IndexSchema.java L360, L369) now uses the ISO form."

**Verified and consistent:** head citations DatePointField L161-L163, DefaultSolrHighlighter L828-L829, SolrDocumentFetcher L735-L736 and L793-L796, DefaultSolrHighlighter L474-L483 (prefetch includes the alternate field and the unique key), LukeRequestHandler L788. Base citations: FieldType L405-L413, and DatePointField L192-L193 (reached through `DefaultAnalyzer`, then `toInternalByteRef`, then `readableToIndexed`). Test L91-L125. Receipt counts: HighlighterTest 36 of 36 at head, base 1 failure with `Invalid Date String:'1343779201999'`, LukeRequestHandlerTest 8 of 8, TestPointFields 104 of 104, dated 2026-10-06 (the receipt's gate run date). Tidy, Error Prone and module check pass. Changelog link resolves at head.

## SOLR-2681 (PR #5104): DRIFT (2 items)

**Item 1. RECORD CONFLICT (no replacement text).**
- Live (Limits): "Other value sources that wrap a query are not handled. A follow-up pull request is planned for the nested forms."
- The records disagree on whether a follow-up is planned.
  - `material/highlighting-round-1-answers.md` (SOLR-2681, line 9) records the planned-submission sentence as the decision.
  - `reports/highlighting-answers-check-2681.md` (section 3), `reports/highlighting-round-1-g1.md` (line 12), and `reports/highlighting-answers-check.md` (line 11) say no record shows a plan.
  - `receipts/SOLR-2681.md` records no planned follow-up.
  - `pr-formula.md` says a broader issue gets an offer to open a follow-up on request.
- No replacement is chosen. The main side and Nick must confirm which record stands.

**Item 2. Scope wording (three places).**
- Live (What this change does): "When the top-level query is a function query whose value source is a query(...) call, the term extractor collects terms from the wrapped query." and "All other queries take the same path as before."
- Live (Behavior change): "a request whose top-level query is `query(...)` now highlights..."
- Evidence: the new branch sits in `CustomSpanTermExtractor.extract` (head `DefaultSolrHighlighter.java` L297-L304). It applies to every call to `extract`, not only to the request's top-level query. So a `query(...)` function query reached as a clause of another query also takes the new path. "All other queries take the same path" is then not accurate for composite queries that contain one. The Lucene superclass recursion is not in this checkout, so I did not verify it. The replacement is worded to hold either way. Section 4 of `reports/highlighting-answers-check-2681.md` proposed this change at an earlier head. The live body still carries the original wording.
- Replacement (What this change does): "When the term extractor reaches a function query whose value source is a query(...) call, it collects terms from the wrapped query (link unchanged). Queries with no query(...) function query take the same path as before."
- Replacement (Behavior change): "Behavior change: a query(...) function query now highlights the wrapped query's terms in the requested fields, wherever the term extractor reaches it. Before this change, those terms were not highlighted."
- The title keeps "top-level", as the answers decision records. It refers to the function's own position, which matches Limits. Not counted as drift.

**Verified and consistent:** title and changelog title (both match the receipt's shipped title); head citation L300-L303 (the new branch); base L295-L298 (method start and first branch, no FunctionQuery case); test L91-L109 (query, parameters and expected text); 36 of 36 at head, base 1 failure (the new test); verification date 2026-10-09 (title re-gate); FastVector path untouched; Limits "Only a query(...) call at the top of a function query is handled" (accurate for the function's own position); the ticket's `product($v1,$v2)` example (`research/jira-context/SOLR-2681.json`); changelog link resolves at head.

## SOLR-4540 (PR #5105): CONSISTENT

**Verified:** title and changelog title (both match the receipt's shipped title). QTime claim against the ticket (`research/jira-context/SOLR-4540.json`: over 10 s with `fulltext_*`, about 30 ms explicit). Base L636 (builder lookup first) and base L415-L421 (BAD_REQUEST "Unknown fragmentsBuilder"). Head guard L636-L640, and the caller's alternate-field fallback on null (head, around L509). Test L94-L123, failing at line 106 (receipt premise log). Receipt count 3 of 3 at head, dated 2026-10-09.

Corroboration run: fork CI run 37622803991 is `workflow_dispatch` (manual), conclusion success, job `org.apache.solr.highlight.FastVectorHighlighterTest`, created 2026-10-07, head `3543d10b`. The `solr/` tree is identical between `3543d10b` and `180b6e8a`.

Limits and the default-path statement match the diff: only the FVH method changes (five lines of guard, one test, one changelog file).

**Record note (no body change):** `receipts/SOLR-4540.md` calls `3543d10b` "the earlier head" on top of `62c06439fb0`. In the live branch history, `3543d10b` is not an ancestor of `180b6e8a` (history: `fd68bae5d1a`, `62c06439fb0`, `180b6e8a7d3`). The body says "an earlier commit" and makes no ancestry claim, so this is not drift. The receipt's wording does not show when the fork branch moved.

## SOLR-16630 (PR #5106): CONSISTENT

**Verified:** title equals the changelog title at head. The receipt's live head `9c77a4db` equals the API head. The only commit past the gated head `9bea5974` changes the changelog title. The branch diff touches only the test file and the changelog.

Base citations at merge-base `3f5d4c5bf8`: `TestCoordinatorRole.java` L221 (PULL client), L241 (random delay), L259-L261 (sleep and stop), L306 (add), L309 (catch SolrException). `SolrServerException.java` L24 (extends Exception). `JettySolrRunner.java` L612 (`stop()`), L616-L617 (close cached client), L847-L852 (`getSolrClient` cache).

Head citations: `TestCoordinatorRole.java` L243 (latch), L262-L264 (await of 2 minutes and the warning), L311-L313 (add, commit, countDown). Changelog L1-L10.

Proof: the base run on 2026-10-11 (UTC) failed with the premise shape (receipt). Six focused runs at `9bea5974` each show 1 test, 0 failures, 0 errors. The gate finished 2026-10-10 UTC (`gates/SOLR-16630.md`), matching "2026-10-10 (UTC)".

"Reopened ticket": a public JIRA REST read on 2026-10-11 shows status "Reopened", resolution null. The issue history was not checked, so this supports the claim but does not prove the transition. The pooled-connection limit is hedged ("could") and was accepted in round 3. Not a record conflict.

**Optional note (not drift):** the JIRA summary reads "...fails 100% of attempts on branch_9x", while the PR targets main. The body does not say which branch the fix covers. pr-formula.md does not require this.

## Record conflicts flagged without replacement text

- SOLR-2681 follow-up sentence: `material/highlighting-round-1-answers.md` records a planned submission. `reports/highlighting-answers-check-2681.md`, `reports/highlighting-round-1-g1.md`, and `receipts/SOLR-2681.md` record no plan. See SOLR-2681 item 1.

## Not done

No live body edit, comment, review, branch edit, draft edit, build, test or gate. No commit or push. One public, unauthenticated JIRA REST read. Scratch copies of the live bodies were written to the scratchpad, not the repository, and were removed afterwards.
