# Claim: security and authentication round 1 (audit, then drafts for the draftable tickets)

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` gained `assignments/security-round-1.md` in commit `eb1c0ef578a`. The assignment covers four branches carrying four tickets, with their receipts under `receipts/`. The round is one audit pass, the main side does one answers pass, and then drafts go to `pr-drafts/security/`.

Scope: SOLR-10627 (draftable), SOLR-18368 (a docs-only draft that waits on an owner call about its key), SOLR-11678 (audit only), and SOLR-12161 (audit only, test-only, premise never run).

## Heads checked live on 2026-10-10

Fetched with `git ls-remote origin refs/heads/solr-<ticket>-submit`. The two heads the assignment names match. The two audit tickets are recorded in their receipts.

| Ticket | Branch | Live head | Expected | Result |
|---|---|---|---|---|
| 10627 | `solr-10627-submit` | `5a15dc0ba220` | `5a15dc0ba22` | matches |
| 11678 | `solr-11678-submit` | `55d8cd189d15` | audit-only (receipt) | matches receipt |
| 12161 | `solr-12161-submit` | `1725cbd84898` | audit-only (receipt) | matches receipt |
| 18368 | `solr-18368-submit` | `a7ec9a1b65c0` | `a7ec9a1b65c` | matches |

## Staffing

Four subagents in parallel, one per ticket. This is within the cap of six at once, and no other round runs while this one does. The lead writes the roll-up, and the cross-ticket interaction check is done by the lead from the four part reports.

- **S1:** SOLR-10627 (draft). Scope the claim to the edit-time rule, check the changelog title, and state the mirror-rule Limit.
- **S2:** SOLR-18368 (draft, waiting on the owner's key call). Verify the three corrected lines against current main and the built client classes.
- **S3:** SOLR-11678 (audit only). The key manager password path, and the four guesses in the TESTING note.
- **S4:** SOLR-12161 (audit only). The premise read: whether the scenario is unpinned, and what exception the scenario surfaces.

## Shared rules for every part

- Read the receipt first. A receipt at the exact live tip settles gate state. The audit verifies the recorded state against the branch and judges PR readiness.
- No builds, no Gradle, no tests, no gate runs. Gate evidence comes from the receipts. Where a receipt says NO GATE, or records a construction or compile verification, the draft says so and names what it was checked against.
- No PRs, no comments, no edits to submit branches or live PR descriptions, no posting.
- Code claims cite file and line at the head SHA. Use `git show <head>:<path>` and `git diff`. Do not check anything out.
- Read the Jira ticket from `C:\Users\shaninna\dev\Solr-issues\research\jira-context\SOLR-<ticket>.json` if it exists (read only; in the main checkout). Do not call JIRA.
- Drafts follow `pr-formula.md`. Each draft names its head in its Proof. Public text carries no internal process vocabulary (no gate, receipt, ledger, rc=0, "fresh JUnit XML", "pre-fix proof" as a label, "owed", "round"). Titles must be accurate.
- Lucene versions: if a draft names one version, name every version that applies. Main and branch_10x pin Lucene 10.4.0; branch_9x pins 9.12.3.
- Plain words. No em dash and no en dash, anywhere, including the report.
- Each subagent returns its part report as text. The lead writes the files.

## Deliverables

1. `reports/security-round-1.md`: the per-ticket verdicts (draftable, waiting on an owner call, or audit only), disagreements with the receipts, the cross-ticket interaction check (the security.json seam with SOLR-10627, and the basic-auth test family), and a short owner-decision list at the end.
2. Drafts in `pr-drafts/security/`: SOLR-10627 (draftable) and SOLR-18368 (marked as waiting on the owner's key call).
3. The part reports, written by the lead to `reports/security-round-1-s1.md` through `-s4.md`.

## Not in scope

- SOLR-18010, SOLR-13097, SOLR-17708 (Core admin round 1), SOLR-18132 (CLI round 1), SOLR-11650 (Replication and backup round 1), SOLR-10322 (Streaming expressions round 1), SOLR-9039 and SOLR-13705 (Build, docs and misc round, not yet assigned).
- Opening PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/security-round-1.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
