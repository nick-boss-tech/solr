# SOLR-11678 - hypothetical reproduction (NOT RUN)

Guessed, never compiled or executed. No Gradle was run.

JIRA: SSL fails when the keystore password and the key password differ. The old audit note called it "config/support", but
`jetty-ssl.xml` only ever sets `KeyStorePassword`; there is no way to set Jetty's `KeyManagerPassword`, so such a keystore cannot be used.

Change: new credential type `SSL_KEY_MANAGER_PASSWORD` (sysprop `solr.jetty.keymanager.password`, env `SOLR_SSL_KEY_MANAGER_PASSWORD`),
`SSLConfigurations.getKeyManagerPassword()`, `jetty-ssl.xml` sets `KeyManagerPassword`, `bin/solr` exports the env var, docs lines in
`solr.in.sh` / `solr.in.cmd`. Unset means null, i.e. Jetty falls back to the keystore password (today's behaviour).

Tests: `SSLConfigurationsTest` (property, env, null default); the two provider tests include the new enum value.

Guesses to verify first:
- Jetty XML `<Set name="KeyManagerPassword"><Ref .../></Set>` accepts a null referenced value (same pattern as trustStorePassword).
- Provider tests iterate `DEFAULT_CREDENTIAL_KEY_MAP` in enum order; the Env test supplies pw5 for the new constant last.
- Ref guide (`enabling-ssl.adoc`) not updated.
- `solr.cmd` needs nothing (env var inherited), not verified.
