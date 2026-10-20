# Advisory: balk at stuck and duplicate work, and clear it

Owner direction, 2026-10-19: agents keep redoing work that is already done, already claimed, or structurally unable to finish. That stops. When one of the shapes below matches the item in front of you, refuse the item: do not do the work, record the balk, and clear the stale record in the same push. A balk with the record cleared is finished work, not a failure, and it outranks filling the tick.

## The check

Before starting any claimed item, spend the first minutes looking for these shapes. They are cheap to check and expensive to miss: the claims folder, the assignment's own Status line, the fork's branch list, and the item's queue record.

## Shape 1: already done

A claim for the same item already carries a DONE line, or the deliverable already exists at the recorded head. Doing it again produces a second record that a later host must reconcile.

Example: SOLR-16930 was closed out twice in one day, by vm5 in the morning (`claims/skip-SOLR-16930-vm5.md`) and by vm4 in the afternoon, the second pass discarding the first. The same shape created a second draft branch for SOLR-16355 (`solr-16355-draft-close`) instead of reusing the branch the ticket already had; check `git ls-remote` for the ticket's draft branches before creating one.

Clearing action: stamp the duplicate claim SUPERSEDED with a pointer to the claim that stands. If you created a branch or a record before noticing, name it in the balk so it can be retired in the records; do not build on it.

## Shape 2: already claimed and live

Another host's claim on the item has a heartbeat inside the staleness window in WORKFLOW.md. Two live claims means one host's work will be thrown away.

Example: item 3 of the consistency round drew claims from vm3 and vm4 four minutes apart; vm4 did the work, and the duplicate claim only added noise to reconcile.

Clearing action: withdraw your claim in the same push (stamp it WITHDRAWN, naming the claim that stands) and take other work.

## Shape 3: structurally cannot finish

The item's completion floor cannot be met from the material that exists, no matter how many times it is redone. Redoing it manufactures motion, not evidence.

Example: SOLR-16785's remediation redo is complete and honest, and its code reading still reads "not done at the code level", because the ticket contains no error text, no stack trace, and no code pointer, and nothing in it can be matched to a line at the pinned head. A fourth redo cannot manufacture the missing material.

Clearing action: record the terminal disposition in the item's record and claim: INSUFFICIENT-REPORT, the verdict stands, the unmeetable floor item is named with the reason. Annotate the item as terminal in `material/skip-audit/remediation-current.json` in the same push so it leaves the claimable pool. A ticket that has had a full redo and still offers no reproduction and no code pointer is finished, not stuck.

## Shape 4: stale paperwork is not work

The assignment says DONE and a claim still says ACTIVE, or a status line disagrees with the deliverables on the branch. The remaining work is the stamp, not the batch.

Example: the batch 02 remediation claim still read ACTIVE hours after its assignment, its report, and all 25 per-ticket claims recorded the batch done; it sat inviting a takeover redo of finished work until the stamp was corrected.

Clearing action: correct the stamp to match the deliverables, cite them, and move on.

## What a balk record contains

The item, the shape number, the evidence (the claim path and its DONE line, the live claim and its heartbeat, or the missing material), and the clearing action taken. Stamp your own claim WON'T DO, with a closing line in the same push. WON'T DO is a terminal label: the tick treats the claim as closed, the item leaves the claimable pool, and no third host walks into it.

## What is not a balk

Difficulty, size, an unfamiliar area, or a failed first attempt. Those follow the normal rules: escalate with the evidence, hand back with the record, or ask for the missing input. Balk only on the four shapes above.
