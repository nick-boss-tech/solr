# Receipt: PR review apache/solr #2783 (vm4)

- PR: https://github.com/apache/solr/pull/2783, open, not draft, `mergeable_state` dirty at the snapshot. Base `1db0effa82`, head `64f1cc705d` on `epugh/solr` branch `SOLR-17492`. 25 commits, 9 files, +344/-16.
- Fetched the head from the author's fork (`git fetch https://github.com/epugh/solr.git SOLR-17492`). Diff scoped to the PR files against base `1db0effa82`.
- Files: changelog entry, `gradle/libs.versions.toml` (+2), `solr/solr-ref-guide/build.gradle` (+9), `deployment-nav.adoc`, `deployment-topology-overview.adoc` (new, +302), `installing-solr.adoc`, `taking-solr-to-production.adoc`, `zookeeper-ensemble.adoc`, `playbook.template.yml` (+4).
- Dash check: `git diff ... -- solr/solr-ref-guide | grep '^+' | grep -c` for em or en dashes returned 0.
- Cross-references: 8 unique `xref:` targets in the added lines. Seven resolve to files in the PR head tree. `xref:deployment-topology-overview` (no `.adoc`) did not match a page name and is flagged in the draft.
- Gate: none. The Antora docs build is blocked on this host (missing `@asciidoctor/tabs`), per earlier notes.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/2783.md`.
