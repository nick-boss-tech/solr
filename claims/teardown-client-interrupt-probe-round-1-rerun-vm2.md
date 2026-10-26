# Claim: vm2 rerun of the teardown client-interrupt probe (round 1, with stack capture)

- Claimant host: vm2. Started: 2026-10-26T00:30:21Z.
- Supersedes: `claims/teardown-client-interrupt-probe-round-1-vm2.md` (verdict incomplete: closer stack not captured; thread filter unverified). That claim and its reports stay unchanged.
- Owner direction for this rerun: the owner said "you can rerun as needed so that I have a complete report to send back to implementor" (2026-10-26).
- Input change (recorded before the run): scaffold v2 adds stack dumps of the closer and client-like threads at 5 s and 30 s, a full thread-name listing, and a broader thread-name filter. Scaffold v2 diff SHA-256: `f6d7f6f252051b6c628bb97614fb3a3cefde4e5fcb0d023ada97a23dc2c22709` (file `/workspace/gates/gates-vm2/teardown-probe-scaffold-v2.diff`, 175 lines).
- Unchanged: source `d298cd0b42602247bee7bf9e2bcf302e725f6690`, seed `FFF5A3C3AAC69084`, the same two methods and the same Gradle filters, 30 s per-arm cap, 20 min total cap, one invocation.
- Runner: `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm2.lock`.
- Deliverables (new files only): `reports/teardown-client-interrupt-probe-round-1-rerun-scaffold.md` and `reports/teardown-client-interrupt-probe-round-1-rerun.md`.
- Decision ladder: rung 2 (a probe that separates the explanations), with the owner's rerun direction.
