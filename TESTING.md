# TESTING.md: decisions for the PR preparation batches

Decisions taken from the reviews, batch by batch. A decision
marked ADOPTED follows the recorded recommendation, under Nick's
standing agreement. A decision marked DISCUSS is a tough call: the
recommendation is recorded, the call is not taken, and the item
waits for a discussion with Nick. Audits and PR drafts on this
branch follow this file.

## Update processing and atomic updates (2026-10-08)

### ADOPTED

- **SOLR-12245, framing.** The PR is framed as an error-message
  improvement: the distributed-update error message now names the
  target replica. The description states plainly that the
  ticket's MDC ask is not addressed by this change and offers it
  as a follow-up. The review's optional null-guard test is not
  added; the gated tree stays untouched, and the missing
  null-guard coverage is named in the draft's Limits instead.
- **SOLR-16655, upgrade note.** The branch's design position
  stands. The upgrade note the review is owed goes into the PR
  description draft when it is written. No code change, no
  re-gate.
- **SOLR-18505, comment-only head.** No re-gate for the
  comment-only commit past the gated head on the live PR.
  Upstream CI runs on the live head and covers it.

### DISCUSS (recommendation recorded, call not taken)

- **SOLR-6045, the factory defect. DECIDED by Nick,
  2026-10-08: option A, fix it in this branch before the PR,
  with a re-gate.** The branch's premise work
  found a real pre-existing defect on the opt-in atomic path:
  the factory returns the list of maps, the instanceof Map skip
  misses, and the maps are stored as values. Recommendation:
  fix it in this branch before the PR; it is the same code path
  the PR touches. Consequence: a production change on a gated
  branch, plus a re-gate. The alternative is to ship as-is with
  the defect named in Limits and a follow-up offer. Tough
  because it trades scope discipline against shipping a known
  defect.
- **SOLR-12864, the passing pin. DECIDED by Nick, 2026-10-08:
  option B, keep it as a test-only coverage PR guarding the
  SOLR-16811 fix for the echo plus mapUniqueKeyOnly combination,
  framed as coverage and never as a bug fix.** The review's owner call:
  accept a test that pins working behavior (its fail-before
  stage reports NOT_PROVEN), or drop the branch. The ticket's
  symptom does not reproduce on the current base. Recommendation:
  drop the standalone pin. The alternative is to accept it as
  coverage. This is the same open question as SOLR-10641.
- **SOLR-13696, fix or retire. DECIDED by Nick, 2026-10-08:
  option B, fund the fix as its own project, handed off in
  `assignments/solr-13696-fix-project.md` on this branch.** The branch's re-enabled tests
  fail on current main for schema and config drift beyond the
  commitWithin race the ticket names, so its premise does not
  hold as shipped. Recommendation: retire the branch as it
  stands; funding the routed-alias test-drift fix is a separate
  project. Retire calls are Nick's. The branch stays untouched
  while the hold stands, whichever way the call goes.

## Added by the last-review audits (2026-10-08)

The ten audits are in `audits/update-processing/` on this
branch. Four branches are certified ready for the final round:
SOLR-4841, SOLR-5505, SOLR-5887 and SOLR-6973. The other six are
not ready. Their code fixes are main-side dev work; the items
below are the decisions those fixes wait on.

### ADOPTED (recommendations, under Nick's standing agreement)

- **SOLR-5939, items 3 and 4.** The client-visible error text
  change and the marking of every request in a non-retriable
  failed stream as failed are stated in the PR draft's Limits,
  not redesigned. The per-request attribution design goes to
  the PR's Choices section for maintainer judgment; no separate
  Jira design round first.
- **SOLR-5941, design choices.** The branch's choices (which
  chain, the buffering skip, the extra version stamp, the
  replica update lock) stand as implemented. Items 4 and 5 are
  stated in the draft's main text, not removed.
- **SOLR-5754, hardening framing.** The change is kept and
  claimed as hardening: the changelog title and the TESTING note
  are reworded so they do not claim a fixed bug, and the
  fail-before result is stated as inconclusive by construction.

### What the dev fixes are (main side, before the queue)

- **SOLR-5939:** remove the registry leak (successful requests
  stay reachable until the update request ends); count each
  shared remote error once in the tolerant path; clear the test
  system property in `@AfterClass`. Then gate the combined
  SOLR-5754 + SOLR-5939 tree with the tolerant-update cloud
  tests in the run list.
- **SOLR-5941:** fix the `getBool(COMMIT_END_POINT, ...)`
  defect in `DistributedZkUpdateProcessor.processCommit` and
  `RoutedAliasUpdateProcessor.wrap()` (the string values are
  not accepted, so the distributed path misreads the flag);
  add a SolrCloud commit test; run
  `ParallelCommitExecutionTest` and `HttpPartitionOnCommitTest`;
  correct the round 30 report's claims and rewrite the draft
  to the formula.
- **SOLR-5754:** the changelog and TESTING-note rewording
  above, and correct the skip-list record, which says
  "synchronizedList gone" about main. That is wrong for the
  error list.
- **SOLR-11475:** correct the wrong test comment, fix the
  queue record's file pointer, then resume and complete the
  gate at the current head, with the fail-before behavior
  confirmed by a run and reported as it lands.
- **SOLR-12864 and SOLR-13696:** decided by Nick on
  2026-10-08 (see the DISCUSS section). SOLR-12864 proceeds as
  a test-only coverage PR: package it and gate it at the
  packaged head, with the pin's proof stated as inconclusive by
  construction. SOLR-13696 proceeds through the fix-project
  handoff named above; the branch stays untouched until that
  project claims it.
