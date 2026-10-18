# Host resource census, round 1

Assignment: assignments/pool-host-resource-census-round-1.md. All four factory hosts completed their slices and marked their claims DONE (claims/pool-host-resource-census-round-1-vm2.md, -vm3.md, -vm4.md, -vm5.md).

Record note (main side, 2026-10-17): the vm3, vm4, and vm5 sections were lost when commit b5eeb41fac rewrote this file from a stale base; they are restored verbatim below from the pre-rewrite version at db54e19a45. The vm2 section is the current one.

## vm2

| Item | Result |
| --- | --- |
| 1. Disk | `/` (ext4, /dev/vdc): 7.8G total, 2.8G used, 4.6G free, 39%. `/workspace` (NFS, 127.0.0.1:/agents/vi2g484tmb/workspace): reports 8.0E available. Inodes: `/` 524288 total, 43243 used, 9%. `/workspace` reports 0 inodes (NFS, not reliable). |
| 2. Shared `/workspace`? | `stat -c %d /workspace` = 47. `/tmp` and `/home/claude` are device 65056 on `/`. Mount: `nfs4 rw, vers=4.2, hard, proto=tcp, port=20890, addr=127.0.0.1, export /agents/vi2g484tmb/workspace`. Whether another host mounts the same export cannot be seen from vm2. Each host should compare its export path and device number with this line. |
| 3. Gradle | GRADLE_USER_HOME=/workspace/gradle-home, device 47, so on the NFS volume and not on `/`. 6 `*.lock` files under it right now. Build runs use `--no-daemon` and a per-run tests.workDir under /tmp. Lock file paths: gradle-home/caches/*.lock (user-id, modules-2, jars-9, and others). |
| 4. Compute | nproc 4. Memory 7G total, 5G free, 6G available. `ulimit -n` 65536. |
| 5. Footprint | Gate worktrees measured: 247M (17268-native), 247M (17268-fix), 170M (17268-push). 160 scratch worktree directories under /workspace/gates. Gradle caches: not measured (du on /workspace/gradle-home timed out at 400s on NFS). A full gate on this host has not been measured for disk. |

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

## Bottom line (consolidated, main side)

- Storage is NOT shared between hosts. Each host mounts its own NFS export under a different agent path: vm2 `vi2g484tmb`, vm3 `om9wpuz85k`, vm4 `ysyf6wtgd7`, vm5 `hg3bt762n6`. All hosts report device 47 for /workspace, which made it look shared; the device number is not proof of sharing, the export path is, and the paths differ.
- So the Gradle queues are genuinely separate: each host's GRADLE_USER_HOME sits on its own export, and cache-lock contention is limited to each host's own builds. vm5 using a lock named .vm3-gradle.lock is a naming problem only, on its own volume.
- The binding constraint on every host is the local root disk: 7.8 GB total, about 4.5 to 4.6 GB free, inodes about 9 to 10 percent used. Each host has 4 CPUs and 7 GB RAM. Builds write test workdirs and scratch worktrees that grow faster than caches (vm2 already has 160 scratch worktree directories under /workspace/gates). The cron prompt's stop rules (under 2 GB free on /, under 10 percent inodes free) are the right guard.
- Conclusion: all four factory hosts can build in parallel. Watch root-disk free space per host, and prune finished scratch worktrees; that, not CPU or shared storage, is what will stop the burn first.
