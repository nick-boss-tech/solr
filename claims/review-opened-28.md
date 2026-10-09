# Claim: internal review of the 28 opened update-processing PRs

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/review-opened-28.md` (commit `42d9492fc79`), which follows the openings in `reports/open-update-29-prs.md` (commit `e2cafde3a88`). The 28 pull requests are open as drafts on apache/solr, in the assignment's order. The review decides which can flip to ready. It is read-only.

Seven groups, one subagent each, four pull requests per group:
- Group 1: SOLR-3657 #5069, SOLR-4841 #5070, SOLR-5065 #5071, SOLR-5505 #5072. Report: `reports/review-opened-28-g1.md`.
- Group 2: SOLR-5754 #5073, SOLR-5939 #5075, SOLR-5941 #5076, SOLR-5887 #5074. Report: `reports/review-opened-28-g2.md`.
- Group 3: SOLR-6045 #5077, SOLR-7504 #5081, SOLR-12703 #5085, SOLR-12705 #5096. Report: `reports/review-opened-28-g3.md`.
- Group 4: SOLR-6065 #5078, SOLR-6973 #5079, SOLR-7022 #5080, SOLR-11475 #5082. Report: `reports/review-opened-28-g4.md`.
- Group 5: SOLR-11483 #5083, SOLR-12245 #5084, SOLR-12864 #5086, SOLR-13265 #5087. Report: `reports/review-opened-28-g5.md`.
- Group 6: SOLR-13696 #5088, SOLR-13943 #5089, SOLR-14262 #5090, SOLR-14718 #5091. Report: `reports/review-opened-28-g6.md`.
- Group 7: SOLR-16356 #5092, SOLR-16655 #5093, SOLR-16673 #5094, SOLR-16910 #5095. Report: `reports/review-opened-28-g7.md`.

Method: each pull request is read with `gh pr view` (read only). The head SHA is checked against the gated head in `receipts/SOLR-<ticket>.md`. The body is compared with the draft `pr-drafts/update-processing/SOLR-<ticket>.md` on pr-prepare, and with the branch diff at the head. Each citation is checked at the head.

Deviation from the assignment, stated here: the subagents write their group files and do not commit or push them. The lead commits and pushes all seven group files together and then writes the roll-up. Seven concurrent pushes to one worktree would race. The content and the file names are as the assignment says.

Not in scope: flipping any draft, editing any pull request, description, comment, or submit branch, builds, Gradle, and test runs. Gate state comes from `receipts/`.
