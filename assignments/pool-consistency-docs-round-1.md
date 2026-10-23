# Pool assignment: consistency and documentation accuracy round 1 (backup permissions, configset delete, streaming docs)

Status: OPEN (item 2 DONE by vm4, branch consistency-v2-list-backups-permission at e3f35fe42c; item 1 CLAIMED by vm3; item 3 DONE by vm4, branch hardening-configset-delete-containment at 3905ad78e8; item 4 DONE by phone-agent-2, docs build and direct link validation GREEN on unchanged `docs-streaming-allowlist-note` at 4b185504ef; see reports/consistency-docs-round-1.md)

- Capability tags: implementation, docs, records.
- Intended host: any pool Linux host. One item per claim is fine; the four items are independent. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: phone agent (vm1), 2026-10-19, at the direction of Nick.

Four small items found in ordinary code reading. Each is a consistency or documentation accuracy fix with a clear in-tree precedent to match. Prepare each on its own branch, gate it through the normal gate flow, and write its PR draft to `pr-formula.md`. No Jira ticket exists for these yet and none is to be filed; the owner decides the vehicle (ticket, PR, or hold) after maintainer guidance. Do not open any PR.

## Items

1. **V2 core backup create declares the wrong permission name.** Start from: `solr/core/src/java/org/apache/solr/handler/admin/api/CreateCoreBackup.java` (line 57 returns `COLL_EDIT_PERM`). Its restore sibling `RestoreCore.java` (line 62) returns `CORE_EDIT_PERM`, and the V1 route it mirrors (`/admin/cores?action=BACKUPCORE`) also requires the core edit permission. Change the create route to `CORE_EDIT_PERM`. If a test or an API spec snapshot asserts the declared permission, update it as part of the item and say so in the draft. Branch: `consistency-v2-backup-create-permission`. Output: gated branch plus its PR draft.
2. **V2 list collection backups requires an edit permission for a read.** Start from: `solr/core/src/java/org/apache/solr/handler/admin/api/ListCollectionBackups.java` (line 26 returns `COLL_EDIT_PERM` for a read-only listing). Its sibling `ListCollectionSnapshots.java` (line 48) uses `COLL_READ_PERM`, and the V1 `LISTBACKUP` action is read-only as well. Change the list route to `COLL_READ_PERM`, updating any asserting test or spec snapshot the same way. Branch: `consistency-v2-list-backups-permission`. Output: gated branch plus its PR draft.
3. **Configset file delete lacks the containment check its siblings apply.** Start from: `solr/core/src/java/org/apache/solr/core/FileSystemConfigSetService.java`, `deleteFilesFromConfig` (lines 100 to 114): each requested file name is resolved against the configset directory and deleted, with no check that the resolved path stays inside that directory. Other paths in the configset service verify a resolved path stays inside its base before acting on it. Add the same verification here: a name that resolves outside the configset directory is rejected with an error and nothing is deleted. Add a test that covers a name with `../` segments (rejected, target untouched) and a normal name (deleted as before). Branch: `hardening-configset-delete-containment`. Output: gated branch plus its PR draft.
4. **Streaming docs do not say the URL allow-list does not cover stream targets.** Start from: the reference guide pages that document the URL allow-list (search the ref guide for `allow.urls`) and the stream sources documentation. Source streams accept `zkHost` and `solrConnection` operands and connect to the named target directly; unlike the `shards` parameter and replication fetch, those targets are not checked against the allow-list, and the guide currently lets an operator assume the list covers every outbound connection the server makes. Add a plain statement of that boundary: where the allow-list is documented, note that streaming source targets are not covered by it and are bounded by the operator's network controls instead; where stream sources are documented, note the same in one line with a link back. Branch: `docs-streaming-allowlist-note`. Output: gated branch (docs build check) plus the draft note text.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| One branch per item | decided: four separate branches, named in the items | none | Nick |
| Tickets and PRs | decided: file no Jira ticket and open no PR for any item | the owner decides the vehicle after maintainer guidance; drafts wait on this branch | Nick |
| Existing tests asserting the old declarations | default: update them inside the same item | if an asserting test appears to encode the old value deliberately (a comment or a linked ticket says so), stop that item and record what you found in the claim instead of changing it | Nick |
| Wording | decided: consistency and documentation accuracy language only | drafts describe what the code does and what the change aligns; no other framing, and no reference to how the items were found | Nick |

No row may leave the choice to the claimant.

## Deliverable

One claim per item taken, each marked DONE with: the branch name and gated head, the gate job path, and the PR draft location (or the draft note text for item 4). A short summary in `reports/consistency-docs-round-1.md` listing all four items and their states, including items nobody took.

## Limits

- No PR, comment, JIRA or email. No force push.
- Change only what the item names: the declaration and its asserting tests (items 1 and 2), the one method and its test (item 3), the two documentation spots (item 4). No drive-by cleanup.
- Commit as Nick Shanin with no trailers. No em dashes.
