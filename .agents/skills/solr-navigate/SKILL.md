---
name: solr-navigate
description: Find Apache Solr source, developer documentation, tests and relevant PR history for a bounded implementation or architecture question. Use for orientation and context preparation before a full review or patch.
---

# Navigate Solr

Prepare a short, sourced context brief for the requested behavior. Start with the user's target revision and the applicable workspace instructions. Distinguish the factory workspace, the Solr checkout and the coordination board; do not read a dirty checkout as if it were the reviewed upstream revision.

Use [the source map](references/source-map.md) to choose an entrypoint. Trace only the code path needed for the question, identify its contract and nearest callers/tests, and consult the existing PR scan before proposing more research. The shared package's `handbook/INDEX.md` indexes available monthly material; it is optional context, not a prerequisite for source navigation.

For a changed extension point, inspect another real consumer when one exists. Search existing fixtures before suggesting a new harness. Treat PR descriptions and review comments as evidence to check against source. Open proposals describe possible future behavior; merged PRs still need reconciliation with the requested code revision.

Return the revision, useful file/symbol links, the behavior traced, nearby tests/consumers, relevant existing work and specific unanswered questions. Label a docs pointer as a pointer rather than an assessed subsystem. Stop at the requested context boundary; a navigation request does not launch broad archaeology or authorize a patch.
