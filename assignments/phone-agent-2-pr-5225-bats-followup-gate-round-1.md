# Directed assignment: run the PR 5225 BATS follow-up gate (phone-agent-2)

Status: OPEN
Directed to: phone-agent-2, at Nick Shanin's direction on 2026-10-26. Other hosts leave this gate to her while this assignment is open.
Tags: gate, BATS, packaging

## Task

Claim and run the gate defined in `gates/pr-5225-bats-followup-round-1.md`, which is the authoritative definition and is not restated here. Candidate: fork branch `nick-boss-tech/solr:solr-5225-bats-followup` pinned at `35378004f1797c5b9ae3ba68d2503e4edb3840e8`. You hold both runtime JDKs this gate needs (21 and 25) and ran every leg recorded so far, which is why it comes to you.

## Already recorded at the pinned head (verify, do not redo without cause)

- Tidy preflight passed 2026-10-26T01:26Z; worktree clean.
- JDK 25 Commons CLI and Picocli legs passed; the gate accepts these as the completed JDK 25 portion (`reports/pr-5225-jdk25-regex-fix-final-head-follow-up.md`).
- JDK 21 Picocli leg passed at the pinned head (`reports/pr-5225-bats-followup-round-1-jdk21-picocli.md`), and a JDK 21 Commons CLI pass is recorded in `reports/pr-5225-bats-followup-round-1-evidence.md`. Confirm both records name the exact pinned SHA; rerun any leg whose record is at the parent head `096da6567e3db6d2bd141e3d2bf556ad1b3d2509` instead.

## Remaining work

1. Any runtime-matrix leg from the gate file that lacks a final-head record, run fresh at the pinned SHA with fresh JUnit XML and complete Solr logs preserved.
2. Final checks at the pinned head: `tidy`, `git diff --check`, and `:solr:packaging:check -x test`. Record `errorProneSkipped` accurately if it occurs.
3. The gate's three discriminating controls, in isolated scratch trees only, never pushed. Control 2 (pre-seeded matching log line plus the unrelated-error mutation) is the one the reconciliation flagged: this assertion searches the whole log with no checkpoint. If a control does not discriminate, keep the gate OPEN, report the evidence, and stop; do not change the candidate source under this gate.

## Deliverables

- Claim: `claims/pr-5225-bats-followup-round-1-phone-agent-2.md`
- Report: `reports/pr-5225-bats-followup-round-1-phone-agent-2.md`
- On a GREEN verdict only: receipt `receipts/pr-5225-bats-followup-round-1.md` with the final fork SHA and the full test matrix, per the gate's Completion section.

## Limits

No PR opening, no PR comment, no Jira write, no upstream write. Mutations stay in scratch trees. Follow the gate file wherever this assignment and the gate file differ.
