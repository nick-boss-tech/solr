# Assignment: PR #5139 (SOLR-7550) assertion-free PeerSync test (round 1)

Status: DONE (2026-10-23T04:47Z, vm3, head 54d7e21f28fc7ffbbb224da068442d3105da7fc8; report `reports/pool-pr5139-test-strengthen-round-1.md`)

Posted by the phone agent at the owner's direction, 2026-10-22. Source: Eric Pugh's "Nick-Boss-Tech Breach Report" (Claude.ai artifact, October 2026, Refresh 2), quoted on the dev list. Claim before starting by adding `claims/pool-<host>-{name}.md` per WORKFLOW.md. Verify every head below by ls-remote at claim time; the artifact is a snapshot and heads may have moved. If a tip has moved, work at the live tip and record it.

## The critique

The artifact's only Class V card: PeerSync decides a replica has caught up from the versions response; this is cluster-recovery-critical code, and its shipped test "checks the call doesn't throw, not what it returns." A test that cannot fail on the decision it guards is not evidence.

## Branch

- PR #5139, branch `solr-7550-submit` at `fcc8a874ab` when checked 2026-10-22. Ticket: SOLR-7550.

## Work

1. Read the ticket, the diff, and the shipped test. Establish from the code what the intended decision is for each combination: response 500, response 503, and unreachable, each with `cantReachIsSuccess` set and unset. Note that the artifact's summary sentence and the PR title phrase the 500 direction differently; settle which is right from the ticket and the code, and say so in the report.
2. Reproduce the base behavior by hand: on the base production code, run the scenario the PR fixes and record the decision PeerSync actually makes.
3. Strengthen the test so it asserts the decision (the returned success/failure and any recorded state), not merely the absence of an exception. Show the strengthened test failing on the base production code and passing at the head.
4. Gate the branch at its final head with the focused class or classes the change touches, per the standard gate (changelog, tidy, Error Prone compile, focused run counted from fresh XML, module check for the touched module).
5. If the strengthened test exposes a real defect in the fix, that is a finding: fix it in the patch loop if it is small and mechanical, otherwise report it as the verdict.

## Deliverable

`reports/pool-pr5139-test-strengthen-round-1.md`: the decision table from step 1 with citations, the base reproduction, the strengthened test and its both-directions results, the gate results, and the final head.

## Limits

Pushes to the branch are authorized (it is the owner's own open PR). No PR state change, no comments, no Jira edits. If the work changes the PR's claims, update the PR description to match and record the edit in the report; description edits on the owner's own PRs carry standing authorization. No em dashes. Commit as Nick Shanin with no trailers.
