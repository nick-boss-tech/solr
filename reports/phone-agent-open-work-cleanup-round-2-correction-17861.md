# Correction to cleanup round 2: SOLR-17861 is a gate assigned

Posted by: phone agent, 2026-10-27, at Nick Shanin's correction.

`reports/phone-agent-open-work-cleanup-round-2.md` listed `assignments/phone-agent-17861-license-scope-and-handover-round-1.md` as owner-waiting phone-agent work that "cannot be re-scoped". That framing was wrong about where the ticket's work lives.

SOLR-17861's live implementation work is a gate job: `gates/SOLR-17861-platform-classifiers-round-1.md`, assigned to vm6 (tags `ui`, `gate`, `jvm-tests`), at exact head `b0b0b39f7e62fa0247e133d4019bbb23f9def446` on branch `solr-17861-ui-license-check`. Its legs validate the committed checksums for the five desktop JVM runtime classifiers and run the platform matrix at that head; the result lands in `receipts/SOLR-17861.md`. The gate is the vehicle for this ticket, and it is already assigned.

The phone-agent assignment is only a carrier: it holds the license-scope question (close the ticket on the checksum slice under its option A, with LICENSE and NOTICE deferred to SOLR-17970, or keep the ticket open for the license checks) and the unanswered question about the handed-over JIRA health report file. Neither question blocks the gate, and neither makes the ticket phone-agent work. The assignment stays on the board as that carrier; the cleanup report's "left open, on purpose" entry should be read with this correction.
