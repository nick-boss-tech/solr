# Skip record: SOLR-12844

- Ticket: SOLR-12844, TestDistributedStatsComponentCardinality failure.
- Verdict: keep-open.
- Draft branch: solr-12844-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 1 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-10-09, updated 2019-06-08, no fix versions, no links, no comments. The report: a nightly run on the 7.x branch failed in `TestDistributedStatsComponentCardinality.test`, with the seed `CCA4941EC30785C` printed by the test runner. The ticket names the nightly job, not a stack trace or assertion text.

## Code reading

Read at the pin:

- `solr/core/src/test/org/apache/solr/handler/component/TestDistributedStatsComponentCardinality.java:110`: `public void test() throws Exception`. The test exists at the pin under the same name, with no `@AwaitsFix` or `@BadApple` marker found in the file.
- No commit at the pin names SOLR-12844 (the evidence tool lists none).
- The test's cardinality assertions (the HyperLogLog-style distinct counts it compares across shards) were not traced in this pass; the failing assertion from the nightly run is not in the ticket.

## History

- The test's history was not traced in this pass.
- No commit at the pin names the ticket.

## Reproduction attempts

Not run in this record. The record that the batch 08 audit marked suspect cites an earlier run (the A1 flake round, `reports/2026-10-18-skip-repro/A1-flake-round-1.md`, row SOLR-12844): `:solr:core:test --rerun --tests ...TestDistributedStatsComponentCardinality`, seed `A0D025C0835FBE2A`, result PASS, 1/0/0. That run used a different seed from the ticket's `CCA4941EC30785C`, so it does not reproduce the ticket's failure. This pass ran no builds or tests.

## Gap analysis

- The run data cited by the earlier record uses a different seed from the ticket, so it does not show the ticket's failure is gone.
- The ticket's failing assertion is not recorded, so the failure cannot be matched to a line at the pin.
- The test is present and unmarked at the pin.

## Verdict justification

keep-open: the test is present, the ticket's seed has not been run at the pin, and the only run cited uses a different seed and passed. close-cannot-reproduce needs a reproduction attempt with the ticket's seed at the pin; close-fixed and close-obsolete do not fit.

Missing for this verdict: a run of `TestDistributedStatsComponentCardinality` with seed `CCA4941EC30785C` at the pin, with the failing assertion or the pass result and the test XML.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
