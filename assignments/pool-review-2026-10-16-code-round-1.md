# Pool assignment: 2026-10-16 review follow-ups, code and tests (round 1)

Status: CLAIMED

- Capability tags: implementation, gate.
- Intended host: any Linux host with the Gradle toolchain (vm1, vm2, or vm3). Claim under `claims/` before starting (WORKFLOW.md). The Windows host never claims this work.
- Posted by: Windows review lane (review-round coordinator), 2026-10-16, at the direction of Nick Shanin.

These items come from the review round of open apache/solr PRs by nick-boss-tech, cycle 2026-10-16. Each item gives the PR, the head reviewed, the claim, and the check. Most findings were read from code and not run. Each item starts by running its check. If the check does not reproduce the finding, record that in the deliverable and close the item. Do not change code to fit a finding that did not reproduce.

## Items

1. **SOLR-10424 (PR 5159): does the V2 update path lose the id-less UUID default?** Head `5b087b1a389`, fork branch `solr-10424-submit`. The finding says the V2 update API (`/api/update`, `/api/update/json`) reads the same JSON-docs set, so removing the flag drops the id-less UUID default, and no `json-docs-uuid` chain applies to V2. At the head, the `initParams` block that attaches that chain matches only `/update/json/docs` (`solr/server/solr/configsets/sample_techproducts_configs/conf/solrconfig.xml`, line 656). The identifier `_UPDATE_JSON_DOCS` does not appear at the head or on upstream/main, so the original citation is wrong. Check: write a focused test that posts an id-less JSON document to `/api/update/json`, and run it at the head and at upstream/main. Start from: `solr-10424-submit`. Output: `reports/2026-10-16-pr-batch/SOLR-10424-check.md`.

2. **SOLR-7390 (PR 5136): the existing elevation test may now fail.** Head `7463dd006a7`, fork branch `solr-7390-submit`. The upstream test `QueryElevationComponentTest.testMarker` sends `fl` with `[elev]` (around line 1074) and expects no marker. At the head, an unregistered bracket transformer in `fl` returns 400, which would fail that test. The default marker name is `elevated` (`QueryElevationComponent.java`, line 135), so `[elev]` is unregistered unless the test config overrides the name. That override is not yet checked. Check: run `QueryElevationComponentTest` at the head with the focused gate, and record the result. Start from: `solr-7390-submit`. Output: `reports/2026-10-16-pr-batch/SOLR-7390-check.md`.

3. **SOLR-6975 (PR 5135): does paging with replicas repeat or skip documents?** Head `c666155ed058`, fork branch `solr-6975-submit`. The cross-shard order uses the slice's replica list, which is shuffled per request. On a slice with replicas, the order can change between identical requests, so start and rows paging can repeat or skip documents. The focused test uses replicationFactor 1 and cannot see this. Check: add a test with replicationFactor 2 that pages a `_docid_` sort across at least two pages and asserts no repeats and no gaps, and run it at the head. Second check: the finding says the merge comparator is not transitive when `_docid_` is a leading sort key. Check that with a small unit test. Start from: `solr-6975-submit`. Output: `reports/2026-10-16-pr-batch/SOLR-6975-check.md`.

4. **SOLR-6759 (PR 5154): does the query-limit test reach the new finally block?** Head `680044f1d2a`, fork branch `solr-6759-submit`. The finding says the test may not reach the new `finally` block, and that its stated base result does not match the base code. The vm1 gate log stack trace recorded in the gate file settles part of it. Check: confirm the path the test takes at the head. If it does not reach the block, correct the test so that it does, and rerun it. Start from: `solr-6759-submit`. Output: `reports/2026-10-16-pr-batch/SOLR-6759-check.md`.

5. **SOLR-8939 (PR 5146): can a duplicate uniqueKey leave a null slot?** Head `1a4a312bb7ab`, fork branch `solr-8939-submit`. The finding, traced from code and not run, says that when two shards return the same uniqueKey, the response can hold a null slot. Check: write a focused test with the same uniqueKey on two shards, assert that the response has no null entry, and run it at the head. Second check: `ShardDocQueue.resultIds(int)` was replaced by an abstract `pageDocs`, which breaks protected subclasses without a changelog note. Search upstream for subclasses that override `resultIds`, and record the list. Start from: `solr-8939-submit`. Output: `reports/2026-10-16-pr-batch/SOLR-8939-check.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Item 1 fix shape | default: restore the id-less UUID default on the V2 update paths | if the check fails at the head, make the change on the head branch and add the test; if the fix needs a config or API change, stop and escalate to the main agent | Nick |
| Item 2 failing test | default: do not change the test expectation | if the test fails with 400, record the output and escalate to the main agent; the choice is between the two behaviors | Nick |
| Item 3 replica order fix | default: escalate | if the paging test fails, record the repeat or gap output and escalate; do not change the sort order without a decision | Nick |
| Item 4 fix target | default: change the test, not production code | if the test cannot reach the block without a production change, stop and escalate | main agent |
| Item 5 null slot fix | default: the smallest fix that keeps the response order | if the fix would change the response shape, stop and escalate | main agent |
| Item 5 API break | default: escalate | record the subclass list and escalate to the main agent; do not add a compatibility method without a decision | Nick |
| Commit history and changelog | decided: no history rewrites and no changelog edits in this assignment | none | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/2026-10-16-pr-batch/`: one check file per item, named as in its Output line. Each file gives the status (confirmed, refuted, escalated, or fixed), the command and its result, the commit SHA if code changed, and any gate job filed (path and head). Then `reports/2026-10-16-pr-batch/code-round-1-index.md` lists the five items with their status.

## Limits

- No PR, comment, JIRA or email. No change to any live PR title or body. No force push.
- Push fix commits only to the matching `solr-NNNN-submit` fork branch, fast-forward only.
- Commit as Nick Shanin with no trailers. No em dashes.
- Items 2 and 3 do not change production code without a decision from the main agent.
