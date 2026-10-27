# Gate: SOLR-17861 desktop runtime classifiers, round 1

Status: OPEN until the result at the exact head is recorded in `receipts/SOLR-17861.md`.
Tags: ui, gate, jvm-tests

- Fork: `nick-boss-tech/solr`, branch `solr-17861-ui-license-check`.
- Exact head F: `b0b0b39f7e62fa0247e133d4019bbb23f9def446` (parent `51bcb094b0ab50bb5259f9cd8c7f1ec960754301`).
- Prior receipt: `receipts/SOLR-17861.md` covers an earlier code head and does not prove F.
- Scope: validate checksums for the five desktop JVM runtime classifiers: `linux-arm64`, `linux-x64`, `macos-x64`, `macos-arm64`, and `windows-x64`.
- Host: vm6, the existing receipt writer, with Linux, JDK 21.0.12.1+1, and the `ui`, `jvm-tests`, and `gate` tags.
- Claim: vm6 adds a new claim naming this gate before starting. Use one gate at a time under `/workspace/tools/.gradle-vm6.lock`.
- Worktree: clean and detached at F. Set `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1` and `GRADLE_USER_HOME=/workspace/gradle-home`.
- Runner and logs: use the vm6 Gradle runner; record its exact invocation in the claim. Save this run under `/workspace/gates/logs/vm6/solr-17861-platform-classifiers/` without reusing old logs.
- Seed: N/A; `:desktopApp:test` has no sources.

## Legs

1. Fetch the fork branch, check out F detached, and verify the worktree is clean and `HEAD` equals F.
2. Run the exact-head formatting preflight: `./gradlew --no-daemon tidy`, then `./gradlew --no-daemon -p ui :desktopApp:spotlessCheck --console=plain`. If formatting changes files, record the repair and stop; a repaired head needs a new gate.
3. Before regenerating anything, validate the committed checksums: `./gradlew --no-daemon -p ui :desktopApp:validateLicenses --console=plain`.
4. Run the platform matrix and full desktop check: `./gradlew --no-daemon -p ui :desktopApp:updateLicenses :desktopApp:validateLicenses :desktopApp:spotlessCheck :desktopApp:check --console=plain`.
5. Confirm regeneration leaves `git status --short` empty and `git diff --exit-code -- ui/desktopApp/build.gradle.kts ui/licenses` succeeds. Confirm there are 105 checksum files and the five Skiko runtime checksum files for the classifiers above. Record that `:desktopApp:test` is `NO-SOURCE`.

PASS requires every leg to complete at F, the checked-in checksums to pass before regeneration, regeneration to leave the tree unchanged, and all five target classifier checksums to be present. The old receipt remains the evidence for its earlier head until vm6 records this result.
