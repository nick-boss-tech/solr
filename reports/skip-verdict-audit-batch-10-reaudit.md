# Skip verdict re-audit, batch 10 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-10.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 19 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-8171 | keep-open (solr-8171-draft-open) | suspect | closeout claim; Jira read in the record; repro not run |
| SOLR-8158 | close-obsolete (solr-8158-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading) |
| SOLR-8117 | close-obsolete (solr-8117-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-8106 | keep-open (solr-8106-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-8098 | keep-open (solr-8098-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below) |
| SOLR-8089 | close-obsolete (solr-8089-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading) |
| SOLR-8087 | close-obsolete (solr-8087-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading) |
| SOLR-8079 | keep-open (solr-8079-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-8076 | close-obsolete (solr-8076-draft-close) | suspect | closeout claim; grep result in the record; the message text is absent at the pin (see notes) |
| SOLR-8024 | close-fixed (solr-8024-draft-close) | suspect | closeout claim; Jira read; repro not run |
| SOLR-7998 | keep-open (solr-7998-draft-open) | suspect | closeout claim; Jira read; repro on a current lsof not run |
| SOLR-7992 | keep-open (solr-7992-draft-open) | suspect | closeout claim; Jira read; code not read; repeated seeded runs named but not run |
| SOLR-7982 | keep-open (solr-7982-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below) |
| SOLR-7973 | close-obsolete (solr-7973-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-7953 | close-fixed (solr-7953-draft-close) | suspect | closeout claim; Jira read; repro not run |
| SOLR-7947 | keep-open (solr-7947-draft-open) | suspect | closeout claim; Jira read; missing piece named only in the B3 reading |
| SOLR-7867 | keep-open (solr-7867-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below) |
| SOLR-7846 | keep-open (solr-7846-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-7806 | keep-open (solr-7806-draft-open) | suspect | closeout claim; Jira read; missing piece not stated ("see the evidence below" has no evidence below) |

Tally: pass 0, suspect 19, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 19 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 19 Open with no resolution. Last-updated dates range from 2015 to 2020.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-8158, close-obsolete. The verdict holds: 67afcc228d (SOLR-16661) is an ancestor, and no `AnalyticsComponent` or `AnalyticsRequestParser` appears under `solr/core/src/java`.
- SOLR-8117, close-obsolete. The verdict holds: 321b4fa0de (SOLR-14930) is an ancestor, and `cloud/rule/` is absent at the pin.
- SOLR-8089, close-obsolete. The verdict holds: `git grep -l -i enablePositionIncrement -- '*.java'` is empty at the pin. The record says no removal commit was located; that gap should be stated.
- SOLR-8087, close-obsolete. The verdict holds: a37a213975 (SOLR-11812) is an ancestor, and `LeaderInitiatedRecovery` or `lirState` has no hits under `solr/core/src` or `solr/solrj/src`.
- SOLR-8076, close-obsolete. The verdict holds on the DIH removal: a57ba25400 is an ancestor. The message text "Unable to load Transformer" is absent from the Java source at the pin. Core still loads response transformers through `SolrConfig.java:443` (`TransformerFactory`); the ticket's `foo.FieldTransformer` is a DIH transformer, so the record is right, but it should name the grep and say why the core loader does not apply.
- SOLR-7973, close-obsolete. The verdict holds: 00e0870c4d (SOLR-18168) is an ancestor, and `JettyWebappTest` has no hits at the pin.
- SOLR-8024, close-fixed. The code path holds: f66aa9ed07 (SOLR-17649) is an ancestor, and `FacetField.java` routes numeric DocValues fields away from UIF at the cited lines (157, and the conditions near 90 and 162). Linkage is by code path.
- SOLR-7953, close-fixed. The code path holds: 2502af9f3f is an ancestor, and `BaseEditorialTransformer.java:61-63` checks `instanceof IndexableField`. Linkage is by code path; the commit does not cite SOLR-7953.
- SOLR-8171, keep-open. The code path holds: `FacetProcessor.java:431` calls `searcher.getDocSet(q, base)`, and `SolrIndexSearcher` keeps a filterCache. The lookups-versus-hits statistics mismatch is not checked; that is the missing piece, and it is not stated as a run.
- SOLR-8106, keep-open. The code path holds: `NumericStatsValues` keeps doubles (`StatsValuesFactory.java`, about lines 416-424). No pinned commit mentions the ticket. No run.
- SOLR-8098, keep-open. The record cites `ManagedIndexSchemaFactory.java:57-58, 109-140` and `ConfigSetService.java:300`. Both are present. The record says the schema rename on collection create was not located; that is the missing piece, and the record states it only as "see the evidence below".
- SOLR-8079, keep-open. The code path holds: `SearchHandler` reads `first-components` and `last-components` (`INIT_FIRST_COMPONENTS`, `INIT_LAST_COMPONENTS`). No build run.
- SOLR-7998, keep-open. The code path holds: `solr/bin/solr` gates the lsof call on `lsof -v | grep revision` at about lines 1381-1387. Not run against lsof 4.80.
- SOLR-7992, keep-open. One passing run; the test is flaky. The record names repeated seeded runs as the missing piece; not run.
- SOLR-7982, keep-open. `ZkController.java` reads coreNodeName from cluster state; the record names the next step (confirm `ClusterStateMutator.getAssignedCoreNodeName` and trace the core.properties writer). Neither is in the record.
- SOLR-7947, keep-open. The record's missing piece (logs or a repro) is named in the B3 reading, which is not in the record. The ref guide gap at `index-segments-merging.adoc:289-310` is cited.
- SOLR-7867, keep-open. The crash is inside Lucene's doc values reader; the record names the next step (repro with digit-leading multivalued docValues strings on an implicit router). Not in the record as a run.
- SOLR-7846, keep-open. `PropertiesUtil.substitute` throws on a missing property at the cited area. No build run.
- SOLR-7806, keep-open. The advertised host comes from `AddressUtils.getHostToAdvertise` (`solr.host` defaulting to localhost, `AddressUtils.java:34`), introduced by 3b2b1b7f79 (SOLR-16926). `CLIUtils.java:108` still uses localhost. The record says the decision is open; that is the missing piece.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
