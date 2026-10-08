# solr-11678-submit

- Branch: origin/solr-11678-submit
- Head: 55d8cd189d15 (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base cabedd1d968, 16 commits behind)
- Scope: 13 files, +76/-4. New credential type `SSL_KEY_MANAGER_PASSWORD` (sysprop `solr.jetty.keymanager.password`, env `SOLR_SSL_KEY_MANAGER_PASSWORD`) across `SSLCredentialProvider`, `AbstractSSLCredentialProvider`, `EnvSSLCredentialProvider`, `SSLConfigurations.getKeyManagerPassword()`; `server/etc/jetty-ssl.xml` sets `KeyManagerPassword`; `bin/solr` exports the env var; `solr.in.sh` / `solr.in.cmd` comment lines; three test files; changelog fragment; `SOLR-11678-TESTING.md` (author's unrun note, left in place).
- Verdict: Nearly (at 55d8cd189d15)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

MEDIUM (posed, not patched): `solr-ref-guide/.../enabling-ssl.adoc` was not updated. The author's note lists this as unchecked. AGENTS.md asks to consider a ref-guide update for user-facing changes. The new env var and sysprop are user-facing and are documented only in the `solr.in.*` comments. Wording and placement are the author's call, so this is posed rather than patched.

LOW (verified, OK): the `EnumMap` iteration order in `DEFAULT_CREDENTIAL_KEY_MAP` (`AbstractSSLCredentialProvider.java:32-39`) follows the enum ordinal. The new constant is last in both the enum and the map, so `EnvSSLCredentialProviderTest.testGetCredentials` expects `pw5` for it, matching the insertion order of the env map. The `testGetCredentialsWithEnvVars` and `SysPropSSLCredentialProviderTest` changes follow the file's existing style.

LOW (verified): no `switch` over `CredentialType` exists in `solr/core/src/java`, so adding the constant does not break an exhaustive switch.

LOW (style): `bin/solr` uses `${SOLR_SSL_KEY_MANAGER_PASSWORD:-}` in the new `if`, while the neighboring `SOLR_SSL_KEY_STORE_PASSWORD` line uses the bare `$VAR`. Harmless; the difference is only in the test expression.

LOW (not verified): `solr.cmd` is unchanged. The author's guess is that the env var is inherited by the Java process, and `EnvSSLCredentialProvider` reads the environment directly, so it should work on Windows as well. Not checked on Windows.

verified (checked against the code):
- `jetty-ssl.xml` gets `keyManagerPassword` from `SSLConfigurationsFactory.current`, the same pattern as `trustStorePassword`. A null value falls through to Jetty's default, which is the keystore password. That preserves today's behavior when the variable is unset (author's guess 1 by analogy; Jetty's own handling was not read).
- `SSLConfigurations.getKeyManagerPassword()` goes through `getPassword(...)`, the same path as the other getters, so the unset default is null (`SSLConfigurationsTest.testKeyManagerPasswordDefaultsToNull`).
- The changelog type `added` is valid, and the ICLA author and JIRA link are present.

## Not checked
- Nothing compiled, formatted, or run. No Gradle, no tests.
- Jetty's `SslContextFactory.setKeyManagerPassword(null)` behavior, and the `Ref`-to-null path in `jetty-ssl.xml`, were not read in the Jetty source.
- `solr.cmd` behavior on Windows (see LOW).
- `enabling-ssl.adoc` content (see MEDIUM).
