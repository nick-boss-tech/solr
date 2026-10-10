# Claim: admin UI round 1 (audit only; no ticket in this round is draftable on the recorded state)

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` gained `assignments/admin-ui-round-1.md` in commit `5c9b8de53ca`. The assignment covers three branches carrying three tickets, SOLR-9759, SOLR-9818 and SOLR-9831. All three are ungated fresh arrivals. Their own TESTING.md notes say the change and the test were guessed from reading main, and nothing was compiled or run. Each test is a Selenium test in `solr/webapp` that needs Chrome. The round is an audit pass only, and no draft is written unless a ticket is draftable on evidence the receipts support.

## The premise the round must state once

All three tickets were first flagged as "Admin UI angular, obsolete UI". The Angular UI under `solr/webapp/web` is still the shipped UI on current main, so that premise is false. Each ticket is judged against the UI as it ships today.

## Heads checked live on 2026-10-10

Fetched explicitly (`git fetch origin refs/heads/solr-<ticket>-submit:refs/remotes/origin/solr-<ticket>-submit`). All three match the assignment. The merge-base is `cabedd1d968` for all three.

| Ticket | Branch | Live head | Expected | Files | Result |
|---|---|---|---|---|---|
| 9759 | `solr-9759-submit` | `31e702622dae` | `31e702622dae` | 5 | matches |
| 9818 | `solr-9818-submit` | `63f2d7ce9267` | `63f2d7ce926` | 4 | matches |
| 9831 | `solr-9831-submit` | `f269a70e84f0` | `f269a70e84f` | 4 | matches |

## Staffing

Three subagents in parallel, one per ticket. This is within the cap of six at once, and no other round runs while this one does. The lead writes the roll-up, and the cross-ticket interaction check (the opt-out interaction between SOLR-9759 and SOLR-9818, and the shared test harness) is done by the lead from the three part reports.

- **U1:** SOLR-9759 (Stream screen POST and visible failures).
- **U2:** SOLR-9818 (the retry policy in the HTTP interceptor).
- **U3:** SOLR-9831 (the logging screen's level cell and the stray tag).

## Shared rules for every part

- No builds, no Gradle, no tests, no Selenium or Chrome runs. The evidence shape is the TESTING note's reading and the receipt's record. Nothing has run. Say so per ticket.
- No PRs, no comments, no edits to submit branches or live PR descriptions, no posting. No draft is written this round.
- Code claims cite file and line at the head SHA, or at base `cabedd1d968` for the base state. Use `git show <head>:<path>`, `git diff` and `git grep`. Do not check anything out.
- Read the Jira ticket from `C:\Users\shaninna\dev\Solr-issues\research\jira-context\SOLR-<ticket>.json` if it exists (read only). Do not call JIRA.
- Plain words. No em dash and no en dash, anywhere, including the report.
- Each subagent returns its part report as text. The lead writes the files.

## Deliverables

1. `reports/admin-ui-round-1.md`: the per-ticket verdict (premise holds on a reading, fails, or cannot be settled by a reading, with citations), the premise-run spec per ticket as main-side work owed, the disagreements with the receipts, and a short owner list at the end, including the SOLR-9818 retry-scope points with recommendations.
2. The part reports, written by the lead to `reports/admin-ui-round-1-u1.md` through `-u3.md`.
3. No drafts.

## Not in scope

- SOLR-15024 (Core admin; cross-filed) and SOLR-18317 (its Admin UI half has merged).
- Opening PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, and test runs.
