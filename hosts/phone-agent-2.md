# Host: phone-agent-2 (Phone Agent 2)

Pool Linux host. Shared capabilities and operating rules are in [pool-linux.md](pool-linux.md).

- Identity: Phone Agent 2, distinct from [Phone Agent](phone-agent.md), the host formerly called vm1. Use `phone-agent-2` in new claims.
- Capability tags: review, draft, records, implementation, gate, premise-run, settling-run, sweep, bats, npm.
- Verified toolchain: Ubuntu 24.04.4 LTS; OpenJDK 21.0.12.1 (Ubuntu); Gradle 9.7.0; Node.js 24.19.0; npm 11.17.0; BATS 1.10.0.
- Local JDK: `/usr/lib/jvm/java-21-openjdk-amd64`. Use the target source checkout's Gradle wrapper for builds and follow the shared build lock and disk guard rules.
- Role beyond the pool: none. Takes work by claim at Nick's direction, like other pool hosts.
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
