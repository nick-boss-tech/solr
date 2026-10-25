# Claim: backlog-first-100-unit-3 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: CLAIMED.
- Job or assignment: `assignments/backlog-first-100-pack-slices-round-1.md`, Unit 3 only.
- Slice: `closure-obsolete`, records 1 to 25 in batch order (ascending batch number, then record order within each batch, per the unit plan; vm4's locator will confirm the order). Each record live-checked in JIRA at fill time, drops listed with reasons.
- Deliverables: 25 filled drafts at `jira/jira-comment-<KEY>-draft.md`; unit report at `reports/backlog-first-100-unit-3.md`.
- Capability tags used: draft, records, sweep.
- Constraints: nothing posted to JIRA or GitHub; claims and drafts only.
- Started: 2026-10-25T16:55Z (UTC). Attempts: 1. Lease: live while heartbeats continue.
- Decision ladder: rung 1 (the H1 batch records and the family template); rung 3 (the pack parent's fill and exclusion rules).

Heartbeat: 2026-10-25T16:55Z

Correction: the start time above is 2026-10-25T16:51Z (the claim commit), not 16:55Z.

Status note 2026-10-25T17:02Z: BLOCKED on one owner question, nothing filled yet. All 68 `closure-obsolete` records in the rev4 batches carry `conflict: true` (conflict note: bug in a closure disposition, flagged for the severity screen, `assignments/harmonize-audits-comment-drafts-round-1.md` line 86). Pack rule 2 skips conflict-flagged records, so the unit has zero eligible records as written. The pack lifts the bug bar but does not clear the flag or say who runs the severity screen. Also, the records carry no removal-evidence field; the template's REMOVAL_EVIDENCE (file and commit) must be found per record. Question sent to Nick: does the pack clear these flags, or must the severity screen clear them first?
