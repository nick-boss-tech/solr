# Skip record: SOLR-13304

- Ticket: SOLR-13304, PreanalyzedField#createField swallows Exception.
- Verdict: close-obsolete.
- Draft branch: solr-13304-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code and grep read at the pin in this pass.
- Source: the earlier B2 reading (vm2, 2026-10-19) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-03-07, updated 2019-06-08, no fix versions, no links. The report: `PreAnalyzedField.createField` returns a field for an ill-formatted pre-analyzed value after swallowing the exception, so a bad value looks indexed unless the logs are watched. The one comment (ab, 2019-03-07) agrees and asks for the field name in the message.

## Code reading

Removal at the pin:

- `3a33fa7009` (SOLR-17631, SOLR-17839, "Removing PreAnalyzed fields", 2025-08-07), an ancestor of the pin, deletes `solr/core/src/java/org/apache/solr/schema/PreAnalyzedField.java`, `JsonPreAnalyzedParser.java`, `SimplePreAnalyzedParser.java`, `solr/core/src/java/org/apache/solr/update/processor/PreAnalyzedUpdateProcessorFactory.java`, and the test configs `schema-preanalyzed.xml` and `cloud-managed-preanalyzed/conf/managed-schema.xml`.
- The 10.x upgrade notes record the removal: `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc:298` ("PreAnalyzedField and PreAnalyzedUpdateProcessor have been removed due to incompatibility with Lucene 10 (SOLR-17839)").

Not a rename: no replacement class with the same behavior exists. The removed classes are gone from the tree, and the only remaining mention is the upgrade note above.

## Grep at the pin

- `git grep -n -i 'preanalyzed' 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` excluding `solr/solr-ref-guide`: the only code match is `SortableTextField.java:38`, a Javadoc phrase "original (pre-analyzed) String values" that describes input text, not a use of the removed class.
- `git grep -l -i 'PreAnalyzedField' ... -- solr` excluding the ref guide: no files.

## History

- `3a33fa7009` (SOLR-17839 and SOLR-17631): removal commit, ancestor of the pin.
- `9684ba8ca2` (2025-10-30, SOLR-17619, "Delete CHANGES.txt from main"): mentions PreAnalyzedField in the removed changelog; not a code change.
- No commit at the pin names SOLR-13304 (evidence tool: none).

## Reproduction attempts

Not applicable: the class is removed. No build or test run in this pass.

## Gap analysis

- The code the ticket names is removed, and a code-wide search at the pin finds no remaining use, so the rubric's close-obsolete condition is met.
- The ticket's underlying concern (silently accepted bad pre-analyzed input) is no longer reachable through this class; no other code path with the same pattern was checked.

## Verdict justification

close-obsolete: `PreAnalyzedField` and its parser and update processor were removed by `3a33fa7009` (an ancestor of the pin), the grep at the pin is empty for code, and the removal is documented in the 10.x upgrade notes.

Missing for this verdict: none for the rubric minimum (grep with empty result at the pin, above).

## Limits

This record does not establish a reproduction. It does not check other pre-analyzed-like paths. It does not post anything to JIRA or GitHub.
