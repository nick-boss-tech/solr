# Skip verdict re-audit, batch 05 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-05.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 20 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-9970 | keep-open (solr-9970-draft-open) | suspect | closeout claim; Jira read in the record; missing piece not stated ("see the evidence below" has no evidence) |
| SOLR-9965 | keep-open (solr-9965-draft-open) | suspect | closeout claim; Jira read; code not read; repeated runs at several seeds named but not run |
| SOLR-9942 | keep-open (solr-9942-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9925 | keep-open (solr-9925-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9913 | close-obsolete (solr-9913-draft-close) | suspect | closeout claim; grep command and result in the record (the record says it was not re-run) |
| SOLR-9872 | close-obsolete (solr-9872-draft-close) | suspect | closeout claim; grep command and result in the record |
| SOLR-9871 | keep-open (solr-9871-draft-open) | suspect | closeout claim; Jira read; the commit path cited is not a commit path (see notes) |
| SOLR-9866 | keep-open (solr-9866-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9830 | close-fixed (solr-9830-draft-close) | suspect | closeout claim; Jira read |
| SOLR-9826 | close-fixed (solr-9826-draft-close) | suspect | closeout claim; Jira read |
| SOLR-9825 | keep-open (solr-9825-draft-open) | suspect | closeout claim; Jira read; missing piece not stated for case 2 |
| SOLR-9809 | keep-open (solr-9809-draft-open) | suspect | closeout claim; Jira read; record misstates the code (see notes) |
| SOLR-9791 | keep-open (solr-9791-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9769 | keep-open (solr-9769-draft-open) | suspect | closeout claim; Jira read; repro on the pin not run |
| SOLR-9754 | close-obsolete (solr-9754-draft-close) | suspect | closeout claim; grep command and result in the record |
| SOLR-9749 | close-obsolete (solr-9749-draft-close) | suspect | closeout claim; Jira status now Resolved (Invalid), updated 2026-10-07; the record reads the B1 status |
| SOLR-9741 | close-obsolete (solr-9741-draft-close) | suspect | closeout claim; grep command and result in the record |
| SOLR-9723 | keep-open (solr-9723-draft-open) | suspect | closeout claim; Jira read; missing piece not stated |
| SOLR-9713 | keep-open (solr-9713-draft-open) | suspect | closeout claim; Jira read; the B3 missing piece is not stated in the record |
| SOLR-9677 | close-fixed (solr-9677-draft-close) | suspect | closeout claim; Jira read |

Tally: pass 0, suspect 20, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it. The 20 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all Open with no resolution, except SOLR-9749 (Resolved, Invalid, updated 2026-10-07). Last-updated dates are 2019-06-08 for most tickets, and 2020 for SOLR-9830, SOLR-9769, and SOLR-9723.

Code checks at the pin, with the commit ancestry checked by `git merge-base --is-ancestor`:

- SOLR-9913, close-obsolete. The verdict holds: a37a213975 (SOLR-11812) is an ancestor, and `git grep -i leaderinitiatedrecovery -- '*.java'` is empty. The record must state that grep and its result.
- SOLR-9872, close-obsolete. The verdict holds: `solr/core/src/java/org/apache/solr/update/TransactionLog.java` has no `setLength` at the pin. The record must state the grep.
- SOLR-9826, close-fixed. The verdict holds: 9d8a2faf18 and a37a213975 are ancestors; `UpdateShardHandler.java` close() reads "do not interrupt" at about line 225.
- SOLR-9830, close-fixed. The code path holds: bf7b438f12 (SOLR-15029) is an ancestor; `RequestHandlerBase.java:330` checks the tragic exception and `DirectUpdateHandler2.java:430-433` handles the error. The linkage is by commit title (SOLR-15029 is about tragedy leader election, not this ticket); the record says so.
- SOLR-9825, keep-open. Case 1 is covered by 8d28bbc905 (ancestor, `DirectUpdateHandler2.java:412-418`). Case 2 (ZK-related 400s) has no repro or trace; that is the missing piece, and the record does not state it.
- SOLR-9809, keep-open. The record says `createFields` (TrieField.java:666-670) "adds it unconditionally". The code at the pin guards the call with `if (sf.hasDocValues())` at line 667, so the record misstates the path. The keep-open verdict may still stand, but the record must be corrected.
- SOLR-9871, keep-open. `SolrCore.getIndexDir` synchronizes on `searcherLock` (`SolrCore.java:386-388`), and `registerSearcher` holds `searcherLock` (`SolrCore.java:2796`). The record's commit path names `SolrSnapshotMetaDataManager.java:237`, which is `isSnapshotted`, not a commit path; the trace from commit is not shown. The record must name the commit path or the repro.
- SOLR-9866, keep-open. `ExpandComponent.java:234` sizes the collapsed set from docList. The missing piece (an allocation profile on current code) is named in the code reading, not in the gap analysis.
- SOLR-9791, keep-open. `RestoreCmd.java` async tracker is present (`CollectionHandlingUtils.asyncRequestTracker`); the missing repro is not stated in the record.
- SOLR-9769, keep-open. `solr/bin/solr` prints "No process found" and exits 1 at about line 1030-1033. The record's repro is missing.
- SOLR-9754, close-obsolete. The verdict holds: ccd5ede68b (SOLR-14410) is an ancestor, and `solr/bin/init.d/solr` is absent at the pin. The record must state the grep.
- SOLR-9749, close-obsolete. The verdict holds on the code: 94e9dfe356 (SOLR-17853) is an ancestor, and `ImplicitPlugins.json` has no `/admin/plugins` at the pin. The Jira status changed to Resolved (Invalid) on 2026-10-07, which the record does not reflect; the record must cite this pass's Jira read and the status.
- SOLR-9741, close-obsolete. The verdict holds: 07f6549972 (SOLR-17140) is an ancestor and `SolrQueryTimeoutImpl` has no hits under `solr/core/src/java`. The record must state the grep.
- SOLR-9723, keep-open. The missing piece (a repro or the full stack trace) is not stated in the record ("see the evidence below").
- SOLR-9713, keep-open. The record points to the B3 reading for the missing piece, which is not in the record. The code cited (`solr/bin/solr.cmd`, `EnvSSLCredentialProvider.java:44`) is present.
- SOLR-9677, close-fixed. The verdict holds on the code: 9481c1f623 (SOLR-5163) is an ancestor, and `ExtendedDismaxQParser.java` validates query fields and throws `SyntaxError` at the pin (lines 223-229).
- SOLR-9970, SOLR-9942, SOLR-9925, SOLR-9965: the keep-open records do not state their missing pieces. SOLR-9965 rests on one passing run, which is not a reproduction of a flaky test; the record names repeated runs at several seeds as the missing piece, and does not run them. The code cited for SOLR-9942 (`MoreLikeThisHandler.java:118-127`) is present.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
