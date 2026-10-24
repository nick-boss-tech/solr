# SOLR-17861 first slice, round 1

Claim: `claims/pool-ui-SOLR-17861.md` (vm6). Scope: the checksum half of the staged ask in `jira/jira-comment-SOLR-17861-draft.md` (option A). Nothing posted to JIRA or GitHub; no PR opened.

## Result

- Branch: `solr-17861-ui-license-check` on nick-boss-tech/solr, base apache/solr main `c8ab71430f`. Commits `022acd4ec9` (build change and 101 checksum files) and `92aed103a0` (draft PR description `pr-draft/SOLR-17861.md`). Pushed as a new branch after the gate.
- Gate on the committed tree, host vm6 (linux-aarch64): `./gradlew -p ui :desktopApp:updateLicenses :desktopApp:validateLicenses :desktopApp:spotlessCheck :desktopApp:check` returned BUILD SUCCESSFUL. Regenerating reproduces the checked-in files byte for byte. Log: `/workspace/gates/logs/vm6/pool-ui-17861/final-17861-165817.log`.
- Mutation (rung 2): one checksum corrupted, one deleted, one stale file added; `validateLicenses` failed with all three messages. Restored and passing. Log: `mut-17861-165555.log`.
- Host-specific jars: the runtime classpath on this host carries `desktop-jvm-linux-arm64` and `skiko-awt-runtime-linux-arm64`. The check excludes them and adds `compose.desktop.linux_x64`; the resulting file set has the linux-x64 modules and none of the arm64, macOS or Windows ones.

## Decisions

- Layout (rung 1): root convention for checksum files (`licenses/`, one `.sha1` per jar, `updateLicenses` writes them) with the file name keyed by group. Jar names collide across `androidx.*` and `org.jetbrains.androidx.*` (`runtime-desktop-1.12.1.jar`, `lifecycle-common-jvm-2.11.0.jar` differ in bytes); the first run failed on exactly these 10 entries.
- Project jar excluded (rung 1): `shared-jvm.jar` is this build's own output.
- Location (rung 1, `pool-ui-tickets-round-1.md` and staged ask): the check lives in the `ui` build, not the root, because `ui` is independent since #4650.
- Owner direction: "You can start anyway" (Nick, 2026-10-24), recorded in the claim. This overrides the decision map's wait for staged-ask engagement for this ticket.

## Not done in this slice

- LICENSE and NOTICE presence per jar (root `<jar>-LICENSE-<type>.txt` form). This is 101 jars of license text and type decisions. Question for the owner: include it in this ticket's first slice, and if so, where does each license text come from (the jar's own META-INF, or a reviewed source)?
- Other OS classifiers and the wasmJs yarn lock.
- `./gradlew -p ui check` was not run here: `:shared:wasmJsBrowserTest` needs a browser, and this host has none (see `reports/ui-17676-nobrowser-probe-round-1.md`).
- Not verified on an x86_64 host.

## Status

NEEDS-OWNER for the LICENSE/NOTICE scope question above. The checksum slice is complete on the branch; no further host work is scheduled.
