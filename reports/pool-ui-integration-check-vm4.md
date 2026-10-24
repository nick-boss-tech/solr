# Report: combined merge check for the seven vm6 pool-ui branches

Claim: `claims/pool-ui-integration-check-vm4.md` (vm4). Grouped-gate proposal: `reports/workflow-settlement-ui-gates-round-1.md`.

- Base: `c8ab71430f` (apache/main, the base the vm6 branches use).
- Integration branch on the fork: `ui-integration-check-vm4` at `ec6601afb62a5d130beacfd7d2e92b514dcdb0a0`. Seven merge commits, no code of the branches changed.
- Branches merged, in order: `solr-17850-search-handler-form`, `solr-17887-file-tree`, `solr-17909-breadcrumb-wrap`, `solr-17911-file-content-viewer`, `solr-17913-collection-details-card`, `solr-17914-create-collection-dialog`, `solr-17849-index-query-screen`.
- Conflicts: only `ui/shared/src/commonMain/composeResources/values/strings.xml`, three times (one per merge with 17913, 17914 and 17849). Resolution: keep every string from both sides. 17913 and 17914 both define `label_collection_name` and `label_replication_factor` with the same text. One copy of each is kept, so the resource file has no duplicate names (92 strings, checked).
- Result at `ec6601afb62a5d130beacfd7d2e92b514dcdb0a0`: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck` passes. JUnit: 24 suites, 83 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest` and `StartContentTest`, both outside these branches). Build 2m 41s.
- Logs on vm4: `/workspace/gates/logs/vm4/integ-check/` (`full-test-spotless.log`, `junit-full/`, `head.txt`).
- What this does not show: each branch's own receipt still names its own gated head. This report is only the combined check. The combined head is not a gate of any ticket.
- Consequence for PRs: the seven PRs cannot be opened as independent changes to `apache/main` without the same `strings.xml` resolution. The first PR merged will make the others conflict in that file.
