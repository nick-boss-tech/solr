# Claim: PR 5229 fielded-query test and fail-before handoff, Windows round 1

- Host: windows (Codex desktop); tags: implementation, review, records, draft.
- Started: 2026-10-26T00:39:51Z.
- Assignment: Nick's direct instruction in the PR 5229 review chat to add the optional fielded-query test and prepare fail-before proof for the new head.
- Reviewed board: `4de75ba5626f15c317029e61a950aed773229da9`.
- Starting PR/fork head: `f2001e640a464e8e861ee3c202821b6dcc4bd497`, `solr-10928-submit`.
- Slice: add a focused endpoint test for `q=text:MMMM&elevate.q=YYYY`, preserving the no-override control; push a plain commit; post a new exact-head Linux gate with executable matched head/base proof instructions. Update the previously authorized PR body to state new-head proof pending.
- Deliverables: a pushed commit on `solr-10928-submit`; `gates/SOLR-10928-pr5229-fielded-query-round-2.md`; `reports/pr-5229-fielded-query-windows-round-1.md`.
- Ownership check: vm6's earlier gate is DONE with an exact-head receipt; vm2's duplicate is explicitly stopped by `claims/solr-10928-live-head-regate-vm2-duplicate.md`. No active implementation claim for this slice was found.
- Decision ladder: rung 1, parameter-selection code and endpoint assertions establish the narrow test shape. Fixed proof base is `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, the PR merge base. Only the new head's existing endpoint test file is transplanted onto that base; the new helper test class is not copied. No synthetic production shim is needed.
- Status: implementation in progress. Windows runs no Gradle or test queue. Linux proof remains pending, to be run by Nick or a capable Linux host through the new gate.
