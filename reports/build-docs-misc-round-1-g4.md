# Build, docs and misc round 1, part G4: SOLR-6430, SOLR-7119, SOLR-11700, SOLR-17356 (audit only, NO GATE)

Read-only premise check against main (`3f5d4c5bf8ac`). Nothing was built, run, tested, drafted, posted, pushed or written. No JIRA calls. No draft is written for this group, as the assignment requires. Condensed by the lead from the subagent's final report.

Common refs: heads fetched and matched (6430 `22f83870c4a0`, 7119 `9593f4bd0d63`, 11700 `c513388be053`, 17356 `ea7fc15cade4`; 16914 `cd878023d3d0` for the interaction). Each of the four adds only its `SOLR-NNNN-TESTING.md`, committed 2026-10-06. Merge bases, computed (no receipt records a base): 6430, 7119 and 11700 at `cabedd1d968`; 17356 at `b6b2b8f10e98`; 16914 at `14c7aac0d151`. The Gradle cache was read only (listing and class strings; nothing extracted or run).

## SOLR-6430 (`field-type-definitions-and-properties.adoc`): premise accurate

- The branch adds one table row sentence (main line 202), a changelog fragment (type `other`) and the TESTING note.
- Premise verdict: the sentence describes what main does for string, numeric and date fields, and claims no behavior change. Not settled by reading for `docValues=false` (uninverted) fields.
- Evidence: `missingValue()` returns null unless `sortMissingFirst` or `sortMissingLast` is set (`FieldType.java:854-862`). Numeric and date missing values default to 0, pinned by `DocValuesMissingTest` (float, int, double, long and date; the date case is commented "treats as 1970"). String fields sort missing first ascending, pinned for `StrField` only (`DocValuesMissingTest.java:437-455`). `TrieField` uses the same helper (read, not run).
- Owner decisions: (1) keep the sentence as the record of current behavior, and leave the behavior question open on the ticket. Recommendation: keep. (2) Changelog: drop, matching 5821 and 16914. Recommendation: drop.
- Not checked: `docValues=false`; `TextField` and `SortableTextField` defaults; branch_9x; Antora build.

## SOLR-7119 (`faceting.adoc`): premise accurate for the page's wording, the receipt overstates it

- The branch adds 12 lines inside "Interval Faceting" (base line 815), a changelog fragment (type `other`) and the TESTING note.
- The ticket's blanket claim is only half true today. Interval facets honor `ex` on `facet.interval`, so exclusion works for the whole facet. They ignore `ex` on `facet.interval.set`, which is the form the ticket used. The page places the working form and says set items take only `key`, which matches the code.
- Evidence: `SimpleFacets.parseParams` reads `CommonParams.EXCLUDE` from the facet's local params (`SimpleFacets.java:214`, `:218-219`). Set-item parsing reads only `OUTPUT_KEY` (`IntervalFacets.java:526`). `TestIntervalFaceting.testFilterExclusion` (`:1443-1509`) asserts the working form on main; no test asserts the set-level form.
- Receipt disagreement: `receipts/SOLR-7119.md:7` says the branch documents "the ticket's claim that tag and exclude local params do not work for interval facets". The verified state is narrower: exclusion works on `facet.interval` and fails only on `facet.interval.set`.
- Owner decisions: (1) test scope: docs only, as written (recommended), or add a negative assertion for the set-level form. (2) Changelog: drop (recommended), same convention as 6430.
- Verification owed (main side, not run): Antora build; optionally a base run of the set-level form with `ex` expecting 0 (the only case the premise needs that no test asserts).
- Not checked: the distributed interval path; branch_9x; any run.

## SOLR-11700 (`filters.adoc`): the corrected positions are accurate on both Lucene lines; two parts are not settled

- The branch changes two `Out:` lines (base 3840 and 3859) and adds a NOTE after 3859, plus a changelog fragment (type `other`) and the TESTING note.
- Lucene versions, all three apply: main pins 10.4.0 (`gradle/libs.versions.toml:39`); branch_10x pins 10.4.0 (`solr/modules/analysis-extras/gradle.lockfile:98`); branch_9x pins 9.12.3 (`versions.props:61`). The same wrong text sits on all three (main 3840 and 3859; branch_10x 3840 and 3859; branch_9x 3854 and 3873).
- Premise verdict: accurate for the corrected positions of the two catenate examples, on 10.4.0 and 9.12.3. The Solr tests on each line pin the same positions for the same config (`FieldAnalysisRequestHandlerTest.java:597-612` on main; `:591-611` on branch_9x, with `schema-copyfield-test.xml:218-222`). A concatenated token takes the position of the first part it joins and is emitted before it; later parts shift. The text currently on main is wrong on both lines.
- Not settled by reading: (i) the `catenateAll` example, since the rule is pinned for `catenateWords` and `catenateNumbers` but not `catenateAll`; (ii) the NOTE's "spans the parts it joins", a position-length claim the tests do not print. The changelog title's "real token positions" overstates until (i) is checked.
- Owner decisions: (1) the two examples: correct both now, or correct the `catenateWords`/`catenateNumbers` example now and hold `catenateAll` for a focused run. Recommendation: the second. (2) NOTE wording: keep "spans" after a position-length check, or drop the clause. Recommendation: drop unless the check is run. (3) Mirror to branch_10x and branch_9x, which carry the same wrong text. Recommendation: mirror. (4) Changelog: drop, as 6430.
- Verification owed (main side, not run): a focused analysis run of "XL-4000/ES" with `catenateAll=1` on main (10.4.0) and branch_9x (9.12.3); a `TokenStream` check of `PositionLengthAttribute` if "spans" stays; Antora build.
- Not checked: Lucene source (not in the repo or the cache); `catenateAll` pins; any run.

## SOLR-17356 (`language-analysis.adoc`, Ukrainian section): premise wrong in part; no draft

- The branch makes four targeted edits in the Ukrainian section (base hunks 3463, 3477, 3492 and 3506): the module name, the dictionary paragraph, and two example attributes. It adds a changelog fragment (type `fixed`) and the TESTING note. The assignment's word "rewritten" overstates it.
- Premise: accurate that the dictionary is not in the Lucene 10.4.0 morfologik jar. **Wrong in two places:**
  - The default Ukrainian dictionary is already on analysis-extras' runtime classpath, via `morfologik-ukrainian-search:4.9.1` (`solr/modules/analysis-extras/build.gradle:27`; lockfile `:187`). Its jar holds `ua/net/nlp/ukrainian.dict`, the path Lucene's default resource names. So "you must add morfologik-ukrainian-lt" is not needed for the default.
  - The new path `org/languagetool/resource/uk/ukrainian.dict` is not in `morfologik-ukrainian-search`. It could be in `morfologik-ukrainian-lt`, which is neither in main's build nor in the cache, and was not downloaded. The branch's two new `dictionary` attributes resolve only if the owner adds that jar.
- The old path `org/apache/lucene/analysis/uk/ukrainian.dict` is not in the 10.4.0 jar; main still states it at `language-analysis.adoc:3480`, `:3495` and `:3509`. The jar name `lucene-analyzers-morfologik` at main 3466 and 3480 is stale; `lucene-analysis-morfologik` is the current artifact, and branch_9x builds the same artifact.
- Stopwords: `org/apache/lucene/analysis/uk/stopwords.txt` is in the 10.4.0 jar, so the stopword example resolves on 10.4.0 (not checked on 9.12.3).
- **Class name error, unchanged by the branch:** `language-analysis.adoc:3463` (inside the first hunk as context) says `solr.MorphologikFilterFactory`. The class is `MorfologikFilterFactory`, and the factory table (3468) and class example (3509) use that name. The same typo is at branch_9x line 3463.
- Placement advice ("add ... to the analysis-extras classpath, for example in the lib directory") matches the module README (`solr/modules/analysis-extras/README.md:39-41`).
- Interaction with SOLR-16914: 16914 edits lines 2292, 2313-2316 and 2347-2350, inside the Japanese tokenizer section (2288-2351); 17356 edits lines 3463-3509, inside the Ukrainian section (3461). No overlap; the Polish section (2857) sits between them. Either order lands cleanly.
- Owner decisions: (1) the dictionary path in the examples: (a) `ua/net/nlp/ukrainian.dict`, on main's classpath today, needs no dependency change (recommended for the docs); (b) keep the languagetool path and add `morfologik-ukrainian-lt`, the ticket's suggestion, as a separate dependency decision with a jar check and a license and size review. (2) Class name at line 3463: fix in this change (recommended). (3) Backport and mirror: after the main version is verified, to branch_9x and branch_10x (both carry the stale lines); recommended. (4) Changelog: drop (recommended, as 5821 and 16914), or keep as `other`. The title's first half would change if the path moves.
- Verification owed (main side, not run): Antora build; a focused analysis check with the dictionary path the page will name (for `ua/net/nlp/ukrainian.dict`, a run or an analysis-extras test; for the languagetool path, fetch and list `morfologik-ukrainian-lt` first); the 9.12.3 morfologik jar before any backport; the `xref:#analysis-extras-module` anchor.
- Not checked: `morfologik-ukrainian-lt` contents; the 9.12.3 jar; Antora; the xref anchor; any run.

## Cross-ticket

- None of the four receipts records a base SHA; the computed bases are above.
- The changelog convention is one open question for all four fragments. 5821 and 16914 ship none.
- 16914 and 17356 do not overlap in `language-analysis.adoc`.
- Nothing was built, run, drafted, posted or pushed.
