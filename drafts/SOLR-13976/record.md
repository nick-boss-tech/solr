# Skip record: SOLR-13976

- Ticket: SOLR-13976, Solr java.lang.ArrayIndexOutOfBoundsException: Index 1048 out of bounds for length 848. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-11-28, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-13976-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree).
- Source: remediation redo of batch 05, item 13, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the C reading) cited the createValue range; the range is confirmed here, and its history claim is corrected below.

## Ticket reading

Searching a SolrCloud collection that sorts or groups on a field throws ArrayIndexOutOfBoundsException (index 1048 for length 848) from Packed64SingleBlock.set, reached through GrowableWriter.set in the uninverting FieldCache. The stack goes through TermGroupSelector.setNextReader. The reporter gives no data or Solr version.

## Code reading at the pinned head

- solr/core/src/java/org/apache/solr/uninverting/FieldCacheImpl.java:950: `static class SortedDocValuesCache extends Cache`.
- FieldCacheImpl.java:992-993: `new GrowableWriter(startTermsBPV, maxDoc, acceptableOverheadRatio)`, sized from `reader.maxDoc()`.
- FieldCacheImpl.java:1023: `docToTermOrd.set(docID, 1 + termOrd);`, with the bound check on `termOrd >= maxDoc` before it (about :1008-1010).

So the writer that raises the ticket's exception is in this class at the pin. The write index is docID, and the size is maxDoc. A set beyond the array length (the reporter's 1048 against 848) would need a docID above what the writer holds, and the code path that could allow that was not established by reading.

## History

- The file has 21 commits (`git log --oneline -- solr/core/src/java/org/apache/solr/uninverting/FieldCacheImpl.java | wc -l`). The earlier record's statement that the region is unchanged in file history is not checked and is not relied on here. The commits that touched this class were not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a segment whose maxDoc and docID ranges match the reporter's shape (1048 against 848), which the ticket does not supply. This pass did not build or run a sorted or grouped query.

## Gap analysis

The exception's write is in this class at the pin, but the reporter's data is not in the ticket and no run reproduces the out-of-bounds index. Whether the reporter's version had the same sizing, and whether a current version of the packed-ints writer fixes it, is not settled.

## Verdict justification

keep-open: the attempts made are the code read and grep above at the pinned head, the Jira read, and the history count. The missing piece is the reporter's index (or a segment shape that reproduces index 1048 against length 848) and a run of a sorted or grouped query on the pin against it. close-fixed and close-obsolete are not supported: neither a fix commit nor a removal is identified. pr-needed is not supported without a reproduction.

Missing for a closing or PR verdict: a reproduction with the index shape, and the run output.

## Limits

This record does not reproduce the exception. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
