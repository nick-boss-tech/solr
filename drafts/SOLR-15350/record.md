# Skip record: SOLR-15350

- Ticket: SOLR-15350, fieldCache stats contain entries for other things (a regression). JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-04-16, updated 2021-06-23, no fix versions, no links, no comments.
- Verdict: close-obsolete.
- Draft branch: solr-15350-draft-close (the existing close draft; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree). Upstream sha not cited.
- Source: remediation redo of batch 03, item 12, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (vm2, from the B2 reading) cited a grep that was not re-run; this record re-runs it.

## Ticket reading

The ticket reports that `/solr/<core>/admin/mbeans?key=fieldCache&stats=true` returns, on 9x, stats for other MBeans (CORE.*, INDEX.*, SEARCHER.*), where 8x returned only the two fieldCache entries. The endpoint is the subject of the ticket.

## Grep at the pinned head

Each command was run from the pinned head and its result is recorded here:

- `git grep -n "admin/mbeans" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0. No Java file refers to the `admin/mbeans` path.
- `git grep -n "\"mbeans\"\|MBeansHandler" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0. No mbeans handler class or path name exists.

The replacement is the metrics handler: `git grep -l "class MetricsHandler\|admin/metrics" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` returns solr/core/src/java/org/apache/solr/handler/admin/MetricsHandler.java and solr/core/src/java/org/apache/solr/handler/admin/api/GetMetrics.java.

## Code reading

- solr/core/src/java/org/apache/solr/handler/admin/MetricsHandler.java: the metrics endpoint that replaces the mbeans endpoint at this head.
- solr/core/src/java/org/apache/solr/search/SolrFieldCacheBean.java:67-71: the fieldCache bean now records its own stats (`fieldCacheStats.info`, `totalSize`) into the metrics registry.
- solr/core/src/java/org/apache/solr/core/CoreContainer.java:953: the fieldCache bean is initialized with the core's metrics.

So the endpoint the ticket describes no longer exists, and the fieldCache stats are reported only through the metrics handler.

## History

- None cited in the earlier B2 reading. The commit that removed the mbeans endpoint was not identified in this pass; `git log -S "admin/mbeans"` was not run, so the removal commit is not cited.

## Reproduction attempts

Not attempted. The ticket's symptom is on an endpoint that is absent at this head, so no run can show the old output here.

## Gap analysis

close-obsolete rests on the removed endpoint, which the grep shows is absent. The fieldCache stats are still reported, through the metrics handler, so the ticket's underlying wish (fieldCache stats not mixed with other beans) is not tested by this verdict. That part is not shown either way at this head.

## Verdict justification

close-obsolete: the endpoint the ticket is about (`admin/mbeans`) has been removed, the grep at the pinned head finds no remaining use (the two empty results above), and the replacement is the metrics handler. The record does not claim the stats output through the metrics handler was checked for the same mixing.

## Limits

This record does not reproduce the mixed stats on any build. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
