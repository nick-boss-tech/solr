# Skip record: SOLR-10759

- Ticket: SOLR-10759, TestUseDocValuesAsStored sometimes fails. JIRA read in this pass: Open, resolution none, fix versions none, 1 comment (2017-05-26, the full log of the same failure); updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J and L).
- Draft branch: solr-10759-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: TestUseDocValuesAsStored.testMultipleSearchResults failed once on a Jenkins 6.6 build with "mismatch: 'myid1'!='myid' @ response/docs/[0]/id": the first document in the response was not the expected one. The reporter attached the log. The 2017 comment only attaches the full log.

## Code reading

- solr/core/src/test/org/apache/solr/schema/TestUseDocValuesAsStored.java:405-431 (testMultipleSearchResults): the test indexes three documents (myid1, myid2, myid3) and checks the response with assertJQ using req("q", "id:myid*", "fl", "*", "sort", "id asc") (line 431). With the sort on id, the order of the three documents is fixed, so the ordering that the ticket's mismatch depends on is deterministic at the pin.
- The sort parameter was added by commit 4b6e545d3a (SOLR-17556, 2025-01-10, "Add solr-home option for examples"), whose subject does not mention SOLR-10759 and which added the sort as part of that change. It is an ancestor of the pin.

So the test at the pin cannot order these three documents by chance, but the change that did so is not a fix for this ticket, and no commit names SOLR-10759.

## History

- No commit at the pin names SOLR-10759 (git log --grep at the pin: none).
- The test file's history before SOLR-17556 was not traced in this pass.

## Reproduction attempts

Not run. A repeated run of TestUseDocValuesAsStored at the pin (with seeds from the ticket) would show whether the test still fails; a sort-free variant of the query (the 2017 version, if recoverable) would be needed to reproduce the ordering flake directly.

## Gap analysis

The current test does not depend on the order the ticket's failure relied on, but no commit ties that to this ticket. Whether the 2017 failure was an ordering flake or something else cannot be established from the log the ticket attaches.

## Verdict justification

keep-open: the current test is deterministic in the way that matters, but the change that made it so is not linked to this ticket as a fix (SOLR-17556, unrelated subject), so close-fixed is not supported by the record; close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a run of TestUseDocValuesAsStored at the pin with repeated seeds (zero failures would support close-cannot-reproduce), or a maintainer statement that the SOLR-17556 change resolves this ticket (then close-fixed with that commit cited by code path).

## Limits

This record does not run the test, does not recover the 2017 query form, and does not post anything to JIRA or GitHub.
