# Skip verdict re-audit, batch 09 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-09.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 8 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-8681 | keep-open (solr-8681-draft-open) | suspect | closeout claim; Jira read in the record; missing piece not stated ("see the evidence below" has no evidence below); repro not run |
| SOLR-8635 | keep-open (solr-8635-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-8623 | close-fixed (solr-8623-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-8572 | keep-open (solr-8572-draft-open) | suspect | closeout claim; Jira read; the queue note ("resolved") is wrong: Jira is Open |
| SOLR-8469 | keep-open (solr-8469-draft-open) | suspect | closeout claim; Jira read; the IOException path the ticket names was not traced |
| SOLR-8463 | keep-open (solr-8463-draft-open) | suspect | closeout claim; Jira read; missing piece (the NPE stack and a shards.tolerant run) not stated as a run |
| SOLR-8365 | keep-open (solr-8365-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-8310 | keep-open (solr-8310-draft-open) | suspect | closeout claim; Jira read; the CentOS 5 lsof check is named but not run |

Tally: pass 0, suspect 8, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 8 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 8 Open with no resolution. Last-updated dates are 2015 or 2016 for most, 2018-07-25 for SOLR-8681, and 2020-09-09 for SOLR-8463.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-8681, keep-open. The code path holds: `solr/core/src/java/org/apache/solr/search/facet/FacetRangeMerger.java:43-46` `sortBuckets` keeps shard order and assumes every shard returns all buckets. The missing piece (a multi-shard repro with the 2012, 2013, 2014 buckets) is named only as "see the evidence below".
- SOLR-8635, keep-open. The record has no code path and no repro; the missing piece (the router type and the duplicate id setup) is named only as "see the evidence below".
- SOLR-8623, close-fixed. The verdict holds on the code path: 988a16fe95 (SOLR-15191) is an ancestor, and `FacetRangeProcessor.java` returns `EnumCalc` for `EnumFieldType` at the cited lines. Linkage is by commit title and code path; no repro run.
- SOLR-8572, keep-open. The record flags its own conflict: the queue note says the ticket is resolved, but Jira is Open. The record's code path (`RequestUtil.java:323`, `ObjectBuilder.fromJSONStrict`) is present; the missing piece (duplicate-key behavior in the noggit version used) is named in the code reading and is not run.
- SOLR-8469, keep-open. The code path holds: `HttpSolrCall.java` `sendRemoteProxy` sends "Error trying to proxy request for url" at about lines 633-641. The IOException and buffer path the ticket names is not traced, which the record admits.
- SOLR-8463, keep-open. The null guards hold: `FacetComponent.java:723-724` and `:985-986`, and 900363a9b6 is an ancestor. The record names the missing piece correctly (the NPE stack from the original report, and a shards.tolerant run on the head); no run.
- SOLR-8365, keep-open. The code path holds: `LogUpdateProcessorFactory.java:63-66` builds the processor only when info logging is enabled or a slow-update threshold is set. No build run.
- SOLR-8310, keep-open. The code path holds: `solr/bin/solr` gates the lsof call on `lsof -v | grep revision` at about lines 1381-1387, and 859415c7e1 is an ancestor. The CentOS 5 lsof check is named as the missing piece and was not run.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
