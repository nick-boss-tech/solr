# Assignment: Critical tickets implementation triage (round 1)

Status: OPEN
Tags: premise-run, records

Owner directive (2026-10-24): alongside comment coverage, the open Critical set (`material/critical-tickets-census-2026-10-24.md`, 86 tickets) needs its implementation candidates found. This assignment ranks them; it writes no production code.

## Work

1. Scan the full census. For each Bug or Improvement whose ticket states a concrete defect or a bounded change, assess tractability from the ticket text: is the failure mode specific, is a reproduction or a code trace feasible on current main, is the likely fix surface small, is the area already covered by an open PR (check GitHub) or an owner branch (check this board)?
2. Rank the 10 strongest implementation candidates. For each: the ticket, the defect in one line, the premise-run plan (the exact reproduction or trace, the class or component, what result would confirm or kill the premise), the expected fix surface, and any collision (open PR, owner branch, active maintainer thread).
3. Explicitly list the near-misses: tickets that look tractable but fail one test (too broad, premise already refuted somewhere, security-route, design-first), each with its failing test named.

## Deliverables

- `reports/critical-implementation-triage-round-1.md` with the ranked 10 and the near-miss list.

The ranked candidates become premise-run assignments in later stocking; this assignment's report is their source. Security-sensitive candidates are named with a `security-route` flag and no exploit detail.
