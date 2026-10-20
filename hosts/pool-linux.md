# Host: pool Linux host

Every Linux pool host has the same capabilities. A host's label (the name a claim uses) is only a tracking name. One host also carries the bats tag; see its file in this folder.

- Capability tags: review, draft, records, implementation, gate, premise-run, settling-run, sweep. Not bats or npm unless this host's own file in hosts/ lists them.
- Toolchain: JDK 21.0.12.1 (Temurin) and Gradle 9.7.0 through the repo wrapper. The JDK install path can differ between hosts, so set JAVA_HOME to the local install and record the path in the claim.
- Build serialization: one Gradle build per host. Take the host lock with `flock -n` on `/workspace/tools/.gradle-$(hostname -s).lock`. If `flock -n` fails, a build is already running on this host and the tick exits.
- Shared storage: `/workspace` is an NFS mount, and the Gradle home `/workspace/gradle-home` sits on it. Whether every host sees the same export is open in `assignments/pool-host-resource-census-round-1.md`; keep the disk guard below until that closes.
- Disk guard: stop and record in the claim if free space on `/` is under 2 GB, or if free inodes on `/` are under 10%.
- Heartbeat: once an hour while a claim is active.
- Handback: a claim the host cannot finish is released with its reason written in the claim file, and the assignment is set to HANDED. Any host may pick up a HANDED item once the decision it waits for is recorded in the item.
