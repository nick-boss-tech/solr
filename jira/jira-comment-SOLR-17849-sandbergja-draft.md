[~sandbergja]

Evidence, checked on the fork: the Index & Query screen is on a branch at b91f75ed93. It has the section header with the collection and request handler dropdowns, the data layer and state holder, and it hosts the screen on the Configsets Index / Query tab. Its test run passes 18 of 18.

The screen uses the solr.SearchHandler form from SOLR-17850, so it waits on that ticket. The section name on the branch is "Index & Query", as Christos confirmed on 2025-10-24.

Are you still planning to work on SOLR-17849?

Options:
 * You take the ticket and review the branch first, then build on it.
 * The ticket stays open with the branch noted, and Christos decides who takes the next steps.
 * The header goes on its own and the screen waits for SOLR-17850.

I would take the first option. Your comment on 2025-10-23 asked for this ticket, and the branch already has the header and screen to review.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
