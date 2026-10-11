# SolrCloud drafts round 1, part SC5 (windows draft agent, 2026-10-11)

Slice SC5: SOLR-3865, SOLR-4754, SOLR-10234, SOLR-10641, SOLR-16437. Worktree HEAD at start: `bd71ed56f21` (the brief names `6ae4a14ff46`; HEAD was used as it stands). Files written: `pr-drafts/solrcloud/SOLR-16437.md` only, plus this report. No commit, push, PR or Jira write, no build, no test.

## Per ticket

- **SOLR-3865: not drafted (audit only, not ready; held on DISCUSS).** Receipt: NO GATE. Head `363e8f0651e9` matches the fork tip `solr-3865-submit`. Answers DISCUSS item 8 (keep the ticket link with the disclosure, recommended; not taken). Owed before any draft: premise run on the audit spec (base with the branch tests fails with a connect failure, not AlreadyClosedException); short connect timeout in the second test (branch fix, part p5 finding 7); handoff-note cleanup. Any later draft must state the behavior change: after close, every later call throws AlreadyClosedException, including a call that used to connect again (part p5 finding 8).

- **SOLR-4754: not drafted (audit only, not ready; held on DISCUSS).** Receipt: NO GATE. Head `d2e9038881f6` matches the fork tip `solr-4754-submit`. Answers DISCUSS item 9 (keep the scheme-only guard with its text fixes, recommended; not taken). Owed: error text names `-Dhost`, which the shipped solr.xml does not read (it reads `solr.host.advertise`) (branch fix, part p5 finding 2); changelog title (finding 3); premise run showing `solr.host.advertise=http://` registering an empty host on base; handoff-note cleanup. Fail-before is inconclusive by construction, so no text may claim a failing base run for the current test.

- **SOLR-10234: not drafted (audit only, plausible; held on DISCUSS).** Receipt: NO GATE. Head `16825538a766` matches the fork tip `solr-10234-submit`. Answers DISCUSS item 10 (suppression on the base classes as implemented, recommended, versus a Lucene-side per-node limit; and drop the test-only changelog fragment, recommended; neither taken). Owed after the call: premise run (the pin fails on base) and a first gate. Any Proof must say the change pins the annotation only and does not reproduce the 2048 handle limit.

- **SOLR-10641: not drafted (audit only, no draft; held on DISCUSS).** Receipt: NO GATE. Head `4ab4bd2040f3` matches the fork tip `solr-10641-submit`. Answers DISCUSS item 11 (ship the passing pin as its own PR, recommended after the tidy fold-in and a first gate; not taken). Owed: the tidy fold-in the receipt names (join the assertEquals arguments in OverseerTaskQueueTest.java; replacement lines in part p5 finding 6), then a first gate. The new tests also pass on base, so any Proof is inconclusive by construction.

- **SOLR-16437: corrected (existing draft fixed in place).** `pr-drafts/solrcloud/SOLR-16437.md`.
  - Gate state conflict: the answers (2026-10-09) say NO GATE and "draftable once it lands". The receipt (first gate finished 2026-10-10, GATE GREEN at `aa2a6b8afb6f`) is later and wins. The fork tip `solr-16437-submit` is `aa2a6b8afb6f` (ls-remote match). The draft therefore does not wait on the gate. The answers' gate-state line is stale and the main side should update it. If the lead prefers to hold the draft until that update, it is a one-line hold.
  - Proof: added the receipt counts that the draft lacked. At head `aa2a6b8afb6f`, CollectionsAPISolrJTest runs 25 tests, 1 skipped, 0 failures, recorded 2026-10-10. With CollApiCmds.java reverted to the base `e2cdb2d7e8ae`, the same class runs 25 tests, 1 skipped, exactly 1 failure (testAddReplicaPropRejectsUnknownReplica).
  - Citations: pre-change code (What happens today; the old-code lines in What this change does; the Choice's update-path link) now links the merge-base `e2cdb2d7e8ae`, and the text says so. Code the change produces (CollApiCmds L345-L351 and L352-L362, the test L1292-L1322, the DELETEREPLICAPROP limit L385-L390, the changelog) links head `aa2a6b8afb6f`. All cited ranges were read at those SHAs and match.
  - What happens today: corrected who logs. ReplicaMutator throws (L165-L175); the Overseer and the distributed updater catch, log and skip. The draft had the updater logging.
  - Adopted answer lines kept: scope is ADDREPLICAPROP only; DELETEREPLICAPROP and the other APIs are in Limits with the follow-up offer; the wrong-shard behavior change is stated (400 now; before, 200 with the property set on no replica, and for preferredleader a possible clear of the named shard's property).
  - Changelog title (`The ADDREPLICAPROP collection API now returns HTTP 400 when the collection, shard or replica does not exist instead of silently returning 200.`) is accurate for the unknown-name cases. The wrong-shard case is covered in the body. No branch correction is owed; optional only.
  - Length: about 3,180 visible characters (about 5,000 with link targets).
  - Not verified: "the other APIs the ticket mentions" was not checked against the Jira text (no Jira read). Limits wording follows the adopted answer and the prior draft.

## Gaps and holds

- Hold on the four DISCUSS tickets (3865, 4754, 10234, 10641): no draft, per the brief. Each waits on its DISCUSS call and on a premise run and first gate on the branch.
- Gate-state conflict on SOLR-16437 (answers stale versus receipt): flagged above.
- Worktree HEAD `bd71ed56f21`, not the `6ae4a14ff46` the brief names.
- No SC5 ticket is named in the answers' "Draft corrections owed" list, and none is a live PR or a retire/close item.
