# LB attempt limits, PR 1: Windows implementation

Status: IMPLEMENTED, PROOF PENDING

- Owner instruction: implement only the first PR from the October 26 mailing-list scope reply; await discussion before PRs 2 and 3.
- Claim: claims/lb-attempt-limits-pr1-windows-round-1.md, posted at caea3488aabd12c76f38232b17cae1a1f64fbb6d.
- Initial reviewed board: e9bc0cdb59a77c4048294f030ddce28a94b8d2c0; pre-publication refresh: caea3488aabd12c76f38232b17cae1a1f64fbb6d.
- Base: apache/solr main 0d6b2e32f598d772aed094038022a6a588fdfc5a.
- Fork branch: solr-lb-attempt-limits-pr1-submit.
- Pushed head: 7a226ef031f8b82527a1209ce760fcf37e611601, separately verified through the fork Git refs API.
- Local checkout: C:/Users/shaninna/dev/Solr-issues/wt/LB-ATTEMPT-LIMITS-PR1, clean after commit. The descriptive branch in the initial claim was renamed before the first push to fit the existing fork-only commit/push wrapper. No remote branch was rewritten or deleted.
- Author and committer: Nick Shanin <nick.boss.us@gmail.com>; no attribution trailers.
- Gate: gates/lb-attempt-limits-pr1-round-1.md. Receipt target: receipts/lb-attempt-limits-pr1.md.

## Problem and implementation

The standalone LBSolrClient.request(SolrRequest, String, Integer) previously limited only the live loop. Zombie fallback could exceed the configured limit. A positive limit with an empty live list could also reach pickServer on an empty array, and a limit larger than the live snapshot could cycle through failed endpoints again.

The explicit-endpoint iterator already bounded dispatches, but incremented its counter for a rejected attempt and reported exhaustion as no live servers. LBAsyncSolrClient then discarded that iterator exception and completed the response with the preceding failure.

The patch shares an execution-local AttemptCounter across the relevant live/zombie paths and exposes LBSolrClient.AttemptLimitExceededException with actual attempts, configured maximum and last failure. The standalone live loop visits at most its snapshot size. The async path preserves the new exhaustion exception; it retains its previous exception behavior for other iterator failures, including unbounded endpoint exhaustion.

The initial attempt and failed connections consume the allowance. Skipping an endpoint does not. Existing non-positive values allow no attempts, and null retains unbounded failover through eligible endpoints. Zombie-specific allowances remain a separate, potentially tighter restriction. Counters are allocated per execution rather than stored on Req. Existing request-type checks, unsent-request classification, deadlines and cancellation logic are unchanged.

This is an LB dispatch limit. The reviewed JDK implementation delegates to HttpClient.send/sendAsync; Jetty delegates to Request.send and explicitly recognizes transport-level GOAWAY requeueing. Internal HTTP retries, redirects and background health checks are not counted by this patch, and the API documentation states the underlying-client retry boundary. This does not claim a bound on wire transmissions, distinct JVMs, Cloud retries, or a distributed request tree.

## Exact outbound files

- solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java
- solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBAsyncSolrClient.java
- solr/solrj/src/test/org/apache/solr/client/solrj/impl/LBSolrClientAttemptLimitTest.java
- solr/solrj/src/test/org/apache/solr/client/solrj/impl/LBAsyncSolrClientAttemptLimitTest.java

Four files, 648 additions and 36 deletions. The new suites declare 24 tests; none has run on Windows. Coverage includes boundary success, live/zombie caps, empty live snapshots, excessive live-loop cycling, independent zombie allowances, zero/negative limits, request reuse and overlapping async executions, unsafe update/admin requests, unsent updates, cancellation and processing-limit failures. Mocks record delegate dispatches and disable health checks; no destructive query or real node crash is needed.

## Review and evidence

git diff --check and git diff --cached --check passed. The exact outbound list, clean worktree, ICLA identity, parent/head and remote head were checked. Upstream main was refreshed and matched the base. Code was formatted manually; no Gradle, compiler, test, tidy, or Windows test queue ran. Linux proof, formatting and required checks remain pending.

The original iterator-limit test and the recent history for SOLR-18086 and SOLR-18401 were reviewed. JIRA searches found related SOLR-18341 (request replay safety) and SOLR-18402 (classification consolidation, open PR 4829), but no matching attempt-limit ticket. Those changes are excluded. No existing implementation claim for this scope appeared on the reviewed board.

Security disposition: ordinary availability/control improvement arising from the public mailing-list design discussion. No demonstrated crashing query or new authentication/authorization boundary was found or introduced. No transport classification changes, new outbound destinations, endpoints, credentials or dependencies are included. SECURITY.md and THREAT_MODEL.md were read. This is not a claim of a newly discovered vulnerability.

No changelog was added: repository AGENTS.md explicitly requires an assigned JIRA or GitHub PR number first. No JIRA key was invented or borrowed from a related ticket. Public PR creation, JIRA writes and mailing-list follow-ups remain for Nick's direction after evidence and discussion. PRs 2 and 3 remain untouched.

## Suggested PR description, pending Linux evidence

Title: SolrJ: consistently enforce LB request attempt limits

The standalone LB request path can continue into zombie fallback after spending numServersToTry, and can revisit failed live endpoints when the configured limit exceeds the live snapshot. Async requests also hide attempt-limit exhaustion behind the preceding transport failure.

Use execution-local counting for live and zombie attempts and expose a distinct exhaustion exception carrying the actual count, maximum and last cause. Preserve existing retry eligibility and unconfigured behavior. Document that the limit covers LB dispatches, including the initial attempt, rather than internal HTTP-client retries.

Validation: pending the exact-head Linux gate. The patch adds 24 deterministic test methods and requests neighboring LB suites, an API-only synthetic-base regression comparison, tidy and check -x test. Replace this paragraph with observed results only after the receipt is green.

AI assistance: OpenAI Codex assisted with research, implementation, tests and drafting. Human review is still pending; no review or responsibility claim is made on Nick's behalf.
