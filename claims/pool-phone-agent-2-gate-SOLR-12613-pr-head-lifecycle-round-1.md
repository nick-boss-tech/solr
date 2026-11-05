# Claim: SOLR-12613 actual PR-head lifecycle gate, phone-agent-2, round 1
- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: ACTIVE; exact-head gate proof in progress.
- Job: `gates/solr-12613-pr-head-lifecycle-round-1.md`.
- Scope: complete all positive, paired-neighbor, and isolated fail-before proof legs at pinned P/F; verify logs, fresh XML, final clean source, and final live refs; write the gate receipt on completion. No PR/source-branch edits, public comments, Jira writes, or PR state changes.
- Capability tags used: `gate`, `selenium`, `review`, `records`.
- Started: 2026-11-05T19:36:08Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat under `WORKFLOW.md`, Claims. Attempts: 1. Idempotency key: `SOLR-12613-pr-head-lifecycle-round-1@f02bb7bb06decce976c3ed2fdfbf5907764c2183`.
- Links: Ticket `SOLR-12613`; open Apache Solr PR #5110. Category: gate-only (no `pool/work-map.md` on the live branch). Siblings: prior Windows browser verification and the superseded synthetic selection-frontend gate, both inputs named by this job. Supersedes: none.
- Board snapshot at claim: `31e38aae65bbb51d7fd9224e4cbca6dfc89862aa`. The exact gate is OPEN; no claim file for this execution or other live pool gate claimant was present.
- Candidate: fork `nick-boss-tech/solr:pr-5110-core-menu-lifecycle`, F `f02bb7bb06decce976c3ed2fdfbf5907764c2183`; its only parent P is `6b5d830132b941faa85856347099ade8b190be0e`, the current open PR #5110 head. The approved diff contains only `solr/webapp/web/js/angular/app.js` and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java`.
- Owner authorization: continue this already-approved gate after registering the verified Linux Selenium capability and routing. Keep source and PR unchanged; the required fail-before overlay is isolated and restored before final source verification. If the live pins move or any repair would need to be pushed, stop and hand back rather than changing source or PR state.
- Environment: Ubuntu 24.04.4; JDK 21.0.12.1 at `/usr/lib/jvm/java-21-openjdk-amd64`; Gradle 9.7.0 wrapper; Chrome 155.0.8059.39; cached ChromeDriver 155.0.8059.84; Java Selenium modules. Use `/workspace/gradle-home`, host lock `/workspace/tools/.gradle-phone-agent-2.lock`, and the disk/inode guard for every Gradle run. `NODE_ENV` is unset for the detached preflight.
- Pre-claim preflight reused: detached `pool/bin/gate-preflight.sh` at exact F completed 2026-11-05T19:03:49Z to 19:05:29Z, exit 0, 62 actionable tasks, `tidy clean at f02bb7bb06decce976c3ed2fdfbf5907764c2183`. No source changes; minimum observed free inodes 955,677/1,868,096 (51.16%). Logs: `/workspace/gates/logs/phone-agent-2/solr-12613-pr-head-lifecycle-round-1/preflight/preflight.log`, `preflight-tidy.log`, `headroom.tsv`.
- At claim check: host lock free; no Gradle/ChromeDriver test process. Root free inodes 966386/1868096 (51.73% free), free disk 49465462784 bytes.
- Logs/evidence root: `/workspace/gates/logs/phone-agent-2/solr-12613-pr-head-lifecycle-round-1/`.
- Receipt reserved: `receipts/SOLR-12613-pr-head-lifecycle-round-1.md`. Gate stays OPEN/Implemented, Proof Pending unless all required proof legs pass at final verified F.
Decision: rung 3, direct owner instruction authorizes this exact gate. No product source repair or public action is authorized by this claim.
Heartbeat: 2026-11-05T19:36:08Z
Result: pending exact-head gate execution.
