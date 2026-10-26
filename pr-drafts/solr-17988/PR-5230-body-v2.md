🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17988

## What happens today

**Solr 10 configuration changes need clearer upgrade guidance, including where the settings take effect.**

The port pages still use `SOLR_PORT`, while the startup scripts use `SOLR_PORT_LISTEN`. The upgrade notes also need the SSL client-authentication property renames and the distinction between settings read by the shell and exported variables mapped by Java. An absent shell-script read does not mean an exported variable is ignored.

## What this change does

**Documentation only, in three pages.**

[The upgrade notes](https://github.com/nick-boss-tech/solr/blob/bf285189f74b775165fc53ba305d35dccf880a47/solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc) document the port, host and request-log replacements, SSL property renames, and `bin/solr postlogs`. They also explain that exported `SOLR_ADMIN_UI_DISABLED` and `SOLR_WAIT_FOR_ZK` remain deprecated Java aliases with warnings, and that exported `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS` sets the current ZooKeeper property.

The UI guidance distinguishes the two switches: exported `SOLR_UI_ENABLED=false` disables the classic UI as well as loading of the new UI module; `SOLR_UI_EXPERIMENTAL_ENABLED=false` disables only the new module. Unexported assignments in `solr.in.sh` reach the shell script but are not inherited by Java. The note gives the `export` and `SOLR_OPTS` alternatives.

[The control-script reference](https://github.com/nick-boss-tech/solr/blob/bf285189f74b775165fc53ba305d35dccf880a47/solr/solr-ref-guide/modules/deployment-guide/pages/solr-control-script-reference.adoc) and [cluster upgrade guide](https://github.com/nick-boss-tech/solr/blob/bf285189f74b775165fc53ba305d35dccf880a47/solr/solr-ref-guide/modules/deployment-guide/pages/upgrading-a-solr-cluster.adoc) use `SOLR_PORT_LISTEN` for the port settings.

## Proof

**The current head, `bf285189f74b775165fc53ba305d35dccf880a47`, passed the Linux guide checks and the behavior checks below.**

- Ref Guide: `buildLocalSite`, `checkSiteLinks` (no errors or warnings) and `checkCliDocsUpToDate` all exit 0 on JDK 21.0.12.1 with Gradle 9.7.0. The tracked tree is clean afterwards. Two earlier attempts failed in the build host's environment, not on the branch, and were rerun at the same head.
- Environment probes against the Solr 10.0.0 release in fresh JVMs: each deprecated alias maps to its new property with a warning, an unexported assignment does not reach Java, and `SOLR_UI_EXPERIMENTAL_ENABLED=false` leaves `solr.ui.enabled` unset.
- Start script against the release: `--module=new-ui` is omitted for `SOLR_UI_ENABLED=false` and for `SOLR_UI_EXPERIMENTAL_ENABLED=false`. `SOLR_OPTS="-Dsolr.ui.enabled=false"` reaches the Solr JVM, and the classic UI returns HTTP 404 in both disabled cases.

The earlier pass at `16d3005293d` predates the corrected wording and is not relied on.

## Scope and limits

No code or startup scripts change. Adding shell warnings would be a separate behavior change; existing Java alias warnings are documented here. The broader system-property inventory is planned follow-up work.

Only the Linux start script was run. `solr.cmd` was not exercised.

An exported deprecated alias overrides an explicit `-D` for the new property name. With `SOLR_WAIT_FOR_ZK=45` and `-Dsolr.cloud.wait.for.zk.seconds=60`, Solr uses 45 and logs a warning. The note recommends the new name but does not state this order. A sentence covering it would need a new head and a new gate. Maintainers can decide whether to add it.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
