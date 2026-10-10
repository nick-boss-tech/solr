# Review confidence round 2: roll-up

Assignment: `assignments/pool-review-confidence-round-2.md`. Claim: `claims/pool-review-confidence-round-2.md`. Lead: the windows review agent. Four subagents ran in parallel, each writing a part report. The lead read every part report, spot-checked the draft edits against their descriptions, verified the one security claim (below), and reverted one citation edit (E9, SOLR-17377).

Part reports: `reports/review-confidence-round-2-a1.md` (SolrCloud, replication and backup, spellcheck), `-a2.md` (core admin, suggester, highlighting), `-b.md` (live update-processing bodies), `-c.md` (premise re-checks).

## Totals

| Slice | Scope | Result |
|---|---|---|
| A1 | 26 drafts | PASS 6, FIXED 3, FLAGGED 17 |
| A2 | 28 drafts | PASS 8, FIXED 5, FLAGGED 15 (one A2 FIXED reverted by the lead, see E9) |
| B | 28 live PR bodies | CONSISTENT 24, DRIFT 4, UNREAD 0 |
| C | 3 NO GATE premises | HOLDS 3 (SOLR-10882 narrowed; its double and long premise is dead) |

Every draft's cited head equals its branch's live tip, so no draft is written against a moved branch.

## Decisions for Nick and the main side (not taken)

1. **Symptom links and the head-only rule.** The rule is that every citation links the branch head. Many "What happens today" claims describe base behavior, and the branch changed those lines (A1 F1, F3, F7, F12, F14, F15, F16, F20, F22, F24; A2 SOLR-17377). No head link can show the old behavior. Options: allow a labelled base-SHA link in that section, drop the cited detail, or decide per draft. The lead took no side. A2 changed one such link (SOLR-17377, E9) to a merge-base SHA with different lines. The lead reverted that edit, so the link is at the head again. The "former check location" claim stays flagged.
2. **Eight suggester tickets have no receipt file.** `receipts/` lacks SOLR-9227, 9637, 9968, 10937, 11844, 14171, 17215 and 17393. Their Proof numbers were checked against the adopted answers only (A2 S1). The main side should add the receipts before these count as verified.
3. **Count placeholders.** `[CONFIRM: count]` stays in SOLR-9865 and SOLR-17287. The receipt for each says TestRestoreCore 4. The head file has three `@Test` methods. The count needs the gate log, which is not in the workspace (A1 F17, F19).
4. **Co-author trailers on branch heads (standing rule).** `solr-12007-submit` has three commits with "Co-Authored-By: Claude Sonnet 5.5" (8f44c181c5c, 4a538bc3cf1, 50a0052c148). `solr-15805-submit` has one (b2a463cf64f). Rewriting either moves its head and the draft's links (A2 S2).
5. **SOLR-18010.** The answers hold this ticket with no draft, but a draft exists. The receipt now says the settling log is on disk. Main side lifts the hold or holds the draft (A2 F14a).
6. **Security: unmapped key-manager variable (SOLR-11678 branch, from Slice C).** At head `55d8cd189d15`, `solr/solrj/src/resources/EnvToSyspropMappings.properties` has no entry for `SOLR_SSL_KEY_MANAGER_PASSWORD`. The lead read `EnvUtils.init` at the same head: any `SOLR_` variable without a custom mapping becomes a system property, here `solr.ssl.key.manager.password`. So a secret set in that variable would reach system properties, while the four existing password variables are mapped to empty. Main side should decide whether to map it before any run or PR.
7. **Ticket text.** Several drafts rely on ticket text that is not in the workspace (SOLR-15024 owed check, SOLR-15035 summary sentence, SOLR-4989, SOLR-8275, SOLR-9637, SOLR-11844, SOLR-17215, SOLR-16725, SOLR-2681; A1 F5, A2 S4).
8. **Update-processing live state (Slice B).** PR #5070 (SOLR-4841) is MERGED on GitHub. Its body matches the branch. The drifts in Slice B are listed below for the main agent to apply. Body edits are not made under this round.

## Slice A: verdict per draft

Verdicts: PASS (no change), FIXED (wording or citation edit made, named in the part report), FLAGGED (issue stated in the part report; no claim, number, choice, limit or title changed).

### SolrCloud (A1)

| Draft | Verdict | Note |
|---|---|---|
| SOLR-5813 | FIXED | Citation re-pointed from base SHA to head (same line, E1). |
| SOLR-9155 | FLAGGED | Symptom link shows post-change code (A1 F1). |
| SOLR-11288 | FLAGGED | Choice poses blank nodes only; answers record all three APIs (F2). |
| SOLR-12651 | PASS | Receipt head equals live tip. |
| SOLR-12991 | FLAGGED | Symptom base-SHA link (F3); "60 seconds" not in receipt (F4). |
| SOLR-13186 | PASS | |
| SOLR-13369 | FIXED | "proven by construction only" to "inconclusive by construction" (E2). |
| SOLR-14919 | FIXED | Two citation ranges widened (E3, E4). |
| SOLR-15035 | FLAGGED | Ticket-summary sentence not confirmed (F5). |
| SOLR-15106 | FLAGGED | "60 seconds" not in receipt (F6). |
| SOLR-15386 | PASS | |
| SOLR-15674 | FLAGGED | Link target fixed (E5); base-SHA symptom links (F7); Limits lack the follow-up offer (F8). |
| SOLR-15863 | PASS | |
| SOLR-16437 | FLAGGED | Six base links re-pointed to head (E6, E7); wrong-shard sentence differs from answers (F9); Choice not in answers (F10); Proof lacks counts (F11). |
| SOLR-17292 | FLAGGED | Symptom base-SHA link (F12). |
| SOLR-17680 | FLAGGED | Numbers 10 and 11 not in receipt (F13); symptom cites post-change code (F14). |
| SOLR-17733 | PASS | |

### Replication and backup (A1)

| Draft | Verdict | Note |
|---|---|---|
| SOLR-8430 | FLAGGED | Symptom base-SHA link (F15). HOLD per owner note. |
| SOLR-9598 | FLAGGED | Symptom non-head link (F16). HOLD per owner note. |
| SOLR-9865 | FLAGGED | `[CONFIRM: count]` (F17); SolrCloud restore claim not in answers (F18). |
| SOLR-11650 | FLAGGED | Symptom links show post-change code (F20); "three tests" not in receipt (F21). Re-point owed. |
| SOLR-12085 | PASS | |
| SOLR-12246 | FLAGGED | Symptom WARN cited to lines that log at INFO (F22). |
| SOLR-17287 | FLAGGED | `[CONFIRM: count]` (F19). |

### Spellcheck (A1)

| Draft | Verdict | Note |
|---|---|---|
| SOLR-3701 | FLAGGED | Process wording fixed (E8, E9). No pass count at head (F23). Base link (F24). |
| SOLR-4367 | FLAGGED | Process wording fixed (E10); changelog linked (E11). Proof placeholder unresolved (F25). |

### Core admin (A2)

| Draft | Verdict | Note |
|---|---|---|
| SOLR-4989 | PASS | Ticket text not checked (S4). |
| SOLR-6438 | PASS | |
| SOLR-8275 | PASS | |
| SOLR-8576 | FLAGGED | "Alias unchanged" sentence does not match the branch (F8). OWED hold stays. |
| SOLR-9750 | FIXED | Base links to fork host, same SHA and lines (E7). Changelog upgrade step owed on branch. |
| SOLR-11939 | PASS | Root `SOLR-11939-TESTING.md` still on branch; owed removal moves the head (S6). |
| SOLR-12007 | PASS | Branch carries co-author trailers (S2). |
| SOLR-13246 | FIXED | Base link to fork host (E7). |
| SOLR-15024 | FLAGGED | Ticket-text check owed (F9). Base link to fork host (E7). |
| SOLR-15805 | FIXED | Base links to fork host (E7). Branch commit has a co-author trailer (S2). |
| SOLR-16725 | FLAGGED | Mechanism sentence fixed (E8). "Which most collections use" has no source (F10). |
| SOLR-16849 | FLAGGED | DISCUSS item (open PR or close as fixed by SOLR-18083) not posed (F11). Base links to fork host (E7). |
| SOLR-17297 | PASS | |
| SOLR-17377 | FLAGGED | A2 changed the "former check location" link to a merge-base SHA (E9). The lead reverted it to head `22b5f209a11a` lines 682-684. That link shows a comment saying the check is no longer there, so the claim needs decision 1. |
| SOLR-17708 | FLAGGED | Branch changelog title overstates the change (F12). OWED hold. |
| SOLR-17731 | FLAGGED | Owed fixes and `ListAliasesAPITest` run (F13). |
| SOLR-18010 | FLAGGED | Held by answers (F14a). "Never a partly written file" is broader than the draft's own Limits (F14b). Changelog title narrower in answers (F14c). |

### Suggester (A2)

| Draft | Verdict | Note |
|---|---|---|
| SOLR-9227 | FIXED | "Gate" and "receipt" wording (E1). |
| SOLR-9637 | FLAGGED | Counts and date not in the record (F1). "Check step" wording fixed (E2). |
| SOLR-9968 | FLAGGED | Top-up gate recorded as running, not passed (F2). Hold. |
| SOLR-10937 | PASS | Bytecode check has no lane report; accepted because the decision is adopted unconditionally. |
| SOLR-11844 | FLAGGED | Weight-0 check result not recorded (F3). |
| SOLR-14171 | FLAGGED | Gate counts not recorded (F4). Lucene constant checked on 10.4.0 only. |
| SOLR-17215 | FLAGGED | Gate, counts and premise run not recorded (F5). Lucene claims not checked on 9.x. |
| SOLR-17393 | FLAGGED | Baseline result not recorded (F6). |

### Highlighting (A2)

| Draft | Verdict | Note |
|---|---|---|
| SOLR-2681 | PASS | |
| SOLR-3704 | FIXED | Internal hold comment removed (E3); "Covered" to "Tested" (E4). |
| SOLR-4540 | FLAGGED | GitHub run stated at this head, but the receipt names an earlier head (F7). Run identifiers removed (E5, E6). |

Count check: A1 26 drafts, A2 28 drafts, 54 in total. PASS 14, FIXED 8, FLAGGED 32.

## Edits made in this round

Seventeen draft files carry wording or citation edits. Each edit is listed in its part report (A1 E1 to E11; A2 E1 to E8). Two categories:

- Citation edits that point a link at the head SHA, or change a link's host, keep the SHA and lines the same (A1 E1, E6, E7; A2 E7).
- Wording edits remove internal vocabulary (gate, receipt, run identifiers, seeds, log names, "check step", hold comments). None changes a claim, a number, a Choice or a Limits line.

A1 E2 changes "proven by construction only" to "inconclusive by construction". The sentence after it still says no run showed the failure. The lead accepts it as formula wording.

The lead reverted A2 E9 (SOLR-17377). Nothing else was reverted.

## Slice B: live body consistency (update-processing)

Method: each PR was read with a read-only `gh pr view`. Live heads equal the fork branch tips. Every live body is byte-identical to its draft in `pr-drafts/update-processing/` after line-ending normalization.

| Ticket | PR | Result |
|---|---|---|
| SOLR-3657 | #5069 | CONSISTENT |
| SOLR-4841 | #5070 (MERGED) | CONSISTENT |
| SOLR-5065 | #5071 | CONSISTENT |
| SOLR-5505 | #5072 | CONSISTENT |
| SOLR-5754 | #5073 | CONSISTENT |
| SOLR-5887 | #5074 | DRIFT |
| SOLR-5939 | #5075 | CONSISTENT |
| SOLR-5941 | #5076 | CONSISTENT |
| SOLR-6045 | #5077 | CONSISTENT |
| SOLR-6065 | #5078 | CONSISTENT |
| SOLR-6973 | #5079 | CONSISTENT |
| SOLR-7022 | #5080 | CONSISTENT |
| SOLR-7504 | #5081 | CONSISTENT |
| SOLR-11475 | #5082 | CONSISTENT |
| SOLR-11483 | #5083 | CONSISTENT (close read: nothing depends on the removed bullet) |
| SOLR-12245 | #5084 | CONSISTENT |
| SOLR-12703 | #5085 | CONSISTENT |
| SOLR-12864 | #5086 | CONSISTENT |
| SOLR-13265 | #5087 | CONSISTENT |
| SOLR-13696 | #5088 | CONSISTENT |
| SOLR-13943 | #5089 | DRIFT |
| SOLR-14262 | #5090 | CONSISTENT |
| SOLR-14718 | #5091 | CONSISTENT |
| SOLR-16356 | #5092 | CONSISTENT |
| SOLR-16655 | #5093 | CONSISTENT |
| SOLR-16673 | #5094 | DRIFT |
| SOLR-16910 | #5095 | CONSISTENT |
| SOLR-12705 | #5096 | DRIFT (minor wording) |

Drift, for the main agent to apply (no body edit made):

- **SOLR-5887 (#5074).** (1) Body links cite `c4c57ef7bcbd`; the live head is `fa60337f88b4`. The only commit between them changes two test files, and the production lines still match, so the links should move to `fa60337f88b4`. (2) "17 of 17 (verified 2026-10-07 at this head)" is not in the receipt, which records 17 at `fa60337f88b4` in the re-gate that finished 2026-10-10. (3) The commit edits `RootFieldTest` and `JsonLoaderTest`, and the body says nothing about it. The receipt records both methods re-run green.
- **SOLR-13943 (#5089).** (1) Body says "this branch adds no changelog fragment"; the head has `changelog/unreleased/SOLR-13943.yml`, restored in `ca443f6f680`. (2) Scope says "two test files"; the commits above the base change three files (changelog, the new `TimeRoutedAliasDateMathInStartTest.java`, and `TimeRoutedAliasUpdateProcessorTest.java`). (3) Links and "runs at this head" sit at `cc155cf68e1d`, not the live head `ca443f6f6806`.
- **SOLR-16673 (#5094).** The body's pre-fix result and the focused count of 43 of 43 are not in the receipt. The receipt records the gated head `d7170b12f312` and a changelog-only top-up. Refresh the receipt from the gate log, or drop those sentences.
- **SOLR-12705 (#5096).** Limits say SOLR-7504 conflicts "once, in one import block". The merge conflicts in two hunks, both in one import block. Suggested wording: "conflicts in two hunks, both in one import block".

## Slice C: premise re-checks (NO GATE branches)

| Ticket | Verdict | Run owed |
|---|---|---|
| SOLR-11678 | HOLDS | Packaged JKS premise run, base vs head (R1 to R8) |
| SOLR-9852 | HOLDS | `JdbcTest.testDriverMetadata` at base, plus a `getTypeInfo` probe |
| SOLR-10882 | HOLDS, narrowed to the text and boolean ClassCastException and the null NullPointerException. The ticket's own double and long premise is DEAD on main. | `ArrayEvaluatorTest` probes A, B and C (C is new) |

The full premise-run specs follow (copied from `reports/review-confidence-round-2-c.md`). Each run is a prediction until the gate confirms it. Nothing was built or run in this round.

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
