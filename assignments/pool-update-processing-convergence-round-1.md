# Pool assignment: update processing convergence, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 3, split by draft. Claim path: claims/pool-update-processing-convergence-round-1.md (slice-level claims by draft group are fine).

## Purpose

The update-processing last-review audits (`audits/update-processing/`, 29 files) and the drafts (`pr-drafts/update-processing/`, 28 files) are complete. Each review pass has still found new items. This round is the first pass of a convergence loop. The loop closes when a full pass over every in-scope draft yields no finding that is not already on the recorded register.

## Scope

- The 28 drafts in `pr-drafts/update-processing/`, read against the audits, receipts and material they cite.
- Out of scope for the verdict: SOLR-13696 (its gate r6 failed at `08f9384e47c`, and the draft is not to be drafted this round; read its draft only to confirm it does not claim a green gate). SOLR-18505 (closed in round 1; not touched).
- Settled decisions recorded in `material/update-processing-*.md` are not reopened. If a draft follows a recorded decision, do not flag it as a finding. If a draft contradicts one, flag it and quote the decision.

## Method for every draft

1. Read the draft in full.
2. Resolve the head the draft names with `git ls-remote origin refs/heads/<branch>` and check it matches. Record any mismatch before judging claims.
3. Check every number in Proof and Limits (test counts, pass and fail counts, dates, seeds) against the receipt, the addendum or the material that the draft names, at the head it names.
4. Check every citation to code. A link to code the change produces must use the head SHA and land on the cited lines. A link to pre-change symptom code must use the merge-base and say so in the text. Read the lines at the SHA; do not trust the audit's line numbers.
5. Check the draft against `pr-formula.md`: AI header and footer where the draft carries them, a bold one-line summary opening each section, plain language, no internal process vocabulary, no unsupported precision.
6. Check the draft's Limits and follow-up statements against the audit's findings: an open audit finding that the draft does not mention is a finding.
7. Grade each finding: blocking (the draft asserts something the evidence does not support, or a link is wrong in a way that misleads), should-fix (wording, citation form, or a missing Limit the audit records), or note.

## Findings register

Every finding gets an ID `UPC-R1-nn` and a row: draft, grade, the exact text at issue, the evidence and the file and line it rests on, and the exact replacement text for blocking and should-fix findings. Later rounds reuse the IDs, so a finding is either new or carried.

## Deliverable

`reports/update-processing-convergence-round-1.md`, with:

- Counts up front: drafts checked, drafts with no finding, drafts with blocking, should-fix and note findings only.
- The register.
- A convergence line: NOT CONVERGED while any blocking or should-fix finding is open; CONVERGED only if this pass found none that are new.
- A list of the drafts that changed since their last review, so the next round can re-check those first.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies the replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
