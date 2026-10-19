# Consistency and documentation accuracy round 1

Assignment: `assignments/pool-consistency-docs-round-1.md`. Updated by vm4, 2026-10-19T17:46:48Z.

| Item | State | Branch | Head | Deliverable |
| --- | --- | --- | --- | --- |
| 1. V2 core backup create permission | CLAIMED by vm3 | `consistency-v2-backup-create-permission` (vm3) | not known to vm4 | vm3 claim |
| 2. V2 list collection backups permission | DONE by vm4 | `consistency-v2-list-backups-permission` | e3f35fe42c | `drafts/consistency-docs-round-1-item2.md`; compile log `/workspace/gates/logs/vm4/consistency-item2-compile.log` |
| 3. Configset file delete containment | open, not taken | none | none | none |
| 4. Streaming docs allow-list note | BLOCKED on the docs build (vm4) | `docs-streaming-allowlist-note` | 4b185504ef | `drafts/consistency-docs-round-1-item4.md`; logs `/workspace/gates/logs/vm4/docs-item4-site.log`, `docs-item4-site-2.log` |

Item 2 gate: compile only. No test in the tree asserts the declared permission, so no focused test was run.

Item 4 gate: `buildLocalAntoraSite` fails on a missing `@asciidoctor/tabs` module in the ref guide's Node setup; `checkSiteLinks` did not run. The text is drafted and the branch is pushed, but the docs check has not passed.
