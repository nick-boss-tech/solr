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

Items added by the Groups C and D reconciliation
(`reports/update-processing-cd-reconcile.md` on this branch).
None of these is decided; each records the audit's evidence
and the recommendation.

- **SOLR-5065, locale handling and suggester inference.**
  Two open items from the audit. First, `normalizeExponent`
  rewrites the exponent before `NumberFormat` runs, whatever
  an explicit `locale` says; the audit leaves the regex
  option versus a locale-aware parse as a maintainer choice,
  and the ticket has no consensus. Second, the same helper
  makes `DefaultSchemaSuggester` infer Double for `E+` and
  lowercase samples, and no test covers that inference
  change. Recommendation: ship the branch as it stands;
  state the locale behavior in the draft's Limits, present
  the suggester inference in the draft as an intended
  consequence, and add a suggester test only if maintainers
  ask. Tough because the inference change reaches a second
  component that the ticket never mentions.
- **SOLR-6065, the ticket's cloud test.** The ticket asks
  for cloud based tests that set a lower limit and verify
  clean error messages for a single shard. The branch has
  one single-core test and no cloud test, and the audit
  makes that its blocking item. Recommendation: add the
  cloud test before the PR, since the ticket names it as
  the acceptance shape and a maintainer can be expected to
  ask for it. The alternative is to ship the single-core
  coverage and record that scope as the decision here.
  Tough because a cloud test is real work for an error-path
  branch whose code the reviews otherwise accept.
- **SOLR-12245, target detail in the client response.** The
  message this branch improves now puts the target's URL,
  collection and shard into the error the client receives.
  The ADOPTED framing accepts the message change; it does
  not decide whether that detail belongs in the client
  response or only in logs. The round 36 review leaves it
  as an owner call. Recommendation: keep the detail in the
  client response, since naming the target replica for the
  user who receives the error is the point of the adopted
  framing, and state the choice in the draft. Tough because
  node URLs in client-visible errors are the kind of detail
  maintainers sometimes ask to keep server-side.
- **SOLR-12705, counting processors on the atomic path.**
  The fix sits in the shared `FieldMutatingUpdateProcessor`
  base class, so a counting processor shares the new path:
  a single-map `add` on a counted field becomes
  `{add: <count of the operand>}` and `remove` becomes
  `{remove: <count>}`, where base wrote a plain count.
  Neither case is in the changelog or the tests. The audit
  leaves it as exclude in code or state the behavior.
  Recommendation: state the behavior in the draft (main
  text and Limits) and ship; do not exclude counting
  processors in code unless maintainers ask. Tough because
  it is a behavior change on a component the ticket does
  not name, discovered by reading rather than by a failing
  test.
- **SOLR-16356, the second close-race ERROR.** The branch
  silences the update-log close race. The ticket transcript
  also shows a second stack trace: the periodic task in
  `DocExpirationUpdateProcessorFactory` commits after its
  delete-by-query, the commit fails with `SolrCoreState
  already closed`, and it is logged at ERROR. The branch
  does not touch that factory, and the changelog title
  already names only the update-log path. Recommendation:
  split the expiration-task trace as a follow-up; state it
  in the draft's Limits so the PR does not read as covering
  every close-time stack trace. The alternative is to
  suppress the second ERROR in this PR. Tough because the
  ticket's own transcript contains both traces.
- **SOLR-16910, scope of the logging fix.** The branch
  fixes the slow-WARN format in `LogUpdateProcessorFactory`
  so the WARN carries the request details. The ticket also
  calls out `SolrCore.Request` logging behavior, and the
  reporter says the desired behavior there is undecided;
  the branch does not touch it. Recommendation: ship the
  narrow fix and name the `SolrCore.Request` item in the
  draft's Limits as not addressed. The alternative is to
  hold the PR until that behavior is decided on the
  ticket. Tough because the undecided half belongs to the
  same ticket.
- **SOLR-7504, chain placement and the "fixed" title.** A
  separate item from the branch's code fix (the plain-first
  silent drop, in the reconciliation report). The counter
  runs only when the source field is in the update, so an
  update that leaves the source field alone is not
  covered: a trailing `DefaultValue` writes a plain 0 into
  the partial document, which an atomic update applies as
  a `set`, and the ticket's symptom persists for that
  shape. The changelog title says "fixed".
  Recommendation: document the chain-placement limit in
  the draft's Limits and narrow the changelog title so it
  claims only the covered shape; do not change the chain
  in this branch. The alternative is a chain change, which
  is a larger behavioral step. Tough because the
  uncovered shape is arguably the ticket's own case.
- **SOLR-14718, the second flaw in the ticket packet.**
  A separate item from the branch's test fix (the
  timing-dependent regression test, in the reconciliation
  report). The ticket packet names a second flaw, the
  per-node streaming-client association that can report
  the first document of a batch. The branch fixes only the
  command-reuse case, and its changelog is correctly
  limited to that. Recommendation: record the second flaw
  as a follow-up and state it in the draft's Limits; do
  not include it in this branch. The alternative is to
  include it, which widens a small, gated fix into a
  second change. Tough because both flaws sit in the
  same ticket.

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

## Groups C and D reconciliation (2026-10-08)

The 19 Group C and Group D audits are reconciled against
this file in `reports/update-processing-cd-reconcile.md`.
Classes: (a) ready for the final round as-is: SOLR-3657,
SOLR-12703, SOLR-13265. (b) ready with draft-level notes
only: SOLR-7022, SOLR-11483, SOLR-14262, SOLR-16655,
SOLR-16673, SOLR-18505. (c) needs a code fix or a settling
run before final review: SOLR-6045 (the child-document
guard), SOLR-7504 (the plain-first detection), SOLR-13943
(a run to settle the class-level AwaitsFix question),
SOLR-14718 (a deterministic regression test). (d) needs a
decision from Nick: SOLR-5065, SOLR-6065, SOLR-12245,
SOLR-12705, SOLR-16356, SOLR-16910, plus one item each
inside SOLR-7504 and SOLR-14718. The (d) items are in the
DISCUSS section above. SOLR-6045's audit predates the
option A fix: its first blocking item is done at
`e06aa7624853`, and only the guard item keeps it out of
the final round.
