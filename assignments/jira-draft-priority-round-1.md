# Assignment: Jira draft production priority, round 1

Status: OPEN

Owner direction, 2026-10-24, recorded by the phone agent: the factory produces Jira drafts to the maximum. Drafting is the default product of every judgement that ends in "a comment is warranted": the deliverable is the draft text, not a recommendation that a draft could be written. When capacity contends, the standing work stores outrank draft production, in this order: gate jobs first, then the day scan and the misc box, then the harmonized backlog store. Draft work takes the capacity that is left and is never the reason a store waits.

## What this means in practice

- Every comment-readiness or triage judgement that concludes a comment is warranted delivers the drafted comment with it (comment formula, Jira wiki markup, footer form for Jira), filed under `drafts/` or `jira/` per the owning assignment, marked DRAFT (not posted).
- A judgement that concludes no comment is warranted states the missing evidence exactly, so the draft can be produced the moment the evidence exists.
- Nothing in this assignment posts anything. Every text under the owner's name needs his go per text.

## Immediate draft queue (claim one item per claim)

1. SOLR-17861 closing note: the scope is settled (option A, see `reports/decision-ladder-pass-2026-10-24.md`). Draft the note that closes the ticket on the checksum slice and names SOLR-17970 as the home of LICENSE and NOTICE presence for the desktop distribution. Addressee context: Christos Malliaridis's ticket. Deliverable: `jira/jira-comment-SOLR-17861-closing-draft.md`.
2. The harmonized backlog comment families (closure-fixed, closure-obsolete, closure-not-a-bug, closure-duplicate, ping-stale, status-note), already assigned as phase H2 of `assignments/harmonize-audits-comment-drafts-round-1.md`; this file records that those drafts are wanted at maximum volume once H1 batches pass QA, subject to the store order above.
3. The bulk-update notice draft, already reposted in `assignments/bulk-notice-and-housekeeping-repost-round-1.md` unit 1.

## Capability tags

`draft`, `records`, `review`.

## Limits

- Drafts only. No Jira, GitHub, or dev-list posts. No new tickets or PRs.
- Drafts follow `comment-formula.md`. First person as the owner. No "we" for the owner plus the agent. No em dashes. No internal process terms in the draft text itself.
