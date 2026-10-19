# Skip record: SOLR-13785

- Ticket: SOLR-13785, Optimistic concurrency issue for nested documents. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-13785-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-1, 2026-10-04, audit verdict confirmed-skip).

## Ticket reading

The summary as recorded in the queue: Optimistic concurrency issue for nested documents. Skip note: stale 2019 nested-doc version-conflict report, no maintainer direction in 7 years, design-heavy version-bucket semantics — corpus entry written.

## Code reading

Taken from the audit evidence, not re-read at the pinned head: Read ticket: Smiley says overlapping child/parent ids are user error with no protection by design.

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
