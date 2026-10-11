# Highlighting leftovers, SOLR-16885 Jira comment text (part s2)

Scope: draft the Jira comment text for SOLR-16885 in wiki markup. Deliverable: `material/SOLR-16885-jira-comment.md`. Nothing posted to GitHub or Jira, no build, Gradle, test or gate run, no commit or push, and no file edited other than the two named deliverables. The claim file `claims/pool-highlighting-leftovers-2632-16885.md` was not edited in this part; the main side marks it DONE.

## Facts used, with their lines

Receipt `receipts/SOLR-16885.md`:
- Line 3: NO GATE. The premise did not reproduce. Premise run 2026-10-09, base production (cabedd1d968) with the branch test files in place, TestUnifiedSolrHighlighter 34 of 34 green, new test testTermVectorOffsetsWithoutPositions PASSED on base, claimed IndexOutOfBoundsException does not occur on current main, fix addresses no demonstrated failure at main.
- Line 4: branch solr-16885-submit at 2b9b80119a92d787970b02b1472bfea6d2fa3613, as received.

Round 1 group 2 report `reports/highlighting-round-1-g2.md`:
- Line 10: merge-base with upstream/main (8e62c2686882) is cabedd1d968 for all three branches; none of the files these branches touch changed on upstream since that merge-base.
- Line 34: no gate and no run for solr-16885-submit.

Round 1 report `reports/highlighting-round-1.md`:
- Line 5: done 2026-10-09 (date of the round; used for "by that date").
- Line 15 (SOLR-16885 row): audit only, owner decision; the branch's note is unrun.

Round 1 answers `material/highlighting-round-1-answers.md`:
- Line 29: results recorded 2026-10-09 (main side).
- Line 32: SOLR-16885 premise NOT grounded; TestUnifiedSolrHighlighter 34 of 34 passed on base; new test passed; IndexOutOfBoundsException does not reproduce on current main.

Inventory `branch-focus-inventory-2026-10-08.md`:
- Line 128: ticket title "UnifiedHighlighter guard for term vectors without positions". Used only to describe the ticket's scenario in general terms.

`reports/highlighting-round-1-g1.md`: searched for 16885; no mention. Nothing used from it.

Wording in the comment that is not a direct copy: "The branch was built on this commit" (from the merge-base in g2 line 10); "The branch also had no test run before this check" (g2 line 34).

## Links in the comment

1. [SOLR-16885|https://issues.apache.org/jira/browse/SOLR-16885]: not checked live. Jira tools were not used, per instructions. The URL form follows the approved template in `pr-formula.md` (line 130) and the ticket key from the receipt. The key exists in the record; the URL was not opened.
2. [cabedd1d968|https://github.com/apache/solr/commit/cabedd1d968059215188f4e7563fb303241899ed]: checked locally. The short hash resolves in the worktree repository to the full SHA cabedd1d968059215188f4e7563fb303241899ed (`rev-parse`). It is an ancestor of upstream main 8e62c2686882 (`merge-base --is-ancestor`, exit 0). The `upstream` remote is https://github.com/apache/solr.git. The URL was not opened in a browser.

No code path or file line is cited. The receipt and report give line numbers only for the branch's code, which is not upstream main code, so none were linked.

## Could not verify, or left out

- Ticket wording: the comment says the ticket describes an IndexOutOfBoundsException in the Unified Highlighter for term vectors without positions. This comes from the receipt, the round 1 report (g2 line 37 "the ticket's IndexOutOfBoundsException") and the inventory title (line 128). The Jira ticket text itself was not read.
- Left out: the report's note that the ticket says "not likely a fix in Solr" (g2 line 38; round 1 report line 15). Not checked against the ticket text, and the Solr-side decision is still open (round 1 report line 26).
- Left out: the report's code-reading note that a Solr-side change would alter behavior for every term-vector field without positions (g2 line 38). Not a run, and the owner has not decided. The owner may add it if wanted.
- Left out: the branch code lines (g2 line 35: UnifiedSolrHighlighter.java L274-L288, schema L40 and L53, test L85), since they are branch code, not upstream main code, and the record does not say they exist at cabedd1d968.
- Left out: the Lucene quote and the note that the exception may not reproduce on current Lucene (g2 lines 35-36). The record does not check the Lucene source, and no Lucene version is recorded for SOLR-16885.
- Left out: the log name, seed and run identifiers (receipt line 3; answers line 32), per the no-run-identifiers rule for the comment.
- Left out: hygiene items (SOLR-16885-TESTING.md must not ship; changelog title is a folded scalar). Internal, not for Jira.
- Not verified: "current main" is the receipt's wording. Upstream main may have moved after 2026-10-09; the comment dates the check.
- Header: used the approved template line (`pr-formula.md` line 128) in its Jira italic form, with the robot emoji repeated between the two parts as in the template. The owner may drop the second emoji if the Jira form should match the assignment's order exactly.
- Bullets use the " * " form from the assignment. Not rendered in Jira.
- Writing check: no em dashes and no process vocabulary in the comment (grep for the dash characters, gate, receipt, ledger, seed, claim, pool, assignment, subagent, submission, Claude, hypothetical returned no matches).
