# Retire final pack: SOLR-9759

Verdict: RETIRE CLEAN. Superseded on main by PR #5048. The branch carries no fix that main lacks.

Checked 2026-10-15.
- Fork tip: `origin/solr-9759-submit` = 31e702622dae43a36ed8d7ec8477c1547e5ebc42 (ls-remote origin refs/heads/solr-9759-submit). Matches the receipt and RR2.
- Main: upstream main = 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 (live).

## Premise

From `receipts/SOLR-9759.md`:
- "NO GATE. No gate for this branch is recorded on the main side."
- The branch's own `SOLR-9759-TESTING.md` says the change and the test "were guessed from reading main and that nothing was compiled or run."
- The receipt lists the owed verification as main-side: compile, then run the test on base and at the head. None was run. RR2 judged that not worth a job, because the verdict does not depend on it.

## Main side (checked 2026-10-15)

- `c6f910d3ccf` is "SOLR-9759: Admin UI should post streaming expressions" = apache/solr PR #5048 (author Eric Pugh), merged 2026-10-08T22:46:49Z (`gh pr view 5048`).
- It is an ancestor of upstream main 714d6ceff.
- `solr/webapp/web/js/angular/services.js`, `solr/webapp/web/js/angular/controllers/stream.js` and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiStreamScreenTest.java` are unchanged on main since c6f910d3ccf (empty diff).
- Main's `services.js` line 380 defines `Query.queryPost`, which sends the expression as a form-encoded POST. Its error path shows the HTTP status (RR2).

## What the branch holds

Merge-base cabedd1d968. Five files:
- `solr/webapp/web/js/angular/services.js` and `controllers/stream.js`: a form-encoded POST and an error callback. The text differs from main's.
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiStreamScreenTest.java`: 9 insertions and 30 deletions against main's copy.
- `changelog/unreleased/SOLR-9759-admin-ui-stream-post.yml`: uppercase in the branch, lowercase on main. Not a gain.
- `SOLR-9759-TESTING.md`.

## Gainable, and what is taken

- Gainable: one thing. The branch test's positive assertion, `testLongStreamingExpressionViaUi`: a 10,000-space expression still returns stream-doc-1 and stream-doc-3. Main's `testLargeExpressionSucceedsViaUi` (20,000-character padded fq) asserts only the EOF response. RR2 judged the positive assertion not worth a job by itself.
- Not a gain: the address-bar caveat (the GET URL fails for very long expressions) is still true on main, and main already documents it.
- Taken: the fix, through PR #5048.

## Owner step

1. Label the fork branch `solr-9759-submit` RETIRED and leave it in place (owner rule, 2026-10-14: a retired branch is never deleted).
2. PR: none. No PR authored by nick-boss-tech on apache/solr has this head (`gh pr list --author nick-boss-tech --state all`, 2026-10-15).
3. Local worktree `wt\SOLR-9759` is clean and tracks origin. Its local branch is `wt-solr-9759-submit`. Leave both as they are.
4. Main-side record: the takeover entry still reads AWAITING PIPELINE (fresh arrival 2026-10-06, round 36 routing 2026-10-07). Set it to RETIRED, superseded by c6f910d3ccf (PR #5048). The inventory row may be updated too.

Deletion is not the current rule. It would apply only if the owner reverses the 2026-10-14 ruling: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-9759-submit`. Here origin is the fork (nick-boss-tech/solr).

## Jira note (draft, not posted, optional)

"Fixed on main by PR #5048 (commit c6f910d3ccf, merged 2026-10-08), which makes the Admin UI post streaming expressions in the request body. The fork branch that guessed at the change was never compiled or run, and it is retired. No further change is proposed."

Jira status: not recorded in the local packet and not checked live. Check it before posting. If the ticket is already closed, no comment is needed.

## Condition

The verdict depends on main carrying the behaviour, and it does. The owed verification in the receipt is not needed for the retire call. If the owner later wants the positive document assertion, it is a test-only change on main, not a reason to keep this branch.
