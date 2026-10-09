# Highlighting answers check: SOLR-4540 draft

Checked: `pr-drafts/highlighting/SOLR-4540.md` at origin/pr-prepare against `material/highlighting-round-1-answers.md`, `receipts/SOLR-4540.md`, `reports/highlighting-round-1.md`, `reports/highlighting-round-1-g2.md`, and `pr-formula.md`. Code checked at head 62c06439fb0653b299ec54f110e4f94e420cea9b. Read only. No builds, tests, or writes outside this report.

## 1. OPEN line (filled; not verifiable here)

Draft line 24: "- On the base code, this test fails: `FastVectorHighlighterTest.testFieldAbsentFromDocIsSkipped` fails at line 106 of the test (premise run, seed 4540C0FFEE4540, log g4540-premise.log)."

Receipt: "- Premise: grounded by run (base production plus the branch test file only), recorded in the takeover log 2026-10-07."

The receipt does not record the base result. It gives no failure text, line, seed, or log name. Searches of `research/` found no seed or log name, and the takeover log is not in the workspace. The base-code sentence therefore rests on the main-side run, which this workspace cannot verify. The sentence must be cleared before posting.

Proposed text, for ratification (not applied):
- If the owner clears the base result: "- On the base code (upstream main with only this test file added), `FastVectorHighlighterTest.testFieldAbsentFromDocIsSkipped` fails at line 106 of the test."
- If not cleared: delete line 24. The Proof then has no base-code failure, which pr-formula section 3 requires, so the owner must decide.

## 2. Internal text in public body (yes, two places)

- Line 1, HTML comment: "<!-- Written against branch head 62c06439fb0653b299ec54f110e4f94e420cea9b. Remove before posting. -->". Not rendered, but present in the raw body. Replacement: delete line 1.
- Line 24: "(premise run, seed 4540C0FFEE4540, log g4540-premise.log)". Replacement: delete the parenthetical; see item 1.

## 3. Changelog title (still the slowdown claim, in the branch file)

The draft's Changelog line (line 34) names only the path. The title is in `changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml` at head 62c06439: "FastVectorHighlighter no longer does per-field work for fields a document does not have, which made wildcard hl.fl (many sparse fields) slow." No timing exists for this change. The draft's Limits line ("No timing is measured in this change") is consistent.

Proposed title, for packaging with a re-gate (not applied): "FastVectorHighlighter skips fields a document does not have, so a bad fragmentsBuilder set for such a field no longer fails the request."

## 4. Cited code lines at head 62c06439

- Test method `testFieldAbsentFromDocIsSkipped`: JavaDoc L94-L98, `@Test` L99, method L100-L123. `assertQ(` opens at L106. Draft link L94-L123 matches.
- DefaultSolrHighlighter.java skip: L636-L640 (absent-field `return null`). Builder call L641. Throw "Unknown fragmentsBuilder" L415-L421. All draft links match.

## 5. Internal vocabulary

Hits only: "premise run", "seed 4540C0FFEE4540", "log g4540-premise.log" (line 24), and "Remove before posting." (line 1). No hits for gate, receipt, ledger, JUnit, rc=, takeover, live tip, owner, main side, audit, queued, or GATE. No first-person plural. Em dashes: 0.

## 6. Replacement summary

Items 1, 2, and 3 above. Item 1 has two branches (cleared or not).

SOLR-4540 posting state: HOLD until the base result is cleared and internal text removed
