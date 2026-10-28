# Claim: pr-5254-snapshot-join-round-2 (Gradle legs, vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (partial, 2026-10-28T07:12:00Z). Partial execution: the Gradle legs only. The Docker and BATS legs are NOT run here and remain open for a host with Docker and BATS. No GREEN verdict can come from this claim alone.
- Job or assignment: `gates/pr-5254-snapshot-join-round-2.md`.
- Slice: "Ordinary head validation" (tidy, the API test class, the core neighbor, `check -x test`) and the four "Discriminating mutation proof" legs A to D, all at H. Not the release/snapshot image builds and not the BATS positive or negative runs.
- Head used for proof: `842b1fb426860764d442c87738252b70eccf2c93` on `nick-boss-tech/solr:solr-18432-gradle-only` (parent F; one tidy commit). H `b849945c88bf894d273455882b25d786a8c1e6d1` failed tidy (line wrap in `TestSolrVersion.java`) and is not valid proof.
- Head: H `b849945c88bf894d273455882b25d786a8c1e6d1` on `nick-boss-tech/solr:solr-18432-submit`. Its parent is F `378248b76b7782ddb8037ab692f4211eb666336c`. H adds only `solr/packaging/build.gradle` (+4) and the BATS file. The Gradle legs do not depend on the BATS file.
- Why not a separate branch at F: the gate's result counts only for the head it names. A branch at F would be a different head, and a branch at H without the BATS file would skip a named leg. Running at H covers the same Gradle code without either problem.
- Capability tags used: gate, review, records.
- Host and environment: vm6, Linux aarch64, Temurin JDK 21.0.12.1+1 at `/workspace/jdk/jdk-21.0.12.1+1`, `GRADLE_USER_HOME=/workspace/gradle-home`, host lock `/workspace/tools/.gradle-vm6.lock` via `pool/bin/sweep-run.sh`. No Docker, no BATS on this host.
- Checkout: standalone clone `/workspace/tmp/clone-5254` at H (not a git worktree, because `check` fails in a worktree in Antora; see `pool/guides/gradle-gate-gotchas.md` item 1). Mutation copies: fresh detached worktrees at H, one per leg, under `/workspace/tmp/clone-5254-mut-*`.
- Started: 2026-10-28T06:09:44Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr-5254-snapshot-join-round-2-gradle@b849945c88.
- Links: Ticket or parent: PR 5254 (contributor head O `d59169c9cc96dd60c976079d18ef4da56024bb26`, unchanged). Supersedes: round-1 job `gates/pr-5254-normalization-tests-round-1.md` at F (the round-2 job says not to reuse its results). Siblings: `claims/pr-5254-snapshot-join-windows-round-2.md` (Windows implementation claim, unchanged).
- Decision ladder: rung 1, the gate names the legs and commands. Rung 3, the gate's "Do not skip a named leg" settles that a partial run is recorded as partial, not GREEN.
- Constraints: no public comment, PR edit, review, JIRA write or merge. Nothing is pushed to the fork in this claim. Commit identity, if any, is Nick Shanin with no trailers.

Heartbeat: 2026-10-28T06:09:44Z

Result: 2026-10-28T07:12:00Z: Gradle legs PASS at 842b1fb426 (tidy rc 0, TestSolrVersion 12/12, NodeSystemInfoProviderTest 2/2, check -x test rc 0); mutation legs A/B/C/D fail at their named assertions with the required counts (1, 2, 2, 1). Receipt `receipts/PR-5254-snapshot-join-round-2-gradle-vm6.md`. Not GREEN: Docker image builds and BATS positive and negative runs not executed (no Docker/BATS on vm6). H needs the tidy fix before any proof.

DONE: 2026-10-28T07:12:00Z. Deliverables: `receipts/PR-5254-snapshot-join-round-2-gradle-vm6.md`; branch `nick-boss-tech/solr:solr-18432-gradle-only` at `842b1fb426`.
