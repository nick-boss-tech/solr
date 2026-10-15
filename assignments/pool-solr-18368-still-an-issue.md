# Pool assignment: SOLR-18368, is the issue still there?

Status: DONE

Capability tags: `review`, `premise-run`. Intended host: windows for the reading; any run the reading specs goes to vm1 or vm2. Staffing: 1. Claim path: claims/pool-solr-18368-still-an-issue.md.

Background: SOLR-18368 is RESOLVED/Fixed upstream. The removal the ticket asked for (CloudSolrClient.Builder.withInternalClientBuilder) landed upstream in the project's own commit 3ef3d093c06 on main, and the same removal on branch_10x. The branch (solr-18368-submit) is a salvage: its gated tree is 1acaced80b2, and its live tip a7ec9a1b65c05b2e359e9a186fc2938fe7b7ca7d is docs-only past the gate, with a final diff of exactly one file against its base: `solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc`. The salvage record (receipts/SOLR-18368.md) says that on the main of 2026-10-04, that page's example did not compile, for three reasons: it called the removed withInternalClientBuilder, it wrote HttpJettySolrClient.Builder() without `new`, and the sentence above the example named Http2SolrClient.Builder, which no longer exists. The branch fixes all three.

Nick's rule (2026-10-11): if an issue still exists on a ticket Jira shows resolved, open a PR for it or file a new ticket. That makes the open question factual, and it is this assignment's question: **does the ref-guide defect the branch fixes still exist on current apache main?** Main has moved since 2026-10-04; the page may have been fixed upstream in the meantime, which would retire the branch, or it may still carry one or more of the three defects, which would settle the owner call standing in the receipt.

## Scope

1. Fetch current apache/solr main and record its tip and date. Read the page above on current main and check each of the three recorded defects separately: the withInternalClientBuilder call, the missing `new`, and the Http2SolrClient.Builder sentence. Also grep the page and the ref guide for any other mention of either removed name.
2. Compare against the branch tip: confirm the branch's version of the page resolves exactly the defects found present on main, and introduces nothing else (the final diff is one file; verify that still holds at the live tip with `git ls-remote` and a diff against current main, noting the branch's base is older main).
3. Check whether any upstream commit since the receipt (2026-10-04) touched this page, and if so what it changed; a partial upstream fix changes the verdict shape (the branch may then be partly stale, and the report must say which defects remain).

Reading should settle this: the defects are text on a page. If reading cannot settle a point (for example, whether the corrected example compiles against current main's classes, if the page on main has drifted into a shape neither the receipt nor the branch describes), do not guess: spec the exact run for a build host (the snippet compile check against built :solr:solrj and :solr:solrj-jetty classes, or a buildLocalSite run on the page; name the tree, the command shape, and the expected result each way), and record that spec as a claimable run spec appended to this assignment file in the same push as the report, marked UNCLAIMED and tagged `premise-run`, so a build host can claim it without a new assignment round.

## Verdict

One verdict in the report:

- **STILL AN ISSUE**: at least one of the three defects is present on current main, quoted with file and line. The consequence per Nick's rule (a PR opens, or a new ticket is filed) is the main side's to execute; the report's job is the evidence.
- **NO LONGER AN ISSUE**: all three defects are gone from current main, with the upstream commit or commits that removed them named. The branch then joins the retire pile, and the report says so.

A split result (some defects fixed upstream, some not) is reported as STILL AN ISSUE with the remaining defects listed and the branch's now-stale hunks named.

## Deliverable and limits

Deliverable: `reports/solr-18368-still-an-issue.md`, verdict up front, then the per-defect evidence against current main.

This assignment is read-only apart from the claim, the report, and (only if needed) the appended run spec in this file. Do not edit the branch, do not open a PR, do not file a ticket, do not post anything to Jira, and do not run a build or test on the reading host.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
