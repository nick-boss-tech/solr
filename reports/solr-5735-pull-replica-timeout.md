# SOLR-5735: the seeded timeout did not recur on local disk

All three additional local-disk runs passed: **3 passes, 0 failures, 0 skips**. Each run executed the one test in `org.apache.solr.cloud.ChaosMonkeyNothingIsSafeWithPullReplicasTest`, using seed `E4EFD2B86C0B81D4` and nightly tests enabled, at unchanged upstream/main commit `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.

These results do not support a deterministic failure from this seed on this local filesystem. Three passes do not exclude an intermittent failure or prove that the prior network-filesystem failure is fixed.

## Three additional runs

| Run | UTC start | UTC finish | Test duration | Tests / skipped / failures / errors | Result |
| --- | --- | --- | --- | --- | --- |
| 1 | 2026-10-23 06:59:41 | 2026-10-23 07:02:08 | 57.601 s | 1 / 0 / 0 / 0 | PASS, exit 0 |
| 2 | 2026-10-23 07:02:08 | 2026-10-23 07:03:41 | 53.083 s | 1 / 0 / 0 / 0 | PASS, exit 0 |
| 3 | 2026-10-23 07:03:41 | 2026-10-23 07:05:15 | 53.904 s | 1 / 0 / 0 / 0 | PASS, exit 0 |

Durations are the JUnit suite durations, not total build times. Each run used `cleanTest` to execute the test again. Fresh JUnit XML and console logs were copied aside before the next run. The console logs confirm the exact seed and `BUILD SUCCESSFUL`: run 1 lines 25 and 128; runs 2 and 3 lines 19 and 95.

## What was run

Host: Phone Agent 2. Source checkout: `/workspace/solr-5735-repeatability`. Both the checkout and its build/test temporary directories are on `/dev/root`, filesystem `ext4`, mounted at `/`; this host's `/workspace` is not NFS. JDK: `/usr/lib/jvm/java-21-openjdk-amd64`; Gradle cache: `/home/ubuntu/.gradle`.

Each invocation ran through the assignment's `pool/bin/sweep-run.sh`. The serial batch also held `/workspace/tools/.gradle-55d52da071bf.lock`, and the sweep runner held `/workspace/tools/.gradle-phone-agent-2.lock` for each build. Disk and inode guards passed.

```sh
pool/bin/sweep-run.sh /workspace/solr-5735-repeatability \
  /workspace/phone-agent-2-logs/solr-5735-repeatability run-N \
  --no-daemon --max-workers=2 \
  :solr:core:cleanTest :solr:core:test \
  --tests org.apache.solr.cloud.ChaosMonkeyNothingIsSafeWithPullReplicasTest \
  -Ptests.nightly=true -Dtests.seed=E4EFD2B86C0B81D4 -Ptests.jvms=1
```

`run-N` denotes `run-1`, `run-2`, or `run-3`. Source HEAD and the clean source tree were verified before and after the batch. No product code change, product branch, Jira post, GitHub comment, or PR was made.

## Prior timeout and missing original diagnostics

The [assignment update](../assignments/solr-5735-pull-replica-timeout-round-1.md#update-2026-10-23-vm2) records item 1 as already answered: the pull replica could not copy the index on the network filesystem because hard-link creation in `IndexFetcher` at line 1224 raised `Too many links`; the same seed passed on local disk. The [existing PR draft](../pr-drafts/SOLR-5735/pr-description.md) independently records that same finding. This report carries that forward as a prior finding, not as something independently reproduced in this batch.

The earlier failure was `Cluster state still in flux after 30 retry intervals` in `waitForThingsToLevelOut`, called from the pull-replica test. The inspected coordination records did not contain the original seeded failure log, its path, or a saved `collections/collection1/state.json`. Consequently:

- Cluster state at the original timeout: unavailable in the inspected records.
- Specific inactive replicas at that timeout: unavailable.
- Each replica's last state-change log line: unavailable.

There was no timeout in these three reruns, so no new timeout snapshot exists. No replica identities or log line numbers have been inferred or invented. The resumed slice was item 2 only, as explicitly recorded in the claim; its three reruns are complete. Recovering the original run's detailed state evidence, if still required, needs the original host's log artifacts rather than another successful local rerun.

## Preserved evidence

The [evidence directory](solr-5735-pull-replica-timeout-evidence/) contains each console log, headroom log, and complete JUnit XML compressed losslessly as `.xml.gz`, plus `state.json` with commands, timings, filesystem and parsed counts. `SHA256SUMS` identifies the original console logs and uncompressed XML files.

Original uncompressed artifacts remain on Phone Agent 2 at `/workspace/phone-agent-2-logs/solr-5735-repeatability/`. Each run has `run-N.log`, `run-N.headroom.log`, and `run-N.xml/TEST-org.apache.solr.cloud.ChaosMonkeyNothingIsSafeWithPullReplicasTest.xml`. The serial runner is `/workspace/tools/phone-agent-2-5735-reruns.py`.

Disposition: item 2 DONE; prior item 1 finding retained with the original-artifact gap explicitly recorded. No code fix is proposed or implied by these results.
