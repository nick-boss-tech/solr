# Pool assignment: CLI convergence, round 1

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-cli-convergence-round-1.md.

## Purpose

The CLI, bin scripts and packaging round 1 audit is complete (`reports/cli-bin-packaging-round-1.md` with parts s1 to s6), and the drafts in `pr-drafts/cli/` are written. This round opens a convergence loop in the shape of the update processing loop: drafts are checked against the receipts and the recorded decisions until a full pass finds nothing new. The loop closes when a full pass over every in-scope draft yields no finding that is not already on the recorded register.

## Scope

- The 9 drafts in `pr-drafts/cli/`: SOLR-9342, 10667, 12347, 16272, 16813, 17029, 17598, 18132, 18339.
- Decision record for this category: the per-ticket verdicts and owner decisions in `reports/cli-bin-packaging-round-1.md` and its part reports, the settlements in the draft fidelity round (`reports/draft-fidelity-cli-security-builddocs-metrics.md` and `reports/draft-fidelity-fixes-application-response.md`), and the receipts in `receipts/`. No consolidated answers file exists for this category yet. Recorded decisions are not reopened, including items adopted under the owner's decision default. If a draft follows a recorded decision, do not flag it. If a draft contradicts one, flag it and quote the decision.
- Gate state is settled by the receipt in `receipts/` at the exact live tip; where a draft claim and a receipt differ, the receipt wins. SOLR-10667 and SOLR-12347 were audit-only at round 1 and gated after it; their receipts govern.
- SOLR-17598: no gate applies by construction (the change is `solr.cmd` only), and its confirming Windows run is deferred under the owner's Windows rule. The absence of a gate is not a finding; check that the draft keeps that limit visible and claims nothing beyond it.
- SOLR-18339: the opening timing is owner-held, and the draft carries a secured-node Choice from the record. Neither is a finding; check the draft text against the record.
- SOLR-9342: its receipt was corrected in the fidelity pass (a Windows timezone sentence is no longer stated as fact there). Check the draft against the corrected receipt and the recorded settlements for this draft (one item softened to what the records verify, one deferred as a named Windows follow-up).

## Method for every draft

1. Read the draft in full.
2. Resolve the head the draft names with `git ls-remote origin refs/heads/<branch>` and check it matches. Record any mismatch before judging claims.
3. Check every number in Proof and Limits (test counts, pass and fail counts, dates, seeds) against the receipt or the material the draft names, at the head it names.
4. Check every citation to code. A link to code the change produces must use the head SHA and land on the cited lines. A link to pre-change symptom code must use the merge-base and say so in the text. Read the lines at the SHA; do not trust the audit's line numbers.
5. Check the draft against `pr-formula.md`: AI header and footer where the draft carries them, a bold one-line summary opening each section, plain language, no internal process vocabulary, no unsupported precision. Titles must be accurate. If a draft names one Lucene version, it names the others.
6. Check the draft's Limits and follow-up statements against the decision record: an adopted item the draft omits, or a follow-up the record names that the draft does not carry, is a finding.
7. Grade each finding: blocking (the draft asserts something the evidence does not support, or a link is wrong in a way that misleads), should-fix (wording, citation form, or a missing Limit the record supports), or note.

## Findings register

Every finding gets an ID `CLI-R1-nn` and a row: draft, grade, the exact text at issue, the evidence and the file and line it rests on, and the exact replacement text for blocking and should-fix findings. Later rounds reuse the IDs, so a finding is either new or carried.

## Deliverable

`reports/cli-convergence-round-1.md`, with:

- Counts up front: drafts checked, drafts with no finding, drafts with blocking, should-fix and note findings only.
- A verdict per draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: NOT CONVERGED while any blocking or should-fix finding is open; CONVERGED only if this pass found none that are new.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies the replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
