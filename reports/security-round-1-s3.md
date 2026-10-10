# Security and authentication round 1, part S3: SOLR-11678 (audit only)

## Verdict

- **Audit only, not draftable this round.** The premise is unverified (no run), and the head is not PR-ready as it stands.
- **Head verification:** `git ls-remote origin refs/heads/solr-11678-submit` returns `55d8cd189d15367b6896f0ea07570d3395ce404b`. That matches the claim table and the receipt. The local ref `refs/remotes/origin/solr-11678-submit` already points at it, so no fetch was needed. The tip has not moved.
- **Base and main:** `git merge-base upstream/main origin/solr-11678-submit` is `cabedd1d968`, the receipt's base. Forty-one commits separate that base from `upstream/main` `8e62c2686882`. None of the paths the branch touches changed between base and main, so the wiring citations below (on main) apply to the branch textually. The merge was not test-applied.
- On read, the server-side wiring is consistent across all six surfaces (item 5), and Jetty 12.1.12 accepts a null for the new key and falls back to the keystore password (item 3a). Whether the premise is real and the fix works is for the run in item 4.

**Blocking items for any draft or PR, from this side:**
1. `SOLR-11678-TESTING.md` is at the repository root and is one of the 13 files in the branch diff (tip commit `55d8cd189d1`, 18 insertions). Its text says "Guessed, never compiled or executed". It cannot ship in the PR.
2. `SOLR_SSL_KEY_MANAGER_PASSWORD` has no `solr/solrj/src/resources/EnvToSyspropMappings.properties` entry. All four existing password environment variables are mapped to nothing there. Without that entry, the new variable becomes system property `solr.ssl.key.manager.password` holding the secret (details in item 5).
3. The branch covers the server half only. The SolrJ client, the JDK default client keystore and the test framework have no key-manager-password path on main, and the branch adds none.
4. The premise is unrun. A valid run needs a JKS keystore and an explicit `SOLR_SSL_KEY_STORE_TYPE=JKS` (item 4).

**What verification needs (main side):** the item 4 premise run on packaged base and head; the focused tests (`SSLConfigurationsTest`, `EnvSSLCredentialProviderTest`, `SysPropSSLCredentialProviderTest`) through the queue, with the fail-before proof; and owner decisions 1 to 3 below before any draft.

## Items 1 and 2: wiring map

Main is `upstream/main` `8e62c2686882`. Head is `55d8cd189d15`.

| Place | Main | Head |
|---|---|---|
| Jetty XML, `solr/server/etc/jetty-ssl.xml` | Get `keyStorePassword` line 15, Set `KeyStorePassword` line 19; no key manager | Get `keyManagerPassword` line 17, Set `KeyManagerPassword` line 21 |
| Jetty module chain (`ssl.mod` loads `jetty-ssl.xml`; `ssl-reload.mod` with `jetty-ssl-context-reload.xml` lines 7 to 9) | The reload scanner attaches to the same `sslContextFactory`, so no separate password | Unchanged; reloads reuse the factory, so the key manager password applies (read) |
| `SSLConfigurations` system properties, `solr/core/.../util/configuration/SSLConfigurations.java` | Lines 34 to 39: four system properties, no key manager | Line 37 added |
| `SSLConfigurations` getter | `getKeyStorePassword` lines 85 to 89; `getClientKeyStorePassword` lines 103 to 107; no key manager getter | `getKeyManagerPassword` lines 96 to 98 |
| `SSLConfigurations.init()` | Lines 56 to 80 set only `javax.net.ssl.keyStorePassword` (and trust) | Unchanged |
| `SSLCredentialProvider.CredentialType` | Lines 22 to 27: four constants | Line 27 adds `SSL_KEY_MANAGER_PASSWORD` |
| `AbstractSSLCredentialProvider.DEFAULT_CREDENTIAL_KEY_MAP` (used by the system property provider) | Lines 31 to 37, four entries | Entry added (head line 37); import line 22 |
| `EnvSSLCredentialProvider` | EnvVars lines 32 to 39; map entries lines 51 to 54 | EnvVars constant line 40; map entry line 55 |
| `SysPropSSLCredentialProvider` | Lines 24 to 34; uses the shared map | Not edited; reaches the new type through the shared map |
| `class://` custom providers (`SSLCredentialProviderFactory.java`) | Lines 60 to 65 load arbitrary classes; none has key manager support | Not reachable by the branch; they receive the new type unchanged |
| Environment-to-system-property mapping, `EnvToSyspropMappings.properties` | Header line 5: "Map to nothing to avoid setting any system property". Lines 72, 75, 78 and 83 map `SOLR_SSL_CLIENT_KEY_STORE_PASSWORD`, `SOLR_SSL_CLIENT_TRUST_STORE_PASSWORD`, `SOLR_SSL_KEY_STORE_PASSWORD` and `SOLR_SSL_TRUST_STORE_PASSWORD` to empty | No entry added |
| `EnvUtils` environment conversion | `init` lines 206 to 217 turn any `SOLR_*` environment variable into a system property unless mapped empty; `envNameToSyspropName` lines 243 to 247; `setProperty` lines 198 to 201 | Not changed |
| Node property redaction, `NodeConfig.java` | Default hidden list lines 643 to 654 includes `.*password.*`; a custom list replaces the defaults (lines 858 to 874) | Not changed; redaction holds by default |
| `bin/solr` SSL block | Block from line 212; keystore password export lines 226 to 228; keystore type lines 229 to 231; trust password lines 236 to 238; client keystore password lines 257 to 259; client trust password lines 283 to 285 | Export block at lines 229 to 231 (229 `if`, 230 `export`, 231 `fi`) |
| `solr.in.sh` | Line 163, `#SOLR_SSL_KEY_STORE_PASSWORD=secret` | Lines 164 and 165 added (a comment, `#SOLR_SSL_KEY_MANAGER_PASSWORD=secret`) |
| `solr.in.cmd` | Line 148, `REM set SOLR_SSL_KEY_STORE_PASSWORD=secret` | Lines 149 and 150 added |
| `solr.cmd` | No password handling; line 43 `CALL`s `solr.in.cmd` | Unchanged |
| SolrJ server and client factories, `solr/solrj-jetty/.../SSLConfig.java` | `createContextFactory` lines 125 to 141 (keystore password line 133); `createClientContextFactory` lines 144 to 163 (lines 154 to 156); `configureSslFromSysProps` lines 166 to 184 (lines 172 to 174, `javax.net.ssl.keyStorePassword` only) | Unchanged |
| SolrJ default HTTP/2 client, `HttpJettySolrClient.java` | `getDefaultSslContextFactory` lines 1101 to 1125 (keystore password lines 1110 and 1111 only) | Unchanged |
| JDK default client keystore (`javax.net.ssl.*`) | JDK 21 `SSLContextImpl.java` line 976 default store password property; lines 1051 to 1053 pass that one password to `KeyManagerFactory.init` for the keys too | Not addressed; no property can carry a separate key password here |
| Test framework, `SSLTestConfig.java` | `TEST_PASSWORD` line 56 "secret"; `buildClientSSLContext` lines 140 to 166 (`kmf.init` line 157); `buildServerSSLConfig` lines 200 to 229 (`kmf.init` line 211, a programmatic `SslContextFactory.Server`, not `jetty-ssl.xml`); `create-keystores.sh` lines 31 and 35 use `-storepass` and `-keypass`, both "secret" | No key manager input added |
| Test server, `JettySolrRunner.java` | Line 224 builds the factory in Java through `SSLConfig.createContextFactory`; line 329 calls `SSLConfigurationsFactory.current().init()` ("normally happens in `jetty-ssl.xml`") | Unchanged |
| Reference guide, `enabling-ssl.adoc` | Line 44 keytool example with equal passwords (PKCS12); samples at lines 87 and 113; `javax.net.ssl` sample lines 327 and 330 | Not changed |
| Tests | `SSLConfigurationsTest` (system property, environment, null default), and both provider tests | Added (head lines 56 and 186 to 197; provider tests lines 40, 59, 42 and 64) |

**Item 2, providers.** The main chain default is `env;sysprop` (`SSLCredentialProviderFactory.java` line 35, overridable through `solr.ssl.credential.provider.chain` line 36). The built-in map is lines 38 to 41. Of the built-ins, the branch touches the environment provider and the shared abstract map (which the system property provider uses). It does not edit the system property provider. Custom `class://` providers are outside the branch.

## Item 3: the four guesses

**(a) Null accepted by Jetty. Read, not run.**
- Jetty XML layer (jetty-xml 12.1.12, `XmlConfiguration$JettyXmlConfiguration.refObj`, read from javap): the method throws only when the referenced value is null and the `Ref` element has child nodes. The branch's `<Ref refid="keyManagerPassword"/>` has no children, so it returns null. A mistyped id would also return null silently. That is a silent-failure risk, not a crash.
- Setter layer (`set()` in the same class): for a null value, the exact-class lookup falls through to a scan that skips primitive parameters. The check was read; not every branch was traced.
- SslContextFactory layer (jetty-util 12.1.12): `setKeyManagerPassword(null)` calls `getCredential("org.eclipse.jetty.ssl.keypassword")`, which reads that JVM property and is null when unset. `getKeyManagers` passes the key-manager credential if set, else the keystore credential, to `KeyManagerFactory.init`. So null means the keystore password, as the receipt says.
- Main already relies on the same route: `trustStorePassword` (`jetty-ssl.xml` lines 16 and 21) is null whenever neither the environment variable nor the system property is set. No packaging test was found that starts SSL with the trust store password unset. Every SSL start checked in `test_ssl.bats` sets `SOLR_SSL_TRUST_STORE_PASSWORD` (lines 49, 86, 148 and 202). So the null path is not directly tested in the repo.
- **Side effect the receipt omits:** main never calls `setKeyManagerPassword` (there is no caller in the `SslContextFactory` class), so the Jetty property `org.eclipse.jetty.ssl.keypassword` is inert for Solr today. After the branch, an unset environment variable makes Jetty read that property. It is low risk, but "unset keeps today's behavior" is true only with that qualifier.
- Not verifiable by reading: whether the server starts, and what the exact error text is. The item 4 run decides.

**(b) Enum-order assumption. Read: yes, in the environment test only.**
- `EnvSSLCredentialProviderTest.testGetCredentials` (head lines 35 to 48) assigns `pw1` to `pw5` in `Map.of` source order, then checks each `DEFAULT_CREDENTIAL_KEY_MAP` entry in `EnumMap` iteration order against `pw(index+1)`. It passes because source order equals ordinal order, with the new constant last in both the enum (line 27) and the `Map.of` (line 40). The same positional logic exists on main over four entries, so the branch extends an inherited assumption.
- `SysPropSSLCredentialProviderTest.testGetCredentials` (head lines 45 to 54) sets each key and checks it in the same loop. It does not depend on order.

**(c) Reference guide update. Read: not required for the code; optional for the docs.**
- The page shows a sample subset, not a complete variable list. It has no `SOLR_SSL_CLIENT_*` password variable either, so omitting the new one matches existing practice.
- Its keytool example (line 44) uses equal store and key passwords with PKCS12, which is exactly the case where the new variable is not needed.
- Optional wording: say the variable applies only to JKS keystores whose key password differs. The SolrJ example (lines 327 to 330) still uses `javax.net.ssl.keyStorePassword` alone, which the branch does not change.

**(d) `solr.cmd` needs nothing. Read from script structure; runtime not verifiable here.**
- Main's `solr.cmd` has no password handling at all. It `CALL`s `solr.in.cmd` (line 43), and it never exports `SOLR_SSL_KEY_STORE_PASSWORD` either. A `set` in `solr.in.cmd` reaches the Java process by inheritance, the same route the existing store password uses. So "`solr.cmd` needs nothing" is consistent with main. Windows runtime inheritance is not verified.

## Item 4: premise run (not run; main side; needs packaged base and head)

**Premise source:** the local JIRA export (`research/jira-context/SOLR-11678.json`, read only). The reporter says Solr "fails to read certificate from JKS file" with differing store and key passwords, on Solr 6.6.2. A 2017 comment relays the Jetty list: the exception is thrown while loading the keystore, so either the password is wrong or a key manager password was not set in the Jetty configuration. In 2019, Jan Høydahl asked for a step-by-step reproduction including keystore generation. The export has nine comments and no reply to that request.

**Keystore that carries the premise** (JKS; the key password differs):
```
keytool -genkeypair -storetype JKS -keystore <abs>/keypw-differs.jks -storepass storepw -keypass keypw -alias solrkp -keyalg RSA -keysize 2048 -validity 2 -dname "CN=localhost, OU=SolrTest, O=SolrTest, C=US" -ext "SAN=dns:localhost,ip:127.0.0.1"
```
Matched control keystore: the same, with `-keypass storepw` and file `keypw-matched.jks`.

**Common environment (both trees):**
```
SOLR_SSL_ENABLED=true
SOLR_SSL_KEY_STORE=<abs>/keypw-differs.jks
SOLR_SSL_KEY_STORE_TYPE=JKS
SOLR_SSL_KEY_STORE_PASSWORD=storepw
SOLR_SSL_TRUST_STORE=<abs>/keypw-differs.jks
SOLR_SSL_TRUST_STORE_TYPE=JKS
SOLR_SSL_TRUST_STORE_PASSWORD=storepw
SOLR_SSL_RELOAD_ENABLED=false
```
**Command shape:** `bin/solr start -p 8983` from the packaged distribution, then `curl -k -sS -o /dev/null -w "%{http_code}" https://localhost:8983/solr/admin/info/system`, then `bin/solr stop -p 8983`. Read startup errors from `server/logs/solr-8983-console.log`.

**Runs:**
- R1, base, differs, no key manager variable.
- R2, base, matched control.
- R3, base, differs, `SOLR_SSL_KEY_MANAGER_PASSWORD=keypw` exported (base has no wiring).
- R4, head, differs, no variable (the fallback check).
- R5, head, differs, `SOLR_SSL_KEY_MANAGER_PASSWORD=keypw` (the environment path).
- R6, head, differs, no environment variable, `SOLR_OPTS="-Dsolr.jetty.keymanager.password=keypw"` (the system property path).
- R7, head, matched control (regression).
- Optional R8: as R5 with `SOLR_SSL_RELOAD_ENABLED=true`, to exercise the keystore scanner path.

**Outcomes:**
- R1 fails with a key-recovery error (for example `UnrecoverableKeyException` or "Cannot recover key"): the premise is reproduced. If R1 starts, the premise is not reproduced for this JDK and keystore. Do not draft.
- R2 must start with a 200. If not, the harness is wrong (type, path or trust store), and the run proves nothing.
- R3 fails: expected, and it confirms that base ignores the variable. If it starts, something else reads the variable; investigate.
- R4 fails with the same error as R1: the fallback is unchanged. A different error is a regression.
- R5 starts with a 200: the environment path and the XML path are both wired, and the premise is fixed. A key-recovery failure means the XML or the environment provider is not wired (check the `EnvVars` map entry and the `bin/solr` export). Another error needs a separate look.
- R6 starts with a 200: the system property path is wired. A failure points at the shared map entry in `AbstractSSLCredentialProvider`.
- R7 starts: no regression on the matched path.

**Pitfalls that would make a false result:**
- Omitting `SOLR_SSL_KEY_STORE_TYPE=JKS`. `jetty-ssl.xml` line 24 defaults the keystore type to PKCS12, so the failure would be a format error, not a key password error.
- Omitting `SOLR_SSL_TRUST_STORE_TYPE=JKS` when the trust store is the same JKS file (`jetty-ssl.xml` line 25 defaults to PKCS12).
- A PKCS12 keystore cannot carry a different key password: the JDK 21 `keytool` source ignores `-keypass` for PKCS12 (`sun/security/tools/keytool/Main.java` lines 2374 to 2379). So the premise is JKS-only, and the ticket and docs should say so.

## Items 5 and 6

**Item 5, consistency.** The name `SOLR_SSL_KEY_MANAGER_PASSWORD` is spelled the same in `bin/solr` (lines 229 and 230), `solr.in.sh` (line 165), `solr.in.cmd` (line 150) and `EnvVars` (head line 40). The system property `solr.jetty.keymanager.password` is the same in `SSLConfigurations` line 37 and the shared map line 37. The XML id `keyManagerPassword` matches in `jetty-ssl.xml` lines 17 and 21, and the getter `getKeyManagerPassword` (line 96) follows the Get bean-name rule. The `bin/solr` export is environment only, which matches the other passwords (no `-D` on the command line).

Differences:
- `bin/solr` uses `${SOLR_SSL_KEY_MANAGER_PASSWORD:-}` where its neighbors use bare `$VAR`. Harmless and cosmetic.
- **Real gap: the `EnvToSyspropMappings` entry.** Main's convention (header line 5; lines 72, 75, 78 and 83) maps every password environment variable to nothing, and `EnvUtilsTest.testNotMapped` (line 97) asserts that the store-password system property is absent. The branch omits the entry, so `solr.ssl.key.manager.password` exists as a system property with the secret. Default redaction (`.*password.*`) hides it from the node-properties API (`GetNodeProperties` lines 82 and 87). A custom `solr.responses.hidden.sys.props` replaces the defaults and would expose it, which the existing store password never does. Fix: add `SOLR_SSL_KEY_MANAGER_PASSWORD=` after line 78, and add a `testNotMapped`-style assertion.
- The docs say the variable is needed only when the key password differs. That is correct. Adding "JKS only" would be clearer (optional).

**Item 6, overlap with the CLI round, from this side only.** The named hunks in `solr/bin/solr`:
- One hunk, head `@@ -226,6 +226,9 @@`. It inserts three lines at head lines 229 to 231, after the `SOLR_SSL_KEY_STORE_PASSWORD` export block (main lines 226 to 228) and before the `SOLR_SSL_KEY_STORE_TYPE` block (main lines 229 to 231). Lines after main 228 move down by three.
- It does not touch the trust store block (main lines 233 to 241), the client keystore block (lines 254 to 274), or any `SOLR_SSL_OPTS` line.
- A textual conflict arises only if a CLI branch edits main lines 225 to 231. Hunks after line 228 only shift by three. If the CLI round lands first, 11678's anchor (the keystore password export) is unchanged, so the rebase is content-free.
- Also touched by 11678 (not `bin/solr`): `solr.in.sh` (after main line 163), `solr.in.cmd` (after main line 148), and `jetty-ssl.xml`.
- For SOLR-18132, from this side: 11678 changes no existing `SSLConfigurations` method (only a new getter, a new constant and a new map entry). Nothing on main switches over `CredentialType` (its uses are confined to the configuration package and its tests), so the enum addition is compile-safe on main. The CLI branches were not audited.

## Receipt disagreements (exact wording, `receipts/SOLR-11678.md`)

1. **Line 5:** "and the SSL configuration family in solrj (SSLConfigurations, SSLCredentialProvider, AbstractSSLCredentialProvider, EnvSSLCredentialProvider)". The four files are under `solr/core/src/java/org/apache/solr/util/configuration/`. No solrj file is in the diff. It should read "solr/core".
2. **Line 7:** "the provider tests iterate DEFAULT_CREDENTIAL_KEY_MAP in enum order". Only `EnvSSLCredentialProviderTest`'s expected values depend on order. `SysPropSSLCredentialProviderTest` is order-independent.
3. **Line 6:** "Unset means null, which is Jetty's fallback to the keystore password: today's behavior." Correct for the keystore fallback. It omits that null also reads the JVM property `org.eclipse.jetty.ssl.keypassword` (item 3a).
4. **Lines 4 and 5** (13 files; the tip adds only the TESTING note): the counts and the tip content agree with git. The receipt does not say that the TESTING note sits at the repository root inside the PR diff.
5. **Confirmed as stated:** head `55d8cd189d15`, base `cabedd1d968`, 76 insertions and 4 deletions, the Jetty XML, `bin/solr` and docs hunks, and "Owed on the main side".

## Owner decisions

1. **Scope:** ship server-side only, and state in the description that SolrJ clients and the JDK default client keystore are not covered (recommended), or add a client follow-up. Jetty's client factory could take the same value; the JDK default `SSLContext` path cannot carry a separate key password.
2. **EnvToSyspropMappings:** approve adding `SOLR_SSL_KEY_MANAGER_PASSWORD=` with a test assertion (recommended). Without it the secret becomes a system property.
3. **TESTING note:** remove `SOLR-11678-TESTING.md` from the outbound tree before any PR. The owner chooses how the submit branch changes; no edit was made this round.
4. **Premise run:** authorize the main-side run in item 4. Accept that the premise and the ticket text are JKS-only.
5. **Side effect:** accept the `org.eclipse.jetty.ssl.keypassword` side effect (item 3a) as a documented non-issue, or decide otherwise.
6. **Optional docs:** a JKS-only sentence in the `solr.in` files and in `enabling-ssl.adoc` near line 87.
7. **Landing order** against the CLI round's `bin/solr` lines 225 to 231.
8. **Custom `class://` providers** now receive the new constant at every server start. Accept it, or document it.

## Not checked

- No build, Gradle, test, premise run, `gh` write, commit, push, post or file edit (the round's rules).
- Jetty and JDK behavior was read from class files (javap of `jetty-xml-12.1.12.jar` and `jetty-util-12.1.12.jar` from the Gradle cache, and the JDK 21 `src.zip`). Nothing was executed. The `set()` fallback was only partly traced.
- Whether the server starts, and the exact Jetty error text: the item 4 run decides.
- Windows `solr.cmd` runtime inheritance: read from the script structure only.
- The 41 commits between base and main were reviewed only for the touched paths and the wiring paths cited. The merge was not test-applied.
- The CLI round's seven branches and SOLR-18132's code were not read (outside this part's scope).
- The reference guide page was read as source, not rendered.
- `JWTAuthPluginIntegrationTest` (line 597) uses a separate key password, but it is a JWT test, not an SSL server path, so it was not examined.
- Changelog fragment: the shape matches main (type "added" is a listed value; nick and JIRA-link forms appear on main fragments). It was not otherwise audited.
