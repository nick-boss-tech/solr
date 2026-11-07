# Execution claim: PR 5305 ignored-field sibling fixes gate, round 1 (phone agent)

Status: CLAIMED, execution in progress.

- Job: gates/pr-5305-ignored-fields-round-1.md.
- Candidate head F: 219fee78bf7cae5039e50fe885108a1d4937ccd3; tree 0f979d6c070da09f889b829f51c915743df77239.
- Contributor parent O: 00292cd3729126194235e939afe38e351d850c70 (verified live as F's direct parent and as the fork branch's parent; live fork ref read back equal to F before claiming).
- Branch: pr-5305-solr-18627-ignored-fields on nick-boss-tech/solr. The public PR 5305 is not touched by this gate.
- Host: phone agent VM (Linux, runs as root; no Solr-starting leg is in this job). JDK 21 (vendored Temurin 21.0.12.1+1) and Gradle 9.7.0 via ~/workspace/tools/solr-gradle.sh. Builds serialized on the host build lock ~/workspace/tools/.solr-test-queue.lock (fd 9, Gradle children invoked with 9>&-).
- Detached clones: ~/workspace/solr-clones/5305-pos (positive legs and root check, a normal clone) and ~/workspace/solr-clones/5305-fb (fail-before overlay), both detached at F with HEAD and tree recorded before building.
- Runners: ~/workspace/tools/gate-5305/run-preflight.sh, run-failbefore.sh, run-positive.sh.
- Evidence root: ~/workspace/tools/gate-5305/ (per-leg logs and fresh JUnit XML copied out after each leg, before any later cleanTest).
- Detached tidy preflight completed before this claim: root tidy at F, rc 0, zero tracked changes, git diff --check rc 0, 2026-11-07T18:27:34Z to 18:29:36Z.
- Seed for every test leg: 18627C0DE.
- Claim posted: 2026-11-07T18:30:00Z (approx, at posting).
