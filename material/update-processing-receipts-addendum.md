# Receipts addendum: update processing final round

Added by the main side after the final-round report held two
branches for receipts that live in the main side's ledger and did
not reach the drafting workspace. Source for both entries:
`test-receipts-63-branches.md` (the main side's receipts ledger).

## SOLR-16673, head `d5c19e64ba1b`

Hardening receipt at this head: changelog parse clean, tidy rc=0,
Error Prone compile rc=0, pre-fix proof PASS, focused
`ParsingFieldUpdateProcessorsTest` 43 of 43, `:solr:core:check -x
test` rc=0. This releases the final-round hold on SOLR-16673: the
receipt names the head and gives the pre-fix result, as the report
asked.

## SOLR-16356, head `39c0585072f`

Hardening receipt at this head: tidy rc=0, Error Prone compile
rc=0, pre-fix proof PASS, focused `UpdateLogClosedCoreTest` 1 of 1
and `UpdateLogTest` 5 of 5, `:solr:core:check -x test` rc=0. The
draft already written for SOLR-16356 cites an earlier receipt at
`dcb16c775d62`; its Proof should be updated to cite this receipt at
the head instead.

## SOLR-5939 and SOLR-5754: merged-tree receipt confirmed

The final-round report held both branches because the merged tree
in the receipt (`1ddbf36202d`) is not in the drafting repository.
Verified on the main side 2026-10-08: a fresh merge of the current
heads (`f8d4bdbea518` and `7fbe0128d8b`) produces tree
`f2e33340f0ead8f3531e96e699df47c2ed69ce20`, and the gate's merged
commit `1ddbf36202d` has exactly that tree. The combined-gate
receipt (steps 0 to 4 rc=0, focused tests 26 of 26) therefore
covers the current head pair byte for byte. This releases the
tree-confirmation part of the hold; SOLR-5754 still needs its
root-level testing note removed before drafting.
