# Assignment: PR 5265 split-shard coverage and changelog follow-up, round 1

Status: OPEN

- Posted: 2026-10-29, by Nick Shanin at the owner's explicit request.
- Capability tags: implementation, review, records; Linux proof requires gate, review, records.
- Intended host: any available pool Linux host with implementation and gate capabilities, JDK 21, Git, and the pinned Solr build dependencies. Use that host's serialized Gradle lock and resource guards.
- Authority: Nick authorizes a scoped patch on the existing Nick-owned PR branch and a normal, non-forced push to update that branch. This does not authorize a live PR body edit, GitHub comment/review, or JIRA write. The suggested reply supplied in chat is not to be posted.
- Target: [PR 5265](https://github.com/apache/solr/pull/5265), SOLR-16398, titled `SOLR-16398: Migrate split shard v2 API to JAX-RS`.
- PR branch: `nick-boss-tech/solr:solr-16398-split-shard`.
- Reviewed PR head: `6c56a1823689af74ecef99caca12fa7f3f8990ad`.
- PR base: `9859d8bb2e8d63061116b8281639a4beb1be480c`; merge base: `74197140a666d4f0396b68484c2bf9df7e79221e`.
- Board snapshot used: `coordination-ui` at `a5cf3b3e3288802bc3007e2155674d9009c3e7fa`.
- Existing evidence: `receipts/SOLR-16398-split.md` records the original PR head and the initial branch tests. It is not a receipt for any new head. No assignment or claim for PR 5265 was found at this snapshot; the new assignment path was confirmed absent before posting.

## Owner clarification: keep SOLR-17241 separate

The non-hosting-node `wt`/JavaBin response issue is being tracked separately by Nick under SOLR-17241. Do not implement that shared response-writer/filter repair here, add a JavaBin non-hosting-node probe to PR 5265, or make this PR assignment depend on the SOLR-17241 gate.

At this board snapshot, `receipts/SOLR-17241.md` records Linux gate round 2 as **FAIL** at `7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e`: the filter change still reaches `JavaBinResponseWriter` with a null request on the non-hosting path. This assignment makes no claim that the issue is fixed. The generated OpenAPI client test in PR 5265 uses its typed JSON response parser; keep that proof scoped to JSON.

## Required corrections

### 1. Add focused regression coverage for the request body

The resource currently maps the request fields in `solr/core/src/java/org/apache/solr/handler/admin/api/SplitShard.java`. The replacement `SplitShardAPITest` exercises `numSubShards` through the generated `ShardsApi.SplitShard` client and checks a missing collection. The removed `V2ShardsAPIMappingTest` had assertions for the remaining message conversions.

Add focused assertions for every `SplitShardRequestBody` field reaching the correct resource/overseer-message boundary:

- `ranges`
- `splitKey`
- `numSubShards`
- `splitFuzz`
- `timing`
- `splitByPrefix`
- `followAliases`
- `splitMethod`
- `coreProperties`, including flattening with the `property.` prefix
- `async`, including the resulting asynchronous request identifier/handling
- `waitForFinalState`

Follow `dev-docs/apis-v2-conventions.adoc`: use the generated client and existing HTTP/cloud test infrastructure for the end-to-end API path. Do not revive the obsolete V1 `V2ApiMappingTest` harness. Where mutually exclusive split options cannot be sent together in a valid operation, add small, focused assertions at the closest useful resource-to-overseer-message boundary rather than sending an invalid combination to a live split. Search existing API tests and helpers before adding any fixture. The tests must detect a dropped or misnamed mapping; checking only that DTO setters exist is insufficient.

If an assertion reveals a real production mapping defect, fix only the affected mapping in `SplitShard.java`, record the evidence, and include that file in the exact-head gate. Keep the endpoint path, `@RequestBody` description, response type, permission, and v1 `/admin/collections?action=SPLITSHARD` behavior unchanged unless a demonstrated defect requires a separately justified change.

### 2. Narrow the changelog title

`changelog/unreleased/SOLR-16398.yml` currently says this PR migrates split-shard, move-replica, rebalance-leaders, and migrate-docs. This PR contains only split-shard. Change the title to describe only the v2 split-shard API migration. Keep the correct ICLA author (`Nick Shanin`) and avoid a `nick:` line.

## Worktree, branch, and ownership

1. Read the live PR, current remote branch head, and this assignment before claiming. Record a claim before source work. Use an isolated worktree based on the exact PR branch; do not use the dirty shared `source/` checkout.
2. Preserve the existing PR history. Commit with the ICLA identity and a ticket-based message such as `SOLR-16398: cover split-shard request mappings`; do not put `#5265` in the commit message.
3. Review the outbound diff for ticket scope, generated API compatibility, changelog author, and formatting. Push a normal fast-forward commit to `nick-boss-tech/solr:solr-16398-split-shard`; never force-push. Independently read back the remote ref and record the full SHA. If the PR branch moved, refresh and reconcile before pushing.
4. After the final SHA is known, post a separate immutable gate job under `gates/` that pins that full SHA. Claim and run that gate under the Linux host lock. Do not rely on the earlier branch receipt or GitHub `action_required` runs as proof for this new head.
5. Do not edit this assignment, `OPEN-WORK.md`, `WORKFLOW.md`, or the existing `receipts/SOLR-16398-split.md`. If the gate is green, the gating host adds a new, phase-specific receipt/report path after checking ownership. A failed or incomplete gate gets a failure/incomplete report, never a green receipt.

## Exact-head Linux proof

Run the repo wrapper tasks only on the assigned Linux host, after the detached tidy preflight and under the host build lock. Use fresh test output and record actual commands, UTC start/end, seed, exit codes, test counts, final branch SHA, JDK, host, and raw log/XML paths.

- Tidy and confirm the tree is clean.
- Error Prone compile and OpenAPI client generation: `:solr:api:compileJava :solr:solrj:compileJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Focused tests at the pushed head: `org.apache.solr.handler.admin.api.SplitShardAPITest` and `org.apache.solr.handler.admin.TestCollectionAPIs`, using seed `16398C0DE` and fresh JUnit XML.
- Module check at the same head: `:solr:core:check -x test`.
- Record `git diff --check` and verify that the pushed branch head is still the tested SHA.

If tidy changes the patch, commit and push the formatting repair, post a superseding exact-head gate job, and rerun proof at that head. Windows must not run Gradle or the retired local queue.

## Deliverables and limits

- Normal commit and remote read-back on `solr-16398-split-shard`.
- A phase-specific gate file pinned to the final full SHA, followed by a gate report and a new receipt only if the exact-head proof passes.
- `reports/pr-5265-split-shard-followup-round-1.md` with the findings addressed, exact file list, full new SHA, diffstat, test results, and remaining limits.
- No public GitHub comment, review submission, PR body edit, JIRA write, or new PR. The live PR had no comments or reviews at assignment preparation. If an acknowledgment becomes appropriate, prepare a draft with the final results and wait for Nick's explicit go-ahead before posting.

After the patch, the PR is **Implemented, Proof Pending** until the new exact-head Linux gate passes. SOLR-17241 remains a separately tracked issue and is neither fixed nor verified by this assignment.
