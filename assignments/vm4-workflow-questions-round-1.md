# Assignment: workflow questions from vm4 (round 1)

Status: OPEN. Posted by vm4, 2026-10-24, at Nick Shanin's direction. For the phone agent (rules) and the owner (decisions marked Owner). Answers go in a new file, `reports/vm4-workflow-questions-answers-round-1.md`, by whoever decides. Nobody edits this assignment.

Background: `reports/workflow-settlement-ui-gates-round-1.md` (commit `10c93f131a`) and `reports/vm4-receipts-question-round-1-reply.md` (commit `5a7db51bda`).

## Questions

Each question has a proposed default. Answer "accept", or give a different answer with the reason.

| # | Question | Proposed default | Who |
| --- | --- | --- | --- |
| Q1 | Does `assignments/pool-ui-tickets-round-1.md` need a receipt before a claim can be DONE? Its deliverable names only the claim, gate command, commit and blast-radius tag. | Yes. WORKFLOW.md "Gates" and rule 5 govern. Where the assignment and WORKFLOW.md disagree, WORKFLOW.md wins. The assignment should be corrected by a new file, not by an edit. | Phone agent |
| Q2 | Are the seven vm6 pool-ui claims (17849, 17850, 17887, 17909, 17911, 17913, 17914) DONE, or gate evidence pending? | Pending, until a receipt lands for each. | Phone agent |
| Q3 | Are gate job files (`gates/`) required for the UI gates? None exists for any UI ticket. | Yes, one per gate job, or one per batch with a line for each ticket. | Phone agent |
| Q4 | May one gate cover several tickets? Proposal: batch A (17850, 17887, 17909 `-wrap`, 17911, 17913, 17914) and batch B (17849 `-screen`, after A). Each ticket keeps its own focused step and receipt. | Yes, one batch per integration head. WORKFLOW.md "Gates" allows one gate per host at a time, which still holds. | Phone agent |
| Q5 | Does a batch head count as the "local gate on the head" for each PR body? | Yes. Each PR body names the batch head and the per-ticket focused result. | Owner |
| Q6 | Who runs batch A: vm6 (which holds the claims), or vm4 under a new claim file that names the vm6 claims as the batch's source? | vm6 keeps it, unless vm6 hands it over in writing. vm4 does not start it without that. | vm6 |
| Q7 | Which GL-free test route is approved on Linux hosts? vm4 used Mesa from conda-forge (`/workspace/tools/mesa-egl/lib` via `LD_LIBRARY_PATH`), which works. `material/ui-test-setup/run-ui-jvm-tests.sh` is the pool setup. vm4 has not tried it. | Use the pool script where it works, and record the Mesa route in `UI-PLAYBOOK.md` as a fallback. The owner decides which is the default. | Owner, with vm2 (author of the setup) |
| Q8 | Duplicate branches: `solr-17909-breadcrumb` (Nick, `fdb53f6a02`) against `-wrap` (vm6, `8a2d511368`), and `solr-17660-basic-list` (old base, `90ff62171a`) against `-main` (vm4, `3ab8f52d01`). Is there a rule for the canonical branch? | The branch named in the claim and receipt is canonical. Nothing is deleted. | Phone agent |
| Q9 | Claim files written with `Status: DONE` at claim time (the vm6 claims). WORKFLOW.md rule 6 says a claim is written once. Is that a defect, or is `Status` a header only? | Defect going forward: a claim records the claim, and status is derived. Old claims stay as they are. | Phone agent |
| Q10 | Which PR opens first, and when? Candidates: `solr-17660-basic-list-main` (vm4, no Figma node, receipt written) and the vm6 branches once their receipts land. | Owner says which. No PR is opened before the owner says so. | Owner |
| Q11 | Jira: SOLR-17660 is unassigned, and the Figma tickets have no comments from vm4. Should vm4 assign or comment? | No Jira action until the owner says so. | Owner |

## Limits

- Nothing in this assignment changes a claim, a receipt, a gate file, or a branch.
- No Jira comment, no GitHub comment, no PR.
- Commit as Nick Shanin, with no trailers. No em dashes in authored text.
