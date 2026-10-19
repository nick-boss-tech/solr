# Skip record: SOLR-14133

- Ticket: SOLR-14133, PackageLoader does not invoke SolrCoreAware#inform() for classes loaded from packages. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-20, updated 2020-01-21, no fix versions, no links, empty description, no comments. The ticket is still Open in JIRA; the fix is on the fork.
- Verdict: close-fixed.
- Draft branch: solr-14133-draft-close (new; supersedes the keep-open draft solr-14133-draft-open, which is kept).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited name grepped at this head (detached worktree).
- Source: remediation redo of batch 05, item 3, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-3, 2026-10-06, confirmed-skip) said the bug was fixed by SOLR-14525; this record checks that claim at the pin.

## Ticket reading

Classes loaded from a package do not get SolrCoreAware.inform() called. The ticket has an empty description and no comments, so the report is the summary line.

## Fixing commit and code at the pinned head

- Fixing commit: e841d7625c (2020-06-02), "SOLR-14525 For components loaded from packages SolrCoreAware, ResourceLoaderAware are not honored (#1547)". `git merge-base --is-ancestor e841d7625c 498a09aea97aac86c2cc7ac114113b946d249942` succeeds, so the fix is on the pinned head.
- The commit changes solr/core/src/java/org/apache/solr/pkg/PackagePluginHolder.java, solr/core/src/java/org/apache/solr/pkg/PackageLoader.java, solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java, solr/CHANGES.txt and solr/core/src/test/org/apache/solr/pkg/TestPackages.java.
- Changed code present at the pin: PackagePluginHolder.java:159-165, `handleAwareCallbacks`. When the instance is a SolrCoreAware, it calls `core.getResourceLoader().addToCoreAware(coreAware)`, and if that returns false it calls `coreAware.inform(core)` directly (lines 160-164). A ResourceLoaderAware instance is handled at :166 by `SolrResourceLoader.assertAwareCompatibility(...)`.
- The call site is PackagePluginHolder.java:146, `handleAwareCallbacks(newest.getLoader(), instance, core)`.
- A test covers it: solr/core/src/test/org/apache/solr/pkg/TestPackages.java, which implements SolrCoreAware in its test handler class (TestPackages.java:705 and the import at :83).

## History

- e841d7625c is the commit that fixed the reported problem; no later commit on the pin removes its code path (the `handleAwareCallbacks` body is present, as above).

## Reproduction attempts

Not run in this pass. The close-fixed rung needs the commit, the ancestor check and the changed code present at the head, which are shown above. The package test (TestPackages.java) was not run here.

## Gap analysis

The fix is in, on the pin, with a test in the same commit. The JIRA ticket is still Open, which is a tracker state, not a code gap. What this record does not show is a run of TestPackages on this head.

## Verdict justification

close-fixed: a commit on main (e841d7625c) fixes the reported problem (SolrCoreAware is honored for package components), the commit is an ancestor of the pinned head, and the changed code is present at the head (PackagePluginHolder.java:159-165 with the inform call at :164). The JIRA status (Open) is not evidence against the close; the fix is on the fork's main.

## Limits

This record does not run the package tests on the pin. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
