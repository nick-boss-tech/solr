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

## Confirmations ratified by the owner (2026-10-09)

The owner ratified the five remaining items from `reports/review-corrections-28.md`: (1) the planned follow-up commitments in the 22 drafts stand; (2) the five gaps without a plan sentence (5065, 12705, 14718, 14262, 7022) stay as stated limitations; (3) the SOLR-7022 Proof line about DirectUpdateHandler2CommitWaitTest is dropped (done in the drafts with this entry); (4) the seven long drafts are accepted as the complex cases; (5) the receipts addendum is an accepted source for SOLR-16673's earlier-head facts. With item 4 ruled earlier (plain language) and the plain-language pass pushed at 78ce00d90af, the corrected drafts are final and the main side applies them to the 28 live PR bodies.

## Six verification findings dispositioned by the main side (2026-10-09)

The verification of the plain-language pass (reports/review-plain-language-28.md) raised six findings. The owner asked why they were folded into round 2 instead of fixed; five are record-accuracy fixes and were made directly, and the sixth is covered by an earlier ruling:

1. SOLR-7022 receipt: corrected. The commits after the original gate change no production code, but 6a233ab2fdb adds a test file; the receipt now says so, and notes the top-up did not run that class.
2. SOLR-6973: the "(by reading)" framing is gone. Proof states the base run (7 tests, 1 failure) and identifies the failure as the new test because the base code sets both values it checks for absence.
3. SOLR-13696 pre-fix heads: accepted as sourced from the round 3 close-out answers and the round 8 addendum, on the same basis as the ratified SOLR-16673 addendum ruling.
4. SOLR-13943: "all in normal mode" dropped; normal mode is claimed only for the class the record supports.
5. "Live tip" replaced in the four drafts (4841, 5754, 5939, 7022) with "current head" phrasing.
6. SOLR-5941 and SOLR-13265: the method phrases the records do not cover are removed; the run claims stand as recorded.

## Record additions and round 2 fix pass (2026-10-09, main side)

Two title fixes were applied by the main side under the O1 accuracy ruling on 2026-10-09 and reported to the owner in chat, but were missing from this file: SOLR-12864 now reads "Add test coverage for echo with mapUniqueKeyOnly in JSON updates" and SOLR-12245 now reads "Name the replica in failed distributed update errors". Six title fixes in total stand: 5754, 13943, 16673, 5505, 12864, 12245.

Round 2 (reports/review-28-round-2.md) verified the corrections. The record-accuracy fixes were applied directly to the drafts and the affected PR bodies, and the receipts were corrected where they carried the same errors: the merge-commit and tree wording in the SOLR-5754 and SOLR-5939 drafts and receipts (1ddbf36202d is the merge commit; its tree is f2e33340f0ea); SOLR-4841 "does not compile on base"; SOLR-5887's scoped bold line; SOLR-5941's restored base-not-run sentence; SOLR-13943's two unsupported sentences removed; the planned follow-up sentences the corrections round missed, added for SOLR-5505, SOLR-6973, SOLR-16655 and SOLR-16673; and the SOLR-7504 receipt History counts corrected to 29 and 42, as its gate receipt in the ledger records. Items round 2 marked for owner ratification (the remaining proposed titles, the SOLR-7022 Limits and Proof replacements, the SOLR-12705 follow-up sentence, the extra lengths, and the SOLR-6045 and SOLR-12703 branch changelog edits with re-gates) were put to the owner on 2026-10-09 and are not applied here.
