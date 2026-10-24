# Claim: pool-ui-SOLR-17911

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, one ticket per claim. Owner answer A (Nick Shanin, 2026-10-24) granted vm6 the `ui` and `jvm-tests` tags; the host file is updated in this commit.
- Slice: SOLR-17911 (file content viewer). The component and its test already exist on fork branch `solr-17911-file-content-viewer` at `8047b99ef0`. This claim is the gate: verify the branch's tests and spotless on a fresh checkout of that commit, and record the result. No code change.
- Capability tags used: ui, jvm-tests.
- Started: 2026-10-24T07:50:26Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17911@8047b99ef0.
- Links: Ticket: SOLR-17911. Category: ui. Siblings: `claims/ui-volunteer-drafts-round-1-SOLR-17911.md` (vm6 staged-ask draft, DONE; Nick has not yet approved the ask). Supersedes: none.
- Decision ladder: rung 1 for the branch; the gate run is the check. GL: the component uses no Canvas, DrawScope or graphicsLayer, so it is not `gl-blast-radius` (checked by reading `FileContentViewer.kt`).
- Constraints: no JIRA or GitHub post, no PR. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-24T07:50:26Z

Run: job `pool-ui-17911-8047b99ef0-075259`, log dir `/workspace/gates/logs/vm6/pool-ui-17911/`, head `8047b99ef0`, started 2026-10-24T07:53:16Z (UTC). Steps: spotless, then `:shared:jvmTest --tests org.apache.solr.ui.views.files.FileContentViewerTest` through `pool/bin/bg-steps.sh`, with the stand-in libEGL and SOFTWARE_FAST.

Heartbeat: 2026-10-24T07:53:16Z
