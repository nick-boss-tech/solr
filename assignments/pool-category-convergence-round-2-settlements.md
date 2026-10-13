# Pool assignment: category convergence round 2 settlements (build-docs, CLI, security, streaming, SolrJ)

Capability tags: `review`. No build work. Claim under `claims/` before starting, per WORKFLOW.md. This assignment may be claimed per category; each category produces its own response report.

## Standing patch-loop directive

Where a settlement calls for a patch or a wording change, apply it and keep reviewing the item until it settles. A findings-only round does not discharge a category. Wording settlements are applied to the drafts under `pr-drafts/` and, where the item owns a live PR body and its record authorizes body edits, to the live body, each edit verified by re-reading. Code items are delivered as patch files or as gate job files under `gates/` for a Linux host; do not push a submit branch from this assignment unless the item's own record authorizes the patch loop for that branch.

## Starting records

Per category, the round 1 application record and the receipts:

- `reports/build-docs-convergence-round-1-response.md`
- `reports/cli-convergence-round-1-response.md`
- `reports/security-convergence-round-1-response.md`
- `reports/streaming-convergence-round-1-response.md`
- `reports/solrj-convergence-round-1-response.md`
- `receipts/`, one file per ticket. A receipt at the exact live fork tip settles gate state for that ticket.

Convergence rule: start from the records, not from a fresh audit. A branch already gated at its live tip, or an item already decided in the round 1 response or a later receipt, is not re-audited. Several round 1 flags have moved since the responses were written (later gates, pushes and settlements are in the receipts and in the response reports' own summaries); settle each item from its current record, and where the current record already settles it, say so in one line and move on.

## Work, per category

1. List the category's flagged remainders from its round 1 response: every item that did not read CONVERGED, plus any item whose convergence was stated as conditional.
2. Work each item under the decision default and the three-rung ladder: missing information, get more information (a reading, a record check, or a gate job file that names the exact run owed); cost is effort, do the hard thing; optional, defer as a named follow-up in the draft's Limits or a Choice. Record which rung settled each item.
3. Apply the settlement: wording to the draft (and live body where authorized), code as a patch file or a gate job file. History rewrites are carved out of the patch loop wherever a category record says so; a rewrite item is recorded as an owner item with the reason, not performed.
4. Close the category with `reports/<category>-convergence-round-2-response.md`: one line per remainder ticket with its final state, CONVERGED or a named hold (owner item, owed gate, landing order, or follow-up), the rung that settled it, and the artifacts produced.

## Category notes from the records

- Build-docs: round 1 left SOLR-5821, SOLR-6430 and SOLR-17752 not converged (SOLR-6430's fragment drop has since gated green; settle from the current receipt). SOLR-17842's draft carries a WAITING ON OWNER CALL banner (published results versus instructions only); that call is the owner's, so the item closes as a named hold, not a settlement.
- CLI: round 1 converged eight drafts and flagged the rest; SOLR-18339 carries a secured-node Choice and an opening-timing call that is the owner's, and SOLR-17598 is NO GATE APPLIES with a deferred confirming run that its draft must keep visible. Named holds of that shape are correct closures.
- Security: SOLR-11678 has since converged (its round 1 gate is green and recorded); SOLR-12161 was not converged in round 1 and its packaged pin has since been pushed, so settle it from the current records. SOLR-10627's fragment title replacement is recorded verbatim in the round 1 response.
- Streaming: SOLR-12505 was the round 1 remainder; its gate has since recorded GREEN at `1a1875d95f` with the corrected proof leg, so settle it from the current receipt. ST-R1-27 stands rebutted in the round 1 response; do not reopen it.
- SolrJ: the largest remainder set. Work it in the response report's own order. Changelog title corrections named in the round 1 records for individual tickets are packaging items: record them as such, with the replacement title where the record gives one, rather than re-deriving them.

## Out of scope

Configsets (all four drafts converged in round 1, no round 2), schema, SolrCloud, core admin, replication and update processing (settled, or in their own loops and assignments).

## Deliverable

The five response reports, one per category, at `reports/build-docs-convergence-round-2-response.md`, `reports/cli-convergence-round-2-response.md`, `reports/security-convergence-round-2-response.md`, `reports/streaming-convergence-round-2-response.md` and `reports/solrj-convergence-round-2-response.md`. The assignment is discharged per category when every remainder from its round 1 response reads CONVERGED or named hold in its round 2 response.
