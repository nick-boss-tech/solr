# Skip record: SOLR-7982

- Ticket: SOLR-7982, SolrCloud: collection creation: There are duplicate coreNodeName in core.properties in a same collection.. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-7982-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: SolrCloud: collection creation: There are duplicate coreNodeName in core.properties in a same collection..

## Code reading

unclear | coreNodeName is now read from cluster state (ZkController.java:2070-2078, 2139) rather than generated per core, but no commit at 498a09aea9 mentions SOLR-7982 and the core.properties writer was not traced. Next: confirm ClusterStateMutator.getAssignedCoreNodeName prevents duplicate names and trace the core.properties writer.

## History

- `498a09aea9`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
