# Pool assignment: flaky root-cause reading, the MiniSolrCloudCluster shutdown hang

Capability tags: `review`. Staffing: up to 2. Claim path: claims/pool-flaky-rca-cluster-shutdown-hang.md.

Background: reports/flaky-rca-gcs-install-shard.md established that two upstream test runs (37384974639 on the SOLR-18506 branch and 37987579785 on the SOLR-3657 branch) failed the same way: a MiniSolrCloudCluster shutdown hang, where the HTTP client stop inside HttpShardHandlerFactory.close blocks, ExecutorUtil.awaitTermination waits 60 seconds, interrupts, waits 60 more, and throws "Timeout waiting for pool to shutdown". The failing branches were not involved either time. This assignment is the next step: characterize the hang itself.

Work: read the shutdown path end to end on current main (MiniSolrCloudCluster.shutdown, HttpShardHandlerFactory.close, ExecutorUtil.awaitTermination, and the executor and HTTP client lifecycles they touch). Identify what the close call can block on, which threads or connections keep the pool from draining, whether the block is bounded by any timeout, and what state from a test class (open connections, in-flight requests, shared clients) can set it up. Rank the mechanisms with code evidence. Then write a reproduction spec the Linux hosts can execute: the smallest test shape likely to trip it (or a targeted unit-level construction if the class-level shape is the trigger), seeds or settings if relevant, and the pass condition that would confirm each mechanism. A fix sketch is welcome but not required; no fix is implemented under this assignment.

Deliverable: reports/flaky-rca-cluster-shutdown-hang.md. No builds or test runs. Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverable).
