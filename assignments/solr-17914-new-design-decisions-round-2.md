# Assignment: SOLR-17914 new-design form, decisions settled by the ladder (round 2)

Status: CLAIMED (vm4, claim `claims/solr-17914-new-design-decisions-round-2.md`, started 2026-10-27T23:40:00Z)
Tags: implementation, ui, jvm-tests, records
Posted by: vm4, 2026-10-27, at the direction of Nick Shanin: settle the open choices with the decision ladder (`material/decision-ladder.md`) and justify them in the PR, rather than leaving them as limits or questions.

## Work

Branch `solr-17914-create-collection-dialog-new-design` (head `125448970d`). New commits only; no force push.

1. Apply the decisions in the table below.
2. Update `CreateCollectionFormTest` for each changed rule and new field. Each new validation assertion is shown to fail on a mutation (rung 2).
3. Gate: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks` at the new head, under `/workspace/tools/.gradle-vm4.lock`. Logs `/workspace/gates/logs/vm4/solr-17914-decisions-round-2/`.
4. Deliverable `reports/solr-17914-decisions-round-2.md`: each decision with rung, evidence and the reason; the PR split; the gate result. Receipt `receipts/SOLR-17914-decisions-round-2-vm4.md`.

No PR, no Jira or GitHub post. No edits to the former-design branch.

## Decisions

| # | Question | Decision | Rung | Evidence and reason |
| --- | --- | --- | --- | --- |
| D1 | Which characters a name may use | Letters, digits, `_` and `-`; no leading `-`. Periods are rejected. | 3 | The drawn helper is the spec the user reads. The server allows periods, so the form is stricter, which means it never sends a name the user was told is invalid. The leading `-` rule stays because the server rejects it (`SolrIdentifierValidator`). |
| D2 | Name already exists | No pre-check. The server's 400 "collection already exists: <name>" (`CreateCollectionCmd.java:134`) is mapped to `nameError` by the caller. | 1 | The new UI has no collections list, and the server is the only check that is race-free. The old UI's own check was a `@todo`. |
| D3 | Configset label | "Configset", passed in as an optional label to the shared `ConfigsetsDropdown`; other callers keep "Configsets". | 3 | The drawn label. The shared component has four callers, so the default must not change. |
| D4 | Async | Optional text field, sent as `async` when not blank. | 1 | `CreateCollectionRequestBody.async` is a String (`api/.../model/CreateCollectionRequestBody.java:56`). |
| D5 | Properties | One text field, `key=value` pairs separated by commas. Parsed to `Map<String, String>`; a pair without `=` or with an empty key disables Create and sets `isError` on the field. | 1 | `CreateCollectionRequestBody.properties` is `Map<String, String>` (line 54). The drawn row names "properties" but no control, so the text format is the smallest that maps to the API type. |
| D6 | Placement | Read-only field showing "Any node, spread across zones (recommended)". Not sent. No other option is added. | 3 | Only this option is drawn, and the ticket does not ask for placement. Not sending it leaves the server default. |
| D7 | Placement preview card; "Preview configset" link; configset usage count; "Which replica type do I need?" link | Not built. | 3 | None is in the ticket's acceptance criteria. The card needs node and zone data the new UI does not have. |
| D8 | Create action location | The "+ Create Collection" button in the Collections page header, beside Restore. It opens this dialog. The empty-state inline card is not built. | 3 | Drawn in `2635-33680`. The ticket asks for "an action ... in the collections section", which the header button meets. The empty-state card is a different, smaller form. The prototype does not open a dialog, so the wiring is decided here, not read from the prototype. |
| D9 | Where the header action can ship | In the Collections page from SOLR-17912, not in this form's PR. | 3 | The action has no page to sit on until SOLR-17912 lands. |
| D10 | Create request mapping | shards to `numShards`; configset to `config`; NRT, TLOG, PULL to `nrtReplicas`, `tlogReplicas`, `pullReplicas`; router to `router.name`; route field to `router.field` when not blank; properties and async as above. | 1 | `CreateCollectionRequestBody` fields (lines 25 to 58). |
| D11 | Failed create and general error text | The server's message is shown as the general error when the response has one. Otherwise the string "Could not create the collection." | 4 | A server message is more specific than any fixed text, and the fallback covers a response with no message. Recorded here; implemented with the create request. |
| D12 | Tooltips and input limits | Not in this ticket. | 3 | The ticket says tooltips and limits go to a separate ticket if not covered here. |
| D13 | PR split | PR 1: this form (stacked on SOLR-17913). PR 2: the create request, error mapping (D2, D10, D11), and the header action (D8) on the Collections page once SOLR-17912 exists. | 4 | The form is testable without the page. The request needs the page only for the action. Splitting lets the form ship first for Christos's review. |

## Limits

- Opening a PR and posting to Jira stay with Nick.
- The round-1 report and receipt are not rewritten; this round adds its own record.
