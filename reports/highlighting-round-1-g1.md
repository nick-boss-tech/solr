# Highlighting round 1, subagent G1 report

Scope: SOLR-2681 (draft) and SOLR-2632 (audit, no draft). Read-only on branches; no builds, no tests, no PR or Jira writes. Files written: `pr-drafts/highlighting/SOLR-2681.md` and this report.

## T1. SOLR-2681: DRAFT written

- Live tip checked: `git ls-remote origin refs/heads/solr-2681-submit` = `4cb25b1691b9ca66552a93687f79cd62993efcf9`, matches the receipt. Not a HOLD.
- Drafted head: `4cb25b1691b9ca66552a93687f79cd62993efcf9`. Base: `cabedd1d968059215188f4e7563fb303241899ed` (merge-base with upstream/main).
- Changelog fragment on the branch: `changelog/unreleased/SOLR-2681-highlight-function-query.yml`.
- Branch diff: 3 files, +34/-1. Production change is in the original highlighter's term extractor only, and handles only a top-level `query(...)` function query.
- Proof uses only the receipt's facts: HighlighterTest 36 of 36 at head, and on base with the new test file one failure, `testHighlightQueryInsideFunctionQuery`. No run date is given; the receipt's 2026-10-06 is the push date, so no verification date is printed.
- Limits come from the diff, not the ledger. The receipt records no ledger notes and no planned follow-up, so the draft offers a follow-up on request and does not claim a planned submission.

Issues to resolve before posting:
1. Count mismatch. `HighlighterTest.java` at head has 31 `@Test` methods (30 at base). The receipt's 36 cannot be reproduced from source. The named logs (`g2681-*.log`) are not in the workspace. The draft carries 36 with an internal note at its top to delete before posting.
2. Changelog title overstates the code. It says "a query nested inside a function query, e.g. {!func}query($q)". The code matches only when the whole function query is `query(...)`. `{!func}product(query(...),...)` is not handled. Suggest narrowing the title (not edited here).
3. The approved AI footer contains the word "review". Kept verbatim as the approved template; drop it if the strict vocabulary list applies.

## T2. SOLR-2632: audit, no draft

- Branch tip checked live: `solr-2632-submit` = `1d7018f3fd7b94ad1a9858df8eb1af4843107bf8`, matches the receipt.
- Diff against base (4 files, +67):
  - `SOLR-2632-TESTING.md` at repo root: pipeline handoff note. It says "nothing was compiled or run" and lists guesses the receipt has since answered. Not outbound.
  - Changelog `SOLR-2632-highlight-boost-wrapper.yml`: says highlighting "now sees through" `FunctionScoreQuery` for original and fastVector. If the production hunks are inert, this describes a change that does not happen.
  - `DefaultSolrHighlighter.java`: two hunks unwrap `FunctionScoreQuery`, one in `CustomSpanTermExtractor.extract` (original) and one in the FastVector `flatten`.
  - `HighlighterTest.testHighlightQueryWrappedInBoost`: `q=+id:1 +_query_:"{!boost b=3 v=$qq}"`, `qq=<field>:keyword`, for original (`t_text`) and fastVector (`tv_text`).
- Ticket match: the ticket's scenario uses `defType=dismax` inside `{!boost}`, with `hl.fl`. The branch test does not use dismax. The receipt says "the ticket's 2011 scenario works under all three methods", but the branch test does not show that, and the receipt does not say the scratch legs used dismax.
- Unified method: not in the branch. The receipt has a scratch leg only.
- Not re-verified here: the Lucene 10.4.0 bytecode claim, and the premise runs. The named logs are not in the workspace; the findings are as the receipt records them.

Disposition options, stated plainly:
- (a) Test-only pin PR. Drop both production hunks, the changelog, and the root note. Add the unified leg, and optionally the dismax variant. The test passes on base, so the PR must say it is a regression guard, not a fail-before proof. Added legs need a fresh run at the new head; the unified result so far is scratch only.
- (b) Jira comment, no PR. Post the finding that the 2011 path already works on current main for the tested methods. This is a public write and needs Nick's explicit go-ahead, plus its own draft.
- (c) Bank as received. Keep `1d7018f` unsubmitted. Nothing goes out. As it stands, the branch would claim a fix that changes no behavior, so it should not be submitted in this form.

The receipt recommends (a) or (b). This branch in its current form does not match the record on the changelog and the root note.

## Disagreements with the receipts

1. SOLR-2681: 36 of 36 versus 31 `@Test` methods in source (see T1 item 1).
2. SOLR-2681: the receipt does not mention the changelog title's scope (see T1 item 2).
3. SOLR-2632: "the 2011 scenario works under all three methods" is not shown by the branch test, which does not use dismax.
4. SOLR-2632: the root note's "nothing was compiled or run" predates the premise runs.

## Owner decisions

- SOLR-2681: approve the draft; reconcile the 36 count; narrow the changelog title or accept it as is.
- SOLR-2632: choose (a), (b), or (c).
