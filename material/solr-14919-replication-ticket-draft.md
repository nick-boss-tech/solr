# Draft: follow-up ticket for the replication part of SOLR-14919

Status: DRAFT for Nick to file. Not filed. Nothing has been posted to JIRA.

Why it exists: the processor PR for SOLR-14919 accepts the forwarded commit values `leaders` and `replicas`. It does not fix replica recovery. Recovery commits carry no marker, so the processor treats them as client commits. The replication failure needs its own ticket and test.

## Summary

Replica recovery commit is rejected by IgnoreCommitOptimizeUpdateProcessorFactory when it has no commit_end_point marker

## Description

When a replica recovers, its recovery commit carries no commit_end_point marker. IgnoreCommitOptimizeUpdateProcessorFactory treats such a commit as a client commit, and rejects it with the configured status, 403 by default.

The processor PR for SOLR-14919 fixes forwarded commits only. It does not change this path.

Upstream commit 75b183196798 (SOLR-12933) changed the forwarded values to leaders and replicas, and commented out one test's use of the recovery marker. Its message gives no reason for that.

Open questions:

- Should recovery commits carry a marker, which would set commit_end_point=true on recovery commits and change which shards receive the commit?
- Should the processor check where a commit came from? Today any client can send commit_end_point, and the processor does not check the source.

Needed: a test that runs a replica recovery through the processor chain, on a cluster, and checks that the recovery commit is accepted.

## Before filing

- Re-read the recovery path at the current head. The line numbers in the processor PR draft were checked at the processor PR's own head only.
- Check the reporter's comments on SOLR-14919 for the replication symptom, and link them.
