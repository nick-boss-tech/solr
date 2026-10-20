# Skip record: SOLR-12268

- Ticket: SOLR-12268, Opening a new NRT Searcher can take a while and block calls to /admin/cores. JIRA read in this pass: Open, resolution none, fix versions none, 4 comments (2018-04-26 and 2018-04-27, maintainers discussing whether the isCurrent call should stay in the default output).
- Verdict: keep-open.
- Draft branch: solr-12268-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: opening a new NRT reader takes the IndexWriter lock and may run applyAllDeletesAndUpdates for a long time. Calls to /admin/cores call isCurrent on the reader, which checks whether the IndexWriter is closed and needs that lock, so the cores call waits as long as the reopen. The first comment says the default output should not take an IndexWriter lock; the maintainers note the isCurrent field is already optional for getIndexInfo but on by default and of little use.

## Code reading

- solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java:1080-1095 (getIndexInfo): sets indexInfo.current = closeSafe(reader::isCurrent) unconditionally, so every caller that builds index info calls isCurrent.
- LukeRequestHandler.java:1112-1118 (closeSafe): calls the IOSupplier and returns false only on AlreadyClosedException or IOException.
- solr/core/src/java/org/apache/solr/handler/admin/api/CoreStatus.java:51 (INDEX_INFO_DEFAULT_VALUE = true) and :64-73 (getAllCoreStatus and getCoreStatus use it when indexInfo is not given): the cores status call that the ticket describes builds index info by default.
- CoreStatus.java:158-168 (fetchStatusInfo path): when index info is needed, getIndexInfo(searcher.get().getIndexReader()) is called, which reaches the isCurrent call above.

So the call path from /admin/cores to isCurrent is present by default at the pin. Whether isCurrent blocks for the length of a reopen depends on Lucene's DirectoryReader.isCurrent implementation, which is outside this tree; the record does not measure it.

## History

- No commit at the pin names SOLR-12268 (git log --grep at the pin: none).
- The file history of LukeRequestHandler.java and CoreStatus.java was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a core with NRT indexing and a long applyAllDeletesAndUpdates, with a concurrent /admin/cores status call, recording the status latency with the default indexInfo and with indexInfo=false. Not built or run in this pass.

## Gap analysis

The path from the admin cores call to isCurrent exists by default at the pin. The ticket's blocking effect is a timing claim that needs a run. The maintainers' 2018 question (whether isCurrent should be in default output) is still a design question at the pin, since the default is true.

## Verdict justification

keep-open: the call path is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made. Whether the default should change is a maintainer decision the ticket's comments raise.

Missing for a closing verdict: a timing run at the pin comparing /admin/cores latency during a reopen with indexInfo unset and indexInfo=false, and a maintainer decision on the default for isCurrent.

## Limits

This record does not run a timing test, does not measure Lucene's isCurrent behavior, and does not post anything to JIRA or GitHub.
