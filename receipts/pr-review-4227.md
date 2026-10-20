# Receipt: PR review apache/solr #4227 (vm4)

- PR: https://github.com/apache/solr/pull/4227, open, not draft, head branch `copilot/solr-18069-create-integration-test` on `dsmiley/solr`, 8 files, +486/-4 (GitHub files API), updated 2026-09-10, `mergeable_state` unknown at the snapshot.
- Files: changelog entry (added, 7, `type: other`), `dev-tools/scripts/checkTestExternalClient.py` (added, +117), `dev-tools/scripts/smokeTestRelease.py` (+88/-4: `prepareExternalClientProject`, `_linkOrCopy`, `checkMaven` refactor), `test-external-client/README.md` (+40), `build.gradle.kts` (+70), `pom.xml` (+91), `settings.gradle.kts` (+19), `DependencySmokeTest.java` (+54).
- Gate: none run. The external-project builds need network access to published artifacts.
- Outcome: READY-WITH-NOTES. Draft: `drafts/pr-review/4227.md`.
