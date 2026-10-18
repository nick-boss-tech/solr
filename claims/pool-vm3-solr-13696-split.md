# Claim: SOLR-13696 production fixes split from PR 5088

Status: DONE 2026-10-18 (branch solr-13696-production-split at 3a72def350, pushed to the fork; no PR opened)

- Claimed by vm3 on 2026-10-18 under the 13696 review decision (Nick: split the production changes out of PR 5088).
- Branch base: c3cdf7b46e (upstream main merge-base). Commits: f588eb5707 (CreateAlias dimensional router name and field, with its CreateAliasAPITest update), c77484fd69 (TimeRoutedAlias route value parse), 3a72def350 (changelog fragment). Commits keep Nick Shanin as author.
- Verification: :solr:core:compileJava and compileTestJava with -Pvalidation.errorprone=true pass; CreateAliasAPITest 13 of 13 pass. Changelog validator pass.
- PR 5088 itself is unchanged. Opening a PR from this branch or retargeting PR 5088 needs Nick's approval.
