# Source and evidence entrypoints

These are search entrypoints from the Solr developer-doc index, not verified architecture chapters. Resolve them at the task's actual Solr revision; paths can change.

| Question | Start with |
| --- | --- |
| Distributed updates | `dev-docs/distributed-update-internals.adoc`, then the update entrypoint and neighboring tests |
| Shard split | `dev-docs/shard-split/shard-split.adoc` |
| Overseer and cluster state | `dev-docs/overseer/overseer.adoc` |
| APIs and JAX-RS migration | `dev-docs/apis.adoc`, `dev-docs/apis-v2-conventions.adoc` |
| Plugins and packages | `dev-docs/plugins-modules-packages.adoc` |
| New Admin UI | `dev-docs/ui/`, root `ui/`; inspect its actual build definition |
| Documentation and upgrades | `dev-docs/ref-guide/`, `solr/solr-ref-guide/` |
| Test infrastructure | Applicable `AGENTS.md`, `dev-docs/gradle-help/tests.txt`, existing tests near the changed behavior |
| Security boundary | `THREAT_MODEL.md`, `SECURITY.md` at the assessed revision |

In Nick's factory, the live fork's `coordination-ui` branch holds assignments, claims, gates and receipts. Fetch once, pin its full SHA, then read the relevant records from that snapshot. Local `research/pr-adjacency/` and `research/branch-reviews/` contain scan and review history; these may not be present on other hosts. Ask for or locate the relevant artifact if absent; never imply it was read.

Useful context-brief fields: requested behavior, Solr revision, source entrypoint, invariant, callers, tests/fixtures, known PRs/tickets, evidence revision, confidence and remaining gap. A match on module or PR title alone is a search lead.
