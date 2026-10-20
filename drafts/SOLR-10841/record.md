# Skip record: SOLR-10841

- Ticket: SOLR-10841, TestTlogReplica.testRecovery sometimes fails. JIRA read in this pass: Open, resolution none, fix versions none, 8 comments (2017-06-07 to 2018-10-23); the 2018-10-23 comment says the failure is still seen in Jenkins; updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J and L).
- Draft branch: solr-10841-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: TestTlogReplica.testRecovery fails in Jenkins with "Can not find doc 8" at checkRTG, reported by the reporter on a seed they could not reproduce locally. The 2018 commits name the failure and the fix; a 2018-10-23 comment reports that the failure still appears in Jenkins.

## Code reading

- Fix commit 39256e8348 (SOLR-10841, 2018-05-21, "TestTlogReplica.testRecovery sometimes fails") is an ancestor of the pin (checked). It changes solr/core/src/test/org/apache/solr/cloud/TestTlogReplica.java: a retry loop (three attempts) for the update that must reach a minimum replication factor of 2, with the comment that the leader fails to communicate with the follower with a broken pipe when the doc is added immediately; the checkRTG expectation moves from 9 to 10 documents.
- Commit a4f83f66ea (SOLR-10841, 2018-05-21, "Remove BaddApples annotation") is also an ancestor of the pin.
- At the pin, the retry loop is present (TestTlogReplica.java:659 and :703, log "Min RF not achieved yet. retrying"), and checkRTG(3, 10, ...) is at :712.

So the fix in the ticket is a test-side workaround: it retries a write until the minimum replication factor is reached. It does not address the broken-pipe failure between leader and follower that the comment describes as unexplained.

## History

- The 2018-10-23 comment reports the failure again after the fix (a Jenkins run on master). That report is not answered in the ticket.
- No commit at the pin names the product-side cause (the leader-to-follower broken pipe).

## Reproduction attempts

Not run. A reproduction needs TestTlogReplica.testRecovery at the pin with the ticket's seed (for example repeated runs with the seed from the Jenkins log), recording whether checkRTG fails.

## Gap analysis

The test has a retry that the ticket's fix added, and the 2018 report says the failure still happened afterwards. The underlying broken pipe is not explained, so the product behavior is not shown to be fixed.

## Verdict justification

keep-open: the fix is test-side and the later report says the failure recurred, so close-fixed is not supported by the record; close-cannot-reproduce needs a run attempt, and none was made. close-obsolete is not applicable (the test still exists and runs, with the BadApples annotation removed).

Missing for a closing verdict: repeated runs of TestTlogReplica.testRecovery at the pin (including the ticket's seed) with zero failures (close-cannot-reproduce), or a product-side explanation of the broken pipe with a fix commit (close-fixed).

## Limits

This record does not run the test, does not trace the leader-follower communication path, and does not post anything to JIRA or GitHub.
