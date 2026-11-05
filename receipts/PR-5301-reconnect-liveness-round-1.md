# Receipt: PR 5301 reconnect liveness gate, round 1 job at repaired head H2

GATE FAILED at H2 27a9cf105b25f1bf3d604534cf8bdd8674c470fc (fork branch solr-18623-reconnect-liveness; H1 19579b2ed239 plus the formatting-only tidy commit recorded in reports/pr-5301-reconnect-repair-round-1.md). Contributor head H0 01e820f0539c, base branch_9x.

Job: gates/pr-5301-reconnect-liveness-round-1.md as superseded for the exact head by gates/pr-5301-reconnect-liveness-round-2.md. Executor: phone-agent. Seed: 5301C0DE. Legs 2026-11-05T22:16:01Z to 22:33:40Z; diagnostics to 22:57:45Z.

Passed: ordering suite 5 of 5; ConnectionManagerTest clean (testLikelyExpired and testReconnectWhenZkDisappeared executed; the one skip is the pre-existing ignored testConnectionManager); SolrZkClientTest 7 of 7; negative leg A failed at its specified assertion with the production file reverted to H0; negative leg B failed all three stale-state tests under the accept-all mutation; changelog generation produced both SOLR-18623 entries (the validator's own errors are cross-branch bookkeeping and name no fragment of this branch).

Failed, both in the change as offered:

1. Root check fails at :solr:core:validateSourcePatterns: ConnectionManagerOrderingTest uses Mockito but has no assumeWorkingMockito() call.
2. TestConnectionManagerReconnectStress fails at the pinned seed in two independent runs, same sub-seed and same assertion: after an injected session expiration the reconnect listener is not released within the drain window (expireSession, test line 183).

Not counted against the branch: the Error Prone compile failure (FloatRangeFieldTest FloatingPointLiteralPrecision warning) reproduces identically at H0 in a file outside the diff.

Report: reports/pr-5301-reconnect-gate-round-2.md. Evidence: reports/pr-5301-reconnect-liveness-round-1-evidence/ (archive with SHA-256 manifest).

## Evidence draft for the eventual PR text (from these results; nothing posted by this gate)

Verified on Linux at 27a9cf105b (branch_9x): the five new ordering tests pass, and each fails when its guard is removed (the expired-replacement test at its named assertion with the production file reverted; the three stale-state tests under an accept-all mutation). ConnectionManagerTest and SolrZkClientTest pass, and the changelog entries generate. Two items remain before this is ready: the ordering test needs the standard assumeWorkingMockito guard to pass the repository check, and the nightly stress test fails reproducibly at seed 5301C0DE when a session expiration is injected, which needs diagnosis in the expiration path or the test's drain timing.
