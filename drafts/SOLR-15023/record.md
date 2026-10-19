# Skip record: SOLR-15023

- Ticket: SOLR-15023, "Timeout Issue with Solr Metrics API". JIRA status Open, resolution none, fix versions none, created 2020-12-02, updated 2022-03-23. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15023-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15023-vm2.md).

## Ticket reading

On a SolrCloud cluster with about 200 collections and four nodes, the Admin UI's cloud page shows "Connection to Solr lost". The reporter traces it to the admin metrics API call, which takes about 20 seconds, and to a "AdminHandlerProxy timeout" warning in the logs. Repeated calls from the UI pile up. The report names SOLR-9812 and SOLR-4735 (both resolved) as related. One comment (2022-03-23). No test class is named.

## Code reading

- The proxy named in the report, AdminHandlerProxy, has no references in origin/main: `git grep -c AdminHandlerProxy origin/main -- solr/core/src` returns 0, and `git ls-tree -r --name-only origin/main | grep -i AdminHandlerProxy` returns nothing. The path history of the class file is empty.
- The metrics endpoint still exists: solr/core/src/java/org/apache/solr/handler/admin/MetricsHandler.java (its collection and disabled-metrics handling is at lines 127 and onward). Its per-request cost with many collections was not measured in this pass.

Commands at the pinned head:
- git grep -c 'AdminHandlerProxy' origin/main -- solr/core/src: 0.
- git ls-tree -r --name-only origin/main | grep -i AdminHandlerProxy: no output.

## History

- The removal of AdminHandlerProxy was not located. A pickaxe search (git log -S) and a deleted-file search (git log --diff-filter=D with a glob) both exceeded the time limit of this pass, so the commit that removed it is not identified.
- Commits on origin/main naming SOLR-15023: none.

## Reproduction attempts

Not attempted. The reported slowdown needs a cluster with about 200 collections and a timed call to the metrics endpoint, which was not set up in this pass.

## Gap analysis

The proxy the report blames is gone from main, but the metrics endpoint the UI calls still exists, and the 20-second call the reporter measured has not been reproduced. Whether the metrics endpoint is still slow with many collections, or whether the timeout was only in the removed proxy, is not established. The removal commit is also not identified.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-obsolete does not hold yet: the named proxy is absent, but the removal commit is not identified (the rubric's evidence for a removed class is the removal together with the empty grep), and the metrics endpoint the UI calls still exists. The missing pieces are (1) the removal commit for AdminHandlerProxy, and (2) a timed run of the metrics endpoint on a cluster with about 200 collections on the pinned head.

## Limits

This record does not identify the removal commit. It does not time the metrics endpoint. It does not establish whether the Admin UI still calls it with the same pattern.
