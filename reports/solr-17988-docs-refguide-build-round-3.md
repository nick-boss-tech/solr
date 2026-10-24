# SOLR-17988 Ref Guide standalone build and link-check result (round 3)

- **Status: DONE.** The three requested tasks passed; this result applies to the retry only. The original assignment remains HANDED and unchanged.
- **Completed:** 2026-10-24T17:20:33Z (UTC).
- **Host:** `phone-agent-2`; Node.js 24.19.0, npm 11.17.0, OpenJDK 21.0.12.1.
- **Docs branch:** `solr-17988-docs`, fixed head `16d3005293d3c6a047277bc586b30e05b6b6ef11`. `git ls-remote` returned this SHA before and after the standalone clone and again after the run.
- **Standalone checkout:** `/workspace/solr-17988-docs-standalone-r3`, with its own `.git` directory and no objects alternates. The checked-out head matched the pinned SHA. The original linked checkout remained clean; no docs or source files were changed and the docs branch was not pushed.
- **Runner and lock:** detached `pool/bin/bg-steps.sh` invoking `pool/bin/sweep-run.sh` sequentially under `/workspace/tools/.gradle-phone-agent-2.lock`.

## Task results

| Task | Exit code | Result and log |
| --- | ---: | --- |
| `:solr:solr-ref-guide:buildLocalSite` | 0 | Build successful. Log: `/workspace/logs/solr-17988-docs-refguide-build-round-3/solr-17988-docs-refguide-build-round-3/buildLocalSite.log` |
| `:solr:solr-ref-guide:checkSiteLinks` | 0 | No broken links; 858 files parsed, with `errors: []` and `warnings: []`. Log: `/workspace/logs/solr-17988-docs-refguide-build-round-3/solr-17988-docs-refguide-build-round-3/checkSiteLinks.log` |
| `:solr:solr-ref-guide:checkCliDocsUpToDate` | 0 | Output: “CLI docs are up to date.” Log: `/workspace/logs/solr-17988-docs-refguide-build-round-3/solr-17988-docs-refguide-build-round-3/checkCliDocsUpToDate.log` |

The full step summary is `/workspace/logs/solr-17988-docs-refguide-build-round-3/solr-17988-docs-refguide-build-round-3/steps.tsv`; runner state is `done`. Resource headroom was 184,946 of 837,408 inodes free (22.09%) before cloning, 174,799 (20.87%) immediately before Gradle, and 134,948 (16.12%) at final verification, remaining above the 15% floor. The filesystem had 30 GB free at completion. No cleanup was performed.

The link-validator installation emitted an npm audit summary of 9 vulnerabilities (7 moderate, 2 high); it did not fail any task. No PR, Jira, or public GitHub text was posted. The new round-3 claim and terminal DONE record accompany this report; earlier claims, reports, and logs remain preserved.
