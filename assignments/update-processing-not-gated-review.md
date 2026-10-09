# Assignment: last-review audit of the ten ungated branches in Update processing and atomic updates

Revised 2026-10-08. This replaces the first version of this
assignment. The task is an audit, not a fresh review.

## Purpose

The inventory on this branch (`branch-focus-inventory-2026-10-08.md`)
lists ten branches in this category with no completed gate on the
current queue record. Before any of them enters a final round of
review, audit the last review or disposition of each branch:

- Is every finding in the last review addressed at the current
  head, still open, or overtaken by events?
- Does the evidence the last review cites still stand at the
  current head?
- Is the branch ready for the final round of review, and if not,
  what exactly blocks it?

## A correction the audit must not inherit

The inventory's state column is stale for five of the ten. Records
on the main side show SOLR-4841, SOLR-5505, SOLR-5887, SOLR-5939
and SOLR-5941 gated and pushed at exactly the inventory heads,
under round 28 and round 30 reports. Those reports are copied to
`material/last-reviews/` on this branch. Treat their claims as
claims: the audit verifies each one against the live branch
before relying on it. Where a claim does not hold, the audit file
says so plainly.

## Rules for this assignment

- Reading work only. No builds, no Gradle, no test runs, nothing
  executed. Gates run later on Linux or on the GitHub runner.
- Push nothing to any submit branch. All work product lands on
  this branch, `pr-prepare`.
- Claim first, per group. Group A is already claimed, at
  `claims/update-processing-not-gated-group-a.md`. Before starting
  Group B, commit `claims/update-processing-not-gated-group-b.md`
  with your name and the date. A rejected push means the group is
  already taken.
- Verify each branch's live head first. If it differs from the
  head the last review covered, the audit covers the delta and
  says which findings the movement affects.
- Tag every factual claim in an audit file as verified (seen in
  the diff, the ticket, or the cited record) or hypothesis.

## Deliverable, per branch

An audit file at `audits/update-processing/SOLR-<ticket>.md`
holding:

- the last review identified: source, date, head covered,
  verdict;
- its findings, one by one, each marked addressed, still open,
  or overtaken, with the evidence for the mark;
- the evidence status at the current head: a gate receipt stands
  at this head, stands at an older head only, or none exists;
- the readiness line: **Ready for final review**, or **Not
  ready** with the blocking items named.

For a branch certified ready that has no PR description draft
yet, also write one at
`pr-drafts/update-processing/SOLR-<ticket>.md`, following
`pr-formula.md` on this branch, with the reviewed head named at
the top. Proof counts come only from a gate receipt. The five
Group A reports under `material/last-reviews/` are receipts and
may be cited for counts, with their dates and heads. A branch
with no receipt gets "Awaiting gate" in its Proof section.

## Group A: audit the five branches whose last reports claim a completed gate at the current head

| Ticket | Branch | Head | Last review |
|---|---|---|---|
| SOLR-4841 | solr-4841-submit | f8850ffd421 | Round 28 pipeline report, `material/last-reviews/4841-round28-pipeline-report.md`. Verdict: PR-ready |
| SOLR-5505 | solr-5505-submit | 44c444aa5cd | Round 28 disposition, `material/last-reviews/5505-round28-disposition.md`. Verdict: DONE, PR-ready |
| SOLR-5887 | solr-5887-submit | c4c57ef7bcb | Round 28 disposition, `material/last-reviews/5887-round28-disposition.md`. Verdict: PR-ready, gated |
| SOLR-5939 | solr-5939-submit | 8f7a36fa6dd | Round 30 design report, `material/last-reviews/5939-round30-design-report.md`, with `material/last-reviews/5939-5941-design-assessment.md`. Recorded DONE, GATED, PUSHED |
| SOLR-5941 | solr-5941-submit | 62516cc338e | Round 30 design report, `material/last-reviews/5941-round30-design-report.md`, with the same design assessment. Recorded DONE, GATED, PUSHED |

Notes for Group A:

- SOLR-4841's gate ran on a tree that differs from the shipped
  head only by the dropped handoff document; the report claims
  the shipped code is byte-identical to the gated code. The audit
  confirms or refutes that claim.
- SOLR-5941's round 30 report already contains a complete draft
  PR description. Audit that draft against `pr-formula.md`
  instead of writing a new one.
- SOLR-5939 changes `SolrCmdDistributor` and
  `StreamingSolrClients`. SOLR-5754 in Group B changes the same
  two files. The audit names the overlap and says whether the
  two branches can proceed independently.

## Group B: audit the five branches with no completed gate at the current head

| Ticket | Branch | Head | Last record |
|---|---|---|---|
| SOLR-6973 | solr-6973-submit | 4c6092614e5 | Round 36 review on the `code-review` branch, `reviews/solr-6973-submit.md`. Verdict: Ready for review, read only |
| SOLR-12864 | solr-12864-submit | 8c5455d3d24 | Round 36 review on the `code-review` branch, `reviews/solr-12864-submit.md`. Verdict: Nearly |
| SOLR-11475 | solr-11475-submit | 42fb8817b25 | Premise audit, 2026-10-05: premise GROUNDED at this head. A gate is paused mid-run on the Linux side |
| SOLR-13696 | solr-13696-submit | 05ab4664dac | Held, banked untouched. A 2026-10-05 gate was not shipped; a 2026-10-06 note records its re-enabled tests failing on current main for schema and config drift |
| SOLR-5754 | solr-5754-submit | 36fe859d5fb | No prior review found in the main-side records or on the `code-review` branch |

Notes for Group B:

- SOLR-6973 and SOLR-12864 were reviewed read-only on
  2026-10-08 at the inventory heads. The audit checks that the
  heads have not moved since, and whether anything in those
  reviews is still open. SOLR-6973's branch still carries its
  `SOLR-6973-TESTING.md` handoff document; note what its removal
  at packaging time means for the reviewed tree.
- SOLR-12864's review found that the code does not reproduce
  the ticket's symptom on this base and that the test pins
  working behavior. The audit's question is whether the pin
  proceeds to a final round at all, and what it protects if it
  does.
- SOLR-11475: the paused gate is not evidence either way. The
  audit confirms the branch is unchanged since the premise
  audit and states what the final round needs (the gate
  resumed, or branch changes first).
- SOLR-13696: the audit is a disposition check only. Confirm
  whether the recorded hold reasons still hold. No readiness
  certification is available for it while the hold stands.
- SOLR-5754 has no last review to audit. Give it a first review
  in the audit format: premise, the branch's own diff, the
  test-oracle reading, the overlap with SOLR-5939, and a
  readiness call.
