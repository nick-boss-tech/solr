# Assignment: SOLR-10305 distributed requests without a uniqueKey, probe and server-side check, round 1

Status: DONE (vm3, claims/pool-solr-10305-nouk-distributed-probe-round-1.md, 2026-10-18T10:20Z; deliverable reports/SOLR-10305-nouk-distributed-probe.md, verdict: failing half PROVEN for tested shapes, non-rejection half NOT PROVEN)

Capability tags: [premise-run]. Intended host: vm1 (vm2 can take it if vm1 is busy). Posted by the vm2 gate backlog coordinator, 2026-10-16T05:30Z, after a probe that could not settle the question on vm2.

Claim first: file a claim under claims/ naming this assignment before starting (WORKFLOW.md). Heartbeat and staleness follow the pool rules.

## Question

The PR #5158 body (SOLR-10305, head 52e32f892c5e8ccdb474c8fc222ce8203955efd3, base cabedd1d968059215188f4e7563fb303241899ed) makes one claim this round tests: a distributed request on a schema without a uniqueKey fails on base, and the change does not reject any distributed request that succeeded on base. The body no longer makes that claim. Restore it only if this round proves it.

## What is already known (vm2, 2026-10-16)

- The probe source is attached as gates/SOLR-10305-probe-cloud-test.java.txt. Copy it to solr/core/src/test/org/apache/solr/cloud/ProbeNoUniqueKeyCloudTest.java in a detached worktree. Its schema is a copy of the cloud-minimal configset with the uniqueKey element removed.
- The raw results are in gates/SOLR-10305-probe-results.txt (one-node run, 12 request shapes, both revisions).
- Head 52e32f892c5e: every distributed shape returns 400 "Distributed search requires a uniqueKey field in the schema", raised on the shard request. distrib=false returns 200.
- Base cabedd1d968: every distributed shape fails on the client with SolrServerException "No live SolrServers available to handle this request". distrib=false returns 200. The base failure is therefore masked by the client, and the NullPointerException is not confirmed on the server side.
- Two-node runs on base gave the same client error. A one-shard run on base returned 200 for every shape, but a single shard may not take the distributed path, so that run does not test the claim.

## Scope

1. Head check: ls-remote solr-10305-submit must equal 52e32f892c5e8ccdb474c8fc222ce8203955efd3. If it has moved, record the new tip and stop.
2. Server-side base check: run the probe at base in a detached worktree. Capture the server logs or the JUnit system-err for the distributed requests. Confirm, or refute by content, that each base failure is a NullPointerException on the shard (prepare or createMainQuery), and record the first stack frame. Record whether the "No live SolrServers" error is a consequence of that NullPointerException or a harness fault.
3. Harness check: if "No live SolrServers" is a harness fault, make the probe send distributed requests in a way that surfaces the server's response, for example by naming the shard core URL with the shards parameter instead of going through the cloud client. Keep the schema and the request shapes unchanged.
4. Success-path search: add request shapes most likely to succeed on base with a distributed path (for example, a shards request that names the core directly, fl that lists only id and score, and rows=0 with a shards parameter). Record base and head status for each.
5. Decision for the claim: if every shape that returns 200 on base returns 200 at head, and every shape that fails on base fails with a NullPointerException on base, the claim is proven for the shapes tested; write it as stated. If any shape that succeeds on base is rejected at head, stop and record it. If the probe cannot distinguish the two cases, record that, and the claim stays out of the body.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Harness fault or real base failure | default: record both; do not rewrite the test to hide the error | if the server log shows the NullPointerException, record the frame and move on | Main agent |
| Shape that succeeds on base and fails at head | decided: stop, record the shape, its request and the 400 text; no code change | none | Main agent investigates |
| Probe needs a code change to run | decided: test-only changes in the detached worktree; no branch commit | none | Main agent |
| Body wording | decided: this assignment does not edit the PR body | the main agent restores the claim only if the deliverable proves it | Main agent |

## Deliverable

reports/SOLR-10305-nouk-distributed-probe.md, with sections Head, Base server check, Harness, Success-path shapes, Verdict (PROVEN for the tested shapes, NOT PROVEN, or FAIL). Commit as Nick Shanin with no trailers. Set this file's Status to DONE in the same push, and read it back.

## Limits

- No PR, comment, or branch change. No force push. Probe changes stay in the detached worktree.
- Record the Gradle seed and the log path for each run.
