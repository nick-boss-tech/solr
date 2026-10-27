# Claim: christos-reply-17849-header-field-design-round-3

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: follow-on to `claims/christos-reply-17849-header-criteria-round-2-vm6.md` (DONE at `14ff707865`), on branch `solr-17849-header-submit`.
- Slice: the closed Request Handler field shows the selected handler's class on the same line as its endpoint, as in Figma node `137-12066`, and the two dropdowns take the design's widths (256 and 384 px). Acceptance criterion 10 of SOLR-17849.
- Capability tags used: ui, jvm-tests, gate, implementation.
- Started: 2026-10-27T22:43:27Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: christos-reply-17849-header-field-design-round-3@cf35ab0329.
- Links: Ticket: SOLR-17849. Category: ui. Siblings: round 2 claim (DONE). Supersedes: none.
- Decision ladder:
  - Rung 1 (code): the closed field shows only `value` (the name) in `IndexQueryHeader.kt`, `HeaderDropdown`. The class appears only in the open menu items.
  - Rung 3 (stated intent and design), settled here: criterion 10 says the handler dropdown shows "the endpoint (name) and the request handler (class) are displayed", and "take that into account when chooses sizes". The design capture `material/figma/render/137-12066.png` draws `/select` with `solr.SearchHandler` at the right of the same field, and the handler field wider than the collection field (about 384 px against 256 px at 1920 px width). Those are the design's text and widths; the separator is none, the class sits at the right end in monospace.
  - Rung 2 (mutation): the new assertion `testSelectedHandlerShowsItsClassInTheClosedField` was run on the round 2 code. Result: 1 of 6 failed (the new test, `AssertionError` at `IndexQueryHeaderTest.kt:142`); the other five passed. The assertion discriminates.
  - Environment incident, recorded before the retry: the first mutation run (`header-17849-mutation-224043`) failed all six tests with `UnsatisfiedLinkError` in Skiko. Cause: `/tmp/ui-egl-stub/libEGL.so.1` was missing (removed from `/tmp`). Fix: the stub was rebuilt from `material/ui-test-setup/egl-stub/stub.c`. The retry (`header-17849-mutation-retry-224215`) is the mutation result above. This is an environment change, so the rerun is allowed under `pool/cron-prompt.md` step "Failed runs".
  - Rung 4 (options), one chosen: (a) class in the closed field's trailing slot, monospace, right-aligned (chosen; matches the design's position); (b) a two-line closed field (rejected: the design draws one line); (c) keep name-only and show the class only in the menu (rejected: the design draws it in the closed field).
- Constraints: no PR opening, no Jira or GitHub post. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-27T22:43:27Z

Head: `cf35ab0329c41cdef11429906d1e669d38b9f3a9` on `nick-boss-tech/solr:solr-17849-header-submit` (fast-forward from `14ff707865`; remote SHA verified). Gate input recorded before the run: spotless, then `IndexQueryHeaderTest` (6 tests) on the software renderer.
