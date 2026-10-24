# Claim: pool-ui-SOLR-17660-upstream-base-vm4

- Claimant host: vm4.
- Status: LIVE.
- Reason: `solr-17660-basic-list-main` (`3ab8f52d01`) is based on the fork's `main` (`498a09aea9`), which has diverged from apache main. Its diff against `upstream/main` is 1128 files. This claim moves the same component onto `upstream/main` (`13c693d805`).
- Slice: cherry-pick `1e8aca0ffc` (component, tests, preview) and `3ab8f52d01` (draft PR description) onto `upstream/main`. Gate: `:shared:jvmTest :shared:spotlessCheck` at the new head.
- Capability tags used: ui, jvm-tests.
- Started: 2026-10-24T20:14:17Z (UTC), written with its receipt, after the gate run.
- Constraints: no PR, no Jira or GitHub comment.

Heartbeat: 2026-10-24T20:14:17Z
