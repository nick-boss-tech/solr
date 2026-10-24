# Claim: solr-18556-cuvs-lucene-round-1

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: LIVE.
- Job or assignment: `assignments/solr-18556-cuvs-lucene-round-1.md`, the whole round.
- Ticket type: research and draft (JIRA is read-only for this claim; the draft is not posted). The immediately previous claim by vm6 was a PR review (PR 5211), so owner complaint 5 is met.
- Slice: the assignment's deliverables, `jira/SOLR-18556-comment-draft-round-1.md` and `reports/solr-18556-round-1.md`. No code change, no pin bump, no gate, no JIRA or GitHub post.
- Capability tags used: review, draft, records.
- Started: 2026-10-24T19:36:08Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18556-cuvs-lucene-round-1@2026-10-24T19:36:08Z.
- Links: Ticket: SOLR-18556. Prior work on the same ticket: `reports/rc3-delta-audit-round-1.md` (item 3, the cuVS note, POM check of releases 25.10.0 to 26.10.0) and `drafts/rc3-vote-note-draft.md`. Those cover the wording, not the options. Supersedes: none.
- Constraints: no GPU-only code run here. Commit as Nick Shanin with no trailers.
- Decision ladder: rung 1 for the codec and the NoSuchMethodError linkage (bytecode of the cuvs-lucene jar against the Lucene jars). Rung 3 for the ticket's expected behavior. Options that change Solr behavior are left to the owner (decision 2 of the assignment).

Heartbeat: 2026-10-24T19:36:08Z
