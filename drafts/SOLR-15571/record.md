# Skip record: SOLR-15571

- Ticket: SOLR-15571, Content of some fields not return in streaming expressions. JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2021-07-29, updated 2021-09-28, 0 comments, no fix versions, no issue links. Affects version 8.7 (the only version listed).
- Verdict: close-cannot-reproduce.
- Draft branch: solr-15571-draft-close. The earlier draft (solr-15571-draft-open, keep-open) is superseded by this record.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5, 2026-10-19 UTC, in the remediation redo for batch 03 (claims/skip-SOLR-15571-vm5.md).

## Ticket reading

The reporter queries `canonical_type:" " AND Collaborator_ss:" "` in the standard query window and gets `Collaborator_ss` values such as `" "`, next to `Collaborator_rt_source_ss` values that are URLs. The same query through a streaming expression, `search(core_data, q=..., qt:"/export", fl="id, label,Collaborator_rt_source_ss, Collaborator_ss", sort="id asc")`, returns `Collaborator_rt_source_ss` correctly but `Collaborator_ss` as ten empty strings (`""`) for one document. The reporter's own note says the two fields have the same `*_ss` type and the same content except for the angle brackets. The ticket names no schema definition, no Solr version beyond 8.7, and no index history.

## Code reading

- `solr/core/src/java/org/apache/solr/handler/export/MultiFieldWriter.java:84-105`: a multi-valued, non-point field is read from SortedSetDocValues. Each value is `lookupOrd(nextOrd())`, converted with `indexedToReadable`, then written with `toObject` (or as the string itself when there is no field). No trimming or replacement happens in this path.
- `solr/core/src/java/org/apache/solr/handler/export/StringFieldWriter.java:112-120` (`writeBytes`): the single-valued path converts the bytes with `indexedToReadable` and writes the string. It also writes the value verbatim.
- `solr/core/src/java/org/apache/solr/handler/export/ExportWriter.java:501-539`: field selection. A field with usable DocValues is written from DocValues (513-515). A stored-only field is written only with `includeStoredFields=true`; otherwise a glob-matched one is skipped silently (516-519, 527), and an explicitly requested one fails with "must have DocValues" (520-526). A field with neither DocValues nor stored values is skipped silently when glob-matched (538) and fails when named explicitly (528-535).
- What this shows at the pinned head: the value path writes the stored bytes as they are, so a whitespace-only or angle-bracketed value cannot come out as `""` through this path. The silent-skip branch can drop a whole field, but it does not produce empty strings. The export guide (`solr-ref-guide/.../exporting-result-sets.adoc:28-31, 49-53`) documents that fields without DocValues need `includeStoredFields=true`.
- `grep -rn 15571` on the pinned head: no hits in code or commit messages (evidence tool: no commits name the ticket).

## History

- `dafbe800c2` (2023-09-26, SOLR-16989, "Optimize and consolidate reuse of DocValues iterators"): rewrote the multi-valued export path to use `DocValuesIteratorCache`. Before this, MultiFieldWriter kept a per-reader iterator and reused it when `docID() < sortDoc.docId`. That is the code the 8.7 reporter ran, so the 8.7 behavior may differ from the pinned head. Ancestor of the pinned head: yes. Not identified as a fix for this ticket.
- `4a6e18b54c` (2024-09-04, SOLR-17416): ExportHandler stopped silently suppressing errors. Ancestor: yes. Not a fix for this ticket.
- `45adb11a9e` (2026-01-31, SOLR-18071): added `includeStoredFields`. Ancestor: yes.
- `64df5a65f0` (2021-01-19, SOLR-14608): faster sorting for `/export`. Ancestor: yes. Not identified as a fix for this ticket.

## Reproduction attempts

All three use the pinned head, a scratch worktree at that sha, the sweep runner under the host lock, and `:solr:core:test --rerun --tests org.apache.solr.handler.export.TestSolr15571Repro`. Test source and schema: `drafts/SOLR-15571/repro/`. Log paths are on the auditing host: `/workspace/gates/logs/vm5/15571/`.

1. Stored versus export, one field, three commits and an optimize (`testStoredVersusExport`; values `" "`, `"  "`, `"<http://x/y>"`, `""`, `"a"`). Stored and export output match. Result: pass, run once (log `repro15571c.log`).
2. Reporter's field list (`testReporterFieldList`): 12 documents in three commits, `Collaborator_ss` holds `" "`, `"  "` and an angle-bracketed URL, `Collaborator_rt_source_ss` holds two URLs, `label` holds text, and the query and `fl` are the reporter's. The export matches the stored values for all 10 documents the stored query returned (`rows=10`; the export returns all 12 matches, and the 10 shared documents have no value difference). Result: pass, run three times (logs `repro15571d.log`, `repro15571e.log`, `repro15571f.log`).
3. Variant (`testVariantReindexDeleteMerge`): documents re-indexed in a second commit with changed values, one document deleted, then optimize. Stored and export values match on every shared document. Result: pass, run twice (logs `repro15571e.log`, `repro15571f.log`).

The two passing negatives are the same command with the same result, so they count as conclusive for "not reproduced on synthetic data". Attempt 1 is superseded by attempts 2 and 3.

## Gap analysis

The pinned head does not produce empty strings for `Collaborator_ss`-shaped data (indexed, stored, DocValues, multi-valued string), either on fresh data or after re-indexing and merges. The value path writes the stored bytes as they are, which matches the tests. The explanation that remains open is the reporter's own index: Solr 8.7, its schema history, and its update and merge history. The rewrite in `dafbe800c2` changed the multi-valued export path after 8.7, so the old behavior may have been real and then fixed without a ticket reference. That is not confirmed by a run on 8.7.

## Verdict justification

close-cannot-reproduce: reproduction was attempted on the pinned head (three variants, the reporter's `fl` and query, two identical runs for the variants), the result did not show the problem, and the code path argument rules out `""` from the value path. The record states what the attempts do not cover (below). Minimum evidence met: pinned head, Jira status and date read in this pass, code paths with file and line, reproduction command, result and log paths.

## Limits

This record does not establish that the reporter's data is fine. It covers synthetic data built with the test framework, one document shape, and the pinned head only. It does not cover: the reporter's Solr 8.7 build; the reporter's schema history (if DocValues were added after the documents were indexed, the older segments would have no DocValues); the reporter's update and merge sequence beyond the variant; the reporter's `includeStoredFields` setting; or the ten-empty-value document. It does not post anything to JIRA or GitHub. Any JIRA reply should ask for the schema definition, the Solr version, and one streaming expression with its output.
