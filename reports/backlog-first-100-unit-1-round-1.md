# Unit 1 report: closure-not-a-bug, records 1 to 25 (vm5, round 1, 2026-10-26)

Status: PARTIAL. No drafts filled. The 105 ticket-text records have not been code-checked; a 5-ticket sample was checked (below). Decision needed from the owner (rung 5).

## What was screened

Source: latest H1 revision per batch (`material/harmonized-backlog/batches/h1-*.json`, 16 batches). Disposition `closure-not-a-bug`: 127 records. Excluded by pack rule 2: 2 conflict-flagged. Remaining eligible: 125. No record is Critical or Blocker; all are Bug type, Major priority.

Basis per record, from the audit evidence in `queue-current.json` (archive branch `archive/coordination-2026-10-23`, `material/skip-audit/`):

| Basis | Count | Example |
|---|---|---|
| Audit-3 Tier 1 pass: ticket text only, and the audit says "needs maintainer call" | 105 | SOLR-1557 ("Basis is ticket text, not a fresh code repro") |
| Code or ruling cited in the audit | about 11 | SOLR-1780 (`AddUpdateCommand.extractIdsIfNeeded` on upstream/main; "by design per Hoss") |
| Other: a reading from the ticket or a Hoss comment | 19 | SOLR-5376 (FC/FCS downgrade "by design") |

## The conflict

The family template says: "I looked at this against current main," and it requires `CODE_OR_DOC_CITATION` ("Not fillable without it"). Pack rule 1 says the basis may be "ticket text or a maintainer's ruling". Those two disagree. For 105 records the audit did not look at current main and said the call was still open, so the template's first-person claim would be false.

## What is fillable now

About 11 records have a code or ruling basis that the template can cite honestly, and some of the 19 "other" have a Hoss ruling. Those are not drafted yet and not live-checked. The unit would need 25 from this pool, so the shortfall is about 14 even with all of them.

## Decision for the owner

Choose one:
1. Keep the template as written. Then Unit 1 has about 11 to 20 fillable drafts and the rest must be dropped from the pack.
2. Change the template for the ticket-text basis: a closure that says "closing as not a bug on the ticket text and the existing maintainer discussion, with no fresh code check." Then all 125 are fillable after a live check. That changes what the comment says, so it's the owner's call.

## Sample: 5 ticket-text records, code read against apache/main `b73f03976fa`

Method: ticket text and comments read from JIRA REST (2026-10-26); code read in `solr/core/src/java/org/apache/solr` on main.

| Key | Ticket asks | What the code shows | Closable as not-a-bug? |
|---|---|---|---|
| SOLR-1557 | shards param: whitespace and multivalued | `HttpShardHandler.prepDistributed` reads `params.get(SHARDS)` (line 473); the split step was not located in this round | Inconclusive |
| SOLR-2420 | TV component response format | `TermVectorComponent` still builds a NamedList per doc (lines 134, 265): the format is as described | Yes, as a format-change request; cite line 134 |
| SOLR-2516 | stop caching searchers | Searchers are held and cached on `SolrCore` (`SolrCore.java`, `SolrIndexSearcher` line 614): the behavior is as described | Yes, as a design request; not a bug |
| SOLR-2855 | huge range facets exhaust memory (overflow was in the client) | No cap on bucket count found in `FacetRangeProcessor`/`FacetRange` (grep for max/limit/too many). Server-side unbounded allocation may still be there | No. Needs maintainer call before any closure |
| SOLR-3008 | edismax pf with a quoted subphrase "makes no sense" | `ExtendedDismaxQParser.java:308` drops phrase clauses from pf shingling on purpose | Partly: the behavior is intentional in code, but the ticket disputes the design. Maintainer call |

Reading: code reading is viable and gives a citable basis for about 2 of 5 (SOLR-2420, SOLR-2516, both as design requests). One (SOLR-2855) looks unsafe to close. One (SOLR-3008) is intentional in code but disputed. One is inconclusive. For the 105 pool, expect roughly 40% to be citable and the rest needing a maintainer call. This is a 5-ticket sample, not a rate.

## Second sample: 5 more ticket-text records, same method

| Key | Ticket asks | What the code shows | Closable as not-a-bug? |
|---|---|---|---|
| SOLR-3407 | `fl` with digit-leading names returns odd output | No digit-handling found in `ReturnFields` on a quick grep. A commenter ties it to SOLR-2444 (parsing side effect) | Inconclusive. Needs a fresh repro |
| SOLR-3473 | distributed dedup with non-uniqueKey signatureField | `SignatureUpdateProcessorFactory.java:92` and `:100` reject unsafe combinations in SolrCloud. The ticket's own latest comment says safe use needs those constraints | Yes, as "guarded by config check on main"; cite lines 92 and 100 |
| SOLR-3539 | rethink `softCommit`/`openSearcher` on commit | Both parameters exist (`UpdateRequestHandler.java:56`). The ticket is a design request | Yes, as a design request |
| SOLR-3676 | lowercased uniqueKey allows duplicates on update | The ulog keys updates by the indexed id (`UpdateLog.java:866`). Did not confirm the mismatch in the code path. A 2021 commenter reports it still happening | No. Looks like a possible real bug; needs a repro |
| SOLR-3721 | concurrent recoveries for the same shard | `ZkController.java:1516-1521` waits on a recovery future, which suggests a guard, but I did not confirm it covers this path. The last comment suggests close | Inconclusive |

Reading after 10 tickets: code gives a citable basis for about 4 of 10 (SOLR-2420, SOLR-2516, SOLR-3473, SOLR-3539), counting design requests as citable. Two look unsafe or possibly real (SOLR-2855, SOLR-3676), and SOLR-3008 is disputed. The rest are inconclusive. The sample is small, so the 40% estimate from round 1 holds only loosely.

## Not done

- No drafts filled, no live Jira checks run for this unit.
- Unit 2 (`closure-fixed`) not screened.
- Nothing posted. No Jira action.
