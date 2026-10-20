# Skip record: SOLR-9648

- Ticket: SOLR-9648, "Wrap all solr merge policies with SolrMergePolicy". JIRA status read 2026-10-20T12:35:36Z (UTC): Open, resolution None, updated 2019-06-08. Linked: LUCENE-7523 (Open).
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9648-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:35:36Z (UTC).

## Ticket reading

Wrap every configured merge policy in one Solr-level policy, so that a single entry point can upgrade segments on startup and handle schema-driven index changes. Comments from 2016 show an initial POC, a discussion of configuration semantics, and LUCENE-7523 for a Lucene-side change. No maintainer decision.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/index/UpgradeIndexMergePolicyFactory.java:9-18`: a `WrapperMergePolicyFactory` that wraps the configured merge policy in Lucene's `UpgradeIndexMergePolicy`. So segment upgrade on merge exists as an opt-in configuration.
- The ticket's "wrap all" design (a default wrapper around every configured policy) is not present: the wrapper is configured per policy, not applied to every policy by default. The grep `git grep -l "SolrMergePolicy\|UpgradeIndexMergePolicy" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java` returns only this factory.

## History

- evidence.py at the pinned head: no commits naming SOLR-9648.
- The path-scoped history of the factory was not read in this pass (the full path log timed out).

## Reproduction attempts

Not run. The question is a design choice about default behavior, which a run does not settle.

## Gap analysis

The capability the ticket describes exists as opt-in. Whether every merge policy should be wrapped by default, and with what startup-upgrade behavior, is a maintainer decision on configuration semantics, not a reproducible defect.

## Verdict justification

keep-open meets the rubric: the attempts made are the code-path reading and the grep; the missing piece is the maintainer's decision on whether the wrap is default behavior (and whether LUCENE-7523 changes the design). close-answered is not supported: the answer is a design choice, not a docs question. close-fixed is not supported: the default-wrap design is not present.

## Limits

This record does not decide the design. It does not check LUCENE-7523's status beyond the ticket's note.
