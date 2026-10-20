# Skip record: SOLR-12236

- Ticket: SOLR-12236, "Admin UI logging page: Core column never gets populated". JIRA status read 2026-10-20 (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: keep-open. This replaces the recorded close-fixed (batch 9 item 1, remediation item status open).
- Draft branch: solr-12236-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20 (UTC).

## Ticket reading

The logging page's Core column stays empty for log events from the techproducts example. The reporter's log line shows x:techproducts, but the UI column is blank. Comments: the reporter first saw the column empty on branch_7x, then saw x:techproducts on master, then could not reproduce on branch_7x with a clean checkout. The only patch comment changes a deprecated LogEvent call, and the reporter says there is no bug in LogEvent. No comment names a fixing commit.

## Code reading

- `solr/core/src/java/org/apache/solr/logging/log4j2/Log4j2Watcher.java:297-318` (`toSolrDocument`): copies every key of `event.getContextMap()` into the document, then sets `core` to "" only if no `core` key was copied. So a `core` value in the MDC context reaches the UI.
- `solr/core/src/java/org/apache/solr/logging/MDCLoggingContext.java:83` (`setCoreName`): puts `CORE_NAME_PROP` into MDC.
- `solr/core/src/java/org/apache/solr/core/SolrCore.java:1696`: `MDCLoggingContext.setCore(this)` is called on core use.
- `solr/core/src/java/org/apache/solr/core/CoreContainer.java:994` and `:1711`: `MDCLoggingContext.setCoreDescriptor(...)` on core load.
- Reading: on the pinned head the path that the ticket's log line takes (update request on the techproducts core, which calls setCore) should put the core name into the MDC context, and `toSolrDocument` should copy it. This is a code-path argument only. It does not show that the UI column is populated for that exact event.

## History

- `git log --oneline 498a09aea97 --grep=SOLR-12236` returns no commits.
- `git log -S'contextMap' -- solr/core/src/java/org/apache/solr/logging/log4j2/Log4j2Watcher.java` shows ec78ef7852 (entryset change, #868) and 624d128b5e (SOLR-7887, log4j2 upgrade). Neither names the ticket.
- `git log -- solr/core/src/java/org/apache/solr/logging/log4j2/Log4j2Watcher.java` shows 824bab96a7 (SOLR-18107, register parents) and 090f5b995c (Error Prone bump). Neither touches the Core column.
- No fixing commit exists on the pinned head, so the recorded close-fixed verdict has no commit to cite. That is why the verdict changes.

## Reproduction attempts

Not attempted in this pass. The ticket describes a testable symptom: an update request on the techproducts core that fails, then the log page's Core column. A reproduction needs a running node with the techproducts configset and a logged update error, read through the logging handler. Its command, seed and log path belong in the next pass.

## Gap analysis

The code path looks correct at the pinned head, so the column should be populated when the MDC has the core name. The reporter's second and third comments suggest the earlier blank column came from a different branch or setup, and the reporter could not reproduce it later. That leaves two explanations open: the bug was fixed by a change that the search above did not name, or the bug is environment-specific and does not reproduce on main. A run that separates them is the missing piece.

## Verdict justification

keep-open meets the rubric's keep-open line: the attempts made (code path and history) and the missing piece (a reproduction through the update error path, read via the logging handler at the pinned head). close-fixed is not supported, since no fixing commit is named or found. close-cannot-reproduce is not supported, since no reproduction was run.

## Limits

This record does not establish that the column is populated on main. It does not check branch_7x or 9x.
