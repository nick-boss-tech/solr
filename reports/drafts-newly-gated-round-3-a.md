# Round 3 newly gated drafts, part A (SOLR-5011, SOLR-11678, SOLR-12161)

Drafts written (text only; nothing committed, pushed, posted or built):

- `pr-drafts/core-admin/SOLR-5011.md` at head `3fd85bedef64`
- `pr-drafts/security/SOLR-11678.md` at packaged head `611eae246453`
- `pr-drafts/security/SOLR-12161.md` at head `cdd265168a2a`

No build, Gradle run, test, test-queue command, Jira write, gh write or branch edit was made. The only git action was `git fetch origin solr-5011-submit` in the worktree, to read the SOLR-5011 head objects (the commit was not in the local object store). No file was changed by it.

Worktree HEAD at start: `d23dda9e89b`, clean.

## Head check (ls-remote origin, 2026-10-11)

- SOLR-5011: `3fd85bedef646b5b73c26e26a86a6130b5fc209f`. Matches the receipt head. Draft at this head.
- SOLR-11678: fork tip `55d8cd189d15367b6896f0ea07570d3395ce404b`, which matches the receipt's branch tip (receipt line 4). The gated packaged head `611eae2464536fdae61b8cae32abac97ee3a453b` is not in the worktree or source object stores (vm2 local). Delta: one packaging commit that removes `SOLR-11678-TESTING.md` (receipt line 5), not pushed. Draft at the packaged head as the assignment says. Code line ranges were read at the fork tip `55d8cd189d15`, which per the receipt differs only by that removal.
- SOLR-12161: fork tip `1725cbd8489889a1dad75279e4b2c11b0ec93d66`, which matches the receipt's branch tip (receipt line 4). The gated head `cdd265168a2adc76fb788f3d1f5587a3eee05e42` is vm2 local. Delta: one packaging commit that removes `SOLR-12161-TESTING.md` (receipt line 5). Line ranges were read at `1725cbd8`.

## Receipt numbers used

SOLR-5011 (`receipts/SOLR-5011.md`):
- line 3: gate green at `3fd85bed`; live tip verified 2026-10-11 UTC.
- line 4: checks run 2026-10-11 (runner done 2026-10-11). Used for "checked 2026-10-11" and the module check and Error Prone compile lines.
- line 5: CoreCloseResourceLoaderTest 1 test 0 failures; SolrCoreTest 9 tests 0 failures; TestCoreContainer 25 tests, 3 skipped, 0 failures.
- line 6: base check with only `SolrCore.java` reverted to `cabedd1d968`; CoreCloseResourceLoaderTest 1 test, 1 failure, AssertionError at line 49 (`assertTrue(oldLoader.isClosed())`); branch `SolrResourceLoader.java` kept.
- line 8: shared-schema finding (Limits).
- Not used in the draft: line 7 (test correction history, left out of the public text).

SOLR-11678 (`receipts/SOLR-11678.md`):
- line 3: proof leg inconclusive by compile; premise held.
- line 5: packaged head and the packaging delta.
- line 6: base `cabedd1d968`.
- line 9: premise run dated 2026-10-11 (vm2).
- lines 11 to 14: standalone Jetty 12.1.12 vehicle; without KeyManagerPassword "Cannot recover key"; with it, usable; limit on the environment path.
- lines 22 to 24: SSLConfigurationsTest 20 run, 0 failures, 0 errors, 0 skipped; EnvSSLCredentialProviderTest 2 run, 0 failures; SysPropSSLCredentialProviderTest 2 run, 0 failures.
- line 25: module check rc 0 (used as "module check passes").
- lines 30 to 31: base compile of core tests rc 1, 10 errors; inconclusive by compile.
- lines 35 to 36: unverified items (jetty-ssl.xml not run in a live server; null Ref in Jetty XML Set not verified; enabling-ssl.adoc not updated; solr.cmd not checked).
- Date gap: the receipt gives no date for the focused test runs (lines 21 to 24). The Proof gives the date only for the premise run. The focused runs have no date in the draft.

SOLR-12161 (`receipts/SOLR-12161.md`):
- line 3: gate green at packaged head; PIN, not a fix.
- line 4: branch tip `1725cbd8` verified 2026-10-10.
- line 5: gated head and packaging delta.
- lines 8 to 12: pin run 2026-10-11 UTC; head test 1 run, 0 failures, 0 errors, 0 skipped; base `cabedd1d968` with head test file 1 run, 0 failures, 0 errors, 0 skipped.
- line 13: failure surfaces as SolrException with code 401 at both trees.
- line 14: claimed path does not reproduce on base; test is a regression pin.
- line 18: no changelog fragment (test-only change). Draft says so.
- line 21: BasicAuthIntegrationTest at head `cdd265168a2a`: 1 run, 0 failures, 0 errors, 0 skipped.
- line 24: gate window 2026-10-11 05:49Z to 05:58Z.

## Titles

- SOLR-5011: changelog fragment at `3fd85bed` (`changelog/unreleased/SOLR-5011-close-core-resource-loader.yml`): "A SolrCore now closes its SolrResourceLoader when the core is closed (unload, reload, shutdown), releasing the class loader and the jars of the core's lib directories". The template has no title line, so this is used for the framing only.
- SOLR-11678: fragment at `55d8cd18`, unchanged by packaging: "Add SOLR_SSL_KEY_MANAGER_PASSWORD (solr.jetty.keymanager.password) so HTTPS works when the key password differs from the keystore password." Type `added`.
- SOLR-12161: no fragment (test-only). Suggested PR title from the commit subject: "Add a regression pin: an update without credentials through CloudSolrClient is refused under basic auth".

## Choice section decisions

- SOLR-5011: none. The open question is the shared-schema case. The assignment puts it in Limits, and the receipt says it was not decided (line 8). A choice section would ask maintainers to ratify a call the author has not made, and the narrowing alternative has no run behind it. If the shared-schema case is run and breaks, a choice section becomes owed.
- SOLR-11678: none. The server-only versus client scope is a narrow-scope call, which the formula sends to Limits. The Jetty property side effect is a behavior note, not a live alternative.
- SOLR-12161: none. Keep-the-pin-or-drop is the owner's call, not a design choice for maintainers. The pin framing comes from the receipt.

## Citations and checks

All citation line ranges were read with `git show <sha>:<path>` and numbered with awk.

SOLR-5011 (code produced at `3fd85bed`; symptom at merge-base `cabedd1d968`):
- SolrCore.java head 1892-1903: the added resource loader close block. Checked.
- SolrCore.java base 1889-1893: the close method end with no loader close. Checked at `cabedd1d968`.
- SolrResourceLoader.java head line 141 (closed field), 912-919 (close, isClosed). Checked.
- CoreCloseResourceLoaderTest.java head 36-56 (method); line 49 is the `assertTrue(oldLoader.isClosed())` line the receipt names. Checked.
- Changelog path: from the branch diff at `3fd85bed`. Checked.

SOLR-11678 (code produced at packaged head link; ranges read at `55d8cd18`; symptom at `cabedd1d968`):
- jetty-ssl.xml head 17-21: checked at `55d8cd18`.
- jetty-ssl.xml base 14-19: checked at `cabedd1d968`.
- bin/solr 229-231: checked at `55d8cd18`.
- solr.in.sh 164-165: checked at `55d8cd18`.
- solr.in.cmd 149-150: checked at `55d8cd18`.
- SSLConfigurations.java line 37 (constant), 92-98 (getKeyManagerPassword with javadoc): checked at `55d8cd18`.
- SSLConfigurationsTest.java 184-199 (three new tests): checked at `55d8cd18`.
- Changelog path: checked in the branch diff.

SOLR-12161 (ranges read at `1725cbd8`; link uses packaged head `cdd265168a2a`):
- BasicAuthIntegrationTest.java 248-260 (the new block, inside testBasicAuth): checked.

## Hold and flags

1. HOLD, SOLR-11678 (not written into the draft): the security audit (`reports/security-round-1.md`, "Blocking 2", and owner decision 4) says `SOLR_SSL_KEY_MANAGER_PASSWORD` needs an entry in `solr/solrj/src/resources/EnvToSyspropMappings.properties`. At `55d8cd18` that file has no such line (the SOLR_SSL_KEY_STORE_PASSWORD line is present, line 78), and no file in the branch diff touches it. The receipt does not mention this item. I did not verify the runtime effect the audit describes (the variable becoming system property `solr.ssl.key.manager.password`). The lead should decide whether to add the mapping line (which changes the head and needs a new check) or document it before the SOLR-11678 draft is used.
2. Links to unpushed heads: the SOLR-11678 and SOLR-12161 drafts link to packaged heads `611eae24` and `cdd265168a2a`, which are not on the fork. The links resolve only after the packaging commits are pushed. Line ranges were checked at the fork tips, which the receipts say differ only by the removed root file.
3. Keystore type: the receipt does not name the keystore type. The audit says the premise is JKS only, because PKCS12 cannot carry a different key password. The draft does not name a type. Add one only if the receipt is amended.
4. SOLR-12161 receipt wording: the TESTING note's "wrapped SolrServerException" does not occur for a 401 (audit item 3). The draft says SolrException only. The receipt's closed-path reason (isSolrThread) is wrong per the audit (`reports/security-round-1-s4.md`, item 1). The draft gives no reason, so it does not repeat that error.
5. Claim file `claims/pool-drafts-newly-gated-round-3.md` was not edited (the brief forbids it). The lead should mark NG3A done.

## Length and wording

Character counts (`wc -m`): SOLR-5011 3,788 with link targets, 2,854 without. SOLR-11678 4,717 with, 3,464 without (the longest; the scope and unverified items are the reason). SOLR-12161 2,497 with, 2,327 without. No em dash in any draft. Forbidden process terms (gate, receipt, ledger, seed, claim, pool, assignment, subagent, submission, rc=, JUnit, pre-fix, fresh) do not appear in the drafts.

## Not verified

- Focused test dates for SOLR-5011 and SOLR-11678 beyond the receipt dates above.
- The JVM and Jetty behavior claims in the SOLR-11678 Limits (read from classes in the audit, not run here).
- Any run of the code. Nothing was built or tested.
