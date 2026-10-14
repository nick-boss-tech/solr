# Streaming convergence, round 2: response (W2 close-out)

Assignment: `assignments/pool-category-closeout-overnight-round-1.md`, W2, under `assignments/pool-category-convergence-round-2-settlements.md`. Claim: `claims/pool-category-closeout-w2-streaming-round-1.md`. Interim: `reports/streaming-convergence-round-2.md`. Round 1: `reports/streaming-convergence-round-1-response.md`. Review side, Windows host. No build, test, Gradle or gate run on this host. Git was read-only (`git show`, `git cat-file`, `git merge-base`, `git rev-list`, `git ls-remote`). No push, commit, GitHub write or claim edit.

## Live tips and receipts at the tip

| Ticket | Live fork tip (ls-remote, 2026-10-13) | Receipt at the tip | Interim report said |
|---|---|---|---|
| SOLR-12505 | `1a1875d95f3680c20447006efc3c949c57f5135f` | GATE GREEN, `receipts/SOLR-12505.md` section "Gate at 1a1875d95f" | `5f20e1171bc` (superseded) |
| SOLR-10882 | `b12b0ffab487e88a790c0d6dab31bd4a7464be47` | GREEN, title re-gate, `receipts/SOLR-10882.md` "Current head" | `5d7d07a5b17` (superseded) |
| SOLR-14231 | `593463f398cb96a50ea9ab4a70b26c0b9a5b1599` | GATE GREEN, `receipts/SOLR-14231.md` "Re-gate at 593463f398" | `2f73d00a399` (superseded) |
| SOLR-12657 | `23cc27a9892594b96a689fa4cf0ed768792bc678` | unchanged; not re-audited | matches |

Each tip was checked against its receipt and against the head the draft cites. The tip changes after the interim report are title-only (`git diff --stat`: changelog fragment only), so the code evidence carries over.

## Mapping correction

The assignment and the claim list ST-R1-08 and ST-R1-09 under SOLR-12505. The register and the round 1 response place them on SOLR-10882 (its bold lines, Limits and Proof). ST-R1-23 is the SOLR-12505 item. This response settles each item on its register ticket. Decision D0: the register mapping stands; the assignment label is wrong. ST-R1-27 stays rebutted and was not reopened.

## Remainders, one line each

| Ticket and item | Final state | Rung that settled it | Artifacts |
|---|---|---|---|
| SOLR-12505, ST-R1-23 (`on=` field name) | CONVERGED on the code. The check is in place at the tip, with a test and GATE GREEN. | Do the hard thing: validate route (reject the name at construction), gated at `1a1875d95f`. | None new. Draft Limits and "What this change does" checked against the route and the lines at the tip (FetchStream 153 to 159 and 255 to 257; the defType test at 951 to 1006 is unchanged since `5f20e1171bc`, and the new join-name test is at 1009 to 1036). |
| SOLR-10882, ST-R1-08 (null-value claims and changelog title) | CONVERGED. Draft text applied in round 1; the branch title at the tip is the recorded replacement verbatim. | Do the hard thing: branch title patch, re-gated at `b12b0ffab487` (changelog, tidy, compile, focused 7 of 7, module check). | `pr-drafts/streaming/SOLR-10882.md`: links at lines 15 and 23 re-pointed from `5d7d07a5b17` to `b12b0ffab487` (no code changed between the two; `git diff --stat` is the changelog fragment only). |
| SOLR-10882, ST-R1-09 (proof date) | CONVERGED. The date is now carried, from the receipt's gate window. | Get information: record check. The receipt's gate window (2026-10-13 00:09 to 00:23 UTC, 2026-10-12 18:09 to 18:23 MDT) encloses the focused run at the tip. D4 below. | `pr-drafts/streaming/SOLR-10882.md` Proof line 24: head `b12b0ffab487`, "verified 2026-10-12 18:09 to 18:23 MDT (2026-10-13 00:09 to 00:23 UTC)". The draft had `5d7d07a5b17` and "2026-10-11". |
| SOLR-14231, ST-R1-33 (changelog title) | CONVERGED on text. The title at the tip is the recorded replacement verbatim; the draft's ST-R1-32 wording is present and carries no run date. | Do the hard thing: title patch, re-gated at `593463f398` (changelog, tidy, compile, GraphExpressionTest 8 of 8, ScoreNodesStreamTest 1 of 1, module check). | None new. Draft unchanged. |
| SOLR-14231, fail-without-fix run (owner item on the record) | HOLD for the opening, not for the text: owed gate. The draft already says "not run". | Do the hard thing: gate job posted to run the proof leg (D5). | `gates/SOLR-14231-fail-before.md` (OPEN, gate tag). Reason: WORKFLOW's fail-before rule for tickets that add tests; the receipt records no fail-before run for the new tests. |
| SOLR-10882, Choice (error versus fixed order) | CONVERGED. The implemented error route stands; the fixed order is a named follow-up. | Optional: defer as a named follow-up in the Choice (D1). | None. Draft unchanged. |
| SOLR-12505, StreamFactory cause text (owner item 3) | CONVERGED. Limits line kept, with the follow-up named and offered. | Optional: defer as a named follow-up in Limits (D2). | `pr-drafts/streaming/SOLR-12505.md` Limits bullet 2: "Fixing that is outside this change; a follow-up ticket and PR can be opened on request." |
| SOLR-12505, merge-base recount (owner item 7) | CONVERGED. 77 commits behind `upstream/main` at `d38e0f1623728`. | Get information: `git rev-list --count 14c7aac0d151..d38e0f16237` on the local `upstream/main` ref (D3). | `pr-drafts/streaming/SOLR-12505.md` Limits bullet 6: "as counted on 2026-10-13". |
| SOLR-12505, quote and backslash names (owner item 5) | CONVERGED. The draft claims no base behavior for them; it says they are not tested. | Get information: draft read against the code at the tip. | None. |
| SOLR-12505, defType premise (test "as it stands at 5f20e1171bc") | CONVERGED. The test body is identical at the tip (the only hunk is the new test). | Get information: diff against `5f20e1171bc`; premise run recorded in the receipt's earlier section. | None. |
| SOLR-12505, Choice (defType versus terms parser) | HOLD, owner. | Owner item (the caller's routing; the Choice is a live alternative under pr-formula). Not decided here. | None. |
| SOLR-12505, Unicode whitespace (owner item 1) | HOLD, owner. The draft's wording is the "ASCII whitespace" reword. | Owner item (the caller's routing). Note: the decision default's harder option is to widen the check with `\p{javaWhitespace}`. That is a code change and needs its own gate, which is not in this pass. | None. |
| SOLR-12505, changelog title (owner item 4) | HOLD, owed gate. The title still describes only defType. | Do the hard thing: `gates/SOLR-12505-changelog-title.md` (HOLD). The title's first clause depends on the Choice, so the job does not open before the Choice is ruled. | `gates/SOLR-12505-changelog-title.md`, proposed title in the file. |
| SOLR-12505, JIRA attributions (owner item 6: Bernstein, Smiley comment 16519637, Pugh) | HOLD, owner. | Get information failed: no JIRA access on this host, and JIRA text goes through Nick. | None. The draft carries the attributions unchanged. Verify against JIRA before the owner posts. |
| SOLR-12657 | CONVERGED (round 1). Not reopened. ST-R1-27 stays rebutted. | Round 1 record. | None. |

## Holds in one place

- SOLR-12505 is not ready to open. Holds: the Choice (owner), the Unicode whitespace decision (owner), the JIRA attributions (owner, verify against JIRA), and the changelog title (owed gate, HOLD until the Choice is ruled). The code and the proof at the tip are settled.
- SOLR-14231 is not ready to open until the fail-without-fix gate reports (`gates/SOLR-14231-fail-before.md`).
- SOLR-10882 is CONVERGED. Opening-time steps only: rebase before opening, under owner decision 5 as settled in round 1 (ST-R1-12). That is not a draft edit.

## Decisions recorded (decision default)

- D0. Ticket mapping: ST-R1-08 and -09 are SOLR-10882; ST-R1-23 is SOLR-12505. The assignment and the claim label are wrong. Settled on the register.
- D1. SOLR-10882 Choice: the error route stands, because it is what the gated code does. The fixed-order route would add an ordering the project must define, so it is a named follow-up in the Choice, not built here.
- D2. SOLR-12505 StreamFactory (owner item 3): the Limits line stays, with the follow-up named and offered. No new Jira ticket, since new tickets need Nick's direction.
- D3. SOLR-12505 recount (owner item 7): 77 commits behind `upstream/main` at `d38e0f16237`, counted 2026-10-13 against the local ref. The posting-time check stays with the owner's posting step.
- D4. SOLR-10882 date: the receipt's gate window, shown in the house style (MDT with the UTC pair), as in `reports/mergeids-pr-draft-text-round-1.md`.
- D5. SOLR-14231 fail-without-fix run (listed as an owner item on the record): taken as an owed gate under the harder option. Posted, not run here.
- D6. SOLR-12505 changelog title: the proposed text is in the HOLD job, which waits on the Choice. The title was not changed on the branch (no push in this pass).

## Artifacts

- `pr-drafts/streaming/SOLR-10882.md`: Proof date and head (line 24), two changelog-style links re-pointed to `b12b0ffab487` (lines 15 and 23).
- `pr-drafts/streaming/SOLR-12505.md`: Limits, StreamFactory follow-up (bullet 2); Limits, recount (bullet 6).
- `pr-drafts/streaming/SOLR-14231.md`: unchanged; its "not run" sentence stays accurate until the gate reports.
- `gates/SOLR-14231-fail-before.md`: new, OPEN, gate tag.
- `gates/SOLR-12505-changelog-title.md`: new, HOLD.
- `reports/streaming-convergence-round-2-response.md`: this file.

## Notes for the coordinator

- The claim `claims/pool-category-closeout-w2-streaming-round-1.md` should be marked DONE by the coordinator (this review did not edit `claims/`).
- The three gate jobs from ST-R2-01 (`gates/SOLR-12505-fetchstream-fix.md`, `gates/SOLR-10882-title-fix.md`, `gates/SOLR-14231-title-fix.md`) are DONE. The `82088f66784` record note is resolved.
- `pr-drafts/streaming/SOLR-12505-candidate-round-1.md` is a staging file, not the live draft. It still carries bracketed placeholders (`[N]`, `[Owner decides ...]`, `[Confirm after the full run.]`) and must not be posted from. Not edited.
- The worktree is shared. `git status` also shows uncommitted edits by other lanes (`pr-drafts/build-docs/*`, `pr-drafts/cli/*`, untracked `gates/SOLR-17842-placeholder-fix.patch`). This review did not touch them and does not own them.
- The fork has a branch `ci/12505-stream` at `b673677a4afabd307fd946f6a9a4035e13de7fcf` (ls-remote). It is in no record. Named for the owner; not touched, since deletions are reserved to Nick.

## Not checked

- Gate logs and JUnit XML (not in this worktree). Counts rest on the receipts.
- JIRA text and the attributions in the SOLR-12505 draft (no JIRA access on this host).
- The base-run log for the defType test (`logs/vm2/queue-g12505-basecheck-20261013T162852Z.out`); the premise rests on the receipt's earlier 5f20e117 section and the unchanged test body.
- Upstream freshness: the recount uses the local `upstream/main` ref, which is at `d38e0f16237`.
