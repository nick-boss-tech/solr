# Skip record: SOLR-12263

- Ticket: SOLR-12263, Log replay throwing ClassCastException. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments, updated 2022-03-24. The full description (1489 characters) was read through the Jira REST record.
- Verdict: keep-open.
- Draft branch: solr-12263-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: during tlog replay at startup, JavaBinCodec.readSolrInputDocument throws ClassCastException (java.lang.Long cannot be cast to java.lang.String), reported from the 2018 trace through TransactionLog.LogReader.next and UpdateLog.LogReplayer.doReplay. The reporter's node could not finish replay.

## Code reading

- solr/solrj/src/java/org/apache/solr/common/util/JavaBinCodec.java:767-805 (readSolrInputDocument): the field name is read with readVal and cast to String ((String) readVal(dis) at the boost branch and (String) obj otherwise). A Long in the field-name position throws ClassCastException here, as in the ticket's trace.
- solr/core/src/java/org/apache/solr/update/UpdateLog.java:2346-2349 (replay loop, REPLAY_ERR path): ClassCastException is caught, counted in recoveryInfo.errors, and logged as "REPLAY_ERR: Unexpected log entry or corrupt log"; the replay continues with the next entry. The 2018 trace went through a different replay path (LogReplayer.doReplay at a different line), so the failure in the ticket is now handled by a catch that the ticket's trace did not show.
- solr/core/src/java/org/apache/solr/update/UpdateLog.java:1605-1607 and 1915-1919 carry the same ClassCastException catch on the other replay paths.

So the cast still exists, and the replay path now logs and skips the entry rather than stopping. Whether the skip is the intended outcome for this entry, and whether the document is lost, is not established; a run would show the replay result.

## History

- No commit at the pin names SOLR-12263 (git log --grep at the pin: none).
- The JavaBinCodec file history at the pin ends with 090f5b995c (dependency update) and bb52661e60 (package tidy); neither changes the cast or the replay catch in a way traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a tlog whose entry carries a Long in a field-name position (for example, a hand-built or corrupted javabin entry), replayed at the pin, recording the replay log and the resulting document count. Not built or run in this pass.

## Gap analysis

The cast the ticket fails on is still present, and the replay handling is the catch at UpdateLog.java:2346. The reporter's blocking failure is expected to be handled by the catch now, but whether the entry is dropped silently or the replay still stops in another path is not established without a run.

## Verdict justification

keep-open: the mechanism is present at the pin and the handling is only partly established by reading. close-fixed and close-obsolete are not supported: the cast remains and no fix commit names the ticket. close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a run replaying a tlog with a Long field-name entry at the pin, recording whether replay completes with the entry skipped and the logged error, and a maintainer decision on whether a skipped entry is acceptable.

## Limits

This record does not run the replay, does not construct a tlog entry, and does not post anything to JIRA or GitHub.
