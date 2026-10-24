# SOLR-17988: property transitions, Solr 9.x to 10.0

Claim: `claims/solr-17988-property-audit-round-1.md` (vm6). Assignment: `assignments/solr-17988-property-audit-round-1.md`. Read-only: no code change, no Jira or GitHub post.

## Method and bounds

- Old tree: merge-base of `releases/solr/10.0.0` (commit `6c6c48a6f7`) and `branch_9x` at `d38a9545cc`. New tree: `releases/solr/10.0.0`.
- Scripts: `solr/bin` in both trees. Names compared as exact `SOLR_*` tokens.
- System properties: exact `"solr.*"`, `"jetty.*"` string literals and `-D` names in `solr/core`, `solr/solrj`, `solr/server`, `solr/bin`. Names in both trees are not listed. This is a name-level comparison: a property whose key is built by concatenation would be missed.
- Docs: `solr/solr-ref-guide` at 10.0 (all pages), and `major-changes-in-solr-10.adoc` lines cited below.
- Not checked: `solr.xml` element and attribute changes, cluster property names, and the `solr.in.cmd` text beyond the names. The `CHANGES.txt` 10.0 section was not read. The "previous name" column in `solr-properties.adoc` was read as the rename record.

## 1. Environment variables set by users

Only the variables users set (in `solr.in.sh`, `solr.in.cmd`, or the environment) are listed. Internal script variables such as `SOLR_TOOL_HOST`, `SOLR_URL`, `SOLR_LOG_LEVEL_OPT`, and `SOLR_HOST_ARG` were renamed or removed inside the scripts only and are not transitions.

| Old (9.x) | New (10.0) | 10.0 scripts | Documented in 10.0 upgrade notes | Other 10.0 doc | Status |
| --- | --- | --- | --- | --- | --- |
| `SOLR_JETTY_HOST` | `SOLR_HOST_BIND` | Warns, does not read the value (`bin/solr` lines 895 to 898; `solr.cmd` lines 246 to 249) | Yes, line 44 | `solr-properties.adoc` | Covered |
| `SOLR_HOST` | `SOLR_HOST_ADVERTISE` | Not read, no warning (commit `b68f9703f2`, SOLR-15442) | Yes, line 46, which says "deprecated and now use" | Several 10.0 pages still use `SOLR_HOST` in examples, e.g. `solr-in-docker.adoc`, `securing-solr.adoc` | Gap: silent ignore, and the note says "deprecated", not removed |
| `SOLR_PORT` | `SOLR_PORT_LISTEN` | Not read, no warning. The commit renamed it with no fallback (`b68f9703f2`, 2025-11-03, Eric Pugh) | No | `upgrading-a-solr-cluster.adoc` line 45 and 89 still say `SOLR_PORT`; `solr-control-script-reference.adoc` lines 187 to 188 still derive the stop port from `SOLR_PORT`. `SOLR_PORT_LISTEN` has no doc hit | Gap: silent ignore, and stale docs |
| `SOLR_REQUESTLOG_ENABLED` | `SOLR_LOGS_REQUESTLOG_ENABLED` | Not read, no warning | No | `configuring-logging.adoc` names the new one | Gap: silent ignore |
| `SOLR_ADMIN_UI_DISABLED` | `SOLR_UI_ENABLED` (inverted: `false` disables) | Not read, no warning. The only UI switch is `solr.ui.enabled` (`LoadAdminUiServlet.java` line 44) | No | None: `SOLR_UI_ENABLED` has zero doc hits | Gap: silent ignore. A user who set `SOLR_ADMIN_UI_DISABLED=true` gets the UI back, and nothing says so |
| `SOLR_WAIT_FOR_ZK` | `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS` | Not read, no warning. The matching sysprop `solr.cloud.wait.for.zk.seconds` exists | No | None: zero doc hits for the env var | Gap: silent ignore. Mapping inferred from names |
| `SOLR_HADOOP_CREDENTIAL_PROVIDER_PATH` | (removed) | Removed with the HDFS and Kerberos code | Covered at module level, lines 177 and 179 | None for the variable | Covered at module level |
| `SOLR_ENV`, `SOLR_CMD` (init.d) | (removed) | `init.d/solr` deleted; `systemd/solr.service` added | Yes, line 126 | None | Covered |
| `bin/post`, `bin/postlogs` | (removed) | Deleted in 10.0 | No match for `bin/post`, `post tool`, or `SimplePostTool` in the upgrade notes | None | Gap. Not a property, but a user-facing script removal |

New 10.0 variables with no doc hit: `SOLR_UI_ENABLED`, `SOLR_UI_EXPERIMENTAL_ENABLED`, `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS`, `SOLR_PORT_LISTEN`. `SOLR_CLUSTERING_ENABLED` appears only in the 9.x upgrade notes. The following have doc pages: `SOLR_MODULES`, `SOLR_DELETE_UNKNOWN_CORES`, `SOLR_PLACEMENTPLUGIN_DEFAULT`, `SOLR_SOLRXML_REQUIRED`, `SOLR_ZOOKEEPER_EMBEDDED_HOST`, `SOLR_LOGS_REQUESTLOG_ENABLED`, `SOLR_SSL_RELOAD_ENABLED`. `SOLR_CLOUD_OVERSEER_ENABLED` and `SOLR_CLOUD_DOWNGRADE_ENABLED` are covered at lines 34 and 104.

## 2. System properties

The name comparison found 90 names in 9.x that are absent from 10.0 code, and 122 names in 10.0 that are not in 9.x. Each 9.x-only name was checked against the 10.0 code, the 10.0 docs, and the rename table in `solr-properties.adoc`.

**Renamed, with a row in the previous-name column of `solr-properties.adoc`:** `solr.allowPaths` to `solr.security.allow.paths`; `solr.allowUrls` to `solr.security.allow.urls`; `solr.auth.jwt.allowOutboundHttp` to `solr.auth.jwt.outbound.http.enabled`; `solr.authorization.superuser` to `solr.auth.superuser`; `solr.default.confdir` to `solr.configset.default.confdir`; `solr.disable.allowUrls` to `solr.requests.allow.urls`; `solr.facet.stream.tiered` to `solr.streamingexpressions.facet.tiered.enabled`; `solr.httpclient.builder.factory` to `solr.solrj.http.jetty.customizer`; `solr.jetty.host` to `solr.host.bind`; `solr.log.dir` to `solr.logs.dir`. The table is the record for these.

**Renamed or removed, and covered by a 10.0 upgrade note:** `jetty.port` to `solr.port.listen` (line 48); `solr.redaction.system.pattern` to `solr.hiddenSysProps` (lines 152 to 157); `bootstrap_confdir` to `solr.configset.bootstrap.confdir` (line 69); the overseer properties (lines 103 to 104); `solr.xml` loaded from ZooKeeper (line 159); `solrcore.properties` (line 216). The 9.x upgrade notes also cover `solr.disable`, `solr.storeBaseUrl`, `solr.useUnsafeOverseerResponse`, and `solr.ssl.checkPeerName`.

**Removed with a feature, covered by a module-level note only:**
- Kerberos (`solr.kerberos.*`, `solr.impersonator.user.name`, and the Kerberos `solr.hdfs.security.*` names): line 177.
- HDFS (`solr.hdfs.*`, 30-plus names): line 179.
- PHP, Python, and Ruby writers (`solr.PHPResponseWriter` and the others): line 167.
- `PreAnalyzedField` and its update processor: line 190.
- `analytics` module: line 150.

The upgrade notes do not list the property names inside these features.

**Removed, with no replacement and no doc mention in 10.0:**
- `solr.disable.shardsWhitelist`
- `solr.disableConfigSetsCreateAuthChecks`
- `solr.indexfetcher.sotimeout`
- `solr.httpclient.retries`
- `solr.httpclient.socketFactory.registry.provider`
- `solr.search.NoOpRegenerator`
- `solr.v2RealPath`
- `solr.solrxml.location`
- `solr.OverseerStateUpdateDelay` (10.0 has `solr.OverseerStateUpdateMaxQueueSize`; not traced to a commit)
- `jetty.gzip.checkGzExists`, `jetty.gzip.excludedUserAgent`, `jetty.gzip.syncFlush` (10.0 has `jetty.gzip.deflaterPool.*`; not traced to a commit)
- `solr.jetty.ssl.needClientAuth` and `solr.jetty.ssl.wantClientAuth` (10.0 has `solr.jetty.ssl.need.client.auth.enabled` and `want.client.auth.enabled`; the rename is inferred from names, and neither old name nor new one is in the docs)

**Stale in 10.0 docs (removed from code, still described as current):**
- `solr.disableFingerprint`, `solr.dns.prevent.reverse.lookup`, `solr.allow.unsafe.resourceloading`, `solr.node`. Each is still in a 10.0 doc page (`solr-properties.adoc` or `node-roles.adoc`). Each is absent from 10.0 code.

**New in 10.0 and not in the reference guide (56 names):** the full list is in the appendix. The user-facing groups are: the Jetty HTTP and SSL tuning names (`solr.jetty.http.*`, `solr.jetty.ssl.*`, about 25), the `solr.admin.async.*` timeouts, `solr.cloud.overseer.enabled`, `solr.cloud.downgrade.enabled`, `solr.cloud.prs.enabled`, `solr.logs.requestlog.enabled`, `solr.pid.dir`, `solr.install.symDir`, `solr.configset.upload.mimetypes.forbidden`, `solr.update.docLockTimeoutMs`, and `solr.ui.headers.csp.connect-src.urls`.

## 3. Gaps, ranked

1. **Silent env var renames with no note and no warning:** `SOLR_PORT`, `SOLR_HOST`, `SOLR_REQUESTLOG_ENABLED`, `SOLR_ADMIN_UI_DISABLED`, `SOLR_WAIT_FOR_ZK`. The 10.0 scripts do not read the old names and do not warn. Only `SOLR_JETTY_HOST` warns. `SOLR_ADMIN_UI_DISABLED` is the most serious, because a user who turned the UI off gets it back without notice. This is the main reason the ticket's ask is not met.
2. **Stale docs on the port:** `solr-control-script-reference.adoc` (lines 187 to 188) and `upgrading-a-solr-cluster.adoc` (lines 45, 89) still describe `SOLR_PORT`, which 10.0 does not read. `SOLR_PORT_LISTEN` has no doc page.
3. **New env vars with no doc:** `SOLR_UI_ENABLED`, `SOLR_UI_EXPERIMENTAL_ENABLED`, `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS`, `SOLR_PORT_LISTEN`.
4. **`SOLR_HOST` wording:** the note says "deprecated", but 10.0 does not read it and does not warn. Either it should warn like `SOLR_JETTY_HOST`, or the note should say "removed".
5. **Stale sysprop entries:** four names still in 10.0 docs but removed from code (see section 2).
6. **Undocumented renames:** `solr.jetty.ssl.needClientAuth` and `wantClientAuth`.
7. **Undocumented script removal:** `bin/post` and `bin/postlogs`.
8. **Feature removals listed without their property names:** Kerberos, HDFS, and the writers. Acceptable as coverage, but a user searching for their property name will not find it.
9. **56 new sysprops with no doc.** Most are tuning knobs. The Jetty SSL and HTTP tuning names are the ones an operator is most likely to set.

## 4. Verdict

**Covered with named gaps.** The large changes are documented: the Jetty host and port sysprops, `bootstrap_confdir`, the Overseer, the redaction names, the removed `solr.xml` in ZooKeeper, and the Kerberos, HDFS, and writer removals. The renames in the reference guide's table are recorded. The gaps are narrow in count but serious in effect. Five user-set environment variables (`SOLR_PORT`, `SOLR_HOST`, `SOLR_REQUESTLOG_ENABLED`, `SOLR_ADMIN_UI_DISABLED`, `SOLR_WAIT_FOR_ZK`) are now ignored without a note, and the 10.0 control-script reference still tells users to set `SOLR_PORT`. The ticket asks for the places users set properties to be conveyed, and that is not yet true for these.

The status comment on SOLR-17988 posted today says the docs "look complete with the release". This audit does not support that statement for the environment variables. The five gaps above and the stale control-script text are the evidence against it.

## Appendix: 10.0-only system properties with no doc hit

jetty.gzip.deflaterPool.capacity, jetty.gzip.deflaterPool.compressionLevel, jetty.gzip.deflaterPool.noWrap, jetty.server.stopTimeout, solr.NumFieldLimitingUpdateRequestProcessorFactory, solr.OverseerStateUpdateMaxQueueSize, solr.admin.async.max, solr.admin.async.timeout.completed.minutes, solr.admin.async.timeout.minutes, solr.allowPartialResultsDefault, solr.blockcache.blockshift, solr.blockcache.readbuffer.size.default, solr.cloud.downgrade.enabled, solr.cloud.overseer.enabled, solr.cloud.prs.enabled, solr.configset.upload.mimetypes.forbidden, solr.exportbuffers.exchange.timeout.sec, solr.install.symDir, solr.jetty.http.acceptedReceiveBufferSize, solr.jetty.http.acceptedSendBufferSize, solr.jetty.http.acceptedTcpNoDelay, solr.jetty.http.reuseAddress, solr.jetty.http.reusePort, solr.jetty.keystoreParentPath, solr.jetty.ssl.acceptedReceiveBufferSize, solr.jetty.ssl.acceptedSendBufferSize, solr.jetty.ssl.acceptedTcpNoDelay, solr.jetty.ssl.need.client.auth.enabled, solr.jetty.ssl.provider, solr.jetty.ssl.renegotiationAllowed, solr.jetty.ssl.renegotiationLimit, solr.jetty.ssl.reuseAddress, solr.jetty.ssl.reusePort, solr.jetty.ssl.sni.host.check.enabled, solr.jetty.ssl.sni.required.enabled, solr.jetty.ssl.sniRequired, solr.jetty.ssl.sslSessionCacheSize, solr.jetty.ssl.sslSessionTimeout, solr.jetty.ssl.sts.include.subdomains.enabled, solr.jetty.ssl.sts.max.age.secs, solr.jetty.ssl.useCipherSuitesOrder, solr.jetty.ssl.want.client.auth.enabled, solr.keystore.reload.enabled, solr.log.cputime, solr.logs.requestlog.enabled, solr.otelDefaultConfigurator, solr.pid.dir, solr.schemaUpdateTimeoutSeconds, solr.search.multiThreaded.queueSize, solr.solrj.cache.timeout.sec, solr.solrj.javabin.readMapAsNamedList, solr.traceHostName, solr.ui.headers.csp.connect-src.urls, solr.update.docLockTimeoutMs, solr.url.scheme, solr.use.str.intern
