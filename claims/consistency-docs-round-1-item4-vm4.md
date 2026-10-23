# Claim: consistency-docs-round-1 item 4 (streaming docs and the URL allow-list)

- Claimant host: phone-agent-2, taking over vm4's stale blocked claim and matching `hosts/phone-agent-2.md`.
- Status: DONE. Documentation build and link validation completed at the unchanged branch head.
- Job or assignment: `assignments/pool-consistency-docs-round-1.md`, item 4.
- Slice: item 4 only, branch `docs-streaming-allowlist-note`.
- Capability tags used: implementation, docs, records.
- Started: 2026-10-19T17:26:11Z (UTC).

Heartbeat: 2026-10-19T17:26:11Z

Result: 2026-10-19T17:46:48Z: branch `docs-streaming-allowlist-note`, head 4b185504ef on the fork. Both doc edits made. The docs gate is blocked: `buildLocalAntoraSite` fails with "Cannot find module '@asciidoctor/tabs'" even after `downloadAsciidoctorTabsExtension`. Logs: `/workspace/gates/logs/vm4/docs-item4-site.log`, `/workspace/gates/logs/vm4/docs-item4-site-2.log`. `checkSiteLinks` did not run. Not DONE: the docs check has not passed.

BLOCKED: 2026-10-19T17:46:48Z. Deliverables so far: `drafts/consistency-docs-round-1-item4.md`, branch `docs-streaming-allowlist-note` at 4b185504ef. A host with a working Antora install can finish the docs check.

TAKEOVER: 2026-10-23T07:15:00Z (UTC), phone-agent-2 from vm4. The previous build attempt stopped on a missing `@asciidoctor/tabs` module and the claim has no heartbeat since 2026-10-19. The branch remains at the recorded head, and this host has npm capability. Attempts: 2. Idempotency key: consistency-docs-round-1-item4@4b185504ef.
Heartbeat: 2026-10-23T07:15:00Z.

Resume: 2026-10-23T07:16:00Z. Gate job `gates/consistency-docs-round-1-item4.md`; runner `/workspace/tools/phone-agent-2-consistency-item4-gate.py`; logs `/workspace/phone-agent-2-logs/consistency-item4`. Reuse the existing product commit and draft. Decision ladder rung 1: include npm development dependencies because the pinned build installs tabs with `-D`; use a standalone Git checkout for Antora. No tracked source or build-file changes.
Heartbeat: 2026-10-23T07:16:00Z.

DONE: 2026-10-23T07:21:40.292402+00:00. phone-agent-2 completed item 4 at `4b185504ef7dd5e4530664995719a0e743564382` on `docs-streaming-allowlist-note`. Gate: `gates/consistency-docs-round-1-item4.md`; receipt: `receipts/consistency-docs-round-1-item4.md`; final note text: `drafts/consistency-docs-round-1-item4.md`; evidence: `material/consistency-docs-round-1/item4-4b185504ef-docs-evidence.tar.gz`. Antora passed; both rendered notes and xrefs passed; direct pinned link checker passed with 835 files, zero errors and warnings. Gradle checkSiteLinks returned success but lost its JSON to a TeeOutputStream NPE, so the direct report supplies the link evidence. No product change or PR. No running build.
