# Assignment: Jira housekeeping pipeline, round 1 (design and phase 0)

- Status: OPEN. Design posted by the phone agent at the owner's direction, 2026-10-24. Phase 0 is claimable under this file; phases 1 and later open only as the owner and the dev thread settle each wave.
- What this is: the factory-side pipeline for the Jira pruning campaign Jan Høydahl proposed on dev@ ("[DISCUSS] JIRA housekeeping, let's prune 3,000+ stale issues") and restated on 2026-10-24: where the project could use a software factory is triaging new and old Jira issues, and pruning becomes easier to accept if every open issue gets individual judgement first. This pipeline produces that judgement, per issue, with evidence. It closes nothing by itself.

## Inputs

1. The health report handed over by the owner (`assignments/attachments/report-1792861769937.html`, figures as of May 2026, to be refreshed in phase 0): 4,093 open issues; 3,454 (84%) not updated in 2+ years; 1,379 with zero comments; 3,432 unassigned; 2,869 with no fix version; about 660 still carrying EOL fix versions (6.0: 317, 4.9: 214, 7.0: 44, 5.5: 28, 5.0: 25, 5.2: 16, 6.2: 12, 6.7: 11). Types: Bug 1,567, Improvement 1,471, New Feature 432, Sub-task 245, Task 190, Test 121, Wish 67. Priorities: Major 2,821, Minor 1,098, Critical 87, Trivial 71, Blocker 16.
2. The report's three tiers. Tier 1 (est. 1,200): zero comments AND no update in 3+ years; ping the reporter, close after 30 days of silence. Tier 2 (est. 2,200): EOL fix version (8.x or older), or unassigned AND no fix version AND not updated in 2+ years; component leads review. Tier 3 (est. 700): updated in the last 2 years, or has votes or comments; keep and triage properly.
3. Positions stated on the dev thread, treated as constraints, not decoration:
   - Phase one excludes bugs (David Smiley, 2026-10-24, warming to +1 with that change).
   - Severity plays a role; Critical issues get more attention (Andrzej Białecki, 2026-10-24; there are 87 Critical and 16 Blocker open).
   - Closures need a resolution that makes "auto-closed for staleness" easy to detect later (Jason Gerlowski, +0, agreeing with David Smiley). The project picks the resolution value; the pipeline requires that one exists and records it on every judgement.
   - Individual judgement per issue is the acceptance condition for heavy pruning (Jan Høydahl). A 3+ year old feature request carries no weight by itself.
   - After the backlog is down, a nightly triage routine (categorize, label, ask the reporter for reproduction steps, identify duplicates) is the steady state Jan named. That is phase 4 here, not a starting point.
4. The skip-audit taxonomy (this factory's own audit of 750 skipped tickets, tally at goal files `skip-audit-tally-2026-10-17.md`). Per-issue judgement categories, overlapping by design: not a bug or by design; stale; feature, improvement, proposal, design-heavy, or umbrella; no comments; no reproduction ever provided; obsolete or removed on main; already fixed; not reproducible; awaiting the reporter. The 642 audited verdicts from that pile are seed evidence for any issue that appears in both sets, cited as prior judgement, never copied as a verdict without a fresh read.

## The judgement record (one per issue)

Stored as data, one folder per batch under `receipts/jira-housekeeping/<batch>/`, with a `manifest.md` per batch written for a committer to read:

- Issue key, type, priority, component, reporter, assignee, fix versions, created and last-updated dates, comment count, watcher and vote counts.
- Tier assignment under the report's rules, with the rule that fired.
- Category from the skip-audit taxonomy, each with its evidence: the code path checked on current main (file and line) for already-fixed and obsolete claims, the duplicate candidate with its key for duplicate claims, the last substantive comment quoted for staleness claims.
- Recommended action, one of: close as stale (after the ping window), close as already fixed (naming the fixing issue or commit), close as duplicate of a named issue, ping the reporter with a drafted question, keep (with the reason and the next triage step), or escalate for individual attention.
- Confidence and the checker state (below).

Judgements are provisional until audited, the skip-audit lesson: a second host re-judges a random sample of each batch (at least 10%), disagreements are listed in the manifest, and a batch with sample disagreement above 10% goes back for a full re-judgement before it is shown to anyone outside the factory.

## Hard exclusions (never auto-close candidates, whatever the tier says)

- Bugs, in phase 1 (Smiley's condition). Bugs enter only in a later phase the dev thread agrees to.
- Critical and Blocker priorities: pulled out for individual attention in every phase (Andrzej's condition).
- Issues with a patch attached, a linked or mentioned open PR, or a fix version in a live release line.
- Issues the day scan has flagged as freshly active (a watch-list committer commented within the current window).
- The owner's own reported tickets and any ticket with one of his open PRs; this pipeline does not judge the factory's own work.
- Sub-tasks whose parent is open and active; the parent's judgement governs.

## Phases

- Phase 0 (claimable now, records only): refresh the census from the Jira REST API with the day scan's method and pacing (anonymous reads, slow), reproduce the report's headline counts at the current date, assign every open issue a tier, and cross-reference the skip audit's 642 verdicts. Deliverable: `receipts/jira-housekeeping/census/` with the refreshed counts, the tier table, and the overlap list. Nothing is written to Jira.
- Phase 1 (opens on the owner's go, after the dev thread settles the resolution value and the ping text): Tier 1 non-bug issues only. For each, a judgement record and a drafted ping in Jira wiki markup under the comment formula (footer form). Pings are posted by a human hand the project designates; this pipeline does not post. Issues silent 30 days after their ping move to a closure manifest, in waves of at most 100, each wave a separate record a committer can execute and the list can see.
- Phase 2: Tier 2, with the severity screen applied first. Judgement records plus per-component summaries shaped for component leads, per the report. No closure wave leaves this phase without its own dev thread agreement.
- Phase 3: Tier 3 triage proper: component, label, and severity corrections and duplicate links, delivered as records for committers to apply. The factory proposes; it does not edit fields it has no rights to.
- Phase 4 (only if the project asks): the nightly triage routine as a standing store in the day scan's shape, one claim per day, covering newly filed and newly stale issues.

## Execution leg, stated plainly

The factory has no Jira rights to close, comment, or edit at scale, and this design does not pretend otherwise. The pipeline's product is judgement: cited, sampled, auditable records and ready-to-post texts. Execution belongs to the project's own actors (Jan as proposer, or any committer the thread designates), wave by wave, on manifests this pipeline publishes to the board and the owner carries to the thread. Any text posted under the owner's name needs his go per text, as usual. Nothing in any phase posts to Jira, GitHub, or the dev list by itself.

## Claims and staffing

- One claim per batch of at most 100 issues, claim file `claims/jira-housekeeping-<batch>.md`, tags `records` and `review`. One live batch per host; the census crawl obeys the day scan's one-scan-at-a-time rule.
- Judgement work follows premise scrutiny: a category claim stands only on the evidence the record cites, and "already fixed" or "obsolete" without a code path or a fixing reference is recorded as unverified, not as a verdict.
- Commit as Nick Shanin with no trailers. No em dashes in authored text.
