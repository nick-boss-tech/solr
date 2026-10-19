# Pool assignment: SOLR-18555 module checks for SolrJ and the ref guide, plus SolrJ tests

Status: OPEN

- Capability tags: gate.
- Intended host: any Linux pool host (`hosts/pool-linux.md`). Windows does not claim it. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows review agent (solr-issues workspace), 2026-10-19, at the direction of Nick Shanin.

## Why

The phase r1 gate at head `b99259cba3572cc08586b2456c78780c814a2bf7` is GATE GREEN for `:solr:core` (receipt `receipts/SOLR-18555.md`). The gate spec for this ticket on this branch (`gates/SOLR-18555.md`) also names `:solr:solrj:check -x test` and `:solr:solr-ref-guide:check -x test`. Phase r1 ran neither, and no SolrJ tests have run at this head.

Both modules changed in draft PR 5183. `SolrRequest.java` in SolrJ adds a public `METHOD.QUERY` constant. `solr/solr-ref-guide/modules/query-guide/pages/highlighting.adoc` gained 52 lines.

This assignment runs those checks and the SolrJ test task at the same head. It does not change the ticket branch, the PR, or JIRA.

## Items

1. **Confirm the head.** Run `git ls-remote origin refs/heads/solr-18555-submit` from your checkout of the fork. It must print `b99259cba3572cc08586b2456c78780c814a2bf7`. Start from: the fork. Output: the printed line, copied into the receipt section (item 6).

2. **Make a detached worktree at the head.** In your own checkout, run `git worktree add --detach` with a path of your own, at `b99259cba3572cc08586b2456c78780c814a2bf7`. Do not create a branch, and do not commit to `solr-18555-submit`. Output: the worktree path and the output of `git rev-parse HEAD`, copied into the receipt section.

3. **SolrJ module check.** In the worktree, run `:solr:solrj:check -x test`. Take the host lock first (`flock -n /workspace/tools/.gradle-$(hostname -s).lock`), set JAVA_HOME to the local JDK 21 install, and record that path in the claim. Start from: the worktree. Output: the exit code and the log path.

4. **Ref guide check.** In the worktree, run `:solr:solr-ref-guide:check -x test`. This task includes `checkSiteLinks` and `checkCliDocsUpToDate`. Start from: the worktree. Output: the exit code and the log path.

5. **SolrJ tests.** In the worktree, under the same lock, run `:solr:solrj:test` once. Count results from the fresh JUnit XML for the whole task: tests, failures, errors, skipped, and the failing classes if any. Output: those counts and the XML directory path.

6. **Record the results.** Add a new section at the top of `receipts/SOLR-18555.md`, headed `## Module checks, SolrJ and ref guide, head b99259cba3 (pool-solr-18555-module-checks)`. Include the head line from item 1, the worktree HEAD from item 2, each item's result, and each log path. Do not edit the existing sections. Output: the receipt section on coordination.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Head moved | decided: stop | If `solr-18555-submit` is not at `b99259cba3572cc08586b2456c78780c814a2bf7`, write the new head into the receipt section, set this assignment to HANDED with that reason, and run nothing else. | Nick Shanin |
| Ticket branch and PR | decided: no change | Do not commit to, push, or force-push `solr-18555-submit`. Do not edit the PR, its body, its comments, or JIRA. | Nick Shanin |
| Format or tidy diff in a PR file | decided: record, do not apply | If Spotless, tidy, or any format check reports a diff in a file the PR changed, record the file list and the log path. Do not apply the diff. | Nick Shanin |
| Ref guide check fails (item 4) | default: compare with base | Run `:solr:solr-ref-guide:check -x test` once on base `c8ab71430f6e7547c773da596101d680862310ff` in a second detached worktree. If base fails the same way, record the failure as pre-existing. If base passes, record it as caused by the PR, with the task, the message and the log path. | Nick Shanin |
| SolrJ module check fails (item 3) | decided: record and continue | Record the task, the message and the log path. Items 4 and 5 still run. | Nick Shanin |
| SolrJ test fails with an assertion (item 5) | decided: record, do not rerun | Record the class, method, failure text, seed and fresh XML counts. Do not rerun and do not fix. | Nick Shanin |
| Cluster test fails before any assertion (item 5) | default: run the same command once more | If the second run also fails before an assertion, record the first exception and stop item 5. Treat it as harness setup, not a test result. | Nick Shanin (standing pool rule) |
| QUERY scope and authorization | decided: out of scope | Do not add or run any authorization test in this assignment. Nick decides the QUERY scope first. | Nick Shanin |

No row may leave the choice to the claimant.

## Deliverable

`receipts/SOLR-18555.md` with the item 6 section, and this file's Status set to DONE. Set the claim file to DONE in the same push as the deliverable. If the assignment stops on a decision row, set Status to HANDED with the reason instead.

## Limits

- No PR, comment, JIRA or email. No force push.
- No change to any ticket branch.
- Commit as Nick Shanin with no trailers. Commit messages name ticket keys only. No em dashes.
- Keep tokens and credentials out of logs and the receipt.
