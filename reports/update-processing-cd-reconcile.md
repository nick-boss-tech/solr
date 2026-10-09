# Reconciliation: Update processing Groups C and D audits against TESTING.md

Reconciled 2026-10-08. Sources: the 19 audit files in
`audits/update-processing/` on this branch, the decisions in
`TESTING.md` on this branch, and the live fork tips from
`git ls-remote fork` taken during this pass. No submit branch
was changed and nothing was built or run in this lane.

Head check: 18 of the 19 submit tips match the head their
audit states, exactly. The one exception is SOLR-6045, whose
tip moved after its audit because the fix its audit was
waiting for landed (see its section).

## Summary

| Branch | Audit verdict | Class | Basis in one line |
|---|---|---|---|
| SOLR-3657 | Ready for final review | (a) | No blockers; remaining items are optional cleanups |
| SOLR-5065 | Ready for final review | (d) | Locale handling and suggester inference are open owner calls |
| SOLR-6045 | Not ready | (c) | Factory fix landed after the audit; the child-document guard is still owed |
| SOLR-6065 | Not ready | (d) | The ticket asks for a cloud test; add it or record the single-core scope |
| SOLR-7022 | Ready for final review | (b) | Changelog cause wording and a return-normal statement for the draft |
| SOLR-7504 | Not ready | (c) | A plain-first value is silently dropped; also one owner call (DISCUSS) |
| SOLR-11483 | Ready for final review | (b) | Retention growth goes in the draft's Limits |
| SOLR-12703 | Ready for final review | (a) | One small error-message tweak is final-round work, not a blocker |
| SOLR-12705 | Ready for final review | (d) | Counting processors on the new atomic path need an owner call |
| SOLR-14262 | Ready for final review | (b) | The header contract is posed to maintainers; replica claims stay out of the draft |
| SOLR-12245 | Ready for final review | (d) | One new call: target detail in the client response or in logs only |
| SOLR-13265 | Ready for final review | (a) | Only gate-evidence mechanics remain, at the final gate |
| SOLR-13943 | Not ready | (c) | Settle by run whether the method still sits under a class-level AwaitsFix |
| SOLR-14718 | Not ready | (c) | The regression test is timing-dependent; also one scope call (DISCUSS) |
| SOLR-16356 | Not ready | (d) | The second close-race ERROR: suppress here or split as a follow-up |
| SOLR-16655 | Ready for final review | (b) | Settled by the ADOPTED decision; upgrade note and overlap go in the draft |
| SOLR-16673 | Ready for final review | (b) | Two LOW items go in the draft's Limits; Proof awaits a gate at this head |
| SOLR-16910 | Not ready | (d) | Narrow WARN-format scope or the SolrCore.Request item: an owner call |
| SOLR-18505 | Ready for final review | (b) | Settled by the ADOPTED decision; one optional sentence in the live description |

Class counts: (a) 3, (b) 6, (c) 4, (d) 6.

Eight new decision items were appended to the DISCUSS
section of `TESTING.md` in the same commit as this report:
SOLR-5065 (locale and suggester), SOLR-6065 (cloud test),
SOLR-12245 (client-response detail), SOLR-12705 (counting
processors), SOLR-16356 (second ERROR), SOLR-16910 (scope),
SOLR-7504 (chain placement and the "fixed" title), and
SOLR-14718 (second flaw scope). None of them is decided
here.

A note on gate evidence: the audits were read-only and
several could not see main-side gate receipts from the
review workspace. Where that happens, the branch section
says so. It is not treated as a finding against the branch;
the draft's Proof follows the receipt that exists when the
draft is written, per the formula.

## Group C

### SOLR-3657

- Audit verdict: **Ready for final review.** Last review:
  round 28 (Nearly), plus the 2026-10-08 code-review record
  at the live head (Nearly).
- Head check: audit `14edaca577c`; live tip
  `14edaca577c0`. Match.
- Findings: F1 (test asserted only the prefix) and F3
  (cause-chain depth) are addressed in the head commit. F4
  (the round 28 claim about the second catch block) is
  refuted by the audit's own code reading; no action. F7
  (the new test fails on base) verified by reading. Open:
  F2, the vector message names the destination twice
  (cosmetic); F5, SolrException metadata is not carried to
  the new exception (consistent with base); F6, a test
  comment could name why the first destination is numeric.
- Against TESTING.md: no decision recorded for this branch;
  nothing in the audit conflicts with one.
- **Class (a), ready for the final round as-is.** F2 and F6
  are optional cleanups the final round may take or leave.
  The draft's Changes section states that the copyField
  error text changes, as the changelog already does.

### SOLR-5065

- Audit verdict: **Ready for final review**, with two owner
  calls to settle before the PR. Last review: round 28
  (Close), plus the 2026-10-08 code-review record at the
  live head (Close).
- Head check: audit `c0a0ce1b8d9`; live tip
  `c0a0ce1b8d9b`. Match.
- Findings: F1 (lowercase marker with a minus) and F6
  (changelog accuracy) are addressed. F7 is a hypothesis
  about JDK behavior, not run; it needs no action. Open:
  F2, `normalizeExponent` runs before `NumberFormat`
  whatever an explicit `locale` says; F3,
  `DefaultSchemaSuggester` now infers Double for `E+` and
  lowercase samples, with no test covering the inference
  change; F4, the accepted exponent syntax is not
  documented in the ref guide; F5, remaining test gaps (a
  non-ROOT locale, surrounding whitespace).
- Against TESTING.md: no decision recorded. F2 and F3 are
  genuinely new decision items; both are appended to
  DISCUSS with a recommendation. F4 is draft work: the
  syntax note belongs in the PR text or a ref-guide line in
  the final round.
- **Class (d), needs a decision from Nick** (DISCUSS:
  SOLR-5065, locale handling and suggester inference).

### SOLR-6045

- Audit verdict: **Not ready**, on two blocking items.
  Last review: round 28 (Needs work) and the 2026-10-08
  code-review record (Nearly); the audit checked the
  mechanism where the two records disagreed.
- Head check: audit `dcdef50d700`; live tip
  `e06aa7624853`. **Moved after the audit.** The one commit
  since (`e06aa762485`, "skip repeated operation maps in
  AtomicUpdateProcessorFactory") touches only
  `AtomicUpdateProcessorFactory.java` and
  `AtomicUpdateProcessorFactoryTest.java`. It is the
  option A fix Nick decided on 2026-10-08, and the
  main-side handoff records its gate green at this head.
  The merger file is untouched by the commit.
- Findings: F1 (the factory defect) was the audit's first
  blocking item and is **done since the audit**, per the
  commit above and the recorded decision in TESTING.md.
  F2 (child-document lists in a mixed atomic document) is
  still open and was the audit's second blocking item:
  `mergeDocHavingSameId` tests `getFirstValue() instanceof
  Map` with no `SolrDocumentBase` guard, and `SolrDocument`
  values are Maps, so a field whose first value is a child
  document enters `atomicOperations` and fails as an
  unknown operation; whether base accepted that input is
  a hypothesis the audit did not settle. F3 (JSON arrays of
  operation maps are now atomic; the changelog is silent),
  F4 (mixed `[Map, plain]` returns a 400 the changelog
  does not mention; `[plain, Map]` is silently indexed),
  and F5 (operation order documented only in a test
  comment) are open as draft Limits material. F6 is open
  and benign. F7 and F8 are addressed or verified.
- Against TESTING.md: the DISCUSS decision (option A) is
  consistent with the audit and is now implemented. F2 is
  not covered by any recorded decision.
- **Class (c), needs a code fix before final review.**
  The fix, exactly: add the `SolrDocumentBase` guard in
  `mergeDocHavingSameId` and in `atomicOperations`, with
  a test for a field whose first value is a child
  document; or prove by a run that base also returned a
  400 for that input and carry the proof into the draft.
  Until one of those lands, the branch is not ready.
  Findings 3, 4 and 5 go in the draft's Limits either way.
  The PR also sequences after or before SOLR-12703, which
  changes the same method; the two are not merged cold.

### SOLR-6065

- Audit verdict: **Not ready**, on one blocking item. Last
  review: round 28 (Close) at the live head.
- Head check: audit `6aef011ee8d`; live tip
  `6aef011ee8d5`. Match.
- Findings: F5 (500 versus 400) is settled: the head
  carries the SERVER_ERROR mapping from Nick's locked
  Review1 decision, and the audit's code reading answers
  the retry question (a 500 from a replica is not retried
  by the leader). The audit notes the lock itself is not
  recorded in TESTING.md; the mapping is in the head and
  the decision stands from Review1, so no new item is
  opened for it. F3 (recognition by message text) is
  addressed by design, with the dependence stated in a
  code comment and pinned by the test. F1 is the blocker:
  the ticket asks for cloud tests that set a lower limit
  and verify the clean error for a single shard, and the
  branch has only a single-core test. F2 (only `addDoc`
  maps the message; other write paths unmapped, merge path
  untraced) and F6 (the "delete documents and optimize"
  advice wording) are open at draft level. F4 (the test's
  reflective, JVM-global setup) is a hypothesis the gate
  resolves when it runs the test.
- Against TESTING.md: no decision recorded for the cloud
  test question. It is genuinely new and is appended to
  DISCUSS.
- **Class (d), needs a decision from Nick** (DISCUSS:
  SOLR-6065, the ticket's cloud test). The alternative the
  decision names is the (c) remedy: add a cloud test with
  a low max-docs limit on one shard, checking that the
  clean error reaches the client.

### SOLR-7022

- Audit verdict: **Ready for final review.** Last review:
  round 28 (Close) at the live head.
- Head check: audit `6a233ab2fdb`; live tip
  `6a233ab2fdb0`. Match.
- Findings: F3 (the helper test cannot fail on base) is
  addressed by the commit-level test, which discriminates
  by reading. F6 (the production change) is verified. Open:
  F1, the changelog's example cause ("an autocommit during
  core reload") is unproven; F2, an abandoned wait returns
  normally, so a commit with `waitSearcher=true` can
  report success without the searcher registered; F4, the
  commit-level test is timing-sensitive and needs a seed
  check at the gate; F5, two test classes for a four-line
  change is a judgment maintainers may revisit.
- Against TESTING.md: no decision recorded; none of the
  open items needs one. F1 is a wording fix (reword to "an
  interrupt while waiting", or trace the interrupter) and
  F2 is a statement the PR owes.
- **Class (b), ready with draft-level notes only.** The
  draft rewords or drops the changelog's example cause and
  states the return-normal behavior in the main text. The
  F4 seed check happens at the branch's final gate.

### SOLR-7504

- Audit verdict: **Not ready**, on two blocking items.
  Last review: round 28 (Needs work) at the live head,
  plus the 2026-10-08 code-review record (Nearly).
- Head check: audit `e3fdc8eb58f`; live tip
  `e3fdc8eb58f2`. Match.
- Findings: F1 (non-`set` operations now fail the request)
  is accepted by the locked BAD_REQUEST design from
  Review1 and stated in the changelog; settled. F3 is the
  first blocker, verified by reading: the counter checks
  `src.getFirstValue()` for a Map, so with `[plain, Map]`
  input it takes the old path, stores an integer count,
  and drops the operation map, while the changelog says
  other operations are rejected "instead of being
  silently dropped". The tests cover only Map-first input.
  F2 is the second blocker: the ticket's symptom is not
  covered for updates that leave the source field alone,
  because the counter runs only when the source field is
  in the update and a trailing `DefaultValue` then writes
  a plain 0 that an atomic update applies as a `set`; the
  changelog title says "fixed". F4 (the shape check is
  duplicated with SOLR-6045), F5 (tests stop at the
  counter), F6 (null and collection semantics for the
  owner to confirm) and F7 (long changelog title) are open
  at draft level. F8 is a combined-tree item: with
  SOLR-12705 in the same tree, `mutateAtomicOperations`
  runs before the counter and this branch's rejection is
  bypassed; it binds only if the two land together.
- Against TESTING.md: the BAD_REQUEST lock is not recorded
  in TESTING.md, but it is in the head under Nick's
  Review1 decision and the audit accepts it; no new item.
  F2 is genuinely new and is appended to DISCUSS. F3 is a
  code fix, stated below.
- **Class (c), needs a code fix before final review.**
  The fix, exactly: make the counter's detection look at
  every value of the source field, so the plain-first
  shape is rejected the way the Map-first shape is; add
  the plain-first test; re-gate. The audit's alternative
  (narrow the changelog claim and say why) is a wording
  fallback, not the recommendation here, because the
  branch's stated purpose is to stop silent drops.
  Separately **(d)**: finding 2 goes to DISCUSS
  (chain placement and the "fixed" title).

### SOLR-11483

- Audit verdict: **Ready for final review.** Last review:
  round 28 (Close) at the live head.
- Head check: audit `4431a250f66`; live tip
  `4431a250f665`. Match.
- Findings: F5 (the test discriminates) verified by
  reading; F6 (ref-guide sentence and changelog)
  addressed. Open: F1, the default file cap becomes 1000
  instead of 10 when `numRecordsToKeep` is set and
  `maxNumLogsToKeep` is absent, a default change with no
  maintainer sign-off yet; F2, the trigger is the presence
  of the setting, not a non-default value (hypothesis on
  placeholder handling); F3 and F4, LOW test nits
  (literals instead of the constants; an unclosed log in
  `TestRecovery`).
- Against TESTING.md: no decision recorded. F1 does not
  open a new decision item: the ticket itself proposes
  this default and names the fallback (close with
  documentation if maintainers object). What the PR owes
  is disclosure, which is draft work.
- **Class (b), ready with draft-level notes only.** The
  draft's Limits states the retention growth for configs
  that set `numRecordsToKeep`. F2 to F4 are final-round
  polish.

### SOLR-12703

- Audit verdict: **Ready for final review.** Last review:
  round 28 (Close) at the live head.
- Head check: audit `ed95d555e62`; live tip
  `ed95d555e62b`. Match.
- Findings: F3 (the nested check runs per operand) and F4
  (the changelog names the case and the 400) are verified
  or addressed. Open: F1, the 400 message appends the
  nested operand itself, so a request value goes into the
  response text; F2, the test covers the direct merger
  call only. F5 names the overlap with SOLR-6045 (same
  method, clean merge both orders, untested combination).
- Against TESTING.md: no decision recorded; none needed.
- **Class (a), ready for the final round as-is.** F1's
  remedy is a small production-message change (name the
  outer operation and the operand's shape or keys, not
  the value); the audit classes it as before-the-PR, not
  as a blocker, so it is final-round work rather than a
  bar to entering the round. The PR sequences with
  SOLR-6045, per F5.

### SOLR-12705

- Audit verdict: **Ready for final review**, with one
  owner decision before the PR. Last review: round 28
  (Close) at the live head.
- Head check: audit `b053944b127`; live tip
  `b053944b1277`. Match.
- Findings: F2 (the fix sits in the shared base class) is
  addressed by the changelog's framing. Open: F1, the
  test has no `add-distinct` or `remove` case; F3, a
  counting processor shares the new path, so on this
  branch a single-map `add` on a counted field becomes
  `{add: <count of the operand>}` and `remove` becomes
  `{remove: <count>}`, where base wrote a plain count,
  and neither case is in the changelog or the tests; F4,
  repeated operation maps are skipped by the new path, a
  limit to name; F5, a mutator returning null for every
  value operation drops the whole field, unmentioned in
  the changelog. F6 is the merge conflict with
  SOLR-16655 in `FieldMutatingUpdateProcessor.java`
  (merge-tree exits 1, verified by both audits). F7 names
  the 7504 bypass and an untested combined case with
  SOLR-5065.
- Against TESTING.md: no decision recorded. F3 is
  genuinely new and is appended to DISCUSS. F6 is
  coordination, not a decision: the two PRs sequence and
  the second rebases, and the fact goes in both drafts.
- **Class (d), needs a decision from Nick** (DISCUSS:
  SOLR-12705, counting processors on the atomic path).
  F1 and F4 settle in the final round as a test addition
  or a stated limit.

### SOLR-14262

- Audit verdict: **Ready for final review.** Last review:
  round 28 (Needs work) at the previous head; the live
  head's added commit answers its F2.
- Head check: audit `1e8d2b0075d`; live tip
  `1e8d2b0075d7`. Match.
- Findings: F2 (the test checked only that the header is
  present) is addressed at the live head, which asserts
  the `commitIgnored` value. F3 (the header is written
  only on the skip branch) is verified. Open: F1, the
  response contract is a custom header where the ticket
  discussion also weighs waiting for the commit or
  reporting `rf`; F4, `rsp` itself is not null-checked
  (hypothesis, low); F5, cloud propagation is untraced:
  a leader forwarding a commit to a buffering replica may
  not pass the replica's header back, so the ticket's case
  may not be reported in SolrCloud. F6 names the overlap
  with SOLR-5941 on the same skip path (clean merge; an
  autocommit has no client response, so a header written
  there goes nowhere).
- Against TESTING.md: no decision recorded. F1 is a
  maintainer decision, not Nick's: the branch implements
  the header, and the PR poses the contract question.
  That is Choices material under the formula, not a
  DISCUSS item.
- **Class (b), ready with draft-level notes only.** The
  draft documents the header, poses the contract question
  in Choices, and claims nothing about replicas until F5
  is traced; any replica claim waits for a cloud case.

## Group D

### SOLR-12245

- Audit verdict: **Ready for final review.** Last review:
  round 36 delta review (Nearly) at the live head,
  carrying the round 28 framing finding.
- Head check: audit `4a93167458b5`; live tip
  `4a93167458b5`. Match.
- Findings: F1 (the framing: the ticket asks about MDC,
  the branch changes the message) is addressed by the
  ADOPTED decision in TESTING.md; consistent, not new.
  F4 (null-guard coverage gap) is addressed by the same
  decision: no test added, the gap named in Limits;
  consistent. F2 (whether the node in `SolrError.req` is
  the failing request's node) is resolved by the audit's
  own code reading. F5 is overtaken (no consumer parses
  the message). F3 is still open and not covered by the
  ADOPTED decision: the message puts the target's URL,
  collection and shard into the error the client receives,
  and whether that detail belongs in the client response
  or only in logs is an owner call the round 36 review
  leaves open.
- Against TESTING.md: the ADOPTED framing accepts the
  message change but does not speak to F3's placement
  question. F3 is appended to DISCUSS with a
  recommendation.
- **Class (d), needs a decision from Nick** (DISCUSS:
  SOLR-12245, target detail in the client response). The
  draft also carries F1's framing and F4's Limits, per
  the ADOPTED decision. The audit could not see a gate
  receipt at this head from the review workspace; the
  draft's Proof follows the main-side record when the
  draft is written.

### SOLR-13265

- Audit verdict: **Ready for final review.** Last review:
  round 28 (Needs work) at the previous head; the round
  35 finding is the same item.
- Head check: audit `c134b34aa27f`; live tip
  `c134b34aa27f`. Match.
- Findings: F1 (the root handoff note in the outbound
  diff) is addressed at this head; the diff lists three
  files and no root note. That is the round 35 finding,
  upheld and confirmed DONE earlier; consistent, not new.
  F2 (test strength) is resolved by reading: the changed
  decision does not read the replica type, so the
  standalone test exercises the same branch. F4
  (changelog) is addressed. F3 stays open as a gate item:
  the test asserts `errors == null || errors.getValue()
  == 0`, so if the metric name were wrong the datapoint
  would be absent and the test would pass on base too;
  the registered name and the helper's name form are
  verified against main, but only a run settles whether
  the datapoint exists on base and the test fails there.
- Against TESTING.md: no decision recorded; none needed.
- **Class (a), ready for the final round as-is.** The
  final gate runs the focused test and reports the
  fail-before verdict, per F3; until that receipt exists
  the draft's Proof says "Awaiting gate", which is the
  formula's normal state, not a blocker.

### SOLR-13943

- Audit verdict: **Not ready**, on two blocking items.
  Last review: round 28 (Close) at the previous head,
  re-verified at this head in the round 38 closeout.
- Head check: audit `b37d7abfa2e9`; live tip
  `b37d7abfa2e9`. Match.
- Findings: F2 is the blocker. The class-level annotation
  `@LuceneTestCase.AwaitsFix(bugUrl = SOLR-13059)` stays
  on `TimeRoutedAliasUpdateProcessorTest` (line 78, on
  base and at the head); the branch removes only the
  method-level `@AwaitsFix(SOLR-13943)`. Whether the
  Lucene test framework still skips a method under a
  class-level AwaitsFix is not verified, and that
  framework's source is not in this repo. If it does skip,
  the branch re-enables nothing. F1 (no deterministic
  fail-before; the race is timing-based and was not
  reproduced) stays open as a gate item, and F4 (the
  reliability mechanism) stays a hypothesis for the draft
  to state as one. F3 is a record correction: the
  assignment's note that the changelog fragment is in the
  tree is wrong at this head; commit `b37d7abfa2e`
  removed the fragment on purpose ("test-only change").
- Against TESTING.md: no decision recorded. The fragment
  question is a convention call the audit does not make;
  a test-only branch shipping no fragment is consistent
  with the SOLR-12864 precedent in TESTING.md, so it is
  recorded here rather than opened as a decision.
- **Class (c), needs work before final review.** The
  step, exactly: run `testDateMathInStart` at the head
  and settle whether it executes under the class-level
  AwaitsFix. If it executes, the branch enters the final
  round and F1 is carried as its gate item. If it does
  not execute, the branch needs an owner decision on the
  class-level annotation (which belongs to SOLR-13059)
  before the test change means anything, and that
  decision goes to DISCUSS at that point. The main-side
  assignment record also needs its changelog note
  corrected, per F3.

### SOLR-14718

- Audit verdict: **Not ready**, on two blocking items.
  Last review: round 28 (Needs work) at the previous
  head; the round 35 gate at this head is recorded green
  on the main side.
- Head check: audit `29c09959791a`; live tip
  `29c09959791a`. Match.
- Findings: F3 (the root handoff note in the outbound
  diff) is addressed at this head. F4 (the mechanism) is
  verified by reading. F1 is the first blocker: the
  regression test clears the command right after
  `distribAdd` and before `finish()`, with no barrier, so
  if the failure is handled before the clear, the test
  can pass on the old code; the green gate therefore does
  not show the test measures the fix, because the test is
  unchanged since round 28. F2 is the second blocker: the
  ticket packet names a second flaw, the per-node
  streaming-client association that can report the first
  document of a batch, and the branch does not change it;
  the changelog is correctly limited to command reuse.
- Against TESTING.md: no decision recorded. F2 is
  genuinely new and is appended to DISCUSS.
- **Class (c), needs a code fix before final review.**
  The fix, exactly: make the regression test
  deterministic, for example a barrier that releases the
  failure only after `cmd.clear()` has run, then re-gate
  so the receipt shows the test discriminates. The change
  is test-only. Separately **(d)**: finding 2 goes to
  DISCUSS (the second flaw, follow-up or include).

### SOLR-16356

- Audit verdict: **Not ready**, on one blocking item.
  Last review: round 28 (Needs work) at the live head,
  rechecking the round 3 findings.
- Head check: audit `39c0585072f0`; live tip
  `39c0585072f0`. Match.
- Findings: F1 (cache clearing after a closed-core catch)
  and F2 (test and changelog present) are addressed. F4
  (the changelog title must not be read as covering every
  close-time stack trace) is addressed: the title names
  the update-log path. F3 is the blocker: the periodic
  task in `DocExpirationUpdateProcessorFactory` commits
  after its delete-by-query, that commit fails with
  `SolrCoreState already closed`, and it is logged at
  ERROR; the ticket transcript includes this second stack
  trace, and the branch does not touch that factory.
  Evidence note: the gate receipt in the review workspace
  stands at `dcb16c775d6`, which is not an ancestor of the
  live head, but the `solr/` and `changelog/` changes are
  patch-identical at both heads (same stable patch-id),
  and the receipt head carried a handoff doc the live
  head does not. The draft must say the receipt names an
  older head with identical code.
- Against TESTING.md: no decision recorded. F3 is
  genuinely new and is appended to DISCUSS.
- **Class (d), needs a decision from Nick** (DISCUSS:
  SOLR-16356, the second close-race ERROR). The patch-id
  evidence note goes in the draft's Proof either way.

### SOLR-16655

- Audit verdict: **Ready for final review.** Last review:
  round 28 (Needs work) at the live head, over the round
  5 and round 12 findings.
- Head check: audit `aa7898d972a7`; live tip
  `aa7898d972a7`. Match.
- Findings: F1 (the compatibility question: every
  selector-based mutator now applies to child documents)
  is addressed by the ADOPTED decision in TESTING.md:
  the design stands, the upgrade note goes into the
  draft, no code change and no re-gate. Consistent, not
  new. F3 (the labelled-child gap from rounds 5 and 12)
  is addressed in the head. F2 (test volume, 461 lines of
  near-repeated setup) is open, style only, not blocking.
  F4 is the coordination item: a content conflict with
  SOLR-12705 in `FieldMutatingUpdateProcessor.java`
  (merge-tree exits 1, verified here and in the 12705
  audit); the two PRs sequence and the second rebases.
- Against TESTING.md: the governing decision is the
  ADOPTED one; the audit matches it.
- **Class (b), ready with draft-level notes only.** The
  draft carries the owed upgrade note and names the
  SOLR-12705 overlap with its sequencing. Proof follows
  the main-side gate record at this head when the draft
  is written; the audit could not see that receipt from
  the review workspace.

### SOLR-16673

- Audit verdict: **Ready for final review.** Last review:
  round 28 (Close) at the live head, over round 3.
- Head check: audit `d5c19e64ba1b`; live tip
  `d5c19e64ba1b`. Match.
- Findings: F1 (the Int and Float siblings missing the
  guard) and F2 (test and changelog) are addressed. F3
  stays open as follow-up coverage: no caller-level test
  exercises the Schema Designer path into the Long parser.
  F4 is addressed by scope: the changelog and the test
  cover only the empty-string NPE, and the ticket's other
  errors (a non-string unique key, child-document `_root_`
  type errors) are separate work. Evidence note: the
  receipt in the review workspace records no head SHA and
  finished 2026-10-03, while the two fix commits carry
  author dates of 2026-10-04, so no receipt can be tied
  to this head.
- Against TESTING.md: no decision recorded; none needed.
- **Class (b), ready with draft-level notes only.** The
  draft's Limits carries F3's coverage gap and F4's
  narrow scope, and its Proof says "Awaiting gate" until
  a receipt names `d5c19e64ba1b`; the final round gates
  the branch at this head.

### SOLR-16910

- Audit verdict: **Not ready**, on one blocking item.
  Last review: round 28 (Needs work) at the previous
  head; the main-side record has the gate green on the
  third run on 2026-10-07 at this head.
- Head check: audit `9fce3e9a7058`; live tip
  `9fce3e9a7058`. Match.
- Findings: F2 (test coverage of the WARN-only and
  neither-emitted cases), F3 (the handoff doc in the
  outbound diff) and F4 (the license comment and final
  newline) are all addressed in the commits since the
  round 28 snapshot. F5 verifies by reading that the
  clearing behavior is unchanged apart from the WARN now
  carrying the request details. F1 is the blocker: the
  ticket also calls out `SolrCore.Request` logging
  behavior, the reporter says the desired behavior there
  is undecided, and the branch fixes only the WARN format
  in `LogUpdateProcessorFactory.finish()`.
- Against TESTING.md: no decision recorded. F1 is
  genuinely new and is appended to DISCUSS.
- **Class (d), needs a decision from Nick** (DISCUSS:
  SOLR-16910, scope of the logging fix).

### SOLR-18505

- Audit verdict: **Ready for final review.** Delta audit
  of the live PR, per the assignment. Last review: round
  31 (Close) at the gated head.
- Head check: audit `e28739b4069d`; live tip
  `e28739b4069d`. Match. The one commit past the gated
  head `8ca33200e37` is comment-only (one file, one hunk,
  every changed line a comment), verified by the audit's
  full-diff read.
- Findings: all four round 31 items are addressed or
  overtaken. F1 (no CI evidence) is answered on the PR
  itself: Gradle Precommit and Solr Tests via Crave both
  pass on the live head. F2 is answered in the
  description's Limits. F3 is the delta itself. F4 (a
  suggested re-gate) is overtaken by the ADOPTED decision
  in TESTING.md: no re-gate for the comment-only head;
  consistent, not new. The audit's one exactness note:
  the description names a fork Actions run as dispatched
  "for the same class at this head"; the run's commit is
  the live head plus one commit adding the workflow file,
  with identical test sources, so the claim holds for the
  test code but the description could name the run's
  commit to be exact.
- Against TESTING.md: the governing decision is the
  ADOPTED one; the audit matches it. No change to the PR
  is recommended by the audit.
- **Class (b), ready with draft-level notes only.** The
  single note is the optional one-sentence correction to
  the live description's corroboration line, naming the
  fork run's commit. Nothing else is owed.
