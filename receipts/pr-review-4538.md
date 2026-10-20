# Receipt: PR review apache/solr #4538 (vm4)

- PR: https://github.com/apache/solr/pull/4538, open, not draft, head branch `SOLR-17559` on `dsmiley/solr`, base `28ac2a5744`, 2 commits, 2 files, +65/-54, `mergeable_state` unknown at the snapshot (GitHub files API).
- Files: `.github/labeler.yml` (modified, +4/-6, unrelated to SOLR-17559), `solr/solrj/src/java/org/apache/solr/common/cloud/DocCollection.java` (modified, +61/-48).
- Patch check: one added `public` method in `DocCollection` (`getProperties`). No removed `public` methods in the patch.
- Base checks at `c8ab71430f`: `solr/solrj/build.gradle` has `compileOnly libs.stephenc.jcip.annotations` (line 38); `ObjectReleaseTracker.java` already imports `net.jcip.annotations`. So `@Immutable` compiles at base.
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4538.md`.
