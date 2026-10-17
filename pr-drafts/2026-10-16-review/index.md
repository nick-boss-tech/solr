# 2026-10-16 review follow-ups, PR text drafts: index (round 1)

Assignment: assignments/pool-review-2026-10-16-text-round-1.md. Claim: claims/pool-review-2026-10-16-text-round-1.md. Drafted by vm2, 2026-10-17. Nothing here is applied to a live PR. No PR, comment, JIRA or email was touched, and no history was rewritten.

| Item | PR | Draft | Result | Visible characters | Notes |
| --- | --- | --- | --- | --- | --- |
| 1 | 5139 (SOLR-7550) | PR-5139-text.md | Drafted | 3,491 | Title names the SolrCoreInitializationException rule. Proof cites receipts/SOLR-7550.md at fcc8a874abbb (PeerSyncHandleResponseTest 3 of 3, PeerSyncTest 1 of 1, proof leg fails at line 70). Update-loss cost added to option 1. Body links still point at 5f4570b20a and need re-pointing when applied. |
| 2 | 5156 (SOLR-9396) | PR-5156-text.md | Drafted | 3,172 | Cut from 3,984. Decision text and proof kept. Head counts from reports/SOLR-9396-distrib-row-field-verify.md at f62c06588f27. |
| 3 | 5169 (SOLR-12505) | PR-5169-text.md | Drafted | 3,460 | Cut from 6,723. Head counts (60 of 60) from receipts/SOLR-12505.md at cc3b9d681e. The body no longer says every new test fails without its fix: the closing-brace and leading-quote test has no fail-before run on record. Trailers not touched; the rewrite needs Nick's go-ahead. |
| 4 | 5141 (SOLR-8009) | PR-5141-text.md | Drafted | 3,477 | One sentence added on the SERVICE_UNAVAILABLE failure for a shard with no live replica, traced to HttpShardHandler.java line 255. Not run by any test; the draft says so. Nick may want a run before this goes live. |
| 5 | 5145 (SOLR-8240) | PR-5145-text.md | Drafted | 3,461 | "Three f=txt examples" corrected to one: only the V1 /update/json/docs tab changes (the sample's params apply to that path only). SOLR-10424 dependency stated; the body says not to use the flag as a default with f until it lands. The guide's "Setting JSON Defaults" section still recommends the default. Whether to edit that section in this PR is for the owner. |
| 6 | 5138 (SOLR-7520) | PR-5138-text.md | Drafted | 2,929 | Wider change named: complete() runs on every post filter (CommandHandler.java lines 262 to 266 at 15cc9ba5a6). Changelog fragment and tests do not yet say so; that edit is owed to the branch owner. |
| 7 | 5170 (SOLR-18368) | PR-5170-text.md | Drafted (not skipped) | 2,716 | Title becomes "SOLR-18368: Fix stale names in the Basic Authentication Plugin example". The round-1 SOLR-18368 fix does not change the title. The body's first sentence, which said the PR carries no ticket, is reworded. |

## Open for Nick

- The PR 5169 commit trailers (68d4b0b668, 1249998133, d0e8385b9b). A rewrite is a force-push to an open PR head and needs Nick's go-ahead. Nothing was rewritten.
- Item 5: whether the guide's "Setting JSON Defaults" section should change in PR 5145.
- Item 4: whether to run a test for the shard-with-no-live-replica case before the new sentence goes live.
- Item 6: the changelog fragment and test names for PR 5138.
- Re-pointing the body links to the current heads (items 1, 2, 4 and 6) when the text is applied.
