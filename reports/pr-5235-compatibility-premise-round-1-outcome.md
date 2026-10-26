# PR 5235 compatibility premises: outcome report, round 1

Gate: `gates/pr-5235-compatibility-premise-superseding-a7215a5.md`. Receipt: `receipts/PR-5235-compatibility-premise-round-1.md` (the exact heads, commands, exit codes, and 14-method table). This report classifies the premises. It is not a review of the PR and does not claim the PR is fixed or broken.

## Classification

| Premise | Result | Evidence |
| --- | --- | --- |
| F1: embedded unnamed content streams drop documents | CONFIRMED | Method 3 passes on base, fails on PR with `expected 2 but was 1`. Named control passes on both. |
| F2: command two- and three-argument `readCommands` missing | CONFIRMED (reflective) | Methods 1 and 2 pass on base, fail on PR with `NoSuchMethodException`. |
| F3a: Jetty legacy request POST body | CONFIRMED | Methods 5 and 6 pass on base, fail on PR with a 0-byte captured body against 21 expected. |
| F3b: JDK-client legacy request POST body | BLOCKED BY HARNESS | Methods 7 and 8 fail on base with the same empty body, so they cannot show the PR's behavior. |
| F4a: Jetty legacy stream override bodies | CONFIRMED | Methods 9 and 10 pass on base (custom writer bytes), fail on PR (original 21-byte body sent instead). |
| F4b: JDK-client legacy stream override bodies | BLOCKED BY HARNESS | Methods 11 and 12 fail on base with an empty body, so base does not show the expected custom bytes. |

## What this means for the review

- Three of the four findings reproduce with the predicted behavior: F1, F2, and the Jetty half of F3 and F4. Each has a passing base, a failing PR, and passing controls.
- The JDK-client halves of F3 and F4 are not reproduced. The base JDK tests send an empty body, which is a result this gate cannot explain from the premise. Settling them needs a follow-up on how `HttpJdkSolrClient` sends these requests on base, not a change to this PR's claims.
- No production change was made, and nothing in this gate is a fix verdict.

## Proposed maintainer update (for Nick to review; not posted)

Draft only, not for posting until Nick approves: a short comment that F1 (embedded unnamed streams), F2 (`readCommands` signatures), and the Jetty legacy and stream-override paths reproduce on the PR head with the predicted behavior, with passing base and control tests. The JDK-client cases still need investigation before they can be stated.

## Next

- Nothing here closes the review finding for the JDK-client cases; a follow-up gate on the base JDK behavior would be the next step, if Nick wants it.
- The PR itself is not changed by this gate.
