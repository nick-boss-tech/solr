# Pool assignment: Configsets convergence, round 1

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-configsets-convergence-round-1.md.

## Purpose

The Configsets and config API round 1 audit is complete (`reports/configsets-round-1.md` with parts a to c), and the drafts in `pr-drafts/configsets/` are written. This round opens a convergence loop in the shape of the update processing loop: drafts are checked against the receipts and the recorded decisions until a full pass finds nothing new. The loop closes when a full pass over every in-scope draft yields no finding that is not already on the recorded register.

## Scope

- The 4 drafts in `pr-drafts/configsets/`: SOLR-6960, 7267, 15478, 17363.
- Decision record for this category: the per-ticket verdicts and owner decisions in `reports/configsets-round-1.md` and its part reports, the settlements in the draft fidelity round (`reports/draft-fidelity-configsets-query-schema.md` and `reports/draft-fidelity-fixes-application-response.md`), and the receipts in `receipts/`. No consolidated answers file exists for this category yet. Recorded decisions are not reopened, including items adopted under the owner's decision default. If a draft follows a recorded decision, do not flag it. If a draft contradicts one, flag it and quote the decision.
- Gate state is settled by the receipt in `receipts/` at the exact live tip; where a draft claim and a receipt differ, the receipt wins. SOLR-7323 was gated after the audit and has no draft in this folder; SOLR-13706 and SOLR-18178 are live pull requests; all three are out of scope.
- SOLR-15478 pairs with SOLR-15674 in SolrCloud, and the recorded landing order is 15674 first. The version-reset wording in the two drafts stays identical: check the 15478 draft's wording against `pr-drafts/solrcloud/SOLR-15674.md` and flag any divergence (the 15674 draft itself is out of scope for a verdict here), and check that the landing order the 15478 draft states matches the record.
- SOLR-7267 carries one recorded Choice (an additive alias for the configset command versus renaming the existing one). The Choice staying posed is not a finding; check the draft text against the record.
- SOLR-17363: a still-existing stale replica is not covered by the branch, and the scope call sits under the decision record. Check that the draft carries the uncovered case in Limits; the scope call itself is not a finding.
- SOLR-6960: round 1 held the draft on one branch fix (the changelog title overstates the coverage; built-in handlers are not covered). That fix is branch-side packaging: check the draft's own text against its Limits and receipt; do not flag the branch state itself.

## Method for every draft

1. Read the draft in full.
2. Resolve the head the draft names with `git ls-remote origin refs/heads/<branch>` and check it matches. Record any mismatch before judging claims.
3. Check every number in Proof and Limits (test counts, pass and fail counts, dates, seeds) against the receipt or the material the draft names, at the head it names.
4. Check every citation to code. A link to code the change produces must use the head SHA and land on the cited lines. A link to pre-change symptom code must use the merge-base and say so in the text. Read the lines at the SHA; do not trust the audit's line numbers.
5. Check the draft against `pr-formula.md`: AI header and footer where the draft carries them, a bold one-line summary opening each section, plain language, no internal process vocabulary, no unsupported precision. Titles must be accurate. If a draft names one Lucene version, it names the others.
6. Check the draft's Limits and follow-up statements against the decision record: an adopted item the draft omits, or a follow-up the record names that the draft does not carry, is a finding.
7. Grade each finding: blocking (the draft asserts something the evidence does not support, or a link is wrong in a way that misleads), should-fix (wording, citation form, or a missing Limit the record supports), or note.

## Findings register

Every finding gets an ID `CS-R1-nn` and a row: draft, grade, the exact text at issue, the evidence and the file and line it rests on, and the exact replacement text for blocking and should-fix findings. Later rounds reuse the IDs, so a finding is either new or carried.

## Deliverable

`reports/configsets-convergence-round-1.md`, with:

- Counts up front: drafts checked, drafts with no finding, drafts with blocking, should-fix and note findings only.
- A verdict per draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: NOT CONVERGED while any blocking or should-fix finding is open; CONVERGED only if this pass found none that are new.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies the replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
