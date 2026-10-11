# Highlighting post-PR review round 1, slice 1 (SOLR-3704)

Live draft PR: apache/solr #5103, from nick-boss-tech:solr-3704-submit.
Head checked: de63d4e5d5d1ddde0da6a100a631e254c078bd55.
Sources: origin/pr-prepare (assignment, pr-formula.md, material/highlighting-round-1-answers.md, receipts/SOLR-3704.md, pr-drafts/highlighting/SOLR-3704.md, claims/query-parsing-round-1.md, WORKFLOW.md). gates/SOLR-3704.md is not on origin/pr-prepare (no such file). Live PR read through research/gh.ps1 (read-only only).
Branch diff checked against merge-base cabedd1d968059215188f4e7563fb303241899ed (upstream/main at review time was 3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c). The branch adds 51 lines and deletes none, in 4 files: DatePointField.java, DefaultSolrHighlighter.java, HighlighterTest.java, and the changelog YAML.

## 1. Verdict

**STILL OPEN.** Three fixes are needed. Apply each to the live body and to pr-drafts/highlighting/SOLR-3704.md together, so the two stay equal.

- F1 (wording, code-accuracy): the body says the docValues route covers "Date docValues fields that are not stored" and "docValues-only" fields. The code also sends a stored, single-valued date field through docValues when the request's `fl` names only such fields. The Limits and the What bullet must name that route. (Items 7a, 7b, 7e.)
- F2 (formula): the Proof line has no verification date. pr-formula.md section 3 asks for "verified <date> at this head". The receipt records no run date, so the receipt must record one first. (Item 4a.)
- F3 (formula): "Extra runs" puts two test classes on one line. pr-formula.md asks for one line per test class. (Item 4b.)

Everything else checks out: head, title, body-to-draft equality, every Proof number, citations and anchors, bold summaries, the Choice position, and the Lucene rule (no Lucene mention).

## 2. Item table

| # | Item | Live wording (short) | Source check | Status |
|---|---|---|---|---|
| 1 | Head and state | headRefOid de63d4e5d5d1ddde0da6a100a631e254c078bd55; OPEN; draft; mergeStateStatus UNSTABLE | `git ls-remote origin refs/heads/solr-3704-submit` returns the same SHA; fork fetch done read-only | SATISFIED |
| 2 | Title | "SOLR-3704: Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds (or a Date.toString()) and failed with 'Invalid Date String'." | Stored path: StoredField holds the epoch ms, `toExternal` returns `stringValue()` (FieldType.java L405-L413), so the text is 1343779201999 (verified with `date -u`). Date path: base `else` branch calls `value.toString()`, and the Date text has no 'Z', so DateMathParser throws "Invalid Date String" (DateMathParser.java L231-L234). The title's parenthetical is supported by the base code. The new Date branch also reaches legacy TrieDateField docValues (NOTE N1). | SATISFIED (N1) |
| 3 | Body equals draft | Live body vs draft | CR removed from both; `diff` exit 0; both 4,092 bytes | SATISFIED |
| 4 | Proof numbers | HighlighterTest 36 of 36 at head; base 36 tests with 1 failure, the new test, `Invalid Date String:'1343779201999'`; LukeRequestHandlerTest 8 of 8; TestPointFields 104 of 104; tidy, Error Prone compile, module check pass; changelog YAML parses | Every number matches receipts/SOLR-3704.md | SATISFIED (numbers) |
| 4a | Proof verification date | "passes 36 of 36 at this head" (no date) | pr-formula.md section 3: "with the verification date and head". The receipt has no run date (only "pushed 2026-10-06"). | STILL OPEN (F2) |
| 4b | One line per test class | "Extra runs at this head: LukeRequestHandlerTest 8 of 8, TestPointFields 104 of 104." | Two classes on one line | STILL OPEN (F3) |
| 4c | No seeds, run IDs, internal vocabulary | Scanned for seed, C0FFEE, log names, gate, receipt, ledger, rc=0, premise, pre-fix, handoff, takeover, audit, JUnit | None found. "Changelog" (a file name) is the only hit on "log". | SATISFIED |
| 5 | File citations | 9 links, each on blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55 | Each anchor checked against `git show <head>:<path>`; see section 3 | SATISFIED |
| 6 | Bold one-line summary per section | Four sections, each opens with one bold line | Verified in the live body | SATISFIED |
| 7a | Limits line and bullet 1 | "Tested: stored date fields. Not tested: docValues-only date fields and date unique keys." / "No test in this change covers a docValues-only date field. That path takes the java.util.Date branch." | The new test's field x_date_p is `*_date_p`, stored and docValues (test schema.xml L863). With the default `fl` (wantsAllFields), storedFields is null and the stored read is used. The receipt's error text (the epoch ms string) confirms the stored route. But when `fl` names only a stored, single-valued DATE field, `RetrieveFieldsOptimizer` moves it to docValues (SolrDocumentFetcher.java L179-L180, L213-L225, L793-L796; DefaultSolrHighlighter.java L496 calls `solrDoc(docId, returnFields)`), and decodeDVField returns `new Date` (L735-L736). The Date branch therefore runs for a stored field too. The Limits do not name this route. | STILL OPEN (F1) |
| 7b | What bullet 2 | "Date docValues fields that are not stored reach the highlighter this way." | Same route as 7a. "Not stored" is narrower than the code. | STILL OPEN (F1) |
| 7c | Limits bullets 2 and 3 | No test covers a date unique key; UnifiedHighlighter code not changed; unified run checks match count only | No unique-key test in the diff. UnifiedSolrHighlighter is not in the diff. The test loops "unified" with a count assertion only (HighlighterTest.java L96-L112). | SATISFIED |
| 7d | Choice | No Choice section in the body | answers file records no Choice for SOLR-3704, so none is required (N3) | SATISFIED |
| 7e | Answers file decisions | "docValues-only Date branch ... not separately discriminated; date unique keys are untested" | Body follows the answers' substance. The answers' "docValues-only" scope is narrower than the code (7a). The answers file must record the widened wording as well. | STILL OPEN (F1) |
| 7f | What bullet 3, wider effects | Luke's value and the printed date unique key "now use the ISO form" | Luke L788; IndexSchema L360 and L369; printableUniqueKey callers (highlight keys, MoreLikeThis, term vectors, child docs, response log). TopGroupsResultTransformer L246 and L308 also call `toExternal` on a unique key (grouping id). DocsStreamer L213 is not reached for DatePointField (it is in KNOWN_TYPES, DocsStreamer L239). | SATISFIED (N2) |
| 8 | Lucene version mentions | None in body (grep count 0) | Rule not triggered | SATISFIED (N/A) |
| 9 | CI, reviews, comments | See section 6 | No failures; three runs held as action_required | SATISFIED (reported) |

Other formula checks:
- N4: The changelog line is a code span, not a link. The approved template uses a code span, and the rule covers file:line citations. Optional: link it at the head.
- N5: Symptom citations point at head, not base. The round-1 rule allows this ("may"). The code is unchanged, so the claims also hold at merge-base. Merge-base line numbers are in section 3.
- N6: The body is about 4,090 characters. The formula's guide is about 3,500 unless the ticket is unusually complex. Not a required fix.
- N7: The Proof line "Tidy, the Error Prone compile, and the module check pass" reports outcomes, not caught slips. Allowed by the formula.

## 3. Citation table (head de63d4e5d5d1ddde0da6a100a631e254c078bd55)

| Body location | File and anchor at head | Claim | Code at head | Result |
|---|---|---|---|---|
| What happens today | FieldType.java L405-L413 | Stored value read as text; epoch ms for a DatePointField | `toExternal` returns `f.stringValue()`, with `binaryValue` fallback | Holds. File unchanged; merge-base lines identical. |
| What happens today | DatePointField.java L197-L198 | Analyzer passes the text to the date parser, which rejects it | `readableToIndexed` calls `toNativeType`, which calls DateMathParser. Chain checked: DefaultAnalyzer (FieldType.java L546-L600) to PointField.toInternalByteRef (PointField.java L208-L212) to readableToIndexed. | Holds. Merge-base lines: DatePointField.java L192-L193 (five lines inserted above). |
| What this change does, bullet 1 | DatePointField.java L161-L163 | `toExternal` returns the ISO form | Returns `((Date) toObject(f)).toInstant().toString()` | Holds. Added by the branch. |
| What this change does, bullet 2 | DefaultSolrHighlighter.java L828-L829 | A java.util.Date gets the same form | `else if (value instanceof Date)` branch | Holds. Added by the branch. |
| What this change does, bullet 2 | SolrDocumentFetcher.java L735-L736 | Date docValues reach the highlighter | `case DATE: return new Date(value);` | Route holds; the wording is incomplete (7a, 7b). File unchanged. |
| What this change does, bullet 3 | LukeRequestHandler.java L788 | Luke's per-field value uses the ISO form | `ftype.toExternal(field)` | Holds. File unchanged. |
| What this change does, bullet 3 | IndexSchema.java L360 | Printed unique key (Document) | `uniqueKeyFieldType.toExternal(f)` | Holds. File unchanged. |
| What this change does, bullet 3 | IndexSchema.java L369 | Printed unique key (SolrDocument) | `uniqueKeyFieldType.toExternal(...)` | Holds. File unchanged. |
| Proof bullet 1 | HighlighterTest.java L91-L125 | The new test | `@Test` at L91; method L92-L125 | Holds. Added by the branch. |

The fix wording in section 5 cites two more anchors at head: SolrDocumentFetcher.java L179-L180 (fields added to dvsCanSubstituteStored), and L793-L796 (the substitution). Both were read at head.

## 4. Proof check

| Body claim | Receipt (receipts/SOLR-3704.md on pr-prepare tip) | Match |
|---|---|---|
| HighlighterTest passes 36 of 36 at this head | "HighlighterTest 36 of 36 at the head" | Yes |
| Base: 36 tests, 1 failure, the new test, `Invalid Date String:'1343779201999'` | "HighlighterTest runs 36 tests with exactly 1 failure, the new testHighlightDatePointFieldDoesNotFail, failing with Invalid Date String:'1343779201999'" | Yes |
| LukeRequestHandlerTest 8 of 8 | "LukeRequestHandlerTest 8 of 8" | Yes |
| TestPointFields 104 of 104 | "TestPointFields 104 of 104" | Yes |
| Tidy, Error Prone compile, module check pass; changelog YAML parses | "Changelog YAML parses; tidy clean; Error Prone compile passes; module check passes." | Yes |
| Verification date | Not in the receipt | No (F2) |
| Lines per test class | Receipt is per class | Extra runs line joins two classes (F3) |

The receipt's gate log (g3704-gate.log) and premise log (g3704-premise.log) are not on this host. Proof numbers were taken from the receipt only. No build, Gradle, or test run was done.

## 5. Fix list for the main side (apply to the live body and to the draft)

F1a, What bullet 2. Replace the last sentence of the bullet:
- Current: "Date docValues fields that are not stored reach the highlighter this way ([SolrDocumentFetcher.java L735-L736](https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java#L735-L736))."
- New: "Date values read from docValues reach the highlighter this way ([SolrDocumentFetcher.java L735-L736](https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java#L735-L736)). That includes a stored, single-valued date field when the request's `fl` names only such fields, because Solr then reads it from docValues ([SolrDocumentFetcher.java L793-L796](https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java#L793-L796))."

F1b, Limits bold line:
- Current: "**Tested: stored date fields. Not tested: docValues-only date fields and date unique keys.**"
- New: "**Tested: a stored date field, read from its stored value. Not tested: date values read from docValues, and date unique keys.**"

F1c, Limits bullet 1:
- Current: "No test in this change covers a docValues-only date field. That path takes the `java.util.Date` branch."
- New: "No test in this change covers a date value read from docValues. That path takes the `java.util.Date` branch. It covers docValues-only date fields and stored date fields that Solr reads from docValues."

F2, Proof, the HighlighterTest line. The main side first records the run date in receipts/SOLR-3704.md (from the gate record). Then the line reads "With this change, HighlighterTest passes 36 of 36 at this head, verified <date>." If no run date can be recovered, the receipt should say so, and the main side decides whether to drop the date requirement for this body.

F3, Proof. Split the extra-runs bullet:
- Current: "Extra runs at this head: LukeRequestHandlerTest 8 of 8, TestPointFields 104 of 104."
- New: two bullets, "LukeRequestHandlerTest passes 8 of 8 at this head." and "TestPointFields passes 104 of 104 at this head."

Also for the main side, not a body edit: material/highlighting-round-1-answers.md (SOLR-3704 Limits) should record the widened wording from F1, since its adopted note says "docValues-only".

## 6. CI and review state (read-only, PR 5103 and head de63d4e5)

- PR: OPEN, draft, headRefOid de63d4e5d5d1ddde0da6a100a631e254c078bd55 (equals fork tip). reviewDecision empty. mergeStateStatus UNSTABLE.
- Reviews: none (empty list). Issue comments: none. Inline review comments: 0.
- statusCheckRollup (and head check-runs endpoint): Pull Request Labeler, SUCCESS (completed 2026-10-11T00:15:09Z).
- Head Actions runs:
  - Gradle Precommit (run 38097776082): action_required, 0 jobs.
  - Solr Tests via Crave (run 38097776083): action_required, 0 jobs.
  - Validate Changelog (run 38097776145): action_required, 0 jobs.
  - Pull Request Labeler (run 38097776098): success.
- Combined commit status: pending, 0 statuses.
- Reading: action_required, with zero jobs, means the runs were not started. GitHub typically holds fork-PR runs for a maintainer's approval. No test output exists to check, and this is not a failure. The main side should not treat it as a pass either.

## 7. Verified and rejected automated findings

None. There are no bot comments, reviews, or inline comments. The labeler is a status check only. Nothing to verify or reject.

## 8. Not checked

- The gate logs (g3704-gate.log, g3704-premise.log, g3704-extra.log) are not on this host, and the run date is not in the receipt.
- No build, Gradle, test run, Selenium, gate, or test-queue run. Behavior claims were checked by reading code at head and by `date -u` for the epoch value.
- The unified highlighter's own handling of date values was not traced. Its file is unchanged, and the body's Limits say so.
- Fields other than the date fields in the test schema were not checked beyond what the test needs.
