# SOLR-17988 Ref Guide build and link-check retry

- Status: HANDED.
- Date: 2026-10-24; run completed at 2026-10-24T17:08:46Z (UTC).
- Host: `phone-agent-2`.
- Docs branch and pinned head: `solr-17988-docs` at `16d3005293d3c6a047277bc586b30e05b6b6ef11`. The local checkout and remote branch were both at this SHA immediately before the run and after it.
- Coordination: retry claim `claims/solr-17988-docs-refguide-build-round-2.md`; terminal handoff `claims/solr-17988-docs-refguide-build-round-2-handed.md`. The original assignment and first attempt remain unchanged. The original assignment was already `HANDED`; `WORKFLOW.md` makes assignment files immutable for workers, so the retry status is recorded in new claim and report files.

## Task results

| Task | Exit code | Result |
| --- | ---: | --- |
| `:solr:solr-ref-guide:buildLocalSite` | 1 | Failed in Antora task `:solr:solr-ref-guide:buildLocalAntoraSite`. The runner stopped after this failure. |
| `:solr:solr-ref-guide:checkSiteLinks` | Not run | The runner stopped after `buildLocalSite` returned 1. No broken-link list is available. |
| `:solr:solr-ref-guide:checkCliDocsUpToDate` | Not run | The runner stopped after `buildLocalSite` returned 1. |

The failure output identifies the Antora local content source as not being recognized as a Git repository at the checkout path; the `npx` process returned 1:

```text
{"level":"fatal","time":1792861713813,"name":"antora","hint":"Add the --stacktrace option to see the cause of the error.","msg":"Local content source must be a git repository: /workspace/solr-17988-docs-build"}
Execution failed for task ':solr:solr-ref-guide:buildLocalAntoraSite' (registered in build file 'solr/solr-ref-guide/build.gradle').
Process 'command \'/workspace/solr-17988-docs-build/solr/solr-ref-guide/.gradle/node/nodejs/node-v22.18.0-linux-x64/bin/npx\'' finished with non-zero exit value 1
BUILD FAILED in 40s
40 actionable tasks: 36 executed, 4 up-to-date
rc=1
```

No retry or substitute task was run. The docs checkout remained clean with no tracked diff, and no docs or source files were edited. No cleanup was performed. The host lock was free after the failed run.

## Preserved first attempt

The original claim `claims/solr-17988-docs-refguide-build-round-1.md` and report `reports/solr-17988-docs-refguide-build-round-1.md` remain unchanged. That attempt invoked the ambiguous prefix `:solr:solr-ref-guide:buildLocal`, which returned 1; `buildLocalSite`, `checkSiteLinks`, and `checkCliDocsUpToDate` were not run. Its log is `/workspace/logs/solr-17988-docs-refguide-build-round-1/solr-17988-docs-refguide-build-round-1/buildLocal.log`.

## Logs and resource guard

- Exact task log: `/workspace/logs/solr-17988-docs-refguide-build-round-2/solr-17988-docs-refguide-build-round-2/buildLocalSite.log`
- Per-task exit summary: `/workspace/logs/solr-17988-docs-refguide-build-round-2/solr-17988-docs-refguide-build-round-2/steps.tsv`
- Runner state: `/workspace/logs/solr-17988-docs-refguide-build-round-2/solr-17988-docs-refguide-build-round-2/state`
- Headroom log: `/workspace/logs/solr-17988-docs-refguide-build-round-2/solr-17988-docs-refguide-build-round-2/buildLocalSite.headroom.log`
- Step definition: `/workspace/logs/solr-17988-docs-refguide-build-round-2/solr-17988-docs-refguide-build-round-2/steps.txt`

The runner recorded 208,793 free inodes at start and 184,999 after the failed task, out of 837,408 total. Free space remained above the 15% floor. No files were cleaned.
