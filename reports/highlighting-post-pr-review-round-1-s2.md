# Highlighting post-PR review round 1, slice 2: SOLR-2681 (draft PR #5104)

Reviewer: windows review agent, slice 2. Read-only: GitHub read through the read-only wrapper, git read-only, one read-only fork fetch. No builds, tests, Gradle, gate or queue runs, no PR or Jira writes.

Inputs: assignments/pool-highlighting-post-pr-review-round-1.md, pr-formula.md, material/highlighting-round-1-answers.md, receipts/SOLR-2681.md, pr-drafts/highlighting/SOLR-2681.md (all read from origin/pr-prepare at bd6f4181994), the branch at a3b1ea7994d932cdf78667f799542d7d823f7d49, and the live PR 5104.

## Verdict

**STILL OPEN, one item.** The title, changelog title, Limits, citations, and summaries match the branch and the receipt. The body equals the draft. The one gap is that the Proof line has no verification date, which pr-formula section 3 requires.

Remaining item (apply to the live body and to pr-drafts/highlighting/SOLR-2681.md on origin/pr-prepare):

- Live wording: "At head `a3b1ea7994d`, HighlighterTest passes 36 of 36."
- Fix: "At head `a3b1ea7994d`, HighlighterTest passes 36 of 36 (verified 2026-10-09)."
- Basis: the receipt says the title re-gate at this head finished 2026-10-09, and the answers file records the recount as logged 2026-10-09. The count of 36 does not change.

## State checked

- Fork tip: `git ls-remote origin refs/heads/solr-2681-submit` returns a3b1ea7994d932cdf78667f799542d7d823f7d49. This equals the PR headRefOid. The read-only fetch succeeded and the commit is present locally.
- PR 5104: OPEN, draft (isDraft true), base main (baseRefOid eaf9c86369daebd82d1abe92e5af7b7200e40d2e, not in the local clone), mergeable MERGEABLE, mergeStateStatus UNSTABLE, reviewDecision empty.
- Branch: merge-base cabedd1d968059215188f4e7563fb303241899ed. Commits aeaeb82b902 (code and test), 4cb25b1691b (changelog), a3b1ea7994d (title narrowed). Diff from the merge-base: changelog file added (+7), DefaultSolrHighlighter.java (+7, -1), HighlighterTest.java (+20). The PR files endpoint shows the same three files and counts.

## Item table

| # | Item | Live wording | Source check | Result |
|---|---|---|---|---|
| 1 | Head and state | headRefOid a3b1ea7994d932cdf78667f799542d7d823f7d49; draft, OPEN | Equals fork tip from ls-remote | SATISFIED |
| 2a | PR title | "SOLR-2681: The original highlighter now extracts terms from the query inside a top-level query(...) function query" | Head code matches only a top-level FunctionQuery whose value source is QueryValueSource (DefaultSolrHighlighter.java L300-L301). Title claims only that form. | SATISFIED |
| 2b | Changelog title (changelog/unreleased/SOLR-2681-highlight-function-query.yml at head) | "The original highlighter now extracts terms from the query inside a top-level query(...) function query." | Matches the answers file (narrowed to the top-level form). The receipt quotes the same text without the final period; no claim differs. | SATISFIED |
| 3 | Body vs draft | Live body | Identical to pr-drafts/highlighting/SOLR-2681.md on origin/pr-prepare after CR strip (neither has CRs). No differences. | SATISFIED |
| 4a | Proof counts | "HighlighterTest runs 36 tests and one fails: this test." and "At head a3b1ea7994d, HighlighterTest passes 36 of 36." | Receipt: base with branch test, 36 runs, exactly 1 failure (the new test); 36 of 36 at head from fresh JUnit XML. Answers recount: 36 run at 4cb25b1691b. The two commits differ only in the one-line changelog YAML, so the Java tree is the same. | SATISFIED |
| 4b | Proof verification date | No date in the Proof section | pr-formula section 3: "with the verification date and head." Receipt re-gate finished 2026-10-09; recount logged 2026-10-09. | STILL OPEN (see fix above) |
| 5 | Limits: nested form | "Only a `query(...)` call at the top of a function query is handled." Bullets: the ticket example `{!func}product($v1,$v2)` nests the query and is not reached, no test covers it; other value sources that wrap a query are not handled; "A follow-up submission is planned for the nested forms."; FastVector not changed or tested. | Code at head matches only the top-level FunctionQuery/QueryValueSource case, so a nested query is not reached by the change. The ticket example in the local JIRA packet (research/jira-context/SOLR-2681.json) is `{!func}product($v1,$v2)` with v1 as the dismax query. The answers require a Limits line with a follow-up planned; present. | SATISFIED |
| 6 | Citations | Three blob links (see citation table) | Anchors checked with git show at each SHA | SATISFIED (changelog line is a NOTE) |
| 7 | Bold one-line summaries | Four content sections each open with one bold line. "### AI assistance" footer has none. | Footer is fixed template text, not a claim section | SATISFIED (NOTE 2) |
| 8 | Lucene version mention | None in the body | grep: no "Lucene" and no version numbers | SATISFIED (nothing to name; see NOTE 8) |
| 9 | Reviews, comments, bots, CI | No reviews, no issue comments, no inline comments; labeler SUCCESS; three workflow runs action_required | See CI section | SATISFIED for content; upstream CI has not run |
| 10 | Leaked vocabulary and seeds | None | grep for seed, C0FFEE, g2681, gate, receipt, ledger, rc=0, pre-fix, run ids: no hits | SATISFIED |

## Citation table

Base SHA cabedd1d968 is the merge-base of the branch and is the base that the PR diff matches (see NOTE 7). Head SHA is a3b1ea7994d.

| Body location | Link target | Commit | Anchor holds | Result |
|---|---|---|---|---|
| What happens today | solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L295-L298 | cabedd1d968 (base, symptom) | L295 `protected void extract(Query, float, Map)`; L298 `if (query instanceof ToParentBlockJoinQuery)`. The file has no FunctionQuery reference at base. | SATISFIED |
| What this change does | solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L300-L303 | a3b1ea7994d (head, fix) | L300-L301 FunctionQuery and QueryValueSource test; L302 cast; L303 `extract(source.getQuery(), boost, terms);` | SATISFIED |
| Proof | solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java#L91-L109 | a3b1ea7994d (head) | L91 `@Test`; L92-L108 `testHighlightQueryInsideFunctionQuery`; L109 closing brace | SATISFIED |
| Changelog line | changelog/unreleased/SOLR-2681-highlight-function-query.yml (bare backticks, no link) | head | File exists and is added by the PR | NOTE 1 |

All three links use the form github.com/nick-boss-tech/solr/blob/<sha>/<path>#L<a>-L<b>. The Jira link is a full URL. There are no bare #NNNN references.

## Proof check

| Body claim | Receipt or answers source | Result |
|---|---|---|
| Base with only the new test: HighlighterTest runs 36 tests, one fails (this test) | Receipt: "on base production with the branch test in place, HighlighterTest runs 36 tests with exactly 1 failure, the new testHighlightQueryInsideFunctionQuery" | Match |
| At head a3b1ea7994d: HighlighterTest passes 36 of 36 | Receipt: "36 of 36 at the head, from fresh JUnit XML"; answers recount: 36 run, 0 skipped, 0 failures (log g2681-recount.log, at 4cb25b1691b) | Match. 4cb25 to a3b1 changes only the changelog YAML, so the count holds for the head. |
| Declared test methods | Not in body. Grep at head: 31 `@Test`; at base: 30. Answers say the same. | Consistent with 36 runs |
| Verification date | Not in body | STILL OPEN (4b) |
| No seeds, run ids, internal vocabulary | Receipt seed 2681C0FFEE2681 and log names absent from body | Clean |

Test class and count in the body are the only test numbers. No other class is cited.

## Body vs draft

No differences. The live body (read from the PR JSON, written without added newlines) has the same text as pr-drafts/highlighting/SOLR-2681.md on origin/pr-prepare; a diff of the two, with no CRs in either, is clean. Both carry the same Proof line, so the STILL OPEN date fix applies to both.

## CI and review state

- statusCheckRollup: `labeler` (Pull Request Labeler, pull_request_target): COMPLETED, SUCCESS. Labeler applied `tests` and `cat:search`.
- Workflow runs for head a3b1ea7994d (pull_request event), not in the rollup:
  - Solr Tests via Crave: COMPLETED, action_required (not run; awaiting approval).
  - Gradle Precommit: COMPLETED, action_required (not run).
  - Validate Changelog: COMPLETED, action_required (not run).
  These are not failures and not passes. Upstream validation has not happened.
- Reviews: none. Issue comments: none. Inline review comments: none. reviewDecision: empty.
- Validate Changelog, when it runs, checks only that the diff adds a file under changelog/unreleased/. The added file satisfies that.

## Verified and rejected automated findings

None present. The PR has no bot comments and no review threads. The labeler's labels are not findings. Nothing was verified and nothing was rejected.

## Notes (not blocking)

1. The changelog line in the body is bare text. The presentation rule in pr-formula asks that file citations be links. The approved template line predates the rule, so this is the main side's call. Optional link: https://github.com/nick-boss-tech/solr/blob/a3b1ea7994d932cdf78667f799542d7d823f7d49/changelog/unreleased/SOLR-2681-highlight-function-query.yml
2. The "### AI assistance" footer has no bold summary. It is the fixed footer text, so it is not treated as a claim section.
3. Limits names the FastVector highlighter but not hl.method=unified. The body does say the change is in the original highlighter's term extractor only. The head has no FunctionQuery handling in the highlight package outside DefaultSolrHighlighter, so the unified path is not changed. Whether unified has the same gap was not tested. Optional line: "The unified highlighter is not changed and not tested." The answers do not require it.
4. The changelog YAML title ends with a period; the receipt's quoted title does not. No claim differs.
5. The recount is logged at 4cb25b1691b and the body names head a3b1ea7994d. The Java tree is the same at both commits (only the changelog YAML line differs), so the number stands.
6. gates/SOLR-2681.md does not exist on origin/pr-prepare; the gates directory has no SOLR-2681 file. The receipt is the only source, as the assignment says. Gate logs were not readable from this checkout.
7. The PR base eaf9c86 is not in the local clone, so its ancestry against cabedd was not checked directly. The PR files endpoint lists three files with the same line counts as the diff from cabedd, so the base lines cited match the PR base.
8. The body names no Lucene version. If a Lucene claim is added to the symptom text, name the 9.x and 10.x checks together, per the "Lucene version claims" rule in claims/query-parsing-round-1.md. The receipt records 9.12.3 and 10.4.0 bytecode checks; those were not re-verified here.

## Not checked

No builds or tests, no gate or queue runs. The receipt's Lucene bytecode checks and the gate logs were not re-run or read. The ticket example comes from the local JIRA packet, not a live Jira read.
