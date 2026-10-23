# Review assignment: PR #5202 (SOLR-18446) review fixes and draft replies

Status: CLAIMED (claims/pool-windows-pr-5202-review-fixes-round-1.md)

Review only; do not push, post, or edit anything. Claim this assignment before starting, and write the verdict to `reports/pr-5202-review-fixes-round-1.md` when done.

## Code under review

Branch `solr-18446-review-fixes` on the fork (nick-boss-tech/solr), head `1c0a942ff8669aa1c4f4a1a4b06198eb66b7c58a`, based on the PR #5202 head `7e0251ee029125152c4d1791660189ef1e9dea06`. One commit: rename the protected field `origCorename` to `coreOrCollection` in `HttpSolrCall.java` and `V2HttpCall.java`, answering Eric Pugh's naming comment on the PR. Gate evidence (phone-agent, vm1, 2026-10-23): tidy clean, Error Prone compile pass, HttpSolrCallCloudTest 5 tests and AliasIntegrationTest 9 tests green at seed 18446, `:solr:core:check -x test` pass; log at `~/workspace/tools/gate-logs-2026-10-23/gate-5202-fixes.log` on vm1 if you have host access, otherwise take the diff on its own.

Check:
1. The rename is complete: no remaining `origCorename` reference anywhere in the tree, including tests, javadoc, and subclasses of `HttpSolrCall`.
2. The rename is purely mechanical: no behavior, visibility, or serialization change; the field's role (the path segment that names a core or a collection) is unchanged.
3. The new name reads correctly at every use site, including the two places that already derive a local `coreOrColName` from it.

## Text under review

Draft replies to Pugh's four review comments are reproduced verbatim in the appendix below. Pugh's comments, in order: (1) the `origCorename` name, suggesting `coreOrCollection`; (2) whether comma-delimited collections in the path are a supported concept; (3) whether the `collection` parameter is being dropped or deprecated, and whether the Ref Guide should still document it; (4) whether the major-changes entry means the parameter is no longer needed.

The PR's actual semantics, against which every reply must check: the URL path governs scope; the `collection` parameter no longer selects collections on its own and can only narrow the set named in the path; comma-delimited path segments and aliases remain supported.

Check each draft for factual accuracy against the PR diff and the ticket (SOLR-18446, including David Smiley's and Jan Høydahl's comments on it), for tone (brief, no internal process vocabulary, no "we" for the author plus agent), and for anything promised that the branch does not deliver.

## Deliverable

A verdict per item in `reports/pr-5202-review-fixes-round-1.md`: the rename (sound / findings), and each of the four replies (post as-is / edit, with the replacement text). List every factual error you find with the file or ticket text that proves it. If you find nothing, say so plainly.

## Appendix: the draft replies, verbatim

### 1. V2HttpCall.java: the `origCorename` name

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Renamed `origCorename` to `coreOrCollection`, the name you suggested, on a review branch (`solr-18446-review-fixes`) so it can be checked before it lands here. The field lives in `HttpSolrCall` and is inherited by `V2HttpCall`, so the rename covers both entry paths. No behavior change.

### 2. BasicDistributedZkTest.java: comma-delimited collections in the path

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Comma-delimited collections in the path stay, and after this change they are the way to name several collections in one request. That support is older than this PR; on the ticket, David describes adding it in the time-routed-alias context and argues for keeping it. This PR only removes the `collection` parameter's ability to override the path.

### 3. Ref Guide page: dropping or deprecating the `collection` parameter

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Neither dropped nor deprecated. The parameter no longer selects collections on its own; it can only narrow the set named in the path. The page now documents that narrower role, and the example is corrected to show narrowing: `/solr/collection1,collection2,collection3/select?collection=collection2`.

### 4. Major changes entry: is the parameter no longer needed

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

The parameter is still accepted, but it no longer routes by itself: the path governs scope, and the parameter only narrows within it. That is what the entry tries to say; if it still reads as a full removal, I can sharpen the wording.
