# Jira comment draft: SOLR-17660, round 2

Status: DRAFT (not posted). Refresh of the round-1 draft at Nick Shanin's direction on 2026-10-26: branch and diff URLs added. Addressee: Christos Malliaridis (reporter). Posting needs Nick Shanin's explicit go.

Branch state, verified 2026-10-26: `solr-17660-basic-list-main` tip `9897d950964` after removing `pr-draft/SOLR-17660.md` and the factory workflow file `.github/workflows/fork-test-runner.yml`; the compare against main shows exactly 3 files. Gated head `1e8aca0ffc`; the strip deletes non-code files only.

---

[~malliaridis] I implemented the basic list component from this ticket on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17660-basic-list-main], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17660-basic-list-main] is {{SolrList.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/components/}} with its test {{SolrListTest.kt}} and a JVM preview. The component, its tests, and the preview were run at the gated head and pass.

Is this the right scope for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
