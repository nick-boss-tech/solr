# Skip verdict re-audit, batch 16 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-16.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 8 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-5435 | keep-open (solr-5435-draft-open) | suspect | closeout claim; Jira read in the record; the edismax parse of the reported query at this sha not run |
| SOLR-5383 | keep-open (solr-5383-draft-open) | suspect | closeout claim; Jira read; missing piece (Lucene TermGroupFacetCollector behavior, or a head repro) not stated as a run |
| SOLR-5376 | keep-open (solr-5376-draft-open) | suspect | closeout claim; Jira read; repro on the pin not run |
| SOLR-5252 | keep-open (solr-5252-draft-open) | suspect | closeout claim; Jira read; missing piece (unload then create of the same name) not stated as a run |
| SOLR-5046 | keep-open (solr-5046-draft-open) | suspect | closeout claim; Jira read; missing piece (Lucene TopDocs.merge behavior, and a repro) not stated as a run |
| SOLR-4851 | keep-open (solr-4851-draft-open) | suspect | closeout claim; Jira read; missing piece (Lucene TokenSources and a runtime repro) not stated as a run |
| SOLR-4733 | keep-open (solr-4733-draft-open) | suspect | closeout claim; Jira read; standalone rollback repro not run |
| SOLR-4686 | keep-open (solr-4686-draft-open) | suspect | closeout claim; Jira read; missing piece (Lucene HTMLStripCharFilter source at the Lucene version) not stated as a run |

Tally: pass 0, suspect 8, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 8 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 8 Open with no resolution. Last-updated dates range from 2013 to 2018, with SOLR-4733 updated 2015-06-08.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-5435, keep-open. `DisMaxQParser.java:232` references the extended dismax parser. The record says the parse path was not traced and the reported query was not run at this sha; that is the missing piece.
- SOLR-5383, keep-open. `SimpleFacets.java` `getGroupedCounts` (about line 775) and `getNumericHidingWrapper` (about line 836) are present. The record names the missing piece (whether Lucene's TermGroupFacetCollector enumerates precision-step terms, or a head repro with a multivalued trie field); not run.
- SOLR-5376, keep-open. `SimpleFacets.java:766-768` forces FC for multi-token fields after an explicit `facet.method`, including fcs (`FCS can't deal with multi token fields`). No run.
- SOLR-5252, keep-open. `UnloadDistributedZkTest.java` and `admin/api/UnloadCore.java` exist. No unload-then-create path is traced; the record names this as the missing piece.
- SOLR-5046, keep-open. `TopGroupsShardResponseProcessor.java` calls `TopDocs.merge` at about lines 215-218. `fillFields` has no hits under `solr/core/src/java`. The missing piece (Lucene `TopDocs.merge` behavior at the pinned Lucene version, and a repro) is named; not run.
- SOLR-4851, keep-open. `DefaultSolrHighlighter.java` imports `org.apache.lucene.search.highlight.TokenSources` at about line 52; the duplicate-token logic is in Lucene. The record names Lucene's TokenSources and a runtime repro as the missing piece; not run.
- SOLR-4733, keep-open. 93b33b5fdb (SOLR-4895) is an ancestor, and `DirectUpdateHandler2.java:971-973` rejects SolrCloud rollback. The standalone rollback path (rollbackWriter, about line 983) is the one to repro; not run.
- SOLR-4686, keep-open. `HTMLStripFieldUpdateProcessorFactory.java` imports Lucene's `HTMLStripCharFilter`, and there is no `lucene/` directory at the pin. The missing piece (Lucene's HTMLStripCharFilter and highlighter source at the Lucene version used) is named; the record does not state a run.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
