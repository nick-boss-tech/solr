# Highlighting post-PR review round 3, slice 1 (SOLR-3704, PR #5103)

Assignment: `assignments/pool-highlighting-post-pr-review-round-3.md`, slice 1. Live draft PR: apache/solr #5103, from nick-boss-tech:solr-3704-submit.
Head checked: `git ls-remote origin refs/heads/solr-3704-submit` returns `de63d4e5d5d1ddde0da6a100a631e254c078bd55`, equal to the PR head. A read-only fetch of the fork branch was run. The merge-base with upstream/main is `cabedd1d968059215188f4e7563fb303241899ed` (`git merge-base` confirms it; the base is an ancestor of the head).
Sources read from origin/pr-prepare: pr-formula.md, reports/highlighting-post-pr-review-round-2.md and -s1.md, material/highlighting-round-1-answers.md, receipts/SOLR-3704.md, pr-drafts/highlighting/SOLR-3704.md, and the round 3 assignment. claims/query-parsing-round-1.md for the Lucene rule.
Live PR read through research/gh.ps1 only (pr view, read-only api GETs for reviews, comments, check runs, workflow runs, jobs and status). Nothing written to GitHub.
Hard limits held: no build, Gradle, test, Selenium, gate or test-queue run; no PR body edit, comment, review, close, submit-branch edit or Jira write; no file edited except this report.

## 1. Verdict

**STILL OPEN: one item (D).** Round 2 items A, B and C are fixed in the live body and in the draft. The final whole-body read finds one remaining item: the changelog line is a bare code span, not a link. Slice 2 of this round treats that same form as a missing link, so the rule must be applied the same way here.

- D. Changelog line, last line of the body. Fix in section 9.

Everything else checks out at the head and the base. The CI state is a state, not a pass: three upstream workflow runs are `action_required` with no jobs.

## 2. Item table

| # | Item | Live wording (quoted) | Source check | Status |
|---|---|---|---|---|
| A | Round 2 item A: the fetch condition in bullet 2 | "That includes a stored, single-valued date field when every stored field the highlighter fetches can be read from docValues ([SolrDocumentFetcher.java L793-L796](...de63d4e5...)). The highlighter fetches the highlighted fields, any alternate field, and the unique key, not the request's `fl` ([DefaultSolrHighlighter.java L474-L483](...de63d4e5...))." | Head DefaultSolrHighlighter.java L474 `getHighlightFields`; L476 `getDocPrefetchFieldNames`, which (L591-603) always returns a non-null set of the highlighted fields plus any alternate field, so the `new SolrReturnFields(new String[0], req)` fallback at L481-482 is not reached by the default highlighter; L479 adds the unique key; L480 builds `SolrReturnFields` from those names only. SolrReturnFields.java L172 falls back to all fields only for a null or empty list, and the list always holds the key, so the request's `fl` is not used. SolrDocumentFetcher.java L789 `calcStoredFieldsForReturn` gives the stored names among the returned fields; L793 moves them to docValues only when `dvsCanSubstituteStored.containsAll(storedFields)`. `canSubstituteDvForStored` (L213-225) returns false for a multi-valued field, so "single-valued" is correct. L735-736 is `case DATE: return new Date(value);`. The condition and the fetch sentence match the code. | SATISFIED |
| A2 | Round 2 item A in the answers file (2026-10-10 correction) | "That includes a stored, single-valued date field when every stored field the highlighter fetches can be read from docValues, because RetrieveFieldsOptimizer then reads it from docValues (SolrDocumentFetcher.java lines 793 to 796 at the head). The highlighter fetches the highlighted fields, any alternate field, and the unique key, not the request's `fl` (DefaultSolrHighlighter.java lines 474 to 483 at the head)." | Same condition as the live bullet and the same code (see A). The answers file no longer names the request's `fl`. | SATISFIED |
| B | Round 2 item B: symptom citations at base, saying so | FieldType: "[FieldType.java L405-L413, base code](...blob/cabedd1d968059215188f4e7563fb303241899ed/...FieldType.java#L405-L413)". DatePointField: "[DatePointField.java L192-L193, base code](...blob/cabedd1d968059215188f4e7563fb303241899ed/...DatePointField.java#L192-L193)". | Base FieldType.java L405-415 is `toExternal`: `f.stringValue()`, with the binary fallback. Base DatePointField.java L192-193 is `readableToIndexed`, which calls `toNativeType(val.toString())`. The link label "base code" says the link is the base commit, the wording round 2 asked for. The analyzer chain holds: base DefaultSolrHighlighter.java L938 uses `getIndexAnalyzer()`; base FieldType.java L546-600 (`DefaultAnalyzer`) calls `PointField.toInternalByteRef` for point fields; that calls `readableToIndexed` (PointField.java L208-211). The date parser rejects the text at base DateMathParser.java L231-234 (no 'Z'). | SATISFIED |
| B2 | Round 2 item B: fix citations at the head | Fix links use `blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/` (section 3). | Each anchor holds the claimed code at the head (section 3). | SATISFIED |
| C | Round 2 item C: verification date on the Extra runs lines | "- LukeRequestHandlerTest passes 8 of 8 at this head, verified 2026-10-06." and "- TestPointFields passes 104 of 104 at this head, verified 2026-10-06." | Receipt: "Gate run date: 2026-10-06 (log g3704-gate.log ... the extra runs in g3704-extra.log are the same day)". Receipt counts: "LukeRequestHandlerTest 8 of 8, TestPointFields 104 of 104". Date and counts match. | SATISFIED |
| 1 | Bold one-line summary opens each section | "**Highlighting a stored DatePointField fails with "Invalid Date String".**" / "**Date values now reach the highlighter as ISO instants, such as `2012-08-01T00:00:01.999Z`.**" / "**The new test fails on the base code and passes with this change.**" / "**Tested: a stored date field, read from its stored value. Not tested: date values read from docValues, and date unique keys.**" | Four sections, four bold one-line openers. The AI assistance footer is the approved template text. | SATISFIED |
| 2 | Title accurate for the change | "SOLR-3704: Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds (or a Date.toString()) and failed with 'Invalid Date String'." | Base DefaultSolrHighlighter.java L825-829: an IndexableField goes through `toExternal`; any other value, including a `java.util.Date`, goes through `value.toString()`. A Date.toString() text has no 'Z', so DateMathParser.java L231-234 throws "Invalid Date String". Epoch ms text also has no 'Z'. The changelog YAML title is identical. | SATISFIED |
| 3 | Choice statement | None. The body has no "A choice to check" section. | The answers file records no choice for SOLR-3704. The formula requires a choice section only for a real decision. | SATISFIED (N/A) |
| 4 | Limits statements true of the code | Bullet 1: "No test in this change covers a date value read from docValues. That path takes the `java.util.Date` branch. It covers docValues-only date fields and stored date fields that Solr reads from docValues." Bullet 2: "No test in this change covers a date unique key." Bullet 3: "The UnifiedHighlighter code is not changed. The new test runs `hl.method=unified` with a match-count check only." | The diff against the base is 4 files (DatePointField.java, DefaultSolrHighlighter.java, HighlighterTest.java, the changelog YAML). No Unified file is changed. The test schema `id` has `docValues` false (schema.xml L540), so the test reads `x_date_p` from its stored value. The test schema `*_date_p` is stored, docValues, single-valued (schema.xml L863), so the stored path is the one tested. No test uses a unique key of date type. The Unified assertions (HighlighterTest.java L96-112) check the highlighting entry count and numFound only (see N2). | SATISFIED (N2 wording) |
| 5 | Proof numbers in the receipt | See section 4. | Every number matches. | SATISFIED |
| 6 | Presentation: file citations are links | All code citations are links. The changelog line is a code span (line 38). | See item D. | STILL OPEN (D) |
| 7 | No Lucene version mention | No "Lucene" or version text in the body. | claims/query-parsing-round-1.md "Lucene version claims" applies only when a version is named. | SATISFIED (N/A) |
| 8 | No internal vocabulary or run identifiers | No seed, log file name, gate, receipt, premise, takeover, JUnit, or run ID. "The extra runs above" (Limits, line 35) is a run-log label. | Body searched, case-insensitive. | SATISFIED (N3 optional) |
| 9 | No em or en dashes | 0 found. | Body searched for U+2014 and U+2013. | SATISFIED |
| 10 | Body equals the pr-prepare draft | Identical after CR removal (section 5). | `diff` exit 0. | SATISFIED |

## 3. Citation table

All fix links use `https://github.com/nick-boss-tech/solr/blob/<SHA>/<path>#L<a>-L<b>`. "Fix" means code the change produces or changes its effect on. "Symptom" means pre-change behaviour.

| # | Body location | SHA used | Fix or symptom | Lines | What the anchor holds | Check |
|---|---|---|---|---|---|---|
| 1 | What happens today, FieldType | `cabedd1d968059215188f4e7563fb303241899ed` (base) | Symptom | `solr/core/src/java/org/apache/solr/schema/FieldType.java` L405-L413 | `toExternal`, `f.stringValue()` with binary fallback | Holds at base. Label "base code". OK. |
| 2 | What happens today, DatePointField | `cabedd1d968059215188f4e7563fb303241899ed` (base) | Symptom | `DatePointField.java` L192-L193 | `readableToIndexed`, `toNativeType(val.toString())` | Holds at base. Label "base code". The chain to the highlighter analyzer holds (section 2, B). OK. |
| 3 | What this change does, bullet 1 | `de63d4e5d5d1ddde0da6a100a631e254c078bd55` (head) | Fix | `DatePointField.java` L161-L163 | `toExternal` returns `((Date) toObject(f)).toInstant().toString()` | OK |
| 4 | What this change does, bullet 2 | head | Fix | `DefaultSolrHighlighter.java` L828-L829 | `else if (value instanceof Date)` gives the ISO form | OK |
| 5 | What this change does, bullet 2 | head | Route (existing code) | `SolrDocumentFetcher.java` L735-L736 | `case DATE: return new Date(value);` | OK |
| 6 | What this change does, bullet 2 | head | Route (existing code) | `SolrDocumentFetcher.java` L793-L796 | Stored fields move to docValues when all are substitutable | OK. The condition matches (item A). |
| 7 | What this change does, bullet 2 | head | Current fetch behaviour (existing code, unchanged) | `DefaultSolrHighlighter.java` L474-L483 | `getHighlightFields`, the prefetch set, the unique key, `SolrReturnFields` from those names | OK. The file gains one import at the head, so the same code is at base L473-L482. A head link is right for code that holds at both. |
| 8 | What this change does, bullet 3 | head | Effect of the change | `LukeRequestHandler.java` L788 | `ftype.toExternal(field)` | OK |
| 9 | What this change does, bullet 3 | head | Effect of the change | `IndexSchema.java` L360 | `uniqueKeyFieldType.toExternal(f)` in `printableUniqueKey(Document)` | OK |
| 10 | What this change does, bullet 3 | head | Effect of the change | `IndexSchema.java` L369 | Same call for `SolrDocument` | OK |
| 11 | Proof, bullet 1 | head | Test added by the change | `solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java` L91-L125 | `@Test` at L91; `testHighlightDatePointFieldDoesNotFail` to L125 | OK |
| 12 | Changelog line | none (code span) | File reference | `changelog/unreleased/SOLR-3704-highlight-date-point.yml` | File exists at the head; its title matches the PR title | STILL OPEN (D) |

Symptom links at base are not stale: the base lines hold the claimed code (rows 1 and 2).

## 4. Proof and date check

| Body line (live) | Receipt line (origin/pr-prepare receipts/SOLR-3704.md) | Result |
|---|---|---|
| "With this change, HighlighterTest passes 36 of 36 at this head, verified 2026-10-06." | "Counts: HighlighterTest 36 of 36 at the head." and "Gate run date: 2026-10-06" | Match |
| "On the base code, with this test file, HighlighterTest runs 36 tests with 1 failure, which is this test, failing with `Invalid Date String:'1343779201999'`." | "HighlighterTest runs 36 tests with exactly 1 failure, the new testHighlightDatePointFieldDoesNotFail, failing with Invalid Date String:'1343779201999'" | Match |
| "LukeRequestHandlerTest passes 8 of 8 at this head, verified 2026-10-06." | "LukeRequestHandlerTest 8 of 8" and "the extra runs in g3704-extra.log are the same day" | Match |
| "TestPointFields passes 104 of 104 at this head, verified 2026-10-06." | "TestPointFields 104 of 104" and the same day | Match |
| "Tidy, the Error Prone compile, and the module check pass at this head. The changelog YAML parses." | "Changelog YAML parses; tidy clean; Error Prone compile passes; module check passes." | Match |
| Epoch value 1343779201999 | `date -u` gives 2012-08-01T00:00:01Z = 1343779201 s, so .999 s = 1343779201999 ms | Match |

Note N6: the receipt's log finish stamp is "2026-10-07 02:50 UTC". The receipt's stated run date is 2026-10-06, and the body follows the stated run date. The receipt supports 2026-10-06 as written.

## 5. Body versus draft

`pr-drafts/highlighting/SOLR-3704.md` on origin/pr-prepare, compared with the live body (read exact through gh, no CR in either). After CR removal the two are identical, 4,967 bytes each, `diff` exit 0.

## 6. CI and review state (read-only)

- PR #5103: OPEN, draft. headRefOid `de63d4e5d5d1ddde0da6a100a631e254c078bd55`, equal to the fork tip from ls-remote. mergeStateStatus UNSTABLE. reviewDecision empty.
- statusCheckRollup (the only check in the rollup): `labeler`, CheckRun, Pull Request Labeler, SUCCESS (started 2026-10-11T00:15:01Z, completed 00:15:09Z; run 38097776098).
- Workflow runs at the head (same SHA), from the Actions API, with job counts from the jobs API:
  - Gradle Precommit, run 38097776082: action_required, 0 jobs.
  - Solr Tests via Crave, run 38097776083: action_required, 0 jobs.
  - Validate Changelog, run 38097776145: action_required, 0 jobs.
  - Pull Request Labeler, run 38097776098: success (pull_request_target).
- Combined commit status: pending, 0 statuses.
- Reading: action_required with 0 jobs means the upstream workflows have not run. It is a state, not a failure and not a pass. No test output exists for these runs. The main side should not treat them as green.
- Reviews: none (reviews API returns an empty list). Issue comments: none. Inline review comments: none.

## 7. Verified and rejected automated findings

None. There are no bot comments, reviews or inline comments on #5103. The labeler is a status check with no finding.

## 8. Notes (no effect on the verdict)

- N1. The bold line in "What this change does" says "Date values now reach the highlighter as ISO instants". The change sits in DefaultSolrHighlighter's value path (head L824-L833). UnifiedSolrHighlighter.java gets its content from Lucene's highlighter and calls `solrDoc` only for unique keys (L232). Its content path was not traced. The statement is accurate for the default highlighter. Optional: "Date values now reach the default highlighter as ISO instants." Limits already says the Unified code is unchanged.
- N2. Limits bullet 3 says the Unified run has "a match-count check only". The assertions (HighlighterTest.java L110-111) are `count(//lst[@name='highlighting'])=1` and `//result[@numFound='1']`. They check the highlighting entry count and the hit count, not snippet text. Optional: "with a hit-count check only, and no snippet check."
- N3. "The extra runs above" (Limits, line 35) is a run-log label. Optional: "The other test classes that pass at this head are LukeRequestHandlerTest and TestPointFields."
- N4. Limits bullets 1 and 2 repeat the bold line's claim in their first sentences (pr-formula presentation rule). Round 2 graded this optional, so it is not counted here.
- N5. Length: 4,967 bytes, above the roughly 3,500-character guide in pr-formula. Round 2 N7 graded this not required for a ticket of this complexity.
- N6. Date basis: see section 4.
- N7. The "Tidy", "Error Prone compile" and "module check" lines are pass outcomes, not internal catches, so the proof rule allows them.

## 9. Fix list for the main side (apply to the live body and to pr-drafts/highlighting/SOLR-3704.md)

Fix D. The last line of the body (line 38). Replace

"Changelog: `changelog/unreleased/SOLR-3704-highlight-date-point.yml`"

with

"Changelog: [`changelog/unreleased/SOLR-3704-highlight-date-point.yml`](https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/changelog/unreleased/SOLR-3704-highlight-date-point.yml)"

Basis: the presentation rule in pr-formula.md (2026-10-08) says file citations are links, and round 2 required the same link for the SOLR-2681 changelog line (reports/highlighting-post-pr-review-round-2.md, slice 2, item 1). The approved template (2026-10-04) shows a code span. If the main side rules that the template governs changelog lines, item D is withdrawn for all three PRs. The lead should make that call once and apply it to all three.

After any edit: the body and the draft must match after CR removal, and the head SHA in every fix link must stay `de63d4e5d5d1ddde0da6a100a631e254c078bd55`.

## 10. Not checked

- The gate logs (g3704-gate.log, g3704-extra.log, g3704-premise.log) are not on this host. Dates and counts come from the receipt only.
- No build, test or Selenium run. Behaviour was read from the code at the head and the base.
- The UnifiedSolrHighlighter content path was not traced (N1).
- The answers file's 2026-10-10 correction was checked by reading its text against the body and the head code. It was not diffed against the body, because the two texts are not meant to match word for word.
- The takeover log named in the receipt was not read.
