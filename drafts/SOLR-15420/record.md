# Skip record: SOLR-15420

- Ticket: SOLR-15420, Exception thrown during intensive batch partial updating and segment merging. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-05-20, updated 2021-08-13, no fix versions. Linked as a duplicate of SOLR-15419 (Resolved, resolution Duplicate; SOLR-15419 links back to this ticket as Open).
- Verdict: keep-open.
- Draft branch: solr-15420-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 8, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

Solr 8.8.2, standalone, about 7 million documents. A daily job atomically updates one multi-valued field (up to 300 strings, up to 5 words each) on every document. Large segment merges then fail with "Server error writing document id ... to the index". The reporter asks whether it is a bug or a misconfiguration. Comments (janhoy, dweiss, jpountz, 2021-05-20) trace the failure to a Lucene stored-fields writer that converts a size to int (`Math.toIntExact`), and ask whether a single document reaches 2 GB. The reporter says no document is that large.

## Code reading

- The "Server error writing document id" message is at solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:416. It wraps an AlreadyClosedException raised while adding the document (addDoc at :408-416). This is the Solr frame of the reported error. The underlying cause is not in the ticket's trace, so this frame alone does not name the failing write.
- The stored-fields writer named in the comments (CompressingStoredFieldsWriter) is not in this tree: `git grep -n "CompressingStoredFieldsWriter" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` returns no lines. Solr takes Lucene as a dependency: gradle/libs.versions.toml:39 pins `apache-lucene = "10.4.0"`. The Lucene 8.8.2 code in the ticket is not the code at this pin.

## History

- The 8.8.2 stack in the ticket predates the move to Lucene 9 and 10, so the failing Lucene code is not the code at this pin. Whether Lucene 10.4.0's stored-fields writer has the same size limit was not checked: its source is not in this repository.
- SOLR-15419, the ticket this one was marked a duplicate of, is resolved as Duplicate. Neither ticket names a fix commit.

## Reproduction attempts

Not run. A reproduction needs documents or chunks near the 2 GB limit (the reporter's shape, 300 strings per document, is too small to reach it, which is the reporter's own point). That needs a run with a large synthetic document on the pinned head, which this pass did not do.

## Gap analysis

The ticket's failure is in Lucene 8.8.2 code that is not on the pinned head. Two questions remain: (1) whether Lucene 10.4.0 still overflows on a large stored-field chunk, which the record cannot answer from the Solr tree; and (2) what the reporter's real failing document was, because the reporter says no document is near 2 GB. Neither is closed by a commit on main.

## Verdict justification

keep-open: the attempts made are the code path reads above at the pinned head, the Jira read, and the duplicate link check. The missing piece is a check of the Lucene 10.4.0 stored-fields writer for the size overflow (its source or a run with a large document on the pin), and the reporter's failing document shape. close-obsolete is not supported: the 8.8.2 writer is gone from this tree, but the same overflow could exist in Lucene 10.4.0, and a grep of the Solr tree does not show that either way.

Missing for a closing or PR verdict: (1) a run or source read of Lucene 10.4.0's stored-fields writer with a document near the limit; (2) the reporter's sample document or the failing cause from the logs.

## Limits

This record does not reproduce the failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
