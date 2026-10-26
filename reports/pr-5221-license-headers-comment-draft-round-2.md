# PR 5221: revised PR comment draft, round 2 (not posted)

Status: DRAFT for Nick Shanin's review. Not posted. Replaces the pending-validation draft in `reports/pr-5221-license-headers-handoff-comment-draft-round-1.md` now that the Linux gate is green (`receipts/PR-5221-license-headers-round-1.md`). Read the live PR again before posting.

## Copy-ready draft for apache/solr PR 5221

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@jaykay12 I tried a follow-up on top of your current head, `e0453c0931ca`: [two-commit diff](https://github.com/nick-boss-tech/solr/compare/e0453c0931cab27fcb5c001f6bca3e412b3b1570...2855d3a04f29cc9121ceb677b9e495849cdae25a). It changes `gradle/license-headers.gradle`, `dev-docs/gradle-help/formatting.txt` and `dev-tools/scripts/test_license_headers.py`.

The header task now keeps existing leading notices (ASF, copyright, MIT, BSD, SPDX and generated-source comments) instead of adding a second header over them. It reads Git paths NUL-delimited, so Unicode and spaces in file names work. It falls back to the remote `main` when there is no local `main`, and in the root build it runs before the Java formatting tasks.

On Linux at `2855d3a04f`: all 12 functional tests pass. Three of them fail on your script at `e0453c0931` for the cases above: the Unicode path stays headerless, existing notices get an ASF header added, and the committed file is missed without a local `main`. `./gradlew check -x test` passes on the clean head.

Java in the separate `build-tools/` and `ui/` builds still needs its header added by hand.

Would these changes be useful to fold into this PR?
