# Highlighting post-PR review round 4, slice 1 (SOLR-3704, PR #5103)

Assignment: `assignments/pool-highlighting-post-pr-review-round-4.md`, slice 1. Live draft PR: apache/solr #5103, from nick-boss-tech:solr-3704-submit.

Head check: `git ls-remote origin refs/heads/solr-3704-submit` returns `de63d4e5d5d1ddde0da6a100a631e254c078bd55`, equal to the PR `headRefOid`. A read-only fetch of the fork branch was run, and `origin/solr-3704-submit` resolves to the same SHA. The base `cabedd1d968059215188f4e7563fb303241899ed` is an ancestor of the head. The diff from base changes four files: the changelog YAML (added), `DefaultSolrHighlighter.java`, `DatePointField.java`, and `HighlighterTest.java`.

Read sources (from origin/pr-prepare unless noted): the round 4 assignment, pr-formula.md, reports/highlighting-post-pr-review-round-3.md and -s1.md, material/highlighting-round-1-answers.md, receipts/SOLR-3704.md, pr-drafts/highlighting/SOLR-3704.md, claims/query-parsing-round-1.md (Lucene and vocabulary rules). Live PR read through research/gh.ps1 only (pr view, read-only API GETs for reviews, comments, check runs, workflow runs, jobs, and commit status).

Hard limits held: no build, Gradle, test, Selenium, gate, or test-queue run. No PR body edit, comment, review, close, submit-branch edit, or Jira write. The only file written is this report.

## 1. Verdict

**SATISFIED.** Every check in the assignment passes at the head. The items in section 9 are optional wording and completeness notes. None of them is a required change for this slice.

## 2. Changelog line and file (check 1)

Live line (near the end, before the AI assistance footer):

`Changelog: [changelog/unreleased/SOLR-3704-highlight-date-point.yml](https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/changelog/unreleased/SOLR-3704-highlight-date-point.yml)`

- The link uses the head SHA `de63d4e5d5d1ddde0da6a100a631e254c078bd55`.
- The file exists at the head: `git ls-tree` shows blob `e9ea53d0a33105e33876e165d4a3bc9264fde25c` at `changelog/unreleased/SOLR-3704-highlight-date-point.yml`.
- Its title is "Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds (or a Date.toString()) and failed with 'Invalid Date String'." It is identical to the PR title. The title is accurate for the code. Epoch text reaches the highlighter on the stored path, and a `java.util.Date` reaches it on the docValues path (base `DefaultSolrHighlighter.java` L825-829 sends any non-`IndexableField` value through `toString()`).

## 3. Item table

| # | Item | Live wording (quoted or summarized) | Source check | Status |
|---|---|---|---|---|
| 1 | Changelog line is a link at the head | "Changelog: [changelog/unreleased/SOLR-3704-highlight-date-point.yml](...de63d4e5...)" | Link SHA is the head. File exists at the head (blob e9ea53d). | SATISFIED |
| 2 | Changelog file title accurate | Title identical to PR title | Holds for the stored path (epoch text) and the docValues path (Date.toString, base L825-829). | SATISFIED |
| 3 | Fix citations: head SHA, lines hold the claim | Eight head links, listed in section 4 | Each anchor checked with `git show <head>:<path> \| sed -n`. All hold the claimed code. | SATISFIED |
| 4 | Symptom citations: merge-base SHA, text says so | "FieldType.java L405-L413, base code" and "DatePointField.java L192-L193, base code", both at `cabedd1d...` | Base lines hold `toExternal` (stringValue, binary fallback) and `readableToIndexed` (`toNativeType(val.toString())`). Labels say "base code". | SATISFIED |
| 5 | Title accurate for the change | "SOLR-3704: Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds (or a Date.toString()) and failed with 'Invalid Date String'." | Stored path: epoch text has no 'Z', so `DateMathParser` (base L231-234) throws "Invalid Date String". docValues path: `Date.toString()` also has no 'Z'. The docValues path is code-derived only (see Limits check, item 10). | SATISFIED |
| 6 | Proof numbers and dates match the receipt | Section 5 | Every count and date matches `receipts/SOLR-3704.md`. | SATISFIED |
| 7 | Fetch-condition sentence accurate | "That includes a stored, single-valued date field when every stored field the highlighter fetches can be read from docValues ... The highlighter fetches the highlighted fields, any alternate field, and the unique key, not the request's `fl` (DefaultSolrHighlighter.java L474-L483)." | Head L474 `getHighlightFields`; L476 `getDocPrefetchFieldNames` (L591-603) adds highlighted fields and alternate fields; L479 adds the key; L480 builds `SolrReturnFields` from those names. The `fl` fallback at L482 is not reached on the default path. `SolrDocumentFetcher.java` L793-796 moves stored fields to docValues when `dvsCanSubstituteStored.containsAll(storedFields)`. `canSubstituteDvForStored` (L213-225) returns false for multi-valued fields, so "single-valued" is right. | SATISFIED |
| 8 | Each section opens with a bold one-line summary | Four sections: "What happens today", "What this change does", "Proof", "Limits". Each opens with one bold line. | Checked in the live body. No "Choice" section (see item 9). | SATISFIED |
| 9 | Choice statement true of the code | No "A choice to check" section. | The answers file records no choice for SOLR-3704. The formula requires the section only for a real decision. See note N8. | SATISFIED (N/A) |
| 10 | Limits statements true of the code | Bullet 1: "No test in this change covers a date value read from docValues. That path takes the `java.util.Date` branch. It covers docValues-only date fields and stored date fields that Solr reads from docValues." Bullet 2: "No test in this change covers a date unique key." Bullet 3: "The UnifiedHighlighter code is not changed. The new test runs `hl.method=unified` with a match-count check only." | The diff has no Unified file. The `id` field is stored and not docValues by default (`schema.xml` L540), so the test's date field is read from its stored value. No test uses a date unique key. Bullet 3 is true for the date query, but the test loop (HighlighterTest.java L96-111) also runs `hl.method=original`. See N1 and N2. Caveat on the inherited test: see section 10. | SATISFIED |
| 11 | Bold Limits line matches the answers file | "Tested: a stored date field, read from its stored value. Not tested: date values read from docValues, and date unique keys." | The answers file correction (2026-10-10) asks for exactly this widened scope. | SATISFIED |
| 12 | Answers-file decisions reflected | Limits as above; the docValues path is named as untested; the fetch sentence drops the names of the request's `fl`. | Answers file text matches the body's substance. The body omits the answers file's clause "because RetrieveFieldsOptimizer then reads it from docValues". That is a shortening, not a change of claim. See N11. | SATISFIED |
| 13 | No Lucene version mention, or all versions named | No "Lucene" or version text in the body. | `claims/query-parsing-round-1.md` applies only when a version is named. | SATISFIED (N/A) |
| 14 | No internal vocabulary, run identifiers, or workspace words | Searched the live body for submission, gate, receipt, ledger, handoff, takeover, audit, JUnit, premise, seed, log, harden, gradle, workspace, and "run". Matches: "extra runs above" (Limits), "Tidy, the Error Prone compile, and the module check pass" (Proof). | No banned word found. "Submission" does not appear. The two matches are noted as N3 and N5. | SATISFIED |
| 15 | No em or en dashes | 0 found in the live body (checked U+2014 and U+2013). | Same check on the draft. | SATISFIED |
| 16 | Body equals the pr-prepare draft | Identical after CR removal (section 6). | `diff` exit 0. | SATISFIED |
| 17 | Behavior changes stated openly | Bullet 3 names Luke's per-field value and the printed value of a date unique key as outside uses that now use the ISO form. | Verified in code (section 4, rows 8-10). A wider outside use is noted as N7. | SATISFIED |
| 18 | Length guide | 5,109 bytes (CR removed). | Guide is about 3,500 characters unless the ticket is complex. Round 3 graded this not required. See N9. | SATISFIED (note) |

## 4. Citation table

All fix links use `https://github.com/nick-boss-tech/solr/blob/<SHA>/<path>#L<a>-L<b>`. "Fix" means code the change produces. "Route" means existing code the claim depends on, in a file the change does not touch. "Effect" means a caller whose output changes through the new `DatePointField.toExternal`. "Symptom" means pre-change behavior.

| # | Body location | SHA used | Fix, symptom, route, or effect | File and lines | What the lines hold | Check |
|---|---|---|---|---|---|---|
| 1 | What happens today, FieldType | `cabedd1d968059215188f4e7563fb303241899ed` (merge-base) | Symptom, label "base code" | `solr/core/src/java/org/apache/solr/schema/FieldType.java` L405-L413 | `toExternal`: `f.stringValue()`, binary fallback | SATISFIED |
| 2 | What happens today, DatePointField | `cabedd1d...` (merge-base) | Symptom, label "base code" | `DatePointField.java` L192-L193 | `readableToIndexed`: `toNativeType(val.toString())` | SATISFIED |
| 3 | What this change does, bullet 1 | `de63d4e5...` (head) | Fix | `DatePointField.java` L161-L163 | `toExternal` returns `((Date) toObject(f)).toInstant().toString()` | SATISFIED |
| 4 | What this change does, bullet 2 | head | Fix | `solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java` L828-L829 | `} else if (value instanceof Date) {` and the ISO string (base line 825-829 had only `toString()`) | SATISFIED |
| 5 | What this change does, bullet 2 | head | Route (`SolrDocumentFetcher.java` is not in the diff) | `solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java` L735-L736 | `case DATE: return new Date(value);` in `decodeNumberFromDV` | SATISFIED |
| 6 | What this change does, bullet 2 | head | Route (not in the diff) | `SolrDocumentFetcher.java` L793-L796 | `if (storedFields != null && dvsCanSubstituteStored.containsAll(storedFields))` moves stored fields to docValues | SATISFIED |
| 7 | What this change does, bullet 2 | head | Current fetch behavior (file changed by one import; head link is right for current code) | `DefaultSolrHighlighter.java` L474-L483 | `getHighlightFields`, prefetch set plus key, `SolrReturnFields` built from those names | SATISFIED |
| 8 | What this change does, bullet 3 | head | Effect (not in the diff) | `solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java` L788 | `f.add("value", ... ftype.toExternal(field))` | SATISFIED |
| 9 | What this change does, bullet 3 | head | Effect (not in the diff) | `solr/core/src/java/org/apache/solr/schema/IndexSchema.java` L360 | `uniqueKeyFieldType.toExternal(f)` in `printableUniqueKey(Document)` | SATISFIED |
| 10 | What this change does, bullet 3 | head | Effect (not in the diff) | `IndexSchema.java` L369 | `uniqueKeyFieldType.toExternal((IndexableField) val)` in `printableUniqueKey(SolrDocument)` | SATISFIED |
| 11 | Proof, bullet 1 | head | Test added by the change | `solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java` L91-L125 | `@Test` at L91; the method ends at L125 | SATISFIED |
| 12 | Changelog line | head (`de63d4e5...`) | File reference, link | `changelog/unreleased/SOLR-3704-highlight-date-point.yml` | File exists at the head (blob e9ea53d) | SATISFIED |

Base-code symptom lines were checked against `cabedd1d...`, so the labels are accurate. The head links for route and effect rows are correct because those files are unchanged by the PR, or because the claim is about the current head.

## 5. Proof and date check

| Body line (live) | Receipt line (`receipts/SOLR-3704.md`, origin/pr-prepare) | Result |
|---|---|---|
| "With this change, HighlighterTest passes 36 of 36 at this head, verified 2026-10-06." | "Counts: HighlighterTest 36 of 36 at the head." and "Gate run date: 2026-10-06" | Match |
| "On the base code, with this test file, HighlighterTest runs 36 tests with 1 failure, which is this test, failing with `Invalid Date String:'1343779201999'`." | "HighlighterTest runs 36 tests with exactly 1 failure, the new testHighlightDatePointFieldDoesNotFail, failing with Invalid Date String:'1343779201999'" | Match |
| "LukeRequestHandlerTest passes 8 of 8 at this head, verified 2026-10-06." | "LukeRequestHandlerTest 8 of 8" and "the extra runs in g3704-extra.log are the same day" | Match |
| "TestPointFields passes 104 of 104 at this head, verified 2026-10-06." | "TestPointFields 104 of 104" and the same day | Match |
| "Tidy, the Error Prone compile, and the module check pass at this head. The changelog YAML parses." | "Changelog YAML parses; tidy clean; Error Prone compile passes; module check passes." | Match |
| Epoch value 1343779201999 is 2012-08-01T00:00:01.999Z | `date -u -d @1343779201` gives 2012-08-01T00:00:01, so the .999 part gives 1343779201999 ms | Match |
| Base error text format | Base `DateMathParser.java` L231-L234 throws `"Invalid Date String:'" + val + '\''` when the text has no 'Z' | Matches the receipt's message |

Note N10: the receipt's gate log finish stamp is "2026-10-07 02:50 UTC", while the stated run date is 2026-10-06. The body follows the receipt's stated date. The receipt does not name a time zone. 02:50 UTC on 2026-10-07 is 19:50 on 2026-10-06 in US Pacific daylight time, which may explain the stated date. No change is needed unless the main side wants to state the time zone.

## 6. Body versus draft

`pr-drafts/highlighting/SOLR-3704.md` on origin/pr-prepare, compared with the live body (read exact through gh). Both files were converted to LF (CR removed). The live body is 5,109 bytes and the draft is 5,109 bytes. `diff` exit 0. No differences.

## 7. CI and review state (read-only)

- PR #5103: OPEN, draft. `headRefOid` `de63d4e5d5d1ddde0da6a100a631e254c078bd55`, equal to the fork tip. `mergeStateStatus` UNSTABLE. `reviewDecision` empty.
- `statusCheckRollup` (the only check in the rollup): `labeler`, CheckRun, "Pull Request Labeler", SUCCESS (run 38097776098, completed 2026-10-11T00:15:09Z).
- Workflow runs at the head SHA (Actions API), with job counts (jobs API):
  - Gradle Precommit, run 38097776082: `action_required`, 0 jobs.
  - Solr Tests via Crave, run 38097776083: `action_required`, 0 jobs.
  - Validate Changelog, run 38097776145: `action_required`, 0 jobs.
  - Pull Request Labeler, run 38097776098: `success` (pull_request_target).
- Combined commit status: `pending`, 0 statuses.
- Reading: `action_required` with 0 jobs means the upstream workflows have not run for this head. It is a state, not a failure and not a pass. No test output exists for these runs.
- Reviews: none (`repos/apache/solr/pulls/5103/reviews` returns an empty list). Issue comments: none. Inline review comments: none.

## 8. Automated findings

- None present on #5103. The labeler check posts no finding, and there are no bot comments.
- Verified: none.
- Rejected: none.

## 9. Notes (not counted in the verdict)

- N1. Limits bullet 3 says the new test "runs `hl.method=unified` with a match-count check only." That is true for the date query, but the loop at HighlighterTest.java L96-111 runs the date query with `hl.method=original` too. Optional wording: "The date query runs with `hl.method=original` and `hl.method=unified`, with a hit-count check only. No test checks the highlighted date text."
- N2. Limits bullet 1, second sentence: "It covers docValues-only date fields and stored date fields that Solr reads from docValues." The word "It" refers to the Date branch. Optional: "The Date branch covers ...". Accurate as written.
- N3. Limits bullet 2 opens with "The extra runs above are LukeRequestHandlerTest and TestPointFields." That is a run-log sentence inside Limits, and Proof already names both classes. Optional: remove the sentence and keep "No test in this change covers a date unique key."
- N4. Presentation rule: the bold Limits line and bullets 1 and 2 state the same claim twice. Round 3 graded this optional, and it is not counted here.
- N5. Proof says "Tidy, the Error Prone compile, and the module check pass." These are pass outcomes, not caught-and-fixed slips, so the proof rule allows them. Optional: a maintainer may not know "tidy" as a check name.
- N6. The bold line "Date values now reach the highlighter as ISO instants" is accurate for the default highlighter, which is what the cited code changes. `UnifiedSolrHighlighter` gets its field text from Lucene's `UnifiedHighlighter.loadFieldValues` (`UnifiedSolrHighlighter.java` L429-L437 calls `super`), which this change does not touch. I did not trace the Lucene loader in full. Optional wording: "reach the default highlighter". The Limits line that the Unified code is unchanged is true.
- N7. Outside uses of `DatePointField.toExternal`: the body names Luke's per-field value and the printed unique key. A third use is `TopGroupsResultTransformer.java` L246 and L308, which print a date unique key as the grouped shard `id`. The coordinator reads that value back as a string (L191). The body's "printed value of a date unique key" covers it in general terms, so no change is required. Optional: name it.
- N8. No choice section. The answers file records none for SOLR-3704. Luke's value and the printed unique key change from epoch milliseconds to ISO text, which a maintainer could reasonably question. Whether that calls for a choice section is the main side's decision, and it is not raised here.
- N9. The body is 5,109 bytes, above the roughly 3,500-character guide. Round 3 graded this not required for a ticket of this complexity.
- N10. Date basis: see section 5.
- N11. The answers file's clause "because RetrieveFieldsOptimizer then reads it from docValues" is not in the body. The body keeps the widened scope, so this is a shortening. Optional.
- N12. The title's "(or a Date.toString())" branch is supported by code reading only. The Limits line says the docValues path is untested, so the title and the Limits agree.

## 10. Not checked

- No test run. The Proof counts come from the receipt only. The gate logs named in the receipt were not read here.
- Inherited test. `HighlighterWithoutStoredIdTest` extends `HighlighterTest`, so it inherits `testHighlightDatePointFieldDoesNotFail`. Its `@BeforeClass` sets `solr.tests.id.docValues=true` and `solr.tests.id.stored=false` and never clears them. JUnit 4 runs the parent's `@BeforeClass` (`initCore`, HighlighterTest.java L58-L60) before the subclass's, so the subclass's schema most likely keeps the default `id` settings. The property can still leak to a later class that runs in the same JVM. I did not run anything to settle this. If the subclass did read the date through docValues, bullet 1 of Limits ("No test in this change covers a date value read from docValues") would be wrong for that class. This is a question for the test harness, not a body edit. A later verify run would settle it.
- The default-setting assumption behind "Tested: a stored date field, read from its stored value" depends on the same property defaults (`${solr.tests.id.docValues:false}`, `schema.xml` L540).
- The Lucene loader used by the Unified highlighter was not traced beyond `UnifiedSolrHighlighter.java` L429-L437 (see N6).
- No Jira or GitHub write was made. The only write is this report file.

## Sources checked

- Head SHA: `git ls-remote origin refs/heads/solr-3704-submit`, `git rev-parse origin/solr-3704-submit`, `git merge-base --is-ancestor cabedd1d... de63d4e5...`.
- Head and base file lines: `git show <sha>:<path>` with `sed -n`, and `git diff --name-status cabedd1d... de63d4e5...`.
- Live PR: `research/gh.ps1 pr view 5103 --json ...` (metadata and rollup), `--json body` (body), `api repos/apache/solr/pulls/5103/reviews`, `.../issues/5103/comments`, `.../pulls/5103/comments`, `api repos/apache/solr/commits/<head>/check-runs`, `api repos/apache/solr/actions/runs?head_sha=<head>`, `api repos/apache/solr/actions/runs/<id>/jobs`, `api repos/apache/solr/commits/<head>/status`.
