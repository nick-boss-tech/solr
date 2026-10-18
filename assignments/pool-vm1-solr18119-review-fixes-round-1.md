# Pool assignment: SOLR-18119 review findings follow-up

Status: OPEN

- Capability tags: implementation, gate, review, draft.
- Intended host: vm1. Claim under `claims/pool-vm1-solr18119-review-fixes-round-1.md` before starting (WORKFLOW.md).
- Posted by: Windows review coordinator, 2026-10-18, at Nick's direction.

Each item below is based on the follow-up review of PR #5180. VM1's existing gate at the same head is green, but it did not execute the assembleRelease missing-input path. This assignment closes that gap and corrects the live PR description.

## Items

1. **Close the renderer compatibility and release guard proof gaps.** Start from: `dev-tools/scripts/test_changes2html.py`, `solr/distribution/build.gradle`, `gates/SOLR-18119-release-changes-html.md`, `reports/SOLR-18119-release-changes-html-review.md`, and `receipts/SOLR-18119.md` on `coordination`. Output: focused test and proof changes on the PR branch, with head-specific evidence appended to the gate report and receipt.
   - Add a test for the existing CLI mode without `--output-dir`: invoke the renderer with a changelog argument, assert rendered HTML is written to stdout, and assert it does not create release output files.
   - Add and execute a repeatable negative proof for `:solr:distribution:assembleRelease` while the required generated directory or files are absent. Require failure from `assembleRelease`, name the missing files, and include the suggested renderer command. Then run the existing positive path and verify all four files are packaged.
   - Run the applicable hardening gate again after the patch. The prior report records that the full documentation task, `:solr:solr-ref-guide:checkSiteLinks`, and actual `assembleRelease` execution were not run. Keep those limits explicit unless the new gate runs them.
2. **Correct the live PR #5180 description.** Start from: the live body and current gate evidence. Output: an updated body, read back and checked against the live head.
   - Say that generation is release-only and that `Changes.html` and its stylesheets remain in the release distribution under `changes/`; do not say the HTML is only used when publishing the website.
   - Narrow the verification heading and claims to the exact current-head gate results. Retain the current limits for checks not run. Keep the output hash claim only if it is recorded for the final head.
   - Keep the AI header and footer, the Docker Python helper limit, and the existing no-changelog rationale. Keep the body under the repository's visible character guidance and do not use em dashes or en dashes.
3. **Leave a complete follow-up record.** Start from: the existing coordination gate, report, and receipt. Output: `reports/SOLR-18119-release-changes-html-followup.md`, plus a clearly labeled follow-up section in the existing gate and receipt for the final head. Record the pushed SHA, test and proof results, exact limits, and PR body readback. Mark this assignment DONE in the same coordination push as the deliverable.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Patch branch | decided: patch only `solr-18119-remove-changes2html`, the PR #5180 head branch at `1029a6b0cf09dc75bdb380cd63fce1511bf440b2` | If the fork tip differs before work begins, stop and report the new tip to the main agent before editing | Nick |
| Scope | decided: keep the renderer in Python and release-only; keep the local missing-link behavior and Docker test helper unchanged | If a needed fix would broaden that scope, stop and ask the main agent | Nick |
| Changelog | decided: do not add a changelog fragment; this remains release tooling only | If implementation changes beyond that scope, stop and ask the main agent | Nick |
| Public communication | decided: update only PR #5180's body after fresh head and discussion checks; do not post a PR comment or JIRA comment, and do not change PR state | If the head, comments, reviews, or gate result changes before the body edit, stop and report for a decision | Nick |
| Failed proof | decided: report any failure that occurs before the intended guard assertion; do not count unrelated dependency failure as proof and do not retry an executed failing test without investigation | If the failure is unrelated or the guard cannot be exercised, record the exact output and escalate to the main agent | Nick |

## Deliverable

`reports/SOLR-18119-release-changes-html-followup.md`: one section per item with status, source changes and commit, exact final branch head, test and gate evidence, updated coordination records, and PR body readback.

## Limits

- Use the live PR branch `solr-18119-remove-changes2html`; do not update the `solr-18119-release-python` alias or another superseded branch.
- Push a plain fast-forward commit to the Nick fork branch. Never force push or write to `apache/solr`.
- No JIRA comment, PR comment, PR opening, closing, or state flip.
- Do not add a changelog fragment or change the user-visible release-only design.
- Run Gradle only on vm1. Keep the patch limited to the renderer test, release guard proof, necessary code changes, and the PR body.
- Commit as Nick Shanin with no trailers. Public text must contain no em dashes or en dashes.
