# Skip verdict re-audit, batch 07 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-07.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 15 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-9328 | keep-open (solr-9328-draft-open) | suspect | closeout claim; Jira read in the record; code not read; repeated seeded runs named but not run |
| SOLR-9312 | keep-open (solr-9312-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below) |
| SOLR-9267 | close-fixed (solr-9267-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-9250 | keep-open (solr-9250-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9248 | close-obsolete (solr-9248-draft-close) | suspect | closeout claim; grep command and result in the record (the record says the grep was from the C reading) |
| SOLR-9213 | keep-open (solr-9213-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9208 | keep-open (solr-9208-draft-open) | suspect | closeout claim; Jira read; repro on the pin not run (code path verified) |
| SOLR-9206 | keep-open (solr-9206-draft-open) | suspect | closeout claim; Jira read; repro on the pin not run (code path verified) |
| SOLR-9183 | close-obsolete (solr-9183-draft-close) | suspect | closeout claim; grep command and result in the record |
| SOLR-9173 | keep-open (solr-9173-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9144 | keep-open (solr-9144-draft-open) | suspect | closeout claim; Jira read; code not read; repeated seeded runs named but not run |
| SOLR-9126 | close-fixed (solr-9126-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-9122 | keep-open (solr-9122-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9094 | close-fixed (solr-9094-draft-close) | suspect | closeout claim; Jira read; no regression test for a non-score sort found, and not run |
| SOLR-9089 | keep-open (solr-9089-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |

Tally: pass 0, suspect 15, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it, or cites a reading from a prior round. The 15 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 15 Open with no resolution. Last-updated dates are 2016 or 2017 for most, and 2019-06-08 for SOLR-9267 and SOLR-9328.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-9267, close-fixed. The code path holds: 2b4e3dd941 is an ancestor, and `AbstractMLTQParser.java:120-122` calls `parseFieldBoosts` on the qf fields. Linkage is by code path.
- SOLR-9248, close-obsolete. The verdict holds: cf7f5079e8 and 31405c513d are ancestors, `git grep` for `GzipDecompressingEntity` and `DeflateDecompressingEntity` under `solr` is empty, and `HttpSolrClient.java` is abstract. The record must state the grep.
- SOLR-9183, close-obsolete. The verdict holds: 321b4fa0de is an ancestor, and no `*Snitch*` test file exists at the pin. The record must state the grep.
- SOLR-9126, close-fixed. The code path holds: fa9dfac8a7 is an ancestor, and `ExecutorUtil.shutdownAndAwaitTermination` is at the cited lines.
- SOLR-9094, close-fixed. The code path holds: 55bfadbce1 is an ancestor, and `ReRankCollector.java:136-138` populates scores only when sort is non-null. The record notes that no regression test was found and not run; that gap should be stated in the verdict.
- SOLR-9208, keep-open. The code path holds: `SolrCore.java:1766` iterates `closeHooks`, and `addCloseHook` is at line 1913. No build run; the repro is the missing piece, and it is not named.
- SOLR-9206, keep-open. The code path holds: `TermVectorComponent.java:249` dereferences `listAndSet.docList`, which the record's cited range (243-251) covers. No build run.
- SOLR-9173, keep-open. `UpdateLog.lookup` is at `UpdateLog.java:1285`. The missing piece (a repro of the recovery NPE) is named only as "see the evidence below".
- SOLR-9250, keep-open. `SolrQueryParserBase.getWildcardQuery` is at line 1275; the record cites 1277 (the same function). The missing piece is not stated.
- SOLR-9122, keep-open. `ExactStatsCache.java:236` and `:298` match the record's lines. The missing piece (repro for the stats gap) is not stated in the record.
- SOLR-9213, SOLR-9312, SOLR-9089: the keep-open records do not state their missing pieces. `CrossCollectionJoinQParser.java:37` is present for SOLR-9213.
- SOLR-9328, SOLR-9144: both rest on one passing run each. A single pass is not a reproduction of a flaky test; the record names repeated seeded runs as the missing piece and does not run them.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
