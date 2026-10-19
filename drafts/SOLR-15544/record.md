# Skip record: SOLR-15544

- Ticket: SOLR-15544, ValueSourceParser init gets called twice. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-07-15, updated 2021-09-28, no fix versions, no issue links, no comments.
- Verdict: keep-open.
- Draft branch: solr-15544-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (checked out as a detached worktree).
- Source: remediation redo of batch 03, item 2, under assignments/pool-aaa-skip-verdict-remediation-03.md. Earlier record (round audit-1, audit verdict confirmed-skip, 2026-10-04) was checked against upstream main c8ab71430f and is background only.

## Ticket reading

The reporter runs one Solr node as a slave of a master that imports, with a custom ValueSourceParser (EcpcValueSource) that holds static members. The static values are lost after a background core reload, and the reporter asks what triggers the reload and how to keep the static fields.

## Code reading

- solr/core/src/java/org/apache/solr/core/SolrCore.java:1100: a new SolrCore runs `valueSourceParsers.init(ValueSourceParser.standardValueSourceParsers, this)` on every construction. The bag is a field of the core (SolrCore.java:3084-3085), so every core instance has its own PluginBag.
- solr/core/src/java/org/apache/solr/core/SolrCore.java:775-812: `SolrCore.reload` calls `cloneForReloadCore`, which returns `new SolrCore(...)`. A reload therefore runs the init in :1100 again, for the new core.
- solr/core/src/java/org/apache/solr/core/CoreContainer.java:2014: `CoreContainer.reload` calls `core.reload(coreConfig)`, which is the path the reporter's background reload takes.

So the ticket's "init called twice" follows from the design: each reload builds a new core, and the plugin is initialized in the new core. The code read does not show whether the reporter's static fields survive that step, because that depends on which class loader the plugin class is loaded by. That was not read at this head (the ConfigSet and resource loader lifecycle is not traced here).

## History

No commit history was used for this record. The ticket is a 2021 support question and the fork's git log search for SOLR-15544 returned no commits (evidence tool output: none).

## Reproduction attempts

Not attempted in this pass. The ticket's symptom ("init gets called twice") is the designed behavior of a core reload, as the code path shows, so a run would only confirm it. The question that would need a run is whether a static field in a plugin class survives a reload, and that depends on the reporter's plugin jar and class loading, which are not in this repository.

## Gap analysis

The reporter's question is about plugin lifecycle, not a defect in a code path. Nothing on main fixes it, and nothing on main makes it obsolete. The evidence does not show whether the reporter's static state should survive a reload, so no closing verdict is supported.

## Verdict justification

keep-open: the attempts made are the code path reads above at the pinned head and the Jira read. The missing piece is the class loading question: whether the plugin's static members are reloaded with a new class loader on a core reload, and whether Solr should keep plugin static state across reloads. That is a maintainer decision, not a closing fact.

Missing for a closing verdict: a reproduction with the reporter's plugin shape (a static field in a ValueSourceParser, one reload through CoreContainer.reload) at the pinned head, with the result, or a maintainer answer on whether static plugin state should survive a reload.

## Limits

This record does not reproduce the static-field loss. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
