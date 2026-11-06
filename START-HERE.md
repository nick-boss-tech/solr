# Start here: coordination v2

**PREVIEW: do not use as the live queue until the coordinated cutover is recorded.** Until then, `coordination-ui` is the sole live board.

This branch is a fresh Git-backed work board. It begins with no inherited work. Consult the [legacy catalog](migration/legacy-work-catalog.md) and current `coordination-ui` tip when selectively bringing work forward. Read [WORKFLOW.md](WORKFLOW.md), then [the tool guide](tools/README.md). The local workspace's instructions and the user's actual authorization govern host restrictions and external actions.

For a new task: inspect the current board snapshot and related old records; create a narrowly scoped assignment with a stable lowercase job ID; claim it with the same ID before working; report the exact sources, results and limitations. Only a Linux gate host may perform Solr builds or tests when the workspace prohibits them on Windows. Gate work is a separate assignment with its own job ID and claim. A receipt covers only its recorded head and completed proof legs.

Use the [coordination skill](.agents/skills/solr-coordination/SKILL.md) when available. It does not install itself or replace the project's review, proof or contribution workflows. A generated index is a navigation aid, not proof of ownership, liveness or completion.
