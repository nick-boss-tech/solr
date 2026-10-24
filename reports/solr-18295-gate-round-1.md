# SOLR-18295 gate, round 1 (vm6)

Claim: `claims/solr-18295-gate-round-1.md`. Job: `gates/SOLR-18295-routing-round-1.md`. Implementation: `claims/solr-18295-routing-implementation-round-1.md` (windows, not edited here). Receipt: `receipts/SOLR-18295.md`.

## Pins

- Exact head: `b273b245115beff0cc131448c277dc9bde909a0c` on `nick-boss-tech/solr:solr-18295-submit`, one commit by Nick Shanin. Scoped diff: `solr/core/src/test/org/apache/solr/cloud/ReplicationFactorTest.java`, 8 insertions, 2 deletions. Read back on the fork before the claim and before the run.
- Base: upstream/main `13c693d805f6541f7cec684cd27d28eef7b3b614`.
- Host: vm6, Linux 6.1 aarch64, JDK 21.0.12.1+1, no GPU. Runner: `pool/bin/gate-preflight.sh`, then `pool/bin/bg-steps.sh` calling `pool/bin/sweep-run.sh`, under `/workspace/tools/.gradle-vm6.lock`. Logs: `/workspace/gates/logs/vm6/solr-18295/`.
- Worktrees: exact head `/workspace/worktrees/pool-vm6-18295`; synthetic baseline `/workspace/worktrees/pool-vm6-18295-base` (base commit).
- Seeds: `18295ABC0001` to `18295ABC0014`, twenty values. Each run was one fresh whole-class invocation, with `:solr:core:cleanTest` before it. Nothing was relaunched.

## Preflight

Detached tidy at the exact head left the tree clean (`preflight-tidy.log`, `preflight.log`).

## Leg 1: exact head smoke (reported separately)

- Steps, all rc 0: tidy, `:solr:core:spotlessJavaCheck`, `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`, `:solr:core:cleanTest`, `:solr:core:test --tests org.apache.solr.cloud.ReplicationFactorTest -Ptests.seed=18295ABC0001`. Span 20:18 to 20:36 UTC.
- XML `/workspace/gates/logs/vm6/solr-18295/solr18295-leg1/l1-smoke.xml/`: 1 test, 0 failures, 0 errors, 0 skipped. The test's own log markers are in the XML: 1x3 start, 2x2 start, and "replication factor testing complete".

## Leg 2: synthetic baseline (risky routing forced)

Diff, saved as `/workspace/gates/logs/vm6/solr-18295/synthetic-baseline.diff`: from the base commit, the AwaitsFix import and annotation are removed; the head's `CloudSolrClient` import and `createCloudClient` override are added, with `createNewCloudSolrClient(..., false, 30000, 120000)` in place of `true`. The random number consumption matches the head. This is a synthetic baseline, not untouched upstream.

Result: 20 runs, 16 passed and 4 failed. Span 20:36 to 21:48 UTC.

| Run | Seed | Failing line | Failing port (closed proxy) | Cause chain | Follower /update after close |
| --- | --- | --- | --- | --- | --- |
| b01 | 18295ABC0001 | ReplicationFactorTest.java:318 (`sendDoc` for docId=2) | 40085 | SolrServerException, IOException, ClosedChannelException | 0 lines |
| b08 | 18295ABC0008 | :341 | 39425 | SolrServerException, IOException, ClosedChannelException | 0 lines |
| b11 | 18295ABC000B | :341 | 39333 | SolrServerException, IOException, ClosedChannelException; suppressed EofException, Broken pipe | 0 lines |
| b16 | 18295ABC0010 | :341 | 40443 | SolrServerException, IOException, ClosedChannelException; suppressed EofException, Broken pipe | 0 lines |

How each row was read:

- Every failure is a `SolrServerException` on a shard update, with `ClosedChannelException` at the root. No failure is an RF assertion, a setup error, or a worker error.
- Each failing port is the port of a SocketProxy the test had just closed (the test logs "Closing one proxy port" or "Closing second proxy port", then "Closing 5 connections to: http://127.0.0.1:<port>/solr").
- The cloud client logged "request was not communication error it seems" with commError=false, and it did not retry.
- The target node behind each closed proxy logged no `/update` processor line in the window between the close and the failure, in all four runs. So the request did not reach a committed update at the cut follower, as far as the logs show.

Classification: four transport failures before commit, at cut follower endpoints. They do not reproduce the premise as the job states it (a transport exception after commit). The job's rule is that inconclusive commit classification stays inconclusive. Here the logs point to no commit, so the premise is not supported by this sample. Documented limit, not a reason to invent a fail-before proof.

## Leg 3: exact head, matched repetitions

- Same twenty seeds, same clean and log policy, unmodified exact head. Span 21:48 to 23:00 UTC.
- All 20 runs rc 0. Each XML (`/workspace/gates/logs/vm6/solr-18295/solr18295-leg3/h<nn>-run.xml/`): 1 test, 0 failures, 0 errors, 0 skipped, with the three scenario markers.
- Then `:solr:core:check -x test` rc 0 and `:solr:core:spotlessJavaCheck` rc 0. The tree is clean at `b273b24511`.

## Acceptance against the job

- Format, compile, Error Prone and module check at the exact head: pass.
- All 20 matched final-head runs: pass, both scenarios completed, no RF assertion weakened, no retries.
- Baseline: 4 of 20 forced-any-replica runs failed, all as pre-commit transport failures at cut followers. The receipt states this. It does not claim the fix was proven by a baseline failure. The routing fix remains code-path-based.

## Limits

- Local runs on one aarch64 host, not CI. The local retry configuration was kept. CI mode was not used.
- The baseline is synthetic. Its sample is 20 runs and does not establish a failure rate.
- The 20 final-head runs show no failure at this head on this host. They do not establish a rate.
- #4563 (Jan Høydahl, head `54f375b0bed805a08e1b456bd782e15dfce74723`, single-document retry) is a separate open PR. Nothing was posted to it or to JIRA.
- No PR was opened and nothing was pushed to the fork by this gate.
