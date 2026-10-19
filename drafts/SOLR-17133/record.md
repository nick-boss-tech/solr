# Skip record: SOLR-17133

- Ticket: SOLR-17133, "CloudAuthStreamTest fails with PRS=true". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2024-01-29, updated 2024-02-09, no fix version. Description empty; no comments. Reporter and assignee is Noble Paul, per the queue note.
- Verdict: keep-open. Unchanged; this record replaces the earlier one, which relied on a single passing run whose PRS setting was not recorded.
- Draft branch: solr-17133-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 15:01-15:02 UTC.

## Ticket reading

The ticket title says `CloudAuthStreamTest` fails when PRS (per-replica state) is on. The description is empty and there are no comments, so the ticket gives no stack trace, seed, or configuration.

## Code reading

- `solr/test-framework/src/java/org/apache/solr/cloud/SolrCloudTestCase.java:147-155`, `configurePrsDefault()`: a test class annotated `@NoPrs` runs with PRS off. Any other class takes the PRS default from the system property `PRS_DEFAULT_PROP`, and when that is unset, the value is chosen at random (`LuceneTestCase.random().nextBoolean()`). The chosen value is then written back to the property.
- `solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/stream/CloudAuthStreamTest.java`: has `@BeforeClass setupCluster()` (line 89) and no `@NoPrs` annotation. Its PRS setting is therefore random per run, unless the property is set.

What this shows at the pin: the test runs in PRS mode on a random share of runs. A "fails with PRS=true" report is reproducible only when the property is forced to true; a random run may or may not take that path.

## History

- No commit on the pinned head names SOLR-17133 (evidence tool: no hits).

## Reproduction attempts

- Earlier run, from the A3 report (`reports/2026-10-18-skip-repro/A3-other-round-1.md`): `:solr:solrj-streaming:test --rerun --tests org.apache.solr.client.solrj.io.stream.CloudAuthStreamTest`, seed B2EA203DEE3A89E2, build successful, 19 tests, 0 failures, 0 errors, 0 skipped. The PRS value for that run was not recorded, so the run does not show the PRS=true case either way. Conclusive: no.
- Not run in this pass: a run with the PRS default forced to true, over several seeds. That is test execution and is outside this review-only pass.

## Gap analysis

The earlier passing run cannot be read as evidence against the PRS=true failure, because its PRS setting is unknown. The question the ticket asks is still open: does the test fail when PRS is forced on, over several seeds?

Missing piece for a verdict change: runs of `CloudAuthStreamTest` with the PRS default forced to true (the `PRS_DEFAULT_PROP` system property set to true), at several seeds, with the results and the failing output if any. A failure gives the stack and a fix target; repeated passes with PRS forced on support a close as cannot reproduce.

## Verdict justification

keep-open meets the rubric: the code path is read (`SolrCloudTestCase.java:147-155`, `CloudAuthStreamTest.java:89`), the earlier attempt is listed with its PRS value unknown, and the missing piece is named (forced-PRS runs at several seeds). close-cannot-reproduce needs a reproduction attempt on the pin that covers PRS=true, and none does, so it is not met. pr-needed is not met: no failing output is shown.

## Limits

This record does not run the forced-PRS case and does not identify the failure. It does not reconcile the earlier run's PRS setting. It does not post anything to JIRA or GitHub.
