# Skip record: SOLR-15305

- Ticket: SOLR-15305, Multiple nodes from same directory causes bizarre SolrCloud errors. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-15305-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-2, 2026-10-04, audit verdict confirmed-skip).

## Ticket reading

The summary as recorded in the queue: Multiple nodes from same directory causes bizarre SolrCloud errors. Skip note: Same-dir multi-node SolrCloud errors: zero comments since 2021, no isolated root cause; fix shape is a maintainer design decision (fail-fast guard vs shared-dir support); PRS sibling SOLR-15288 already fixed separately.

## Code reading

Taken from the audit evidence, not re-read at the pinned head: Two nodes sharing one solr home is an unsupported setup. A fail-fast guard in ZkController.checkStateInZk (replica node_name != this node) was considered and rejected: node_name legitimately changes (hostname/IP change, container restart) and the core republishes its own node_name, so the guard would break that recovery path. No safe discriminator, so a doc/design matter, not a patch.

## History

No commit history was used for this record.

## Reproduction attempts

Not attempted in this record. The audit did not record a reproduction.

## Gap analysis

The audit (confirmed-skip) classified the ticket as not a current, reproducible defect. Nothing in the audit shows a closing fix or an obsolete feature, and no reproduction was recorded. Whether to close it is a maintainer decision, or a reproduction is needed first.

## Verdict justification

keep-open: the audit verdict (confirmed-skip) is recorded, but the rubric's close verdicts need evidence at the pinned head that this record does not have. Precedent on this queue: the other tickets with the same audit verdict carry keep-open (254 confirmed-skip, 41 deferred), with the audit in the queue entry.

Missing for a closing verdict: a check at the pinned head of the code path the audit names, or a reproduction of the reported behavior.

## Limits

This record does not establish a reproduction, and it does not check the fork pin. It does not post anything to JIRA or GitHub.
