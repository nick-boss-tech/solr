# Claim: pool-ui-SOLR-17861

- Claimant host: vm6, matching `hosts/vm6.md` (`build` tag granted by Nick, 2026-10-24).
- Status: LIVE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, row SOLR-17861, one ticket per claim.
- Ticket type: build (Gradle license and checksum check). The immediately previous claim by vm6 was premise-run (`claims/ui-17676-nobrowser-probe-round-1.md`), so owner complaint 5 is met.
- Slice: the first slice from the staged ask in `jira/jira-comment-SOLR-17861-draft.md` (option A): a `ui`-local check of LICENSE and NOTICE presence and SHA-1 checksums for the `desktopApp` JVM runtime classpath resolved for `linux_x64`, wired into `./gradlew -p ui check`. The other OS classifiers, the wasmJs yarn lock, and any change to the root build are out of this claim.
- Owner direction: Nick, 2026-10-24, "You can start anyway". The staged ask has not been posted and has no engagement from Christos. The decision map's rule that UI code changes wait for engagement is set aside for this ticket by that direction, not by a map change.
- Base: apache/solr main `c8ab71430f` (the round's base). Work branch `solr-17861-ui-license-check`, pushed to the fork only after a green gate.
- Capability tags used: build, gate, implementation.
- Started: 2026-10-24T16:42:07Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17861@c8ab71430f.
- Links: Ticket: SOLR-17861 (updateLicenses task covering the KMP module). Category: build. Siblings: `claims/ui-volunteer-drafts-round-1-item1.md` (the staged ask). Evidence: `reports/new-ui-platform-build-round-1.md`. Supersedes: none.
- Constraints: no JIRA or GitHub post, no PR. Commit as Nick Shanin with no trailers. Push fast-forward only. No dependency added or changed, so the root AGENTS.md rule (`updateLicenses resolveAndLockAll --write-locks` after a dependency change) does not apply; if the design needs a new coordinate, stop and record it.
- Decision ladder: rung 1 for the layout (root convention in `gradle/validation/jar-checks.gradle`: `licenses/<jar>.sha1` and `<jar>-LICENSE-<type>.txt` / `<jar>-NOTICE.txt`). The host is aarch64, so `compose.desktop.currentOs` would resolve linux-arm64 jars; the check names `compose.desktop.linux_x64` explicitly. Rung 4 applies to the presence-check scope, recorded in the report once the classpath is measured.

Heartbeat: 2026-10-24T16:42:07Z
