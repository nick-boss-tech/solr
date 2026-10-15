# Standing calls, round 1

Assignment: `assignments/pool-standing-calls-round-1.md`, items 1 to 4 (the brief's scope). Claims: `claims/pool-standing-calls-round-1-1.md` to `-4.md`. Host: Windows review agent.

Status: DONE. All four items settled under the decision ladder. No item returns to Nick. Each settled item still needs Nick's explicit approval for anything public (see Pending approval).

Limits held: no build, Gradle, test, gate or test-queue run. No GitHub or JIRA write, no PR state change, no PR body edit, no mail. No branch was committed, pushed, checked out or reset. In the source checkout, only read commands ran, plus `git fetch` of four fork refs (`solr-12161-submit`, `solr-17866-submit`, `solr-17393-submit`, `solr-9637-submit`), which moves remote-tracking refs only. Read-only `gh pr list` searched PR titles and bodies.

Item 5 (SOLR-15358, docValues as stored) is in the assignment but not in this round's brief (items 1 to 4), so it was not worked.

## Tip check (2026-10-15)

| Branch | Live tip | Matches assignment |
|---|---|---|
| solr-12161-submit | cdd265168a2adc76fb788f3d1f5587a3eee05e42 | yes |
| solr-17866-submit | 3f9367d796a6446fa2f0549d8e9763e07831e6e8 | yes |
| solr-9637-submit | c50fa4ffd932ede86c36b0748ee6aabe34d30e0a | yes |
| solr-17393-submit | 626241e647e5fcc5ac6cc485f6a96d4ffe12e00a | yes |
| solr-16290-submit | ff760120c81792022458a5a290b6e58d57b9cd17 | yes |

`upstream/main` is the ref already in the source checkout (`7767d36d29fd`). It was not re-fetched.

---

## Item 1: SOLR-12161, which state governs

**Verdict: READY TO OPEN stands. The census governs.** Recommendation only: opening stays with Nick's explicit approval.

**Rung:** settled on rung 1. The record already answers the question.

**Settling evidence**

- The awaiting-pipeline state is a register state. `branch-focus-inventory-2026-10-08.md` line 575 defines the awaiting-pipeline rows as registered hypothetical-reproduction fixes whose tests have not run. Line 576 notes that SOLR-12161's live tip is newer than its register. The row is at line 423.
- The run that the state waited for has happened. `gates/SOLR-12161.md` is DONE (vm2, 2026-10-11T05:58Z, gate green at `cdd265168a2a`). `receipts/SOLR-12161.md` records the pin verdict PIN and two runs with 1 test, 0 failures, 0 errors, 0 skipped: the branch test at head `1725cbd8489`, and the same test at base `cabedd1d968`. Its gate steps are all green (tidy, Error Prone compile, focused test, module check), in a window of 05:49Z to 05:58Z.
- The gated head is the live tip. The tip `cdd265168a2a` is the gated head. `git diff 1725cbd8489 cdd265168a2a` is one file, `SOLR-12161-TESTING.md` (27 deletions), so the packaging commit removes only the handoff note. `git diff --stat cabedd1d968 origin/solr-12161-submit` is one file, `BasicAuthIntegrationTest.java`, 15 insertions. The change is test-only.
- The draft agrees. `pr-drafts/security/SOLR-12161.md` names `cdd265168a2a` in the link (line 15) and the Proof (line 21). `reports/security-convergence-round-2-response.md` records SEC-R1-09 as CONVERGED at the gated head (line 19) and SOLR-12161 as CONVERGED (line 22).
- The morning slate agrees. `reports/morning-openings-slates-2026-10-14.md` line 369 lists the branch in the Security slate as openable as it stands.

**The intake pause**

- No text on record describes the pause. I searched the pool branch markdown and the local `research/` and `research/pipeline/` files for "intake", "pause" and "throttle". The only mention is the assignment's own description: "the pause was Nick's own throttle on intake".
- The branch was admitted before the pause could have applied to it. Its register entry is in `research/pipeline/queue.json` (local, not on the pool branch): audit verdict "branched", dated 2026-10-06, evidence commit `a40db4fdb5c4` (lines 5389 to 5405). The 2026-10-08 inventory carries it as a new row.
- A throttle on intake governs new admissions. It does not hold a branch that was admitted and whose run has finished. The run finished on 2026-10-11.
- **Finding:** the pause does not reach this branch. This finding rests on the assignment's description of the pause as a throttle on intake. If Nick reads the pause as covering completed runs too, the opening approval is the place to say so.

**Stale record, not edited:** the local `research/pipeline/queue.json` entry (line 5396) still reads "test unrun" and status "implemented-hypothetical". The main side owns that file, and it is outside the pool worktree.

**What changed**

- `reports/openings-census-round-1.md`, the SOLR-12161 row (line 343): the Note column now records this settlement. The disposition stays READY TO OPEN. The census totals are unchanged.
- `pr-drafts/security/SOLR-12161.md`: no edit. It already matches the settlement.

---

## Item 2: SOLR-17866, the submit-ahead-of-design flag

**Verdict: waits for the design.** The branch is not submitted. The flag still describes the branch, so it is not cleared.

**Rung:** settled on rung 4. The choice between the implicit and the explicit shape is a genuine choice between defensible behaviors, so it is posed as a Choice in the draft. Submission is held until the Choice is answered, which is the harder option under rung 2.

**Settling evidence**

- **The design step is on the ticket.** `research/jira-context/SOLR-17866.json`, the comment of Jason Gerlowski on 2025-08-21, proposes a variant constructor that takes `requiresCollection`, with the setter and the other constructors deprecated. He says he will aim to work on a PR for that approach. Chris Hostetter's strawman (2025-08-20) and David Smiley's comments (2025-08-18) point the same way.
- **The branch takes another route.** At `3f9367d796a6`, `SolrRequest.process` calls a new protected hook, `collectionProvided` (`SolrRequest.java` lines 308 to 312), and `GenericSolrRequest` sets `requiresCollection` in it unless the setter was called (`GenericSolrRequest.java` lines 109 to 113). This implicit route is not the one the ticket thread names.
- **Upstream check (read only).** `upstream/main` at `7767d36d29fd` has no constructor that takes `requiresCollection` (`git grep` of `GenericSolrRequest.java`). The recent history of `GenericSolrRequest` on main is PR #3423 (`9a1c5200d46`, in base, which kept explicit `setRequiresCollection(true)` calls at call sites and added request types) and PR #3955 (merged 2025-12-28, which replaced most uses of the class). A search of PR titles and bodies for "GenericSolrRequest" found no open PR for the constructor. The search did not cover other wording. The design step is still open.
- **The draft understated the cloud effect.** At the head, `CloudSolrClient.java` line 950 is `else if (!request.requiresCollection())`. The branch sets `requiresCollection` for any non-V2 request that names a collection without an explicit setter. Such a request therefore takes the collection branch (lines 956 to 963), and the same branch exists at base `97d973814336` (line 950). The admin-only effects are the admin check (`CloudSolrClient.java` line 639) and the load-balancing retry decision (`LBSolrClient.java` line 575). The draft's Limits said "admin-typed requests" only.
- **Round-1 record.** SJ-R1-73 (sticky flag, should-fix) and SJ-R1-74 (constructor, should-fix) were applied to the draft. SJ-R1-75 (the owner flag stands) is at `reports/solrj-convergence-round-1.md` lines 115 to 117. `reports/solrj-clients-round-1-s5.md` line 82 recommends checking upstream first, then fixing the side effects, then deciding, and not submitting before the upstream check. The upstream check is done, and it shows the design step is open.

**Missing design step and where it happens**

- Answer on SOLR-17866 choosing among (a) the public field with a documented setter, which this branch implements, (b) a private field, and (c) the explicit constructor the ticket proposes. The answer comes from the ticket thread or the owner's ruling. Any ticket comment is a public write and needs Nick's go-ahead first.

**Direction recorded, not implemented**

- If (c) is chosen, the hook and the implicit path are dropped, and the test is rewritten against the constructor.
- If (a) or (b) is kept, the branch first makes the flag non-sticky after `process`, and forwards the hook through `WrappedSolrRequest`, as the S5 part recommends. It then needs a gate.

**What changed**

- `pr-drafts/solrj/SOLR-17866.md`, Choice (line 33): the closing question now asks the shape question (public field with setter, private field, or explicit constructor as the ticket proposes), not only whether the field choice was right.
- `pr-drafts/solrj/SOLR-17866.md`, Limits (line 42): the cloud-client bullet now says that any non-V2 request that names a collection without the setter takes the collection path (`CloudSolrClient` line 950), and that the admin-only effects are lines 639 and 575. Links use the head `3f9367d796a6`.
- `reports/solrj-convergence-round-2-response.md`: appended section "SOLR-17866: standing call settled" (the SolrJ category record). It supersedes SJ-R1-75 for the opening decision.
- The SOLR-17866 census row (SolrJ section, OWNER ITEM) is not edited. The settlement is in the SolrJ record above, which the next census refresh can cite.

---

## Item 3: SOLR-9637, submission shape against SOLR-17393

**Verdict: the ratified shape is confirmed.** SOLR-17393 submits first, with the merge-only scope. SOLR-9637 follows as its own PR after SOLR-17393 lands, rebased onto the landed tip. No fold into one PR. No rework onto plain main.

**Rung:** settled on rung 1 (the facts at the tips answer it), with the cost comparison as the reasons.

**Settling evidence**

- **The rule on record.** `assignments/pool-draft-fidelity-suggester.md` line 34 records the ratified decision: 9637 opens as its own PR after 17393 lands, rebased onto the landed tip. The owner ratified the merge-only scope for 17393 on 2026-10-09, with the per-shard top-count cut as the named follow-up (`assignments/pool-census-followup-suggester-round-1.md` line 22).
- **17393 is still merge-only.** At `626241e647e5`, `git diff --stat c3cdf7b46e8c` shows three files: `SuggestComponent.java` (29 lines), `SuggestComponentMergeTest.java` (+73) and the changelog fragment (+8). The handoff doc was removed at `063a040e1d7`. The receipt is green at the tip (`receipts/SOLR-17393.md`).
- **The 9637 stack sits on 17393, but on an earlier commit.** The merge base of the two branches is `dd6c82924fff`, the 17393 handoff-doc commit. Two 9637 commits sit on it (`872c8130da2`, `c50fa4ffd93`). Against the 17393 tip, the 9637 branch changes 18 lines in `SuggestComponent.java` (16 added, 2 removed) and adds 19 test lines. Its delta is 80 insertions and 2 deletions across four files, including the handoff doc.
- **The rebase is clean.** A dry run with `git merge-tree --write-tree` (no ref or worktree change) of `872c8130da2` onto the 17393 tip, with merge base `dd6c82924fff`, reports no conflict. A dry run of the whole 9637 branch onto `upstream/main` is also clean. `SuggestComponent.java` has no upstream commits since `c3cdf7b46e8c` (range log is empty), so there is no drift.
- **The handoff doc drops out on rebase.** `SOLR-17393-TESTING.md` is in the 9637 history from `dd6c82924fff`, and it is absent at the 17393 tip. A rebase onto the tip drops it from the PR file list. The current 9637 tip still carries it, so the rebased branch needs a file-list check before any PR.

**Costs at these tips**

- **Rebase:** no code step at these tips. The rebased head needs its own gate, because the 9637 proof ran on the stacked base. The draft says so.
- **Fold:** one PR for two tickets. It would put 9637's dedupe-and-sum behavior into the PR the owner ratified as merge-only, and it needs a combined gate.
- **Rework onto plain main:** 9637's change sits on 17393's merge rewrite (its delta is measured against the 17393 tip). A plain-main branch would rebuild that rewrite inside 9637. That is a fold by another name, plus a new gate.

**What changed**

- `reports/suggester-round-4.md`: appended section "SOLR-9637 stacking: settled" (the suggester category record). It closes the round 4 item 8 call.
- `pr-drafts/suggester/SOLR-9637.md`: no edit. Line 36 already says the PR opens after 17393 lands, rebased onto the landed tip, and that the proof ran on the stacked base.
- The census row for SOLR-9637 (`reports/openings-census-round-1.md`) still reads OWNER ITEM. This round did not edit it or its totals. The main side may move it to HOLD (behind the SOLR-17393 landing) at the next refresh.

---

## Item 4: SOLR-16290, disposition of a gated pin

**Verdict: fund the real fix.** Not a pin-only PR, and not banked. The pin stays on `solr-16290-submit` as the regression test for the fix. The next step is a fix job on that branch (implementation, Linux host). This assignment does not build it.

**Rung:** settled on rung 2 (the hard option, the fix, over the pin or the bank). Nothing on record scopes the fix or caps its cost, so the scope below is only what the record supports. The design of how exclusions compose with block-join domains is a Choice for the fix's draft, not settled here.

**Settling evidence**

- **What the pin buys alone.** The pin is `@AwaitsFix` (`TestJsonFacetsWithNestedObjects.java` line 355 on the branch). Per `reports/search-components-1-f5.md` (line 62), it expects the control's output. So it records the defect and fixes nothing. A pin-only PR would be public without a resolution, and the reported query would still return empty child buckets.
- **The defect is on record.** `receipts/SOLR-16290.md`: the premise run at base `0cc328310f8` fails exactly 1 of 9 with awaitsfix enabled (`testNestedChildFacetWithExclusionKeepsBlockChildrenDomain`, nested buckets empty for all three author buckets). The control passes. Facet code on main has had no commit since that base: `git log 0cc328310f8..upstream/main -- solr/core/src/java/org/apache/solr/search/facet/` is empty (65 commits in total, none there). So the premise is expected to hold on main. It was not run here.
- **Where the fix sits.** The record supports this scope, not a root cause. `solr/core/src/java/org/apache/solr/search/facet/FacetProcessor.java` at base: `handleFilterExclusions` (about lines 190 to 222) rebuilds the base from the query and the filters minus the excluded tags, and `handleBlockJoin` (about lines 255 to 290) applies the parent-to-child transform. The question is their order and interaction for a child facet under a block-join domain. `FacetParser.java` lines 209 to 245 parse `excludeTags` and `blockChildren`. The pin's failing line was not traced. That trace is the fix's first step.
- **Overlap.** No shared file with SOLR-12556 or SOLR-17051 at their tips (`reports/search-components-1-f5.md`, note 11). Other open facet branches were not checked.
- **Cost.** Not estimated on record. The fix needs a design choice and a gate with a fail-before proof. The premise leg is already recorded.
- **What banking loses.** The only public record of a reported wrong-results bug open since 2022 (the packet is `research/jira-context/SOLR-16290.json`: created 2022-07-13, no comments). It also leaves the reported query broken. A bank keeps the reproduction local only.
- **Why not the pin alone.** Its value is a visible record, and the fix is the only disposition that ends the item.

**Follow-up named in the disposition**

- "SOLR-16290 fix, funded 2026-10-15." Acceptance: the `@AwaitsFix` pin passes with the annotation removed, the control test still passes, and the facet suites stay green at the gate. The pin ships inside the fix PR, not as a pin-only PR. The gate carries the fail-before proof.
- Next step, one line: a fix job on `solr-16290-submit` (implementation, Linux host). The branch stays unopened until that gate is green.

**What changed**

- `receipts/SOLR-16290.md`: one disposition line appended (fund the real fix; pin stays as the regression test; no pin-only PR, no bank; next step).
- `reports/branch-review-verdicts-landing-round-1.md`: appended section "SOLR-16290 disposition: settled" (the landing record), with the scope above.
- The census row (`reports/openings-census-round-1.md`, OWNER ITEM) and the morning slate entries (`reports/morning-openings-slates-2026-10-14.md` lines 431 and 586) still read OWNER ITEM. They need the main side's refresh. This round did not edit them.

---

## Files written or edited (absolute paths)

- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\reports\standing-calls-round-1.md` (this report, new)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\reports\openings-census-round-1.md` (SOLR-12161 row, Note column only)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\pr-drafts\solrj\SOLR-17866.md` (Choice question line 33; Limits cloud bullet line 42)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\reports\solrj-convergence-round-2-response.md` (appended SOLR-17866 settlement)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\reports\suggester-round-4.md` (appended SOLR-9637 settlement; one clarifying edit to the appended text)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\receipts\SOLR-16290.md` (one disposition line appended)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\reports\branch-review-verdicts-landing-round-1.md` (appended SOLR-16290 settlement)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\claims\pool-standing-calls-round-1-1.md` to `-4.md` (closing notes appended; DONE marks left to the coordinator)

No draft or record for SOLR-12161, SOLR-9637 or SOLR-16290 was changed beyond the lines named above.

## Pending approval (nothing posted)

- Opening SOLR-12161 (READY TO OPEN, recommendation only). Needs Nick's explicit approval.
- Any JIRA comment on SOLR-17866 or GitHub comment on any PR. None drafted. If the owner answers the design step on the ticket, a comment would be the next public write, and it needs Nick's go-ahead.
- Posting the SOLR-16290 fix job, and the census and slate refresh for items 2 to 4. Both are pool or main-side steps, not done here.

## Could not verify

- The text of the intake pause. None found on record (search described under item 1). The finding depends on the assignment's description.
- The pin's failing line, the fix's cost, and the premise on main. No run or build was allowed.
- Whether other open facet branches share code with the 16290 fix.
- How CloudSolrClient behaves at base for a named-collection GenericSolrRequest (what the node branch sends in practice). The code shows the branch change, not the runtime effect.
- The PR search covered titles and bodies with the word GenericSolrRequest. Other wording was not searched.
