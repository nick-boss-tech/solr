# SolrJ convergence, round 2: category close-out (W2)

Date: 2026-10-14 (UTC). Assignment: `assignments/pool-category-closeout-overnight-round-1.md`, W2, under `assignments/pool-category-convergence-round-2-settlements.md`. Claim: `claims/pool-category-closeout-w2-solrj-round-1.md`. Interim report: `reports/solrj-convergence-round-2.md`. Round 1 response: `reports/solrj-convergence-round-1-response.md`.

Result: 13 remainder lines. 10 are CONVERGED, including the 5 packaging title items (CONVERGED on the draft, each with a named packaging follow-up). 3 are HOLD, all on the SOLR-11356 header copy: the owed gate (SJ-R1-49), the Limits summary revision (SJ-R1-51) and the JDK suite line, which the gate settles.

No build, test, Gradle or gate run on this host. No push, commit, checkout, reset, stash or rebase. No GitHub writes and no PR body edits. No comment, flip, opening or branch deletion.

## Method

- Heads checked by `git ls-remote origin refs/heads/<branch>` and upstream main by `git ls-remote upstream refs/heads/main` (`d38e0f1623728bdd305e76a598f013f1bf32e449`).
- Each packaged head compared with the live tip by `git diff --stat`. Each receipt read for its gated head.
- Order: the round 1 response's per-finding order (ticket ascending, SOLR-10364 before SOLR-11356).
- Draft line citations checked against the files at the base and head commits (`git show`).

## Live state at the start of this pass

| Ticket | Fork tip (ls-remote) | Receipt gated head | Interim said |
|---|---|---|---|
| SOLR-3999 | e3af6f36e31 | e3af6f36e31 (GREEN) | packaging title open |
| SOLR-4335 | 8f6e267d648 | 8f6e267d648 (GREEN) | packaging title open |
| SOLR-4336 | 798618aa08f | 798618aa08f (GREEN) | packaging title open |
| SOLR-6046 | 3b833e369a0 | 3b833e369a0 (GREEN) | packaging title open |
| SOLR-8536 | 28552ddc26eb | 28552ddc26eb (GREEN) | packaging title open |
| SOLR-10364 | efb1e6717f9f (packaged head; was 502bdbf033fa) | efb1e6717f9f (GREEN, 2026-10-11) | HOLD, packaged head not pushed |
| SOLR-11356 | 6120dae28d04 (packaged head; was 8474e5a3a26d) | 6120dae28d04 (GREEN, 2026-10-11) | HOLD, packaged head not pushed |

Both packaged heads now differ from their pre-push tips only by the removal of the handoff note (`git diff --stat`: `SOLR-10364-TESTING.md`, 22 deletions; `SOLR-11356-TESTING.md`, 34 deletions). The Java and the changelog fragments are identical, so each receipt's proof applies to the pushed head.

## Remainders, one line each

Format: final state, the rung that settled it, the artifacts.

1. **SOLR-3999 (packaging PK-1): CONVERGED on the draft; packaging follow-up.** Rung: get information (live title checked at the tip; the replacement is the recorded one; it matches `reports/draft-fidelity-streaming-solrj-s2.md` line 38). Deferred as a named follow-up: the branch title commit is reserved to the opening step (W4). Artifacts: none; the draft carries no title text.
2. **SOLR-4335 (packaging PK-2): CONVERGED on the draft; packaging follow-up.** Rung: get information (as above; replacement matches `reports/draft-fidelity-streaming-solrj-s2.md` line 53). Artifacts: none.
3. **SOLR-4336 (packaging PK-3): CONVERGED on the draft; packaging follow-up.** Rung: get information (replacement matches s2 line 74). Artifacts: none.
4. **SOLR-6046 (packaging PK-4): CONVERGED on the draft; packaging follow-up.** Rung: get information (replacement matches `reports/draft-fidelity-streaming-solrj-s3.md` line 81). Artifacts: none.
5. **SOLR-8536 (packaging PK-5): CONVERGED on the draft; packaging follow-up.** Rung: get information (replacement matches s3 line 129). Artifacts: none.
6. **SOLR-10364, SJ-R1-45 (packaged head): CONVERGED.** Rung: get information. `solr-10364-submit` is `efb1e6717f9f` (ls-remote), the receipt is GREEN at that head, the only change from `502bdbf033fa` is the note removal, and the draft already cites `efb1e6717f9f`. The 2026-10-12 owner decision covers this push and supersedes the HOLD. Artifacts: none beyond item 7.
7. **SOLR-10364, "We" sentence (round 1 flag for the opening pass): CONVERGED.** Rung: do the hard thing (wording applied with the SJ-R1-21 wording used for SOLR-4422). Artifact: `pr-drafts/solrj/SOLR-10364.md` line 27 now reads "A follow-up ticket and PR can be opened on request."
8. **SOLR-11356, SJ-R1-48 (packaged head): CONVERGED.** Rung: get information. `solr-11356-submit` is `6120dae28d04` (ls-remote), the receipt is GREEN at that head, and the only change from `8474e5a3a26d` is the note removal. Artifact: `pr-drafts/solrj/SOLR-11356.md`, all four occurrences (three code links, one changelog link and the "This head" line) re-pointed from `8474e5a3a26d` to `6120dae28d04`; zero occurrences of the old head remain.
9. **SOLR-11356, SJ-R1-53 (merge-base sentence): CONVERGED.** Rung: get information, then the recorded wording adopted. `upstream/main` (`d38e0f1623`) contains `cabedd1d968` as an ancestor, and that is the merge-base of both heads with upstream main (`git merge-base`). The main side's label followed the receipt, which compares with `origin/main` (the fork). The PR targets upstream main. Artifact: `pr-drafts/solrj/SOLR-11356.md` line 9 uses the register's wording: "Both links are at the merge-base with upstream/main, `cabedd1d968059215188f4e7563fb303241899ed`, which the branch starts from." Follow-up E2 (receipt wording).
10. **SOLR-11356, SJ-R1-49 (header copy in the stream key): HOLD, owed gate.** Rung: do the hard thing. The owner's decision (copy) is adopted. The patch is written and checked to apply to the pinned file. The gate cannot run on this host. Hold: a gate on vm1 or vm2, a push by the patching agent, the receipt refresh, then the draft revision in "SOLR-11356, post-gate text" below. Artifacts: `gates/SOLR-11356-sj-r1-49-header-copy.patch`, `gates/SOLR-11356-sj-r1-49-header-copy.md`. The draft's summary line 13 is corrected now to the current behavior (credentials only), so the draft does not claim a copy the head lacks: `pr-drafts/solrj/SOLR-11356.md` line 13.
11. **SOLR-11356, SJ-R1-51 (Limits summary after 49): HOLD, follows item 10.** Rung: defer as a named follow-up. The current summary is accurate at the pinned head, so it stands until the receipt; the replacement is staged below. No file change now.
12. **SOLR-11356, JDK suite line in Limits (from the receipt's scope note): HOLD, closed by item 10.** Rung: defer as a named follow-up. The gate's focused run of `ConcurrentUpdateJdkSolrClientTest` settles the line either way.
13. **SOLR-14298 and SOLR-18341, "we" sentences (round 1 flags for the opening pass): CONVERGED.** Not in the interim's list; settled here because the owner's voice rule covers them. Rung: do the hard thing (wording only). Artifacts: `pr-drafts/solrj/SOLR-14298.md` line 39 ("a follow-up that can be opened on request"); `pr-drafts/solrj/SOLR-18341.md` line 47 ("the broader route is open in this PR if maintainers prefer it") and line 69 ("is a follow-up that can be opened on request"). A search of all `pr-drafts/solrj/*.md` for first-person "we", "our" and "us" now returns nothing.

### Packaging items (PK-1 to PK-5)

Each receipt is GREEN at the live tip (the gated head). The live title was read from the changelog fragment at that tip on 2026-10-14. The replacement is the round 1 record's text, checked against the fidelity report named in the source column. The title commit moves the head, so the receipt's head line is refreshed when the packaging push lands.

| Item | Ticket (tip) | Changelog fragment | Live title at tip | Replacement title (from the round 1 record) | Source |
|---|---|---|---|---|---|
| PK-1 | SOLR-3999 (e3af6f36e31) | changelog/unreleased/SOLR-3999-serial-version-uid.yml | SolrDocumentBase, SolrDocument, SolrInputDocument and SolrInputField declare an explicit serialVersionUID so Java-serialized documents survive recompiles. | SolrDocumentBase, SolrDocument, SolrInputDocument and SolrInputField declare serialVersionUID = 1L, so the version number no longer follows the class shape. | round 1 response; `reports/draft-fidelity-streaming-solrj-s2.md` line 38 |
| PK-2 | SOLR-4335 (8f6e267d648) | changelog/unreleased/SOLR-4335-xml-nonchars.yml | SolrJ XML update requests no longer send the illegal characters U+FFFE and U+FFFF, which made the server reject the whole request; they are escaped like the other illegal control characters. | XML written by SolrJ and Solr no longer contains the illegal characters U+FFFE and U+FFFF; they are escaped like the other illegal control characters. | round 1 response; s2 line 53 |
| PK-3 | SOLR-4336 (798618aa08f) | changelog/unreleased/SOLR-4336-blank-numeric-params.yml | A blank numeric request parameter such as start= or rows= is treated as unset (default applies) instead of failing with a NumberFormatException. | A blank numeric request parameter such as start= or rows= is treated as unset instead of causing a bad request error. | round 1 response; s2 line 74 |
| PK-4 | SOLR-6046 (3b833e369a0) | changelog/unreleased/SOLR-6046-xml-atomic-update-array.yml | SolrJ's XML update writer expands array values of atomic update operations (such as set with a String[]) instead of writing their toString(). | SolrJ's XML update writer expands Object array values of atomic update operations (such as set with a String[]) instead of writing their toString(). | round 1 response; `reports/draft-fidelity-streaming-solrj-s3.md` line 81 |
| PK-5 | SOLR-8536 (28552ddc26eb) | changelog/unreleased/SOLR-8536-mdc-thread-name-control-chars.yml | Thread names set by MDC aware executors no longer contain line breaks or other control characters coming from application MDC values. | Thread names set by MDC aware executors no longer contain ASCII control characters, such as line breaks, that come from application MDC values. | round 1 response; s3 line 129 |

Not re-graded: SJ-R1-47 and SJ-R1-54 stand as record notes; SJ-R1-41 to 44 (SOLR-8536 notes) were outside the two drafts in the interim and were not re-audited. SOLR-8536's draft already carries the Choice its receipt suggests (line 26 onward).

## Decisions (decision default)

- D1. Order: the round 1 response's per-finding order, not the interim's. Recorded here.
- D2. Packaging titles: recorded as packaging follow-ups with the replacement text from the round 1 response (table below), checked against the fidelity reports. Not re-derived. The branch title commits, with the changelog parse and the receipt head refresh they need, belong to the opening step.
- D3. SJ-R1-53: the register's recorded wording is adopted (merge-base with upstream/main). The receipt's `origin/main` comparison is not the PR base, since the PR targets apache/solr main.
- D4. SJ-R1-49: the owner's recorded decision (copy the headers) is adopted and not reopened. Harder option taken: a header-match test (the branch has no test for the header part today) is specified as the proof leg, not only the copy.
- D5. SJ-R1-49, draft summary: corrected now to the current behavior instead of waiting for the gate, so the draft never overstates the code.
- D6. Scope extension: two "we" sentences in SOLR-14298 and SOLR-18341 and one in SOLR-10364 were round 1 flags for the opening pass. Settled now at low cost. Recorded.
- D7. SJ-R1-51 post-gate text departs from the register in one point. The register says "one part of the match is still open: the user principal", which assumes a test covers the copy. No such test is specified, because no deterministic route was found without a runner hook. The staged text keeps two open parts: the user principal and the header copy.
- D8. The PR head for SOLR-11356 is the pushed packaged head `6120dae28d04`, not the code-identical `8474e5a3a26d`. The draft cites the pushed head, since the PR head is that commit.
- D9. No live PR body was edited or read. This pass makes no GitHub writes. The live body edits are owed to the coordinator (below).

## Staged text for the draft after the gate (SOLR-11356)

Apply only after the receipt at the patched head exists. Every count and line anchor is taken from that receipt.

- Summary (line 13): "**Each stream keeps a copy of the credentials and headers of the request that opened it, and only matching requests join it.**"
- Live-view Limits bullet (line 35): delete it, and add: "The header copy has no test of its own. The header match test covers which requests share a stream. No test changes a queued request's headers, so the copy itself is not tested."
- Limits summary (line 32): "**The tests depend on timing, and two parts of the match are still open: the user principal and the header copy.**"
- Proof: add the header match test bullet with the base (merge-base) and head counts from the receipt, with its date.
- Head citations and the changelog link: re-point to the patched head; re-check all line anchors (the import and constructor lines move).
- The JDK suite line (Limits, last bullet): replace with the run result from the receipt.

## Owed items (holds, with owner)

- Packaging PK-1 to PK-5 (SOLR-3999, 4335, 4336, 6046, 8536): the title commit on each submit branch, with the replacement title from the Packaging items table and a changelog YAML parse; then the receipt head line refresh. Owner: main side, at opening (W4).
- SOLR-11356 SJ-R1-49: gate job `gates/SOLR-11356-sj-r1-49-header-copy.md`, then push by the patching agent, then the receipt, then the staged draft text. Owner: the gate host (vm2 or vm1), the patching agent for the push.
- Live PR bodies for SOLR-10364 (the "We" sentence) and SOLR-11356 (head citations, merge-base sentence, summary line 13, and later the staged text): receipt-grounded edits under the coordinator's authority, each verified by re-reading. Not performed here. No PR number for these two is recorded on pr-prepare, so the live body state was not compared.

## Escalations to the main agent

- E1. Record conflict on the SOLR-11356 push. `receipts/SOLR-11356.md` says `solr-11356-submit` was fast-forwarded to `6120dae28d04` and verified on 2026-10-13. `assignments/pool-docs-only-gate-rule.md` (owner decision, 2026-10-12) covers the SOLR-10364 and SOLR-11678 pushes and says that "SOLR-11356 and SOLR-12161 keep the fork-tip adoption from that round". `reports/docs-only-gate-rule-round-1.md` says of the packaging pushes: "Pushing them is a PR action, so it waits for the owner's yes." I found no owner yes for the SOLR-11356 push. This pass did not revert or act on it. The main agent should confirm or reverse.
- E2. Receipt wording, owed to the receipt owner, not edited here. `receipts/SOLR-10364.md` and `receipts/SOLR-11356.md` say `cabedd1d968` "is not on main's history", against `origin/main` (`e044bf20b405`). The PR base is upstream main, where `cabedd1d968` is the merge-base. The receipts should name both remotes.
- E3. Stale gate files: `gates/SOLR-10364.md` and `gates/SOLR-11356.md` still say the packaged head was "not pushed". The receipts record the pushes. The gate host should correct them; this pass did not edit gate files it did not write.

## Artifacts

- `reports/solrj-convergence-round-2-response.md` (this file).
- `pr-drafts/solrj/SOLR-10364.md` (line 27).
- `pr-drafts/solrj/SOLR-11356.md` (head re-point; lines 9 and 13).
- `pr-drafts/solrj/SOLR-14298.md` (line 39).
- `pr-drafts/solrj/SOLR-18341.md` (lines 47 and 69).
- `gates/SOLR-11356-sj-r1-49-header-copy.patch` (checked to apply to the pinned file with `git apply` in a scratch directory, LF endings; not built).
- `gates/SOLR-11356-sj-r1-49-header-copy.md` (gate job).

Working notes: `C:/Users/shaninna/AppData/Local/Temp/2/claude/C--Users-shaninna-dev-Solr-issues/1a4dfad7-722c-4e59-ab2e-961faf1441e1/scratchpad/` (the patch authoring copies).

## Worktree note

The worktree holds uncommitted changes from other lanes (build-docs, CLI and streaming drafts, gates and reports, and a streaming claim). None of them is from this pass, and none was touched. During this pass another lane committed on the worktree's detached HEAD (`5205dda997a` to `f62a86722ee`, the SolrCloud slate lane); that commit contains none of this pass's files. Nothing from this pass is committed.

## Not checked

- The gate logs (not on disk). The receipts' run counts were taken as written.
- The live PR bodies and the open PR state (no GitHub reads).
- The claim file (`claims/pool-category-closeout-w2-solrj-round-1.md`) was not updated: `claims/` is outside this pass's write set. The coordinator should mark its heartbeat and DONE.
