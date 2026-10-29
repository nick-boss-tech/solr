# SOLR-18601 / PR 5253: approved fix offer posted, October 29, 2026

Status: POSTED; awaiting contributor or maintainer response.

- Explicit authorization: Nick said, "It looks good, you can post it," after approving the draft and asking for collapsible Linux verification.
- Comment: https://github.com/apache/solr/pull/5253#issuecomment-6489721489
- GitHub creation time (UTC): 2026-10-29T06:32:56Z
- Public PR head observed before and after posting: `32b91612edde057c19bf1a7963e8276b816551df` (contributor branch `SpookyJumpyBeans/solr:migrate-cleanup-on-failure`). No conversation comments or reviews were present before posting; the post-publication refresh shows only the approved comment and no reviews.
- Offered and tested fork head: `21804641d0ebbb5199a1c205f031af47c9424290`. `solr-18601-review` was read back at this head before posting. Its ancestry above the public PR head was verified while preparing the approved draft; the heads remained unchanged before posting.
- Published once through `gh.ps1 pr comment --body-file` with the approved absolute-path draft. A separate comment GET matched the approved draft after line-ending and final-newline normalization and confirmed the `<details>` / `<summary>` verification block.

## Proof and limits included in the comment

The comment links the pinned original-to-tested-head diff and the closing receipt at board commit `9b674191fb2a38f0deae3eacc807977df28271e0`, `receipts/PR-5253-cleanup-repair-round-5-closed.md`, read with its earlier receipts and corrections. It reports eighteen focused passes at seed 5253C0DE, the four cloud regressions passing at both additional seeds, and 61 neighbour tests with the one pre-existing CoreAdminHandlerTest failure confirmed on upstream `e59ce3c97d` with the same seed. It names tidy, changelog parsing, root and guide checks, separate enabled Error Prone compilation with zero warnings, and the 3/4/1 required fail-before results. Negative A is against the original contributor head, not upstream; upstream cascade failures are not claimed as independent regressions.

The text explains routing-rule restoration, shared-buffer replay, cleanup prerequisites and AssertionError handling, and discloses possible partial failed-migration data on the target. It offers pinned fetch / fast-forward commands from `solr-18601-review`. Public verification is collapsed under a summary naming the tested head, following the owner's new PR-formula requirement.

No JIRA comment, PR body edit, review submission, conversation resolution, contributor-branch change, approval or merge was performed. No Windows Gradle, compiler, formatter or tests ran. Runtime evidence comes from the Linux execution host's receipts; this posting pass did not independently inspect that host's raw logs/XML. Wait for human response before further contribution changes.
