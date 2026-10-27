# Jira day scan receipt: 2026-09-10 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-11. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-10%22+AND+updated+%3C+%222026-09-11%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-10" AND updated < "2026-09-11"`. One search page returned all 8 tickets; each `updated` timestamp falls on 2026-09-10 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 8 histories were fetched serially.
- Scope totals: 8 SOLR tickets; 49 comments across their complete histories; 9 commenter accounts. Three fresh watch-list comments were created that day, all by David Smiley. Nick Shanin did not appear in these histories.

## Commenter table

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Andrzej Bialecki | `ab` | 25 | 5 | Commenter account |
| David Smiley | `dsmiley` | 11 | 6 | Commenter account |
| Chris M. Hostetter | `hossman` | 3 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 3 | 1 | Commenter account |
| Noble Paul | `noble.paul` | 3 | 1 | Commenter account |
| Cassandra Targett | `ctargett` | 1 | 1 | Commenter account |
| Ishan Chattopadhyaya | `ichattopadhyaya` | 1 | 1 | Commenter account |
| Shalin Shekhar Mangar | `shalin` | 1 | 1 | Commenter account |
| Tomas Eduardo Fernandez Lobbe | `tflobbe` | 1 | 1 | Commenter account |

## Fresh watch-list comments

These comments were created on 2026-09-10 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-18069](https://issues.apache.org/jira/browse/SOLR-18069?focusedCommentId=18113862), `2026-09-10T13:51:39.829+0000` | “FWIW I ran the PR manually copied to branch_9x and it passes, so the 9.11 release coming soon is compliant. For 10.1+ I'll work on seeing this through.” |
| David Smiley | [SOLR-18278](https://issues.apache.org/jira/browse/SOLR-18278?focusedCommentId=18113899), `2026-09-10T15:10:59.987+0000` | “There appears to be a misunderstanding here. solr-core (where CoreContainer is) depends on Jetty; this isn't a flaw or requirement we've ever had. Wish-list: someday maybe, I imagine it'd be interesting if we could split solr-core up to isolate the EmbeddedSolrServer use-case from the distributed system that Solr is. If we did that, then I think it'd be a reasonable goal to have that future solr-core not have a depe…” |
| David Smiley | [SOLR-18278](https://issues.apache.org/jira/browse/SOLR-18278?focusedCommentId=18113900), `2026-09-10T15:11:50.261+0000` | “More evidence this is invalid: the "Component" was filed as "SolrJ". CoreContainer is not in SolrJ!” |

## Candidate work from fresh comments

No candidate for this host: on SOLR-18069, David Smiley reports the 9.11 release is compliant and says he will handle the 10.1+ follow-through. On SOLR-18278, his comments correct the issue scope/component and describe only a speculative future architecture wishlist; they do not request current work from this host.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18278](https://issues.apache.org/jira/browse/SOLR-18278) - SOLR JETTY dependency decoupling didn't work properly | `2026-09-10T15:11:50.000+0000` | 2 | David Smiley | David Smiley says the current solr-core/Jetty dependency is expected; he flags the SolrJ component as invalid for CoreContainer and describes a possible future split as wishlist only. |
| [SOLR-18069](https://issues.apache.org/jira/browse/SOLR-18069) - Add test for testing maven solrj artifacts | `2026-09-10T13:51:39.000+0000` | 1 | David Smiley | David Smiley reports a manual run on branch_9x passes and says 9.11 is compliant; he will see through 10.1+. No task for this host. |
| [SOLR-17980](https://issues.apache.org/jira/browse/SOLR-17980) - Complete Major changes and Upgrade Notes in RefGuide for 9.10.0 | `2026-09-10T02:15:54.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-15577](https://issues.apache.org/jira/browse/SOLR-15577) - Avoid ClusterState.getCollectionsMap() when not necessary | `2026-09-10T11:27:50.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14182](https://issues.apache.org/jira/browse/SOLR-14182) - Move metric reporters config from solr.xml to ZK cluster properties | `2026-09-10T11:19:12.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13579](https://issues.apache.org/jira/browse/SOLR-13579) - Create resource management API | `2026-09-10T11:18:43.000+0000` | 32 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13578](https://issues.apache.org/jira/browse/SOLR-13578) - Implement a generic Resource Manager for monitoring and controlling limited resources | `2026-09-10T11:17:46.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12757](https://issues.apache.org/jira/browse/SOLR-12757) - Don't load cores that belong to inactive shards | `2026-09-10T11:20:23.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
