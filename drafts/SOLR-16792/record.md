# Skip record: SOLR-16792

- Ticket: SOLR-16792, "Solr CoreAdmin API status throws a null-pointer exception when the index is empty/corrupted". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-05-08, updated 2025-11-29, no fix version. One comment: epugh (2025-11-29) asks whether it still happens and for reproduction steps. No reply from the reporter is recorded.
- Verdict: keep-open. Unchanged from the earlier verdict; this record replaces the earlier closeout with a fresh pinned-head read and corrects one claim in it.
- Draft branch: solr-16792-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:56-14:57 UTC.

## Ticket reading

After a collection build, the logs show `SolrCoreInitializationException: SolrCore '...' is not available due to init failure: Error opening new searcher`, and the CoreAdmin STATUS call throws a null-pointer exception on that core. The stack in the ticket is cut at `CoreAdminOperation`, which the ticket's text places in an earlier layout.

## Code reading

- `solr/core/src/java/org/apache/solr/handler/admin/api/CoreStatus.java:118-150`: a core that is not loaded is filled from its descriptor only (lines 118-137). A loaded core is opened with `cores.getCore(cname)` in a try-with-resources, and the loaded-core fields are read only when `core != null` (line 143 onward).
- `CoreStatus.java:146-150`: for a ZooKeeper-aware core, `core.getCoreDescriptor().getCloudDescriptor().getLastPublished().toString()`. This is the one dereference on that branch that can fail if its return value is null.
- `CoreStatus.java:152`: `core.getSolrConfig().getZnodeVersion()`, on the same loaded branch.
- `solr/core/src/java/org/apache/solr/cloud/CloudDescriptor.java:41`: `lastPublished` is initialized to `Replica.State.ACTIVE`. `CloudDescriptor.java:66-71`: the getter returns it and the setter assigns whatever it is given. `CloudDescriptor.java:137`: a reload copies the other descriptor's value.
- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:1917`: `cd.getCloudDescriptor().setLastPublished(state)` runs at publish time, with the publish `state`. This reading did not check whether that `state` can be null on any path. The earlier record's statement that "no caller passes null" was based on the method signatures, not on the call sites, and is not confirmed.

What this shows at the pin: the only dereference on the loaded-core branch that a null could reach is `getLastPublished().toString()` (`CoreStatus.java:146-150`). Its safety depends on whether the publish state set at `ZkController.java:1917` is ever null, which is not established. No other path on that branch was found that makes the NPE.

## History

- The ticket's stack names `CoreAdminOperation`, which predates the `api/CoreStatus.java` layout. The refactor was not traced on the fork.
- No commit on the pinned head names SOLR-16792 (evidence tool: no hits).

## Reproduction attempts

Not attempted. The maintainer's request (epugh, 2025-11-29) is for steps that reproduce the NPE. A reproduction on the pinned head would build a collection, damage one replica's index on disk, load the cores, and call the CoreAdmin STATUS for that core, with the server log. That run is the missing piece and is not part of this review-only pass.

## Gap analysis

The reading identifies one candidate site, `CoreStatus.java:146-150`, whose null case depends on the publish state at `ZkController.java:1917`. Whether that state can be null is open. The ticket's stack cannot confirm or rule it out, because it comes from a different code layout.

Missing piece for a verdict change: the reproduction the maintainer asked for, or the full stack trace from a current build. With either, the record can name the exact line that fails, or close as cannot reproduce if a run on the pin does not fail.

## Verdict justification

keep-open meets the rubric: the code path is read at the pin (`CoreStatus.java:118-152`, `CloudDescriptor.java:41, 66-71`, `ZkController.java:1917`), the candidate is named, and the missing piece is named (the reproduction or a current stack trace). close-cannot-reproduce needs a reproduction attempt with a result on the pin, and none has been run, so it is not met. The code-path argument alone is not enough for that close.

## Limits

This record does not run a reproduction, does not check the pre-refactor `CoreAdminOperation` code, and does not establish whether `ZkController.java:1917` can receive a null state. It does not post anything to JIRA or GitHub.
