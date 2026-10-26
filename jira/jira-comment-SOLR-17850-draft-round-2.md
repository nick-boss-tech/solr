# Jira comment draft: SOLR-17850, round 2

Status: DRAFT (not posted). Refresh of the round-1 draft at Nick Shanin's direction on 2026-10-26: branch and diff URLs added, and the field-set question is now stated against the design. The branch's form exposes q, fq, fl, sort, start and rows (read from SearchHandlerForm.kt and strings.xml at the branch tip on 2026-10-26); the Figma Regular Request Builder inventory (node 156-4776 in reports/figma-node-index-round-1.md) additionally shows defType, q.op, wt, paramsets, additional parameters and the highlighting, faceting, spatial and spellcheck sections. The separate vm4 field-set drafts are superseded by this round for posting purposes. Addressee: Christos Malliaridis (reporter). Posting needs Nick Shanin's explicit go.

Branch state, verified 2026-10-26: `solr-17850-search-handler-form` tip `fa9e050e52f` after removing `pr-draft/SOLR-17850.md`; the compare against main shows exactly 3 files. Gated head `887778ce5d`; the strip deletes a non-code file only.

---

[~malliaridis] I implemented the SearchHandler form from this ticket on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17850-search-handler-form], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17850-search-handler-form] is {{SearchHandlerForm.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/searchhandler/}} with its test {{SearchHandlerFormTest.kt}} and strings. Spotless and the test pass at the branch head.

The field set the form exposes is {{q}}, {{fq}}, {{fl}}, {{sort}}, {{start}} and {{rows}}, with Clear all, Execute and Execute in Debug actions. The design's Regular Request Builder also shows {{defType}}, {{q.op}} and {{wt}}, paramsets and additional parameters, and the highlighting, faceting, spatial and spellcheck sections. Are those a later slice, or should they be in this one? Is the rest of the scope right, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
