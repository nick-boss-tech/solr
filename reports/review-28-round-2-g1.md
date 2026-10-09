# Round 2 verification, group 1 (SOLR-3657, SOLR-4841, SOLR-5065, SOLR-5505)

Read-only check on 2026-10-09 against `origin/pr-prepare`: the final drafts in `pr-drafts/update-processing/`, the receipts in `receipts/`, `material/review-opened-28-answers.md`, `reports/review-corrections-28.md`, `reports/review-opened-28.md`, and `reports/review-opened-28-g1.md`. Live PR fields were read with `research/gh.ps1 pr view`. Nothing was edited, committed, or posted.

Checks run on all four drafts: em dashes 0 (`LC_ALL=C grep -c`), first-person plural 0, internal vocabulary (gate, receipt, ledger, JUnit XML, rc=, takeover, live tip, pre-fix label) 0.

Titles: the owner's ruling O1 says every title must be accurate, and the 24 Jira-summary titles stand. Round 1's changelog-title replacements are therefore superseded for this group. Those titles are not counted as missing.

## SOLR-3657 (PR 5069)

Verdict: READY TO FLIP.

Part 1
- Round 1 finding 1 (title): superseded by O1. The live title is accurate.
- Round 1 finding 2 (Limits bold line): applied as specified, `**Two small gaps are left as they are.**`
- O2 follow-up: both Limits bullets state "A follow-up submission is planned." Applied.
- Proof counts (DocumentBuilderTest 17/17, TolerantUpdateProcessorTest 11/11, 2 failures on base) match the receipt at head 14edaca577c0.

Part 2: no disposition names this ticket.

Body: equal to the draft byte for byte (3,146 characters). Head 14edaca577c0 equals the receipt head. isDraft true.

## SOLR-4841 (PR 5070)

Verdict: NOT READY. One text fix (must), and two owner rulings.

Part 1
- Round 1 finding 1 (title): superseded by O1. The live title is accurate.
- Finding 2 (today link to base b6b2b8f): applied as specified. Base line 24 is package-private, and b6b2b8f is the merge base with upstream/main.
- Finding 3 (compile failure): applied, worded "does not compile, because the constructor is not public on base". The added protected-constructor sentence matches the receipt.
- Finding 4 ("two asserts"): applied as specified.
- Missed: the Proof bold line still says the new test "fails on base". The body and the receipt say it fails to compile.

Part 2 (item 5, live tip): "live tip" is gone. The draft says "the tested head" rather than "current head". The meaning holds, so this is accepted.

Proposed replacement (must):
Current: `**The tests pass at head `f8850ffd421`, and the new test fails on base.**`
New: `**The tests pass at head `f8850ffd421`, and the new test does not compile on base.**`

Owner rulings:
- Length: 3,862 characters. The owner accepted only the seven named long drafts. Trim or accept.
- O2: Limits names a test gap ("checks little beyond compiling") with no planned follow-up. Either add "A follow-up submission is planned for a test that runs language detection through a custom identifier." or accept it as a stated limitation.

Body: equal to the draft byte for byte (3,862 characters). Head e4c878627108 equals the receipt head.

## SOLR-5065 (PR 5071)

Verdict: READY TO FLIP, once the owner rules on length. No text change proposed.

Part 1
- Round 1 finding 1 (title): superseded by O1. The live title is accurate.
- Finding 2 (locale gap): applied differently, per O2. The draft reads "A follow-up submission for locale-aware parsing is planned." This replaces the round 1 wording "I can open a follow-up for it on request."
- The test-coverage and reference-guide bullets carry no plan sentence, as the owner ratified.
- Pre-fix claim: the receipt says only "the new test fails on unmodified base". The draft names `testGuessFieldTypeExponentFormsInferDouble`. That test is absent on base and present at head, and round 3 names it. Consistent, but the receipt does not name it, so the gate log should be cited if the owner asks.

Part 2: no disposition names this ticket.

Body: equal to the draft byte for byte (3,901 characters). Head ab894a996c8a equals the receipt head.

Owner ruling: length is 3,901 characters, not among the seven accepted long drafts. Trim or accept.

## SOLR-5505 (PR 5072)

Verdict: NOT READY. One item: the O2 follow-up sentence for the Limits gaps.

Part 1
- Owner call O3: applied as specified. The always-on `[core]` format stays, the type is `changed` (fragment at head 44c444aa5cd3), and the Choice section poses the opt-in alternative.
- Title typo: applied. The live title is accurate under O1.
- Limits bold line: present.
- O2: Limits names two gaps (the reload path is checked by reading only; the format is not tested against any log parser). Neither has a planned follow-up. 5505 is not in the corrections list, so O2 was missed here.
- Proof matches the receipt (TestInfoStreamLogging 2/2 with one failure, testMessagesNameTheCore; TestSolrIndexConfig 2/2; 2026-10-07). The `[core][IW]` check matches the test code.

Part 2: no disposition names this ticket.

Proposed addition to Limits, after the second bullet (for ratification, as a planned follow-up commitment):
`- A follow-up submission is planned for a reload test and a log-parser check.`

Body: equal to the draft byte for byte (3,388 characters). Head 44c444aa5cd3 equals the receipt head. Live title: "SOLR-5505: LoggingInfoStream not usable in a multi-core setup".
