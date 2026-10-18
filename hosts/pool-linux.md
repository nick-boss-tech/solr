# Host: pool Linux host

Every Linux pool host has the same capabilities. A host's label (the name a claim uses) is only a tracking name. The one exception is vm1, which can run npm and BATS; see `hosts/vm1.md`.

- Capability tags: review, draft, implementation, gate, premise-run, settling-run, sweep. Not bats or npm; those need vm1.
- Toolchain: JDK 21.0.12.1 (Temurin) and Gradle 9.7.0 through the repo wrapper. The JDK install path can differ between hosts, so set JAVA_HOME to the local install and record the path in the claim.
- Build serialization: one Gradle build per host. Take the host lock with `flock -n` on `/workspace/tools/.gradle-$(hostname -s).lock`. If `flock -n` fails, a build is already running on this host and the tick exits.
- Shared storage: `/workspace` is an NFS mount, and the Gradle home `/workspace/gradle-home` sits on it. Whether every host sees the same export is open in `assignments/pool-host-resource-census-round-1.md`; keep the disk guard below until that closes.
- Disk guard: stop and record in the claim if free space on `/` is under 2 GB, or if free inodes on `/` are under 10%.
- Heartbeat: once an hour while a claim is active.
- Handback: a claim the host cannot finish is released with its reason written in the claim file, and the assignment is set to HANDED. Only vm1 picks up HANDED items.
