# Pool assignment: re-gate watch and internal-block inventory, round 1

Capability tags: `review`. Intended host: windows. Staffing: two read-only subagents. Claim: `claims/pool-regate-watch-round-1.md`.

## Purpose

The vm2 gate backlog coordinator has claimed three re-gates on the branches below. Each claim names a head that is the live fork tip. The re-gates will move each head, so each draft must be checked against the head it will name after the gate:

- SOLR-14231 (`pr-drafts/streaming/SOLR-14231.md`): title correction, claim `claims/pool-vm2-gate-backlog-14231.md`, head `2f73d00a3996`.
- SOLR-15357 (`pr-drafts/schema-analysis/SOLR-15357.md`): test name fix and title correction, job `gates/SOLR-15357-test-fix.md`, head `d717899b8736`.
- SOLR-15945 (`pr-drafts/schema-analysis/SOLR-15945.md`): text corrections, claim `claims/pool-vm2-gate-backlog-15945.md`, head `adb0fd450c06`.

Separately, fifteen drafts carry internal hold or status blocks (for example "[HOLD", "[WAITING", "OWNER NOTE", "DRAFTABLE", "hold block"). Under the convention adopted in this round, those blocks belong in reports, not in draft bodies. This round lists them so that the cleanup can be done mechanically.

## Checks

Subagent A takes the three re-gate drafts.
1. Named heads in each draft against the claim head and the fork tip.
2. The text each re-gate will change (title, test name, changelog wording), with the exact before and after that the draft needs once the gate is green. Do not edit.
3. Internal blocks inside each of the three drafts, quoted with line numbers.

Subagent B takes the internal-block inventory.
1. For every draft in `pr-drafts/` that matches "\[HOLD|\[WAITING|\[DRAFTABLE|OWNER NOTE|hold block|Owner to supply|\[per-|\[to fill", list each match with its file, line, and the first 160 characters.
2. For each match, classify it as STATUS (a status header or owner note, move to the report), PLACEHOLDER (a missing fact, move to the report and mark the Proof as deferred), or TEXT (real outbound prose that only looks like a note).
3. For each draft with a STATUS or PLACEHOLDER block, say whether the rest of the draft reads correctly once that block is removed, quoting the sentence that would then stand alone.

## Ladder

Information first from the draft, the claim and the fork tip. Where a re-gate is still pending, the draft is held at its current named head and the post-gate text is recorded, not applied. Where a status block is only an owner note, the convention is to move it to the report.

## Hold rule

A draft whose named head will change at a re-gate waits. Do not edit a draft in this round.

## Rules

- Read-only subagents. No builds, no tests, no Gradle, no posting, no PR or comment, no push to a submit branch, no change to a gate job.
- No em dashes or en dashes in authored text. No PR numbers in file names or commit messages.

## Deliverable

`reports/regate-watch-round-1.md`, verdict first: the three drafts with their post-gate text, and the inventory of internal blocks with a classification and the cleanup list. Mark the claim DONE in the same push as the deliverable.
