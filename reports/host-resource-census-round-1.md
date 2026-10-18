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

## vm4 (checked 2026-10-18T03:05Z)

| Item | Result |
| --- | --- |
| 1. Root filesystem `/` (ext4, local disk, `/dev/vdc`) | 7.8 GB total, 2.9 GB used, 4.5 GB available (40% used). Inodes: 524,288 total, 45,961 used, 478,327 free (9% used). |
| 1. `/workspace` | NFS4 mount from `127.0.0.1:/agents/ysyf6wtgd7/workspace`. `df` reports 8.0E size and 883 GB used, and the NFS server reports 0 inodes, so inode headroom on `/workspace` is not visible from this host. |
| 2. Same filesystem as other hosts? | `stat -c %d /workspace` returns 47, the same device number as `/workspace/gradle-home` and `/workspace/tmp`. The device number is not proof of sharing. The mount source is: `ysyf6wtgd7` here, `hg3bt762n6` on vm5. Different sources mean different exports, so vm4 and vm5 do not share the Gradle home or the lock files. Hosts should compare the source string, not the device number. |
| 3. Gradle serialization | Lock file: `/workspace/tools/.vm4-gradle.lock`, taken by every build through `flock`. `GRADLE_USER_HOME=/workspace/gradle-home`, which sits on the NFS mount, not the local disk. The lock file is empty (0 bytes), so it serves only as a lock target. |
| 4. CPU, memory, open files | 4 CPUs. 7 GB RAM, 4 GB available at the check. `ulimit -n` 65,536. |
| 5. Footprint | `/workspace/gradle-home` 2.4 GB. `/workspace/worktrees` 2.0 GB (about 10 worktrees, mostly detached checkouts with build output). `/workspace/solr` 738 MB. `/workspace/gates` 748 KB (logs). A single full gate's added footprint was not measured separately in this read-only slice. |

Observations:
- The local disk is the tight part: 4.5 GB free on `/`, and about 6 GB of the NFS-side data is build output and caches. Worktree build output is the largest thing to trim, not the Gradle cache.
- Gradle's home is on NFS on this host, so cache locks and journal writes go to the NFS server. Builds on this host are serialized by the lock file, so this does not add parallel contention on its own.
- Worktrees that are finished but not removed add up quickly. vm4 removes its scratch worktrees after each closeout.


## vm3 (checked 2026-10-18T02:40Z)

| Item | Result |
| --- | --- |
| 1. Root filesystem `/` (ext4, local disk, `/dev/vdc`) | 7.8 GB total, 2.9 GB used, 4.5 GB available (40% used). Inodes: 524,288 total, 49,552 used, 474,736 free (10% used). |
| 1. `/workspace` | NFS4 mount from `127.0.0.1:/agents/om9wpuz85k/workspace`, reported 8.0E size, 883 GB used. The NFS server reports 0 inodes. |
| 2. Same filesystem as other hosts? | `stat -c %d /workspace` returns 47, the same device number the other hosts report. The export paths differ (`om9wpuz85k` here, `ysyf6wtgd7` on vm4, `hg3bt762n6` on vm5), so each host has its own `/workspace` export. The shared device number is not evidence of sharing. |
| 3. Gradle serialization | Lock file `/workspace/tools/.vm3-gradle.lock`, taken by every build through `flock`. `GRADLE_USER_HOME=/workspace/gradle-home`, on the NFS mount. `TMPDIR=/workspace/tmp`. |
| 4. CPU, memory, open files | 4 CPUs. 7 GB RAM, 3 GB free (3 GB used, 1 GB cache at the check). `ulimit -n` 65,536. |
| 5. Footprint | `/workspace/gradle-home` 1.4 GB. `/workspace/worktrees` 2.1 GB. `/workspace/jdk` 347 MB. `/workspace/gates` 7.1 MB. A full gate's added footprint was not measured in this read-only slice. |

Observations:
- The lock name `.vm3-gradle.lock` is on this host's own volume. It is a naming problem only, since no other host's build can take it. vm5 uses the same name on its own volume.
- Each host's Gradle home is on its own NFS export, so cache-lock contention is limited to each host's own builds. The per-host lock file is the only serialization needed.
