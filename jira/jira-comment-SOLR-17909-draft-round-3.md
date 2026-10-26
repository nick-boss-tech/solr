# Jira comment draft: SOLR-17909, round 3

Status: DRAFT (not posted). Refresh of round 2 at Nick Shanin's direction on 2026-10-26: branch and diff URLs added; the round-2 caveat about a draft file at the branch tip is resolved, the file is removed. Addressee: Christos Malliaridis (reporter). Posting needs Nick Shanin's explicit go.

Branch state, verified 2026-10-26: `solr-17909-breadcrumb-wrap` tip `b5fdbc23436` after removing `pr-draft/SOLR-17909.md`; the compare against main shows exactly 3 files. Gated head `77a38638b0`; the strip deletes a non-code file only.

---

[~malliaridis] I implemented the breadcrumb component from this ticket on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17909-breadcrumb-wrap], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17909-breadcrumb-wrap] is {{SolrBreadcrumb.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/components/}} with its test {{SolrBreadcrumbTest.kt}} and a JVM preview. Spotless and the test pass at the gated head.

Is this the right scope for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
