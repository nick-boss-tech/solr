# Internal review of the 28: main side answers

Answers to `reports/review-opened-28.md` (2026-10-09). The description and Proof findings are accepted; the review lead's draft corrections should proceed on them. The PR bodies stay unchanged until the owner rules on the calls below; the main side then applies the corrected drafts to the live PRs.

## Already decided on the record (no owner call needed)

- **O5 (SOLR-12705 default scope): decided 2026-10-09.** The branch implements the mutateAtomicOperands() hook: atomic operands are mutated by default, with a per-class opt-out. That option was picked and recorded when the branch was built, and the R1 work re-pinned it. The description should state the default and the opt-out plainly; it is not an open choice.

## Recommendations recorded (owner practice: adopted unless he flags them)

- **O2 (follow-up wording):** adopt the standing rule's wording. Limits name the gap and state that a follow-up submission is planned; "on request" phrasing comes out of the drafts it appears in.
- **O3 (SOLR-5505):** keep the always-on `[core]` format and add a Choices section posing the opt-in alternative. The change is the ticket's point; the type stays `changed`.
- **O4 (SOLR-6045 factory path):** scope the claim to the merger path and name the factory path in Limits with a planned follow-up. The R2 ruling governed merger classification; the factory wrapping is pre-existing behavior outside it.
- **O8 (SOLR-5887):** scope the claim to the paths the change covers; the ClassificationUpdateProcessor bypass is named in Limits with a planned follow-up, not fixed on this branch.

## Flagged for the owner as a genuine call

- **O1 (titles, includes O6):** the main side's recommendation is to keep the Jira-summary titles as the base rule (the project's convention) and fix only the titles that are wrong on their face, using the changelog title for those: SOLR-5754 (names a class that does not exist), SOLR-13943 (names the old method), SOLR-16673 (the Jira summary does not describe the change), SOLR-5505 (spelling). Retitling all 28 to changelog titles is the alternative. This one is flagged rather than adopted because it is 28 public writes with a real convention tradeoff.

## Main side actions

- **O7 (SOLR-7504 null-count defect): verified and in progress.** The mutator loops over `src.getValues()` with no null check (CountFieldValuesUpdateProcessorFactory.java, about L78 at 22b77196e662); `getValues()` returns null for a null-valued field, and the base code's `getValueCount()` stored 0. A fix lane is running: reproduce with a new test first, guard the scan, in-lane green required before pushing, then a full re-gate (GATE PENDING). The PR stays a draft until the re-gate records green.
- **SOLR-7022 receipt:** restored with its 2026-10-05 gate counts (2/2 and 7/7) and its grounded pre-fix proof, which the earlier refresh had dropped.

## O1 ruled by the owner (2026-10-09)

The owner does not care which source a title comes from; the rule is that every title is accurate. Applied the same day by the main side: the four titles that were wrong on their face are fixed on the live PRs (SOLR-5754 names StreamingSolrClients and what changed; SOLR-13943 no longer names the old location of the test; SOLR-16673 uses its draft's labelled title; SOLR-5505's spelling is corrected). The other 24 titles stand as opened, and SOLR-16356's title stands under the same rule (O6 closed). Any further title change in the corrections pass must be justified by accuracy, not by source.

## Confirmation item 4 ruled by the owner (2026-10-09): strike the internal jargon

The owner ruled against accepting the process phrases: a "gate entry" is internal record-keeping that Solr maintainers do not know about, and "fresh JUnit XML" is the same kind of internal shorthand. Both come out, and the ruling generalizes: public PR text states results plainly and never uses the internal vocabulary (gate, receipt, ledger, fresh JUnit XML, rc=0, pre-fix proof as a label). A plain-language pass over all 28 drafts' Proof sections is running on the main side under this rule, preserving every count, head, and date. Items 1, 2, 3, 5, and 6 still carry the main side's recorded recommendations.
