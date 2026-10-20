# Skip record: SOLR-11827

- Ticket: SOLR-11827, "MockAuthorizationPlugin should return 401 if no principal is specified". JIRA status read 2026-10-20T07:18:11Z (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: pr-needed. This replaces the recorded keep-open: the test mock's behavior is shown by the code path, and a small test-only change is the fix the ticket asks for.
- Draft branch: solr-11827-draft-pr (the earlier keep-open branch solr-11827-draft-open stays as the prior record).
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:18:11Z (UTC).

## Ticket reading

The ticket says the test mock `MockAuthorizationPlugin` treats a request with no principal as allowed, so `PKIAuthenticationIntegrationTest` cannot catch a request that loses its principal after a PKI timestamp expires. It asks the mock to return 401 when there is no principal, and a test to check that. No comments.

## Code reading (pinned head)

- `solr/core/src/test/org/apache/solr/security/MockAuthorizationPlugin.java:35-54`, `authorize`:
  - With no predicate set and an unprotected resource (lines 42-44), the method returns 200 whether or not there is a principal. This is the case the ticket describes.
  - With a protected resource (lines 46-50), a missing principal returns 403, not 401. A user parameter `uname` can stand in for the principal.
- `solr/core/src/test/org/apache/solr/security/PKIAuthenticationIntegrationTest.java:48, 72, 85, 115`: the test sets `MockAuthorizationPlugin.predicate`, so the no-principal branch that the ticket describes is not reached on the predicate path.
- The needed change is test-only: the mock should return 401 when no principal is present, and `PKIAuthenticationIntegrationTest` should add a check that a request without a principal is rejected.

## History

- evidence.py at the pinned head: no commits naming SOLR-11827. The test-class history shows only test-infrastructure commits (1af33ac699, 44eb5c5a70), neither naming the ticket.

## Reproduction attempts

Not attempted as a run. The code path above is the argument: the mock returns 200 for a missing principal on unprotected resources, which is the behavior the ticket says lets the PKI test pass. The check the PR would add is a PKIAuthenticationIntegrationTest case with a request that has no principal, expected to be rejected; the draft should carry that test and a run showing it fails on the current mock.

## Gap analysis

The gap is the mock, not the product code. The product code path (RuleBasedAuthorizationPlugin returning USER_REQUIRED for a missing principal) is the one the ticket cites, and it is not changed here.

## Verdict justification

pr-needed meets the rubric: the code-path argument with the lines that cause the gap (MockAuthorizationPlugin.java:42-44 and 46-50), and the test the PR would add (a no-principal case in PKIAuthenticationIntegrationTest). The PR itself is not opened here; opening it needs owner approval.

## Limits

This record does not run the new test, so the failure on the current mock is not yet shown. It does not change any product behavior.
