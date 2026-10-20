# Skip record: SOLR-12296

- Ticket: SOLR-12296, Test fail in TimeRoutedAliasUpdateProcessorTest RE "We need to create a new time routed collection but for unknown reasons were unable to do so." JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; the two build links in the description are the only other content.
- Verdict: close-obsolete.
- Draft branch: solr-12296-draft-close (a status change from the earlier keep-open; the keep-open draft solr-12296-draft-open is not updated by this record).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: two Jenkins test failures in TimeRoutedAliasUpdateProcessorTest with the exception "We need to create a new time routed collection but for unknown reasons were unable to do so." The failure is the post-create check in the synchronous collection-creation path of the time-routed alias update processor.

## Code reading

- The message is absent from the whole tree at the pin: git grep for "We need to create a new time routed", "unable to do so", and "We need to create" under solr returns no hits.
- The message and the check around it were removed by commit c97551cc36 (SOLR-13375, 2019-07-15, "2 dimensional routed aliases"), which is an ancestor of the pin (checked with merge-base). The removed hunk is in solr/core/src/java/org/apache/solr/cloud/api/collections/TimeRoutedAlias.java: in the SYNCHRONOUS case, after createNextCollection, the code called updateParsedCollectionAliases and threw this SolrException when it made no progress. The same commit deleted MaintainTimeRoutedAliasCmd.java and added MaintainRoutedAliasCmd.java.
- At the pin, collection creation for routed aliases runs through MaintainRoutedAliasCmd.java (which calls updateParsedCollectionAliases at line 132 and does not throw this message), and CategoryRoutedAlias.java and DimensionalRoutedAlias.java carry their own updateParsedCollectionAliases implementations. The synchronous URP-side creation path that produced the ticket's message is gone.
- The named test class still exists at the pin (solr/core/src/test/org/apache/solr/update/processor/TimeRoutedAliasUpdateProcessorTest.java), and it was changed by c97551cc36 as well.

## History

- The message was introduced in 925733d1ef (SOLR-11653, 2018-01-05), the URP auto-creation change, and moved by d8f2a02fdb (SOLR-13131, 2019-03-13) and removed by c97551cc36 (SOLR-13375, 2019-07-15). The removal commit is an ancestor of the pin (checked).
- No commit at the pin names SOLR-12296 (git log --grep at the pin: none).

## Reproduction attempts

No run was attempted. For close-obsolete the rubric's check is the removed code and the whole-tree grep, which are done above. The 2018 Jenkins failures are for code that no longer exists at the pin.

## Gap analysis

The ticket's failure mode (the post-create check in the URP path) is not in the code at the pin, so the report is about code that was removed. The current routed-alias creation path has different failure modes, and the test class still runs at the pin; if a current failure of TimeRoutedAliasUpdateProcessorTest needs tracking, it should be a new ticket.

## Verdict justification

close-obsolete: the named message and its code path are removed by c97551cc36, an ancestor of the pin; the whole-tree grep for the message is empty. The rubric's close-obsolete check (removed code, empty whole-tree grep) is met.

## Limits

This record does not run TimeRoutedAliasUpdateProcessorTest at the pin and does not check whether another failure mode in the current routed-alias code matches the Jenkins report. It does not post anything to JIRA or GitHub.
