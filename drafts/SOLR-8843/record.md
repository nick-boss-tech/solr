# Skip record: SOLR-8843

- Ticket: SOLR-8843, Missing fallback for NoChildrenForEphemeralsException on ZkController.getLeaderPropsWithFallback for rolling upgrade. JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-8843-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Missing fallback for NoChildrenForEphemeralsException on ZkController.getLeaderPropsWithFallback for rolling upgrade.

## Code reading

getLeaderPropsWithFallback and NoChildrenForEphemeralsException have no hits in solr/core/src or solr/solrj/src, and git log -S finds neither in this history. ZkController.java:1709 (getLeaderProps) has no ephemeral fallback.

## History

- None cited in the B1 reading.

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head (fixed or removed), as described above.

## Verdict justification

close-obsolete: the feature or class the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the B1 reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
