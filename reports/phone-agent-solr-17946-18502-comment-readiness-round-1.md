# Report: comment readiness for SOLR-17946 and SOLR-18502, round 1

By the phone agent, 2026-10-24. Sources re-read live on 2026-10-24. No comment is drafted for either ticket; the reasons differ per ticket. Nothing was posted.

## SOLR-17946: Disabled Overseer shouldn't create ZK queues

- Current state: Open, unassigned, unresolved. Last activity 2026-10-14T00:08Z. One comment: David Smiley's, which states a hypothesis in his own words ("I suspect there are no such queues" when every node sets `SOLR_NODE_ROLES="data:on,overseer:disallowed"`). No update since.
- Is the scan lead still live: yes as a question, no as evidence. Nothing has verified or refuted the hypothesis. The relevant code on current main is in `solr/core/src/java/org/apache/solr/cloud/ZkController.java`, `solr/core/src/java/org/apache/solr/core/CoreContainer.java`, and `solr/core/src/java/org/apache/solr/core/NodeRoles.java`; reading them does not settle runtime queue creation either way.
- Recommendation: no comment now. A staged ask under the comment formula leads with evidence, and there is none yet; a comment would only repeat Smiley's hypothesis back at the ticket. This assignment's limits (no tests) bar the one step that would change that.
- Missing evidence, stated exactly: a run that starts a node with the overseer role disallowed and observes whether the Overseer queues are created in ZooKeeper. If the owner wants this lead pursued, that is a premise-run assignment for a Linux host; its result would then support a status-note or staged-ask draft.

## SOLR-18502: INSTALLSHARD (and RESTORE) should skip write.lock when copying index files

- Current state: Open, unresolved, assigned to Jason Gerlowski. Last activity 2026-10-14T15:43Z (his comment and the revert notice). Four comments total.
- Main today tells the same story as the ticket: the change `f3a798e424bb` (SOLR-18502: INSTALLSHARD skips write.lock download) and its revert `f586eda8ba1` are both in `origin/main` history, with no later commit for this ticket. The change has not been re-landed.
- The thread is complete and owned: Chris Hostetter reported why the S3 test failed ("Test data should include a write.lock"), and Jason reverted, said the fix should not be bad, and said he could not address it immediately. He is the assignee and the author of both commits.
- Recommendation: no comment now. A comment from the owner would carry no new information to the one person who holds the context, and "any update" pressure ten days after a revert he explained is not a contribution. The useful next step on this ticket is the fix itself (re-land with the test data corrected), which is implementation work outside this assignment's scope and a separate owner decision under the warm-lead rules: Jason is active and assigned, so it would start from him, not from a comment.
- Missing evidence: none for a comment decision. If the fix is ever picked up, the S3 test data handling in `S3InstallShardTest` is the known failure point, per Hostetter's comment on the ticket.
