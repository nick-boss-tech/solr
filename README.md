# Coordination v2: migration preview

**DRAFT: not the active board.** Continue to claim, execute and publish work on `coordination-ui`. No existing claim, gate, assignment, receipt, report or host record has been migrated into this preview. Do not use this branch as an empty work queue.

This preview establishes a clean starting tree and a coordination skill for review. The source snapshot is `ed07cb930649addb4799e8e6c9de5bedc52ac406`; the source branch continues to move. Its history and all records remain accessible at `coordination-ui` and at that pinned commit.

Read [the coordination skill](.agents/skills/solr-coordination/SKILL.md) and [the migration plan](MIGRATION.md). Existing Solr skills are a separate package at `factory-solr-skills-round-1`, commit `b30a0a77df5f3ae10191c814256245319f56cdd7`; its behavioral review and installation remain pending. This preview does not replace or install that package.

For a reusable, project-neutral explanation of the Git-branch board and its relationship to the Elephant-Goldfish Model, read [the portable design](docs/portable-board.md). It is a proposal and example, not an installed or independently reviewed implementation.

This is a proposed foundation, not an authorization or instruction to switch agents. A later migration must inventory and reconcile live records, preserve provenance, make tooling work for the new destination, and verify a coordinated cutover. Until then the current board's rules govern.
