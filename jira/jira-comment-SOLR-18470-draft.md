# Jira comment for SOLR-18470 (posted 2026-10-23 at Nick's direction)

🤖 _AI text below_ 🤖 _(reviewed by Nick Shanin)_

[~dsmiley], [PR #5202|https://github.com/apache/solr/pull/5202] covers SOLR-18446 only: the {{collection}} parameter can no longer override the path on distributed requests. It deliberately does not attempt this ticket.

Taking your advice on the dev list to line up a reviewer before starting work: before building anything here, I wanted to check whether this is a direction you want taken up, and whether you would be willing to review it.

The plan I have in mind:
 * Add path scope as a first-class characteristic of {{SolrQueryRequest}}: the collection or core named in the URL path, with an alias or comma-separated list resolved to its set of collections where that applies.
 * Let each handler judge whether it can honor that scope; a collection handler that cannot should return HTTP 400 rather than act on the core or node that happens to serve the request.
 * Start with collection handlers. Treat the core-path behavior, including {{SearchHandler}} searching the whole collection in SolrCloud mode, as a separate slice if you agree it is too much for one change.
 * Parameters such as {{shards}} may narrow the path scope, never expand it.

Three questions: is that first slice the right size, should the core path be in or out of it, and is there anything in the description you would now narrow or drop?
