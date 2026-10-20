# Receipt: PR review apache/solr #4604 (vm4)

- PR: https://github.com/apache/solr/pull/4604, open, not draft, head branch `errorprone-2.50.0-upgrade` on `janhoy/solr`, 150 files, +495/-203, `mergeable_state` unknown at the snapshot (GitHub files API, two pages).
- Categories: 119 Java files, 27 build files (`gradle.lockfile` changes plus `gradle/libs.versions.toml`, line 101: `google-errorprone` 2.41.0 to 2.50.0), 3 `solr/` files outside Java, 1 changelog entry (`type: other`).
- Java additions: 103 added lines mention `@SuppressWarnings` or `@SuppressErrorProne`. The remaining added lines are mostly comments that explain intentional identity checks, plus the `ResponseBuilder.java` change (+2/-0).
- Lockfile diffs were checked for the same artifact changes across modules (each file shows the same 5-line error-prone block changing).
- Gate: none run.
- Outcome: READY-WITH-NOTES. Draft: `drafts/pr-review/4604.md`.
