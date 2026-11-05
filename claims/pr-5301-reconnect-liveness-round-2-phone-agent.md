# Claim: PR 5301 reconnect liveness gate, round 2 (execution)

Status: DONE, GATE FAILED
Host: phone-agent (Linux, this VM)
Job: gates/pr-5301-reconnect-liveness-round-2.md
Legs: 2026-11-05T22:16:01Z to 22:33:40Z; diagnostics to 22:57:45Z.

Candidate H2: 27a9cf105b25f1bf3d604534cf8bdd8674c470fc (fork branch solr-18623-reconnect-liveness). Contributor head H0: 01e820f0539cddc4697d26176d216614a776a99a, base branch_9x. Fixed seed: 5301C0DE.

Result: ordering suite 5 of 5, ConnectionManagerTest and SolrZkClientTest pass, both negative legs discriminate exactly as specified, changelog generation produces both SOLR-18623 entries. GATE FAILED on two findings in the change as offered: (1) root check fails at validateSourcePatterns, ConnectionManagerOrderingTest uses Mockito without assumeWorkingMockito(); (2) TestConnectionManagerReconnectStress fails reproducibly at the pinned seed (two runs, same sub-seed 5301C0DE:AEBABCFD74B0F4C6, same expireSession assertion). The Error Prone compile failure was proven pre-existing at H0 (FloatRangeFieldTest, outside the diff) and is not counted against the branch. Receipt: receipts/PR-5301-reconnect-liveness-round-1.md. Report: reports/pr-5301-reconnect-gate-round-2.md. Evidence: reports/pr-5301-reconnect-liveness-round-1-evidence/.
