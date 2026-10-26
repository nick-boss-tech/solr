# Jira comment draft: SOLR-17861 closing note, round 2

Status: DRAFT (not posted). Supersedes `jira/jira-comment-SOLR-17861-closing-draft.md` at Nick Shanin's direction on 2026-10-26: Christos is now tagged with a real Jira mention, and the words branch and diff carry URLs. Posting needs Nick Shanin's explicit go.

Known caveat, verified 2026-10-26: the branch still carries the factory working file `pr-draft/SOLR-17861.md` (+28), which the diff link below will show. Remove it from `solr-17861-ui-license-check` before posting, as was done for SOLR-17988, so the diff is exactly the slice (tip verified `92aed103a06cd0c994d0b463de1a7dc43a06589e`; content: `ui/desktopApp/build.gradle.kts` +69 and the `ui/licenses/*.sha1` checksum files).

---

Status note on the license scope for the new UI module.

I built the first slice of what this ticket asks for: a checksum check over the desktop JVM classpath on linux_x64, wired into {{./gradlew -p ui check}}. It is on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17861-ui-license-check], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17861-ui-license-check] is the {{desktopApp}} build wiring plus the per-jar checksum files under {{ui/licenses}}. That is the checksum side of what the ticket asks {{updateLicenses}} and the license checks to cover for the KMP module.

For the other half, per-jar LICENSE and NOTICE presence: the repo rule ({{gradle/validation/jar-checks.gradle}}) requires those files for jars shipped in the binary distribution, and the {{ui}} project does not ship jars in the distribution. That question belongs with the desktop distribution work in SOLR-17970, where what ships gets decided.

[~malliaridis] shall I open the checksum slice as a PR for this ticket, and are you content for the LICENSE and NOTICE presence question to move to SOLR-17970?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
