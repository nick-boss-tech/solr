# Gate PR 5107 service access, superseding job for head 5ff8f324ef3

Status: OPEN

- Supersedes the candidate `0c53fa4c6b36` for proof. Preflight tidy at `0c53fa4c6b36` changed three files (formatting only). The gate host applied that output as the repair commit.
- Proof head: `5ff8f324ef3c965fe2bbff9803b8990ab3d2945a` on `nick-boss-tech/solr:solr-8442-pr-5107-submit`, one commit above `0c53fa4c6b36`.
- Steps: tidy (must be clean), compile test sources, focused `AuthToolTest`, `IncludeFileEditorTest`, `SSLToolTest` with seed `8442C0DE`, `:solr:core:check -x test`.
- Not run here: `IncludeFileEditorTest.testDifferentOwnerIsPreserved` requires root in a disposable container. The host is not root; the record will say so.
- Claim: `claims/pr-5107-service-access-round-1.md`.
