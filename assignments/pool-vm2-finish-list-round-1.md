# Assignment: vm2 finish list, round 1

Status: HANDED (vm2, 2026-10-20T10:38:57Z): section A is already complete by evidence (fork solr-8628-submit tip is 2e56ce9990, the packaged head; gates/SOLR-8628-packaging.md is DONE); section B's job gates/SOLR-5394-push-gated-head.md is not on coordination and the fork tip solr-5394-submit is 74532d0f76, not the gated head 6e4c302c4; section C waits on the owner decisions in assignments/pool-vm2-open-decisions-round-1.md and a follow-up gate job not yet filed.

Capability tags: [push, gate, implementation]. Intended host: vm2. Posted by the main side, 2026-10-14, at Nick's direction: post an assignment for what vm2 has not finished.

Claim by section: before starting a section, file a claim under `claims/` naming this assignment and the section letter (for example `claims/pool-vm2-finish-list-round-1-a.md`), per WORKFLOW.md. Sections are independent; take them in any order, one at a time. Heartbeat and staleness follow the pool rules.

Standing rules for every section: one Gradle build at a time under the blocking test-queue flock; test counts come from fresh JUnit XML, never from a log summary alone; a gate that executed tests and failed is never relaunched without a main-side investigation first (environment refusals that executed no tests may be relaunched once, and the refusal recorded).

## Section A: SOLR-8628, push the packaged head (gate already green)

State: `gates/SOLR-8628-packaging.md` is classified GATE GREEN at the packaged head `2e56ce9990b008c00d2d07b2498cf28cdc101a06` (main-side classification, 2026-10-14, recorded in that file). The packaged head is the single packaging commit that deletes `SOLR-8628-TESTING.md` on top of `5b606dc779d0`, and it exists only in vm2's gates worktree; the fork still stands at `5b606dc779d0` (verified by ls-remote on the main side, 2026-10-14). The receipt at the packaged head is already refreshed in `receipts/SOLR-8628.md` and records the push as outstanding. Do not re-run the gate; the remaining steps are:

1. Confirm the starting point: `git ls-remote` shows `solr-8628-submit` at `5b606dc779d0f367d7c8ac7235988a65f092b6a4`, and the local packaged head is `2e56ce9990b008c00d2d07b2498cf28cdc101a06` with parent `5b606dc779d0` and a diff against that parent of only `SOLR-8628-TESTING.md` (41 deletions). Stop and record here if any of that differs.
2. Push the packaged head as a plain fast-forward to `solr-8628-submit` (no force; ancestry allows it). Verify by ls-remote that the fork tip is now `2e56ce9990b008c00d2d07b2498cf28cdc101a06`.
3. Re-point the head citations in `pr-drafts/core-admin/SOLR-8628.md` from `5b606dc779d0` to `2e56ce9990` in the same pass, and commit that on pr-prepare. The draft text is otherwise unchanged; it does not cite the removed testing file.
4. Mark `gates/SOLR-8628-packaging.md` DONE (tick its last open checklist item) and mark its claim `claims/pool-vm2-gate-backlog-8628-packaging.md` DONE, recording the push and the ls-remote verification.

## Section B: SOLR-5394, push the gated head (already filed; pointer only)

No new work is defined here. `gates/SOLR-5394-push-gated-head.md` is already filed and OPEN for vm2: push the gated head `6e4c302c418be91b71305a1fe2f718e96295835f` to `solr-5394-submit` with the lease pinned to the current fork tip, re-point the draft PR's body to the new head, refresh `receipts/SOLR-5394.md`, and mark that job DONE. This section exists so the item sits on the finish list; claim and execute it under that gate file, not under this assignment, and note completion of this section in the claim for this assignment when that job closes.

## Section C: SOLR-17055, proof rerun, then the two owed follow-up tests

State: the fork tip of `solr-17055-submit` stands restored at `c2396f425fa2a67a40fea7afee341840c3896358` (the restore the proof rerun's Fork resolution section waits for is done; verified by ls-remote on the main side, 2026-10-14). The live gate item is `gates/SOLR-17055-sort-path-proof-rerun.md` (OPEN): gate the exact fix commit `2fe0986158391908ac89564090eb97e245788023` held in vm2's detached worktree `/workspace/gates/solr-17055-fix`, at the decided proof base (production at `c2396f425fa2` with the fix commit's test files overlaid; the sort assertion must fail there with the score-order case passing), then on green push the fix plainly as a fast-forward, refresh `receipts/SOLR-17055.md`, re-point the draft, and mark the gate jobs DONE, all as that file's On green section directs. Execute the rerun under its existing claim if one stands, or claim it fresh under WORKFLOW.md.

When the rerun has landed green and the fix commit is the branch head, two follow-up tests are still owed on that head, from the implementation-choice record (`reports/solr-17055-implementation-choice-round-1.md`, questions 2 and 3). Build them on top of the landed head:

1. An `id desc` case in `DistributedKnnTopKTest`. Reason: the pushed test's `sort=id asc` case does not prove that result order follows the requested sort, because in its fixture the scores coincide with id order; an `id desc` case discriminates, since a merge that ignored the sort and returned score order would fail it.
2. A pinning case for maxScore when sorting on score. Reason: the fix suppresses maxScore when scores are not requested (both candidate implementations chose this, and an earlier gate on this ticket failed on exactly that asymmetry); pin the complementary behavior, that maxScore is reported when the request sorts on score, so the suppression is a tested contract rather than an accident.

Gate both tests under the same job family (extend `gates/SOLR-17055-sort-path-proof-rerun.md` with a follow-up section, or file a sibling gate job naming this assignment): changelog parse if a fragment changes, tidy with the GATE INVALID guard, Error Prone compile, the focused class counted from fresh JUnit XML at seed `17055C0FFEE17055`, and module check `:solr:core:check -x test`. State the fail-before position for each new test honestly (what each does on the pre-fix production at the proof base) and record it from a run, not by argument. Push the landed commits plainly as fast-forwards, verify by ls-remote, and refresh `receipts/SOLR-17055.md` again at the final head. The HOLD at the top of `pr-drafts/search-components/SOLR-17055.md` lifts only after these tests are in and green; when they are, finish the draft steps the rerun job's On green section names (links re-pointed to the final head, Limits rewritten, hold comment deleted, draft re-read against the diff).

## Deliverable

Record each section's outcome in its claim file and in the gate or job file it names, with heads verified by ls-remote. When all three sections are closed, append a completion note to the claim for this assignment naming the final heads: `solr-8628-submit`, `solr-5394-submit`, and `solr-17055-submit`.
