# Skip record: SOLR-12302

- Ticket: SOLR-12302, Reproducing BasicDistributedZkTest failure. JIRA read in this pass: Open, resolution none, fix versions none, updated 2020-11-25, 4 comments (2018-05-29, 2018-11-23 x2, 2020-11-25); the links field shows "Duplicate SOLR-12980 (Resolved)".
- Verdict: keep-open.
- Draft branch: solr-12302-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: BasicDistributedZkTest.test fails with a seed reproduced on a Jenkins BadApples job; the failure is a distributed update returning Bad Request from the leader ("Exception writing document id 46 to the index"). The comments add more reproducing seeds (2018 on master, 2018-11 on master), a closed duplicate SOLR-12980, and a 2020 different failure ("too many open files") seen on macOS and a Linux box. The description was read in full through the Jira REST record; the evidence output truncates at 1200 characters.

## Code reading

- solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:419-445 (addDoc): a RuntimeException while writing a document is rethrown as SolrException with BAD_REQUEST and the message "Exception writing document id N to the index; possible analysis error." unless the core has a tragic exception, in which case it is SERVER_ERROR. This is the path that produces the message in the ticket's trace.
- solr/core/src/test/org/apache/solr/cloud/BasicDistributedZkTest.java:751 calls testUpdateProcessorsRunOnlyOnce("distrib-dup-test-chain-explicit"), the chain named in the ticket's failing request. The test file has been changed since the reports (commits cc2d450f93, 218ce981ba, d5ee8873eb are the most recent at the pin).

So the message and the chain in the ticket exist at the pin. Which underlying exception the seeds trigger (analysis, a tragic exception, or a test-framework limit) is not established by reading.

## History

- No commit at the pin names SOLR-12302 (git log --grep at the pin: none).
- SOLR-12980 is listed as a resolved duplicate; the closing of a duplicate is not a fix for this ticket's seeds.
- The 2020 "too many open files" comment is a different failure mode, reported from a macOS run and a Linux run with a large ulimit.

## Reproduction attempts

Not run in this pass. The record needs the test run at the pin with the seeds in the ticket (for example, ant test -Dtestcase=BasicDistributedZkTest -Dtests.method=test -Dtests.seed=AC56303341F3D845 with the test options from the ticket, or the Gradle equivalent) and the result. The Gradle build of the solr tree was not run in this pass.

## Gap analysis

The failing message and the update chain the ticket shows are present at the pin, so the reported failure path still exists in code. Whether the seeds still fail at the pin, and what the underlying exception is, is not established. The reporter's 2020 comment indicates a second, environment-dependent failure that a code reading does not settle.

## Verdict justification

keep-open: the failure path is present and the reproducing seeds have not been re-run at the pin, so close-cannot-reproduce is not supported (no run attempt). close-fixed and close-obsolete are not supported: no fix commit names the ticket, and the duplicate closure of SOLR-12980 is not a code change.

Missing for a closing verdict: a run of BasicDistributedZkTest at the pin with at least one seed from the ticket (recording pass or the exception under the Bad Request message), and a decision on the 2020 "too many open files" comment as a separate issue.

## Limits

This record does not run the test, does not build the Solr tree, and does not post anything to JIRA or GitHub.
