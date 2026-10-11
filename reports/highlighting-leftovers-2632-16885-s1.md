# Highlighting leftovers, SOLR-2632 part report (s1)

Draft written: `pr-drafts/highlighting/SOLR-2632.md` (test-only pin, option (a) of the recorded dispositions). Worktree `wt/pr-prepare-suggester`, detached at e84522fa5bc. Nothing committed, pushed, built, or run. No GitHub or Jira writes. Only the two files named in the assignment were written. The claim file and WORKFLOW.md mark-done step were not touched, because this run was limited to those two files.

## 1. Head check

- `git ls-remote origin refs/heads/solr-2632-submit` = `1d7018f3fd7b94ad1a9858df8eb1af4843107bf8`. Matches the receipt and the assignment. Proceeded; no HOLD on the head.

## 2. Receipt numbers used (`receipts/SOLR-2632.md`)

- Line 4: branch `solr-2632-submit` at `1d7018f3fd7b94ad1a9858df8eb1af4843107bf8`. Used as the head.
- Line 5: base production with only the branch test file, the new test passes 1/1. Used as "1/1 on base with only this test file", verified 2026-10-06.
- Line 5: at the received head, 1/1. Used as "1/1 at head", verified 2026-10-06.
- Line 5: scratch unified-method leg, 1/1 on base. Used as "1/1 on base in a scratch copy on 2026-10-06". The unified leg is not in the committed test.
- Line 6: "present in Lucene 9.12.3 bytecode as well as 10.4.0". Not used for 9.12.3 (see finding 4). The 10.4.0 part is used.
- Line 3: date 2026-10-06 (stop date). Not otherwise used.
- Line 5 seed and log names: not used. They are internal identifiers.

## 3. Findings the assignment did not anticipate

1. **Changelog fragment name and title.** `changelog/unreleased/SOLR-2632.yml` does not exist at `1d7018f3fd7b` (`git show` fails). The fragment is `changelog/unreleased/SOLR-2632-highlight-boost-wrapper.yml`. Its title: "Highlighting now sees through the {!boost} query parser's FunctionScoreQuery when it is nested in another query (original and fastVector methods)." That title is NOT accurate for a test-only change. It claims a behavior change that the premise runs show does not happen. The draft does not use it. The changelog line says "none". This departs from step 6's changelog link: linking the fragment would publish a false claim, and option (a) drops the fragment. If the owner wants the link anyway, it is `https://github.com/nick-boss-tech/solr/blob/1d7018f3fd7b94ad1a9858df8eb1af4843107bf8/changelog/unreleased/SOLR-2632-highlight-boost-wrapper.yml`.
   Proposed PR title (not in the body): `SOLR-2632: Add a regression test for highlighting a {!boost}-wrapped query`.
2. **Upstream main has moved.** The assignment expected `3f5d4c5bf8ac`. Live `ls-remote upstream refs/heads/main` = `d76fc14b9857e77b830d7a82514b28c523a239c2`. The worktree's local `upstream/main` ref is the stale `3f5d4c5bf8ac`, and `d76fc14b` is not in the local object store (not fetched; no fetch run). A read-only GitHub compare (`repos/apache/solr/compare/cabedd1d968...d76fc14b`) gives merge base `cabedd1d968059215188f4e7563fb303241899ed`, behind_by 0, ahead_by 43. Local `merge-base 1d7018f3fd7b 3f5d4c5bf8ac` also gives `cabedd1d968`. So the merge base used for the base-code link is `cabedd1d968`.
3. **The head still carries the wrong content for option (a).** The head `1d7018f3fd7b` has: the two production hunks in `DefaultSolrHighlighter.java`, the changelog fragment, the root `SOLR-2632-TESTING.md` (stale, must not ship), and a two-leg test (original, fastVector). Option (a) drops the production hunks, the changelog, and the root note, and adds the unified leg. So every head-SHA link in the draft points at the received head, not at the final PR head. All head links must be re-pointed after the pin commit is made. The draft does not flag this itself; the owner or main side must re-point the links when the commit exists.
4. **Lucene 9.12.3 point is disputed.** The receipt says the highlighter case is "present in Lucene 9.12.3 bytecode". `reports/lucene-version-claims.md` (item 4, 2026-10-09) says the usage is in `lucene-highlighter`, which exists only at 10.4.0 in the local cache, so the 9.12.3 claim cannot be checked. The draft states the 10.4.0 result only, and the Limits say the 9.12.3 highlighter code was not checked. The owner should confirm this wording.
5. **The ticket's dismax scenario is not shown.** The receipt says "the ticket's 2011 scenario works under all three methods on current main". The committed test uses the standard parser (`+id:1 +_query_:"{!boost b=3 v=$qq}"`), not dismax. The receipt does not say the scratch legs used dismax. The draft does not claim dismax coverage; Limits says the recorded checks do not show it. The draft also says "the base code" instead of "current main", because the runs used base production, and live main was not run.
6. **Option (a) and the template's changelog line conflict.** Covered in finding 1.

## 4. Citations and checks

Each was read at the stated SHA with `git show <sha>:<path>` into the scratchpad, then read with line numbers.

| Draft link | SHA | Lines | Check |
|---|---|---|---|
| `solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java` | 1d7018f3fd7b | L1423-L1447 | L1422 `@Test`, L1423 `public void testHighlightQueryWrappedInBoost() {`, L1447 closing `}`, L1449 `getHighlighter()`. Confirmed. |
| `solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java` | 1d7018f3fd7b | L303-L305 | L303 `} else if (query instanceof FunctionScoreQuery) {`, L304 comment, L305 `extract(...getWrappedQuery()...)`. Confirmed. |
| same file | 1d7018f3fd7b | L566-L571 | L566 `} else if (sourceQuery instanceof FunctionScoreQuery) {`, L567-L571 the `flatten(...)` call. Confirmed. |
| same file | cabedd1d968 (merge base, labelled as such in the draft) | L298-L303 | L298 `if (query instanceof ToParentBlockJoinQuery)`, L302 `} else {`, L303 `super.extract(query, boost, terms);`. No `FunctionScoreQuery` branch. Confirmed. |
| changelog | (not linked) | | See finding 1. |
| Jira | (plain URL) | | `https://issues.apache.org/jira/browse/SOLR-2632`, as the template. |

Version pins: `gradle/libs.versions.toml` at `cabedd1d968` and at `1d7018f3fd7b` both read `apache-lucene = "10.4.0"`. The branch_10x and branch_9x pins (10.4.0 and 9.12.3) come from the assignment and were not re-read.

## 5. Choice section: omitted

Decision: no "A choice to check" section.

Reason: pr-formula.md section 4 needs a route a maintainer would plausibly pick instead. The code has none here:
- The production hunks are inert on Lucene 10.4.0, so no maintainer would pick them to change behavior.
- The other options on record (a Jira comment with no PR, or banking the branch) are the owner's public-posture decisions, not maintainer design choices.
- "Test-only pin versus the broader fix" is scope, which the formula says is not a choice on a small patch.

The draft instead says plainly in its Limits that no defect is claimed and nothing is fixed.

## 6. Not verified

- The live ticket text for SOLR-2632. The "what happens today" wording uses the inventory title and the round 1 g1 description of the scenario (`defType=dismax` inside `{!boost}`, with `hl.fl`). Check it against the ticket before posting.
- The 2026-10-06 log files (`g2632-*.log`) are not in the workspace. The counts are the receipt's own.
- The Lucene 9.12.3 bytecode claim (finding 4).
- The branch_10x and branch_9x Lucene pins (taken from the assignment).
- Live upstream main `d76fc14b9857`: the commit is not in the local store; only the merge base was confirmed via a GitHub compare.
- The unified leg: it is not in the head test, so it has no committed proof.
- No test or build was run, by rule. The final head has no proof yet.
- No em dashes in the draft (grep checked). Internal words (gate, receipt, ledger, seeds, run identifiers, claim, pool, assignment, subagent, submission) do not appear in the draft; "claimed" appears once in "No defect is claimed", as the assignment asks.
