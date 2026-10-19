# Skip record: SOLR-13687

- Ticket: SOLR-13687, Enable the bin/solr script to accept a solr url to run commands. JIRA read 2026-10-19 (this pass, evidence tool): status Resolved, resolution Fixed (2026-10-10, by a JIRA comment from epugh: "I believe this has been solved... all the commands take in a solr url"); created 2019-08-10, updated 2026-10-10; no commit on the pin names the ticket.
- Verdict: keep-open.
- Draft branch: solr-13687-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code and history checked at this head (detached worktree).
- Source: remediation redo of batch 06, item 2, under assignments/pool-aaa-skip-verdict-remediation-06.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the feature from audit evidence; the code is read here.

## Ticket reading

The bin/solr CLI commands (healthcheck, create, create_core, create_collection, delete, version, config, autoscaling) should accept the base URL of any node, so a user does not need to run them on a Solr host. The JIRA ticket was marked Resolved and Fixed in 2026-10-10, by a comment saying the commands now take a Solr URL.

## Code and history at the pinned head

- The CLI accepts a URL: solr/core/src/java/org/apache/solr/cli/ApiTool.java:38 (`--solr-url`), :82-84 (`getSolrUrlFromUri`, a client built from the URL); AssertTool.java:320-322 uses `CLIUtils.normalizeSolrUrl(url)`.
- The URL handling first appears in the CLI through several commits: 44668dce33 (2023-08-08, "SOLR-16800: Move towards using the root of Solr as the -solrUrl in the CLI") and ddfe2f1b44 (2023-10-17, "SOLR-14496: Add Basic Auth support to SolrCLI"). `git log -S normalizeSolrUrl` in the cli directory gives these as the earliest entries listed.
- The listed tools do not all show a URL option in their own class: the grep for solr-url, solrUrl, SOLR_URL, zk-host and ZK_HOST gives 9 hits in CreateTool and 6 in ConfigTool, 3 in VersionTool, and 0 in HealthcheckTool and DeleteTool. Those two may take the option from a base class; this pass did not trace it.

So the code supports a URL for part of the command set, but no single commit fixes the ticket, and the commands listed in the ticket were not all checked.

## Reproduction attempts

Not run. A run would call each listed command with a node URL and no local install.

## Gap analysis

The JIRA resolution is a comment on a 2026 ticket, not a commit. The code has URL support spread over several commits, and two of the listed tools show no URL option in their own files. Whether every listed command accepts the URL is the missing check.

## Verdict justification

keep-open: the attempts made are the code reads, the commit history above, and the per-tool option grep at the pinned head. The missing piece is (1) a run of each listed command against a node URL, and (2) the identity of the change that made the whole set URL-capable (a fixing commit for close-fixed). close-fixed is not supported: no single fixing commit is identified, and two tools are not confirmed. close-answered is not supported: the answer is code, not docs.

Missing for a closing verdict: the per-command run, and the fixing commit.

## Limits

This record does not run the CLI. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
