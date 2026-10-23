# Assignment: PR #5171 (SOLR-17752) corrupted-response warning check (round 1)

Status: CLAIMED (claims/pool-phone-agent-2-pr5171-diagnosability-round-1.md)

Posted by the phone agent at the owner's direction, 2026-10-22. Source: Eric Pugh's "Nick-Boss-Tech Breach Report" (Claude.ai artifact, October 2026, Refresh 2), quoted on the dev list. Claim before starting by adding `claims/pool-<host>-{name}.md` per WORKFLOW.md. Verify every head below by ls-remote at claim time; the artifact is a snapshot and heads may have moved. If a tip has moved, work at the live tip and record it.

## The critique

Class IV card: a corrupted shard response used to be logged with the full shard request and response at WARN, and a query can carry customer data. The fix logs the responding node name, the request's purpose, and the response's top-level key names, no values. The artifact's one open question: "worth confirming the new message still gives on-call enough to diagnose the actual corruption."

## Branch

- PR #5171, branch `solr-17752-submit` at `041c5b0b2e` when checked 2026-10-22. Ticket: SOLR-17752.

## Work

1. Read the diff. List every field the new warning emits and verify by code reading that no request or response value can reach the log through it, including nested or exceptional paths (for example a key name that is itself user data, or an error branch that falls back to the old dump).
2. Judge diagnosability concretely: from the new message alone, what can an operator determine (which node, which request purpose, what shape the corruption took), and what is genuinely lost versus the old dump? Name anything lost that has no other source in the logs.
3. Check the shipped test: does it assert the absence of values, or only the presence of the new fields? Strengthen it in the patch loop if it asserts only presence.
4. Verdict: OPEN-READY or FIX FIRST, with the gate for any branch change per the standard gate.

## Deliverable

`reports/pool-pr5171-diagnosability-round-1.md`: the emitted-field list with citations, the leak check, the diagnosability accounting, any test change and its results, and the verdict.

## Limits

Pushes to the branch are authorized (it is the owner's own open PR). No PR state change, no comments, no Jira edits. If the work changes the PR's claims, update the PR description to match and record the edit in the report; description edits on the owner's own PRs carry standing authorization. No em dashes. Commit as Nick Shanin with no trailers.
