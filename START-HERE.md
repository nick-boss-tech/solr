# Start here: coordination v2

The owner-directed cutover is effective when [the cutover announcement](https://github.com/nick-boss-tech/solr/blob/coordination-ui/reports/coordination-v2-cutover-2026-11-06.md) exists on `coordination-ui`. After that, `coordination-v2` is the live queue and `coordination-ui` is historical. If the announcement is absent, continue using `coordination-ui` and do not claim real work here.

This branch is a fresh Git-backed work board. It begins with no inherited work. Consult the [legacy catalog](migration/legacy-work-catalog.md) and current `coordination-ui` tip when selectively bringing work forward. Read [WORKFLOW.md](WORKFLOW.md), then [the tool guide](tools/README.md). The local workspace's instructions and the user's actual authorization govern host restrictions and external actions.

For a new task: inspect the current board snapshot and related old records; create a narrowly scoped assignment with a stable lowercase job ID; claim it with the same ID before working; report the exact sources, results and limitations. Only a Linux gate host may perform Solr builds or tests when the workspace prohibits them on Windows. Gate work is a separate assignment with its own job ID and claim. A receipt covers only its recorded head and completed proof legs.

For a gate handoff, link the assignment and the exact gate record. Give the job ID, canonical claim path, source repository, branch, full head, expected receipt path, and board snapshot SHA. A gate ID can differ from its job ID. See [how to find and hand off a gate](WORKFLOW.md#find-and-hand-off-a-gate).

For PR implementation, review and descriptions, apply the [test adequacy and minimalism supplement](docs/pr-test-adequacy.md) alongside the historical PR formula and current workspace instructions.

Use the [coordination skill](.agents/skills/solr-coordination/SKILL.md) when available. It does not install itself or replace the project's review, proof or contribution workflows. A generated index is a navigation aid, not proof of ownership, liveness or completion.
