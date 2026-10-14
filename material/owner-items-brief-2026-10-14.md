# Owner items brief, 2026-10-14

Detail for the six items held on 2026-10-14 for more information. Each section: the decision, the options, what the standing rules say, what happens on each option, and a recommendation. Facts are from the records named in each section, read at the fresh pr-prepare tip, plus live Jira and the live PR body where an item needed them.

## 1. SOLR-12505: three holds

The branch (gated green at `1a1875d95f`) makes streaming `fetch()` send `defType=lucene` with every batch, and rejects a join field name that would break the batch query. Records: `pr-drafts/streaming/SOLR-12505.md`, `reports/streaming-convergence-round-2-response.md`.

### 1a. The Choice: defType=lucene, or the terms parser

The decision: which query parser the fetch batch uses. As implemented, the batch keeps its local-params form and sets `defType=lucene`. The alternative from the ticket thread is `defType=terms` with the key values as the query text; that rewrites every batch's query text and replaces the current escaping with the terms separator.

Standing rules: a genuine choice between defensible behaviors is posed as a Choice in the PR, not settled silently (your rule of 2026-10-13, first applied to SOLR-18541). The draft already poses it: Bernstein proposed setting defType, Smiley agreed, and Smiley also suggested the terms parser.

On each option: pose it (open as drafted) and the code, tests and gate stand untouched; a maintainer picks in review, and switching later is one contained change. Rule terms now and the batch construction is rewritten, the escaping reworked, new tests written, and the branch re-gated before it can open; the changelog title job is also waiting on this call.

Recommendation: open SOLR-12505 with the Choice posed exactly as drafted; the change keeps defType=lucene, and the terms parser stays a live alternative for the maintainer to pick in review.

### 1b. The Unicode whitespace decision

The decision: how wide the join-name check is. Today the check rejects ASCII whitespace only (a Java `\s` pattern in `FetchStream`). The local-params reader it defends against splits on `Character.isWhitespace` (QueryParsing line 136), which also covers Unicode spaces such as U+2003 and U+3000. A join field name containing one of those passes the check and still breaks the batch the same way an ASCII space does; the draft's Limits admit this.

Standing rules: where the cost is effort, do the hard thing. The gap is a hole in the branch's own stated purpose, not an optional extra.

On each option: widen and the check uses the reader's own whitespace definition, with one added test using a non-ASCII space; one small branch commit and one gate, and the Limits admission comes out. Keep ASCII-only and the branch opens as-is, carrying a Limits line that says part of the separator set escapes the check.

Recommendation: widen the join-name check to the same whitespace definition the local-params reader uses (Character.isWhitespace), add one test with a non-ASCII space, and re-gate before opening; the cost is one small branch commit and one gate.

### 1c. The JIRA attributions

The decision: whether the draft's three attributions can stand. This hold existed because the review host had no Jira access. Checked against live Jira on 2026-10-14: the assignee is Eric Pugh, and his comment of 2021-10-14 says removing `defType=edismax` from his setup "worked like a champ". Comment 16519637 is David Smiley's, quoting Joel Bernstein's proposal to set the defType parameter and agreeing with it ("+1 great idea"). Smiley's suggestion of the terms parser is his comment 16519475. All three attributions in the draft match.

Standing rules: where information is missing, get it. It is now gotten.

On each option: close the hold and the draft stands unchanged. Keep the hold and the branch waits on a verification that has already been done.

Recommendation: close this hold; the attributions verify against live Jira (assignee Eric Pugh; Bernstein proposed setting defType and Smiley agreed in comment 16519637; Smiley suggested the terms parser), so the draft stands as written.

## 2. SOLR-18368: the key choice

The decision: under what Jira key the PR opens. The ticket is Resolved/Fixed in Jira (verified 2026-10-14): the removal it asked for (`CloudSolrClient.Builder.withInternalClientBuilder`) already landed upstream in PR #4782. What remains is only a reference-guide page whose example cannot compile; the branch fixes three lines in that one page and changes no code. Records: `receipts/SOLR-18368.md`, `reports/security-round-1.md` (owner options), `pr-drafts/security/SOLR-18368.md`.

Options: (a) open with no key: the PR title carries no SOLR-18368, the Jira link line at the top of the draft body is deleted, and the body cites PR #4782 and commit `3ef3d093c06` as the change that made the page wrong. (b) open under the SOLR-18368 key as a follow-up: title carries the key, the Jira line stays, and the PR attaches to the resolved ticket. (c) do not open; fold the lines into a later docs pass.

Standing rules: no new ticket unless someone asks; an issue that still exists on a resolved ticket gets a PR or a new ticket (2026-10-11). The recorded recommendation in the security round 1 record is (a).

On each option: (a) the PR stands on its own with the upstream change cited; if a maintainer wants a ticket, one is created then, per the standing rule. (b) traceability to the ticket is direct, but the PR hangs on a Resolved ticket closed by another contributor's PR, and a maintainer may ask for it to be reopened or replaced with a fresh ticket. (c) the page stays wrong on main indefinitely; the still-an-issue verdict forecloses it in practice.

Recommendation: open under option (a): no SOLR-18368 key in the title, the Jira link line deleted from the body, and upstream PR #4782 cited as the change that made the page wrong; the ticket is Resolved/Fixed and the fix is documentation only.

## 3. SOLR-18339: opening timing

The decision: open the gated branch now, or wait. The branch (gated green at `47e53884609c`, BATS 11 of 11 with both new tests failing on base) ports the bin/solr start wait to StatusTool. The ticket's own text sets a precondition: port bin/solr "once that has been live in 10.1 for some time", meaning SOLR-18336's StatusTool improvement for solr.cmd. Checked on 2026-10-14: SOLR-18336 is still Open in Jira, has no fix version, and no commit on main carries its key. The precondition is not met on its face. The draft's secured-node Choice (the status wait sends no credentials, so a secured node answering 401 reads as a failed start) is already posed in the draft and needs no ruling to open. Records: `receipts/SOLR-18339.md`, `pr-drafts/cli/SOLR-18339.md`.

Standing rules: openings come to you as slates and open as drafts; nothing in the rules overrides a sequencing precondition the ticket itself states.

On each option: open now (as a draft) and review can start, but a maintainer can reasonably park or close it as premature against the ticket's own words, and a long-open draft will need rebasing later. Wait, and nothing is lost: the branch stays gated and banked, and it opens when SOLR-18336 has shipped and been live for a while, as the ticket asks.

Recommendation: hold the opening until SOLR-18336 has actually landed and been in a release for a while, exactly as the ticket's own text preconditions it; the branch stays gated and banked, and the secured-node Choice is already posed in the draft for whenever it opens.

## 4. SOLR-17752: rebase direction

The decision: open the branch against its current base (`56ec140e3636`, an older main commit), or rebase it onto the newer base `14c7aac0d151` first. The branch is five commits, gated green at `e5c0a64f993c`, and already pushed to the fork; its changed files are SolrResponseUtil.java, its test, and the changelog entry. Checked on 2026-10-14: no upstream commit since the branch base has touched either code file, and the branch base is 88 commits behind current main. Records: `receipts/SOLR-17752.md`.

Standing rules: the hardening gate at the exact head is what counts; a branch gated at its live tip is not re-run for a review action that changes no outbound file. The decision default takes the recorded recommendation where one exists; the census records this as an owner item with no recommendation taken.

On each option: open as-is and the PR shows the same three-file diff it would show after a rebase, because the PR diff is computed against current main either way; the gate record at the head stands. Rebase first and every commit is rewritten on a published branch: force-push, all draft citations re-pointed, and a full re-gate at the new head, for no change in what a reviewer sees. If a maintainer asks for a rebase during review, it is done then, which is the normal place for it.

Recommendation: open against the current head as-is, with no rebase; the PR diff against current main is the same three files either way, no upstream commit has touched them since the branch base, and a rebase would rewrite a published, gated branch and force a full re-gate for no visible gain. Rebase later only if a maintainer asks.

## 5. SOLR-9598: the standing owner hold

The decision: lift the hold and let the branch open, or keep holding it. The branch (gated green at `c8407773f76c`) makes RESTORE wait until the restored collection's replicas are active before returning, controlled by `waitForFinalState`, defaulting to true with an opt-out. The hold guards one call: that default. The draft already poses it as a three-way Choice (wait by default with opt-out, as implemented; always wait, the direction SOLR-17712 is taking other commands; or no default wait, as CREATE does today), and the recorded recommendation in the replication answers is option 1, the implemented one. The draft's proof is timing-dependent and says so plainly (the base failure showed in one of five runs). Records: `receipts/SOLR-9598.md`, `material/replication-backup-round-1-answers.md`, `pr-drafts/replication-backup/SOLR-9598.md`.

Standing rules: a genuine choice between defensible behaviors is posed in the PR rather than settled silently (2026-10-13). That is exactly what the draft does with this default.

On each option: lift the hold and the OWNER NOTE header comes off the draft, the branch joins the replication slate and opens as a draft with the Choice posed, and the adopted landing order (9598 before SOLR-12651; both change RestoreCmd.java and trial-merge cleanly) can proceed. Keep the hold and the branch stays banked; the default question stays unanswered in the one place designed to answer it, in front of maintainers, and 12651's landing order stays blocked behind it.

Recommendation: lift the hold and open with the default posed as the draft's Choice; wait-by-default with an opt-out is implemented and recommended in the records, the Choice structure is exactly how your rules handle a genuine behavior default, and lifting it also unblocks the adopted landing order (9598 before SOLR-12651).

## 6. PR #5010 (SOLR-12998): the CI paragraph, keep or cut

The decision: whether one paragraph stays in the live PR body. The paragraph reads: "One failure seen in CI on this branch, `TestUserManagedReplicationWithAuth`, is pre-existing and unrelated to this change. User-managed replication polls `ReplicationHandler.fetchIndex` and never sends REQUESTRECOVERY (verified in code), so the changed code is not on its path. With the failing run's seed pinned (4C2E85AFC002DC), the class passes at this head and fails on the base tree with the same class-level object-leak teardown error; the failure follows the seed, not this branch." Read from the live body on 2026-10-14; the head is `62a17a116b`.

What the settled decision says: the gate stands GREEN and the proof leg is inconclusive by construction, because the branch tests use APIs the branch adds and do not compile against base production; no further proof run is owed (`receipts/SOLR-12998.md`, decision of 2026-10-10; `material/held-items-decisions-2026-10-13.md`). The body's Proof section was already edited on 2026-10-13 to say exactly that.

Does the paragraph overclaim? No. It is about a CI failure on the PR, a different subject from the proof leg, and each of its claims is in the round 29 record in the takeover log: the code check (user-managed replication polls fetchIndex and never sends REQUESTRECOVERY; the only senders are in the cloud path this test does not use), and the seed-pinned pair at 4C2E85AFC002DC (head passes, base fails with the same ObjectTracker leak teardown). It claims the failure is pre-existing and seed-following, which is the recorded classification; it does not claim a fail-before proof for the change itself.

On each option: keep it and a reviewer who sees the red CI result finds the explanation and its evidence in the body. Cut it and the PR carries an unexplained CI failure that reviewers will ask about, with the answer only in internal records.

Recommendation: keep the CI paragraph; every claim in it is backed by the round 29 record (seed-pinned runs on both trees and the code check that user-managed replication never sends REQUESTRECOVERY), it does not touch the settled proof question, and cutting it would leave a red CI result on the PR with no explanation.
