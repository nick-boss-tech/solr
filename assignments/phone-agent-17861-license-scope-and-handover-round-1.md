# Assignment: SOLR-17861 license scope (decision for Nick) and a handed-over file

Status: OPEN. Posted by vm6, 2026-10-24, at Nick Shanin's direction. Requested by Nick; for the phone agent to carry.

## 1. SOLR-17861: is LICENSE and NOTICE presence part of the first slice?

vm6 built the checksum half on `solr-17861-ui-license-check` (commits `022acd4ec9` and `92aed103a0`). The gate passed on the committed tree, and the round report is `reports/pool-ui-SOLR-17861-round-1.md`. The open question is whether per-jar LICENSE and NOTICE presence belongs in this ticket. vm6 is not qualified to choose license types for 101 jars, so the question is posed here with the ladder run and a recommendation.

### Decision ladder (`material/decision-ladder.md`)

- Rung 1, code and history. The root rule requires LICENSE and NOTICE only for jars shipped in the binary distribution. `gradle/validation/jar-checks.gradle` lines 66 and 109-110 define `isDistShippingProject` (`:solr:server`, `:solr:modules:*`, `:solr:cross-dc-manager`), and `validateJarLicenses` at lines 263-271 checks only those. `ui` is not in that list. `updateLicenses` (lines 343-350) writes SHA-1 files only.
- Rung 2, probe: not needed; rung 1 settles the repo rule.
- Rung 3, intent. The ticket asks for `updateLicenses` and "the license checks" to cover the KMP module, output independent of host OS (`reports/new-ui-platform-build-round-1.md`, SOLR-17861 (a)). The checksum slice meets the `updateLicenses` half. The "license checks" half is open, so the ticket's own wording does not settle it.
- Rung 4, defensible options:
  - A. Close the checksum slice now. Defer LICENSE and NOTICE to the desktop distribution work, SOLR-17970. Evidence: no UI distribution exists yet; SOLR-17970 is NEEDS INFRA (no Windows or macOS build host, no nightlies job; `reports/new-ui-platform-build-round-1.md`, SOLR-17970 (c) and (d)).
  - B. Add LICENSE and NOTICE files for all 101 jars now. Cost: license texts and type decisions for each jar, which a committer must review.
  - C. Add a manifest of each jar's license name from its POM, with no license texts, and check its presence. No legal text is written, but the license names still need a reviewer.
- Recommendation: option A. Rung 1 gives the repo's own scope rule, and nothing in the UI build ships yet. Option B adds attribution text that the pool cannot verify, so it should wait for SOLR-17970's distribution work and a committer review.

### Owner question (one)

Should SOLR-17861 close on the checksum slice under option A, with LICENSE and NOTICE moved to SOLR-17970? Or should it stay open for the license checks until a UI distribution exists? Closing the ticket is a JIRA post, so it needs Nick's go.

Do not post anything to Christos or JIRA on this without Nick's go.

## 2. File handed over: Solr JIRA open-issue health report

- Path on the board: `assignments/attachments/report-1792861769937.html`. It is a byte-for-byte copy of `/workspace/uploads/report-1792861769937.html` (18,996 bytes), handed over by Nick with no instructions.
- Title: "Apache Solr JIRA — Open Issue Health Report". It states 4,093 open issues as of May 2026. Those figures are the author's; vm6 has not checked them against the JIRA REST API and has taken no action on the report.
- Opening the file loads Chart.js from cdnjs and fonts from Google Fonts. Nothing else is external.
- Ask: confirm with Nick what the report is for. No further action until he answers.

## Deliverable

- The phone agent's reply to Nick on item 1, with the question above, and the reason it is needed.
- Item 2: Nick's answer on the purpose of the report, or a note that he has not yet said.
