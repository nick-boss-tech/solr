# Claim: ui-17676-nobrowser-probe-round-1

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE. Deliverable `reports/ui-17676-nobrowser-probe-round-1.md`.
- Job or assignment: `assignments/ui-17676-nobrowser-probe-round-1.md`.
- Slice: the whole assignment: one bounded `:shared:wasmJsBrowserTest` run with `CHROME_BIN` pointed at a missing path, plus the report.
- Capability tags used: premise-run.
- Started: 2026-10-24T05:40:18Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: ui-17676-nobrowser-probe-round-1@6ec6295b49.
- Links: Ticket or parent: SOLR-17676 (Enable wasmJs tests). Category: none; `pool/work-map.md` is not on this board. Siblings: `assignments/pool-ui-tickets-round-1.md` (ui tag, not held by vm6), `assignments/ui-volunteer-drafts-round-1.md` item 3 waits on this probe's report. Supersedes: none.
- Constraints: no code change, no branch push, no Jira or GitHub comment, no PR. Commit as Nick Shanin with no trailers. Owner complaint 5: this is a run, not a draft, so it does not repeat the previous claimant's type.
- Decision ladder: no decision inside the probe; the assignment fixes the command. If the run fails at the PKIX download step, that is the recorded result. It is not retried in the same environment and no proxy workaround is attempted.

Heartbeat: 2026-10-24T05:58:27Z

Result: 2026-10-24T05:58:27Z: two launch attempts did not start the run. First, the detached chain stalled in a slow checkout of `origin/main` (498a09aea9), which finished at 05:57Z. Second, the launch named `pool/bin/sweep-run.sh` inside the `main` worktree, which does not carry pool scripts; no log was written. Third attempt: run `probe-17676-498a09aea9-055827`, script from `/workspace/worktrees/pool-vm6-ui/pool/bin/sweep-run.sh`, head 498a09aea97aac86c2cc7ac114113b946d249942, log dir `/workspace/gates/logs/vm6/ui-17676/`, started 05:58:27Z. Outcome: 2026-10-24T06:16:28Z: `probe-17676-498a09aea9-055827` finished `rc=1` after 11 m 17 s. npm setup passed (no PKIX). `:shared:wasmJsBrowserTest` FAILED at the Karma launcher ("Can not find the binary /nonexistent/no-browser-here"); no tests ran, no XML. Conclusive for the default `browser()` config (`ui/shared/build.gradle.kts` 32-33, karma.conf.js 20-21). Log: `/workspace/gates/logs/vm6/ui-17676/probe-17676-498a09aea9-055827.log`. Not retried.

Result: 2026-10-24T06:16:28Z: no-browser criterion answered as failed: the wasm browser tests hard-fail without a browser. Deliverable `reports/ui-17676-nobrowser-probe-round-1.md`.

DONE: 2026-10-24T06:16:28Z. Deliverables: `reports/ui-17676-nobrowser-probe-round-1.md`, `assignments/ui-17676-nobrowser-probe-round-1.md` (status DONE).
