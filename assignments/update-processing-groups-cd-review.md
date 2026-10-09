# Assignment: last-review audit, Groups C and D (the remaining 19 in Update processing and atomic updates)

Groups A and B are done; their audits are in
`audits/update-processing/` on this branch. This assignment
covers the other 19 branches in the category: the ten recorded
PR-ready (Group C) and the eight recorded gated with no PR, plus
the one live PR (Group D). Same audit shape as Groups A and B:
audit the last review or disposition of each branch, finding by
finding, against the current head, and certify readiness for the
final round.

The heads below are the inventory heads and the last-review
facts are the main side's records, given here so the audit is
not blocked on records that live on the main side. Treat them as
claims to verify against the live branch, as in Groups A and B.

## Rules

- Reading work only. No builds, no Gradle, no test runs, nothing
  executed. Push nothing to any submit branch; all work product
  lands on this branch, `pr-prepare`.
- Claim first, per group: `claims/update-processing-audit-group-c.md`
  or `claims/update-processing-audit-group-d.md`, with your name
  and the date. A rejected push means the group is taken.
- Decisions in `TESTING.md` on this branch govern. Where a
  branch carries a DISCUSS item there, the audit records it and
  does not certify past it.
- Verify each live head first. Review the branch's own diff
  (three-dot from the merge-base with upstream main). Tag claims
  verified or hypothesis.

## Deliverables, per branch

An audit file at `audits/update-processing/SOLR-<ticket>.md`, in
the Groups A and B format: last review identified, findings with
their status at the current head, evidence status, and the
readiness line.

For every branch certified ready, a PR description draft at
`pr-drafts/update-processing/SOLR-<ticket>.md` is REQUIRED this
time, following `pr-formula.md`: reviewed head named at the
top, a bold one-line summary opening each section, citations as
blob links at that head, Proof counts from the gate receipt
(the pointer below gives the gate's date and head; where it
gives counts, use them exactly). In Groups A and B only one
draft was produced for four certified branches; the batch
opens from these drafts, so a certified branch without a draft
is unfinished work. Where a usable draft already exists in the
branch's records, audit it against the formula instead of
writing a new one. SOLR-18505 is the exception: audit only, no
draft; its description is already live.

## Group C: the ten recorded PR-ready

| Ticket | Branch | Head | Last review and gate record |
|---|---|---|---|
| SOLR-3657 | solr-3657-submit | 14edaca577c | Family review: Ready with edits. Round 27 disposition DONE, GATED at this head, 2026-10-06 |
| SOLR-5065 | solr-5065-submit | c0a0ce1b8d9 | Round 27 disposition DONE, GATED at this head, 2026-10-05. A round 35 review read an older snapshot; work from the disposition |
| SOLR-6045 | solr-6045-submit | dcdef50d700 | Review1: Ready with one test gap; the gap was closed with a gated test-only commit (production byte-identical). DISCUSS item in `TESTING.md`: do not certify past it |
| SOLR-6065 | solr-6065-submit | 6aef011ee8d | Review1: Needs changes; dispositioned and re-gated at this head (error code moved to SERVER_ERROR under Nick's locked decision) |
| SOLR-7022 | solr-7022-submit | 6a233ab2fdb | Review1 test gap; closed with a commit-level wiring premise at this head, gated |
| SOLR-7504 | solr-7504-submit | e3fdc8eb58f | Review1: Needs changes, four items; dispositioned and re-gated at this head (unsupported operations now throw BAD_REQUEST, under Nick's locked decision) |
| SOLR-11483 | solr-11483-submit | 4431a250f66 | Round 27 DONE, GATED at this head, 2026-10-06; GitHub corroboration recorded |
| SOLR-12703 | solr-12703-submit | ed95d555e62 | Round 27 premise and gate receipts at this head (skip-audit family) |
| SOLR-12705 | solr-12705-submit | b053944b127 | Round 27 DONE, GATED at this head; premise proof 1 of 43 tests fails on base; GitHub corroboration SUCCESS |
| SOLR-14262 | solr-14262-submit | 1e8d2b0075d | Family review: Ready with edits. Round 27 disposition DONE, GATED at this head, 2026-10-06 |

Notes for Group C:

- SOLR-6045 and SOLR-12703 both change
  `AtomicUpdateDocumentMerger`. Their audits must name the
  overlap; the two PRs will be sequenced, not opened cold
  together.
- The family reviews behind SOLR-3657 and SOLR-14262 said
  "Ready with edits". The audits confirm the edits are in the
  shipped trees, or name the ones that are not.

## Group D: the eight recorded gated, no PR, plus the live PR

| Ticket | Branch | Head | Last review and gate record |
|---|---|---|---|
| SOLR-12245 | solr-12245-submit | 4a93167458b | Gate 2026-10-05 at this head. Round 35 bulk review, then a round 36 delta review on the `code-review` branch (`reviews/solr-12245-submit.md`), verdict Nearly: the branch changes the error message, not MDC. The ADOPTED framing decision in `TESTING.md` governs its draft |
| SOLR-13265 | solr-13265-submit | c134b34aa27 | Round 35: Needs work, one finding (a handoff document in the outbound patch). Disposition confirmed DONE from the records at this head, 2026-10-07 |
| SOLR-13943 | solr-13943-submit | b37d7abfa2e | Gate 2026-10-05 at an older head; round 38 closeout re-verified at this head. The changelog question from its spot-check is resolved: the fragment is in the tree. Its reliability hypothesis stays a hypothesis and is stated as one |
| SOLR-14718 | solr-14718-submit | 29c09959791 | Round 35 disposition gate GREEN at this head, 2026-10-07 |
| SOLR-16356 | solr-16356-submit | 39c0585072f | Round 27 DONE, hardened, at this head, 2026-10-04 |
| SOLR-16655 | solr-16655-submit | aa7898d972a | Gate green at this head (2026-10-07 records). Its review finding is the branch's design position, re-raised; `TESTING.md` ADOPTED: the upgrade note goes into its draft |
| SOLR-16673 | solr-16673-submit | d5c19e64ba1 | Round 27 DONE, hardened, at this head, 2026-10-04 |
| SOLR-16910 | solr-16910-submit | 9fce3e9a705 | Gate green on the third run, 2026-10-07, at this head; the test's logger-level assumptions were removed during its disposition |
| SOLR-18505 | solr-18505-submit | e28739b4069 | Live PR. Gated at 8ca33200e37; the live head adds one comment-only commit in the test file. `TESTING.md` ADOPTED: no re-gate. Audit only, no draft |

Notes for Group D:

- SOLR-12705 in Group C and SOLR-16655 here both change
  `FieldMutatingUpdateProcessor`. The SOLR-16655 audit names
  the overlap.
- SOLR-18505's audit is a delta check: confirm the comment-only
  delta is exactly that, and that the live description still
  matches the branch. Nothing else is asked of it.
