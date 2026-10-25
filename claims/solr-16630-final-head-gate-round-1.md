# Claim: solr-16630-final-head-gate-round-1 (gate)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: LIVE.
- Job or assignment: `gates/SOLR-16630-pr5106-final-head.md` (the whole job). Tags: gate.
- Slice: the job's three standard legs at head `a02cc7ba6b905b511c2390f023e7d89dccac1a03` on `nick-boss-tech/solr:solr-16630-submit`, plus the timing runs (10 runs of the class at the head, 1 run at the merge base). Base for comparison: merge base `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (the fork branch was cut from it; the job's "origin/main at gate time" is taken as this merge base).
- Head diff checked before the claim: 2 files (`changelog/unreleased/SOLR-16630.yml`, `TestCoordinatorRole.java`), 217 insertions, 57 deletions; author Nick Shanin.
- Receipt: the job says to extend `receipts/SOLR-16630.md` with a new section. vm2 wrote that file. I will append only a new section and not change vm2's text, as the job directs. If the owner wants a separate file, that can be split later.
- Capability tags used: gate.
- Started: 2026-10-25T00:09:51Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-16630-final-head-gate-round-1@a02cc7ba6b.
- Seeds for the head runs: 681E2A715B2CE1D3 (CI seed), D02E8D0A859FB680, 6898B131C70F6EB5, D271BF7E4A71C40A, 0C550F77AAD157A1, 5894EC32479CE482 (the five seeds already in the receipt), and 16630ABC0001 to 16630ABC0004 (new). Base run: 681E2A715B2CE1D3.
- Runner: `pool/bin/gate-preflight.sh` then `pool/bin/bg-steps.sh` under `/workspace/tools/.gradle-vm6.lock`. `NODE_ENV` is unset for the runs. Logs: `/workspace/gates/logs/vm6/solr-16630/`. Worktrees: `/workspace/worktrees/pool-vm6-16630` (head) and `/workspace/worktrees/pool-vm6-16630-base` (base).
- Constraints: no PR, no JIRA or GitHub post, no fork push, no change to the branch. Commit as Nick Shanin with no trailers.
- Decision ladder: the job's acceptance rule governs. A tidy change at the head would be a GATE INVALID repair (WORKFLOW.md Gate repairs), recorded and not hidden.

Heartbeat: 2026-10-25T00:09:51Z

Result: 2026-10-25T01:14:35Z: legs at head a02cc7ba6b905b511c2390f023e7d89dccac1a03 passed (tidy clean; Error Prone compile rc 0; check -x test rc 0). The first head runs h01 (seed 681E2A715B2CE1D3) and h02 (seed D02E8D0A859FB680) ended rc=1 with the assertion "Pull jetty replicas didn't become active in time" in testNRTRestart. The cause, read from the XML and the log: the PULL replica's index fetch failed with FileSystemException "Too many links" on _0.kdm under the NFS work directory. This is the NFS hard-link problem recorded by vm2 in receipts/SOLR-16630.md. Both attempts are recorded as ENV and not counted, as vm2 did. The driver was stopped after h02, so no further run used the NFS directory.

Setup change, recorded before the rerun: -Ptests.workDir=/tmp/solr16630-tests (local ext4, as vm2 used). Same seeds and the same 10-run plan, plus the base run at 3f5d4c5bf8 with seed 681E2A715B2CE1D3. Relaunched at 2026-10-25T01:14:35Z; logs under solr16630-head-tmp and solr16630-base-tmp.

Result: 2026-10-25T02:14:03Z: gate green at head a02cc7ba6b905b511c2390f023e7d89dccac1a03. Legs rc 0 (tidy clean, Error Prone compile, check -x test). 10 of 10 whole-class runs pass (9 methods each, 0 failures, 0 errors, 0 skipped); base run at 3f5d4c5bf8 passes (testNRTRestart 42.7 s vs head median 11.9 s). Two earlier head attempts are ENV (NFS "Too many links"), not counted; the base run was relaunched once after its first attempt stopped before any test ran. Receipt section appended to receipts/SOLR-16630.md.

DONE: 2026-10-25T02:14:03Z (gate). Deliverable: the receipt section and this claim's Result lines.
