# Skip record: SOLR-9599

- Ticket: SOLR-9599, "DocValues performance regression with new iterator API". JIRA status read 2026-10-20T12:55:29Z (UTC): Open, resolution None, fix version 7.0 listed, updated 2019-06-08. Linked: LUCENE-7462 (Resolved), LUCENE-8374 (Resolved), LUCENE-7407 (Resolved, the regression source).
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9599-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:55:29Z (UTC).

## Ticket reading

Faceting and sorting on docValues fields became slower after the Lucene docValues iterator change (LUCENE-7407). The reporter's 2016 measurements show new/old time ratios of about 1.1 to 2.0 across cardinalities and two index sizes. The ticket is a performance umbrella; eight comments are benchmark tables from 2016.

## Code reading (pinned head)

- The DocValues path is in Lucene (the iterator API), not in Solr's `solr/core`. The Solr-side use is the facet and sort code. Not traced to a line in this pass.
- The linked Lucene issues are Resolved, so the regression's cause is claimed fixed upstream; this tree does not show a measurement.

## History

- evidence.py at the pinned head: no commits naming SOLR-9599.

## Reproduction attempts

Not run. A current measurement needs a multi-million-document index, the same facet and sort queries, and the old and new codec versions. That is benchmark work on a 4-CPU host, not a focused test.

## Gap analysis

The regression was measured in 2016 against an older Lucene. Whether it still shows on the current codec is unknown; the linked Lucene fixes may close it, but no current number is in the record.

## Verdict justification

keep-open meets the rubric: attempts made are the ticket's measurement history and the linked-issue check; the missing piece is a current measurement against the pinned revision. close-fixed is not supported: the linked Lucene issues are Resolved, but no Solr commit or measurement names the fix. close-obsolete is not supported: the path is present.

## Limits

This record does not measure current performance. It does not verify the Lucene fix commits.
