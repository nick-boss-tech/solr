# Claim: replication answers draft pass, and core-admin receipt refresh for three tickets

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Scope, two parts:

1. **Replication answers draft pass.** `material/replication-backup-round-1-answers.md` (commit `6fd9e8f770a`), "Draft corrections owed", items 1 to 4. Apply items 2 to 4 to `pr-drafts/replication-backup/SOLR-9865.md` and `SOLR-17287.md`: bold openers on the Choice and Limits sections, and linked file citations at the head SHA. Item 1 stays owed (the main side confirms the count). Items 5 to 7 are notes, not edits; record their status in the report.
2. **Core-admin receipt refresh.** `receipts/SOLR-12849.md`, `SOLR-15003.md` and `SOLR-18010.md` (commit `fe1bdf120e0`) now record gate green at the live tip, and the answers file's entries for those tickets are out of date. Check each refreshed receipt against its branch at the live head, and give a verdict. 12849 is a live PR (consistency only). 15003 and 18010 were held with no draft; draft them only if the audit finds them draftable.

## Heads checked live on 2026-10-10

| Ticket | Branch | Live head | Receipt gated head | Result |
|---|---|---|---|---|
| 12849 | `solr-12849-submit` | `6b92223bc24f` | `6b92223bc24f` | matches; live PR #5011 |
| 15003 | `solr-15003-submit` | `1004abee39ab` | `1004abee39ab` | matches |
| 18010 | `solr-18010-submit` | `c3685bb37d9d` | `c3685bb37d9d` | matches |
| 9865 and 17287 | (replication drafts) | heads already in the drafts | n/a | drafts only |

## Shared rules for every part

- Edit only the named drafts, in place. Read only otherwise. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree` are fine.
- No builds, no Gradle, no tests. No `gh` write calls. Read-only `gh pr view` and `gh pr list` are allowed for part r2 through `C:\Users\shaninna\dev\Solr-issues\research\gh.ps1` (quote a JSON field list).
- Post nothing anywhere.
- Proof numbers come only from the receipt named for the ticket. Gate logs are often not on disk; say so.
- Drafts follow `pr-formula.md`. Public text carries no internal process vocabulary. Plain words. No em dash and no en dash.
- Every finding: file and line, evidence, and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so.

## Parts

**Part p1: replication draft pass (items 2 to 4).** Edit `pr-drafts/replication-backup/SOLR-9865.md` and `SOLR-17287.md` in place. Report each change (old line, new line), and the status of items 1, 5, 6 and 7 (not edited).

**Part r2: SOLR-12849 (live PR #5011), consistency only.** The refreshed receipt says gate green at `6b92223bc24f`, with counts 4 of 4 and 2 of 2 and a discriminating test. Check the receipt's claims against the branch at the head. Use read-only `gh pr view 5011 --repo apache/solr` with a JSON field list to check the PR head and the PR body's Proof counts against the receipt. Report drift only; draft no PR text.

**Part r3: SOLR-15003 and SOLR-18010.** Check each refreshed receipt against its branch at the live head. For 15003, the receipt says gate green at `1004abee39ab`, with a gate log `g15003r36fix-gate.log` and a fourth test that passed. For 18010, the receipt says gate green at `c3685bb37d9d`, with counts 3, 1 and 5, and a settling run. Give each a verdict (draftable, held, or audit only). Draft only if draftable, under `pr-drafts/core-admin/SOLR-15003.md` or `SOLR-18010.md`, naming the head in its Proof.

## Deliverables

1. `reports/answers-and-receipts-round-2-p1.md`, `-r2.md`, and `-r3.md`. The lead writes `reports/answers-and-receipts-round-2.md`.
2. Edits to `pr-drafts/replication-backup/SOLR-9865.md` and `SOLR-17287.md`; and any new draft under `pr-drafts/core-admin/` for 15003 or 18010 if draftable.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/answers-and-receipts-round-2.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
