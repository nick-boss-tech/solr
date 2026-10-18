# Claim: host resource census, round 1, slice vm3

Status: DONE 2026-10-18T02:40:21Z (assignments/pool-host-resource-census-round-1.md; slice in reports/host-resource-census-round-1.md)

- Claimed and completed by vm3. Read-only checks only: no builds, no cleanup, no lock changes.
- Numbers (root `/` ext4, /workspace NFS, read at 2026-10-18T02:40:21Z):
  - `df -h`: / 7.8G total, 2.9G used, 4.5G free (40%). /workspace 8.0E reported, 883G used (NFS export 127.0.0.1:/agents/om9wpuz85k/workspace).
  - `df -i`: / 524,288 total, 49,552 used, 474,736 free (10%). /workspace reports 0 inodes (NFS does not report them).
  - `stat -c '%d %n'`: /workspace device 47; / device 65056.
  - Mount: /workspace is nfs4 (rw, hard, vers=4.2, port 20135). / is ext4.
  - Gradle: lock file /workspace/tools/.vm3-gradle.lock (taken with flock). GRADLE_USER_HOME=/workspace/gradle-home, which sits on the NFS /workspace volume. TMPDIR=/workspace/tmp.
  - nproc 4. free -g: 7 GB total, 3 GB used, 3 GB free. ulimit -n 65536.
  - Footprint: gradle-home 1.4G, worktrees 2.1G, gates 7.1M, jdk 347M. A full gate footprint was not measured in this slice.
- Shared storage: this host's export is 127.0.0.1:/agents/om9wpuz85k/workspace. vm4 reports agents/ysyf6wtgd7 and vm5 reports agents/hg3bt762n6. The export paths differ, so /workspace is per-host storage even though every host reports device 47. vm5's lock name .vm3-gradle.lock is on vm5's own volume, so it is a naming problem only, with no contention.
- Heartbeat: 2026-10-18T02:40:21Z (claim).
