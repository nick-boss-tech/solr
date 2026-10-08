# Round 36 handoff: review and patch before the queue

Date: 2026-10-07. From: the Linux-side queue owner. Machine-readable
version of this list: `inventory/handoff-2026-10-07-round36.tsv`
(identical content).

## Purpose

The owner is reversing the order for these 78 branches: review and
patch first, then the Linux test queue. Most of them have not been
through the queue at all; the rest were reviewed once, read-only,
in the 2026-10-07 bulk round with their findings still open, or
were dispositioned at their current head and owe only a delta
check. Nothing in this list enters the Linux queue until it comes
back through this channel reviewed, and patched where the review
calls for it.

## How to work the list

- Claim each branch in `claims/` before starting, per the channel
  protocol in the README. One review per commit in `reviews/`.
- Heads are pinned in the TSV. If a branch tip has moved past the
  listed head, stop on that branch and note the new tip in the
  review file; do not review a different tree than the one listed.
- Patching: this handoff authorizes fix commits on the listed
  submit branches (the owner's instruction for this round, an
  exception to the review-only default). Commit author is the ICLA
  identity; no Co-Authored-By trailers. Before pushing a patch,
  re-read the branch tip; push plainly only when the tip is still
  the listed head plus your own commits. Never force. If the tip
  moved underneath you, stop and flag it in the review instead.
- Most group A branches, and some group B branches, carry a
  `SOLR-<ticket>-TESTING.md` file: the author's own run-shape note.
  Read it as context for what the branch is supposed to do. Treat
  any "hypothetical" or "unrun" label in it as an unverified claim.
  Leave the file in place; the Linux side removes it at ship time,
  after gating.
- The channel hard limit stands: nothing is compiled or run for
  this round. Patches are by reading and reasoning, and each patch
  commit message says what changed and why. The Linux side runs
  the full gate (changelog parse, tidy, Error Prone compile,
  premise run against the base, counted focused tests, module
  check) when the branch returns.
- Direction calls and owner-locked designs are flagged in the
  notes. Pose them in the review; do not decide them, and do not
  patch around them.
- Return path: a branch is done on this side when its review file
  carries a final verdict at the current head and any patches are
  pushed. The Linux side picks it up from the channel from there.

## Group A: fresh arrivals, never reviewed (34)

Full review and patch. These arrived with author handoff docs and
have had no review pass of any kind.

| Ticket | Branch | Head | Note |
| --- | --- | --- | --- |
| SOLR-11479 | solr-11479-submit | e6fdc9ed5d50 |  |
| SOLR-11939 | solr-11939-submit | d4cff5e76430 |  |
| SOLR-12347 | solr-12347-submit | b77acba2ad60 |  |
| SOLR-17055 | solr-17055-submit | f0c401a290b2 |  |
| SOLR-17356 | solr-17356-submit | ea7fc15cade4 |  |
| SOLR-4754 | solr-4754-submit | d2e9038881f6 |  |
| SOLR-6973 | solr-6973-submit | 4c6092614e5a |  |
| SOLR-11678 | solr-11678-submit | 55d8cd189d15 |  |
| SOLR-7323 | solr-7323-submit | fb034dc5f877 |  |
| SOLR-8088 | solr-8088-submit | 2398c9bea085 |  |
| SOLR-17987 | solr-17987-submit | e8c22910f507 |  |
| SOLR-11391 | solr-11391-submit | 825d7f81d12b |  |
| SOLR-10131 | solr-10131-submit | b93cf24a9d99 |  |
| SOLR-10641 | solr-10641-submit | 4ab4bd2040f3 | Hypothetical-reproduction handoff doc; premise unverified. |
| SOLR-15712 | solr-15712-submit | 555f9cab6b72 |  |
| SOLR-16570 | solr-16570-submit | 5676646936d5 |  |
| SOLR-16885 | solr-16885-submit | 2b9b80119a92 |  |
| SOLR-8628 | solr-8628-submit | ce8211e05e06 |  |
| SOLR-12161 | solr-12161-submit | a40db4fdb5c4 |  |
| SOLR-12864 | solr-12864-submit | 8c5455d3d242 |  |
| SOLR-10667 | solr-10667-submit | 5cee0d5bcf17 |  |
| SOLR-10694 | solr-10694-submit | 093d90c62ded |  |
| SOLR-11356 | solr-11356-submit | 8474e5a3a26d |  |
| SOLR-9759 | solr-9759-submit | 31e702622dae |  |
| SOLR-9818 | solr-9818-submit | 63f2d7ce9267 |  |
| SOLR-9831 | solr-9831-submit | f269a70e84f0 |  |
| SOLR-3498 | solr-3498-submit | 812598302dee |  |
| SOLR-4502 | solr-4502-submit | 4491f5162c1d |  |
| SOLR-8051 | solr-8051-submit | 44588ce6719e |  |
| SOLR-9595 | solr-9595-submit | 7ff1350ab7b6 |  |
| SOLR-10234 | solr-10234-submit | 16825538a766 |  |
| SOLR-10364 | solr-10364-submit | 502bdbf033fa |  |
| SOLR-10403 | solr-10403-submit | 1e285cd730c2 |  |
| SOLR-16322 | solr-16322-submit | 3f7c6268c5d6 | Tip moved once already (2026-10-07); verify the head before reviewing. |

## Group B: bulk-reviewed, findings open (25)

Reviewed once in the 2026-10-07 bulk round (read-only, nothing
run). Review fresh against the listed head, confirm or drop the
bulk findings, and patch what holds. Verdicts: 9 Needs work,
7 Not ready, 9 Nearly.

| Ticket | Branch | Head | Note |
| --- | --- | --- | --- |
| SOLR-18341 | solr-18341-submit | 458b098719d7 | Bulk verdict Needs work. Carries a standing direction call recorded as the owner's in the design record; pose it, do not re-decide it. |
| SOLR-1877 | solr-1877-submit | 0d5916186797 | Bulk verdict Needs work. |
| SOLR-4367 | solr-4367-submit | 0af6087f43fa | Bulk verdict Needs work. Pairs-round context: composes with SOLR-4399 and SOLR-3722 material. |
| SOLR-4399 | solr-4399-submit | de6cc6b27edd | Bulk verdict Needs work. Pairs-round context: see SOLR-4367. |
| SOLR-4424 | solr-4424-submit | 2e947b7f622c | Bulk verdict Needs work. Composes with SOLR-3722; an earlier GitHub failure on this branch was classified a flake. |
| SOLR-6193 | solr-6193-submit | ec94bf50c80c | Bulk verdict Needs work. |
| SOLR-8536 | solr-8536-submit | 28552ddc26eb | Bulk verdict Needs work. |
| SOLR-8767 | solr-8767-submit | 3b5f2d235732 | Bulk verdict Needs work. Carries a standing direction call recorded as the owner's; pose it, do not re-decide it. |
| SOLR-8954 | solr-8954-submit | 1d981abe7000 | Bulk verdict Needs work. |
| SOLR-12608 | solr-12608-submit | d1dd8a1f9f0a | Bulk verdict Not ready. Caution: the bulk review's snapshot SHA (d1dd8a1f0aa1) and the fork tip (d1dd8a1f9f0a) are a near miss; confirm the tree at the listed head is the intended one before reviewing. |
| SOLR-15478 | solr-15478-submit | 0478bdf0ac5c | Bulk verdict Not ready. Priority: this branch's fix also closes the open P1 on the already-gated SOLR-15674 branch; the two ship together or 15674's P1 stays open. |
| SOLR-17612 | solr-17612-submit | cd0426e40a72 | Bulk verdict Not ready. |
| SOLR-9060 | solr-9060-submit | 704ca28bf79d | Bulk verdict Not ready. |
| SOLR-17393 | solr-17393-submit | dd6c82924fff | Bulk verdict Not ready. SOLR-9637 is stacked on this branch; the submission strategy is an owner call. Review on the merits; do not restack. |
| SOLR-16155 | solr-16155-submit | 0881de1ed68b | Bulk verdict Not ready. Standing owner question (no PR vs upstream #1151); review on the merits. |
| SOLR-9342 | solr-9342-submit | 833e11192a7f | Bulk verdict Not ready. Script-only branch (developer tooling); dispositioned as script-only on the Linux side in an earlier round. |
| SOLR-11288 | solr-11288-submit | cd094c3c623f | Bulk verdict Nearly. |
| SOLR-12094 | solr-12094-submit | 8d957a73f4fb | Bulk verdict Nearly. |
| SOLR-12991 | solr-12991-submit | 1a86966179e1 | Bulk verdict Nearly. |
| SOLR-14919 | solr-14919-submit | 84e7bcaeb57d | Bulk verdict Nearly. |
| SOLR-14967 | solr-14967-submit | ee76643f5b3f | Bulk verdict Nearly. |
| SOLR-16437 | solr-16437-submit | 673ae584de03 | Bulk verdict Nearly. |
| SOLR-17976 | solr-17976-submit | 56ea43c448ed | Bulk verdict Nearly. |
| SOLR-8275 | solr-8275-submit | e52e10fa50a3 | Bulk verdict Nearly. Wording-level branch per the Linux-side record. |
| SOLR-3657 | solr-3657-submit | 14edaca577c0 | Bulk verdict Nearly. Dispositioned on the Linux side 2026-10-07 at this head (test-only commit); delta aspect covered in group C notes of the handoff document. |

## Group C: delta review only (19)

Already dispositioned and gated at the listed head on the Linux
side. The bulk review was written against an older snapshot for
most of them (for five, against the same head, after a
verification-only disposition). The ask is a fresh read at the
current head for anything the earlier passes missed. Patch only
if a real defect surfaces; otherwise a short review file
confirming the delta check is the whole deliverable.

| Ticket | Branch | Head | Note |
| --- | --- | --- | --- |
| SOLR-10322 | solr-10322-submit | 80ce9d7a3c8a | Dispositioned in the test-categories round at this head. Bulk review (older snapshot) said Needs work. |
| SOLR-12245 | solr-12245-submit | 4a93167458b5 | Round 33 verification disposition at this head; bulk review is against the same head. |
| SOLR-15863 | solr-15863-submit | 4bda993525ed | Test-categories round at this head; honestly labeled a pin on the Linux side. Bulk review was against an older snapshot. |
| SOLR-6045 | solr-6045-submit | dcdef50d7006 | Round 33 combined fix proposal exists on the Linux side and is an owner call (sequencing vs SOLR-12703). Review the branch; do not implement that proposal. |
| SOLR-7504 | solr-7504-submit | e3fdc8eb58f2 | Owner-locked design (BAD_REQUEST rejection) verified in round 33 at this head. Do not reverse the locked choice. |
| SOLR-12092 | solr-12092-submit | ca9573dabd38 | Re-dispositioned in round 33 at this head. |
| SOLR-14913 | solr-14913-submit | b80221f46d3c | Round 33 disposition at this head; the MatchNoDocs behavior choice awaits owner ratification. |
| SOLR-15003 | solr-15003-submit | afeab98ea82c | Fixed after a CI root-cause analysis at this head (regression in the branch, fixed and re-gated). |
| SOLR-2309 | solr-2309-submit | 06f5a1c4a87e | Round 32 eDisMax disposition at this head. |
| SOLR-2988 | solr-2988-submit | d2d144dfd9f4 | Round 32 eDisMax disposition at this head. |
| SOLR-3243 | solr-3243-submit | 1db99c13662d | Round 32 eDisMax disposition at this head. |
| SOLR-3729 | solr-3729-submit | 26258a2cbab0 | Round 32 eDisMax disposition at this head. Shares match-all grammar with SOLR-3962. |
| SOLR-3962 | solr-3962-submit | e7d5f3505035 | Round 32 eDisMax disposition at this head. Shares match-all grammar with SOLR-3729. |
| SOLR-4362 | solr-4362-submit | e96a057439c8 | Round 32 eDisMax disposition at this head. The SOLR-6320 demotion rule is an owner call at PR time. |
| SOLR-5065 | solr-5065-submit | c0a0ce1b8d9b | Round 33 disposition at this head. |
| SOLR-6009 | solr-6009-submit | a41bb034a1f4 | Round 32 eDisMax disposition at this head. |
| SOLR-6320 | solr-6320-submit | 2789d8020105 | Round 32 eDisMax disposition at this head. |
| SOLR-8939 | solr-8939-submit | a855a2d8965c | Test-categories round at this head. |
| SOLR-9148 | solr-9148-submit | 30f0d7a42d50 | Test-categories round at this head. |

## Not in this handoff

- SOLR-10789: already in the Linux pipeline (gate running).
- Round 35 wave 5 branches in flight on the Linux side: SOLR-17287,
  SOLR-17297, SOLR-17363, SOLR-17372, SOLR-17708, SOLR-17866.
- The five spot-check branches (SOLR-10492, SOLR-11364, SOLR-12007,
  SOLR-12543, SOLR-13943): a separate small wave on the Linux side.
- Windows gate batch 001 (SOLR-13705, SOLR-14171, SOLR-16499):
  already handed off for gating, not review.
- Held branches awaiting owner decisions (for example SOLR-2632):
  not review work.
