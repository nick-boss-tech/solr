# Claim: ui-17676-nobrowser-probe-round-1

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE. Deliverable `reports/ui-17676-nobrowser-probe-round-1.md` when it lands.
- Job or assignment: `assignments/ui-17676-nobrowser-probe-round-1.md`.
- Slice: the whole assignment: one bounded `:shared:wasmJsBrowserTest` run with `CHROME_BIN` pointed at a missing path, plus the report.
- Capability tags used: premise-run.
- Started: 2026-10-24T05:40:18Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: ui-17676-nobrowser-probe-round-1@6ec6295b49.
- Links: Ticket or parent: SOLR-17676 (Enable wasmJs tests). Category: none; `pool/work-map.md` is not on this board. Siblings: `assignments/pool-ui-tickets-round-1.md` (ui tag, not held by vm6), `assignments/ui-volunteer-drafts-round-1.md` item 3 waits on this probe's report. Supersedes: none.
- Constraints: no code change, no branch push, no Jira or GitHub comment, no PR. Commit as Nick Shanin with no trailers. Owner complaint 5: this is a run, not a draft, so it does not repeat the previous claimant's type.
- Decision ladder: no decision inside the probe; the assignment fixes the command. If the run fails at the PKIX download step, that is the recorded result. It is not retried in the same environment and no proxy workaround is attempted.

Heartbeat: 2026-10-24T05:40:18Z
