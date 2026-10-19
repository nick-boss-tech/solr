# SOLR-18209: Replace Arrays.asList with List.of for immutable static final fields

## What happens today

Several `private static final` lists in solrj and core are built with `Arrays.asList`. That returns a fixed-size list that still accepts `set`, so the field is not immutable even though nothing mutates it.

## What this change does

Builds those four lists with `List.of` instead, in three files:

- `NodeHealth`: `UNHEALTHY_STATES`
- `SchemaDesignerConfigSetHelper`: `includeLangIds`
- `ContentStreamBase`: `UNHELPFUL_TYPES`, `XML_SUF`, `JSON_SUF`, `CSV_SUF`

The values and the lookups are unchanged. No caller writes to these fields. Lists that are copied into a `HashSet` are left as they are, because the set they feed is mutable.

Behavior change: the fields can no longer be modified. `List.of` also rejects null elements, and none of these lists has one.

## Proof

Existing tests cover the changed fields, so no new test was added:

- `ContentStreamTest` covers the XML, JSON, and CSV suffix lists and the octet-stream and gzip fallback in `UNHELPFUL_TYPES`.
- `NodeHealthTest` covers `UNHEALTHY_STATES`.
- `TestSchemaDesignerConfigSetHelper` covers `includeLangIds`.

Head f73f2d4358 on 2026-10-19: `ContentStreamTest` 7 of 7, `NodeHealthTest` 8 of 8, `TestSchemaDesignerConfigSetHelper` 8 of 8, all passing with 0 failures and 0 skipped. `:solr:solrj:spotlessJavaCheck` and `:solr:core:spotlessJavaCheck` pass. Logs: `/workspace/gates/logs/vm4/solr-18209-gate.log` and `solr-18209-TEST-*.xml`.

No fail-before run: the change does not alter behavior that a test could distinguish from the base.

## Question for the maintainer

Should the `HashSet`-wrapped fields (`SKIP_FOLDERS`, `forbiddenTypes`, `removeFieldProps`) move to `Set.of` as well? They are mutable sets, so this PR leaves them alone.
