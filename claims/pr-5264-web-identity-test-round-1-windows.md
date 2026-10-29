# Claim: SOLR-18610 web identity policy regression test, round 1

- Host: windows (Codex desktop)
- Started: 2026-10-29T14:46:49Z
- Tags: implementation, review, records
- Source: Nick's direct request in this chat to build a test for the Security Manager coverage gap in PR 5264.
- Board snapshot reviewed: 8bc6ecb2e988b2c271de8a4c2a9a93e5c16d9508. No matching assignment, active claim, or receipt found.
- Scope: isolated regression on top of contributor head dbb3d85d72dd27889d8b93f10d1f4c2f54aa5e0e, pushed to Nick's solr-18610-submit fork branch. Do not change the contributor's branch or post publicly.
- Approach: a separate JVM running the shipped policy with the Security Manager, direct web identity provider, dummy token and local STS stub. Existing Testcontainers tests keep their Security Manager override. No Docker or AWS account required.
- Decision ladder: rung 1 establishes the SDK's STS construction and exact permission checks; rung 3 is Nick's requested regression coverage. Runtime premises and fail-before proof remain pending Linux execution.
- Deliverables: fork commit with focused test and any minimal test wiring; gates/pr-5264-web-identity-test-round-1.md at exact pushed head; reports/pr-5264-web-identity-test-round-1.md implementation handoff.
- Windows will not compile or run tests/Gradle. A capable Linux gate host must provide Java 21 pass and original-policy failure proof, tidy and checks. No green claim until that receipt exists.
