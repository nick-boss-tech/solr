# Skip record: SOLR-15571

- Ticket: SOLR-15571, Content of some fields not return in streaming expressions. JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2021-07-29, updated 2021-09-28, 0 comments, no fix versions, no links.
- Verdict: keep-open.
- Draft branch: solr-15571-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5, 2026-10-19 UTC, in the remediation redo for batch 03 (claims/skip-SOLR-15571-vm5.md).

## Ticket reading

The reporter runs a streaming expression over a query and gets some fields back but not others. The example fields are dynamic string fields (`Collaborator_ss`, a multi-valued `_ss` suffix). The ticket names no schema, no `fl` parameter, no streaming expression, and no version beyond a 2021 report.

## Code reading

- `solr/core/src/java/org/apache/solr/handler/export/ExportWriter.java:501-539`: the export writer (the `/export` handler that streaming expressions use) builds its field list from the searcher's field names. A field with usable DocValues goes to the DocValues writers (513-515). A stored-only field is written only when `includeStoredFields` is set; otherwise a glob-matched one is skipped (516-519, 527). An explicitly requested stored-only field makes the request fail with "must have DocValues" (520-526). A field with neither DocValues nor stored values is skipped silently when glob-matched, and fails when named explicitly (528-535, 538).
- What this shows at the pinned head: a field that matches a glob in `fl` but has no DocValues, or is stored-only without `includeStoredFields`, is dropped from the output without an error. That is one code path by which "some fields are not returned" happens. Whether it is the path the reporter hits is not shown: the ticket does not give the field's schema definition, the `fl` value, or the `includeStoredFields` setting.
- `git grep` for the ticket key and for `Collaborator` at the pinned head: no hits.

## History

- No commit on the pinned head names SOLR-15571 (evidence tool: no hits).
- The drop-without-error branch (ExportWriter.java:527 and :538) was not traced to its introducing commit in this pass.

## Reproduction attempts

Not attempted. A check needs a 9.x or current build with a schema that has a `*_ss` field stored-only (and a second one with DocValues), a `search()` streaming expression with `fl` naming both, run once with `includeStoredFields` false and once true. This pass is review-only and runs no build, so the result of that check is not known.

## Gap analysis

The reporter's symptom matches the silent-drop branch of the export writer, so the explanation is plausible. It is not confirmed, because the field's schema and the expression are missing from the ticket, and no run has been made on the pinned head. A defect is not shown: the writer's behavior matches what the code says it does, and whether the requirement for DocValues is documented was not checked in this pass.

## Verdict justification

keep-open: the code path is read with file and line (ExportWriter.java:501-539), the Jira status and date are read in this pass, and the missing piece is named: the field's schema definition, the streaming expression's `fl` and `includeStoredFields`, and one run on the pinned head with those inputs. close-fixed and close-obsolete are not met: no fixing commit and no removed code path is shown. close-cannot-reproduce is not met: no reproduction has been attempted. pr-needed is not met: the defect is not shown to be in Solr's code rather than the schema.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. The ticket's 2021 version of Solr is not the pinned head, and this record does not compare against it.
