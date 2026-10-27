# SOLR-17914: form choices settled by the decision ladder (round 2)

Assignment: `assignments/solr-17914-new-design-decisions-round-2.md`. Claim: `claims/solr-17914-new-design-decisions-round-2.md`. Host: vm4. No PR opened, nothing posted to Jira or GitHub.

## Head

- Branch `solr-17914-create-collection-dialog-new-design` on the fork, at `39efaef80285208f67d98fa7551e0ec89c2a21b3`. New commit on top of round 1 (`125448970d`). No force push.

## Gate

- `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks`, software renderer, under `/workspace/tools/.gradle-vm4.lock`. Build successful in 1m 28s.
- JUnit: 58 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest`, `StartContentTest`, outside this change). `CreateCollectionFormTest` 9 of 9. `CollectionDetailsCardTest` 2 of 2.
- Logs: `/workspace/gates/logs/vm4/solr-17914-decisions-round-2/`.

## Mutation check (ladder rung 2)

Two rules broken together in the worktree, then the file restored (`git status` clean).

| Mutant | Test that failed |
| --- | --- |
| Periods allowed in the name | `testRejectsNamesTheServerRejects` |
| Malformed property (no `=`) accepted | `testRejectsMalformedProperties` |

Logs: `/workspace/gates/logs/vm4/solr-17914-decisions-round-2/mutation/`.

## Decisions

Each row names the rung that settled it and the evidence. Rung 4 choices are the ones with more than one defensible option; the reason says why this one.

### Name and naming

- **D1. Allowed characters: letters, digits, `_`, `-`; no leading `-`; periods rejected.** Rung 3. The drawn helper "Letters, digits, _ and -" is the rule the user reads, so the form enforces it. The server also allows periods, so the form is stricter. That is safe: the form never sends a name it told the user was invalid. The leading `-` rule stays because the server rejects it (`solrj/.../SolrIdentifierValidator.java`, pattern `^(?!-)[._A-Za-z0-9-]+$`).
- **D2. Name already exists: no pre-check.** Rung 1. The server returns 400 "collection already exists: <name>" (`solr/core/.../cloud/api/collections/CreateCollectionCmd.java:134`). The caller maps that response to `nameError`. The new UI has no collections list, and the server's check is the only one that cannot race. The old UI's own check was a `@todo` (`solr/webapp/web/js/angular/controllers/collections.js`).

### Form fields

- **D3. Configset label is "Configset".** Rung 3. The drawn label. An optional `label` parameter was added to the shared `ConfigsetsDropdown`. Its other four callers keep "Configsets", so their behavior does not change.
- **D4. Async is an optional text field, sent as `async`.** Rung 1. `CreateCollectionRequestBody.async` is a String (`solr/api/.../model/CreateCollectionRequestBody.java:56`).
- **D5. Properties is one text field of `key=value` pairs separated by commas.** Rung 1 for the type, rung 4 for the input format. `CreateCollectionRequestBody.properties` is `Map<String, String>` (line 54). The drawn row names "properties" with no control, so the text format is the smallest one that maps to that type. A pair without `=` or with an empty key disables Create and marks the field.
- **D6. Placement is a read-only field showing "Any node, spread across zones (recommended)".** Rung 3. It is the only option drawn, and the ticket does not ask for placement. The value is not sent, so the server default applies. No other option is added.
- **D7. Not built: placement preview card, "Preview configset" link, configset usage count, replica-type help link.** Rung 3. None is in the ticket's acceptance criteria. The card also needs node and zone data the new UI does not have.

### Collections page and PR split

- **D8. The create action is the "+ Create Collection" button in the Collections page header, beside Restore.** Rung 3. It is drawn in `2635-33680`, and the ticket asks for "an action ... in the collections section". It opens this dialog. The empty-state inline card is not built: it is a smaller, different form, and the header button already meets the criterion. The prototype does not open a dialog, so the wiring is decided here, not read from the prototype.
- **D9. The header action ships with the Collections page (SOLR-17912), not with this form.** Rung 3. The action has no page to sit on until SOLR-17912 exists.
- **D10. Create request mapping.** Rung 1, from `CreateCollectionRequestBody`. Shards to `numShards`; configset to `config`; NRT, TLOG, PULL to `nrtReplicas`, `tlogReplicas`, `pullReplicas`; router to `router.name` (`compositeId` or `implicit`, the old UI's values); route field to `router.field` when not blank; async and properties as above. The request itself is in PR 2.
- **D11. Failed create and general error text.** Rung 4. The server's message is shown when the response has one, since it is more specific than any fixed text. The fallback is "Could not create the collection." This is implemented with the create request in PR 2.
- **D12. Tooltips and input limits are not in this ticket.** Rung 3. The ticket says both go to a separate ticket when not covered here.
- **D13. PR split.** Rung 4.
  - **PR 1:** this form, stacked on SOLR-17913. It is testable without the Collections page, and it is what Christos asked to review.
  - **PR 2:** the create request, the error mapping (D2, D10, D11), and the header action (D8, D9) on the Collections page once SOLR-17912 exists.
  - The form ships first because it has no dependency on the page. The request needs the page only for the action.

## Justification for the PR body

Each decision above maps to one of these lines in the PR description:

- Name rule: the drawn helper, with the server's leading-hyphen rule (D1).
- Name-exists: the server's response, not a pre-check (D2).
- Advanced fields: the server's field types, with a plain key=value format (D4, D5).
- Placement: the one drawn option, not sent (D6).
- Excluded items: not in the acceptance criteria (D7, D12).

## Not done

- The create request and the header action are PR 2 (D9, D13). The form in this branch does not call the server.
- No PR. No Jira or GitHub post. The former-design branch is unchanged.
