# Claim: solr-17849-browser-check-round-1 (vm5)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: OPEN, started 2026-10-28T05:50Z (UTC).
- Job or assignment: `assignments/solr-17849-browser-check-round-1.md`.
- Slice: the whole assignment. Browser leg of `./gradlew -p ui check` at head `cf35ab0329c41cdef11429906d1e669d38b9f3a9` on `solr-17849-header-submit`, in a clean detached worktree; screen render of `IndexQueryHeader` in the app theme with screenshots under `material/screen-checks/SOLR-17849/`; receipt `receipts/SOLR-17849-browser-check-round-1.md`; draft `drafts/pr-5257-limits-line-update.md` only if the browser leg is green.
- Capability tags used: ui, gate, browser.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-17849-browser-check-round-1@cf35ab0329.
- Links: Ticket: SOLR-17849; PR #5257 (draft). Category: ui. Siblings: the SOLR-17914 browser-check assignment (vm4). Supersedes: none.
- Constraints: no code changes, no pushes to the branch, no PR or Jira posts or edits. One Gradle build at a time under `/workspace/tools/.gradle-vm5.lock`.

Status: DONE 2026-10-28T06:05Z, browser leg NOT green. Receipt `receipts/SOLR-17849-browser-check-round-1.md` (board commit 5310621909). One wasm test fails deterministically: `IndexQueryHeaderTest.testNoCollectionsDisablesBothDropdownsAndShowsMessage` (same failure on rerun). Screen render posted as `material/screen-checks/SOLR-17849/header-populated.png` (commit 7af387dbce). Handler field renders about 308 px against the design's 384 px. No Limits draft written. No code changed.

Heartbeat: 2026-10-28T06:05Z
