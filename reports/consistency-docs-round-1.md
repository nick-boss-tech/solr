# Consistency and documentation accuracy round 1

Assignment: `assignments/pool-consistency-docs-round-1.md`. Updated by phone-agent-2, 2026-10-23T07:15:00Z, to record the item 4 takeover.

| Item | State | Branch | Head | Deliverable |
| --- | --- | --- | --- | --- |
| 1. V2 core backup create permission | CLAIMED by vm3 | `consistency-v2-backup-create-permission` (vm3) | not known to vm4 | vm3 claim |
| 2. V2 list collection backups permission | DONE by vm4 | `consistency-v2-list-backups-permission` | e3f35fe42c | `drafts/consistency-docs-round-1-item2.md`; compile log `/workspace/gates/logs/vm4/consistency-item2-compile.log` |
| 3. Configset file delete containment | DONE by vm4 | `hardening-configset-delete-containment` | 3905ad78e8 | `drafts/consistency-docs-round-1-item3.md`; test logs `/workspace/gates/logs/vm4/configset-item3-test.log` (3 of 3 pass) and `configset-item3-failbefore.log` (base fails the new test); spotless `configset-item3-spotless.log` |
| 4. Streaming docs allow-list note | CLAIMED by phone-agent-2; retrying the docs build after vm4's earlier block | `docs-streaming-allowlist-note` | 4b185504ef | `drafts/consistency-docs-round-1-item4.md`; original vm4 logs `/workspace/gates/logs/vm4/docs-item4-site.log`, `docs-item4-site-2.log` |

Item 2 gate: compile only. No test in the tree asserts the declared permission, so no focused test was run.

Item 3 gate: focused test class run and a fail-before run on the base production file, with spotless. No PR opened.

Item 4 gate: `buildLocalAntoraSite` fails on a missing `@asciidoctor/tabs` module in the ref guide's Node setup; `checkSiteLinks` did not run. The text is drafted and the branch is pushed, but the docs check has not passed.
