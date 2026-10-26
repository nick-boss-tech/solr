# Claim: PR 5188 current API names and regression gaps, Windows round 3

Claimant: windows (Codex desktop)
Started: 2026-10-26T19:16:00.540Z
Tags: implementation, review, records
Authority: Nick explicitly authorized the naming changes and review/addition of missed regression coverage in this chat.
Board snapshot: d31ce26715d398be153dca339a84c0ce4b43c61c.
Fork branch/head: pr-5188-glob-field-loading-followup at 409183aa9be8c8500942eadf22d036abaf1ead5c.
Live upstream PR head: db867d80365510790bb6a04ce33105cd196cf34c (renames), based on e07455a0f953e702da27f677c51ea894b7bafc3a.

Scope: merge the upstream rename commit without rewriting the previously proved history; adapt our added calls to loadsField/loadsFieldExplicitly/returnsAllFields; add focused coverage for unmatched glob selection, alias-output collision/shared-cache reuse, and normal/shard prefetch at 50/51 results. Preserve the original 24-row positive snippet checks and justified fixture repairs. No unrelated production behavior changes and no Gradle on Windows.
Previous gate claim claims/pr-5188-glob-field-loading-round-2-vm6.md has a terminal result and verified receipt at the old exact head; no active gate head is moved.
Decision ladder: rung 1, current ReturnFields API and unchanged QueryComponent.doPrefetch guard establish the implementation/compatibility scope; rung 2 executable proof remains on Linux with base db867d8 and the final test overlay.

Deliverables:
- Normal fork push, current upstream is an ancestor, remote full SHA read back.
- reports/pr-5188-glob-field-loading-implementation-round-3.md
- gates/pr-5188-glob-field-loading-round-3.md

New head remains Implemented, Proof Pending until fresh Linux proof. The old receipt remains valid for its own revision. No public comment, PR or JIRA write is authorized.
