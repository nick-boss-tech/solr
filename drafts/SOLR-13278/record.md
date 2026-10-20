# Skip record: SOLR-13278

- Ticket: SOLR-13278, Using multiple dictionaries with Solr Suggester.
- Verdict: keep-open.
- Draft branch: solr-13278-draft-open. The verdict changed from close-answered, so this branch replaces solr-13278-draft-close for the new verdict; the close draft is left in place and is not supported.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 1 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-02-27, updated 2019-06-08, no fix versions, no links. The report: a query with two suggest dictionaries (two `suggest.dictionary` and two `suggest.cfq` values) returns the same output as the single-dictionary query. The response file is attached, not in the ticket text. Two comments (2019-02-28): ctargett asks for a description of the bug; rsood gives the query and the output.

## Code reading

Read at the pin:

- `solr/solr-ref-guide/modules/query-guide/pages/suggester.adoc:679`: "When using these Suggesters in a query, you would define multiple `suggest.dictionary` parameters in the request, referring to the names given for each Suggester in the search component definition. The response will include the terms in sections for each Suggester." This answers how to pass several dictionaries.
- `solr/core/src/java/org/apache/solr/spelling/suggest/SuggesterParams.java:26`: `SUGGEST_DICT = "suggest.dictionary"`.
- `solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java:94`, `:124-132`: the suggesters are keyed by dictionary name.
- `SuggestComponent.java:296-330` (`finishStage`): shard results are merged by the suggest section label, one `SuggesterResult` per shard response.

Not traced: the prepare path that picks a suggester for each `suggest.dictionary` value, and whether the second `suggest.cfq` changes which suggester answers. That path decides whether the output repeats.

## History

No commit at the pin names SOLR-13278 (the evidence tool lists none). The history of the prepare path was not traced in this pass.

## Reproduction attempts

Run on 2026-10-20 at the pin 498a09aea97aac86c2cc7ac114113b946d249942 (fork commit, base of the run worktree):

- Command: `./gradlew :solr:core:test --offline --tests "*SuggestComponentTest*"` (JAVA_HOME /workspace/jdk/jdk-21.0.12.1+1, GRADLE_USER_HOME /workspace/gradle-home). Result: BUILD SUCCESSFUL, EXIT 0; SuggestComponentTest 13 tests, 0 failures, 0 errors.
- Added for the run only (not committed): `testTwoDictionaries`, which sends two `suggest.dictionary` values (suggest_fuzzy_doc_dict and suggest_fuzzy_with_high_freq_dict) and two `suggest.q` values (exampel) with count 5, and asserts both per-dictionary sections are returned. Result: pass (time 1.213 s).
- Not run: the reporter's exact request, which uses two `suggest.cfq` values. The cfq variant is the remaining difference between the run and the ticket.

The run shows the two-dictionary response returns a section per dictionary for this configuration. It does not reproduce the reported output, because the reported request differs (suggest.cfq, not suggest.q). The missing piece is a run of the ticket's request at this sha: a core with two suggesters, one per dictionary name, then the same query with two `suggest.dictionary` and two `suggest.cfq` values, comparing the two per-dictionary sections. The response file in the ticket would be the expected output to compare against.

## Gap analysis

- The docs answer the usage question (how to pass several dictionaries), so close-answered would hold only if the question were usage.
- The reported symptom is different: the output for two dictionaries repeats the single-dictionary output. The docs do not say why, so they do not answer it.
- Nothing in the code at the pin was removed or fixed for this symptom, so close-obsolete and close-fixed do not fit.

## Verdict justification

keep-open: the usage is documented and the component returns a section per dictionary, but the reported output is not explained by the docs or the code read so far, and it has not been run. pr-needed needs a reproduction first.

Missing for this verdict: the run described under Reproduction attempts, and the prepare-path trace.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
