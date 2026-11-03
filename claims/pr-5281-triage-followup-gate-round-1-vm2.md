# Claim: pr-5281-triage-followup-gate-round-1-vm2

- Claimant host: vm2 (Linux aarch64 pool host, `hosts/vm2.md`).
- Status: LIVE.
- Job: `gates/pr-5281-triage-followup-round-1.md`, whole job.
- Slice: the full five-leg Linux proof for PR 5281 advisory triage follow-up at H1 `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40` (positive offline suite, H0 fail-before, rendered output boundary, workflow lint/offline wiring, formatting and root checks), then the named receipt.
- Capability tags used: gate, review, records.
- Board snapshot: `cbafec66691e39eb0fc5868d975088212aca1373` (coordination-ui, fetched at 2026-11-03T17:10Z). No matching Linux gate claim or receipt existed; only the Windows implementation claim `claims/pr-5281-triage-followup-windows-round-1.md`.
- Preflight: `pool/bin/gate-preflight.sh` on H1, rc=0 "tidy clean" at `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40`. Log: `/workspace/logs/vm2/gate-5281/preflight/preflight.log`.
- Fork ref: `nick-boss-tech/solr:pr-5281-triage-followup` at H1 `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40` (remote ref read confirmed, 2026-11-03T17:01Z).
- H0 (contributor parent): `35970681650b4e602632bdfc344c5922b9f1581f`.
- Toolchain: JDK 21.0.12.1 at `/workspace/jdk/jdk-21.0.12.1+1`, `GRADLE_USER_HOME=/workspace/gradle-home`, Python 3.11.17, Git, Bash, jq. actionlint/ShellCheck/cmarkgfm installed for this gate.
- Started: 2026-11-03T17:11Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr-5281-triage-followup-gate@6bcaa9e1191.
- Expected receipt: `receipts/PR-5281-triage-followup-round-1.md` (owned by execution host).
- Constraints: Python/GitHub Actions tooling patch, no Solr Java production/test changes, no changelog fragment. No model/GitHub credentials needed for offline tests. Do not call the live LLM or post a comment as part of this gate. No force push, no change to David's branch, no upstream write. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-11-03T17:11Z
