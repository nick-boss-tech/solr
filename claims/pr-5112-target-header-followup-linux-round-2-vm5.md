# Claim: PR 5112 target header follow-up, Linux gate, round 2 (superseding round 1)

- Claimant host: vm5, matching the Host id line in `hosts/vm5.md`.
- Status: ACTIVE; a receipt at the exact head is the completion record.
- Job or assignment: `gates/pr-5112-target-header-followup-round-1-superseding-46bc7e4.md`.
- Slice: the whole superseding gate at `46bc7e428b5be519a497ecf65dcd0256b65fa84c`.
- Capability tags used: gate, premise-run.
- Started: 2026-10-27T16:55:00Z (UTC). Supersedes: `claims/pr-5112-target-header-followup-linux-round-1-vm5.md`, which was live at head `3d762be30f6b` and was stopped when the tidy repair changed the head.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr-5112-target-header-followup-round-1@46bc7e428b5be519a497ecf65dcd0256b65fa84c.
- Links: Ticket or parent: fork branch `nick-boss-tech/solr:solr-18537-submit`, head `46bc7e428b5be519a497ecf65dcd0256b65fa84c`, original PR head `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85` (apache/solr PR 5112). Category: review. Siblings: none known.

Decision rung: rung 1 for scope, and the round-1 tidy finding is a repair under the WORKFLOW gate rules (rung 4: applied as a recorded scoped commit). The round-1 gate's tidy-clean step ran only to tidy; its focused run was stopped before any test result and its evidence is not carried forward.

Method note: checkouts are full detached trees under `/workspace/gates/pr-5112/`. Logs and JUnit XML are under `/workspace/gates/logs/vm5/pr5112-linux/`.

Heartbeat: 2026-10-27T16:55:00Z

Result: none yet.
