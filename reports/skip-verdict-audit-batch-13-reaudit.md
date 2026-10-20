# Skip verdict re-audit, batch 13 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-13.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 13 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-6875 | keep-open (solr-6875-draft-open) | suspect | closeout claim; Jira read in the record; the missing piece (a cluster test showing replica count divergence) is named but not run |
| SOLR-6836 | keep-open (solr-6836-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below); no original vs unified run |
| SOLR-6828 | keep-open (solr-6828-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below); no pivot run |
| SOLR-6822 | close-fixed (solr-6822-draft-close) | suspect | closeout claim; Jira read; repro not run (code path verified) |
| SOLR-6811 | close-obsolete (solr-6811-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-6782 | close-obsolete (solr-6782-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-6756 | close-obsolete (solr-6756-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-6712 | keep-open (solr-6712-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below); no cloud run |
| SOLR-6678 | keep-open (solr-6678-draft-open) | suspect | closeout claim; Jira read; missing piece not stated (a heap-path repro); no run |
| SOLR-6588 | close-answered (solr-6588-draft-close) | suspect | closeout claim; Jira read in this pass (the record used the B3 reading) |
| SOLR-6582 | close-obsolete (solr-6582-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-6561 | close-obsolete (solr-6561-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-6534 | close-fixed (solr-6534-draft-close) | suspect | closeout claim; no fix commit in the record and none found at the pin, so the close-fixed minimum is not met (see notes) |

Tally: pass 0, suspect 13, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 13 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 13 Open with no resolution. Last-updated dates range from 2014 to 2024.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-6875, keep-open. The record names the mechanisms that are gone: StreamingSolrServers has no hits under `solr/core/src` (ef809a0f10 is an ancestor), and LIR was removed by a37a213975 (ancestor). The reported symptom is not tied to one path, so the verdict needs a cluster test, which is named and not run.
- SOLR-6836, keep-open. `DefaultSolrHighlighter.java:644` passes FRAGSIZE to the FVH path, and the default method is "unified" (`HighlightComponent.java:177`). The record names the missing piece (run the query with hl.method=original and hl.method=unified) as "see the evidence below"; it is not run.
- SOLR-6828, keep-open. `PivotFacetProcessor.java` `processSingle` and `doPivots` are present. The missing piece (reproduce pivot.fields on a PathHierarchy field) is named only as "see the evidence below".
- SOLR-6822, close-fixed. The code path holds: 6396cb759f (SOLR-10830) is an ancestor, and `IndexSchema.java:582-590` rejects a `_root_` type that differs from the uniqueKey type. Linkage is by code path.
- SOLR-6811, close-obsolete. The verdict holds: 4a6d85790a (SOLR-6895) is an ancestor, and `CloserThread` has no hits under `solr`. The record must state the grep.
- SOLR-6782, close-obsolete. The verdict holds: 101296f606 (SOLR-12901) is an ancestor, and `PostingsSolrHighlighter` has no hits under `solr/core/src`. The record must state the grep.
- SOLR-6756, close-obsolete. The verdict holds: 04ad0955e7 (SOLR-12930) and b6562691c8 are ancestors, and `solr/cloud-dev/` does not exist at the pin. The record must state the grep.
- SOLR-6712, keep-open. `HighlightComponent.java:215-218` still calls `rb.resultIds.size()` with no null check. The missing piece (a group.query highlight run on a cloud collection) is named as "see the evidence below"; not run.
- SOLR-6678, keep-open. `SolrSuggester.java:116-128` closes only the lookup in the close hook. The missing piece (a heap-path repro showing the SolrCore reference through the dictionary) is not stated in the record.
- SOLR-6588, close-answered. The docs answer is present: `solr/solr-ref-guide/modules/indexing-guide/pages/partial-document-updates.adoc:164` says atomic updates are supported for real fields of child documents. The Jira status this pass is Open; the record used the B3 reading and must cite this pass.
- SOLR-6582, close-obsolete. The verdict holds: 1e77bdf062 (SOLR-17609) is an ancestor, and there are no `FileSystem.newInstance` hits under `solr`. The record must state the grep.
- SOLR-6561, close-obsolete. The verdict holds: the old `LBHttpSolrServer` is absent, and `LBSolrClient.java:123` is its replacement. The record must state the grep.
- SOLR-6534, close-fixed. The verdict is not supported. The record's evidence is the Spatial4j version pin (`gradle/libs.versions.toml:175, 406`, spatial4j 0.8) and a remark that the ticket records a Solr 5.0 fix. A close-fixed record needs a fix commit sha, an ancestor check, and the changed code at the pin. A subject search for SOLR-6534 and a `-S ccwRect` search under `solr/core/src/java` found no fix commit. The verdict should become keep-open with the missing piece named (the commit that fixed it, or a repro on the pin), or a fix commit must be found and checked.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
