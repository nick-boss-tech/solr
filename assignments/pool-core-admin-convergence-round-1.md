# Pool assignment: Core admin convergence, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 2, split by draft. Claim path: claims/pool-core-admin-convergence-round-1.md (slice-level claims by draft group are fine).

## Purpose

The Core admin round 1 audit and answers pass are complete (`reports/core-admin-round-1.md` with parts k1 to k6; `material/core-admin-round-1-answers.md`), and the drafts in `pr-drafts/core-admin/` are written. This round opens a convergence loop in the shape of the update processing loop: drafts are checked against the receipts and the recorded answers until a full pass finds nothing new. The loop closes when a full pass over every in-scope draft yields no finding that is not already on the recorded register.

## Scope

- The 21 drafts in `pr-drafts/core-admin/`: SOLR-4502, 4989, 5011, 6438, 8275, 8576, 9750, 11939, 12007, 12916, 13246, 15024, 15805, 16499, 16725, 16849, 17297, 17377, 17708, 17731, 18010.
- Settled decisions recorded in `material/core-admin-round-1-answers.md` are not reopened, including the DISCUSS items adopted under the owner's decision default. If a draft follows a recorded decision, do not flag it. If a draft contradicts one, flag it and quote the decision.
- Gate state is settled by the receipt in `receipts/` at the exact live tip; where a draft claim and a receipt differ, the receipt wins. Several branches the answers file lists as ungated were gated after that pass (among them SOLR-4502, 12916, 16499, 5262 and 8628 in their own categories); the receipt at the live tip governs, not the answers summary.
- SOLR-5011: the draft is on hold on the main side pending a narrowing (a shared-schema analysis found the branch's close behavior unsafe for a second live core sharing the schema path). The hold itself is not a finding. Check only that the draft's text matches the record, including the shared-schema scenario carried as a Limits item and the gate state its receipt states.

## Method for every draft

1. Read the draft in full.
2. Resolve the head the draft names with `git ls-remote origin refs/heads/<branch>` and check it matches. Record any mismatch before judging claims.
3. Check every number in Proof and Limits (test counts, pass and fail counts, dates, seeds) against the receipt or the material the draft names, at the head it names.
4. Check every citation to code. A link to code the change produces must use the head SHA and land on the cited lines. A link to pre-change symptom code must use the merge-base and say so in the text. Read the lines at the SHA; do not trust the audit's line numbers.
5. Check the draft against `pr-formula.md`: AI header and footer where the draft carries them, a bold one-line summary opening each section, plain language, no internal process vocabulary, no unsupported precision. Titles must be accurate. If a draft names one Lucene version, it names the others.
6. Check the draft's Limits and follow-up statements against the answers record: an adopted item the draft omits, or a follow-up the answers name that the draft does not carry, is a finding.
7. Grade each finding: blocking (the draft asserts something the evidence does not support, or a link is wrong in a way that misleads), should-fix (wording, citation form, or a missing Limit the record supports), or note.

## Findings register

Every finding gets an ID `CA-R1-nn` and a row: draft, grade, the exact text at issue, the evidence and the file and line it rests on, and the exact replacement text for blocking and should-fix findings. Later rounds reuse the IDs, so a finding is either new or carried.

## Deliverable

`reports/core-admin-convergence-round-1.md`, with:

- Counts up front: drafts checked, drafts with no finding, drafts with blocking, should-fix and note findings only.
- A verdict per draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: NOT CONVERGED while any blocking or should-fix finding is open; CONVERGED only if this pass found none that are new.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies the replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
