# Claim: pool-ui-SOLR-17909

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, one ticket per claim. Ticket type: component (differs from the screen ticket SOLR-17849 claimed just before, owner complaint 5).
- Slice: SOLR-17909 (breadcrumb component), first slice only: a reusable composable over a list of entries, with a preview and a test. Branch `solr-17909-breadcrumb` from base `c8ab71430f`.
- Rewritten criteria (the ticket's criteria mention the Decompose childstack; the base commit `c8ab71430f` removed Decompose from the shared module, so those criteria are stale):
  - The breadcrumb takes a list of entries (label and an id) and a callback for the selected entry. It is a pure composable with no navigation dependency.
  - Clicking an entry other than the last calls the callback with that entry's index, so the caller can pop back to it.
  - The last entry is highlighted and inert; clicking it does nothing.
  - The row wraps when the width is not enough.
  - Not in this slice: the back-stack hookup on the Collections section (a child ticket under SOLR-17886), and the Decompose-free stack integration.
- Capability tags used: implementation, ui, jvm-tests.
- Started: 2026-10-24T08:32:53Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17909@c8ab71430f.
- Links: Ticket: SOLR-17909 (parent SOLR-17886). Category: ui. Siblings: `claims/ui-volunteer-drafts-round-1-SOLR-17909.md` (staged ask, DONE, not posted), `claims/pool-ui-SOLR-17849.md` (DONE). Supersedes: none.
- Decision ladder: rung 1 for the base and the code. Rung 3 for the rewritten criteria: the ticket's stated intent (a reusable breadcrumb over the layers) is kept; only the stack source is changed, because the stack was removed. GL: the breadcrumb uses text and rows only; no Canvas, DrawScope or graphicsLayer.
- Constraints: no JIRA or GitHub post, no PR. Commit as Nick Shanin with no trailers. Gate before the branch is pushed.

Heartbeat: 2026-10-24T08:32:53Z

Run: job `pool-ui-17909-17c6860195-083805`, log dir `/workspace/gates/logs/vm6/pool-ui-17909/`, head `17c6860195` (local commit on branch solr-17909-breadcrumb, not pushed until the gate passes), started 2026-10-24T08:38:29Z (UTC). Steps: spotless, then `:shared:jvmTest --tests org.apache.solr.ui.views.components.SolrBreadcrumbTest`. Files: `SolrBreadcrumb.kt` (commonMain, FlowRow, earlier entries clickable, last entry bold and inert), `SolrBreadcrumbTest.kt` (commonTest; the wrapping test is a pin, labelled as such in the test name), and `PreviewBreadcrumb.kt` (jvmMain preview).

Heartbeat: 2026-10-24T08:38:29Z

Failed run: job `pool-ui-17909-17c6860195-083805`, head `17c6860195`. spotless rc=0. jvmtest rc=1 at `:shared:compileTestKotlinJvm`: `SolrBreadcrumbTest.kt:23` unresolved import `androidx.compose.ui.test.assertExists`. Cause: `assertExists()` is a member of the node interaction and needs no import (FileContentViewerTest calls it the same way). Patch: remove that import; local commit amended (unpushed) to head `77a38638b0`.

Rerun (one, new head): job `pool-ui-17909-77a38638b0-084441`, log dir `/workspace/gates/logs/vm6/pool-ui-17909/`, started 2026-10-24T08:44:52Z (UTC).

Heartbeat: 2026-10-24T08:44:52Z
