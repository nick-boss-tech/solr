# Jira comment draft: SOLR-17861 closing note

Status: DRAFT (not posted). Shape: status note with one question. Addressee: Christos Malliaridis. Prepared by the phone agent, 2026-10-24, as item 1 of `assignments/jira-draft-priority-round-1.md`; the scope was settled in `reports/decision-ladder-pass-2026-10-24.md` (option A). Posting needs Nick Shanin's explicit go.

---

Status note on the license scope for the new UI module.

I built the first slice of what this ticket asks for: a checksum check over the desktop JVM classpath on linux_x64, wired into {{./gradlew -p ui check}}, on a branch: [solr-17861-ui-license-check|https://github.com/nick-boss-tech/solr/tree/solr-17861-ui-license-check]. That is the checksum side of what the ticket asks {{updateLicenses}} and the license checks to cover for the KMP module.

For the other half, per-jar LICENSE and NOTICE presence: the repo rule ({{gradle/validation/jar-checks.gradle}}) requires those files for jars shipped in the binary distribution, and the {{ui}} project does not ship jars in the distribution. That question belongs with the desktop distribution work in SOLR-17970, where what ships gets decided.

Christos, shall I open the checksum slice as a PR for this ticket, and are you content for the LICENSE and NOTICE presence question to move to SOLR-17970?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
