# Draft: PR 5107 round 3 follow-up comment (NOT POSTED)

Status: POSTED on 2026-11-02 at Nick's go as PR 5107 comment
6585431435 (https://github.com/apache/solr/pull/5107#issuecomment-6585431435),
authored nick-boss-tech, body read back byte-identical after posting.
Post-posting audit of the non-proof text (same day, at Nick's
direction): every claim verified against the code and the live PR,
with one correction edited into the posted comment: "adds one repair
commit on top" became "adds three commits on top: the repair itself,
the generated start-page synchronization, and the removal of the two
scoped changelog fragments," because three commits sit above the
integration merge. The body below is the pre-edit version; the live
comment carries that one sentence in its corrected form.

Prepared by the phone agent on 2026-11-02 after the
round 3 partial gate at F2 `7e531b083182366f8b6e7f00a3781ce3cbb713de`
(receipt `receipts/PR-5107-security-integration-round-3-partial.md`),
and revised the same day to head F3
`e75405eff845eda0e0f4f252d447347e2206acbe`, which removes the two
scoped changelog fragments at Nick's direction (the PR author owns
the changelog entry). Posting needs Nick's per-text go. Target: a
comment on apache/solr PR 5107 (Eric Pugh's draft, SOLR-8442).

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Prepared fixes for the November 2 review, integrated with your current head: [compare 7d193dac0f...F3](https://github.com/apache/solr/compare/7d193dac0f4abc441488f0ac2a9a734983d4fcd2...nick-boss-tech:solr-8442-security-review-r2-submit)

The branch merges your head `7d193dac0f` with the security-review fixes (both histories preserved, no rebase) and adds one repair commit on top. RunExampleTool already accepts `--credentials`; `bin/solr` rejected the option as unsupported, so the repair teaches the shell script to forward `--credentials` and `-u` to example starts as separate arguments, and to reject missing option values and credentials on a plain start before anything starts. The ZooKeeper client-address selection moved back above the logging guard so the log-call validator accepts it again. The secure-example BATS fixture now also covers the credential aliases, password rotation through the API, and a wrong-CA rejection followed by a correct-CA success. The generated CLI start page is synchronized at the head. No changelog fragment rides along; the PR's entry is yours to write.

<details>
<summary>Verification on Linux at e75405eff845eda0e0f4f252d447347e2206acbe</summary>

- This head differs from the gated head `7e531b083182366f8b6e7f00a3781ce3cbb713de` only by the removal of the two scoped changelog fragments; no code or test file changed, and tidy re-verified clean at this head. The runs below are from the gated head.
- `:solr:solr-ref-guide:generateCliDocs` leaves the tree clean; `:solr:core:compileTestJava` and `:solr:core:validateLogCalls` pass; tidy is clean.
- With the security manager: SecureExampleCredentialsTest 5 of 5, SecureExampleZkTest 3 of 3, CLIUtilsTest 10 of 10, AuthToolTest and AuthToolPicocliTest 3 of 3 each; SSLToolTest and IncludeFileEditorTest pass with their usual external-process skips under the manager.
- Without the security manager: SSLToolTest 9 of 9 with no skips, IncludeFileEditorTest 7 of 7, and both secure-setup example methods pass in each parser class.
- Fail-before: with only SSLTool.java and SecureExampleCredentials.java restored from your head, the three defect tests fail with their original signatures (client-auth settings not preserved; the six-character PEM password rejection; an unusable administrator accepted). The same tests pass at this head.
- Root `check -x test` stops at `:solr:webapp:verifyUiWasm` on a checksum mismatch for the prebuilt UI bundle. The same task fails at your head with the same expected checksum; the downloaded bytes differed between the two runs here, so this is not caused by the branch.

</details>

One limit: the packaging BATS suites (test_ssl and test_secure_example in both parser modes) have not been run on this combined head yet, so the end-to-end shell credential flow and the ZK TLS example are verified by the unit suites above, not by BATS. If the `No value present` startup failure from CI recurs there, the fixture now starts examples with `--verbose` so the stack is captured. Separately, per the guide, private trust anchors still need restoring after a truststore regeneration.

To take the branch onto your PR head (your head is an ancestor, so this fast-forwards):

```
git switch SOLR-8442
git fetch https://github.com/nick-boss-tech/solr.git solr-8442-security-review-r2-submit
git merge --ff-only e75405eff845eda0e0f4f252d447347e2206acbe
```

You're welcome to take the commits or adapt the changes.

*Nick Shanin directed the work and takes responsibility for this contribution.*
