# Receipt refresh round 1, part r4: highlighting drafts SOLR-2681 and SOLR-4540

Result: Both live heads match the claim table and both receipts support the core claims, but neither draft is postable as written. SOLR-2681 needs 4 fixes. SOLR-4540 needs 5 fixes and an owner call on a choice section.

Live check: `git ls-remote` (read only) returned `a3b1ea7994d932cdf78667f799542d7d823f7d49` for `solr-2681-submit` and `180b6e8a7d3c23ac308a28d547f61816b5815f04` for `solr-4540-submit`. Both match the claim table. Base for both branches is `cabedd1d968059215188f4e7563fb303241899ed` (merge base with the local `upstream/main` ref). No draft was edited.

## Findings

### SOLR-2681 (`C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester\pr-drafts\highlighting\SOLR-2681.md`)

1. FIX, line 13 (bold summary of "What this change does"). Replace `**The original highlighter now reads the query inside a \`query(...)\` function query.**` with `**The original highlighter now reads the query inside a top-level \`query(...)\` function query.**`
   Evidence: receipt line 5 says the title was narrowed to "top-level" because the broader wording overstated the change. The shipped changelog title at `a3b1ea7994d` (line 1 of `changelog/unreleased/SOLR-2681-highlight-function-query.yml`) reads "...from the query inside a top-level query(...) function query." The body at line 15 also says "top-level". The summary alone drops the word.

2. FIX, line 24 (Proof). Replace `- At head \`a3b1ea7994d\`, HighlighterTest passes 36 of 36.` with `- At head \`a3b1ea7994d\`, HighlighterTest passes 36 of 36; verified 2026-10-09.`
   Evidence: the template line 144 of `pr-formula.md` requires "verified <date> at this head". Receipt lines 3 and 5 give the re-gate finish date as 2026-10-09. The receipt does not date the count line itself (see owner decision 3).

3. FIX, line 33 (Limits). Replace `A follow-up submission is planned for the nested forms.` with `A follow-up ticket and PR for the nested forms can be opened on request.`
   Evidence: `pr-formula.md` section 4 says a narrow-scope item goes in Limits with an offer to open a follow-up ticket and PR on request. Neither the receipt nor the branch records a planned follow-up, so "is planned" states a commitment nobody has made.

4. FIX, line 36 (Changelog). Replace `Changelog: \`changelog/unreleased/SOLR-2681-highlight-function-query.yml\`` with `Changelog: [\`changelog/unreleased/SOLR-2681-highlight-function-query.yml\`](https://github.com/nick-boss-tech/solr/blob/a3b1ea7994d932cdf78667f799542d7d823f7d49/changelog/unreleased/SOLR-2681-highlight-function-query.yml)`
   Evidence: the claim's shared rules require the Changelog line with a link. The file exists at `a3b1ea7994d` (`git show`).

5. NOTE, line 23. "runs 36 tests" does not match a reader's count. The file has 31 `@Test` methods at `a3b1ea7994d` (the base has 30). Receipt line 7 says the class declares 31 methods and executes 36 cases, and the recount log is not on disk. Optional replacement for the words "runs 36 tests": `runs 36 test cases (31 test methods)`.

6. NOTE, line 9 (base link `cabedd1d968.../DefaultSolrHighlighter.java#L295-L298`). The formula says file citations link to the PR head SHA. This link shows today's code, which only the base SHA can show, so it is correct as a labelled base citation. Checked: base lines 295 to 298 are the `extract` signature and the first `if`, with no FunctionQuery case. Owner decision 5 covers this.

### SOLR-4540 (`C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester\pr-drafts\highlighting\SOLR-4540.md`)

7. FIX, line 1. Delete the whole line `<!-- Written against branch head 180b6e8a7d3c23ac308a28d547f61816b5815f04. Remove before posting. -->`.
   Evidence: the AI header must be line 1 (`pr-formula.md` section 6 and the claim's rules). The comment holds process wording ("Written against", "Remove before posting"). The head is named in the Proof after finding 10.

8. FIX, line 10 ("What happens today"). Replace `[DefaultSolrHighlighter.java L641](https://github.com/nick-boss-tech/solr/blob/180b6e8a7d3c23ac308a28d547f61816b5815f04/solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L641)` with `[DefaultSolrHighlighter.java L636](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L636)`.
   Evidence: at `cabedd1d968`, line 636 is `SolrFragmentsBuilder solrFb = getSolrFragmentsBuilder(fieldName, params);`, the first statement of the method, which is the "looks up first" step the sentence describes. At `180b6e8a7d3`, line 641 is that same statement after the new guard (lines 636 to 640). The linked head page therefore shows the lookup after the guard, which contradicts the sentence.

9. FIX, line 24 (Proof, bullet 2). Replace the whole bullet with `- On the base code, this test fails at line 106 of the test.`
   Evidence: "premise run", the seed, and the log file name are internal (the shared rules ban "premise run" and internal log names; the formula's Proof reports outcomes only). The receipt (line 7) is the only source for line 106, and the log is not on disk. At `180b6e8a7d3`, line 106 of `FastVectorHighlighterTest.java` is the `assertQ(` call, which fits a failure at that line.

10. FIX, line 25 (Proof, bullet 3). Replace the whole bullet, which currently reads "A GitHub Actions run of `:solr:core` that covers FastVectorHighlighterTest completed successfully at this head.", with `- At head \`180b6e8a7d3\`, FastVectorHighlighterTest passes 3 of 3; verified 2026-10-09.`
   Evidence: receipt line 8 says run 37622803991 completed at the earlier head (2026-10-07), not at `180b6e8a7d3`, so "at this head" is false. The receipt does not name that earlier head SHA. Receipt line 6 gives 3 of 3 at the head from fresh JUnit XML, and the file has 3 `@Test` methods at `180b6e8a7d3`, so the count matches. The Proof currently has no count, no date, and no head, which the template and the claim's rules require. See owner decision 2.

11. FIX, line 34 (Changelog). Replace `Changelog: \`changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml\`` with `Changelog: [\`changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml\`](https://github.com/nick-boss-tech/solr/blob/180b6e8a7d3c23ac308a28d547f61816b5815f04/changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml)`
   Evidence: the shared rules require a link on the Changelog line. The file exists at `180b6e8a7d3`, and its title matches receipt line 4 ("FastVectorHighlighter no longer does per-field work for fields a document does not have.").

12. NOTE, line 10, `[L415-L421]` (head link). Lines 415 to 421 are identical at base and head (`git show` of lines 412 to 424 shows no difference), so the link is correct. Pointing it at `cabedd1d968` would match finding 8. Optional.

13. NOTE, lines 16 to 17 ("What this change does", behavior change). The bad-builder behavior change is a live alternative a maintainer could reject, so a "A choice to check" section may be warranted. Owner decision 1 has proposed wording. Without that section, the draft is still accurate.

### Repo state (not a draft issue)

14. NOTE, local refs. Local `solr-2681-submit` is at `1eb119ed747` and local `solr-4540-submit` is at `4a0a97d5383`. Each has one extra commit, "add hypothetical-reproduction handoff doc", above a chain whose earlier commits have different SHAs from the gated heads (for example `ca18813767c` and `3bca915696f`). The remote heads match the claim, so the drafts are not affected. Do not push these local refs until the handoff doc is checked against the outbound file list.

## Task results

**SOLR-2681: draftable after fixes 1 to 4.** The branch matches the draft. The diff touches only the original highlighter's term extractor (`DefaultSolrHighlighter.java`, head lines 300 to 303, `FunctionQuery` case) and one new test (`HighlighterTest.java`, lines 91 to 109). The test name, query, and expected `<em>lorem</em> ipsum` match the draft. The ticket example `{!func}product($v1,$v2)` matches the packet at `research/jira-context/SOLR-2681.json`. The Proof count of 36 of 36 comes from the receipt. No choice section is needed: the open question is scope, and the draft already puts it in Limits, which is correct. Verdict: accept after fixes 1 to 4.

**SOLR-4540: not postable as written; draftable after fixes 7 to 11.** The branch matches the draft. The guard at `DefaultSolrHighlighter.java` lines 636 to 640 returns null before the builder lookup, and the new test covers the case the draft describes (two documents, `hl.fl=tv_*`, unknown builders on the two absent fields, query matching only the first). The changelog title matches receipt line 4, and the ticket quote ("QTime is horribly big (> 10s)... ~30ms") matches the packet. The problems are in the text: a process comment on line 1, a "today" link that points at the post-change line, internal Proof wording, a false "at this head" Actions claim, and a Proof with no count, date, or head. Verdict: accept after fixes 7 to 11, and decide owner decision 1 before posting.

Dash and wording check: neither file contains an em dash or an en dash (byte check). The only internal wording found is in SOLR-4540 lines 1, 24, and 25, covered by findings 7, 9, and 10.

## Owner decisions

1. SOLR-4540 choice section (finding 13). Include or skip. Proposed wording for review, not a draft: "**A choice to check.** An unknown fragmentsBuilder on a field now goes unreported when no matching document has that field. The other route checks every requested builder name up front, so a typo always fails, even for a request that matches no document with that field. Was skipping per document the right call?" Recommendation: include it, because a maintainer could plausibly pick the other route.
2. SOLR-4540 Actions claim (finding 10). Delete it (recommended). The receipt does not name the earlier head, so the run cannot be pinned. If kept, the line must name that head.
3. Dates. Confirm 2026-10-09 for both Proof count lines. The receipts date the re-gate finish, not the count run itself.
4. SOLR-2681 test count wording (finding 5). Keep "36 tests" or use "36 test cases (31 test methods)".
5. Base links for "what happens today" (findings 6 and 8). Confirm that base-SHA links are allowed where the formula says head SHA.
6. Local branch refs (finding 14). Confirm the handoff doc is not in any outbound push and that the local refs stay unpushed.

## Not checked

- Gate logs (`g2681-*`, `g4540-*`), the recount log, and GitHub run 37622803991 are not on disk. The base failure at line 106 (SOLR-4540), the 36 case count (SOLR-2681), and the base failure count are not verified. No `gh` call was made.
- No builds, tests, or Gradle runs, as the claim requires.
- The observed failure message on base is not in either receipt, so no finding asks for one.
- The Lucene 9.12.3 bytecode claim (SOLR-2681 receipt line 4) was not checked. The draft does not use it.
- Ticket text was read from the local packets `research/jira-context/SOLR-4540.json` and `research/jira-context/SOLR-2681.json`, not from live JIRA.
- The base comparison uses merge base `cabedd1d968` against the local `upstream/main` ref (`8e62c26`). I did not fetch, so that local ref may be stale.
- Changelog YAML parsing and compilation were not checked.
- Non-stored term-vector fields under the FastVectorHighlighter skip were not analyzed. No test covers them.
- Line 106 is the `assertQ(` call at head. I did not confirm the base run reports the same line.
