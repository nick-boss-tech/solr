# Jira comment draft: SOLR-17861 platform harmonization status

Status: DRAFT (not posted). Shape: status note with one question. Addressee: Christos Malliaridis, replying to his comment 18133004 (2026-10-27) on the operating-system coverage of the checksum checks. Prepared by the phone agent, 2026-10-27, under `assignments/SOLR-17861-windows-linux-comment-harmonization-round-1.md`. Evidence: Windows run recorded in that assignment; Linux gate PASS at the same head in `receipts/SOLR-17861.md`. Posting needs Nick Shanin's explicit go.

---

[~malliaridis], an update on the operating-system coverage you raised.

The branch [solr-17861-ui-license-check|https://github.com/nick-boss-tech/solr/tree/solr-17861-ui-license-check] now carries checksums for all five desktop runtime classifiers, and the checks have run on two host operating systems at the same commit, {{b0b0b39f7e62fa0247e133d4019bbb23f9def446}}.

On 2026-10-27, I ran the checksum checks on Windows 11 x64 with Gradle 9.7.0 and Eclipse Adoptium JDK 21.0.11 at commit {{b0b0b39f7e62fa0247e133d4019bbb23f9def446}}. First, {{.\gradlew.bat -p ui :desktopApp:validateLicenses --console=plain}} passed against the committed files before regeneration. I then ran {{.\gradlew.bat -p ui :desktopApp:updateLicenses :desktopApp:validateLicenses :desktopApp:spotlessCheck :desktopApp:check --console=plain}}; all tasks passed. The generated set contains 105 checksum files, including Skiko runtime checksums for Linux x64 and ARM64, macOS x64 and ARM64, and Windows x64. Regeneration left the checksum contents unchanged. {{:desktopApp:test}} was {{NO-SOURCE}}.

On Linux x86_64 with JDK 21 and Gradle 9.7.0, at the same commit, {{./gradlew -p ui :desktopApp:validateLicenses --console=plain}} passed against the committed files before regeneration, and the full run ({{updateLicenses}}, {{validateLicenses}}, {{spotlessCheck}}, {{check}}) passed, with regeneration leaving the worktree unchanged. The same 105 checksum files, including all five Skiko runtime classifiers, are present, and {{:desktopApp:test}} was {{NO-SOURCE}} there as well.

No macOS host run has been done. The macOS checksums are generated and validated as part of the set on both hosts above, but the checks have not executed on macOS itself. Do the Windows and Linux host results, with all five target classifiers checked, address the concern, or is a separate macOS host run still wanted?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
