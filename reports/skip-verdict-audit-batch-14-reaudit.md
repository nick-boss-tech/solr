# Skip verdict re-audit, batch 14 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-14.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 16 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-6525 | keep-open (solr-6525-draft-open) | suspect | closeout claim; Jira read in the record; missing piece (current workload or latency measurement) not stated as a run |
| SOLR-6468 | close-obsolete (solr-6468-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the C reading) |
| SOLR-6429 | keep-open (solr-6429-draft-open) | suspect | closeout claim; Jira read; repro of unload during tlog replay not run |
| SOLR-6395 | keep-open (solr-6395-draft-open) | suspect | closeout claim; Jira read; runtime hang not reproduced |
| SOLR-6366 | close-fixed (solr-6366-draft-close) | suspect | closeout claim; no fix commit sha in the record (close-fixed minimum not met); repro not run |
| SOLR-6334 | keep-open (solr-6334-draft-open) | suspect | closeout claim; Jira read; missing piece (a repro on the pin) named in the B3 reading, not in the record |
| SOLR-6303 | close-obsolete (solr-6303-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading) |
| SOLR-6287 | keep-open (solr-6287-draft-open) | suspect | closeout claim; Jira read in the record; code not read; repeated seeded runs named but not run |
| SOLR-6255 | keep-open (solr-6255-draft-open) | suspect | closeout claim; Jira read; duplicate-key JSON parser behavior not traced |
| SOLR-6184 | keep-open (solr-6184-draft-open) | suspect | closeout claim; Jira read; current reproduction not run |
| SOLR-6166 | close-obsolete (solr-6166-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading) |
| SOLR-6142 | close-obsolete (solr-6142-draft-close) | suspect | closeout claim; grep result in the record (the record says it was from the B1 reading) |
| SOLR-5995 | keep-open (solr-5995-draft-open) | suspect | closeout claim; Jira read; missing piece (the attachments and a repro) not stated |
| SOLR-5990 | keep-open (solr-5990-draft-open) | suspect | closeout claim; Jira read; missing piece (current logs and settings) not stated |
| SOLR-5919 | keep-open (solr-5919-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-5878 | keep-open (solr-5878-draft-open) | suspect | closeout claim; Jira read; missing piece (a distributed test) not stated as a run |

Tally: pass 0, suspect 16, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 16 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 16 Open with no resolution. Last-updated dates range from 2014 to 2019 for most; SOLR-6184 was updated 2014-11-02.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-6525, keep-open. `Overseer.java` is present and the current queue processor is at the cited lines. The umbrella ticket has no workload or latency measurement; the record's missing piece is not run.
- SOLR-6468, close-obsolete. The verdict holds: 168df90d79 (SOLR-14497) and 513cefa5e9 (LUCENE-6044) are ancestors, and `enablePositionIncrements` has no hits under `solr/core/src/java` or `solr/core/src/test`. The record must state the grep.
- SOLR-6429, keep-open. be10d9e501 (SOLR-8260) is an ancestor. `CoreContainer.unload` is present, and `UpdateLog` waits for replay with no timeout at the cited range. The record names the repro (unload during tlog replay) and does not run it.
- SOLR-6395, keep-open. `solr/webapp/web/js/angular/controllers/cloud.js` builds the ZK subtree from `/` (the `buildZkSubtree('/')` call). The runtime hang is not reproduced. The record names a build run as the missing piece.
- SOLR-6366, close-fixed. The code path holds in part: `SpellCheckCollator.java` bounds collation attempts with `maxTries` and `maxNumberToIterate`. The record cites no fix commit, so the close-fixed minimum (a fix commit sha, an ancestor check, and the changed code) is not met. The verdict should name the commit that bounded the attempts and check it, or become keep-open.
- SOLR-6334, keep-open. `RecoveryStrategy.java` tries PeerSync and then replicate (about lines 687-721). The record says "see the code reading and notes" for the missing piece (a repro on the pin), and the B3 reading that names it is not in the record.
- SOLR-6303, close-obsolete. The verdict holds: a57ba25400 (SOLR-14783) is an ancestor, and `JdbcDataSource` has no hits under `solr/core/src`. The record must state the grep.
- SOLR-6287, keep-open. The record rests on one passing run, which is not a reproduction of a flaky test; repeated seeded runs are named and not run.
- SOLR-6255, keep-open. `DocumentBuilder.java:320` still emits "missing required field". The duplicate-key handling in the JSON parser is not traced, which the record admits.
- SOLR-6184, keep-open. `ReplicationHandler.java:1654` defines `commitReserveDuration`, and the ref guide documents it. The current reproduction is named and not run.
- SOLR-6166, close-obsolete. The verdict holds: 4a6d85790a (SOLR-6895) is an ancestor, and `HttpSolrServer.java` is absent (replaced by `HttpSolrClient.java`). The record must state the grep.
- SOLR-6142, close-obsolete. The verdict holds: no `pom.xml` is tracked at the pin (the build is Gradle). The record must state the `git ls-tree` check.
- SOLR-5995, keep-open. `DirectSolrSpellChecker.java` is present. The attachments and the repro are the missing piece, and the record does not state them.
- SOLR-5990, keep-open. `NodeConfig.java` exposes `coreLoadThreads` at the cited lines. Current logs and settings are the missing piece; the record does not state them.
- SOLR-5919, keep-open. `AbstractFullDistribZkTestBase.java` (solr/test-framework) still creates `control_collection` at about line 384. No run.
- SOLR-5878, keep-open. `QueryComponent.java` and `SimpleEndResultTransformer.java` are present at the cited lines. The record says whether this is a bug or a wiki error is a maintainer decision, and a distributed test is needed; the test is not run.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
