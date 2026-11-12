# Coordination v2 workflow

## Avoid unsolicited GitHub timeline references

Do not include GitHub-autolinked issue or pull request references in commit messages on the fork. Hash references, qualified references, and GitHub issue or PR URLs can create unwanted timeline events on other people's work. Use `PR 5315` or `pr-5315` instead. Check the entire message, including trailers, before pushing. This applies on every host to board, evidence archive, review, and source commits, including manual writes. Required links can remain in board record content. The structured writer's restricted IDs produce safe commit messages; other writers and manual commits need the same care.


The [cutover announcement](https://github.com/nick-boss-tech/solr/blob/coordination-ui/reports/coordination-v2-cutover-2026-11-06.md) on `coordination-ui` activates this workflow. If it is absent, the old board remains live and no real work should be created here. Once present, the old branch is historical and the new queue starts empty. Host configurations that have not been updated must be corrected before those hosts take new work.

## Records and ownership

Each assignment has one stable job ID and one canonical path `records/assignment/<job>.json`. The claimant uses `records/claim/<job>.json`; the Git writer enforces that claim ID equals job ID. Every compliant writer must use this single path. A successful create-only write and read-back wins the claim. Git repository permissions do not enforce compliance or authenticate the human identity in JSON; only trusted, configured writers may participate. Disallow other write paths operationally or enforce them server-side before production use.

Claims are immutable. When the claimant finishes, publish a report and, if handing work to another worker or stopping, a `release` record at `records/release/<job>.json` naming actor, reason and evidence that the worker and any related executor have stopped or completed. A release is a signed-off assertion by a trusted actor, not a process lock or a liveness measurement. Before relying on it, inspect the evidence and ensure the original worker cannot continue conflicting work.

Publish final receipts and reports before the release. The v2 writer refuses new records against a released job on every retry, including a late write racing with release. This restriction only applies to compliant writers; it cannot stop a process or prevent bypass by another Git client.

For a successor or takeover, use a **new** assignment/job ID with `successor_of` naming the released job. The writer requires the release record to exist. The successor must inspect the release and evidence and claim its own canonical path before acting. There is no timed or automatic takeover. A missing heartbeat, unchanged Git tip, disconnected session, or release record without adequate evidence does not prove a process stopped. If a worker cannot be shown to have stopped, do not reassign conflicting work.

For selective carryover, first inspect the current legacy assignment, claims, receipt, live source head and execution state. Link them in the new assignment. Do not duplicate a live claim or gate. Preserve legacy source and evidence links.

## Gate requests

Create a separate gate assignment. The execution host claims that job before running it. The gate record names the exact source head, environment, acceptance criteria, commands and any fail-before or mutation legs. The executor records actual results and links fresh evidence in a receipt. A receipt's head must match its gate and the offered source head; passing one leg does not imply all legs passed. A changed patch or formatting head needs a superseding exact-head gate. Never present a compile error, setup failure, skip or non-reproduction as a passing behavioral proof.

## Find and hand off a gate

A gate assignment and its gate record have independently chosen IDs. The gate ID
may match the job ID or differ from it. The assignment, claim, and release IDs
must equal the job ID. Do not derive a gate ID from a job ID or select a gate
from another job.

When creating a gate:

1. Put the exact gate record path and ID in the assignment. Add the canonical
   claim path, source repository, branch and full head, expected receipt path,
   phase or round, and any superseded gate or receipt.
2. Publish the assignment and gate record. Read both back at a board snapshot.
   Confirm that their job IDs and source heads match. Verify the remote head.
3. Give the recipient links to both records, the job ID, claim path, source
   repository, branch and full head, expected receipt path, and board snapshot
   SHA. State any claim observed at that snapshot. Sending a handoff does not
   claim the job or start the gate.

Before claiming a gate, read the assignment, gate, existing claim and release,
relevant receipts and reports, and current source head. Check host capabilities,
phase and supersession. Refresh and reconcile before a write. A released job
needs a successor under the ownership rules above.

For an older assignment with no gate path, search `records/gate/` at the
fetched board SHA for its job ID. Read each candidate and compare its `job`,
head, phase and supersession fields. If the gate is missing, check the live tip
once and search again if it moved. If the result is still missing or ambiguous,
report that state to the handoff owner. Do not claim or execute a different job.
The [tool guide](tools/README.md) has record examples and lookup commands.

## Publishing

Use `tools/board.py publish` with the intended remote and branch explicitly named; see [the tool guide](tools/README.md) for examples. It performs create-only Git writes and verifies the resulting reachable commit and content. Each worker configures an appropriate Git identity and credentials locally. Never put credentials or sensitive data in board records or URLs. Keep raw evidence in an authorized store and publish its location, hash and source head; confirm the actual stored artifact separately.

The index is generated for a named commit and lists structured v2 records only. It does not establish that a release is truthful, that an owner is idle, or that required proof legs passed. Investigate conflicting records and stale heads before acting. No automatic expiry, process lock, PR/Jira action or escalation of public-write authority is provided by this workflow.
