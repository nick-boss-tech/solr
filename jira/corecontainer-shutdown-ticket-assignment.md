# Jira assignment: file the CoreContainer shutdown ticket (Test, Minor)

Status: OPEN

- Capability tags: jira, filing.
- Intended host: any Windows host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: phone agent (vm1), 2026-10-21, at the direction of Nick.

## Task

File exactly one new ticket in the SOLR Jira project with the fields and text below, verbatim. This is the first item in a new `jira/` area on this branch; Jira filing work lives here from now on.

- Issue type: Test
- Priority: Minor
- Summary (the Jira title field): CoreContainer.shutdown: a client close pool timeout skips the remaining closes and leaks metrics contexts
- Description: the text in the block below, exactly as written, in Jira wiki markup. Do not reformat it, do not convert the markup, and do not add or remove anything.

```
🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_

*Summary: when the client close pool in CoreContainer.shutdown() times out, its throw skips the handler close and the root metrics context close that follow it, so the container's metrics contexts leak and are charged to an unrelated test suite.*

This surfaced as two class teardown failures in one Crave CI run ([PR #5030 run|https://github.com/apache/solr/actions/runs/39699700526/job/119565807999], seed {{A35B8669BA78E261}}). Every test passed. {{CloudHttp2SolrClientTest}} failed in {{tearDownAfterClass}} with "Error shutting down MiniSolrCloudCluster", and {{TestJavaBinCodec}} was charged by the ObjectReleaseTracker with 16 unreleased {{SolrMetricsContext}} objects that the first suite's cluster had created. The two failures are one event.

The mechanism, from the run log and the code: {{CoreContainer.shutdown()}} waits on the close pool through {{ExecutorUtil.awaitTermination}} ({{CoreContainer.java:1314}}). When the waits expire, the utility force-stops the pool and throws {{RuntimeException: Timeout waiting for pool to shutdown}}. The throw aborts {{shutdown()}} before {{containerHandlers.close()}} (line 1362) and the root metrics context close (line 1366), which are the only release calls for those contexts. The leaked contexts stay in the JVM-global tracker and land on the next suite in the fork whose teardown checks it, which is why the charge appears on a suite that created none of them.

The failure is intermittent in CI (the full solrj suite passed twice locally under the same seed), but it reproduces deterministically by blocking a close on a latch and letting the real timeout budget run out: on current code that fails with the same throw and an ObjectTracker leak report. A fix is prepared on [this branch|https://github.com/nick-boss-tech/solr/tree/corecontainer-shutdown-closes]: catch and log the timeout throw at the call site so the trailing closes always run, with a regression test that fails without the fix and passes with it. {{ExecutorUtil}} is unchanged; its throw is a contract other callers rely on.

Out of scope here: why the close exceeded its budget under CI load in the first place. This ticket covers the skipped closes, not the slowness.

_AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution._
```

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Text changes | decided: none; file the text verbatim | if a field rejects the text (length, markup), stop and record the rejection in the claim instead of editing the text | Nick |
| Issue type or priority unavailable | decided: stop and record it | do not substitute a different type or priority silently | Nick |
| Follow-on steps | decided: none in this assignment | the ticket number feeds the fix branch's changelog and a later PR decision, both handled separately by the owner | Nick |

No row may leave the choice to the claimant.

## Deliverable

The claim marked DONE with: the filed ticket key (SOLR-xxxxx), its URL, and a one-line confirmation that the filed title and description match the text above (re-read the filed ticket to confirm). Record the same key and URL in a short result note at `jira/corecontainer-shutdown-ticket-result.md` on this branch.

## Limits

- File exactly one ticket. No second ticket, no edits to any existing ticket, no comments on any ticket.
- No PR, no GitHub comment, no code change. No force push.
- Commit as Nick Shanin with no trailers. No em dashes in anything you author around the filing (the ticket text above is already final).
