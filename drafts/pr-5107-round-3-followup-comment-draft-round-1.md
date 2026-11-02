# Draft: PR 5107 round 3 follow-up comment (POSTED, updated to F4)

Status: POSTED on 2026-11-02 as PR 5107 comment 6585431435 and edited
the same day, first to correct the commit count and the fragment
removal (F3), then to F4 `7aa3f77bed263728a13f361d9f371bf16937672c`
at Nick's direction (credentials-option declaration; report
`reports/pr-5107-credentials-option-fix-round-1.md`). The body below
is the live text, read back byte-identical after the F4 edit.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Prepared fixes for the November 2 review, integrated with your current head: [compare 7d193dac0f...F4](https://github.com/apache/solr/compare/7d193dac0f4abc441488f0ac2a9a734983d4fcd2...nick-boss-tech:solr-8442-security-review-r2-submit)

The branch merges your head `7d193dac0f` with the security-review fixes (both histories preserved, no rebase) and adds four commits on top: the repair itself, the generated start-page synchronization, the removal of the two scoped changelog fragments, and the credentials-option declaration described below. The credentials option needed work at both ends: `bin/solr` rejected it as unsupported, and RunExampleTool read the value without declaring the option, so both Java parsers rejected it as unknown. The branch teaches the shell script to forward `--credentials` and `-u` to example starts as separate arguments, declares the option in RunExampleTool for both parsers and wires the picocli value through, and rejects missing option values and credentials on a plain start before anything starts. The ZooKeeper client-address selection moved back above the logging guard so the log-call validator accepts it again. The secure-example BATS fixture now also covers the credential aliases, password rotation through the API, and a wrong-CA rejection followed by a correct-CA success. The generated CLI start page is synchronized at the head. No changelog fragment rides along; the PR's entry is yours to write.

<details>
<summary>Verification on Linux at 7aa3f77bed263728a13f361d9f371bf16937672c</summary>

- Above the gated head `7e531b083182366f8b6e7f00a3781ce3cbb713de`, this head adds the fragment removal described above and the credentials-option declaration (RunExampleTool plus its parse test). At this head the new parse test fails without the declaration and passes with it, the Error Prone compile and tidy pass, and on a rebuilt distribution `start -e secure-setup --credentials solr:SolrRocks` gets past argument parsing into the example wizard in both parser modes. Example starts cannot complete on that host for an unrelated environment reason: its Java localhost connections are intercepted, so the tool's port probe always reports the port as used. The runs below are from the gated head.
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
git merge --ff-only 7aa3f77bed263728a13f361d9f371bf16937672c
```

You're welcome to take the commits or adapt the changes.

*Nick Shanin directed the work and takes responsibility for this contribution.*
