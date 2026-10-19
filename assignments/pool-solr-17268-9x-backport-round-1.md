# Pool assignment: SOLR-17268 9x backport, test sources and gate, round 1

Status: OPEN

- Capability tags: implementation, gate. Intended host: vm1 (Linux, builds allowed). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm2 gate coordinator, 2026-10-19, at the direction of the user.

Context: SOLR-17268 (JacksonJsonWriter does not support `json.nl=arrntv` with facets, a 500 on 9.3 to 9.6) has a 9x backport branch `solr-17268-9x-port` on the fork, tip `f5fdf1d53f`, base upstream `branch_9x` `3511867c3e`. Its main-code writer compiles on JDK 11 with `--release 11`. Its tests do not compile on the 9x base:

- `JacksonJsonWriterArrntvCloudTest.java` line 23: `InputStreamResponseParser` not found in 9x SolrJ.
- `JSONWriterTest.java` around lines 359 to 550: `write(OutputStream, SolrQueryRequest, SolrQueryResponse, String)` is not a 9x signature; 9x `JacksonJsonWriter.write` takes three arguments.
- `JSONWriterTest.java` line 379: `SolrQueryRequestBase` is abstract on 9x; the test instantiates it.

Separately, the 9x base test tree does not compile under `--release 11` because `CombinedQueryComponentTest.java` contains a Java 15 text block (added by SOLR-18522, PR apache/solr#5060). A candidate rewrite exists locally on branch `solr-18522-9x-textblock-fix` (not pushed). It only changes that one file.

## Items

1. **Adapt the 9x test sources.** Start from `solr-17268-9x-port` at `f5fdf1d53f`. Change only the three test-side API uses above so the tests compile on 9x. Do not change production code unless a test cannot compile without it; if so, stop and record. Output: a patch file `reports/SOLR-17268-9x-test-adapt.patch`, plus the commit hash of the adapted tree in a scratch worktree (not pushed).

2. **Apply the text-block rewrite for this run only.** Apply the change from `solr-18522-9x-textblock-fix` (the file `CombinedQueryComponentTest.java`) in the same scratch tree, so the test tree compiles on release 11. This is not part of the port; record it as such.

3. **Run the focused classes on JDK 11.** `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.response.JSONWriterTest --tests org.apache.solr.response.JacksonJsonWriterArrntvCloudTest` with `-Ptests.seed=17268C0FFEE17268`. Count from fresh XML. Also run `:solr:core:compileTestJava` and record its rc first. Output: `reports/SOLR-17268-9x-backport-round-1.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Push | decided: no push | record the patch and the scratch commit hash only; the user decides on any branch push | user |
| Production code | default: stop | if the tests cannot compile without a production change, record the change needed and stop | vm2 coordinator |
| Text-block rewrite | decided: test tree only, not part of the port | apply it only in the scratch tree for this run | vm2 coordinator |
| Failed test run | default: record, do not relaunch | a run that executes tests and fails is recorded with the test name and failure text, not retried | vm2 coordinator |
| Upstream PR | decided: no posting | no comment on PR 5060 or any upstream thread | user |

## Deliverable

`reports/SOLR-17268-9x-backport-round-1.md`: per item, the status, the patch path, the compile rc, the focused counts from fresh XML (JSONWriterTest and the cloud class), and any failure text verbatim. Then set this file's status to `Status: DONE <date>`.

## Limits

- No PR, comment, JIRA or email. No push to any fork branch.
- Commit only to coordination, as Nick Shanin, with no trailers and no em dashes.
