# Host: phone-agent-2 (Phone Agent 2)

Pool Linux host. Shared capabilities and operating rules are in [pool-linux.md](pool-linux.md).

- Identity: Phone Agent 2, distinct from [Phone Agent](phone-agent.md), the host formerly called vm1. Use `phone-agent-2` in new claims.
- Capability tags: review, draft, records, implementation, gate, premise-run, settling-run, sweep, bats, npm.
- Verified toolchain: Ubuntu 24.04.4 LTS; OpenJDK 21.0.12.1 (Ubuntu); Gradle 9.7.0; Node.js 24.19.0; npm 11.17.0; BATS 1.10.0.
- Local JDK: `/usr/lib/jvm/java-21-openjdk-amd64`. Use the target source checkout's Gradle wrapper for builds and follow the shared build lock and disk guard rules.
- Role beyond the pool: none. Takes work by claim at Nick's direction, like other pool hosts.
- Heartbeat: 2026-10-22T22:46Z (registered as Phone Agent 2 at Nick's direction; tool versions verified locally; no pool assignment claimed by this registration).

- Heartbeat: 2026-10-23T00:20:26Z; directed round `phone-agent-2-blockers-round-1` completed. Report and terminal claim delivered; no build remains running. SOLR-17844 green, SOLR-17840 and SOLR-18446 explicitly handed back.

- Heartbeat: 2026-10-23T05:57:30Z; took over SOLR-16914 unit 15 from the named NEARLY handoff on the newdev/Blocker/Critical review round. Running the guide regate, then passes 2 and 3.
