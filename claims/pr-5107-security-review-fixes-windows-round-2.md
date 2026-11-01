# Claim: PR 5107 security review fixes, Windows round 2

- Claimant: Windows Codex desktop; tags implementation, review, records.
- Started: 2026-11-01T19:44:00Z.
- Owner direction: implement the November 1 review findings on Eric's current branch, then hand off to a Linux gate agent.
- Board snapshot: ff532accc1d99bd59b3004e038b03fa719861b5d. No matching implementation claim exists for this current-head slice. The old service-access gate is a different patch at 5ff8f324ef3 and remains incomplete.
- Contributor parent: 25c88fb1c432120b98f7af668cf58be0052d13d0, Apache PR 5107, SOLR-8442.
- Worktree: C:/Users/shaninna/dev/Solr-issues/wt/PR-5107-security-review-r2.
- Fork branch: solr-8442-security-review-r2-submit, ordinary commits above the contributor head, plain fork push only.
- Slice: retain client-certificate authentication policy during PEM installation, validate supplied administrator credentials before bootstrap, distinguish input PEM password policy from output keystore policy, focused regressions, documentation and changelog.
- Deliverables: reports/pr-5107-security-review-fixes-round-2.md and gates/pr-5107-security-review-fixes-round-2.md on coordination-ui, naming the independently verified fork head, exact outbound files, fresh positive and negative proof, and a dedicated receipt path.
- Execution split: Windows edits and pushes; Linux alone runs Gradle/BATS/OpenSSL proof. No public PR/JIRA writes or contributor-branch changes.
- Decision ladder: scope is directly owner-authorized; no escalation required. Adjacent design questions remain outside the patch unless needed for a safe fix.
