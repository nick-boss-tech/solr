# Assignment: consistency pass over the 29 update-processing and atomic-updates branches

Owner direction, 2026-10-09: before the 29 PRs open, run one bounded pass over the batch for conflicts of position. Desk work only: no code, no gates, no branch edits, no new decisions. Contradictions are reported, not resolved. Claim first in `claims/update-29-consistency-pass.md`, then write the report to `reports/update-29-consistency-pass.md`.

## Why

All 29 branches are gated, all owner decisions for them are recorded in TESTING.md, and drafts are written in `pr-drafts/update-processing/`. Code conflicts were covered by trial merges in the audit rounds. What has not been checked as a batch: whether the drafts take positions that contradict each other, in ways a maintainer reading several of these PRs in the same component area would notice.

## Sources

- The drafts in `pr-drafts/update-processing/`.
- The recorded decisions in TESTING.md.
- The interaction sections of `audits/update-processing/*.md`.
- The 29 tickets: 3657, 4841, 5065, 5505, 5754, 5887, 5939, 5941, 6045, 6065, 6973, 7022, 7504, 11475, 11483, 12245, 12703, 12705, 12864, 13265, 13696, 13943, 14262, 14718, 16356, 16655, 16673, 16910, 18505. (18505 is already open as a PR; include it in the check, it needs no opening.)

## The four checks

1. **Landing order.** State the order the 29 should land in, as a single sequence or grouped tiers, with the constraint behind each placement. Verify and state precisely at least these known ones: SOLR-16655 before SOLR-12705; SOLR-13943 stacked on SOLR-13696; SOLR-6045 and SOLR-12703 share a production file and method; SOLR-7504 and SOLR-12705 overlap behaviorally on set-null and add/remove in atomic updates. Find any others the drafts or audits imply.
2. **Contradictory behavior claims.** Two drafts describing the same mechanism or semantics (especially atomic update operand handling across 7504, 12705, 16356, 16910, 14718) in ways that cannot both be true, or framing the same semantics inconsistently.
3. **Philosophy consistency.** Positions on error surfacing versus silent handling, fail versus skip, and scope, across the batch (for example 6065's error code position, 12245's error detail, 11475's sign-mismatch ship-as-implemented). Per-branch differences the owner decided are not findings. A finding is only: one draft asserts a general rule that another draft's position violates, or two drafts describe the same situation with opposite outcomes.
4. **Follow-up promises.** Any follow-up named in two drafts with different scopes, or the same follow-up promised twice as if it were two separate pieces of work.

## Report shape

- The landing order (check 1), compact.
- Each contradiction found: quoted from both drafts, with file names, rated real or apparent (apparent means the wording differs but the positions agree).
- An explicit "no contradiction" statement for each check where that is the result.
- Nothing else. No per-branch re-audit, no gate claims, no new owner decisions; if a real contradiction needs an owner call, state the two positions and stop there.
