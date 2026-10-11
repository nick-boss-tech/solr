# SolrJ draft fidelity, slice 2

Assignment: `assignments/pool-draft-fidelity-streaming-solrj.md`. Claim: `claims/pool-draft-fidelity-streaming-solrj.md`, slice 2 (SolrJ SOLR-3722, 3999, 4335, 4336, 4422). Drafts: `pr-drafts/solrj/` in this worktree. Worktree HEAD `3576773b2f8` (the tip, per the slice instruction), status clean when the check started. Round 1 reports read: `reports/solrj-clients-round-1.md` and parts `-s1`, `-s2`, `-s3` (the only parts that mention these five tickets; `-s4` to `-s6` were searched for the five numbers and do not mention them).

Head check per draft: `git ls-remote origin refs/heads/solr-<n>-submit` run in this session. All five live tips match the head each draft names. Every SHA the drafts cite resolves in the worktree object store (`git cat-file -t`), so no fetch was needed.

Changelog title rule: the drafts have no separate title line. The changelog title in each branch fragment was compared with the draft's headline claim (the bold line under "What this change does").

## Verdicts

| Draft | Head checked (ls-remote) | Verdict |
|---|---|---|
| SOLR-3722 | `f9d3dda1d3de8d9724a9dc47b7368d378306e01c` (match) | CONSISTENT |
| SOLR-3999 | `e3af6f36e3120536bd7edccc779d34ea4b2b467b` (match) | DRIFT (1 item) |
| SOLR-4335 | `8f6e267d6482516e9853df69da630e4f0142c72a` (match) | DRIFT (1 item) |
| SOLR-4336 | `798618aa08fd6de759bad798a825bdf80ee77201` (match) | DRIFT (2 items) |
| SOLR-4422 | `3e5b7afecddbe93eeea3b2cd468c2836091392a8` (match) | CONSISTENT |

Replacement text for changelog items goes into the branch fragment named in the item (`changelog/unreleased/...`). It does not change the draft body. Keep the title a plain YAML line; the replacement texts below contain no colon-space and no " #".

## SOLR-3722

Verdict: CONSISTENT.

Checked and matching: head; Proof counts (NamedListTest 6/6, SimpleOrderedMapTest 18/18, TestNamedListCodec 4/4, module check passed) against `receipts/SOLR-3722.md` lines 5-6; citations at head and base (`NamedList.java` base 144-151 has no null check; head 145-155 is `nameValueMapToList` with the skip; head 80-92 is the constructor javadoc "Null array elements are skipped"); `SimpleOrderedMap` head line 69-71 calls `super(nameValuePairs)`, so "SimpleOrderedMap gets the same behavior" holds; changelog file exists at head with a title that matches the headline; Choice (skip versus fail with a clear error) is a live alternative with a stated cost; Limits match the round 1 report (S1 recorded: Entry-array constructors only, no component change, shard mismatch not investigated, null key and null value not covered); the test name `testEntryArrayWithNullElements` exists at head (`NamedListTest.java` line 40).

Optional notes, not blocking:
- The receipt does not name the new test. The name is confirmed by the head source, so no change is needed.
- The 2026-10-06 date rests on the receipt's round 27 takeover-log reference (see Not done).
- Round S1 edge (not a draft error): a null key inside an Entry array is not skipped and becomes an unnamed entry, which SOLR-4424 rejects at conversion. The draft's Limits already say null keys are not covered.

## SOLR-3999

Verdict: DRIFT (1 item).

1. Draft says (changelog title, `changelog/unreleased/SOLR-3999-serial-version-uid.yml` at `e3af6f36e3120536bd7edccc779d34ea4b2b467b`): "SolrDocumentBase, SolrDocument, SolrInputDocument and SolrInputField declare an explicit serialVersionUID so Java-serialized documents survive recompiles."
   - Evidence: the draft's Limits say streams from builds without the number are still rejected, including the ticket's own case, and that later builds must keep 1L. The title states the benefit without that limit. Round S2 owner decision 4 and roll-up owner decision 9 call for the wording to be aligned.
   - Replacement (changelog title): "SolrDocumentBase, SolrDocument, SolrInputDocument and SolrInputField declare serialVersionUID = 1L, so the version number no longer follows the class shape."

Checked and matching: head; Proof 2/2 and the base failure `NoSuchFieldException: serialVersionUID` (receipt lines 5-7); the base has no `serialVersionUID` in the four classes (`b6b2b8f10e9`; `SolrInputField.java` line 29 is the class line); head lines SolrDocumentBase 26, SolrDocument 44, SolrInputDocument 38, SolrInputField 30 each declare `serialVersionUID = 1L`; `SolrDocumentList` extends `ArrayList<SolrDocument>` with no number at head; the draft's Proof sentence about `testExplicitSerialVersionUid` matches `SolrDocumentSerialVersionTest.java` at head (lines 32-45, uses `getDeclaredField` and `ObjectStreamClass.lookup`, so it is the test that fails on base).

Optional notes, not blocking:
- Receipt line 7 says the new test "constructs each class". At head only the round-trip test constructs an object. The draft does not repeat that claim; the receipt should be corrected (round S2 item 2).
- The receipt does not name the failing method. The draft's name is confirmed by the test source.
- Plain language: "computed number" and "shape" are compressed. Optional wording: "the number Java computes from the class's members".

## SOLR-4335

Verdict: DRIFT (1 item).

1. Draft says (changelog title, `changelog/unreleased/SOLR-4335-xml-nonchars.yml` at `8f6e267d6482516e9853df69da630e4f0142c72a`): "SolrJ XML update requests no longer send the illegal characters U+FFFE and U+FFFF, which made the server reject the whole request; they are escaped like the other illegal control characters."
   - Evidence: the draft's "What this change does" (third bullet) and the paths it names say the shared `XML.escape` routine also changes the XML response writer (`XMLWriter.java` line 405), the schema XML writer (`SchemaXmlWriter.java` line 475) and the delete-by-id and delete-by-query request writer (`XMLRequestWriter.java` lines 141 and 148), all at `8f6e267`. The title covers only SolrJ update requests, so it is narrower than the code and the draft. Round S3 says the draft states the widening; the title does not.
   - Replacement (changelog title): "XML written by SolrJ and Solr no longer contains the illegal characters U+FFFE and U+FFFF; they are escaped like the other illegal control characters."

Checked and matching: head; Proof 10/10 at head and 2 of 9 on base (receipt lines 5-6); the escape code at head (`XML.java` lines 58-59 note, 75-81 callers, 142-146 new branch, text `#65534;` and `#65535;` with no ampersand) and at base (133-145); `TestXMLEscaping.java` at head: lines 69-86 are the two tests that expect the replacement text, lines 77-80 the supplementary-character test; the base output of U+0000 as `#0;` (`chardata_escapes`, `XML.java` lines 31-37); the Limits (lossy, unpaired surrogates, JSON and javabin writers unchanged, no update-request test).

Optional notes, not blocking:
- The receipt names no failing tests. The draft's names `testNonCharacters` and `testNonCharacterInAttribute` match the head test file; the receipt should name them.
- Date: the draft says "verified 2026-10-06" for 10/10. The receipt cites the 2026-10-06 date for the round 27 entry. The 10/10 result comes from the Test Categories round, whose date the receipt does not record. Confirm before posting (the same point applies to SOLR-4336).
- Choice: none. Roll-up owner decision 1 is open (keep the widening as stated, or narrow to the SolrJ update writer, which would need a Choice section). The draft matches the round default.
- Plain language: "lossy by design" and "unpaired surrogates" are compressed. The draft's next sentence already explains both.

## SOLR-4336

Verdict: DRIFT (2 items).

1. Draft says: "Verified at this head."
   - Evidence: `pr-formula.md` Proof template requires "verified <date>". `receipts/SOLR-4336.md` line 8 records no run date. The 11/11 result comes from the Test Categories round, which the receipt does not date; the round 27 entry it cites is dated 2026-10-06, the same reference the other four drafts in this slice use. Round S1 owner decision 7 asks for the date.
   - Replacement: "Verified 2026-10-06 at this head."
   - Confirm the Test Categories entry date before posting. If it differs, use that date.

2. Draft says (changelog title, `changelog/unreleased/SOLR-4336-blank-numeric-params.yml` at `798618aa08fd6de759bad798a825bdf80ee77201`): "A blank numeric request parameter such as start= or rows= is treated as unset (default applies) instead of failing with a NumberFormatException."
   - Evidence: base `SolrParams.java` lines 217-223 catch the NumberFormatException and rethrow it as `SolrException` with `BAD_REQUEST`. The draft's own "What happens today" says "rethrown as a bad request error". "(default applies)" is also wrong for getters with no default, which return null (draft: "A getter with no default returns null for an empty value"). Round S1 item 4 and owner decision 7 flag the same wording.
   - Replacement (changelog title): "A blank numeric request parameter such as start= or rows= is treated as unset instead of causing a bad request error."

Checked and matching: head; Proof 11/11 at head and 2 of 11 on base (receipt line 6); the two test names exist at head (`SolrParamTest.java` lines 70 and 87); the head getters `getNumeric` and `getFieldNumeric` (lines 209-219) are used by every int, long, float and double getter, and by `getFieldInt`, `getFieldFloat` and `getFieldDouble`, with and without a default (`getFieldDouble` with default at line 403); `RequiredSolrParams` lines 113-116 delegate the defaulted getters, and its `get` (line 41) throws only on null, so the Limits are correct; the blank field value does not fall back to the plain parameter (`getFieldNumeric` uses `getFieldParam`, which returns the empty field value; test line 104).

Optional notes, not blocking:
- The receipt does not name the two failing tests. Round S1 item 2 notes the receipt's "getInt and getFloat ... empty message" wording was not run. The draft avoids that wording, which is correct.
- Plain language: no compressed jargon found.

## SOLR-4422

Verdict: CONSISTENT.

Checked and matching: head; Proof 6/6 at head and the base failure (receipt lines 5-6; the draft says "the keys come back in hash order", which matches the receipt's "hash order"); base `DocumentObjectBinder.java` line 419 `new HashMap<>()` and head line 420 `new LinkedHashMap<>()`; the head import `java.util.LinkedHashMap` (line 32); base `SolrDocument.java` line 49 `new LinkedHashMap<>()`; the list branch uses `ArrayList` (head line 422), as the draft says; the new test `TestDocumentObjectBinder.java` lines 82-101 at head adds 40 fields in descending order (`testDynamicFieldMapKeepsDocumentOrder`); Limits: a map the bean's constructor filled is replaced when matching fields exist, and an empty result returns null so the bean keeps its value (head lines 458-462 and `inject` lines 467-469); the "fill the existing map" alternative sits in Limits, as round S2 recommends; changelog file exists at head and its title matches the headline.

Optional notes, not blocking:
- The receipt's "the two changes are in different methods" is true for method bodies, but the import block conflicts with SOLR-10364 (round S2). That is a landing note, not a draft issue.

## Not done

- No build, Gradle, test, gate or test-queue run. No fetch, no commit, push, PR, comment or JIRA call. Only `ls-remote` on the five submit refs and read-only `git show`, `git grep` and `cat-file`.
- The gate logs named in the receipts (`g3722-gate.log`, `g3999-*.log`, `g4335-*.log`, `g4336-*.log`, `g4422-*.log`) and the main-side takeover log are not reachable from this worktree; I searched the main checkout's `research/` for them and found none. The test counts and the 2026-10-06 dates therefore rest on the receipt text, and the names of the failing tests rest on reading the head and base source, not on run output.
- The round 1 parts `-s4` to `-s6` were not read in full; they do not mention the five tickets (checked by search).
- JIRA snapshots (`research/jira-context/SOLR-*.json`, dated 2026-10-04 per the roll-up) were searched for the keywords behind each "What happens today" quote; the quotes were not checked word for word.
- Draft length and the trim question were not checked (not in the brief's checks).
