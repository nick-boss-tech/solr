# Jira day scan receipt: 2026-09-04 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-05. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-04%22+AND+updated+%3C+%222026-09-05%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-04" AND updated < "2026-09-05"`. One search page returned all 5 tickets; each `updated` timestamp falls on 2026-09-04 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 5 histories were fetched serially.
- Scope totals: 5 SOLR tickets; 15 comments across their complete histories; 8 commenter accounts. Three fresh watch-list comments were created that day on SOLR-18214 by Jan Høydahl and Christos Malliaridis. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| David Smiley | `dsmiley` | 3 | 1 | Commenter account |
| Vishnu Priya | `vchandsekar` | 3 | 1 | Commenter account |
| Daniel Lowe | `dan2097` | 2 | 1 | Commenter account |
| Eric Pugh | `epugh` | 2 | 2 | Commenter account |
| Jan Høydahl | `janhoy` | 2 | 1 | Commenter account |
| Alex Deparvu | `stillalex` | 1 | 1 | Commenter account |
| Christos Malliaridis | `malliaridis` | 1 | 1 | Commenter account |
| Michael Gibney | `magibney` | 1 | 1 | Commenter account |

## Fresh watch-list comments

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Jan Høydahl | [SOLR-18214](https://issues.apache.org/jira/browse/SOLR-18214?focusedCommentId=18111426), `2026-09-04T06:51:00.687+0000` | “There was a PR that went unreviewed and got closed, was that an omission? What's the way forward here? Personally I find it a corner case to first delete _default, but yea, should not break things. Could make _default impossible to delete as an alternative?” |
| Christos Malliaridis | [SOLR-18214](https://issues.apache.org/jira/browse/SOLR-18214?focusedCommentId=18111524), `2026-09-04T11:30:56.992+0000` | “I believe there was no consensus how to address this issue. I would choose the following API behavior: - If the user omits the `baseConfigset` field in the request (null or not set), the configset is created without a base configset (no clone by default) - Is this possible? Can someone create a configset without a base structure? - The user has to set explicitly the `baseConfigset` to `_default` if they want to use…” |
| Jan Høydahl | [SOLR-18214](https://issues.apache.org/jira/browse/SOLR-18214?focusedCommentId=18111537), `2026-09-04T12:24:44.539+0000` | “Depends on when you want to land this change. If you require explicit baseConfigset param then that is back-compat-break and cannot land before 11.0. But falling back to _default and making it a protected configset would be back compat.” |

## Candidate work from fresh comments

Candidate only — not claimed: Jan Høydahl and Christos Malliaridis discuss SOLR-18214’s unresolved `baseConfigset` contract after an earlier PR was closed without review/consensus. Clarify whether an omitted/null parameter means “no base” or `_default`, whether no-base configsets can be created, and how empty strings should behave; then choose the version-compatible path (explicit parameter as a breaking change no earlier than 11.0, or a backward-compatible `_default` fallback with `_default` protected) and decide the path for the closed PR.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18421](https://issues.apache.org/jira/browse/SOLR-18421) - {!child}: childQuery + per-parent childLimit via Lucene ParentsChildrenBlockJoinQuery | `2026-09-04T21:49:11.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18363](https://issues.apache.org/jira/browse/SOLR-18363) - Remove EarlyTerminatingSortingCollector | `2026-09-04T21:05:59.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18214](https://issues.apache.org/jira/browse/SOLR-18214) - Base configset during configset creation is not optional | `2026-09-04T12:24:44.000+0000` | 4 | Jan Høydahl, Christos Malliaridis | Jan Høydahl and Christos Malliaridis discuss an unresolved API contract and compatibility tradeoff; candidate-only, not claimed. |
| [SOLR-18101](https://issues.apache.org/jira/browse/SOLR-18101) - SolrJ, improve commit API to be fluent; deprecate setAction | `2026-09-04T15:55:28.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17433](https://issues.apache.org/jira/browse/SOLR-17433) - SolrStream by default creates a Http2SolrClient that can only stream for 60 seconds | `2026-09-04T00:08:41.000+0000` | 11 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
