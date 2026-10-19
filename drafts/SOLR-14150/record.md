# Skip record: SOLR-14150

- Ticket: SOLR-14150, admin/luke doesn't show indexed fields correctly. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-24, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-14150-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited name grepped at this head (detached worktree).
- Source: remediation redo of batch 05, item 1, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-3, 2026-10-05, deferred) cited the Luke flag code from audit evidence; the lines are confirmed here.

## Ticket reading

The admin/luke output shows docValues as absent for a field whose schema has docValues, and shows numeric fields (Trie) as tokenized. The reporter attached a screenshot and no sample index.

## Grep and code reading at the pinned head

- solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java:654, `getFieldFlags(IndexableField)`: the index flags come from the field type of the field object it is given. The tokenized flag is at :664 (`f.fieldType().tokenized()`) and the docValues flag at :667 (`f.fieldType().docValuesType() != DocValuesType.NONE`).
- LukeRequestHandler.java:890-897: the index flags are built from `doc.getField(fieldName)`, where `doc` is `getFirstLiveDoc(terms, reader)` (defined at :920). That document is reloaded from stored fields, so its field type does not carry the index's docValues setting. That is the source of the wrong docValues display described in the ticket.
- LukeRequestHandler.java:716, `getFieldFlags(SchemaField)`: the schema-based flags use `f.hasDocValues()` (:727). The schema view is therefore correct; only the index view is not.
- `KEY_INDEX_FLAGS = "index"` is at LukeRequestHandler.java:147.

## History

- No commit history was used for this record. The commit that introduced the reload-based index flags was not identified in this pass.

## Reproduction attempts

Not run. A run needs an index with a docValues field and a Trie numeric field, then an admin/luke request on that core, with the index-flag output compared to the schema flags. This pass did not build an index.

## Gap analysis

The mismatch is in code: the index view reads its flags from a reloaded stored document, while the schema view reads the schema. A fix that reads the flags from FieldInfos would change what the index flags mean (the FieldInfo has no tokenized bit, as the audit noted). That is a choice about the flag semantics, not a patch to make on this pass.

## Verdict justification

keep-open: the attempts made are the code reads and greps above at the pinned head, the Jira read, and the schema-versus-index comparison in the code. The missing piece is (1) a run on a docValues field and a Trie numeric field comparing the index view and the schema view, with the output and log path; and (2) the decision on the semantics of the index flags (FieldInfos-based, with the tokenized bit handled separately). No close verdict is supported (the mismatch is in code). No PR verdict is supported until the semantics decision is made.

Missing for a closing or PR verdict: the run output and the semantics decision.

## Limits

This record does not reproduce the display. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
