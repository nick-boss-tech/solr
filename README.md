# Coordination v2

The owner-directed switch takes effect when [the cutover announcement](https://github.com/nick-boss-tech/solr/blob/coordination-ui/reports/coordination-v2-cutover-2026-11-06.md) is present on `coordination-ui`. Until that record exists, continue using the old board. After it exists, use `coordination-v2` as the live board. No existing claim, gate, assignment, receipt, report or host record has been migrated automatically.

This branch starts a fresh queue; old work may be considered one item at a time. The old branch's history and records remain accessible at `coordination-ui` and in the pinned [legacy catalog](migration/legacy-work-catalog.md). Reconcile later writes there before carrying work forward.

Read [the coordination skill](.agents/skills/solr-coordination/SKILL.md) and [the migration plan](MIGRATION.md). Existing Solr skills are a separate package at `factory-solr-skills-round-1`, commit `b30a0a77df5f3ae10191c814256245319f56cdd7`; its behavioral review and installation remain pending. This preview does not replace or install that package.

For a reusable, project-neutral explanation of the Git-branch board and its relationship to the Elephant-Goldfish Model, read [the portable design](docs/portable-board.md). It is a proposal and example, not an installed or independently reviewed implementation.

Isolated experimental writers and inventory tools are in [tools](tools/README.md). The structured-record index does not index the legacy board; the separate inventory can read a pinned legacy snapshot but does not migrate it. Manual releases require verified stop evidence; automatic takeover is not provided.

The [switchover readiness assessment](migration/readiness.md) inventories a pinned legacy snapshot and records concrete compatibility and claim-safety blockers. It does not declare the new board ready.

Read [START-HERE.md](START-HERE.md) and [WORKFLOW.md](WORKFLOW.md) before taking work. Host operators must update and validate their own bootstraps and writers; the cutover announcement alone cannot prevent a stale client from writing to the old branch.
