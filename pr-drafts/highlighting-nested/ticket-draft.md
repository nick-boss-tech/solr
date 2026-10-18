# Ticket draft: broader highlighting of queries inside function queries (for Nick to decide and file)

Status: DRAFT. Not filed. Filing is a public write and Nick's call.

Decision needed first: SOLR-2681 already covers this problem. Its title is "Lucene highlighting unable to extract terms from sub-query of a FunctionQuery", and PR #5104 is only the top-level `query(...)` part of it. Two options:

1. Attach this work to SOLR-2681, and have #5104's changelog and Limits point to that ticket. No new ticket.
2. File a new follow-up ticket, linked to SOLR-2681 as "relates to", and keep #5104 for the top-level case only.

The draft below is for option 2. For option 1, use the body as the PR description and skip the ticket.

---

Issue type: Improvement (or Bug). Priority: Minor. Component: highlighting. Related: SOLR-2681.

Summary: Highlight query terms inside any function query that wraps a query, not only at the top level

Description:

SOLR-2681 lets the highlighter read the query inside a top-level `query(...)` function query. Queries nested deeper, such as `{!func}product($v1,$v2)` with a dismax query in `$v1`, still highlight nothing. The highlighter does not walk into the value sources that Lucene's `MultiFloatFunction` (product, sum and similar) and `DualFloatFunction` (sub, div and similar) build.

This ticket covers walking those value sources, so the terms inside them are highlighted. Other value sources, such as Solr-specific functions, are out of scope here.

---

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
