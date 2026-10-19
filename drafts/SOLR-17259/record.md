# Skip record: SOLR-17259

- Ticket: SOLR-17259, "Reverse the order of two ref guide steps in releaseWizard". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2024-04-28, updated 2024-04-28, no fix version, no comments.
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-17259-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:44-13:45 UTC.

## Ticket reading

The reporter links a problem publishing the 9.6 reference guide to the order of two steps in the release wizard's "Update the website" menu, and proposes reversing them. The ticket has no comments. The queue note calls it a release-workflow proposal, with a speculative cause, from a maintainer report.

## Code reading

- `dev-tools/scripts/releaseWizard.yaml:1269-1277`: the "website" group, titled "Update the website".
- Its todo order at the pinned head, as written in the file: 1. "Publish docs, changes and javadocs" (line 1280); 2. "Update Ref Guide Metadata for release" (line 1308); 3. "Check that the reference guide is ready" (line 1352); 4. "Build the ref guide .htaccess file" (line 1362); 5. "Do a clean git clone of the website repo" (line 1395); 6. "Update website versions" (line 1408); 7. "Update website ref-guide htaccess" (line 1436).
- `dev-tools/scripts/releaseWizard.py` reads this file; no ordering logic of its own was found for these steps in this pass.

What this shows at the pinned head: the current order has the reference guide metadata update and the readiness check before the website steps. The ticket describes an order from 2024 (its menu text is not the same as the current file), so the ticket's specific swap may already be moot. The file does not show whether the swap the reporter proposes was made, or whether it would fix the 9.6 publication problem.

## History

- No commit on the pinned head names SOLR-17259 (evidence tool: no hits).
- Not traced in this pass: the commit that moved the reference guide steps in `releaseWizard.yaml`.

## Reproduction attempts

Not attempted. The release wizard runs against a release candidate and the website repository. Neither is a build or test in this repo, so no reproduction is possible from the pinned head. This pass is review-only.

## Gap analysis

The ordering the ticket complains about may no longer exist. Whether the current order is correct for publishing the guide is a release-process decision, not a code defect. The ticket cannot be closed as fixed or obsolete from the file alone, because the file does not show that the problem went away.

Missing piece for a verdict change: the release manager's statement of the order they want for the reference guide and website steps. With that, the record can say whether the current yaml matches it (close) or needs the swap (pr-needed, a change to `releaseWizard.yaml`).

## Verdict justification

keep-open meets the rubric: the code path is read (`releaseWizard.yaml` lines above), the attempts are listed (no reproduction is possible here), and the missing piece is named (the intended order from the release manager). A close needs a fix or an obsolescence check that the file does not settle. A pr-needed needs a decision on the order, which is not a code defect.

## Limits

This record does not establish whether the 9.6 publication problem came from step order. It does not decide the release process. It does not post anything to JIRA or GitHub.
