# SOLR-17988 Ref Guide build result

- Status: HANDED.
- Date: 2026-10-24, failure recorded at 2026-10-24T16:54:19Z (UTC).
- Host: `phone-agent-2`.
- Docs branch and pinned head: `origin/solr-17988-docs` at `16d3005293d3c6a047277bc586b30e05b6b6ef11`. The live docs tip remained at this SHA.
- Assignment source: `coordination-ui` at `2b6b8839eb2670578a8050e1ad01224dae7ec1dd` and later checked coordination tips specify `:solr:solr-ref-guide:buildLocalSite`, followed by `checkSiteLinks` and `checkCliDocsUpToDate`. The supplied run instructions instead named `:solr:solr-ref-guide:buildLocal`.

## Task results

| Task | Exit code | Result |
| --- | ---: | --- |
| `:solr:solr-ref-guide:buildLocalSite` (task named in the live assignment) | Not run | The supplied run instructions named the ambiguous prefix `buildLocal`; no alternate task was selected. |
| `:solr:solr-ref-guide:checkSiteLinks` | Not run | The detached runner stopped after the first step returned 1. |
| `:solr:solr-ref-guide:checkCliDocsUpToDate` | Not run | The detached runner stopped after the first step returned 1. |
| `:solr:solr-ref-guide:buildLocal` (task named in the supplied run instructions) | 1 | Gradle rejected the task name as ambiguous. |

The failure output was:

```text
Ambiguous matches
Cannot locate tasks that match ':solr:solr-ref-guide:buildLocal' as task 'buildLocal' is ambiguous in project ':solr:solr-ref-guide'. Candidates are: 'buildLocalAntoraPlaybookYaml', 'buildLocalAntoraSite', 'buildLocalAntoraYaml', 'buildLocalSite'.
BUILD FAILED in 42s
rc=1
```

No broken links were reported because `checkSiteLinks` did not run. The docs worktree remained clean, and no docs or source files were edited. No replacement task was run. This is handed back for resolution of the mismatch between the supplied run instructions and the live assignment.

## Logs

- Failed task log: `/workspace/logs/solr-17988-docs-refguide-build-round-1/solr-17988-docs-refguide-build-round-1/buildLocal.log`
- Step summary: `/workspace/logs/solr-17988-docs-refguide-build-round-1/solr-17988-docs-refguide-build-round-1/steps.tsv`
- Runner state: `/workspace/logs/solr-17988-docs-refguide-build-round-1/solr-17988-docs-refguide-build-round-1/state`
- Headroom log: `/workspace/logs/solr-17988-docs-refguide-build-round-1/solr-17988-docs-refguide-build-round-1/buildLocal.headroom.log`
