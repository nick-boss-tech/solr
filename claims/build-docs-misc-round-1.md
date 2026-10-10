# Claim: build, docs and misc round 1 (audit, then drafts for the draftable tickets)

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` gained `assignments/build-docs-misc-round-1.md` in commit `0693fb6d72a`. This is the next unprocessed assignment after the admin UI round (`b4c06dfe53d9`). Its receipts are in the same commit.

The assignment covers twenty branches carrying nineteen tickets. The round is one audit pass, then drafts only for the tickets the audit finds draftable on the recorded state. Gate evidence comes from the main side's receipts. The review side runs no gate, no build, no Gradle and no test.

## Heads checked live on 2026-10-10

Read with `git ls-remote origin refs/heads/<branch>`, read-only. All twenty were read; the ones the assignment names all match.

| Ticket | Branch | Live head | Matches the assignment |
|---|---|---|---|
| 3684 | `solr-3684-submit` | `663b8ce754b6` | yes |
| 5821 | `solr-5821-submit` | `b8af2d1ce2f5` | yes |
| 6430 | `solr-6430-submit` | `22f83870c4a0` | not named |
| 7119 | `solr-7119-submit` | `9593f4bd0d63` | not named |
| 9039 | `solr-9039-submit` | `13faf68fe858` | not named |
| 11700 | `solr-11700-submit` | `c513388be053` | not named |
| 12743 | `solr-12743-submit` | `1bb4b227dfe5` | not named (retire candidate) |
| 13705 | `solr-13705-submit` | `5f141fb2af38` | not named |
| 16322 | `solr-16322-submit` | `65e0b8d7c792` | yes |
| 16914 | `solr-16914-submit` | `cd878023d3d0` | yes |
| 17252 | `solr-17252-submit` | `d730a266a042` | yes |
| 17356 | `solr-17356-submit` | `ea7fc15cade4` | not named |
| 17722 | `solr-17722-submit` | `fee3a26beb3c` | yes (the inventory's `e5c0a64f993` is SOLR-17752's head, not this one) |
| 17752 | `solr-17752-submit` | `e5c0a64f993c` | yes |
| 17825 | `solr-17825-submit` | `0ef08ec86c65` | not named (retire candidate) |
| 17842 | `solr-17842-submit` | `008973f63133` | yes |
| 18119 | `solr-18119-jvm` | `660faedd026d` | yes (live PR, consistency only) |
| 18119 | `solr-18119-submit` | `723022d35fce` | yes (superseded, retire candidate) |
| 18317 | `solr-18317-submit` | `fadbaee999f1` | yes (retire candidate) |
| 18523 | `solr-18523-submit` | `26678c3737ca` | yes (live PR, consistency only) |

The main side is `upstream/main` as resolved here, `3f5d4c5bf8ac`. The assignment does not pin a main SHA. Each part report states the main it read.

## Staffing

Six subagents in parallel, one per cluster, with the lead writing the roll-up. This is within the cap of six at once, and no other round runs while this one does. Rounds run one after another.

- **G1:** SOLR-3684 and SOLR-17752 (verified; drafts).
- **G2:** SOLR-5821 and SOLR-16914 (verified docs-only; drafts).
- **G3:** SOLR-17252 and SOLR-17842 (verified; drafts; 17842 waits on an owner call).
- **G4:** SOLR-6430, SOLR-7119, SOLR-11700 and SOLR-17356 (audit only, docs-shaped; no drafts).
- **G5:** SOLR-9039, SOLR-13705, SOLR-16322 and SOLR-17722 (audit only; no drafts; the SSL family and build tooling interactions).
- **G6:** SOLR-18119 (`solr-18119-jvm` consistency; `solr-18119-submit` retire), SOLR-18523 (consistency), SOLR-18317 (retire), SOLR-12743 and SOLR-17825 (retire confirmations).

## Shared rules for every part

- No builds, no Gradle, no tests, no compile, no spotless, no documentation build, no Chrome. Gate evidence is the receipt's record. Where a receipt says NO GATE or gated at an older head, say what is owed instead.
- No PRs, no comments, no edits to submit branches or live PR descriptions, no posting, no JIRA calls. No `gh` write calls.
- No checkout. Read with `git show <sha>:<path>`, `git diff <base>...<sha>` and `git grep <sha>`. Fetch each head explicitly (`git fetch origin refs/heads/<branch>:refs/remotes/origin/<branch>`) and check it against the table above before reading.
- Read the Jira ticket from `research/jira-context/SOLR-<ticket>.json` if it exists (read only).
- Drafts: a subagent returns its drafts as text inside its part report. The lead writes the files to `pr-drafts/build-docs/`. Each draft names the head it was written against and follows `pr-formula.md`.
- Public draft text carries no internal process vocabulary: no "gate", "receipt", "ledger", "rc=0", "fresh JUnit XML", or "pre-fix proof" as a label. A Proof section says what ran, at which head, and what passed, and says plainly when the proof is a documentation build, a light check or a harness run.
- Where a draft names a Lucene version, it names every version that applies: main and branch_10x pin Lucene 10.4.0, and branch_9x pins Lucene 9.12.3.
- Plain words. No em dash and no en dash, anywhere, including the report.
- Each subagent returns its part report as text. The lead writes the files.

## Deliverables

1. `reports/build-docs-misc-round-1.md`: the per-ticket verdict (draftable, consistency-only outcome for the two live PRs, retire confirmation or disagreement for the retire candidates, audit-only outcome for the unverified tickets, or owner decision with the options and a recommendation), disagreements with the receipts, and a short owner list at the end.
2. Drafts in `pr-drafts/build-docs/` for the draftable tickets, each naming its head.
3. Part reports, written by the lead to `reports/build-docs-misc-round-1-g1.md` through `-g6.md`.

## Not in scope

- SOLR-18317's banked server-side variant (Core admin round 1).
- The coordination branches `assignment-18523-doclint`, `review-18523-pr`, `assignment-pair-18119-18523` and `review-18119-jvm`.
- SOLR-18339's companion branch `solr-18339-ready` (CLI round).
- Opening PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/build-docs-misc-round-1.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
