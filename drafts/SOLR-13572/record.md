# Skip record: SOLR-13572

- Ticket: SOLR-13572, NullPointerException leads to msg=null.
- Verdict: keep-open.
- Draft branch: solr-13572-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier B3 reading (vm2, round 1) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-06-24, updated 2020-01-05, no fix versions. The reporter upgraded from Solr 6.6 to 7.7.2. Indexing a field `sort_X3b_en_node_grants` with the value `node_access_all:0` logs "Error adding field ... msg=null" from `DocumentBuilder.toDocument`. The 2019-06-24 comment quotes a "Caused by: java.lang.NullPointerException" at `org.apache.solr.schema.FieldType.createField`. The 2020-01-05 comment asks the reporter to try a recent 8.x release; there is no reply in the ticket.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/update/DocumentBuilder.java:274-285` (catch SolrException): builds "ERROR: ... Error adding field ... msg=" + `ex.getMessage()`.
- `DocumentBuilder.java:286-297` (catch Exception): the same concatenation with the BAD_REQUEST code. A RuntimeException with a null message reaches this block and produces "msg=null". The "Error adding field" text appears only at :279 and :291 (grep at the pin).
- `solr/core/src/java/org/apache/solr/schema/FieldType.java:295-316` (`createField(SchemaField, Object)`): `toInternal(value.toString())` at :305 is inside a try that wraps a RuntimeException as SERVER_ERROR with the message "Error while creating field ... from value ..." (:304-312), so an NPE from that call gets a non-null message. The call at :316, `createField(field.getName(), val, field)`, is outside the try, so an NPE raised by the overload would escape unwrapped and reach `DocumentBuilder.java:286-297` with a null message.

Which NPE the reporter hit is not identified: the quoted comment names `createField` without a line, and the current `createField` overloads at the pin have not been matched to the 7.7.2 line.

## History

- No commit at the pin names SOLR-13572 (evidence tool: none).
- The `msg=` concatenation was not traced to its introducing commit in this pass.

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- The `msg=null` output is produced at the pin by `DocumentBuilder.java:295-296` for any exception without a message, so the symptom is still reachable by code path.
- Whether the NPE itself still occurs for a `sort_*` field with the value `node_access_all:0` on the pin is not shown. The ticket's stack is from 7.7.2 and the reporter did not answer the 2020 request for an 8.x check.
- The skip note from the B3 reading says the NPE is null-guarded. The guard at the pin is the wrapper in `FieldType.java:304-312`, which covers only `toInternal`; it does not cover the call at :316. This is a code-path argument, not a run.
- Missing piece: a run on the pin that indexes a `sort_*` dynamic field with the value `node_access_all:0` (the sort field type from the reporter's schema is not in the ticket), and the stack of any NPE it produces.

## Verdict justification

keep-open: the code path that turns a null-message exception into `msg=null` is present at the pin, and an unwrapped NPE at `FieldType.java:316` would reach it, but the NPE's source and whether it still occurs were not established. A close verdict would need a run showing the NPE is gone (close-cannot-reproduce) or a fixing commit (close-fixed). A pr-needed verdict would need the NPE line.

Missing for a closing or PR verdict: the reporter's schema for `sort_X3b_en_node_grants` and a run on the pin that shows whether the NPE occurs and where.

## Limits

This record does not establish a reproduction. It does not check the field type for `sort_*` in the reporter's schema, and it does not post anything to JIRA or GitHub.
