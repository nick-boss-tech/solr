# Claim: SOLR-18602 alias opt-out control test, round 2 (phone-agent-2)

- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: ACTIVE; exact-head Linux gate proof only.
- Job: `gates/pr-5259-solr-18602-alias-optout-control-test-round-2.md`.
- Slice: run the whole superseding gate at F2 and H, including the standalone F2 root check, focused positive suites at both heads, the coverage-only fail-before mutation, final ref/source verification, and the executor-owned receipt.
- Capability tags: `gate`, `review`, `records`.
- Started: `2026-11-06T06:08:26Z` (UTC). Live board snapshot immediately before claim: `d1cf8446087859362fa873e4455ff9795bad1da9`; the round-2 gate and claim paths were absent, and the fork ref and PR pins were refreshed.
- Public PR #5259: OPEN and unmerged at H `ec8e706a41a0213dd924b286b1f5ebfc476f7cd0`, base `35e2be2a7de9855a0f909613065ba22243097102`.
- Fork branch `nick-boss-tech/solr:solr-18602-pr5259-alias-optout-test`: F2 `2efbd9884eac22cc8ddd0bdad2f8715f800eebee`, a direct child of F1 `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7`; F1's parent is H. Only the new test file is changed across H..F2. The F1-to-F2 commit is Nick-authored and contains only the formatter's single-line `expectThrows` reflow.
- Supersedes the immutable round-1 claim `claims/pool-phone-agent-2-gate-pr-5259-solr-18602-alias-optout-control-test-round-1.md`; the additive status record states that no proof tests ran at F1.
- Exact-F2 detached tidy preflight: 2026-11-06T06:05:25Z–06:06:58Z; Gradle 9.7.0, 62 actionable tasks, exit 0, `tidy clean at F2`. Watchdog: 33 samples; minimum 778,286/1,868,096 free inodes (41.6620%) and 45,666,197,504 bytes free disk; no stop marker. Full logs and monitor: `/workspace/gates/logs/phone-agent-2/pr-5259-solr-18602-alias-optout-control-test-round-2/preflight/`.
- Environment: Linux; OpenJDK 21.0.12.1 (`JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64`), Gradle wrapper 9.7.0, `GRADLE_USER_HOME=/workspace/gradle-home`, Git 2.43.0. Host lock: `/workspace/tools/.gradle-phone-agent-2.lock`; no Gradle/gate process was running and the lock was free when this claim was prepared. All legs will be serialized under that lock and watched continuously; stop at <=15% free inodes or <2 GiB free disk.
- Evidence root: `/workspace/gates/logs/phone-agent-2/pr-5259-solr-18602-alias-optout-control-test-round-2/`.
- Receipt target: `receipts/pr-5259-solr-18602-alias-optout-control-test-round-2.md`.
- No PR opening/body edit, public PR comment/review, Jira write, upstream write, merge/rebase, or unrelated source edit is authorized or planned. Only the already-pushed tidy-only repair is a source change.