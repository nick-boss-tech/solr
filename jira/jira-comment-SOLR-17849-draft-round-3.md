# Jira comment draft: SOLR-17849, round 3

Status: DRAFT (not posted). Refresh of round 2 at Nick Shanin's direction on 2026-10-26: branch and diff URLs added in the SOLR-17861 round-2 pattern; the Jane Sandberg lead is unchanged. Addressees: Jane Sandberg and Christos Malliaridis. Prepared for the Christos digest package (he asked Nick to post all the UI comments). Posting needs Nick Shanin's explicit go.

Branch state, verified 2026-10-26: `solr-17849-index-query-screen` tip `375143456a0` after removing `pr-draft/SOLR-17849.md` and `pr-draft/SOLR-17850.md`; the compare against main shows exactly 18 files (the indexquery views, components and tests, the configsets wiring and strings, and the SearchHandler form view that is SOLR-17850's component). Gated head `b91f75ed93`; the strip deletes non-code files only.

---

[~sandbergja] You said on 2025-10-23 that you would like to work on this ticket. Are you still planning to? I do not want to duplicate work you have started.

I have an implementation on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17849-index-query-screen], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17849-index-query-screen] is the Index and Query header and screen: {{IndexQueryHeader.kt}} and {{IndexQueryContent.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/indexquery/}}, a data layer and state holder under {{components/indexquery/}}, and {{RequestHandlerOption.kt}} in the domain package, with wiring in the configsets components and strings. Tests: {{IndexQueryHeaderTest}}, {{IndexQueryContentTest}}, {{IndexQueryStateHolderTest}}. Spotless and those tests pass at the gated head. The branch also carries the SearchHandler form view, which is SOLR-17850's component.

If you are not working on it, [~malliaridis], is this the right scope for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
