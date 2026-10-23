# SOLR-16655 re-gate, round 1 (staged 2026-10-23 by phone-agent at the owner's direction)

PR #5093 is receiving a patch and will need a full re-gate at the patched
head. This job is staged ahead of the patch: claim it only when the fork
tip of solr-16655-submit differs from the baseline below.

- Branch: solr-16655-submit
- Baseline tip (pre-patch, gate green there): 5e2317443f41
- Prior gate: receipts/SOLR-16655.md (green 2026-10-09, re-verified at the
  live head 2026-10-15; focused FieldMutatingUpdateProcessorTest 36/36,
  ParsingFieldUpdateProcessorsTest 44/44, module check rc=0)
- Base: origin/main at claim time; record the SHA gated against.
- Module: :solr:core
- Steps: changelog YAML parse for entries the branch adds; tidy (GATE
  INVALID if tidy modifies anything; a formatting-only tidy diff on this
  branch gets a Tidy commit and a retargeted rerun); Error Prone compile
  with -Pvalidation.errorprone=true; the focused tests below; module check.
- Focused tests: org.apache.solr.update.processor.FieldMutatingUpdateProcessorTest,
  org.apache.solr.update.processor.ParsingFieldUpdateProcessorsTest
- Seed: 16655C0FFEE16655 (the prior gate's seed; a different seed is fine if
  recorded)
- Proof leg: the branch's regression test must fail on pre-fix production.
  Prior proof position: production at b201a57fb3e5 with the branch's
  FieldMutatingUpdateProcessorTest in place, 1 failed of 36
  (testTrimFieldsRecurseIntoNestedDocumentsOnlyWhenParentSelected). If the
  patch changes the test file or the guarded production, re-derive the
  proof position from the patch diff and record what was used.
- On green: update receipts/SOLR-16655.md with the new head, counts from
  fresh JUnit XML, and the PR #5093 body state; the receipt's final section
  is the PR draft if the body needs changes. Do not edit the PR body.
- On failure: record the failing step and the first failing test from the
  log and the fresh JUnit XML, and stop. A gate that executed tests and
  failed is not relaunched without investigation.
