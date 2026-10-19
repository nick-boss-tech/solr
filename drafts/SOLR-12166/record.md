# Skip record: SOLR-12166

- Ticket: SOLR-12166, Race condition in rejoinElection and registering replica. JIRA status not recorded in the B2 reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-12166-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B2 reading (reports/2026-10-18-skip-repro/B2-removed-round-2.md, vm3, round 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Race condition in rejoinElection and registering replica.

## Code reading

Missing: whether the publish-DOWN-after-ACTIVE race can happen without the LIR flag, which the ticket's trigger depends on and which is gone (see SOLR-9913). Paths to read or test: solr/core/src/java/org/apache/solr/cloud/Overseer.java:492 (rejoinElection) and ZkController.preRegister (ZkController.java:2126).

## History

- None cited in the B2 reading.

## Reproduction attempts

Not attempted. The B2 scope ran no builds or tests.

## Gap analysis

The code reading is inconclusive; the missing piece is named in the code reading below.

## Verdict justification

keep-open: the code reading does not settle the question; the B2 report names the missing piece below.

Missing for this verdict: The missing piece noted in the code reading below.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
