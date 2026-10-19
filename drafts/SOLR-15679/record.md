# Skip record: SOLR-15679

- Ticket: SOLR-15679, "'Too many close' - error". JIRA status Open, resolution none, fix versions none, created 2021-10-06, updated 2021-10-19. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15679-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15679-vm2.md).

## Ticket reading

A SolrCloud node logs "ERROR ... SolrCore Too many close [count:-1] on org.apache.solr.core.SolrCore@..." and asks for the reason and a workaround. The report has no stack trace, no Solr version, no comments, and no steps to reproduce. The log text in the report ("Please report this exception to solr-user@lucene.apache.org") differs from the current message.

## Code reading

- solr/core/src/java/org/apache/solr/core/SolrCore.java:1721-1730 (origin/main 498a09aea97): SolrCore.close() decrements refCount. If the count goes below zero, it logs "Too many close [count:{}] on {}. Please report this exception to users@solr.apache.org" (line 1727) and runs `assert false : "Too many closes on SolrCore"` (line 1730). The count is -1 in the report, which means one more close than the core has references.
- The message text changed from the reporter's release (solr-user@lucene.apache.org to users@solr.apache.org), so the report is from an older line.
- The cause is an extra release by some caller. The release path that is over-counted is not identified by the report, and it is not established at the pinned head which caller does it.

Commands at the pinned head:
- git grep -n 'Too many close' origin/main -- solr/core/src/java: SolrCore.java:1727 only.
- git grep -n 'Too many close' origin/main — the report text: no match for the old address.

## History

- Commits on origin/main naming SOLR-15679: none.

## Reproduction attempts

Not attempted. The report gives no steps and no stack trace, so no test can be aimed at the over-counting caller. The assert in close() would fire in a test run with an extra close, but which caller to test is the open question.

## Gap analysis

The code path is clear: an extra close on a core reference count. The report does not say which caller releases the core one time too many, and the reporter's circumstances (node restart, reload, or a failed core creation) are not given. Finding the caller needs a stack trace from the error, which the report does not include.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing piece is the stack trace (or the log around the error) from an occurrence, which identifies the over-releasing caller. A close verdict needs that caller fixed or shown not to exist; a pr-needed verdict needs the caller and its fix.

## Limits

This record does not identify the over-releasing caller. It does not reproduce the extra close. It does not establish the reporter's Solr version.
