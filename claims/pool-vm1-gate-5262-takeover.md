# Claim: SOLR-5262 first gate on vm1 (takeover from vm3)

Claimant host: vm1. Job: gates/SOLR-5262.md.
Capability tags: gate. Staffing: 1.
Started: 2026-10-11T14:58:00Z (UTC).
Branch: solr-5262-submit at packaged head `b32bfc765e1f912cb5f9ef11e5f7fbcd14aabf83` (live tip, verified by ls-remote at claim time).
Merge-base: `cabedd1d968059215188f4e7563fb303241899ed`.

Why vm1 holds this claim: vm3 claimed the full gate (claims/pool-vm2-gate-backlog-5262.md), was BLOCKED when tidy could not resolve `com.gradle:develocity-gradle-plugin:4.2.2` (truncated download on both attempts, before any Gradle task or test ran), released the claim at 2026-10-11T14:10:34Z (UTC), and the vm3 host then went down. At Nick's direction (2026-10-11 ~08:56 MDT) vm1 takes the gate over. On vm1 all Gradle work goes through ~/workspace/tools/solr-gradle.sh (vendored JDK 21 and Gradle 9.7.0, proxy relay, shared dependency cache), never ./gradlew.

Gate shape, per gates/SOLR-5262.md: packaging and changelog parse are already recorded done (packaged head above; 223 unreleased entries valid). Remaining steps: tidy with the GATE INVALID guard (tree must stay clean after gradle/libs.versions.toml is restored), Error Prone compile, proof leg (production CoreDescriptor.java reverted to the merge-base, branch tests kept; testUlogDirDefaultsToDataDir must fail with `expected:<data/> but was:<null>`, checked by failure content), focused org.apache.solr.core.TestCoreDescriptorImplicitProperties at the head counted from fresh JUnit XML, and :solr:core:check -x test. Premise is HELD by the vm2 premise run; the gate does not claim a full core load of the scratch config (the recorded premise limit).

Worktree: ~/workspace/solr-worktrees/solr-5262-gate. Runner: ~/workspace/tools/g5262-gate.sh (blocking flock on ~/workspace/tools/.solr-test-queue.lock). Log: ~/workspace/tools/g5262-gate.log.

On green: vm1 writes receipts/SOLR-5262.md at the gated head, marks gates/SOLR-5262.md DONE, and marks this claim DONE. On failure: record the step, the log evidence, and the classification in the takeover log; a gate that executed tests and failed is not relaunched until the main agent investigates.

Heartbeat: 2026-10-11T14:58:00Z (UTC) (claim; runner starts detached on vm1).
