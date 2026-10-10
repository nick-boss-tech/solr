# Claim: metrics and monitoring round 1 (audit; no draft this round)

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` gained `assignments/metrics-round-1.md` in commit `5bc7acc7c1a`. The assignment covers one branch carrying one ticket, SOLR-17987, and asks for an audit report only (no draft this round).

## The live tip has moved: flagged, not silently adopted

The assignment names the tip `dba26c39877a6b7c94baf1cafee3a919c8b544fa`. The live tip is now `38abf6423126112cf8a451f4d3fedea0920eae90`, one commit on top: "SOLR-17987: remove handoff note and apply tidy formatting". The receipt, updated in commit `d5189425c7c` ("Receipt: SOLR-17987 gate green at its packaged head"), records that this new head is GATE GREEN ("first gate, finished 2026-10-10"; gate log `g17987-gate.log`; Error Prone compile and module check pass).

Consequences for this round:
- The audit runs against `38abf6423126`, the gated head. The assignment's premise questions still apply, and the new head is what the receipt describes.
- The assignment's starting state ("NO GATE", "its own SOLR-17987-TESTING.md", "four commits", "seven files") is out of date. The branch now has five commits over base (`715a4fd3108`, `ad6ae2b9439`, `e8c22910f50`, `dba26c39877`, `38abf642312`) and six changed files. The handoff note is removed, and the tidy commit is the only change since the assignment's tip.
- The convergence rule applies: a branch gated at its exact live tip is verified against its record and judged for readiness, not re-audited from scratch. The round still delivers the report alone, as the assignment says. A draft is the next step, after the answers pass.

## Heads checked live on 2026-10-10

| Ticket | Branch | Assignment's tip | Live tip | Result |
|---|---|---|---|---|
| 17987 | `solr-17987-submit` | `dba26c39877a` | `38abf6423126` | moved; flagged above; the receipt records a gate at the new head |

The live head was fetched explicitly (`git fetch origin refs/heads/solr-17987-submit:refs/remotes/origin/solr-17987-submit`).

## Staffing

Two subagents in parallel, split by question. The assignment expects a small team, and this is the minimum the scheduled rule allows.
- **M1:** the premise and mechanism questions (the assignment's items 1 to 5 and 7).
- **M2:** the consumers, the test, the tidy position, and the proof position (the assignment's items 6, 8, 9 and 10).

The lead writes the report, the answers to the handoff note's guesses, and the interaction and ownership notes.

## Shared rules for every part

- No builds, no Gradle, no tests, no gate runs. The gate evidence is the receipt's. Where the receipt and the code disagree, say so.
- No PRs, no comments, no edits to submit branches or live PR descriptions, no posting. No draft is written this round.
- Code claims cite file and line at the head `38abf6423126`, or at base `cabedd1d968` for the base state. Use `git show <head>:<path>`, `git diff`, and `git grep`. Do not check anything out.
- Read the Jira ticket from `C:\Users\shaninna\dev\Solr-issues\research\jira-context\SOLR-17987.json` if it exists (read only). Do not call JIRA.
- Plain words. No em dash and no en dash, anywhere, including the report.
- Each subagent returns its part report as text. The lead writes the files.

## Deliverables

1. `reports/metrics-round-1.md`: the audit verdict, the answers to the handoff note's guesses, the mechanism completeness, the consumer checks, the proof position, the moved-tip flag, the disagreements with the receipt, and a short owner list at the end.
2. The part reports, written by the lead to `reports/metrics-round-1-m1.md` and `-m2.md`.

## Not in scope

- SOLR-13265 (Update processing) and SOLR-18317 (Core admin) are metric-adjacent only and are not re-audited.
- Opening PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/metrics-round-1.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
