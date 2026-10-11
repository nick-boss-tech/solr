# Highlighting post-PR review round 4, slice 2: SOLR-2681 (draft PR #5104)

Assignment: `assignments/pool-highlighting-post-pr-review-round-4.md`, slice 2. Reviewer: windows review agent, slice 2 subagent. Lead: the windows review agent.

Read-only throughout. Commands: git reads on `origin/pr-prepare` and the head commit, one read-only fork fetch of `refs/heads/solr-2681-submit` into a remote-tracking ref, and GitHub GET calls through `research/gh.ps1` (pr view, pulls reviews and comments, issue comments, commits check-runs, actions runs). No PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle, test, Selenium, gate or test-queue run. Nothing committed. Scratch copies of the live body and the draft were written to the session scratchpad only.

## Verdict

**SATISFIED.** The round 3 item (Limits line 33, "submission") is fixed in the live body and in the main-side draft. Every other check passes. The notes at the end are not blocking.

## State checked

- `git ls-remote origin refs/heads/solr-2681-submit` returned `a3b1ea7994d932cdf78667f799542d7d823f7d49`. It equals the live PR `headRefOid`. The read-only fork fetch succeeded.
- The assignment text gives the slice 2 head as `a3b1ea7994d932cdf78667f799542d7d49`, which is 34 characters and is a typo. The full 40-character head above is used throughout.
- `origin/pr-prepare` was read at `58b4e419e1f`, the current tip. Round 3 cited `20529d6abf4`. The inputs were read from this tip.
- PR #5104: OPEN, draft, headRefName `solr-2681-submit`, mergeStateStatus `UNSTABLE`, reviewDecision empty.

## Item table

| # | Item | Live wording | Source check | Result |
|---|---|---|---|---|
| 1 | Limits line on the nested form (round 3 item) | Line 33: "- Other value sources that wrap a query are not handled. A follow-up pull request is planned for the nested forms." | Case-insensitive search of the live body and of `pr-drafts/highlighting/SOLR-2681.md` for submission, gate, receipt, ledger, handoff, takeover, audit, JUnit, premise, seed, round, workspace, claim: no hits. The draft line is identical. Answers: "planned follow-up" wording recorded. | SATISFIED |
| 2 | Limits is true of the head | Line 32: "- The ticket's example, `{!func}product($v1,$v2)`, nests the query inside another function. The change does not reach that form, and no test covers it." | Head `DefaultSolrHighlighter.java` L300-L301 accept only a `FunctionQuery` whose value source is a `QueryValueSource`. The `product(...)` form has a different value source, so it is not reached. Head test L99 uses only `{!func}query($v1)`. The example matches `research/jira-context/SOLR-2681.json` line 4 verbatim. Answers: a Limits line with a planned follow-up. | SATISFIED |
| 3 | Changelog line is a link at the head | Line 36: "Changelog: [changelog/unreleased/SOLR-2681-highlight-function-query.yml](https://github.com/nick-boss-tech/solr/blob/a3b1ea7994d932cdf78667f799542d7d823f7d49/changelog/unreleased/SOLR-2681-highlight-function-query.yml)" | `git ls-tree` at the head lists the file. Title at head: "The original highlighter now extracts terms from the query inside a top-level query(...) function query." It claims only the top-level form. Type `fixed`, author Nick Shanin, `links` with name and url. Static read against `dev-tools/scripts/validate-changelog-yaml.py` at the head: each rule passes (validator not run). Suffixed names such as this one are common in `changelog/unreleased` at the head (117 files with `SOLR-NNNN-` prefixes). | SATISFIED |
| 4a | Fix citations are head links with the claim lines | See citation table | `git show` and `sed -n` at the head: L300-L303 and HighlighterTest L91-L109 contain the claim. | SATISFIED |
| 4b | Symptom citation is at the merge-base and the text says so | Line 9: "([base code](...cabedd1d968059215188f4e7563fb303241899ed/.../DefaultSolrHighlighter.java#L295-L298))" | Merge-base is `cabedd1d968059215188f4e7563fb303241899ed`, an ancestor of the head. The label "base code" says so. Base file has no `FunctionQuery` or `QueryValueSource` text (grep). See note N1 on the line range. | SATISFIED |
| 4c | Title is accurate | PR title: "SOLR-2681: The original highlighter now extracts terms from the query inside a top-level query(...) function query" | Matches the receipt's shipped title (receipt has the same text with a final period) and the changelog title. The head code supports it. | SATISFIED |
| 4d | Proof numbers and date | Line 23: "runs 36 tests and one fails: this test." Line 24: "passes 36 of 36 (verified 2026-10-09)." | See Proof and date check. | SATISFIED |
| 4e | Each section opens with a bold one-line summary | Lines 7, 13, 21 and 30 are bold one-line summaries. The AI footer heading has no claim and is template text. | `pr-formula.md` presentation rule. | SATISFIED |
| 4f | Answers decisions reflected | Count 36 (recount logged 2026-10-09); title narrowed to the top-level form (lines 13, 30, 36); nested form in Limits with a planned follow-up (line 33); internal note absent. | `material/highlighting-round-1-answers.md`, SOLR-2681 entries. | SATISFIED |
| 4g | Lucene version mention | None in the body. | Rule not triggered. | SATISFIED |
| 4h | No internal vocabulary or run identifiers | None. Only "verified 2026-10-09" (date wording from the template) and the word "runs" in its ordinary sense. No seed, log name, round label, gate or receipt word. | Grep as in item 1, plus process words (log, run, tidy, error prone, preflight, pre-fix, lead, slice, queue, drain, pool, subagent). | SATISFIED |
| 4i | No em dash or en dash | 0 in the live body and 0 in the draft. | Byte grep for U+2014 and U+2013. | SATISFIED |
| 4j | Length | 2,832 bytes (round 3 read 2,830; the +2 is "pull request" in place of "submission"). The guide is about 3,500 characters. | Byte count of the draft and the normalized live body. | SATISFIED |
| 4k | Template header, Jira line, changelog line, footer | Line 1 AI header, line 3 Jira link, line 36 changelog link, footer at the end. | `pr-formula.md` template. | SATISFIED |
| 5 | Round 3 item: "submission" to "pull request" | Line 33 now reads "A follow-up pull request is planned for the nested forms." | Same as item 1. | SATISFIED |
| 6 | Body against the main-side draft | Identical. See body-versus-draft result. | Diff after CR and BOM strip. | SATISFIED |
| 7 | Reviewers, automated comments and CI | No reviews, no issue comments, no inline comments. Labeler SUCCESS. Three upstream runs at `action_required`. | See CI and review state. | SATISFIED (state recorded; upstream checks not run) |

## Citation table

| Body location | SHA used | Kind | Lines | Check |
|---|---|---|---|---|
| Line 9, "base code" (What happens today) | `cabedd1d968059215188f4e7563fb303241899ed` (merge-base) | symptom | `solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java` L295-L298 | L295 is the `extract` method header. L298 is the first branch, `if (query instanceof ToParentBlockJoinQuery)`. The full base chain is L295-L303, and it has no `FunctionQuery` branch (grep of the base file finds no `FunctionQuery` or `QueryValueSource`). The label says "base code". SATISFIED. See N1. |
| Line 15, "code" (What this change does) | `a3b1ea7994d932cdf78667f799542d7d823f7d49` (head) | fix | same file, L300-L303 | L300-L301: `if (query instanceof FunctionQuery && ((FunctionQuery) query).getValueSource() instanceof QueryValueSource) {`. L302-L303: unwrap the source and extract the wrapped query. The lines contain the claim. SATISFIED. |
| Line 23, "test" (Proof) | `a3b1ea7994d932cdf78667f799542d7d823f7d49` (head) | fix (test) | `solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java` L91-L109 | L91 `@Test`, L92 method, L99 `{!func}query($v1)`, L106-L107 `hl.method` `original`, L108 expected `<em>lorem</em> ipsum`, L109 closing brace. SATISFIED. |
| Line 36, "Changelog" | `a3b1ea7994d932cdf78667f799542d7d823f7d49` (head) | fix (file) | `changelog/unreleased/SOLR-2681-highlight-function-query.yml` | File exists at the head (`git ls-tree`). Title as in item 3. SATISFIED. |
| Line 3, Jira URL | none | template line | not a citation | Bare URL, as the template specifies. |

## Proof and date check

| Live line | Receipt or answers | Result |
|---|---|---|
| Line 23: "runs 36 tests and one fails: this test" | Receipt Proof line: "HighlighterTest runs 36 tests with exactly 1 failure, the new testHighlightQueryInsideFunctionQuery." | Match |
| Line 24: "passes 36 of 36 (verified 2026-10-09)" | Receipt Counts line: "HighlighterTest 36 of 36 at the head, from fresh JUnit XML." The Counts line has no date. The receipt's title re-gate at `a3b1ea7994d` finished 2026-10-09. | Match on count. Date matches the re-gate date. |
| Answers recount | Answers: HighlighterTest at `4cb25b1691b` ran 36, 0 failures, logged 2026-10-09. The title commit changes only the changelog, so the Java tree is the same. | Match |
| Test declarations | `@Test` annotations: 31 at the head, 30 at the base (grep). The body does not state 31. The receipt says the class declares 31 and executes 36. | No conflict in the body. See N3. |

## Body vs draft

- The live body (`gh pr view 5104 --json body`) and `origin/pr-prepare:pr-drafts/highlighting/SOLR-2681.md` have no differences after the CR and BOM strip. The draft has no CR bytes. Both are 2,832 bytes.
- My save of the live body added one trailing newline through the `--jq` output step. The API body ends with one newline, the same as the draft. That is a save artifact, not a PR difference.

## CI and review state (PR #5104, head `a3b1ea7994d932cdf78667f799542d7d823f7d49`)

| Check | Source | State |
|---|---|---|
| labeler (Pull Request Labeler), run 38097777180 | statusCheckRollup and commits check-runs | COMPLETED, SUCCESS |
| Solr Tests via Crave, run 38097777211 | actions/runs with head_sha | COMPLETED, action_required (not in the rollup) |
| Gradle Precommit, run 38097777263 | same | COMPLETED, action_required (not in the rollup) |
| Validate Changelog, run 38097777293 | same | COMPLETED, action_required (not in the rollup) |

- mergeStateStatus `UNSTABLE`. reviewDecision empty.
- Reviews (`repos/apache/solr/pulls/5104/reviews`): none. Issue comments: none. Inline review comments: none.
- `action_required` is a state: the upstream checks have not run on this head. It is not a code finding.

## Automated findings

- Verified: none. No review, comment or bot finding exists on the PR. The labeler result is a label action, not a finding.
- The changelog validator was read statically at the head, not run. Against `dev-tools/scripts/validate-changelog-yaml.py`, the file passes each rule: title is a non-empty string, type `fixed` is valid, the author has a name, the links entry has name and url, and the template comment text is absent. This is a prediction for the Validate Changelog run, which has not run.
- Rejected: none.

## Notes (not blocking)

- N1. The symptom link L295-L298 shows the method header and the first branch only. The claim "no case for FunctionQuery" is visible across base L295-L303. Widening the range to L295-L303 in line 9 and in the draft would show the whole chain. Optional.
- N2. The symptom wording "lorem is not wrapped in `<em>` tags" is not backed by a recorded base output string. The receipt records one failure on base. The statement follows from the base code, which extracts no terms for a `FunctionQuery` (base L295-L303). Optional: no change needed.
- N3. The body says HighlighterTest runs 36 tests. Head has 31 `@Test` annotations. The receipt explains the difference as a JUnit XML execution count. A reviewer who counts annotations will see 31. The body states the run count, so no change is needed.

## Not checked

- No builds, tests, Gradle or gate runs. The changelog validator was read, not run.
- The ticket example was compared as text. Its parse behavior in Solr was not run.
- The base run's failure output was not re-observed. The receipt records the failure count only.
