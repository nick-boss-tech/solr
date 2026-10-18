# Assignment: SOLR-2681 nested-form highlighting, broader fix on a separate branch, round 1

Status: CLAIMED (claims/pool-solr-2681-nested-forms-round-1.md, vm4, 2026-10-18)

Capability tags: implementation, gate. Intended host: vm4 (Linux), at Nick's direction.

## Context

PR #5104 (SOLR-2681, head a3b1ea7994d) highlights a query only when it sits directly under a function query, `query(...)`. Its Limits say the nested forms, such as the ticket's `{!func}product($v1,$v2)`, are not handled. Nick asked for a separate branch to test whether the broader fix is possible. #5104 is not changed by this work.

## Items

1. Branch `solr-2681-nested-submit` off upstream main (apache/solr), not off #5104's branch.
2. Implement highlighting for queries inside any value source that wraps a query, to the depth the function tree reaches. Keep the change in the highlighter, with no production change outside it.
3. Add a focused HighlighterTest case for the ticket's example. Fail-before on the base, pass with the change.
4. Gate it: tidy, Error Prone compile, focused test with counts from JUnit XML, `:solr:core:check -x test` or the closest module check that runs. Write the receipt.
5. Draft the JIRA ticket and the PR. Do not file or open them.

## Limits

- No push to #5104's branch, and no change to its PR.
- No ticket filing, no PR opening, no comments.
