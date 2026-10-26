# Interrupted HttpJettySolrClient close: fix proof on a new branch (round 1, vm2)

Claim: `claims/teardown-interrupt-fix-round-1-vm2.md`. Inputs: review round 2 (`reports/teardown-client-close-source-review-round-2.md`, 3733d701c4f) and the external-executor result (`reports/teardown-client-close-external-executor-round-1.md`, caadee98de8).

## Branch

- Fork branch: `nick-boss-tech/solr:solr-teardown-interrupt-close-fix`, head `8787c6605ad`. Base: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`.
- Commits (author Nick Shanin, no trailers):
  1. `d1d5e963c70` Test interrupted HttpJettySolrClient close across owned, caller-executor and borrowed clients. Tests only.
  2. `423390023a6` Let HttpJettySolrClient close finish Jetty cleanup when the caller's interrupt is set. Production change, 9 insertions and 2 deletions in `HttpJettySolrClient.java`.
  3. `8787c6605ad` Format the new interrupt-close test lines. Line wrapping only, found by the push hook's formatter check.
- No PR, no comment, no Jira. Opening a PR needs Nick's approval.

## The change

In `close()`, the interrupt flag is captured and cleared after the async drain (`waitForComplete`) and before the Jetty stop. In the `finally`, the executor shutdown runs with the flag clear, and the flag is restored after it. The tracker release, the borrowed-client path (`closeClient == false`) and the caller-executor path (`shutdownExecutor == false`) are unchanged.

Why this shape: the stop waits on a latch in `ManagedSelector.doStop`, which throws `InterruptedException` if the flag is set (`ManagedSelector.java:139`). Clearing the flag only for the stop lets Jetty finish, and restoring it keeps the caller's interrupt. An interrupt that arrives during the stop is not handled by this change.

## Failing before, passing after

Test class `HttpJettySolrClientTest`, seed `FFF5A3C3AAC69084`, JDK 21.0.12.1+1, Linux aarch64, security manager on. Runs through `pool/bin/sweep-run.sh` under the host lock.

| Run | Commit | Result | Notes |
| --- | --- | --- | --- |
| Before (focused, 3 new tests) | d1d5e963c70 | exit 1; 4 counted, 3 failures | owned: `RuntimeException: Exception on closing client`, 60.051 s; caller executor: same exception, 0.025 s; borrowed: passed, 0.008 s; `classMethod` failure: tracker reports 2 unreleased `HttpJettySolrClient` objects, caused by the two failing tests |
| After (whole class) | 423390023a6 | exit 0; 53 tests, 0 failures | new tests pass; owned test 0.006 s |
| Final (whole class, pushed head) | 8787c6605ad | exit 0; 53 tests, 0 failures, 0 errors, 0 skipped | new tests: all three pass |

Evidence: `logs/fix-before/` and `logs/fix-after/`, `logs/fix-final/` in `/workspace/gates/gates-vm2/`. Each contains the runner status and the XML.

What the proof covers:
- Owned client, default executor: the close throws before the fix and returns with the flag preserved after it. The HttpClient is stopped and the client is released from the tracker.
- Caller-owned executor: same, and the caller's pool is not shut down and still runs a task after close.
- Borrowed client: passes before and after. It is a guard, not a failing-before proof. It checks that closing a borrowing client does not stop the parent's HttpClient.

## Limits

- Interrupts that arrive during the stop are not covered. The change handles only an interrupt set before close.
- The formatting commit `8787c6605ad` came after the full-class run on `423390023a6`. It changes line wrapping only in the new test lines, and the final run was repeated on the pushed head.
- Not run: the module `check` task, Error Prone, and the full `:solr:solrj-jetty` suite beyond `HttpJettySolrClientTest`. The formatter check passes on the branch range (`format-check.sh`, no new violations).
- Not tested under real network load. The tests are the focused ones above.
- The interrupt tests run without requests, as the probes did.

## Next

- Owner decision on a PR. Nothing here is posted.
- Source review of the interrupt-during-stop case, which this change leaves open.
