# Pool assignment: SOLR-10424 V2 id-less update check, retry (round 1)

Status: OPEN

- Capability tags: implementation, gate.
- Intended host: vm2 (Linux, the lane that ran the first check). Claim under `claims/` before starting (WORKFLOW.md). The Windows host never claims this work.
- Posted by: Windows review lane (review-round coordinator), 2026-10-17, at the direction of Nick Shanin. This retries item 1 of `pool-review-2026-10-16-code-round-1`. That item stays INCONCLUSIVE until this retry lands.

## Why this retry

The first check (logs/vm2/10424-check-result.txt) failed both legs, upstream main 4b5b922383 and the head. Two tests failed in each leg. `testIdlessDocOnV2UpdateJsonGetsUuid` expected 1 document and got 0. `testIdlessDocOnUpdateJsonDocsGetsUuid` returned "Cannot find API for the path" for `/api/collections/tp/update/json/docs`. Upstream main defines V2 update endpoints only at `/update`, `/update/xml`, `/update/csv`, `/update/json`, and `/update/bin` (`solr/core/src/java/org/apache/solr/handler/admin/api/UpdateAPI.java`). There is no V2 `/update/json/docs`, so that request cannot resolve. The test is at fault, and a failure on upstream main says nothing about the head.

## Items

1. **Correct the two request paths.** Change the test only, in the detached scratch worktree the first check used. Do not commit it to the fork. (a) Legacy case: send the id-less JSON document to the legacy handler path `/update/json/docs`, not through `/api/collections`. (b) V2 case: send the same document to the V2 endpoint `/api/collections/tp/update/json`, the endpoint that `UpdateAPI` defines for JSON updates. The upstream test `V2UpdateAPIMappingTest` (`solr/core/src/test/org/apache/solr/handler/V2UpdateAPIMappingTest.java`) shows that a V2 `/update/json` request is rewritten to the legacy `/update/json/docs` path. So the two cases reach the same handler by different routes, and the check tests whether the UUID default survives that rewrite. Start from: the first check's worktree.

2. **Commit before counting.** The V2 case expected 1 document and got 0. Add `commit=true` to the V2 request, or issue an explicit commit, before the count. Record the HTTP status and the response body of each request. If a response is an error, record the error text. If it is 200 and the count is still 0, record that. Start from: the test from item 1.

3. **Run both legs.** Run the corrected focused test at upstream main `4b5b922383` and at the head `5b087b1a389` (fork branch `solr-10424-submit`). Use a detached worktree for each commit. Record the Gradle seed and the counts from fresh JUnit XML for each leg. Output: section "Two legs" in the check file.

4. **Write the verdict.** Write `reports/2026-10-16-pr-batch/SOLR-10424-check.md` with sections Requests, Two legs, and Verdict. The verdict is:
   - CONFIRMED: upstream main gives the V2 document a UUID, and the head does not.
   - REFUTED: the head gives the V2 document a UUID too.
   - INCONCLUSIVE: any other result, with the reason.
   Set this file's Status to DONE in the same push, commit the check file to `pr-prepare` as Nick Shanin with no trailers, and read it back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scope of the test change | decided: test only, in the scratch worktree. No production change and no commit to the fork in this retry | none | Nick |
| Gate | decided: focused test only, not a full gate | none | Nick |
| V2 case still gets 0 documents with commit=true | default: INCONCLUSIVE, with the responses recorded | escalate to the main agent | main agent |
| Head loses the default (CONFIRMED) | default: record it; no fix in this retry | escalate to the main agent for the fix shape | main agent |
| Upstream main also gets no UUID for the V2 case | default: INCONCLUSIVE, with the responses recorded | escalate to the main agent; the head-versus-upstream comparison cannot be made | main agent |

No row may leave the choice to the claimant.

## Deliverable

`reports/2026-10-16-pr-batch/SOLR-10424-check.md`, with sections Requests, Two legs, and Verdict. Each run gives its seed, log path, and counts from fresh JUnit XML. The Status line of this file is set to DONE in the same push as the check file.

## Limits

- No PR, comment, JIRA or email. No change to any live title or body. No force push. No commit to a fork branch.
- Commit as Nick Shanin with no trailers. No em dashes.
- Record the Gradle seed and the log path for each run.
