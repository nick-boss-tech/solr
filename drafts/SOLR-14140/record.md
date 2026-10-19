# Skip record: SOLR-14140

- Ticket: SOLR-14140, Every Solr plugin must be loadable from packages. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-23, updated 2020-01-21, no fix versions, no links, no comments. The description calls it an umbrella for all pluggable types.
- Verdict: keep-open.
- Draft branch: solr-14140-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited name grepped at this head (detached worktree).
- Source: remediation redo of batch 05, item 2, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-5, 2026-10-17, confirmed-skip) cited the umbrella reading from audit evidence; the package code is checked here.

## Ticket reading

Umbrella: the package loader works for only some plugin types, and the ticket asks to identify and fix all of them. It names no failing plugin type.

## Grep and code reading at the pinned head

- The package code is in solr/core/src/java/org/apache/solr/pkg/: `class PackageAPI` (PackageAPI.java:64), `SolrPackageLoader`, `PackagePluginHolder`, `PackageListeners`, `PackageListeningClassLoader`.
- solr/core/src/java/org/apache/solr/core/PluginBag.java:167: `new PackagePluginHolder<>(info, core, meta)`. A plugin registered through a PluginBag whose info names a package is loaded through the package holder. This is the path the umbrella asks every plugin type to take.
- `git grep -l "PluginBag<" 498a09aea97aac86c2cc7ac114113b946d249942 -- 'solr/core/src/java/*'` finds six source files that use PluginBag. Which plugin types each one covers, and which plugin types do not go through a PluginBag at all, was not surveyed in this pass.
- SolrCore.java:308-309 uses `coreContainer.getPackageLoader().getPackage(pkg)` to resolve a package version for a core.

So the package loading is wired into the plugin registry. The umbrella's question, which plugin types are still loaded without a package, is not answered by this read.

## History

- No commit names SOLR-14140. The commits that added PackagePluginHolder were not identified in this pass.

## Reproduction attempts

Not run. A test per plugin type (put the plugin in a package, reference it, check it loads) would be the check; this pass did not write or run those tests.

## Gap analysis

The ticket is an umbrella with no defect named. The concrete work is a per-type survey and one ticket or check per plugin type that does not load from a package. That decomposition is what the ticket itself asks for and is not done here.

## Verdict justification

keep-open: the attempts made are the grep and code reads above at the pinned head, the Jira read, and the PluginBag wiring check. The missing piece is the per-plugin-type survey (which types go through PluginBag and PackagePluginHolder, and which do not) and a test per type. close-obsolete and close-fixed are not supported: the umbrella is open and the survey is not done. pr-needed is not supported: no plugin type is named as failing.

Missing for a closing verdict: the survey list and the per-type tests, or a decision to split the umbrella into per-type tickets.

## Limits

This record does not reproduce a package load failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
