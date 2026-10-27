# Jira day scan receipt: 2026-10-09 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-10. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-09%22+AND+updated+%3C+%222026-10-10%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-09" AND updated < "2026-10-10"`. Search pagination: one page, 38 of 38 tickets; all returned `updated` timestamps fall on 2026-10-09 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 38 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 38 SOLR tickets updated on the day; 67 comments across their complete histories; 23 commenter accounts. Three fresh watch-list comments were written by David Smiley, one on SOLR-17796 and two on SOLR-18524. Nick-authored comments, if present, stay in full-history totals only and are excluded from candidate highlights.

## Commenter table

Counts cover all comments in the histories of the 38 tickets, including older comments. `jira-bot` is marked as automation, not a person. No `nickshanin` account appeared in this day’s aggregate. The `vladiceanu` account displayed as Nick Vladiceanu is a different commenter.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 22 | 11 | Automation |
| Chris M. Hostetter | `hossman` | 5 | 4 | Person |
| David Smiley | `dsmiley` | 5 | 4 | Person |
| Shikhar Bhushan | `shikhar` | 5 | 1 | Person |
| Uwe Schindler | `uschindler` | 4 | 4 | Person |
| Pushkar Raste | `praste` | 3 | 1 | Person |
| Andrey Kudryavtsev | `werder` | 2 | 1 | Person |
| Ludovic Boutros | `lboutros` | 2 | 1 | Person |
| Mark Miller | `markrmiller@gmail.com` | 2 | 1 | Person |
| Per Steffensen | `steff1193` | 2 | 1 | Person |
| Scott Lindner | `slindner` | 2 | 1 | Person |
| Varun Thacker | `varun` | 2 | 2 | Person |
| Alexandre Rafalovitch | `arafalov` | 1 | 1 | Person |
| Christine Poerschke | `cpoerschke` | 1 | 1 | Person |
| Gus Heck | `gus` | 1 | 1 | Person |
| Jack Krupansky | `jkrupan` | 1 | 1 | Person |
| Jun Ohtani | `johtani` | 1 | 1 | Person |
| Kevin Liang | `liangkaiwen` | 1 | 1 | Person |
| Michael | `MichaelF` | 1 | 1 | Person |
| Nick Vladiceanu | `vladiceanu` | 1 | 1 | Person |
| Paul Heijman | `paulheijman` | 1 | 1 | Person |
| Robert Muir | `rcmuir` | 1 | 1 | Person |
| Ryan Ernst | `rjernst` | 1 | 1 | Person |

## Fresh watch-list comments

All were created on 2026-10-09 UTC by David Smiley. Excerpts are from Jira; candidate highlights exclude Nick-authored comments.

| Person | Ticket and time | Comment |
| --- | --- | --- |
| David Smiley | [SOLR-17796](https://issues.apache.org/jira/browse/SOLR-17796?focusedCommentId=18125302), `2026-10-09T00:34:13.843+0000` | “Sharing a nice analysis by Opus that I agree with. I'm sympathetic to the user experience sucking in this situation and I am busy filing an issue that would end up fixing this. 🤖 Note the title is backwards: per the description, it's {{q.op=AND}} that fails and {{OR}} that works. The problem is where the tag is placed. In {noformat} fq={!tag=collapse_tag}{!collapse field=someid} {noformat} the leading local-params n…” |
| David Smiley | [SOLR-18524](https://issues.apache.org/jira/browse/SOLR-18524?focusedCommentId=18125313), `2026-10-09T01:17:04.950+0000` | “"q.alt" then becomes technically "alt" when viewed by the query parser of "q", due to the hoisting I proposed. For consistency anyway, not because I think the parameter deserves it otherwise. I don't love that change specifically but why not. If I might take this local-param hoisting farther a little, I'd make "q.type" be a preferred substitute for "defType" since "type" happens to be the little-known local-param th…” |
| David Smiley | [SOLR-18524](https://issues.apache.org/jira/browse/SOLR-18524?focusedCommentId=18125314), `2026-10-09T01:22:46.297+0000` | “A different path that would be less controversial is to drop the hoisting idea, but nonetheless still transition to local-param only for certain important parameters -- namely transition "q.op" and "df", to local-param only "op" and "f". This would let us use the DeprecationLog to warn users of legacy request parameters, whereas hoisting wouldn't.” |

## Candidate work from the fresh comments

- **SOLR-17796, David Smiley:** He says the title is backwards, clarifies that `q.op=AND` fails while `OR` works because of the local-param tag placement, and says he is filing a separate issue to fix the user experience. Candidate only: locate that issue once it exists and confirm it captures the corrected reproduction and local-param placement. No task was claimed.
- **SOLR-18524, David Smiley:** The two comments compare hoisting local parameters (including `q.alt` and possibly `q.type`) with a less controversial transition of `q.op` and `df` to `op` and `f`, which would allow `DeprecationLog` warnings. Candidate only: summarize the compatibility trade-offs and seek the maintainer’s direction before proposing implementation. No task was claimed.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Only SOLR-17796 and SOLR-18524 had fresh watch-list activity.

| Ticket | Comments | Fresh watch-list activity | Disposition |
| --- | ---: | --- | --- |
| [SOLR-18524](https://issues.apache.org/jira/browse/SOLR-18524) | 2 | David Smiley | Two fresh comments compare local-parameter migration approaches; candidate only to seek direction before implementation. |
| [SOLR-18519](https://issues.apache.org/jira/browse/SOLR-18519) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18518](https://issues.apache.org/jira/browse/SOLR-18518) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18517](https://issues.apache.org/jira/browse/SOLR-18517) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18516](https://issues.apache.org/jira/browse/SOLR-18516) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18514](https://issues.apache.org/jira/browse/SOLR-18514) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18513](https://issues.apache.org/jira/browse/SOLR-18513) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18512](https://issues.apache.org/jira/browse/SOLR-18512) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18511](https://issues.apache.org/jira/browse/SOLR-18511) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18510](https://issues.apache.org/jira/browse/SOLR-18510) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18509](https://issues.apache.org/jira/browse/SOLR-18509) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18476](https://issues.apache.org/jira/browse/SOLR-18476) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18449](https://issues.apache.org/jira/browse/SOLR-18449) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17796](https://issues.apache.org/jira/browse/SOLR-17796) | 1 | David Smiley | David Smiley corrects the title/reproduction and says he is filing a separate fix issue; candidate only to follow that issue. |
| [SOLR-16910](https://issues.apache.org/jira/browse/SOLR-16910) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-16655](https://issues.apache.org/jira/browse/SOLR-16655) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-16356](https://issues.apache.org/jira/browse/SOLR-16356) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14718](https://issues.apache.org/jira/browse/SOLR-14718) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14262](https://issues.apache.org/jira/browse/SOLR-14262) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13943](https://issues.apache.org/jira/browse/SOLR-13943) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13265](https://issues.apache.org/jira/browse/SOLR-13265) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12864](https://issues.apache.org/jira/browse/SOLR-12864) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12705](https://issues.apache.org/jira/browse/SOLR-12705) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12703](https://issues.apache.org/jira/browse/SOLR-12703) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12245](https://issues.apache.org/jira/browse/SOLR-12245) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12212](https://issues.apache.org/jira/browse/SOLR-12212) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11483](https://issues.apache.org/jira/browse/SOLR-11483) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11475](https://issues.apache.org/jira/browse/SOLR-11475) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7504](https://issues.apache.org/jira/browse/SOLR-7504) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7022](https://issues.apache.org/jira/browse/SOLR-7022) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6065](https://issues.apache.org/jira/browse/SOLR-6065) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6045](https://issues.apache.org/jira/browse/SOLR-6045) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5941](https://issues.apache.org/jira/browse/SOLR-5941) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5939](https://issues.apache.org/jira/browse/SOLR-5939) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5887](https://issues.apache.org/jira/browse/SOLR-5887) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5505](https://issues.apache.org/jira/browse/SOLR-5505) | 8 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5161](https://issues.apache.org/jira/browse/SOLR-5161) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-3657](https://issues.apache.org/jira/browse/SOLR-3657) | 2 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
