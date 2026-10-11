# eDisMax (searchcomponents-edismax) draft fidelity, slice 11

Assignment, claim, slice drafts, and the head checked per draft.

- Tree: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`, HEAD `d627304e96b`, as the lead instructed. The brief names claim commit `e84522fa5bc`; that commit exists in the object store, and this review used `d627304e96b`.
- Claim: `claims/edismax-round-3.md` (named in the round roll-up; not re-read here).
- Slice drafts: `pr-drafts/edismax/SOLR-12092.md`, `SOLR-14913.md`, `SOLR-2309.md`, `SOLR-2988.md`, `SOLR-3243.md`.
- Round reports checked: `reports/edismax-round-3.md` and `reports/edismax-round-3-e1.md` to `-e5.md`.
- Receipts checked: `receipts/SOLR-12092.md`, `SOLR-14913.md`, `SOLR-2309.md`, `SOLR-2988.md`, `SOLR-3243.md`.
- Answers material: `material/` has no file that names these tickets or edismax. A grep for the five numbers and for "edismax" returned no files.
- Head per draft, from `git ls-remote origin refs/heads/<branch>` run once per branch:

| Branch | Live tip | Draft head | Match |
|---|---|---|---|
| `solr-12092-submit` | `ca9573dabd385a38711bd60633973ae46b7a552e` | `ca9573dabd3` | yes |
| `solr-14913-submit` | `b80221f46d3ce5889c510b97a97d8748a2ea4c7c` | `b80221f46d3` | yes |
| `solr-2309-submit` | `06f5a1c4a87eef0fed17435e8d30bbc422079119` | `06f5a1c4a87` | yes |
| `solr-2988-submit` | `d2d144dfd9f4cd8208c856a7b955e6eb997da146` | `d2d144dfd9f` | yes |
| `solr-3243-submit` | `1db99c13662d8f827511a5d3a51763d7e1e2ee4c` | `1db99c13662` | yes |

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-12092 | `ca9573dabd3` (live match) | DRIFT (1 item) |
| SOLR-14913 | `b80221f46d3` (live match) | DRIFT (1 item) |
| SOLR-2309 | `06f5a1c4a87` (live match) | CONSISTENT |
| SOLR-2988 | `d2d144dfd9f` (live match) | DRIFT (1 item) |
| SOLR-3243 | `1db99c13662` (live match) | DRIFT (1 item) |

## SOLR-12092

Verdict: DRIFT (1 item).

1. Draft says: "The reference guide and the changelog both state this."
   - Evidence: At `ca9573dabd3`, the changelog title (`changelog/unreleased/SOLR-12092-edismax-managed-stopwords.yml`, lines 1-3) names only the query-side rule. The reference guide (`solr/solr-ref-guide/modules/query-guide/pages/edismax-query-parser.adoc`, line 110) says only that the query analyzer stop filter is ignored. It does not state the index-side rule, which is `ExtendedDismaxQParser.java` lines 1510-1513. Round 3 requires both to state it (`reports/edismax-round-3.md`, owner decision 3 and the SOLR-12092 row; `reports/edismax-round-3-e4.md` findings 2 and 3).
   - Replacement: Do not paste the current sentence yet. After the two branch fixes below land at a new head and the draft's links are re-pointed, use: "The changelog and the reference guide both state this." Until then, delete the sentence.
     - Branch fix, changelog title (replace lines 1-3):

       ```
       title: >
         The edismax parameter stopwords=false now also keeps the words removed by a
         ManagedStopFilterFactory in the query analyzer, not only those removed by StopFilterFactory.
         If the index analyzer has a stop filter of either kind, stopwords=false has no effect on the query analyzer.
       ```

     - Branch fix, reference guide line 110: replace "If this is set to `false`, then the stop filter in the query analyzer is ignored." with "If this is set to `false`, then the stop filter in the query analyzer is ignored. If the index analyzer also has a stop filter, this parameter has no effect."

Optional notes, not blocking:
- Checked and matching: head; the Proof counts (5 of 5, 2 failing, 39 of 39, 82 tests in nine classes) against the receipt; the two failing tests are `testEdismaxStopwordsFalseKeepsManagedStopwords` and `testEdismaxStopwordsFalseWithManagedStopFilterInIndexAnalyzer`, and the third new test, `testEdismaxStopwordsFalseWithManagedStopFilterInBothAnalyzers`, passes on both and fails under the narrower option, so "pins the index-side rule" holds; the diffstat shows `TestExtendedDismaxParser.java` is not touched; the three new field types are at `schema-rest.xml` lines 503-536, inside the cited L502-L540; the query-side check is at lines 1521-1524 and the index-side check at 1510-1513.
- The branch history has Claude `Co-Authored-By` trailers on `3a54f4f3fdf`, `b989934d4ea` and `cde23f0a9d7`, and "handoff" in the subjects of `3a54f4f3fdf` and `c171ae986d7`. The draft cites no commits, but the PR commit list will show them. Round 3 owner decision 2 is the squash, which needs owner go-ahead.
- Length: about 3,860 bytes. The formula guide says roughly 3,500 characters.

## SOLR-14913

Verdict: DRIFT (1 item).

1. Draft says: "Changelog: [`changelog/unreleased/SOLR-14913.yml`](https://github.com/nick-boss-tech/solr/blob/b80221f46d3ce5889c510b97a97d8748a2ea4c7c/changelog/unreleased/SOLR-14913.yml)"
   - Evidence: The linked fragment's title at `b80221f46d3` says "...instead of failing the whole query; ...". The base (`b5c71bc5573`, the merge-base with upstream/main) does not fail the query. Its `ExtendedDismaxQParser.java` line 427 says "ignore failure and reparse later after escaping reserved chars", and line 428 sets `up.exceptions = false;`. The draft's own "What happens today" (a retry with escaped characters) agrees with the base code. Round 3 e4 finding 7 and the round 3 roll-up SOLR-14913 row name the same problem.
   - Replacement (changelog title line): `title: edismax field aliases now skip targets that are missing from the schema instead of re-parsing the query with escaped characters; an alias with no usable target matches nothing`

Optional notes, not blocking:
- Checked and matching: head; Proof count 40 of 40 (static count at the tip: 39 `public void test` methods plus `killInfiniteRecursionParse`); the tip differs from the gated tree `51fe1087fe9` by one comment-only commit, so "The only commit after `51fe1087fe9` changes comments" holds; the cited ranges at the tip, `ExtendedDismaxQParser.java` lines 1182-1186, 1230-1234, 1411-1413, 1433-1435 and 1449-1474, match the code by exact grep line numbers.
- The Choice (`MatchNoDocsQuery` against the silent drop) is on record as "keep it" (round 3 owner decision 1). Posing it to maintainers is what e4 accepted. The Choice's bold line restates the Change claim, which the formula's presentation rule discourages. Optional trim.
- The compound-query change is not pinned by a test (e4 finding 9). The draft's Proof already says so.
- The date 2026-10-07 for the 40 of 40 at the tip is the round 33 disposition date, not a printed run date. The receipt supports it loosely.
- The "_text_" claim is supported by `research/pipeline/candidates.csv` row 174 (truncated) and by the round 28 review.
- Length: about 3,920 bytes, above the guide.

## SOLR-2309

Verdict: CONSISTENT.

Optional notes, not blocking:
- Checked and matching: head; Proof count 40 of 40 and the one base failure against the receipt; verified date 2026-10-07 (round 32 gate). Citations: the FUZZY case at `ExtendedDismaxQParser.java` lines 1480-1485, the helper at 1501-1517, the new checks in the test file at lines 386-458, and `testFuzzyOnUndefinedField` at 638-647 (its comment and method; the method is absent from base, so it is the one new test). The changelog at head exists and its title matches the code. The parsed-query strings in "What happens today" match the ticket packet (`research/jira-context/SOLR-2309.json`, read only).
- The behavior claims were traced in code: `shouldRemoveStopFilter` and the reparse at lines 425-430 explain "a query where every term is a stopword keeps its fuzzy terms", and `alwaysStopwords=true` drops a lone fuzzy stopword.
- The receipt gives "exactly 1 failure" and does not name it. The draft names `testFocusQueryParser`. The new checks sit in that method, so the name is the only place the failure can be, but the gate log that names it (`g2309r32-gate.log`) is not on disk. Confirm before filing if the log can be found.
- No Choice section. Round 3 lists none for 2309. A maintainer could reject the behavior change (a fuzzy stopword is no longer searched), and the live alternative is keeping base behavior. Add a Choice only if the lead wants one.
- Landing note, not a draft change: 2309 conflicts with 12092 and 2988 at one insertion point (round 3 e1 finding 4, e4 finding 4). Keep both blocks.

## SOLR-2988

Verdict: DRIFT (1 item).

1. Draft says: "The schema used by these tests has no text field with a keyword tokenizer, so that case is not tested."
   - Evidence: `solr/core/src/test-files/solr/collection1/conf/schema12.xml` at `d2d144dfd9f4`, which `TestExtendedDismaxParser` loads (`initCore("solrconfig.xml", "schema12.xml")`), defines the text field type `keywordtok` (lines 293-296: `TextField` with `MockTokenizerFactory pattern="keyword"`) and a field `keywordtok` (line 629). So the schema does have such a field. No test in `TestExtendedDismaxParser.java` uses it (`grep -c keywordtok` returns 0). The untested case is real; the statement that the schema has none is wrong.
   - Replacement: "The test schema has a text field that keeps the whole value as one token (`keywordtok` in `schema12.xml`), but no test puts it in pf, pf2 or pf3, so that case is not tested."

Optional notes, not blocking:
- Confirm before filing: the receipt says "the premise run fails with exactly 3 failures" and does not name the code state. The draft says "On the base code with this test file". The sibling receipt for 12092 uses "premise run" for base production, and the merge-base of `d2d144dfd9f4` with upstream/main is `97d973814336`, so the claim is probably right. Round 3's SOLR-2988 row lists "Confirm the base named for the premise run before filing" as the blocker.
- Checked and matching: head; Proof count 42 of 42 (static count 41 `public void test` methods plus `killInfiniteRecursionParse`); the three named failing tests exist at lines 3383, 3397 and 3440, inside the cited L3381-L3488; the term check at lines 1471-1474 and the helper at 1498-1500; the phrase-text loop at lines 637-647, which at base (`97d973814336`, lines 9-14) put a space after the last word inside the quotes, as the draft says; the changelog exists at head and its title matches the draft.
- Round 3 e1 finding 6 (optional changelog wording) and finding 7 (code comment at `ExtendedDismaxQParser.java` line 1473, "so it does match >1 words", which could read "so the term can hold more than one word").
- No Choice section; round 3 lists the whitespace check as a candidate not drafted as a Choice, and Limits carries it.
- Length: about 3,220 bytes, within the guide.

## SOLR-3243

Verdict: DRIFT (1 item).

1. Draft says: "`-[* TO *]` now excludes every document, as `-*` already does."
   - Evidence: The changelog fragment at `1db99c13662` (`changelog/unreleased/SOLR-3243-edismax-unfielded-range.yml`, title line) names only the inclusive form: "...Documents with no value in any qf field now match an unfielded [* TO *], where the old per-field expansion did not match them." It does not state the negated form that the draft's Limits says changes. The code is the same guard (`ExtendedDismaxQParser.java` lines 1135-1139: no explicit field, both ends inclusive, both ends open, returns `MatchAllDocsQuery`), so the negated form does change. Round 3 requires a negated test and the title change (`reports/edismax-round-3.md`, SOLR-3243 row; `reports/edismax-round-3-e2.md` finding 1). The branch has no negated test.
   - Replacement (changelog title line): `title: edismax treats an unfielded inclusive range [* TO *] as a match all docs query instead of expanding it over every qf field. Documents with no value in any qf field now match an unfielded [* TO *], and a negated -[* TO *] now excludes every document.`

Optional notes, not blocking:
- Checked and matching: head; Proof count 42 of 42 (static count 41 plus `killInfiniteRecursionParse`); base `97d973814336` is the merge-base with upstream/main; the three new test names exist at lines 164, 189 and 225 of the test file, inside the cited L162-L246; the guard is at lines 1135-1139; the exclusive forms are outside the guard, so "keep the old per-field expansion" holds.
- The Choice is supported by the ticket packet (`research/jira-context/SOLR-3243.json`, read only): the first patch's body reads "First attempt for patch. This detects a range query without a field, and generates plain literal field queries for the tokens." The report quote and the parsed-query line in "What happens today" also match the packet.
- The receipt says "3 new or strengthened" tests. The diff adds three tests and one helper and no strengthened test, so the draft's "Three new tests" is right.
- The negated-form behavior comes from round 3 e2's code reading, not a re-derivation.
- Optional: the Choice's bold line repeats the Change claim; the Choice still poses a live alternative, so it is not DRIFT.
- Length: about 3,600 bytes, a little over the guide.

## Not done

- No build, test, Gradle, test-queue, gate, or `gh` write. No commit, push, comment, review, close, edit, or Jira write. Only `ls-remote` and read-only git and file reads were run.
- Live Jira was not read. The local packets `research/jira-context/SOLR-2309.json`, `SOLR-2988.json`, `SOLR-3243.json` and `SOLR-12092.json` were read in the main workspace, read only. There is no packet for SOLR-14913; its "_text_" claim rests on the truncated row in `research/pipeline/candidates.csv` and the round 28 review.
- Gate logs, GitHub runs and round 32/33 goal-file reviews named in the receipts are not on disk. Receipt counts were taken as recorded, and the static test-method counts at each head matched them. The 2309 failing-method name and the 2988 premise-run base are unconfirmed (see notes).
- Fail-before results were not re-run. 2309 stopword behavior and 3243 negated behavior were traced by reading only.
- Lucene behavior: none named by the drafts; not checked.
- The drafts have no PR title line, so the title check was applied to the changelog titles.
- Claim and assignment files were not re-read.
