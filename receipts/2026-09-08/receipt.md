# Jira day scan receipt: 2026-09-08 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-09. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-08%22+AND+updated+%3C+%222026-09-09%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-08" AND updated < "2026-09-09"`. One search page returned all 14 tickets; each `updated` timestamp falls on 2026-09-08 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 14 histories were fetched serially.
- Scope totals: 14 SOLR tickets; 35 comments across their complete histories; 7 commenter accounts. Two fresh watch-list comments were created that day, both by David Smiley. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 21 | 7 | Automation |
| Jason Gerlowski | `gerlowskija` | 4 | 1 | Commenter account |
| David Smiley | `dsmiley` | 3 | 2 | Commenter account |
| Chris M. Hostetter | `hossman` | 2 | 1 | Commenter account |
| Eric Pugh | `epugh` | 2 | 2 | Commenter account |
| Gus Heck | `gus` | 2 | 1 | Commenter account |
| ilariapet | `ilariapet` | 1 | 1 | Commenter account |

## Fresh watch-list comments

These comments were created on 2026-09-08 UTC. The SOLR-18343 excerpt below is shortened; its Jira-linked comment records additional details summarized in the candidate section.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-18343](https://issues.apache.org/jira/browse/SOLR-18343?focusedCommentId=18112505), `2026-09-08T04:37:35.119+0000` | “Reverting on 9.x; led to some mysteries I don't have time to solve on 9x. Using POST exposed some issues. One is that TestSolrCloudWithSecureImpersonation (related to deprecated auth stuff) needed "user" in the URL query portion. I fixed this to ensure it got there. Next I/Claude noticed org.apache.solr.client.solrj.impl.SolrHttpRequestRetryHandler#handleAsIdempotent wasn't activating because the path is /solr/admin…” |
| David Smiley | [SOLR-18100](https://issues.apache.org/jira/browse/SOLR-18100?focusedCommentId=18112699), `2026-09-08T12:19:49.787+0000` | “[~ilariapet] maybe you are unfamiliar with how we do things but we leave "Closing" a merged issue until _after_ it ships on a release. The release manager does this in bulk so you needn't worry about it.” |

## Candidate work from fresh comments

Candidate only — not claimed: David Smiley’s SOLR-18343 comment says the POST-method change was reverted on 9.x after exposing issues, including a retry-handler path check that treated `/solr/admin/...` requests as retryable when they should not be, plus a flaky no-retry test. A bounded follow-up would clarify intended retry behavior and add regression coverage for admin-path idempotency and the secure-impersonation `user` query parameter.

SOLR-18100 is settled process guidance: David Smiley explains that merged issues remain in “Closing” until a release ships, when the release manager closes them in bulk; no follow-up candidate.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18431](https://issues.apache.org/jira/browse/SOLR-18431) - Add v2 API equivalent for "Fetch Replica Prop" | `2026-09-08T23:44:41.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18430](https://issues.apache.org/jira/browse/SOLR-18430) - Add v2 API equivalent for "List Replica Props" | `2026-09-08T23:45:19.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18429](https://issues.apache.org/jira/browse/SOLR-18429) - Add v2 API equivalent for "Fetch Single Replica" | `2026-09-08T23:46:06.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18428](https://issues.apache.org/jira/browse/SOLR-18428) - Add v2 API equivalent for "Fetch Single Shard" | `2026-09-08T23:46:35.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18416](https://issues.apache.org/jira/browse/SOLR-18416) - Deal with VectorSimilarityQuery backcompat break in Lucene 10.5 | `2026-09-08T23:46:11.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18399](https://issues.apache.org/jira/browse/SOLR-18399) - ui-grid icon font is missing from the webapp | `2026-09-08T01:32:13.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18370](https://issues.apache.org/jira/browse/SOLR-18370) - Remove ADDROLE/REMOVEROLE Overseer Roles API | `2026-09-08T00:17:28.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18343](https://issues.apache.org/jira/browse/SOLR-18343) - SolrJ: require an explicit HTTP method on admin requests; stop defaulting to GET | `2026-09-08T04:37:35.000+0000` | 8 | David Smiley | David Smiley reports reverting on 9.x after POST exposed auth-query and admin-path retry issues, with tests becoming flaky; candidate-only regression/design follow-up, not claimed. |
| [SOLR-18100](https://issues.apache.org/jira/browse/SOLR-18100) - Improve Combined Query Feature Documentation | `2026-09-08T12:35:18.000+0000` | 6 | David Smiley | David Smiley explains merged issues stay in “Closing” until release; release manager closes them in bulk. Settled guidance, no candidate. |
| [SOLR-18023](https://issues.apache.org/jira/browse/SOLR-18023) - Field type that has key-value pairs as field value (i.e.: mapping field) | `2026-09-08T00:17:53.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18000](https://issues.apache.org/jira/browse/SOLR-18000) - HuggingFace Embedding Failure Due to Deprecated Endpoint in langchain4j | `2026-09-08T07:09:52.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17999](https://issues.apache.org/jira/browse/SOLR-17999) - LLM Model Store Not Initialized Unless QParser Is Declared | `2026-09-08T07:07:52.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17781](https://issues.apache.org/jira/browse/SOLR-17781) - Add support for binary quantized dense vectors | `2026-09-08T18:47:13.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-15781](https://issues.apache.org/jira/browse/SOLR-15781) - Make v2 APIs more REST-ful and migrate to JAX-RS | `2026-09-08T23:36:51.000+0000` | 8 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
