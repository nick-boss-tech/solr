# Assignment: SOLR-17849 reduce the branch to header only, after Christos's reply

Status: DONE (vm6, 2026-10-27T20:56:43Z). Reduced branch `solr-17849-header-submit` at `5d90d83d5b`; report `reports/christos-reply-17849-header-scope-round-1.md`; PR draft `drafts/pr-solr-17849-header.md`. Not opened.
Tags: implementation, gate
Tag note: the `ui` tag was removed at Nick Shanin's direction on 2026-10-27 so any capable pool host can claim this; only vm6 held `ui`, which left the other pool hosts unable to take Christos's assignments. The ui build facts are in `UI-PLAYBOOK.md`.
Posted at Nick Shanin's direction on 2026-10-27: every reply from Christos Malliaridis gets its own assignment. This is the assignment for his SOLR-17849 reply.

## Source

Christos Malliaridis, Jira comment 18133044 on SOLR-17849 (2026-10-27): Nick creates the PR and Jane Sandberg makes the initial review, since Jane has a good eye for usability flaws and can cross-check her own discoveries. "I would reduce the scope of your branch to only the header, and create a separate PR for the content. The reason for this is there will likely be feedback we may want to iterate over separately of the header, hence why the ticket is explicitly telling that the content should be addressed outside this ticket."

Jane Sandberg's comment 18132611 on the same ticket: she was working on it slowly as a learning project and is happy to step aside.

## Work

Source branch: `solr-17849-index-query-screen` at gated head `b91f75ed93` (verify the live fork tip before starting). It currently carries the header and the content (`IndexQueryHeader.kt`, `IndexQueryContent.kt`, the data layer and state holder, `RequestHandlerOption.kt`, wiring and strings), and it also carries the SearchHandler form view, which is SOLR-17850's component.

1. Build a reduced branch `solr-17849-header-submit` from the source branch carrying only the header component and its test (`IndexQueryHeader.kt`, `IndexQueryHeaderTest.kt`) plus the minimal wiring and strings the header needs to compile and run. The content, the data layer and state holder, and the SOLR-17850 SearchHandler form all stay out. If the header cannot compile without a piece of the data layer, record exactly which piece and why before including it.
2. Gate the reduced branch: spotless and the header test at the exact head, through the normal UI gate route for this repo. Record the head and the results.
3. Draft the PR description to `drafts/pr-solr-17849-header.md` following `pr-formula.md`, naming Christos as sponsor and Jane Sandberg as the initial reviewer he proposed, and stating that the content follows in a separate PR per his comment. Do not file the content ticket or PR; the separate content PR is later work.
4. Stop at "branch gated and PR ready to open, back to the owner." The opening itself needs Nick's explicit go.

## Deliverable

A report at `reports/christos-reply-17849-header-scope-round-1.md`: reduced branch head, what was left out and where it still lives, gate results at the exact head, PR draft location.

## Limits

No PR opening, no Jira post or comment, no GitHub comment, no message to Jane. The original branch `solr-17849-index-query-screen` stays untouched; the reduced branch is new.
