# Coordination v2 workflow

**Preview only.** `coordination-ui` remains live until an explicit cutover record and host readiness verification say otherwise. The old branch is preserved; the new queue starts empty. Do not create real work on this preview.

## Records and ownership

Each assignment has one stable job ID and one canonical path `records/assignment/<job>.json`. The claimant uses `records/claim/<job>.json`; the Git writer enforces that claim ID equals job ID. Every compliant writer must use this single path. A successful create-only write and read-back wins the claim. Git repository permissions do not enforce compliance or authenticate the human identity in JSON; only trusted, configured writers may participate. Disallow other write paths operationally or enforce them server-side before production use.

Claims are immutable. When the claimant finishes, publish a report and, if handing work to another worker or stopping, a `release` record at `records/release/<job>.json` naming actor, reason and evidence that the worker and any related executor have stopped or completed. A release is a signed-off assertion by a trusted actor, not a process lock or a liveness measurement. Before relying on it, inspect the evidence and ensure the original worker cannot continue conflicting work.

Publish final receipts and reports before the release. The v2 writer refuses new records against a released job on every retry, including a late write racing with release. This restriction only applies to compliant writers; it cannot stop a process or prevent bypass by another Git client.

For a successor or takeover, use a **new** assignment/job ID with `successor_of` naming the released job. The writer requires the release record to exist. The successor must inspect the release and evidence and claim its own canonical path before acting. There is no timed or automatic takeover. A missing heartbeat, unchanged Git tip, disconnected session, or release record without adequate evidence does not prove a process stopped. If a worker cannot be shown to have stopped, do not reassign conflicting work.

For selective carryover, first inspect the current legacy assignment, claims, receipt, live source head and execution state. Link them in the new assignment. Do not duplicate a live claim or gate. Preserve legacy source and evidence links.

## Gate requests

Create a separate gate assignment and claim for the execution host. The gate record names the exact source head, environment, acceptance criteria, commands and any fail-before or mutation legs. The executor records actual results and links fresh evidence in a receipt. A receipt's head must match its gate and the offered source head; passing one leg does not imply all legs passed. A changed patch or formatting head needs a superseding exact-head gate. Never present a compile error, setup failure, skip or non-reproduction as a passing behavioral proof.

## Publishing

Use `tools/board.py publish` with the intended remote and branch explicitly named; see [the tool guide](tools/README.md) for examples. It performs create-only Git writes and verifies the resulting reachable commit and content. Each worker configures an appropriate Git identity and credentials locally. Never put credentials or sensitive data in board records or URLs. Keep raw evidence in an authorized store and publish its location, hash and source head; confirm the actual stored artifact separately.

The index is generated for a named commit and lists structured v2 records only. It does not establish that a release is truthful, that an owner is idle, or that required proof legs passed. Investigate conflicting records and stale heads before acting. No automatic expiry, process lock, PR/Jira action or escalation of public-write authority is provided by this workflow.
