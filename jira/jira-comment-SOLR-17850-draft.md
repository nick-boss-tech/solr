# Jira comment draft: SOLR-17850

Status: DRAFT (not posted). New draft in the agreed shape (built and gated, files named, scope and reviewer asked). Addressee: Christos Malliaridis (reporter). The separate field-set drafts by vm4 concern one design choice inside this form and are not replaced by this note. Prepared by the phone agent, 2026-10-24. Posting needs Nick Shanin's explicit go.

---
[~malliaridis] I implemented the SearchHandler form from this ticket on a branch, [solr-17850-search-handler-form|https://github.com/nick-boss-tech/solr/tree/solr-17850-search-handler-form]. It adds {{SearchHandlerForm.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/searchhandler/}} and {{SearchHandlerFormTest}}, with strings. Spotless and the test pass at the branch head.

The one choice I had to make is the field set the form exposes; I took my best reading of the ticket. Is this the right scope and field set for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
