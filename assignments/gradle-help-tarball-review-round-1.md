# Assignment: review of the gradle-help tarball fix (round 1)

Status: OPEN
Tags: review, premise-run
Intended host: any pool Linux host with the `review` and `premise-run` tags, other than vm6 (the author of the change). Claim as `claims/gradle-help-tarball-review-round-1-<host>.md` before starting.
Posted by: vm6, 2026-10-25, for the review of a change vm6 wrote.

## What to review

- Branch `solr-gradle-help-ship-in-tarball` on nick-boss-tech/solr, head `d2923cb65b`, base apache main `07fdebdda5`. Two files: `.gitattributes` and `gradle/validation/rat-sources.gradle`.
- Premise and history: `reports/tarball-help-files-premise-round-1.md`.
- vm6's proof: `receipts/gradle-help-tarball-fix-round-1.md`.
- Draft PR body: `drafts/pr-gradle-help-tarball-body-draft.md`.

## Method

1. Fetch the branch and read the diff. Check that `.gitattributes` is still correct after the rule order (`/dev-docs/**`, then `dev-docs/gradle-help` and `dev-docs/gradle-help/**` with `-export-ignore`) and that only `dev-docs/gradle-help/` is un-exported.
2. Make two fresh trees with `git archive`: one at base `07fdebdda5`, one at head `d2923cb65b`. Do not copy a checkout.
3. In each tree, run `./gradlew --offline :allHelpFilesExit`. Expect a failure at base and a pass at head. Record the archive listing of `dev-docs/` at head.
4. In the head tree, run `./gradlew rat` online. Expect a pass. Then run the mutation: remove the `exclude "dev-docs"` line in the no-git branch of `rat-sources.gradle`, and expect `dev-docs/gradle-help/README.md` to fail. Record the output.
5. Check whether any other archive-based check would now see the new files: the `dev-docs/` license scan (`checkDevDocsLicenseHeaders`, on the guard PR branch, if it is present) and any other place that reads `dev-docs/` from an archive.
6. Read the choice section of the draft PR body. Say whether you agree with the recommendation (ship the help text), and give your reason in one or two sentences.

## Acceptance

- The two runs and the mutation reproduce vm6's results, or the report names the difference and its cause.
- A verdict line: "approve as is", "approve with changes" (list them), or "do not merge" (with reason).

## Limits

- Nothing posted to GitHub or JIRA, and no PR opened. The owner (Nick) decides whether to open the PR.
- Do not push to the branch. Review comments go in the report.
- Full `check` from an archive is optional and not required for the verdict; say whether you ran it.

## Deliverable

`reports/gradle-help-tarball-review-round-1.md`: the diff summary, the four runs with logs, the mutation result, the choice opinion, and the verdict line.
