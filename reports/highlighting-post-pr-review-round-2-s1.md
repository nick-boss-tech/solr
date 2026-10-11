# Highlighting post-PR review round 2, slice 1 (SOLR-3704, PR #5103)

Assignment: `assignments/pool-highlighting-post-pr-review-round-2.md`, slice 1. Live draft PR: apache/solr #5103, from nick-boss-tech:solr-3704-submit.
Head checked: `git ls-remote origin refs/heads/solr-3704-submit` returns `de63d4e5d5d1ddde0da6a100a631e254c078bd55`, equal to the PR head. The head commit is present locally, so the code was read with `git show`.
Sources read from origin/pr-prepare: pr-formula.md, material/highlighting-round-1-answers.md, reports/highlighting-post-pr-review-round-1.md and -s1.md, receipts/SOLR-3704.md, pr-drafts/highlighting/SOLR-3704.md, and the round 2 assignment.
Live PR read through research/gh.ps1 only (pr view, api GET for reviews, comments, check-runs, runs, jobs, status). Nothing written to GitHub.
Hard limits held: no build, Gradle, test, Selenium, gate or test-queue run; no PR, comment, review or Jira write; no edits outside this report.

Correction to the round 1 slice 1 report: its fix F1 proposed the "request's fl" condition for the docValues route. Item 1b below shows that condition is wrong for the highlighter, so that wording must not be applied as written.

## 1. Verdict

**STILL OPEN.** Three items remain. Everything else in the body checks out.

- A. What this change does, bullet 2, the sentence that starts "That includes a stored, single-valued date field". It names the request's `fl` as the trigger. The highlighter does not fetch with the request's `fl`. Fix in section 9. The body and the draft must change together. The answers file's 2026-10-10 correction paragraph carries the same wrong condition and needs the same fix.
- B. What happens today, the two symptom citations (FieldType.java and DatePointField.java) link to the head SHA. This round's rule, and the round 1 assignment rule, require symptom citations to link at the base or merge-base and to say so. Fix in section 9.
- C. Proof, the two "Extra runs" lines (LukeRequestHandlerTest and TestPointFields) carry no verification date. pr-formula.md section 3 asks for "one line per test class ... with the verification date and head". The receipt dates both runs to the same day. Fix in section 9.

Checked and satisfied: head and PR state; the round 1 wording removal (item 1a); the Proof date on the HighlighterTest line (item 2); the Extra runs split (item 3); the title; every Proof number against the receipt; the Limits statements against the code; the bold one-line summary in each section; no Lucene version mention; no seeds, run identifiers or log names; no em or en dashes; body equal to the draft.

## 2. Item table

| # | Item | Live wording (quoted) | Source check | Status |
|---|---|---|---|---|
| 1a | Round 1 item 1: the narrow docValues phrases are gone | Not present: "docValues fields that are not stored". "docValues-only" now appears only as one covered case: "It covers docValues-only date fields and stored date fields that Solr reads from docValues." Bullet 2: "Date values read from docValues reach the highlighter this way". Limits bold line: "Tested: a stored date field, read from its stored value. Not tested: date values read from docValues, and date unique keys." | Live body searched. The phrases the round 1 item named are removed, and the Limits wording matches the answers file correction. | SATISFIED |
| 1b | Round 1 item 1: the replacement condition is accurate | "That includes a stored, single-valued date field when the request's `fl` names only such fields, because Solr then reads it from docValues" | The highlighter's fetch does not use the request's `fl`. DefaultSolrHighlighter.java L474-L483 builds `returnFields` from the highlighted fields, any alternate field, and the unique key (L476 `getDocPrefetchFieldNames`, L479 adds the key), with explicit names, so SolrReturnFields.java `parseFieldList` (L169-L181) uses only those names. L496 calls `solrDoc(docId, returnFields)`. The optimizer moves stored fields to docValues only when every stored field in that set is in `dvsCanSubstituteStored` (SolrDocumentFetcher.java L793-L796; the set is built at L179-L180 by `canSubstituteDvForStored`, L49-L61). The unique key is in the set when it is stored, so it must also be readable from docValues. The test schema's `id` has `docValues` false (schema.xml L540, default `solr.tests.id.docValues:false`), so the test reads `x_date_p` from its stored value. That matches the receipt's error text `1343779201999`. The Date branch itself is right: SolrDocumentFetcher.java L735-L736 (`case DATE: return new Date(value)`) and DatePointField NumberType.DATE (DatePointField.java L104). | STILL OPEN (A) |
| 2 | Round 1 item 2: verification date on the Proof line | "- With this change, HighlighterTest passes 36 of 36 at this head, verified 2026-10-06." | Receipt date line: "- Gate run date: 2026-10-06 (log g3704-gate.log, harden-branch.sh, finished 2026-10-07 02:50 UTC; ...)". The date matches. See note N6 on the UTC finish stamp. | SATISFIED |
| 2b | pr-formula section 3: date and head on each test-class line | "- LukeRequestHandlerTest passes 8 of 8 at this head." and "- TestPointFields passes 104 of 104 at this head." (no date) | Receipt: "the extra runs in g3704-extra.log are the same day" (2026-10-06). The date can be added from the receipt. | STILL OPEN (C) |
| 3 | Round 1 item 3: each Extra runs class on its own line | "- LukeRequestHandlerTest passes 8 of 8 at this head." / "- TestPointFields passes 104 of 104 at this head." (two separate bullets) | Receipt: "LukeRequestHandlerTest 8 of 8, TestPointFields 104 of 104". | SATISFIED |
| 4a | Fix citations link at the head SHA with the claimed code on the anchor lines | All 9 fix and proof links use `blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/` (see section 3) | `git show <head>:<path>` and sed-style line prints for each anchor | SATISFIED |
| 4b | Symptom citations link at base or merge-base and say so | "[FieldType.java L405-L413](...blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/...)" and "[DatePointField.java L197-L198](...blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/...)" | Both are symptom citations. Neither says it points at base code. The content holds at both SHAs; the links do not follow the rule. | STILL OPEN (B) |
| 4c | Title is accurate for the change | "SOLR-3704: Fix highlighting on a DatePointField: the highlighter was handed epoch milliseconds (or a Date.toString()) and failed with 'Invalid Date String'." | Base DefaultSolrHighlighter.java L825 (IndexableField branch) and L829 (`strValue = value.toString();` for other values, including a Date). A Date.toString() value has no 'Z', so DateMathParser.java L231-L234 throws "Invalid Date String". Epoch ms text is the other failure (see N1). | SATISFIED |
| 4d | Every Proof number is in the receipt | See section 4 | Receipt lines quoted there | SATISFIED |
| 4e | Choice statement | None. The body has no "A choice to check" section. | The answers file records no choice for SOLR-3704. Nothing to check. | SATISFIED (N/A, N8) |
| 4f | Limits statements true of the code at the head | Bold: "Tested: a stored date field, read from its stored value. Not tested: date values read from docValues, and date unique keys." Bullet 2: "No test in this change covers a date unique key." Bullet 3: "The UnifiedHighlighter code is not changed. The new test runs `hl.method=unified` with a match-count check only." | The diff at merge-base is 4 files (DatePointField.java, DefaultSolrHighlighter.java, HighlighterTest.java, the changelog YAML); no unique-key test; UnifiedSolrHighlighter.java is not in the diff; the new test loops over "original" and "unified" with count and numFound asserts only (HighlighterTest.java L96-L112). The stored-read claim is true for this test (see 1b). The "date values read from docValues" limit is true: no test in the diff reads a date from docValues. | SATISFIED |
| 4g | Each section opens with a bold one-line summary | Bold lines open What happens today, What this change does, Proof, Limits | Verified in the live body | SATISFIED |
| 4h | Answers file decisions reflected in the body | "Tested: a stored date field, read from its stored value. Not tested: date values read from docValues, and date unique keys." | Matches the 2026-10-10 correction wording. The correction paragraph itself carries the wrong condition (fix A also applies there). | SATISFIED (body); the answers file needs fix A |
| 4i | Lucene version mention names the other versions | None in the body (no match for "Lucene") | Rule not triggered | SATISFIED (N/A) |
| 4j | No internal vocabulary or run identifiers | None: no seed, C0FFEE, log file names, gate, receipt, ledger, premise, pre-fix, handoff, takeover, audit, JUnit or rc=0. "Changelog" appears only as the file name. "The extra runs above" is a run-log label (N3). | Body searched (case-insensitive) | SATISFIED (N3 optional) |
| 4k | No em or en dashes | 0 found in the body | Body searched for U+2014 and U+2013 | SATISFIED |
| 5 | Body equals the pr-prepare draft | Identical after CR removal (neither file has CR). 4,594 bytes each. | `diff` exit 0 | SATISFIED |

## 3. Citation table

All fix links use `https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/<path>#L<a>-L<b>`. The symptom links are listed separately with the base lines.

| Body location | File | Anchor at head | What the anchor holds | Result |
|---|---|---|---|---|
| What happens today, para 1 (symptom) | solr/core/src/java/org/apache/solr/schema/FieldType.java | L405-L413 | `toExternal` returns `f.stringValue()` (binary fallback). DatePointField does not override it at base, so a stored value shows as epoch ms. File not in the branch diff. | Content holds. Link at base (same lines): STILL OPEN (B). |
| What happens today, para 1 (symptom) | solr/core/src/java/org/apache/solr/schema/DatePointField.java | L197-L198 at head | `readableToIndexed` calls `toNativeType(val.toString())`. Base lines are L192-L193. | Content holds. Link at base L192-L193: STILL OPEN (B). |
| What this change does, b1 (fix) | DatePointField.java | L161-L163 | `toExternal` returns `((Date) toObject(f)).toInstant().toString()` | SATISFIED |
| What this change does, b2 (fix) | solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java | L828-L829 | `else if (value instanceof Date)` gives the ISO form | SATISFIED |
| What this change does, b2 (route) | solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java | L735-L736 | `case DATE: return new Date(value);` | SATISFIED |
| What this change does, b2 (condition) | SolrDocumentFetcher.java | L793-L796 | Stored fields move to docValues when all are substitutable | Anchor holds; the sentence's condition is wrong: STILL OPEN (A) |
| What this change does, b3 | solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java | L788 | `ftype.toExternal(field)` | SATISFIED |
| What this change does, b3 | solr/core/src/java/org/apache/solr/schema/IndexSchema.java | L360 and L369 | `printableUniqueKey` calls `uniqueKeyFieldType.toExternal` for Document and SolrDocument | SATISFIED |
| Proof, bullet 1 | solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java | L91-L125 | `@Test` at L91; `testHighlightDatePointFieldDoesNotFail` to L125 | SATISFIED |
| Changelog line | changelog/unreleased/SOLR-3704-highlight-date-point.yml | code span, no link | File exists at head; its title matches the PR title | N5 |

Base lines for the symptom fix (merge-base `cabedd1d968059215188f4e7563fb303241899ed`, the upstream/main merge-base; `git merge-base` confirms it):
- `https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/schema/FieldType.java#L405-L413`
- `https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/schema/DatePointField.java#L192-L193`

Rule conflict for the lead: pr-formula.md says every file citation links at the PR head SHA. The round 1 assignment rule says symptom citations point at base or merge-base code, and this round's check says the same and requires the link to say so. This report applies the assignment rule. The lead should confirm which rule governs.

## 4. Proof and date check

- Proof line, live: "- With this change, HighlighterTest passes 36 of 36 at this head, verified 2026-10-06."
- Receipt date line, origin/pr-prepare receipts/SOLR-3704.md: "- Gate run date: 2026-10-06 (log g3704-gate.log, harden-branch.sh, finished 2026-10-07 02:50 UTC; the takeover log records SOLR-3704 DONE, GATED, PUSHED 2026-10-06; the extra runs in g3704-extra.log are the same day)."
- Result: the date matches the receipt's run date. Item C asks for the same date on the two Extra runs lines.

| Body claim | Receipt line | Match |
|---|---|---|
| HighlighterTest passes 36 of 36 at this head | "Counts: HighlighterTest 36 of 36 at the head." | Yes |
| Base: 36 tests, 1 failure, the new test, `Invalid Date String:'1343779201999'` | "on base production with the branch test file, HighlighterTest runs 36 tests with exactly 1 failure, the new testHighlightDatePointFieldDoesNotFail, failing with Invalid Date String:'1343779201999'" | Yes |
| LukeRequestHandlerTest passes 8 of 8 at this head | "LukeRequestHandlerTest 8 of 8" | Yes |
| TestPointFields passes 104 of 104 at this head | "TestPointFields 104 of 104" | Yes |
| Tidy, the Error Prone compile, and the module check pass; the changelog YAML parses | "Changelog YAML parses; tidy clean; Error Prone compile passes; module check passes." | Yes |

The epoch value in the body was checked with `date -u`: 2012-08-01T00:00:01.999Z is 1343779201999 ms.

## 5. Body versus draft

`pr-drafts/highlighting/SOLR-3704.md` on origin/pr-prepare equals the live body after CR removal. `diff` exit 0. Both are 4,594 bytes. Any fix must go to both.

## 6. CI and review state (read-only)

- PR #5103: OPEN, draft, headRefOid `de63d4e5d5d1ddde0da6a100a631e254c078bd55` (equals the fork tip). mergeStateStatus UNSTABLE. reviewDecision empty.
- statusCheckRollup: `labeler` (CheckRun, Pull Request Labeler) SUCCESS, completed 2026-10-11T00:15:09Z.
- Workflow runs at the head:
  - Gradle Precommit, run 38097776082: action_required, 0 jobs.
  - Solr Tests via Crave, run 38097776083: action_required, 0 jobs.
  - Validate Changelog, run 38097776145: action_required, 0 jobs.
  - Pull Request Labeler, run 38097776098: success.
- Combined commit status: pending, 0 statuses.
- Reviews: none. Issue comments: none. Inline review comments: none.
- Reading: action_required with 0 jobs means the upstream runs have not executed. It is a state, not a failure and not a pass. No test output exists for these runs. The main side should not treat them as green.

## 7. Verified and rejected automated findings

None. There are no bot comments, reviews or inline comments on #5103. The labeler is a status check with no finding.

## 8. Notes (no effect on the verdict)

- N1. The title's "(or a Date.toString())" clause is correct: base DefaultSolrHighlighter.java L829 calls `value.toString()` for a Date, and the result has no 'Z', so DateMathParser.java L231-L234 rejects it. The body's "What happens today" describes only the epoch ms path. Optional: one sentence for the Date.toString path, or leave it.
- N2. Scope of "Date values now reach the highlighter as ISO instants". The change sits in DefaultSolrHighlighter's value path (getFieldValues, L811-L833). UnifiedSolrHighlighter does not extend it and has no call to getFieldValues or toExternal (searched at head). Limits already say the unified code is unchanged and the test checks counts only. Optional: say "in the default highlighter" in the bold line. Not traced in this slice; the unified snippet path was not read.
- N3. Limits: "The extra runs above are LukeRequestHandlerTest and TestPointFields." "Extra runs" is a run-log label. Plain option: "The other test classes that pass at this head are LukeRequestHandlerTest and TestPointFields."
- N4. Presentation rule (pr-formula.md): a claim stated in the bold summary should not be restated in the body. Limits bullets 1 and 2 restate the bold line ("No test in this change covers a date value read from docValues"; "No test in this change covers a date unique key"). Optional: cut the restating first sentences and keep the mechanism sentence.
- N5. The changelog file name is a code span, not a link. The approved template uses a code span, so this is optional. If linked, use the head SHA.
- N6. Date basis. The receipt's run date is 2026-10-06, but its log finish stamp is 2026-10-07 02:50 UTC. The run date is the date the receipt records; the body follows it. If the main side wants UTC, the date may need a change. Not a fix as the receipt stands.
- N7. Length: 4,594 bytes. The pr-formula guide is about 3,500 unless the ticket is complex. Not a required fix.
- N8. No Choice section. The answers file records no choice for SOLR-3704.
- N9. Optional Limits sentence: the test reads the stored value because its unique key has no docValues in the test schema (schema.xml L540). This makes the limit's scope explicit.

## 9. Fix list for the main side (apply to the live body and to pr-drafts/highlighting/SOLR-3704.md)

Fix A. What this change does, bullet 2. Replace the sentence "That includes a stored, single-valued date field when the request's `fl` names only such fields, because Solr then reads it from docValues ([SolrDocumentFetcher.java L793-L796](https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java#L793-L796))." with:

"That includes a stored, single-valued date field when every stored field the highlighter fetches can be read from docValues ([SolrDocumentFetcher.java L793-L796](https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java#L793-L796)). The highlighter fetches the highlighted fields, any alternate field, and the unique key, not the request's `fl` ([DefaultSolrHighlighter.java L474-L483](https://github.com/nick-boss-tech/solr/blob/de63d4e5d5d1ddde0da6a100a631e254c078bd55/solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L474-L483))."

Also fix material/highlighting-round-1-answers.md, the 2026-10-10 correction paragraph, the same way (it names the request's `fl` as the condition). The round 1 s1 report's F1a wording is superseded by this text.

Fix B. What happens today. Make both symptom links point at base and say so:
- Replace "[FieldType.java L405-L413](...)" with "[FieldType.java L405-L413, base code](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/schema/FieldType.java#L405-L413)".
- Replace "[DatePointField.java L197-L198](...)" with "[DatePointField.java L192-L193, base code](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/schema/DatePointField.java#L192-L193)".

Fix C. Proof. Add the date to the two Extra runs lines:
- "- LukeRequestHandlerTest passes 8 of 8 at this head, verified 2026-10-06."
- "- TestPointFields passes 104 of 104 at this head, verified 2026-10-06."

Optional (N1 to N4, N9) as listed above.

After any edit: the body and the draft must match after CR removal, and the head SHA in every fix link must stay `de63d4e5d5d1ddde0da6a100a631e254c078bd55`.

## 10. Not checked

- The gate logs (g3704-gate.log, g3704-extra.log, g3704-premise.log) are not on this host. The dates and counts come from the receipt only.
- No build, test or Selenium run. Behavior was read from the code at the head and the base.
- The UnifiedSolrHighlighter's own value path was not traced (N2).
- Workflow job lists were read only for their counts (0 each).
- The takeover log named in the receipt was not read.
