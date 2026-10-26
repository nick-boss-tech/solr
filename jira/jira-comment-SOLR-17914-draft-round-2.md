# Jira comment draft: SOLR-17914, round 2

Status: DRAFT (not posted). Refresh of the round-1 draft at Nick Shanin's direction on 2026-10-26: branch and diff URLs added. Addressee: Christos Malliaridis (reporter). Posting needs Nick Shanin's explicit go. Note: this branch builds on SOLR-17913's card, so its ask should post after SOLR-17913's.

Branch state, verified 2026-10-26: `solr-17914-create-collection-dialog` tip `794ada81506` after removing `pr-draft/SOLR-17914.md`; the compare against main shows exactly 4 files. Gated head `a502747ec4`; the strip deletes a non-code file only.

---

[~malliaridis] I implemented the create collection dialog from this ticket on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17914-create-collection-dialog], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17914-create-collection-dialog] is {{CreateCollectionForm.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/}} with its test {{CreateCollectionFormTest.kt}}, a JVM preview and strings. Spotless and the test pass at the gated head. The branch builds on the collections details card work in SOLR-17913.

Is this the right scope for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
