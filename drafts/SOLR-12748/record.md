# Skip record: SOLR-12748

- Ticket: SOLR-12748, OOM on Solr cloud example startup on master.
- Verdict: keep-open.
- Draft branch: solr-12748-draft-open (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 5 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-09-06, updated 2022-03-24, no fix versions, no links. The report: on a master build (a SOLR-11966 test branch), after several cluster restarts that switched between embedded and external ZooKeeper, the cloud example node ran out of memory at startup. Two comments (2018-09-06, shalin): the console logs and thread dump show about 5,568 Jetty threads on port 8983 stuck in socket reads; the logs are attached.

## Code reading

Read at the pin:

- `solr/server/etc/jetty.xml:33-36`: the Jetty pool has `minThreads` 10 and `maxThreads` 10000 (`solr.jetty.threads.max`), with an idle timeout. The reporter's count (about 5,568) is under the default cap, so the thread count alone does not show a missing bound.
- The threads stuck in socket reads are on the request path to the external ZooKeeper or the other node; the root of those reads was not traced in this pass.
- No commit at the pin names SOLR-12748 (the evidence tool lists none).

## History

- The ticket was filed on a development branch, and the thread-pool defaults and the ZooKeeper client settings were not traced in history.

## Reproduction attempts

Not run. This pass ran no builds or tests. The reproduction is the reporter's sequence: start the embedded ZooKeeper cloud example, restart while pointing at an external ZooKeeper, cycle back to embedded, and watch the thread count and heap at the pin.

## Gap analysis

- The thread count is under the configured cap; the OOM mechanism (which threads hold memory and why the reads block) is not shown by the code read.
- The stuck reads depend on the ZooKeeper switch sequence, which is a run-specific condition.
- The attached logs are not in the ticket text available here, so the blocking call cannot be matched to a line.

## Verdict justification

keep-open: the thread cap is not the bound that failed, the blocking reads are not traced, and the reproduction sequence has not been run at the pin. close-fixed and close-obsolete do not fit; close-cannot-reproduce needs a run at the pin.

Missing for this verdict: a run of the ZooKeeper switch sequence at the pin with the thread count and heap recorded, and the blocking call from the thread dump.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
