# Skip verdict re-audit, batch 12 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-12.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 11 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-7249 | keep-open (solr-7249-draft-open) | suspect | closeout claim; Jira read in the record; missing piece not stated ("see the evidence below" has no evidence below); the edismax query not run |
| SOLR-7221 | close-obsolete (solr-7221-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading) |
| SOLR-7192 | keep-open (solr-7192-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below); no timing run |
| SOLR-7153 | close-fixed (solr-7153-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-7088 | close-fixed (solr-7088-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-7009 | close-fixed (solr-7009-draft-close) | suspect | closeout claim; Jira read; the record cites a prior round |
| SOLR-6934 | keep-open (solr-6934-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below) |
| SOLR-6929 | keep-open (solr-6929-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below); no-space run not done |
| SOLR-6922 | keep-open (solr-6922-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below); the decision is for the owner |
| SOLR-6903 | close-fixed (solr-6903-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-6890 | close-obsolete (solr-6890-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |

Tally: pass 0, suspect 11, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 11 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 11 Open with no resolution. Last-updated dates are 2014 or 2015 for most; SOLR-7153 was updated 2016-08-16 and SOLR-6934 2024-11-21.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-7249, keep-open. The record cites commit 0eca10b618 (SOLR-18256) as the autoFixPureNegative change; it is an ancestor, and `ExtendedSolrQueryParser` extends `SolrQueryParser` at the cited area. The record names the next step (run the ticket query with defType=edismax at this sha); that is the missing piece and it is not run.
- SOLR-7221, close-obsolete. The verdict holds: 4a6d85790a (SOLR-6895) is an ancestor, and `ConcurrentUpdateSolrServer.java` is absent from the tree. The record must state the grep.
- SOLR-7192, keep-open. The record says the Hunspell stemmer comes from Lucene 10.4.0. The grep for "hunspell" under `solr/core/src/java` is empty, which agrees. The missing piece (time cs_CZ stemming on Lucene 10.4.0 against CzechStemFilter) is named only as "see the evidence below".
- SOLR-7153, close-fixed. The code path holds: f60f243b17 (SOLR-6781) is an ancestor, and `BBoxField.java:135-137` calls `registerSubFields` for dynamic fields. Linkage is by commit title and code path.
- SOLR-7088, close-fixed. The code path holds: cbf0eba176 is an ancestor, and `solr/bin/solr` sets `CDPATH=''` at line 87, before the `cd` calls.
- SOLR-7009, close-fixed. The code path holds: 04193d5252 (SOLR-14207) is an ancestor, and `XMLLoader.java` logs "body: {}" at the cited lines (137-143). The record cites a prior round.
- SOLR-6934, keep-open. The named `SolrCLI.SolrResponseHandler` has no hits under `solr/` (b4b28e52d7 is an ancestor). `StatusTool.waitToSeeSolrUp` is at `StatusTool.java:268`. The record names the next step (check whether a half-started node now fails the `-e cloud` run); not run.
- SOLR-6929, keep-open. `UnifiedSolrHighlighter.java` is the highlighter, and the only special-character test (`TestUnifiedSolrHighlighter.java:524-528`) uses "#" with spaces, which matches the record. The no-space case is not run.
- SOLR-6922, keep-open. The RPT type is present in the managed schemas (`_srpt` at `configsets/_default/conf/managed-schema.xml:160`). The record says the decision (whether the configsets meet the request) is open for the owner.
- SOLR-6903, close-fixed. The code path holds: 7053a99f49 (SOLR-8651) is an ancestor, and `SolrCmdDistributor.java:218-221` calls `uReq.setCommitWithin(cmd.commitWithin)` for deletes.
- SOLR-6890, close-obsolete. The verdict holds: c1062d9406 (SOLR-16781) is an ancestor, and `SolrConfig.java:935-937` logs "<lib/> entries no longer supported" and ignores them. The record must state the grep.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
