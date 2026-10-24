# Assignment: harmonize the skip audit, the CSV triage, and branch state; draft the Jira comment families, round 1

Status: OPEN

## Why

Three stores describe the same backlog in three vocabularies, and they disagree in places that matter before any bulk Jira comment is drafted:

- The skip audit (old board, `material/skip-audit/` on `archive/coordination-2026-10-23`): 1,027 queued tickets; statuses in the 2026-10-07 snapshot are skip 750, implemented-hypothetical 208, implemented 69. The audited verdicts (642) carry rationale categories: not a bug / by design, stale, feature or design request, no comments, obsolete or removed on main, already fixed, no reproduction ever provided, awaiting reporter.
- The CSV triage (same archive, `material/jira-csv-triage/`): a metadata-only classification of the 4,078 open issues from the 2026-10-18 Jira CSV, paused 2026-10-20 at slice 258 of 2,039. 516 issues classified: stale 502, active 9, has-pr 5. Classes by rule: `has-pr` if PR Available is Yes; else `stale` if last updated before 2024-10-19; else `active`. A fourth class, `overlap`, was defined for tickets with prior factory work; no finished slice used it.
- Branch and PR state: the fork carries branches for about 1,100 ticket numbers, and this board's `receipts/` holds the gated results.

Measured overlap (owner side, 2026-10-24): 123 tickets sit in both the skip queue and the classified CSV set. The CSV calls 118 of them stale, while the skip queue holds 27 of the 123 as implemented-hypothetical and 4 carry a PR. A bulk comment written from the CSV class alone would land on tickets with live work. This assignment builds one joined store so that never happens, then drafts the comment families from it.

The store built here is the judgement input for `assignments/jira-housekeeping-pipeline-round-1.md` phase 0. It does not replace that pipeline's per-issue judgement; it gives it consistent inputs.

## Sources of record

- Skip audit: `queue-current.json` on the archive branch (latest), with `queue-snapshot-2026-10-07.json` as the frozen fallback; audited categories from the verdict records there.
- CSV triage: `material/jira-csv-triage/slice-*.csv` on the archive branch (258 files), plus the source metadata CSV `material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv`.
- Branch state: the fork's live refs at build time (`git ls-remote`), this board's `receipts/`, and apache/solr PR state via the API (read-only).
- Read the archive branch with `git show archive/coordination-2026-10-23:<path>` after a fetch, or from a checkout of that branch. Do not write to it; it is frozen.

## Phase H1: the harmonized store

The union set is every ticket in at least one source: the 1,027 skip-queue keys, the 516 CSV-classified keys, and any ticket with a fork branch or a receipt on this board that is in neither. The first H1 claim builds the exact union list in key order, publishes it as `material/harmonized-backlog/INDEX.md` with the count, and cuts it into batches of at most 100. Later claims take batches in order as `claims/harmonize-h1-<batch>.md`.

One record per ticket, written to `material/harmonized-backlog/batches/<batch>.json`:

- `key`
- `sources`: which of `skip`, `csv`, `branch`, `receipt` hold this ticket
- `csv_class`: stale / active / has-pr / overlap / null, with the slice file cited
- `skip_status`: skip / implemented-hypothetical / implemented / null, with the queue record cited
- `skip_category`: the audited rationale category where one exists, else null
- `branch_state`: none / draft-branch / gated-branch / pr-open / pr-merged, with the branch name, head, or PR number cited, verified live at build time
- `disposition`: exactly one canonical value from the crosswalk below
- `conflict`: true when the sources disagree in a way the crosswalk marks as a conflict, with `conflict_note` naming both values

### Crosswalk (precedence: branch_state, then skip, then csv)

- pr-open or pr-merged: disposition `implemented`. Any closure-family comment is barred.
- gated-branch or draft-branch, or skip_status implemented: `implemented`.
- skip_status implemented-hypothetical: `verify-first`. No comment family until a judgement record resolves it.
- skip verdict, category already fixed (with the fixing version or commit cited in the verdict): `closure-fixed`.
- skip verdict, category obsolete or removed on main: `closure-obsolete`.
- skip verdict, category not a bug / by design: `closure-not-a-bug`.
- skip verdict naming a duplicate target: `closure-duplicate`, target recorded.
- skip verdict, categories no reproduction, no comments, awaiting reporter, or stale with no code finding: `ping-stale`.
- skip verdict, category feature or design request or umbrella: `keep-open`. Out of bulk scope.
- No skip verdict; csv_class has-pr: `keep-open` (someone else's PR; out of scope).
- No skip verdict; csv_class stale: `unjudged-stale`. Feeds the housekeeping tiers; no comment draft until judged.
- No skip verdict; csv_class active: `keep-open`.
- csv_class overlap whose cited record is not found in the other sources: `conflict`.

### Conflicts (flag, never silently resolve)

- csv_class stale or active while branch_state is draft-branch or better.
- skip_status skip with a closure category while a PR by anyone is open for the ticket.
- skip_category already fixed while the ticket's Jira state at CSV time was open with no fix version; record both, flag it (the fix claim needs the version citation checked in phase H2 QA).
- Any ticket reported or assigned to the owner, or carrying the owner's PR: disposition gets `owner-excluded` appended in a separate field; closure and ping families skip it regardless of the crosswalk.

### H1 rules

- Read-only everywhere outside this board: Jira state comes from the CSV snapshot, not live calls; branch and PR state is read live and cited with the date.
- Records are built from the sources, not re-judged. Where a source is ambiguous, mark the field null and flag a conflict; do not investigate the ticket itself in this phase.
- QA: a second host re-derives one full batch (the batch after the builder's, modulo the batch count) from the sources and diffs every field. A disagreement rate above 5% returns the batch and its builder's other batches for rebuild. QA result lands as `reports/harmonize-h1-qa-<batch>.md`.

## Phase H2: comment family drafts

Starts only for dispositions whose H1 batches passed QA. One claim per family as `claims/harmonize-h2-<family>.md`; deliverable `drafts/jira-comments/<family>.md`.

Families: `closure-fixed`, `closure-obsolete`, `closure-not-a-bug`, `closure-duplicate`, `ping-stale`. `implemented` gets one `status-note` family in the staged-ask shape of comment-formula.md, for tickets where a PR or gated branch exists but the ticket has never been told.

Each family draft holds:

- One template in Jira wiki markup, written as the owner in first person, closing with the Jira footer from comment-formula.md (no header on Jira).
- Named evidence placeholders, one per fact the text asserts: for closure-fixed, the fixing version and the release or commit that carries it; for closure-duplicate, the surviving ticket; for ping-stale, the last activity date and what response would keep the ticket open. A filled comment may only assert what its store record cites; if the record lacks the citation, the ticket is not fillable and goes back as a conflict.
- The per-family fill rule: which store fields fill which placeholders, and the worked example of one fully filled comment for a real ticket in that family, with its record cited.
- Length: short. These are bulk comments; each reads as one short note, not a report.

Prior art: `material/skip-audit/jira-drafts-fixed-in-version-2026-10-18.md` on the archive branch drafted fixed-in-version texts; use it as input, and say in the family draft where this version differs and why.

## Hard rules

- Drafts only. Nothing is posted to Jira, GitHub, or the dev list in this assignment, and no text goes out under the owner's name. The owner approves each family text once; filled comments are a later phase under the housekeeping pipeline.
- Bugs are excluded from every closure family in this round, whatever the crosswalk says; a bug ticket in a closure disposition is flagged as a conflict for the pipeline's severity screen. Critical and Blocker tickets are excluded from all families the same way.
- No new Jira tickets, no PR openings, no branch pushes in this assignment. Store and draft files on this board only, add-only as usual.
- The backlog-closure sending question (whether, when, and in what waves) is under discussion separately and is not decided by anything here. This assignment prepares texts and the store; it does not schedule sends.

## Capability tags

`records`, `review`.

## Deliverables

- `material/harmonized-backlog/INDEX.md` and `material/harmonized-backlog/batches/<batch>.json` for every batch, with QA reports.
- `drafts/jira-comments/<family>.md` for the five closure and ping families and the one status-note family.
- A closing report `reports/harmonize-audits-round-1.md`: final counts per disposition, the conflict census with examples, and which families are ready for the owner's text approval.
