# Host: phone-agent-2 (Phone Agent 2)

Pool Linux host. Shared capabilities and operating rules are in [pool-linux.md](pool-linux.md).

- Identity: Phone Agent 2, distinct from [Phone Agent](phone-agent.md), the host formerly called vm1. Use `phone-agent-2` in new claims.
- Capability tags: review, draft, records, implementation, gate, premise-run, settling-run, sweep, bats, npm.
- Verified toolchain: Ubuntu 24.04.4 LTS; OpenJDK 21.0.12.1 (Ubuntu); Gradle 9.7.0; Node.js 24.19.0; npm 11.17.0; BATS 1.10.0.
- Local JDK: `/usr/lib/jvm/java-21-openjdk-amd64`. Use the target source checkout's Gradle wrapper for builds and follow the shared build lock and disk guard rules.
- Role beyond the pool: none. Takes work by claim at Nick's direction, like other pool hosts.
- Capacity-based routing: avoid inode-intensive builds, especially full Antora or Ref Guide builds, when another eligible host has safer headroom. Use this host only when no other capable host is available and Nick directly directs the claim. Check inodes before and during a build; stop at 15% free. Do not clean files without explicit approval.
- Heartbeat: 2026-10-22T22:46Z (registered as Phone Agent 2 at Nick's direction; tool versions verified locally; no pool assignment claimed by this registration).

- Heartbeat: 2026-10-23T00:20:26Z; directed round `phone-agent-2-blockers-round-1` completed. Report and terminal claim delivered; no build remains running. SOLR-17844 green, SOLR-17840 and SOLR-18446 explicitly handed back.

- Heartbeat: 2026-10-23T06:09:30Z; completed SOLR-16914 unit 15 from the named NEARLY handoff. All three guide regates are GREEN at the unchanged head; no product repair or running build.

- Heartbeat at takeover: 2026-10-23T06:12:30Z; took over SOLR-5821 unit 23 from the named NEARLY handoff on the newdev/Blocker/Critical review round.

- Heartbeat: 2026-10-23T06:21:49.802110+00:00; completed SOLR-5821 unit 23 at `327547d693e098447ff70c536b794aa364508670` with all three guide regates GREEN. No repair, product commit, or running build.

- Heartbeat at claim: 2026-10-23T06:24:00Z; claimed SOLR-8755 unit 45, the final unit in the top-priority review round.

- Heartbeat: 2026-10-23T06:53:32Z; completed SOLR-8755 unit 45 with all three review gates GREEN at the unchanged head. No product repair, product push, or running build.

- Heartbeat: 2026-10-23T06:58:00Z; claimed SOLR-5735 item 2, the local-disk repeatability run for the pinned test seed. Three reruns and the diagnostic report are pending.

- Heartbeat: 2026-10-23T07:06:00Z; completed SOLR-5735 item 2, three local ext4 seeded runs passed at unchanged upstream/main `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Report and full compressed JUnit artifacts published. No product changes or running build.

- Heartbeat: 2026-10-23T07:15:00Z; took over consistency-docs item 4 from vm4 for Antora on `docs-streaming-allowlist-note` at `4b185504ef7dd5e4530664995719a0e743564382`. This host has npm capability.

- Heartbeat: 2026-10-23T07:21:40.292402+00:00; completed consistency-docs round 1 item 4 at unchanged `4b185504ef7dd5e4530664995719a0e743564382`. Antora and direct site link validation GREEN. No product change, PR or running build. Receipt: `receipts/consistency-docs-round-1-item4.md`.

- Heartbeat: 2026-10-23T07:25:00Z; claimed the SOLR-18249 backup-metadata and async-status reproduction assignment. This is source/synthetic-test work only; no production access or product changes.

- Heartbeat: 2026-10-23T07:31:21.156662+00:00; completed SOLR-18249 source/synthetic investigation. Six focused tests passed; temporary fixture removed, source clean; production match unresolved. No build running. Per owner direction, the separate paired-PR claim is left untouched for owner resolution.

- Heartbeat: 2026-10-23T07:32:00Z; ended the accidental paired PR review claim because its assignment excludes phone-agent-2. The assignment is open again; no review findings, PR actions, or product changes were made.

- Heartbeat: 2026-10-23T07:34:00Z; claimed the recurring merged-PR follow-up promises audit, pass ad, from Store 1. This pass is read-only.

- Heartbeat: 2026-10-23T07:42:00Z; claimed integrated SOLR-18555 phase-r4 SolrJ/query gate at unchanged head `07953eab473f3f05853916f7efb4a42edc1edb0c`. The gate requires core, SolrJ, SolrJ HTTP, and reference-guide checks.

- Heartbeat: 2026-10-23T07:44:40Z; SOLR-18555 integrated phase-r4 handed back before any build because the standard runners require a proof the job explicitly does not request and cannot express the stated changelog check. Claim ENDED; no product change, repair, retry, or running build. Gate and receipt record the supported-runner prerequisite.
- Heartbeat: 2026-10-23T17:36:00Z; claimed `assignments/pool-objectrelease-autocloseable-round-1.md`. Local candidate only, based on current Apache main. No product push or PR action.

- Heartbeat: 2026-10-25T00:53:15Z; claimed `claims/critical-comments-round-1-batch-01.md` under the directed Critical comment-drafting assignment. Scope is batch 01 only; review and individual unposted drafts are in progress.

- Heartbeat: 2026-10-25T03:08:30Z; claimed `claims/warm-ticket-evidence-units-round-2-unit-3-SOLR-18500-phone-agent-2.md` for the read-only SOLR-18500 curl-quoting audit. No build or test planned.

- Heartbeat: 2026-10-25T03:45:30Z; claimed `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-post-tidy.md` for `gates/pr-5108-reverse-cursor-round-1-post-tidy.md` at candidate head `a3d97ca294f43adce99557fe011da90cd2528ce9`. Exact five-file tidy repair is committed and pushed; clean detached preflight completed. The serialized gate commands are pending. #5107 is explicitly sequenced after this gate and remains untouched.
- Heartbeat: 2026-10-25T03:50:20Z; current post-tidy claim is pinned at `a3d97ca294f43adce99557fe011da90cd2528ce9`; no gate test has run. Source inspection found the standard distributed fixture is one shard/two cores, below the gate's required multi-shard layout. The gate-mandated test-fixture-only correction is pushed at `2c8af559c8456ded97811455622fb06786ae74ba`; a clean preflight and superseding claim are required before tests. #5107 remains deferred.
- Heartbeat: 2026-10-25T03:54:24Z; active claim `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard.md` at `2c8af559c8456ded97811455622fb06786ae74ba`; detached preflight is clean (`receipts/SOLR-17786-gate-round-1-two-shard/preflight/preflight.log`). The only warning was the absent `/workspace/.env`; explicit token permitted successful fetch and tidy. No compile/test/check has started. The prior post-tidy claim is superseded; #5107 remains deferred.
- Heartbeat: 2026-10-25T04:06:06Z; claim `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard.md` ended INCOMPLETE with verdict `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard-verdict-incomplete.md`; receipt `receipts/SOLR-17786.md`. No further Gradle task started. `/` had 139,521 free inodes (16.65%), no active worker/test process, and the host lock was available. Candidate branch remained pinned at `2c8af559c8456ded97811455622fb06786ae74ba`; #5107 remains out of scope.
- Heartbeat: 2026-10-25T04:09:34Z; correction to prior heartbeat: no per-ticket receipt was written because the gate is INCOMPLETE; detailed record is `reports/solr-17786-pr5108-gate-incomplete-resource-round-1.md`, with verdict `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard-verdict-incomplete.md`. Gate remains OPEN, no DONE marker, and no additional Gradle task started. The `receipts/SOLR-17786.md` path is absent.- Heartbeat: 2026-10-25T15:33Z; claimed SOLR-17895 warm-ticket evidence unit 2 (source-only lock-node lifecycle trace). No build, test, code edit, or public post is in scope.
