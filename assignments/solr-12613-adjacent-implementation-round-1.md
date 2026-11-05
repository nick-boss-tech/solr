# Assignment: adjacent node-routing follow-up implementation, round 1

Status: OPEN (implementation authorized; Linux proof required).

- Owner direction: Nick set the active chat goal to implement the follow-up plan with code and tests, allowing separate branches. This releases the earlier implementation hold in `assignments/solr-12613-adjacent-followup-plan-round-1.md` for this work.
- Intended host: Windows for source/test implementation and fork pushes; capable Linux hosts for exact-head gates.
- Tags: implementation, review, records; Linux execution needs gate and Selenium for browser legs.
- Inputs: main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, PR 5110 `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`, board `3bab46b66271614def22cab95a436427a98fd8fb`. Refresh before claiming.
- Contribution route: independent follow-up branches integrating main and the node-picker PR. Preserve original ancestry; no contributor-branch writes or force-pushes.

## Scope

Work through the existing plan's areas with distinct patch/test scopes where separable: encoded targeting keys (C2), selection/response lifecycle (C3), request-specific targeting and the SOLR-18619 migration (C4), and encoded-path second-hop coverage (C5). Assess current code and overlapping work before changing it. Do not repeat completed work: C1's status-preservation production change already exists in `solr-18549-submit` at `b89664d164db46cdac432668e2d9a07ce95fbd37`; record that overlap rather than duplicating it.

Each implemented slice must be committed and plain-pushed to Nick's fork in the same session, with separate remote-SHA read-back, add-only implementation report and exact-head Linux gate. No source work remains local. Keep Implemented, Proof Pending until its receipt satisfies all required proof legs. Claims and reports must distinguish hypotheses, confirmed source behavior, runtime reproduction, and coverage-only work.

No Gradle or retired local queue on Windows. Linux jobs must pin baseline/head, focused and neighboring tests, fixed seeds, tidy, root checks, fresh logs/XML, and the appropriate fail-before or mutation proof. Source/formatting repairs require a new pushed head and superseding gate. No PR creation, Jira write, live PR-body edit or adoption-offer comment is authorized by this assignment.

## Deliverables

- `reports/solr-12613-adjacent-implementation-round-1.md`: baseline, area disposition, branches/heads, exact file lists, overlap decisions and gate paths.
- One phase-specific gate per independent pushed slice, named in the report, with a Linux receipt target.
- Follow-up source branches with ordinary commits atop the recorded main-plus-picker integration baseline. Use descriptive commit messages without GitHub PR numbers; Nick Shanin is the author, no trailers.

Do not modify the earlier planning assignment or generated indexes. Complete a bounded slice and publish its artifacts before moving to the next, preserving the full objective across slices.
