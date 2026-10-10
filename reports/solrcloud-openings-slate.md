# SolrCloud openings slate, for Nick

Written 2026-10-10 by Claude Code (AI agent), working for Nick Shanin, after the answers pass and the receipt refresh. Nothing is posted and no PR is opened. This slate lists what can open, what is held, and the decisions that are yours.

Sources: `reports/solrcloud-round-1.md`, `material/solrcloud-round-1-answers.md`, `reports/solrcloud-answers-round-1.md`, `reports/receipt-refresh-round-2.md`.

## Can open, once the stated check is done

These drafts have no open branch change. The stated check is a confirmation, not a code change.

- **SOLR-5813.** Default the empty collection name to the core name (adopted). Choice: the reject route is posed to the maintainer.
- **SOLR-12991.** WARN (adopted). The Choice is posed to the maintainer.
- **SOLR-13186.** Limits only. Confirm the recorded date (2026-10-06).
- **SOLR-13369.** The proof is by construction, stated as such. Confirm the Choice.
- **SOLR-17680.** The draft names the v2 endpoint. The Proof's "10 before, 11 now" count has no source for "10"; confirm or reword.
- **SOLR-17733.** The contract change is stated; the sync complaint is not reproduced.
- **SOLR-9155.** Choice on the declared-`InterruptedException` route. Confirm the recorded date (2026-10-06).
- **SOLR-15674.** Land before SOLR-15478. Choice: mzxid alone against the creation ID plus the data version (adopted). Length is over the guide.
- **SOLR-15106.** Confirm the base failure text and the recorded date (2026-10-07).
- **SOLR-15386.** Confirm the owner question (the cached live-node check). The answers adopt the position.
- **SOLR-15035.** Confirm the ticket summary. The Proof sentence is reworded to the receipt's outcome.
- **SOLR-10694.** Confirm the CSV-only scope. Draft written.
- **SOLR-11288.** Branch changelog title first (one commit). Confirm the blank-only decision (the recommendation is to keep blank-only equal to omitted).
- **SOLR-4989, SOLR-13246, SOLR-8275, SOLR-6438, SOLR-16725, SOLR-11939, SOLR-15805, SOLR-17708, SOLR-12007, SOLR-17377, SOLR-9750, SOLR-16849, SOLR-15024** (core admin). Each needs a check from its draft's own list. Several need a rebase and a new gate first (4989, 15024). SOLR-13246 must be pushed from the PR head `6817c6c0c267`, not the local branch.

## Held: needs a branch commit or a gate first

- **SOLR-17292.** The node-down remedy is adopted but not in the branch. Commit, then re-gate. The draft's links re-pin after that.
- **SOLR-12651.** Run at the live tip `f3131d1ee84` is owed. Changelog title needs a branch commit.
- **SOLR-15863.** Changelog capitalization needs one branch commit.
- **SOLR-14919.** The RecoveryStrategy marker: decide whether to ship the processor alone (recommended).
- **SOLR-16437, SOLR-11479, SOLR-3865, SOLR-4754, SOLR-10234, SOLR-10641.** First gate or premise run owed.
- **SOLR-16013.** The history rewrite and a re-gate at the rewritten head.
- **SOLR-13136.** The behavior decision and a gate.
- **SOLR-12998 and SOLR-18391 (graceful-create).** Gate at the live tip; text corrections.
- **Search components, held:** SOLR-6759, SOLR-8003, SOLR-8051, SOLR-10305, SOLR-9124 (see `reports/receipt-refresh-round-2.md`).

## Not for opening

- **SOLR-13239.** Submission hold stays.
- **SOLR-18277.** Merged. Delete the retired branches, if you decide to.
- **SOLR-17281.** Parked.
- **SOLR-7394.** Consistent live PR. The title edit is your call.
- **SOLR-18391 submit.** Close as superseded, if you decide to.

## Decisions for you (DISCUSS items, recommendations recorded)

1. SOLR-14919: ship the RecoveryStrategy marker in this PR, or the processor alone. Recommended: processor alone.
2. SOLR-13136: keep the live CONSTRUCTION behavior, or restore the gated delete. Recommended: keep the live behavior.
3. SOLR-11288: blank-only means "all", or keeps failing. Recommended: keep blank-only equal to omitted.
4. SOLR-16013: approve the history rewrite of commit `3059f9be884`. Recommended: approve, without squashing.
5. SOLR-18391 submit: close as superseded, or keep as a draft. Recommended: close.
6. SOLR-18277: delete `solr-18277-submit` and the two CI branches. Recommended: delete all three.
7. SOLR-17281: revert the one-line change and ask for logs, or replace later. Recommended: revert.
8. SOLR-3865: keep the ticket link with a disclosure, or drop it. Recommended: keep with disclosure.
9. SOLR-4754: keep the scheme-only guard with the text fixes. Recommended: keep with fixes.
10. SOLR-10234: keep the suppression as implemented, and drop the test-only changelog fragment. Recommended: both.
11. SOLR-10641: ship the passing pin as its own PR. Recommended: ship, after a tidy fold-in and a first gate.
12. SOLR-7394: add the restore half to the live PR title and the branch changelog. Recommended: apply.
13. Search components, held: SOLR-8051 (is there a real body-less response path), SOLR-10305 (text and scope), SOLR-9124 (changelog wording), SOLR-6759 (try/finally), SOLR-8003 (glob design and landing order).

## Re-gates owed (main side)

12651 (live tip), 12998 (`62a17a116b5`), 13136 (`486b3877556`), 16013 (rewritten head), 16437 (first gate), 17292 (after the remedy commit), 11479 (first gate), 18391 graceful-create (live tip `adcda10b501`), 3865, 4754, 10234, 10641 (premise run and first gate each).

## Not done

No DISCUSS item was decided. Nothing was posted or opened. No gate or build was run.
