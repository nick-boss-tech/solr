# Skip record: SOLR-17385

- Ticket: SOLR-17385, "coordinators don't support query requests that use any RH except /select". JIRA status Open, resolution none, created 2024-07-29, last updated 2025-01-28, checked 2026-10-19 UTC. Links: SOLR-17334 (Open), SOLR-17388 (Open).
- Verdict: pr-needed.
- Draft branch: solr-17385-draft-pr.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

The reporter says the coordinator federates a request only when its path ends in `/select`, so a custom request handler under any other name is never federated. The reporter links the mailing-list discussion. Comments: ellaeln (2024-07-29) proposes an RH list in solrconfig.xml that says which handlers federate, and (2024-09-16) links a PR for it; cpoerschke links SOLR-17334 as related. No maintainer decision is recorded in the comments.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/servlet/CoordinatorHttpSolrCall.java:69-76`: `getCoreByCollection` returns null for any path that does not end in `/select`, before the synthetic-core lookup in `getCore(...)` is reached: `if (!path.endsWith("/select")) return null;` at line 74.
- `CoordinatorHttpSolrCall.java:205-217`: the coordinator factory sends `/____v2/` paths to `CoordinatorV2HttpSolrCall` and every other path to `CoordinatorHttpSolrCall`. The suffix check is the only gate on which handlers are federated.
- `git log -L74,74:solr/core/src/java/org/apache/solr/servlet/CoordinatorHttpSolrCall.java`: the line is from `d029eb14aa` (2022-09-13, SOLR-15715, "Dedicated query coordinator nodes in the solr cluster (#996)"). It has not changed since.
- `git grep` for a configurable list of federated handlers (`federat` near `RH` or `handler`) in `solr/core/src/java`: no match. The RH-list change in the comments is not on main.

## History

- `d029eb14aa` (2022-09-13), SOLR-15715: introduced the coordinator and the `/select` suffix check at line 74. Ancestor of the pinned head: yes.
- The ellaeln PR mentioned in the comments: not on the fork at the pinned head (no commit names SOLR-17385, and the RH-list code is absent).
- SOLR-17334 and SOLR-17388 (linked): not traced on the fork.

## Reproduction attempts

Not attempted. The code-path argument at line 74 is direct: a request whose path does not end in `/select` returns null from `getCoreByCollection`, so it is not federated. A reproduction would need a custom request handler under a non-`/select` name in a coordinator cluster; that setup belongs to the PR's test, not to this closeout.

## Gap analysis

The behavior the ticket reports follows from line 74. The change the ticket asks for (let the operator name the handlers to federate, or federate by another rule) is a design choice. The comments propose a list in solrconfig; no maintainer has accepted that design in the recorded comments. The uncertainty is the design, not the existence of the limitation.

## Verdict justification

pr-needed: "The problem reproduces on the pinned head, or the code path shows it, and a change is needed." The code path shows it at `CoordinatorHttpSolrCall.java:74`, and a change is needed. Minimum evidence: the code path with the line that causes the behavior (above), and the test the PR would add. The test: a coordinator test in `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java` (the existing coordinator role test) that sends a query to a custom request handler whose path does not end in `/select` and checks that it is federated once the change is in.

Scope note: PR #5106 (the test-sleep work on this host's records) changes `TestCoordinatorRole.java`. The PR for this ticket would conflict with it in that file; the PR should be opened after #5106 merges or rebased onto it. This closeout does not open any PR.

## Limits

This record does not run a coordinator cluster. It does not check the mailing-list thread. It does not decide the design (solrconfig list or another rule). It does not check SOLR-17334 or SOLR-17388 in detail.
