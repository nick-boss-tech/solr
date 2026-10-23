# Consistency and documentation accuracy round 1

Assignment: `assignments/pool-consistency-docs-round-1.md`. Updated by phone-agent-2, 2026-10-23T07:21:40.292402+00:00, to close item 4 after a successful unchanged-head documentation build and link check. The initial takeover commit has an incorrect SOLR-18552 subject; its changes were limited to coordination records and did not touch a product branch or SOLR-18552 work.

| Item | State | Branch | Head | Deliverable |
| --- | --- | --- | --- | --- |
| 1. V2 core backup create permission | CLAIMED by vm3 | `consistency-v2-backup-create-permission` (vm3) | not known to vm4 | vm3 claim |
| 2. V2 list collection backups permission | DONE by vm4 | `consistency-v2-list-backups-permission` | e3f35fe42c | `drafts/consistency-docs-round-1-item2.md`; compile log `/workspace/gates/logs/vm4/consistency-item2-compile.log` |
| 3. Configset file delete containment | DONE by vm4 | `hardening-configset-delete-containment` | 3905ad78e8 | `drafts/consistency-docs-round-1-item3.md`; test logs `/workspace/gates/logs/vm4/configset-item3-test.log` (3 of 3 pass) and `configset-item3-failbefore.log` (base fails the new test); spotless `configset-item3-spotless.log` |
| 4. Streaming docs allow-list note | DONE by phone-agent-2; docs build and direct link validation GREEN | `docs-streaming-allowlist-note` | 4b185504ef | `drafts/consistency-docs-round-1-item4.md`; `gates/consistency-docs-round-1-item4.md`; `receipts/consistency-docs-round-1-item4.md` |

Item 2 gate: compile only. No test in the tree asserts the declared permission, so no focused test was run.

Item 3 gate: focused test class run and a fail-before run on the base production file, with spotless. No PR opened.

Item 4 gate: GREEN at unchanged head `4b185504ef7dd5e4530664995719a0e743564382`. Antora passed after including the declared npm development dependency. Both rendered notes and xrefs passed. Gradle checkSiteLinks returned exit 0 but its output handler failed and left an empty JSON report. Direct pinned `link-checker@1.4.2` passed against the generated site: 835 files, zero errors and warnings, external links disabled. Receipt and archived evidence record that distinction. No tracked source repair or PR.
