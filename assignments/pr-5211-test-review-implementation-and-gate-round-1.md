# Assignment: PR 5211 test review, implementation and Linux proof

Status: OPEN

- Posted: 2026-10-24, by the Windows review chat at Nick's explicit request.
- Capability tags: phase I `implementation, review, draft`; phase G `gate, review, draft` on Linux.
- Intended host: any capable implementation host for I; any capable Linux host for G. Windows must not run Gradle or inspect the retired local test queue.
- Authority: Nick asked for a proposed implementation and Linux proof so the PR comment can include the actual diff. The diff link is explicitly requested for this comment, overriding the formula's default against unsolicited diff links. No public PR/JIRA comment or PR creation is authorized.
- Target PR: https://github.com/apache/solr/pull/5211 (Jason Gerlowski's PR, not Nick's).
- Ticket: SOLR-18569.
- Reviewed PR head: `6f72e0cac151d139626299a680f90a7c9c123ceb`.
- PR branch: `gerlowskija/solr:SOLR-18569-wait-for-node-role-deletion-on-startup`.
- Upstream merge base: `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Proposed work branch on nick-boss-tech/solr: `solr-18569-pr5211-test-review`. Confirm it is absent before creating it; if already present, inspect its owner/claims and reuse only if it belongs to this assignment.

## Bootstrap and ownership

Read OPEN-WORK.md, WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, comment-formula.md, pool/decision-map.md, material/decision-ladder.md, the host capability file, and pool/owner-complaints-2026-10-19.md. Record one add-only claim per phase. Check all claims and deliverables for this assignment before work; first pushed claim wins. No shared index edits.

The generated OPEN-WORK.md can be stale; this Status and the phase dependencies govern. Phase I is claimable now. Phase G is not an executable gate until the implementation report identifies a pushed exact head and a new phase-specific gate file pins that head. It can be carried out by the same capable Linux host under a separate claim and the host's serialized build lock.

Read the live PR and JIRA. Main and 10x already clean role entries on shutdown; 9x and 9_11 still need that separate change. This assignment is a test-only suggestion for the main PR, not a competing fix or a backport. If the PR head moves, inspect the change and record a new assignment/supersession rather than silently changing the pinned baseline.

## Finding and bounded implementation (phase I)

At the reviewed head, `solr/core/src/test/org/apache/solr/cloud/ZkControllerTest.java` lines 247-254 call checkForExistingEphemeralNode, join the deletion thread in finally, then assert absence. Immediate return while the leftover still exists can pass: cleanup deletes the entry before the assertion. This is a test-sensitivity finding; no production defect was found in this PR.

Create a clean isolated worktree from the exact PR head, fetched through `apache/solr`'s `refs/pull/5211/head`. Do not use branch-work.ps1 new, which starts from an existing Nick fork submit branch. Do not edit the foreign PR branch or the dirty source checkout. Preserve the original contributor's commits; new commits are by Nick Shanin, with no attribution trailers.

Expected outbound delta: only `solr/core/src/test/org/apache/solr/cloud/ZkControllerTest.java`. Keep the existing contributor's changelog unchanged; a test-only suggestion needs no second fragment. Changes outside this file need a recorded, evidence-based scope decision before implementation.

At minimum, assert absence immediately after the check returns, before joining the worker for cleanup. Prefer coordinated worker/check execution that establishes the check remains pending while leftovers exist and completes after deletion, instead of depending only on a sleep. Surface worker exceptions and bound cleanup waits. Do not introduce a public production hook, unrelated test refactoring, or production changes solely to make the test easy. Use the smallest convincing test.

Review the delta, commit, push normally to Nick's work branch, and read back the fork SHA. No force push. The upstream PR is unchanged. The branch is Implemented, Proof Pending until phase G succeeds.

Deliver `reports/pr-5211-test-review-implementation-round-1.md` with original PR head, new full SHA, author, exact file list, rationale, line citations, diffstat, a GitHub compare URL from the reviewed PR head to the new SHA, and the next gate path. Include or link a plain patch of ONLY the incremental delta. Do not claim tests ran on Windows.

Then add an executable gate job at `gates/SOLR-18569-pr5211-test-review-<head12>.md` that pins the actual branch/head, focused class, seed and proof shape below. Before posting, check live claims and receipts; never move a claimed gate head. If a new head is needed, post a superseding job rather than editing an immutable assignment or gate.

## Required Linux proof (phase G)

The following are task requirements to transfer to the exact-head gate, not permission to run an unbound job. Use the host's supported gate runner and local build lock. Detached formatting preflight precedes the gate claim.

- Module: `:solr:core`.
- Focused class: `org.apache.solr.cloud.ZkControllerTest`.
- Sensitivity method: `testStartupWaitsForLeftoverEphemeralNodesToDisappear`, or its explicitly documented replacement.
- Seed: `18569C0FFEE01`.
- Format: tidy; Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`); module check (`:solr:core:check -x test`).
- Clear prior results before every proof leg using the runner's supported `:solr:core:cleanTest` step. Count fresh JUnit XML; record exact test filters, seed, start/end times, exit codes, expected failures, host and raw log paths.

Run three discriminating legs in isolated trees:

1. **Original-test control:** at PR head `6f72e0cac151d139626299a680f90a7c9c123ceb`, replace only the body of the package-private checkForExistingEphemeralNode with immediate return (empty body). Run the original positive method alone. Expected: it passes, demonstrating the original test's weakness. This is a synthetic local mutation, not a code change to push.
2. **Strengthened-test negative control:** same production mutation, with only the assigned test delta overlaid. Run the strengthened positive method alone. Expected: assertion failure caused by premature return / leftover still present, not a compile, harness, worker, or timeout failure. If it passes, improve the test and rebind proof to the new pushed head.
3. **Unmutated final head:** clean tree at the pushed implementation SHA; run the whole focused class. Expected: pass. Complete the named formatting, compile and module checks at this same final head.

If leg 1 fails for the expected assertion, correct the original review claim and stop labeling it reproduced. If leg 2 fails for unrelated reasons, diagnose the cause; it is not sensitivity proof. If either run never executes tests, record that and use the documented environment-relaunch rule. Do not silently drop an acceptance leg.

If tidy changes the patch, the former proof does not cover the repaired head. Commit and push a narrow formatting repair to Nick's work branch, create a superseding exact-head gate, and repeat the required proof on the final clean head. No claim may report green for an earlier SHA.

Deliver `reports/pr-5211-test-review-gate-round-1.md` and the gating host's `receipts/SOLR-18569.md` if no other active job owns it. If another receipt owner exists, use a new named report and a non-conflicting ticket/phase receipt path under WORKFLOW.md; never overwrite another host's receipt. Include all three proof outcomes and the exact final head. A failed leg produces a report with the failure, not a green receipt.

## Comment and diff deliverable

After green, add `drafts/pr-5211-review-with-diff-round-1.md`. Follow comment-formula.md. Open with the GitHub AI disclosure header, mention @gerlowskija, explain the join/assertion ordering, link the incremental compare/commit diff, and state the mutation-control and final class results at their exact SHAs. Name any limits. Use plain public language: no gate, receipt, ledger or internal process vocabulary; no em/en dashes; no checklist. Do not claim a production defect. End with a short question asking whether the test-only suggestion is useful here.

The draft is not ready while it contains placeholders, an unpushed diff, or results from the wrong head. Before writing it, reread the live PR head and discussion. If upstream absorbed the suggestion, report that instead of proposing a duplicate. No comment, review submission, JIRA write, PR creation, or foreign-branch push is authorized. Nick reviews the concrete diff and comment before any public post.
