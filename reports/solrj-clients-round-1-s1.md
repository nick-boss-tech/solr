# SolrJ and clients round 1, part S1: NamedList and params (SOLR-3722, SOLR-4424, SOLR-4336, SOLR-7709)

Audit and drafts only. No builds, tests, `gh` writes, commits, pushes or posts. The worktree holds `pr-drafts/solrj/` with this part's four files, and nothing else changed.

## Per ticket

**SOLR-3722: draftable.** Head `f9d3dda1d3de` verified (`ls-remote` matches the assignment). Draft `pr-drafts/solrj/SOLR-3722.md`, 3,424 characters with links. The diff is 3 files, +27 and -1, as the receipt says. The premise is confirmed in base code: `nameValueMapToList` calls `getKey()` with no null check (base `NamedList.java` lines 144 to 151). Choice: skip nulls (implemented, as the ticket asks) versus fail with a clear error. Limits: only the Entry-array constructors change, no component changes, the shard mismatch is not investigated, and null keys and null values are not covered.

**SOLR-4424: draftable.** Head `2e947b7f622c` verified. Draft `pr-drafts/solrj/SOLR-4424.md`, 3,999 characters with links. That is above the roughly 3,500 guide. The nine links account for about 1,300 characters, and both the Choice and the Limits carry content the assignment requires, so the draft was not trimmed. The diff is 3 files, +29. The premise is confirmed: base `toSolrParams` accepts a null name without complaint (base lines 349 to 356). Choice: a conversion-time check (implemented) versus the check at the config reader, where the ticket places it. Limits: the callers that convert lists to params in this tree, the empty-name gap, the array-value message, and the server error code.

**SOLR-4336: draftable.** Head `798618aa08fd` verified. Draft `pr-drafts/solrj/SOLR-4336.md`, 3,080 characters with links. The diff is 3 files, +77 and -14. The Limits carry the recorded edges: a whitespace-only value still throws, boolean getters are unchanged, and `RequiredSolrParams` and a null from `get()` are untested paths. The part also verified in code that `RequiredSolrParams.get` throws only for a null value, and that its defaulted numeric getters delegate to the wrapped params. The draft states that a blank field value does not fall back to the plain parameter (see owner decision 4). No Choice section, since the assignment names none.

**SOLR-7709: draftable.** Head `501078f22203` verified. Draft `pr-drafts/solrj/SOLR-7709.md`, 2,982 characters with links. The diff is 3 files, +64 and -2 against its merge-base `c3cdf7b`, which is 47 commits behind `upstream/main`. XML parity is confirmed: `XMLLoader.java` line 405 calls `addField` for repeated field elements. No Choice section, since the ticket asks for parity. The draft states the behavior change for senders that repeat a name expecting the last value to win. The fix commit subject repeats the ticket id ("SOLR-7709: SOLR-7709: ...", commit `d85ec90112e`). Noted here only, not in the draft.

**Head verification:** all four match the assignment and live `ls-remote` exactly. Full SHAs: 3722 `f9d3dda1d3de8d9724a9dc47b7368d378306e01c`; 4424 `2e947b7f622c6c530350ecef3679b01e1b004cde`; 4336 `798618aa08fd6de759bad798a825bdf80ee77201`; 7709 `501078f22203f636c171ec90f63d746896365593`. No fetch was needed.

## Landing order

**3722, then 4424, then 7709. SOLR-4336 is independent and can land at any point.**
- `NamedList.java`: 3722 edits `nameValueMapToList` (head lines 145 to 155), and 4424 edits `toSolrParams` (head lines 349 to 361). A three-way merge of the two against their shared base is clean.
- `NamedListTest.java`: both add a test at the same insertion point, between `testToString` and `testRemove`. The second landing gets exactly one conflict hunk (checked with `git merge-file` on extracted blobs in the scratchpad). Resolution: keep both methods. 3722 also adds two imports (`java.util.AbstractMap`, `java.util.Map`). The method names differ.
- Test counts (test-method count per head, not a run): `NamedListTest` is 5 on base and on 7709, 6 on 3722, 6 on 4424 (the assignment mentions only 3722), and 7 after both land. 7709 does not touch `NamedListTest`, so its 5 of 5 reflects its own base. A `NamedListTest` run on the landed tree (7 tests) is owed before any merge. It was not run here.
- The 7709 files (`JavaBinCodec.java`, `TestJavaBinCodec.java`) do not overlap any NamedList or SolrParams file, and main has not changed them since `c3cdf7b`. Each branch's base is 40 commits behind main (3722, 4424 and 4336 at `97d973814336`) or 47 (7709). For every file each branch touches, base and main are identical, so each applies cleanly to main.
- An edge between 3722 and 4424: a null key (not a null element) in an Entry array is not skipped by 3722. It becomes an unnamed NamedList entry, which 4424 rejects at conversion. Not a textual conflict.

## NamedList and params interaction (4336 and 4424)

No shared file or hunk. The answer is **not fully consistent**. The two changes do not conflict in code, but a config author gets four different outcomes:
- **Blank string value**, `<str name="rows"></str>` (`DOMUtil.getText` returns ""; `toSolrParams` stores it): after 4336, the numeric getters return the default, with no error.
- **Unnamed entry**, `<str>5</str>`: 4424 throws when the list is converted to params.
- **Empty name**, `<str name="">5</str>`: `DOMUtil.getAttr` returns "", so 4424 does not reject it. It is accepted silently. The ticket's own comment names this form as unreasonable.
- **Blank typed element**, `<int name="rows"></int>`: `DOMUtil.parseVal` throws at config parse (`Integer.valueOf("")`). Neither change touches this.

The drafts state their own edges. See owner decisions 3 and 5.

## Self-check per draft

- Dashes: zero em or en dashes in all four (byte search).
- Process words: none in the four drafts (case-insensitive search for gate, receipt, ledger, rc=, owed, round, junit, pre-fix, audit, takeover, pipeline, hypothetical, claude, fresh). The only hit was "investigated" in the 3722 draft, a false positive on the substring "gate". The emoji header and the AI footer are verbatim from `pr-formula.md`.
- Head references: every head link uses the full head SHA in the `nick-boss-tech/solr` fork. Base links use the merge-base (`97d973814336` for 3722, 4424 and 4336; `c3cdf7b46e8c` for 7709) and are labeled base in the text.
- Changelog: each draft links the real file name at the head SHA. The formula's pattern `changelog/unreleased/SOLR-<ticket>.yml` does not match these branches' file names, so the real names are used.
- Proof dates: 3722 and 4424 say "2026-10-06" (from the receipts' takeover-log reference); 7709 says "2026-10-05". 4336 has no date (see disagreement 3), so its draft says "Verified at this head" only.
- Character counts (`LC_ALL=C.UTF-8 wc -m`, with links): 3722 at 3,424; 4424 at 3,999; 4336 at 3,080; 7709 at 2,982.
- Lucene: none of the four drafts names a Lucene version, so that rule does not apply.

## Receipt disagreements

1. **SOLR-4424 receipt:** "the check Hoss described, which fires when the XML response is parsed". The assignment repeats it as "the check the ticket describes, which fires when the XML response is parsed". The ticket (`research/jira-context/SOLR-4424.json`) asks for the check on `solrconfig.xml`, and Hoss's comment asks the XML parsing code to complain about unexpected attributes. The config path is `DOMUtil.addToNamedList` (`solr/solrj` `DOMUtil.java` lines 133 to 155). No response parser is involved. The draft says "where solrconfig.xml is read".
2. **SOLR-4336 receipt:** "on base, SolrParamTest fails exactly 2 of 11, getInt and getFloat on an exactly-empty parameter value throwing NumberFormatException with an empty message". At head, the first base-throwing call in `testBlankNumericParamIsUnset` is `getInt("start")` (test line 72). In `testBlankFieldNumericParamIsUnset` it is `getFieldInt("title", "mincount")`. `getFloat` is not reached first in either. Base `getInt` catches the NFE and throws SolrException BAD_REQUEST (base `SolrParams.java` lines 218 to 222), so the caller sees a bad request error, with the NFE as cause. The "empty message" wording needs a run to confirm, and none was run. The receipt does not name the two failing methods. The draft names the two new tests, which are the only tests in base or head that use blank numeric values.
3. **SOLR-4336 receipt has no run date,** but `pr-formula.md`'s Proof template asks for "verified <date>". The draft omits the date.
4. **SOLR-4336 branch changelog (not the receipt):** "instead of failing with a NumberFormatException". The code throws a SolrException with BAD_REQUEST. The changelog wording is loose (owner decision 7).
5. **SOLR-7709 receipt** is accurate on the double id, which was confirmed. Nothing else differs.
6. **Assignment NamedListTest counts:** "its count is 6 against 7709's 5" is correct, but it omits that 4424's head is also 6, so the two land to 7.

## Owner decisions

1. **Landing order:** approve 3722, then 4424, then 7709, with 4336 at any time. Expect one `NamedListTest` conflict on the second landing. Recommendation: approve.
2. **SOLR-4424:** keep the conversion-time check (as implemented), or move it to the config reader as the ticket describes. Recommendation: keep the implemented check. The draft asks maintainers to choose.
3. **SOLR-4424 empty name (`name=""`):** state it as a Limit with a follow-up offer (as drafted), or add the check to this change. Recommendation: Limit now.
4. **SOLR-4336 blank field value** (`f.title.start=` with `start=3` present): keep "unset, no fall-through" (as implemented, tested and drafted), or fall back to the plain value. Recommendation: keep and state it, as drafted.
5. **Accept the split** of blank, unnamed, empty-name and blank-typed outcomes across 4336 and 4424. Recommendation: accept, with the edges stated in both drafts.
6. **SOLR-3722:** confirm the skip-versus-fail question as drafted. Recommendation: keep the implemented skip.
7. **SOLR-4336:** add the run date from the run log, and fix the changelog wording (NumberFormatException versus bad request). Recommendation: fix both before submission.
8. **SOLR-7709:** rebase onto `upstream/main` before submission (the rebase should be clean, since no file it touches has moved), and decide on the double-id commit subject at the next push. The owner's call.

## Not checked

- No build, test or gate run. Pass and fail results and counts are the receipts' records. The part counted test methods per head (grep "public void test") and read the code. The base failure text and the JDK exception messages are unverified.
- `SimpleOrderedMapTest` (18) and `TestNamedListCodec` (4) were not counted against the head.
- "No response parser calls `toSolrParams`" rests on a grep of `toSolrParams()` under `solr/` at `2e947b7`. Not every response path was read.
- "A blank field value does not fall back" rests on the test assertion and base `getFieldParam` (`SolrParams.java` lines 144 to 147). Not run.
- The merge checks used `git merge-file` on extracted blobs in the scratchpad. No refs, index, worktree or commits changed.
- Receipt dates come from the receipts' references to the takeover log. The log itself is not in this worktree.
- The Jira JSON was read from the main checkout, read only.
