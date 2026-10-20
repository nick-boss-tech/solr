# Skip record: SOLR-12944

- Ticket: SOLR-12944, Bugs around createNodeSet=EMPTY.
- Verdict: keep-open.
- Draft branch: solr-12944-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 21 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-10-30, updated 2019-11-21, no fix versions. Links: "Reference SOLR-13248 (Closed)"; "Reference SOLR-13247 (Open)". The report: creating a coreless collection (three shards, replication factor 0) with `createNodeSet=EMPTY` from SolrJ fails, and a non-zero replica count with EMPTY is confusing. One comment (2019-02-15, erickerickson): does not reproduce the create failure on 7.6 and agrees the non-zero replica count with EMPTY is confusing.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/handler/admin/api/CreateCollection.java:234-238`: `createNodeSet=EMPTY` sets `requestBody.createReplicas = false`; any other value is split into `nodeSet`.
- `CreateCollection.java:159-165`: when `createReplicas` is false the message carries `CREATE_NODE_SET=EMPTY` and no replica creation follows.
- `CreateCollection.java:166-170`: the replication factor and `nrtReplicas` are written to the message independently of the EMPTY node set. So a non-zero replication factor with EMPTY is still stored as the collection's replica count, which is the confusing case the ticket and the comment describe.
- `Assign.java:214-236` and `RestoreCmd.java:360` read the EMPTY value for the node list. The EMPTY set is handled in the API path; the reading did not trace whether a stored non-zero replica count with EMPTY is used later by the Overseer.
- The SOLR-13248 commits (`97875af3f9`, `7e2d40197c`) are ancestors of the pin and concern default placement, not EMPTY handling.

## History

No commit at the pin names SOLR-12944 (the evidence tool lists none). The API change that maps EMPTY to no replicas is in the pin, but no commit that names this ticket was found.

## Reproduction attempts

Not run. This pass ran no builds or tests. The ticket's SolrJ call (three shards, zero replicas, `setCreateNodeSet("EMPTY")`) would show whether the failure still occurs; a second call with a non-zero replication factor would show the stored replica count.

## Gap analysis

- The reported failure (the zero-replica create with EMPTY) appears handled by the API mapping at the pin, but that is a reading, not a run.
- The remaining question is the non-zero replica count with EMPTY: the code stores it, and the ticket asks for a storage change, which the audit note calls a redesign.
- Splitting the ticket into the API half (appears fixed) and the storage half (open) is a maintainer decision; the ticket does not split them.

## Verdict justification

keep-open: the API half appears fixed by the EMPTY mapping, but the stored non-zero replica count with EMPTY is open, the zero-replica create has not been run, and the ticket does not record a split. close-fixed would need the ticket's whole request met, which the storage question prevents; close-obsolete does not fit.

Missing for this verdict: a run of the zero-replica EMPTY create and a non-zero replica EMPTY create at the pin, and a maintainer decision on the stored replica count.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
