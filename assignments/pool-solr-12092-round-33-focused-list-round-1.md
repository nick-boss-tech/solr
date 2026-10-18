# Pool assignment: SOLR-12092 round 33 focused class list, round 1

Status: OPEN

- Capability tags: review. Read-only search; no build. Intended host: vm1 (Linux). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm2 gate coordinator, 2026-10-18, at the direction of the user.

Context: the SOLR-12092 receipt (`receipts/SOLR-12092.md`, section "Gate record at ca9573dabd38") says the round 33 gate ran on 2026-10-07 across nine focused classes, 82 tests, 0 failures. Its logs are `g12092r33-gate.log` and `g12092r33-check2.log`, and the module check was rerun once after a VM replacement. The round 33 dispatches on the fork (`ci/12092-*-r33`) confirm six of the nine classes: TestManagedStopFilterFactory, TestBulkSchemaAPI, TestRestManager, TestSolrConfigHandler, TestManagedSynonymGraphFilterFactory, TestExtendedDismaxParser. The remaining three are unknown. The P5 gate job (`gates/SOLR-12092-edismax-p5.md`) stays stopped until they are found.

## Items

1. **Find the round 33 logs.** Search this host for `g12092r33-gate.log` and `g12092r33-check2.log`: `/workspace`, the home directory, `/tmp`, and any old gate directory. Also search for a `solr-12092` worktree whose `solr/core/build/test-results/test/TEST-*.xml` files still exist; those files name the classes that ran. Start from: `/workspace/gates/` and `~/`. Output: `reports/SOLR-12092-round-33-focused-list.md`, section "Item 1", with every path searched and every path found.

2. **If a log or XML set is found, read the class list.** Read only the test class names, counts and failure totals from the log or the JUnit XML. Do not rerun anything. Output: the same report, section "Item 2", with the nine class names, each class's count, the total, the seed, and the file path and its sha256.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Build or test run | decided: none | no Gradle, no test run, no checkout changes; this is a read-only search | vm2 coordinator |
| Source changes | decided: none | no edits in any worktree, including the found ones | vm2 coordinator |
| Logs found | default: report the class list | if the log or XML names all nine classes, record them in the report; if it names fewer, record what it names and stop | vm2 coordinator |
| Logs not found | default: report the search and stop | if nothing is found, record every path searched and stop; do not guess the list | vm2 coordinator |

## Deliverable

`reports/SOLR-12092-round-33-focused-list.md`: one section per item with its status, the paths searched or found, and the class list if found. Then mark this assignment's status line `Status: DONE` with the date.

## Limits

- No PR, comment, JIRA or email. No force push. No secrets printed.
- Do not copy the log into coordination; record its path and sha256 only.
- Commit as Nick Shanin with no trailers. No em dashes.
