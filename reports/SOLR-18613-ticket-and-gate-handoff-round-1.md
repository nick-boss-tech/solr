# SOLR-18613: ticket created and Linux gate handed off

Status: DONE for the records claim; implementation remains published-head proof pending.

- Completed: 2026-11-02.
- Authority: Nick requested JIRA creation and a gate handoff under the ticket number.
- Records claim: claims/SOLR-18613-ticket-gate-handoff-windows-round-1.md, commit 2acfd43988db034ee62ed21f17f14bcdc275201b.
- Ticket: https://issues.apache.org/jira/browse/SOLR-18613
- Summary: Consistently enforce LBSolrClient request attempt limits.
- Type/status: Bug / Open.
- JIRA issue ID: 13676812. The create response was independently read back; the stored summary and full description match the local draft at research/lb-attempt-limits-pr1/jira-issue-draft.txt.
- Duplicate search: current SOLR searches for retry/retries/attempt/LBSolrClient summaries, limit with retry/attempt text, numServersToTry and the query-of-death phrase found related work but no equivalent ticket. SOLR-11107 was read in full and concerns zombie-classification thresholds. SOLR-18341 and SOLR-18402 concern eligibility and classification.
- Scope: existing PR 1 implementation only. CloudSolrClient controls and server/per-shard controls remain separate proposed follow-ups. No PR 2/3 source work was performed.
- Existing fork branch retained: solr-lb-attempt-limits-pr1-submit.
- Independently confirmed remote source head: c2d2c6259462a7e848008b065d65df69a1b82ebb.
- Complete tree: 4d09e238ca0a7890e125b5f36d2dd78ff90f7110, matching vm2's locally tested repaired tree.
- New numbered gate: gates/SOLR-18613-published-head-round-1.md.
- Gate posted and byte-verified at board commit 7ff05dc33b90d628f34ec17b1ba44adbd6682165, blob 328e2efc231399473079061ab430f3ced58fc012. Poster verified reachability from the then-live branch.
- New receipt target: receipts/SOLR-18613.md, owned by the future executing Linux host. No execution claim or success is asserted by this handoff.
- Required work: exact published-head tidy, 47 focused/neighboring methods, root check in a full clone, fresh XML/logs, clean tree, remote head confirmation; reconcile the four existing synthetic-base behavioral failures and carry their limits forward. Rerun the negative leg only if existing evidence cannot support the reconciliation.
- Existing receipt receipts/lb-attempt-limits-pr1.md remains historical evidence at 28dbd2edfce1520c354dbc71b555db4ced3fbebe; it was not edited. Its old publication blocker has been resolved by the published replay.
- No source/changelog edits, Windows Gradle run, upstream PR, follow-up public comment, or mailing-list message.
- Public disclosure: the ticket explicitly says AI-assisted text and implementation with human review pending. It does not claim that Nick has reviewed this new issue description.

