# Highlighting post-PR review round 2, slice 2: SOLR-2681 (draft PR #5104)

Reviewer: windows review agent, slice 2 subagent. Read-only: GitHub reads through the read-only wrapper (pr view and GET api calls), git reads, and one read-only fork fetch. No builds, tests, Gradle, Selenium, gate or queue runs. No PR body edit, comment, review, close, submit-branch edit or Jira write.

Inputs: assignments/pool-highlighting-post-pr-review-round-2.md, pr-formula.md, material/highlighting-round-1-answers.md, reports/highlighting-post-pr-review-round-1.md and reports/highlighting-post-pr-review-round-1-s2.md, claims/query-parsing-round-1.md (shared rules), receipts/SOLR-2681.md and pr-drafts/highlighting/SOLR-2681.md. All of these are from origin/pr-prepare at cc4be36156d. The branch is at a3b1ea7994d932cdf78667f799542d7d823f7d49. The ticket example comes from the local JIRA packet, research/jira-context/SOLR-2681.json.

## Verdict

**STILL OPEN, one item.** The round 1 date item is fixed in the live body and in the draft. The changelog title, the nested-form Limits line, the code and test citations, and the body-vs-draft match all hold. One file citation is still bare text.

Remaining item:

- Line: the Changelog line near the end of the live body (line 36): "Changelog: `changelog/unreleased/SOLR-2681-highlight-function-query.yml`".
- Fact: this file citation is not a link. pr-formula.md, presentation rule (2026-10-08): "link each file citation to the blob at the PR head SHA". The query-parsing claim (2026-10-09) also says the Changelog line carries a link. The approved template line (2026-10-04) predates that rule.
- Fix, to apply in the live body and in pr-drafts/highlighting/SOLR-2681.md: replace line 36 with `Changelog: [`changelog/unreleased/SOLR-2681-highlight-function-query.yml`](https://github.com/nick-boss-tech/solr/blob/a3b1ea7994d932cdf78667f799542d7d823f7d49/changelog/unreleased/SOLR-2681-highlight-function-query.yml)`.

## State checked

- Fork tip: `git ls-remote origin refs/heads/solr-2681-submit` returned a3b1ea7994d932cdf78667f799542d7d823f7d49. This equals the PR headRefOid. The read-only fetch succeeded and the commit is present locally.
- PR 5104: OPEN, isDraft true, headRefName solr-2681-submit, mergeStateStatus UNSTABLE, reviewDecision empty.
- Branch shape: merge-base cabedd1d968059215188f4e7563fb303241899ed. The PR base eaf9c86 is not in the local clone. Three commits sit above the merge-base: aeaeb82b902 (code and test), 4cb25b1691b (changelog fragment), and a3b1ea7994d (title only). The diff from 4cb25b1691b to a3b1ea7994d is one changelog line, so the Java tree is the same at both commits. The diff from the merge-base is three files, +34 -1, which matches the PR files endpoint.

## Item table

| # | Item | Live wording | Source check | Result |
|---|---|---|---|---|
| 1 | Round 1 item: verification date on the Proof line | Line 24: "- At head `a3b1ea7994d`, HighlighterTest passes 36 of 36 (verified 2026-10-09)." | Receipt date line: "g2681-title-gate.log (re-gate at the title head, finished 2026-10-09 after one VM-replacement relaunch)". The date matches. The receipt's Counts line (36 of 36 at the head) is the title re-gate run. | SATISFIED |
| 2 | Changelog title claims only the top-level form | Changelog file at head: `title: "The original highlighter now extracts terms from the query inside a top-level query(...) function query."` | Head code (DefaultSolrHighlighter.java L300-L301) accepts only a FunctionQuery whose value source is QueryValueSource. Answers record the narrowing. | SATISFIED |
| 3 | PR title | "SOLR-2681: The original highlighter now extracts terms from the query inside a top-level query(...) function query" | Matches the code and the changelog title. | SATISFIED |
| 4 | Limits: nested query(...) form, follow-up planned | Lines 28-34: "The ticket's example, `{!func}product($v1,$v2)`, nests the query inside another function. The change does not reach that form, and no test covers it." Line 33: "A follow-up submission is planned for the nested forms." | Head code has no nested handling (L300-L303 only). The head test (HighlighterTest.java L91-L109) uses `{!func}query($v1)` only. The ticket example in research/jira-context/SOLR-2681.json is `{!func}product($v1,$v2)` with v1 = `{!dismax qf=t_text}lorem`, so the query sits inside product. The answers require a Limits line with a planned follow-up, and the line is present. | SATISFIED |
| 5 | Answers: "on request" replaced by the planned sentence; internal note removed | Line 33 has the planned sentence. No internal note in the body. | material/highlighting-round-1-answers.md, SOLR-2681 Limits bullet. | SATISFIED |
| 6 | Answers: count and recount | "36 of 36" (line 24); "36 tests and one fails" (line 23) | Answers: recount at 4cb25b1691b logged 2026-10-09, 36 run, 0 failures. The 4cb25 to a3b1 diff is the changelog line only. | SATISFIED |
| 7 | Changelog line is a link | Line 36: bare backticks, no link | pr-formula presentation rule: "link each file citation to the blob at the PR head SHA". | STILL OPEN (fix above) |
| 8 | Each section opens with a bold one-line summary | Bold lines at 7 (What happens today), 13 (What this change does), 21 (Proof), 30 (Limits). The "### AI assistance" footer has no bold line; it is fixed template text. | pr-formula presentation rule. | SATISFIED |
| 9 | Claim not restated in the body | Line 17 ("Behavior change: ...") restates the summary of section 2 in concrete terms. pr-formula section 2 requires behavior changes to be stated, so the line stays. | pr-formula section 2. | SATISFIED |
| 10 | Choice section | None in the body. The answers record none, and pr-formula says a narrow-scope call is not a choice. | pr-formula section 4. | SATISFIED (none needed) |
| 11 | Lucene version mention | None in the body (no "Lucene", no version numbers). | claims/query-parsing-round-1.md, "Lucene version claims". | SATISFIED (rule not triggered) |
| 12 | No internal vocabulary or run identifiers | Grep for gate, receipt, ledger, handoff, seed, C0FFEE, g2681, rc=0, pre-fix, JUnit, premise, takeover, audit: no hits. "submission" on line 33 is the only term the vocabulary rule covers (see note 1). | claims/query-parsing-round-1.md, shared rules. | NOTE (note 1) |
| 13 | Dashes | No em dash or en dash in the body. | claims/query-parsing-round-1.md, shared rules. | SATISFIED |
| 14 | Length | 2,684 characters (guide: about 3,500). | pr-formula length guide. | SATISFIED |
| 15 | AI header, Jira link line, AI footer | Header on line 1; Jira link line 3; footer at the end. | pr-formula template. | SATISFIED |

## Citation table

Base SHA cabedd1d968 is the merge-base. The PR files endpoint hunk header for DefaultSolrHighlighter.java is "@@ -295,7 +297,11 @@", which matches the base lines cited.

| Body location | Link target | Commit | Anchor check (git show, nl -ba) | Result |
|---|---|---|---|---|
| What happens today (line 9 link, label "base code") | solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L295-L298 | cabedd1d968 (merge-base, symptom) | L295 `protected void extract(Query, float, Map)`; L296 `throws IOException {`; L297 comment; L298 `if (query instanceof ToParentBlockJoinQuery) {`. `grep FunctionQuery` on the base file returns nothing (exit 1). | SATISFIED (labeled as base) |
| What this change does (line 15 link, label "code") | solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L300-L303 | a3b1ea7994d (head, fix) | L300 `if (query instanceof FunctionQuery`; L301 `&& ((FunctionQuery) query).getValueSource() instanceof QueryValueSource) {`; L302 `QueryValueSource source = ...`; L303 `extract(source.getQuery(), boost, terms);` | SATISFIED |
| Proof (line 23 link, label "test") | solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java#L91-L109 | a3b1ea7994d (head) | L91 `@Test`; L92 `public void testHighlightQueryInsideFunctionQuery() {`; L109 closing brace. | SATISFIED |
| Changelog line (line 36) | changelog/unreleased/SOLR-2681-highlight-function-query.yml, bare backticks | head | The file exists at the head and the PR adds it. | STILL OPEN (fix above) |

All three links use the form github.com/nick-boss-tech/solr/blob/<sha>/<path>#L<a>-L<b>. The Jira link is a full URL. There are no bare #NNNN references.

## Proof and date check

- Live Proof line (line 24): "- At head `a3b1ea7994d`, HighlighterTest passes 36 of 36 (verified 2026-10-09)."
- Receipt date line (receipts/SOLR-2681.md on origin/pr-prepare at cc4be36): "- Gate logs: g2681-gate.log (original gate at 4cb25b1691b) and g2681-title-gate.log (re-gate at the title head, finished 2026-10-09 after one VM-replacement relaunch)." The Cross-version line also says "check recorded 2026-10-09". The dates agree.
- Head count: the receipt's Counts line says "HighlighterTest 36 of 36 at the head, from fresh JUnit XML". That line has no date of its own. Its date comes from the title re-gate line, which names the title head a3b1ea7. The answers recount (logged 2026-10-09 at 4cb25b1691b) gives the same 36 on the same Java tree.
- Base count (line 23): "HighlighterTest runs 36 tests and one fails: this test." The receipt's Proof line says the same: "HighlighterTest runs 36 tests with exactly 1 failure, the new testHighlightQueryInsideFunctionQuery".
- Every number in the body is in the receipt or the answers. The verification date and the head are both present.

## Body vs draft

- Live body (PR JSON, saved to the scratchpad) and pr-drafts/highlighting/SOLR-2681.md on origin/pr-prepare: both 2,684 bytes after CR strip. Neither has CR bytes. `diff` is empty. No differences.
- The fix for item 7 must go to both.

## CI and review state

- statusCheckRollup on PR 5104: one check, `labeler` (Pull Request Labeler, pull_request_target), COMPLETED, SUCCESS (run 38097777180).
- Workflow runs for head a3b1ea7994d (actions/runs?head_sha, event pull_request), all COMPLETED with conclusion action_required, so none has run:
  - Solr Tests via Crave (run 38097777211)
  - Gradle Precommit (run 38097777263)
  - Validate Changelog (run 38097777293)
- The check-runs endpoint at the head lists only `labeler` (success). The three action_required runs do not appear in statusCheckRollup.
- mergeStateStatus: UNSTABLE. reviewDecision: empty.
- Reviews: none (repos/apache/solr/pulls/5104/reviews returned []). Issue comments: none. Inline review comments: none.

## Automated findings

- Verified: none.
- Rejected: none.
- No bot comments, review threads or automated review findings exist on PR 5104. The labeler's labels are not findings.
- Validate Changelog has not run. Read statically against dev-tools/scripts/validate-changelog-yaml.py at the head: the title is non-empty, type `fixed` is valid, the author has a name, and the link has name and url. The file meets those rules. The validator was not run.

## Notes (not blocking)

1. Line 33: "A follow-up submission is planned for the nested forms." The claims vocabulary rule bans "submission" as a workspace word. In a public PR it reads as an ordinary noun, so it is not a STILL OPEN item. Optional wording: "A follow-up pull request is planned for the nested forms." If changed, change the draft too.
2. The receipt's head count has no date line of its own. The dated title re-gate line supplies the date. This is consistent and is not a blocker.
3. The changelog title ends with a period. The validator has no rule about that. No claim differs.
4. The body names no Lucene version. The receipt's Lucene 9.12.3 and 10.4.0 checks were not re-run and are not needed for the body.
5. The "base code" link points at the merge-base cabedd1d, not the PR base eaf9c86. pr-formula and the assignment allow a merge-base symptom citation, and the hunk header matches.

## Not checked

- No builds, tests or gate runs. The gate logs named in the receipt are not on disk (no gates/SOLR-2681.md on origin/pr-prepare). The receipt is the only Proof source, as the assignment says.
- The PR base eaf9c86 is not in the local clone, so its ancestry against cabedd1d was not checked directly.
- The Validate Changelog validator was read, not run.
- The live PR was read through the read-only wrapper and GET api calls only. No write call was made.
