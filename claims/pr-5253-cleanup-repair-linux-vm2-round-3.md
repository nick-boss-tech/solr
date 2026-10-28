# Claim: SOLR-18601 / PR 5253 cleanup repair, round 3 gate (vm2, Linux)

Status: CLAIMED

- Claimant host: vm2. Tags: gate, review, records.
- Started: 2026-10-28T04:20Z (UTC).
- Job: `gates/pr-5253-cleanup-repair-round-3.md` (board `7f93a5fa3a9`).
- Exact positive head I: `6a99e731dadd4c3004b46e2bb42a0e6cad59cfc4` on `nick-boss-tech/solr:solr-18601-submit` (live `ls-remote` matched). I is one normal commit above H `98d1127e4f5d4d149992e1dc494b95928bac057a`.
- Original PR head O: `32b91612edde057c19bf1a7963e8276b816551df`. Not written to.
- Competing claims: `claims/pr-5253-cleanup-repair-windows-round-2.md` is the Windows repair claim, no execution.
- Host: JDK 21 `/workspace/jdk/jdk-21.0.12.1+1`, Gradle home `/workspace/gradle-home`, wrapper 9.7.0 with the verified wrapper jar (SHA-256 `2db75c40782f5e8ba1fc278a5574bab070adccb2d21ca5a6e5ed840888448046`). Build lock `/workspace/tools/.gradle-vm2.lock`, serialized.
- Standalone clones (not worktrees): positive I `/workspace/worktrees/pr5253-I-clone`; negative A at O `/workspace/worktrees/pr5253-O-clone`; negative B at I `/workspace/worktrees/pr5253-I-B-clone`; negative C at H `/workspace/worktrees/pr5253-H-clone`.
- Logs and XML: `/workspace/gates/logs/vm2/solr18601-cleanup/repair-I/`. Earlier logs preserved in the parent directory.
- Constraints: no GitHub, PR, JIRA or contributor-branch write. No force push. Any tidy or source repair follows the superseding-gate rule. No edits to the gate job.
- Deliverables: `receipts/PR-5253-cleanup-repair-round-3.md` and an add-only completion or failure report. The prior NOT GREEN receipt stays unchanged.
