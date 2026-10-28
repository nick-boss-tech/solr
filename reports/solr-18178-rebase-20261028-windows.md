# SOLR-18178: rebase and PR body update, Windows, 2026-10-28

Status: DONE (rebase, fork push and authorized body edit); implementation proof pending at the new head.

Source: Nick's direct October 28 request to rebase PR 4968 and update its body. No pooled implementation assignment was taken. Board snapshots `be344beb34313a759cbb00c749e73f32545db771` and then `7f93a5fa3a9d217ba7d025a2bb3cf2c32ab20828` were checked before the new gate; no competing ticket gate claim was found.

## Result and scope

- PR: https://github.com/apache/solr/pull/4968
- Fork branch: `nick-boss-tech/solr:solr-18178-verify`.
- Old public head: `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2`.
- New public head: `d6abc36c94687dc5744a7573bafb1ce473acc75f`.
- Rebase base: `35e2be2a7de9855a0f909613065ba22243097102` on apache/solr main.
- The authorized lease force-push pinned the old fork SHA. The helper's read-back, a separate GitHub fork-ref read and the live PR head match the new SHA.
- GitHub reports MERGEABLE / UNSTABLE. The source conflict is resolved; this does not establish passing checks at the new head.

The clean existing ticket worktree was fast-forwarded to the live old PR head, then all 13 non-merge ticket commits were replayed. The only conflict was in the initial commit's `DownloadConfigSet.java` tree walk. Kept main's SOLR-18320 delegation to `Utils.zipDirectory`; that production file is identical to the rebase base. Removed the redundant `toZipEntryName` helper and its direct unit test in the first rebased commit. The raw ZIP-entry integration assertion remains, so the new expected download-class count is four rather than the historical five.

Upload and filesystem-listing production, upload/listing tests and designer tests are unchanged from the old head. The guide retains main's other edits and the ticket's four-line upload note. New commit `d6abc36c94687dc5744a7573bafb1ce473acc75f` aligns the changelog title with the remaining production changes. All outbound commits retain Nick Shanin's ICLA identity.

Final delta: eight ticket files, 428 additions and 18 deletions. Ancestry, clean worktree, `git diff --check`, scope, authorship and absence of conflict markers/helper references were checked. No Gradle, formatting task, compilation, tests or local test queue ran on Windows.

## Authorized PR body edit

The live body and discussion were saved in `research/scratch/pr4968-rebase-20261028/`. The approximately 2,750-character draft explains main's existing exporter, the remaining upload/listing changes and their limits, and pending proof at the exact new head. It preserves the AI header/footer and labels old results historical. Em/en dash checking was clean.

The first REST PATCH hit a TLS handshake timeout. A fresh read confirmed the old body and discussion unchanged at the new head; the retry succeeded. A separate read-back at **2026-10-28T04:20:01.8479880Z** matched the draft and confirmed the exact head, unchanged discussion (two comments and one COMMENTED review) and `isDraft=false`. The PR title and ready/draft state were not changed. No public comment, JIRA write, approval, merge or close was posted.

Local evidence: `before-body-edit.json`, `live-body-before.md`, `body-draft.md`, and `after-body-edit.json` in the scratch folder. The lease-push helper's legacy credential loader was corrected to the current workspace token path and now refreshes stale inherited credentials; no credential was printed or copied.

## Required new proof

Posted and verified `gates/solr-18178-rebased-head-round-1.md` at board commit `8e58ce59033c463ec349dc5435eb23c2409b4c4e`, the live tip at that verification. It requests clean tidy, explicitly enabled Error Prone compilation, root `check -x test`, four focused classes, repeated upload seeds, and three named upload regressions against unchanged main production. The base scratch test removes only direct references to new helpers absent on base, as documented in the job; selected assertions and production are unchanged.

Receipt target: `receipts/SOLR-18178-rebased-20261028.md`. If any repair moves the head, require a superseding job and fresh exact-head proof. The old receipt and green CI do not cover this head. The ticket remains **Implemented, Proof Pending**. The gate authorizes no further public PR writes.
