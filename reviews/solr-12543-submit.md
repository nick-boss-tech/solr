# solr-12543-submit

- Branch: origin/solr-12543-submit (the PR branch; ci/12543-exportwriter has the same solr/ code plus a fork-only workflow commit)
- Head: 355f9019bf5
- Base: upstream/main at 9d7cc2884e8 (merge-base 14c7aac0d15, 45 commits behind, 7 ahead)
- Scope: ExportHandler.java, TestExportWriter.java (updated), TestExportHandlerHttpStatus.java (new), changelog fragment. No fork-only files.
- Verdict: Close
- Reviewer: spot-check, 2026-10-07

## Findings (ranked)

- **MEDIUM (verified, compatibility):** Clients that read an /export error from a 200 body will now see a 400. The changelog says "fixed" and gives no upgrade note. Consider a "changed" entry or an upgrade note.
- **LOW (verified):** The sort and fl checks now run in ExportHandler and still run in ExportWriter, so two places enforce the same rule. Say in a comment which one is authoritative.
- **LOW (hypothesis):** `fl=` (empty value) passes the `get(FL) == null` check. Check how ExportWriter treats an empty fl.
- **Checked, OK:** the HTTP-level test covers missing sort, missing fl, and the local-param sort case. The local-param sort case is the key regression guard.
- **Housekeeping:** 45 commits behind upstream/main. Rebase before PR.

## Not checked

Nothing was compiled or run.
