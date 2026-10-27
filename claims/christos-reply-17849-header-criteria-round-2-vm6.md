# Claim: christos-reply-17849-header-criteria-round-2

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: follow-on to `assignments/christos-reply-17849-header-scope-round-1.md` (DONE at `5d90d83d5b`), on the same branch `solr-17849-header-submit`.
- Slice: the header part of the data layer and state holder, so the header meets the ticket's acceptance criteria 1, 3 and 5. Request execution (`execute`, response, executing) stays out. New head, new gate.
- Capability tags used: ui, jvm-tests, gate, implementation.
- Started: 2026-10-27T21:11:21Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: christos-reply-17849-header-criteria-round-2@<new head sha, set when committed>.
- Links: Ticket: SOLR-17849 (Jira, read 2026-10-27 through the public REST API). Category: ui. Siblings: round 1 claim `claims/christos-reply-17849-header-scope-round-1-vm6.md`; the full screen stays on `solr-17849-index-query-screen`. Supersedes: round 1 head `5d90d83d5b` as the PR candidate.
- Decision ladder:
  - Rung 1 (code and history): the header's collection and handler loading and its selection state sit in `IndexQueryStateHolder.kt` (`loadCollections`, `selectCollection`, `selectHandler`) and `IndexQueryRepository.kt` (`listCollections`, `listRequestHandlers`). `execute` and the response, executing and error paths are request execution.
  - Rung 2: not needed.
  - Rung 3 (stated intent), settled here: the ticket's acceptance criteria say "The available collections are listed", "The available request handlers are loaded based on the collection that is currently selected", and "The currently selected collection and request handler are part of the state". Its "Additional Information" excludes only "the actual form of a request handler". So the loading and selection state are in this ticket; the form and response are not.
  - Rung 4 (options, one chosen):
    - A: header only, criteria 3 and 5 open in the PR's Limits. Rejected: the PR would not meet the ticket it names.
    - B: header, plus the header part of the data layer and state holder. Chosen: meets criteria 1, 3 and 5 with the least code, and keeps the ticket's exclusion (the form and response).
    - C: the whole screen, including `execute`. Rejected: that is the content the ticket and Christos's comment `18133044` place in a separate PR.
  - Rung 5: the change widens the approved round 1 scope (which kept the data layer and state holder out). The approval is the user's direction in this turn to do the fuller change and explain the options in the PR body. That direction is recorded here, not an owner question.
- Constraints: no PR opening, no Jira or GitHub post. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-27T21:11:21Z

Head: `14ff7078657e6907212dbaa2bdabeee59e3678c8` on `nick-boss-tech/solr:solr-17849-header-submit` (fast-forward from round 1 `5d90d83d5b`; remote SHA verified). Gate input recorded before the run: spotless, then `IndexQueryHeaderTest` and `IndexQueryStateHolderTest` on the software renderer.
