# Pool assignment: SOLR-17752 review fix round 1 (shard address fallback in the exception, request purpose in the warning, shorter changelog title)

Status: DONE (GATE GREEN at 041c5b0b2e44e903e7010a40530de3a24ea9bb06, 2026-10-16)

- Capability tags: implementation, gate.
- Intended host: vm1. Claimed and executed by the vm1 implementation lane at the direction of the main agent, 2026-10-16.
- Branch: solr-17752-submit, from e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9.

## Direction

A review of the opened draft PR raised five items; the main side verified the facts against the source and the Jira ticket. Fix shape:

1. In SolrResponseUtil.getSubsectionFromShardResponse, compute the display name once (node name if non-null, else shard address) and use it in both the log.warn and the SolrException below it; the exception previously concatenated the raw node name and printed "null".
2. Add the shard request's purpose to the log.warn line (ShardRequest.purpose via ShardResponse.getShardRequest(), an int, no query text), keeping the line's existing shape and the response key names.
3. Shorten the changelog fragment title to: Don't log the shard request or response in corrupted-response warnings.
4. Nothing else. Error section contents stay unlogged (parse errors can echo the query); the ticket is scoped to exactly this log line, so no scope change.

## Gate

Blocking-flock runner on vm1 (solr-gradle.sh only): changelog YAML parse; :solr:core:tidy with the GATE INVALID guard; Error Prone compile of main and test sources; focused TestShardResponseLogging at the new head counted from fresh JUnit XML, plus the delta proof of the new null-node-name test against pre-fix production; wider caller tests (DistributedTermsComponentTest, DistributedExpandComponentTest, DistributedSpellCheckComponentTest, TestDistributedGrouping); :solr:core:check -x test. Push plainly to the fork only on a fully green gate. Outcome recorded in receipts/SOLR-17752.md.

## Outcome (2026-10-16, vm1)

DONE, GATE GREEN. Fix commit 041c5b0b2e44e903e7010a40530de3a24ea9bb06 pushed plainly to solr-17752-submit (fast-forward from e5c0a64f993c, ls-remote verified). Runner ~/workspace/tools/g17752-reviewfix-gate.sh, log g17752-reviewfix-gate.log, seed 17752C0FFEE1775: changelog parse rc 0; tidy clean; Error Prone compile rc 0; head run TestShardResponseLogging 2 tests, 0 failures from fresh JUnit XML; delta proof by content (on pre-fix production the new null-name test fails, the exception cause reading "is absent in response from null"); caller tests DistributedTermsComponentTest 1 of 1, DistributedExpandComponentTest 1 of 1, DistributedSpellCheckComponentTest 1 of 1, TestDistributedGrouping 2 of 2; module check rc 0. The first run at 0515533382 was GATE INVALID (tidy joined one wrapped line in the new test); the tidied formatting was folded into the commit by amend and the full gate re-ran green at the amended head. Receipt: receipts/SOLR-17752.md top section. Flagged for the main side: stale lines in pr-drafts/build-docs/SOLR-17752.md, quoted in the lane's report.
