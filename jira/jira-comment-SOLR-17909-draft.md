[~malliaridis] I looked at SOLR-17909 (breadcrumb component). I have not run anything yet. The points below are read from the code on apache/solr main on 2026-10-24.

 * The child stack is already in use. {{components/main/MainComponent.kt}} line 40 exposes {{childStack}}, {{components/main/integration/DefaultMainComponent.kt}} line 55 builds it with {{childStack(...)}}, and {{views/main/MainContent.kt}} line 51 subscribes to it.
 * {{views/components/}} holds {{SolrButton.kt}}, {{SolrCard.kt}}, {{SolrLinearProgressIndicator.kt}}, {{SolrOutlinedTextField.kt}} and {{SolrTextButton.kt}}. Nothing named breadcrumb exists on main.
 * The pop-to-index call the ticket relies on is not checked against the Decompose version in this repo. That is the one thing I could not read from the code alone.

Does this evidence meet the bar you set for the ticket? That is the one question I am asking.

The options as I see them:
 * Option A: keep the ticket open, with the first slice being the composable over a fixed list of entries and a preview, and the Decompose pop call checked in that same change.
 * Option B: keep the ticket open with the Decompose pop call confirmed first, before any component code.
 * Option C: close this ticket once the component exists, and file the Collections use as a child ticket.

My recommendation is Option A. The component can be tested with a fixed list and no stack, so the Decompose check does not need to come first. The Collections use is optional under the ticket, so it can move to a child without holding this one open. I expect to add a composable file and its preview next to {{SolrButton.kt}} and {{SolrCard.kt}} in {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/components/}}.

No review is asked here. If the direction is right and work follows, I will ask about that then.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
