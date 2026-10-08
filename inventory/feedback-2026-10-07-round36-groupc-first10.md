# Feedback from the queue owner: round 36 Group C, first ten reviews

Date: 2026-10-07. For the reviewer of the Group C delta checks.
Overall: the ten reviews are accepted as the record for those
branches. Verdicts stand. The notes below are calibration, plus
the proof pointers the reviews could not see from the branch
trees.

## What worked, keep doing it

- The delta sections: comparing the bulk review's snapshot to the
  current head commit by commit is exactly the check this group
  exists for.
- Claims tagged verified or hypothesis, with file and line
  citations, and an explicit "Not checked" list per review. The
  hedges were accurate every time they were checked.
- Owner calls posed, not decided (10322, 14913, 12245), and the
  owner-locked design on 7504 checked as implemented rather than
  re-argued.
- Proposed changes marked "not applied". Correct for this channel.

## Calibration

- "Proof outstanding" (2309, 2988): the proof exists, but it lives
  in the queue owner's ledger, not in the branch tree. Phrase it
  as "proof not visible in the tree" and it will be supplied.
  Pointers for the ten reviewed branches:
  - 2309: full gate green (round 32, 2026-10-07); GitHub run
    37610684322 SUCCESS.
  - 2988: full gate green (round 32); GitHub run 37607125537
    SUCCESS.
  - 15003: the fix commit was gated (all steps rc=0) and GitHub
    run 37635192796 is SUCCESS at the current head.
  - 12092: gated in round 33 at the current head; six GitHub runs
    SUCCESS there.
  - 14913: gated in round 33; the premise run against the base is
    also the answer to finding 2 (next section).
  - 10322, 12245, 15863, 6045, 7504: gated at the listed heads in
    the test-categories and round 33 passes; per-branch reports
    sit in the queue owner's round folders.
- 14913, finding 2 is settled by a run on the Linux side: on the
  base, an all-invalid alias clause is silently dropped and the
  query returns documents (1 doc in the recorded run); the branch
  makes it 0. So the test comment is the accurate one and the
  changelog sentence ("fails the whole query") is the wrong one.
  The changelog will be corrected when the owner's ratification
  lands; no action for the reviewer.
- 15003, finding 3 is confirmed as scoped: none of the three new
  tests changes a config file, so none exercises the fetcher
  reload path; the regression evidence for the fix is the
  pre-existing stress test that caught the failure. The finding
  stands and goes back for a verify-and-fix pass on the Linux
  side, together with findings 1, 2 and 4. Same for 15863: the
  aggregation has no discriminating test; findings 1 to 4 go back
  for a verify-and-fix pass. Neither branch re-enters the queue
  until those passes finish.
- When a finding turns on platform behaviour (15003 finding 1),
  naming the platform the evidence comes from helps; the Windows
  angle is what made that finding sharp.

## For the remaining nine Group C reviews

Same shape. Where a verdict would otherwise be "proof
outstanding", write "proof not visible in the tree" and the
pointers will be added to this file or its successor.
