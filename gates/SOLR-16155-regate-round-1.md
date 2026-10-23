# SOLR-16155 re-gate, round 1 (staged 2026-10-23 by phone-agent at the owner's direction)

PR #5200 is receiving a patch and will need a full re-gate at the patched
head. This job is staged ahead of the patch: claim it only when the fork
tip of solr-16155-submit differs from the baseline below.

- Branch: solr-16155-submit
- Baseline tip (pre-patch, the union head PR #5200 opened at): 9d1d0e5df9d
- Prior gate: receipts/SOLR-16155.md (green at 0881de1ed68b on 2026-10-04 by
  a hybrid local plus GitHub gate; the union head itself was audited at
  opening, not fully gated, and GitHub gates no longer exist, so this
  re-gate is local-only and is the first full gate of the union line)
- Base: origin/main at claim time; record the SHA gated against.
- Module: :solr:core
- Steps: changelog YAML parse for entries the branch adds; tidy (GATE
  INVALID if tidy modifies anything; a formatting-only tidy diff on this
  branch gets a Tidy commit and a retargeted rerun); Error Prone compile
  with -Pvalidation.errorprone=true; the focused tests below; module check.
- Focused tests: org.apache.solr.update.processor.JavabinLoaderTest,
  org.apache.solr.update.DocumentBuilderTest,
  org.apache.solr.update.processor.TolerantUpdateProcessorTest
- Seed: record the seed used.
- Proof leg: the message-content assertions tied to this change must fail
  on pre-fix production. The prior gate's pre-fix runs failed at the
  focused test step from the message-content assertions; re-derive the
  exact pre-fix position from the patch diff (the production the patch
  changes, at its parent) and record what was used.
- On green: update receipts/SOLR-16155.md with the new head, counts from
  fresh JUnit XML, and the PR #5200 body state; the receipt's final section
  is the PR draft if the body needs changes. Do not edit the PR body.
- On failure: record the failing step and the first failing test from the
  log and the fresh JUnit XML, and stop. A gate that executed tests and
  failed is not relaunched without investigation.
