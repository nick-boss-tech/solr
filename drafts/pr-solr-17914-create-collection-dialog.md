🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17914

**Stacked on SOLR-17913.** Open and merge SOLR-17913 first. Until then, this PR's diff also contains the SOLR-17913 files. Christos Malliaridis is the sponsor and the promised reviewer. He asked for this as a temporary integration of the former designs while the new Figma designs are in progress (SOLR-17914 comment 18133018).

## What happens today

**The new UI has no way to create a collection.** The Collections section is not in the new UI yet. Its screen is SOLR-17912, and the sidebar entry is disabled.

SOLR-17914 asks for a create dialog with a configset picker, a collection name, shards and replication factor, and an inline error when the name already exists.

## What this change does

**It adds the form that the create dialog is built from.** `CreateCollectionForm` shows a name field, a configset picker, shards and replication-factor fields, an inline error under the name, a general error line, and Create and Cancel buttons ([CreateCollectionForm.kt](https://github.com/nick-boss-tech/solr/blob/5ea0055190c7e0857c42a3d4ef354e200c250b32/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/CreateCollectionForm.kt#L62-L150)).

- The form keeps the typed values itself. The caller passes in the configsets, the two error messages, and the `onCreate` callback ([lines 53-67](https://github.com/nick-boss-tech/solr/blob/5ea0055190c7e0857c42a3d4ef354e200c250b32/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/CreateCollectionForm.kt#L53-L67)).
- The first configset is selected by default ([line 72](https://github.com/nick-boss-tech/solr/blob/5ea0055190c7e0857c42a3d4ef354e200c250b32/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/CreateCollectionForm.kt#L72)). The picker is the existing `ConfigsetsDropdown`.
- Create is enabled only when the name is not blank, a configset is selected, and the shard and replica counts are positive whole numbers ([line 144](https://github.com/nick-boss-tech/solr/blob/5ea0055190c7e0857c42a3d4ef354e200c250b32/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/CreateCollectionForm.kt#L144)).
- It adds three strings and a preview.

Nothing calls the form yet. The create request (`POST /api/collections`) and the Collections screen that hosts the dialog are not in this change.

## Proof

**The four form tests pass, and the full JVM suite passes.** Clean run at head `5ea0055190` on 2026-10-27, on the software renderer because the test host has no GPU driver:

- `CreateCollectionFormTest`: 4 of 4 pass ([test file](https://github.com/nick-boss-tech/solr/blob/5ea0055190c7e0857c42a3d4ef354e200c250b32/ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/collections/CreateCollectionFormTest.kt)).
- `CollectionDetailsCardTest` (from SOLR-17913): 2 of 2 pass.
- Full `:shared:jvmTest`: 53 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest` and `StartContentTest`, both outside this change).
- `:shared:spotlessCheck` passes.

This proof is inconclusive by construction. The form is new, so the base has nothing to fail on.

## The choice

**The caller checks whether the name exists, not the form.** The form only shows the `nameError` it receives. The options are (a) the caller checks the name against the existing collections and passes the error in, or (b) the form receives the existing names and checks them itself.

Option (a) fits the Collections screen, which already loads the collection list, and keeps the form a plain component. Option (b) fits a form that has to work without that screen. I recommend (a). Keep the name check in the caller, or give the form the existing names?

## Limits

- The create request and the Collections screen are not in this change, so the form is not reachable in the app yet.
- Advanced options (hidden by default, with labels) and tooltips are not included. SOLR-17914 lists the advanced options, so this PR does not meet that criterion. A follow-up ticket and PR can cover them on request.
- The existing-name check is the caller's job, not the form's.
- No changelog entry.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
