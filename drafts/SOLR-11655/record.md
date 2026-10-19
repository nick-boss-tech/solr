# Skip record: SOLR-11655

- Ticket: SOLR-11655, SolrCloud should Support encrypted zookeeper ACL Digest details. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11655-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-5, 2026-10-17, audit verdict confirmed-skip).

## Ticket reading

The summary as recorded in the queue: SolrCloud should Support encrypted zookeeper ACL Digest details. Skip note: feature request / support question (encrypted ZK creds), not a bug..

## Code reading

Taken from the audit evidence, not re-read at the pinned head: The ticket asks whether the VMParams digest ZooKeeper providers can accept encrypted credentials and how to configure them; it reports no authentication failure or credential exposure. This is a new secret-storage capability, not a defect in the documented providers.

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
