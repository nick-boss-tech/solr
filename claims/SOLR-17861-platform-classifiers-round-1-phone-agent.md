# Claim: SOLR-17861 platform classifiers gate, round 1 (phone agent)

- Claimant host: phone-agent, matching `hosts/phone-agent.md`.
- Status: LIVE.
- Job: `gates/SOLR-17861-platform-classifiers-round-1.md`, at exact head `b0b0b39f7e62fa0247e133d4019bbb23f9def446` on branch `solr-17861-ui-license-check`.
- Routing: the gate file names vm6 as host because it wrote the prior receipt. The owner directed the phone agent to take this gate instead (2026-10-27), with the standing point that work is not routed to an individual VM just because it worked on the item before. This claim supersedes the gate file's host line only; every leg, the exact head, and the PASS criteria are unchanged.
- Capability tags used: ui, gate, jvm-tests.
- Toolchain on this host: the vendored JDK 21 and Gradle 9.7.0 via `~/workspace/tools/solr-gradle.sh` (the wrapper jar is not used in worktrees on this VM). Serialized on this host's build lock, `~/workspace/tools/.solr-test-queue.lock`.
- Worktree: `~/workspace/solr-worktrees/17861-gate`, detached at the exact head.
- Constraints: no JIRA or GitHub post, no PR, no push. If formatting changes files at the preflight leg, record the repair and stop, per the gate file.
- Result: recorded in `receipts/SOLR-17861.md` as a superseding head record when the run completes.
