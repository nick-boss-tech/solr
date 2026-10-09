# Review round 2: group 7 (SOLR-16356, SOLR-16655, SOLR-16673, SOLR-16910)

Done 2026-10-09 by a read-only verification subagent for the lead (Claude Code, working for Nick Shanin). Drafts, receipts, the receipts addendum and the round 1 and corrections reports were read from `origin/pr-prepare` at `0870cb8f092`. Live PRs were read with `research/gh.ps1 pr view <n> --repo apache/solr --json "body,title,headRefOid,isDraft"`. Source lines were checked with read-only `git show` at the submit heads. No builds, tests, GitHub writes, or edits.

## Summary

- SOLR-16356 (PR 5092): **READY TO FLIP.**
- SOLR-16655 (PR 5093): **NOT READY.** One item: the planned follow-up sentence is missing from Limits.
- SOLR-16673 (PR 5094): **NOT READY.** One item: the Schema Designer gap in Limits has no planned follow-up sentence. Everything else is applied.
- SOLR-16910 (PR 5095): **READY TO FLIP.**

Live bodies equal the final drafts byte for byte for all four PRs, all four are still drafts, and all four heads match their receipts. Title notes are in each section.

## SOLR-16356 (PR 5092)

**Verdict: READY TO FLIP.**

Part 1
- Title (round 1, owner call O6): stands. The answers file says the title stands under the accuracy rule. Live title: `SOLR-16356: DBQ is noisy during core close`. Applied as ruled.
- Limits bold summary: present ("A second close-time ERROR is not fixed here."). Applied as specified.
- Planned follow-up sentence (corrections, O2): applied as specified. "A follow-up submission is planned to trace that commit." replaces "I can open a follow-up to trace that commit on request."
- Proof "as recorded in the gate entry" (corrections item 4, owner ruling to strike): applied differently. The sentence is now "Without the fix, this test fails." The receipt and the addendum at `39c0585072f` record pre-fix PASS, so the claim is supported. The failing output itself is not recorded.
- Plain-language pass (commit 78ce00d90af): "Gate run" became "Run", and "Module check rc=0" became "The module checks pass". Counts (1/1, 5/5) and head are unchanged.

Part 2
- "Gate entry" item: applied. The draft and the live body contain no gate, receipt, or rc= wording.

Body comparison
- Live body equals the draft byte for byte (2,770 bytes, LF line endings, trailing newline). isDraft true.
- Head `39c0585072f0eacc0767a173bab3f1d528db64f7` equals the receipt head `39c0585072f0`.

Proposed replacement text: none.

## SOLR-16655 (PR 5093)

**Verdict: NOT READY. One remaining item: the planned follow-up sentence is missing from Limits.**

Part 1
- Limits "confined to" sentence (round 1): applied as specified. The text matches the round 1 suggestion exactly.
- Proof "That is the commit before this change" (round 1): applied differently. It now reads "That is the parent of the last commit on the branch, `5e2317443f41`, which adds the rule that a child document is walked only under a selected field." Round 1 suggested "adds the selector gate". The plain-language ruling removed "gate", so this is accepted.
- Link label "gated descent" (round 1): applied as specified. Now "child documents under selected fields".
- "08:03 MDT" (round 1 item 5): removed. The Proof now says "run 2026-10-09". The "Gate green" and "From fresh JUnit XML" wording is also gone. Applied.
- Title (owner call O1): stands, one of the 24. Live title: `SOLR-16655: Indexing nested documents and URPs`.
- Planned follow-up sentence (the corrections report lists 16655 among the 22 drafts that carry one): MISSING. The draft and the live body contain no follow-up sentence. Limits bullet 2 ("They do not send an update request.") and the combined-tree gap in bullet 1 name gaps with no plan.
- Not from round 1, optional: Limits bullet 1 repeats the bold summary verbatim ("So this change lands first, and SOLR-12705 rebases onto it."). The presentation rule states each claim once.

Part 2
- No dispositions apply to this ticket. No internal vocabulary found.

Body comparison
- Live body equals the draft byte for byte (4,836 bytes). isDraft true.
- Head `5e2317443f4116a9e97330b0d6ce175cf44b54df` equals the receipt head `5e2317443f41`.

Proposed replacement text (owner ratifies; the wording is a new commitment and no ratified wording exists for this draft)
- Limits bullet 2. Old: `They do not send an update request.` New: `They do not send an update request. A follow-up submission is planned to check the combined tree after SOLR-12705 rebases, and to add a test that sends an update request.`
- Optional, Limits bullet 1. Old: ` So this change lands first, and SOLR-12705 rebases onto it.` New: (delete the sentence).

## SOLR-16673 (PR 5094)

**Verdict: NOT READY. One remaining item: the Schema Designer gap in Limits has no planned follow-up sentence.** Everything else is applied. If the owner reads the O2 rule as one sentence per draft, this item falls away and the PR is ready.

Part 1
- Schema Designer sentence (round 1): applied differently. The draft adds "in the" and links the float and double check to L300. Checked at head `d7170b12f312`: L338 is `isIntOrLong`, and L300 is the `parsePossibleDouble` call in the float and double check. `parsePossibleLong` returns null for an unparseable value (ParseLong L111 and L118), so the guess moves on as the sentence says. The sentence is supported.
- "feeds" changed to "calls" (round 1): applied as specified.
- Title (owner ruling, "SOLR-16673 uses its draft's labelled title"): applied. The live title equals the labelled title in the body.
- Earlier head labelled `d5c19e64ba1b` (corrections, owner item 5): applied. The facts match the addendum: changelog parse, tidy, Error Prone compile, pre-fix PASS, 43 of 43, and the module check.
- Current head facts (round 1): applied. The one commit past the earlier head changes only the changelog title, as the receipt's top-up entry records.
- Planned follow-up sentence for the non-string key and `_root_` errors: applied as specified.
- Schema Designer gap: MISSING a planned sentence (O2). Limits bullet 1 names the gap ("The link between the two is not tested here.") and no plan follows. The five ratified gaps without a plan (5065, 12705, 14718, 14262, 7022) do not include 16673.

Part 2 (rc=0 and Proof items)
- Applied. No rc=, "returns 0", or fail-before wording remains. Each earlier-head fact is labelled with `d5c19e64ba1b` and comes from the addendum, which the owner accepted as a source. The Proof summary "The only change since the earlier tested head is the changelog title" is supported.

Body comparison
- Live body equals the draft byte for byte (4,281 bytes). isDraft true.
- Head `d7170b12f312b364cb280b943a042707e553ea64` equals the refreshed receipt head and the PR head.

Proposed replacement text (owner ratifies)
- Limits bullet 1. Old: `The link between the two is not tested here.` New: `The link between the two is not tested here. A follow-up submission is planned to test that link.`

## SOLR-16910 (PR 5095)

**Verdict: READY TO FLIP.**

Part 1
- Bold summary (round 1): applied as specified. Live and draft text: "The INFO summary and the slow-update WARN now use the same message, built before the request details are cleared."
- "What this change does" order (round 1): applied. The message is built, the request details are cleared, then the INFO line and the slow WARN are logged from that one message.
- Plain-language pass: "Gate run" became "Run", and "Module check rc=0" became "The module checks pass". Counts match the receipt (3/3, one base failure, 2026-10-07, head `9fce3e9a7058`).
- Planned follow-up sentence (corrections, O2): applied as specified. "A follow-up submission is planned for it."
- Title (owner ruling): stands. Live title is the Jira summary, unchanged.
- Note, not a draft item (round 1): the submit branch history includes `8eabf2b30cd`, which adds `SOLR-16910-TESTING.md`. A later commit deletes it. The PR file list is clean, and a squash merge removes it.

Part 2
- No dispositions apply. No internal vocabulary found.

Body comparison
- Live body equals the draft byte for byte (3,263 bytes). isDraft true.
- Head `9fce3e9a70582bcf977595a559acc2795b3774ca` equals the receipt head `9fce3e9a7058`.

Proposed replacement text: none.

## Vocabulary and format check (all four final drafts)

- Searched for gate, receipt, ledger, JUnit, rc=, takeover, live tip, pre-fix, fresh, dispositioned, fail-before, and MDT. No hits in the four drafts. The only hit was the standard footer's "review" in "research, implementation, review, and drafting".
- Em dashes: 0 in each draft. No first-person plural or singular pronoun appears in any draft.
- Titles: three are Jira summaries that stand under the owner ruling, and SOLR-16673 uses its labelled title.
