# Pool assignment: vm2 open items handed to the review agent, round 1

Capability tags: `review`, `implementation`. Intended host: Windows (review agent). Claim under `claims/` before starting, per WORKFLOW.md. Windows runs no builds: where a verdict or a change needs runtime evidence, file a gate job under `gates/` for vm2 (or any Linux host) to claim, with the exact branch, head, base, module, test classes and seed, following the existing gate job shape. Posted by vm2 (gate backlog coordinator), 2026-10-14.

## Items

1. **SOLR-17055, sort-path fix (knn topK with a sort).** The premise is confirmed (`gates/SOLR-17055-sort-path.md`, "Premise run result"): with `sort=id asc` the distributed list holds 9 documents against a control of 3. Implement the fix on `solr-17055-submit` per the job's acceptance bar and suggested route, add the sort case to `DistributedKnnTopKTest`, and file a gate job for the new head. Push only as a plain fast-forward from `c2396f425fa2`, and only when the patch is ready to gate. Record each change in the job file.
2. **SOLR-15906, sort-paren fix.** Read `gates/SOLR-15906-sort-paren-fix.md`, write the fix it describes on its branch, and file its gate job.
3. **SOLR-11356, SJ-R1-49 header copy.** Read `gates/SOLR-11356-sj-r1-49-header-copy.md`. The job asks the claimant to write the header test (`testRequestsWithDifferentHeadersAreNotSentOnOneStream`) and the servlet recording. Write them, apply `gates/SOLR-11356-sj-r1-49-header-copy.patch`, and record each change. The fork push is the patching agent's step, as a plain fast-forward from `6120dae28d04`. Note: the job says the header test is timing-dependent; record the observed shares, not a deterministic claim.
4. **SOLR-16725, branch edits.** `gates/SOLR-16725-branch-edits-topup-gate.md` needs two edits: a raw-request test comment and a ClusterStatus javadoc. The records describe them but do not quote the text (`reports/core-admin-convergence-round-2.md`, line 22 and line 59). Draft the wording from the draft `pr-drafts/core-admin/SOLR-16725.md` and the code it describes, record the proposed text in the job file, and push only when the job file says the edits may land.
5. **SOLR-14919 precision and SOLR-17733 title, landed heads.** Per `gates/SOLR-14919-precision-round-1-gate.md` and `gates/SOLR-17733-title-narrowing-round-1-gate.md`, the patching agent applies each patch to its fork branch as one plain fast-forward (`solr-14919-submit` from `8f1f4ef91e`, `solr-17733-submit` from `636196b7954a`) and records the landed head (ls-remote) in the job file. The diff scope is already checked: 14919 changes the changelog and one test comment, 17733 changes only the changelog title. Once the landed heads are recorded, vm2 can gate them.
6. **SOLR-8628, stale claim.** `claims/pool-vm2-gate-backlog-8628.md` has no heartbeat since 2026-10-11 and `gates/SOLR-8628-packaging.md` is OPEN. Read both, check the job's steps, and record whether the claim is stale and what the job still needs. Do not take the claim over; vm2 decides that after a stopped-log check.

## Not in this assignment

- SOLR-17842 (docs-only fork push): needs the owner's decision on the push; the owner has been asked.
- SOLR-10667 and SOLR-12347: vm2 leaves them alone (npm cache issue, vm1 workaround).
- SOLR-18541 and SOLR-18539 review patches: handled by their own review rounds.

## Deliverable

`reports/vm2-open-items-handoff-round-1.md`: one section per item with its status, the changes made (before and after), the gate job filed (path and head), and any owner question. Commit as Nick Shanin, no trailers, no em dashes.

## Limits

No PR, comment, Jira or email. No force push. Fork pushes only where the item above names one.
