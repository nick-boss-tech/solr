# Jira day scan receipt: 2026-09-20 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-21. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-20%22+AND+updated+%3C+%222026-09-21%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-20" AND updated < "2026-09-21"`. One search page returned all 22 tickets; each `updated` timestamp falls on 2026-09-20 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 22 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 22 SOLR tickets; 118 comments across their complete histories; 24 commenter accounts. There were 14 fresh watch-list comments on 2026-09-20. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 23 | 8 | Automation |
| Cassandra Targett | `ctargett` | 19 | 5 | Commenter account |
| Eric Pugh | `epugh` | 16 | 12 | Commenter account |
| Chris M. Hostetter | `hossman` | 10 | 5 | Commenter account |
| Jan Høydahl | `janhoy` | 10 | 4 | Commenter account |
| Joel Bernstein | `jbernste` | 6 | 2 | Commenter account |
| Alexandre Rafalovitch | `arafalov` | 5 | 1 | Commenter account |
| Oussema Hidri | `Dmidma` | 4 | 1 | Commenter account |
| Andrzej Bialecki | `ab` | 3 | 2 | Commenter account |
| David Smiley | `dsmiley` | 3 | 3 | Commenter account |
| Pierre Salagnac | `psalagnac` | 2 | 1 | Commenter account |
| Sanjay Dutt | `sanjaydutt` | 2 | 1 | Commenter account |
| Shawn Heisey | `elyograg` | 2 | 1 | Commenter account |
| Uwe Schindler | `uschindler` | 2 | 1 | Commenter account |
| Varun Thacker | `varun` | 2 | 2 | Commenter account |
| Avtar Singh | `asmehra95` | 1 | 1 | Commenter account |
| Christine Poerschke | `cpoerschke` | 1 | 1 | Commenter account |
| Christos Malliaridis | `malliaridis` | 1 | 1 | Commenter account |
| Gus Heck | `gus` | 1 | 1 | Commenter account |
| Isabelle Giguere | `igiguere` | 1 | 1 | Commenter account |
| Jason Gerlowski | `gerlowskija` | 1 | 1 | Commenter account |
| Nazerke Seidan | `nazerke` | 1 | 1 | Commenter account |
| Upayavira | `upayavira` | 1 | 1 | Commenter account |
| Uwe Reh | `uwe reh` | 1 | 1 | Commenter account |

## Fresh watch-list comments

Excerpts are short extracts from the retrieved comments; each links to its Jira source.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Eric Pugh | [SOLR-12961](https://issues.apache.org/jira/browse/SOLR-12961?focusedCommentId=18117196), `2026-09-20T00:37:27.063+0000` | “i think this can be closed as won't do or good enough. I just don't see us having the capacity to maintain a real keywrods capablity, and honestly google and agents figure it out” |
| Eric Pugh | [SOLR-5467](https://issues.apache.org/jira/browse/SOLR-5467?focusedCommentId=18117197), `2026-09-20T00:45:23.827+0000` | “We have embraced web and need to think about agents and markdown not epub... Please reopen if htis changes!” |
| Eric Pugh | [SOLR-10728](https://issues.apache.org/jira/browse/SOLR-10728?focusedCommentId=18117198), `2026-09-20T00:47:35.805+0000` | “Still an issue? I think rat works?” |
| Eric Pugh | [SOLR-10290](https://issues.apache.org/jira/browse/SOLR-10290?focusedCommentId=18117199), `2026-09-20T00:48:11.042+0000` | “I think after many moons, we can close this one! We have a great model.” |
| Eric Pugh | [SOLR-10290](https://issues.apache.org/jira/browse/SOLR-10290?focusedCommentId=18117200), `2026-09-20T00:48:45.459+0000` | “Since our web appraoch with our built in js based search, I htink the ref guide has been rocking!” |
| Eric Pugh | [SOLR-11766](https://issues.apache.org/jira/browse/SOLR-11766?focusedCommentId=18117201), `2026-09-20T00:49:10.701+0000` | “anything left on this?” |
| Eric Pugh | [SOLR-17403](https://issues.apache.org/jira/browse/SOLR-17403?focusedCommentId=18117202), `2026-09-20T00:49:50.392+0000` | “There has been some progress on this topic with the Picocli effort....!” |
| Eric Pugh | [SOLR-15931](https://issues.apache.org/jira/browse/SOLR-15931?focusedCommentId=18117203), `2026-09-20T00:50:23.474+0000` | “This was delivered sometime in the 9x time line!” |
| Eric Pugh | [SOLR-10329](https://issues.apache.org/jira/browse/SOLR-10329?focusedCommentId=18117204), `2026-09-20T00:51:50.153+0000` | “One thing I ran into recently was "gettingstarted" versus "techproducts" and it would be nice if our Ref Guide pages leaned into using the techproducts since we all have it for various examples... Plus, it would mkae it easier to test out the commands in the ref guide if they were developed agaisnt a real techproducts. "LLM, run every curl command in the ref guide"....” |
| Eric Pugh | [SOLR-12603](https://issues.apache.org/jira/browse/SOLR-12603?focusedCommentId=18117205), `2026-09-20T00:54:45.284+0000` | “I think the _default does this...” |
| Eric Pugh | [SOLR-11918](https://issues.apache.org/jira/browse/SOLR-11918?focusedCommentId=18117206), `2026-09-20T00:55:42.224+0000` | “Based on [~igiguere] feedback, closing.” |
| Eric Pugh | [SOLR-4987](https://issues.apache.org/jira/browse/SOLR-4987?focusedCommentId=18117207), `2026-09-20T00:56:09.144+0000` | “Still want a button in the Solr UI!” |
| David Smiley | [SOLR-18414](https://issues.apache.org/jira/browse/SOLR-18414?focusedCommentId=18117475), `2026-09-20T20:26:25.783+0000` | “Thanks for doing this :-)” |
| Christos Malliaridis | [SOLR-18459](https://issues.apache.org/jira/browse/SOLR-18459?focusedCommentId=18117500), `2026-09-20T22:22:00.299+0000` | “[~epugh] Since this is an umbrella ticket and the changes of subtasks are minimal, I think it is worth creating one changelog that covers all sub-tasks, rather than one changelog entry for each sub-task. What do you think?” |

## Candidate work from fresh comments

The candidates below are not claims or assignments. Comments that explicitly close, settle, or merely acknowledge work are recorded in the ticket table without a candidate.

- **Candidate only - SOLR-10728, Eric Pugh:** verify whether the RAT/source-check issue still reproduces and, if the current check is clean, confirm whether the ticket can be closed or narrowed.
- **Candidate only - SOLR-11766, Eric Pugh:** inventory any remaining work on the Streaming Expression reference redesign and decide whether the ticket can close if no concrete page work remains.
- **Candidate only - SOLR-10329, Eric Pugh:** assess moving reference-guide examples from `gettingstarted` to the shared `techproducts` example, then validate whether the documented curl commands run against it.
- **Candidate only - SOLR-12603, Eric Pugh:** check whether `_default` already satisfies the minimal-configset need; if so, confirm closure or rescope rather than duplicating existing behavior.
- **Candidate only - SOLR-4987, Eric Pugh:** clarify whether the reindex API should also expose a Solr UI button and what user action the interface should support.

## Per-ticket dispositions

“Comments” counts the complete retrieved history for each ticket. Fresh activity is listed by its watch-list commenter; candidate lines above are limited to fresh comments.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18467](https://issues.apache.org/jira/browse/SOLR-18467) - Look a bit at SEO | `2026-09-20T10:40:59.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18466](https://issues.apache.org/jira/browse/SOLR-18466) - Support _route_ in the streaming expression search function to prune shard fan-out | `2026-09-20T02:29:29.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18461](https://issues.apache.org/jira/browse/SOLR-18461) - Make more example commands curl based and using techproducts | `2026-09-20T01:01:18.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18459](https://issues.apache.org/jira/browse/SOLR-18459) - Buff up the Ref Guide Umbrella Issue | `2026-09-20T22:22:00.000+0000` | 1 | Christos Malliaridis | Christos Malliaridis asks Eric Pugh whether one umbrella changelog should cover minimal subtask changes; addressed to Eric, not this host’s candidate work. |
| [SOLR-18441](https://issues.apache.org/jira/browse/SOLR-18441) - Add Global Ordinals Join Query Parser (`{!globalOrdinalsJoin}`) for single-index, single-field joins | `2026-09-20T20:22:34.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18440](https://issues.apache.org/jira/browse/SOLR-18440) - `{!join score=... from=numerics_dv to=points_int}...` | `2026-09-20T20:23:11.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18438](https://issues.apache.org/jira/browse/SOLR-18438) - Unify retrieving "live node" information in Solr Admin to a single API | `2026-09-20T20:25:58.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18414](https://issues.apache.org/jira/browse/SOLR-18414) - Randomize use of QueryLimits in all tests | `2026-09-20T20:26:25.000+0000` | 6 | David Smiley | David Smiley thanks the contributor; acknowledgement only. |
| [SOLR-18397](https://issues.apache.org/jira/browse/SOLR-18397) - Manually cleanup '*Copy' configuration names in grade.lockfiles | `2026-09-20T22:27:59.000+0000` | 8 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18305](https://issues.apache.org/jira/browse/SOLR-18305) - Time-limited requests are scored with the default BM25Similarity instead of the schema similarity | `2026-09-20T19:19:18.000+0000` | 10 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18207](https://issues.apache.org/jira/browse/SOLR-18207) - Add derived stored retrieval for DenseVectorField to avoid duplicate vector storage | `2026-09-20T00:19:53.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17403](https://issues.apache.org/jira/browse/SOLR-17403) - Restructure Ref Guide Page Solr Control Script Reference | `2026-09-20T00:49:50.000+0000` | 3 | Eric Pugh | Eric Pugh notes progress through the Picocli effort; status only, with no specific follow-up in the excerpt. |
| [SOLR-15931](https://issues.apache.org/jira/browse/SOLR-15931) - Consolidate Ref Guide "films" tutorial with /solr/example/films/README.md | `2026-09-20T00:50:23.000+0000` | 1 | Eric Pugh | Eric Pugh says the change was delivered in the 9.x line; apparent completion, no candidate. |
| [SOLR-12961](https://issues.apache.org/jira/browse/SOLR-12961) - Ref Guide: Add keyword metadata to pages | `2026-09-20T00:37:27.000+0000` | 3 | Eric Pugh | Eric Pugh says close as Won’t Do/good enough because keyword capability would be costly to maintain; recorded as a closure direction, no candidate. |
| [SOLR-12603](https://issues.apache.org/jira/browse/SOLR-12603) - Create a super-minimal configset | `2026-09-20T00:54:45.000+0000` | 1 | Eric Pugh | Candidate only; see candidate section. |
| [SOLR-11918](https://issues.apache.org/jira/browse/SOLR-11918) - Document usage of SystemInfoHandler at the node level | `2026-09-20T00:55:42.000+0000` | 4 | Eric Pugh | Eric Pugh says it is closing based on Isabelle Giguere’s feedback; settled, no candidate. |
| [SOLR-11766](https://issues.apache.org/jira/browse/SOLR-11766) - Ref Guide: redesign Streaming Expression reference pages | `2026-09-20T00:49:10.000+0000` | 19 | Eric Pugh | Candidate only; see candidate section. |
| [SOLR-10728](https://issues.apache.org/jira/browse/SOLR-10728) - fix/improve rat-sources and check-forbidden-api in solr-ref-guide | `2026-09-20T22:00:02.000+0000` | 6 | Eric Pugh | Candidate only; see candidate section. |
| [SOLR-10329](https://issues.apache.org/jira/browse/SOLR-10329) - Rebuild Solr examples | `2026-09-20T00:51:50.000+0000` | 14 | Eric Pugh | Candidate only; see candidate section. |
| [SOLR-10290](https://issues.apache.org/jira/browse/SOLR-10290) - New Publication Model for Solr Reference Guide | `2026-09-20T00:48:45.000+0000` | 18 | Eric Pugh | Eric Pugh explicitly says the publication-model ticket can close and praises the current model; settled, no candidate. |
| [SOLR-5467](https://issues.apache.org/jira/browse/SOLR-5467) - Provide Solr Ref Guide in .epub format | `2026-09-20T00:45:23.000+0000` | 7 | Eric Pugh | Eric Pugh says the project is focused on web, agents, and Markdown rather than EPUB; status/decision, no candidate. |
| [SOLR-4987](https://issues.apache.org/jira/browse/SOLR-4987) - Add reindex API | `2026-09-20T00:56:09.000+0000` | 6 | Eric Pugh | Candidate only; see candidate section. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
