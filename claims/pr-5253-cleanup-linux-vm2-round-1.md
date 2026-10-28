# Claim: SOLR-18601 / PR 5253 migration cleanup follow-up, Linux gate round 1 (vm2)

Status: CLAIMED

- Claimant host: vm2. Tags: gate, review, records.
- Started: 2026-10-28T03:05Z (UTC).
- Job: `gates/pr-5253-cleanup-round-1.md` (board `3e84be3f668`).
- Exact positive head F: `5cbffe2f243c4f2cf3606de9f2434981fc097019` on `nick-boss-tech/solr:solr-18601-submit` (live `ls-remote` matched).
- Original PR head O (negative base): `32b91612edde057c19bf1a7963e8276b816551df`. O is an ancestor of F; F is one commit above O (verified). The public PR 5253 is not modified.
- Competing claims: `claims/pr-5253-cleanup-windows-round-1.md` is the Windows implementation claim, no execution. No Linux claim exists for this job.
- Host: JDK 21 `/workspace/jdk/jdk-21.0.12.1+1`, Gradle home `/workspace/gradle-home`, Gradle wrapper 9.7.0 with the verified wrapper jar (SHA-256 `2db75c40782f5e8ba1fc278a5574bab070adccb2d21ca5a6e5ed840888448046`). Build lock `/workspace/tools/.gradle-vm2.lock` on every Gradle run, serialized.
- Runner layout: standalone clones (not git worktrees; check/Antora rejects a worktree `.git` file): positive clone at F `/workspace/worktrees/pr5253-F-clone`; negative A at O as a separate clone `/workspace/worktrees/pr5253-O-clone`; negative B at F as a separate clone `/workspace/worktrees/pr5253-B-clone`.
- Logs and XML: `/workspace/gates/logs/vm2/solr18601-cleanup/`.
- Constraints: no GitHub, PR, JIRA or upstream write. No force push. Any tidy or source repair follows the job's superseding-gate rule. No edits to the gate job.
- Deliverables: `receipts/PR-5253-cleanup-round-1.md`, and an add-only completion or failure report. Commit identity Nick Shanin, no trailers if any commit is ever needed.
