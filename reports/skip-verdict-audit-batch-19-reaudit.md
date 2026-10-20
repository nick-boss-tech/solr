# Skip verdict re-audit, batch 19 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-19.md`. Auditor: vm5 (not the excluded host, vm2). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 12 tickets with `skip-ticket-evidence`.

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-3742 | keep-open (solr-3742-draft-open) | suspect | closeout claim; Jira read in the record; missing piece (repro of group.func with group.facet) named only in the B3 reading |
| SOLR-3741 | close-fixed (solr-3741-draft-close) | suspect | closeout claim; the fix commit 5e587ab4d1 is SOLR-2649 (MM), not a fix for this ticket (see notes) |
| SOLR-3740 | close-fixed (solr-3740-draft-close) | suspect | closeout claim; same fix commit, which is SOLR-2649 (MM), not this ticket (see notes) |
| SOLR-3739 | close-fixed (solr-3739-draft-close) | suspect | closeout claim; the record says 5e587ab4d1 is a feature commit, not a bug fix; close-fixed linkage not established |
| SOLR-3721 | keep-open (solr-3721-draft-open) | suspect | closeout claim; Jira read; the record's "resolved" skip note is wrong (Jira is Open); attached logs not reviewed |
| SOLR-3716 | keep-open (solr-3716-draft-open) | suspect | closeout claim; Jira read; missing piece (non-request threads: executors, recovery) named but not checked |
| SOLR-3692 | keep-open (solr-3692-draft-open) | suspect | closeout claim; Jira read; missing piece (the ticket's analyzer, schema, and docs) named but not reproduced |
| SOLR-3605 | keep-open (solr-3605-draft-open) | suspect | closeout claim; Jira read; missing piece (a test of the group.main javabin round trip) named but not run |
| SOLR-3576 | keep-open (solr-3576-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-3523 | keep-open (solr-3523-draft-open) | suspect | closeout claim; Jira read; repro not run |
| SOLR-3452 | keep-open (solr-3452-draft-open) | suspect | closeout claim; Jira read; missing piece (the distributed-merge-to-text output path) named but not traced |
| SOLR-3380 | keep-open (solr-3380-draft-open) | suspect | closeout claim; Jira read; the umbrella ticket names no code path; the missing piece is not stated |

Tally: pass 0, suspect 12, deferred 0. Every item lacks a per-ticket closeout claim (no `claims/skip-<KEY>-*.md` exists), and every record says JIRA was not re-read for it or cites a reading from a prior round. The 12 are added to `material/skip-audit/remediation-current.json` as open in the same push.

## Per-item notes

Jira status, read 2026-10-20: all 12 Open with no resolution. Last-updated dates are 2012 for most, with SOLR-3741, SOLR-3740, and SOLR-3739 updated 2018-04-09.

Code checks at the pin, with ancestry from `git merge-base --is-ancestor`:

- SOLR-3741, SOLR-3740, SOLR-3739, close-fixed. The record cites 5e587ab4d1 as the fix. That commit's subject is "SOLR-2649: MM ignored in edismax queries with operators" (2015-12-15, git-svn trunk r1720062). It is an ancestor of the pin and the constructor it touches (`ExtendedDismaxQParser.java:1010-1014`, the `q.op` default operator) is present. But the commit names SOLR-2649, not these tickets, and SOLR-8812 (3871439976) later reworked the same mm and operator behavior, citing SOLR-2649 as its cause. Linkage by code path to a commit about a different ticket is not enough for a close-fixed verdict on tickets from 2012. The SOLR-3739 record itself calls 5e587ab4d1 "a feature commit rather than a bug-fix commit". Each of the three verdicts must either name a fix commit that cites these tickets and is checked, or become keep-open with the reason; a behavior probe on the edismax q.op case would settle it.
- SOLR-3742, keep-open. `SimpleFacets.java:506-519` (group.facet branch) and `:792-796` (getGroupedCounts rejects requests without group.field) are the path; the 400 response is the behavior the B3 skip note reports. The record needs a run of group.func with group.facet.
- SOLR-3721, keep-open. 48ae881287 (SOLR-3721 fix in 2012) is an ancestor and fixed a join-interrupt double-recovery race; `DefaultSolrCoreState.java:379-386` (cancelRecovery) has no join. The skip note calls the ticket "resolved", but Jira is Open, and the record does not correct that. The attached recovery logs were not reviewed.
- SOLR-3716, keep-open. `HttpSolrCall.java:297` and `CoreContainer.java:751` set the context class loader. The ticket asks for every thread Solr sees; executor and recovery threads are not verified.
- SOLR-3692, keep-open. `DefaultSolrHighlighter.java` uses Lucene TokenSources (about line 706); the "exceeds length" check is in Lucene. The ticket's analyzer and schema are not reproduced.
- SOLR-3605, keep-open. The javabin round trip for group.main with start>0 (`QueryComponent.java:330`) needs a test run, which the record names as missing.
- SOLR-3576, keep-open. `HttpShardHandlerFactory.java:100` sets the throughput default (accessPolicy false), read from fairnessPolicy at about line 266. No build run.
- SOLR-3523, keep-open. `SolrReturnFields.java:272` routes plain `fl=score` through `addField(..., false)`, and the score is special-cased at about line 512. No build run.
- SOLR-3452, keep-open. `DocsStreamer.java:201` and `JavaBinCodec.java:1288` handle byte[]; the distributed-merge-to-text path is not traced. No repro.
- SOLR-3380, keep-open. The umbrella ticket names no class, method, or test gap. The record's missing piece (a specific class or test gap to check) is not stated.

## Limits

This audit reads the records, the Jira reports, and the code at the pin. It runs no build or test, and it does not post to Jira or GitHub.
