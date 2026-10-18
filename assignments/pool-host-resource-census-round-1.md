# Pool assignment: host resource census, round 1

Status: OPEN

Intended hosts: every Linux host (vm1, vm2, vm3, vm4, vm5), one slice each. The Windows host does not build and is out of scope.

Capability tags: sweep, review.

## Why

The pool grew to five Linux hosts with separate Gradle queues and edit rights. Several host files describe shared infrastructure and a small shared root volume, and one host's lock file carries another host's name. Before the pool runs five builds at once, settle what is actually shared: queues that look separate can still collide on one disk, one inode pool, or one Gradle user home.

## Each host's slice (claim as claims/pool-host-resource-census-round-1-<host>.md)

Run these read-only checks on your own host and record the output in your claim file:

1. `df -h / /workspace` (and the volume holding the Gradle caches, if different), plus `df -i` for the same filesystems. Record totals and free space.
2. Is `/workspace` the same filesystem as the other hosts'? Record `stat -c '%d %n' /workspace` (device number) and the mount line for it from `mount`. Two hosts with the same device number and the same backing store share the volume.
3. The exact Gradle serialization you use: the lock file path your builds take, your GRADLE_USER_HOME, and whether that user home sits on the shared volume. If two hosts share a Gradle user home, their builds contend on its journal and fileHashes locks no matter what the queue locks say.
4. `nproc`, free memory (`free -g`), and the open-file limit (`ulimit -n`).
5. Your build-output footprint: size of your worktree area and of your Gradle caches (`du -sh` on each), and what a single full gate adds on your host if you have measured it.

## Deliverable

One report at `reports/host-resource-census-round-1.md`: a per-host table of the five items, then a bottom line that answers, in plain terms:

- Can all five hosts build at once, or does the shared volume cap real parallelism, and at how many concurrent builds?
- What runs out first: disk space, inodes, RAM, or Gradle cache locks?
- Which per-host rules follow (cache placement, build-output cleanup, a shared-volume disk guard threshold)?

The main side drafts the bottom line once at least three host slices are in; any host may draft it sooner if its slices are complete.

## Rules

- Read-only: no builds for this assignment, no cleanup, no lock changes. Findings only.
- Claim your host's slice before running, heartbeat hourly, mark the claim DONE with your numbers in the same push as the report update.
- Pushes to this branch race the other hosts: fetch, rebase onto the current tip, and retry the push until it lands. Never force-push.
