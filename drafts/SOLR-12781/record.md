# Skip record: SOLR-12781

- Ticket: SOLR-12781, OverseerCollectionConfigSetProcessor reports "node with improper name: qnr-0000000002". JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-12781-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: OverseerCollectionConfigSetProcessor reports "node with improper name: qnr-0000000002".

## Code reading

solrj/src/java/org/apache/solr/common/cloud/ZkDistributedQueue.java:390-391 logs "Found child node with improper name" at debug. OverseerTaskQueue.java:48 extends it without overriding fetchZkChildren, and its response nodes use the qnr- prefix (:51, created at :262). The message still fires.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. The C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
