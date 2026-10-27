# Claim: SOLR-18325 authorized Jira comment and read-back review, Windows round 1

- Host: windows. Tags: review, draft, records.
- Started: 2026-10-27 21:36:44 UTC.
- Owner authorization: Nick explicitly said, "OK I think you could post the comment and give it a review pass after posted" in this chat, following discussion and approval of the client-first proposal and the still-open server migration target.
- Scope: one Jira comment on SOLR-18325, separate read-back and post-publication review, and an add-only board report plus append-only daily comment record. No PR, source change, gate alteration or schedule change.
- Board snapshot: `13abb7ad5eccc28fefa080e81de90f87c9a64156`. Read the live comment formula, scoped list, posting-cap rule, daily ledger, closeout and binary-attribution receipts. The issue still has only the two prior comments; no equivalent proposal exists.
- Candidate remote independently verified at `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`. The client prototype and its proof have not changed.
- Local approved-context draft: `research/SOLR-18325-jira-comment-draft.txt`, refined to express the latest agreed staging: client-only review first, then server assessment for 10.x, with 11 as maintainer preference. Summary at top, evidence link, inherited HTTP refactor links, version/Boot/server limits, footer disclosure.
- Cap/duplication: daily ledger has five owner-directed entries, none for SOLR-18325; the ledger is well below the daily cap. This post is owner-directed, matching the ledger's existing classification outside scan streams.
- Deliverables: `reports/solr-18325-jira-comment-windows-round-1.md` and a new row in `receipts/comment-ledger/2026-10-27.md`. Shared `jira/COMMENTS.md` stays with its designated owner; the report supplies its add-only update information.
- Decision ladder: the release policy remains for maintainers. No new technical compatibility claim or requirement waiver is made. User authorization settles this public action; no additional approval is requested.
