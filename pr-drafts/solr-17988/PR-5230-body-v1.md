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

**Validation of the corrected head is pending.**

Current head: `bf285189f74b775165fc53ba305d35dccf880a47`. The corrections were checked against the Solr 10.0.0 startup script, `EnvUtils`, its alias mappings, `NodeConfig`, and `LoadAdminUiServlet`. No new runtime results are claimed.

Historically, `buildLocalSite`, `checkSiteLinks`, and `checkCliDocsUpToDate` passed at `16d3005293d` on 2026-10-24. Those results predate the corrected wording. Fresh Linux guide checks and exported-environment probes are pending at the current head.

## Scope and limits

No code or startup scripts change. Adding shell warnings would be a separate behavior change; existing Java alias warnings are documented here. The broader system-property inventory is planned follow-up work.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
