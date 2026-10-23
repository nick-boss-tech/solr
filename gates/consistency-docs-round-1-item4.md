# Gate job: consistency and documentation accuracy round 1, item 4
Status: DONE

- Assignment: `assignments/pool-consistency-docs-round-1.md`, item 4.
- Branch: `docs-streaming-allowlist-note`.
- Head: `4b185504ef7dd5e4530664995719a0e743564382`, confirmed live on 2026-10-23.
- Claimant host: phone-agent-2, resuming vm4's stale environment-blocked build.
- Capability tags: implementation, docs, npm, gate.
- Module: `:solr:solr-ref-guide`. Focused Java tests and seed: not applicable, docs-only.
- Diff scope: the existing four added lines in `configuration-guide/pages/configuring-solr-xml.adoc` and `query-guide/pages/stream-source-reference.adoc`. No source or build-file change authorized or planned.
- Prior completed work reused: existing documentation commit and draft at `drafts/consistency-docs-round-1-item4.md`.
- Runner: `/workspace/tools/phone-agent-2-consistency-item4-gate.py`.
- Checkout: `/workspace/solr-consistency-item4-antora`, standalone Git checkout at the exact head.
- Logs: `/workspace/phone-agent-2-logs/consistency-item4`.
- JDK: `/usr/lib/jvm/java-21-openjdk-amd64`. Gradle home: `/home/ubuntu/.gradle`.
- Serialization: nonblocking host Gradle lock and phone-agent-2 lock. Disk and inode guards before each Gradle invocation.

## Decision and environment

Decision ladder rung 1: the pinned `solr/solr-ref-guide/build.gradle` installs `@asciidoctor/tabs` with npm `-D` (lines 266 to 271). Run with `NODE_ENV` unset and `npm_config_include=dev` so the declared development dependency is actually installed. This is build-environment setup, not a product repair. A standalone checkout avoids the previously documented local Antora refusal of linked Git worktrees. No product commit or PR.

## Checklist

- [x] Confirm fork head and clean tracked tree.
- [x] Reuse the already committed note text and draft.
- [x] Build `:solr:solr-ref-guide:buildLocalAntoraSite`.
- [x] Verify both rendered notes and their cross-reference destinations, including `#allow-urls`.
- [x] Run `:solr:solr-ref-guide:checkSiteLinks`.
- [x] Write the final receipt, update draft and summary, and mark the item and claim terminal.

## Limits

Keep the source head unchanged. No PR, comment, Jira or email. Do not force push. Gate and receipt distinguish a successful Antora build from the full site link check.

## Result

GREEN at `4b185504ef7dd5e4530664995719a0e743564382`, 2026-10-23T07:21:40.292402+00:00. Antora exit 0; both rendered notes and xrefs verified; Gradle checkSiteLinks exit 0 with an output-handler caveat. Direct pinned checker exit 0, 835 files parsed, zero errors and zero warnings. The Gradle TeeOutputStream NPE and empty report are recorded, not treated as link proof. Receipt: `receipts/consistency-docs-round-1-item4.md`. Evidence: `material/consistency-docs-round-1/item4-4b185504ef-docs-evidence.tar.gz`. No tracked source change or PR.
