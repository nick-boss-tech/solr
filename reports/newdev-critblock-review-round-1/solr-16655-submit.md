# Review: SOLR-16655 (Indexing nested documents and URPs), branch solr-16655-submit (unit 13)

Reviewed by vm3 under `assignments/pool-newdev-critblock-review-round-1.md`, unit 13 (Critical). Claim: `claims/pool-newdev-critblock-review-round-1-solr-16655.md`.

## Pass 1

- Head reviewed: `5e2317443f41`. Unit commits: `0c527ea270` to `5e2317443f` (six). Base: `14c7aac0d1` (parent of the first unit commit). Diff: 4 files, 611 insertions, 25 deletions. Main code: `solr/core/.../update/processor/FieldMutatingUpdateProcessor.java` (67 insertions, 25 deletions). Tests: `FieldMutatingUpdateProcessorTest.java` (+487), `ParsingFieldUpdateProcessorsTest.java` (+50), changelog `SOLR-16655-field-mutating-urps-nested-docs.yml` (+7).
- Ticket: "Indexing nested documents and URPs", Open, Critical. Reported: custom URPs do not apply to child documents; Solr uses the built-in chain for children.
- Receipt on file: `receipts/SOLR-16655.md` (gate at 5e2317443f on 2026-10-09; pre-fix proof at b201a57fb3).

Checks:

1. The change does what the ticket asks: `processAdd` now calls `mutateDocument`, which applies the field mutators to the document and recurses into child documents (both child values under a selected field and `getChildDocuments()`). Rung 1 (code read).
2. An identity set guards against mutating one instance twice (`seen`); the comment says not every mutation is idempotent. Reasonable. No finding.
3. Field-level descent runs only when `selector.shouldMutate(fname)` is true; the commit message ("descend into child documents only under fields the selector selects") matches that path.
4. Finding for pass 2, minor-to-major, not yet settled: the anonymous child path (`getChildDocuments()`) is outside any selector check, so children are mutated even when the selector would not select any field that holds them. The ticket asks that child documents get the URP; whether anonymous children should follow the selector is an intent question. Rung 3 (the commit message says selector-scoped; the code does not do that for anonymous children). The pass-2 review reads the tests to decide; if the tests pin unconditional mutation of anonymous children, the finding is recorded as an intended behavior and cited.
5. No changelog problem found in the unit's own changelog file; the gate's step 0 checks it.
6. The tests (+487 lines) cover the nested and labelled child cases in the ticket; not yet read line by line (pass 2).

Repairs: none in pass 1 (none needed before the gate).

Regate: `gates/SOLR-16655-review-pass-1.md`.
