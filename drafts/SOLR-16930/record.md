# Skip record: SOLR-16930

- Ticket: SOLR-16930, "schema short class name support can use factories w/different names then specified name". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-08-11, updated 2023-10-15, no fix version. One comment, from hossman (2023-08-11), listing SPI names in Lucene 9x that more than one factory class uses.
- Verdict: keep-open. Unchanged from the earlier keep-open verdict; the earlier vm5 closeout (DONE, pre-remediation) is not reused.
- Draft branch: solr-16930-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:32-14:32 UTC.

## Ticket reading

A schema in the wild declares `<filter class="solr.EdgeNGramTokenizerFactory" .../>` in a filter position. The short-name lookup for `solr.<Name>` accepts a factory of the wrong kind, and the reporter argues the lookup should be stricter or the naming should be consistent. The only comment lists SPI names in Lucene that are used by more than one class, which is the root of the leniency. The ticket asks for a spec direction; the reporter (a maintainer) is unsure it is worth fixing.

## Code reading

- `solr/core/src/java/org/apache/solr/schema/FieldTypePluginLoader.java:255`: char filters go through `CharFilterFactory.forName(name, params)`.
- `FieldTypePluginLoader.java:319`: tokenizers go through `TokenizerFactory.forName(name, params)`. Token filters use the same pattern through Lucene's `TokenFilterFactory`.
- The short-name resolution itself (`forName`, and the SPI name lookup behind it) is in Lucene, not in this repository. Solr delegates to it.

What this shows at the pin: Solr does not implement the cross-kind lookup. The leniency the ticket describes comes from Lucene's SPI resolution, which Solr calls. A fix would be in Lucene's naming or lookup, or in Solr's schema loader if it wanted to validate the kind of factory it gets back.

## History

- No commit on the pinned head names SOLR-16930 (evidence tool: no hits).
- The hossman comment's list of duplicate SPI names is from Lucene 9x (2023-08-11). Not re-checked at the pin, because the list is in Lucene.

## Reproduction attempts

Not attempted. A reproduction would show that the schema loads with a mismatched factory, which the ticket already describes. Whether that should be an error is the open question, and a run does not answer it. This pass is review-only.

## Gap analysis

The behavior is a design question: whether a schema should be allowed to name a factory of another kind, and whether that is a defect or a permitted fallback. The ticket asks for that direction, and the code path is in Lucene. Without a decision, neither a close nor a PR has a defined correct behavior to implement.

Missing piece for a verdict change: a decision from the maintainers on whether cross-kind names should be rejected (a Lucene and Solr change) or accepted (close, with a docs note). With that decision, the record can name the change to make or close the ticket.

## Verdict justification

keep-open meets the rubric: the code path is read (`FieldTypePluginLoader.java:255, 319`), the mechanism is traced to Lucene's SPI lookup, and the missing piece is named (the spec direction). close-answered is not met: the docs do not settle the question. pr-needed is not met: no correct behavior is defined in the ticket or the code.

## Limits

This record does not reproduce the schema behavior, does not inspect Lucene's SPI code, and does not decide the design question. It does not post anything to JIRA or GitHub.
