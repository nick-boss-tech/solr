# Update-29 reconciliation and consistency pass: answers from the main side

Main side, 2026-10-09. Answers to `reports/update-29-receipts-reconciliation.md` and the owner-decision list in `reports/update-29-consistency-pass.md`.

## The two confirmations the reconciliation asked for

- **SOLR-11483 gate: confirmed.** The receipts ledger records it DONE, GATED, PUSHED on 2026-10-06 (round 27 inventory queue), rebuilt and pushed to 4431a250f66, ls-remote verified at the time. The draft predates that record and should be corrected from the receipt.
- **The merged-tree hashes (5754, 5939): the receipts are right, and the drafts' hash is a different object for the same combination.** The gated tree 1ddbf36202d1df4ba99efe60995c5e01c76886a2 is a merge commit created in a main-side worktree for the combined re-gate (2026-10-08 23:33 MDT, log g5939-5754-combined-gate.log; it supersedes the 22:13 run at 9d3be2d914d). It resolves in the main side's repo and was never pushed, so it cannot resolve on the review side. The hash in the drafts is a trial-merge tree of the same two heads. No correction is owed on either side; the receipt files now note the tree is a local merge commit.

## Two listed decisions are already resolved on the record

- **SOLR-6065 status code (item 3.5): resolved.** The owner locked SERVER_ERROR 500 on 2026-10-06 (his standing disposition for this ticket, executed and gated 2026-10-09 at 3d2cec9e1ab3); the draft poses 400 in its Choices for maintainers. The decision is recorded in the takeover log and the receipts ledger; TESTING.md not carrying it is a record gap on the main side, not an open call.
- **SOLR-13696 batch status: confirmed in the batch.** The owner decided the CreateAlias fold-in route on 2026-10-09; the branch gated at r8 head da4fa6df117 and its draft was re-pointed to that head in the round 3 close-out. SOLR-13943's placement after it stands.

## The three real calls (recommendations recorded, not taken)

- **R1 (7504 against 12705, counted fields): recommendation, position (i).** The 7504 rule governs counted fields: a set operand is counted, and add, remove, inc, add-distinct and removeregex on a counted field are rejected with BAD_REQUEST. Rejecting an operation the counter cannot honor is the safer outcome; silently counting it as one value corrupts the count. Consequences: 7504 lands before 12705, the 12705 branch's pinned counting test is adjusted to the combined behavior, and the pair gets a combined-tree test run before 12705 opens.
- **R2 (7504 against 6045, plain value first): recommendation, position (i).** One rule in both places: a field mixing a plain value and an operation map is rejected, whichever comes first. Storing an operation map as a literal value is a silent corruption shape. Consequences: 6045's merger check widens to plain-first, gains tests for both orders (its draft records that none exist), and re-gates; the shared helper the drafts discuss then has one semantics.
- **R3 (14718 against 5939, failed-stream attribution): recommendation, position (ii).** 5939's per-request attribution is the substantive fix and is already gated in its combined tree. 14718 lands after 5939, and its Limits are rewritten to describe the post-5939 mechanism. No code change to 14718.

## Corrections that need no owner call

Accepted for the next drafting round: the SOLR-12705 audit note (R4); the stale 5939 and 7504 audit notes (A3, A4); the SOLR-12245 draft's missing Limits (F19: the MDC ask not addressed, offered as a follow-up, and the null-guard coverage named); and the Proof sections of the nine drafts the reconciliation lists as mismatched, corrected from the receipts (most predate their gates). The four moved tips (4841, 5754, 7022, 16673) get top-up verifications at openings time; their deltas are non-production (changelog, TESTING doc removal, title and comment wording).
