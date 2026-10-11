# Pugh reply draft, s1 (SOLR-5065): verification report

Deliverable: material/SOLR-5065-pugh-reply-draft.md (reply text only, AI header, bold summary, body).

## Head check

- `git ls-remote origin refs/heads/solr-5065-submit` returned `ab894a996c8aa9825c4ac5af38536aec1ee865eb`.
- This matches the named head. The commit object is present locally, so no fetch was needed.
- Result: proceed. The reply was written.
- Line 62 of ParseNumericFieldUpdateProcessorFactory.java at the head is `static String normalizeExponent(String value) {`, which matches the review comment's location.

## Facts from the assignment

1. Four Parse factories parse with a ThreadLocal NumberFormat in the configured locale, Locale.ROOT by default. VERIFIED BY READING.
   - Default: ParseNumericFieldUpdateProcessorFactory.java:53 `protected Locale locale = Locale.ROOT;`
   - `NumberFormat.getInstance(locale)` inside the ThreadLocal: ParseDoubleFieldUpdateProcessorFactory.java:77, ParseFloatFieldUpdateProcessorFactory.java:77, ParseIntFieldUpdateProcessorFactory.java:75, ParseLongFieldUpdateProcessorFactory.java:75.

2. Grouping separators are parsed, and the class javadoc gives a French example, "12 345,899" parsed as 12345.899. PARTLY CONTRADICTED.
   - Grouping: the javadoc states "Grouping separators (',' in the ROOT locale) are parsed" at ParseDouble:33, ParseFloat:33, ParseInt:32, ParseLong:32. Runtime parsing is a main-side record. Head test assertions that read as support: ParsingFieldUpdateProcessorsTest.java:624 (`10,898.83491` asserted as 10898.83491) and :576 (`2,894,518` in a float test asserted as Float).
   - CONTRADICTED: the "12 345,899" example is Russian (ru_RU), not French. It appears at ParseDoubleFieldUpdateProcessorFactory.java:44-45 and ParseFloatFieldUpdateProcessorFactory.java:44-45 ("Russian/Russia", parsed as 12345.899 and 12345.899f). The French/fr_FR text is in the base class javadoc at ParseNumericFieldUpdateProcessorFactory.java:31 and :35, with no parsed value. The French claim is not used in the reply.

3. normalizeExponent makes exactly two rewrites, both anchored to a trailing exponent. A lowercase e right after a digit becomes E. A plus sign in the exponent is removed. Only the Double and Float factories call it. VERIFIED BY READING.
   - Patterns: ParseNumericFieldUpdateProcessorFactory.java:49 `([eE])\+(\d+)$` replaced by `E$2`; :51 `(\d)e([+-]?\d+)$` replaced by `$1E$2`. Method at :62-64.
   - Calls: ParseDoubleFieldUpdateProcessorFactory.java:104 and ParseFloatFieldUpdateProcessorFactory.java:93. ParseInt (:90) and ParseLong (:101) parse `srcVal.toString()` directly and make no call.

4. NumberFormat rejects both forms (a plus in the exponent, a lowercase marker). Java, JSON and the Solr field types accept both. CODE COMMENT ONLY, RUNTIME MAIN-SIDE RECORD.
   - Stated in the base javadoc at ParseNumericFieldUpdateProcessorFactory.java:56-60. The runtime rejection by NumberFormat was not re-run.
   - The reply says NumberFormat "rejects" both forms. That rests on the main-side record.

5. JDK 21 measurements: Double.parseDouble, BigDecimal and commons-lang3 NumberUtils all reject grouping separators, and none supports locales. MAIN-SIDE RECORD ONLY (cannot re-run).
   - Pin verified by reading: gradle/libs.versions.toml:29 `apache-commons-lang3 = "3.20.0"`.
   - Additional verified fact, not in the assignment: solr/core/build.gradle:89 `implementation libs.apache.commons.lang3`. Solr core already depends on commons-lang3. The reply says this.

6. Exponent completeness: after the two rewrites, the accepted exponent spellings are E or e with plus, minus or no sign, with or without grouping. The claim says this is the full set Java, JSON and BigDecimal accept. Hex floats and a trailing d stay rejected and were out of scope. MIXED.
   - Sign and case, trailing digits: VERIFIED BY READING of the two patterns (base :49, :51). Head tests that read as support: ParsingFieldUpdateProcessorsTest.java:652-661 (`4.5E+10`, `4.5e+3`, `-1.25E+2` as Double), :668-678 (`4.5e3`, `4.5e-3`), :702 (`1.5E+3` Float), :713-718 (`1.5e3`, `2.5e-1` Float). Tests were read, not run.
   - "With or without grouping": NOT VERIFIED. The exponent patterns match only `\d+`, so grouping inside the exponent is not handled. Main-side record only. Not used in the reply.
   - "The full set that Java, JSON and BigDecimal accept": CONTRADICTED IN PART BY READING. A dot directly before a lowercase marker, as in `5.e3`, is not rewritten, because LOWERCASE_EXPONENT needs a digit right before the e (base :51). A trailing space also blocks the rewrite, because the patterns are anchored with `$` right after `\d+`. Not run: whether Java or BigDecimal accept `5.e3` or a padded value. From memory of the Java floating-point grammar and Double.valueOf (which trims whitespace), Java likely accepts both, and JSON does not accept `5.e3`. The reply says only that the rewrite does not touch them, not that Java accepts them. The completeness claim is not used.
   - Hex floats and trailing d: no rewrite path exists (only :49 and :51). "Rejected before this change" is VERIFIED against the base commit 0cc328310f8 (cited in pr-drafts/update-processing/SOLR-5065.md): ParseDoubleFieldUpdateProcessorFactory.java at the base has no normalizeExponent and parses `srcVal.toString()` directly. Runtime rejection of hex and d is a main-side record.

## Facts the code contradicts (kept out of the reply)

- Fact 2, French example: the "12 345,899" example is Russian (ru_RU), in the Double and Float javadoc at lines 44-45.
- Fact 6, full-set completeness: the rewrite does not cover a dot before the exponent (`5.e3`) or a trailing space. The reply makes no completeness claim.

## Reply word count

- Body after the summary sentence: 142 words (`sed -n '5,$p' | wc -w`). The AI header line and the 20-word summary sentence are excluded.
- Reply claims rest on: verified reads (Int and Long do not call the rewrite; Solr core depends on commons-lang3; the two rewrites and their anchoring; trailing space and dot-before-exponent not rewritten) and main-side records (JDK parser behavior; NumberFormat rejecting plus and lowercase forms; grouping parse at runtime).

## Could not verify

- JDK behavior of Double.parseDouble, BigDecimal and NumberUtils (grouping rejected, no locale). Main-side record only.
- NumberFormat rejecting a plus in the exponent and a lowercase marker. Stated in the base javadoc; runtime not re-run.
- Runtime grouping parse under ROOT and ru_RU. Read from javadoc and test assertions only.
- Whether Java, JSON and BigDecimal accept `5.e3` and whitespace-padded values. Not run.
- The "with or without grouping" exponent claim. Not verifiable by reading.
- Test results. No tests or builds were run, per the limits.

## Workflow notes

- The claim file claims/pool-pugh-reply-draft.md is present in the worktree (from claim commit b69b5cd). I did not edit it, because this run may edit only the two named files. The main side must mark the claim DONE in the same push as the deliverable (WORKFLOW.md, claim rule 5).
- The AI header follows the convention in pr-formula.md:128 and the existing drafts. The reply uses short sentences and plain words (pr-formula.md, simple-language rule). It contains no proof numbers.
- Em dash check: zero U+2014 characters in the reply file and in this report.
- Nothing was committed, pushed, or posted. No GitHub write was run.
