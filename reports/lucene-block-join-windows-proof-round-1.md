# Lucene block join missing values: Windows proof outcome

Status: REQUIRED WINDOWS PROOF PASSED at the exact head below, completed 2026-11-05 04:57:45 UTC.

Nick authorized necessary Lucene Gradle execution on this Windows host, superseding the original Linux route. No Solr Gradle or retired queue was used.

- Source: `nick-boss-tech/lucene`, branch `fix-block-join-missing-values`, exact pushed head `55c3174bd154b5cc987d9b24e1c77043e23fe71e`, base `aa366ae209ae8296dd70d2835c6d47f8fc201972`.
- [Pinned source diff](https://github.com/nick-boss-tech/lucene/compare/aa366ae209ae8296dd70d2835c6d47f8fc201972...55c3174bd154b5cc987d9b24e1c77043e23fe71e).
- Gate: `gates/lucene-block-join-missing-values-windows-round-2.md`.
- Receipt: `receipts/lucene-block-join-missing-values-windows-round-1.md`.
- Focused suites: 10 passed; full join module: 104 passed, 3 existing nightly skips, no failures/errors. Exact-head join tidy passed cleanly. Four unchanged regression methods each fail at runtime against original production and pass with the repair.
- Root `check -x test`: PASS, exit 0, 530 tasks (513 executed, 17 up-to-date). Full repository runtime tests, nightly tests, performance benchmarks, maintenance backport and Solr 10.6 integration are not claimed. Default-disabled Error Prone and unconfigured eclint are not passed checks.

The adjacent review added paging over missing/real string keys and actual multi-valued numeric children with explicit and null missing policies. No additional production defect was established. Existing null-string constructor wording remains a separate documentation ambiguity; this repair preserves tested behavior.

The disposable original-production mutation was restored after negative proof. The candidate and fail-before worktrees are clean. No upstream PR, issue or comment was published. This is internal evidence for human review and ownership, not a Lucene PR body.
