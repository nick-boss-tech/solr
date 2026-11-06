<!--
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements. See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

# Experiments for distributed cluster processing

This is an experiment specification for
[SOLR-17293](https://issues.apache.org/jira/browse/SOLR-17293), the umbrella
for decentralized cluster processing as the default. It contains no measured
results and proposes no default change. Use disposable Linux clusters for
the workloads below.

The questions are whether independent admin commands scale across receiving
nodes, whether hot resources starve unrelated work, and whether recovery
causes excessive contention on shared ZooKeeper state. Answer those before
changing executor admission, adding a dispatcher, or changing state layout.

## Source baseline and comparison

Pin the Apache Solr commit, JDK, ZooKeeper version, hardware and configuration
for each run. The initial source baseline is
`c0c219e374611e0edc475c0f793c9bd688b13292`. Compare fresh clusters with
`overseerEnabled=true` and `overseerEnabled=false`, each with PRS enabled and
disabled on the test collections. Set the mode before starting nodes. Do not
attempt a rolling mode switch: the two modes use different work queues and
async tracking. Test a fresh cluster for each cell of the matrix.

First establish correctness with the existing test infrastructure:

- `org.apache.solr.cloud.api.collections.CollectionApiLockingTest` covers
  hierarchical locks and reuse of a calling lock ID.
- `org.apache.solr.cloud.ConfigSetApiLockingTest` covers ConfigSet locks.
- `org.apache.solr.cloud.DistributedApiAsyncTrackerTest` covers request tracking.
- `org.apache.solr.cloud.TestRebalanceLeaders` exercises distributed async
  progress during leader rebalancing.
- `org.apache.solr.cluster.events.ClusterEventProducerTest` exercises a real
  cluster singleton consumer of the retained election lifecycle.

Confirm the class paths at the selected revision; do not treat a missing or
renamed class as a passing proof leg. Record the actual coordination mode in
the test logs as well as the intended setting.

## Workload matrix

For steady workloads use 1, 2, 4, 8, 16, 32 and 64 concurrent clients, target
one receiving node and then distribute requests across all receiving nodes.
Use the same collection topology, request sequence and client retry policy
in each mode. Separate warmup from measured intervals, run at least three
independent repetitions and retain individual request records.

| Workload | Procedure | Correctness and performance questions |
| --- | --- | --- |
| Independent collections | Precreate one collection per active client. Repeatedly issue RELOAD, with no concurrent operation on the same collection. | Does spreading requests across nodes improve accepted throughput? Are collection contents and replica states preserved? |
| One hot collection | All clients issue RELOAD on one collection. | Are conflicting operations ordered? How many executor threads wait for locks? Does ZooKeeper traffic grow with waiters? |
| Hot and independent mix | Direct 90 percent of operations to one collection and 10 percent to independent collections. Repeat at the same total offered rate. | Does hot-resource waiting consume every command worker? What happens to cold-resource p95/p99 latency and rejection rate? |
| Unknown shard target | Use SPLITSHARD with split.key while another request names the resolved shard; separately use DELETEREPLICA with count and no shard. | Establish conflict exclusion with deterministic test barriers before any timing run. A scheduler must protect the actual possible targets. |
| Source and target resources | ConfigSet CREATE from a base concurrent with base DELETE; routed-alias maintenance concurrent with direct backing-collection commands. | Audit existing lock infrastructure first. A second resource must not disappear while the command uses it. Do not assume action.lockLevel describes every resource. |
| Node-wide work | REPLACENODE or DELETENODE concurrent with collection and replica operations touching that node. | Check eventual topology and replica ownership. A new node barrier requires a demonstrated missing invariant and a nested-lock ordering design. |
| Recovery fan-in | Stop a node hosting many replicas, then restart it. Sweep collection count and replicas per collection. | Measure recovery duration, state znode versions, CAS retries and ZooKeeper operations. Compare PRS modes independently. |
| Admission overload | Hold a real collection write lock, submit async work until the executor rejects, then release the lock and retry the rejected ID. | Accepted work must reach a terminal result. Rejected IDs must remain reusable. Duplicate accepted IDs must be rejected. An HTTP timeout must not be counted as cancellation. |

RELOAD is a reproducible admin workload, not a substitute for SPLITSHARD or
recovery measurements. The latter involve storage and replication costs that
must be reported separately. For recovery, also retain document counts and
per-replica results after the cluster becomes healthy.

## Measurements

For every request retain these fields in CSV or JSON Lines:

`run_id, source_sha, coordination_mode, prs, scenario, receiver, request_id,
resource, offered_at, accepted_at, completed_at, http_status, terminal_status,
retry_of, error`

Use monotonic client time for durations. Keep wall-clock UTC only for
correlating client, server and ZooKeeper logs. Record submission latency
separately from end-to-end async completion latency; a returned request ID
does not prove command completion. Keep polling traffic in a separate counter
and use the same polling cadence in every comparison.

Report offered, accepted and completed throughput; p50/p95/p99 latency;
rejections, timeouts and failed commands; unfinished commands at the end of
the run; node CPU, heap, GC and command-pool thread states; and ZooKeeper
request counts, outstanding requests, latency and state-write/CAS retry counts.
Verify available metrics in the selected build. If a required counter does
not exist, document the observation method and its overhead before adding
instrumentation. Do not infer lock wait time from whole-command duration.

Include the test dataset, cluster size, collection/shard/replica counts,
storage, client placement, JVM arguments, warmup and measurement duration,
seeds/request sequence, raw records and a complete command transcript. Show
per-run distributions and rejection rates; do not combine successful and
timed-out requests into a misleading average.

## Decisions justified by the results

An admission or continuation dispatcher is worth prototyping only if
lock-waiting commands demonstrably starve independent work. Compare cold
latency, accepted throughput, queue bounds, resource usage, shutdown and
failure cleanup. Increasing an executor's thread limit alone is not evidence
that the scheduling problem is solved.

Narrower state records or batching require evidence that a particular shared
state record becomes hot under recovery, followed by an explicit consistency
and upgrade design. The SOLR-14928 discussion raises the contention question;
it does not supply results for the current implementation.

The reference fork's independent/hot/skewed/multi-key/node-barrier dispatch
benchmarks provide useful scenario ideas. Its single leader-hosted Raft task
executor and synthetic dispatch results do not establish the behavior of
ZooKeeper-backed distributed execution in Apache Solr. Adapt invariants and
workloads, then measure the implementation being proposed.
