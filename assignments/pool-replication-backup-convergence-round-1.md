# Pool assignment: Replication and backup convergence, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-replication-backup-convergence-round-1.md.

## Purpose

The Replication and backup round 1 audit and answers pass are complete (`reports/replication-backup-round-1.md` with parts g1 to g4; `material/replication-backup-round-1-answers.md`), and the drafts in `pr-drafts/replication-backup/` are written. This round opens a convergence loop in the shape of the update processing loop: drafts are checked against the receipts and the recorded answers until a full pass finds nothing new. The loop closes when a full pass over every in-scope draft yields no finding that is not already on the recorded register.

## Scope

- The 9 drafts in `pr-drafts/replication-backup/`: SOLR-8430, 9091, 9382, 9598, 9865, 11650, 12085, 12246, 17287.
- Settled decisions recorded in `material/replication-backup-round-1-answers.md` are not reopened, including the DISCUSS items adopted under the owner's decision default (9598 waits by default; the 8430 limiter is per rate value with a cap; 6711 persists its opt-in). If a draft follows a recorded decision, do not flag it. If a draft contradicts one, flag it and quote the decision.
- Gate state is settled by the receipt in `receipts/` at the exact live tip; where a draft claim and a receipt differ, the receipt wins. Some branches the answers file lists as ungated were gated or packaged after that pass (8430's cap work, 9091 and 9382 packaged heads among them); the receipt at the live tip governs.
- Landing orders recorded in the answers are consistency checks between drafts: RestoreCore cluster lands 9865, then 17287, then 9091; IndexFetcher cluster lands 12246, then 12085, then 11650, then 6711 (6711 has no draft in this directory; check only that no draft here contradicts its place in the order); 9598 lands before 12651 (12651's draft sits in SolrCloud; check only the 9598 side). A draft that states a different order, or text in one draft that contradicts another draft in its cluster (the 9865 and 18532 rollback overlap is recorded: 18532 lands first and 9865's branch drops its overlapping hunk at packaging), is a finding.
- The TestRestoreCore count question the answers record leaves open for 9865 and 17287 (receipt 4 of 4, head file 3 `@Test` methods, `[CONFIRM: count]` placeholders in the drafts): report the placeholder state as it stands; do not settle the count from the test file alone, and do not grade the recorded open item as a new finding.

## Method for every draft

1. Read the draft in full.
2. Resolve the head the draft names with `git ls-remote origin refs/heads/<branch>` and check it matches. Record any mismatch before judging claims.
3. Check every number in Proof and Limits (test counts, pass and fail counts, dates, seeds) against the receipt or the material the draft names, at the head it names.
4. Check every citation to code. A link to code the change produces must use the head SHA and land on the cited lines. A link to pre-change symptom code must use the merge-base and say so in the text. Read the lines at the SHA; do not trust the audit's line numbers.
5. Check the draft against `pr-formula.md`: AI header and footer where the draft carries them, a bold one-line summary opening each section, plain language, no internal process vocabulary, no unsupported precision. Titles must be accurate. If a draft names one Lucene version, it names the others.
6. Check the draft's Limits and follow-up statements against the answers record: an adopted item the draft omits, or a follow-up the answers name that the draft does not carry, is a finding.
7. Grade each finding: blocking (the draft asserts something the evidence does not support, or a link is wrong in a way that misleads), should-fix (wording, citation form, or a missing Limit the record supports), or note.

## Findings register

Every finding gets an ID `RB-R1-nn` and a row: draft, grade, the exact text at issue, the evidence and the file and line it rests on, and the exact replacement text for blocking and should-fix findings. Later rounds reuse the IDs, so a finding is either new or carried.

## Deliverable

`reports/replication-backup-convergence-round-1.md`, with:

- Counts up front: drafts checked, drafts with no finding, drafts with blocking, should-fix and note findings only.
- A verdict per draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: NOT CONVERGED while any blocking or should-fix finding is open; CONVERGED only if this pass found none that are new.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies the replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
