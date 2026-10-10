# Review confidence round 2, slice C: premise re-checks by code reading

Slice C of `assignments/pool-review-confidence-round-2.md`. Three NO GATE branches, one premise each. No builds, tests, Gradle, or reproduction runs were made. Each run below is a spec for the gate backlog, and each outcome is a prediction until that run lands.

## Scope and method

- Current main: `upstream/main` = `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (confirmed with `git rev-parse`). All main evidence is `git show 3f5d4c5bf8ac:<path>` or `git grep` at that commit.
- Heads, confirmed with `git ls-remote origin` on 2026-10-10, matching the receipts:
  - `solr-11678-submit` = `55d8cd189d15367b6896f0ea07570d3395ce404b`
  - `solr-9852-submit` = `31f58dbe8e6130c3b58dc35903d7aa3aa542e29b`
  - `solr-10882-submit` = `83fc3dfeb24b875639150579a2f8aeb35b7d6a9c`
  - Fork branches were read with a read-only `git fetch origin refs/heads/<branch>:refs/remotes/origin/<branch>`. The local refs already matched these heads.
- The change under review is each branch against its merge-base `cabedd1d968059215188f4e7563fb303241899ed`, which is the base named in the receipts. For every path the premise depends on, `git diff cabedd1d968 3f5d4c5bf8ac` is empty. The only change under `solr/solrj-streaming` between the two is a two-line `gradle.lockfile`. So base and main evidence are the same for these files.
- Jetty 12.1.12 bytecode was read with `javap -c -p` against `jetty-util-12.1.12.jar` from the local Gradle cache. JDK 21 sources were read from `lib/src.zip` of the local JDK. Nothing was executed.
- Ticket text came from the local JIRA exports under `research/jira-context/` (read only). Live JIRA was not queried.

## Summary

| Ticket | Premise checked on main | Verdict | Run owed |
|---|---|---|---|
| SOLR-11678 | A JKS keystore whose key password differs from its store password cannot be served over HTTPS by the default SSL configuration. Solr has no setting that gives Jetty a separate key password. | HOLDS | Packaged premise run, base vs head, JKS only |
| SOLR-9852 | `DatabaseMetaData.getColumns` returns null and `getTypeInfo` throws `UnsupportedOperationException`. | HOLDS | `JdbcTest.testDriverMetadata` at base, plus a `getTypeInfo` probe |
| SOLR-10882 | `array(..., sort=...)` throws `ClassCastException` for text or boolean mixed with numbers, and `NullPointerException` for a null value. The ticket's own double and long case is DEAD on main. | HOLDS (narrowed to the two defects the branch fixes) | `ArrayEvaluatorTest` probes at base, one of them new |

---

## SOLR-11678

### Verdict: HOLDS

Premise, stated for main: with the default `solr/server/etc/jetty-ssl.xml`, a JKS keystore whose key password differs from its store password fails to load, so Solr cannot serve HTTPS. Main gives Jetty no way to set a separate key password. The ticket's own report (SOLR-11678, local export) is the same: "If I specify different passwords for store and key then Solr fails to read certificate from JKS file."

### Code evidence at 3f5d4c5bf8ac

1. **The server XML has no key-manager input.** `solr/server/etc/jetty-ssl.xml` lines 14-17 read `keyStorePassword` (line 15) and `trustStorePassword` (line 16) from `SSLConfigurationsFactory`. Line 19 sets `KeyStorePassword`. Nothing sets `KeyManagerPassword`. `git grep -i keymanager` over the tree finds only Java `KeyManagerFactory` uses, test code, and an unrelated Kafka config key. The default keystore type is PKCS12 (line 24).

2. **The Solr Java side has no key-manager password.** `solr/core/src/java/org/apache/solr/util/configuration/SSLConfigurations.java` declares four system properties (lines 34-39, none for a key manager). Its getters are the keystore (lines 85-89), truststore (94-98), client keystore (103-107) and client truststore (112-116). The branch's Java files are under `solr/core`, not `solrj` as the receipt says.

3. **Jetty passes the keystore password for key recovery.** Jetty is `eclipse-jetty = "12.1.12"` (`gradle/libs.versions.toml` line 84). In `org.eclipse.jetty.util.ssl.SslContextFactory` (bytecode of `getKeyManagers(KeyStore)`), the code calls `KeyManagerFactory.init(keyStore, pw)` with `_keyManagerCredential` if set, otherwise `_keyStoreCredential`. The field `_keyManagerCredential` is written only inside `setKeyManagerPassword`. Solr's main never calls that setter, because the XML never sets `KeyManagerPassword`. So on main every key recovery uses the keystore password.

4. **The JVM property `org.eclipse.jetty.ssl.keypassword` is inert on main.** The bytecode reads that property only inside `setKeyManagerPassword(null)`. Main never calls it, so the property does nothing for Solr today. Consequence for the branch: on head an unset variable does call `setKeyManagerPassword(null)`, which then reads this property. The receipt's "unset means today's behavior" needs that qualifier.

5. **The JDK fails at key manager init.** The default `ssl.KeyManagerFactory.algorithm` is `SunX509` (`conf/security/java.security` line 329). `sun/security/ssl/KeyManagerFactoryImpl.java` (`SunX509.engineInit`, lines 61-66) builds `SunX509KeyManagerImpl` with no catch. That constructor calls `ks.getKey(alias, password)` for every key entry (`SunX509KeyManagerImpl.java` lines 126-141). A wrong key password reaches `sun/security/provider/KeyProtector.java` line 294, which throws `UnrecoverableKeyException("Cannot recover key")`. Jetty runs this while it loads the `SslContextFactory`, so the expected failure is at server start, not only at handshake.

6. **The premise is JKS-only.** The JDK 21 keytool (`sun/security/tools/keytool/Main.java`, around lines 1105-1110 and 2374-2380) drops a different key password for PKCS12 keystores with a warning. A PKCS12 keystore cannot reproduce the premise.

Mechanism verdict: by reading, the default configuration gives Jetty only the keystore password for key recovery, and a JKS key with a different password fails inside `KeyManagerFactory.init`. Whether a packaged server fails in practice is what the run decides.

### Premise-run spec (gate backlog; not run)

**Build.** A packaged distribution at two trees: base = `cabedd1d968` (identical to main for the touched files) and head = `55d8cd189d15`. The gate host (Linux, Gradle toolchain) builds both. Removing `SOLR-11678-TESTING.md` for packaging does not change server code; record the packaged head used.

**Keystores.** Both are JKS with differing key and store passwords, plus a matched control. Use absolute paths:

```
keytool -genkeypair -storetype JKS -keystore <abs>/keypw-differs.jks -storepass storepw -keypass keypw -alias solrkp -keyalg RSA -keysize 2048 -validity 2 -dname "CN=localhost, OU=SolrTest, O=SolrTest, C=US" -ext "SAN=dns:localhost,ip:127.0.0.1"
keytool -genkeypair -storetype JKS -keystore <abs>/keypw-matched.jks -storepass storepw -keypass storepw ... (same other options)
```

**Common environment.** `SOLR_SSL_ENABLED=true`, `SOLR_SSL_KEY_STORE=<abs>/<keystore>.jks`, `SOLR_SSL_KEY_STORE_TYPE=JKS`, `SOLR_SSL_KEY_STORE_PASSWORD=storepw`, `SOLR_SSL_TRUST_STORE=<same file>`, `SOLR_SSL_TRUST_STORE_TYPE=JKS`, `SOLR_SSL_TRUST_STORE_PASSWORD=storepw`, `SOLR_SSL_RELOAD_ENABLED=false`.

**Command shape.** `bin/solr start -p 8983`, then `curl -k -sS -o /dev/null -w "%{http_code}" https://localhost:8983/solr/admin/info/system`, then `bin/solr stop -p 8983`. Read startup errors from `server/logs/solr-8983-console.log`.

| Run | Tree | Keystore | Extra setting | Expected | What it shows |
|---|---|---|---|---|---|
| R1 | base | differs | none | No HTTP 200. Log shows `UnrecoverableKeyException: Cannot recover key`, most likely as a start failure | Premise reproduced |
| R2 | base | matched | none | HTTP 200 | Harness control. If not 200, the run proves nothing |
| R3 | base | differs | `SOLR_SSL_KEY_MANAGER_PASSWORD=keypw` | Same failure as R1 | Base ignores the new variable |
| R4 | head | differs | none | Same failure as R1 | Fallback to the keystore password is unchanged |
| R5 | head | differs | `SOLR_SSL_KEY_MANAGER_PASSWORD=keypw` | HTTP 200 | XML and environment path fixed |
| R6 | head | differs | no variable; `SOLR_OPTS=-Dsolr.jetty.keymanager.password=keypw` | HTTP 200 | System property path fixed |
| R7 | head | matched | none | HTTP 200 | Regression check: the default configuration must still start with a null `KeyManagerPassword` |
| R8 (optional) | base | differs | `SOLR_OPTS=-Dorg.eclipse.jetty.ssl.keypassword=keypw` | Same failure as R1 | Confirms the bytecode finding that the JVM property is inert on main |

**Reading the runs.**
- Premise reproduced: R1 is not HTTP 200 and the key-recovery error appears in the log, and R2 is HTTP 200. A start failure and a handshake failure both count, since either shows the premise.
- Premise DEAD: R1 returns HTTP 200. Then the default configuration already works with differing passwords on this JDK, and the ticket is dead on main.
- Premise narrowed: R8 starts. Then the JVM property already works on main, and the premise holds only for configurations without it. The receipt and ticket text must say so.
- Head fix holds: R5, R6 and R7 return HTTP 200.
- R7 failing with an XML or Jetty error at start is a regression in the null-`Ref` path (`KeyManagerPassword` set to null, the same shape as `trustStorePassword`). That is a head defect, not a premise result. The repo's `test_ssl.bats` always sets the trust store password, so no existing test covers the null path.

**Pitfalls that would give a false result.**
- Leaving out `SOLR_SSL_KEY_STORE_TYPE=JKS` or `SOLR_SSL_TRUST_STORE_TYPE=JKS`. The default is PKCS12 (`jetty-ssl.xml` lines 24-25), so the failure becomes a format error, not a key password error.
- Using a PKCS12 keystore. It cannot carry a different key password (keytool, above).
- Reading only the start status. Check the HTTP code and the log.

### Observations for the gate and review (not verdict inputs)

- **Secret exposure.** The head diff does not map `SOLR_SSL_KEY_MANAGER_PASSWORD` in `solr/solrj/src/resources/EnvToSyspropMappings.properties`. Main maps the four existing password variables to empty (lines 72, 75, 78, 83). `EnvUtils.init` (`solr/solrj/src/java/org/apache/solr/common/util/EnvUtils.java` lines 209-216) turns any unmapped `SOLR_` variable into a system property through `envNameToSyspropName` (lines 243-247). So the new variable would set `solr.ssl.key.manager.password` to the secret. This matches Security round S3, item 2.
- The receipt's file list places the Java classes in `solrj`. They are under `solr/core`.
- The `TESTING.md` file sits at the repository root inside the PR diff. It must be removed for packaging.

---

## SOLR-9852

### Verdict: HOLDS

Premise, stated for main: `DatabaseMetaData.getColumns` returns null, so JDBC clients get no column metadata, and `DatabaseMetaData.getTypeInfo` throws `UnsupportedOperationException` (the stack trace in the ticket). The ticket's export summary is "Solr JDBC doesn't implement columns' metadata".

### Code evidence at 3f5d4c5bf8ac

- `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/sql/DatabaseMetaDataImpl.java` lines 755-758: `getColumns` returns `null` (line 758).
- Lines 816-817: `getTypeInfo` throws `new UnsupportedOperationException()`.
- Lines 714-731 (`getTables`) run a query over `metadata.TABLES`. The branch copies that pattern for `metadata.COLUMNS`. That is mechanism context, not part of the premise.
- `DatabaseMetaDataImpl` is the only `DatabaseMetaData` implementation (`ConnectionImpl.java` line 74 constructs it).
- No test on main calls `getColumns` or `getTypeInfo` (`git grep` over `solr/solrj-streaming/src/test`).

### Premise-run spec (gate backlog; not run)

- **Module.** `solr:solrj-streaming` (`settings.gradle` line 41), Gradle task `:solr:solrj-streaming:test`.
- **Class and method.** `org.apache.solr.client.solrj.io.sql.JdbcTest`, a `SolrCloudTestCase`. Use method `testDriverMetadata` (`@Test` at line 573, method at line 574). It calls the private helper `testJDBCMethods` twice: first with `connectionString1`, then with the map_reduce `connectionString2`. The branch's new assertions sit inside the helper, after the `getTables` block (main lines 676-686).
- **Tree.** Production at `cabedd1d968` (identical to main for the touched files), with only the branch's `JdbcTest.java` on top. The branch's `TESTING.md` is not needed.
- **Expected base result.** `testDriverMetadata` fails at the first new assertion, `assertNotNull("getColumns must return a result set", rs)` inside `testJDBCMethods`, on the first call. Record that failure line in the branch's `getColumns` block.
- **Masking.** That first failure hides the `getTypeInfo` assertion. To see the `UnsupportedOperationException` on base, add a separate probe that calls `getTypeInfo()` on a connection and expects `UnsupportedOperationException` at base. Without the probe, the `getTypeInfo` half rests on the code read at lines 816-817.
- **Harness rule.** If the run fails in cluster setup (`createServers`, `control_collection` timeout, watch-limit noise) before `getColumns` is reached, the run is INCONCLUSIVE. Narrow it and rerun per the workflow notes. Do not count it.
- **Refutation.** Base `getColumns` returns a non-null `ResultSet` (the `assertNotNull` passes): the "returns null" premise is DEAD on main. A `getTypeInfo` probe that does not throw `UnsupportedOperationException` kills that half.
- **Head check (gate proof, not the premise).** At the packaged head `31f58dbe8e6`, the same test should pass the `getColumns` block (`id`, `a_i`, `a_s` present; the `a_s` filter returns one row), and `getTypeInfo` should throw `SQLFeatureNotSupportedException`. A SQL error from the new query at head is a fix failure, not a premise refutation.

---

## SOLR-10882

### Verdict: HOLDS, narrowed to the two defects the branch fixes

The ticket's own premise is DEAD on main. The last ticket comment (local export, 2017-06-19) says sort "doesn't take into account differing types (double and long)." Main already normalizes numbers, so double and long values sort together. The branch's narrower premises hold by reading: text or boolean mixed with numbers under sort throws `ClassCastException`, and a null value under sort throws `NullPointerException`.

The receipt's path shorthand, `org/apache/solr/client/solrj/io/eval/ArrayEvaluator.java`, is `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/eval/ArrayEvaluator.java` in the repository.

### Code evidence at 3f5d4c5bf8ac

**The ticket's double and long case is dead.**
- `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/eval/RecursiveEvaluator.java` lines 59-63 convert `Double` to `BigDecimal` (NaN to null). Lines 66-67 convert any other `Number` to `BigDecimal`.
- Line 211 (`recursivelyEvaluate`) applies `normalizeInputType` to every contained result before `doWork`. So `ArrayEvaluator` receives `BigDecimal` for every number, and double and long sort together.
- Lines 115-116 convert `BigDecimal` back to `double` on output.

**Text or boolean mixed with numbers throws `ClassCastException` (holds).**
- `ArrayEvaluator.java` line 50 (ascending comparator) is `(left, right) -> left.compareTo(right)` on a raw `Comparable`.
- Lines 69-77 check only that each value is `Comparable`. Nothing checks that values are comparable with each other.
- Lines 79-83 sort the mixed list. A `String` or `Boolean` compared with a `BigDecimal` makes the bridge cast fail with `ClassCastException`. A comparison sort must compare some adjacent pair of different kinds when the kinds are mixed, so the sort cannot finish without that cast.

**A null value throws `NullPointerException` (holds, reachable two ways).**
- A missing tuple field: `FieldValueEvaluator.java` line 45 (`tuple.get`), lines 91-99 return null when the value is missing. Line 93 records `"null"` in the stream context.
- A literal `null`: `StreamFactory.java` lines 627-632 return null. `RecursiveEvaluator.java` lines 167-170 wrap it in a `RawValueEvaluator`, whose `evaluate` returns the null (`RawValueEvaluator.java` lines 75-77).
- `normalizeInputType` returns null for null (`RecursiveEvaluator.java` lines 55-56). `recursivelyEvaluate` adds it (line 211).
- `ArrayEvaluator.java` lines 69-75: null is not `Comparable`, so the argument `value.toString()` at line 75 throws `NullPointerException`. `RecursiveEvaluator.evaluate` (lines 197-205) unwraps only `UncheckedIOException`, so the NPE escapes.

### Premise-run spec (gate backlog; not run)

- **Module and class.** `solr:solrj-streaming`. Class `org.apache.solr.client.solrj.io.stream.eval.ArrayEvaluatorTest` (`SolrTestCase`, no cluster). File: `solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/stream/eval/ArrayEvaluatorTest.java`.
- **Tree.** Production at `cabedd1d968`. Test files from the branch, plus Probe C in a scratch copy.

**Probe A (branch test `arrayMixedTypesSortTest`).** Values `a="a"`, `b=2L`, `c="c"`, expression `array(a,b,c, sort=asc)`.
- Base expected: ERROR with `java.lang.ClassCastException`. The `expectThrows(IOException.class, ...)` call fails on an unexpected exception type.
- Head expected: PASS, with an `IOException` whose message contains "values of different types".
- Refutation: base throws `IOException`, not `ClassCastException`. The mixed-type premise is then DEAD.

**Probe B (branch test `arrayMixedNumberTypesSortTest`, control).** Values `a=11.5D`, `b=4L`, `c=12.3D`, `d=0L`, expression `array(a,b,c,d, sort=asc)`, expecting `[0, 4, 11.5, 12.3]`.
- Base expected: PASS. If base fails, the ticket's double and long premise is alive, and the DEAD finding above is wrong. Stop and report.

**Probe C (new; the branch has no null test).** Add a method to `ArrayEvaluatorTest`: values `a=1L` with `b` absent, `setStreamContext(new StreamContext())`, expression `array(a,b, sort=asc)`.
- Base expected: `java.lang.NullPointerException` escapes `evaluate()`, not `IOException`.
- Head expected: `IOException` whose message contains `non-Comparable value ('null')`.
- Refutation: base throws `IOException`. The NPE premise is then DEAD, and the NPE sentence in the changelog and PR text must go.
- Probe C belongs in a scratch copy of the gate tree, not in the submit branch, unless the owner approves an edit.

**Focused filter.** `ArrayEvaluatorTest` methods `arrayMixedTypesSortTest`, `arrayMixedNumberTypesSortTest`, and Probe C. The gate's `-WithFailBefore` applies to the branch's tests.

---

## Could not check

- Nothing was built or run. Every verdict is by reading. The run outcomes above are predictions for the gate to confirm.
- SOLR-11678: the JDK 21 key recovery path was read from source, not executed. Whether a packaged server fails at start or only at the first handshake is for R1 to show. Either counts as reproduction. Jetty's `Set` with a null `Ref` in `jetty-ssl.xml` was not traced through `jetty-xml`. R7 decides that.
- SOLR-11678: the keytool and JDK behavior were read from the local JDK's `src.zip` and `java.security`. They were not run.
- SOLR-9852: whether `metadata.COLUMNS` exposes the 18 camelCase column names the query selects, in the pinned Calcite and Avatica, was not verified. This affects only the head check.
- SOLR-10882: Probe B passing on base is a reading, not a run. SOLR-13524's interaction with this premise was not re-checked; the Streaming round 1 read stands.
- Live JIRA and live PR state were not queried. Ticket text comes from the local exports.
- A scratch file named `$out` was left in the session scratchpad. An `rm` of it was denied by a safety check and was not retried. It is outside the repository.
- This report is the only file written. Nothing was committed, pushed, commented, posted, or claimed.
