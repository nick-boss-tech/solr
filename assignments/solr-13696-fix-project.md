# Assignment: SOLR-13696 fix project (repair the re-enabled routed-alias tests)

The owner directed this project on 2026-10-08. It supersedes
the hold on this branch: the PARKED record said not to reopen
SOLR-13696 without direction, and this assignment is that
direction. The decision and its background are recorded in
`TESTING.md` on this branch.

## Where things stand

- Branch: `solr-13696-submit` on the fork, banked untouched at
  `05ab4664dace`. Base (merge-base with upstream main):
  `c3cdf7b46e8`.
- The branch is test-only. It removes `@AwaitsFix` from the
  abstract base `RoutedAliasUpdateProcessorTest`, which
  re-enables the Category and Dimensional subclasses (they carry
  no annotation of their own; the Time subclass keeps its own
  annotation for SOLR-13059), and it replaces the random
  `commitWithin` path with explicit commits.
- A gate run on the main side on 2026-10-05 was NOT shipped:
  the re-enabled Category tests failed 7 times on current main
  for schema and config drift, including "unknown field
  'ship_name_en'". The drift is in the test resources, beyond
  the commitWithin race the ticket names. The branch touches
  no schema or config file, so it cannot address the drift as
  shipped.
- The last-review audit is on this branch at
  `audits/update-processing/SOLR-13696.md`. Read it first. Its
  findings 1 to 3 are part of this project's scope.

## The work

1. Claim first: add `claims/solr-13696-fix-project.md` on this
   branch with your name and the date. A rejected push means
   the project is taken.
2. Characterize the drift. Work out which schema and config
   files the Category and Dimensional tests run against, what
   those files provide on current main, and exactly which
   references in the tests no longer resolve (the 'ship_name_en'
   failure is one; find the rest from the 2026-10-05 failure
   shape and the test code). Write the characterization into
   the report (deliverable below) before changing anything.
3. Repair the drift with the smallest test-side changes that
   make the re-enabled tests address current main honestly.
   Test resources and test code are in scope. Production code
   is not: if the repair surfaces what looks like a real
   product bug, stop, record it in the report, and do not fix
   it under this assignment.
4. Answer the premise question: does the commitWithin race the
   ticket names still reproduce on current main, or did later
   work make it moot? Give the evidence you have; a claim
   without a run or a code trace is a hypothesis and must be
   labeled one.
5. Address the audit's findings explicitly:
   - Finding 2 (coverage): removing the random commitWithin
     path removes routed-alias commitWithin coverage entirely.
     Either keep an honest commitWithin exercise somewhere in
     the re-enabled tests, or state in the report why dropping
     it is the right call, as a decision for the owner, not a
     side effect.
   - Finding 3 (possible NPE): the branch's explicit-commit
     loop iterates `getAliasesAsLists().get(getAlias())`. Settle
     by reading whether a missing alias can make that null, and
     fix or clear it.
6. Push the repaired branch to the fork as
   `solr-13696-submit` (fast-forward from `05ab4664dace`;
   author Nick Shanin, no trailers, no em dashes in commit
   messages). The gate itself is arranged from the main side
   once the branch lands; do not run Gradle builds.

## Deliverable

A report at `reports/solr-13696-fix-project.md` on this
branch: the drift characterization, the repairs made, the
premise answer with its evidence class, the finding 2 and 3
dispositions, the new head, and a closing verdict, one of:
ready for the main side to gate, still blocked (with the
blocker named), or retire (with the reason). Nothing is posted
to Jira or GitHub under this assignment.
