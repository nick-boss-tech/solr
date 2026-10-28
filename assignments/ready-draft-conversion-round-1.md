# Assignment: mark Nick's open PRs [Ready], add the cap heading, and convert to draft, round 1

Status: OPEN

Posted 2026-10-27 by the phone agent at Nick Shanin's direct instruction. Intended hosts: any capable host with the `records` tag and GitHub API access. Claim one numbered unit at a time (small-claims rule); each unit is a separate claim file.

## What and why

Nick has 91 open PRs on apache/solr. The project's open-PR cap (7) means new work from him can only travel as drafts or handoffs. At his direction, every open PR of his except the three that carry an approval gets the same treatment PR #5017 received on 2026-10-27: a `[Ready]` title prefix, a heading in the body explaining the draft state, and conversion to draft. The three approved PRs are exempt and must not be touched: **#5212, #5098, #5079**.

## Per-PR treatment, in this order

1. Re-read the PR live. Confirm it is still open and still has no approval. If it has gained an approval, or is closed or merged, skip it and record the skip.
2. Title: prepend `[Ready] ` if the prefix is absent. Change nothing else in the title.
3. Body: insert this heading, exactly, immediately after the AI disclosure header line at the top of the body (and its blank line), if absent:

   ## Ready for review, but held as a draft due to the project's open-PR cap; I'll mark it open on request.

   Change nothing else in the body.
4. Convert the PR to draft (GraphQL `convertPullRequestToDraft`). #5017 is already a draft with the prefix and heading applied; for it, verify all three and record it as already treated.

## Irreversibility, read before claiming

For Nick, conversion to draft is one-way: he cannot flip a draft back to ready, and he cannot reopen a closed PR. A conversion applied to the wrong PR cannot be undone from his side. Work from the unit list below, re-check each PR number against the live PR before converting, and stop the unit and report if anything does not match (wrong state, an approval present, a title or body that does not carry the expected AI header shape).

## Units (88 PRs, verified open and unapproved 2026-10-27 21:47 MDT)

- Unit 1: #4968, #4996, #4998, #5000, #5002, #5003, #5004, #5009, #5010, #5011, #5013
- Unit 2: #5014, #5015, #5016, #5017, #5028, #5029, #5031, #5069, #5071, #5072, #5074
- Unit 3: #5075, #5076, #5077, #5078, #5080, #5081, #5082, #5083, #5084, #5085, #5086
- Unit 4: #5087, #5088, #5089, #5090, #5091, #5092, #5093, #5094, #5095, #5096, #5100
- Unit 5: #5101, #5103, #5104, #5105, #5115, #5116, #5118, #5130, #5131, #5132, #5134
- Unit 6: #5135, #5136, #5137, #5138, #5139, #5141, #5142, #5144, #5145, #5146, #5147
- Unit 7: #5150, #5154, #5155, #5156, #5157, #5158, #5159, #5160, #5169, #5171, #5181
- Unit 8: #5192, #5199, #5200, #5202, #5203, #5206, #5208, #5209, #5210, #5213, #5216

## Deliverable per unit

One record file `reports/ready-draft-conversion-unit-<n>-<host>.md` listing, for each PR in the unit: the number, the title prefix applied or already present, the heading inserted or already present, the conversion result (draft confirmed by read-back, already draft, or skipped with the reason). Read back at least the converted state of every PR after treating it; a PATCH or mutation response alone is not the record.

## Boundaries

- No comments on any PR. No body edits beyond the one heading. No title edits beyond the prefix. No closing, reopening, or ready-flipping anything.
- Do not touch #5212, #5098, or #5079 in any way.
- Do not batch-claim: one claim per unit, one unit per host at a time.
