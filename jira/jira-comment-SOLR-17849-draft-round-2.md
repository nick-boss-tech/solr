# Jira comment draft: SOLR-17849

Status: DRAFT (not posted). Refresh of vm6's round-1 draft after the gate: the branch is built and its tests pass. Addressees: Jane Sandberg (who volunteered on the ticket) and Christos Malliaridis. Prepared by the phone agent, 2026-10-24. Posting needs Nick Shanin's explicit go.

---
[~sandbergja] You said on 2025-10-23 that you would like to work on this ticket. Are you still planning to? I do not want to duplicate work you have started.

I have an implementation on a branch, [solr-17849-index-query-screen|https://github.com/nick-boss-tech/solr/tree/solr-17849-index-query-screen]: the Index and Query header and screen, with the collection and request handler dropdowns. The files it adds are {{IndexQueryHeader.kt}} and {{IndexQueryContent.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/indexquery/}}, a data layer and state holder under {{components/indexquery/}}, and {{RequestHandlerOption.kt}} in the domain package, with wiring in the configsets components and strings. Tests: {{IndexQueryHeaderTest}}, {{IndexQueryContentTest}}, {{IndexQueryStateHolderTest}}. Spotless and those tests pass at the branch head. The branch also carries the SearchHandler form view, which is SOLR-17850's component.

If you are not working on it, [~malliaridis], is this the right scope for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
