# Assignment: SOLR-17914 form built to the new Figma design (round 1)

Status: CLAIMED (vm4, claim `claims/solr-17914-new-design-form-round-1.md`, started 2026-10-27T22:41:57Z)
Tags: implementation, ui, jvm-tests
Posted by: vm4, 2026-10-27, at the direction of Nick Shanin ("Better do the new design than the old one... use the decision ladder, the PR will need edits anyway").

## Source

- New design: Figma node `2635-40394` (WIP, Christos comment 18133018). Reading: `reports/figma-christos-17914-nodes-round-1.md`, item 2. Captures: `material/figma/2635-40394.png`.
- Former design (superseded for this work): node `2119-22708`.
- Ticket: SOLR-17914 acceptance criteria. Ticket text has not changed (Jira changelog empty).

## Work

1. New branch `solr-17914-create-collection-dialog-new-design` from the stacked branch tip `5ea0055190`. Remove `pr-draft/SOLR-17913.md` and `pr-draft/SOLR-17914.md` in the first commit (the PR-prep blocker in `reports/christos-reply-17914-pr-prep-round-1.md`).
2. Rebuild `CreateCollectionForm` to the new design, using the decisions table below. Keep the caller-supplied `nameError` and `generalError`.
3. Update `CreateCollectionFormTest` and `PreviewCreateCollectionForm` to the new fields and callback.
4. Gate: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon` under `/workspace/tools/.gradle-vm4.lock`, from a clean worktree at the new head. Logs under `/workspace/gates/logs/vm4/solr-17914-new-design/`.
5. Mutation check for each new validation assertion (ladder rung 2): break the rule in a scratch copy, show the matching test fails, record which.
6. Deliverable: `reports/solr-17914-new-design-round-1.md` with the decisions table filled in (rung used and the source), the gate result, the mutation results, the head SHA, and the list of drawn-but-unbuilt items. Plus a receipt `receipts/SOLR-17914-new-design-vm4.md` if the counts are cited publicly.

## Decisions (decision ladder, material/decision-ladder.md)

| # | Item | Decision | Rung | Source |
| --- | --- | --- | --- | --- |
| D1 | Name rule | Client check matches the server pattern `^(?!-)[._A-Za-z0-9-]+$`. Helper text kept as drawn, "Letters, digits, _ and -". Periods are allowed by the server and absent from the helper; record as a PR question for design. | 1 | `solr/solrj/src/java/org/apache/solr/client/solrj/util/SolrIdentifierValidator.java` |
| D2 | Label "Name" (was "Collection Name") | As drawn | 3 | new design |
| D3 | Shards: stepper, default 2, minimum 1 | As drawn. Non-integer text disables Create. | 3 | new design |
| D4 | Replica counts NRT 2, TLOG 0, PULL 1; replication factor field removed | As drawn | 3 | new design |
| D5 | Replica validity: each count a whole number of 0 or more, and NRT + TLOG at least 1 | The old UI already rejects PULL-only ("A collection can't be made up of just PULL replicas") | 1 | `solr/webapp/web/js/angular/controllers/collections.js` (addCollection, PULL check) |
| D6 | Placement dropdown, one option "Any node, spread across zones (recommended)" | Shown as drawn. Not sent to the caller. Other options are not drawn and are not invented. | 4 | new design; PR question on the option list |
| D7 | Placement preview card, "Preview configset" link, configset "(used by N)", "Which replica type do I need?" link | Not built. The card needs node and zone data; the others have no drawn target or data source. Listed as limits. | 4 | new design |
| D8 | Advanced row, collapsed by default | Row text as drawn: "Advanced: router, route field, properties, async". Expands to a Router dropdown (Composite ID drawn in the former expanded variant; options Composite ID and Implicit from the old UI) and a Route field text field. Properties and async are not drawn beyond the row and are not built. | 4 | new design row; former variant `2119-22708`; old UI router options |
| D9 | Router value sent | `compositeId` or `implicit`, the old UI's API values; route field sent only when not blank | 1 | old UI `collections.js` and `collections.html` (router option values) |
| D10 | Button label "Create Collection" (was "Create") | As drawn | 3 | new design footer |
| D11 | Name-exists check | Not built. The form shows the caller's `nameError`. No collections data layer exists in the new UI. Listed as a limit. | 4 | ticket AC; `ui/` has no collections fetch |
| D12 | Error texts | The caller supplies them. No text is drawn for name-exists, failed create, or general error. | 3 | ticket AC; `reports/figma-christos-17914-nodes-round-1.md` item 4 |
| D13 | Callback | `onCreate(CreateCollectionInput)` with name, configset, shards, nrtReplicas, tlogReplicas, pullReplicas, router, routeField | 4 | the create request is out of scope; the caller needs every typed value |

## Deliverable and limits

- Commit as Nick Shanin with no trailers. No em dashes or en dashes in authored text.
- Do not open a PR, post to Jira or GitHub, or push to `solr-17914-create-collection-dialog-stacked` (keep it as the former-design record).
- Push the new branch to the fork only as a fast-forward new branch.
