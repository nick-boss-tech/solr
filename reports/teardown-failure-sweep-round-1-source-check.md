# Teardown sweep round 1: source-name correction and closeout

Status: COMPLETE
Host: windows
Primary report: reports/teardown-failure-sweep-round-1.md, commit 56fed234eead9cc4d423b1c014aa28c129017256
Linux follow-up: assignments/teardown-client-interrupt-probe-round-1.md, commit ef54b9e843c69b62fa23ddbdfc06a00a7445849d

One source-name correction to P4 of the primary report: the nested class is
`CoreAdminHandler.CoreAdminAsyncTracker`, not `CoreAdminHandler.TaskProcessor`.
The linked file, line range 416-419, and sequential-executor observation are
correct. The class declaration is at line 362 of the same pinned M snapshot:
https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java#L362

Final evidence check computed the three full CI log SHA-256 values locally;
all match the provenance values printed in the primary report. No new log
download or test execution was needed.

The audit claim's report deliverable is present, so the Windows static claim is
complete. The Linux probe assignment is a separate OPEN unit with its own
claim, two-arm evidence requirements and cap. Its existence does not establish
any premise result. The audit did not change source or run Gradle or tests.

This add-only clarification preserves the original report and claim under the
board's records rules. No generated index, host capabilities or assignment was
rewritten.
