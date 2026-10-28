# Receipt: PR 5235 upload fix re-based onto the PR head and re-gated (phone agent, 2026-10-28)

Run by the phone agent at the owner's direction, after the owner noted that the round-1 upload gate (`receipts/PR-5235-upload-contentwriter-fix-round-1.md`, vm4, GREEN at `e2ad60e`) validated the patch on the fork branch only, never on the PR's current head, while the PR's CI is red.

## Heads

- PR head B: `ccace89730ae47cf94127643a39da6d57fe22e22` on `dsmiley/solr:SOLR-18351-backport-10x` (apache/solr PR 5235), verified live 2026-10-28.
- Fix commit: `e2ad60ecedda7451cb331538a5fd40b66144126b` on `nick-boss-tech/solr:solr-18351-submit` ("SOLR-18351: preserve content writer for upload files", one file, `ConfigSetAdminRequest.java`, +14/-4).
- Combined head F: `f890a62fc7956bd336850a187d9fc007f9f6321e`, the cherry-pick of `e2ad60e` onto B. Applies cleanly, no conflicts; the PR author's two adjustment commits (`16d9e2823daf`, `ccace89730ae`) touch other files. Pushed as `nick-boss-tech/solr:solr-18351-pr5235-upload-fix` at that head.

## Fail-before (at B, unpatched)

Upstream CI on `ccace89730ae`: check "Run SolrJ Tests" (job 122270614461) and "Run Solr Tests using Crave.io resources" (job 122270615366) both fail; "gradle check" passes. The only failing test in both job logs is `TestConfigSetAdminRequest.testUpload`: NullPointerException at `TestConfigSetAdminRequest.java:55`, because `ConfigSetAdminRequest$Upload.getContentWriter(String)` returns null after `setUploadFile`. 1361 tests completed, 4 failed (the one method's repeats), 45 skipped, in each job. Suite seed `747FA8C56DB2B91B`.

Root cause, from the PR's own history: only two commits in the PR range touch `ConfigSetAdminRequest.java`, `c2331291f7d` (the main SOLR-18351 commit) and `73fa87f1f41` (the author's back-compat restoration). `setUploadFile` routes through the restored deprecated `setUploadStream`, which sets only the `stream` field; the `getContentWriter` fallback through `super` returns null on that path. None of the contributor commits the author adopted touch this file.

## Legs at F (this VM, 2026-10-28, JDK 21 via the local gate toolchain, serialized under the test-queue lock)

Commands mirror the round-1 gate's required legs. Logs and XML under `~/workspace/tools/pr5235-regate/` on the phone agent's VM.

- `tidy`: rc 0, tree clean afterwards (before and after the test legs).
- testUpload, seed `747FA8C56DB2B91B` (the CI seed, ISO-8859-1, security manager): rc 0. Fresh XML: 1 test, 0 failures, 0 errors, 0 skipped.
- testUpload, seed `4478EC01207498A8` (UTF-8, security manager): rc 0. Fresh XML: 1 test, 0 failures, 0 errors, 0 skipped.
- Full `TestConfigSetAdminRequest`, seed `5235C0FFEE18351`: rc 0. Fresh XML: 5 tests, 0 failures, 0 errors, 0 skipped.

Local confirmation at B, same chain, seed `747FA8C56DB2B91B`: rc 1; fresh XML 1 test, 1 failure, the identical NullPointerException (`getContentWriter` returned null). The proof pair is complete at matched shape: the method fails at B and passes at F under the same seed and flags.

## Scope

- The patch content is identical to vm4's `e2ad60e` (same one-file diff); this run adds proof at the PR's actual head, which round 1 did not cover.
- Not run: the full `:solr:solrj:test` suite at F, and the Windows leg. CI's own count at B (1361 tests, only testUpload failing) bounds the regression to this method.
