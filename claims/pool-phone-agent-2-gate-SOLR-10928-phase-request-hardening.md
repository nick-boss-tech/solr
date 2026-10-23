# Claim: pool-phone-agent-2-gate-SOLR-10928-phase-request-hardening

- Claimant host: phone-agent-2, matching `hosts/phone-agent-2.md`.
- Status: CLAIMED; independent hardening gate for `nick-boss-tech/solr:solr-10928-submit` at `d5621a29281f28659db7b9e9ca57cc46c2651f53`.
- Job or assignment: `gates/SOLR-10928-phase-request-hardening.md`.
- Slice: hardening gate for `solr-10928-submit` at `d5621a29281f28659db7b9e9ca57cc46c2651f53`, base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Capability tags used: gate.
- Started: 2026-10-23T03:40:35Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-10928-phase-request-hardening@d5621a29281f28659db7b9e9ca57cc46c2651f53.
- Links: Ticket: SOLR-10928. Category: Search and query components. Related phase: `SOLR-10928-phase-elevate-q` (completed, preserved). Supersedes: none.
- Initial checks: exact product tip matches gate and fork tip; clean product tree; 7-file, 269-insertion/3-deletion scope matches the job; no matching live claim exists; host lock available; root has 35 GB free and 441,273 free inodes.
- Plan: use `/workspace/solr` only for builds and this independent coordination worktree at `/workspace/coord-10928-independent-gate`; preserve all existing log directories. Run the standard runner unchanged through a request-specific wrapper with strict mutation proof verifiers, the four-class head and second-seed suites, core module check, and root `check -x test`. Archive fresh XML immediately after each test command.
- Constraints: no product source edits unless a gate repair is required by the job; no PR, comment, Jira, email, or public-text action. Push coordination records fast-forward as Nick Shanin with no trailers.

Heartbeat: 2026-10-23T03:40:35Z
