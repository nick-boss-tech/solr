# Jira day scan receipt: 2026-08-15 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-16. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-15%22+AND+updated+%3C+%222026-08-16%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-15" AND updated < "2026-08-16"`. One search page returned 1 SOLR ticket updated on 2026-08-15 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; the ticket history was fetched serially.
- Scope totals: 1 ticket, 1 comment across its complete history, 1 commenter account, and 1 fresh watch-list comment by Eric Pugh. No `nickshanin` account comments appear; the full comment history is retained in the totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |
## Fresh watch-list comment
| Person | Ticket and time | Comment summary |
| --- | --- | --- |
| Eric Pugh | [SOLR-18348](https://issues.apache.org/jira/browse/SOLR-18348?focusedCommentId=18105010), `2026-08-15T17:33:13.904+0000` | Eric says he has started exploring a scoped `buildOnCommitAsync` setting. He distinguishes that fix from the existing `waitSearcher=false` workaround, which avoids blocking the commit response but also changes other new-searcher behavior. [Full Jira comment](https://issues.apache.org/jira/browse/SOLR-18348?focusedCommentId=18105010). |
## Candidate work from fresh comments
Candidate only — not claimed: offer Eric Pugh a focused review/test of [PR #4742](https://github.com/apache/solr/pull/4742), checking that the scoped asynchronous suggester rebuild avoids waiting on the commit path without the broad effects of `waitSearcher=false`, while preserving warmup, searcher registration, and search visibility. Ask before starting; Eric is assigned to the ticket and already has this work in progress.
## Per-ticket disposition
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18348](https://issues.apache.org/jira/browse/SOLR-18348?focusedCommentId=18105010) - `buildOnCommit=true` suggester rebuild blocks commit (and the client request) indefinitely with no timeout | `2026-08-15T20:48:04.000+0000` | 1 | Eric Pugh: scoped async config vs the existing `waitSearcher=false` workaround | Open / Unresolved and assigned to Eric. Linked [PR #4742](https://github.com/apache/solr/pull/4742) is open and unmerged; candidate-only review/test opportunity, no work claimed. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
