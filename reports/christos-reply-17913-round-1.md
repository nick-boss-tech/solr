# Report: SOLR-17913 hold and PR 5258 impact assessment (phone agent)

Status: COMPLETE. Fulfills `assignments/christos-reply-17913-hold-and-5258-impact-round-1.md` items 1 and 2; item 3 (draft sync reply) is at the end, not posted.

- Trigger: Christos Malliaridis on SOLR-17913, 2026-10-30, comment 18134861: collections designs updated and WIP, the details card "will not be used in this format anymore", sync before proceeding, a PR with the current changes is not worth reviewing yet.
- Hold recorded: no PR and no further implementation on SOLR-17913 until Christos signals the new design or a sync happens. The work stays on `solr-17913-collection-details-card` (tip `4003196af10d`, gated PASS at its parent `0a04d2239ee`).

## Impact on the open PRs

Only PR #5258 (SOLR-17914, draft, head `2ec6db48f542fb29080ee3dd52727b5b8f37e148`) contains 17913 work. PRs #5256 and #5257 were checked file by file: no 17913 content in either.

The 17913-owned content inside #5258 (17 files, +1467/-1 total) is:

- `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/CollectionDetailsCard.kt` (+90): the card itself.
- `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/domain/CollectionDetails.kt` (+37): the card's data model.
- `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/collections/CollectionDetailsCardTest.kt` (+69): the card's test.
- About 10 of the +35 lines in `ui/shared/src/commonMain/composeResources/values/strings.xml`: the card's string entries (the standalone 17913 branch's strings delta is exactly +10; the rest belongs to the create collection form).

Coupling: a tree-wide search of the 17914 branch finds `CollectionDetails` and `CollectionDetailsCard` referenced only by the card and its test. No create-collection file (form, repositories, errors, preview, dropdown) uses them. Removing the three Kotlin files and the card's string entries cannot break the dialog's compilation or behavior on the source evidence; a build check after removal would confirm. The PR would drop to 14 files and about +1261 lines, all 17914.

## Options

- A, leave #5258 as is: the 17914 review then also carries a component Christos has just said will not be used in this format; the disclosed inclusion was made before his design update.
- B, remove the card from #5258 (recommended): one commit deleting the three files and the card's strings, plus a body edit dropping the 17913 disclosure. Nothing is lost; the card stays gated on its own branch for the sync. #5258 has no maintainer approval or review comments, so no approval freeze applies.
- C, hold #5258 too: not supported by any signal from Christos about the dialog itself.

Awaiting Nick's call between A and B; no branch or body change was made by this report.

## Draft Jira reply for SOLR-17913 (NOT posted; assumes option B; needs Nick's per-text go)

{{{
Thanks for the update. I will hold the details card until the collections designs settle; the current implementation stays on my branch in case parts of it are useful in the new format. The card also rides inside the Create Collection dialog PR as a disclosed inclusion, and I will take it out of that PR so the dialog can be reviewed on its own. Happy to sync once the new designs are ready; comments here or a short call both work for me.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
}}}
