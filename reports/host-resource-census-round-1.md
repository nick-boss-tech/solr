# Host resource census, round 1

Assignment: `assignments/pool-host-resource-census-round-1.md`. Status of the assignment: OPEN until every Linux host reports. Each host adds its own slice below; the bottom line is drafted by the main side.

## vm5 (checked 2026-10-18T02:34Z)

| Item | Result |
| --- | --- |
| 1. Root filesystem `/` (ext4, local disk) | 7.8 GB total, 2.8 GB used, 4.6 GB available (39% used). Inodes: 524,288 total, 42,892 used, 481,396 free (9% used). |
| 1. `/workspace` | NFS4 mount from `127.0.0.1:/agents/hg3bt762n6/workspace`, reported 8.0E size, 883 GB used. The NFS server reports 0 inodes, so inode headroom on `/workspace` is not visible from this host. |
| 2. Same filesystem as other hosts? | `stat -c %d /workspace` returns 47, the same device number as `/workspace/tools` and `/workspace/gradle-home`. The mount source contains an agent identifier (`hg3bt762n6`). This host cannot tell whether another host mounts the same export. Some gate logs from vm2 (`/workspace/gates/logs/vm2/`) are not present here, which suggests vm2 does not see this export. Other hosts should run the same `stat` and `findmnt` checks and compare the source string. |
| 3. Gradle serialization | Lock file: `/workspace/tools/.vm3-gradle.lock`. The name carries another host's label; it is the lock this host uses for every build. GRADLE_USER_HOME: `/workspace/gradle-home`, on the NFS mount (device 47), not on the local disk. Gradle's cache locks therefore sit on the shared mount. |
| 4. CPU, memory, open files | 4 CPUs. 7 GB RAM, 4 GB available. `ulimit -n` 65,536. |
| 5. Footprint | `/workspace/gradle-home` 1.2 GB. `/workspace/worktrees` 764 MB. Full gate footprint on this host not measured in this slice (read-only). |

Observations for the bottom line:
- The local disk is the tightest small pool: 4.6 GB free on `/`, with the Gradle cache on NFS instead.
- The lock file name `.vm3-gradle.lock` is used by this host. Rename it per host so the name no longer points at another host.
- If hosts share the NFS export, every build contends on the same Gradle user home. Gradle locks files in that home during dependency resolution and cache updates, so concurrent builds can block each other even when their lock files differ.
