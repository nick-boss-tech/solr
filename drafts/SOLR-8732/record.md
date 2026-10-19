# Skip record: SOLR-8732

- Ticket: SOLR-8732, is there a way to get solr.beidermorsefilterfactory  to preserve original. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-8732-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-5, 2026-10-17, audit verdict confirmed-skip).

## Ticket reading

The summary as recorded in the queue: is there a way to get solr.beidermorsefilterfactory  to preserve original. Skip note: support question.

## Code reading

Taken from the audit evidence, not re-read at the pinned head: The ticket asks for Beider-Morse phonetic filtering to preserve the original token. The Solr reference guide documents the filter as phonetic encoding, and the report requests an additional preserve-original option; it shows no regression from the documented analyzer behavior.

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
