# Owner complaints, 2026-10-19

From Nick Shanin, the owner. Read this before claiming work. These are
the things I am unhappy about after this morning's census of the branch
(tip e3c351ae136, 2026-10-19 09:07 MDT). Each complaint names what I
expect instead. This file stays until I replace it; work that answers a
complaint should say so in its closeout.

Owner correction, later on 2026-10-19: assignments on this branch are
addressed to a capability, not to a named host. Any available Linux
host claims Linux work and any available Windows host claims Windows
work, as the capability tags say. Where a complaint below names a
host, read it as the host that had the context or the file, not as
the only host allowed to act.

## 1. The cross-audit program has not started

The 21 pool-aab cross-audit batches cover 506 verdicts and have been
open since 2026-10-19. Delivered batch reports so far: zero. Audits
are the only thing that turns remediation work into cleared verdicts,
so a pool that remediates and never audits is running in place. Every
host takes aab batches, starting with the lowest unclaimed number.
A host that finishes a remediation batch takes an audit batch next.

## 2. Cleared is stuck at 15 while remediations pile up done

material/skip-audit/remediation-current.json says 233 items, 15
cleared, 218 open, and it has said that while batch 01 remediations
kept landing. When you re-audit another host's redo and it passes the
full rubric, set the item to cleared in that file in the same push.
When it fails, leave it open and append the gaps to
reports/skip-verdict-audit.md in the same push. A redo that nobody
re-audits is not finished work; it is inventory.

## 3. vm2 has not touched the CSV assignment

The CSV is believed to sit on vm2's storage, and as of this census
no host has claimed assignments/pool-post-issues-csv-vm2.md and no
CSV is on the branch. Whoever can reach the file claims the
assignment and posts the file as it says; if the file cannot be
found, the claim is marked BLOCKED with the search record: where was
looked, what was found. A silent assignment is the one unacceptable
outcome.

## 4. The remediation queue still carries a retired ownership note

The note at the head of remediation-current.json says the file is
owned by the vm1 honesty watch and that factory hosts do not edit it.
That role was retired on 2026-10-19 and pool/cron-prompt.md says the
pool maintains this file itself. The note is wrong and it is giving
hosts a reason to leave the file alone. The next host that touches
the file for any reason fixes the note to match cron-prompt.md.

## 5. The load is lopsided

This morning one host (vm4) produced nearly all of the closeouts,
items 17 to 24 of remediation batch 01 in about forty minutes, while
the audit batches sat untouched and vm2 sat idle. Speed on one batch
is not the goal; the queue drains when remediation, re-audit, and
clearing all move. Spread out: when every active host is on the same
batch type, the next claimant takes a different type.

## 6. SOLR-18507 phase 2 is parked with nobody investigating

gates/SOLR-18507-phase-2.md is OPEN after a round 4 gate failure: the
new fingerprint cache path reaches a closed IndexReader after a
delete. The record correctly says do not relaunch. It does not say
ignore it forever. A pool host with core experience claims the
investigation: read the branch, find why a closed reader is reached,
and either land a fix on the branch with a gate re-run or write the
diagnosis into the gate file. Parked is a state, not a resolution.

## 7. SOLR-18503 waits for a re-gate nobody has taken

Its gate file sits at GATE INVALID for tidy with the tidy fix already
landed at cc5b84dfcdd. The re-gate is ordinary pool work. Somebody
claim it and run it.

## What good looks like by the next census

- At least one aab batch report delivered, and the delivered count
  rising every day after.
- The cleared count in remediation-current.json moving on the same
  days remediations land.
- The CSV assignment claimed, done, or blocked with a record.
- No file on this branch describing a retired role as if it still
  runs the work.
