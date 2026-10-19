# Skip record: SOLR-14201

- Ticket: SOLR-14201, "some SolrCore are not released after being removed". JIRA status Open, resolution none, fix versions none, created 2020-01-21, updated 2023-08-10. Links SOLR-10506 (Resolved). Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14201-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14201-vm2.md).

## Ticket reading

Cores that are removed from a SolrCloud node (a collection is deleted or reloaded) are not released: the classes stay loaded and memory grows. The reporter (vinhlh, 2020-01-22) gives a loop that creates and drops collections, and a later comment (cpoerschke, 2020-01-27) asks whether the optimize and alias steps matter; the reporter (2020-01-28) finds the loaded classes keep increasing even without them. The ticket asks for a new investigation on the latest 8.x release. No commit names it.

## Code reading

- The ticket is an investigation of core reference counts and class loading. The core close path (SolrCore.close, which logs "Too many close" when the count goes negative, SolrCore.java:1721-1730) was read for a related ticket (SOLR-15679); it is the place where a core's references are released.
- Whether every path that removes a core drops its last reference (including the classloader for the core's plugins) was not traced in this pass.

Commands at the pinned head:
- git log --oneline --grep='SOLR-14201' origin/main: no output.
- git grep -n 'Too many close' origin/main -- solr/core/src/java: SolrCore.java:1727 (SOLR-15679 read).

## History

- Commits on origin/main naming SOLR-14201: none.
- The related issue SOLR-10506 (resolved) was not traced; the ticket says its fix was in 6.6.6 and 7.0.

## Reproduction attempts

Not run. The reporter's loop creates and drops collections over time and watches the loaded classes; that is a cluster run of many minutes, and the ticket asks for the first step to be a reproduction on the latest release. It was not set up in this pass.

## Gap analysis

The question is whether removed cores release their references and classes on the current release. The release-path code was not traced end to end, and no reproduction on the pin was run. Without the class-count result on the pin, the ticket can neither be closed nor fixed.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket. close-cannot-reproduce does not hold: no reproduction was run and the release path was not traced. The missing piece is a reproduction on the pin (the reporter's create-and-drop loop with a loaded-class count over time), then a trace of the release path if the count grows.

## Limits

This record does not reproduce the leak. It does not trace the core release path end to end. It does not check SOLR-10506.
