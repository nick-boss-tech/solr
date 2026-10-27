# Jira day scan receipt: 2026-10-07 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-08. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-07%22+AND+updated+%3C+%222026-10-08%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-07" AND updated < "2026-10-08"`. Search pagination: one page, 16 of 16 tickets; all returned `updated` timestamps fall on 2026-10-07 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 16 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 16 SOLR tickets updated on the day; 70 comments across their complete histories; 20 commenter accounts. Five fresh watch-list comments were authored by David Smiley (two on SOLR-18437) and Eric Pugh (one each on SOLR-15592, SOLR-9749, and SOLR-8100). Nick Shanin’s one historical comment remains in the aggregate only and is excluded from candidate highlights.

## Commenter table

Counts cover all comments in the histories of the 16 tickets, including older comments. `jira-bot` is marked as automation, not a person. Nick Shanin is labeled self and included for aggregate accounting only.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 22 | 6 | Automation |
| David Smiley | `dsmiley` | 9 | 6 | Person |
| Mikhail Khludnev | `mkhl` | 9 | 1 | Person |
| Eric Pugh | `epugh` | 5 | 4 | Person |
| Andrzej Bialecki | `ab` | 4 | 3 | Person |
| Jan Høydahl | `janhoy` | 4 | 1 | Person |
| Christine Poerschke | `cpoerschke` | 2 | 1 | Person |
| Rahul Goswami | `rahulgoswami` | 2 | 1 | Person |
| Steven Rowe | `sarowe` | 2 | 1 | Person |
| Alessandro Benedetti | `abenedetti` | 1 | 1 | Person |
| Anshum Gupta | `anshum` | 1 | 1 | Person |
| Aviral Sinha | `aveeraldsinha` | 1 | 1 | Person |
| Gus Heck | `gus` | 1 | 1 | Person |
| Lucas Kot-Zaniewski | `lkot` | 1 | 1 | Person |
| Nick Shanin | `nickshanin` | 1 | 1 | Self, aggregate only |
| Noble Paul | `noble.paul` | 1 | 1 | Person |
| Piotr Karwasz | `pkarwasz` | 1 | 1 | Person |
| Ravi Ranjan Jha | `ravijha` | 1 | 1 | Person |
| Upayavira | `upayavira` | 1 | 1 | Person |
| Yifei Geng | `pigeongeng` | 1 | 1 | Person |

## Fresh watch-list comments

These comments were created on 2026-10-07 UTC. Nick Shanin’s comments remain in the history totals only and are excluded from candidate highlights.

| Person | Ticket and time | Comment |
| --- | --- | --- |
| David Smiley | [SOLR-18437](https://issues.apache.org/jira/browse/SOLR-18437?focusedCommentId=18124228), `2026-10-07T01:48:46.440+0000` | “Didn't review TBH” |
| David Smiley | [SOLR-18437](https://issues.apache.org/jira/browse/SOLR-18437?focusedCommentId=18124435), `2026-10-07T11:19:37.808+0000` | “oops; Jan got to this now after the release: [https://github.com/apache/solr/pull/5035]” |
| Eric Pugh | [SOLR-15592](https://issues.apache.org/jira/browse/SOLR-15592?focusedCommentId=18124529), `2026-10-07T14:12:22.376+0000` | “The new Solr UI embraces internationalization from the very beginning, so we don't need a seperate ticket for it. We aren't planning on extending the existing Solr Admin UI, so I don't think we need this ticket. Please do reopen if you think this is soemthing we want to move forward and put energy in! Otehrwise, I hope we do leverage the Solr UI framework to move byeond just English!” |
| Eric Pugh | [SOLR-9749](https://issues.apache.org/jira/browse/SOLR-9749?focusedCommentId=18124530), `2026-10-07T14:16:51.930+0000` | “Given time passing, this end point no longer exists.... So closing it.” |
| Eric Pugh | [SOLR-8100](https://issues.apache.org/jira/browse/SOLR-8100?focusedCommentId=18124535), `2026-10-07T14:18:33.647+0000` | “This is in progress as part of the V2 api rewrite and we've added the new Selenium based tests. Between that, and the use of the strongly typed JS client generated from V2 APIs, I think this is covered!” |

## Candidate work from the fresh comments

No follow-up candidate was identified. The fresh comments report completed or covered work, a closed or unnecessary ticket, or a follow-up handled by another contributor; no task was claimed.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Fresh comments appeared only on SOLR-18437, SOLR-15592, SOLR-9749, and SOLR-8100.

| Ticket | Comments | Fresh watch-list activity | Disposition |
| --- | ---: | --- | --- |
| [SOLR-18451](https://issues.apache.org/jira/browse/SOLR-18451) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18437](https://issues.apache.org/jira/browse/SOLR-18437) | 2 | David Smiley | David Smiley says he did not review; then notes Jan handled it after the release in PR #5035. No candidate for this host. |
| [SOLR-18307](https://issues.apache.org/jira/browse/SOLR-18307) | 13 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18273](https://issues.apache.org/jira/browse/SOLR-18273) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18210](https://issues.apache.org/jira/browse/SOLR-18210) | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18200](https://issues.apache.org/jira/browse/SOLR-18200) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18186](https://issues.apache.org/jira/browse/SOLR-18186) | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18178](https://issues.apache.org/jira/browse/SOLR-18178) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18097](https://issues.apache.org/jira/browse/SOLR-18097) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18086](https://issues.apache.org/jira/browse/SOLR-18086) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18027](https://issues.apache.org/jira/browse/SOLR-18027) | 15 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17392](https://issues.apache.org/jira/browse/SOLR-17392) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-16701](https://issues.apache.org/jira/browse/SOLR-16701) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-15592](https://issues.apache.org/jira/browse/SOLR-15592) | 3 | Eric Pugh | Eric Pugh says the new Solr UI covers internationalization and the old Admin UI will not be extended; reopen if that direction is reconsidered. Settled, no candidate. |
| [SOLR-9749](https://issues.apache.org/jira/browse/SOLR-9749) | 4 | Eric Pugh | Eric Pugh says the endpoint no longer exists and is closing the ticket. Settled, no candidate. |
| [SOLR-8100](https://issues.apache.org/jira/browse/SOLR-8100) | 3 | Eric Pugh | Eric Pugh says V2 API work, Selenium tests, and the typed JS client cover the need. Status signal, no candidate. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
