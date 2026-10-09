# Assignment: internal review of the 28 opened update-processing PRs

Owner direction: given in the main chat on 2026-10-09. The 28 update PRs are open as drafts on apache/solr (execution report: `reports/open-update-29-prs.md`). This review is the internal pass that decides which drafts can flip to ready. It is read-only: do not edit any PR, description, branch, or comment, and do not flip any draft. Findings and verdicts come back as reports on this branch.

## Staffing

Run **7 subagents in parallel**, one per group below (4 PRs each). Each subagent writes its own group report file so the seven never write the same file. The lead then writes the roll-up.

- Group 1 (`reports/review-opened-28-g1.md`): SOLR-3657 #5069, SOLR-4841 #5070, SOLR-5065 #5071, SOLR-5505 #5072
- Group 2 (`reports/review-opened-28-g2.md`): SOLR-5754 #5073, SOLR-5939 #5075, SOLR-5941 #5076, SOLR-5887 #5074
- Group 3 (`reports/review-opened-28-g3.md`): SOLR-6045 #5077, SOLR-7504 #5081, SOLR-12703 #5085, SOLR-12705 #5096
- Group 4 (`reports/review-opened-28-g4.md`): SOLR-6065 #5078, SOLR-6973 #5079, SOLR-7022 #5080, SOLR-11475 #5082
- Group 5 (`reports/review-opened-28-g5.md`): SOLR-11483 #5083, SOLR-12245 #5084, SOLR-12864 #5086, SOLR-13265 #5087
- Group 6 (`reports/review-opened-28-g6.md`): SOLR-13696 #5088, SOLR-13943 #5089, SOLR-14262 #5090, SOLR-14718 #5091
- Group 7 (`reports/review-opened-28-g7.md`): SOLR-16356 #5092, SOLR-16655 #5093, SOLR-16673 #5094, SOLR-16910 #5095

## What to check for every PR

1. **The head is the gated head.** The PR's head SHA equals the gated head in `receipts/SOLR-<ticket>.md`. The receipts folder settles gate state; do not re-run tests and do not re-audit branches.
2. **The description matches the branch.** Read the PR body against the branch diff at that head. Every claim in What happens today and What this change does must be true of the diff. Proof numbers must match the receipt for that ticket exactly. Citations must be real blob links at the head SHA and their line anchors must land on the lines they claim.
3. **The formula holds.** Sections in order, each opening with a bold one-line summary; simple language; the AI header at the top and the AI footer at the end; a Choices section only for a real decision with a live alternative; known gaps in Limits with a stated plan to submit a follow-up (no new Jira ticket promised anywhere); no em dashes; the text never uses "we" for the owner plus agent.
4. **The diff is clean.** Only the intended change: no TESTING or handoff documents, no stray files, no unrelated edits. The changelog fragment parses as YAML and its title matches the change.
5. **Cross-PR rulings hold.** Group 3 owns this for the atomic cluster: SOLR-7504's counted-field rule governs and SOLR-12705's PR must not claim counted-field outcomes its re-scoped tests no longer pin; SOLR-6045 and SOLR-7504 state the same rule for a plain value mixed with an operation map (rejected in both orders). Group 6: SOLR-14718's Limits must not promise attribution behavior that SOLR-5939's per-request mechanism replaces (the rewrite to the post-5939 mechanism is expected in the drafting follow-up; flag the current wording if it contradicts the ruling in `material/update-29-rulings.md`), and SOLR-13943's description must make its stack on SOLR-13696 plain. Group 7: SOLR-16655 lands before SOLR-12705 and SOLR-12705's description must not contradict that order.

## Verdicts

Per PR, exactly one of:

- **READY TO FLIP**: no findings, or only cosmetic ones listed with exact replacement text.
- **FIX FIRST**: findings that must be corrected before flipping, each with the file or description section, the current text, and the corrected text.
- **OWNER CALL**: a genuine decision only the owner can make, stated in one or two sentences with the alternatives. Do not manufacture owner calls out of findings that have a clearly right correction.

## Reports

- Each subagent commits and pushes its group file (`reports/review-opened-28-g<N>.md`) with one section per PR: verdict first, then findings with file and line citations.
- The lead writes `reports/review-opened-28.md`: a verdict table for all 28, the FIX FIRST items grouped by what kind of fix they are (description text, branch content, citation), and the OWNER CALL list. Nothing else is needed in the roll-up.

## Rules

- Read-only on GitHub and on the submit branches. The only writes are the report files on pr-prepare.
- Claim first: the lead pushes a claim file under `claims/` naming the seven groups before the subagents start.
- No builds and no test runs in this assignment; gate state comes from `receipts/`.
