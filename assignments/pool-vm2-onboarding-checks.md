# Pool assignment: VM2 onboarding checks

Capability tags: `windows-check` analogue for the second Linux VM; only the vm2 host can claim this (claim path: claims/pool-vm2-onboarding-checks.md). Staffing: 1.

Purpose: before vm2 takes gate work from the pool, establish that the machine can run a full hardening gate. On 2026-10-08 this host was blocked by a 1024 open-file limit (soft and hard): the OpenAPI step produced a stub spec and every full gate failed at the module check. These checks settle whether that is fixed and what else the host needs.

Checks, with results written into hosts/vm2.md and this assignment's claim marked DONE:

1. Open-file limit: report `ulimit -n` (soft) and `ulimit -Hn` (hard). Gate work needs 4096 or better.
2. Disk: report free space on the home volume and confirm room for several Solr worktrees plus a Gradle cache (tens of GB).
3. Toolchain: report the Java version available; the gate pattern uses a vendored JDK 21 and Gradle 9.7.0 through a solr-gradle.sh equivalent (the main agent will supply the script if the host lacks it; say so in the host file).
4. Persistence: state whether the home directory survives a VM replacement, if that is known.
5. Smoke test (only if checks 1 to 3 pass): check out any gated branch tip from receipts/, run the changelog parse and tidy steps only, and report the outcome in the claim.

Deliverable: the updated hosts/vm2.md with capabilities confirmed or the blockers named. No gate runs under this assignment.
