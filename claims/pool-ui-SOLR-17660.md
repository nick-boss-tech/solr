# Claim: pool-ui-SOLR-17660

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE (2026-10-24T04:36:56Z). Basic list component (SOLR-17660), first slice: a stateless list composable with a preview and fake rows. Logging wiring waits for a logging data source (see reports/new-ui-index-query-components-round-1.md).
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`.
- Capability tags: ui, jvm-tests.
- Base: apache/main c8ab71430f. Branch: `solr-17660-basic-list` on the fork at 90ff62171a (commits b43dc58ce3 and 90ff62171a).
- Deliverables: `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/components/SolrList.kt`, `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/components/SolrListTest.kt`, `ui/shared/src/jvmMain/kotlin/org/apache/solr/ui/preview/components/PreviewSolrList.kt`, draft `pr-draft/SOLR-17660.md`.
- Gate: `REPO=<checkout> material/ui-test-setup/run-ui-jvm-tests.sh org.apache.solr.ui.views.components.SolrListTest` passes 5 of 5. Full `./gradlew -p ui :shared:jvmTest` passes: 16 suites, 52 tests, no failures. `./gradlew -p ui :shared:spotlessCheck` passes.
- Blast radius: not flagged. Layout, text input and a standard icon button only.
- Started: 2026-10-24T04:20:00Z.

Heartbeat: 2026-10-24T04:20:00Z
Done: 2026-10-24T04:36:56Z, deliverables above, marked on the same push as the fork branch.
