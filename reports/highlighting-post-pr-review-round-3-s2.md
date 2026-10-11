# Highlighting post-PR review round 3, slice 2: SOLR-2681 (draft PR #5104)

Reviewer: windows review agent, slice 2 subagent. Read-only: git reads, one read-only fork fetch, and GitHub GET calls through the read-only wrapper. No builds, tests, Gradle, Selenium, gate or queue runs. No PR body edit, comment, review, close, submit-branch edit or Jira write. Nothing committed.

Inputs, all from origin/pr-prepare at 20529d6abf4 (the current tip; round 2 cited cc4be36): assignments/pool-highlighting-post-pr-review-round-3.md (slice 2 and Rules), pr-formula.md, reports/highlighting-post-pr-review-round-2.md and -s2.md, material/highlighting-round-1-answers.md, receipts/SOLR-2681.md, pr-drafts/highlighting/SOLR-2681.md, claims/query-parsing-round-1.md (shared rules). The ticket example comes from research/jira-context/SOLR-2681.json in the workspace.

## Verdict

**STILL OPEN, one item.** Every check passes except one word in the Limits line. Line 33 says "A follow-up submission is planned for the nested forms." The shared rules (claims/query-parsing-round-1.md, line 47) name "submission" as workspace vocabulary. Round 2 slice 2 recorded this as a non-blocking note. This round holds it under the shared rule. If the lead reads the rule as not covering this use, the item drops and the slice is SATISFIED.

Fix, in the live body and in pr-drafts/highlighting/SOLR-2681.md:
- Current: "A follow-up submission is planned for the nested forms."
- Fix: "A follow-up pull request is planned for the nested forms."

The answers record the planned follow-up sentence, not this word, so the change keeps the recorded decision.

## State checked

- `git ls-remote origin refs/heads/solr-2681-submit` returned a3b1ea7994d932cdf78667f799542d7d823f7d49. It equals the PR headRefOid. The read-only fork fetch succeeded.
- PR #5104: OPEN, draft, headRefName solr-2681-submit, mergeStateStatus UNSTABLE, reviewDecision empty.
- Branch shape: merge-base cabedd1d968059215188f4e7563fb303241899ed. Commits above it: aeaeb82b902 (code and test), 4cb25b1691b (changelog fragment), a3b1ea7994d (title only). 4cb25b1691b to a3b1ea7994d is one changelog line. The merge-base to head diff is three files, +34 -1.
- Worktree is detached at origin/pr-prepare 20529d6abf4.

## Item table

| # | Item | Live wording | Source check | Result |
|---|---|---|---|---|
| 1 | Round 2 item: changelog line is a link | Line 36: "Changelog: [changelog/unreleased/SOLR-2681-highlight-function-query.yml](https://github.com/nick-boss-tech/solr/blob/a3b1ea7994d932cdf78667f799542d7d823f7d49/changelog/unreleased/SOLR-2681-highlight-function-query.yml)" | The link targets the head SHA. `git ls-tree` at a3b1ea7994d lists changelog/unreleased/SOLR-2681-highlight-function-query.yml. Round 2 asked for exactly this link. | SATISFIED |
| 2 | Changelog title at head against the answers | Title at head: "The original highlighter now extracts terms from the query inside a top-level query(...) function query." Type fixed, author Nick Shanin, links entry with name and url. | Answers: the title "narrows to the top-level query(...) form the code handles." Head code (DefaultSolrHighlighter.java L300-L301) takes only a FunctionQuery whose value source is a QueryValueSource. The validator rules were read at head and the file passes them. The validator was not run. | SATISFIED |
| 3 | PR title | "SOLR-2681: The original highlighter now extracts terms from the query inside a top-level query(...) function query" | Matches the changelog title with the ticket prefix and the receipt's shipped title. The head code supports it. | SATISFIED |
| 4 | What happens today (symptom) | Bold summary. "lorem is not wrapped in `<em>` tags." Symptom link "base code" to cabedd1d. | Base file has no FunctionQuery or QueryValueSource text (grep, exit 1). Head test L108 expects `<em>lorem</em> ipsum`, so the symptom matches. | SATISFIED |
| 5 | What this change does | Bold summary. Code link. "All other queries take the same path as before." "The change is in the original highlighter's term extractor only." | Head L300-L303 is the new branch. Head L304-L309 (the other branches and super.extract) are unchanged. CustomSpanTermExtractor is used only by the QueryScorer in getSpanQueryScorer (L269). The FastVector path is separate. Among the code files, only DefaultSolrHighlighter.java changed. | SATISFIED |
| 6 | Proof counts and date | "On the base code, with only the new test added, HighlighterTest runs 36 tests and one fails: this test." and "At head `a3b1ea7994d`, HighlighterTest passes 36 of 36 (verified 2026-10-09)." | Receipt: 36 tests, exactly 1 failure on base with the branch test; 36 of 36 at the head; gate log dated 2026-10-09. Answers recount: 36 run, 0 failures, 2026-10-09. | SATISFIED |
| 7 | Test scope | "The test uses `hl.method=original` and covers only the top-level `query(...)` form." | Head test L99 uses `{!func}query($v1)` and L106-L107 use hl.method original. No other form is tested. | SATISFIED |
| 8 | Limits: nested form not handled | "The ticket's example, `{!func}product($v1,$v2)`, nests the query inside another function. The change does not reach that form, and no test covers it." | The packet's test query is `{!func}product($v1,$v2)`. Head L300-L301 accepts only a top-level QueryValueSource, so the product form is not reached. No test covers it. | SATISFIED |
| 9 | Limits: other sources and planned follow-up | "Other value sources that wrap a query are not handled. A follow-up submission is planned for the nested forms." | The only branch is the QueryValueSource one, so the first sentence is true. The answers record a planned follow-up sentence. The word "submission" is named in the shared vocabulary rule. | STILL OPEN: change "submission" to "pull request" (line 33 and the draft) |
| 10 | FastVector | "The FastVector highlighter is not changed and not tested." | Diff has no FastVector file. The test uses hl.method=original. | SATISFIED |
| 11 | Answers decisions | Count 36 after recount; title narrowed; "on request" replaced by a planned follow-up sentence; internal note removed. | Answers file, SOLR-2681 entries. The body and draft carry each decision. Item 9 keeps the decision. | SATISFIED |
| 12 | Each claim section opens with a bold one-line summary | Four sections (What happens today, What this change does, Proof, Limits) each open with bold. The "AI assistance" footer has no bold line. | pr-formula presentation rule covers claim sections. The footer is fixed template text with no claim. | SATISFIED |
| 13 | Citations are links | Four links (see citation table). The Jira line is a bare URL, which is the template's line. | Citation table. | SATISFIED |
| 14 | No internal vocabulary or run identifiers | Only "submission" (line 33). No gate, receipt, seed, log names, C0FFEE, premise, takeover, JUnit XML or round labels. | Grep of the body against the shared vocabulary list. | STILL OPEN (item 9) |
| 15 | No em dash or en dash | None in the body. | Grep for the UTF-8 bytes of U+2014 and U+2013: 0 matches. | SATISFIED |
| 16 | Lucene version mention | None in the body. | The rule is not triggered. | SATISFIED |
| 17 | Length | 2,830 bytes. The guide is about 3,500 characters. | pr-formula length guide. | SATISFIED |
| 18 | AI header, Jira link line, AI footer | Header on line 1. Jira link on line 3. Footer at the end. | pr-formula template. | SATISFIED |

## Citation table

| Body location | Link SHA | Kind | Lines | Check |
|---|---|---|---|---|
| What happens today, "[base code]" | cabedd1d968059215188f4e7563fb303241899ed (merge-base) | symptom | solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java L295-L298 | L295 method header, L298 first `if`. No FunctionQuery at the base. The link label says "base code", so the text marks it as pre-change. Accepted; see note below. SATISFIED |
| What this change does, "[code]" | a3b1ea7994d932cdf78667f799542d7d823f7d49 (head) | fix | same file, L300-L303 | L300-L301 the new condition, L302-L303 the extract on the wrapped query. SATISFIED |
| Proof, "[test]" | a3b1ea7994d932cdf78667f799542d7d823f7d49 (head) | fix (test) | solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java L91-L109 | L91 @Test, L92 method, L99 the query, L108 the expected highlight, L109 the closing brace. SATISFIED |
| Changelog line | a3b1ea7994d932cdf78667f799542d7d823f7d49 (head) | file | changelog/unreleased/SOLR-2681-highlight-function-query.yml | File exists at head with the title in item 2. SATISFIED |

Note on the symptom citation: the label says "base code" and the link is the merge-base, which is the pre-change code the change starts from. Under the pr-formula rule ("the text says so"), this is accepted. If the lead wants the explicit phrase "at the merge-base commit" that round 3 asks for on SOLR-4540, that is a wording change, not a link error.

## Proof and date check

| Live line | Receipt or answers | Result |
|---|---|---|
| "runs 36 tests and one fails: this test" (line 23) | Receipt Proof line: "HighlighterTest runs 36 tests with exactly 1 failure, the new testHighlightQueryInsideFunctionQuery." | Match |
| "passes 36 of 36 (verified 2026-10-09)" (line 24) | Receipt Counts line: 36 of 36 at the head. The receipt's Counts line has no date. The gate-log line names the title re-gate at a3b1, finished 2026-10-09. | Match |
| Recount date | Answers: recount at 4cb25b1691b, logged 2026-10-09, 36 run, 0 failures. 4cb25 to a3b1 is one changelog line, so the Java tree is the same. | Match |
| 31 declared, 36 run | The body does not state the 31 figure. | No conflict |
| Base SHA for the fail-before run | Receipt says "base production" and does not name the SHA. The body says "base code" and does not name it either. | Not a conflict; noted only |

## Body vs draft

- The live body (`gh pr view 5104 --json body`) and origin/pr-prepare:pr-drafts/highlighting/SOLR-2681.md are identical after removing the BOM and the CR bytes. Both are 2,830 bytes.
- My first save added one trailing newline. That came from my save step, not from the PR body. The JSON body ends with a single newline, the same as the draft.
- The item 9 fix must be applied to both.

## CI and review state (PR #5104, head a3b1ea7994d)

| Check | Source | State |
|---|---|---|
| labeler (Pull Request Labeler) | statusCheckRollup | COMPLETED, SUCCESS (run 38097777180) |
| Solr Tests via Crave | actions runs with head_sha | COMPLETED, action_required (run 38097777211). Not in the rollup. |
| Gradle Precommit | same | COMPLETED, action_required (run 38097777263). Not in the rollup. |
| Validate Changelog | same | COMPLETED, action_required (run 38097777293). Not in the rollup. |

- mergeStateStatus UNSTABLE. reviewDecision empty.
- Reviews: none. Issue comments: none. Inline review comments: none.
- action_required means the upstream checks have not run. That is a state, not a code finding. Validate Changelog has not run. A static read of the changelog against dev-tools/scripts/validate-changelog-yaml.py at head passes.

## Automated findings

- Verified: none. No review, comment or bot finding exists on the PR. The labeler output is a label, not a finding.
- Rejected: none.

## Not checked

- No builds, tests or gate runs. The changelog validator was read, not run.
- The PR base eaf9c86 was not re-checked in this round. The merge-base cabedd1d is the branch's ancestor shown by `git merge-base`.
