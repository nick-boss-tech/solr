# Claim: reconcile the three PR 5110 findings and add deprecation follow-up

Started: 2026-11-05 17:19:49 UTC. Host: Windows Codex desktop. Tags: review, draft, records.

Direct owner instruction: briefly review the current PR against Nick's three quoted P2 findings and add the legacy node/nodes deprecation follow-up to the assignment. Board snapshot 50ef548e1fb1a7ce5488bd98f71cb6bcf8523511; original package draft claim is complete, and no competing claim for this new slice exists at that snapshot.

Live PR head: 6b5d830132b941faa85856347099ade8b190be0e. New inline comment from David confirms Eric's understanding: https://github.com/apache/solr/pull/5110#discussion_r4483637626 . Source reads stay pinned to the fetched head. This is a targeted source review, not a full PR approval or runtime verification.

Decision ladder rung 3: direct owner direction and the maintainer's consumer-first migration/deprecation direction settle the record update. Add a new assignment addendum rather than editing immutable assignments. Do not broaden SOLR-18619's prepared read migration or silently implement the deprecation.

Deliverables: reports/solr-12613-three-findings-reconciliation-round-2.md and assignments/solr-followup-deprecation-pr-package-round-3.md. Preserve the six existing complete draft packages; distinguish the newly planned seventh candidate from implemented/proven source. No code edits, source branch changes, Gradle, tests, public PR/Jira comments or PR opening.
