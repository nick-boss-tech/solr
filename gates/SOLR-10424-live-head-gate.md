# Gate job: SOLR-10424 (live-head gate at b0a03a5226)

Status: DONE

- Branch: solr-10424-submit. Head: `b0a03a5226e64abbc1545bb6bc42db8fb73eef06` (fork tip, confirmed by ls-remote on 2026-10-15). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `0072435a9d12128d8012bec0a4146b0b2c09593b`, the history-rewrite head whose tree is identical to the gated e629ab8bb293 tree (receipts/SOLR-10424.md). Merge-base with upstream/main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Intended host: vm1 (claimed by vm1; claims/pool-vm1-gate-10424-live-head.md). Any capable Linux host may claim if the intended host is busy (WORKFLOW.md).
- Capability tags: gate.
- Modules: `:solr:core` and `:solr:solrj`. Focused classes: `org.apache.solr.handler.TechproductsJsonDocsParamsTest` (core) and `org.apache.solr.client.solrj.SolrExampleJsonTest` (solrj; the concrete carrier of the changed testArbitraryJsonIndexing). Seed: `10424C0FFEE10424`.
- Diff scope against `cabedd1d968059215188f4e7563fb303241899ed`: 5 files: changelog/unreleased/SOLR-10424-techproducts-json-docs.yml (+7), solr/core/src/test/org/apache/solr/handler/TechproductsJsonDocsParamsTest.java (+43), solr/server/solr/configsets/sample_techproducts_configs/conf/params.json (-1), solr/server/solr/configsets/sample_techproducts_configs/conf/solrconfig.xml (+14), solr/solrj/src/test/org/apache/solr/client/solrj/SolrExampleTests.java (+3). No production Java file changes. Anything else in the tree is out of scope.
- Why: the branch moved one commit past the carried-over gated head. Commit b0a03a5226 ("keep generating ids for id-less posts to /update/json/docs") adds a json-docs-uuid update chain and /update/json/docs initParams to the techproducts sample config, plus two findability assertions in testArbitraryJsonIndexing. The commit arrived from the Windows side unverified (never compiled or run), so the exact head gets a full gate before anything cites it.

## What to run

Use the host's standard gate runner (blocking flock on the test-queue lock). Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-10424-techproducts-json-docs.yml`.
- Step 1: tidy (`:solr:core:tidy` and `:solr:solrj:tidy`). If tidy changes any file, the run is GATE INVALID and stops; no tidy commit is made.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava :solr:solrj:compileJava :solr:solrj:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg A (receipt premise): merge-base production and config, with the branch's TechproductsJsonDocsParamsTest.java copied in. Expected: exactly 1 failure, the designed assertFalse on mapUniqueKeyOnly, judged by content from fresh JUnit XML. Proof leg B (tonight's delta): head tree with only sample_techproducts_configs/conf/solrconfig.xml reverted to the merge-base version; run SolrExampleJsonTest. Expected: testArbitraryJsonIndexing fails because the id-less JSON docs get no generated id without the json-docs-uuid chain; judged by content from fresh JUnit XML. Any other failure shape stops the job and follows the failure-route decision.
- Step 4, focused head run: clean first, then TechproductsJsonDocsParamsTest in `:solr:core` and SolrExampleJsonTest in `:solr:solrj` at the seed. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test` and `:solr:solrj:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Compile failure at step 2 | decided: record it as a defect in the head commit (exact file, line, error) | none; the commit arrived unverified, so a compile failure is a commit defect, not an environment problem | main side, 2026-10-15 |
| Tidy changes the tree | decided: the run is GATE INVALID at this head; restore gradle/libs.versions.toml and record it | the gate lane makes no tidy commit on the submit branch | standing rule |
| Public text | decided: no PR body or draft edit from this job | falsified sentences are flagged in the outcome for the main side | main side |

## On green

1. Write `receipts/SOLR-10424.md` with a new top section: head, gate date and host, log path, step results, the proof-leg results (test names and failure text), and the focused counts from the fresh XML.
2. Mark this job DONE and the claim DONE in the same push as the receipt.
3. Append one line to the takeover log.

If any step cannot be verified, record it here and stop.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog parse
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [x] step 3 proof legs, expected failures by content
- [x] step 4 focused head runs, counted from fresh XML
- [x] step 5 module checks
- [x] receipt written
- [x] job DONE, claim DONE

## Outcome (vm1, 2026-10-16)

GATE GREEN at b0a03a5226e64abbc1545bb6bc42db8fb73eef06. Log: ~/workspace/tools/g10424-live-gate.log (flock acquired 2026-10-16T05:52Z). Step 0 changelog parse rc 0. Step 1 tidy rc 0, tree unchanged after tidy. Step 2 Error Prone compile rc 0 for :solr:core and :solr:solrj; the unverified-commit compile risk did not materialize. Step 3 leg A: base plus the branch test file, exactly 1 failure, the designed mapUniqueKeyOnly assertFalse in testUpdateJsonDocsParamSetDoesNotDropFields. Step 3 leg B: head tree with the techproducts solrconfig.xml reverted to base, exactly 1 failure in 42 tests, testArbitraryJsonIndexing with "Document is missing mandatory uniqueKey field: id". Step 4, from fresh JUnit XML at seed 10424C0FFEE10424: TechproductsJsonDocsParamsTest 1 of 1; SolrExampleJsonTest 42 tests, 0 failures, 1 skipped. Step 5 module checks rc 0. Receipt: receipts/SOLR-10424.md, new top section.

The branch tip moved to 5b087b1a389c892850401f79154824607cb338ac while this gate was queued and running (one changelog fragment title line). That head is not gated by this job; see the receipt's tip-moved note.

Flagged for the main side (not edited here): PR #5159's Proof says commits b0a03a5226e and 5b087b1a389 "have not been run", and its Limits says the name queries commit b0a03a5226e adds are "not yet run". The b0a03a5226e half is now falsified by this gate; 5b087b1a389 remains ungated. The pr-prepare draft pr-drafts/search-components/SOLR-10424.md still cites head 0072435a9d12 and does not describe the json-docs-uuid chain; the live PR body is ahead of it.

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
- The submit branch is not edited by this job.
