# Assignment: review the 24 opened search components draft PRs, round 1

Status: DONE

Capability tags: [review]. Intended host: Windows (review agent, acting as review coordinator). Claim under claims/ before starting, per WORKFLOW.md. The coordinator spawns 6 subagents, one per slice below, and each subagent reviews the 4 PRs in its slice. This is a reading and description-editing assignment: no builds run on Windows, and no branch is pushed (see the patch loop rules).

## Owner directive

Nick, 2026-10-14: the first 24 search components drafts are open, and the review agent reviews them now, 6 subagents with 4 PRs each. Findings are applied, not just listed, under the standing patch loop rule (Nick, 2026-10-12): description and title fixes go onto the live PRs in this assignment; code findings go through a gate job before anything moves a live head. Rounds continue per slice until every PR in the slice carries a SATISFIED verdict or an explicitly recorded blocker.

## Background

- The 24 PRs opened on 2026-10-14 as drafts from the search components slate (`reports/morning-openings-slates-2026-10-14.md`, the annotated rows). Each branch was gated before opening and its draft text was prepared on this branch; the review target now is the OPEN PR as it stands, checked against the code and the record.
- Each ticket's gate record is in `receipts/` on this branch, one file per ticket. A receipt at the exact PR head settles what ran; the takeover log is the fallback if a receipt and the head disagree.
- The PR formula is `files/pr-formula.md` in the goal workspace, with a copy under `material/` on this branch: bold one-line summary opening each section, real blob citations, plain language, no internal process vocabulary, changelog line linked at the head.

## The slices (slate order; one subagent each)

- Slice 1: PR #5130 (SOLR-4374), PR #5131 (SOLR-5394), PR #5132 (SOLR-6207), PR #5133 (SOLR-6759)
- Slice 2: PR #5134 (SOLR-6831), PR #5135 (SOLR-6975), PR #5136 (SOLR-7390), PR #5137 (SOLR-7498)
- Slice 3: PR #5138 (SOLR-7520), PR #5139 (SOLR-7550), PR #5140 (SOLR-8003), PR #5141 (SOLR-8009)
- Slice 4: PR #5142 (SOLR-8020), PR #5143 (SOLR-8051), PR #5144 (SOLR-8088), PR #5145 (SOLR-8240)
- Slice 5: PR #5146 (SOLR-8939), PR #5147 (SOLR-9124), PR #5148 (SOLR-9396), PR #5149 (SOLR-9595)
- Slice 6: PR #5150 (SOLR-9864), PR #5151 (SOLR-10305), PR #5152 (SOLR-10424), PR #5153 (SOLR-10492)

## Per PR: what to read

1. The live PR itself: title and body as they stand on GitHub (UI or API, as available to this host). Review what a maintainer sees, not the prepared draft on this branch.
2. The branch code at the PR head (fetch the head SHA and read the diff against its merge-base).
3. The ticket's receipt in `receipts/`, and the ticket's slate row for any recorded landing order or follow-up note.

## Per PR: the checks

1. **Claims against code and receipt.** Every claim in the body checks against the code at the head and the receipt: the change description matches the diff; proof counts and dates match the receipt at this head; the statement that the new test fails without the fix matches what the receipt records; Limits are honest, with named follow-ups and landing orders stated where the record has them (SOLR-10424, PR #5152, carries a before-SOLR-8240 note; check that it is present and accurate); any Choice section poses a real choice with the implemented position stated.
2. **Formula compliance.** A bold one-line summary opens each section. Citations are real blob links: change code at the PR head SHA, pre-change symptom code at the merge-base SHA, with the text saying which is which. The changelog line is present and its fragment parses as YAML (parse it with a real YAML parser, not by eye; an unquoted colon-space in a title is invalid). Plain language throughout: no internal process words (gate, receipt, ledger, fresh JUnit XML, rc, seed values, run identifiers), no em dashes, no "we" for Nick plus agent. The title is accurate against the diff.
3. **Diff fidelity.** The diff at the head does what the title and body say, and nothing more: spot-check the production files and the test files, confirm the change is scoped to the ticket, and confirm nothing unrelated rode along. The branch was gated before opening; this is a fidelity review, not a re-gate, and no test run is owed here.

## Patch loop

- **Description and title findings: fix them on the live PR in this assignment**, under the standing description-edit authorization. Record every edit in the slice report with the before and after text.
- **Code findings: do not push code.** Record the finding in the slice report with a proposed patch (the diff, or a precise description of it if the diff is long), and file a gate job under `gates/` for the fix, naming the branch, the head it applies to, the finding it answers, and the full gate shape from the standing record (changelog YAML parse, tidy with the versions-file restore, Error Prone compile, pre-fix proof, focused runs counted from fresh JUnit XML, module check). The fix moves the live head only after that gate passes, in a later round or a separate lane.
- Re-review after each round of edits. A slice is done when each of its 4 PRs carries a SATISFIED verdict or a recorded blocker naming what is missing and whose move it is.

## Deliverables

One report per slice: `reports/search-components-opened-review-round-1-slice-<n>.md` (n = 1 to 6), each carrying:

- Per PR: the verdict (SATISFIED, or the blocker), the checks run, and the head SHA reviewed.
- Every description or title edit made, with before and after text.
- Every gate job filed, with its file name and the finding it answers.
- Any receipt or slate disagreement found, stated plainly (the record gets corrected by the main side; flag it, do not silently work around it).

## Hard limits

- No ready flips: every PR stays a draft.
- No comments on the PRs, and no replies to maintainer or Copilot comments.
- No branch pushes and no code commits to the PR branches.
- No new Jira tickets, and no edits to Jira.
- Description and title edits to these 24 PRs are the only live changes this assignment makes.
