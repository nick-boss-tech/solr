# SolrCloud drafts round 1, part SC1 (windows draft agent, 2026-10-11)

Slice: SOLR-15674, SOLR-5813, SOLR-17292, SOLR-12991, SOLR-15035. All five have an adopted answer, a receipt and an existing draft under `pr-drafts/solrcloud/`. No new drafts were needed.

Worktree: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`. HEAD is `bd71ed56f21`, as the task states. The brief names origin tip `6ae4a14ff46` on branch `pr-prepare`; that does not match HEAD, so HEAD was used as it stands. Tree was clean at start. No build, test, commit, push or public write.

Fork tips checked with `git ls-remote origin` against the receipts: all five match the gated heads (no head discrepancy).

## Per ticket

### SOLR-15674: corrected
- Head `ba01c83d4c5`, count 1 of 1, proof inconclusive by construction (stated in the draft). Verified 2026-10-03 (receipt ledger; answers agree).
- Symptom links (IndexSchemaFactory L178-L182, SolrConfig L199-L209) pointed at apache/solr. Re-pointed to nick-boss-tech/solr at the merge-base `b5c71bc5573`; text says so. (Formula and brief: symptom code links the merge-base.)
- Limits: the SolrCore watcher and managed-schema fallback lines now carry the follow-up offer (answers, ADOPTED; part p1 findings 6 and 7).
- Configset line: "in its own PR" changed to "separately". SOLR-15478 is a gated branch; no PR is shown for it.
- Checked, unchanged: Choice (ADOPTED as written); bold openers; changelog link at head and fragment title; head citations (IndexSchemaFactory L149-L150, L187; SolrConfig L161-L170, L387-L389; ZkSolrResourceLoader L56-L61); test description against the test file.
- Length about 4.9 KB, over the 3,500 guide. Accepted by answers item 7; not trimmed.

### SOLR-5813: corrected
- Head `90b8baa08ae`, count 3 of 3, verified 2026-10-07 (receipt takeover log).
- Symptom link (CloudDescriptor L53, empty name kept) re-pointed to the merge-base `cabedd1d968`; text says so.
- Proof: added the receipt's base-run outcome (class runs 3 tests with exactly 1 failure, on testEmptyCollectionNameDefaultsToCoreName).
- Limits, SOLR-5811 line: the draft said the ticket attributes the Overseer-queue failures to SOLR-5811. The Jira packet says the poison pill reaches the queue first and the follow-on problems are due to SOLR-5811. Reworded to match the packet; added the follow-up offer for the named broader issue (formula rule).
- Checked, unchanged: Choice (ADOPTED as drafted); Jira summary and 2014 commit messages; head lines L54, CoreDescriptor L209, test L33-L38, reload L128-L131; setter has no other caller in solr/core/src/java.
- Length about 3.7 KB.

### SOLR-17292: corrected
- Head `f614a42fbc8` (fork tip). Count 4 of 4; base run 4 tests with exactly 1 failure; verified 2026-10-10 (receipt re-gate, which is the receipt's date and head).
- The node-down remedy is in the branch (commit `f614a42fbc8`, on top of `e43200b0fb6`). The draft already describes the post-remedy branch: no Choice section, node-down bullet updated.
- Symptom link (PerReplicaStatesOps.persist L131-L147) re-pointed to the merge-base `14c7aac0d15`; text says so.
- Checked, unchanged: head citations (PerReplicaStatesOps L132-L158, ZkStateWriter L274, DistributedClusterStateUpdater L467, ShardLeaderElectionContextBase L241, ZkController L1968, L2064, L3058-L3068, L3074-L3079, Overseer L387-L390); Jira "throw the relevant exception" wording; duplicate ERROR line in Limits (answers ADOPTED).
- Length about 4.9 KB, over the 3,500 guide. The answers do not call for trimming; not trimmed.

### SOLR-12991: corrected
- Head `1a86966179e`, count 1 of 1, verified 2026-10-06 (receipt).
- Symptom link (RecoveryStrategy pingLeader L829-L836) re-pointed to the merge-base `97d973814336`; text says so.
- Proof: added the receipt's base-run outcome (1 test, exactly 1 failure, on the WARN assertion).
- Checked, unchanged: WARN Choice (ADOPTED as drafted); the 2018 ticket comment ("normal for recovery to fail talking to someone during cluster transitions") supports the Choice wording; test class name, 60-second poll on RecoveryStrategy's WARN, IOException in the cause chain; head L829-L837; changelog link and fragment title.

### SOLR-15035: corrected
- Head `12d7491e82c`, count 4 of 4, adjacent suites 8 of 8 and 44 of 44, verified 2026-10-03 (receipt).
- Proof opener and first bullet reworded to the receipt's recorded outcome: the ADDREPLICA change alone fails and the full change passes. No failure line is quoted, because the observed base failure line is not in the receipt, the round-28 review or the gate logs (answers draft-correction item 4, second option). The "CoreAdmin allowlist dropped numShards" bullet is kept.
- New-test citation tightened to L238-L265 (the test method; the old L238-L278 included the helper).
- Checked, unchanged: head citations AddReplicaCmd L305 and CoreAdminHandler L250; the split Limits (ADOPTED as drafted); no Choice section, since neither the answers nor the receipt call for one.

## Gaps and holds

- No hold on any of the five drafts. No DISCUSS item applies to this slice.
- SOLR-15035, ticket summary not verified: no Jira packet for SOLR-15035 is in `research/jira-context`. The "What happens today" line rests on the summary. Answers say confirm before opening.
- SOLR-15035, base failure: the "fails on base" claim rests on the receipt's fail-before verdict (PASS). No base failure line is on disk.
- SOLR-17292, record conflict: answers say gated at `e43200b0fb6` and verified 2026-10-04. The receipt says re-gated green at `f614a42fbc8` on 2026-10-10, and `e43200b0fb6` is an ancestor of it. Receipt wins: the draft uses `f614a42fbc8` and 2026-10-10. The openings slate line "remedy not in the branch; commit, then re-gate" is stale. The lead should confirm that the answers' "held for one in-branch fix" is lifted before opening.
- SOLR-15674 version-reset wording: "A new znode starts that version over at 0." is identical to the SOLR-15478 draft. The shared clause "ZooKeeper does not reuse this ID" matches; the tail differs (15478: "a recreated configset gets a new cache key"; 15674: "a recreated znode gets a new cache key"). Left as is; the lead decides whether the tails must match.
- Length: SOLR-15674 (about 4.9 KB, accepted by answers) and SOLR-17292 (about 4.9 KB, not addressed by answers) are over the 3,500 guide. Lead to decide on trimming.
- Branch side for this slice: no branch correction is owed beyond the SOLR-17292 remedy, which has landed. Changelog titles at the heads are accurate for their changes. The SOLR-17292 title covers the persist change only; the node-down behavior change is stated in the draft, not the title.
- Scope: no live PR, retire, held or audit-only ticket in this slice. All five are drafted or draftable.
- Not verified: the SOLR-15035 ticket summary (above). Everything else in the five drafts was checked against the receipts, the answers, the code at the cited SHAs and, where present, the Jira packets (SOLR-5813, SOLR-12991, SOLR-15674, SOLR-17292).
- Text check: no em dashes and no internal process vocabulary in the five drafts (the AI header is the only required non-prose line).
- Shared worktree: other agents are writing in the same worktree (uncommitted changes to other SOLR-* drafts, `reports/solrcloud-drafts-round-1-sc2.md`, and untracked drafts). None of those were touched by this part. The lead should stage only the five drafts and this report.
