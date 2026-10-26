# Jira comment draft: SOLR-17913, round 2

Status: DRAFT (not posted). Refresh of the round-1 draft at Nick Shanin's direction on 2026-10-26: branch and diff URLs added. Addressee: Christos Malliaridis (reporter). Posting needs Nick Shanin's explicit go.

Branch state, verified 2026-10-26: `solr-17913-collection-details-card` tip `4003196af10` after removing `pr-draft/SOLR-17913.md`; the compare against main shows exactly 4 files. Gated head `0a04d2239e`; the strip deletes a non-code file only.

---

[~malliaridis] I implemented the collections details card from this ticket on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17913-collection-details-card], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17913-collection-details-card] is {{CollectionDetailsCard.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/}}, a {{CollectionDetails}} type in the domain package, and {{CollectionDetailsCardTest}}, with strings. Spotless and the test pass at the branch head.

Is this the right scope for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
