# Search components draft fidelity, slice s10

Assignment: slice s10 of the search-components draft pool, read-only. Drafts: `pr-drafts/search-components/SOLR-9148.md`, `SOLR-9396.md`, `SOLR-9864.md`. Brief: the scratchpad `brief-draft-fidelity.md`.

Claim: worktree HEAD `d627304e96b` ("Claim the queue buffer review, draft and RCA assignments (windows review agent)"), as the lead named it. The brief's `e84522fa5bc` is an ancestor of that HEAD, so the claim is consistent with it.

Round 1 sources: `reports/search-components-3-w2.md` (SOLR-9396), `reports/search-components-3-w5.md` (SOLR-9148, SOLR-9864), roll-up `reports/search-components-3.md`. Receipts: `receipts/SOLR-9148.md`, `receipts/SOLR-9396.md`, `receipts/SOLR-9864.md`. Answers material: a grep of `material/` for 9148, 9396 and 9864 returned no hits. Other reports in `reports/` that match these numbers are other slices, not this category.

Heads: `git ls-remote origin refs/heads/<branch>` matched each draft's named head on this run. Every cited SHA resolves in the worktree (`cat-file -t`). No build, test, gate, commit, push or post was done.

## Verdicts

| Draft | Head checked (ls-remote) | Verdict |
|---|---|---|
| SOLR-9148 | `30f0d7a42d501316bd9d5336fe564608e78bfbba` (solr-9148-submit: same) | DRIFT (2 items) |
| SOLR-9396 | `a5ab2eda4e6a9bcad5668653e80c0d0b510e6388` (solr-9396-submit: same) | DRIFT (3 items) |
| SOLR-9864 | `b5826e466b9a8664adf11e0f247cff220f2f5953` (solr-9864-submit: same) | DRIFT (1 item) |

## SOLR-9148

Verdict: DRIFT (2 items).

1. Draft says: "([SQLHandler.java L121-L127](https://github.com/nick-boss-tech/solr/blob/30f0d7a42d501316bd9d5336fe564608e78bfbba/solr/modules/sql/src/java/org/apache/solr/handler/sql/SQLHandler.java#L121-L127))"
   - Evidence: the sentence describes the pre-change symptom, the fixed list of connection settings. `pr-formula.md` lines 92-94: pre-change symptom code links the merge-base, and the text says so. The merge-base of `30f0d7a` with local `upstream/main` (`3f5d4c5bf8a`) is `97d973814336101e12475558d7419321c743de79`. Lines 121-127 are identical at that base and at the head.
   - Replacement: "([SQLHandler.java L121-L127 before this change](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/modules/sql/src/java/org/apache/solr/handler/sql/SQLHandler.java#L121-L127))"

2. Draft says: "- Run date: [run date to confirm]."
   - Evidence: a placeholder. `pr-formula.md` line 27 and template line 147 require a verification date. `receipts/SOLR-9148.md` line 4 records the gated head date 2026-10-06, and line 9 records a takeover-log entry dated 2026-10-06. No run log is on disk. Round w5 owner decision 1 asks the owner to confirm run dates.
   - Replacement: "- Verified 2026-10-06 at `30f0d7a42d501316bd9d5336fe564608e78bfbba`."

Consistent: the counts (TestSQLHandler 35 of 35 at the tip; first assertion `assertEquals(1, tuples.size())` at 30f0d7a `TestSQLHandler.java` L485, base gives 3) match `receipts/SOLR-9148.md` lines 6-7. `f8758ebe763` is the commit before the docs-only commit `30f0d7a` (one commit between them). The five builder calls are at head `SolrTable.java` L313, L544, L663, L805, L897 (`addFilterQueries`). The fq write is at `SQLHandler.java` L133-L139 and the reader prefix `solr.sql.fq.` is at `SolrTable.java` L121. Limits match round w5 Finding 2 (JDBC wording, used verbatim) and owner decision 3 (select distinct under map_reduce named as untested follow-up). No choice section; round w5 gives none, and none is needed.

Optional notes, not blocking:
- Branch-side, not a draft error: `sql-query.adoc` line 454 at `30f0d7a` still reads "The JDBC driver cannot set filter queries, since it has no request parameters." Round w5 Finding 2 asks for the replacement the draft already uses. Until the branch changes, the PR carries the overstated sentence. The docs edit moves the head and needs a fresh gate.
- Branch-side: `changelog/unreleased/SOLR-9148-sql-filter-queries.yml` line 2 lists "(select, stats, group by)". The draft's scope includes select distinct under map_reduce (round w5 Finding 3). Optional title: "...translated into (select, stats, group by, select distinct)." Also a head change.
- The Limits bullet "They do not authenticate the caller, and a caller who sends the request can leave them out." is accurate from the code, but it is not in round w5. Confirm or drop.
- The five builders are listed as select, stats, facet group by, map_reduce group by, select distinct, but the links run 313, 544, 663, 805, 897 (select, map_reduce group by, facet group by, select distinct, stats). Optional: reorder the links to match the sentence.
- The changelog file name carries a suffix (`SOLR-9148-sql-filter-queries.yml`), not the `SOLR-<ticket>.yml` form in the template. The link goes to the fragment at the head, as `pr-formula.md` line 174 says. Not blocking.
- Plain language: "plan shape" and "builders" are compressed, and "tip" is git jargon. Optional: "in every query shape above"; "at the latest commit".
- Base merge-base is computed here, not read from a recorded base. The lead should confirm it.

## SOLR-9396

Verdict: DRIFT (3 items).

1. Draft says: "([SubQueryAugmenterFactory.java L375-L396](https://github.com/nick-boss-tech/solr/blob/a5ab2eda4e6a9bcad5668653e80c0d0b510e6388/solr/core/src/java/org/apache/solr/response/transform/SubQueryAugmenterFactory.java#L375-L396))"
   - Evidence: this is the pre-change symptom (the `transform` method and its catch with the `while invoking` message). Per `pr-formula.md` lines 92-94 it must link the merge-base `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622` and say so. At that base the same method is at L344-L365 (the file is 367 lines). Head L375-L396 and base L344-L365 are identical by diff.
   - Replacement: "([SubQueryAugmenterFactory.java L344-L365 before this change](https://github.com/nick-boss-tech/solr/blob/c3cdf7b46e8cfff3673f76d881f32cf8e7b00622/solr/core/src/java/org/apache/solr/response/transform/SubQueryAugmenterFactory.java#L344-L365))"

2. Draft says (Limits, first bullet): "- Only a `$row.field` reference written in the subquery's own parameters is scanned. A reference that reaches the subquery through another request parameter is not scanned, so that field must still be listed in `fl`."
   - Evidence: at head `a5ab2eda` line 307 is `Pattern.compile("\\$\\{?row\\.([\\w.\\-]*\\w)")`. The `\{?` branch also matches `${row.field}`, so "only" is wrong at this head. `receipts/SOLR-9396.md` line 9 records "only literal $row. and ${row. references ... are scanned". Round w2 finding 5 and owner decision 4: the `\{?` branch requests a field no subquery reads. The draft does not mention `${row.` either way.
   - Replacement: "- Only a `$row.field` or `${row.field}` reference written in the subquery's own parameters is scanned. The `${row.field}` form is scanned, but no subquery reads that field, so it costs one extra field read. A reference that reaches the subquery through another request parameter is not scanned, so that field must still be listed in `fl`."
   - If the owner applies the regex fix (round w2 finding 5, `Pattern.compile("\\$row\\.([\\w.\\-]*\\w)")`), this bullet goes back to the draft's current wording. The head, the Proof counts and the changelog link then change too, and need a fresh gate.

3. Draft says: "## Limits" followed directly by the bullets, with no bold summary line.
   - Evidence: `pr-formula.md` lines 81-84 require every section to open with a bold one-line summary. The other sections of this draft do. Limits does not.
   - Replacement (insert directly under "## Limits"): "**Two limits: a reference that reaches the subquery through another request parameter is not scanned, and `/get` has no test.**"

Consistent: the counts (`receipts/SOLR-9396.md` line 6: 17 of 17 = 11 + 3 + 1 + 2) and the base run (line 7: 1 failure of 11, `testRowFieldNotInFl`). At head, `TestSubQueryTransformer.java` L257-L283 holds `testRowFieldNotInFl` and ends with `count(//result/doc/arr[@name='dept_ss_dv'])=0`, which matches "read for the subquery but not written". The scan is at head L307-L362 (`ROW_REFERENCE`, `findRowReferences`, `getExtraRequestFields`). The changelog fragment `changelog/unreleased/SOLR-9396-subquery-row-fields.yml` exists at head, its title matches the bold lead of "What this change does", and its type is fixed. The Jira packet's example fails with "while invoking xxx:[subquery]", and its workaround (fl=* or list the fields) matches the draft. `/get` is consistent with round w2 finding 7.

Choice to check: the live alternative is reading every field whenever a [subquery] is present. Its cost (every subquery reads every field) is stated. The question is pointed. The ticket text itself names the `getExtraRequestFields` approach as the right fix, so the alternative is plausible. Round w2 does not evaluate this choice, so the lead may want round confirmation.

Optional notes, not blocking:
- Pre-post cleanup is not on the branch yet: squash the handoff commits `f0e03ca25da` and `a5ab2eda4e6` (round w2 FIX 4; both subjects show in the PR commit list), and apply the regex fix (round w2 FIX 5). Either moves the head, so the head, citations, Proof counts and changelog link need a fresh gate.
- "verified 2026-10-05" comes from the branch date and the takeover-log entry (round w2 finding 13). No run log is on disk. Owner decision 6 asks the owner to confirm.
- Round w2 finding 6 (null element in `findRowReferences`) is not in the draft. Optional guard `if (value == null) continue;`.
- The changelog file name has a suffix, as in SOLR-9148.
- Plain language: "base run" can read "run before this change"; "row-reference" is compressed.

## SOLR-9864

Verdict: DRIFT (1 item).

1. Draft says: "- Run date: [run date to confirm]."
   - Evidence: a placeholder. `receipts/SOLR-9864.md` line 4 records the gated head date 2026-10-05, and line 9 records a takeover-log entry dated 2026-10-05. No run log is on disk. Round w5 "Open item: run date" and owner decision 1.
   - Replacement: "- Verified 2026-10-05 at `b5826e466b9a8664adf11e0f247cff220f2f5953`."

Consistent: the symptom link `SolrQuery.java L1182-L1188` is at the merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` (computed merge-base; matches receipt base), and the text says "at base". Base lines 1182-1188 are the whole `getCopy()`. The fix citation `SolrQuery.java L1187-L1189` at head holds the sort-list copy. `SortClause` has final `item` and `order` fields (head L1293 onward). The new test `testGetCopyKeepsSortClauses` is at head L132-L146 of `SolrQueryTest.java`. Its first comparison (L138, `assertEquals(q.getSorts(), copy.getSorts())`) fails on base, where `getSorts()` on the copy returns `List.of()`. Base `addSort` rewrites the `sort` parameter with only the new clause, as the draft says. The diff from base to head is 3 lines in `SolrQuery.java`, 16 in the test, and 9 in the changelog. `getCopy()` is the only copy method. The counts match `receipts/SOLR-9864.md` line 6 (27 of 27 = 17 + 5 + 5). The Jira versions 4.8.1, 6.3, 7.7 and 9.0 match `research/jira-context/SOLR-9864.json` lines 14-17. The changelog `SOLR-9864-solrquery-getcopy-sorts.yml` exists at head, is type fixed, and its title matches the bold lead. Limits (no-sort copy stays null; no other copy method changed) match the code. Round w5 names no Limits or choice for this ticket.

Optional notes, not blocking:
- Branch-side fix pending (round w5 Finding 1): at head, the block comment for `testGetSortImmutable` (`SolrQueryTest.java` L128-L131) sits above the new test. Move the new method above the comment. This moves the head and needs a fresh gate. The draft text is unaffected.

## Not done

- No `gh` calls. PR state, CI runs and the Actions run numbers in the receipts were not checked; they are not part of the checks in this brief.
- No `git fetch`. Live heads come from `ls-remote` only.
- The 9148 merge-base is computed from the local `upstream/main`. No recorded base for 9148 exists in the receipt or the round, so the lead should confirm it.
- Jira was checked only for the 9864 versions and the 9396 example. The 9148 "What happens today" examples (geo and access filters) were not checked against the packet.
- Round gate logs are not on disk (round w5). Receipt counts were taken as recorded and not re-derived. No builds, tests or gate runs.
