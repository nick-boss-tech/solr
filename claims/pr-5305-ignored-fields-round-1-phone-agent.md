# Execution claim: PR 5305 ignored-field sibling fixes gate, round 1 (phone agent)

Status: DONE. Verdict: GATE GREEN at F. Receipt: receipts/PR-5305-ignored-fields-round-1.md. Report: reports/pr-5305-ignored-fields-gate-round-1.md. Evidence: evidence/pr-5305-ignored-fields-round-1/pr-5305-ignored-fields-round-1-evidence.tar.gz, SHA-256 b57cddb838c83b7c9dd1d878ab9bc8dbb159e8714ea9f75f9f1dccfc35038d5d.

- Job: gates/pr-5305-ignored-fields-round-1.md.
- Candidate head F: 219fee78bf7cae5039e50fe885108a1d4937ccd3; tree 0f979d6c070da09f889b829f51c915743df77239.
- Contributor parent O: 00292cd3729126194235e939afe38e351d850c70 (verified live as F's direct parent; live fork ref read back equal to F before and after the run).
- Branch: pr-5305-solr-18627-ignored-fields on nick-boss-tech/solr. The public PR 5305 was not touched by this gate.
- Host: phone agent VM (Linux). JDK 21 (vendored Temurin 21.0.12.1+1) and Gradle 9.7.0 via ~/workspace/tools/solr-gradle.sh. Builds serialized on the host build lock ~/workspace/tools/.solr-test-queue.lock (fd 9, Gradle children invoked with 9>&-).
- Detached clones: ~/workspace/solr-clones/5305-pos (positive legs and root check, a normal clone) and ~/workspace/solr-clones/5305-fb (fail-before overlay), both detached at F.
- Runners: ~/workspace/tools/gate-5305/run-preflight.sh, run-failbefore.sh, run-positive.sh, run-check-rerun.sh. Evidence root: ~/workspace/tools/gate-5305/.
- Detached tidy preflight: root tidy at F, rc 0, zero tracked changes, git diff --check rc 0, 2026-11-07T18:27:34Z to 18:29:36Z.
- Results (seed 18627C0DE, fresh JUnit XML): fail-before overlay produced exactly the six named core NullPointerException failures at java.util.List.of in the named createFields methods and the one ICU failure, with all controls passing; positive legs pass 107 tests with 0 failures and 0 errors (one characterized pre-existing assumption skip in TestTrie); root check -x test passes on rerun after the relay-corrupted UI wasm download was replaced with the pin-verified zip (first attempt failed at :solr:webapp:verifyUiWasm only).
- Execution window: 2026-11-07T18:27:34Z to 18:52:42Z.
