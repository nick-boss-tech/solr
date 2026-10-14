# Assignment: SOLR-3923 draft and premise gate job, census follow-up round 1

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Why this is separate

The eDisMax final pass (`assignments/pool-edismax-final-review-round-1.md`) covers the ten gated drafts and excludes SOLR-3923 by name: the branch is GATE GREEN at `723f61ee4ab7` per `receipts/SOLR-3923.md`, but it has no draft, and the round 3 record (`reports/edismax-round-3.md`) records its premise run as still owed. This assignment closes those two gaps.

## Owner directive: run to SATISFIED

- Write the draft, review it against the receipt and the branch, apply what the review calls for, and keep going until a SATISFIED verdict comes back on the draft.
- The draft follows `pr-formula.md` exactly (AI header, bold one-line summary opening every section, Proof only from the receipt, citations at the head SHA or the named merge-base, plain language, no internal process vocabulary, no "we", no em dashes). Its Proof must not claim a fail-before result: none is on record. It states the premise run as owed until the gate job below reports.
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.

## Work

1. Write `pr-drafts/edismax/SOLR-3923.md` from `receipts/SOLR-3923.md` (eDismax: a complex fielded query with parens is not recognized; branch `solr-3923-submit`, live tip to be confirmed by ls-remote at claim time).
2. File the premise gate job under `gates/` in the pool's gate job format: branch and head, module `:solr:core`, the focused test class or classes the receipt names, a seed in the branch's recorded style (`3923C0FFEE3923` if the receipt names none), the proof base (merge-base with `upstream/main`, to be confirmed by the claiming host), and the proof shape: the branch's new or changed tests run against merge-base production with the branch test files kept, each new test's outcome recorded from fresh JUnit XML (fails on base with the premise failure, or passes on base and is recorded as a regression guard). Expected premise, from the ticket: the parenthesized fielded query form is not recognized on base production.

## Deliverable

`reports/census-followup-edismax-leftover-round-1.md`: the draft verdict, the gate job path, and anything the ladder left as a named follow-up.
