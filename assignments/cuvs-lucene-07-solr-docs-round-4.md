# Assignment: reconcile SOLR-18556 documentation and status drafts, round 4

Status: OPEN

- Intended host: review/draft/records host; Windows is suitable. This assignment is drafts only.
- Capability tags: review, draft, records.
- Claim: `claims/cuvs-lucene-07-solr-docs-round-4-<host>.md`.
- Existing owner route: `assignments/solr-18556-closure-round-1.md`, `reports/solr-18556-round-1.md`, and `jira/SOLR-18556-comment-draft-round-2.md` where present.
- Context: `material/cuvs-lucene-next-steps-round-4.md` and both GREEN CPU receipts.

## Objective

Prepare accurate current documentation/status wording without duplicating another worker's Solr patch or prematurely changing behavior. Existing docs/guard decisions were explicitly separate from the upstream implementation. A fix is proposed upstream; that does not mean a compatible artifact is released or Solr is validated.

## Work

1. Read the live closure record, relevant claims/drafts, hydrated Jira context and current upstream Solr source through immutable refs. Check active Solr receipts/branches before proposing work. Do not read source/ as if its dirty working tree were current upstream. Cite actual current lines and revision for the affected doc sentences.
2. Inspect the known locations in solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc and solr/solr-ref-guide/modules/query-guide/pages/dense-vector-search.adoc. Verify whether the statements promising or timing a cuVS fix still exist. If already corrected, record the exact revision and avoid duplicate edits.
3. Prepare a small docs-only proposed diff against the selected Solr revision describing the known affected dependency combination and the verified upstream proposal. Use current primary evidence for PR/release state. Avoid promising December or any release date solely because old Jira text expected one. If upstream now released, distinguish release availability from validation of the Solr integration.
4. Prepare one replacement Jira draft under comment-formula.md with a named addressee verified from current context. Summarize CPU proof and its limits; do not claim that a GPU-skipped Solr test reproduced or fixed the ticket. Reconcile with the existing draft instead of queueing two competing posts. No live ticket update occurs here.
5. Reassess the old guard option from the upstream response: keep it deferred while the compatibility fix remains viable. Do not add broad NoSuchMethodError catching, fallback behavior or a new CuVSCodec policy. If the upstream route is abandoned, make a new evidence-backed owner decision rather than silently adopting the guard.
6. Record a concrete owner choice for any desired Solr docs implementation: exact diff, target branch, scope and competing active work. Coordinate through board records. Draft readiness is not authorization to edit/push a Solr branch or post Jira.

## Deliverables and acceptance

- `reports/solr-18556-docs-reconciliation-round-4.md` with current source/evidence, ownership, obsolete draft dispositions and the recommended route.
- `material/solr-18556-docs-round-4/proposed-docs.patch` if changes are still needed, explicitly a draft with base/digest.
- `jira/SOLR-18556-comment-draft-round-4.md`, marked DRAFT, or a documented reason no new comment is useful.

Accept when the wording is accurate for the current state, the existing route is reconciled without shared-file edits, the guard/pin remain separate, and Nick can review a concrete small proposal. If approved later, implementation goes through the normal ticket branch and any required Linux docs/formatting proof. Do not create a generic gate or use this draft report as execution proof.

## Limits

No Solr source edit, code guard, dependency pin, Gradle, Jira/GitHub post or changed shared draft/index. Public text has no em/en dashes, no internal process vocabulary and uses the required Jira footer. Do not claim Nick reviewed it until he has.
