# Receipt refresh round 2, part b2 (corrected): SOLR-6759, SOLR-8003, SOLR-10694

## Correction to the earlier pass

The earlier pass of this report used stale refs. Its `origin/solr-6759-submit` and `origin/solr-8003-submit` were not fetched when it ran, so its values were pre-packaging tips (6759 at 62974ef8d18, 8003 at f1c99a44961). Its holds on 6759 and 8003 were for the missing gated heads. Those branches are now fetched. Every check below uses only the `origin/` refs at 42f03eb52376 (6759), 17c516a607a8 (8003) and 64e86811548b (10694). The local `solr-<ticket>-submit` branches were not used.

What changed:

- Earlier Finding 1 (claim table rows for 6759 and 8003 wrong) is withdrawn. The claim table rows are correct. The earlier pass compared them with the stale refs.
- 6759: the handoff note is gone at the head. The earlier pass saw it at the stale tip. The verdict stays HOLD, for a new reason: the changelog title claims a fix the test does not show.
- 8003: the handoff note is gone at the head. The SOLR-14678 textual conflict still holds at the head. The verdict stays HOLD, now for owner decisions rather than the missing head.
- 10694: verdict unchanged (draftable, already drafted). Confirmed at 64e86811548. Small NOTEs on the existing draft.
- No new drafts were written. Nothing was committed, pushed, built, tested, fetched, or posted.

## Heads checked (origin refs only)

| Ticket | Origin ref | Head checked | Receipt gated head | Match |
|---|---|---|---|---|
| 6759 | origin/solr-6759-submit | 42f03eb52376 | 42f03eb5237 | yes |
| 8003 | origin/solr-8003-submit | 17c516a607a8 | 17c516a607a8 | yes |
| 10694 | origin/solr-10694-submit | 64e86811548b | 64e86811548b | yes |

## Verdicts

| Ticket | Verdict at fetched head | Earlier pass | Changed |
|---|---|---|---|
| SOLR-6759 | HOLD, not drafted | HOLD (head missing) | Yes, reason. Handoff gone. Changelog title overclaims. |
| SOLR-8003 | HOLD, not drafted | HOLD (head missing) | Yes, reason. Handoff gone. Owner decisions remain. |
| SOLR-10694 | DRAFTABLE, draft exists and is confirmed | DRAFTABLE | No. Draft confirmed at head. |

## Findings

**F1. FIX. SOLR-6759 changelog title.** `changelog/unreleased/SOLR-6759-expand-complete-postfilter.yml`, line 2, at 42f03eb52376. The title says the change means post filters "that emit their last group there (for example block collapse) no longer lose it in the expanded section."

Evidence:
- `CompleteTrackingQParserPlugin.TrackingPostFilter` (test source) only counts `complete()` calls. Its collector is a plain `DelegatingCollector` that passes every document through and holds none back. No group can be lost under this test filter.
- `TestExpandComponent.testExpandCompletesPostFilterCollectors` (lines 950 to 975): the expanded count check is at 958 to 965 (`count=2`), and the counter check is at 971 to 974 (`CREATED` equals `COMPLETED`). The receipt says the only failure on base is the counter mismatch (`COMPLETED` one less than `CREATED`). So the expanded count passes on base too and does not discriminate.
- The handoff note removed at 42f03eb52376 says the user-visible symptom (block collapse through `expand.fq`, last group missing) is "not covered." Read at its parent commit 42f03eb5237^.

Replacement for line 2: `title: ExpandComponent now calls complete() on the post filter collector chain it builds for the expand search.`

This is a branch edit. It was not made here.

**F2. NOTE. SOLR-6759 partial-results path.** `ExpandComponent.java` lines 448 to 451 at the head: `searcher.search(...)` at 448, `complete()` at 450, no try or finally. The upstream pattern in `SolrIndexSearcher` (read in the earlier pass at upstream/main 8e62c268688, not re-read here) calls `complete()` in a finally. If the search throws, the new call is skipped. This is an owner call. If the pattern is copied:

```
      try {
        searcher.search(QueryUtils.combineQueryAndFilter(query, pfilter.filter), collector);
      } finally {
        if (collector instanceof DelegatingCollector) {
          ((DelegatingCollector) collector).complete();
        }
      }
```

Name it in Limits either way.

**F3. NOTE. SOLR-6759 receipt mechanism.** The receipt's Proof says a post filter that holds documents until `complete()` "loses its last group." The test filter holds nothing back (see F1), so that mechanism is not exercised. Public text must say the test checks the `complete()` call count, not a lost group. No branch change.

**F4. NOTE. Commit bodies (6759, 8003, 10694).** `3e26e5673ca`, `a8a217dbc2a` and `66437fe30f1` each say "Hypothetical, unrun regression test; see <TICKET>-TESTING.md." The referenced files were removed in 42f03eb5237, e23884822f3 and 29a4051de1c, so the references point to nothing, and the wording is process language. A squash merge with a clean PR title and body covers this. Rewriting the fork branch needs an explicit decision and a force push. Not done here.

**F5. NOTE. SOLR-8003 changelog scope.** `changelog/unreleased/SOLR-8003-fl-glob-raw-transformer.yml`, lines 2 to 3, at 17c516a607a8. The title says the glob "writes every returned field matching the glob raw" with no writer limit.

Evidence:
- `RawValueTransformerFactory.appliesTo` (lines 94 to 104) returns true only when the request's writer matches the factory's `wt`. `createForGlob` (lines 113 to 114) returns null otherwise.
- The default json factory is `new RawValueTransformerFactory("json")` (`TransformerFactory.java` line 117).
- `testGlobJsonTransformer` (line 305) checks that `[xml]` on a JSON response returns fields as normal.

Replacement for lines 2 to 3: `title: The fl parameter accepts a glob in front of a raw value transformer, for example su*:[json]. In a JSON response, every returned field matching the glob is written raw instead of failing with "Error parsing fieldname".`

**F6. NOTE. SOLR-8003 textual conflict with SOLR-14678 (still holds).** `git merge-tree --write-tree 17c516a607a8 origin/solr-14678-submit` (origin/solr-14678-submit at 5d94e6cf3981) reports `CONFLICT (content)` in `solr/core/src/java/org/apache/solr/response/transform/DocTransformers.java`. Both sides add an override at the same spot: 8003 adds `getRawFieldGlobs()` (line 57), and 14678 adds `getExtraResponseFields()` (line 59 on its side). Resolution for whichever lands second: keep both overrides. This is sequencing only. 8003's own diff does not need a change for it.

**F7. NOTE. SOLR-8003 proof wording.** The receipt's Proof uses `fl=*_json:[json]` as the example. The test uses `fl=id,su*:[json],link?:[json]` (line 305 onward), then `au*:[xml]` on a JSON response, then `su*:[docid]` for the error case. A public draft should quote the tested form. Base rejects a glob with a transformer at `SolrReturnFields.java` lines 431 and 440 ("Error parsing fieldname"), which matches the receipt's base failure. `expectThrows(SolrException.class)` matches the SolrJ remote type (`RemoteSolrException extends SolrException`). The test was not run.

**F8. NOTE. SOLR-8003 long line.** `SolrReturnFields.java` line 330 at 17c516a607a8 is 102 characters and is branch-added (the glob block spans lines 318 to 345). The tidy commit 17c516a607a8 fixed the over-long assertion in the test file (the earlier 113-character line is now split) but not this one. The receipt says tidy is clean at the head. Tidy was not run here. If tidy flags it, split the string literal.

**F9. NOTE. SOLR-10694 draft test link.** In `pr-drafts/search-components/SOLR-10694.md`, the test link reads `TestCSVResponseWriter.java#L359-L397`. The method ends at line 396 (line 397 is blank). Replacement: `#L359-L396`. Optional.

**F10. NOTE. SOLR-10694 failing-test name is an inference.** The receipt says one of four tests fails on base but does not name it. The draft names `testStructuredValuesAreWrittenAsJsonCells`. The inference is strong: the test file diff is additions only (43 insertions, 0 deletions), the new test is the only one that writes structured values, and base `TabularResponseWriter.java` lines 121, 136, 139 and 142 write nothing. The gate log is not on disk. Keep the name and confirm it from the gate log before the PR opens. No draft change now.

**F11. NOTE. SOLR-10694 draft length.** The draft is 3,844 characters, about 10 percent over the 3,500 guide. Optional trim: drop the second `writeArray` link (L142) and the L436 link. Not applied.

**F12. NOTE. SOLR-10694 changelog title.** `changelog/unreleased/SOLR-10694-csv-structured-values.yml`, line 2, at 64e86811548. The title lists map, NamedList and iterator values. `writeCellVal` also writes `Object[]` (`CSVResponseWriter.java` line 460). Optional replacement: `title: The CSV response writer writes map, NamedList, iterator and array values of a document as a JSON cell instead of dropping the cell and shifting the following columns.`

## Task results

**SOLR-6759 at 42f03eb52376: HOLD, not drafted.**
- Handoff note: gone. Packaging commit 42f03eb5237 ("remove handoff doc") deletes `SOLR-6759-TESTING.md`, 24 deletions, and nothing else.
- Branch-only files (relative to merge base cabedd1d968): changelog (8 lines), `ExpandComponent.java` (+4), `solrconfig-collapseqparser.xml` (+1), `CompleteTrackingQParserPlugin.java` (+102), `TestExpandComponent.java` (+28).
- Test methods: 9 at the head, 8 at base. Matches the receipt's "9 of 9".
- Holds: F1 (title overclaims) and the owner calls in F2.
- Draftable when: the title is corrected on the branch (F1), the owner picks the wording and the try/finally (F2), and the draft's "What happens today" names the missing `complete()` call and says the JIRA symptom is not reproduced. The Proof says the test checks the `complete()` call count.

**SOLR-8003 at 17c516a607a8: HOLD, not drafted.**
- Handoff note: gone. Packaging commit e23884822f3 deletes `SOLR-8003-TESTING.md`, 33 deletions.
- Tidy commit 17c516a607a: `TestRawTransformer.java` only (3 insertions, 1 deletion). It splits the over-long assertion. Matches the receipt's "one tidy commit for the branch's test."
- Test methods: 3 at the head, 2 at base. Matches "3 of 3."
- Proof matches the code path (F7). Counts come from the receipt; the test was not run.
- Holds: F6 (conflict still holds), F5 (changelog scope), and two owner decisions: confirm the glob-level raw design against per-field transformers (earlier owner decision 4), and the landing order with SOLR-14678.
- Draftable when: the owner decides both, and the changelog scope (F5) is fixed on the branch.

**SOLR-10694 at 64e86811548b: DRAFTABLE, already drafted, draft confirmed.**
- Handoff note: absent. Removed in 29a4051de1c.
- Tidy commit 64e86811548: `CSVResponseWriter.java` only, 2 insertions and 2 deletions, all inside the `writeCellVal` javadoc. Javadoc-only, as the claim says.
- Test methods: 4 at the head (`testCSVOutput`, `testStructuredValuesAreWrittenAsJsonCells`, `testPseudoFields`, `testForDVEnabledFields`), 3 at base. Matches "4 of 4."
- Draft Proof names head `64e86811548` and the full SHA appears in the links.
- Draft line references checked at the head: L121, L136, L139, L142 (`TabularResponseWriter`: `writeNamedList`, `writeMap`, and the two `writeArray` overloads), L418, L436, L441, and L454 to L465 (`writeCellVal`). All correct. The test link is off by one line (F9).
- Draft formula check: AI header and footer present, the four sections present, narrow scope named in Limits with a follow-up offer. No em dash or en dash.
- Open: F9 to F11 (NOTEs). F10 needs the gate log.

## Owner decisions (not decided here)

1. SOLR-6759: changelog wording (F1), whether to copy the try/finally (F2), and whether to open a PR whose proof is a call-count check when the user-visible symptom is not covered.
2. SOLR-8003: confirm the glob-level raw design against per-field transformers, and decide landing order with SOLR-14678 (F6).
3. SOLR-10694: the draft already scopes to CSV with a Limits line, which matches the formula's small-patch rule. Hold only if the owner wants every writer covered first (the JIRA summary says all response formats).
4. Commit bodies (F4): squash merge or accept as is. A rewrite needs a force-push decision.

## Not checked

- No fetch, `ls-remote`, build, Gradle, test, `gh` call, post, commit, or push. Nothing committed.
- The worktree is at a detached HEAD (`f52f0504fc8`) with unrelated uncommitted edits (`pr-drafts/solrcloud/*`, other `reports/*`). Left untouched.
- Gate logs: none on disk for g6759, g8003 or g10694 (searched `research/test-queue` and the receipts folders). Proof counts come from the receipts. Test counts are method counts read from source at the heads.
- Tidy and Error Prone were not run. F8 is not judged by tidy.
- JIRA was not read live, and the local packets were not re-read in this pass.
- The failing-test name for 10694 is an inference (F10).
