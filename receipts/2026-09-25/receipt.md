# Jira day scan receipt: 2026-09-25 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-26. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-25%22+AND+updated+%3C+%222026-09-26%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-25" AND updated < "2026-09-26"`. One search page returned all 5 tickets; every `updated` timestamp falls on 2026-09-25 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 5 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 5 SOLR tickets; 9 comments across their complete histories; 8 commenter accounts. Four fresh watch-list comments were created that day: three on SOLR-18131 (Jan Høydahl, David Smiley, Jan Høydahl) and one on SOLR-18483 (Eric Pugh). Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` was not present among this day’s commenters; all eight entries are human accounts.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Jan Høydahl | `janhoy` | 2 | 1 | Commenter account |
| Amrit Sarkar | `sarkaramrit2@gmail.com` | 1 | 1 | Commenter account |
| Andrzej Bialecki | `ab` | 1 | 1 | Commenter account |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |
| Eduard Zlotnik | `eduardzl` | 1 | 1 | Commenter account |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |
| Justin Pealing | `justinpealing` | 1 | 1 | Commenter account |
| Lucas Kot-Zaniewski | `lkot` | 1 | 1 | Commenter account |

## Fresh watch-list comments

All four comments below were created on 2026-09-25 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Jan Høydahl | [SOLR-18131](https://issues.apache.org/jira/browse/SOLR-18131?focusedCommentId=18119179), `2026-09-25T12:23:57.371+0000` | “Won't this couple zk detail with a public API? Of course, the version could be an opaque string, with no ZK ref. I floated another idea on the list for using an event-stream (SSE) based API for clusterstate, where clients will get a real time stream of cluster state changes, e.g. a new list of live_nodes every time it changes. Problem is that no single solr node has ZK watches for the entire state, only the collecti…” |
| David Smiley | [SOLR-18131](https://issues.apache.org/jira/browse/SOLR-18131?focusedCommentId=18119188), `2026-09-25T13:00:04.541+0000` | “We do want ZK behind the scenes but it isn't going away. It's the most "load bearing" thing in SolrCloud. Note that this proposal doesn't increase the ZK exposure; this proposes a _different_ way to do what we're doing today. And even though it's technically in the public API; it's an optional header exchange detail for a client that wishes to use it. SSE seems great for keeping a browser's webpage up-to-date on a v…” |
| Jan Høydahl | [SOLR-18131](https://issues.apache.org/jira/browse/SOLR-18131?focusedCommentId=18119547), `2026-09-25T22:02:15.584+0000` | “You could of course filter the envent-stream on a list of collections too, but it is still an active connection and transferred data that may not be needed. Of course a ClusterStateProvider could be smarter and disconnect from the event-stream as long as it does not need the state for routing decisions, and then re-connect on demand, requesting only the set of events that have changed since that timestamp. That woul…” |
| Eric Pugh | [SOLR-18483](https://issues.apache.org/jira/browse/SOLR-18483?focusedCommentId=18119548), `2026-09-25T22:16:30.456+0000` | “Oh joy, turns out SolrJ was still using wt=javabin and wasn't using Accepts even with the V2 apis it supported.” |

## Candidate work from fresh comments

- **Candidate only - [SOLR-18131](https://issues.apache.org/jira/browse/SOLR-18131), Jan Høydahl and David Smiley:** clarify whether this ticket stays scoped to the optional version header or also pursues a broader SSE cluster-state stream. If SSE remains in scope, settle collection filtering and on-demand disconnect/reconnect behavior to avoid unnecessary active traffic. No work is claimed.
- **Candidate only - [SOLR-18483](https://issues.apache.org/jira/browse/SOLR-18483), Eric Pugh:** verify how SolrJ negotiates V2 response formats, since the fresh note says it still used `wt=javabin` and was not using `Accept`; clarify the client behavior before relying on disabling `wt` in V2. No work is claimed.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Tickets without fresh watch-list activity have no candidate from this scan.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18483](https://issues.apache.org/jira/browse/SOLR-18483) - Prevent wt= parameter from working in V2 land | `2026-09-25T23:18:56.000+0000` | 1 | Eric Pugh | Eric Pugh notes SolrJ still used wt=javabin rather than Accept for V2; candidate-only client-negotiation check. |
| [SOLR-18481](https://issues.apache.org/jira/browse/SOLR-18481) - Proxied requests should use X-FORWARDED-FOR header | `2026-09-25T06:27:03.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18316](https://issues.apache.org/jira/browse/SOLR-18316) - Slow Solrcloud recovery following restart of Zookeeper leader | `2026-09-25T13:53:09.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18131](https://issues.apache.org/jira/browse/SOLR-18131) - SolrCloud should return HTTP header(s) of certain ZK node versions | `2026-09-25T22:02:15.000+0000` | 3 | David Smiley, Jan Høydahl | Three design comments debate optional version headers vs. SSE, public API/ ZooKeeper exposure, and filtering/reconnection; candidate-only scope clarification. |
| [SOLR-9904](https://issues.apache.org/jira/browse/SOLR-9904) - JSON API facet incorrect counts for specific values when exclusion applied | `2026-09-25T15:03:07.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
