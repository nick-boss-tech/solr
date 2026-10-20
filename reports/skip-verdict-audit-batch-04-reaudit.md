# Skip verdict re-audit, batch 04 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-04.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 14 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-10511 | close-fixed (solr-10511-draft-close, 09c333b1a9) | suspect | closeout claim with DONE line; Jira read in the record |
| SOLR-10497 | close-fixed (solr-10497-draft-close, cca8eec154) | suspect | closeout claim with DONE line; Jira read in the record |
| SOLR-10407 | close-obsolete (solr-10407-draft-close, 56aa5fe044) | suspect | closeout claim; empty grep at the pin (grep is not empty) |
| SOLR-10395 | keep-open (solr-10395-draft-open, 88f82743a3) | suspect | closeout claim; Jira read; missing piece named in the record |
| SOLR-10334 | keep-open (solr-10334-draft-open, a9e176624b) | suspect | closeout claim; Jira read; missing piece named in the record |
| SOLR-10326 | close-obsolete (solr-10326-draft-close, dd04b513f0) | suspect | closeout claim; grep command and result in the record; hits at the pin |
| SOLR-10325 | keep-open (solr-10325-draft-open, b53522cd00) | suspect | closeout claim; Jira read; missing piece named in the record |
| SOLR-10242 | keep-open (solr-10242-draft-open, a6b2b97a34) | suspect | closeout claim; Jira read; missing piece named in the record |
| SOLR-10201 | keep-open (solr-10201-draft-open, 71fa895db7) | suspect | closeout claim; Jira read in the record |
| SOLR-10110 | keep-open (solr-10110-draft-open, 1af887b3f8) | suspect | closeout claim; Jira read in the record |
| SOLR-10096 | close-answered (solr-10096-draft-close, 5732435401) | suspect | closeout claim; Jira read in this pass (the record used the B3 reading) |
| SOLR-10041 | close-obsolete (solr-10041-draft-close, 6389478bff) | suspect | closeout claim; grep command and empty result in the record |
| SOLR-10022 | close-obsolete (solr-10022-draft-close, 165dfab310) | suspect | verdict does not fit (see below); closeout claim; Jira read in the record |
| SOLR-10010 | keep-open (solr-10010-draft-open, 8af9f98f3c) | suspect | closeout claim; Jira read; missing piece named in the record |

Tally: pass 0, suspect 14, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it. The 14 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status for each ticket, read 2026-10-20: Open, no resolution, last updated 2019-06-08. Code claims checked at the pin are listed where they were checked.

- SOLR-10511, close-fixed. The fix commit in the record (69d0c70651) is an ancestor of the pin, and the guard `if (matchingIterations[0])` is present at `solr/webapp/web/js/angular/controllers/replication.js:108`. Linkage is by code path. The verdict holds on the code; the record needs the closeout claim and the Jira read.
- SOLR-10497, close-fixed. Commit 0dadf2c2a8 is an ancestor, and `solr/webapp/web/partials/index.html:215` has `track by $index`. The verdict holds on the code; the record needs the claim and the Jira read.
- SOLR-10407, close-obsolete. The verdict does not meet the rubric: the grep for `availableSubPaths` is not empty at the pin. Hits: `solr/solrj/src/java/org/apache/solr/common/util/PathTrie.java:181-182` and `solr/solr-ref-guide/modules/configuration-guide/pages/v2-api.adoc:94, :105, :127`. The ref guide still documents the output that 8efed05558 stopped emitting. A correct closing needs the guide fixed, or the verdict is keep-open.
- SOLR-10395, keep-open. The record says "see the evidence below", and there is no evidence below it. No attempt is named beyond "not attempted". The code line cited (hit count read, `SpellCheckCollator.java` in `solr/core/src/java/org/apache/solr/spelling/`, lines 165-171) is present. The missing piece must be named: the reporter's custom llparser and ngram field, as the reproduction input.
- SOLR-10334, keep-open. The missing piece is not named in the record. The only input is a report on Cloudera Solr 4.10.3, which is outside the fork. The timeout message at `CollectionsHandler.java:401` is present.
- SOLR-10326, close-obsolete. The grep is not empty at the pin: `solr/core/src/java/org/apache/solr/cluster/events/impl/CollectionsRepairEventListener.java:51` (a Javadoc comment) and `solr/solrj/src/test/org/apache/solr/common/cloud/SolrZkClientCompressedDataTest.java:62` (a JSON test fixture string). The record does not state the grep or its result. The rule for comment and fixture hits must be stated in the record, or the verdict changes.
- SOLR-10325, keep-open. The missing piece is not named ("see the evidence below"). The cited cleanup on failure is present at `solr/core/src/java/org/apache/solr/core/CoreContainer.java:1572-1575`. The ticket description is empty, so the intended scope is a maintainer question; the record should say so.
- SOLR-10242, keep-open. The missing piece is not named. The cited lines are correct for `solr/core/src/java/org/apache/solr/handler/RestoreCore.java`: `restore.` name at :102, `openNewSearcher` at :212. The record should name the repro: autoCommit with openSearcher=false and no soft commit, then a query after indexing.
- SOLR-10201, keep-open. The verdict holds: `solr/webapp/web/js/angular/app.js:391-393` sets the 10000 ms default, and `doNotTimeout` has no hits under `solr/webapp`. Attempts: none; the missing piece (a build reproduction with the ticket's replicationFactor input) is named in the record. The Jira read is still missing.
- SOLR-10110, keep-open. The verdict holds: `CommitTracker.java:280-283` logs and continues on commit failure, and `SolrCore.java:2438` throws "openNewSearcher called on closed core". No guard or fix commit was found. The missing piece is a reproduction on the pin; the Jira read is still missing.
- SOLR-10096, close-answered. The verdict holds: `solr/solr-ref-guide/modules/query-guide/pages/sql-query.adoc:305-306` says per-group COUNT(DISTINCT) is not supported. The record needs this pass's Jira read and the closeout claim.
- SOLR-10041, close-obsolete. The verdict holds on the code: commit f1ce5419ee (SOLR-11702, "Remove old LIR call in SolrCmdDistributor") is an ancestor of the pin, and `SolrCmdDistributor.java` has no LIR or recovery call at the pin. The record must state the grep command and its empty result; it says the grep was not re-run.
- SOLR-10022, close-obsolete. The verdict does not fit. The reported failure is a Lucene error during a full import after a schema fieldType change: "cannot change DocValues type from SORTED to NUMERIC for field". Removing DataImportHandler (a57ba25400, SOLR-14783) removes the reporter's path, not the schema field type change, and the DocValues check is still reachable through `DirectUpdateHandler2`. The close-obsolete rule ("the feature or code the ticket is about has been removed") is not met. A correct verdict must address the DocValues error on the current code, or be keep-open with that as the missing piece.
- SOLR-10010, keep-open. The missing piece is not named ("see the evidence below"). The cited code path is present: `IndexSchema.postReadInform` at `solr/core/src/java/org/apache/solr/schema/IndexSchema.java:662-665`, called from `ManagedIndexSchema.java` (lines 469, 521, 622, and others). The record should name the repro: an NGram tokenizer with a synonym filter on a managed schema.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
