# Host resource census, round 1

Assignment: assignments/pool-host-resource-census-round-1.md. vm2 slice: claims/pool-host-resource-census-round-1-vm2.md. Read-only. Other hosts' rows are missing and must come from their own claim files.

## vm2

| Item | Result |
| --- | --- |
| 1. Disk | `/` (ext4, /dev/vdc): 7.8G total, 2.8G used, 4.6G free, 39%. `/workspace` (NFS, 127.0.0.1:/agents/vi2g484tmb/workspace): reports 8.0E available. Inodes: `/` 524288 total, 43243 used, 9%. `/workspace` reports 0 inodes (NFS, not reliable). |
| 2. Shared `/workspace`? | `stat -c %d /workspace` = 47. `/tmp` and `/home/claude` are device 65056 on `/`. Mount: `nfs4 rw, vers=4.2, hard, proto=tcp, port=20890, addr=127.0.0.1, export /agents/vi2g484tmb/workspace`. Whether another host mounts the same export cannot be seen from vm2. Each host should compare its export path and device number with this line. |
| 3. Gradle | GRADLE_USER_HOME=/workspace/gradle-home, device 47, so on the shared NFS volume and not on `/`. 6 `*.lock` files under it right now. Build runs use `--no-daemon` and a per-run tests.workDir under /tmp. Lock file paths: gradle-home/caches/*.lock (user-id, modules-2, jars-9, and others). |
| 4. Compute | nproc 4. Memory 7G total, 5G free, 6G available. `ulimit -n` 65536. |
| 5. Footprint | Gate worktrees measured: 247M (17268-native), 247M (17268-fix), 170M (17268-push). 160 scratch worktree directories under /workspace/gates. Gradle caches: not measured (du on /workspace/gradle-home timed out at 400s on NFS). A full gate on this host has not been measured for disk. |

## Bottom line for vm2 (partial, pending the other four hosts)

- Disk on `/` is the tightest local resource: 4.6G free. A full gate's build output adds to it, and the worktrees grow faster than the caches.
- Gradle caches sit on the NFS `/workspace` volume, so any two hosts that mount the same export contend on the same caches and lock files.
- RAM (7G, 4 cores) is likely the cap on concurrent test JVMs on this host. Not measured under load.
- Inodes are not the limit on `/` (9% used).
- Open items: measure the Gradle cache size on a non-NFS path, and check whether another host shares the NFS export.
