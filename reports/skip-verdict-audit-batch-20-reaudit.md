# Skip verdict re-audit, batch 20 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-20.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 12 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-3201 | close-obsolete (solr-3201-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading); Jira read in this pass not in the record |
| SOLR-3124 | keep-open (solr-3124-draft-open) | suspect | closeout claim; Jira read; repro of the explain output not run |
| SOLR-3064 | close-obsolete (solr-3064-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading) |
| SOLR-3050 | keep-open (solr-3050-draft-open) | suspect | closeout claim; Jira read; repro with int and float fields not run |
| SOLR-3034 | keep-open (solr-3034-draft-open) | suspect | closeout claim; Jira read; repro with replicateAfter optimize not run |
| SOLR-3027 | keep-open (solr-3027-draft-open) | suspect | closeout claim; Jira read; missing piece (a test run showing two plugin instances) named but not run |
| SOLR-3023 | keep-open (solr-3023-draft-open) | suspect | closeout claim; Jira read; missing piece (the partial-file state after disk full) named but not run |
| SOLR-2976 | close-fixed (solr-2976-draft-close) | suspect | closeout claim; Jira read; the cited fix commits do not cite the ticket and linkage is by code path; repro not run |
| SOLR-2865 | keep-open (solr-2865-draft-open) | suspect | closeout claim; Jira read; code not read; repeated seeded runs named but not run |
| SOLR-2845 | keep-open (solr-2845-draft-open) | suspect | closeout claim; Jira read; missing piece (a reproduction on current analyzers and a Solr-side path) not stated |
| SOLR-2751 | keep-open (solr-2751-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-2507 | keep-open (solr-2507-draft-open) | suspect | closeout claim; Jira read; the actual ParseException text (needs a parser run) named but not run |

Tally: pass 0, suspect 12, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 12 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 12 Open with no resolution. Last-updated dates are 2011 or 2012, except SOLR-3064 (2021-10-28) and SOLR-2976 (2013-07-04).

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-3201, close-obsolete. The verdict holds: a57ba25400 (SOLR-14783) is an ancestor, and `VariableResolver` has no hits under the Solr Java sources. The record must state the grep.
- SOLR-3124, keep-open. `DebugComponent.java:443` sets `parsedquery_toString` from the raw `query.toString()`, as the record says. No build run.
- SOLR-3064, close-obsolete. The verdict holds: 78862e7f54 (SOLR-18351) is an ancestor, and `ContentStreamUpdateRequest` has no hits under `solr`. The record must state the grep.
- SOLR-3050, keep-open. `DefaultSolrHighlighter.java` returns null for `TrieField` (the TODO cites LUCENE-3080), which matches the record. Point types are not covered. No run.
- SOLR-3034, keep-open. `ReplicationHandler.java:1353-1354` sets `replicateOnOptimize = !replicateOnCommit && replicateAfter.contains("optimize")`, so optimize is dropped when commit is set. No run.
- SOLR-3027, keep-open. Two instantiation paths are present: `SolrCore.loadSearchComponents` (about line 2005) and the plugin init path. The record says which path double-creates cannot be settled by reading; a test run is the missing piece.
- SOLR-3023, keep-open. `IndexFetcher` cleans up the temp index directory through `cleanup(...)` on failure. No disk-full handling is found; the partial-file state after a disk-full failure is the missing piece.
- SOLR-2976, close-fixed. The code path holds: 8fba601ed1 and 47541d03bc are both ancestors, and `TrieField.isTokenized()` returns false at `TrieField.java:311-313`. The commits do not cite SOLR-2976, so linkage is by code path. Repro not run.
- SOLR-2865, keep-open. The record rests on one passing run of a flaky test; repeated seeded runs are named as the missing piece and not run.
- SOLR-2845, keep-open. Synonym highlight term extraction is in Lucene's highlighter, outside this repo. The record names a reproduction on current analyzers as the missing piece; not run.
- SOLR-2751, keep-open. `TermsComponent.java:279-288` continues on a regex miss before the upper-bound break. No run.
- SOLR-2507, keep-open. The message "not allowed as first character" is absent from Solr source (no hits), and `SolrQueryParserBase.java:137` allows leading wildcards. The ParseException text needs a parser run, which the record names.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
