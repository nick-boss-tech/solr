# Assignment: solr-17988-docs-refguide-build-round-1 (Ref Guide build for the SOLR-17988 docs branch)

- Status: HANDED. Intended host: any pool Linux host with the `npm` tag (per its `hosts/` file; `hosts/phone-agent-2.md` lists it). Posted by vm2, 2026-10-24.
- Scope: one build and one link check on a fixed head. No code change, no doc change, no push, no PR, no Jira or GitHub text.

## Why

The docs branch `solr-17988-docs` (fork) assembles the SOLR-17988 upgrade-note and port-doc changes. The Ref Guide is built with Antora through npm, and vm2 does not list npm, so vm2 cannot run this step. The assembly and PR draft are done on vm2.

## Run

Head: `16d3005293d3c6a047277bc586b30e05b6b6ef11` on `origin/solr-17988-docs`. Check the fork tip first with `git ls-remote origin refs/heads/solr-17988-docs`. If the tip has moved, stop and record the new tip. Do not retarget.

From the repository root, at that head:

    JAVA_HOME=... GRADLE_USER_HOME=... ./gradlew :solr:solr-ref-guide:buildLocalSite :solr:solr-ref-guide:checkSiteLinks :solr:solr-ref-guide:checkCliDocsUpToDate

Run it under the host lock and detached, as the tick prompt requires (`pool/bin/sweep-run.sh` or `pool/bin/bg-steps.sh`).

## Expected

- The three tasks finish with exit code 0.
- `checkSiteLinks` reports no broken links in the pages this branch changes: `major-changes-in-solr-10.adoc`, `solr-control-script-reference.adoc`, `upgrading-a-solr-cluster.adoc`.

## Deliverable

A result section in `reports/solr-17988-docs-refguide-build-round-1.md`: the head, the host, the date, each task's exit code, the log path, and the broken-link list if any. Set this status line to DONE in the same push. If any task fails, set it to HANDED with the failing output, and do not change the docs.

## Limits

- Do not edit any file on the docs branch. A broken link goes in the result, for the author to fix in a new commit.
- Commit as Nick Shanin with no trailers. No em dashes.
