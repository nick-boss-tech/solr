# Claim: SOLR-13696 production fixes split from PR 5088

Status: DONE 2026-10-18 (superseded: the production commits stay in PR 5088; branch solr-13696-production-split is kept as a reference)

- Claimed by vm3 on 2026-10-18. Nick said "relaunch and split them up" in reply to the 13696 options; vm3 read that as approval to split the production commits onto their own branch. That reading is not written in any Decisions table, and no PR decision was made.
- Branch base: c3cdf7b46e (upstream main merge-base). Commits: f588eb5707 (CreateAlias dimensional router name and field, with its CreateAliasAPITest update), c77484fd69 (TimeRoutedAlias route value parse), 3a72def350 (changelog fragment). Commits keep Nick Shanin as author.
- Verification: :solr:core:compileJava and compileTestJava with -Pvalidation.errorprone=true pass; CreateAliasAPITest 13 of 13 pass. Changelog validator pass.
- PR 5088 itself is unchanged. Opening a PR from this branch or retargeting PR 5088 needs Nick's approval.

- 2026-10-18: Decision recorded (decision ladder: no new Jira ticket without Nick; PR openings and moves need Nick; scope default applied). Production commits 98ad9d3fcc and a4e0da4223 stay in PR 5088. No regate of a test-only branch: the PR body states that the Dimensional suite stops at CREATEALIAS without the create-alias fix, and that is the reason the commits stay together; a test-only run was not done to confirm it. Reviewer text drafted in pr-drafts/2026-10-18-review/PR-5088-behavior-change.md for Nick to apply.

DONE: 2026-10-18. Deliverable: PR 5088 body (Behavior change and Choices for maintainers); branch solr-13696-production-split at 3a72def350 kept as a reference.
