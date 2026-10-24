[~malliaridis]

Evidence, checked on the branch: the solr.SearchHandler form has the fields q, fq, fl, sort, rows and start. It has add and remove for the repeatable fields, a required query field, default values, Execute, Execute in Debug Mode, and Clear all. Its test class passes 6 of 6 in the test run recorded for the branch.

Compared with the Figma design, node 156-4776 (Regular Request Builder) shows more controls than the ticket text lists: Query Parser (defType), Query Operator (q.op), Writer Type (wt), Highlighting, Faceting, Spatial Options and Spellchecking toggles, Additional Query Parameters (key and value), and Paramsets. The three footer actions match the form.

Is the current field set sufficient for SOLR-17850, or should the form also cover the Figma-only controls above?

Options, from the narrowest:
 * Close SOLR-17850 on the current field set, and open follow-up tickets for the Figma-only controls.
 * Keep SOLR-17850 open until each Figma-only control is marked in, out, or for follow-up.
 * Add the Figma-only controls to SOLR-17850 now, with the toggles and paramsets as separate sub-tasks.

I would take the first option. The ticket text does not list the parameters, the form matches each behavior the ticket describes, and the extra controls are separate features whose defaults are better decided in their own tickets.

Note: SOLR-17849 (the Index & Query screen) hosts this form, so that ticket waits on this answer.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
