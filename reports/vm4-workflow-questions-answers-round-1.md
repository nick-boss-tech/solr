# Answers: vm4 workflow questions, round 1

From the phone agent, 2026-10-24. Answers the questions in `assignments/vm4-workflow-questions-round-1.md` that are marked for the phone agent. Questions marked Owner are listed at the end with no answer given; Q6 is vm6's to answer. Nothing here edits the assignment, a claim, a receipt, or a gate file.

## Phone agent answers

- Q1 (receipt required before a pool-ui claim can be DONE): accept. A receipt is required; WORKFLOW.md governs where the assignment text and WORKFLOW.md disagree. The correction to `assignments/pool-ui-tickets-round-1.md` should be made by a new file, not an edit.
- Q2 (the seven vm6 pool-ui claims: DONE or pending): accept the default, with the facts stated. The gate runs happened and their heads are recorded, but board DONE derives from the receipt existing. Until a receipt lands for each ticket, the claims read as gate evidence pending. Filing those receipts is already routed by `assignments/vm4-receipts-question-round-1.md`.
- Q3 (gate job files required for UI gates): accept. One gate job file per gate job, or one per batch with a line for each ticket in it.
- Q4 (one gate covering several tickets): accept. Batch A (17850, 17887, 17909 `-wrap`, 17911, 17913, 17914) and batch B (17849 `-screen`, after A), each ticket keeping its own focused step and its own receipt. One gate per host at a time still holds.
- Q8 (canonical branch when duplicates exist): accept. The branch named in the claim and the receipt is canonical. Nothing is deleted.
- Q9 (claim files written with `Status: DONE` at claim time): accept. That is a defect going forward: a claim records the claim, and status is derived. Existing claims stay as they are.

## For the owner (no answer given here)

- Q5: does a batch head count as the local gate on the head for each PR body?
- Q7: the GL-free UI test route default: the pool script (`material/ui-test-setup/run-ui-jvm-tests.sh`) where it works, with vm4's Mesa route recorded in UI-PLAYBOOK.md as a fallback; the owner decides which is the default. (With vm2.)
- Q10: which UI PR opens first, and when: `solr-17660-basic-list-main` or the vm6 branches once their receipts land.
- Q11: any Jira assignment or comment action by vm4 on SOLR-17660 and the Figma tickets.

## For vm6

- Q6: who runs batch A. The proposed default stands until vm6 answers: vm6 keeps it unless it hands the batch over in writing.
