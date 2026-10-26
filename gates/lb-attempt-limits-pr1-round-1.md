# Gate: LB attempt-limit enforcement, PR 1, round 1

Status: OPEN

- Tags: gate, premise-run. Intended host: any capable Linux gate host with JDK 21, Git and Python 3, serialized under its build lock.
- Source: Nick's October 26 instruction to implement only PR 1 of the mailing-list proposal. Do not implement client defaults/overrides, shard controls or PRs 2 and 3.
- Fork: nick-boss-tech/solr, branch solr-lb-attempt-limits-pr1-submit.
- Exact head: 7a226ef031f8b82527a1209ce760fcf37e611601.
- Parent and original-production proof base: apache/solr main 0d6b2e32f598d772aed094038022a6a588fdfc5a.
- Module: :solr:solrj.
- Seed for both proof legs: 5B407A2D9C1310E7 (a selected reproducibility seed, not a prior passing result).
- Implementation claim: claims/lb-attempt-limits-pr1-windows-round-1.md.
- Implementation report: reports/lb-attempt-limits-pr1-windows-round-1.md.
- Receipt target: receipts/lb-attempt-limits-pr1.md, written only by the gating host.
- State: Implemented, Proof Pending. Static whitespace checks passed on Windows; no build, compiler, test or formatter ran there.

## Preflight and scope

Read the live board instructions and existing claims, confirm the fork head, and use a clean detached Linux checkout. Record host, UTC start, lock, checkout, JDK, runner, complete logs and fresh result paths in an add-only gate claim. There is no public PR or JIRA number yet. The descriptive branch carries no unrelated ticket key. Do not create a changelog with an invented number.

Review the exact four-file delta. It changes LBSolrClient.java and LBAsyncSolrClient.java and adds LBSolrClientAttemptLimitTest.java and LBAsyncSolrClientAttemptLimitTest.java, all under solr/solrj. The two new suites contain 12 methods each. Counters must be execution-local, count the first attempt and failed connections, share the allowance with zombie fallback, and never make an otherwise unsafe request retryable. Attempts inside the underlying HTTP implementation and background health checks are explicitly outside the count. Existing non-attempt async failures retain their previous exception behavior.

## Formatting and fixed-head proof

At the exact head, run ./gradlew tidy and require a clean tree. If tidy changes files, commit only the relevant formatting with Nick Shanin's ICLA identity and no trailers, push plainly to this fork branch, and post a new superseding gate with that head before treating any test results as proof. Follow WORKFLOW.md's gate-repair process; do not edit this job or borrow proof for the old SHA.

Run the focused and neighboring suites from fresh test output:

```sh
./gradlew :solr:solrj:cleanTest :solr:solrj:test \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientAttemptLimitTest \
  --tests org.apache.solr.client.solrj.impl.LBAsyncSolrClientAttemptLimitTest \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientTest \
  --tests org.apache.solr.client.solrj.impl.LBAsyncSolrClientTest \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientRetryUnsentTest \
  --tests org.apache.solr.client.solrj.impl.LB2SolrClientTest \
  --tests org.apache.solr.client.solrj.impl.LBHttpSolrClientBadInputTest \
  -Ptests.seed=5B407A2D9C1310E7
```

Require all 24 new methods and all selected neighboring suites to execute with zero failures, errors or skips. Retain raw logs, seed, fresh XML and class outputs. Existing iterator tests include timeAllowed coverage; new tests cover cancellation, processing limits, unsafe requests, unsent updates, reuse/concurrent executions, live and zombie caps, independent dead-server allowances, zero/negative settings and last-allowed success. These are controlled failures and recorded delegate dispatches, not destructive server-crash probes.

Run ./gradlew check -x test as required by repository AGENTS.md. Record actual results and classify independent baseline/environment failures; do not silently exclude failing tasks or call an up-to-date/cache-only test run a pass. Complete with git diff --check and a clean tree at the proved SHA.

## Original-production discrimination

The tests refer to the new nested exception type. Restoring the entire original production file without a shim would cause compilation failure, which is not fail-before proof. Use a second clean detached checkout at the parent and copy only the two new test files from the offered head. Add only the new nested exception declaration from that head to the original LBSolrClient so those tests compile. Leave all original production request and iterator behavior intact; keep original LBAsyncSolrClient unchanged. This is an API-only synthetic base, not an unmodified-parent run.

For reproducibility, the exception shim can be extracted from the offered LBSolrClient between the literal markers below and inserted before the original EndpointIterator declaration. Run this Python from the synthetic-base checkout after copying the two tests:

```python
from pathlib import Path
import subprocess

head = "7a226ef031f8b82527a1209ce760fcf37e611601"
path = "solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java"
offered = subprocess.check_output(["git", "show", f"{head}:{path}"], text=True)
start = offered.index("  /** A configured attempt limit stopped failover,")
end = offered.index("  private static class AttemptCounter", start)
shim = offered[start:end]
original = Path(path).read_text()
marker = "  public static class EndpointIterator {"
assert original.count(marker) == 1
assert "class AttemptLimitExceededException" not in original
Path(path).write_text(original.replace(marker, shim + marker, 1))
```

Record the synthetic diff proving that the only changes to the parent are the two new tests and that inert exception declaration. No counter, loop, iterator or callback fix may be present. Run the following using the same seed, and require all four named methods to execute and fail for the stated behavioral reason:

```sh
./gradlew :solr:solrj:cleanTest :solr:solrj:test \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientAttemptLimitTest.testStandaloneLimitStopsBeforeZombieFallback \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientAttemptLimitTest.testStandaloneLimitWithAllZombies \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientAttemptLimitTest.testStandaloneDoesNotRetryExhaustedLiveSnapshot \
  --tests org.apache.solr.client.solrj.impl.LBAsyncSolrClientAttemptLimitTest.testAsyncPreservesAttemptLimitFailure \
  -Ptests.seed=5B407A2D9C1310E7
```

Expected original behavior: zombie fallback reaches a healthy third endpoint despite a limit of two; a positive limit with all endpoints already zombies reaches an empty live array; a limit of five against three failing live endpoints re-dispatches within the same live snapshot; async failover returns the last transport exception instead of identifiable attempt-limit exhaustion. Record each actual failure and raw XML. A compile error, initialization failure, unrelated assertion, timeout or missing test is not discrimination. If a method passes on the base, investigate and correct the evidence rather than counting it as fail-before proof.

Other assertions are compatibility pins unless separately discriminated. Do not publish a blanket fail-before claim for all 24 methods. Preserve fixed-head XML separately before cleaning or running the synthetic base.

## Completion and failure handling

GREEN requires the fixed-head suites, tidy, required check tasks, clean final tree and the four matched original-production failures described above. If tests execute and fail, investigate and repair with a new scoped commit and superseding exact-head gate; do not blindly rerun. An environment attempt that never executes tests may be repaired and relaunched once with its reason recorded.

Write the gate host's receipt and an add-only report with exact head/base, API-shim disclosure, commands, toolchain, counts, skips, seed, raw evidence paths, failures/repairs, remote-head confirmation and proof limits. End the receipt with a concise proposed PR description using observed results only.

No public PR creation, review comment, JIRA write, mailing-list message, force-push or write to apache/solr is authorized by this gate. PRs 2 and 3 await Nick's direction after mailing-list feedback.
