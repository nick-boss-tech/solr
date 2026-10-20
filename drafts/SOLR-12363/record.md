# Skip record: SOLR-12363

- Ticket: SOLR-12363, Duplicates with random search, cursors, and fixed seed. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments.
- Verdict: keep-open.
- Draft branch: solr-12363-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2026-10-04, audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: a SolrCloud view sorts with a RandomSortField dynamic field (random_123 asc, id asc), with the seed 123 kept in a cookie for 24 hours, and uses a cursor. The reporter sees results reshuffle on each request even with the same seed and cursor, and sometimes sees duplicate documents across pages. The Jira description was read in full (the evidence output is truncated at 1200 characters; the rest is the example URL and a second cursor example).

## Code reading

- solr/core/src/java/org/apache/solr/schema/RandomSortField.java:84-91 (getSeed): the seed is fieldName.hashCode() + context.docBase + (int) top.getVersion(), where top is the top-level DirectoryReader. The seed therefore changes when the index version changes (any commit or reopen that bumps the version) and depends on the segment's docBase.
- RandomSortField.java:140-146 (comparator): compareBottom and copy use hash(doc + seed) with the segment-relative doc id, so a document's sort value depends on its segment position and on the seed.
- RandomSortField.java:160-163 (compareTop): the cursor's top value is compared with hash(doc + seed) for the current segment and seed, so a sort value stored for the previous page is only meaningful for the same seed and the same segment layout.
- RandomSortField.java:187-189 (RandomValueSource.intVal): the function-query form uses the same seed.

So a cursor's stored random sort value is valid only for the index version and segment layout it was computed from. A commit between pages, or a merge that changes docBase, changes the values for the same documents, which is consistent with the ticket's report of reshuffles and duplicates. The reporter's observation without commits is not explained by this path alone and is not established.

## History

- No commit at the pin names SOLR-12363 (git log --grep at the pin: none).
- The file history of RandomSortField.java at the pin ends with b76a29d17c (SOLR-16427, error-prone rule) and 4d22f0c962 (SOLR-17097, Lucene 9.9.2). Neither is a fix for this ticket; the getSeed body was not traced through their diffs in this pass.

## Reproduction attempts

Not run. A reproduction needs a SolrCloud or standalone core at the pin with a RandomSortField sort and a cursor walk, run once on a static index and once with a commit between pages, recording whether ids repeat or are missed. Not built or run in this pass.

## Gap analysis

The code at the pin ties the random sort value to the index version and segment layout, so cursor pagination across commits is not stable with this field. That is the mechanism the ticket points to. Whether it accounts for the reported duplicates on a static index is not established; the reporter's cookie-seed reshuffle without commits is not explained by the code reading alone.

## Verdict justification

keep-open: the mechanism is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made. Whether the seed should be stable across versions is a design question.

Missing for a closing verdict: a cursor run at the pin on a static index and across a commit, recording duplicates and reshuffles, and a maintainer decision on whether the seed should depend on the index version.

## Limits

This record does not run the cursor walk, does not trace the file-history diffs, and does not post anything to JIRA or GitHub.
