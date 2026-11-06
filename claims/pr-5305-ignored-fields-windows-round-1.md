# Claim: SOLR-18627 ignored-field sibling repair for PR 5305

Status: CLAIMED. Immutable implementation claim; completion will be an add-only report and Linux gate.

- Host: windows; tags: implementation, review, records.
- Started: 2026-11-06 16:47:57 UTC.
- Owner direction: "Build the patched branch and then hand it off to gate", following the deeper PR 5305 review.
- Board snapshot checked: 108151913871cf082c57ce84e5f22ee1763c827d. No matching assignment, claim, gate or receipt exists.
- Contributor parent O: 00292cd3729126194235e939afe38e351d850c70, verified against live apache/solr PR 5305 and fetched PR ref.
- Worktree: C:/Users/shaninna/dev/Solr-issues/wt/PR-5305-ignored-fields.
- Fork branch: pr-5305-solr-18627-ignored-fields on nick-boss-tech/solr only. Preserve contributor ancestry and add ordinary commits.
- Slice: null-safe no-field returns in BoolField, BinaryField, SortableTextField, CollationField, ICUCollationField, TrieField, and test-framework SortableBinaryField; focused regression coverage including docValues preservation; scoped changelog update retaining contributor attribution and adding Nick Shanin by name only.
- Decision ladder: rung 1, original SOLR-18164 commit 1af33ac6997adee643f8d24e3682b4dfe19e9c4c and current nullable creation paths establish the regression and existing empty-list contract. No public API change or DocumentBuilder field bypass is needed.
- Deliverables: pushed scoped source/test/changelog commit with separately verified remote SHA; reports/pr-5305-ignored-fields-implementation-round-1.md; gates/pr-5305-ignored-fields-round-1.md. Receipt reserved for the executing Linux host: receipts/PR-5305-ignored-fields-round-1.md.
- Windows runs no Gradle, compiler, formatter or tests. The gate will require exact-head Linux build/test/formatting/root-check proof and fail-before runs retaining the added tests over O production.
- Public PR body correction remains a local draft; this claim does not authorize editing the contributor PR or posting a comment. Status stays Implemented, Proof Pending until the gate receipt is complete.
