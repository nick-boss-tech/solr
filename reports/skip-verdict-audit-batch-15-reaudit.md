# Skip verdict re-audit, batch 15 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-15.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 16 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-5850 | close-obsolete (solr-5850-draft-close) | suspect | closeout claim; grep result in the record (the one hit at the pin is a configset comment, not code, and the record does not say so) |
| SOLR-5845 | keep-open (solr-5845-draft-open) | suspect | closeout claim; Jira read in the record; missing piece (a client-side repro with SolrJ or extract upload) named only in the B3 reading |
| SOLR-5833 | close-obsolete (solr-5833-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-5793 | keep-open (solr-5793-draft-open) | suspect | closeout claim; Jira read; code not read; repeated seeded runs named but not run |
| SOLR-5792 | keep-open (solr-5792-draft-open) | suspect | closeout claim; Jira read; code not read; repeated seeded runs named but not run |
| SOLR-5791 | keep-open (solr-5791-draft-open) | suspect | closeout claim; Jira read; code not read; repeated seeded runs named but not run |
| SOLR-5788 | keep-open (solr-5788-draft-open) | suspect | closeout claim; Jira read; missing piece (a current request or schema repro) not stated as a run |
| SOLR-5772 | keep-open (solr-5772-draft-open) | suspect | closeout claim; Jira read; missing piece (a repro of the duplicate) named only in the B3 reading |
| SOLR-5759 | keep-open (solr-5759-draft-open) | suspect | closeout claim; Jira read; missing piece (a self-contained repro with hl.method=original) not stated as a run |
| SOLR-5736 | keep-open (solr-5736-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-5717 | close-fixed (solr-5717-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-5712 | close-fixed (solr-5712-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-5579 | keep-open (solr-5579-draft-open) | suspect | closeout claim; Jira read; missing piece (a repro of a failed reload with no leader) not stated as a run |
| SOLR-5566 | keep-open (solr-5566-draft-open) | suspect | closeout claim; Jira read; missing piece (a shards highlighting repro) not stated as a run |
| SOLR-5559 | close-fixed (solr-5559-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-5508 | keep-open (solr-5508-draft-open) | suspect | closeout claim; Jira read; the commit that added the status assertion is not found |

Tally: pass 0, suspect 16, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 16 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 16 Open with no resolution. Last-updated dates range from 2013 to 2021.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-5850, close-obsolete. The verdict holds on the code: 4a6d85790a (SOLR-6895) is an ancestor, and `ConcurrentUpdateSolrServer` is not a class at the pin. The only `ConcurrentUpdateSolrServer` hit under `solr` is a Javadoc-style comment in `solr/server/solr/configsets/sample_techproducts_configs/conf/managed-schema.xml:42`. The record says "no hits under solr/", which is not exact; it should say the one hit is a configset comment. The successor `ConcurrentUpdateBaseSolrClient` is the place for a new report.
- SOLR-5845, keep-open. The code reading finds no server path (`git grep -i 'file lock'` finds only a Lucene lock-factory note). The B3 reading names the missing piece (a client-side file handle after a SolrJ or extract upload); the record defers to that reading, which is not in the record.
- SOLR-5833, close-obsolete. The verdict holds: `forceOverSeer` has no hits under `solr`, and `OverseerElectionContext.java:42` uses the ephemeral elected leader node. The record must state the grep.
- SOLR-5793, SOLR-5792, SOLR-5791, keep-open. Each rests on one passing run of a flaky test; the record names repeated seeded runs as the missing piece and does not run them.
- SOLR-5788, keep-open. `HttpSolrClient.java:226-262` builds remote exceptions from HTTP responses (`RemoteSolrException` at about line 226). The old message path is not established; a current request or schema repro is the missing piece, not run.
- SOLR-5772, keep-open. The ref guide `indexing-nested-documents.adoc` has no hits for uniqueKey or duplicate, which matches the record. The missing piece (a repro of the duplicate) is named only in the B3 reading, which is not in the record.
- SOLR-5759, keep-open. `HighlightComponent.java` keeps `original` selectable while defaulting to `unified` (about lines 59, 61, 177, 186). The record names the next step (verify with hl.method=original and the full analyzer and query); not run.
- SOLR-5736, keep-open. `QueryComponent.java:685` calls `mergeIds` and `mergeIds` is defined at line 971. The NPE trigger at the reported line is not checked, and no run was done.
- SOLR-5717, close-fixed. The code path holds: caf329717e (LUCENE-7701) is an ancestor, and `CommandHandler.java:189-200` uses the numeric group selector when `getNumberType() != null`. Linkage is by code path; no repro.
- SOLR-5712, close-fixed. The code path holds: 96e8392921 (SOLR-11770) is an ancestor, and `TermVectorComponent.java:291-292` guards the all-fields-empty branch. The commit does not cite SOLR-5712; linkage is by code path.
- SOLR-5579, keep-open. `Overseer.java` defines `/overseer/collection-queue-work` and `RecoveryStrategy.java` still calls `getLeaderRetry`. The missing piece (a repro of a failed reload with no leader on the rewritten Overseer) is named in the record; not run.
- SOLR-5566, keep-open. `HighlightComponent.finishStage` (lines 215-235) now calls `getAllHighlights`, which removes nulls first. `NamedList.java:91` still takes `Entry[]`. The record names a shards highlighting repro as the missing piece; not run.
- SOLR-5559, close-fixed. The code path holds: `SolrXmlConfig.java:243` rejects a `solr/cores` configuration (`failIfFound(root.get("cores").exists() ? "" : null, "solr/cores")`). Linkage is by code path; no commit named.
- SOLR-5508, keep-open. `AbstractFullDistribZkTestBase.java:467-473` asserts the createCollection status is 0. The record says the commit that added the assertion was not found (`git log -S` inconclusive), and that is the missing piece; it is not stated as a finding.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
