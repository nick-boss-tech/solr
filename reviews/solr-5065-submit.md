# solr-5065-submit

- Branch: origin/solr-5065-submit
- Head: c0a0ce1b8d9b (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 0cc328310f8f, 24 commits behind)
- Scope: 3 commits, 5 files (+106/-2). `ParseNumericFieldUpdateProcessorFactory.java` (+17: `EXPONENT_PLUS` at line 49, `LOWERCASE_EXPONENT` at line 51, `normalizeExponent` at line 62), `ParseDoubleFieldUpdateProcessorFactory.java` and `ParseFloatFieldUpdateProcessorFactory.java` (one line each, calling `normalizeExponent` before the parse), `ParsingFieldUpdateProcessorsTest.java` (+79), changelog `SOLR-5065-parse-double-exponent-plus.yml` (+8, type `fixed`)
- Verdict: Close (no code defect found. The locale and design question is an owner call to settle before the PR.)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-5065-review.md` (verdict Close) was written at snapshot `20cd47c2466`, an ancestor of the head. The delta adds the lowercase-exponent rewrite (`LOWERCASE_EXPONENT`) and its tests, and changes the changelog wording (`c0a0ce1b8d9`).
- Round-28 F1 (lowercase handled only with a plus; `4.5e3` not handled): addressed. `LOWERCASE_EXPONENT` rewrites `4.5e3` and `4.5e-3`. The tests assert both, for Double and Float.
- Round-28 F2 (the strip ignores an explicit `locale`): still open; see owner calls.
- Round-28 F3 (`DefaultSchemaSuggester` inference change, untested): still open; see finding 3.
- Round-28 F4 (no documentation of the accepted syntax): still open; see finding 4.
- Round-28 F5 (missing test cases): mostly addressed. The tests now cover `E+`, `e`, `e-`, negatives, and `4.5E+` as a String. Still missing: surrounding whitespace and a non-ROOT locale.

## Verified code facts

- `LOWERCASE_EXPONENT` (`(\d)e([+-]?\d+)$`) and `EXPONENT_PLUS` (`([eE])\+(\d+)$`) are both end-anchored, so text in the middle of a value is not touched.
- On failure the original value is kept. `ParseDoubleFieldUpdateProcessorFactory.parsePossibleDouble` returns `null` at line 114 when the parse index is short, and the Float processor returns `SKIP_FIELD_VALUE_LIST_SINGLETON`. The normalised text is never written back, so `version 2e10` is not rewritten.

## Findings (ranked)

1. **Owner call, verified by reading. The normalisation ignores an explicit `locale`.** `normalizeExponent` runs before `NumberFormat` in both processors, whatever the `locale` init parameter says. Hoss asked in the ticket that an explicit locale keep its own rules, and this branch does not. The ticket has no maintainer consensus, so the PR text should say that this is the regex option (Steve Rowe's proposal) and invite a maintainer to choose between that and a java-mode parser. Not decided here.

2. **LOW, verified. A changed public caller.** `DefaultSchemaSuggester.java:300` calls the public static `parsePossibleDouble`. Schema-designer inference now produces Double for `E+` and lowercase-exponent samples. This behaviour change is outside the processors and no test in this diff covers it. Proof item, or an owner call on whether the change is intended.

3. **LOW, verified (round-28 F3, still open).** The same as finding 2, stated as a proof gap: add a suggester test, or record that the inference change is intended.

4. **LOW, verified (documentation).** The accepted exponent syntax is not documented in the diff. The ref guide was not checked. Jack Krupansky's complaint in the ticket was about undocumented limits.

5. **LOW, verified (wording).** The changelog reads "a plus sign in the exponent and a lowercase exponent marker (for example 4.5E+10 or 4.5e3)". That matches the code, so no change is needed.

## Owner calls (not decided here)

- Regex normalisation (this branch) or a locale-aware parse (the other option in the ticket). Settle this before the PR.
- Whether schema-designer inference should produce Double from these samples (finding 2).

## Interactions with other branches

- None. This branch shares no code with the eDisMax, SQL or grouping branches.

## Not checked

- Not compiled, formatted, or run. The `NumberFormat` acceptance of `E-` and of lowercase markers in ROOT is taken from the code comments and the tests, not run.
- Non-ROOT locale behaviour and whitespace around the value.
- The ref guide text (finding 4).
- Spotless and Error Prone were not run.
- No GitHub or JIRA writes.
