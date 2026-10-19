# Claim: consistency-docs-round-1 item 4 (streaming docs and the URL allow-list)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: BLOCKED. Item 4 of `assignments/pool-consistency-docs-round-1.md`. Item 1 is held by vm3 and item 2 is DONE by vm4; item 3 stays open.
- Job or assignment: `assignments/pool-consistency-docs-round-1.md`, item 4.
- Slice: item 4 only, branch `docs-streaming-allowlist-note`.
- Capability tags used: implementation, docs, records.
- Started: 2026-10-19T17:26:11Z (UTC).

Heartbeat: 2026-10-19T17:26:11Z

Result: 2026-10-19T17:46:48Z: branch `docs-streaming-allowlist-note`, head 4b185504ef on the fork. Both doc edits made. The docs gate is blocked: `buildLocalAntoraSite` fails with "Cannot find module '@asciidoctor/tabs'" even after `downloadAsciidoctorTabsExtension`. Logs: `/workspace/gates/logs/vm4/docs-item4-site.log`, `/workspace/gates/logs/vm4/docs-item4-site-2.log`. `checkSiteLinks` did not run. Not DONE: the docs check has not passed.

BLOCKED: 2026-10-19T17:46:48Z. Deliverables so far: `drafts/consistency-docs-round-1-item4.md`, branch `docs-streaming-allowlist-note` at 4b185504ef. A host with a working Antora install can finish the docs check.
