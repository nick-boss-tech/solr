# Claim: CLI, bin scripts and packaging round 1 (audit, then drafts for the draftable tickets)

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` gained `assignments/cli-bin-packaging-round-1.md` in commit `bfc4d3a7026`. The assignment covers eleven branches carrying eleven tickets, with their receipts under `receipts/`. The round is one audit pass, the main side does one answers pass, and then drafts go to `pr-drafts/cli/`.

Scope: seven draftable tickets (9342, 16272, 16813, 17029, 17598, 18132 and 18339, with 17598 proven by construction only), and four audit-only tickets (7924, 10390, 10667 and 12347).

## Heads checked live on 2026-10-10

Fetched with `git ls-remote origin refs/heads/solr-<ticket>-submit`. Every head matches the head the assignment names, or, for the three tickets the assignment does not name, the receipt's recorded head.

| Ticket | Branch | Live head | Expected | Result |
|---|---|---|---|---|
| 7924 | `solr-7924-submit` | `96ef3a52bdbf` | `96ef3a52bdb` | matches; audit only (gate incomplete) |
| 9342 | `solr-9342-submit` | `833e11192a7f` | `833e11192a7` | matches |
| 10390 | `solr-10390-submit` | `4af4a6834e2b` | audit-only (receipt) | matches receipt |
| 10667 | `solr-10667-submit` | `32b594f280c5` | audit-only (receipt) | matches receipt |
| 12347 | `solr-12347-submit` | `b77acba2ad60` | audit-only (receipt) | matches receipt |
| 16272 | `solr-16272-submit` | `d2cf81739169` | `d2cf8173916` | matches |
| 16813 | `solr-16813-submit` | `1b288170e8aa` | `1b288170e8a` | matches |
| 17029 | `solr-17029-submit` | `4a98ef0a89d1` | `4a98ef0a89d` | matches |
| 17598 | `solr-17598-submit` | `8c91cf047a96` | `8c91cf047a9` | matches; construction proof |
| 18132 | `solr-18132-submit` | `54835cac6f85` | `54835cac6f8` | matches |
| 18339 | `solr-18339-submit` | `47e53884609c` | `47e53884609` | matches |

## Staffing

Six subagents in parallel, split by ticket cluster. The cap of six at once is met, and no other round runs while this one does. The lead writes the roll-up.

- **S1, start script and BATS:** 9342 (draft) and 7924 (audit only; the gate is incomplete, so the spinner premise and the AIX claims are read, not settled).
- **S2, package tooling:** 16272 (draft) and 16813 (draft). The install path and the load path, read as one story about the filestore.
- **S3, script parsing and Windows construction:** 17029 (draft, BATS evidence) and 17598 (draft, construction proof only).
- **S4, TLS and status wait:** 18132 (draft) and 18339 (draft, with the recorded Choice on the status wait's credentials).
- **S5, audit only:** 10390 (the lsof-free port probe) and 10667 (the LTR example directory in the assembled distribution).
- **S6, stop wait and the shared-file reconciliation:** 12347 (audit only, the stop wait default) and the cross-branch checks: the landing order for `solr/bin/solr` (seven branches), the `test_start_solr.bats` counts and names per branch, and the Windows parity matrix across the round.

## Shared rules for every part

- Read the receipt first. A receipt at the exact live tip settles gate state. Do not re-audit a settled branch's gate. The audit verifies the recorded state against the branch and judges PR readiness.
- No builds, no Gradle, no tests, no gate runs, no BATS runs. Gate and BATS evidence comes from the receipts. The receipts state the evidence shape: a BATS control run, a premise run, a construction proof, or NO GATE. The drafts must say the same.
- No PRs, no comments, no edits to submit branches or live PR descriptions, no posting.
- Code claims cite file and line at the head SHA. Use `git show <head>:<path>` and `git diff`. Do not check anything out.
- Read the Jira ticket from `C:\Users\shaninna\dev\Solr-issues\research\jira-context\SOLR-<ticket>.json` if it exists (read only; in the main checkout). Do not call JIRA.
- Drafts follow `pr-formula.md`. Each draft names its head in its Proof. Public text carries no internal process vocabulary (no gate, receipt, ledger, rc=0, "fresh JUnit XML", "pre-fix proof" as a label, "owed", "round"). Proof sections name the evidence shape: a BATS suite ran (with its counts as the receipt gives them), a construction proof (verified by construction, with the run still owed), or a partial proof (as the receipt records it). Titles must be accurate.
- Lucene versions: if a draft names one version, name every version that applies. Main and branch_10x pin Lucene 10.4.0; branch_9x pins 9.12.3.
- Plain words. No em dash and no en dash, anywhere, including the report.
- Each subagent returns its part report as text. The lead writes the files.

## Deliverables

1. `reports/cli-bin-packaging-round-1.md`: the per-ticket verdicts (draftable, audit-only outcome, or owner decision with options and a recommendation), disagreements with the receipts, the landing order for `solr/bin/solr`, the test-file reconciliation, the Windows parity matrix, and a short owner-decision list at the end.
2. Drafts in `pr-drafts/cli/` for the seven draftable tickets, each naming its head.
3. The part reports, written by the lead to `reports/cli-bin-packaging-round-1-s1.md` through `-s6.md`.

## Not in scope

- SOLR-11678 (filed under Security; its audit home is the Security round). Its `bin/solr` overlap is named from this round's side only.
- Opening PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, and test runs.
