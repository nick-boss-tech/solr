# Receipt: gradle-help in the source tarball (vm6, 2026-10-25)

Branch: `solr-gradle-help-ship-in-tarball` on nick-boss-tech/solr, head `d2923cb65b`, base apache main `07fdebdda5`. Commits: `.gitattributes` and `gradle/validation/rat-sources.gradle` (2 files, 5 insertions, 1 deletion). Premise and history: `reports/tarball-help-files-premise-round-1.md`.

## Runs (proof base and head)

Each run used a fresh tree made with `git archive` (no `.git`), under `flock /workspace/tools/.gradle-vm6.lock`, JAVA_HOME `/workspace/jdk/jdk-21.0.12.1+1`, GRADLE_USER_HOME `/workspace/gradle-home`, NODE_ENV unset. Host free space on `/` at start: 4,808,920 KB; free inodes 479,454 (91%).

| Tree | Command | Result |
|---|---|---|
| base `07fdebdda5` | `./gradlew --offline :allHelpFilesExit` | exit 1, "Help file missing: dev-docs/gradle-help/workflow.txt"; 0 help files in the archive |
| head `d2923cb65b` | `./gradlew --offline :allHelpFilesExit` | exit 0; 12 help files in the archive (11 required, plus README.md) |
| head `d2923cb65b` | `./gradlew rat` (online) | exit 0, BUILD SUCCESSFUL in 1m 10s |
| mutation: head minus the fallback `exclude "dev-docs"` | `./gradlew rat` (online) | exit 1: `/dev-docs/gradle-help/README.md  Unknown license` |

Notes:
- `rat --offline` could not resolve the RAT jars (not in the offline cache). The RAT runs above are online runs, not offline ones.
- The mutation shows the second commit is needed: without the fallback exclude, the source-release RAT scan fails on `README.md`, which has no license header and is not `.txt`.

## Archive listing

`git archive d2923cb65b | tar -t | grep '^dev-docs/'`: the directory entry, `dev-docs/gradle-help/` and its 12 files. No other `dev-docs/` entry.

## Not run

- Full `./gradlew check` from the archive (vm5's earlier run took about 2 hours and was not repeated).
- `precommit` on the branch.
